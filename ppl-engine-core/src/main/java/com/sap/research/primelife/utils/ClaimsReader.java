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
package com.sap.research.primelife.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.saml._2_0.assertion.AttributeStatementType;
import oasis.names.tc.saml._2_0.assertion.AttributeStatementTypeAttributeOrEncryptedAttributeItem;
import oasis.names.tc.saml._2_0.assertion.StatementAbstractType;

import com.sap.research.primelife.exceptions.ValidationException;

import eu.primelife.ppl.claims.impl.ClaimType;
import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.claims.impl.Decision;
import eu.primelife.ppl.claims.impl.ResponseType;
import eu.primelife.ppl.claims.impl.StickyPolicyStatementType;
import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.StickyPolicyType;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.MismatchesType;


/**
 * Helper class to read the claims format and provide an easy acces to the attributes.
 * 
 *
 */
public class ClaimsReader {
	
	private Decision decision;
	private List<String> missingPII = new LinkedList<String>();
	private List<String> deniedPII = new LinkedList<String>();
	
	private List<PIIType> piiList = new LinkedList<PIIType>();
	private List<PIIObject> piiObjectList = new LinkedList<PIIObject>();
	/**
	 * Map the sticky Policy Id to the contained data handling policy
	 */
	private Map<String, List<Object>> stickyPolicies = new HashMap<String, List<Object>>();
	/**
	 * Map the sticky policy id to the contained mismatch type
	 */
	private Map<String, MismatchesType> mismatches = new HashMap<String, MismatchesType>();
	
	private List<AttributeType> attributes = new LinkedList<AttributeType>();
	
	private static final eu.primelife.ppl.policy.impl.ObjectFactory of = new eu.primelife.ppl.policy.impl.ObjectFactory();
	private static Logger logger = LoggerFactory.getLogger(ClaimsReader.class);
	
	/**
	 * Temporary PII to store information associated with that pii.
	 * 
	 *
	 */
	public class PIIObject {
		private Integer id;
		private String attributeName;
		private String attributeValue;
		private String stickyPolicyId;
		
		protected PIIObject(Integer id, String attributeName, String attributeValue, String spId) {
			this.id = id;
			this.attributeName = attributeName;
			this.attributeValue = attributeValue;
			this.stickyPolicyId = spId;
		}

		public Integer getId() {
			return id;
		}

		public String getAttributeName() {
			return attributeName;
		}

		public String getAttributeValue() {
			return attributeValue;
		}

		public String getStickyPolicyId() {
			return stickyPolicyId;
		}
		
		
	}
	
	/**
	 * Create the ClaimsReader object containing easy accessible information of claims.
	 * @param claims - the JAXBElement 
	 */
	public ClaimsReader(ClaimsType claims) {
		for (ClaimType claim : claims.getClaim()) {

			extractDecision(claim.getAssertion());
			extractPII(claim.getAssertion());
			extractStickyPolicies(claim.getAssertion());
		}
	}
	
	/**
	 * helper method which extracts the sticky policy of an attribute type and converts it to a PolicyType
	 * with a sticky policy.
	 * @param attribute - the attribute from the claims
	 * @return a List containing exactly one {@link PolicyType} object
	 */
	private List<Object> convertToPolicyList(AttributeType attribute){
		StickyPolicyType sp = of.createStickyPolicyType();
		sp.setAuthorizationsSet(attribute.getAuthorizationsSet());
		sp.setObligationsSet(attribute.getObligationsSet());

		PolicyType policy = of.createPolicyType();
		policy.setStickyPolicy(sp);
		List<Object> policies = new ArrayList<Object>(1);
		policies.add(policy);
		return policies;
	}
	
	/**
	 * Read the PII with attribute names and values plus references to sticky policies from the AttributeStatements
	 * @param assertions - all assertion elements from the claims
	 */
	private void extractPII(List<AssertionType> assertions) {
		int i = 1;
		for (AssertionType assertion : assertions) {
			for (StatementAbstractType statement : assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
				if (statement instanceof AttributeStatementType) {
					AttributeStatementType attrStatement = (AttributeStatementType) statement;
					
					for (AttributeStatementTypeAttributeOrEncryptedAttributeItem attr : attrStatement.getAttributeOrEncryptedAttributeItems()) {
						oasis.names.tc.saml._2_0.assertion.AttributeType itemAttr = attr.getItemAttribute();
						eu.primelife.ppl.claims.impl.AttributeType claimsAttributeType = (eu.primelife.ppl.claims.impl.AttributeType) itemAttr;
						String attrName = itemAttr.getName();
						
						// create PII according to attribute name and value
						PIIType pii = new PIIType();
						pii.setAttributeName(attrName);
						pii.setAttributeValue(((String) itemAttr.getAttributeValue().get(0)).trim());
						String stickyPolicyID = claimsAttributeType.getStickyPolicyId();
						if (stickyPolicyID == null) {
							logger.warn("A sticky policy id was null!");
						}
						piiList.add(pii);
						piiObjectList.add(new PIIObject(i, pii.getAttributeName(), pii.getAttributeValue(), stickyPolicyID));
						i++;
					}
				}
			}
		}
		
	}
	
	/**
	 * Read all the sticky policies and mismatch elements from the StickyPolicyStatements
	 * @param assertions - all Assertions of the claims
	 */
	private void extractStickyPolicies(List<AssertionType> assertions) {
		for (AssertionType assertion : assertions) {
			for (StatementAbstractType statement : assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
				if (statement instanceof StickyPolicyStatementType) {
					StickyPolicyStatementType spStatement = (StickyPolicyStatementType) statement;
						
					for (AttributeType attr : spStatement.getAttribute()) {
						attributes.add(attr);
						stickyPolicies.put(attr.getID(), convertToPolicyList(attr));
						mismatches.put(attr.getID(), attr.getMismatches());
					}
				}
			}
		}
	}

	/**
	 * Read the final decision, missing and denied PIIs from the ResponseType
	 * @param assertions - all assertions of the claims
	 */
	private void extractDecision(List<AssertionType> assertions) {
		for (AssertionType assertion : assertions) {
			for (StatementAbstractType statement : assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
				if (statement instanceof ResponseType) {
					ResponseType response = (ResponseType) statement;
					this.decision = response.getDecision();
					this.missingPII.addAll(response.getMissingPII().getValue());
					this.deniedPII.addAll(response.getDeneyPII().getValue());
				}
			}
		}
	}
	
	/**
	 * get the sticky policy for this pii using the sticky policy id.
	 * This only works as long as the pii was not changed (e.g. by persisting it)
	 * @param pii
	 * @return the pii's sticky policy from the claims
	 */
	public List<Object> getStickyPolicyOfPii(PIIObject pii) {
		String spId = null;
		for (PIIObject piiTemp : this.piiObjectList) {
			if (piiTemp.getAttributeName().equals(pii.getAttributeName()) 
					&& piiTemp.getAttributeValue().equals(pii.getAttributeValue()) 
					&& piiTemp.getStickyPolicyId().equals(pii.getStickyPolicyId())) {
				spId = piiTemp.getStickyPolicyId();
			}
		}
		if (spId == null) {
			throw new ValidationException("The PII " + pii.toString() + "does not occur in the claims.");
		}
		List<Object> stickyPolicy = this.getStickyPolicies().get(spId);
		if (stickyPolicy == null) {
			//there is no sticky policy for this pii
		}
		return stickyPolicy;
	}
	
	/**
	 * returns the obligations set of the PII's sticky policy. The sticky policy must be contained in the PII.
	 * @param pii
	 * @return an ObligationsSet
	 */
	public static ObligationsSet getObligationsSetOfStickyPolicy(PIIType pii) {
		List<Object> policy = pii.getPolicySetOrPolicy();
		return ((PolicyType) policy.get(0)).getStickyPolicy().getObligationsSet();
	}
	
	public List<PIIType> getPiiList() {
		return piiList;
	}
	public Map<String, List<Object>> getStickyPolicies() {
		return stickyPolicies;
	}
	public Decision getDecision() {
		return decision;
	}
	public List<String> getMissingPII() {
		return missingPII;
	}
	public List<String> getDeniedPII() {
		return deniedPII;
	}
	public Map<String, MismatchesType> getMismatches() {
		return mismatches;
	}
	
	public List<AttributeType> getAttributes(){
		return attributes;
	}
	
	public List<PIIObject> getPiiObjects() {
		return this.piiObjectList;
	}
	
	public List<String> getPIIAttributeNames() {
		List<String> names = new LinkedList<String>();
		for (PIIType pii : this.piiList) {
			names.add(pii.getAttributeName());
		}
		return names;
	}
	
	
}
