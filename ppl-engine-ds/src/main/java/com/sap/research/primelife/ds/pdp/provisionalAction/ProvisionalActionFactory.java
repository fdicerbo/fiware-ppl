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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.query.policy.PolicyDoNotSelectStrategy;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;

import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.ProvisionalActionType;
import eu.primelife.ppl.policy.xacml.impl.AttributeValueType;


/**
 * Factory of the provisional action depending of the ProvisionalAction action.
 * 
 * @Version 0.1
 * @Date May 6, 2010
 * 
 */
public class ProvisionalActionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProvisionalActionFactory.class);

	/**
	 * Return the appropriate provisional action depending of the action.
	 * @param type
	 * 			The type or the action of the provisional action.
	 * @return
	 * 			The appropriate provisional action.
	 * @throws ValidationException
	 * @throws SyntaxException if the provisional action cannot be understood with the given policy
	 * @throws MissingPreferenceGroupException - if the preferenceGroup is not in the preference store
	 */
	public static IProvisionalAction getProvisionalAction(PDPRequest request, ProvisionalActionType pa, Map<String, String> credentialMap)
			throws ValidationException, SyntaxException, MissingPreferenceGroupException {
		
		LOGGER.info("ProvisionalAction Factory...");
		
		String type = pa.getActionId();
		List<AttributeValueType> attributes = pa.getAttributeValue();
		LOGGER.info("Get the provisional action for the action: " + type);

		if ("http://www.primelife.eu/ppl/Reveal".equals(type)){
			checkParameterNumbers(attributes, 1, 2, "Reveal");
			String attributeName = attributes.get(0).getContent().get(0).toString();
			String credentialReference = getOptionalCredentialId(attributes, 1, credentialMap, type);
			
			Reveal provAction =  new Reveal(attributeName);
			provAction.setCredentialIdReference(credentialReference);
			provAction.setXacmlRequest(request.createXacmlRequest(attributeName));
			provAction.setPiiLoadingStrategy(request.getPIILoadingStrategy());
			provAction.setPolicyQueryStrategy(new PolicyDoNotSelectStrategy());
			provAction.setHandleMissingPiiStrategy(request.getFilterPiiStrategy());
			return provAction;
		}
		if ("http://www.primelife.eu/ppl/RevealUnderDHP".equals(type)) {
			checkParameterNumbers(attributes, 2, 3, "RevealUnderDHP");
			String attributeName = attributes.get(0).getContent().get(0).toString();
			String dhpName = attributes.get(1).getContent().get(0).toString();
			String credentialReference = getOptionalCredentialId(attributes, 2, credentialMap, type);
			
			RevealUnderDhp provAction =  new RevealUnderDhp(attributeName);
			provAction.setCredentialIdReference(credentialReference);
			provAction.setXacmlRequest(request.createXacmlRequest(attributeName));
			provAction.setDataHandlingPolicy(getDataHandlingPreferences(request.getDhpMap(), dhpName));
			provAction.setPiiLoadingStrategy(request.getPIILoadingStrategy());
			provAction.setPolicyQueryStrategy(request.getPolicyQueryStrategy());
			provAction.setFilterPiiStrategy(request.getFilterPiiStrategy());
			provAction.setMergingStrategy(request.getMergingStrategy());
			return provAction;
		}
		if ("http://www.primelife.eu/ppl/RevealTo".equals(type)) {
			checkParameterNumbers(attributes, 2, 3, "RevealTo");
			String attributeName = attributes.get(0).getContent().get(0).toString();
			String thirdParty = attributes.get(1).getContent().get(0).toString();
			String credentialReference = getOptionalCredentialId(attributes, 2, credentialMap, type);
			
			RevealTo provAction =  new RevealTo(attributeName);
			provAction.setCredentialIdReference(credentialReference);
			provAction.setThirdParty(thirdParty);
			provAction.setXacmlRequest(request.createXacmlRequest(attributeName));
			return provAction;
		}
		if ("http://www.primelife.eu/ppl/RevealToUnderDHP".equals(type)) {
			checkParameterNumbers(attributes, 3, 4, "RevealToUnderDHP");
			String attributeName = attributes.get(0).getContent().get(0).toString();
			String thirdParty = attributes.get(1).getContent().get(0).toString();
			String dhpName = attributes.get(2).getContent().get(0).toString();
			String credentialReference = getOptionalCredentialId(attributes, 3, credentialMap, type);
			
			RevealToUnderDhp provAction =  new RevealToUnderDhp(attributeName);
			provAction.setCredentialIdReference(credentialReference);
			provAction.setXacmlRequest(request.createXacmlRequest(attributeName));
			provAction.setThirdParty(thirdParty);
			provAction.setDataHandlingPolicy(getDataHandlingPreferences(request.getDhpMap(), dhpName));
			return provAction;
		}
		if ("http://www.primelife.eu/ppl/Sign".equals(type)) {
			checkParameterNumbers(attributes, 1, 1, "Sign");
			String statement = attributes.get(0).getContent().get(0).toString();
			return new Sign(statement);
		}
		if ("http://www.primelife.eu/ppl/Spend".equals(type)) {
			checkParameterNumbers(attributes, 4, 4, "Spend");
			String credentialId = getOptionalCredentialId(attributes, 0, credentialMap, type);
			int unitsToSpend = Integer.valueOf(attributes.get(1).getContent().get(0).toString());
			int spendingLimit = Integer.valueOf(attributes.get(2).getContent().get(0).toString());
			String scope = attributes.get(3).getContent().get(0).toString();;
			
			return new Spend(credentialId, unitsToSpend, spendingLimit, scope);
		}
		throw new ValidationException("The provisional action type does not exist !");
	}
	
	private static String getOptionalCredentialId(List<AttributeValueType> attributes, int position, Map<String, String> credentialMap, String type) throws SyntaxException{
		if (attributes.size() > position) {
			String credentialId = attributes.get(position).getContent().get(0).toString().trim();
			if (credentialMap != null && !credentialMap.containsKey(credentialId)){
				LOGGER.error("The credential " + credentialId + "for the provisional Action" + type + "does not occur in the credential map.");
				throw new SyntaxException("The credential Id "
						+ credentialId
						+ " does not occur in the credentials store.");
			}
			return credentialMap.get(credentialId);
		} else {
			return null;
		}
	}
	
	private static void checkParameterNumbers(List<AttributeValueType> attributes, int min, int max, String type) throws SyntaxException{
		if (attributes.size() < min){
			LOGGER.error("Provisional action "+ type + "requires at least " + min + "parameters.");
			throw new SyntaxException("Reveal action contains "
					+ attributes.size()
					+ " parameters but "+ min + " to " + max + " parameters are required.");
		}
		if (attributes.size() > max) {
			LOGGER.error("Provisional action "+ type + "requires not more than " + max + "parameters.");
			throw new SyntaxException("Reveal action contains "
					+ attributes.size()
					+ " parameters but "+ min + " to " + max + " parameters are required.");
		}
	}
	
	private static DataHandlingPolicyType getDataHandlingPreferences(Map<String, DataHandlingPolicyType> dhpMap, String policyId) throws SyntaxException {
		if (!dhpMap.containsKey(policyId)){
			LOGGER.error("The data handling policy " + policyId + " could not be found.");
			throw new SyntaxException("The data handling policy " + policyId + " could not be found.");
		} else {
			return dhpMap.get(policyId);
		}
	}

}
