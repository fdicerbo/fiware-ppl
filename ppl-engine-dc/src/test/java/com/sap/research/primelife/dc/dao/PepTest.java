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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dc.initializer.Initializer;
import com.sap.research.primelife.dc.pep.PEP;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallFactory;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.claims.impl.ObjectFactory;
import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;


public class PepTest {
	
	private PEP pep;
	
	@BeforeClass 
	public static void init(){
		Initializer.getInstance();
	}
	
	@Test
	public void testTwoSubjects() throws SyntaxException,
			WritingException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException,
			DatatypeConfigurationException, IOException, JAXBException,
			ValidationException, MissingPreferenceGroupException {

		Initializer.getInstance();
		pep = new PEP();
		UnmarshallImpl unmarshaller =
			UnmarshallFactory.createUnmarshallImpl(AttributeType.class.getPackage());

//		PiiDao dao = new PiiDao();
//		dao.deleteAllPii();
//		PolicyDao poldao = new PolicyDao();
//		poldao.deleteAllPreferenceGroups();
		
		AttributeType stickyPolicy = (AttributeType) unmarshaller.unmarshal(getClass().getResourceAsStream("/pepTest/stickyPolicy.xml"));
		PIIType pii = pep.storePii("testType", "TestValue", stickyPolicy);

		String policy = readResource("/pepTest/requestPolicy.xml");
		ClaimsType result = pep.processRequestForSpecificPii(policy.replaceFirst("piiId", pii.getHjid().toString()), "");
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

	}
	
	/**
	 * test uploading a claims file to the DC pep
	 * @throws JAXBException 
	 * @throws SyntaxException 
	 * @throws WritingException 
	 */
	@Test
	public void testProcessClaims() throws JAXBException, SyntaxException, WritingException {
		Initializer.getInstance();
		UnmarshallImpl unmarshaller = UnmarshallFactory.createUnmarshallImpl(ClaimsType.class.getPackage());
		ClaimsType claims = (ClaimsType) unmarshaller.unmarshal(getClass().getResourceAsStream("/pepTest/1_claims.xml"));
		PEP pep = new PEP();
		//the obligation enforcement engine throws some weird errors
		List<PIIType> pii = pep.processPolicy("register", claims, true);
		Assert.assertEquals(3, pii.size());
	}
	
	@Test
	public void testUpdatePii() throws JAXBException, SyntaxException {
		Initializer.getInstance();
		pep = new PEP();
		UnmarshallImpl unmarshaller = UnmarshallFactory.createUnmarshallImpl(AttributeType.class.getPackage());
		AttributeType stickyPolicy = (AttributeType) unmarshaller.unmarshal(getClass().getResourceAsStream("/pepTest/stickyPolicy.xml"));
		PIIType pii = pep.storePii("testUpdate", "value1", stickyPolicy);
		PIIType pii2 = new PiiDao().findObject(PIIType.class, pii.getHjid());
		Assert.assertEquals("value1", pii2.getAttributeValue());
		stickyPolicy = (AttributeType) unmarshaller.unmarshal(getClass().getResourceAsStream("/pepTest/stickyPolicy.xml"));
		pep.updatePii(pii, "testUpdate", "value2", stickyPolicy);
		
		PIIType pii3 = new PiiDao().findObject(PIIType.class, pii.getHjid());
		Assert.assertEquals("value2", pii3.getAttributeValue());
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
