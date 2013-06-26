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
package com.sap.research.primelife.ds.pdp.provisionalAction.filter;

import java.util.List;

import eu.primelife.ppl.pii.impl.PIIType;


/**
 * This Strategy is about handling the case that the search for PII returned an empty result.
 * The {@link ConsiderMissingPiiStrategy} adds a temporary PII with an empty value, so that the 
 * user can fill out the value later.
 * </br>
 * The {@link ConsiderOnlyPresentPiiStrategy} ignores missing PII, which is the case for downstream usage.
 * 
 *
 */
public interface IFilterPiiStrategy {

	/**
	 * Changes the piiList, may add a temporary PII with that attributeName or not.
	 * @param attributeName - the attributeName of the original request
	 * @param piiList - the pii-result list to change optionally
	 */
	void filter(String attributeName, List<PIIType> piiList);
	
	/**
	 * true if the matching should be performed although a pii is not in the database
	 * assumes that max. one pii is expected for the request
	 * @return
	 */
	boolean considerPii();
	
}
