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

import eu.primelife.ppl.policy.impl.AuthorizationType;

/**
 * A data container for the matching result of two AuthorizationTypes
 * like AuthzForDownstreamUsage.
 * 
 *
 */
public class AuthorizationMatchingResult {
	
	private boolean isMatching;
	private JAXBElement<? extends AuthorizationType> authForSP;
	private Object mismatchType;
	
	/**
	 * 
	 * @return true if there was a matching
	 */
	public boolean isMatching() {
		return isMatching;
	}
	
	/**
	 * The authorizationType for the sticky policy.
	 * Its matching attributes are set to false in case of mismatches.
	 * @return the authorizationForTheStickyPolicy or null
	 */
	public JAXBElement<? extends AuthorizationType> getAuthForSP() {
		return authForSP;
	}
	
	/**
	 * If there was a mismatch the MismatchType explains the differences.
	 * @return a mismatch type or null
	 * possible values:
	 * AuthzForDownstreamUsageMismatchType or AuthzUseForPurposeMismatchType
	 */
	public Object getMismatchType() {
		return mismatchType;
	}
	
	/**
	 * Initializer setting all attributes.
	 * @param isMatching
	 * @param authForSP
	 * @param mismatchType
	 */
	public AuthorizationMatchingResult(boolean isMatching,
			JAXBElement<? extends AuthorizationType> authForSP, Object mismatchType) {

		this.isMatching = isMatching;
		this.authForSP = authForSP;
		this.mismatchType = mismatchType;
	}
	
	

}
