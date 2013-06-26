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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.research.primelife.dc.pep.PEP;
import com.sap.research.primelife.message.request.PiiDeleteRequest;
import com.sap.research.primelife.message.response.PiiDeleteResponse;
import com.sap.research.primelife.rest.file.FileManager;
import com.sap.research.primelife.rest.file.FileManagerTest;
import com.sap.research.primelife.test.generator.PolicyGenerator;
import com.sap.research.primelife.test.matcher.IsSamePii;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

public class PiiServiceTest {

	private final static String FILE_DIR = FileManagerTest.class.getResource("/piiService").getPath() + File.separator;
	private PiiService piiService;
	@Mock private FileManager mFileManager;
	@Mock private PEP mPep;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		piiService = new PiiService(mFileManager, mPep);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	/*
	 * Test Create Pii
	 * The PiiService should call the FileManager to store the file
	 * The PiiService should call the PEP to store the PEP
	 * The PiiService should return the created Pii
	 */
	public void testCreate() {
		String content = "fkldfjlsdjlgf";
		InputStream file = new ByteArrayInputStream(content.getBytes());
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		String owner = ""+ 1;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file.ext");
		piiWitness.setAttributeValue("214578_file.ext");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		when(mFileManager.store("file.ext", file)).thenReturn("214578_file.ext");
		when(mPep.storePii("file.ext", "214578_file.ext", sPolicy.getAttribute().get(0), owner)).thenReturn(piiWitness);
		
		PIIType pii = piiService.create("file.ext", file, sPolicy, owner);
		
		// The PiiService should call the FileManager to store the file
		verify(mFileManager).store("file.ext", file);
		// The PiiService should call the PEP to store the PEP
		verify(mPep).storePii("file.ext", "214578_file.ext", sPolicy.getAttribute().get(0), owner);
		
		IsSamePii matcher = new IsSamePii("file.ext", "214578_file.ext", owner, piiWitness.getPolicySetOrPolicyItems());
		assertThat(pii , matcher);
	}
	
	@Test
	/*
	 * Test Update Pii
	 * The PiiService should call the FileManager to store the file
	 * The PiiService should call the FileManager to delete the previous file
	 * The PiiService should call the PEP to update the PEP
	 * The PiiService should return the updated Pii
	 */
	public void testUpdate() {
		String content = "fkldfjlsdjlgf";
		InputStream file = new ByteArrayInputStream(content.getBytes());
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		String owner = ""+1;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file.ext");
		piiWitness.setAttributeValue("214578_file.ext");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		PIIType piiNotUpdated = new PIIType();
		piiNotUpdated.setHjid((long) 0);
		piiNotUpdated.setAttributeName("file.ext");
		piiNotUpdated.setAttributeValue("214578_file.ext");
		piiNotUpdated.setOwner(owner);
		piiNotUpdated.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		when(mFileManager.store("file2.ext", file)).thenReturn("214578_file2.ext");
		when(mPep.updatePii(piiNotUpdated, "file2.ext", "214578_file2.ext", sPolicy.getAttribute().get(0))).thenAnswer(new Answer<PIIType>(){

			@Override
			public PIIType answer(InvocationOnMock invocation) throws Throwable {
				PIIType pii = (PIIType) invocation.getArguments()[0];	
				String name = (String) invocation.getArguments()[1];
				String value = (String) invocation.getArguments()[2];
				AttributeType attrType = (AttributeType) invocation.getArguments()[3];
				
				PIIType piiRet = new PIIType();
				piiRet.setHjid(pii.getHjid());
				piiRet.setOwner(pii.getOwner());
				piiRet.setAttributeName(name);
				piiRet.setAttributeValue(value);
				piiRet.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(attrType));
				return piiRet;
			}});
		
		PIIType pii = piiService.update(piiNotUpdated, "file2.ext", file, sPolicy);
		
		// The PiiService should call the FileManager to store the file
		verify(mFileManager).store("file2.ext", file);
		// The PiiService should call the FileManager to delete the previous file
		verify(mFileManager).delete("214578_file.ext");
		 // The PiiService should call the PEP to update the PEP
		IsSamePii matcher = new IsSamePii("file.ext", "214578_file.ext", owner);
		verify(mPep).updatePii(argThat(matcher), eq("file2.ext"), eq("214578_file2.ext"), (AttributeType) anyObject());
		 // The PiiService should return the updated Pii
		matcher = new IsSamePii("file2.ext", "214578_file2.ext", owner, piiWitness.getPolicySetOrPolicyItems());
		assertThat(pii , matcher);
	}
	
	@Test
	/*
	 * Test Get Pii
	 * The PiiService should call the PEP to retrieve the PII
	 * The PiiService should have no interaction with the FileManager
	 * The PiiService should return the Pii
	 */
	public void testGet() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		String owner = ""+1;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file.ext");
		piiWitness.setAttributeValue("214578_file.ext");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		when(mPep.getPii((long) 123456789, owner)).thenReturn(piiWitness);
		
		PIIType pii = piiService.getPii((long) 123456789, owner);
		
		// The PiiService should call the PEP to retrieve the PII
		verify(mPep).getPii((long) 123456789, owner);
		// The PiiService should have no interaction with the FileManager
		verifyZeroInteractions(mFileManager);
		// The PiiService should return the Pii
		IsSamePii matcher = new IsSamePii("file.ext", "214578_file.ext", owner, piiWitness.getPolicySetOrPolicyItems());
		assertThat(pii, matcher);
		
		pii = piiService.getPii((long) 987654321, owner);
		
		// The PiiService should call the PEP to retrieve the PII
		verify(mPep).getPii((long) 987654321, owner);
		// The PiiService should have no interaction with the FileManager
		verifyZeroInteractions(mFileManager);
		// The PiiService should return null
		assertNull(pii);
	}
	
	@Test
	/*
	 * Test Get PiiFile
	 * The PiiService should call the PEP to retrieve the PII
	 * The PiiService should call the FileManager to retrieve the File
	 * The PiiService should return the File
	 */
	public void testGetPiiFile() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		String owner = ""+1;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file.ext");
		piiWitness.setAttributeValue("214578_file.ext");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		File f = new File(FILE_DIR + "214578_file.ext");
		
		when(mPep.getPii((long) 123456789, owner)).thenReturn(piiWitness);
		when(mFileManager.get("214578_file.ext")).thenReturn(f);
		
		File fileReturned = piiService.getPiiFile((long) 123456789, owner);
		
		// The PiiService should call the PEP to retrieve the PII
		verify(mPep).getPii((long) 123456789, owner);
		// The PiiService should have no interaction with the FileManager
		verify(mFileManager).get("214578_file.ext");
		// The PiiService should return the File
		assertEquals("214578_file.ext", fileReturned.getName());
	}
	
	@Test
	/*
	 * Test Get PiiFile with Pii does not exist
	 * The PiiService should call the PEP to retrieve the PII
	 * The PiiService should have no interaction with the FileManager
	 * The PiiService should return null
	 */
	public void testGetPiiFilePiiDoesNotExist() {
		String owner = ""+ 1;
		
		when(mPep.getPii((long) 123456789, owner)).thenReturn(null);
		
		File fileReturned = piiService.getPiiFile((long) 123456789, owner);
		
		// The PiiService should call the PEP to retrieve the PII
		verify(mPep).getPii((long) 123456789, owner);
		// The PiiService should have no interaction with the FileManager
		verifyZeroInteractions(mFileManager);
		// The PiiService should return null
		assertNull(fileReturned);
	}
	
	@Test
	/*
	 * Test delete PiiFile
	 * The PiiService should call the PEP to delete the PII
	 * The PiiService should call the FileManager to delete the file
	 * The PiiService should return a Positive Response
	 */
	public void testDelete() {
		String owner = ""+ 1;
		
		when(mPep.deletePii((long) 123456789, owner)).thenReturn("214578_file.ext");
		when(mFileManager.fileExist("214578_file.ext")).thenReturn(true);
		when(mFileManager.delete("214578_file.ext")).thenReturn(true);
		
		PiiDeleteRequest piiDeleteRequest = new PiiDeleteRequest();
		piiDeleteRequest.setOwner(owner);
		piiDeleteRequest.setPiiUniqueId((long) 123456789);
		
		PiiDeleteResponse response = piiService.delete(piiDeleteRequest);
		
		// The PiiService should call the PEP to delete the PII
		verify(mPep).deletePii((long) 123456789, owner);
		// The PiiService should call the FileManager to delete the file
		verify(mFileManager).delete("214578_file.ext");
		// The PiiService should return null
		assertTrue(response.isDeleted());
	}
	
	@Test
	/*
	 * Test delete PiiFile, Pii not found
	 * The PiiService should call the PEP to delete the PII
	 * The PiiService should have no interaction with FileManage
	 * The PiiService should return a negative Response
	 */
	public void testDeletePiiNotFound() {
		String owner = ""+ 1;
		
		when(mPep.deletePii((long) 123456789, owner)).thenReturn(null);
		when(mFileManager.delete("214578_file.ext")).thenReturn(true);
		
		PiiDeleteRequest piiDeleteRequest = new PiiDeleteRequest();
		piiDeleteRequest.setOwner(owner);
		piiDeleteRequest.setPiiUniqueId((long) 123456789);
		
		PiiDeleteResponse response = piiService.delete(piiDeleteRequest);
		
		// The PiiService should call the PEP to delete the PII
		verify(mPep).deletePii((long) 123456789, owner);
		// The PiiService should have no interaction with the FileManager
		verifyZeroInteractions(mFileManager);
		// The PiiService should return null
		assertFalse(response.isDeleted());
	}

}
