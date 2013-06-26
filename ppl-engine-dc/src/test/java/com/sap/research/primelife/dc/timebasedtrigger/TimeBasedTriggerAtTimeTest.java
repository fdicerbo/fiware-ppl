/*******************************************************************************
 * Copyright (c) 2013, SAP AG
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *  
 *     - Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *     - Neither the name of the SAP AG nor the names of its contributors may
 *      be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.sap.research.primelife.dc.timebasedtrigger;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.research.primelife.dc.entity.OEEStatus;
import com.sap.research.primelife.dc.event.IEventHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;

public class TimeBasedTriggerAtTimeTest {
	
	private IEventHandler eventHandler;
	private PIIType pii;
	private PiiUniqueId piiUid;
	private OEEStatus oeeStatus;

	@Before
	public void setUp() throws Exception {
		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
		
		piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setPii(pii);
		piiUid.setId((long) 123456789);
		
		eventHandler = mock(IEventHandler.class);		
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test single execution of a TimeBasedTriggerAtTime
	 * TimeBasedTriggerAtTime should not interact with the event handler within n seconds after the start
	 * After n seconds TimeBasedTriggerAtTime should call fireAtTine on the EventHandler
	 */
	@Test
	public void testSingleTriggerAtTime() {
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(triggerAtTime);
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus.setTriggeredOnce(false);
		
		
		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		timeBasedTriggerAtTime0.start();
		
		try {
			for(int i=0; i<10; i++){
				verify(eventHandler, never()).fireAtTime(pii);
				Thread.sleep(1000);
			}
			Thread.sleep(1000);
			timeBasedTriggerAtTime0.cancel();
			verify(eventHandler).fireAtTime(pii);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("");
		}
		
	}
	
	/*
	 * Test single execution of a TimeBasedTriggerAtTime
	 * TimeBasedTriggerAtTime should not interact with the event handler within n seconds after the start
	 * After n seconds TimeBasedTriggerAtTime should call fireAtTine on the EventHandler
	 */
	@Test
	public void testSingleTriggerAtTimeAlreadyTriggered() {
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(triggerAtTime);
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus.setTriggeredOnce(true);
		
		
		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		timeBasedTriggerAtTime0.start();
		
		try {
			Thread.sleep(11000);
			timeBasedTriggerAtTime0.cancel();
			verifyZeroInteractions(eventHandler);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("");
		}
	}
	
	/*
	 * Test execution of n TimeBasedTriggerAtTime
	 * TimeBasedTriggerAtTime should not interact with the event handler within s seconds after the start
	 * After s seconds TimeBasedTriggerAtTime should call fireAtTine on the EventHandler n times
	 */
	@Test
	public void testNTriggersAtTime() {
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(triggerAtTime);
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus.setTriggeredOnce(false);
		
		
		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime1 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime2 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime3 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime4 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		timeBasedTriggerAtTime0.start();
		timeBasedTriggerAtTime1.start();
		timeBasedTriggerAtTime2.start();
		timeBasedTriggerAtTime3.start();
		timeBasedTriggerAtTime4.start();

		try {
			for(int i=0; i<10; i++){
				verify(eventHandler, never()).fireAtTime(pii);
				Thread.sleep(1000);
			}
			Thread.sleep(1000);
			timeBasedTriggerAtTime0.cancel();
			timeBasedTriggerAtTime1.cancel();
			timeBasedTriggerAtTime2.cancel();
			timeBasedTriggerAtTime3.cancel();
			timeBasedTriggerAtTime4.cancel();
			verify(eventHandler, times(5)).fireAtTime(pii);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("");
		}
	}
	
	/*
	 * Test single execution of a TimeBasedTriggerAtTime
	 * TimeBasedTriggerAtTime should not interact with the event handler within n seconds after the start
	 * After n seconds TimeBasedTriggerAtTime should call fireAtTine on the EventHandler
	 */
	@Test
	public void testCancelTriggerAtTime() {
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(triggerAtTime);
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus.setTriggeredOnce(true);
		
		
		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 = new TimeBasedTriggerAtTime(oeeStatus, eventHandler);
		timeBasedTriggerAtTime0.start();
		try {
			Thread.sleep(5000);
			timeBasedTriggerAtTime0.cancel();
			Thread.sleep(5000);
			verifyZeroInteractions(eventHandler);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("");
		}
	}

}
