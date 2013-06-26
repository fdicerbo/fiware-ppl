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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.EventLog;
import com.sap.research.primelife.dc.logger.LoggerHandler;
import com.sap.research.primelife.dc.obligation.ObligationHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;

public class ActionHandlerServiceTest {

	private IActionHandlerService actionHandlerService;
	
	@Mock private PiiUniqueIdDao piiUniqueIdDAO;
	@Mock private DaoImpl<EventLog> eventLogDAO;
	@Mock private ObligationHandler obligationHandler;
	@Mock private LoggerHandler loggerHandler;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		actionHandlerService = new ActionHandlerService(piiUniqueIdDAO, eventLogDAO, obligationHandler, loggerHandler);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDelete() {
		PiiUniqueId piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setUniqueId((long) 123456789);
		
		PIIType pii = new PIIType();
		pii.setHjid((long) 0);
		pii.setOwner("0");
		piiUid.setPii(pii);
		
		when(piiUniqueIdDAO.findByUniqueId((long) 123456789)).thenReturn(piiUid);
		
		actionHandlerService.delete((long) 123456789);
		
		verify(piiUniqueIdDAO).findByUniqueId((long) 123456789);
		verify(loggerHandler).logDelete(pii);
		verify(obligationHandler).deleteObligations(pii);
		verify(piiUniqueIdDAO).deleteObject(piiUid);
	}
	
	@Test
	public void testLog1() {
		PiiUniqueId piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setUniqueId((long) 123456789);
		final PIIType pii = new PIIType();
		pii.setHjid((long) 0);
		pii.setOwner("0");
		piiUid.setPii(pii);
		
		when(piiUniqueIdDAO.findByUniqueId((long) 123456789)).thenReturn(piiUid);
		
		actionHandlerService.log((long) 123456789, "a message");
		
		verify(piiUniqueIdDAO).findByUniqueId((long) 123456789);
		EventLog log = new EventLog();
		log.setPii(pii);
		log.setMessage("a message");
		
		class IsValidEventLog extends ArgumentMatcher<EventLog> {
		      public boolean matches(Object eventLog) {
		          return ((EventLog) eventLog).getMessage().equals("a message") &&
		        		  ((EventLog) eventLog).getPii().equals(pii);
		      }
		   }
		
		verify(eventLogDAO).persistObject(argThat(new IsValidEventLog()));
	}
	
	@Test
	public void testLog2() {
		PiiUniqueId piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setUniqueId((long) 123456789);
		final PIIType pii = new PIIType();
		pii.setHjid((long) 0);
		pii.setOwner("0");
		piiUid.setPii(pii);
		
		when(piiUniqueIdDAO.findByUniqueId((long) 123456789)).thenReturn(piiUid);
		
		actionHandlerService.log((long) 123456789, "a message", "toto");
		
		verify(piiUniqueIdDAO).findByUniqueId((long) 123456789);
		EventLog log = new EventLog();
		log.setPii(pii);
		log.setMessage("a message");
		log.setSharedWith("toto");
		
		class IsValidEventLog extends ArgumentMatcher<EventLog> {
		      public boolean matches(Object eventLog) {
		          return ((EventLog) eventLog).getMessage().equals("a message") &&
		        		  ((EventLog) eventLog).getPii().equals(pii) &&
		        		  ((EventLog) eventLog).getSharedWith().equals("toto");
		      }
		   }
		
		verify(eventLogDAO).persistObject(argThat(new IsValidEventLog()));
	}
	
	@Test
	/**
	 * Unable to test this method ...
	 */
	public void testNotify() {
		System.setProperty("systemConfigFilePath", getClass().getResource("/system.properties").getFile().toString());
		actionHandlerService.notify((long) 123456789, "Media", "paul.cervera.y.alvarez@sap.com", "a message for Toto");
	}
	
	@Test
	public void testAnonymise() {
		try{
			actionHandlerService.anonymize((long) 123456789);
			fail("IActionHandlerService.anonymise(long piiUid) should raise NotImplementedException");
		}catch (NotImplementedException e) {
			
		}
	}
	
	@Test
	public void testSecureLog() {
		try{
			actionHandlerService.secureLog((long) 123456789);
			fail("IActionHandlerService.secureLog(long piiUid) should raise NotImplementedException");
		}catch (NotImplementedException e) {
			
		}
	}

}
