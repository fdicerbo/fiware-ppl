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

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.ResultType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;
import com.sap.research.primelife.utils.ConverterFunctions;

import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * 
 * 
 * @Version 0.1
 * @Date Jun 11, 2010
 * 
 */
public class HerasPdpTest {

	private static UnmarshallImpl unmarshallerPrime;
	private org.herasaf.xacml.core.context.impl.ObjectFactory ofHerasContext;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		unmarshallerPrime = new UnmarshallImpl(PolicyType.class.getPackage());
	}

	/**
	 * The same subject (data controller id) and resource (pii attribute type)
	 * are specified in the target of preference policy and in the request.
	 * Result should be therefore PERMIT.
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce1() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences1.xml",
				"http://www.example.com",
				"http://www.example.org/names#user_name");
		assertEquals(DecisionType.PERMIT, decision);
	}

	/**
	 * Data controller id is different then in the preferences target.
	 * Result should be NON_APPLICABLE.
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce2() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences1.xml",
				"http://www.test.com",
				"http://www.example.org/names#user_name");
		assertEquals(DecisionType.NOT_APPLICABLE, decision);
	}

	/**
	 * Data controller id is the same as in the target of preferences but the
	 * resource type is different than requested.
	 * Result should be NON_APPLICABLE.
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce3() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences1.xml",
				"http://www.example.com",
				"http://www.w3.org/2006/vcard/ns#email");
		assertEquals(DecisionType.NOT_APPLICABLE, decision);
	}

	/**
	 * Data controller id is provided in Target element of the Policy element
	 * in preferences. Resource id is provided in Target element of nested Rule
	 * element. Subject and resources ids are the same in request and in
	 * preferences, so result should be PERMIT.
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce4() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences2.xml",
				"http://www.example.com",
				"http://www.example.org/names#user_name");
		assertEquals(DecisionType.PERMIT, decision);
	}

	/**
	 * Data controller id is provided in Target element of the Policy element
	 * in preferences. Resource id is provided in Target element of nested Rule
	 * element. Resources id is the same in request and in preferences, but
	 * subject id (data controller id) is different.
	 * Result should be NOT_APPLICABLE.
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce5() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences2.xml",
				"http://www.test.com",
				"http://www.example.org/names#user_name");
		assertEquals(DecisionType.NOT_APPLICABLE, decision);
	}

	/**
	 * This that shows tha if you override the subject id (data controller id)
	 * in Target element nested inside Rule element, than the result is
	 * NON_APLICABLE (no matter what is the subject id value in the request).
	 * @throws JAXBException
	 */
	@Test
	public void testEnforce6() throws SyntaxException, FileNotFoundException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException {
		DecisionType decision = enforce(
				"/heradPdpTest/HerasPreferences2.xml",
				"http://www.example.com",
				"http://www.w3.org/2006/vcard/ns#email");
		assertEquals(DecisionType.NOT_APPLICABLE, decision);

		DecisionType decision2 = enforce(
				"/heradPdpTest/HerasPreferences2.xml",
				"http://www.test.com",
				"http://www.w3.org/2006/vcard/ns#email");
		assertEquals(DecisionType.NOT_APPLICABLE, decision2);
	}

	/**
	 * Enforces policy given in <code>preferencePath</code> against request
	 * stored in <code>request</code>.
	 *
	 * @param preferencePath	preference policy path (in resources)
	 * @param domain			data controller id
	 * @param resource			resource attribute type
	 * @return	policy enforcement result
	 * @throws SyntaxException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws JAXBException
	 */
	private DecisionType enforce(String preferencePath, String domain,
			String resource) throws SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, JAXBException {
		SimplePDPFactory.useDefaultInitializers();
		PDP simplePDP = SimplePDPFactory.getSimplePDP();
		PolicyRepository repo = simplePDP.getPolicyRepository();
		PolicyType policy = (PolicyType) unmarshallerPrime.unmarshal(
				getClass().getResourceAsStream(preferencePath));
		repo.deploy(ConverterFunctions.convertToHerasPolicy(policy));
		RequestCtx request = createRequestContext(domain, resource);
		ResponseCtx responseCtx = simplePDP.evaluate(request);
		List<ResultType> results = responseCtx.getResponse().getResults();
		return results.get(0).getDecision();
	}

	/**
	 * Prepares the request for the matching.
	 */
	private RequestCtx createRequestContext(String domain, String resourceType) {
		ofHerasContext = new org.herasaf.xacml.core.context.impl.ObjectFactory();
		RequestType request = ofHerasContext.createRequestType();

		// set Subject element
		SubjectType subject = ofHerasContext.createSubjectType();
		AttributeType attribute = ofHerasContext.createAttributeType();
		AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();

		attribute.setAttributeId("http://www.primelife.eu/ppl/DataControllerID");
		attribute.setDataType(new AnyURIDataTypeAttribute());
		attribute.setIssuer(domain);

		attributeValue.getContent().add(domain);
		attribute.getAttributeValues().add(attributeValue);
		subject.getAttributes().add(attribute);
		request.getSubjects().add(subject);

		// set Resource element
		ResourceType resource = ofHerasContext.createResourceType();
		AttributeType resourceAttr = ofHerasContext.createAttributeType();
		AttributeValueType resourceAttrValue = ofHerasContext.createAttributeValueType();

		resourceAttr.setAttributeId("http://www.primelife.eu/ppl/UncertifiedAttributeType");
		resourceAttr.setDataType(new AnyURIDataTypeAttribute());
		resourceAttr.setIssuer(domain);

		resourceAttrValue.getContent().add(resourceType);
		resourceAttr.getAttributeValues().add(resourceAttrValue);
		resource.getAttributes().add(resourceAttr);
		request.getResources().add(resource);

		// set Action element
		ActionType action = ofHerasContext.createActionType();
		AttributeType actionAttr = ofHerasContext.createAttributeType();
		AttributeValueType actionAttrValue = ofHerasContext.createAttributeValueType();

		actionAttr.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		actionAttr.setDataType(new StringDataTypeAttribute());

		actionAttrValue.getContent().add("read");
		actionAttr.getAttributeValues().add(actionAttrValue);
		action.getAttributes().add(actionAttr);
		request.setAction(action);

		// set Environment
		request.setEnvironment(ofHerasContext.createEnvironmentType());
		return new RequestCtx(request);
	}

}
