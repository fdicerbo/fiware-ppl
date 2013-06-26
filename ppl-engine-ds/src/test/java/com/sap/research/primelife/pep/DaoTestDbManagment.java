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
package com.sap.research.primelife.pep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.ObjectFactory;
import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.PolicyType;


/**
 * 
 * 
 * @Version 0.1
 * @Date May 3, 2010
 * 
 */
public class DaoTestDbManagment {

	private static DaoImpl<PIIType> dao;
	private static ObjectFactory ofPii;
	private static UnmarshallImpl unmarshaller;
	private static final String PATH = "/daoTestDbManagement/";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new DaoImpl<PIIType>();
		ofPii = new ObjectFactory();
		unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
	}

	@Test
	public void testPersistEmail() throws SyntaxException{
		List<Object> emailPrefList = new ArrayList<Object>();
		PolicyType emailPref = (PolicyType) unmarshaller.unmarshal(
				new File(getClass().getResource(PATH + "EmailPreferences.xml").getFile()));
		emailPrefList.add(emailPref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
		pii.setAttributeValue("john.doe@example.com");
		pii.setPolicySetOrPolicy(emailPrefList);

		PIIType piiResult = (PIIType) dao.persistObject(pii);
		assertNotNull(piiResult);
		assertNotNull(piiResult.getHjid());
		assertEquals(pii, piiResult);

		PIIType pii2 = (PIIType) dao.findObject(PIIType.class, piiResult.getHjid());
		assertNotNull(pii2);
		assertEquals(pii.getHjid(), pii2.getHjid());
		assertEquals(pii.getAttributeName(), pii2.getAttributeName());
		assertEquals(pii.getAttributeValue(), pii2.getAttributeValue());
	}

	@Test
	public void testPersistAddress() throws SyntaxException{
		List<Object> addressPrefList = new ArrayList<Object>();
		PolicyType addressPref = (PolicyType) unmarshaller.unmarshal(
				new File(getClass().getResource(PATH + "AddressPreferences.xml").getFile()));
		addressPrefList.add(addressPref);

		PIIType pii = ofPii.createPIIType();
		pii.setAttributeName("http://www.fgov.be/eID/address");
		pii.setAttributeValue("45, rue Henri Poincare");
		pii.setPolicySetOrPolicy(addressPrefList);

		PIIType piiResult = (PIIType) dao.persistObject(pii);
		assertNotNull(piiResult);
		assertNotNull(piiResult.getHjid());
		assertEquals(pii, piiResult);

		PIIType pii2 = (PIIType) dao.findObject(PIIType.class, piiResult.getHjid());
		assertNotNull(pii2);
		assertEquals(pii.getHjid(), pii2.getHjid());
		assertEquals(pii.getAttributeName(), pii2.getAttributeName());
		assertEquals(pii.getAttributeValue(), pii2.getAttributeValue());
	}
	
}
