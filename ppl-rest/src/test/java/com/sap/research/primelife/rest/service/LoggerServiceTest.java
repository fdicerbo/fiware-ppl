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
package com.sap.research.primelife.rest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.logger.ILoggerHandler;
import com.sap.research.primelife.message.response.HistoryResponse;

import eu.primelife.ppl.piih.impl.EventLoggedCreate;
import eu.primelife.ppl.piih.impl.PiiLogged;

public class LoggerServiceTest {

	private LoggerService loggerService;
	@Mock private ILoggerHandler mLoggerHandler;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		loggerService = new LoggerService(mLoggerHandler);
	}

	/*
	 * Test getLogsByOwner with n log entries returned for the Owner
	 * The LoggerService should call the LoggerHandler to retrieve the log entries
	 * The LoggerService should return a HistoryResponse containing the Log entries
	 */
	@Test
	public void testGetLogsByOwnerNLogEntries() {
		String owner = ""+1;
		int n = 3;
		List<PiiLogged> list = new ArrayList<PiiLogged>(n);
		for(int i=0; i<n; i++){
			PiiLogged piiLogged = new PiiLogged();
			piiLogged.setId((long) i);
			piiLogged.setDate(new Date());
			piiLogged.setEvent(new EventLoggedCreate());
			piiLogged.setOwner(owner);
			piiLogged.setPiiAttributeName("123" + i);
			piiLogged.setPiiAttributeValue("456" + i);
			piiLogged.setPiiUid((long) (123456789 + i));
			list.add(piiLogged);
		}
		
		when(mLoggerHandler.getHistoryByOwner(owner)).thenReturn(list);
		
		HistoryResponse response = loggerService.getLogsByOwner(owner);
		
		verify(mLoggerHandler).getHistoryByOwner(owner);
		
		assertEquals(list.size() ,response.getEvents().size());
		for(int i=0; i<n; i++){
			assertEquals(list.get(i).getId(), response.getEvents().get(i).getId());
			assertEquals(list.get(i).getDate(), response.getEvents().get(i).getDate());
			assertEquals(list.get(i).getOwner(), response.getEvents().get(i).getOwner());
			assertEquals(list.get(i).getPiiAttributeName(), response.getEvents().get(i).getPiiAttributeName());
			assertEquals(list.get(i).getPiiAttributeValue(), response.getEvents().get(i).getPiiAttributeValue());
			assertEquals(list.get(i).getPiiUid(), response.getEvents().get(i).getPuid());
			assertEquals("Created", response.getEvents().get(i).getEvent());
		}
	}
	
	/*
	 * Test getLogsByOwner with no log entry returned for the Owner
	 * The LoggerService should call the LoggerHandler to retrieve the log entries
	 * The LoggerService should return a HistoryResponse containing no log entries
	 */
	@Test
	public void testGetLogsByOwner0LogEntry() {
		String owner = ""+1;
		int n = 0;
		List<PiiLogged> list = new ArrayList<PiiLogged>(n);
		
		when(mLoggerHandler.getHistoryByOwner(owner)).thenReturn(list);
		
		HistoryResponse response = loggerService.getLogsByOwner(owner);
		
		verify(mLoggerHandler).getHistoryByOwner(owner);
		
		assertEquals(list.size() ,response.getEvents().size());
	}
	
	/*
	 * Test getLogsByPii with n log entries returned for the pii
	 * The LoggerService should call the LoggerHandler to retrieve the log entries
	 * The LoggerService should return a HistoryResponse containing the Log entries
	 */
	@Test
	public void testGetLogsByPiiNLogEntries() {
		String owner = ""+1;
		int n = 3;
		List<PiiLogged> list = new ArrayList<PiiLogged>(n);
		for(int i=0; i<n; i++){
			PiiLogged piiLogged = new PiiLogged();
			piiLogged.setId((long) i);
			piiLogged.setDate(new Date());
			piiLogged.setEvent(new EventLoggedCreate());
			piiLogged.setOwner(owner);
			piiLogged.setPiiAttributeName("123" + i);
			piiLogged.setPiiAttributeValue("456" + i);
			piiLogged.setPiiUid((long) (123456789));
			list.add(piiLogged);
		}
		
		when(mLoggerHandler.getHistoryByPii((long) 123456789)).thenReturn(list);
		
		HistoryResponse response = loggerService.getLogsByPii((long) 123456789);
		
		verify(mLoggerHandler).getHistoryByPii((long) 123456789);
		
		assertEquals(list.size() ,response.getEvents().size());
		for(int i=0; i<n; i++){
			assertEquals(list.get(i).getId(), response.getEvents().get(i).getId());
			assertEquals(list.get(i).getDate(), response.getEvents().get(i).getDate());
			assertEquals(list.get(i).getOwner(), response.getEvents().get(i).getOwner());
			assertEquals(list.get(i).getPiiAttributeName(), response.getEvents().get(i).getPiiAttributeName());
			assertEquals(list.get(i).getPiiAttributeValue(), response.getEvents().get(i).getPiiAttributeValue());
			assertEquals(list.get(i).getPiiUid(), response.getEvents().get(i).getPuid());
			assertEquals("Created", response.getEvents().get(i).getEvent());
		}
	}
	
	/*
	 * Test getLogsByPii with 0 log entry returned for the pii
	 * The LoggerService should call the LoggerHandler to retrieve the log entries
	 * The LoggerService should return a HistoryResponse containing no log entry
	 */
	@Test
	public void testGetLogsByPii0LogEntry() {
		int n = 0;
		List<PiiLogged> list = new ArrayList<PiiLogged>(n);
		
		when(mLoggerHandler.getHistoryByPii((long) 123456789)).thenReturn(list);
		
		HistoryResponse response = loggerService.getLogsByPii((long) 123456789);
		
		verify(mLoggerHandler).getHistoryByPii((long) 123456789);
		
		assertEquals(list.size() ,response.getEvents().size());
	}
}
