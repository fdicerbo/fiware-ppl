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

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.utils.IdGenerator;

import eu.primelife.ppl.policy.impl.AuthorizationType;
import eu.primelife.ppl.policy.impl.AuthzUseForPurpose;
import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.authorization.impl.AuthzUseForPurposeMismatchType;
import eu.primelife.ppl.stickypolicy.authorization.impl.PurposeListType;

/**
 * Wrapper class for matching and updating AuthzUseForPurpose elements
 * For an overview of the matching and updating algorithm, see the file authorizationMatching.pdf
 * 
 *
 */
public class HandyAuthzForPurpose implements HandyAuthorization {
	
	private List<String> purposes;
	private JAXBElement<? extends AuthorizationType> original = null;
	private ObjectFactory purposeFactory = new ObjectFactory();
	private boolean isDefined;
	private Logger log = LoggerFactory.getLogger(HandyAuthzForPurpose.class);
	
	private static eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory ofStickyPolicy 
	= new eu.primelife.ppl.stickypolicy.authorization.impl.ObjectFactory();

	@SuppressWarnings("unchecked")
	public HandyAuthzForPurpose(JAXBElement<? extends AuthorizationType> auth) {
		purposes = new LinkedList<String>();
		if (auth == null) {
			this.setDefined(false);
		} else {
			this.setDefined(true);
			for (Object obj : auth.getValue().getAny()) {
				purposes.add(((JAXBElement<String>) obj).getValue());			
			}
			this.original = auth;
		}
	}

	/**
	 * 
	 * @return the list of purposes which data shall be authorized for
	 */
	public List<String> getPurposes() {
		return purposes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JAXBElement<AuthzUseForPurpose> getOriginal(){
		return (JAXBElement<AuthzUseForPurpose>)this.original;
	}

	/**
	 * Creates an authorizationType which will not cause mismatches with the same policy next time.
	 * @param preference - the preference object which is in mismatch with the policy (this)
	 * @return an AuthzUseForPurpose which won't mismatch with {@code this}
	 */
	public JAXBElement<? extends AuthorizationType> createUpdatedPreference(HandyAuthzForPurpose preference) {
		
		if (!this.isDefined && !preference.isDefined) {
			// mismatch, no preferences available
			log.info("There is a general mismatch in AuthorizationForPurpose because neither the policy nor the preferences are defined.");
			log.info("Creating a new AuthzForPurpose with the any Purpose.");
			JAXBElement<AuthzUseForPurpose> authPurp = purposeFactory.createAuthzUseForPurpose(new AuthzUseForPurpose());
			JAXBElement<String> purposeElement =  purposeFactory.createPurpose("http://www.primelife.eu/purposes/any");
			authPurp.getValue().getAny().add(purposeElement);
			return authPurp;
			
		} else if (!this.isDefined && preference.isDefined) {
			// copy the preferences and add the any Purpose
			JAXBElement<String> purposeElement =  purposeFactory.createPurpose("http://www.primelife.eu/purposes/any");
			if (!preference.getPurposes().contains("http://www.primelife.eu/purposes/any"))
				preference.getOriginal().getValue().getAny().add(purposeElement);
			return preference.getOriginal();
			
		} else if (this.isDefined && !preference.isDefined) {
			// copy the policy
			log.info("There is a general mismatch in AuthorizationForPurpose because the preferences are not defined.");
			return this.getOriginal();
		} else {
			// merge the purposes
			for (String purpose : this.getPurposes()){
				if (!preference.getPurposes().contains(purpose)){
					//we have to add the purpose to make it matching
					//create a ppl:Purpose
					JAXBElement<String> purposeElement =  purposeFactory.createPurpose(purpose);
					preference.getOriginal().getValue().getAny().add(purposeElement);
				}
			}
			return preference.getOriginal();
		}
	}

	/**
	 * Matches with the preferences, creating an AuthzUseForPurpose element with matching attributes.
	 * @param preference - the handy authorization to compare with
	 * @return an AuthzUseForPurpose for the sticky policy
	 */
	public AuthorizationMatchingResult createAuthorizationForSP(HandyAuthzForPurpose preference) {
		AuthzUseForPurposeMismatchType mismatchType = null;
		if (!this.isDefined && !preference.isDefined) {
			// mismatch, no preferences available
			log.info("There is a general mismatch in AuthorizationForPurpose because neither the policy nor the preferences are defined.");
			return new AuthorizationMatchingResult(false, null, null);
		} else if (!this.isDefined && preference.isDefined) {
			// copy the preferences, but it is still a mismatch
			if (preference.getPurposes().contains("http://www.primelife.eu/purposes/any")) {
				log.info("There is a match in AuthorizationForPurpose because the preferences allow any purpose.");
				preference.getOriginal().getValue().setMatching(true);
				return new AuthorizationMatchingResult(true, preference.getOriginal(), mismatchType);
			} else {
				log.info("There is a general mismatch in AuthorizationForPurpose because the policy is not defined.");
				mismatchType = createMismatchType(this, preference);
				preference.getOriginal().getValue().setMatching(false);
				preference.getOriginal().getValue().setMismatchId(mismatchType);
				return new AuthorizationMatchingResult(false, preference.getOriginal() , mismatchType);
			}
		} else if (this.isDefined && !preference.isDefined) {
			// copy the policy, but it is also a mismatch
			log.info("There is a general mismatch in AuthorizationForPurpose because the preferences are not defined.");
			mismatchType = createMismatchType(this, preference);
			this.getOriginal().getValue().setMatching(false);
			this.getOriginal().getValue().setMismatchId(mismatchType);
			return new AuthorizationMatchingResult(false, this.getOriginal() , mismatchType);
		} else {
			
			// result of the matching depends on the values
			boolean matching = true;
			for (String purpose : this.purposes) {
				if (!preference.getPurposes().contains(purpose) 
						&& !preference.getPurposes().contains("http://www.primelife.eu/purposes/any")) {
					matching = false;
				}
			}
			this.getOriginal().getValue().setMatching(matching);
			
			if (!matching) {
				mismatchType = createMismatchType(this, preference);
				this.getOriginal().getValue().setMismatchId(mismatchType);
			}
			log.info("The result of the matching in AuthorizationForPurpose is "+ matching);
			return new AuthorizationMatchingResult(matching, this.getOriginal(), mismatchType);
		}
		
	}
	
	/**
	 * Creates an AuthzUseForPurposeMismatchType for the mismatches part of the sticky policy.
	 * @param policy - the HandyAuthzForPurpose of the server
	 * @param preferences - the HandyAuthzForPurpose of the client
	 * @return the AuthzUseForPurposeMismatchType showing both values; can be used as a mismatch id for the SP 
	 */
	public static AuthzUseForPurposeMismatchType createMismatchType(HandyAuthzForPurpose policy, HandyAuthzForPurpose preferences) {
		
		AuthzUseForPurposeMismatchType authMismatch = ofStickyPolicy.createAuthzUseForPurposeMismatchType();
		String mismatchId = "mismatch" + IdGenerator.generatePositiveInt();
		authMismatch.setMismatchId(mismatchId);
		
		PurposeListType policyPurposes = ofStickyPolicy.createPurposeListType();
		PurposeListType preferencePurposes = ofStickyPolicy.createPurposeListType();
		if (policy != null) {
			for (String purpose : policy.getPurposes()) {
				policyPurposes.getPurpose().add(purpose);
			}
		}
		if (preferences != null) {
			for (String purpose : preferences.getPurposes()) {
				preferencePurposes.getPurpose().add(purpose);
			}
		}
		authMismatch.setPolicy(policyPurposes);
		authMismatch.setPreference(preferencePurposes);
		return authMismatch;
	}

	public void setDefined(boolean isDefined) {
		this.isDefined = isDefined;
	}

	public boolean isDefined() {
		return isDefined;
	}
}
