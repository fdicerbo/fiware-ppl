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
package com.sap.research.primelife.utils;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.policy.PolicyConverter;
import org.herasaf.xacml.core.policy.impl.TargetType;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallFactory;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.RuleType;
import eu.primelife.ppl.policy.xacml.impl.PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem;
import eu.primelife.ppl.policy.xacml.impl.PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem;

/**
 * Class contain different converter functions
 * 
 * 
 * @Version 0.1
 * @Date Jun 29, 2010
 * 
 */
public class ConverterFunctions {

	/**
	 * Convert PPL Target to Heras Target. This is by meaning of canonical format and types.
	 * @param PPL Target
	 * 		PPL Target
	 * @return
	 * 		Heras Target
	 * @throws WritingException
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws JAXBException
	 */
	public static TargetType fromPPLTargetToHerasTarget(
			eu.primelife.ppl.policy.xacml.impl.TargetType pPLTarget)
			throws WritingException, SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, JAXBException {
		MarshallImpl marshaller = MarshallFactory.createMarshallImpl(eu.primelife.ppl.policy.xacml.impl.TargetType.class.getPackage(), false);  
//			new MarshallImpl(eu.primelife.ppl.policy.xacml.impl.TargetType.class.getPackage(), false);

		ObjectFactory ofPrimelife = new ObjectFactory();
		PolicyType policyForTarget = ofPrimelife.createPolicyType();
		eu.primelife.ppl.policy.xacml.impl.TargetType targetCopy = new eu.primelife.ppl.policy.xacml.impl.TargetType();

		targetCopy.setActions(pPLTarget.getActions());
		targetCopy.setEnvironments(pPLTarget.getEnvironments());
		targetCopy.setResources(pPLTarget.getResources());
		targetCopy.setSubjects(pPLTarget.getSubjects());
//		targetCopy.setHjid(null);

//		policyForTarget.setHjid(null);

//		targetCopy.getSubjects().setHjid(null);

		// modify objects being iterated, delete the 'hjid' and other unuseless  attribute
//		ListIterator<SubjectType> litrSubject =
//			((List<SubjectType>) targetCopy.getSubjects().getSubject())
//			.listIterator();

//		while (litrSubject.hasNext()) {
//			SubjectType subject = (SubjectType) litrSubject.next(); 
//			subject.setHjid(null);

//			ListIterator<SubjectMatchType> litrSubjectMatch =
//				subject.getSubjectMatch().listIterator();

//			while (litrSubjectMatch.hasNext()) {
//				SubjectMatchType subjectMatch =
//					(SubjectMatchType) litrSubjectMatch.next(); 
//				subjectMatch.setHjid(null);

//				subjectMatch.getAttributeValue().setHjid(null);
//				subjectMatch.getSubjectAttributeDesignator().setHjid(null);
//				subjectMatch.getSubjectAttributeDesignator().setMustBePresent(null);
//			}
//		}

		policyForTarget.setTarget(targetCopy);
		policyForTarget.setRuleCombiningAlgId("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides");
		policyForTarget.setPolicyId("policyConvertion");

		TargetType result = null;
		StringWriter out = new StringWriter();

		marshaller.marshal(ofPrimelife.createPolicy(policyForTarget), out);

		String modifiedPolicy = out.toString().replaceAll("ppl:Policy", "xacml:Policy");

		StringReader in = new StringReader(modifiedPolicy);

		result = PolicyConverter.unmarshal(in).getTarget();

		return result;
	}

	/**
	 * Transforms PolicySet element from PPL schema to conform with pure XACML
	 * schema (e.g. removes Data Handling Policy and Data Handling Preferences).
	 *
	 * @param pplPolicySet	PolicySet element from PPL schema
	 * @return	PolicySet element from HERAS XACML
	 * @throws JAXBException
	 */
	public static org.herasaf.xacml.core.policy.impl.PolicySetType convertToHerasPolicySet(
			PolicySetType pplPolicySet) throws WritingException,
			org.herasaf.xacml.core.SyntaxException, SyntaxException,
			JAXBException {
		ObjectFactory objectFactory = new ObjectFactory();
		MarshallImpl marshaller = MarshallFactory.createMarshallImpl(PolicySetType.class.getPackage(), false); 
//			new MarshallImpl(PolicySetType.class.getPackage(), false);
		UnmarshallImpl unmarshaller = UnmarshallFactory.createUnmarshallImpl(PolicySetType.class.getPackage());
//			new UnmarshallImpl(PolicySetType.class.getPackage());
		StringWriter writer = new StringWriter();

		// PPL PolicySet is marshalled and unmarshalled, just to create ideal copy
		// (because transformation will be removing PPL-specific parts of PolicySet,
		// and that changes shouldn't affect original Policy)
		marshaller.marshal(objectFactory.createPolicySet(pplPolicySet), writer);
		Reader reader = new StringReader(writer.toString());
		pplPolicySet = (PolicySetType) unmarshaller.unmarshal(reader);

		// remove PPL-specific elements
		convertToHeras(pplPolicySet);

		// marshal to string again
		writer = new StringWriter();
		marshaller.marshal(objectFactory.createPolicySet(pplPolicySet), writer);

		// change namespace perfix of PPL elements
		String modifiedPolicySet = writer.toString()
			.replaceAll("ppl:PolicySet", "xacml:PolicySet")
			.replaceAll("ppl:Policy", "xacml:Policy")
			.replaceAll("ppl:Rule", "xacml:Rule");
		reader = new StringReader(modifiedPolicySet);

		return (org.herasaf.xacml.core.policy.impl.PolicySetType)
				PolicyConverter.unmarshal(reader);
	}

	/**
	 * Recursively converts all nested PolicySet or Policy elements to conform
	 * with HERAS XACML structure (e.g. removes Data Handling Preferences and
	 * Data Handling Policies).
	 *
	 * @param pplPolicySet	PolicySet element converted to HERAS XACML
	 */
	private static void convertToHeras(
			eu.primelife.ppl.policy.impl.PolicySetType pplPolicySet) {
		pplPolicySet.setDataHandlingPolicy(null);
		pplPolicySet.setDataHandlingPreferences(null);
		pplPolicySet.setStickyPolicy(null);

		for (PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem item :
				pplPolicySet.getPolicySetOrPolicyOrPolicySetIdReferenceItems()) {
			PolicyType policy = (PolicyType) item.getItemPolicy();

			if (policy != null)
				convertToHeras(policy);

			eu.primelife.ppl.policy.impl.PolicySetType policySet =
				(eu.primelife.ppl.policy.impl.PolicySetType) item.getItemPolicySet();

			if (policySet != null)
				convertToHeras(policySet);
		}
	}

	/**
	 * Transforms Policy element from PPL schema to conform with pure XACML
	 * schema (e.g. removes Data Handling Policy and Data Handling Preferences).
	 *
	 * @param pplPolicy	Policy element from PPL schema
	 * @return	Policy element from HERAS XACML
	 * @throws JAXBException
	 */
	public static org.herasaf.xacml.core.policy.impl.PolicyType convertToHerasPolicy(PolicyType pplPolicy) throws WritingException,
																						org.herasaf.xacml.core.SyntaxException, SyntaxException,
																						JAXBException {
		ObjectFactory objectFactory = new ObjectFactory();
		MarshallImpl marshaller = MarshallFactory.createMarshallImpl(PolicyType.class.getPackage(), false);
//			new MarshallImpl(PolicyType.class.getPackage(), false);
		UnmarshallImpl unmarshaller = UnmarshallFactory.createUnmarshallImpl(PolicyType.class.getPackage());
//			new UnmarshallImpl(PolicyType.class.getPackage());
		StringWriter writer = new StringWriter();

		// PPL Policy is marshalled and unmarshalled, just to create ideal copy
		// (because transformation will be removing PPL-specific parts of Policy,
		// and that changes shouldn't affect original Policy)
		marshaller.marshal(objectFactory.createPolicy(pplPolicy), writer);
		Reader reader = new StringReader(writer.toString());
		pplPolicy = (PolicyType) unmarshaller.unmarshal(reader);

		// remove PPL-specific elements
		convertToHeras(pplPolicy);

		// marshal to string again
		writer = new StringWriter();
		marshaller.marshal(objectFactory.createPolicy(pplPolicy), writer);

		// change namespace perfix of PPL elements
		String modifiedPolicy = writer.toString()
			.replaceAll("ppl:Policy", "xacml:Policy")
			.replaceAll("ppl:Rule", "xacml:Rule");
		reader = new StringReader(modifiedPolicy);

		return (org.herasaf.xacml.core.policy.impl.PolicyType) PolicyConverter
				.unmarshal(reader);
	}

	/**
	 * Converts Policy and nested Rule elements to conform
	 * with HERAS XACML structure (e.g. removes Data Handling Preferences and
	 * Data Handling Policies).
	 *
	 * @param pplPolicy	Policy element converted to HERAS XACML
	 */
	private static void convertToHeras(PolicyType pplPolicy) {
		pplPolicy.setDataHandlingPolicy(null);
		pplPolicy.setDataHandlingPreferences(null);
		pplPolicy.setStickyPolicy(null);

		for (PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item :
				pplPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()) {
			RuleType rule = (RuleType) item.getItemRule();

			if (rule != null) {
				rule.setDataHandlingPolicy(null);
				rule.setDataHandlingPreferences(null);
				rule.setStickyPolicy(null);
			}
		}
	}

	/**
	 * Transform a eu.primelife.ppl.policy.xacml.impl.PolicyType into org.herasaf.xacml.core.policy.impl.PolicyType
	 * @param eu.primelife.ppl.policy.xacml.impl.PolicyType, a XACML Policy
	 * @return
	 * @throws WritingException 
	 * @throws SyntaxException 
	 * @throws JAXBException 
	 * @throws org.herasaf.xacml.core.SyntaxException 
	 */
	public static org.herasaf.xacml.core.policy.impl.PolicyType convertToHerasPolicy(eu.primelife.ppl.policy.xacml.impl.PolicyType policy) throws WritingException, SyntaxException, JAXBException, org.herasaf.xacml.core.SyntaxException {
		
		eu.primelife.ppl.policy.xacml.impl.ObjectFactory objectFactory = new eu.primelife.ppl.policy.xacml.impl.ObjectFactory();
		MarshallImpl marshaller = MarshallFactory.createMarshallImpl(eu.primelife.ppl.policy.xacml.impl.PolicyType.class.getPackage(), false);
		StringWriter writer = new StringWriter();

		// The transformation is trivial just marshal the policy and then unmarshal into the herasaf beans
		marshaller.marshal(objectFactory.createPolicy(policy), writer);
		String str = writer.toString();
		Reader reader = new StringReader(str);

		org.herasaf.xacml.core.policy.impl.PolicyType hpolicy = (org.herasaf.xacml.core.policy.impl.PolicyType) PolicyConverter.unmarshal(reader);
		
		return hpolicy;
	}


}
