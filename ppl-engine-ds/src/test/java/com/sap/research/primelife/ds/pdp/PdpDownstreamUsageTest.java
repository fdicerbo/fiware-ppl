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
package com.sap.research.primelife.ds.pdp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;

import com.sap.research.primelife.dao.DaoInitializer;
import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pdp.request.DownstreamPDPRequestForNames;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicyType;

public class PdpDownstreamUsageTest {

	private static UnmarshallImpl unmarshallerPrime;
	private static PiiDao piiDao;
	private static PolicyDao polDao;
	@SuppressWarnings("unused")
	private static final String EMAIL_ATTRIBUTE_NAME = "http://www.w3.org/2006/vcard/ns#email";
	@SuppressWarnings("unused")
	private static final String DOWNSTREAM_SP = "/pdpDownstreamUsageTest/DownstreamStickyPolicy.xml";

	@BeforeClass
	public static void setUpBeforeClass() throws JAXBException {
		// clear all PIIs with the specific attribute name
		DaoInitializer.getInstance();
		piiDao =  new PiiDao();
		polDao = new PolicyDao();
		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
		piiDao.deleteAllPii();
		polDao.deleteAllPreferenceGroups();
		new PolicyDao().deleteAllPreferenceGroups();
	}

//	@Test
//	public void testEnforceDownstreamUsage() throws IOException,
//			SyntaxException, org.herasaf.xacml.core.SyntaxException,
//			ProcessingException, MissingAttributeException, WritingException,
//			DatatypeConfigurationException, JAXBException, ValidationException, MissingPreferenceGroupException {
//		String attrValue1 = "alice@example.com";
//		String attrValue2 = "bob@example.com";
//		addPii(EMAIL_ATTRIBUTE_NAME, attrValue1, DOWNSTREAM_SP);
//		addPii(EMAIL_ATTRIBUTE_NAME, attrValue2, DOWNSTREAM_SP);
//		PDPRequest pepRequest =
//			createPdpRequest("/pdpDownstreamUsageTest/DownstreamAssertion.xml");
//		
//		PolicyType defaultPolicy = readPolicyPrefs("/pdpDownstreamUsageTest/DefaultServerPrefs.xml");
//		polDao.addAsPreferenceGroup(defaultPolicy);
//		
//		PDP pdp = new PDP();
//		PdpResponse pdpResponse = pdp.evaluate(pepRequest, null);
//		ClaimsType claims = pdpResponse.toClaim();
//
//		Map<String, AttributeType> spMap = new HashMap<String, AttributeType>();
//		List<String> spIdList = new ArrayList<String>();
//		List<String> attrValues = new ArrayList<String>();
//
//		assertFalse(claims.getClaim().isEmpty());
//
//		for (ClaimType claim : claims.getClaim()) {
//			assertFalse(claim.getAssertion().isEmpty());
//
//			for (AssertionType assertion : claim.getAssertion()) {
//				assertFalse(assertion
//						.getStatementOrAuthnStatementOrAuthzDecisionStatement()
//						.isEmpty());
//
//				for (StatementAbstractType statement : assertion
//						.getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
//					if (statement instanceof StickyPolicyStatementType) {
//						StickyPolicyStatementType spStatement = (StickyPolicyStatementType) statement;
//
//						for (AttributeType attr : spStatement.getAttribute()) {
//							spMap.put(attr.getID(), attr);
//						}
//					}
//					else if (statement instanceof AttributeStatementType) {
//						AttributeStatementType attrStatement = (AttributeStatementType) statement;
//	
//						for (AttributeStatementTypeAttributeOrEncryptedAttributeItem attr : attrStatement
//								.getAttributeOrEncryptedAttributeItems()) {
//							oasis.names.tc.saml._2_0.assertion.AttributeType itemAttr =
//								attr.getItemAttribute();
//							eu.primelife.ppl.claims.impl.AttributeType claimsAttributeType =
//								(eu.primelife.ppl.claims.impl.AttributeType) itemAttr;
//							String stickyPolicyId = claimsAttributeType.getStickyPolicyId();
//							spIdList.add(stickyPolicyId);
//
//							String attrName = itemAttr.getName();
//							assertEquals(EMAIL_ATTRIBUTE_NAME, attrName);
//
//							String attrValue = ((String) itemAttr
//									.getAttributeValue().get(0)).trim();
//							attrValues.add(attrValue);
//						}
//					}
//				}
//			}
//		}
//
//		assertEquals(2, spIdList.size());
//		assertEquals(2, attrValues.size());
//		assertTrue(attrValues.contains(attrValue1));
//		assertTrue(attrValues.contains(attrValue2));
//
//		for (String spId : spIdList) {
//			assertTrue(spMap.containsKey(spId));
//			assertTrue(spMap.get(spId).isMatching());
//		}
//	}

	@SuppressWarnings("unused")
	private void addPii(String attributeName, String attributeValue,
			String preferencesPath) throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, IOException {
		// persist PII and its preferences
		PIIType pii = new PIIType();
		pii.setAttributeName(attributeName);
		pii.setAttributeValue(attributeValue);
		PolicyType policy = (PolicyType) unmarshallerPrime.unmarshal(
				getClass().getResourceAsStream(preferencesPath));
		pii.getPolicySetOrPolicy().add(policy);
		piiDao.persistObject(pii);
	}

	@SuppressWarnings("unused")
	private PDPRequest createPdpRequest(String assertionPath)
			throws IOException, SyntaxException, JAXBException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(assertionPath)));
		
		while (reader.ready())
			sb.append(reader.readLine());

		return new DownstreamPDPRequestForNames(sb.toString(), "default");
	}
	
	@SuppressWarnings("unused")
	private PolicyType readPolicyPrefs(String path) throws JAXBException, SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
	}

}
