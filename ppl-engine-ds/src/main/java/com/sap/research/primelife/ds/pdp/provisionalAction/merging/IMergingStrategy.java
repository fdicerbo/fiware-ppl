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
package com.sap.research.primelife.ds.pdp.provisionalAction.merging;

import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;

import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;

/**
 * Interface to combine strategies for policy handling.
 * A policy and a preference object can be matched against each other. See {@link MatchStrategy}
 * </br>
 * Or the preference can be updated to match against the policy in the next request. See {@link UpdateStrategy}
 * 
 *
 */
public interface IMergingStrategy {

	/**
	 * Merges a policy and a data handling preference type.
	 * @param actionResponse - the response to be completed with matching information
	 * @param preferences - the data handling preferences (client side)
	 * @param policy - the data handling policy (server side)
	 * @throws ValidationException
	 * @throws WritingException
	 * @throws SyntaxException
	 */
	void processPolicy(ActionResponse actionResponse, DataHandlingPreferencesType preferences, DataHandlingPolicyType policy)
	throws ValidationException, WritingException, SyntaxException;
	
}
