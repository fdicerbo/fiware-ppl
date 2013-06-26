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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.research.primelife.message.request.DelegateRequest;
import com.sap.research.primelife.rest.file.FileManager;
import com.sap.research.primelife.rest.service.DownstreamUsageService;
import com.sap.research.primelife.test.generator.PolicyGenerator;
import com.sun.jersey.multipart.MultiPart;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

public class DownStreamUsageControllerTest {

	private final static String FILE_DIR = DownStreamUsageControllerTest.class.getResource("/downStreamUsageControllerTest").getPath() + File.separator;
	private DownstreamUsageController downstreamUsageController;
	@Mock private DownstreamUsageService mDownstreamUsageService;
	@Mock private FileManager mFileService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		downstreamUsageController = new DownstreamUsageController(mDownstreamUsageService, mFileService);
	}

	/*
	 * Test Request
	 * Given the request encounters no problem and the request match n Piis
	 * The DownstreamUsageController should call the DownstreamUsageService to evaluate the request
	 * For each PII
	 * 		- The DownstreamUsageController should call the FileManager to retrieve the file
	 * The DownstreamUsageController should return a Response with 200 status and the n files
	 */
	@Test
	public void testRequestNoProblemNMatching() {
		int n = 3;
		final String owner = ""+1;
		final StickyPolicy sp = PolicyGenerator.buildStickyPolicy();
		
		DelegateRequest delegateRequest = new DelegateRequest();
		delegateRequest.setResource("file.txt");
		delegateRequest.setSubject("toto@sap.com");
		
		List<PIIType> list = new ArrayList<PIIType>();
		for(int i=0; i<n; i++){
			PIIType pii = new PIIType();
			pii.setHjid((long) i);
			pii.setAttributeName("file.txt");
			pii.setAttributeValue(i+"_file.txt");
			pii.setOwner(owner);
			pii.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sp.getAttribute().get(0)));
			list.add(pii);
		}
		when(mDownstreamUsageService.requestPiis(delegateRequest)).thenReturn(list);
		when(mFileService.get(anyString())).thenAnswer(new Answer<File>(){

			@Override
			public File answer(InvocationOnMock invocation) throws Throwable {
				String fileName = (String)invocation.getArguments()[0];
				return new File(FILE_DIR+fileName);
			}
			
		});
		
		Response response = downstreamUsageController.requestPiiFile(delegateRequest);
		
		verify(mDownstreamUsageService).requestPiis(delegateRequest);
		for(int i=0;i<n;i++){
			verify(mFileService).get(i+"_file.txt");
		}
		
		assertEquals(200, response.getStatus());
		
		MultiPart multipart = (MultiPart) response.getEntity();
		assertEquals(n, multipart.getBodyParts().size());
		
		for(int i=0;i<n;i++){
			File fileRet = (File) multipart.getBodyParts().get(i).getEntity();
			File fileExpected = new File(FILE_DIR+i+"_file.txt");
			assertEquals(fileExpected,fileRet);
		}
	}
	
	/*
	 * Test Request
	 * Given the request encounters no problem and the request match 0 Pii
	 * The DownstreamUsageController should call the DownstreamUsageService to evaluate the request
	 * The DownstreamUsageController should have no interaction with the FileManager
	 * The DownstreamUsageController should return a Response with 200 status and the 0 files
	 */
	@Test
	public void testRequestNoProblem0Matching() {
		DelegateRequest delegateRequest = new DelegateRequest();
		delegateRequest.setResource("file.txt");
		delegateRequest.setSubject("toto@sap.com");
		
		List<PIIType> list = new ArrayList<PIIType>();
		when(mDownstreamUsageService.requestPiis(delegateRequest)).thenReturn(list);
		
		Response response = downstreamUsageController.requestPiiFile(delegateRequest);
		
		verify(mDownstreamUsageService).requestPiis(delegateRequest);
		verifyNoMoreInteractions(mFileService);
		
		assertEquals(204, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}
	
	/*
	 * Test Request
	 * Given the request is wrong
	 * The DownstreamUsageController should have no with the DownstreamUsageService
	 * The DownstreamUsageController should have no interaction with the FileManager
	 * The DownstreamUsageController should return a Response with 400 status and no content
	 */
	@Test
	public void testRequestProblem() {		
		Response response = downstreamUsageController.requestPiiFile(null);
		
		verifyNoMoreInteractions(mDownstreamUsageService);
		verifyNoMoreInteractions(mFileService);
		
		assertEquals(400, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}

}
