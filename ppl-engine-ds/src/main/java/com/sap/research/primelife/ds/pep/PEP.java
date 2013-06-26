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
package com.sap.research.primelife.ds.pep;

import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.saml._2_0.assertion.NameIDType;
import oasis.names.tc.saml._2_0.assertion.ObjectFactory;

import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pdp.PDP;
import com.sap.research.primelife.ds.pdp.request.DataSubjectPDPRequest;
import com.sap.research.primelife.ds.pdp.request.DataSubjectUpdatePrefRequest;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.ds.pdp.response.PdpResponse;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.utils.IdGenerator;

import eu.primelife.ppl.claims.impl.ClaimType;
import eu.primelife.ppl.claims.impl.ClaimsType;


/**
 * The data subject's PEP, which is used to access local PII. It can match preference groups against a policy, 
 * as well as it can update the preference group. Internally it calls the PDP and selects the right PDPRequest for the processing.
 */
@SuppressWarnings("unused")
public class PEP {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PEP.class);
	private static ObjectFactory ofSAML = new ObjectFactory();
	private static final org.herasaf.xacml.core.context.impl.ObjectFactory ofHerasContext = new org.herasaf.xacml.core.context.impl.ObjectFactory();
//	private AssertionType assertion;

	protected PDP pdp;
	protected PolicyDao dao = new PolicyDao();

	/**
	 * Default constructor
	 */
	public PEP(){
		pdp = new PDP();
	}
	
	/**
	 * Updates a preference group. After updating, the same policyQuery won't cause a mismatch.
	 * If newPreferenceGroupId is set, it copies the preferenceGroup identified by preferenceGroupId
	 * and persists a matching preference group under the new name.
	 * Otherwise, it updates the preferenceGroup identified by preferenceGroupId.
	 * 
	 * @param policyQuery - the SAML Assertion, same input as in {@link #process(String, String)}
	 * @param preferenceGroupId - the preference group to update
	 * @param newPreferenceGroupId - optional: the new Name of the updated preference group
	 * @throws JAXBException
	 * @throws WritingException
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws MissingPreferenceGroupException
	 */
	public void updatePreferenceGroup(String policyQuery, String preferenceGroupId, String newPreferenceGroupId)
			throws JAXBException, WritingException, SyntaxException,
			FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, MissingPreferenceGroupException {
		// check whether the preference group exists
		String preferenceGroup = preferenceGroupId;
		if (newPreferenceGroupId != null) {
			//clone preferenceGroup with preferenceGroupId and set the new name
			//may throw a validationException if the newPreferenceGroupId is already used for another
			dao.clonePreferenceGroup(preferenceGroupId, newPreferenceGroupId);
			preferenceGroup = newPreferenceGroupId;
		}
		//create a new Request for the updatePreferences
		PDPRequest req = new DataSubjectUpdatePrefRequest(policyQuery, preferenceGroup);
		//ignore credentials
		PdpResponse resp = pdp.evaluate(req, null);
		//call the pdp
		//check if the processing was successful
	}

	/**
	 * Processes a request to access resource. It understands the SAML assertion with its policy (the server's request)
	 * and loads requested PII from the database, performs access control, matches with the specified preference group. 
	 * @param policyQueryResponse The DC policy for the specific resource
	 * @param preferenceGroup -(optional) if not null, specifying the policy to use
	 * @return The PEP response in {@link ClaimsType} which will be passed to the UI to show the result.
	 * @throws SyntaxException
	 * @throws WritingException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws FileNotFoundException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws DatatypeConfigurationException
	 * @throws org.herasaf.xacml.credential.core.SyntaxException 
	 * @throws JAXBException
	 * @throws MissingPreferenceGroupException 
	 */
	public ClaimsType process(String policyQueryResponse, String preferenceGroup)
			throws SyntaxException, WritingException,
			org.herasaf.xacml.core.SyntaxException, FileNotFoundException,
			ProcessingException, MissingAttributeException,
			DatatypeConfigurationException, JAXBException, MissingPreferenceGroupException {
		/* STEP 1:
		 * Decomposition of the policy and policySets
		 */
		PDPRequest pepRequest = new DataSubjectPDPRequest(policyQueryResponse, preferenceGroup);

		/* STEP 2:
		 * Invoke the credentialHandler
		 * FIXME we only consider the first policy
		 */
		// Get the claims using the Credential Handler 
		// FIXME Credential Handler is not used
		List<ClaimType> samlClaimList = pepRequest.getSamlClaimList();

		/* STEP 3:
		 * Invoke the policy decision point
		 * consider credentials if existing
		 */
		
		PdpResponse pdpResponseFinal = null;
		//if there are credentials
		if (samlClaimList != null) {
			//loop over the different claim combinations
			for (ClaimType claim : samlClaimList) {
				/*PdpResponse pdpReponse = */
				pdp.evaluate(pepRequest, claim);
				//TODO do some process
			}
		}
		else {
			pdpResponseFinal = pdp.evaluate(pepRequest, null);
		}

		return pdpResponseFinal.toClaim();
	}

	/**
	 * Creates an assertion to communicate the PDP's decision
	 * sets id, version, nameId and issue instant.
	 * @return the decision assertion
	 * @throws DatatypeConfigurationException
	 */
	public static AssertionType createSamlAssertion()
			throws DatatypeConfigurationException {
		/*
		 * Put the Decision result Assertion
		 */
		AssertionType decisionAssertion = ofSAML.createAssertionType();
		decisionAssertion.setVersion("2.0");
		decisionAssertion.setID("assertion"
				+ String.valueOf(IdGenerator.generatePositiveInt()));
		decisionAssertion.setIssueInstant(DatatypeFactoryImpl.newInstance()
				.newXMLGregorianCalendar(new GregorianCalendar(TimeZone.getDefault())));
		NameIDType nameId = new NameIDType();
		nameId.setValue("http://www.primelife.eu/claims/self-issued");
		decisionAssertion.setIssuer(nameId);
		
		return decisionAssertion;
	}
}
