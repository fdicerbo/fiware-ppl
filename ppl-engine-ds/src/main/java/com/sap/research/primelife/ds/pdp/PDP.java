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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusCodeType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusType;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.evaluation.AccessControlUtils;
import com.sap.research.primelife.ds.pdp.provisionalAction.IProvisionalAction;
import com.sap.research.primelife.ds.pdp.query.pii.IPIIQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.PiiQueryByAttributeNameStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPiiStrategy;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.ds.pdp.response.IResponseHandlingStrategy;
import com.sap.research.primelife.ds.pdp.response.PdpResponse;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;

import eu.primelife.ppl.claims.impl.ClaimType;
import eu.primelife.ppl.pii.impl.PIIType;

/**
 * PDP component, it is used for the data protection mechanism of the data subject and data controller instance.
 * 
 */
public class PDP {

	protected static final Logger LOGGER = LoggerFactory.getLogger(PDP.class);
	protected IPIIQueryStrategy piiQueryStategy;
	protected IPolicyQueryStrategy policyQueryStrategy;
	protected AccessControlUtils accessControlUtils;
	
	public PDP() {
		 piiQueryStategy = new PiiQueryByAttributeNameStrategy();
		 policyQueryStrategy = new PolicyByPiiStrategy(null);
		 accessControlUtils = new AccessControlUtils();
	}
	
	protected PDP(IPIIQueryStrategy piiQueryStategy, IPolicyQueryStrategy policyQueryStrategy, AccessControlUtils accessControlUtils) {
		this.piiQueryStategy = piiQueryStategy;
		this.policyQueryStrategy = policyQueryStrategy;
		this.accessControlUtils = accessControlUtils;
	}

	/**
	 * Processes the request containing provisional actions for different contexts. Compare all subclasses of {@link PDPRequest}.
	 * 
	 * @param pdpRequest - the request object containing the server's policy and context information
	 * @param claim - needed for handling credentials (currently ignored)
	 * @return a PdpResponse which can create the claims as a response to the request
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws JAXBException
	 * @throws MissingPreferenceGroupException
	 */
	public PdpResponse evaluate(PDPRequest pdpRequest, ClaimType claim) throws SyntaxException,
		FileNotFoundException, org.herasaf.xacml.core.SyntaxException,
		ProcessingException, MissingAttributeException, WritingException,
		JAXBException, MissingPreferenceGroupException {
		LOGGER.info("Start PDP Evaluation....");

		List<ActionResponse> actionResponses = new LinkedList<ActionResponse>();
		
		for (IProvisionalAction pa : pdpRequest.getProvisionalActionHandlerList()){
			List<ActionResponse> actionResponsesOfPA = pa.handle();
			actionResponses.addAll(actionResponsesOfPA);
		}
		
		IResponseHandlingStrategy handler = pdpRequest.getResponseHandlingStrategy();

		return handler.handleActionResponses(actionResponses);
	}
	
	
	/**
	 * @param XACML request 
	 * @return XACML reponse
	 * @throws JAXBException 
	 * @throws org.herasaf.xacml.core.SyntaxException 
	 * @throws WritingException 
	 * @throws org.herasaf.xacml.core.SyntaxException 
	 * @throws MissingAttributeException 
	 * @throws ProcessingException 
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 */
	public ResponseType evaluate(RequestType request) {
		String name;
		
		ResponseType response = new ResponseType();
		List<ResultType> resultList = new ArrayList<ResultType>();
		response.setResult(resultList);
		
		try{
			name = (String)(request.getResources().get(0).getAttributes().get(0).getAttributeValues().get(0).getContent().get(0));
			if(name == null){
				resultList.add(generateResultMissingAttribute());
				return response;
			}
		}catch(RuntimeException e){
			resultList.add(generateResultMissingAttribute());
			return response;
		}
		
		// Get the requested Piis
		List<PIIType> piiList = piiQueryStategy.executeQuery(name);
		
		if(piiList == null || piiList.size() == 0){
			LOGGER.info("No Pii found");
			return null;
		}
		
		// Foreach Pii
		for(PIIType pii : piiList){
			evaluate(request, resultList, pii);
		}
		
		return response;
	}

	private ResultType generateResultMissingAttribute() {
		ResultType result;
		StatusType status;
		StatusCodeType statusCode;
		result = new ResultType();
		status = new StatusType();
		statusCode = new StatusCodeType();
		statusCode.setValue(StatusCodeType.STATUS_MISSING_ATTRIBUTE);
		status.setStatusCode(statusCode);
		result.setStatus(status);
		result.setDecision(DecisionType.NOT_APPLICABLE);
		return result;
	}

	private void evaluate(RequestType request, List<ResultType> resultList, PIIType pii) {
		ResultType result;
		StatusType status;
		StatusCodeType statusCode;
		// as default we use all policies associated with PII
		List<Object> policySetOrPolicy = policyQueryStrategy.executeQuery(pii);
		
		result = new ResultType();
		result.setResourceId(pii.getHjid().toString());
		
		if (policySetOrPolicy == null || policySetOrPolicy.size() == 0) {
			LOGGER.info("No Policy found");
			result.setDecision(DecisionType.NOT_APPLICABLE);
			statusCode = new StatusCodeType();
			statusCode.setValue(StatusCodeType.STATUS_OK);
			status = new StatusType();
			status.setStatusCode(statusCode);
			result.setStatus(status);
			resultList.add(result);
		}else{
			LOGGER.info("Number policies found: " + policySetOrPolicy.size());
			
			// Check access
			org.herasaf.xacml.core.context.impl.DecisionType hDecision;
			try {
				hDecision = accessControlUtils.checkAccess(policySetOrPolicy, request);
				DecisionType decision = DecisionType.fromValue(hDecision.value());
				result.setDecision(decision);
				status = new StatusType();
				statusCode = new StatusCodeType();
				statusCode.setValue(StatusCodeType.STATUS_OK);
				status.setStatusCode(statusCode);
				result.setStatus(status);
				resultList.add(result);
				LOGGER.info("HERAS PDP enforcement decision is " + decision.value());
			} catch (Exception e) {
				if(		e instanceof WritingException ||
						e instanceof org.herasaf.xacml.core.SyntaxException ||
						e instanceof SyntaxException ||
						e instanceof JAXBException){
					LOGGER.error("Error during Herasaf evaluation");
					e.printStackTrace();
				}else{
					LOGGER.error("Unexpected Exception");
					e.printStackTrace();
				}
				status = new StatusType();
				statusCode = new StatusCodeType();
				statusCode.setValue(StatusCodeType.STATUS_PROCESSING_ERROR);
				status.setStatusCode(statusCode);
				status.setStatusMessage("Error during Herasaf evaluation");
				result.setStatus(status);
				result.setDecision(DecisionType.NOT_APPLICABLE);
				resultList.add(result);
			}
		}
	}

}
