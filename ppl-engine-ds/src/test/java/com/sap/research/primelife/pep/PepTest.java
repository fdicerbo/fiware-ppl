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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.Assert;
import org.junit.Test;

import com.sap.research.primelife.PiiDaoHelper;
import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pep.PEP;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.initializer.Initializer;
import com.sap.research.primelife.marshalling.MarshallFactory;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallImpl;
import com.sap.research.primelife.utils.ClaimsReader;

import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.claims.impl.Decision;
import eu.primelife.ppl.claims.impl.ObjectFactory;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

public class PepTest {

	/**
	 * Black box test before refactoring. The Obligation Matching Engine needs to run.
	 * 
	 * @throws SyntaxException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws DatatypeConfigurationException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws MissingPreferenceGroupException
	 * @throws ValidationException
	 */
	@Test
	public void hasExpectedMatchingResult() throws SyntaxException,
			WritingException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException,
			DatatypeConfigurationException, IOException, JAXBException,
			ValidationException, MissingPreferenceGroupException {

		Initializer.getInstance();

		PiiDao dao = new PiiDao();
		dao.deleteAllPii();
		PolicyDao poldao = new PolicyDao();
		poldao.deleteAllPreferenceGroups();

		PiiDaoHelper piiDaoHelper = new PiiDaoHelper(dao);
		piiDaoHelper.createOrUpdatePII("http://www.w3.org/2006/vcard/ns#email",	"test");
		piiDaoHelper.createOrUpdatePII("http://www.example.org/names#display_name", "test2");
		piiDaoHelper.createOrUpdatePII("http://www.example.org/names#user_name", "test3");
		PolicyType prefs = readPolicyPrefs("/pepTest/peptestPrefs.xml");
		poldao.addAsPreferenceGroup(prefs);

		String policy = readResource("/pepTest/DCResponse.xml");
		PEP pep = new PEP();
		ClaimsType result = pep.process(policy, "PepTest");
		Assert.assertNotNull(result);
		//System.out.println(result.toString());

		ObjectFactory ofObligationClaims = new ObjectFactory();
		Writer writer = new FileWriter(new File("test2.xml"));
		MarshallImpl claimsMarshaller = MarshallFactory.createMarshallImpl(
				ClaimsType.class.getPackage(), true);
		// new MarshallImpl(ClaimsType.class.getPackage(), false)
		claimsMarshaller.marshal(ofObligationClaims.createClaims(result),
				writer);
		writer.close();

		// Assert.assertEquals(expectedResult, result.trim());

	}
	
	/**
	 * Same as the {@link #hasExpectedMatchingResult()} test, but with simpler examples.
	 * @throws SyntaxException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws DatatypeConfigurationException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws ValidationException
	 * @throws MissingPreferenceGroupException
	 */
	@Test
	public void hasExpectedMatchingResult2() throws SyntaxException,
			WritingException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException,
			DatatypeConfigurationException, IOException, JAXBException,
			ValidationException, MissingPreferenceGroupException {

		Initializer.getInstance();

		PiiDao dao = new PiiDao();
		PiiDaoHelper piiDaoHelper = new PiiDaoHelper(dao);
		dao.deleteAllPii();
		PolicyDao poldao = new PolicyDao();
		poldao.deleteAllPreferenceGroups();

		piiDaoHelper.createOrUpdatePII("http://www.w3.org/2006/vcard/ns#email",	"test");
		PolicyType prefs = readPolicyPrefs("/pepTest/peptestPrefs_simple.xml");
		poldao.addAsPreferenceGroup(prefs);

		String policy = readResource("/pepTest/DCResponse_simple.xml");
		PEP pep = new PEP();
		ClaimsType result = pep.process(policy, "PepTest");
		Assert.assertNotNull(result);
		//System.out.println(result.toString());

		ObjectFactory ofObligationClaims = new ObjectFactory();
		Writer writer = new FileWriter(new File("test.xml"));
		MarshallImpl claimsMarshaller = MarshallFactory.createMarshallImpl(
				ClaimsType.class.getPackage(), true);
		// new MarshallImpl(ClaimsType.class.getPackage(), false)
		claimsMarshaller.marshal(ofObligationClaims.createClaims(result),
				writer);
		writer.close();

		// Assert.assertEquals(expectedResult, result.trim());

	}
	
	/**
	 * Test the case that for a requested PII there is no applicable target in the preferences. 
	 * @throws JAXBException
	 * @throws SyntaxException
	 * @throws IOException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws DatatypeConfigurationException
	 * @throws MissingPreferenceGroupException
	 */
	@Test
	public void testPIICaseDifference() throws JAXBException, SyntaxException, IOException,
		WritingException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, DatatypeConfigurationException, MissingPreferenceGroupException{
		Initializer.getInstance();
		
		PiiDao dao = new PiiDao();
		dao.deleteAllPii();
		PolicyDao poldao = new PolicyDao();
		poldao.deleteAllPreferenceGroups();
		PiiDaoHelper piiDaoHelper = new PiiDaoHelper(dao);
		
		piiDaoHelper.createOrUpdatePII("http://www.w3.org/2006/vcard/ns#Email", "testPIICaseDifference");
		piiDaoHelper.createOrUpdatePII("http://www.example.org/names#display_name", "testPIICaseDifference");
		
		PolicySetType prefs = readPolicySetPrefs("/pepTest/1_pref_tuner_20_12.xml");
		prefs.setPolicySetId("testPIICaseDifference");
		poldao.addAsPreferenceGroup(prefs);
		
		String policy = readResource("/pepTest/2_dc.xml");
		
		PEP pep = new PEP();
		ClaimsType result = pep.process(policy, "testPIICaseDifference");
		ClaimsReader claims = new ClaimsReader(result);
		Assert.assertEquals(Decision.INDETERMINATE, claims.getDecision());
		Assert.assertEquals(2, claims.getPiiList().size());
		Assert.assertEquals(1, claims.getMissingPII().size());
		Assert.assertFalse(claims.getPIIAttributeNames().contains("http://www.w3.org/2006/vcard/ns#Email"));
		Assert.assertTrue(claims.getMissingPII().contains("http://www.w3.org/2006/vcard/ns#Email"));
	}
	
	
	private PolicyType readPolicyPrefs(String path) throws JAXBException,
			SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
	}
	
	private PolicySetType readPolicySetPrefs(String path) throws JAXBException,
	SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicySetType.class.getPackage());
		return (PolicySetType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
}

	private String readResource(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				PepTest.class.getResourceAsStream(path)));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
}
