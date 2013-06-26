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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * finds the specified preference group in the database
 * to be used for queries on DS
 * 
 *
 */
public class PolicyByPreferenceGroupStrategy implements IPolicyQueryStrategy {

	private String preferenceGroup;
	private PolicyDao dao;
	private static Logger logger = LoggerFactory.getLogger(PolicyByPreferenceGroupStrategy.class); 
	
	/**
	 * 
	 * @param preferenceGroup
	 * @throws MissingPreferenceGroupException - if the preference group was not found
	 */
	public PolicyByPreferenceGroupStrategy(String preferenceGroup) throws MissingPreferenceGroupException {
		this.preferenceGroup = preferenceGroup;
		dao = new PolicyDao();
		if (preferenceGroup == null ) {
			if (dao.getAllPreferenceGroups().size() == 0) {
				throw new MissingPreferenceGroupException(preferenceGroup);
			}
			//ignore
		} else if (!dao.getAllPreferenceGroups().contains(preferenceGroup)) {
			throw new MissingPreferenceGroupException(preferenceGroup);
		}
	}
	
	/**
	 * returns all preference groups if the preferenceGroup is null,
	 * otherwise selects the preference group.
	 */
	@Override
	public List<Object> executeQuery(PIIType pii) {
		
		List<Object> policySetOrPolicy = dao.getAllPreferenceGroupPolicies();
		//filter by preference group
		//if the preferencegroup is null we take all policies
		logger.info("Using preference group " + this.preferenceGroup);
		if (this.preferenceGroup != null) {
			policySetOrPolicy = filterByPreferenceGroup(policySetOrPolicy, preferenceGroup);  
		}
		return policySetOrPolicy;
		
	}
	
	private static List<Object> filterByPreferenceGroup(List<Object> policySetOrPolicy, String preferenceGroup){
		
		List<Object> temp = new LinkedList<Object>();
		for (Object o : policySetOrPolicy) {
			if (o instanceof PolicyType && ((PolicyType)o).getPolicyId().equals(preferenceGroup)) {
				temp.add(o);
			} else if (o instanceof PolicySetType && ((PolicySetType)o).getPolicySetId().equals(preferenceGroup)) {
				temp.add(o);
			}
		}
//		if (temp.isEmpty() && !policySetOrPolicy.isEmpty()) {
//			// take the first policy as default if the pii does not have a policy with that name
//			temp.add(policySetOrPolicy.get(0));
//		} 
		return temp;
					
	}

}
