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
package com.sap.research.primelife.dc.obligation;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.OEEStatus;
import com.sap.research.primelife.dc.timebasedtrigger.ITimeBasedTriggerHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.ObjectFactory;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataSent;
import eu.primelife.ppl.policy.obligation.impl.TriggersSet;
import eu.primelife.ppl.policy.obligation.impl.TriggersSetTriggerItem;

public class ObligationHandlerTest {

	private IObligationHandler obligationHandler;
	@Mock private OEEStatusDao oeeStatusDao;
	@Mock  private PiiUniqueIdDao piiUniqueIdDao;
	@Mock  private ITimeBasedTriggerHandler timeBasedTriggerHandler;
	
	private PIIType pii;
	private PiiUniqueId piiUid;
	
	private ObjectFactory ofObligation = new ObjectFactory();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		obligationHandler = new ObligationHandler(oeeStatusDao, piiUniqueIdDao, timeBasedTriggerHandler);
		
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

	/**
	 * Test add n obligations with no time based trigger on an existing Pii
	 * The Obligation Handler should call n times persistObject on OEEStatusDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsNoTimeBasedTriggers() {
		
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		
		class IsSameOeeStatus extends ArgumentMatcher<OEEStatus> {
			public boolean matches(Object obj) {
				OEEStatus oeeStatus = (OEEStatus) obj;
				for(Obligation ob : obligationSet.getObligation()){
					if( oeeStatus.getAction().equals(ob.getActionValue()) &&
						oeeStatus.getPiiUniqueId().equals(piiUid) &&
						oeeStatus.getTriggerName().equals(ob.getTriggersSet().getTrigger().get(0).getName().toString()) &&
						oeeStatus.getTrigger().equals(ob.getTriggersSet().getTrigger().get(0).getValue())){
							return true;
					}
				}
				return false;
			}
		}
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		obligationHandler.addObligations(obligationSet, pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verify(oeeStatusDao, times(3)).persistObject(argThat(new IsSameOeeStatus()));
		verifyZeroInteractions(timeBasedTriggerHandler);
	}
	
	/**
	 * Test add n obligations with m time based trigger on an existing Pii
	 * The Obligation Handler should call n times persistObject on OEEStatusDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsTimeBasedTriggers() {
		
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		
		final Obligation obligationWithTriggerAtTime = createSimpleObligationWithTriggerAtTime();
		final Obligation obligationWithTriggerPeriodic = createSimpleObligationWithTriggerPeriodic();
		
		obligationSet.getObligation().add(obligationWithTriggerAtTime);
		obligationSet.getObligation().add(obligationWithTriggerPeriodic);
		
		class IsSameOeeStatus extends ArgumentMatcher<OEEStatus> {
			public boolean matches(Object obj) {
				OEEStatus oeeStatus = (OEEStatus) obj;
				for(Obligation ob : obligationSet.getObligation()){
					if( oeeStatus.getAction().equals(ob.getActionValue()) &&
						oeeStatus.getPiiUniqueId().equals(piiUid) &&
						oeeStatus.getTriggerName().equals(ob.getTriggersSet().getTrigger().get(0).getName().toString()) &&
						oeeStatus.getTrigger().equals(ob.getTriggersSet().getTrigger().get(0).getValue())){
							return true;
					}
				}
				return false;
			}
		}
		
		class IOeeStatusTimeBase extends ArgumentMatcher<OEEStatus> {
			public boolean matches(Object obj) {
				OEEStatus oeeStatus = (OEEStatus) obj;
				
				if( oeeStatus.getAction().equals(obligationWithTriggerAtTime.getActionValue()) &&
					oeeStatus.getPiiUniqueId().equals(piiUid) &&
					oeeStatus.getTriggerName().equals(obligationWithTriggerAtTime.getTriggersSet().getTrigger().get(0).getName().toString()) &&
					oeeStatus.getTrigger().equals(obligationWithTriggerAtTime.getTriggersSet().getTrigger().get(0).getValue())){
						return true;
				}
				
				if( oeeStatus.getAction().equals(obligationWithTriggerPeriodic.getActionValue()) &&
						oeeStatus.getPiiUniqueId().equals(piiUid) &&
						oeeStatus.getTriggerName().equals(obligationWithTriggerPeriodic.getTriggersSet().getTrigger().get(0).getName().toString()) &&
						oeeStatus.getTrigger().equals(obligationWithTriggerPeriodic.getTriggersSet().getTrigger().get(0).getValue())){
							return true;
					}
				return false;
			}
		}
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		obligationHandler.addObligations(obligationSet, pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verify(oeeStatusDao, times(5)).persistObject(argThat(new IsSameOeeStatus()));
		verify(timeBasedTriggerHandler, times(2)).handle(argThat(new IOeeStatusTimeBase()));
	}
	
	/**
	 * Test add 0 obligations with no time based trigger on an existing Pii
	 * The Obligation Handler should call 0 times persistObject on OEEStatusDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsEmptyObligationSet() {
		
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		obligationHandler.addObligations(obligationSet, pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verifyZeroInteractions(oeeStatusDao);
		verifyZeroInteractions(timeBasedTriggerHandler);
	}
	
	/**
	 * Test add n obligations for a Pii with no PiiUniqueId registerd
	 * The Obligation Handler should have no interaction with OEEStatusDao and Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsUnExistingPiiUniqueId() {
		
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(null);
		
		obligationHandler.addObligations(obligationSet, pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verifyZeroInteractions(oeeStatusDao);
		verifyZeroInteractions(timeBasedTriggerHandler);
	}
	
	/**
	 * Test deletion With No Obligation
	 * The Obligation Handler should not call deleteObject on OEEStatusDao 
	 * and have no interaction with the Time Based Trigger Handler
	 */
	@Test
	public void testDeleteObligationsWithNoObligation(){
		List<OEEStatus> oeeStatusList = new ArrayList<OEEStatus>();
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(oeeStatusDao.findByPiiUid(piiUid)).thenReturn(oeeStatusList);
		
		obligationHandler.deleteObligations(pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verify(oeeStatusDao).findByPiiUid(piiUid);
		verifyZeroInteractions(timeBasedTriggerHandler);
		verify(oeeStatusDao, never()).deleteObject((OEEStatus) anyObject());
	}
	
	/**
	 * Test deletion with n obligations (no time-based trigger)
	 * The Obligation Handler should call n times deleteObject on OEEStatusDaom with the correct OEEStatus instances
	 * should have no interaction with the Time Based Trigger Handler
	 */
	@Test
	public void testDeleteObligationsWithObligations(){
		List<OEEStatus> oeeStatusList = new ArrayList<OEEStatus>();
		OEEStatus oeeStatus0 = new OEEStatus();
		oeeStatus0.setId(0);
		oeeStatus0.setTriggeredOnce(false);
		oeeStatus0.setAction(new Action());
		oeeStatus0.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus0);
		
		OEEStatus oeeStatus1 = new OEEStatus();
		oeeStatus1.setId(1);
		oeeStatus1.setTriggeredOnce(false);
		oeeStatus1.setAction(new Action());
		oeeStatus1.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus1);
		
		OEEStatus oeeStatus2 = new OEEStatus();
		oeeStatus2.setId(2);
		oeeStatus2.setTriggeredOnce(false);
		oeeStatus2.setAction(new Action());
		oeeStatus2.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus2);
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(oeeStatusDao.findByPiiUid(piiUid)).thenReturn(oeeStatusList);
		
		obligationHandler.deleteObligations(pii);
		
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verify(oeeStatusDao).findByPiiUid(piiUid);
		verify(oeeStatusDao).deleteObject(oeeStatus0);
		verify(oeeStatusDao).deleteObject(oeeStatus1);
		verify(oeeStatusDao).deleteObject(oeeStatus2);
		verifyZeroInteractions(timeBasedTriggerHandler);
	}
	
	/**
	 * Test deletion with n obligations and m containing time-based triggers
	 * The Obligation Handler should call n times deleteObject on OEEStatusDao with the correct OEEStatus instances
	 * should call m times unHandle on the Time Based Trigger Handler with the correct OEEStatus instances
	 */
	@Test
	public void testDeleteObligationsWithTimeBaseObligations(){
		// ----- Test n obligations (with k time based triggers) -----
		List<OEEStatus> oeeStatusList = new ArrayList<OEEStatus>();

		OEEStatus oeeStatus0 = new OEEStatus();
		oeeStatus0.setId(0);
		oeeStatus0.setTriggeredOnce(false);
		oeeStatus0.setAction(new Action());
		oeeStatus0.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus0);
		
		OEEStatus oeeStatus1 = new OEEStatus();
		oeeStatus1.setId(1);
		oeeStatus1.setTriggeredOnce(false);
		oeeStatus1.setAction(new Action());
		oeeStatus1.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus1);
		
		OEEStatus oeeStatus2 = new OEEStatus();
		oeeStatus2.setId(2);
		oeeStatus2.setTriggeredOnce(false);
		oeeStatus2.setAction(new Action());
		oeeStatus2.setPiiUniqueId(piiUid);
		oeeStatusList.add(oeeStatus2);
		
		OEEStatus oeeStatus3 = new OEEStatus();
		oeeStatus3.setId(3);
		oeeStatus3.setTriggeredOnce(false);
		oeeStatus3.setAction(new Action());
		oeeStatus3.setPiiUniqueId(piiUid);
		oeeStatus3.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		oeeStatus3.setTrigger(new TriggerAtTime());
		oeeStatusList.add(oeeStatus3);
		
		OEEStatus oeeStatus4 = new OEEStatus();
		oeeStatus4.setId(3);
		oeeStatus4.setTriggeredOnce(false);
		oeeStatus4.setAction(new Action());
		oeeStatus4.setPiiUniqueId(piiUid);
		oeeStatus4.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		oeeStatus4.setTrigger(new TriggerPeriodic());
		oeeStatusList.add(oeeStatus4);
		
		when(piiUniqueIdDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(oeeStatusDao.findByPiiUid(piiUid)).thenReturn(oeeStatusList);
		
		obligationHandler.deleteObligations(pii);
		verify(piiUniqueIdDao).findByPiiId((long) 0);
		verify(oeeStatusDao).findByPiiUid(piiUid);
		verify(oeeStatusDao).deleteObject(oeeStatus0);
		verify(oeeStatusDao).deleteObject(oeeStatus1);
		verify(oeeStatusDao).deleteObject(oeeStatus2);
		verify(oeeStatusDao).deleteObject(oeeStatus3);
		verify(oeeStatusDao).deleteObject(oeeStatus4);
		verify(timeBasedTriggerHandler).unHandle(oeeStatus3);
		verify(timeBasedTriggerHandler).unHandle(oeeStatus4);
	}
	
	private Obligation createSimpleObligationWithSimpleTrigger(){
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(ofObligation.createActionDeletePersonalData(actionDelete));
		
		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList = new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);
		
		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		
		TriggerPersonalDataSent triggerPersonalDataSent = new TriggerPersonalDataSent();
		Duration duration = new Duration();
		duration.setDuration("P0Y0M0DT0H5M0S");
		triggerPersonalDataSent.setMaxDelay(duration);
		triggerPersonalDataSent.setId("http://www.primelife.eu/ppl/email");
		triggerItem.setItem(ofObligation.createTriggerPersonalDataSent(triggerPersonalDataSent));
		
		return obligation;
	}
	
	private Obligation createSimpleObligationWithTriggerAtTime(){
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(ofObligation.createActionDeletePersonalData(actionDelete));
		
		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList = new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);
		
		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT1H0M0S");
		triggerItem.setItem(ofObligation.createTriggerAtTime(triggerAtTime));
		
		return obligation;
	}
	
	private Obligation createSimpleObligationWithTriggerPeriodic(){
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(ofObligation.createActionDeletePersonalData(actionDelete));
		
		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList = new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);
		
		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerPeriodic triggerPeriodic = new TriggerPeriodic();
		DateAndTime startDate = new DateAndTime();
		startDate.setStartNowObject("<StartNow/>");
		triggerPeriodic.setStart(startDate);
		Duration duration = new Duration();
		duration.setDuration("P0Y0M0DT1H0M0S");
		triggerPeriodic.setMaxDelay(duration);
		DateAndTime endDate = new DateAndTime();
		endDate.setDateAndTimeItem(new Date());
		triggerPeriodic.setEnd(endDate);
		
		triggerItem.setItem(ofObligation.createTriggerPeriodic(triggerPeriodic));
		
		return obligation;
	}

}
