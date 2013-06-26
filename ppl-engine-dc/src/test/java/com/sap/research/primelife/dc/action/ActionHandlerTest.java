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
package com.sap.research.primelife.dc.action;

import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.ActionAnonymizePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionLog;
import eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject;
import eu.primelife.ppl.policy.obligation.impl.ActionSecureLog;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose;

public class ActionHandlerTest {

	private IActionHandler actionHandler;
	@Mock private IActionHandlerService mActionHandlerService;
	
	private OEEStatus oeeStatus;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		actionHandler = new ActionHandler(mActionHandlerService);
		
		oeeStatus = new OEEStatus();
		oeeStatus.setId(0);
		PiiUniqueId piiUniqueId = new PiiUniqueId();
		piiUniqueId.setId((long) 0);
		piiUniqueId.setPii(null);
		piiUniqueId.setUniqueId((long) 123456789);
		oeeStatus.setPiiUniqueId(piiUniqueId);
		oeeStatus.setTrigger(new TriggerPersonalDataAccessedForPurpose());
		oeeStatus.setTriggeredOnce(false);
		oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPerform1() {
		Action action = new ActionDeletePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus);
		verify(mActionHandlerService).delete((long) 123456789);
		
		action = new ActionLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus);
		verify(mActionHandlerService).log((long) 123456789, null, null);
		
		ActionNotifyDataSubject actionNotify = new ActionNotifyDataSubject();
		actionNotify.setAddress("toto@sap.com");
		actionNotify.setMedia("MEDIA");
		oeeStatus.setAction(actionNotify);
		actionHandler.perform(oeeStatus);
		verify(mActionHandlerService).notify((long) 123456789, "MEDIA", "toto@sap.com", null);
		
		action = new ActionAnonymizePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus);
		verify(mActionHandlerService).anonymize((long) 123456789);
		
		action = new ActionSecureLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus);
		verify(mActionHandlerService).secureLog((long) 123456789);
	}
	
	@Test
	public void testPerform2() {	
		Action action = new ActionDeletePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message");
		verify(mActionHandlerService).delete((long) 123456789);
		
		action = new ActionLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message");
		verify(mActionHandlerService).log((long) 123456789, "This is a message", null);
		
		ActionNotifyDataSubject actionNotify = new ActionNotifyDataSubject();
		actionNotify.setAddress("toto@sap.com");
		actionNotify.setMedia("MEDIA");
		oeeStatus.setAction(actionNotify);
		actionHandler.perform(oeeStatus, "This is a message");
		verify(mActionHandlerService).notify((long) 123456789, "MEDIA", "toto@sap.com", "This is a message");
		
		action = new ActionAnonymizePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message");
		verify(mActionHandlerService).anonymize((long) 123456789);
		
		action = new ActionSecureLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message");
		verify(mActionHandlerService).secureLog((long) 123456789);
	}
	
	@Test
	public void testPerform3() {
		Action action = new ActionDeletePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message", "SharedWithToto");
		verify(mActionHandlerService).delete((long) 123456789);
		
		action = new ActionLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message", "SharedWithToto");
		verify(mActionHandlerService).log((long) 123456789, "This is a message", "SharedWithToto");
		
		ActionNotifyDataSubject actionNotify = new ActionNotifyDataSubject();
		actionNotify.setAddress("toto@sap.com");
		actionNotify.setMedia("MEDIA");
		oeeStatus.setAction(actionNotify);
		actionHandler.perform(oeeStatus, "This is a message", "SharedWithToto");
		verify(mActionHandlerService).notify((long) 123456789, "MEDIA", "toto@sap.com", "This is a message");
		
		action = new ActionAnonymizePersonalData();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message", "SharedWithToto");
		verify(mActionHandlerService).anonymize((long) 123456789);
		
		action = new ActionSecureLog();
		oeeStatus.setAction(action);
		actionHandler.perform(oeeStatus, "This is a message", "SharedWithToto");
		verify(mActionHandlerService).secureLog((long) 123456789);
	}

}
