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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;

public class TimeBasedTriggerHandlerTest {
	
	private ITimeBasedTriggerHandler timeBasedTriggerHandler;
	@Mock private ITimeBasedTriggerFactory timeBasedTriggerFactory;
	@Mock private OEEStatusDao oeeStatusDao;
	private PIIType pii;
	private PiiUniqueId piiUid;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		timeBasedTriggerHandler = new TimeBasedTriggerHandler(oeeStatusDao, timeBasedTriggerFactory);
		
		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
		
		piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setPii(pii);
		piiUid.setId((long) 123456789);
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test handle and unHandle with an Obligation embedding a trigger at time
	 * TimeBaseTriggerHandler should call makeTimeBasedTriggerAtTime on TimeBasedTriggerFactory
	 * TimeBaseTriggerHandler should call start on TimeBasedTriggerAtTime
	 * TimeBaseTriggerHandler should call cancel on TimeBasedTriggerAtTime
	 * TimeBaseTriggerHandler should have no interaction with OEEStatusDao
	 */
	@Test
	public void testObligationWithTriggerAtTime() {
		OEEStatus oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(new TriggerAtTime());
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus.setTriggeredOnce(false);
		
		TimeBasedTriggerAtTime triggerAtTime = mock(TimeBasedTriggerAtTime.class);
		when(timeBasedTriggerFactory.makeTimeBasedTriggerAtTime((OEEStatus) anyObject())).thenReturn(triggerAtTime);
		
		timeBasedTriggerHandler.handle(oeeStatus);
		timeBasedTriggerHandler.unHandle(oeeStatus);
		
		verify(timeBasedTriggerFactory).makeTimeBasedTriggerAtTime(oeeStatus);
		verify(triggerAtTime).start();
		verify(triggerAtTime).cancel();
		verifyZeroInteractions(oeeStatusDao);
	}
	
	/*
	 * Test handle and unHandle with an Obligation embedding a trigger periodic
	 * TimeBaseTriggerHandler should call makeTimeBasedTriggerPeriodic on TimeBasedTriggerFactory
	 * TimeBaseTriggerHandler should call start on TimeBasedTriggerPeriodic
	 * TimeBaseTriggerHandler should call cancel on TimeBasedTriggerPeriodic
	 * TimeBaseTriggerHandler should have no interaction with OEEStatusDao
	 */
	@Test
	public void testHandleObligationWithTriggerPeriodic() {
		OEEStatus oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setAction(new ActionDeletePersonalData());
		oeeStatus.setPiiUniqueId(piiUid);
		oeeStatus.setTrigger(new TriggerPeriodic());
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		oeeStatus.setTriggeredOnce(false);
		
		TimeBasedTriggerPeriodic triggerPeriodic = mock(TimeBasedTriggerPeriodic.class);
		when(timeBasedTriggerFactory.makeTimeBasedTriggerPeriodic((OEEStatus) anyObject())).thenReturn(triggerPeriodic);
		
		timeBasedTriggerHandler.handle(oeeStatus);
		timeBasedTriggerHandler.unHandle(oeeStatus);
		
		verify(timeBasedTriggerFactory).makeTimeBasedTriggerPeriodic(oeeStatus);
		verify(triggerPeriodic).start();
		verify(triggerPeriodic).cancel();
		verifyZeroInteractions(oeeStatusDao);
	}
	
	/*
	 * Test start when n obligations are already in DB
	 * TimeBaseTriggerHandler should call findObject on OEEStatusDao
	 * TimeBaseTriggerHandler should call makeTimeBasedTriggerPeriodic on TimeBasedTriggerFactory
	 * TimeBaseTriggerHandler should call start on TimeBasedTriggerPeriodic
	 * TimeBaseTriggerHandler should call makeTimeBasedTriggerAtTime on TimeBasedTriggerFactory
	 * TimeBaseTriggerHandler should call cancel on TimeBasedTriggerAtTime
	 */
	@Test
	public void testStartWithObligations() {
		OEEStatus oeeStatus0 = new OEEStatus();
		oeeStatus0.setId(0);
		oeeStatus0.setAction(new ActionDeletePersonalData());
		oeeStatus0.setPiiUniqueId(piiUid);
		oeeStatus0.setTrigger(new TriggerPeriodic());
		oeeStatus0.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		oeeStatus0.setTriggeredOnce(false);
		
		OEEStatus oeeStatus1 = new OEEStatus();
		oeeStatus1.setId(0);
		oeeStatus1.setAction(new ActionDeletePersonalData());
		oeeStatus1.setPiiUniqueId(piiUid);
		oeeStatus1.setTrigger(new TriggerAtTime());
		oeeStatus1.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus1.setTriggeredOnce(false);
		
		List<OEEStatus> listOeeStatus = new ArrayList<OEEStatus>();
		listOeeStatus.add(oeeStatus0);
		listOeeStatus.add(oeeStatus1);
		
		TimeBasedTriggerPeriodic triggerPeriodic = mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime = mock(TimeBasedTriggerAtTime.class);
		
		when(timeBasedTriggerFactory.makeTimeBasedTriggerAtTime((OEEStatus) anyObject())).thenReturn(triggerAtTime);
		when(timeBasedTriggerFactory.makeTimeBasedTriggerPeriodic((OEEStatus) anyObject())).thenReturn(triggerPeriodic);
		when(oeeStatusDao.findObjects(OEEStatus.class)).thenReturn(listOeeStatus);
		
		timeBasedTriggerHandler.start();
		
		verify(oeeStatusDao).findObjects(OEEStatus.class);
		
		verify(timeBasedTriggerFactory).makeTimeBasedTriggerPeriodic(oeeStatus0);
		verify(triggerPeriodic).start();
		
		verify(timeBasedTriggerFactory).makeTimeBasedTriggerAtTime(oeeStatus1);
		verify(triggerAtTime).start();
	}
	
	/*
	 * Test start when no obligations are already in DB
	 * TimeBaseTriggerHandler should call findObject on OEEStatusDao
	 * TimeBaseTriggerHandler should have no interaction with TimeBasedTriggerFactory
	 */
	@Test
	public void testStartWithNoObligations() {
		List<OEEStatus> listOeeStatus = new ArrayList<OEEStatus>();
		
		when(oeeStatusDao.findObjects(OEEStatus.class)).thenReturn(listOeeStatus);
		
		timeBasedTriggerHandler.start();
		
		verify(oeeStatusDao).findObjects(OEEStatus.class);
		verifyZeroInteractions(timeBasedTriggerFactory);
	}
	
	/*
	 * Test stop when n obligations are already managed
	 * TimeBaseTriggerHandler should call cancel on every TimeBasedTrigger objects it manages
	 */
	@Test
	public void testStopWithObligations() {
		OEEStatus oeeStatus0 = new OEEStatus();
		oeeStatus0.setId(0);
		oeeStatus0.setAction(new ActionDeletePersonalData());
		oeeStatus0.setPiiUniqueId(piiUid);
		oeeStatus0.setTrigger(new TriggerPeriodic());
		oeeStatus0.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		oeeStatus0.setTriggeredOnce(false);
		
		OEEStatus oeeStatus1 = new OEEStatus();
		oeeStatus1.setId(0);
		oeeStatus1.setAction(new ActionDeletePersonalData());
		oeeStatus1.setPiiUniqueId(piiUid);
		oeeStatus1.setTrigger(new TriggerAtTime());
		oeeStatus1.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus1.setTriggeredOnce(false);
		
		TimeBasedTriggerPeriodic triggerPeriodic = mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime = mock(TimeBasedTriggerAtTime.class);
		
		when(timeBasedTriggerFactory.makeTimeBasedTriggerAtTime((OEEStatus) anyObject())).thenReturn(triggerAtTime);
		when(timeBasedTriggerFactory.makeTimeBasedTriggerPeriodic((OEEStatus) anyObject())).thenReturn(triggerPeriodic);
		
		timeBasedTriggerHandler.handle(oeeStatus0);
		timeBasedTriggerHandler.handle(oeeStatus1);
		timeBasedTriggerHandler.stop();
		
		verify(triggerPeriodic).cancel();
		verify(triggerAtTime).cancel();
	}
	
	/*
	 * Test stop when no obligations managed
	 * Should do nothing
	 */
	@Test
	public void testStopWithNoObligation() {
		TimeBasedTriggerPeriodic triggerPeriodic = mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime = mock(TimeBasedTriggerAtTime.class);
		
		when(timeBasedTriggerFactory.makeTimeBasedTriggerAtTime((OEEStatus) anyObject())).thenReturn(triggerAtTime);
		when(timeBasedTriggerFactory.makeTimeBasedTriggerPeriodic((OEEStatus) anyObject())).thenReturn(triggerPeriodic);
		
		timeBasedTriggerHandler.stop();
		
		verifyZeroInteractions(triggerPeriodic);
		verifyZeroInteractions(triggerAtTime);
		verifyZeroInteractions(timeBasedTriggerFactory);
		verifyZeroInteractions(oeeStatusDao);
	}

}
