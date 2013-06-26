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
package com.sap.research.primelife.pdp.impl;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.ds.pdp.PDP;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.stickypolicy.impl.ObjectFactory;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * 
 * 
 * @Version 0.1
 * @Date Jun 11, 2010
 * 
 */
@SuppressWarnings("unused")
public class PdpImplTest {

	private static UnmarshallImpl unmarshallerPrime;
	private static MarshallImpl marshallerPrime;
	private final String path = new String(
			"test/com/sap/research/primelife/pdp/impl/resources/");
	private static PDP pdp;
	private static MarshallImpl marshallerSP;
	private static ObjectFactory ofSP;
	private org.herasaf.xacml.core.context.impl.ObjectFactory ofHerasContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
		marshallerPrime = new MarshallImpl(PolicyType.class.getPackage());
		marshallerSP = new MarshallImpl(StickyPolicy.class.getPackage());
		ofSP = new ObjectFactory();
	}

	@Test
	public void _1Case() throws SyntaxException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException {
		PolicyType policy;

		/*
		 * preparing the request for the matching
		 */
		String domain = "http://www.example.com";
		ofHerasContext = new org.herasaf.xacml.core.context.impl.ObjectFactory();
		RequestType request = ofHerasContext.createRequestType();

		SubjectType subject = ofHerasContext.createSubjectType();
		AttributeType attribute = ofHerasContext.createAttributeType();
		AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();
		AnyURIDataTypeAttribute anyURI = new AnyURIDataTypeAttribute();

		attribute.setAttributeId("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
		attribute.setDataType(anyURI);
		attribute.setIssuer(domain);

		attributeValue.getContent().add(domain);
		attribute.getAttributeValues().add(attributeValue);
		subject.getAttributes().add(attribute);
		request.getSubjects().add(subject);

		pdp = new PDP();			

		policy = (PolicyType) unmarshallerPrime.unmarshal(new File(getClass().getResource("/pdpImplTest/Policy.xml").getFile()));
//		PdpResponse response = pdp.evaluate(policy, request, null); //FIXME

//		switch(response.getAction()){
//		case Access:
//			break;
//		case Deny:
//			break;
//		case Indeterminate:
//			if (response.getMismatchablePII() != null && ! response.getMismatchablePII().isEmpty()) {
//				StickyPolicy sp = response.getStickyPolicy();
//
//				File file = new File(path+"GeneratedSP.xml");
//				FileOutputStream outputFile;
//				outputFile = new FileOutputStream(file);
//				NamespacePrefixMapper mapper;
//				marshallerSP.marshal(ofSP.createStickyPolicy(sp), outputFile);
//			}
//
//			break;
//		}
	}

}
