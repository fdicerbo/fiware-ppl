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
package com.sap.research.primelife.dc.event;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.action.IActionHandler;
import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataDeleted;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataSent;

public class EventHandlerTest {

	private IEventHandler eventHandler;
	
	@Mock private PiiUniqueIdDao puidDao;
	@Mock private OEEStatusDao oeeDao;
	@Mock private IActionHandler actionHandler;
	
	private PIIType pii;
	private PiiUniqueId piiUid;
	private OEEStatus oeeStatus;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		eventHandler = new EventHandler(puidDao, oeeDao, actionHandler);
		
		System.setProperty("systemConfigFilePath", getClass().getResource("/system.properties").getFile().toString());
		System.setProperty("EntityName", "DC");
		
		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
		
		piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setPii(pii);
		piiUid.setId((long) 123456789);
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		oeeStatus.setTriggeredOnce(false);
		oeeStatus.setAction(new Action());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFirePersonalDataUseForPurpose() {
		String triggerName = "{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose";
		oeeStatus.setTriggerName(triggerName);
		oeeStatus.setTrigger(new TriggerPersonalDataAccessedForPurpose());

		// Test when we dont't have a registered matching Obligation in the database
		when(puidDao.findObject(PiiUniqueId.class, (long) 0)).thenReturn(piiUid);
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(null);
		
		eventHandler.firePersonalDataUseForPurpose(pii, "purpose", "sharedWith");
		// Check if the EventHandler never calls the ActionHandler
		verifyZeroInteractions(actionHandler); 
		
		// Test when we have a registered matching Obligation in the database
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(oeeStatus);
		
		eventHandler.firePersonalDataUseForPurpose(pii, "purpose", "sharedWith");
		
		// Check if the EventHandler calls the ActionHandler with the correct methods and args
		verify(actionHandler).perform(argThat(new IsSameOeeStatus()), anyString(), eq("sharedWith"));
	}
	
	@Test
	public void testFirePersonalDataSent(){
		String triggerName = "{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataSent";
		oeeStatus.setTriggerName(triggerName);
		oeeStatus.setTrigger(new TriggerPersonalDataSent());
		
		// Test when we dont't have a registered matching Obligation in the database
		when(puidDao.findObject(PiiUniqueId.class, (long) 0)).thenReturn(piiUid);
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(null);
		
		eventHandler.firePersonalDataSent(pii, "sharedWith");
		// Check if the EventHandler never calls the ActionHandler
		verifyZeroInteractions(actionHandler); 
		
		// Test when we have a registered matching Obligation in the database
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(oeeStatus);
		
		eventHandler.firePersonalDataSent(pii, "sharedWith");
		
		// Check if the EventHandler calls the ActionHandler with the correct methods and args
		verify(actionHandler).perform(argThat(new IsSameOeeStatus()), anyString(), eq("sharedWith"));
	}
	
	@Test
	public void testFirePersonalDataDeleted(){
		String triggerName = "{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataDeleted";
		oeeStatus.setTriggerName(triggerName);
		oeeStatus.setTrigger(new TriggerPersonalDataDeleted());
		
		// Test when we dont't have a registered matching Obligation in the database
		when(puidDao.findObject(PiiUniqueId.class, (long) 0)).thenReturn(piiUid);
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(null);
		
		eventHandler.firePersonalDataDeleted(pii);
		// Check if the EventHandler never calls the ActionHandler
		verifyZeroInteractions(actionHandler); 
		
		// Test when we have a registered matching Obligation in the database
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(oeeStatus);
		
		eventHandler.firePersonalDataDeleted(pii);
		
		// Check if the EventHandler calls the ActionHandler with the correct methods and args
		verify(actionHandler).perform(argThat(new IsSameOeeStatus()),anyString());
	}
	
	@Test
	public void testFireDataSubjectAccess(){
		try{
			eventHandler.fireDataSubjectAccess(pii);
			fail("IEventHandlerService.fireDataSubjectAccess(PIIType pii) should raise NotImplementedException");
		}catch(NotImplementedException e){
			
		}
	}
	
	@Test
	public void testFireDataLost(){
		try{
			eventHandler.fireDataLost(pii);
			fail("IEventHandlerService.fireDataLost(PIIType pii) should raise NotImplementedException");
		}catch(NotImplementedException e){
			
		}
	}
	
	@Test
	public void testFireOnViolation(){
		try{
			eventHandler.fireOnViolation(pii);
			fail("IEventHandlerService.fireOnViolation(PIIType pii) should raise NotImplementedException");
		}catch(NotImplementedException e){
			
		}
	}
	
	@Test
	public void testFireAtTime(){
		String triggerName = "{http://www.primelife.eu/ppl/obligation}TriggerAtTime";
		oeeStatus.setTriggerName(triggerName);
		oeeStatus.setTrigger(new TriggerPersonalDataDeleted());
		
		// Test when we dont't have a registered matching Obligation in the database
		when(puidDao.findObject(PiiUniqueId.class, (long) 0)).thenReturn(piiUid);
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(null);
		
		eventHandler.fireAtTime(pii);
		// Check if the EventHandler never calls the ActionHandler
		verifyZeroInteractions(actionHandler); 
		
		// Test when we have a registered matching Obligation in the database
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(oeeStatus);
		
		eventHandler.fireAtTime(pii);
		
		// Check if the EventHandler calls the ActionHandler with the correct methods and args
		verify(actionHandler).perform(argThat(new IsSameOeeStatus()),anyString());
	}
	
	@Test
	public void testFirePeriodic(){
		String triggerName = "{http://www.primelife.eu/ppl/obligation}TriggerPeriodic";
		oeeStatus.setTriggerName(triggerName);
		oeeStatus.setTrigger(new TriggerPersonalDataDeleted());
		
		// Test when we dont't have a registered matching Obligation in the database
		when(puidDao.findObject(PiiUniqueId.class, (long) 0)).thenReturn(piiUid);
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(null);
		
		eventHandler.firePeriodic(pii);
		// Check if the EventHandler never calls the ActionHandler
		verifyZeroInteractions(actionHandler); 
		
		// Test when we have a registered matching Obligation in the database
		when(oeeDao.consumeEvent(piiUid ,triggerName)).thenReturn(oeeStatus);
		
		eventHandler.firePeriodic(pii);
		
		// Check if the EventHandler calls the ActionHandler with the correct methods and args
		verify(actionHandler).perform(argThat(new IsSameOeeStatus()),anyString());
	}
	
	private class IsSameOeeStatus extends ArgumentMatcher<OEEStatus> {
		public boolean matches(Object obj) {
			return ((OEEStatus) obj).equals(oeeStatus);
		}
	}

}
