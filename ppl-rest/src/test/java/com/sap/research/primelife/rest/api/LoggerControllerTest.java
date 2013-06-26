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
package com.sap.research.primelife.rest.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.message.request.HistoryRequest;
import com.sap.research.primelife.message.response.HistoryResponse;
import com.sap.research.primelife.message.response.HistoryResponseItem;
import com.sap.research.primelife.rest.service.LoggerService;

public class LoggerControllerTest {

	private LoggerController loggerController;
	@Mock private LoggerService mHistoryService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		loggerController = new LoggerController(mHistoryService);
	}

	/**
	 * Test GetHistoryByOwner
	 * Given the params are correct and the request match n log entries
	 * The LoggerController should call the LoggerService to retrieve the n log entries
	 * The LoggerController should return a Response with status 200 and an HistoryResponse containing the n log entries
	 */
	@Test
	public void testGetHistoryByOwnerNMatching() {
		final int n = 3;
		final Long uniqueId = (long) 123456789;
		final String owner = ""+1;
		HistoryRequest request = new HistoryRequest();
		request.setOwner(owner);
		
		HistoryResponse historyReponse = new HistoryResponse();
		List<HistoryResponseItem> events = new ArrayList<HistoryResponseItem>();
		historyReponse.setEvents(events);
		for(int i=0;i<n;i++){
			HistoryResponseItem item = new HistoryResponseItem();
			item.setDate(new Date());
			item.setEvent("Created");
			item.setId((long) i);
			item.setOwner(owner);
			item.setPiiAttributeName("test.txt");
			item.setPiiAttributeValue(i+"_test.txt");
			item.setPuid(uniqueId);
			events.add(item);
		}
		
		when(mHistoryService.getLogsByOwner(owner)).thenReturn(historyReponse);
		
		Response response = loggerController.getHistoryByOwner(request);
		
		verify(mHistoryService).getLogsByOwner(owner);
		
		assertEquals(200, response.getStatus());
		
		HistoryResponse historyResponseReturned = (HistoryResponse) response.getEntity();
		
		assertEquals(n, historyResponseReturned.getEvents().size());
		
		for(int i=0;i<n;i++){
			assertEquals(historyReponse.getEvents().get(i), historyResponseReturned.getEvents().get(i));
		}
	}
	
	/**
	 * Test GetHistoryByOwner
	 * Given the params are correct and the request match 0 log entries
	 * The LoggerController should call the LoggerService to retrieve the n log entries
	 * The LoggerController should return a Response with status 200 and an HistoryResponse containing the 0 log entries
	 */
	@Test
	public void testGetHistoryByOwner0Matching() {
		final int n = 0;
		final String owner = ""+1;
		HistoryRequest request = new HistoryRequest();
		request.setOwner(owner);
		
		HistoryResponse historyReponse = new HistoryResponse();
		List<HistoryResponseItem> events = new ArrayList<HistoryResponseItem>();
		historyReponse.setEvents(events);
		
		when(mHistoryService.getLogsByOwner(owner)).thenReturn(historyReponse);
		
		Response response = loggerController.getHistoryByOwner(request);
		
		verify(mHistoryService).getLogsByOwner(owner);
		
		assertEquals(200, response.getStatus());
		
		HistoryResponse historyResponseReturned = (HistoryResponse) response.getEntity();
		
		assertEquals(n, historyResponseReturned.getEvents().size());
	}
	
	/**
	 * Test GetHistoryByOwner
	 * Given the params are incorrect
	 * The LoggerController should have no interaction with the LoggerService
	 * The LoggerController should return a Response with status 400 and no content
	 */
	@Test
	public void testGetHistoryByOwnerIncorrectRequest() {
		
		Response response = loggerController.getHistoryByOwner(null);
		
		verifyZeroInteractions(mHistoryService);
		
		assertEquals(400, response.getStatus());
		Object entity = response.getEntity();
		assertNull(entity);
	}
}
