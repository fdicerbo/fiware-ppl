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
package com.sap.research.primelife.ds.pdp.evaluation;

import org.herasaf.xacml.core.context.impl.DecisionType;

import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;

/**
 * Evaluation response class. Contains the access control decision for a
 * given PII attribute type. In case of permitted access, it contains also the
 * PII value and the applicable preferences policy.
 * 
 * 
 * @version 0.1
 * @date Nov 24, 2010
 */
public class EvaluationResponse {

	private String attributeValue;
	private DecisionType decision;
	private DataHandlingPreferencesType preferences;

	/**
	 * Attribute enforcement response constructor.
	 *
	 * @param decision		HERAS access control decision
	 */
	public EvaluationResponse(DecisionType decision) {
		this.decision = decision;
	}

	/**
	 * Returns access controls decision.
	 *
	 * @return access control decision
	 */
	public DecisionType getDecision() {
		return decision;
	}

	/**
	 * Returns data handling preferences associated with the PII (only if access
	 * control decision is <code>PERMIT</code>).
	 * 
	 * @return preferences	data handling preferences associated with the PII
	 */
	public DataHandlingPreferencesType getPreferences() {
		return preferences;
	}

	/**
	 * Sets data handling preferences for the requested PII.
	 *
	 * @param preferences	data handling preferences associated with the PII
	 */
	public void setPreferences(DataHandlingPreferencesType preferences) {
		this.preferences = preferences;
	}

	/**
	 * Returns PII value associated with the response (only if access
	 * control decision is <code>PERMIT</code>).
	 *
	 * @return	the PII value or <code>null</code> if access control decision was
	 * 			other than <code>PERMIT</code>
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets PII attribute value.
	 * @param attributeValue	the PII attribute value to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

}
