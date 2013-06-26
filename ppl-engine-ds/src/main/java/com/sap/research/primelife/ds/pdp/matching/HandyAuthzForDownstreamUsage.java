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
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.utils.IdGenerator;

import eu.primelife.ppl.policy.impl.AuthorizationType;
import eu.primelife.ppl.policy.impl.AuthzDownstreamUsageType;
import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.authorization.impl.AuthzDownstreamUsageMismatchType;

/**
 * Wrapper class for matching and updating AuthzForDownstreamUsage elements.
 * For an overview of the matching and updating algorithm, see the file authorizationMatching.pdf
 * 
 *
 */
public class HandyAuthzForDownstreamUsage implements HandyAuthorization {
	
	private Boolean isAllowed;
	private JAXBElement<? extends AuthorizationType> original;
	private Logger log = LoggerFactory.getLogger(HandyAuthzForDownstreamUsage.class);
	private static eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory ofStickyPolicy 
	= new eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory();
	private static ObjectFactory ofPrimelife = new ObjectFactory();
	private boolean isDefined;
	
	public HandyAuthzForDownstreamUsage(JAXBElement<? extends AuthorizationType> auth) {
		if (auth == null) {
			this.isDefined = false;
		} else {
			this.isDefined = true;
			this.isAllowed = Boolean.valueOf(((AuthzDownstreamUsageType)auth.getValue()).getAllowed());
			this.original = auth;
		}
	}

	/**
	 * 
	 * @return a boolean value for the getAllowed attribute, null if the AuthzForDownstreamUsage is not defined.
	 */
	public Boolean getIsAllowed() {
		return isAllowed;
	}

	/*
	 * (non-Javadoc)
	 * @see com.sap.research.primelife.ds.pdp.matching.HandyAuthorization#getOriginal()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JAXBElement<AuthzDownstreamUsageType> getOriginal(){
		return (JAXBElement<AuthzDownstreamUsageType>)this.original;
	}
	/**
	 * 
	 * @return true if the original AuthzDownstreamUsage existed, false if it was null
	 */
	public boolean isDefined() {
		return isDefined;
	}

	/**
	 * Creates an authorizationType which will not cause mismatches with the same policy next time.
	 * For an overview of the matching algorithm, see the file final_matching_spec.docx
	 * @param preference - the element to be taken as preference
	 * @return an AuthzDownstreamUsage element 
	 */
	public JAXBElement<? extends AuthorizationType> createUpdatedPreference(HandyAuthzForDownstreamUsage preference) {
		
		if (!this.isDefined && !preference.isDefined) {
			// create an AuthDSU with allowed = true
			log.info("There is a mismatch in the downstream usage: neither policy nor preferences are defined");
			JAXBElement<AuthzDownstreamUsageType> auth = ofPrimelife.createAuthzDownstreamUsage(new AuthzDownstreamUsageType());
			auth.getValue().setAllowed("true");
			return auth;
		} else if (!this.isDefined && preference.isDefined) {
			// copy the preferences, but it is still a mismatch
			if (preference.getIsAllowed()) {
				log.info("There is a match in the downstream usage because the preferences allow it.");
				return preference.getOriginal();
			} else {
				log.info("There is a mismatch in the downstream usage because the policy is not defined.");
				log.info("Updating the preferences to allow downstream usage.");
				preference.getOriginal().getValue().setAllowed("true");
				return preference.getOriginal();	
			}
		} else if (this.isDefined && !preference.isDefined) {
			if (!this.getIsAllowed()) {
				log.info("There is a match in the downstream usage because the policy does not allow it.");
				log.info("Creating an auth for Downstream Usage object in the preferences, not allowing downstream usage.");
				JAXBElement<AuthzDownstreamUsageType> auth = ofPrimelife.createAuthzDownstreamUsage(new AuthzDownstreamUsageType());
				auth.getValue().setAllowed("false");
				return auth;
			} else {				
				// copy the policy, but it is also a mismatch
				log.info("There is a mismatch in the downstream usage because the preferences are not defined.");
				log.info("Creating an auth for Downstream Usage object in the preferences, allowing downstream usage.");
				JAXBElement<AuthzDownstreamUsageType> auth = ofPrimelife.createAuthzDownstreamUsage(new AuthzDownstreamUsageType());
				auth.getValue().setAllowed("true");
				return auth;
			}
		} else {
	
			if (this.isAllowed && !preference.getIsAllowed()) {
				//this is a mismatch
				//set preference downstream usage to true
				preference.getOriginal().getValue().setAllowed("true");
			} else {
				log.debug("No updating of downstream usage preferences necessary");
				// this is a match, no need to change
			}
			return preference.getOriginal();
		}
	}

	/**
	 * Matches with the preferences, creating an AuthzDownstreamUsage element with matching attributes.
	 * @param preference - the handy authorization to compare with
	 * @return an AuthorizationMatchingResult including the sticky policy
	 */
	public AuthorizationMatchingResult createAuthorizationForSP(HandyAuthzForDownstreamUsage preference) {
		
		AuthzDownstreamUsageMismatchType mismatchType = null;
		if (!this.isDefined && !preference.isDefined) {
			// mismatch, no preferences available
			log.info("There is a mismatch in the downstream usage: neither policy nor preferences are defined");
			return new AuthorizationMatchingResult(false, null, null);
		} else if (!this.isDefined && preference.isDefined) {
			// copy the preferences, but it is still a mismatch
			if (preference.getIsAllowed()) {
				log.info("There is a match in the downstream usage because the preferences allow it.");
				preference.getOriginal().getValue().setMatching(true);
				return new AuthorizationMatchingResult(true, preference.getOriginal() , mismatchType);		
			} else {
				log.info("There is a mismatch in the downstream usage because the policy is not defined.");
				mismatchType = createMismatchType(this, preference);
				preference.getOriginal().getValue().setMatching(false);
				preference.getOriginal().getValue().setMismatchId(mismatchType);
				return new AuthorizationMatchingResult(false, preference.getOriginal() , mismatchType);				
			}
		} else if (this.isDefined && !preference.isDefined) {
			if (!this.getIsAllowed()) {
				log.info("There is a match in the downstream usage because the policy does not allow it.");
				this.getOriginal().getValue().setMatching(true);
				return new AuthorizationMatchingResult(true, this.getOriginal() , mismatchType);
			} else {				
				// copy the policy, but it is also a mismatch
				log.info("There is a mismatch in the downstream usage because the preferences are not defined.");
				mismatchType = createMismatchType(this, preference);
				this.getOriginal().getValue().setMatching(false);
				this.getOriginal().getValue().setMismatchId(mismatchType);
				return new AuthorizationMatchingResult(false, this.getOriginal() , mismatchType);
			}
		} else {
			// result of the matching depends on the values
			boolean matching = !this.isAllowed || (this.isAllowed && preference.isAllowed); 
			
			if (this.isAllowed) {
				//copy downstream usage policy of the user
				this.getOriginal().getValue().setAny((preference.getOriginal().getValue().getAny()));
			}
			this.getOriginal().getValue().setMatching(matching);
			
			if (!matching) {
				mismatchType = createMismatchType(this, preference);
				this.getOriginal().getValue().setMismatchId(mismatchType);
			}
			log.info("The result of the downstream usage match is: " + matching);
			return new AuthorizationMatchingResult(matching, this.getOriginal(), mismatchType);
		}
	}
	
	/**
	 * Creates an AuthzDownstreamUsageMismatchType for the mismatches part of the sticky policy.
	 * @param policy - the HandyAuthzForDownstreamUsage of the server
	 * @param preferences - the HandyAuthzForDownstreamUsage of the client
	 * @return the AuthzDownstreamUsageMismatchType showing both values; can be used as a mismatch id (set by JAXB) for the SP 
	 */
	public static AuthzDownstreamUsageMismatchType createMismatchType(HandyAuthzForDownstreamUsage policy, 
			HandyAuthzForDownstreamUsage preferences) {
		
		AuthzDownstreamUsageMismatchType authMismatch = ofStickyPolicy.createAuthzDownstreamUsageMismatchType();
		String mismatchId = "mismatch" + IdGenerator.generatePositiveInt();
		authMismatch.setMismatchId(mismatchId);
		
		AuthorizationType policyMm = ofPrimelife.createAuthorizationType();
		AuthorizationType preferencesMm = ofPrimelife.createAuthorizationType();
		if (policy != null && policy.getIsAllowed() != null) {
			policyMm.getOtherAttributes().put(new QName("allowed"), policy.getIsAllowed().toString());			
		}
		if (preferences != null && preferences.getIsAllowed() != null) {
			preferencesMm.getOtherAttributes().put(new QName("allowed"), preferences.getIsAllowed().toString());
		}
		authMismatch.setPolicy(policyMm);
		authMismatch.setPreference(preferencesMm);
		return authMismatch;
	}

	
}
