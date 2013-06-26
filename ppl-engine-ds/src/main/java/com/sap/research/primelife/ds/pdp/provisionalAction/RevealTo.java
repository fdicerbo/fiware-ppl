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

import java.io.FileNotFoundException;
import java.util.List;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;

/**
 * 
 * 
 * @Version 0.1
 * @Date May 6, 2010
 * 
 */
public class RevealTo implements
	IProvisionalAction, IUseThirdParty, IUseCredentials, IUseAccessControl {
	
	protected String attributeName;
	protected String thirdParty;
	protected String credentialIdReference;
	protected RequestType request;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(RevealTo.class);
	
	public RevealTo(String attributeName) {
		this.setAttributeName(attributeName);
	}
	
	@Override
	public void setXacmlRequest(RequestType xacmlRequest) {
		this.request = xacmlRequest;
	}

	@Override
	public void setCredentialIdReference(String credentialIdReference) {
		this.credentialIdReference = credentialIdReference;
	}

	@Override
	public void setThirdParty(String thirdPartyURI) {
		this.thirdParty = thirdPartyURI;
	}

	@Override
	public List<ActionResponse> handle() throws SyntaxException,
			FileNotFoundException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException, WritingException {
		throw new UnsupportedOperationException("RevealTo is not implemented yet.");
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}	
}
