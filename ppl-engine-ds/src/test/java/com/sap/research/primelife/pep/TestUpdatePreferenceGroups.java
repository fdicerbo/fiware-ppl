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
package com.sap.research.primelife.pep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pdp.matching.HandyAuthorizationsSet;
import com.sap.research.primelife.ds.pep.PEP;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.RuleType;

public class TestUpdatePreferenceGroups {

	private static PolicyDao dao;

	private PolicyType readPolicyPrefs(String path) throws JAXBException,
			SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(
				PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(
				path));
	}

	@BeforeClass
	public static void setUp() {
		dao = new PolicyDao();
		dao.deleteAllPreferenceGroups();
	}

	@Test
	public void testUpdatePreferenceGroup() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		String policy = readFile("/testUpdatePrefGroups/demo_policy.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences.xml");
		dao.addAsPreferenceGroup(preferences);
		eu.primelife.ppl.policy.xacml.impl.PolicyType newPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup1");
		RuleType rule = (RuleType) newPolicy
				.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()
				.get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule
				.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertFalse(handy.getAuthzForDownstreamUsage().getIsAllowed());
		Assert.assertEquals(2, handy.getAuthzForPurpose().getPurposes().size());

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup1", null);

		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup1");
		rule = (RuleType) changedPolicy
				.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()
				.get(0).getItemRule();
		handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences()
				.getAuthorizationsSet());
		Assert.assertTrue(handy.getAuthzForDownstreamUsage().getIsAllowed());
		Assert.assertEquals(3, handy.getAuthzForPurpose().getPurposes().size());
	}

	@Test
	public void testUpdatePreferenceGroup2() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		// test the case that the policy is null
		// the preferences must not be updated
		String policy = readFile("/testUpdatePrefGroups/demo_policy2.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences.xml");
		dao.addAsPreferenceGroup(preferences);

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup1", null);

		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup1");
		RuleType rule = (RuleType) changedPolicy
				.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()
				.get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule
				.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertFalse(handy.isUnknown());
		Assert.assertEquals(2, handy.getAuthzForPurpose().getPurposes().size());
	}

	@Test
	public void testUpdatePreferenceGroup3() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		// test the case that the policy is empty
		String policy = readFile("/testUpdatePrefGroups/demo_policy3.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences.xml");
		dao.addAsPreferenceGroup(preferences);

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup1", null);

		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup1");
		RuleType rule = (RuleType) changedPolicy
				.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()
				.get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule
				.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertFalse(handy.isUnknown());
		Assert.assertEquals(2, handy.getAuthzForPurpose().getPurposes().size());

	}

	@Test
	public void testUpdatePreferenceGroup4() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		// Test the case that the preferences are unknown, but the policy is set
		String policy = readFile("/testUpdatePrefGroups/demo_policy.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences2.xml");
		dao.addAsPreferenceGroup(preferences);
		eu.primelife.ppl.policy.xacml.impl.PolicyType newPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup2");
		RuleType rule = (RuleType) newPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertTrue(handy.isUnknown());

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup2", null);

		// after updating, the preference should contain the policy values
		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup2");
		rule = (RuleType) changedPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertTrue(handy.getAuthzForDownstreamUsage().getIsAllowed());
		Assert.assertEquals(3, handy.getAuthzForPurpose().getPurposes().size());
	}
	
	@Test
	public void testUpdatePreferenceGroup5() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		//Test that the preference has no downstream element and no useForPurpose element
		
		String policy = readFile("/testUpdatePrefGroups/demo_policy.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences3.xml");
		dao.addAsPreferenceGroup(preferences);
		eu.primelife.ppl.policy.xacml.impl.PolicyType newPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup3");
		RuleType rule = (RuleType) newPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertFalse(handy.getAuthzForDownstreamUsage().isDefined());
		Assert.assertFalse(handy.getAuthzForPurpose().isDefined());

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup3", null);

		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup3");
		rule = (RuleType) changedPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertTrue(handy.getAuthzForDownstreamUsage().getIsAllowed());
		Assert.assertEquals(3, handy.getAuthzForPurpose().getPurposes().size());
	}
	
	@Test
	public void testUpdatePreferenceGroup6() throws IOException, JAXBException,
			SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, DatatypeConfigurationException,
			MissingPreferenceGroupException {
		//Test that the preference has no downstream element and the policy does not allow dsu
		
		String policy = readFile("/testUpdatePrefGroups/demo_policy4.xml");
		PolicyType preferences = readPolicyPrefs("/testUpdatePrefGroups/demo_preferences3.xml");
		dao.addAsPreferenceGroup(preferences);
		eu.primelife.ppl.policy.xacml.impl.PolicyType newPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup3");
		RuleType rule = (RuleType) newPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		HandyAuthorizationsSet handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertFalse(handy.getAuthzForDownstreamUsage().isDefined());
		Assert.assertFalse(handy.getAuthzForPurpose().isDefined());

		PEP pep = new PEP();
		pep.updatePreferenceGroup(policy, "prefGroup3", null);

		eu.primelife.ppl.policy.xacml.impl.PolicyType changedPolicy = (eu.primelife.ppl.policy.xacml.impl.PolicyType) dao
				.getPreferenceGroupPolicy("prefGroup3");
		rule = (RuleType) changedPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems().get(0).getItemRule();
		handy = new HandyAuthorizationsSet(rule.getDataHandlingPreferences().getAuthorizationsSet());
		Assert.assertTrue(handy.getAuthzForDownstreamUsage().isDefined());
		Assert.assertFalse(handy.getAuthzForDownstreamUsage().getIsAllowed());
		Assert.assertEquals(3, handy.getAuthzForPurpose().getPurposes().size());
	}

	private String readFile(String path) throws IOException {
		InputStream in = getClass().getResourceAsStream(path);
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}

}
