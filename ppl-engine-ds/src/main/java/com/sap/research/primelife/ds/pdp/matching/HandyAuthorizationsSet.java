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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem;
import eu.primelife.ppl.policy.impl.AuthzDownstreamUsageType;
import eu.primelife.ppl.policy.impl.AuthzUseForPurpose;
import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.authorization.impl.AuthorizationsMismatchType;
import eu.primelife.ppl.stickypolicy.authorization.impl.AuthzDownstreamUsageMismatchType;
import eu.primelife.ppl.stickypolicy.authorization.impl.AuthzUseForPurposeMismatchType;

/**
 * The HandyAuthorizationsSet adds functionality to an {@link AuthorizationsSetType}
 * which occurs within DataHandlingPreferences or a DataHandlingPolicy.
 * <p>
 * An empty AuthorizationsSet means, that nothing is allowed since there are no authorizations.
 * </p>
 * <p>
 * A non-existing AuthorizationsSet occurs when there is no AuthorizationsSet element within the 
 * DataHandlingPreferences or DataHandlingPolicy. It means that the value is unknown or not defined.
 * For the matching we assume an arbitrary value. If there is a case that could lead to a mismatch,
 * we assume this case.
 * </p>
 * <h2>Functions of a HandyAuthorizationsSet</h2>
 * First, it contains all the logic to match itself against a preferences object. So {@code this} always
 * represents the policy. Second, it is able to create an updated AuthorizationsSet which is matching
 * with the preferences.
 * 
 * 
 *
 */
public class HandyAuthorizationsSet {
	
	private List<HandyAuthorization> authItems;
	private List<AuthorizationsSetTypeAuthorizationItem> originalItems; 
	private boolean isUnknown;
	private AuthorizationsMismatchType mismatch;
	
	private ObjectFactory ofPrimelife = new ObjectFactory();
	private eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory ofStickyPolicy 
		= new eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory();
	private Logger log = LoggerFactory.getLogger(HandyAuthorizationsSet.class);
	
	public HandyAuthorizationsSet(AuthorizationsSetType auth) {
		this.authItems = new LinkedList<HandyAuthorization>();
		if (auth == null) {
			this.isUnknown = true;
			authItems = new LinkedList<HandyAuthorization>();
		} else {
			this.isUnknown = false;
			authItems = createHandies(auth.getAuthorizationItems());
			this.originalItems = auth.getAuthorizationItems();
		}
		//log.info("There are " + authItems.size() + " elements in the AuthorizationSet.");
	}
	
	/**
	 * A HandyAuthorizationsSet is unknown if the original 
	 * AuthorizationsSetType is null.
	 * @return true if the authorizationsSet is not specified 
	 */
	public boolean isUnknown() {
		return this.isUnknown;
	}
	
	/**
	 * 
	 * @return the original list of AuthorizationItems
	 */
	public List<AuthorizationsSetTypeAuthorizationItem> getOriginalItems() {
		return originalItems;
	}
	
	/**
	 * 
	 * @return true if it does not contain any AuthorizationItems
	 */
	public boolean isEmpty() {
		return this.authItems.size() == 0;
	}
	
	/**
	 * Currently the HandyAuthorization list can only contain
	 * HandyAuthzForDownstreamUsage and HandyAuthzForPurpose.
	 * @return a list of HandyAuthorizations representing the AuthorizationItems
	 */
	public List<HandyAuthorization> getItems (){
		return this.authItems;
	}
	
	/**
	 * Return a special HandyAuthorization item.
	 * (Method is useful if we want generic AuthorizationTypes)
	 * @param <T> the Class we are looking for e.g. HandyAuthzForDownstreamUsage
	 * @param type
	 * @return a handyAuthorization
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAuthorization(Class<T> type){
		for (Object element : this.authItems) {
			if (element.getClass() == type) {
				return (T) element;
			}
		}
			
		return null;
	}
	
	/**
	 * The HandyAuthzForPurpose is created and initialized with a null value
	 * if it does not exist.
	 * 
	 * @return the HandyAuthForPurpose item
	 */
	public HandyAuthzForPurpose getAuthzForPurpose(){
		HandyAuthzForPurpose handy = new HandyAuthzForPurpose(null);
		for (HandyAuthorization a : authItems) {
			if (a instanceof HandyAuthzForPurpose) {
				handy = (HandyAuthzForPurpose) a;
			}
		}
		return handy;
	}

	/**
	 * The HandyAuthzForDownstreamUsage is created and initialized with a null value
	 * if it does not exist.
	 * 
	 * @return the HandyAuthzForDownstreamUsage item
	 */
	public HandyAuthzForDownstreamUsage getAuthzForDownstreamUsage(){
		HandyAuthzForDownstreamUsage handy = new HandyAuthzForDownstreamUsage(null);
		for (HandyAuthorization a : authItems) {
			if (a instanceof HandyAuthzForDownstreamUsage) {
				handy = (HandyAuthzForDownstreamUsage) a;
			}
		}
		return handy;
	}
	
	/**
	 * Factory method to create the HandyAuthorizationItems from the list in the original
	 * @param items - the authorizationItems in the AuthorizationsSet
	 * @return a list with HandyAuthorizations representing the authorizationItems
	 */
	private List<HandyAuthorization> createHandies(List<AuthorizationsSetTypeAuthorizationItem> items) {
		List<HandyAuthorization> result = new LinkedList<HandyAuthorization>();
		for (AuthorizationsSetTypeAuthorizationItem i : items) {
			if (i.getItem().getDeclaredType().equals(AuthzDownstreamUsageType.class)){
				//log.info("found dsu");
				result.add(new HandyAuthzForDownstreamUsage(ofPrimelife.createAuthzDownstreamUsage((AuthzDownstreamUsageType)i.getItemValue())));
			} else if (i.getItem().getDeclaredType().equals(AuthzUseForPurpose.class)){
				//log.info("found purpose");
				result.add(new HandyAuthzForPurpose(ofPrimelife.createAuthzUseForPurpose((AuthzUseForPurpose)i.getItemValue())));
			}			
		}
		//log.info("There are " + items.size() + " items.");
		return result;
	}
	
	/**
	 * Matches two authorizationSets, returns the final authorizationsSet including matching attributes
	 * The matching attribute is true if the enforcement of the instance's authorization does not violate the preferences 
	 * At the same time: creates Mismatch elements describing the differences of policy (this) and preferences in case of a mismatch 
	 * For an overview of the matching algorithm, see the file final_matching_spec.docx
	 *
	 * @param preferences - the handy authorizationsSet to compare with
	 * @return an authorizationsSet including matching attributes for the sticky policy
	 */
	public AuthorizationsSetType getStickyAuthorizationsSet(HandyAuthorizationsSet preferences) {
		AuthorizationsSetType authSP = ofPrimelife.createAuthorizationsSetType();
		AuthorizationsMismatchType authMismatch = ofStickyPolicy.createAuthorizationsMismatchType();
		
		if (this.isUnknown() && (preferences.isUnknown || preferences.isEmpty())) {
			// policy is not defined (i.e. arbitrary) and preferences are empty
			// not comparable, so generally mismatch
			log.info("There is a general mismatch because the policy and the preferences are unknown");
			authSP.setMatching(false);		
			
		} else if (this.isEmpty() && (preferences.isUnknown || preferences.isEmpty())) {
			// matching since nothing is allowed
			log.info("There is a match because the policy is empty and so it does not allow anything.");
			authSP.setMatching(true);
			
		} else {
			// there is at least one Authorization Element either in the preferences or in the policy
			boolean matching = true;
			// Schema does not allow a general handling
			
			// handle the AuthzForDownstreamUsage
			HandyAuthzForDownstreamUsage prefDSU = preferences.getAuthzForDownstreamUsage();
			HandyAuthzForDownstreamUsage polDSU = this.getAuthzForDownstreamUsage();
			//AuthzUseForPurposeMismatchType mismatchId = HandyAuthzForPurpose.createMismatchType(polPurp, prefPurp);
			AuthorizationMatchingResult resultDSU = polDSU.createAuthorizationForSP(prefDSU);
			if (!resultDSU.isMatching()) {
				matching = false;
				if (resultDSU.getAuthForSP() != null) {
					log.debug("Mismatch, mismatch Id is: " + ((AuthzDownstreamUsageMismatchType)resultDSU.getMismatchType()).getMismatchId());
					authMismatch.setAuthzDownstreamUsage((AuthzDownstreamUsageMismatchType)resultDSU.getMismatchType());
					authSP.getAuthorization().add(resultDSU.getAuthForSP());
				}
			} else {
				// in case of a matching, the Item for the SP should always be set
				if (resultDSU.getAuthForSP() != null) {
					authSP.getAuthorization().add(resultDSU.getAuthForSP());
				}
			}			
			
			// handle the AuthzUseForPurpose
			HandyAuthzForPurpose prefPurp = preferences.getAuthzForPurpose();
			HandyAuthzForPurpose polPurp = this.getAuthzForPurpose();
			//AuthzUseForPurposeMismatchType mismatchId = HandyAuthzForPurpose.createMismatchType(polPurp, prefPurp);
			AuthorizationMatchingResult resultPurp = polPurp.createAuthorizationForSP(prefPurp);
			if (!resultPurp.isMatching()) {
				matching = false;
				if (resultPurp.getAuthForSP() != null) {
					log.debug("Mismatch, mismatch Id is: " + ((AuthzUseForPurposeMismatchType)resultPurp.getMismatchType()).getMismatchId());
					authMismatch.setAuthzUseForPurpose((AuthzUseForPurposeMismatchType)resultPurp.getMismatchType());
					authSP.getAuthorization().add(resultPurp.getAuthForSP());
				}
			} else {
				// in case of a matching, the Item for the SP should always be set
				if (resultPurp.getAuthForSP() != null) {
					authSP.getAuthorization().add(resultPurp.getAuthForSP());
				}
			}					
			
			authSP.setMatching(matching);
			
			//content of the mismatch should be:
			// for each mismatching element
			//policy is set and preferences are set accordingly
		}
		mismatch = authMismatch;
		
		return authSP;
	}
	/**
	 * Has to be called after {@link HandyAuthorizationsSet#getStickyAuthorizationsSet(HandyAuthorizationsSet)}
	 * This is empty if there was a matching.
	 * @return the authorizationsMismatchType which was produced during the matching
	 */
	public AuthorizationsMismatchType getStickyMismatches() {		
		return this.mismatch;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("AuthorizationsSet ");
		sb.append("is unknown: ");
		sb.append(this.isUnknown);
		sb.append(", is empty: ");
		sb.append(this.isEmpty());
		sb.append(", Authorizations: ");
		sb.append(this.getItems().size());
		return sb.toString();
	}
}
