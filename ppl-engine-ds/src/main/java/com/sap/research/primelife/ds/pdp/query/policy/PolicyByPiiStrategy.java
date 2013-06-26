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
package com.sap.research.primelife.ds.pdp.query.policy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.AuthorizationType;
import eu.primelife.ppl.policy.impl.AuthorizationTypeAnyItem;
import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem;
import eu.primelife.ppl.policy.impl.AuthzDownstreamUsageType;
import eu.primelife.ppl.policy.impl.DataHandlingPreferencesType;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.RuleType;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.xacml.impl.EffectType;
import eu.primelife.ppl.policy.xacml.impl.TargetType;

/**
 * gets the downstream usage policy connected to the pii if present
 * otherwise returns the root policy
 * 
 * This is used in the Sticky Policy scenarios where the PII is on a data controller and is requested by a third party.
 * 
 *
 */
public class PolicyByPiiStrategy implements IPolicyQueryStrategy {

	private Logger LOGGER = LoggerFactory.getLogger(PolicyByPiiStrategy.class);
	private String defaultPreferenceGroup;
	private PolicyDao dao; 
	private static final eu.primelife.ppl.policy.impl.ObjectFactory of =
		new eu.primelife.ppl.policy.impl.ObjectFactory();
	
	/**
	 * 
	 * @param defaultPreferenceGroup - the preference group to choose when the PII has no sticky policy
	 * @throws MissingPreferenceGroupException (deactivated)
	 */
	public PolicyByPiiStrategy(String defaultPreferenceGroup){
		this.defaultPreferenceGroup = defaultPreferenceGroup;
		this.dao = new PolicyDao();
//		if (!dao.getAllPreferenceGroups().contains(defaultPreferenceGroup)){
//			throw new MissingPreferenceGroupException(defaultPreferenceGroup);
//		}
			
	}
	
	/**
	 * Select the sticky policies' downstream usage policy.
	 * If there is no sticky policy, select the server's default policy.
	 * 
	 */
	/*
	 * (non-Javadoc)
	 * @see com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy#executeQuery(eu.primelife.ppl.pii.impl.PIIType)
	 */
	@Override
	public List<Object> executeQuery(PIIType pii) {
		List<Object> policySetOrPolicy = pii.getPolicySetOrPolicy();
		// There is no policy assigned to the PII
		if (policySetOrPolicy.isEmpty()){
			LOGGER.info("The PII attributeName=" + pii.getAttributeName() 
					+ " attributeValue = " + pii.getAttributeValue() + " has no Sticky Policy. " +
							"Taking as default the preferenceGroup " + this.defaultPreferenceGroup);
			// Use the servers policy
			List<Object> policySet = new LinkedList<Object>();
			Object policy = dao.getPreferenceGroupPolicy(this.defaultPreferenceGroup);
			policySet.add(policy);
			return  policySet;				
		} else {
			return selectDownstreamUsagePolicy(pii.getPolicySetOrPolicy());
		}
		
	}
	
	/**
	 * Selects the downstream usage policy of the PII or if not present, the sticky policy.
	 * The data handling preferences have to be inside a rule element to be processed later.
	 * @param policySetOrPolicy - the policies connected to the PII
	 * @return a policySet or policy to be used for matching and access control; if downstream usage is not allowed, it returns null
	 */
	@SuppressWarnings("unchecked")
	private List<Object> selectDownstreamUsagePolicy(List<Object> policySetOrPolicy) {
		
		boolean dsuAllowed = true;
		List<Object> policySet = policySetOrPolicy;
		
		PolicyType policy = (PolicyType) policySetOrPolicy.get(0);
		//LOGGER.info(policy.toString());
		AuthorizationsSetType authSet = (AuthorizationsSetType) policy.getStickyPolicy().getAuthorizationsSet();		

		for (AuthorizationsSetTypeAuthorizationItem authItem : authSet.getAuthorizationItems()) {
			AuthorizationType authItemValue = authItem.getItemValue();
			//get the AuthzDownstreamUsage item
			if (authItemValue instanceof AuthzDownstreamUsageType) {
				AuthzDownstreamUsageType adu = (AuthzDownstreamUsageType) authItemValue;
				LOGGER.info("Authorization for Downstream Usage: " + adu.getAllowed());

				if (!Boolean.valueOf(adu.getAllowed())) {
					dsuAllowed = false;
					break;
				}

				List<AuthorizationTypeAnyItem> anyItems = adu.getAnyItems();

				// instead use nested policy if it exists
				if (!anyItems.isEmpty()) {
					LOGGER.info("Nested policy found");
					policySet = new ArrayList<Object>(1);
					policySet.add(((JAXBElement<PolicyType>) anyItems.get(0).getItem()).getValue());
				} else {
					LOGGER.info("No Nested policy found");
					//create a new temporary rule with the root preferences
					DataHandlingPreferencesType prefs = new DataHandlingPreferencesType();
					prefs.setAuthorizationsSet(authSet);
					prefs.setObligationsSet((ObligationsSet)policy.getStickyPolicy().getObligationsSet());
					RuleType rule = new RuleType();
					rule.setDataHandlingPreferences(prefs);
					rule.setEffect(EffectType.PERMIT);
					rule.setRuleId("downstreamUsageDefaultPolicy");
					rule.setTarget(new TargetType());
					JAXBElement<RuleType> jaxbItem = of.createRule(rule);
					policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().add(jaxbItem);
				}
			}
		}
		if (dsuAllowed) {
			return policySet;
		} else {
			return null;
		}
	}

}
