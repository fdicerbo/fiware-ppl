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
package com.sap.research.primelife.ds.pdp.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.saml._2_0.assertion.StatementAbstractType;

import org.herasaf.xacml.core.context.impl.DecisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pep.PEP;

import eu.primelife.ppl.claims.impl.ClaimType;
import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.claims.impl.Decision;
import eu.primelife.ppl.claims.impl.ListPIIType;
import eu.primelife.ppl.claims.impl.ObjectFactory;
import eu.primelife.ppl.claims.impl.ResponseType;
import eu.primelife.ppl.claims.impl.StickyPolicyStatementType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * A data object collecting the results of the provisional action handlers.
 * It is able to create the final response in the claims format.
 *
 */
public class PdpResponse {

	private static final Logger LOGGER =
		LoggerFactory.getLogger(PdpResponse.class);
	private StickyPolicy stickyPolicy;
	private DecisionType decision;
	private List<String> missingPiiList;
	private List<String> denyAccessPiiList;
	private List<String> mismatchablePiiList;
	private List<ActionResponse> actionResponseList;
	private static ObjectFactory ofClaims = new ObjectFactory();
	private static eu.primelife.ppl.stickypolicy.impl.ObjectFactory ofStickyPolicy =
		new eu.primelife.ppl.stickypolicy.impl.ObjectFactory();

	/**
	 * Creates an empty PdpResponse. 
	 * Default decision is Permit.
	 */
	public PdpResponse() {
		stickyPolicy = ofStickyPolicy.createStickyPolicy();
		decision = DecisionType.PERMIT;
		missingPiiList = new ArrayList<String>();
		denyAccessPiiList = new ArrayList<String>();
		mismatchablePiiList = new ArrayList<String>();
		actionResponseList = new ArrayList<ActionResponse>();
	}

	/**
	 * @param decision	the access control decision
	 */
	private void setDecision(DecisionType decision) {
		//if the response action is not Indeterminate, we make it to Deny
		if ((decision == DecisionType.DENY && this.decision != DecisionType.INDETERMINATE)
				|| decision != DecisionType.DENY) {
			LOGGER.info("Final decision set to " + decision);
			this.decision = decision;
		}
	}

	/**
	 * Adds an action response to the final result. Using the decision of the 
	 * action response, the final response is adjusted.
	 * @param actionResponse	action response for the PII
	 */
	@SuppressWarnings("incomplete-switch")
	public void addActionResponse(ActionResponse actionResponse) {
		String attributeName = actionResponse.getAttributeName();

		DecisionType actionResponseDecision = actionResponse.getEnforcementResponse().getDecision();
		setDecision(actionResponseDecision);

		//dependently of the action response, we make the specific process
		switch (actionResponse.getEnforcementResponse().getDecision()) {
		case PERMIT:
			AttributeType attributeSP = actionResponse.getAttributeSP();

			if (attributeSP != null && !attributeSP.isMatching()) {
				LOGGER.info("There's mismatch for the PII: " + attributeName);

				//we indicate in the sticky policy, that there's a mismatch
				stickyPolicy.setMatching(false);

				//add the mismatch attribute to the list of the mismatchable PII
				mismatchablePiiList.add(attributeName);

				setDecision(DecisionType.INDETERMINATE);
			}
			else {
				LOGGER.info("The PII : " + attributeName + " is enforcable.");
			}

			actionResponseList.add(actionResponse);

			//add the sticky policy of the PII
			stickyPolicy.getAttribute().add(actionResponse.getAttributeSP());

			break;
		case DENY:
			LOGGER.info("The PII : " + attributeName + " is NOT enforcable.");
			denyAccessPiiList.add(attributeName);
			break;
		case NOT_APPLICABLE:
			// this may happen if the policy's targets are for specific resources and the request is for another resource
			// So the HERAS decides that no rule is applicable. We cannot do the matching, we say the pii is missing.
			//TODO test downstream usage - does this behavior cause problems there?
			LOGGER.info("The Policy for PII : " + attributeName + " is NOT applicable.");
			missingPiiList.add(attributeName);
			setDecision(DecisionType.INDETERMINATE);
			
		}
	}

	/**
	 * Create the claims to be sent to UI based on the assertionList.
	 * @return	claims containing access control and matching result,
	 * 			missing PIIs list and all the attributes with permitted access,
	 * 			as well as mismatching PIIs
	 * @throws DatatypeConfigurationException 
	 */
	public ClaimsType toClaim() throws DatatypeConfigurationException {
		ResponseType responseStatement = ofClaims.createResponseType();

		switch (decision) {
		case PERMIT:
			responseStatement.setDecision(Decision.ACCESS);
			break;
		case DENY:
			responseStatement.setDecision(Decision.DENY);
			break;
		case INDETERMINATE:
		case NOT_APPLICABLE:
			responseStatement.setDecision(Decision.INDETERMINATE);
			break;
		}

		ListPIIType denyAccessPii = ofClaims.createListPIIType();
		denyAccessPii.setValue(denyAccessPiiList);
		responseStatement.setDeneyPII(denyAccessPii);

		ListPIIType missingPii = ofClaims.createListPIIType();
		missingPii.setValue(missingPiiList);
		responseStatement.setMissingPII(missingPii);

		//create a list of assertion
		List<AssertionType> assertionList = new ArrayList<AssertionType>();

		List<StatementAbstractType> statementList =	new ArrayList<StatementAbstractType>();
		statementList.add(responseStatement);

		/*
		 * Create the decisionAssertion according to the PDP response
		 * (and the other Assertions)
		 */
		AssertionType decisionAssertion = PEP.createSamlAssertion();
		decisionAssertion.setStatementOrAuthnStatementOrAuthzDecisionStatement(statementList);
		assertionList.add(decisionAssertion);

		/*
		 * Create a list of PII assertions
		 */
		
		//loop over the attributes and add them one by one in new assertion
		for (ActionResponse actionResponse : actionResponseList) {
			assertionList.add(actionResponse.toSamlAssertion());
		}

		//if we have a sticky policy, we add it in an assertion
		if (stickyPolicy != null) {
			AssertionType assertion = PEP.createSamlAssertion();

			StickyPolicyStatementType spStatementTmp = ofClaims.createStickyPolicyStatementType();
			spStatementTmp.setAttribute(stickyPolicy.getAttribute());

			List<StatementAbstractType> listStatementTmp = new ArrayList<StatementAbstractType>();
			listStatementTmp.add(spStatementTmp);

			assertion.setStatementOrAuthnStatementOrAuthzDecisionStatement(listStatementTmp);

			assertionList.add(assertion);
		}

		/*
		 * prepare the claim for return
		 */

		//create a new claim
		ClaimType claim = ofClaims.createClaimType();
		//add the list of the assertion to the claim
		claim.setAssertion(assertionList);

		//create a list of claim
		List<ClaimType> listClaim = new ArrayList<ClaimType>();
		//add the claim to the list of the claims
		listClaim.add(claim);

		ClaimsType claimsReturn = ofClaims.createClaimsType();
		//add the list of the claims to the 'Claims' main element of the file that we will return it to the UI
		claimsReturn.setClaim(listClaim);

		return claimsReturn;
	}

	/**
	 * Returns list of provisional action responses. Depending on the chosen {@link IResponseHandlingStrategy},
	 * it only contains actions with final result <code>PERMIT<code> and policy match (for API), or 
	 * with final result which is any decision (User interaction needed).
	 * @return	list of provisional actions responses
	 */
	public List<ActionResponse> getActionResponses() {
		return actionResponseList;
	}

	
}
