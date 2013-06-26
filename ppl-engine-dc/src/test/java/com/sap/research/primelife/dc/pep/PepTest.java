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
package com.sap.research.primelife.dc.pep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusCodeType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusType;

import org.herasaf.xacml.core.context.impl.RequestType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dc.dao.DaoImpl;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.event.IEventHandler;
import com.sap.research.primelife.dc.logger.ILoggerHandler;
import com.sap.research.primelife.dc.obligation.ObligationHandler;
import com.sap.research.primelife.ds.pdp.PDP;
import com.sap.research.primelife.test.generator.PolicyGenerator;
import com.sap.research.primelife.test.matcher.IsSameObligationsSet;
import com.sap.research.primelife.test.matcher.IsSamePii;
import com.sap.research.primelife.test.matcher.IsSamePiiUniqueId;
import com.sap.research.primelife.test.matcher.IsSamePolicyItem;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PIITypePolicySetOrPolicyItem;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;

public class PepTest {

	private PEP pep;
	@Mock private PiiUniqueIdDao mPuidDao;
	@Mock private PDP mPdp;
	@Mock private IEventHandler mEventHandler;
	@Mock private ILoggerHandler mLoggerHandler;
	@Mock private ObligationHandler mObligationHandler;
	@Mock private PiiDao mPiiDao;
	@Mock private DaoImpl<PIITypePolicySetOrPolicyItem> mPiiPolicySetOrPolicyItemDao;
	
	private eu.primelife.ppl.policy.impl.ObjectFactory of = new eu.primelife.ppl.policy.impl.ObjectFactory();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		pep = new PEP(mPdp, mPiiDao, mPuidDao, mPiiPolicySetOrPolicyItemDao, of, mObligationHandler, mEventHandler, mLoggerHandler);
	}
	
	/*
	 * Test storePii with owner
	 * PEP should call the PiiDao to persist the new Pii
	 * PEP should call the PiiUniqueId to create a new PiiUniqueId
	 * PEP should call the ObligationHandler to manage the obligations
	 * PEP should call the LoggerHandler to log the creation of a pii
	 */
	@Test
	public void testStorePiiWithOwner() {
		AttributeType spAttribute = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute));
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("1");
		
		when(mPuidDao.createUniqueId((PIIType)anyObject())).thenReturn((long) 123456789);
		
		pep.storePii("attrName", "attrValue", spAttribute, "1");
		
		// PEP should call the PiiDao to persist the new Pii
		verify(mPiiDao).persistObject(argThat(new IsSamePii("attrName", "attrValue", "1", piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the PiiUniqueId to create a new PiiUniqueId
		verify(mPuidDao).persistObject(argThat(new IsSamePiiUniqueId((long) 123456789, piiWitness)));
		// PEP should call the ObligationHandler to manage the obligations
		verify(mObligationHandler).addObligations(argThat(new IsSameObligationsSet(spAttribute.getObligationsSet())), argThat(new IsSamePii("attrName", "attrValue", "1", piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the LoggerHandler to log the creation of a pii
		verify(mLoggerHandler).logCreate(argThat(new IsSamePii("attrName", "attrValue", "1", piiWitness.getPolicySetOrPolicyItems())));
	}
	
	/*
	 * Test storePii without owner
	 * PEP should call the PiiDao to persist the new Pii
	 * PEP should call the PiiUniqueId to create a new PiiUniqueId
	 * PEP should call the ObligationHandler to manage the obligations
	 * PEP should call the LoggerHandler to log the creation of a pii
	 */
	@Test
	public void testStorePiiWithoutOwner() {
		AttributeType spAttribute = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute));
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner(null);
		
		when(mPuidDao.createUniqueId((PIIType)anyObject())).thenReturn((long) 123456789);
		
		pep.storePii("attrName", "attrValue", spAttribute);
		
		// PEP should call the PiiDao to persist the new Pii
		verify(mPiiDao).persistObject(argThat(new IsSamePii("attrName", "attrValue", null, piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the PiiUniqueId to create a new PiiUniqueId
		verify(mPuidDao).persistObject(argThat(new IsSamePiiUniqueId((long) 123456789, piiWitness)));
		// PEP should call the ObligationHandler to manage the obligations
		verify(mObligationHandler).addObligations(argThat(new IsSameObligationsSet(spAttribute.getObligationsSet())), argThat(new IsSamePii("attrName", "attrValue", null, piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the LoggerHandler to log the creation of a pii
		verify(mLoggerHandler).logCreate(argThat(new IsSamePii("attrName", "attrValue", null, piiWitness.getPolicySetOrPolicyItems())));
	}
	
	/*
	 * Test updatePii
	 * PEP should call the ObligationHandler to remove the previous obligations
	 * PEP should call the PiiPolicySetOrPolicyItemDao to remove the previous Policy
	 * PEP should call the PiiDao to persist the updated Pii with its Policy
	 * PEP should call the ObligationHandler to manage the new obligations
	 * PEP should call the LoggerHandler to log the update of a pii
	 */
	@Test
	public void testUpdatePii() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		AttributeType spAttribute1 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setOwner("0");
		pii.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PIIType piiWitness = new PIIType();
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute1));
		
		List<PIITypePolicySetOrPolicyItem> oldPolicyLists = pii.getPolicySetOrPolicyItems(); 
		List<PIITypePolicySetOrPolicyItem> newPolicyLists = piiWitness.getPolicySetOrPolicyItems(); 
		
		pep.updatePii(pii, "newName", "newValue", spAttribute1);
		
		//PEP should call the ObligationHandler to remove the previous obligations
		verify(mObligationHandler).deleteObligations(pii);
		// PEP should call the PiiPolicySetOrPolicyItemDao to remove the previous Policy
		for(PIITypePolicySetOrPolicyItem policyItem : oldPolicyLists){
			verify(mPiiPolicySetOrPolicyItemDao).deleteObject(argThat(new IsSamePolicyItem(policyItem)));
		}
		// PEP should call the PiiDao to persist the updated Pii with its Policy
		verify(mPiiDao).persistObject(argThat(new IsSamePii("newName", "newValue", "0", newPolicyLists)));
		// PEP should call the ObligationHandler to manage the new obligations
		verify(mObligationHandler).addObligations(argThat(new IsSameObligationsSet(spAttribute1.getObligationsSet())), argThat(new IsSamePii("newName", "newValue", "0", newPolicyLists)));
		// PEP should call the LoggerHandler to log the update of a pii
		verify(mLoggerHandler).logUpdate(argThat(new IsSamePii("newName", "newValue", "0", newPolicyLists)));
	}
	
	/*
	 * Test updatePii without new Policy
	 * PEP should call the PiiDao to update the updated Pii
	 * PEP should call the LoggerHandler to log the update of a pii
	 */
	@Test
	public void testUpdatePiiWithoutNewPolicy() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setOwner("0");
		pii.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		List<PIITypePolicySetOrPolicyItem> oldPolicyLists = pii.getPolicySetOrPolicyItems(); 
		
		pep.updatePii(pii, "newName", "newValue");
		
		// PEP should call the PiiDao to persist the updated Pii
		verify(mPiiDao).updateObject(argThat(new IsSamePii("newName", "newValue", "0", oldPolicyLists)));
		// PEP should call the LoggerHandler to log the update of a pii
		verify(mLoggerHandler).logUpdate(argThat(new IsSamePii("newName", "newValue", "0", oldPolicyLists)));
	}
	
	/*
	 * Test getPii
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should return the pii
	 */
	@Test
	public void testGetPii() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		
		PIIType returnedPii = pep.getPii((long) 123456789, ""+0);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 123456789, ""+0);
		// PEP should return the pii
		IsSamePii matcher = new IsSamePii("attrName", "attrValue", "0", piiWitness.getPolicySetOrPolicyItems());
		assertThat("Pii returned by PEP differs", returnedPii, matcher);
	}
	
	/*
	 * Test getPii with incorrect PiiUniqueId and correct Owner
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should return null
	 */
	@Test
	public void testGetPiiWithWrongPiiUniqueIdAndRightOwner() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		when(mPuidDao.findByUniqueIdAndOwner((long) 987654321, ""+0)).thenReturn(null);
		
		PIIType returnedPii = pep.getPii((long) 987654321, ""+0);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 987654321, ""+0);
		// PEP should return the pii
		assertTrue("Pii returned by PEP differs", returnedPii == null);
	}
	
	/*
	 * Test getPii with incorrect PiiUniqueId and incorrect Owner
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should return null
	 */
	@Test
	public void testGetPiiWithWrongPiiUniqueIdAndWrongOwner() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		when(mPuidDao.findByUniqueIdAndOwner((long) 987654321, ""+1)).thenReturn(null);
		
		PIIType returnedPii = pep.getPii((long) 987654321, ""+1);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 987654321, ""+1);
		// PEP should return the pii
		assertTrue("Pii returned by PEP differs", returnedPii == null);
	}
	
	/*
	 * Test getPii with correct PiiUniqueId and incorrect Owner
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should return null
	 */
	@Test
	public void testGetPiiWithRightPiiUniqueIdAndWrongOwner() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+1)).thenReturn(null);
		
		PIIType returnedPii = pep.getPii((long) 123456789, ""+1);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 123456789, ""+1);
		// PEP should return the pii
		assertTrue("Pii returned by PEP differs", returnedPii == null);
	}
	
	/*
	 * Test deletePii with existing Pii
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should call the EventHandler to firePersonalDataDeleted event on the Pii
	 * PEP should call the LogHandler to log this event
	 * PEP should call the ObligationHandler to remove its obligation
	 * PEP should call the PiiUniqueIdDao to remove the PiiUniqueId (delete the Pii and its Policy by cascading)
	 * PEP should return the attribute value of the deleted Pii
	 */
	@Test
	public void testDeletePiiOnExistingPii() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		
		String retAttrValue = pep.deletePii((long) 123456789, ""+0);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 123456789, ""+0);
		// PEP should call the EventHandler to firePersonalDataDeleted event on the Pii
		verify(mEventHandler).firePersonalDataDeleted(argThat(new IsSamePii("attrName", "attrValue", "0", piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the LogHandler to log this event
		verify(mLoggerHandler).logDelete(argThat(new IsSamePii("attrName", "attrValue", "0", piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the ObligationHandler to remove its obligation
		verify(mObligationHandler).deleteObligations(argThat(new IsSamePii("attrName", "attrValue", "0", piiWitness.getPolicySetOrPolicyItems())));
		// PEP should call the PiiUniqueIdDao to remove the PiiUniqueId (delete the Pii and its Policy by cascading)
		verify(mPuidDao).deleteObject(argThat(new IsSamePiiUniqueId((long) 123456789, piiWitness)));
		// PEP should return the attribute value of the deleted Pii
		assertEquals("PEP should return the correct attributeValue", retAttrValue, "attrValue");
	}
	
	/*
	 * Test deletePii with incorrect PiiUniqueId and Owner
	 * PEP should call the PiiUniqueIdDao to get the Pii
	 * PEP should have no interaction with the EventHandler
	 * PEP should have no interaction with the LogHandler
	 * PEP should have no interaction with the ObligationHandler
	 * PEP should return null
	 */
	@Test
	public void testDeletePiiOnUnknownPii() {
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy().getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setAttributeName("attrName");
		piiWitness.setAttributeValue("attrValue");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		PiiUniqueId puid = new PiiUniqueId();
		puid.setPii(piiWitness);
		puid.setUniqueId((long) 123456789);
		
		when(mPuidDao.findByUniqueIdAndOwner((long) 123456789, ""+0)).thenReturn(puid);
		when(mPuidDao.findByUniqueIdAndOwner((long) 987654321, ""+1)).thenReturn(null);
		
		String retAttrValue = pep.deletePii((long) 987654321, ""+1);
		
		// PEP should call the PiiUniqueIdDao to get the Pii
		verify(mPuidDao).findByUniqueIdAndOwner((long) 987654321, ""+1);
		// PEP should have no interaction with the EventHandler
		verifyZeroInteractions(mEventHandler);
		// PEP should have no interaction with the LogHandler
		verifyZeroInteractions(mLoggerHandler);
		// PEP should have no interaction with the ObligationHandler
		verifyZeroInteractions(mObligationHandler);
		// PEP should return null
		assertEquals("PEP should return the correct attributeValue", retAttrValue, null);
	}
	
	@Test
	/*	
	 * Test ProcessDownstreamUsageRequest with a resource and a subject that matches n Piis
	 * The PEP should generate the XACML request
	 * The PEP should call the PDP to evaluate the request
	 * The PEP should for the response with decision equals to PERMIT 
	 * 		- call the PiiDao to retrieve the corresponding Pii
	 * 		- call the EventHandler to firePersonalDataSent
	 * 		- call the LoggerHandler to log the event
	 * The PEP should return the list containing the matching Piis
	 */
	public void testProcessDownstreamUsageRequestOneMatching(){	
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		resourcesList.add("file0.ext");
		resourcesList.add("file1.ext");
		resourcesList.add("file2.ext");
		String delete = "P0Y0M0DT0H0M120S";
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file0.ext");
		piiWitness.setAttributeValue("123_file0.ext");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		ResponseType mResponseType = new ResponseType();
		List<ResultType> mResults = new ArrayList<ResultType>();
		ResultType mResult = new ResultType();
		DecisionType mDecision = DecisionType.fromValue("Permit");
		mResult.setDecision(mDecision);
		mResult.setResourceId("0");
		StatusCodeType statusCode = new StatusCodeType();
		statusCode.setValue(StatusCodeType.STATUS_OK);
		StatusType status = new StatusType();
		status.setStatusCode(statusCode);
		mResult.setStatus(status);
		mResults.add(mResult);
		mResponseType.setResult(mResults);
		
		when(mPdp.evaluate((RequestType)anyObject())).thenReturn(mResponseType);
		when(mPiiDao.findObject(PIIType.class, (long) 0)).thenReturn(piiWitness);
		
		List<PIIType> returnedPiis = pep.processDownstreamUsageRequest("toto@sap.com", "file0.ext");
		
		// The PEP should generate the XACML request
		// Unable to test this directly, unable to mock
		
		// The PEP should call the PDP to evaluate the request
		verify(mPdp).evaluate((RequestType)anyObject());
		
		// The PEP should for each response with decision equals to PERMIT 
		// 		- call the PiiDao to retrieve the corresponding Pii
		verify(mPiiDao).findObject(PIIType.class, (long) 0);
		// 		- call the EventHandler to firePersonalDataSent
		IsSamePii isSamePii = new IsSamePii("file0.ext", "123_file0.ext", "0", piiWitness.getPolicySetOrPolicyItems());
		verify(mEventHandler).firePersonalDataSent(argThat(isSamePii), eq("toto@sap.com"));
		// 		- call the LoggerHandler to log the event
		verify(mLoggerHandler).logDownstreamUsage(argThat(isSamePii), eq("toto@sap.com"));
		
		// The PEP should return the list containing the matching Piis
		assertEquals(1, returnedPiis.size());
		assertThat(returnedPiis.get(0), new IsSamePii("file0.ext", "123_file0.ext", "0", piiWitness.getPolicySetOrPolicyItems()));
	}
	
	@Test
	/*	
	 * Test ProcessDownstreamUsageRequest with a resource and a subject that matches n Piis
	 * The PEP should generate the XACML request
	 * The PEP should call the PDP to evaluate the request
	 * The PEP should for each response with decision equals to PERMIT 
	 * 		- call the PiiDao to retrieve the corresponding Pii
	 * 		- call the EventHandler to firePersonalDataSent
	 * 		- call the LoggerHandler to log the event
	 * The PEP should return the list containing the matching Piis
	 */
	public void testProcessDownstreamUsageRequestNMatching(){	
		int n = 3;
		
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		for(int i=0; i<n; i++){
			resourcesList.add("file"+i+".ext");			
		}
		String delete = "P0Y0M0DT0H0M120S";
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);

		ResponseType mResponseType = new ResponseType();
		List<ResultType> mResults = new ArrayList<ResultType>();
		mResponseType.setResult(mResults);
		List<PIIType> piis = new ArrayList<PIIType>();
		
		for(int i=0; i<n; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file"+i+".ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner(""+i);
			piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
			piis.add(piiWitness);
			
			ResultType mResult = new ResultType();
			DecisionType mDecision = DecisionType.fromValue("Permit");
			mResult.setDecision(mDecision);
			mResult.setResourceId(""+i);
			StatusCodeType statusCode = new StatusCodeType();
			statusCode.setValue(StatusCodeType.STATUS_OK);
			StatusType status = new StatusType();
			status.setStatusCode(statusCode);
			mResult.setStatus(status);
			mResults.add(mResult);
		}
		
		when(mPdp.evaluate((RequestType)anyObject())).thenReturn(mResponseType);
		for(int i=0; i<n; i++){
			when(mPiiDao.findObject(PIIType.class, (long) i)).thenReturn(piis.get(i));
		}
		
		List<PIIType> returnedPiis = pep.processDownstreamUsageRequest("toto@sap.com", "file0.ext");
		
		// The PEP should generate the XACML request
		// Unable to test this directly, unable to mock
		
		// The PEP should call the PDP to evaluate the request
		verify(mPdp).evaluate((RequestType)anyObject());
		
		// The PEP should for each response with decision equals to PERMIT 
		for(int i=0; i<n; i++){
			// 		- call the PiiDao to retrieve the corresponding Pii
			verify(mPiiDao).findObject(PIIType.class, (long) i);
			// 		- call the EventHandler to firePersonalDataSent
			IsSamePii isSamePii = new IsSamePii("file"+i+".ext", "123_file"+i+".ext", ""+i, piis.get(i).getPolicySetOrPolicyItems());
			verify(mEventHandler).firePersonalDataSent(argThat(isSamePii), eq("toto@sap.com"));
			// 		- call the LoggerHandler to log the event
			verify(mLoggerHandler).logDownstreamUsage(argThat(isSamePii), eq("toto@sap.com"));
		}
		
		// The PEP should return the list containing the matching Piis
		assertEquals(n, returnedPiis.size());
		for(int i=0; i<n; i++){
			IsSamePii isSamePii = new IsSamePii("file"+i+".ext", "123_file"+i+".ext", ""+i, piis.get(i).getPolicySetOrPolicyItems());
			assertThat(returnedPiis.get(i), isSamePii);			
		}
	}
	
	@Test
	/*	
	 * Test ProcessDownstreamUsageRequest with any as a resource and a subject that matches n Piis
	 * The PEP should generate the XACML request
	 * The PEP should for each Pii call the PDP to evaluate the request
	 * The PEP should for each response with decision equals to PERMIT 
	 * 		- call the PiiDao to retrieve the corresponding Pii
	 * 		- call the EventHandler to firePersonalDataSent
	 * 		- call the LoggerHandler to log the event
	 * The PEP should return the list containing the matching Piis
	 */
	public void testProcessDownstreamUsageRequestAnyNMatching(){	
		int n = 3;
		
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		String delete = "P0Y0M0DT0H0M120S";
		
		List<PIIType> piis = new ArrayList<PIIType>();
		List<ResponseType> pdpResponses = new ArrayList<ResponseType>();
		for(int i=0; i<n; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file"+i+".ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner(""+i);
			resourcesList.clear();
			resourcesList.add("file"+i+".ext");
			AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
			piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
			piis.add(piiWitness);
			
			ResponseType mResponseType = new ResponseType();
			List<ResultType> mResults = new ArrayList<ResultType>();
			mResponseType.setResult(mResults);
			ResultType mResult = new ResultType();
			DecisionType mDecision = DecisionType.fromValue("Permit");
			mResult.setDecision(mDecision);
			mResult.setResourceId(""+i);
			StatusCodeType statusCode = new StatusCodeType();
			statusCode.setValue(StatusCodeType.STATUS_OK);
			StatusType status = new StatusType();
			status.setStatusCode(statusCode);
			mResult.setStatus(status);
			mResults.add(mResult);
			pdpResponses.add(mResponseType);
		}
		
		when(mPiiDao.getAllPII()).thenReturn(piis);
		when(mPdp.evaluate((RequestType)anyObject())).thenReturn(pdpResponses.get(0), pdpResponses.get(1), pdpResponses.get(2));
		for(int i=0; i<n; i++){
			when(mPiiDao.findObject(PIIType.class, (long) i)).thenReturn(piis.get(i));
		}
		
		List<PIIType> returnedPiis = pep.processDownstreamUsageRequest("toto@sap.com");
		
		// The PEP should generate the XACML request
		// Unable to test this directly, unable to mock
		
		// The PEP should call the PDP to evaluate the request
		verify(mPdp, times(n)).evaluate((RequestType)anyObject());
		
		// The PEP should for each response with decision equals to PERMIT 
		for(int i=0; i<n; i++){
			// 		- call the PiiDao to retrieve the corresponding Pii
			verify(mPiiDao).findObject(PIIType.class, (long) i);
			// 		- call the EventHandler to firePersonalDataSent
			IsSamePii isSamePii = new IsSamePii("file"+i+".ext", "123_file"+i+".ext", ""+i, piis.get(i).getPolicySetOrPolicyItems());
			verify(mEventHandler).firePersonalDataSent(argThat(isSamePii), eq("toto@sap.com"));
			// 		- call the LoggerHandler to log the event
			verify(mLoggerHandler).logDownstreamUsage(argThat(isSamePii), eq("toto@sap.com"));
		}
		
		// The PEP should return the list containing the matching Piis
		assertEquals(n, returnedPiis.size());
		for(int i=0; i<n; i++){
			IsSamePii isSamePii = new IsSamePii("file"+i+".ext", "123_file"+i+".ext", ""+i, piis.get(i).getPolicySetOrPolicyItems());
			assertThat(returnedPiis.get(i), isSamePii);			
		}
	}
	
	@Test
	/*	
	 * Test ProcessDownstreamUsageRequest with a resource and a subject that matches 0 Piis
	 * The PEP should generate the XACML request
	 * The PEP should call the PDP to evaluate the request
	 * The PEP should for each response with decision equals to PERMIT 
	 * 		- call the PiiDao to retrieve the corresponding Pii
	 * 		- call the EventHandler to firePersonalDataSent
	 * 		- call the LoggerHandler to log the event
	 * The PEP should return an empty list
	 */
	public void testProcessDownstreamUsageRequestNoMatching(){	
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		resourcesList.add("file0.ext");
		resourcesList.add("file1.ext");
		resourcesList.add("file2.ext");
		String delete = "P0Y0M0DT0H0M120S";
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
		
		/*List<PIIType> piis = new ArrayList<PIIType>();
		for(int i=0; i<3; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file"+i+".ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner((long) i);
			piiWitness.setPolicySetOrPolicy(convertToPolicy(spAttribute0));
			piis.add(piiWitness);
		}*/
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file0.ext");
		piiWitness.setAttributeValue("123_file0.ext");
		piiWitness.setOwner("0");
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		
		ResponseType mResponseType = new ResponseType();
		List<ResultType> mResults = new ArrayList<ResultType>();
		ResultType mResult = new ResultType();
		DecisionType mDecision = DecisionType.fromValue("Deny");
		mResult.setDecision(mDecision);
		mResult.setResourceId("0");
		StatusCodeType statusCode = new StatusCodeType();
		statusCode.setValue(StatusCodeType.STATUS_OK);
		StatusType status = new StatusType();
		status.setStatusCode(statusCode);
		mResult.setStatus(status);
		mResults.add(mResult);
		mResponseType.setResult(mResults);
		
		when(mPdp.evaluate((RequestType)anyObject())).thenReturn(mResponseType);
		when(mPiiDao.findObject(PIIType.class, (long) 0)).thenReturn(piiWitness);
		
		List<PIIType> returnedPiis = pep.processDownstreamUsageRequest("toto@sap.com", "file0.ext");
		
		// The PEP should generate the XACML request
		// Unable to test this directly, unable to mock
		
		// The PEP should call the PDP to evaluate the request
		verify(mPdp).evaluate((RequestType)anyObject());
		
		// The PEP should for each response with decision equals to PERMIT 
		// 		- call the PiiDao to retrieve the corresponding Pii
		verifyZeroInteractions(mPiiDao);
		// 		- call the EventHandler to firePersonalDataSent
		verifyZeroInteractions(mEventHandler);
		// 		- call the LoggerHandler to log the event
		verifyZeroInteractions(mLoggerHandler);
		
		// The PEP should return an empty list
		assertEquals(0, returnedPiis.size());
	}
}
