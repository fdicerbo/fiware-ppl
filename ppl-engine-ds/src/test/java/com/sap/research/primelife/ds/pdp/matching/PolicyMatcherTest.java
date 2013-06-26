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
package com.sap.research.primelife.ds.pdp.matching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;


/**
 * 
 * 
 * @Version 0.1
 * @Date May 3, 2010
 * 
 */
@SuppressWarnings("unused")
public class PolicyMatcherTest {
	
	private static UnmarshallImpl unmarshallerPrime;
	private static MarshallImpl marshallerPrime;
	private static UnmarshallImpl unmarshallerSP;
	private static MarshallImpl marshallerSP;
	private static ObjectFactory ofSP;

	private String path = "test/com/sap/research/primelife/matching/pdp/resource/authorizations/";

	private static PolicyMatcher matcher;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
//		marshallerPrime = new MarshallImpl(PolicyType.class.getPackage());
//		unmarshallerSP = new UnmarshallImpl(StickyPolicy.class.getPackage());
//		marshallerSP = new MarshallImpl(StickyPolicy.class.getPackage());
//		matcher = new com.sap.research.primelife.pdp.matching.MatchingImpl();
//		ofSP = new ObjectFactory();
	}

	@Test
	public void _1Case() throws SyntaxException, FileNotFoundException, WritingException{
//		PolicyType policy ;
//		PolicyType preferences;
//		matcher = new com.sap.research.primelife.pdp.matching.MatchingImpl();
//		policy = (PolicyType)unmarshallerPrime.unmarshall(new File(path+"Policy.xml"));
//		preferences = (PolicyType)unmarshallerPrime.unmarshall(new File(path+"EmailPreferences1.xml"));
		//FIXME
//		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());
//
//		StickyPolicy sp = ofSP.createStickyPolicy();
//		sp.getAttribute().add(attr);
//
//		File file = new File(path+"SP1.xml");
//		FileOutputStream outputFile;
//		outputFile = new FileOutputStream(file);
//		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _2Case() throws SyntaxException, FileNotFoundException, WritingException{
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP2.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _3Case() throws SyntaxException, FileNotFoundException, WritingException{
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences1.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP3.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	/*
	 * Downstream usage matching
	 */
	
	//@Test
	public void _4Case() throws SyntaxException, FileNotFoundException, WritingException{
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences3.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP4.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _5Case() throws SyntaxException, FileNotFoundException, WritingException{
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP5.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _6Case() throws SyntaxException, FileNotFoundException, WritingException{
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences3.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP6.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _71thCase() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();
		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP71.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

//	@Test
	public void _72thCase() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP72.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _73thCase() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences4.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP73.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _74thCase() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences4.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP74.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}
	
	/*
	 * UseFor purposes matching
	 */
	
	//@Test
	public void _8Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences4.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP8.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _9Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP9.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _10Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences4.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP10.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}
	
	//@Test
	public void _111Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences3.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP111.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test(expected = ValidationException.class )
	public void _112Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP112.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _113Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP113.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}

	//@Test
	public void _114Case() throws SyntaxException, FileNotFoundException, WritingException {
		PolicyType policy ;
		PolicyType preferences;
		matcher = new PolicyMatcher();

		policy = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"Policy.xml"));
		preferences = (PolicyType)unmarshallerPrime.unmarshal(new File(path+"EmailPreferences2.xml"));
		//FIXME
		AttributeType attr = null;//matcher.MatchingDHPolicyDHPrefForOneAttribute("http://www.w3.org/2006/vcard/ns#email", policy.getDataHandlingPolicy().get(0), preferences.getDataHandlingPreferences());

		StickyPolicy sp = ofSP.createStickyPolicy();
		sp.getAttribute().add(attr);

		File file = new File(path+"SP114.xml");
		FileOutputStream outputFile;
		outputFile = new FileOutputStream(file);
		marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
	}
	
}
