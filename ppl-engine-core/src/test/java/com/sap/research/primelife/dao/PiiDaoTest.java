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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.dao.IDao;
import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.ObjectFactory;
import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicyType;


/**
 * These tests cover the PiiDao and the DaoImpl
 * TODO: check completeness
 * 
 * @Version 0.1
 * @Date May 3, 2010
 * 
 */
public class PiiDaoTest {

	private static IDao<PolicyType> dao;
	private static PiiDao piiDao;
	private static ObjectFactory ofPii;
	private static UnmarshallImpl unmarshaller;
	private static final String PREFERENCES_PATH = "/daoTest/";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DaoInitializer.getInstance();
		dao = new DaoImpl<PolicyType>();
		piiDao = new PiiDao();
		ofPii = new ObjectFactory();
		unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
	}

	/**
	 * Test persisting a pii together with preferences
	 * (still from old version where pii and preferences were linked together explicitly)
	 * @throws SyntaxException
	 */
	@Test
	public void testPersist() throws SyntaxException {
		List<Object> listPreferences = new ArrayList<Object>();

		PolicyType pref = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream(
						PREFERENCES_PATH + "EmailPreferences.xml"));
		listPreferences.add(pref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("mail@example.com");
		pii.setPolicySetOrPolicy(listPreferences);

		PIIType piiResult = piiDao.persistObject(pii);
		assertEquals(pii, piiResult);
	}
	
	/**
	 * Test persisting a policy without a pii
	 * @throws SyntaxException
	 */
	@Test
	public void testPersistPolicy() throws SyntaxException {
		PolicyType prefPolicy = (PolicyType) unmarshaller.unmarshal(
				getClass().getResourceAsStream(
						PREFERENCES_PATH + "AddressPreferences.xml"));
		dao.persistObject(prefPolicy);
		assertNotNull(prefPolicy.getHjid());
	}

	/**
	 * Test finding a pii, persisting it with a policy
	 * @throws SyntaxException
	 */
	@Test
	public void testFind() throws SyntaxException {
		List<Object> listPreferences = new ArrayList<Object>();

		PolicyType pref = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream(
						PREFERENCES_PATH + "EmailPreferences.xml"));
		listPreferences.add(pref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("mail@example.com");
		pii.setPolicySetOrPolicy(listPreferences);

		piiDao.persistObject(pii);

		PIIType pii2 = piiDao.findObject(PIIType.class, pii.getHjid());
		assertNotNull(pii2);
		assertEquals(pii, pii2);
		assertEquals(pii2.getPolicySetOrPolicy(), pii.getPolicySetOrPolicy());
	}

	/**
	 * test deleting a pii with a policy
	 * @throws SyntaxException
	 */
	@Test
	public void testDelete() throws SyntaxException {
		List<Object> listPreferences = new ArrayList<Object>();

		PolicyType pref = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream(
						PREFERENCES_PATH + "EmailPreferences.xml"));
		listPreferences.add(pref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("mail@example.com");
		pii.setPolicySetOrPolicy(listPreferences);

		piiDao.persistObject(pii);

		PIIType pii2 = piiDao.findObject(PIIType.class, pii.getHjid());
		assertNotNull(pii2);

		Long piiHjid = pii2.getHjid();
		piiDao.deleteObject(pii2);

		PIIType pii3 = piiDao.findObject(PIIType.class, piiHjid);
		assertNull(pii3);
	}

	/**
	 * Test updating a pii's value
	 * @throws SyntaxException
	 */
	@Test
	public void testUpdate() throws SyntaxException {
		List<Object> listPreferences = new ArrayList<Object>();

		PolicyType pref = (PolicyType) unmarshaller.unmarshal(
				getClass().getResourceAsStream(PREFERENCES_PATH + "EmailPreferences.xml"));
		listPreferences.add(pref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("mail@example.com");
		pii.setPolicySetOrPolicy(listPreferences);

		piiDao.persistObject(pii);
		pii.setAttributeValue("mail2@example.com");
		piiDao.updateObject(pii);

		PIIType pii2 = piiDao.findObject(PIIType.class, pii.getHjid());
		assertEquals("mail2@example.com", pii2.getAttributeValue());
	}

	/**
	 * Test finding all PII
	 */
	@Test
	public void testFindAll() {
		List<PIIType> piiList = piiDao.findObjects(PIIType.class);

		for (PIIType pii : piiList)
			piiDao.deleteObject(pii);

		piiList = piiDao.findObjects(PIIType.class);
		assertEquals(0, piiList.size());

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("mail@example.com");
		piiDao.persistObject(pii);

		PIIType pii2 = ofPii.createPIIType();
		pii2.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii2.setAttributeValue("mail2@example.com");
		piiDao.persistObject(pii2);

		piiList = piiDao.findObjects(PIIType.class);
		assertEquals(2, piiList.size());
	}
	
	/**
	 * Test delete all pii
	 */
	@Test
	public void testDeleteAllPii() {
		int before = piiDao.getAllPII().size();
		piiDao.deleteAllPii();
		int after = piiDao.getAllPII().size();
		Assert.assertTrue(before >= after);
		Assert.assertTrue(after == 0);
	}

}
