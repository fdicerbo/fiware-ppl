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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.message.request.PiiDeleteRequest;
import com.sap.research.primelife.message.response.PiiCreateResponse;
import com.sap.research.primelife.message.response.PiiDeleteResponse;
import com.sap.research.primelife.message.response.PiiUpdateResponse;
import com.sap.research.primelife.rest.file.FileManagerTest;
import com.sap.research.primelife.rest.service.PiiService;
import com.sap.research.primelife.test.generator.PolicyGenerator;
import com.sun.jersey.core.header.FormDataContentDisposition;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

public class PiiControllerTest {

	private final static String FILE_NAME = "test.jpg";
	private final static String FILE_DIR = FileManagerTest.class.getResource("/piiController").getPath() + File.separator;
	private PiiController piiController;
	@Mock private PiiUniqueIdDao mPuidDao;
	@Mock private PiiService mPiiService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		piiController = new PiiController(mPiiService, mPuidDao);
	}

	/*
	 * Test GetPii with correct parameters
	 * Given the parameters match a Pii
	 * The PiiController should call the PiiService to retrieve the File of the corresponding Pii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 200 and the File 
	 */
	@Test
	public void testGetCorrectParamMatchingPii() {
		File f = new File(FILE_DIR + FILE_NAME);
		final String owner = "1";
		when(mPiiService.getPiiFile((long) 123456789, owner)).thenReturn(f);
		
		Response response = piiController.getPii((long) 123456789, owner);
		
		verify(mPiiService).getPiiFile((long) 123456789, owner);
		verifyZeroInteractions(mPuidDao);
		
		assertEquals(200, response.getStatus());
		File fileReturned = (File) response.getEntity();
		assertEquals(f, fileReturned);
	}
	
	/*
	 * Test GetPii with correct parameters
	 * Given the parameters match no Pii
	 * The PiiController should call the PiiService to retrieve the File of the corresponding Pii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 204 and no content
	 */
	@Test
	public void testGetCorrectParamMatchingNoPii() {
		final String owner = "1";
		when(mPiiService.getPiiFile((long) 123456789, owner)).thenReturn(null);
		
		Response response = piiController.getPii((long) 123456789, owner);
		
		verify(mPiiService).getPiiFile((long) 123456789, owner);
		verifyZeroInteractions(mPuidDao);
		
		assertEquals(204, response.getStatus());
		assertNull(response.getEntity());
	}
	
	/*
	 * Test GetPii with incorrect parameters
	 * The PiiController should have no interaction with PiiService
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 400 and no content
	 */
	@Test
	public void testGetIncorrectParam() {
		final String owner = "1";
		Response response = piiController.getPii(null, owner);
		
		verifyZeroInteractions(mPiiService);
		verifyZeroInteractions(mPuidDao);
		
		assertEquals(400, response.getStatus());
		assertNull(response.getEntity());
	}
	
	/*
	 * Test CreatePii with correct parameters
	 * Given the creation of the Pii encounters no problem
	 * The PiiController should call PiiService to store the Pii
	 * The PiiController should call PiiUniqueDao to retrieve the PiiUniqueId
	 * The PiiController should return a Response with status 201 and a PiiCreateResponse containing the PiiUniqueId
	 */
	@Test
	public void testCreateCorrectParamNoCreationPb() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		final String owner = ""+1;
		final Long piiUniqueId = (long) 12345789;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("test.jpg");
		piiWitness.setAttributeValue("214578_test.jpg");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		PiiUniqueId piiUniqueIdWitness = new PiiUniqueId();
		piiUniqueIdWitness.setId((long) 0);
		piiUniqueIdWitness.setPii(piiWitness);
		piiUniqueIdWitness.setUniqueId(piiUniqueId);
		
		FormDataContentDisposition fileDetail = FormDataContentDisposition.name("file").fileName("test.jpg").build();
		InputStream uploadedInputStream = null;
		try {
			uploadedInputStream = new FileInputStream(new File(FILE_DIR + FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		when(mPiiService.create("test.jpg", uploadedInputStream, sPolicy, owner)).thenReturn(piiWitness);
		when(mPuidDao.findByPiiId((long) 0)).thenReturn(piiUniqueIdWitness);
		
		Response response = piiController.createPiiFile(uploadedInputStream, fileDetail, sPolicy, owner);
		
		verify(mPiiService).create("test.jpg", uploadedInputStream, sPolicy, owner);
		verify(mPuidDao).findByPiiId((long) 0);
		assertEquals(201, response.getStatus());
		
		PiiCreateResponse piiCreateResponse = (PiiCreateResponse) response.getEntity();
		assertEquals(piiUniqueId, piiCreateResponse.getUniqueId());
	}
	
	/*
	 * Test CreatePii with correct parameters
	 * Given the creation of the Pii encounters problems
	 * The PiiController should call PiiService to store the Pii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 500 and no content
	 */
	@Test
	public void testCreateCorrectParamCreationPb() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		final String owner = ""+1;
		
		FormDataContentDisposition fileDetail = FormDataContentDisposition.name("file").fileName("test.jpg").build();
		InputStream uploadedInputStream = null;
		try {
			uploadedInputStream = new FileInputStream(new File(FILE_DIR + FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		when(mPiiService.create("test.jpg", uploadedInputStream, sPolicy, owner)).thenReturn(null);
		
		Response response = piiController.createPiiFile(uploadedInputStream, fileDetail, sPolicy, owner);
		
		verify(mPiiService).create("test.jpg", uploadedInputStream, sPolicy, owner);
		verifyZeroInteractions(mPuidDao);
		assertEquals(500, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}
	
	/*
	 * Test CreatePii with incorrect parameters
	 * The PiiController should have no interaction with PiiService
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 400 and no content
	 */
	@Test
	public void testCreateIncorrectParam() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		final String owner = ""+1;
		
		Response response = piiController.createPii(null, null, sPolicy, owner);
		
		verifyZeroInteractions(mPiiService);
		verifyZeroInteractions(mPuidDao);
		assertEquals(400, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}
	
	/*
	 * Test UpdatePii with correct parameters
	 * Given the update of the Pii encounters no problem
	 * The PiiController should call PiiService to update the Pii
	 * The PiiController should return a Response with status 201 and a PiiUpdateResponse containing the PiiUniqueId
	 */
	@Test
	public void testUpdateCorrectParamNoCreationPb() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		final String owner = ""+1;
		final Long piiUniqueId = (long) 12345789;
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("test.jpg");
		piiWitness.setAttributeValue("214578_test.jpg");
		piiWitness.setOwner(owner);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		PIIType piiUpdated = new PIIType();
		piiUpdated.setHjid((long) 0);
		piiUpdated.setAttributeName("newFile.jpg");
		piiUpdated.setAttributeValue("214578_newFile.jpg");
		piiUpdated.setOwner(owner);
		piiUpdated.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(sPolicy.getAttribute().get(0)));
		
		PiiUniqueId piiUniqueIdWitness = new PiiUniqueId();
		piiUniqueIdWitness.setId((long) 0);
		piiUniqueIdWitness.setUniqueId(piiUniqueId);
		
		FormDataContentDisposition fileDetail = FormDataContentDisposition.name("file").fileName("newFile.jpg").build();
		InputStream uploadedInputStream = null;
		try {
			uploadedInputStream = new FileInputStream(new File(FILE_DIR + FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		when(mPiiService.update(eq(piiUniqueId), eq(owner), eq("newFile.jpg"), eq(uploadedInputStream), eq(sPolicy))).thenReturn(piiUpdated);
		
		Response response = piiController.updateFile(piiUniqueId.toString(), uploadedInputStream, fileDetail, sPolicy, owner);
		
		verify(mPiiService).update(eq(piiUniqueId), eq(owner), eq("newFile.jpg"), eq(uploadedInputStream), eq(sPolicy));
		assertEquals(200, response.getStatus());
		
		PiiUpdateResponse piiUpdateResponse = (PiiUpdateResponse) response.getEntity();
		assertEquals(piiUniqueId, piiUpdateResponse.getUniqueId());
		assertTrue(piiUpdateResponse.isUpdated());
	}
	
	/*
	 * Test UpdatePii with correct parameters
	 * Given the pii does not exist
	 * The PiiController should call PiiService to retrieve the Pii
	 * The PiiController should call PiiService to update the Pii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 404 and no content
	 */
	@Test
	public void testUpdateCorrectParamPiiDoesNotExist() {
		StickyPolicy sPolicy = PolicyGenerator.buildStickyPolicy();
		final String owner = ""+1;
		final Long piiUniqueId = (long) 12345789;
		
		FormDataContentDisposition fileDetail = FormDataContentDisposition.name("file").fileName("newFile.jpg").build();
		InputStream uploadedInputStream = null;
		try {
			uploadedInputStream = new FileInputStream(new File(FILE_DIR + FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		when(mPiiService.update(eq(piiUniqueId), eq(owner), eq("newFile.jpg"), eq(uploadedInputStream), eq(sPolicy))).thenReturn(null);
		
		Response response = piiController.updateFile(piiUniqueId.toString(), uploadedInputStream, fileDetail, sPolicy, owner);
		
		mPiiService.update(eq(piiUniqueId), eq(owner), eq("newFile.jpg"), eq(uploadedInputStream), eq(sPolicy));
		verifyZeroInteractions(mPuidDao);
		assertEquals(200, response.getStatus());
		
		PiiUpdateResponse piiUpdateResponse = (PiiUpdateResponse) response.getEntity();
		assertEquals(piiUniqueId, piiUpdateResponse.getUniqueId());
		assertFalse(piiUpdateResponse.isUpdated());
	}
	
	/*
	 * Test UpdatePii with incorrect parameters
	 * The PiiController should have no interaction with PiiService
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 400 and no content
	 */
	@Test
	public void testUpdateIncorrectParam() {
		final String owner = ""+1;
		final Long piiUniqueId = (long) 12345789;
		
		FormDataContentDisposition fileDetail = FormDataContentDisposition.name("file").fileName("newFile.jpg").build();
		
		Response response = piiController.updateFile(piiUniqueId.toString(), null, fileDetail, null, owner);
		
		verifyZeroInteractions(mPiiService);
		verifyZeroInteractions(mPuidDao);
		assertEquals(400, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}
	
	/*
	 * Test DeletePii with correct parameters
	 * Given the update of the Pii encounters no problems
	 * The PiiController should call PiiService to deletePii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 200 and a Positive PiiDeleteResponse
	 */
	@Test
	public void testDeleteCorrectParamNoDeletePb() {
		final String owner = "1";
		final Long piiUniqueId = (long) 12345789;
		
		PiiDeleteRequest piiRequest = new PiiDeleteRequest();
		piiRequest.setOwner(owner);
		piiRequest.setPiiUniqueId(piiUniqueId);
		
		PiiDeleteResponse piiDeleteResponse = new PiiDeleteResponse();
		piiDeleteResponse.setDeleted(true);
		when(mPiiService.delete(piiRequest)).thenReturn(piiDeleteResponse);
		
		Response response = piiController.delete(piiRequest);
		
		verify(mPiiService).delete(piiRequest);
		verifyZeroInteractions(mPuidDao);
		assertEquals(200, response.getStatus());
		
		PiiDeleteResponse returnedResponse = (PiiDeleteResponse) response.getEntity();
		assertTrue(returnedResponse.isDeleted());
	}
	
	/*
	 * Test DeletePii with correct parameters
	 * Given the update of the Pii encounters problems
	 * The PiiController should call PiiService to deletePii
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 200 and a Negative PiiDeleteResponse
	 */
	@Test
	public void testDeleteCorrectParamDeletePb() {
		final String owner = "1";
		final Long piiUniqueId = (long) 12345789;
		
		PiiDeleteRequest piiRequest = new PiiDeleteRequest();
		piiRequest.setOwner(owner);
		piiRequest.setPiiUniqueId(piiUniqueId);
		
		PiiDeleteResponse piiDeleteResponse = new PiiDeleteResponse();
		piiDeleteResponse.setDeleted(false);
		when(mPiiService.delete(piiRequest)).thenReturn(piiDeleteResponse);
		
		Response response = piiController.delete(piiRequest);
		
		verify(mPiiService).delete(piiRequest);
		verifyZeroInteractions(mPuidDao);
		assertEquals(200, response.getStatus());
		
		PiiDeleteResponse returnedResponse = (PiiDeleteResponse) response.getEntity();
		assertFalse(returnedResponse.isDeleted());
	}
	
	/*
	 * Test DeletePii with incorrect parameters
	 * The PiiController should have no interaction with PiiService
	 * The PiiController should have no interaction with PiiUniqueDao
	 * The PiiController should return a Response with status 400 and no content
	 */
	@Test
	public void testDeleteIncorrectParam() {

		Response response = piiController.delete(null);
		
		verifyZeroInteractions(mPiiService);
		verifyZeroInteractions(mPuidDao);
		assertEquals(400, response.getStatus());
		
		Object entity = response.getEntity();
		assertNull(entity);
	}

}
