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
package com.sap.research.primelife.ds.pdp.evaluation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.RequestInformation;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.herasaf.xacml.core.targetMatcher.TargetMatcher;
import org.herasaf.xacml.core.targetMatcher.impl.TargetMatcherImpl;

import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.utils.ConverterFunctions;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.RuleType;
import eu.primelife.ppl.policy.xacml.impl.PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem;
import eu.primelife.ppl.policy.xacml.impl.PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem;
import eu.primelife.ppl.policy.xacml.impl.TargetType;

/**
 * The AccessControlUtils contain methods which use HERAS. 
 * 
 *
 */
public class AccessControlUtils {
	
	private static TargetMatcherImpl targetMatcher = new TargetMatcherImpl();
	
	/**
	 * Access control:
	 * Check the target elements of the policySetOrPolicy with HERAS against the request.	 * 
	 *  
	 * @param policySetOrPolicy - a list of PolicyType or PolicySetType objects from PPL
	 * @param request - the XACML request
	 * @return the decision (PERMIT, DENY, INDETERMINATE, NOT_APPLICABLE)
	 * @throws WritingException
	 * @throws SyntaxException
	 * @throws com.sap.research.primelife.exceptions.SyntaxException
	 * @throws JAXBException
	 */
	public DecisionType checkAccess(List<Object> policySetOrPolicy, RequestType request) 
		throws WritingException, SyntaxException, com.sap.research.primelife.exceptions.SyntaxException, JAXBException{
		
		// we evaluate request against policy repository associated with the PII
		SimplePDPFactory.useDefaultInitializers();
		PDP simplePDP = SimplePDPFactory.getSimplePDP();
		PolicyRepository repo = simplePDP.getPolicyRepository();
		// initialize policy repository
		for (Object obj : policySetOrPolicy) {
			Evaluatable evaluatable = null;
			
			if (obj instanceof PolicySetType) {
				evaluatable = ConverterFunctions.convertToHerasPolicySet(
						(PolicySetType) obj);
			}
			else if (obj instanceof PolicyType) {
				evaluatable = ConverterFunctions.convertToHerasPolicy(
						(PolicyType) obj);
			}
			else if (obj instanceof eu.primelife.ppl.policy.xacml.impl.PolicyType) {
				evaluatable = ConverterFunctions.convertToHerasPolicy((eu.primelife.ppl.policy.xacml.impl.PolicyType) obj);
			}
			repo.deploy(evaluatable);
		}
		ResponseCtx responseCtx = simplePDP.evaluate(new RequestCtx(request));
		DecisionType decision =	responseCtx.getResponse().getResults().get(0).getDecision();
		return decision;
	}
	
	/**
	 * Finds the applicable Rule element; for each PolicySet, Policy and Rule
	 * element it will match its Target against the request which is composed by:
	 * subject, resource and action.
	 *
	 * @param policyList	list with PolicySet or Policy element
	 * @return applicable Rule element
	 * @throws SyntaxException 
	 * @throws com.sap.research.primelife.exceptions.SyntaxException 
	 * @throws WritingException 
	 * @throws FileNotFoundException 
	 * @throws MissingAttributeException 
	 * @throws ProcessingException 
	 * @throws JAXBException
	 */
	public RuleType findApplicableRule(List<Object> policyList, RequestType request) throws FileNotFoundException,
			WritingException,
			com.sap.research.primelife.exceptions.SyntaxException,
			SyntaxException, ProcessingException, MissingAttributeException, JAXBException {
		
		RuleType rule = null;

		for (Object obj : policyList) {
			if (obj instanceof PolicySetType) {
				PolicySetType policySet = (PolicySetType) obj;

				if (matchTarget(request, policySet.getTarget())) {
					List<Object> childrenPolicyList = new ArrayList<Object>();

					for (PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem item :
						policySet.getPolicySetOrPolicyOrPolicySetIdReferenceItems()) {

						if (item.getItemPolicySet() instanceof PolicySetType) {
							PolicySetType policySetType =
								(PolicySetType) item.getItemPolicySet();
							childrenPolicyList.add(policySetType);
						}
						else if (item.getItemPolicy() instanceof PolicyType) {
							PolicyType policyType =
								(PolicyType) item.getItemPolicy();
							childrenPolicyList.add(policyType);
						}

						RuleType ruleTemp =
							findApplicableRule(childrenPolicyList, request);

						if (ruleTemp != null)
							rule = ruleTemp;
					}
				}
			}
			else if (obj instanceof PolicyType) {
				PolicyType policyType = (PolicyType) obj;

				if (matchTarget(request, policyType.getTarget())) {
					for (PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item :
							policyType.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()) {
						if (item.getItemRule() instanceof RuleType) {
							RuleType ruleType = (RuleType) item.getItemRule();

							if (matchTarget(request, ruleType.getTarget()))
								rule = ruleType;
						}
					}
				}
			}/*else if (obj instanceof eu.primelife.ppl.policy.xacml.impl.PolicyType) {
				eu.primelife.ppl.policy.xacml.impl.PolicyType policyType = (eu.primelife.ppl.policy.xacml.impl.PolicyType) obj;
				
				if(matchTarget(request, policyType.getTarget())){
					for (PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item : 
						policyType.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()) {
						if (item.getItemRule() instanceof eu.primelife.ppl.policy.xacml.impl.RuleType) {
							eu.primelife.ppl.policy.xacml.impl.RuleType ruleType = (eu.primelife.ppl.policy.xacml.impl.RuleType) item.getItemRule();
	
							if (matchTarget(request, ruleType.getTarget()))
								rule = ruleType;
						}
					}
				}
			}*/
		}

		return rule;
	}

	/**
	 * Converts Target element from PPL schema to HERAS schema and matches
	 * it against the request using HERAS {@link TargetMatcher}.
	 *
	 * @param request	HERAS request
	 * @param target	Target element in PPL schema format
	 *
	 * @return	result of matching (true if the target is applicable)
	 * @throws JAXBException
	 */
	private static boolean matchTarget(RequestType request, TargetType target)
			throws WritingException,
			com.sap.research.primelife.exceptions.SyntaxException,
			FileNotFoundException, SyntaxException, ProcessingException,
			MissingAttributeException, JAXBException {
		
		//converting the target from PPL TargetType to HERAS TargetType
		org.herasaf.xacml.core.policy.impl.TargetType herasTarget = ConverterFunctions.fromPPLTargetToHerasTarget(target);
		return targetMatcher.match(request, herasTarget, new RequestInformation(null));
	}

}
