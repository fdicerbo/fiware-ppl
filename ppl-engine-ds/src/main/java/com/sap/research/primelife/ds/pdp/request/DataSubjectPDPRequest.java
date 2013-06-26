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
package com.sap.research.primelife.ds.pdp.request;

import javax.xml.bind.JAXBException;

import com.sap.research.primelife.ds.pdp.provisionalAction.filter.ConsiderMissingPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.IFilterPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.IMergingStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.MatchStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.IPIIQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.PiiQueryByAttributeNameStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPreferenceGroupStrategy;
import com.sap.research.primelife.ds.pdp.response.IResponseHandlingStrategy;
import com.sap.research.primelife.ds.pdp.response.ResponseForUserStrategy;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;

/**
 * Represents the request for accessResource:
 * Configuration for request handling: 
 * <li>PIIs are queried by attribute name</li>
 * <li>Missing PIIs are temporarily created</li>
 * <li>Policies are selected by preference group</li>
 * <li>Policies are matched</li>
 * <li>All responses are sent in the claims</li>
 * 
 *
 */
public class DataSubjectPDPRequest extends PDPRequest {
	
	private String preferenceGroup;

	public DataSubjectPDPRequest(String policy, String preferenceGroup) throws SyntaxException,
			JAXBException {
		super(policy);
		this.preferenceGroup = preferenceGroup;
	}

	@Override
	public IPIIQueryStrategy getPIILoadingStrategy() {
		return new PiiQueryByAttributeNameStrategy();
	}

	@Override
	public IPolicyQueryStrategy getPolicyQueryStrategy() throws MissingPreferenceGroupException {
		return new PolicyByPreferenceGroupStrategy(this.preferenceGroup);
	}

	@Override
	public IResponseHandlingStrategy getResponseHandlingStrategy() {
		return new ResponseForUserStrategy();
	}

	@Override
	public IFilterPiiStrategy getFilterPiiStrategy() {
		return new ConsiderMissingPiiStrategy();
	}

	@Override
	public IMergingStrategy getMergingStrategy() {
		return new MatchStrategy();
	}

}
