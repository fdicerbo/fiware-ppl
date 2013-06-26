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
package com.sap.research.primelife;

import java.util.List;

import javax.xml.bind.JAXBException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;

import org.junit.Assert;
import org.junit.Test;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.initializer.Initializer;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.RuleType;
import eu.primelife.ppl.policy.xacml.impl.PolicySetType;



public class TestRuleImport {

	@Test
	public void testRuleImport() throws SyntaxException, JAXBException {
		Initializer.getInstance();
		DaoImpl<RuleType> ruleDao = new DaoImpl<RuleType>();
		List<RuleType> rules = ruleDao.findObjects(RuleType.class);
		int rulesBefore = rules.size();
		
		DaoImpl<PolicySetType> psDao = new DaoImpl<PolicySetType>();
		List<PolicySetType> policies = psDao.findObjects(PolicySetType.class);
		int policySetsBefore = policies.size();
		
		DaoImpl<PolicyType> pDao = new DaoImpl<PolicyType>();
		List<PolicyType> ps = pDao.findObjects(PolicyType.class);
		int policiesBefore = ps.size();
		
		DaoImpl<PolicySetType> asDao = new DaoImpl<PolicySetType>();
		UnmarshallImpl unmarshaller = new UnmarshallImpl(AssertionType.class.getPackage());
		PolicySetType policy = (PolicySetType) unmarshaller.unmarshal(
				getClass().getResourceAsStream("/testRuleImport/testPrefs.xml"));
		asDao.persistObject(policy);

		rules = ruleDao.findObjects(RuleType.class);
		int rulesAfter = rules.size();
		policies = psDao.findObjects(PolicySetType.class);
		int policySetsAfter = policies.size();
		ps = pDao.findObjects(PolicyType.class);
		int policiesAfter = ps.size();
		
		Assert.assertTrue(rulesBefore < rulesAfter);
		Assert.assertTrue(policiesBefore < policiesAfter);
		Assert.assertTrue(policySetsBefore < policySetsAfter);
	}
}
