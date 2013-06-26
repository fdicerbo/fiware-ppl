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
package com.sap.research.primelife.ds.pdp.request;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.saml._2_0.assertion.AttributeStatementType;
import oasis.names.tc.saml._2_0.assertion.AttributeType;
import oasis.names.tc.saml._2_0.assertion.NameIDType;
import oasis.names.tc.saml._2_0.assertion.StatementAbstractType;

import org.apache.xerces.dom.ElementNSImpl;
import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.BooleanDataTypeAttribute;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.ds.pdp.provisionalAction.IProvisionalAction;
import com.sap.research.primelife.ds.pdp.provisionalAction.ProvisionalActionFactory;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.ConsiderMissingPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.ConsiderOnlyPresentPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.IFilterPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.IMergingStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.MatchStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.UpdateStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.IPIIQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.PiiQueryByAttributeNameStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.PiiQueryByIdStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.IPolicyQueryStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPiiStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPreferenceGroupStrategy;
import com.sap.research.primelife.ds.pdp.response.IResponseHandlingStrategy;
import com.sap.research.primelife.ds.pdp.response.ResponseAfterMergingStrategy;
import com.sap.research.primelife.ds.pdp.response.ResponseForAPIStrategy;
import com.sap.research.primelife.ds.pdp.response.ResponseForUserStrategy;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.claims.impl.ClaimType;
import eu.primelife.ppl.claims.impl.PPLPolicyStatementType;
import eu.primelife.ppl.policy.PPLEvaluatable;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.ProvisionalActionType;

/**
 * Represents all the information for a request to the PPL Engine. This includes
 * <li>the SAML-Assertion which includes provisional actions and the server's policy</li>
 * <li>the request context e.g. the preference group - this is implemented by the subclasses</li>
 * </br>
 * From these information it is possible to create the XACML Request for HERAS.
 * </br>
 * <p> Currently credential handling is not possible.</p>
 * </br>
 * 
 *
 */
public abstract class PDPRequest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PDPRequest.class);
	private static final ObjectFactory ofHerasContext = new ObjectFactory();
	private List<AttributeType> attributes = new LinkedList<AttributeType>();
	private PPLEvaluatable pplEvaluatable;
	
	private NameIDType issuer;
	private Map<String, DataHandlingPolicyType> dhpMap;
	private List<ProvisionalActionType> provisionalActions;
	
	/**
	 * Create a PDPRequest from the SAML assertion including provisional actions.
	 * @param policy - the server's SAML assertion representing the request as a string
	 * @throws SyntaxException - if the policy could not be unmarshalled
	 * @throws JAXBException - if the policy could not be unmarshalled
	 */
	public PDPRequest(String policy) throws SyntaxException, JAXBException {
		unmarshalPolicy(policy);
		
		dhpMap = createDhpMap(pplEvaluatable);
		if (pplEvaluatable.getProvisionalActions() != null) {
			provisionalActions = pplEvaluatable.getProvisionalActions().getProvisionalAction();
		}
	}
	
	/**
	 * Using the ProvisionalActionFactory, this method creates a list of provisional action handlers
	 * @return a list of ProvisionalActions for handling the request
	 * @throws ValidationException  if the handler for a provisional action is not implemented
	 * @throws SyntaxException  if the data handling policy for RevealUnderDhp cannot be found in the server's policy
	 * @throws MissingPreferenceGroupException  if a preference group needed to process the request is not found in the database
	 */
	public List<IProvisionalAction> getProvisionalActionHandlerList() throws ValidationException, SyntaxException, MissingPreferenceGroupException {
		List<IProvisionalAction> list = new LinkedList<IProvisionalAction>();
		for (ProvisionalActionType p : provisionalActions) {
			// FIXME for credential handler, forward credential Map
			list.add(ProvisionalActionFactory.getProvisionalAction(this, p, null));
		}
		return list;
	}

	/**
	 * Unmarshals the SAML assertion, extracting the issuer, attributes and the ppl policy.
	 * @param policy : the string of the server's policy
	 * @throws SyntaxException if the policy could not be unmarshalled
	 * @throws JAXBException if the policy could not be unmarshalled
	 */
	private void unmarshalPolicy(String policy) throws SyntaxException,
			JAXBException {
		LOGGER.info("Unmarshalling a SAML xml file to java object.");
		UnmarshallImpl unmarshallerSAML = UnmarshallFactory.createUnmarshallImpl(AssertionType.class.getPackage());
//			new UnmarshallImpl(AssertionType.class.getPackage());
		AssertionType assertion = (AssertionType) unmarshallerSAML.unmarshal(
				new StringReader(policy));

		//get the list of the statement with an abstract type
		List<StatementAbstractType> statementList =
			assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement();

		for (StatementAbstractType statement : statementList) {
			if (statement instanceof AttributeStatementType) {
				LOGGER.debug("Found an AttributeStatement of type");
				List<Object> attributes = ((AttributeStatementType) statement).getAttributeOrEncryptedAttribute();
				for (Object obj : attributes) {
					if (obj instanceof AttributeType)
						this.attributes.add((AttributeType) obj);
				}
			}
			
			if (statement instanceof PPLPolicyStatementType) {
				LOGGER.debug("Found a PPLPolicyStatement");
				// FIXME at his point, we suppose that we have only one Policy or PolicySet.
				// In the future, it have to be modified that it process all the policies
				pplEvaluatable = (PPLEvaluatable) ((PPLPolicyStatementType) statement)
				.getPolicyOrPolicySet().get(0);
			}
		}
		this.issuer = assertion.getIssuer();
		
	}
	
	/**
	 * 
	 * @return a Map with the attribute names of the provisional actions and the according policies
	 */
	public Map<String, DataHandlingPolicyType> getDhpMap() {
		return dhpMap;
	}
	
	/**
	 * 
	 * @return the Policy of the server with easy access for the ppl elements
	 */
	public PPLEvaluatable getEvaluatable() {
		return this.pplEvaluatable;
	}
	
	/**
	 * Create the subject for the HERAS Request out of the SAML Assertion's attributes
	 * The data type is always the AnyURIDataType, except the attribute value is true or false, then we set it to BooleanDataType.
	 * If other data types are used in the preference's target subjects, this will lead to deny. 
	 * @param a - the attribute
	 * @return a HERAS Subject
	 */
	private org.herasaf.xacml.core.context.impl.SubjectType toXacmlAttribute(AttributeType a) {
		SubjectType subjectDcId = ofHerasContext.createSubjectType();
		org.herasaf.xacml.core.context.impl.AttributeType attributeType = ofHerasContext.createAttributeType();
		AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();
		attributeType.setAttributeId(a.getName());
		// issuer is optional
		attributeType.setIssuer(this.issuer.getValue());
		
		//FIXME maybe problem with the datatype, but there is no way to guess it
		attributeType.setDataType(new AnyURIDataTypeAttribute());
		ElementNSImpl attrValue = (ElementNSImpl) a.getAttributeValue().get(0);
		String value = attrValue.getFirstChild().getNodeValue();
		attributeValue.getContent().add(value);
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			attributeType.setDataType(new BooleanDataTypeAttribute());
		}

		attributeType.getAttributeValues().add(attributeValue);
		subjectDcId.getAttributes().add(attributeType);
		return subjectDcId;
	}
	
	/**
	 * Create a simple XACML Request with a data controller id, used for testing.
	 * @param dataControllerId
	 * @param attributeName
	 * @return a request to be used for HERAS access control
	 */
	public static RequestType createDummyXacmlRequest(String dataControllerId, String attributeName) {
		//create the request query
		RequestType request = ofHerasContext.createRequestType();

		//create the DC Id subject
		SubjectType subjectDcId = ofHerasContext.createSubjectType();

		if (dataControllerId != null) {
			org.herasaf.xacml.core.context.impl.AttributeType attributeDcId = ofHerasContext.createAttributeType();
			AttributeValueType attributeValue = ofHerasContext.createAttributeValueType();
			AnyURIDataTypeAttribute anyURI = new AnyURIDataTypeAttribute();

			attributeDcId.setAttributeId("http://www.primelife.eu/ppl/DataControllerID");
			attributeDcId.setDataType(anyURI);
			attributeDcId.setIssuer(dataControllerId);

			attributeValue.getContent().add(dataControllerId);
			attributeDcId.getAttributeValues().add(attributeValue);
			subjectDcId.getAttributes().add(attributeDcId);

			request.getSubjects().add(subjectDcId);
		}

		//no european privacy Seal

		// create resource with the value of attributeType
		ResourceType resource = ofHerasContext.createResourceType();
		org.herasaf.xacml.core.context.impl.AttributeType resourceAttr = ofHerasContext.createAttributeType();
		AttributeValueType resourceAttrValue = ofHerasContext.createAttributeValueType();

		resourceAttr.setAttributeId("http://www.primelife.eu/ppl/UncertifiedAttributeType");
		resourceAttr.setDataType(new AnyURIDataTypeAttribute());
		resourceAttr.setIssuer(dataControllerId);

		resourceAttrValue.getContent().add(attributeName);
		resourceAttr.getAttributeValues().add(resourceAttrValue);
		resource.getAttributes().add(resourceAttr);
		request.getResources().add(resource);

		// create "read" action
		AttributeValueType attributeValueAction = ofHerasContext.createAttributeValueType();
		attributeValueAction.getContent().add("read");
		org.herasaf.xacml.core.context.impl.AttributeType attributeAction = ofHerasContext.createAttributeType();
		attributeAction.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		attributeAction.setDataType(new StringDataTypeAttribute());
		attributeAction.setIssuer(dataControllerId);
		attributeAction.getAttributeValues().add(attributeValueAction);
		ActionType action = ofHerasContext.createActionType();
		action.getAttributes().add(attributeAction);
		request.setAction(action);

		// create empty environment
		request.setEnvironment(new EnvironmentType());

		return request;
	}
	
	/**
	 * Create a simple XACML Request
	 * With one resource and one subject
	 * Action: read
	 * @param email
	 * @param resourceValue
	 * @return a request to be used for HERAS access control
	 */
	public static RequestType createXacmlRequest(String subjectId, String resourceId) {
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

	/**
	 * Create a XACML request used for the access control enforcement.
	 * <li>The subject is derived fom the SAML Assertion's Attributes</li>
	 * <li>The resource is derived from the attribute name of the provisional action </li>
	 * <li>the action is always set to read</li>
	 * <li>the environment is always an empty environment</li>
	 * @return	XACML request
	 */
	public RequestType createXacmlRequest(String attributeName) {
		//create the request query
		RequestType request = ofHerasContext.createRequestType();
		
		for (AttributeType a : this.attributes) {
			request.getSubjects().add(this.toXacmlAttribute(a));
		}

		// create resource with the value of attributeType
		ResourceType resource = ofHerasContext.createResourceType();
		org.herasaf.xacml.core.context.impl.AttributeType resourceAttr = ofHerasContext.createAttributeType();
		AttributeValueType resourceAttrValue = ofHerasContext.createAttributeValueType();

		resourceAttr.setAttributeId("http://www.primelife.eu/ppl/UncertifiedAttributeType");
		resourceAttr.setDataType(new AnyURIDataTypeAttribute());
		resourceAttr.setIssuer(this.issuer.getValue());

		resourceAttrValue.getContent().add(attributeName);
		QName key = new QName("AttributeId");
		String value = "http://www.primelife.eu/ppl/fileName";
		resourceAttrValue.getOtherAttributes().put(key, value);
		resourceAttr.getAttributeValues().add(resourceAttrValue);
		resource.getAttributes().add(resourceAttr);
		request.getResources().add(resource);
		

		// create "read" action
		AttributeValueType attributeValueAction = ofHerasContext.createAttributeValueType();
		attributeValueAction.getContent().add("read");
		org.herasaf.xacml.core.context.impl.AttributeType attributeAction = ofHerasContext.createAttributeType();
		attributeAction.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		attributeAction.setDataType(new StringDataTypeAttribute());
		attributeAction.setIssuer(this.issuer.getValue());
		attributeAction.getAttributeValues().add(attributeValueAction);
		ActionType action = ofHerasContext.createActionType();
		action.getAttributes().add(attributeAction);
		request.setAction(action);

		// create empty environment
		request.setEnvironment(new EnvironmentType());		
		return request;
	}
	
	/**
	 * Creates DPP map that associates the DHP id with DHP.
	 */
	private static Map<String, DataHandlingPolicyType> createDhpMap(
			PPLEvaluatable pplEvaluatable) {
		if (pplEvaluatable.getDataHandlingPolicy() != null
				&& pplEvaluatable.getDataHandlingPolicy().size() != 0) {
			Map<String, DataHandlingPolicyType> dhpMap =
				new HashMap<String, DataHandlingPolicyType>();

			for (DataHandlingPolicyType dhp : pplEvaluatable.getDataHandlingPolicy())
				dhpMap.put(dhp.getPolicyId(), dhp);

			return dhpMap;
		}

		return null;
	}

	
	/* RELATED TO CREDENTIALS */
	
	/**
	 * Checks whether the PPLEvaluatable has a credential requirement.
	 *
	 * @param pplEvaluatable
	 * @return <code>true</code> if there is a credential requirement
	 */
	private boolean hasCredentialRequirement() {
		return pplEvaluatable.getCredentialRequirements() != null
			&& pplEvaluatable.getCredentialRequirements().getCredential().size() != 0;
	}
	
	/**
	 * Returns the SAMLClaimList of the policy if a credential Requirement exists. 
	 * @return a list of SAML Claims
	 * @throws org.herasaf.xacml.core.SyntaxException 
	 * @throws WritingException 
	 * @throws SyntaxException 
	 */
	public List<ClaimType> getSamlClaimList()
			throws SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException {
		List<ClaimType> samlClaimList = null;

		//check if the PPLEvaluatable has credential requirement or not
		if (hasCredentialRequirement()) {
			//creation of the claim parameters
			LOGGER.debug("The policy contains credential requirements, call the credential handler");

			//get the list of the claims
			samlClaimList = getClaimsFromEvaluatable();
			LOGGER.debug("listSAMLClaim: " + samlClaimList);
		}

		return samlClaimList;
	}
	
	/**
	 * From the {@link PPLEvaluatable} we call the credential handler and we
	 * get the list of the {@link SAMLClaim}. This method converts at the first
	 * point the SAP PPL {@link PPLEvaluatable} into IBM {@link Evaluatable}.
	 * After that it calls the credential handler with the getCombinations
	 * function to get the list of the {@link SAMLClaim}.
	 * FIXME it is just empty method for now, needs to be updated when
	 * proper Credential Handler implementation will be provided by IBM
	 *
	 * @param pplEvaluatable
	 * 		The DC Policy
	 * @return
	 * 		List of {@link SAMLClaim}
	 * @throws SyntaxException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws org.herasaf.xacml.credential.core.SyntaxException 
	 */
	private List<ClaimType> getClaimsFromEvaluatable() throws SyntaxException,
			WritingException, org.herasaf.xacml.core.SyntaxException {
		//comvert SAP PPL Evaluatable to ibm Evaluatable
//		Evaluatable ibmEvaluatable = (Evaluatable) ConverterFunctions.sapPplToIbmPpl(pplEvaluatablepolicy);

		//get the credential handler instance from the singleton.
//		CredentialHandler ch = CHInitializer.getInstance().getCred();

		//call the credential Handler and get the EvaluationResponse to get the credential claims
//		EvaluationResponse evaluationResponse = (EvaluationResponse) ch.getCombinations(ibmEvaluatable);

		//if there's some missing credential
//		if (! evaluationResponse.getMissingCredentialIDs().isEmpty()){
			//to add to the PEP response to indicate the missing credential
			LOGGER.debug("to add to the PEP response to indicate the missing redential");
//		}

		//call the credential Handler and get the different combination of the credentials
//		List<ClaimType> listSamlClaim = evaluationResponse.getSAMLClaims();

		return null;
	}
	
	/**
	 * Either {@link PiiQueryByAttributeNameStrategy} or {@link PiiQueryByIdStrategy}. 
	 * @return the PIIQueryStrategy implementation
	 */
	public abstract IPIIQueryStrategy getPIILoadingStrategy();
	
	/**
	 * Either {@link PolicyByPiiStrategy} or {@link PolicyByPreferenceGroupStrategy}.
	 * @return the PolicyQueryStrategy implementation
	 * @throws MissingPreferenceGroupException - if the preference group of the request could not be found
	 */
	public abstract IPolicyQueryStrategy getPolicyQueryStrategy() throws MissingPreferenceGroupException;
	/**
	 * Either {@link ResponseAfterMergingStrategy} or {@link ResponseForAPIStrategy} or {@link ResponseForUserStrategy}.
	 * @return the ResponseHandlingStrategy implementation
	 */
	public abstract IResponseHandlingStrategy getResponseHandlingStrategy();
	/**
	 * Either {@link ConsiderMissingPiiStrategy} or {@link ConsiderOnlyPresentPiiStrategy}.
	 * @return the FilterPiiStrategy implementation
	 */
	public abstract IFilterPiiStrategy getFilterPiiStrategy();
	/**
	 * Either {@link UpdateStrategy} or {@link MatchStrategy}.
	 * @return the MergingStrategy implementation
	 */
	public abstract IMergingStrategy getMergingStrategy();

	public NameIDType getIssuer() {
		return issuer;
	}

	public void setIssuer(NameIDType issuer) {
		this.issuer = issuer;
	}
		
}
