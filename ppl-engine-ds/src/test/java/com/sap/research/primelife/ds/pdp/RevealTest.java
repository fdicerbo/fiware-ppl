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
package com.sap.research.primelife.ds.pdp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pep.PEP;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.policy.impl.PolicyType;


public class RevealTest {

	
	private static PolicyDao dao;
	private static PiiDao piiDao;
	
	@BeforeClass
	public static void setUp(){
		dao = new PolicyDao();
		dao.deleteAllPreferenceGroups();
		piiDao = new PiiDao();
		piiDao.deleteAllPii();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testReveal() throws IOException, SyntaxException, WritingException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, DatatypeConfigurationException, JAXBException, MissingPreferenceGroupException{
		String policy = readFile("/testReveal/demo_policy.xml");
		PolicyType preferences = readPolicyPrefs("/testReveal/demo_preferences.xml");
		dao.addAsPreferenceGroup(preferences);
		PEP pep = new PEP();
		ClaimsType cl = pep.process(policy, null);
		//TODO assertions
	}
	
	private String readFile(String path) throws IOException {
		InputStream in  = getClass().getResourceAsStream(path);
		//read it with BufferedReader
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
     	StringBuilder sb = new StringBuilder();
     	String line;
    	while ((line = br.readLine()) != null) {
    		sb.append(line);
    	} 
    	br.close();
     	return sb.toString();
	}
	
	private PolicyType readPolicyPrefs(String path) throws JAXBException, SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
	}
}
