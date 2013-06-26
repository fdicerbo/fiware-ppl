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

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.policy.impl.TargetType;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * 
 * 
 * @date Nov 17, 2010
 * 
 */
public class HerasConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// HERAS initialization
		SimplePDPFactory.useDefaultInitializers();
		SimplePDPFactory.getSimplePDP();
	}

	/**
	 * Test converting a policy for HERAS.
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws JAXBException
	 */
	@Test
	public void testConvertToHerasPolicy() throws SyntaxException, FileNotFoundException,
			WritingException, org.herasaf.xacml.core.SyntaxException, JAXBException {
		UnmarshallImpl unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
		PolicyType addressPref = (PolicyType) unmarshaller.unmarshal(getClass()
				.getResourceAsStream("/herasConverterTest/AddressPreferences.xml"));
		assertNotNull(addressPref);
		org.herasaf.xacml.core.policy.impl.PolicyType herasAddressPref = ConverterFunctions
				.convertToHerasPolicy(addressPref);
		assertNotNull(herasAddressPref);

		PolicyType emailPref = (PolicyType) unmarshaller.unmarshal(getClass().getResourceAsStream(
				"/herasConverterTest/EmailPreferences.xml"));
		assertNotNull(emailPref);
		org.herasaf.xacml.core.policy.impl.PolicyType herasEmailPref = ConverterFunctions
				.convertToHerasPolicy(emailPref);
		assertNotNull(herasEmailPref);
	}

	/**
	 * Test converting a policySet for HERAS.
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws JAXBException
	 */
	@Test
	public void testConvertToHerasPolicySet() throws SyntaxException, FileNotFoundException,
			WritingException, org.herasaf.xacml.core.SyntaxException, JAXBException {
		UnmarshallImpl unmarshaller = new UnmarshallImpl(PolicySetType.class.getPackage());
		PolicySetType policySetPref = (PolicySetType) unmarshaller.unmarshal(getClass()
				.getResourceAsStream("/herasConverterTest/PolicySetPreferences.xml"));
		assertNotNull(policySetPref);
		org.herasaf.xacml.core.policy.impl.PolicySetType herasPolicySetPref = ConverterFunctions
				.convertToHerasPolicySet(policySetPref);
		assertNotNull(herasPolicySetPref);
	}
	
	/**
	 * Test fromPPLTargetToHerasTarget
	 * converting a policy to target
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws JAXBException
	 */
	@Test
	public void testConvert() throws SyntaxException, FileNotFoundException,
			WritingException, org.herasaf.xacml.core.SyntaxException, JAXBException {
		UnmarshallImpl unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
		
		SimplePDPFactory.useDefaultInitializers();
		SimplePDPFactory.getSimplePDP();
		
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(
				getClass().getResourceAsStream("/herasConverterTest/Policy.xml"));
		assertNotNull(policy);
		assertNotNull(policy.getTarget());
		TargetType targetHeras = ConverterFunctions.fromPPLTargetToHerasTarget(
				policy.getTarget());
		assertNotNull(targetHeras);
	}

}
