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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.pep.PEP;
import com.sap.research.primelife.message.request.DelegateRequest;

import eu.primelife.ppl.pii.impl.PIIType;

public class DownstreamUsageServiceTest {

	private DownstreamUsageService downstreamUsageService;
	@Mock private PEP mPep;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		downstreamUsageService = new DownstreamUsageService(mPep);
	}

	/*
	 * Test Request with a specific resource matching n Pii
	 * The DownstreamUsageService should call the PEP to evaluate the request
	 * The DownstreamUsageService should return a List of n PIIs
	 */
	@Test
	public void testRequestSpecificResourceNMatching() {
		DelegateRequest delegateRequest = new DelegateRequest("toto@sap.com", "doc");
		List<PIIType> list = new ArrayList<PIIType>(3);
		list.add(new PIIType());
		list.add(new PIIType());
		list.add(new PIIType());
		
		when(mPep.processDownstreamUsageRequest("toto@sap.com", "doc")).thenReturn(list);
		
		List<PIIType> listReturned = downstreamUsageService.requestPiis(delegateRequest);
		
		verify(mPep).processDownstreamUsageRequest("toto@sap.com", "doc");
		assertEquals(list.size(), listReturned.size());
	}
	
	/*
	 * Test Request with a specific resource matching 0 Pii
	 * The DownstreamUsageService should call the PEP to evaluate the request
	 * The DownstreamUsageService should return a List of 0 PIIs
	 */
	@Test
	public void testRequestSpecificResource0Matching() {
		DelegateRequest delegateRequest = new DelegateRequest("toto@sap.com", "doc");
		List<PIIType> list = new ArrayList<PIIType>(0);
		
		when(mPep.processDownstreamUsageRequest("toto@sap.com", "doc")).thenReturn(list);
		
		List<PIIType> listReturned = downstreamUsageService.requestPiis(delegateRequest);
		
		verify(mPep).processDownstreamUsageRequest("toto@sap.com", "doc");
		assertEquals(0, listReturned.size());
	}
	
	/*
	 * Test Request with a no specific resource matching n Pii
	 * The DownstreamUsageService should call the PEP to evaluate the request
	 * The DownstreamUsageService should return a List of n PIIs
	 */
	@Test
	public void testRequestNoSpecificResourceNMatching() {
		DelegateRequest delegateRequest = new DelegateRequest("toto@sap.com", "any");
		List<PIIType> list = new ArrayList<PIIType>(3);
		list.add(new PIIType());
		list.add(new PIIType());
		list.add(new PIIType());
		
		when(mPep.processDownstreamUsageRequest("toto@sap.com")).thenReturn(list);
		
		List<PIIType> listReturned = downstreamUsageService.requestPiis(delegateRequest);
		
		verify(mPep).processDownstreamUsageRequest("toto@sap.com");
		assertEquals(list.size(), listReturned.size());
	}
	
	/*
	 * Test Request with a no specific resource matching 0 Pii
	 * The DownstreamUsageService should call the PEP to evaluate the request
	 * The DownstreamUsageService should return a List of 0 PIIs
	 */
	@Test
	public void testRequestNoSpecificResource0Matching() {
		DelegateRequest delegateRequest = new DelegateRequest("toto@sap.com", "any");
		List<PIIType> list = new ArrayList<PIIType>(0);
		
		when(mPep.processDownstreamUsageRequest("toto@sap.com")).thenReturn(list);
		
		List<PIIType> listReturned = downstreamUsageService.requestPiis(delegateRequest);
		
		verify(mPep).processDownstreamUsageRequest("toto@sap.com");
		assertEquals(list.size(), listReturned.size());
	}

}
