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
package com.sap.research.primelife.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.Ejb3Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The initializer of the DAO (persistence manager).
 * It uses the Singleton Design Pattern to have only one instance of the EntityManagerFactory.
 * 
 * @Version 0.1
 * @Date Jun 18, 2010
 * 
 */
public class DaoInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoInitializer.class);
	protected static DaoInitializer INSTANCE;
	protected EntityManagerFactory emf;
	protected EntityManager em;
	protected static final String PERSISTANCE_UNIT_NAME = "primelifePU";

	/**
	 *  Protected constructor prevents instantiation from other classes
	 *  but not for the classes that want to inherit from this.
	 */
	protected DaoInitializer() {
	}

	/**
	 *  Private constructor prevents instantiation from other classes.
	 */
	private DaoInitializer(String persistenceUnitName) {
		LOGGER.info("core.DaoInitializer(String persistenceUnitName), " + persistenceUnitName);
		emf = configure(persistenceUnitName).buildEntityManagerFactory();
		em = emf.createEntityManager();
	}

	private static void initialize(String persistenceUnitName) {
		LOGGER.info("Persistence component initilization...");
		LOGGER.info("Persistence unit name: " + persistenceUnitName);
		INSTANCE = new DaoInitializer(persistenceUnitName);
	}

	/**
	 * Call this method to start the initializer
	 * @return instance of the class
	 */
	public static DaoInitializer getInstance() {
		return getInstance(PERSISTANCE_UNIT_NAME);
	}

	/**
	 * Call this method to start the initializer by specifying
	 * persistence unit name.
	 * @return instance of the class
	 */
	public static DaoInitializer getInstance(String persistenceUnitName) {
		if (INSTANCE == null) {
			initialize(persistenceUnitName);
		}

		return INSTANCE;
	}
	
	/**
	 * Create a singleton EntityManagerFactory
	 * @return
	 */
	public EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}
	
	/**
	 * Create a singleton EntityManager
	 * @return
	 */
	public EntityManager getEntityManager() {		
		//LOGGER.info("Get EntityManager...");
		if (em == null){
			LOGGER.debug("EntityManager is null, creating new EntityManager.");
			//em = getEntityManagerFactory().createEntityManager();
		}
		
		return em;
	}
	/**
	 * Close the EntityManager
	 */
	public void closeEntityManager(){
		if(em != null)
			em.close();
	}
	
	/**
	 * Close the EntityManagerFactory
	 */
	public void closeEntityManagerFactory(){
		if(emf != null)
			emf.close();
	}

	protected Ejb3Configuration configure(String persistenceUnitName) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		return cfg.configure(persistenceUnitName, null)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.AttributeStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.AttributeType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ClaimType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ClaimsType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ConditionStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.EvidenceStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.EvidenceType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.EvidenceTypeAnyItem.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ListPIIType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ListPIITypeValueItem.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.PPLPolicyStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.PPLPolicyStatementTypePolicyOrPolicySetItem.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ProvisionalActionStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ProvisionalActionType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ResourceQueryType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.ResponseType.class)
				.addAnnotatedClass(eu.primelife.ppl.claims.impl.StickyPolicyStatementType.class)
				.addAnnotatedClass(eu.primelife.ppl.pii.impl.PIIType.class)
				.addAnnotatedClass(eu.primelife.ppl.pii.impl.PIITypePolicySetOrPolicyItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.AttributeMatchAnyOfType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.AttributeMatchAnyOfTypeMatchValueOrUndisclosedExpressionItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.ConditionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.CredentialAttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.CredentialRequirementsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.CredentialType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.CredentialTypeAttributeMatchAnyOfOrUndisclosedExpressionItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.MatchValueType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.MatchValueTypeContentItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.PrimelifeApplyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.UndisclosedExpressionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.credential.impl.UndisclosedExpressionTypeAttributeIdItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthorizationType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthorizationTypeAnyItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthorizationsSetType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthzDownstreamUsageType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.AuthzUseForPurpose.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.CommonDHPSPType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.DataHandlingPolicyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.DataHandlingPreferencesType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.PolicySetType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.PolicyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.ProvisionalActionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.ProvisionalActionsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.RuleType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.impl.StickyPolicyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.Action.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ActionAnonymizePersonalData.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ActionLog.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ActionSecureLog.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.DateAndTime.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.Duration.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.Obligation.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.ObligationsSet.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.Trigger.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerAtTime.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerDataLost.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerDataSubjectAccess.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerOnViolation.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurposePurposeItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataDeleted.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataSent.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggersSet.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.obligation.impl.TriggersSetTriggerItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ActionAttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ActionMatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ActionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ActionsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ApplyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ApplyTypeExpressionItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.AttributeAssignmentType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.AttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.AttributeSelectorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.AttributeValueType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.AttributeValueTypeContentItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.CombinerParameterType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.CombinerParametersType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ConditionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.DefaultsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.EnvironmentAttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.EnvironmentMatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.EnvironmentType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.EnvironmentsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ExpressionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.FunctionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.IdReferenceType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ObligationType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ObligationsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicyCombinerParametersType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicySetCombinerParametersType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicySetType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicyType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ResourceAttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ResourceMatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ResourceType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.ResourcesType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.RuleCombinerParametersType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.RuleType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.SubjectAttributeDesignatorType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.SubjectMatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.SubjectType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.SubjectsType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.TargetType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.VariableDefinitionType.class)
				.addAnnotatedClass(eu.primelife.ppl.policy.xacml.impl.VariableReferenceType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.AuthorizationsMismatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.AuthorizationsSetMismatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.AuthzDownstreamUsageMismatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.AuthzUseForPurposeMismatchType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.PurposeListType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.authorization.impl.PurposeListTypePurposeItem.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.impl.AttributeType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.impl.MismatchesType.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.impl.StickyPolicy.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.obligation.impl.Mismatch.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.obligation.impl.MismatchPolicy.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.obligation.impl.Mismatches.class)
				.addAnnotatedClass(eu.primelife.ppl.stickypolicy.obligation.impl.ObligationsSet.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ActionType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AdviceType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AdviceTypeAssertionIDRefOrAssertionURIRefOrAssertionItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AssertionType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AttributeStatementType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AttributeStatementTypeAttributeOrEncryptedAttributeItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AttributeType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AttributeTypeAttributeValueItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AudienceRestrictionType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AudienceRestrictionTypeAudienceItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AuthnContextType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AuthnContextTypeContentItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AuthnStatementType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.AuthzDecisionStatementType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.BaseIDAbstractType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ConditionAbstractType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ConditionsType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ConditionsTypeConditionOrAudienceRestrictionOrOneTimeUseItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.EncryptedElementType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.EvidenceType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.EvidenceTypeAssertionIDRefOrAssertionURIRefOrAssertionItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.KeyInfoConfirmationDataType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.NameIDType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.OneTimeUseType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ProxyRestrictionType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.ProxyRestrictionTypeAudienceItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.StatementAbstractType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectConfirmationDataType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectConfirmationDataTypeContentItem.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectConfirmationType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectLocalityType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectType.class)
				.addAnnotatedClass(oasis.names.tc.saml._2_0.assertion.SubjectTypeContentItem.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ActionType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.AttributeType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueTypeContentItem.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.EnvironmentType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.MissingAttributeDetailType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.RequestType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ResourceContentType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ResourceContentTypeContentItem.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ResourceType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ResponseType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.ResultType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.StatusCodeType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.StatusDetailType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.StatusDetailTypeAnyItem.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.StatusType.class)
				.addAnnotatedClass(oasis.names.tc.xacml._2_0.context.schema.os.SubjectType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.CanonicalizationMethodType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.CanonicalizationMethodTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.DSAKeyValueType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.DigestMethodType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.DigestMethodTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.KeyInfoType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.KeyInfoTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.KeyValueType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.KeyValueTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.ManifestType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.ObjectType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.ObjectTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.PGPDataType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.PGPDataTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.RSAKeyValueType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.ReferenceType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.RetrievalMethodType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SPKIDataType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SPKIDataTypeSPKISexpAndAnyItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignatureMethodType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignatureMethodTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignaturePropertiesType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignaturePropertyType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignaturePropertyTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignatureType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignatureValueType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.SignedInfoType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.TransformType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.TransformTypeContentItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.TransformsType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.X509DataType.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.X509DataTypeX509IssuerSerialOrX509SKIOrX509SubjectNameItem.class)
				.addAnnotatedClass(org.w3._2000._09.xmldsig_.X509IssuerSerialType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.AgreementMethodType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.AgreementMethodTypeContentItem.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.CipherDataType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.CipherReferenceType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptedDataType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptedKeyType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptedType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptionMethodType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptionMethodTypeContentItem.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptionPropertiesType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptionPropertyType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.EncryptionPropertyTypeContentItem.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.ReferenceList.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.ReferenceListDataReferenceOrKeyReferenceItem.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.ReferenceType.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.ReferenceTypeAnyItem.class)
				.addAnnotatedClass(org.w3._2001._04.xmlenc_.TransformsType.class);
	}

}
