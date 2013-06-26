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
import java.util.List;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.exceptions.WritingException;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;
import eu.primelife.ppl.policy.impl.RuleType;

/**
 * (Formerly called attribute enforcement)
 * Responsible for access control and policy selection.
 * 
 * @Version 0.1
 * @Date May 21, 2010
 * 
 */
public class PolicyEvaluation {

	private static final Logger LOGGER =
		LoggerFactory.getLogger(PolicyEvaluation.class);
	private static AccessControlUtils accessControlUtils = new AccessControlUtils();
	
	/**
	 * Checks the access control of the PII and selects the applicable policy for the matching
	 * @param pii - the requested PII
	 * @param request - the xacml request
	 * @param policyQueryStrategy - the strategy for finding the right policy
	 * @return an EvaluationResponse
	 * @throws JAXBException 
	 * @throws com.sap.research.primelife.exceptions.SyntaxException 
	 * @throws SyntaxException 
	 * @throws WritingException 
	 * @throws MissingAttributeException 
	 * @throws ProcessingException 
	 * @throws FileNotFoundException 
	 */
	public static EvaluationResponse evaluate (PIIType pii, RequestType request, IPolicyQueryStrategy policyQueryStrategy) throws WritingException, SyntaxException, com.sap.research.primelife.exceptions.SyntaxException, JAXBException, FileNotFoundException, ProcessingException, MissingAttributeException {
		// as default we use all policies associated with PII
		List<Object> policySetOrPolicy = policyQueryStrategy.executeQuery(pii);

		// if the policySetOrPolicy is null, the response will always be deny because e.g. downstream usage is not allowed
		// if no policy was found, then the list is empty
		if (policySetOrPolicy == null) {
				return new EvaluationResponse(DecisionType.DENY);
		}
		
		DecisionType decision = accessControlUtils.checkAccess(policySetOrPolicy, request);

		EvaluationResponse response = new EvaluationResponse(decision);

		switch (decision) {
		case PERMIT:
			LOGGER.info("HERAS PDP enforcement decision is PERMIT");

			RuleType rule = accessControlUtils.findApplicableRule(policySetOrPolicy, request);			

			if (rule == null) {
				LOGGER.info("Applicable preferences not found");
				return new EvaluationResponse(DecisionType.INDETERMINATE);
			}

			DataHandlingPreferencesType preferences = rule.getDataHandlingPreferences();
			response.setPreferences(preferences);
			response.setAttributeValue(pii.getAttributeValue());
			break;
		case DENY:
			LOGGER.info("HERAS PDP enforcement decision is DENY");
			break;
		case INDETERMINATE:
			LOGGER.info("HERAS PDP enforcement decision is INDETERMINATE");
			break;
		case NOT_APPLICABLE:
			LOGGER.info("HERAS PDP enforcement decision is NOT_APPLICABLE");
			break;
		}

		return response;

	}

}
