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
package com.sap.research.primelife.ds.pdp.policyEvaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.PiiDaoHelper;
import com.sap.research.primelife.dao.DaoInitializer;
import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pdp.evaluation.EvaluationResponse;
import com.sap.research.primelife.ds.pdp.evaluation.PolicyEvaluation;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPreferenceGroupStrategy;
import com.sap.research.primelife.ds.pdp.request.DataSubjectPDPRequest;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicyType;

public class PolicyEvaluationTest {

	private static UnmarshallImpl unmarshallerPrime;
	private static PiiDao piiDao;
	private static PolicyDao policyDao;
	private static PiiDaoHelper piiDaoHelper;

	@BeforeClass
	public static void setUp() throws JAXBException {
		DaoInitializer.getInstance();
		piiDao = new PiiDao();
		piiDaoHelper = new PiiDaoHelper(piiDao);
		policyDao = new PolicyDao();
		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
		piiDao.deleteAllPii();
		policyDao.deleteAllPreferenceGroups();
	}

	@Test
	public void testEnforce1() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, IOException,
			JAXBException, MissingPreferenceGroupException {
		String attributeName = "http://www.example.org/names#user_name";
		String attributeValue = "john_doe";
		EvaluationResponse response = enforcePreferences(attributeName,
				attributeValue, "/policyEvaluationTest/HerasPreferences1.xml",
				"/policyEvaluationTest/HerasAssertion.xml");

		assertEquals(DecisionType.PERMIT, response.getDecision());
		assertNotNull(response.getPreferences());
		String enforcedPiiValue = response.getAttributeValue();
		assertNotNull(enforcedPiiValue);
		assertEquals(attributeValue, enforcedPiiValue);
	}

	@Test
	public void testEnforce2() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, IOException,
			JAXBException, MissingPreferenceGroupException {
		String attributeName = "http://www.w3.org/2006/vcard/ns#email";
		String attributeValue = "john.doe@example.com";
		EvaluationResponse response = enforcePreferences(attributeName,
				attributeValue, "/policyEvaluationTest/HerasPreferences2.xml",
				"/policyEvaluationTest/HerasAssertion.xml");

		assertEquals(DecisionType.NOT_APPLICABLE, response.getDecision());
		assertNull(response.getPreferences());
		assertNull(response.getAttributeValue());
	}

	private EvaluationResponse enforcePreferences(String attributeName,
			String attributeValue, String preferencesPath, String assertionPath)
			throws SyntaxException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException, WritingException,
			IOException, JAXBException, MissingPreferenceGroupException {
		// persist PII and its preferences
		PolicyType policy = (PolicyType) unmarshallerPrime.unmarshal(getClass()
				.getResourceAsStream(preferencesPath));
		policy.setPolicyId(preferencesPath);
		policyDao.addAsPreferenceGroup(policy);
		PIIType pii = piiDaoHelper.createOrUpdatePII(attributeName, attributeValue);

		// get SAML assertion with DataSubjectID value
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(assertionPath)));

		while (reader.ready())
			sb.append(reader.readLine());

		PDPRequest pepRequest = new DataSubjectPDPRequest(sb.toString(),
				preferencesPath);

		RequestType xacmlRequest = pepRequest.createXacmlRequest(attributeName);

		EvaluationResponse response = PolicyEvaluation.evaluate(pii,
				xacmlRequest, new PolicyByPreferenceGroupStrategy(
						preferencesPath));

		return response;
	}

	@Test
	public void testSelectPreferenceGroups() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, IOException,
			JAXBException, MissingPreferenceGroupException {
		String attributeName = "http://www.example.org/names#user_name2";
		String attributeValue = "john_doe";

		PolicyType policy1 = (PolicyType) unmarshallerPrime
				.unmarshal(getClass().getResourceAsStream(
						"/policyEvaluationTest/demo_preferences.xml"));
		PolicyType policy2 = (PolicyType) unmarshallerPrime
				.unmarshal(getClass().getResourceAsStream(
						"/policyEvaluationTest/demo_preferences2.xml"));

		PIIType pii = piiDaoHelper.createOrUpdatePII(attributeName, attributeValue);
		policyDao.addAsPreferenceGroup(policy1);
		policyDao.addAsPreferenceGroup(policy2);

		RequestType xacmlRequest = PDPRequest.createDummyXacmlRequest(
				"http://store.example.com", attributeName);

		EvaluationResponse response1 = PolicyEvaluation
				.evaluate(pii, xacmlRequest,
						new PolicyByPreferenceGroupStrategy("prefGroup1"));
		EvaluationResponse response2 = PolicyEvaluation
				.evaluate(pii, xacmlRequest,
						new PolicyByPreferenceGroupStrategy("prefGroup2"));
		Assert.assertEquals(DecisionType.DENY, response1.getDecision());
		Assert.assertEquals(DecisionType.PERMIT, response2.getDecision());
	}

	@AfterClass
	public static void tearDown() {
		piiDao.deleteAllPii();
		policyDao.deleteAllPreferenceGroups();
	}

}
