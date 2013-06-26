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
package com.sap.research.primelife.ds.pdp.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.saml._2_0.assertion.AttributeStatementType;
import oasis.names.tc.saml._2_0.assertion.StatementAbstractType;

import com.sap.research.primelife.ds.pdp.evaluation.EvaluationResponse;
import com.sap.research.primelife.ds.pep.PEP;

import eu.primelife.ppl.stickypolicy.impl.AttributeType;

/**
 * Data Transfer Object to represent the response of Evaluation and Matching.
 * 
 */
public class ActionResponse {

	private String attributeName;
	private EvaluationResponse enforcementResponse;
	private AttributeType attributeSP;

	/**
	 * Provisional action object constructor.
	 *
	 * @param attributeName	PII attribute name
	 * @param enforcementResponse	attribute enforcement response
	 */
	public ActionResponse(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Returns PII attribute name.
	 *
	 * @return PII attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Sets attribute enforcement response.
	 *
	 * @return enforcementResponse	attribute enforcement response
	 */
	public void setEnforcementResponse(EvaluationResponse enforcementResponse) {
		this.enforcementResponse = enforcementResponse;
	}

	/**
	 * Returns attribute enforcement response.
	 *
	 * @return attribute enforcement response
	 */
	public EvaluationResponse getEnforcementResponse() {
		return enforcementResponse;
	}

	/**
	 * Returns sticky policy as {@link AttributeType} object.
	 *
	 * @return sticky policy as {@link AttributeType} object if there was
	 * 		a matching, otherwise <code>null</code>.
	 */
	public AttributeType getAttributeSP() {
		return attributeSP;
	}

	/**
	 * Sets sticky policy represented as {@link AttributeType} object.
	 *
	 * @param attributeSP	sticky policy
	 */
	public void setAttributeSP(AttributeType attributeSP) {
		this.attributeSP = attributeSP;
	}

	/**
	 * Creates an SAML AssertionType with an attribute according to this
	 * response.
	 *
	 * @return	SAML assertion
	 * @throws DatatypeConfigurationException
	 */
	public AssertionType toSamlAssertion()
			throws DatatypeConfigurationException {
		eu.primelife.ppl.claims.impl.AttributeType attribute =	new eu.primelife.ppl.claims.impl.AttributeType();

		//put the attribute name
		attribute.setName(attributeName);
		//put the Sticky Policy reference
		attribute.setStickyPolicyId(attributeSP.getID());

		//put the attribute value of the PII
		List<Object> attributeValueList = new ArrayList<Object>();
		//lisAttributeValue.add(ofSAML.createAttributeValue(actionResponse.getAttributeValue()));
		attributeValueList.add(enforcementResponse.getAttributeValue());

		attribute.setAttributeValue(attributeValueList);

		//List of attribute
		List<Object> attributeList = new ArrayList<Object>();
		//add the attribute to the list of the attribute
		attributeList.add(attribute);

		AttributeStatementType attributeStatementTmp =	new AttributeStatementType();
		attributeStatementTmp.setAttributeOrEncryptedAttribute(attributeList);

		List<StatementAbstractType> listStatementTmp =	new ArrayList<StatementAbstractType>();
		listStatementTmp.add(attributeStatementTmp);

		AssertionType assertion = PEP.createSamlAssertion();
		assertion.setStatementOrAuthnStatementOrAuthzDecisionStatement(listStatementTmp);

		return assertion;
	}

}
