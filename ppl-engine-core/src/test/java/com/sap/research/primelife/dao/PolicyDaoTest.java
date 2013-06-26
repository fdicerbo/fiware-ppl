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

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * These tests cover the PolicyDao.
 * They are not independent of each other.
 * 
 * @Version 0.1
 * @Date Jun 16, 2010
 * 
 */
public class PolicyDaoTest {

	private static UnmarshallImpl unmarshaller;
	private static DaoImpl<PolicyType> dao;
	private static PolicyDao polDao;
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(PolicyDaoTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DaoInitializer.getInstance();
		unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
		dao = new DaoImpl<PolicyType>();
		polDao = new PolicyDao();
		new PiiDao().deleteAllPii();
		polDao.deleteAllPreferenceGroups();
		int prefGroups = polDao.getAllPreferenceGroups().size();
//		System.out.println(polDao.getAllPreferenceGroupPolicies().toString());
//		System.out.println("prefGroups:" + prefGroups);
		Assert.assertTrue(prefGroups == 0);
		
	}	

	/**
	 *  Test persisting a policy
	 * @throws SyntaxException
	 */
	@Test
	public void testPersistPolicy() throws SyntaxException{
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/Server.xml"));
		dao.persistObject(policy);
		assertNotNull(policy.getHjid());

		Query query;
		EntityManager em;

		em = DaoInitializer.getInstance().getEntityManager();
		query = em.createQuery("SELECT p FROM " + PolicyType.class.getName() +" p where p.hjid = :id");
		query.setParameter("id", policy.getHjid());

		PolicyType policyResult = (PolicyType) query.getSingleResult();

		assertNotNull(policyResult);
	}
	
	/**
	 * Test finding connected and orphan policies
	 */
	@Test
	public void testGetOrphanPolicies(){
		List<PolicyType> connected = polDao.getConnectedPolicies();
		List<PolicyType> policies = polDao.getOrphanPolicies();
		int orphanPolicies = policies.size();
		int connectedPolicies = connected.size();
		int allPolicies = polDao.findObjects(PolicyType.class).size();
		Assert.assertTrue(orphanPolicies + connectedPolicies == allPolicies);
	}
	
	/**
	 * Test finding connected and orphan policySets
	 */
	@Test
	public void testGetOrphanPolicySets(){
		List<PolicySetType> connected = polDao.getConnectedPolicySets();
		List<PolicySetType> policies = polDao.getOrphanPolicySets();
		
		int orphanPolicies = policies.size();
		int connectedPolicies = connected.size();
		int allPolicies = new DaoImpl<PolicySetType>().findObjects(PolicySetType.class).size();
		Assert.assertTrue(orphanPolicies + connectedPolicies == allPolicies);
	}
	
	/**
	 * Test getting all preference groups.
	 * A sticky policy connected to the PII should not be considered as a preference group.
	 * @throws JAXBException
	 * @throws SyntaxException
	 */
	@Test
	public void testGetAllPreferenceGroups() throws JAXBException, SyntaxException{
		PiiDao dao = new PiiDao();
		dao.deleteAllPii();
		polDao.deleteAllPreferenceGroups();
		PIIType pii = new PIIType();
		pii.setAttributeName("testGetAllPrefGroups");
		pii.setAttributeValue("withSP");
		PolicyType policy = readPolicyPrefs("/policyDaoTest/StickyPolicyStressTest.xml");
		pii.getPolicySetOrPolicy().add(policy);
		dao.persistObject(pii);
		
		PolicyType policy2 = readPolicyPrefs("/policyDaoTest/MissingPiiTestPreferences.xml");
		polDao.addAsPreferenceGroup(policy2);
		
//		List<String> prefgroups = polDao.getAllPreferenceGroups();
//		System.out.println(prefgroups);
		int policies = polDao.getOrphanPolicies().size() + polDao.getOrphanPolicySets().size();
		int piiPolicies = polDao.findPoliciesConnectedToAnyPii().size();
		Assert.assertEquals(1, policies);
		Assert.assertEquals(1, piiPolicies);
		
	}
	
	/**
	 * Test adding a policy as a preference group
	 * @throws SyntaxException
	 */
	@Test
	public void testAddPrefGroupPolicy() throws SyntaxException{
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/ClientAttributeAddress1.xml"));
		polDao.addAsPreferenceGroup(policy);
		Assert.assertNotNull(polDao.getPreferenceGroupPolicy("policy1"));
		Assert.assertEquals(PolicyType.class, polDao.getPreferenceGroupPolicy("policy1").getClass());
	}
	
	/**
	 * Test adding a policy set as a preference group, possibly overwriting the previous policy test
	 * @throws SyntaxException
	 */
	@Test
	public void testOvervritePrefGroupPolicySet() throws SyntaxException{
		Assert.assertEquals(PolicyType.class, polDao.getPreferenceGroupPolicy("policy1").getClass());
		PolicySetType policy = (PolicySetType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/PolicySetPreferences.xml"));
		policy.setPolicySetId("policy1");
		polDao.addAsPreferenceGroup(policy);
		Assert.assertNotNull(polDao.getPreferenceGroupPolicy("policy1"));
		Assert.assertEquals(PolicySetType.class, polDao.getPreferenceGroupPolicy("policy1").getClass());
	}
	
	/**
	 * Test deleting a preference group
	 * (after previouse test have run)
	 */
	@Test
	public void testDeletePrefGroup(){
		Assert.assertNotNull(polDao.getPreferenceGroupPolicy("policy1"));
		polDao.deletePreferenceGroup("policy1");
		Assert.assertNull(polDao.getPreferenceGroupPolicy("policy1"));
		polDao.deletePreferenceGroup("policy1");
	}
	
	/**
	 * Test listing all preference groups
	 * @throws SyntaxException
	 */
	@Test
	public void testListPrefGroups() throws SyntaxException {
		polDao.deleteAllPreferenceGroups();
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/ClientAttributeAddress1.xml"));
		polDao.addAsPreferenceGroup(policy);
		PolicySetType policy2 = (PolicySetType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/PolicySetPreferences.xml"));
		polDao.addAsPreferenceGroup(policy2);
		Assert.assertEquals(2, polDao.getAllPreferenceGroupPolicies().size());
	}
	
	/**
	 * Test cloning a preference groupo
	 * @throws SyntaxException
	 * @throws JAXBException
	 * @throws WritingException
	 */
	@Test
	public void testClonePreferenceGroup() throws SyntaxException, JAXBException, WritingException{
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream("/policyDaoTest/ClientAttributeAddress1.xml"));
		polDao.addAsPreferenceGroup(policy);
		Assert.assertNull(polDao.getPreferenceGroupPolicy("testClonePrefGroup"));
		polDao.clonePreferenceGroup(policy.getPolicyId(), "testClonePrefGroup");
		Assert.assertNotNull(polDao.getPreferenceGroupPolicy("testClonePrefGroup"));
		Assert.assertNotNull(polDao.getPreferenceGroupPolicy(policy.getPolicyId()));
	}
	
	/**
	 * clean up the database afterwards
	 */
	@AfterClass
	public static void tearDown() {
		new PiiDao().deleteAllPii();
		polDao.deleteAllPreferenceGroups();
	}
	
	/**
	 * helper method to read a policy from resources
	 * @param path
	 * @return
	 * @throws JAXBException
	 * @throws SyntaxException
	 */
	private PolicyType readPolicyPrefs(String path) throws JAXBException, SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
	}
	
}
