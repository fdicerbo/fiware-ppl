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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusCodeType;

import org.herasaf.xacml.core.SyntaxException;
import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.research.primelife.ds.pdp.evaluation.AccessControlUtils;
import com.sap.research.primelife.ds.pdp.query.pii.IPIIQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.test.generator.PolicyGenerator;
import com.sap.research.primelife.test.matcher.IsSamePii;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;

public class PdpTest {

	private PDP pdp;
	@Mock private IPIIQueryStrategy mPiiQueryStrategy;
	@Mock private IPolicyQueryStrategy mPolicyQueryStrategy;
	@Mock private AccessControlUtils mAccessControlUtils;
	private int count;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		pdp = new PDP(mPiiQueryStrategy, mPolicyQueryStrategy, mAccessControlUtils);
	}
	
	/*
	 * Test PDP evaluate with a malformed request not specifying a resource
	 * PDP should have no interaction with IPIIQueryStrategy or IPolicyQueryStrategy
	 * PDP should return a Response containing one single Result with StatusCode to MISSING_ATTRIBUTE and Decision NOT_APPLICABLE
	 */
	@Test
	public void testEvaluateWithRequestNoResource(){
		RequestType requestWithoutResource= this.createXacmlRequestWithNoResource("toto@sap.com");
		
		ResponseType response = pdp.evaluate(requestWithoutResource);
		
		// PDP should have no interaction with IPIIQueryStrategy or IPolicyQueryStrategy
		verifyZeroInteractions(mPiiQueryStrategy);
		verifyZeroInteractions(mPolicyQueryStrategy);
		// PDP should return a Response containing one single Result with StatusCode to MISSING_ATTRIBUTE
		assertEquals(1, response.getResult().size());
		assertEquals(StatusCodeType.STATUS_MISSING_ATTRIBUTE, response.getResult().get(0).getStatus().getStatusCode().getValue());
		assertEquals(DecisionType.NOT_APPLICABLE, response.getResult().get(0).getDecision());
	}
	
	/*
	 * Test PDP evaluate with a malformed request with resource
	 * The resource will match one single Pii
	 * PDP should have call IPIIQueryStrategy to retrieve the Pii
	 * PDP should for the Pii
	 * 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
	 * 		- Evaluate the Request against the Policy
	 * PDP should return a Response containing one single Result with StatusCode to PROCESSING_ERROR and Decision NOT_APPLICABLE
	 */
	@Test       
	public void testEvaluateWithMalFormedRequestOnePii(){
		RequestType malFormedRequest = this.createXacmlMalFormedRequest("file0.ext");
		
		String owner = "0";
		
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		String delete = "P0Y0M0DT0H0M120S";
		
		List<PIIType> piis = new ArrayList<PIIType>();
		
		PIIType piiWitness = new PIIType();
		piiWitness.setHjid((long) 0);
		piiWitness.setAttributeName("file0.ext");
		piiWitness.setAttributeValue("123_file0.ext");
		piiWitness.setOwner(owner);
		resourcesList.clear(); 
		resourcesList.add("file0.ext");
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
		piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
		piis.add(piiWitness);
		
		//  The resource will match one single Pii
		ArrayList<PIIType> list = new ArrayList<PIIType>();
		list.add(piiWitness);
		when(mPiiQueryStrategy.executeQuery("file0.ext")).thenReturn(list);
		when(mPolicyQueryStrategy.executeQuery(piiWitness)).thenReturn(piiWitness.getPolicySetOrPolicy());
		
		ResponseType response = pdp.evaluate(malFormedRequest);
		
		// PDP should have call IPIIQueryStrategy to retrieve the  Pii
		verify(mPiiQueryStrategy).executeQuery("file0.ext");
		// PDP should for the Pii
		// 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
		// 		- Evaluate the Request against the Policy
		verify(mPolicyQueryStrategy).executeQuery(argThat(new IsSamePii("file0.ext", "123_file0.ext", owner, piiWitness.getPolicySetOrPolicyItems())));
		// PDP should return a Response containing one single Result with StatusCode to PROCESSING_ERROR and Decision NOT_APPLICABLE
		assertEquals(1, response.getResult().size());
		assertEquals(StatusCodeType.STATUS_PROCESSING_ERROR, response.getResult().get(0).getStatus().getStatusCode().getValue());
		assertEquals(DecisionType.NOT_APPLICABLE, response.getResult().get(0).getDecision());
	}
	
	/*
	 * Test PDP evaluate with a malformed request with resource
	 * The resource will match n Piis
	 * PDP should have call IPIIQueryStrategy to retrieve n Piis
	 * PDP should for each Pii
	 * 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
	 * 		- Evaluate the Request against the Policy
	 * PDP should return a Response containing n Results with StatusCode to PROCESSING_ERROR and Decision NOT_APPLICABLE
	 */
	@Test
	public void testEvaluateWithMalFormedRequestNPii(){
		List<String> subjectsList = new ArrayList<String>();
		subjectsList.add("toto@sap.com");
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		String delete = "P0Y0M0DT0H0M120S";
		
		int n = 3;
		List<PIIType> piis = new ArrayList<PIIType>(n);
		AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
		
		for(int i=0; i<n; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file.ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner("0");
			resourcesList.clear();
			resourcesList.add("file.ext");
			piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
			piis.add(piiWitness);
		}
		
		//  The resource will match n Piis
		when(mPiiQueryStrategy.executeQuery("file.ext")).thenReturn(piis);		
		when(mPolicyQueryStrategy.executeQuery((PIIType)anyObject())).thenAnswer(new Answer<List<Object>>(){
			@Override
			public List<Object> answer(InvocationOnMock invocation) throws Throwable {
				PIIType pii = (PIIType) invocation.getArguments()[0];
				return pii.getPolicySetOrPolicy();
			}
		});
		
		RequestType malFormedRequest = this.createXacmlMalFormedRequest("file.ext");
		ResponseType response = pdp.evaluate(malFormedRequest);
		
		// PDP should have call IPIIQueryStrategy to retrieve the n Pii
		verify(mPiiQueryStrategy).executeQuery("file.ext");
		// PDP should for each Pii
		// 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
		// 		- Evaluate the Request against the Policy
		ArgumentCaptor<PIIType> piiCapturor = ArgumentCaptor.forClass(PIIType.class);
		verify(mPolicyQueryStrategy, times(n)).executeQuery(piiCapturor.capture());
		for(int i=0; i<n; i++){
			IsSamePii matcher = new IsSamePii("file.ext", "123_file"+i+".ext", ""+0);
			assertTrue(matcher.matches(piiCapturor.getAllValues().get(i)));
		}
		// PDP should return a Response containing n Result with StatusCode to STATUS_PROCESSING_ERROR
		assertEquals(n, response.getResult().size());
		for(int i=0; i<n; i++){
			assertEquals(StatusCodeType.STATUS_PROCESSING_ERROR, response.getResult().get(i).getStatus().getStatusCode().getValue());
			assertEquals(DecisionType.NOT_APPLICABLE, response.getResult().get(i).getDecision());
		}
	}
	
	/*
	 * Test PDP evaluate with a valid request with resource
	 * The resource will match 0 Piis
	 * PDP should have call IPIIQueryStrategy to retrieve 0 Piis
	 * PDP should have no interaction with IPolicyQueryStrategy
	 * PDP should return null
	 */
	@Test
	public void testEvaluateWithRequestMatching0Pii(){
		//  The resource will match n Piis
		when(mPiiQueryStrategy.executeQuery("file.ext")).thenReturn(new ArrayList<PIIType>());		
		
		RequestType request = this.createXacmlRequest("toto@sap.com", "file.ext");
		ResponseType response = pdp.evaluate(request);
		
		// PDP should have call IPIIQueryStrategy to retrieve 0 Piis
		verify(mPiiQueryStrategy).executeQuery("file.ext");
		// PDP should have no interaction with IPolicyQueryStrategy
		verifyZeroInteractions(mPolicyQueryStrategy);
		// PDP should return null
		assertNull("The PDP response should be null", response);
	}
	
	/*
	 * Test PDP evaluate with a valid request with resource
	 * The resource will match n Piis, but no policy
	 * PDP should have call IPIIQueryStrategy to retrieve n Piis
	 * PDP should for each Pii
	 * 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
	 * 		- Not Evaluate the Request against the Policy
	 * PDP should return a Response containing n Results with StatusCode to OK and Decision NOT_APPLICABLE
	 */
	@Test
	public void testEvaluateWithRequestWithNoMatchingPolicies(){

		int n = 3;
		List<PIIType> piis = new ArrayList<PIIType>(n);
		
		for(int i=0; i<n; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file.ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner("0");
			piis.add(piiWitness);
		}
		
		//  The resource will match n Piis
		when(mPiiQueryStrategy.executeQuery("file.ext")).thenReturn(piis);		
		when(mPolicyQueryStrategy.executeQuery((PIIType)anyObject())).thenAnswer(new Answer<List<Object>>(){
			@Override
			public List<Object> answer(InvocationOnMock invocation) throws Throwable {
				PIIType pii = (PIIType) invocation.getArguments()[0];
				return pii.getPolicySetOrPolicy();
			}
		});
		
		RequestType request = this.createXacmlRequest("toto@sap.com", "file.ext");
		ResponseType response = pdp.evaluate(request);
		
		// PDP should have call IPIIQueryStrategy to retrieve the n Pii
		verify(mPiiQueryStrategy).executeQuery("file.ext");
		// PDP should for each Pii
		// 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
		// 		- Not Evaluate the Request against the Policy
		ArgumentCaptor<PIIType> piiCapturor = ArgumentCaptor.forClass(PIIType.class);
		verify(mPolicyQueryStrategy, times(n)).executeQuery(piiCapturor.capture());
		for(int i=0; i<n; i++){
			IsSamePii matcher = new IsSamePii("file.ext", "123_file"+i+".ext", ""+0);
			assertTrue(matcher.matches(piiCapturor.getAllValues().get(i)));
		}
		// PDP should return a Response containing n Result with StatusCode to STATUS_PROCESSING_ERROR
		assertEquals(n, response.getResult().size());
		for(int i=0; i<n; i++){
			assertEquals(StatusCodeType.STATUS_OK, response.getResult().get(i).getStatus().getStatusCode().getValue());
			assertEquals(DecisionType.NOT_APPLICABLE, response.getResult().get(i).getDecision());
		}
	}
	
	/*
	 * Test PDP evaluate with a valid request
	 * The resource will match n Piis, with policy
	 * PDP should have call IPIIQueryStrategy to retrieve n Piis
	 * PDP should for each Pii
	 * 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
	 * 		- Evaluate the Request against the Policy
	 * PDP should return a Response containing n Results with StatusCode to OK and Decision NOT_APPLICABLE or PERMIT
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testEvaluateWithRequestWithMatchingPolicies(){

		List<String> subjectsList = new ArrayList<String>();
		String notify = "notify@sap.com";
		List<String> resourcesList = new ArrayList<String>();
		String delete = "P0Y0M0DT0H0M120S";
		
		RequestType request = this.createXacmlRequest("toto@sap.com", "file.ext");
		
		int n = 5;
		List<PIIType> piis = new ArrayList<PIIType>(n);
		AttributeType spAttribute;
		
		for(int i=0; i<n; i++){
			PIIType piiWitness = new PIIType();
			piiWitness.setHjid((long) i);
			piiWitness.setAttributeName("file.ext");
			piiWitness.setAttributeValue("123_file"+i+".ext");
			piiWitness.setOwner("0");
			
			resourcesList.clear(); 
			resourcesList.add("file.ext");
			subjectsList.clear();
			
			if(i%2 == 0){
				subjectsList.add("toto@sap.com");
				spAttribute = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
			}else{
				subjectsList.add("titi@sap.com");
				spAttribute = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
			}
			piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute));
			piis.add(piiWitness);
		}
		
		//  The resource will match n Piis
		when(mPiiQueryStrategy.executeQuery("file.ext")).thenReturn(piis);		
		when(mPolicyQueryStrategy.executeQuery((PIIType)anyObject())).thenAnswer(new Answer<List<Object>>(){
			@Override
			public List<Object> answer(InvocationOnMock invocation) throws Throwable {
				PIIType pii = (PIIType) invocation.getArguments()[0];
				pii.getPolicySetOrPolicyItems();
				return pii.getPolicySetOrPolicy();
			}
		});
		
		try {
			count = 0;
			when(mAccessControlUtils.checkAccess((List<Object>) anyObject(), eq(request))).thenAnswer(new Answer<org.herasaf.xacml.core.context.impl.DecisionType>() {

				@Override
				public org.herasaf.xacml.core.context.impl.DecisionType answer(InvocationOnMock invocation) {
					org.herasaf.xacml.core.context.impl.DecisionType decision;
					if(count%2 == 0){
						decision = org.herasaf.xacml.core.context.impl.DecisionType.PERMIT;
					}else{
						decision = org.herasaf.xacml.core.context.impl.DecisionType.NOT_APPLICABLE;
					}
					count++;
					return decision;
				}
				
			});
		} catch (WritingException e) {
			e.printStackTrace();
		} catch (SyntaxException e) {
			e.printStackTrace();
		} catch (com.sap.research.primelife.exceptions.SyntaxException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		ResponseType response = pdp.evaluate(request);
		
		// PDP should have call IPIIQueryStrategy to retrieve the n Pii
		verify(mPiiQueryStrategy).executeQuery("file.ext");
		// PDP should for each Pii
		// 		- Call IPolicyQueryStrategy to retrieve the Policy of the Pii
		// 		- Not Evaluate the Request against the Policy
		ArgumentCaptor<PIIType> piiCapturor = ArgumentCaptor.forClass(PIIType.class);
		verify(mPolicyQueryStrategy, times(n)).executeQuery(piiCapturor.capture());
		for(int i=0; i<n; i++){
			IsSamePii matcher = new IsSamePii("file.ext", "123_file"+i+".ext", ""+0);
			assertTrue(matcher.matches(piiCapturor.getAllValues().get(i)));
		}
		// PDP should return a Response containing n Results with StatusCode to OK and Decision NOT_APPLICABLE or PERMIT
		assertEquals(n, response.getResult().size());
		for(int i=0; i<n; i++){
			assertEquals(StatusCodeType.STATUS_OK, response.getResult().get(i).getStatus().getStatusCode().getValue());
			if(i%2 == 0){
				assertEquals(DecisionType.PERMIT, response.getResult().get(i).getDecision());
			}else{
				assertEquals(DecisionType.NOT_APPLICABLE, response.getResult().get(i).getDecision());
			}
		}
	}
	
	private RequestType createXacmlRequestWithNoResource(String subjectId) {
		ObjectFactory ofHerasContext = new ObjectFactory();
		
		//create the request query
		RequestType request = ofHerasContext.createRequestType();

		//create the Subject
		SubjectType subjectEmail = ofHerasContext.createSubjectType();

		org.herasaf.xacml.core.context.impl.AttributeType attributeEmail = ofHerasContext.createAttributeType();
		AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();
		StringDataTypeAttribute stringDataType = new StringDataTypeAttribute();

		attributeEmail.setAttributeId("http://www.primelife.eu/ppl/email");
		attributeEmail.setDataType(stringDataType);
		
		attributeValue.getContent().add(subjectId);
		attributeEmail.getAttributeValues().add(attributeValue);
		subjectEmail.getAttributes().add(attributeEmail);
		request.getSubjects().add(subjectEmail);

		// create "read" action
		AttributeValueType attributeValueAction = ofHerasContext.createAttributeValueType();
		attributeValueAction.getContent().add("read");
		org.herasaf.xacml.core.context.impl.AttributeType attributeAction = ofHerasContext.createAttributeType();
		attributeAction.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		attributeAction.setDataType(new StringDataTypeAttribute());
		attributeAction.getAttributeValues().add(attributeValueAction);
		ActionType action = ofHerasContext.createActionType();
		action.getAttributes().add(attributeAction);
		request.setAction(action);

		// create empty environment
		request.setEnvironment(new EnvironmentType());

		return request;
	}
	
	private RequestType createXacmlRequest(String subjectId, String resourceId) {
		ObjectFactory ofHerasContext = new ObjectFactory();
		
		//create the request query
		RequestType request = ofHerasContext.createRequestType();

		//create the Subject
		SubjectType subjectEmail = ofHerasContext.createSubjectType();

		org.herasaf.xacml.core.context.impl.AttributeType attributeEmail = ofHerasContext.createAttributeType();
		AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();
		StringDataTypeAttribute stringDataType = new StringDataTypeAttribute();

		attributeEmail.setAttributeId("http://www.primelife.eu/ppl/email");
		attributeEmail.setDataType(stringDataType);
		
		attributeValue.getContent().add(subjectId);
		attributeEmail.getAttributeValues().add(attributeValue);
		subjectEmail.getAttributes().add(attributeEmail);
		request.getSubjects().add(subjectEmail);
		
		// create Resource with the value of attributeType
		ResourceType resource = ofHerasContext.createResourceType();
		org.herasaf.xacml.core.context.impl.AttributeType resourceAttr = ofHerasContext.createAttributeType();
		AttributeValueType resourceAttrValue = ofHerasContext.createAttributeValueType();

		resourceAttr.setAttributeId("http://www.primelife.eu/ppl/fileName");
		resourceAttr.setDataType(new StringDataTypeAttribute());

		resourceAttrValue.getContent().add(resourceId);
		resourceAttr.getAttributeValues().add(resourceAttrValue);
		resource.getAttributes().add(resourceAttr);
		request.getResources().add(resource);

		// create "read" action
		AttributeValueType attributeValueAction = ofHerasContext.createAttributeValueType();
		attributeValueAction.getContent().add("read");
		org.herasaf.xacml.core.context.impl.AttributeType attributeAction = ofHerasContext.createAttributeType();
		attributeAction.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		attributeAction.setDataType(new StringDataTypeAttribute());
		attributeAction.getAttributeValues().add(attributeValueAction);
		ActionType action = ofHerasContext.createActionType();
		action.getAttributes().add(attributeAction);
		request.setAction(action);

		// create empty environment
		request.setEnvironment(new EnvironmentType());

		return request;
	}
	
	private RequestType createXacmlMalFormedRequest(String resourceId) {
		ObjectFactory ofHerasContext = new ObjectFactory();
		
		//create the request query
		RequestType request = ofHerasContext.createRequestType();

		//create the Subject
		
		// create Resource with the value of attributeType
		ResourceType resource = ofHerasContext.createResourceType();
		org.herasaf.xacml.core.context.impl.AttributeType resourceAttr = ofHerasContext.createAttributeType();
		AttributeValueType resourceAttrValue = ofHerasContext.createAttributeValueType();

		resourceAttr.setAttributeId("http://www.primelife.eu/ppl/fileName");
		resourceAttr.setDataType(new StringDataTypeAttribute());

		resourceAttrValue.getContent().add(resourceId);
		resourceAttr.getAttributeValues().add(resourceAttrValue);
		resource.getAttributes().add(resourceAttr);
		request.getResources().add(resource);

		// create "read" action
		AttributeValueType attributeValueAction = ofHerasContext.createAttributeValueType();
		attributeValueAction.getContent().add("read");
		org.herasaf.xacml.core.context.impl.AttributeType attributeAction = ofHerasContext.createAttributeType();
		attributeAction.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		attributeAction.setDataType(new StringDataTypeAttribute());
		attributeAction.getAttributeValues().add(attributeValueAction);
		ActionType action = ofHerasContext.createActionType();
		action.getAttributes().add(attributeAction);
		request.setAction(action);

		// create empty environment
		request.setEnvironment(new EnvironmentType());

		return request;
	}/*
	List<ResponseType> pdpResponses = new ArrayList<ResponseType>();
for(int i=0; i<n; i++){
PIIType piiWitness = new PIIType();
piiWitness.setHjid((long) i);
piiWitness.setAttributeName("file"+i+".ext");
piiWitness.setAttributeValue("123_file"+i+".ext");
piiWitness.setOwner((long) i);
resourcesList.clear();
resourcesList.add("file"+i+".ext");
AttributeType spAttribute0 = PolicyGenerator.buildStickyPolicy(subjectsList, notify, delete, resourcesList).getAttribute().get(0);
piiWitness.setPolicySetOrPolicy(PolicyGenerator.convertToPolicy(spAttribute0));
piis.add(piiWitness);

ResponseType mResponseType = new ResponseType();
List<ResultType> mResults = new ArrayList<ResultType>();
mResponseType.setResult(mResults);
ResultType mResult = new ResultType();
DecisionType mDecision = DecisionType.fromValue("Permit");
mResult.setDecision(mDecision);
mResult.setResourceId(""+i);
StatusCodeType statusCode = new StatusCodeType();
statusCode.setValue(StatusCodeType.STATUS_OK);
StatusType status = new StatusType();
status.setStatusCode(statusCode);
mResult.setStatus(status);
mResults.add(mResult);
pdpResponses.add(mResponseType);
}*/
}
