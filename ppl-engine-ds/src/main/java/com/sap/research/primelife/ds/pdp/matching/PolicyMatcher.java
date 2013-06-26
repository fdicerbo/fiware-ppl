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
package com.sap.research.primelife.ds.pdp.matching;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.utils.IdGenerator;

import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.MismatchesType;
import eu.primelife.ppl.stickypolicy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.obligation.impl.ObligationsSet;

/**
 * Matches preferences and policies by calling the AuthorizationsMatching and
 * ObligationsMatcher, combining the results to an AttributeType.
 * 
 *
 */
public class PolicyMatcher {

	private static final ObjectFactory ofStickyPolicy = new ObjectFactory();
	private static final PolicyDao dao = new PolicyDao();
	private static Logger log = LoggerFactory.getLogger(PolicyMatcher.class);

	/**
	 * 
	 * @param pii
	 * @param dhPolicy
	 * @param dhPrefs
	 * @return
	 * @throws ValidationException
	 * @throws WritingException
	 * @throws SyntaxException
	 */
	public static AttributeType match(String pii, DataHandlingPolicyType dhPolicy,
			DataHandlingPreferencesType dhPrefs){
		
		//attribute element for the sticky policy factory
		AttributeType attributeElementSP = ofStickyPolicy.createAttributeType();
		
		//put the attribute URI of the sticky policy
		attributeElementSP.setAttributeURI(pii);

		//put an ID to the StickyPolicy
		attributeElementSP.setID("SP" + String.valueOf(IdGenerator.generatePositiveInt()));

		/*
		 * Authorization
		 */
		AuthorizationsSetType detachedPreferences = null;
		AuthorizationsSetType detachedPolicy = null;
		try {
			//This is done to avoid changes in the session object of the preferences
			// Not doing this will lead to problems in HERAS marshaling for the next request
			detachedPreferences = dao.cloneAuthorizationSet(dhPrefs.getAuthorizationsSet());
			//This is done because the authorizationsSet of the policy might contain Hjids,
			//which could lead to the well known "detached entity passed to persist" error on DC
			detachedPolicy = dao.cloneAuthorizationSet(dhPolicy.getAuthorizationsSet());
		} catch (JAXBException e) {
			log.error("Error while cloning the authorizationsSet", e);
		} catch (WritingException e) {
			log.error("Error while cloning the authorizationsSet", e);
		} catch (SyntaxException e) {
			log.error("Error while cloning the authorizationsSet", e);
		}
		HandyAuthorizationsSet pref = new HandyAuthorizationsSet(detachedPreferences);
		HandyAuthorizationsSet policy = new HandyAuthorizationsSet(detachedPolicy);
		
		AuthorizationsSetType authSpResult = policy.getStickyAuthorizationsSet(pref);
		
		//put the authorizationsSet SP into the attribute element
		attributeElementSP.setAuthorizationsSet(authSpResult);

		/*
		 * Obligation
		 */
		// get the ObligationsSet sticky policy
		
		ObligationsMatcher obligationsMatcher = new ObligationsMatcher();
		
		ObligationsSet obligResult = obligationsMatcher.getStickyPolicy(
				dhPolicy.getObligationsSet(), dhPrefs.getObligationsSet());
		
		//put the ObligationsSet SP into the attribute element
		attributeElementSP.setObligationsSet(obligResult.getObligationsSet());

		attributeElementSP.setMatching(true);

		/*
		 * Mismatch
		 */

		if (!authSpResult.isMatching() || !obligResult.isMatching()) {
			//create a mismatchType element
			MismatchesType mismatch = ofStickyPolicy.createMismatchesType();

			//indicate in the attributeElement that a mismatch exist
			attributeElementSP.setMatching(false);

			if (!authSpResult.isMatching()) {
				//get the authorization mismatch and add them
				mismatch.setAuthorizationsMismatch(policy.getStickyMismatches());
			}

			if (!obligResult.isMatching()) {
				//get the obligation mismatch and add them
				mismatch.setObligationsMismatch(obligResult.getMismatches());
			}

			//add the mismatch to the attribute PII element
			attributeElementSP.setMismatches(mismatch);
		}
		
		return attributeElementSP;
	}

}
