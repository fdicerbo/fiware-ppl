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
package com.sap.research.primelife.dc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.Query;
import javax.xml.bind.JAXBException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dc.dao.DCResource;
import com.sap.research.primelife.dc.dao.DCResourceDao;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;


/**
 * 
 * 
 * @version 0.1
 * @date Sep 13, 2010
 * 
 */
public class DCResourceDaoTest {
	private static DCResourceDao dao;
	private static final String RESOURCE_NAME = "/register";
	private static final String POLICY_FILE_PATH = "/policies/DCtoDSpolicy.xml";
	private static UnmarshallImpl unmarshallerSAML;

	@BeforeClass
	public static void oneTimeSetUp() throws JAXBException {
		DaoInitializer.getInstance();
		dao = new DCResourceDao();
		unmarshallerSAML = new UnmarshallImpl(AssertionType.class.getPackage());
	}

	@Before
	public void setUp() {
		Query query = dao.getEm().createQuery(
				"SELECT resource FROM " + DCResource.class.getName()
				+ " resource WHERE resource.name = :name");
		query.setParameter("name", RESOURCE_NAME);

		// delete resources with specified name to avoid unique constraint violation
		for (Object resource : query.getResultList())
			dao.deleteObject((DCResource) resource);
	}

	@Test
	public void testPersistObject() throws SyntaxException {
		DCResource resource = new DCResource();
		resource.setName(RESOURCE_NAME);

		AssertionType assertion = (AssertionType) unmarshallerSAML.unmarshal(
				DCResourceDaoTest.class.getResourceAsStream(POLICY_FILE_PATH));
		resource.setPolicy(assertion);

		dao.persistObject(resource);
		assertNotNull(resource.getId());
	}

	@Test
	public void testFindByName() throws SyntaxException {
		DCResource resource = new DCResource();
		resource.setName(RESOURCE_NAME);
		AssertionType assertion = (AssertionType) unmarshallerSAML.unmarshal(
				DCResourceDaoTest.class.getResourceAsStream(POLICY_FILE_PATH));
		resource.setPolicy(assertion);

		dao.persistObject(resource);
		DCResource resource2 = dao.findByName(RESOURCE_NAME);
		assertNotNull(resource2);
		assertEquals(resource.getId(), resource2.getId());
		assertEquals(RESOURCE_NAME, resource2.getName());
		assertEquals(assertion, resource2.getPolicy());
	}

}
