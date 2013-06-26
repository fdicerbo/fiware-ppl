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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
//import com.sap.research.primelife.obligation.matching.client.IOMEService;
//import com.sap.research.primelife.obligation.matching.client.IOMEServiceGetMatchingPreferencePPLDeserializationExceptionFaultFaultMessage;
//import com.sap.research.primelife.obligation.matching.client.IOMEServiceGetMatchingPreferencePPLStickyPolicyExceptionFaultFaultMessage;
//import com.sap.research.primelife.obligation.matching.client.OMEService;

import eu.primelife.ppl.policy.impl.AuthorizationType;
import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;
import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;

/**
 * Contains the logic to update data handling preferences. 
 * Depends on the HandyAuthorizationsSet and the ObligationMatching Webservice. 
 * For an overview of the matching and updating algorithm, see the file authorizationMatching.pdf
 * 
 *
 */
public class PolicyUpdater {
	
	private static DaoImpl<DataHandlingPreferencesType> dao = new DaoImpl<DataHandlingPreferencesType>();
	private static DaoImpl<AuthorizationsSetType> authDao = new DaoImpl<AuthorizationsSetType>();
	@SuppressWarnings("unused")
	private static DaoImpl<ObligationsSet> oblDao = new DaoImpl<ObligationsSet>();
	private static PolicyDao polDao = new PolicyDao();
	private static ObjectFactory ofPrimelife = new ObjectFactory();
	private static final Logger LOGGER = LoggerFactory.getLogger(PolicyUpdater.class);

	/**
	 * Updates data handling preferences in the database to match against the policy.
	 * @param dhprefs - the preferences to be updated and persisted
	 * @param policy - the policy to match against
	 */
	public static void updatePreferences(DataHandlingPreferencesType dhprefs,
			DataHandlingPolicyType policy) {

		AuthorizationsSetType auth = null;
		LOGGER.info("updating Preferences");
		try {
			// because we don't know in advance which attributes have to be changed or added,
			// we simply create a new authorizationsSet and remove the old one
			auth = polDao.cloneAuthorizationSet(dhprefs.getAuthorizationsSet());
			
		} catch (SyntaxException e) {
			LOGGER.error("Failed cloning the authorizationSet", e);
		} catch (WritingException e) {
			LOGGER.error("Failed cloning the authorizationSet", e);
		} catch (JAXBException e) {
			LOGGER.error("Failed cloning the authorizationSet", e);
		}
		//deleting the old authorizationsSet would be nice, but does not work
//			authDao.deleteObject(dhprefs.getAuthorizationsSet());
		AuthorizationsSetType newAuthSet = mergeAuthorizations(auth, policy.getAuthorizationsSet());
		// persist the updated authorizationsSet
		if (newAuthSet != null) {
			authDao.persistObject(newAuthSet);
		}
		// set the updated authorizationsSet
		dhprefs.setAuthorizationsSet(newAuthSet);		
		dao.updateObject(dhprefs);
		
		
		// create an updated obligationsSet, call the ome
//		ObligationsSet obl = policy.getObligationsSet();
//		oblDao.deleteObject(policy.getObligationsSet());
//		ObligationsSet newObligations = mergeObligations(obl, policy.getObligationsSet());
//		oblDao.persistObject(newObligations);
//		dhprefs.setObligationsSet(newObligations);
//		dao.updateObject(dhprefs);
		
	}

//	private static ObligationsSet mergeObligations(ObligationsSet prefs,
//			ObligationsSet policy) {
//		OMEService service = new OMEService();
//		IOMEService ome = service.getBasicHttpBindingIOMEService();
//		
//		try {
//			return ome.getMatchingPreference(prefs, policy);
//		} catch (IOMEServiceGetMatchingPreferencePPLDeserializationExceptionFaultFaultMessage e) {
//			LOGGER.error("Failed to call OME", e);
//			e.printStackTrace();
//		} catch (IOMEServiceGetMatchingPreferencePPLStickyPolicyExceptionFaultFaultMessage e) {
//			LOGGER.error("Failed to call OME", e);
//			e.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * Matches an {@link AuthorizationsSet} policy against {@link AuthorizationsSet} preferences.
	 * @param authorizationsPolicy
	 * 			the DataHandlingPolicy of the ACP
	 * @param authorizationsPreferences
	 * 			the DataHandlingPreference of the DS preferences of one PII
	 * @return
	 * 			{@link AuthorizationsSet} sticky policy
	 */
	public static AuthorizationsSetType mergeAuthorizations(AuthorizationsSetType authorizationsPreferences,
			AuthorizationsSetType authorizationsPolicy) {
		
		HandyAuthorizationsSet policy = new HandyAuthorizationsSet(authorizationsPolicy);
		HandyAuthorizationsSet pref = new HandyAuthorizationsSet(authorizationsPreferences);
		
		if (policy.isEmpty()) {
			//nothing is allowed in the policy, every preference will match
			//so we don't change the preferences
			LOGGER.info("the policy is empty, we don't have to update the preferences.");
			return authorizationsPreferences;
		}
		LOGGER.info("comparing authorization elements of policy with preferences.");
		// update authorization elements
		AuthorizationsSetType finalAuth = ofPrimelife.createAuthorizationsSetType();
		
		// updated the auth for downstream usage explicitly
		JAXBElement<? extends AuthorizationType> dsu = policy.getAuthzForDownstreamUsage().createUpdatedPreference(pref.getAuthzForDownstreamUsage());
		// update the auth for purpose explicitly
		JAXBElement<? extends AuthorizationType> purp = policy.getAuthzForPurpose().createUpdatedPreference(pref.getAuthzForPurpose());
		finalAuth.getAuthorization().add(dsu);
		finalAuth.getAuthorization().add(purp);
		
		return finalAuth;	
		
	}

}
