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
package com.sap.research.primelife.ds.pdp.provisionalAction;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.evaluation.EvaluationResponse;
import com.sap.research.primelife.ds.pdp.evaluation.PolicyEvaluation;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.IFilterPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.IMergingStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.IPIIQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;

/**
 * 
 * 
 * @Version 0.1
 * @Date May 6, 2010
 * 
 */
public class RevealUnderDhp implements IProvisionalAction, IUseCredentials, IUseAccessControl, IUseDhp {

	private static Logger  LOGGER = LoggerFactory.getLogger(RevealUnderDhp.class); 
	
	private String attributeName;
	private DataHandlingPolicyType policy;
	private String credentialIdReference;
	private RequestType request;
	private IPIIQueryStrategy piiLoadingStrategy;
	private IPolicyQueryStrategy policyQueryStrategy;
	private IFilterPiiStrategy filterPiiStrategy;
	private IMergingStrategy mergingStrategy;

	public RevealUnderDhp(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * performs loading of PII, access control and policy matching
	 */			
	@Override
	public List<ActionResponse> handle() throws SyntaxException,
			FileNotFoundException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException, WritingException,
			JAXBException {
		
		List<ActionResponse> result = new LinkedList<ActionResponse>();
		if (credentialIdReference != null) {
			LOGGER.warn("Credential handling not supported for downstream usage scenario.");
		} else {
			//Load PII
			List<PIIType> piiList = this.piiLoadingStrategy.executeQuery(attributeName);
			
			this.filterPiiStrategy.filter(this.attributeName, piiList);

			//at least one pii was found
			for (PIIType pii : piiList) {
				//result is an Action Response				
				ActionResponse actionResponse = new ActionResponse(pii.getAttributeName());
				//access control and selecting policy
				EvaluationResponse evaluationResponse = PolicyEvaluation.evaluate(pii, request, this.policyQueryStrategy);
				//set enforcementResponse
				actionResponse.setEnforcementResponse(evaluationResponse);
				
				if (evaluationResponse.getDecision() == DecisionType.PERMIT) {
					this.mergingStrategy.processPolicy(actionResponse, evaluationResponse.getPreferences(), policy);

				}
				result.add(actionResponse);				
			}			
		}
		return result;
	}
	
	
	@Override
	public void setDataHandlingPolicy(DataHandlingPolicyType dhp) {
		this.policy = dhp;
	}

	@Override
	public void setXacmlRequest(RequestType xacmlRequest) {
		this.request = xacmlRequest;
	}

	@Override
	public void setCredentialIdReference(String credentialIdReference) {
		this.credentialIdReference = credentialIdReference;
	}

	public void setPiiLoadingStrategy(IPIIQueryStrategy piiLoadingStrategy) {
		this.piiLoadingStrategy = piiLoadingStrategy;
	}
	
	public void setPolicyQueryStrategy(IPolicyQueryStrategy policyQueryStrategy) {
		this.policyQueryStrategy = policyQueryStrategy;
	}


	public void setFilterPiiStrategy(
			IFilterPiiStrategy strategy) {
		this.filterPiiStrategy = strategy;
	}

	public void setMergingStrategy(IMergingStrategy mergingStrategy) {
		this.mergingStrategy = mergingStrategy;
	}

	
}
