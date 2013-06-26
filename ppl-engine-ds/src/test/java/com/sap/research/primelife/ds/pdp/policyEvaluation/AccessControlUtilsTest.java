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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.ds.pdp.evaluation.AccessControlUtils;
import com.sap.research.primelife.ds.pdp.request.DataSubjectPDPRequest;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.xacml.impl.RuleType;

public class AccessControlUtilsTest {

	private static UnmarshallImpl unmarshallerPrime;
	private static AccessControlUtils accessControlUtils = new AccessControlUtils();
	
	@BeforeClass
	public static void setUp() throws JAXBException {
		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
	}
	
	@Test
	public void testCheckAccess() throws SyntaxException, JAXBException, WritingException, org.herasaf.xacml.core.SyntaxException, IOException {
		PDPRequest req = readPolicy("/accessControlUtilsTest/demo_policy.xml", "dummy");
		RequestType request = req.createXacmlRequest("http://www.example.org/names#user_name");
		PolicyType policy1 = (PolicyType) unmarshallerPrime.unmarshal(
				getClass().getResourceAsStream("/accessControlUtilsTest/demo_preferences.xml"));
		
		List<Object> policySetOrPolicy = new LinkedList<Object>();// preferenceGroup from file
		policySetOrPolicy.add(policy1);
		
		DecisionType decision = accessControlUtils.checkAccess(policySetOrPolicy, request);
		Assert.assertEquals(DecisionType.DENY, decision);
	}

	@Test
	public void testFindApplicableRule() throws WritingException, SyntaxException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, JAXBException, IOException {
		
		PDPRequest req = readPolicy("/accessControlUtilsTest/demo_policy.xml", "dummy");
		RequestType request = req.createXacmlRequest("http://www.example.org/names#user_name");
		PolicyType policy1 = (PolicyType) unmarshallerPrime.unmarshal(
				getClass().getResourceAsStream("/accessControlUtilsTest/demo_preferences.xml"));
		PolicyType policy2 = (PolicyType) unmarshallerPrime.unmarshal(
				getClass().getResourceAsStream("/accessControlUtilsTest/test_applicable_policy.xml"));
		
		List<Object> policySetOrPolicy = new LinkedList<Object>();// preferenceGroup from file
		policySetOrPolicy.add(policy1);
		
		RuleType rule = accessControlUtils.findApplicableRule(policySetOrPolicy, request);
		Assert.assertEquals("#username", rule.getRuleId());
		
		policySetOrPolicy.clear();
		policySetOrPolicy.add(policy2);
		request = req.createXacmlRequest("http://www.w3.org/2006/vcard/ns#email");
		rule = accessControlUtils.findApplicableRule(policySetOrPolicy, request);
		Assert.assertEquals("#loginPreferences", rule.getRuleId());
		
		
	}
	
	private PDPRequest readPolicy(String fileName, String preferenceGroup) throws IOException, SyntaxException, JAXBException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(fileName)));

		while (reader.ready())
			sb.append(reader.readLine());

		PDPRequest request = new DataSubjectPDPRequest(sb.toString(), preferenceGroup);
		return request;
	}

}
