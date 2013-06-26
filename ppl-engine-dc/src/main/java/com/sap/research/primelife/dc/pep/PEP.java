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
package com.sap.research.primelife.dc.pep;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import oasis.names.tc.xacml._2_0.context.schema.os.StatusCodeType;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dc.dao.DaoImpl;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.event.EventHandler;
import com.sap.research.primelife.dc.event.IEventHandler;
import com.sap.research.primelife.dc.logger.LoggerHandler;
import com.sap.research.primelife.dc.logger.ILoggerHandler;
import com.sap.research.primelife.dc.obligation.ObligationHandler;
import com.sap.research.primelife.ds.pdp.PDP;
import com.sap.research.primelife.ds.pdp.request.DownstreamPDPRequestForIds;
import com.sap.research.primelife.ds.pdp.request.DownstreamPDPRequestForNames;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.ds.pdp.response.PdpResponse;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;
import com.sap.research.primelife.utils.ClaimsReader;
import com.sap.research.primelife.utils.ClaimsReader.PIIObject;

import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PIITypePolicySetOrPolicyItem;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.impl.AuthorizationType;
import eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem;
import eu.primelife.ppl.policy.impl.AuthzUseForPurpose;
import eu.primelife.ppl.policy.impl.PolicyType;
import eu.primelife.ppl.policy.impl.StickyPolicyType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;

/**
 * The PEP of the data controller. There is a method for each DC use case.
 * 
 * @Version 0.1
 * @Date Sep 02, 2010
 *
 */
public class PEP {

    private static final Logger LOGGER = LoggerFactory.getLogger(PEP.class);
    protected PDP pdp;
    protected PiiDao piiDao;
    protected PiiUniqueIdDao puidDao;
    protected DaoImpl<PIITypePolicySetOrPolicyItem> piiPolicySetOrPolicyItemDao;
    protected eu.primelife.ppl.policy.impl.ObjectFactory of;
    protected ObligationHandler obligationHandler;
    protected IEventHandler eventHandler;
    protected ILoggerHandler loggerHandler;

    public PEP() {
    	pdp = new PDP();
        piiDao = new PiiDao();
        puidDao = new PiiUniqueIdDao();
        piiPolicySetOrPolicyItemDao = new DaoImpl<PIITypePolicySetOrPolicyItem>();
        of = new eu.primelife.ppl.policy.impl.ObjectFactory();
        obligationHandler = ObligationHandler.getInstance();
        eventHandler = new EventHandler();
        loggerHandler = new LoggerHandler();
    }
    
    public PEP(PDP pdp, PiiDao piiDao, PiiUniqueIdDao puidDao, DaoImpl<PIITypePolicySetOrPolicyItem> piiPolicySetOrPolicyItemDao, eu.primelife.ppl.policy.impl.ObjectFactory of, ObligationHandler obligationHandler, IEventHandler eventHandler, ILoggerHandler loggerHandler) {
    	this.pdp = pdp;
    	this.piiDao = piiDao;
    	this.puidDao = puidDao;
    	this.piiPolicySetOrPolicyItemDao = piiPolicySetOrPolicyItemDao;
    	this.of = of;
    	this.obligationHandler = obligationHandler;
    	this.eventHandler = eventHandler;
    	this.loggerHandler = loggerHandler;
    }

    /**
     * Processes claims response from DS (or DC in downstream usage scenario)
     * to retrieve PIIs and associated sticky policies.
     * PIIs are persisted in PII store and obligations are set if
     * <code>setObligations</code> is <code>true</code>.
     *
     * TODO:
     * Check if received sticky policy conforms with access policy that is
     * defined for resource identified by <code>resourceName</code> parameter.
     *
     * @param resourceName    name of the DC resource that sticky policy is applicable to
     * @param claims    claims with sticky policies and PIIs' values
     * @param setObligations    defines if obligations will be set using OEE
     * @return    list of PIIs that were added to DC's PII store
     * @throws WritingException    could occur during obligation enforcement
     */
    public List<PIIType> processPolicy(String resourceName,
            ClaimsType claims, boolean setObligations)
            throws WritingException {
       
        ClaimsReader reader = new ClaimsReader(claims);
        List<PIIType> persistedPII = new LinkedList<PIIType>();
       
        for (PIIObject pii : reader.getPiiObjects()){
            
        	List<Object> stickyPolicy = reader.getStickyPolicyOfPii(pii);
            PIIType newPii = new PIIType();
            
            
            newPii.setAttributeName(pii.getAttributeName());
            System.out.println("Attribute Name "+pii.getAttributeName());
            
            newPii.setAttributeValue(pii.getAttributeValue());
            System.out.println("Attribute Value "+pii.getAttributeValue());
            
            if (stickyPolicy != null) {
                newPii.setPolicySetOrPolicy(stickyPolicy);
            }
            
        
            System.out.println("PII PEP = " + newPii);
            
            piiDao.persistObject(newPii);
            
            //piiDao.updateObject(newPii);
            LOGGER.info("PII { " + pii.getAttributeName() + " : " + pii.getAttributeValue()
                    + " } persisted with HJID " + newPii.getHjid() + ", with sticky policy id " + pii.getStickyPolicyId());
           
            //call obligation enforcement
           
            if (setObligations) {
                LOGGER.info("Setting obligations on PII with HJID " + newPii.getHjid() + ", with sticky policy id " + pii.getStickyPolicyId());
                obligationHandler.addObligations(ClaimsReader.getObligationsSetOfStickyPolicy(newPii), newPii);
            }
            persistedPII.add(newPii);
        }
        return persistedPII;
    }

    /**
     * Enables direct access to the PII store (bypassing the DC resource policy
     * control). Obligations recorded in the sticky policy are enforced
     * like in the initial scenario.
     *
     * @param name    PII's attribute name
     * @param value    PII's attribute value
     * @param spAttribute    sticky policy as AttributeType element
     * @return    PII entity stored in the PII store
     */
    public PIIType storePii(String name, String value, AttributeType spAttribute) {
    	return storePii(name, value, spAttribute, null); 
    }
    
   /**
     * Enables direct access to the PII store (bypassing the DC resource policy
     * control). Obligations recorded in the sticky policy are enforced
     * like in the initial scenario.
     *
     * @param name    PII's attribute name
     * @param value    PII's attribute value
     * @param spAttribute    sticky policy as AttributeType element
     * @param owner
     * @return    PII entity stored in the PII store
    */
    public PIIType storePii(String name, String value, AttributeType spAttribute, String owner) {
    	PIIType pii = new PIIType();
        pii.setAttributeName(name);
        pii.setAttributeValue(value);
        pii.setPolicySetOrPolicy(convertToPolicy(spAttribute));
        pii.setOwner(owner);
        piiDao.persistObject(pii);
        
        PiiUniqueId piiUniqueId = new PiiUniqueId();
		piiUniqueId.setId(pii.getHjid());
		piiUniqueId.setPii(pii);
		piiUniqueId.setUniqueId(puidDao.createUniqueId(pii));
		puidDao.persistObject(piiUniqueId);
        
        // setting obligations
        obligationHandler.addObligations(spAttribute.getObligationsSet(), pii);
        
        loggerHandler.logCreate(pii);
        
        return pii;
    }
   
    /**
     * Updates a PII and its sticky policy.
     * Also update the obligations through the ObligationManager
     * @param pii - the PII to update
     * @param name - the new name
     * @param newValue - the new value
     * @param spAttribute - the new sticky policy
     * @return 
     */
    public PIIType updatePii(PIIType pii, String name, String newValue, AttributeType spAttribute) {
    	// Delete Old Obligations
    	obligationHandler.deleteObligations(pii);
    	LOGGER.debug("Old obligations deleted");
    	
    	List<PIITypePolicySetOrPolicyItem> policyLists = pii.getPolicySetOrPolicyItems(); 
    	pii.setPolicySetOrPolicyItems(null);
    	piiDao.updateObject(pii);
    	LOGGER.debug("Removing current policy of the Pii");
    	
    	// Delete Old StickyPolicy
    	for(PIITypePolicySetOrPolicyItem obj : policyLists){
    		piiPolicySetOrPolicyItemDao.deleteObject(obj);
    	}
    	LOGGER.debug("Old StickyPolicy deleted");
    	
    	// Updating the Pii with the new StikcyPolicy
        pii.setAttributeName(name);
        pii.setAttributeValue(newValue);
        pii.setPolicySetOrPolicy(convertToPolicy(spAttribute));
        piiDao.persistObject(pii);
        LOGGER.debug("Pii Updated with the new StickyPolicy");
        
        // Setting the new obligations
        obligationHandler.addObligations(spAttribute.getObligationsSet(), pii);
        
        loggerHandler.logUpdate(pii);
        
        LOGGER.debug("New Obligation added");
        LOGGER.info("Pii sucessfully updated");
        return pii;
    }
   
    /**
     * Updates a PII
     * @param pii
     * @param name
     * @param newValue
     * @return 
     */
    public PIIType updatePii(PIIType pii, String name, String newValue) {
        pii.setAttributeName(name);
        pii.setAttributeValue(newValue);
        piiDao.updateObject(pii);
        loggerHandler.logUpdate(pii);
        return pii;
    }
    
    /**
     * Return directly the PII, null if not found
     * @param uniqueId
     * @param owner
     * @return
     */
    public PIIType getPii(Long uniqueId, String owner){
    	PiiUniqueId uid = puidDao.findByUniqueIdAndOwner(uniqueId, owner);
		if(uid == null){
			return null;
		}
		
		if(uid.getPii() == null){
			LOGGER.warn("Inconsistent state of the data base: the following uniqueId "+ uid.getUniqueId() +" has no Pii");
    		return null;
		}
		
		return uid.getPii();
    }
    
    /**
     * Delete a PII, the stickyPolicy associated to it and the obligations
     * return the value of the deleted Pii, null if PII not found
     * @param uniqueId
     * @param owner
     * @return
     */
    public String deletePii(Long uniqueId, String owner){
    	PiiUniqueId puid = puidDao.findByUniqueIdAndOwner(uniqueId, owner);
    	if(puid == null){
    		return null;
    	}
    	
    	if(puid.getPii() == null){
    		LOGGER.warn("Inconsistent state of the data base: the following uniqueId "+ puid.getUniqueId() +" has no Pii");
    		return null;
    	}
    	
    	String attrValue = puid.getPii().getAttributeValue();
    	this.deletePii(puid);
    	return attrValue;
    }
    
    /**
     * Delete a PII, the stickyPolicy associated to it and the obligations
     * @param pii
     */
     private void deletePii(PiiUniqueId puid){
    	PIIType pii = puid.getPii();
    	eventHandler.firePersonalDataDeleted(pii);
    	loggerHandler.logDelete(pii);
    	// Delete Old Obligations
    	obligationHandler.deleteObligations(pii);
    	puidDao.deleteObject(puid);
    }
    
    /**
     * Process Downstream usage request in string format
     * @param subjectId
     * @param resourceName
     * @return
     */
    public List<PIIType> processDownstreamUsageRequest(String subjectId, String resourceName){
    	RequestType request = PDPRequest.createXacmlRequest(subjectId, resourceName);
		ResponseType response;
		List<PIIType> piis = new ArrayList<PIIType>();
    	response = pdp.evaluate(request);

    	List<ResultType> results = response.getResult();
    	for(ResultType result : results){
    		if(result.getStatus().getStatusCode().getValue() == StatusCodeType.STATUS_OK){
	    		if(result.getDecision() == DecisionType.PERMIT){
	    			PIIType pii = piiDao.findObject(PIIType.class, Long.parseLong(result.getResourceId()));
	    			eventHandler.firePersonalDataSent(pii, subjectId);
	    			loggerHandler.logDownstreamUsage(pii, subjectId);
	    			piis.add(pii);
	    		}
    		}
    	}
    	
    	return piis;
    }
    
    public List<PIIType> processDownstreamUsageRequest(String subjectId){
    	List<PIIType> result = new ArrayList<PIIType>();
		List<String> names = new ArrayList<String>();
		
		for(PIIType pii : piiDao.getAllPII()){
			if(!names.contains(pii.getAttributeName())){
				names.add(pii.getAttributeName());
			}
		}
		
		for(String name : names){
			result.addAll(this.processDownstreamUsageRequest(subjectId, name));
		}
		
		return result;
    }
   
    /**
     * Same processing as in {@link PEP#processPolicy(String, ClaimsType, boolean)}
     * except it accepts claims in string format.
     *
     * @param resourceName    name of the DC resource that sticky policy is applicable to
     * @param claims    string with claims containing s
     * @param setObligations    defines if obligations will be set using OEE
     * @return    list of PIIs that were added to DC's PII store
     * @throws SyntaxException    if sticky policy cannot be unmarshalled
     * @throws WritingException    could occur during obligation enforcement
     * @throws JAXBException
     */
    public List<PIIType> processPolicy(String resourceName,
            String claims, boolean setObligations)
            throws SyntaxException, WritingException, JAXBException {
        UnmarshallImpl unmarshallerSAML = UnmarshallFactory.createUnmarshallImpl(ClaimsType.class.getPackage());
//            new UnmarshallImpl(ClaimsType.class.getPackage());
        ClaimsType claimsType = (ClaimsType) unmarshallerSAML.unmarshal(
                new StringReader(claims));

        return processPolicy(resourceName, claimsType, setObligations);
    }
   
    /**
     * Same processing as in {@link PEP#processPolicy(String, ClaimsType, boolean)}
     * except it accepts claims in Reader format.
     *
     * @param resourceName    name of the DC resource that sticky policy is applicable to
     * @param claims    Reader with claims containing pii
     * @param setObligations    defines if obligations will be set using OEE
     * @return    list of PIIs that were added to DC's PII store
     * @throws SyntaxException    if sticky policy cannot be unmarshalled
     * @throws WritingException    could occur during obligation enforcement
     * @throws JAXBException
     */
    public List<PIIType> processPolicy(String resourceName,
            Reader claims, boolean setObligations)
            throws SyntaxException, WritingException, JAXBException {
        UnmarshallImpl unmarshallerSAML = UnmarshallFactory.createUnmarshallImpl(ClaimsType.class.getPackage());
//            new UnmarshallImpl(ClaimsType.class.getPackage());
        ClaimsType claimsType = (ClaimsType) unmarshallerSAML.unmarshal(
                claims);

        return processPolicy(resourceName, claimsType, setObligations);
    }

    /**
     * (Processing is similar to DS PEP.)
     * Processes downstream usage request in string format.
     * All of the steps are the same like for DC to DS scenario except the
     * multiple PIIs will be returned for the single PII attribute name.
     *
     * @param policyQueryResponse
     *         query in the same format as DC to DS policy - only
     *         <em>RevealUnderDhp</em> will be relevant in that request
     * @return
     *         The PEP response in {@link ClaimsType} which will be passed to the UI to show the result.
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
     * @throws ValidationException
     */
    @SuppressWarnings("unchecked")
    public ClaimsType processDownstreamUsage(String policyQueryResponse, String defaultServerPolicy)
            throws SyntaxException, WritingException,
            org.herasaf.xacml.core.SyntaxException, FileNotFoundException,
            ProcessingException, MissingAttributeException,
            DatatypeConfigurationException, JAXBException, MissingPreferenceGroupException {
        
        PDPRequest pdpRequest = new DownstreamPDPRequestForNames(policyQueryResponse, defaultServerPolicy);
        PdpResponse pdpResponse = pdp.evaluate(pdpRequest, null);
        
        List<String> purposes = new ArrayList<String>();
       
        for (AuthorizationsSetTypeAuthorizationItem item : pdpRequest.getEvaluatable().getDataHandlingPolicy().get(0).getAuthorizationsSet().getAuthorizationItems()) {
            AuthorizationType auth = item.getItemValue();

            if (auth instanceof AuthzUseForPurpose) {
                AuthzUseForPurpose authForPurpose = (AuthzUseForPurpose) auth;

                for (Object any : authForPurpose.getAny()) {
                    String purpose = ((JAXBElement<String>) any).getValue();
                    purposes.add(purpose);
                }
            }
        }
       
        List<ActionResponse> actionResponses = pdpResponse.getActionResponses();
       
        //Trigger use for purpose (or data shared with third party)
   
        for (ActionResponse actionResponse : actionResponses) {  
                
            PIIType piiType = piiDao.findAllByAttributeValue(actionResponse.getEnforcementResponse().getAttributeValue());
            for (String purpose : purposes){
            	eventHandler.firePersonalDataUseForPurpose(piiType, purpose,pdpRequest.getIssuer().getValue());
            }
           
        }
   
        return pdpResponse.toClaim();
    }

    /**
     * (Processing is similar to DS PEP.)
     * Processes downstream usage request in string format.
     * All of the steps are the same like for DC to DS scenario except that
     * a special PII for requested hjids.
     *
     * @param policyQueryResponse
     *         query in the same format as DC to DS policy - only
     *         <em>RevealUnderDhp</em> will be relevant in that request
     * @return
     *         The PEP response in {@link ClaimsType} which will be passed to the UI to show the result.
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
     * @throws ValidationException
     */
    @SuppressWarnings("unchecked")
    public ClaimsType processRequestForSpecificPii(String policyQueryResponse, String defaultServerPolicy)
            throws SyntaxException, 
            WritingException,
            org.herasaf.xacml.core.SyntaxException, 
            FileNotFoundException,
            ProcessingException, 
            MissingAttributeException,
            DatatypeConfigurationException, 
            JAXBException, 
            MissingPreferenceGroupException {

        PDPRequest pdpRequest = new DownstreamPDPRequestForIds(policyQueryResponse, defaultServerPolicy);
        PdpResponse pdpResponse = pdp.evaluate(pdpRequest, null);

        List<String> purposes = new ArrayList<String>();
       
        for (AuthorizationsSetTypeAuthorizationItem item : pdpRequest.getEvaluatable().getDataHandlingPolicy().get(0).getAuthorizationsSet().getAuthorizationItems()) {
            AuthorizationType auth = item.getItemValue();

            if (auth instanceof AuthzUseForPurpose) {
                AuthzUseForPurpose authForPurpose = (AuthzUseForPurpose) auth;

                for (Object any : authForPurpose.getAny()) {
                    String purpose = ((JAXBElement<String>) any).getValue();
                    purposes.add(purpose);
                }
            }
        }
       
        //List<ActionResponse> actionResponses = pdpResponse.getActionResponses();

       
        //Trigger use for purpose (or data shared with third party)
       
//        for (ActionResponse actionResponse : actionResponses) {  
//                
//            PIIType piiType = piiDao.findAllByAttributeValue(actionResponse.getEnforcementResponse().getAttributeValue());
//            for (String purpose : purposes){
//            EventTrigger.triggerUsePiiForPurpose(piiType, purpose);
//            }
//           
//        }

        return pdpResponse.toClaim();
    }
    
    /**
     * Converts sticky policy {@link AttributeType} to {@link StickyPolicyType}
     * that is wrapped inside list.
     * @param attribute
     * @return
     */
    private List<Object> convertToPolicy(AttributeType attribute) {
        StickyPolicyType sp = of.createStickyPolicyType();
        sp.setAuthorizationsSet(attribute.getAuthorizationsSet());
        sp.setObligationsSet(attribute.getObligationsSet());

        PolicyType policy = of.createPolicyType();
        policy.setStickyPolicy(sp);
        List<Object> policies = new ArrayList<Object>(1);
        policies.add(policy);

        return policies;
    }
    
}
