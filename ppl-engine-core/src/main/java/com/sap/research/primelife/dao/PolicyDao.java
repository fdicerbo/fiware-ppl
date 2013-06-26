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
package com.sap.research.primelife.dao;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallFactory;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PIITypePolicySetOrPolicyItem;
import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.xacml.impl.PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem;

/**
 * Convenience class to manage policies and policySets.
 * Always use these methods to keep the integrity of preference groups.
 * 
 *
 */
public class PolicyDao extends DaoImpl<PolicyType> {

	public PiiDao piiDao = new PiiDao();
	private DaoImpl<PolicySetType> policySetDao = new DaoImpl<PolicySetType>();

	/**
	 * Finds all the PII that use the given PolicySet or Policy object. This is
	 * used to find out whether a policy can be deleted in the DS grails
	 * visualization.
	 * 
	 * In the new DS version, PII and Policies are connected implicitly.
	 * So it is only used for testing now.
	 * 
	 * @param policy
	 * @return a set of PIIs
	 */
	public List<Object> findPoliciesConnectedToAnyPii() {
		List<PIIType> allPii = piiDao.findObjects(PIIType.class);
		List<Object> result = new LinkedList<Object>();
		for (PIIType pii : allPii) {
			for (PIITypePolicySetOrPolicyItem pol : pii
					.getPolicySetOrPolicyItems()) {
				if (pol.getItemPolicy() != null) {
					result.add(pol.getItemPolicy());
				} else if (pol.getItemPolicySet() != null) {
					result.add(pol.getItemPolicySet());
				}
			}
		}

		return result;
	}

	/**
	 * Retrieves all Policy objects having a parent element (another policySet)
	 * or being connected to a pii
	 * 
	 * @return a list of policies
	 */
	@SuppressWarnings("unchecked")
	public List<PolicyType> getConnectedPolicies() {
		List<PolicyType> result = em
				.createQuery(
						"SELECT policySetPolicy.itemPolicy FROM "
								+ PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem.class
										.getName()
								+ " policySetPolicy WHERE policySetPolicy.itemPolicy is not null ")
				.getResultList();
		// select all hjid of policies in policysets
		List<PolicyType> connectedToPii = em.createQuery(
				"SELECT pii.itemPolicy FROM "
						+ PIITypePolicySetOrPolicyItem.class.getName()
						+ " pii WHERE pii.itemPolicy is not null")
				.getResultList();
		result.addAll(connectedToPii);
		return result;
	}

	/**
	 * Retrieves all PolicySets having a parent element (another policySet) or
	 * being connected to a pii
	 * 
	 * @return a list of policySets
	 */
	@SuppressWarnings("unchecked")
	public List<PolicySetType> getConnectedPolicySets() {
		List<PolicySetType> result = em
				.createQuery(
						"SELECT policySetPolicy.itemPolicySet FROM "
								+ PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem.class
										.getName()
								+ " policySetPolicy WHERE policySetPolicy.itemPolicySet is not null ")
				.getResultList();
		// select all hjid of policies in policysets
		List<PolicySetType> connectedToPii = em.createQuery(
				"SELECT pii.itemPolicySet FROM "
						+ PIITypePolicySetOrPolicyItem.class.getName()
						+ " pii WHERE pii.itemPolicySet is not null")
				.getResultList();
		result.addAll(connectedToPii);

		return result;
	}

	/**
	 * Retrieves all Policy objects having no parent elements, being not
	 * connected to any pii and having a not-null id
	 * 
	 * @return a list of policies
	 */
	public List<PolicyType> getOrphanPolicies() {
		List<PolicyType> connected = this.getConnectedPolicies();
		List<PolicyType> policies = this.findObjects(PolicyType.class);
		policies.removeAll(connected);
		List<PolicyType> result = new LinkedList<PolicyType>();
		for (PolicyType p : policies) {
			if (p.getPolicyId() != null)
				result.add(p);
		}
		return policies;
	}

	/**
	 * Retrieves all PolicySets having no parent elements and being not
	 * connected to any pii and having a not-null id
	 * 
	 * @return a list of PolicySets
	 */
	public List<PolicySetType> getOrphanPolicySets() {
		List<PolicySetType> connected = this.getConnectedPolicySets();
		List<PolicySetType> policies = policySetDao
				.findObjects(PolicySetType.class);
		policies.removeAll(connected);
		List<PolicySetType> result = new LinkedList<PolicySetType>();
		for (PolicySetType p : policies) {
			if (p.getPolicySetId() != null)
				result.add(p);
		}
		return result;
	}

	// /**
	// * Checks if a policy is a root element.
	// * @param policy - the policy to check
	// * @return true if the policy is an orphan, i.e. has no parent policy
	// elements
	// */
	// @SuppressWarnings("rawtypes")
	// public boolean isOrphan(PolicyType policy){
	// Query query = em.createQuery("SELECT policySetPolicy.itemPolicy FROM "
	// +
	// PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem.class.getName()+
	// " policySetPolicy WHERE policySetPolicy.itemPolicy.hjid = :id ");
	// query.setParameter("id", policy.getHjid());
	// List result = query.getResultList();
	// return result.isEmpty();
	// }
	//
	// /**
	// * Checks if a policySet is a root element.
	// * @param policy - the policySet to check
	// * @return true if the policy set is an orphan, i.e. has no parent policy
	// elements
	// */
	// @SuppressWarnings("rawtypes")
	// public boolean isOrphan(PolicySetType policy){
	// Query query = em.createQuery("SELECT policySetPolicy.itemPolicySet FROM "
	// +
	// PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem.class.getName()+
	// " policySetPolicy WHERE policySetPolicy.itemPolicySet.hjid = :id ");
	// query.setParameter("id", policy.getHjid());
	// List result = query.getResultList();
	// return result.isEmpty();
	// }

	/**
	 * Gets the names of alle preference groups, i.e. the ids of all orphan
	 * policySets and policies.
	 * 
	 * @return a list of preference group names.
	 */
	public List<String> getAllPreferenceGroups() {
		Set<String> result = new HashSet<String>();
		List<PolicySetType> policySets = this.getOrphanPolicySets();
		List<PolicyType> policies = this.getOrphanPolicies();

		for (PolicySetType pol : policySets) {
			result.add(pol.getPolicySetId());
		}

		for (PolicyType pol : policies) {
			result.add(pol.getPolicyId());
		}
		List<String> preferenceGroupNames = new LinkedList<String>();
		preferenceGroupNames.addAll(result);
		return preferenceGroupNames;
	}

	/**
	 * Gets all Policy objects acting as preference groups, i.e. all orphan
	 * policySets or policies, to be used on DS.
	 * 
	 * @return a list of PolicySetType and PolicyType objects
	 */
	public List<Object> getAllPreferenceGroupPolicies() {
		List<Object> result = new LinkedList<Object>();
		List<PolicySetType> policySets = this.getOrphanPolicySets();
		List<PolicyType> policies = this.getOrphanPolicies();

		result.addAll(policySets);
		result.addAll(policies);

		return result;
	}

	/**
	 * Returns the object for a certain preferenceGroup name if the preference
	 * Group does not exist, it returns null If there is more than one object
	 * with this name, only the first is returned. (This should not happen in a
	 * consistent database.)
	 * 
	 * @param preferenceGroup
	 *            - the name of the preference group, i.e. the policySetId or
	 *            policyId
	 * @return a PolicySetType or a PolicyType
	 */
	public Object getPreferenceGroupPolicy(String preferenceGroup) {

		List<Object> temp = new LinkedList<Object>();
		List<Object> preferenceGroups = this.getAllPreferenceGroupPolicies();
		for (Object o : preferenceGroups) {
			if (o instanceof PolicyType
					&& ((PolicyType) o).getPolicyId() != null
					&& ((PolicyType) o).getPolicyId().equals(preferenceGroup)) {
				temp.add(o);
			} else if (o instanceof PolicySetType
					&& ((PolicySetType) o).getPolicySetId() != null
					&& ((PolicySetType) o).getPolicySetId().equals(
							preferenceGroup)) {
				temp.add(o);
			}
		}
		if (temp.size() == 0) {
			return null;
		}
		return temp.get(0);
	}

	/**
	 * Adds a preference group. If the id is already used, it deletes the old
	 * preference group first.
	 * 
	 * @param policy
	 *            - a detached policy
	 */
	public void addAsPreferenceGroup(PolicyType policy) {
		Object pref = getPreferenceGroupPolicy(policy.getPolicyId());
		if (pref != null) {
			// the name is already used
			this.deletePreferenceGroup(policy.getPolicyId());
		}
		this.persistObject(policy);
	}

	/**
	 * Adds a preference group. If the id is already used, it deletes the old
	 * preference group first.
	 * 
	 * @param policy
	 *            - a detached policySet
	 */
	public void addAsPreferenceGroup(PolicySetType policy) {
		Object pref = getPreferenceGroupPolicy(policy.getPolicySetId());
		if (pref != null) {
			// the name is already used
			this.deletePreferenceGroup(policy.getPolicySetId());
		}
		policySetDao.persistObject(policy);
	}

	/**
	 * Deletes the object with the given policyId or policySetId. It has to
	 * refer to an orphan PolicySet or Policy.
	 * 
	 * @param preferenceGroup
	 *            - the name of the object to be deleted
	 */
	public void deletePreferenceGroup(String preferenceGroup) {
		Object toDelete = this.getPreferenceGroupPolicy(preferenceGroup);
		if (toDelete instanceof PolicyType) {
			this.deleteObject((PolicyType) toDelete);
		} else if (toDelete instanceof PolicySetType) {
			policySetDao.deleteObject((PolicySetType) toDelete);
		}
	}

	/**
	 * Deletes all PolicySets and PolicyIds acting as a preference group. checks
	 * whether the prefgroup is not used by a pii
	 */
	public void deleteAllPreferenceGroups() {
		List<PIIType> piis = piiDao.getAllPII();
		Set<Long> policyIds = new HashSet<Long>();
		Set<Long> policySetIds = new HashSet<Long>();
		// get the policy ids used by a pii
		for (PIIType p : piis) {
			for (PIITypePolicySetOrPolicyItem item : p
					.getPolicySetOrPolicyItems()) {
				if (item.getItemPolicy() != null) {
					policyIds.add(item.getItemPolicy().getHjid());
				}
				if (item.getItemPolicySet() != null) {
					policySetIds.add(item.getItemPolicySet().getHjid());
				}
			}
		}
		//delete all orphan policies that are not used by a pii
		List<PolicyType> policies = this.getOrphanPolicies();
		for (PolicyType pol : policies) {
			if (!policyIds.contains(pol.getHjid()))
				this.deleteObject(pol);
		}
		List<PolicySetType> policySets = this.getOrphanPolicySets();
		for (PolicySetType pol : policySets) {
			if (!policySetIds.contains(pol.getHjid()))
				policySetDao.deleteObject(pol);
		}

	}

	/**
	 * Used in the grails app to rename a policySet.
	 * resetting the policySetId of the policySet.
	 * throws a ValidationException if the newName is already used by another preference group.
	 * @param policy - the policySet
	 * @param newName - the new Id
	 */
	public void renamePreferenceGroup(PolicySetType policy, String newName) {
		if (this.getPreferenceGroupPolicy(newName) == null) {
			policy.setPolicySetId(newName);
		} else {
			throw new ValidationException(
					"This name is already used for another preferenceGroup.");
		}
	}

	/**
	 * Used in the grails app to rename a policy.
	 * resetting the policyId of the policySet.
	 * throws a ValidationException if the newName is already used by another preference group.
	 * @param policy - the policy
	 * @param newName - the new Id
	 */
	public void renamePreferenceGroup(PolicyType policy, String newName) {
		if (!newName.equals(policy.getPolicyId())) {
			if (this.getPreferenceGroupPolicy(newName) == null) {
				policy.setPolicyId(newName);
				this.updateObject(policy);
			} else {
				throw new ValidationException(
						"This name is already used for another preferenceGroup.");
			}
		}
	}
	
	/**
	 * Creates a new AuthorizationsSet by copying all the values from the given set.
	 * Internally marshals and unmarshals the AuthorizationsSet to detach it from the database.
	 * All Hjid attributes are deleted.
	 * @param authSet - the authorizationsSet to clone
	 * @return a detached authorizationsSet
	 * @throws JAXBException
	 * @throws WritingException - if it could not write to the StringWriter (very unlikely)
	 * @throws SyntaxException
	 */
	public AuthorizationsSetType cloneAuthorizationSet (AuthorizationsSetType authSet) throws JAXBException, WritingException, SyntaxException {
		MarshallImpl m = MarshallFactory.createMarshallImpl(AuthorizationsSetType.class.getPackage(), false);
		UnmarshallImpl um = UnmarshallFactory.createUnmarshallImpl(AuthorizationsSetType.class.getPackage());
		StringWriter temp = new StringWriter();
		ObjectFactory of = new ObjectFactory();
		
		m.marshal(of.createAuthorizationsSet(authSet), temp);
		String auth = temp.toString();
		//remove all hjid
		auth = auth.replaceAll("Hjid=\"(\\d)*\"", " ");
		
		Object result = um.unmarshal(new StringReader(auth));
		
		return (AuthorizationsSetType) result;
	}

	/**
	 * Copies a preferenceGroup named oldName, setting the id to newName;
	 * Internally it marshals and unmarshals the policySet or Policy representing the preference group.
	 * All Hjids are removed.
	 * @param oldName
	 *            - the name of the preferenceGroup to copy
	 * @param newName
	 * @throws JAXBException
	 * @throws WritingException
	 * @throws SyntaxException 
	 */
	public void clonePreferenceGroup(String oldName, String newName)
			throws JAXBException, WritingException, SyntaxException {
		// TODO
		if (this.getPreferenceGroupPolicy(newName) != null) {
			// name already used
			throw new ValidationException(
					"This name is already used for another preference group.");
		} else {
			// copy
			MarshallImpl m = MarshallFactory.createMarshallImpl(PolicySetType.class.getPackage(), false);
			UnmarshallImpl um = UnmarshallFactory.createUnmarshallImpl(PolicySetType.class.getPackage());
			StringWriter temp = new StringWriter();
			ObjectFactory of = new ObjectFactory();
			Object preferenceGroup = this.getPreferenceGroupPolicy(oldName);
			
			if (preferenceGroup instanceof PolicySetType) {
				m.marshal(of.createPolicySet((PolicySetType)preferenceGroup), temp);
				String policy = temp.toString();
				//remove all hjid
				policy = policy.replaceAll("Hjid=\"(\\d)*\"", " ");
				
				Object result = um.unmarshal(new StringReader(policy));
				
				PolicySetType pol = (PolicySetType) result;
				pol.setPolicySetId(newName);
				this.addAsPreferenceGroup(pol);
			} else if (preferenceGroup instanceof PolicyType) {
				m.marshal(of.createPolicy((PolicyType)preferenceGroup), temp);
				String policy = temp.toString();
				//remove all hjid
				policy = policy.replaceAll("Hjid=\"(\\d)*\"", " ");
				Object result = um.unmarshal(new StringReader(policy));
				
				PolicyType pol = (PolicyType) result;
				pol.setPolicyId(newName);
				this.addAsPreferenceGroup(pol);
				
			}
		}
	}

}
