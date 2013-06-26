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
package com.sap.research.primelife.restful.client.service;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import eu.primelife.ppl.policy.impl.AuthorizationTypeAnyItem;
import eu.primelife.ppl.policy.impl.AuthorizationsSetType;
import eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem;
import eu.primelife.ppl.policy.impl.AuthzDownstreamUsageType;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.ObjectFactory;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataSent;
import eu.primelife.ppl.policy.obligation.impl.TriggersSet;
import eu.primelife.ppl.policy.obligation.impl.TriggersSetTriggerItem;
import eu.primelife.ppl.policy.xacml.impl.ActionAttributeDesignatorType;
import eu.primelife.ppl.policy.xacml.impl.ActionMatchType;
import eu.primelife.ppl.policy.xacml.impl.ActionType;
import eu.primelife.ppl.policy.xacml.impl.ActionsType;
import eu.primelife.ppl.policy.xacml.impl.AttributeValueType;
import eu.primelife.ppl.policy.xacml.impl.AttributeValueTypeContentItem;
import eu.primelife.ppl.policy.xacml.impl.EffectType;
import eu.primelife.ppl.policy.xacml.impl.PolicyType;
import eu.primelife.ppl.policy.xacml.impl.PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem;
import eu.primelife.ppl.policy.xacml.impl.ResourceAttributeDesignatorType;
import eu.primelife.ppl.policy.xacml.impl.ResourceMatchType;
import eu.primelife.ppl.policy.xacml.impl.ResourceType;
import eu.primelife.ppl.policy.xacml.impl.ResourcesType;
import eu.primelife.ppl.policy.xacml.impl.RuleType;
import eu.primelife.ppl.policy.xacml.impl.SubjectAttributeDesignatorType;
import eu.primelife.ppl.policy.xacml.impl.SubjectMatchType;
import eu.primelife.ppl.policy.xacml.impl.SubjectType;
import eu.primelife.ppl.policy.xacml.impl.SubjectsType;
import eu.primelife.ppl.policy.xacml.impl.TargetType;
import eu.primelife.ppl.stickypolicy.impl.AttributeType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * 
 */
public class PolicyService {
	
	private static ObjectFactory ofObligation = new ObjectFactory();
	private static eu.primelife.ppl.policy.impl.ObjectFactory ofPolicy = new eu.primelife.ppl.policy.impl.ObjectFactory();
	private static eu.primelife.ppl.policy.xacml.impl.ObjectFactory ofXacml = new eu.primelife.ppl.policy.xacml.impl.ObjectFactory();
	
	/**
	 * @param delegates
	 * @param notify
	 * @param delete
	 * @param fileNames 
	 */
	public static StickyPolicy buildStickyPolicy(List<String> delegates, String notify, String delete, List<String> fileNames, boolean bool){
		
		if(delete != null){
			Date deleteDate = formatDate(delete);
			if(deleteDate == null){
				System.out.println("Unable to build StikcyPolicy because fail to parse the deleteDate.");
				return null;
			}
		}
		
		StickyPolicy stickyPolicy = new StickyPolicy();
		
		List<AttributeType> attributeList = new ArrayList<AttributeType>();
		stickyPolicy.setAttribute(attributeList);
		
		// Attribute Element
		AttributeType attribute = new AttributeType();
		attributeList.add(attribute);
		AuthorizationsSetType authorizationSet = new AuthorizationsSetType();
		attribute.setAuthorizationsSet(authorizationSet);
		ObligationsSet obligationSet = new ObligationsSet();
		attribute.setObligationsSet(obligationSet);
		
		// AuthorzationSet Element
		List<AuthorizationsSetTypeAuthorizationItem> authorizationsSetAuthorizationItemList = new ArrayList<AuthorizationsSetTypeAuthorizationItem>();
		authorizationSet.setAuthorizationItems(authorizationsSetAuthorizationItemList);
		AuthorizationsSetTypeAuthorizationItem authorizationsSetTypeAuthorizationItem = new AuthorizationsSetTypeAuthorizationItem();
		authorizationsSetAuthorizationItemList.add(authorizationsSetTypeAuthorizationItem);
		
		// AuthzDownStreamUsage Element
		AuthzDownstreamUsageType authzDownstreamUsage = new AuthzDownstreamUsageType();
		authzDownstreamUsage.setAllowed("true");
		authorizationsSetTypeAuthorizationItem.setItem(ofPolicy.createAuthzDownstreamUsage(authzDownstreamUsage));
		List<AuthorizationTypeAnyItem> authorizationAnyItemList = new ArrayList<AuthorizationTypeAnyItem>();
		
		authzDownstreamUsage.setAnyItems(authorizationAnyItemList);
		AuthorizationTypeAnyItem authorizationTypeAnyItem = new AuthorizationTypeAnyItem();
		authorizationAnyItemList.add(authorizationTypeAnyItem);
		
		// Nested Policy
		PolicyType nestedPolicy = new PolicyType();
		nestedPolicy.setPolicyId("policy-" + UUID.randomUUID());
		nestedPolicy.setRuleCombiningAlgId("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides");												  
		authorizationTypeAnyItem.setItem(ofXacml.createPolicy(nestedPolicy));
		
		//Target element
		TargetType target = new TargetType();
		nestedPolicy.setTarget(target);
		
		// Actions element
		ActionsType actions = new ActionsType();
		target.setActions(actions);
		List<ActionType> actionList = new ArrayList<ActionType>();
		actions.setAction(actionList);
		
		// Action element
		ActionType action = new ActionType();
		actionList.add(action);
		List<ActionMatchType> actionMatchList = new ArrayList<ActionMatchType>();
		action.setActionMatch(actionMatchList);
		
		//ActionMatch element
		ActionMatchType actionMatch = new ActionMatchType();
		actionMatchList.add(actionMatch);
		actionMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
		
		// AttributeValue element
		AttributeValueType attributeValue = new AttributeValueType();
		actionMatch.setAttributeValue(attributeValue);
		attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
		List<AttributeValueTypeContentItem> contentList = new ArrayList<AttributeValueTypeContentItem>();
		attributeValue.setContentItems(contentList);
		AttributeValueTypeContentItem contentItem = new AttributeValueTypeContentItem();
		contentList.add(contentItem);
		contentItem.setText("read");
		
		// ActionAttributeDesignator element
		ActionAttributeDesignatorType actionAttributeDesignator = new ActionAttributeDesignatorType();
		actionMatch.setActionAttributeDesignator(actionAttributeDesignator);
		actionAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		actionAttributeDesignator.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		
		
		// Subjects element
		SubjectsType subjects = new SubjectsType();
		target.setSubjects(subjects);
		List<SubjectType> subjectList = new ArrayList<SubjectType>();
		subjects.setSubject(subjectList);
		
		for(String delegate : delegates){
			// Subject element
			SubjectType subject = new SubjectType();
			subjectList.add(subject);
			List<SubjectMatchType> subjectMatchList = new ArrayList<SubjectMatchType>();
			subject.setSubjectMatch(subjectMatchList);
			
			// SubjectMatch element
			SubjectMatchType subjectMatch= new SubjectMatchType();
			subjectMatchList.add(subjectMatch);
			subjectMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			
			// AttributeValue element
			attributeValue = new AttributeValueType();
			subjectMatch.setAttributeValue(attributeValue);
			attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
			contentList = new ArrayList<AttributeValueTypeContentItem>();
			attributeValue.setContentItems(contentList);
			contentItem = new AttributeValueTypeContentItem();
			contentList.add(contentItem);
			contentItem.setText(delegate);
			
			// SubjectAttributeDesignatore element
			SubjectAttributeDesignatorType subjectAttributeDesignator = new SubjectAttributeDesignatorType();
			subjectMatch.setSubjectAttributeDesignator(subjectAttributeDesignator);
			subjectAttributeDesignator.setAttributeId("http://www.primelife.eu/ppl/email");
			subjectAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		}
		
		// Resources element
		ResourcesType resources = new ResourcesType();
		target.setResources(resources);
		List<ResourceType> resourceList = new ArrayList<ResourceType>();
		resources.setResource(resourceList);
		
		for(String fileName : fileNames){
			// Resource element
			ResourceType resource = new ResourceType();
			resourceList.add(resource);
			List<ResourceMatchType> resourceMatchList = new ArrayList<ResourceMatchType>();
			resource.setResourceMatch(resourceMatchList);
			
			// ResourceMatch element
			ResourceMatchType resourceMatch = new ResourceMatchType();
			resourceMatchList.add(resourceMatch);
			resourceMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			
			// AttributeValue
			attributeValue = new AttributeValueType();
			resourceMatch.setAttributeValue(attributeValue);
			attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
			contentList = new ArrayList<AttributeValueTypeContentItem>();
			attributeValue.setContentItems(contentList);
			contentItem = new AttributeValueTypeContentItem();
			contentList.add(contentItem);
			contentItem.setText(fileName);
			
			// ResourceAttributeDesignator element
			ResourceAttributeDesignatorType resourceAttributeDesignator = new ResourceAttributeDesignatorType();
			resourceMatch.setResourceAttributeDesignator(resourceAttributeDesignator);
			resourceAttributeDesignator.setAttributeId("http://www.primelife.eu/ppl/fileName");
			resourceAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		}
		
		// ObligationSet element
		List<Obligation> obligationList = new ArrayList<Obligation>();
		obligationSet.setObligation(obligationList);
		
		// Obligation element
		Obligation obligation = new Obligation();
		obligationList.add(obligation);
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(ofObligation.createActionDeletePersonalData(actionDelete));
		
		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList = new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);
		
		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		//triggerItem.setItemValue(triggerAtTime);
		//triggerItem.setItemName("TriggerAtTime");
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M30S");
		triggerItem.setItem(ofObligation.createTriggerAtTime(triggerAtTime));
		
		if(bool){
			// Obligation element
			obligation = new Obligation();
			obligationList.add(obligation);
			ActionNotifyDataSubject actionNotify = new ActionNotifyDataSubject();
			actionNotify.setAddress(notify);
			actionNotify.setMedia("Email"); 
			obligation.setAction(ofObligation.createActionNotifyDataSubject(actionNotify));
			
			// TriggerSet element
			triggerSet = new TriggersSet();
			obligation.setTriggersSet(triggerSet);
			triggerItemList = new ArrayList<TriggersSetTriggerItem>();
			triggerSet.setTriggerItems(triggerItemList);
			
			// TriggerPersonalDataSent
			triggerItem = new TriggersSetTriggerItem();
			triggerItemList.add(triggerItem);
			TriggerPersonalDataSent triggerPersonalDataSent = new TriggerPersonalDataSent();
			duration = new Duration();
			duration.setDuration("P0Y0M0DT0H1M20S");
			triggerPersonalDataSent.setMaxDelay(duration);
			triggerPersonalDataSent.setId("http://www.primelife.eu/ppl/email");
			triggerItem.setItem(ofObligation.createTriggerPersonalDataSent(triggerPersonalDataSent));
			/*TriggerPersonalDataAccessedForPurpose triggerPersonalDataAccessedForPurpose = new TriggerPersonalDataAccessedForPurpose();
			duration = new Duration();
			triggerPersonalDataAccessedForPurpose.setMaxDelay(duration);
			duration.setDuration("P0Y0M0DT0H5M0S");
			triggerItem.setItem(ofObligation.createTriggerPersonalDataAccessedForPurpose(triggerPersonalDataAccessedForPurpose));*/
		}
		
		// Rule element
		RuleType rule = new RuleType();
		rule.setRuleId("rule-" + UUID.randomUUID());
		rule.setEffect(EffectType.PERMIT);
		
		List<PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem> itemList = new ArrayList<PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem>();
		PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item = new PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem();
		item.setItem(ofXacml.createRule(rule));
		itemList.add(item);
		nestedPolicy.setCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems(itemList);
		
		return stickyPolicy;
	}
	
	public static StickyPolicy buildStickyPolicy2(List<String> delegates, String notify, List<String> fileNames){
		
		StickyPolicy stickyPolicy = new StickyPolicy();
		
		List<AttributeType> attributeList = new ArrayList<AttributeType>();
		stickyPolicy.setAttribute(attributeList);
		
		// Attribute Element
		AttributeType attribute = new AttributeType();
		attributeList.add(attribute);
		AuthorizationsSetType authorizationSet = new AuthorizationsSetType();
		attribute.setAuthorizationsSet(authorizationSet);
		ObligationsSet obligationSet = new ObligationsSet();
		attribute.setObligationsSet(obligationSet);
		
		// AuthorzationSet Element
		List<AuthorizationsSetTypeAuthorizationItem> authorizationsSetAuthorizationItemList = new ArrayList<AuthorizationsSetTypeAuthorizationItem>();
		authorizationSet.setAuthorizationItems(authorizationsSetAuthorizationItemList);
		AuthorizationsSetTypeAuthorizationItem authorizationsSetTypeAuthorizationItem = new AuthorizationsSetTypeAuthorizationItem();
		authorizationsSetAuthorizationItemList.add(authorizationsSetTypeAuthorizationItem);
		
		// AuthzDownStreamUsage Element
		AuthzDownstreamUsageType authzDownstreamUsage = new AuthzDownstreamUsageType();
		authzDownstreamUsage.setAllowed("true");
		authorizationsSetTypeAuthorizationItem.setItem(ofPolicy.createAuthzDownstreamUsage(authzDownstreamUsage));
		List<AuthorizationTypeAnyItem> authorizationAnyItemList = new ArrayList<AuthorizationTypeAnyItem>();
		
		authzDownstreamUsage.setAnyItems(authorizationAnyItemList);
		AuthorizationTypeAnyItem authorizationTypeAnyItem = new AuthorizationTypeAnyItem();
		authorizationAnyItemList.add(authorizationTypeAnyItem);
		
		// Nested Policy
		PolicyType nestedPolicy = new PolicyType();
		nestedPolicy.setPolicyId("policy-" + UUID.randomUUID());
		nestedPolicy.setRuleCombiningAlgId("urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:permit-overrides");												  
		authorizationTypeAnyItem.setItem(ofXacml.createPolicy(nestedPolicy));
		
		//Target element
		TargetType target = new TargetType();
		nestedPolicy.setTarget(target);
		
		// Actions element
		ActionsType actions = new ActionsType();
		target.setActions(actions);
		List<ActionType> actionList = new ArrayList<ActionType>();
		actions.setAction(actionList);
		
		// Action element
		ActionType action = new ActionType();
		actionList.add(action);
		List<ActionMatchType> actionMatchList = new ArrayList<ActionMatchType>();
		action.setActionMatch(actionMatchList);
		
		//ActionMatch element
		ActionMatchType actionMatch = new ActionMatchType();
		actionMatchList.add(actionMatch);
		actionMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
		
		// AttributeValue element
		AttributeValueType attributeValue = new AttributeValueType();
		actionMatch.setAttributeValue(attributeValue);
		attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
		List<AttributeValueTypeContentItem> contentList = new ArrayList<AttributeValueTypeContentItem>();
		attributeValue.setContentItems(contentList);
		AttributeValueTypeContentItem contentItem = new AttributeValueTypeContentItem();
		contentList.add(contentItem);
		contentItem.setText("read");
		
		// ActionAttributeDesignator element
		ActionAttributeDesignatorType actionAttributeDesignator = new ActionAttributeDesignatorType();
		actionMatch.setActionAttributeDesignator(actionAttributeDesignator);
		actionAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		actionAttributeDesignator.setAttributeId("urn:oasis:names:tc:xacml:1.0:action:action-id");
		
		
		// Subjects element
		SubjectsType subjects = new SubjectsType();
		target.setSubjects(subjects);
		List<SubjectType> subjectList = new ArrayList<SubjectType>();
		subjects.setSubject(subjectList);
		
		for(String delegate : delegates){
			// Subject element
			SubjectType subject = new SubjectType();
			subjectList.add(subject);
			List<SubjectMatchType> subjectMatchList = new ArrayList<SubjectMatchType>();
			subject.setSubjectMatch(subjectMatchList);
			
			// SubjectMatch element
			SubjectMatchType subjectMatch= new SubjectMatchType();
			subjectMatchList.add(subjectMatch);
			subjectMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			
			// AttributeValue element
			attributeValue = new AttributeValueType();
			subjectMatch.setAttributeValue(attributeValue);
			attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
			contentList = new ArrayList<AttributeValueTypeContentItem>();
			attributeValue.setContentItems(contentList);
			contentItem = new AttributeValueTypeContentItem();
			contentList.add(contentItem);
			contentItem.setText(delegate);
			
			// SubjectAttributeDesignatore element
			SubjectAttributeDesignatorType subjectAttributeDesignator = new SubjectAttributeDesignatorType();
			subjectMatch.setSubjectAttributeDesignator(subjectAttributeDesignator);
			subjectAttributeDesignator.setAttributeId("http://www.primelife.eu/ppl/email");
			subjectAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		}
		
		// Resources element
		ResourcesType resources = new ResourcesType();
		target.setResources(resources);
		List<ResourceType> resourceList = new ArrayList<ResourceType>();
		resources.setResource(resourceList);
		
		for(String fileName : fileNames){
			// Resource element
			ResourceType resource = new ResourceType();
			resourceList.add(resource);
			List<ResourceMatchType> resourceMatchList = new ArrayList<ResourceMatchType>();
			resource.setResourceMatch(resourceMatchList);
			
			// ResourceMatch element
			ResourceMatchType resourceMatch = new ResourceMatchType();
			resourceMatchList.add(resourceMatch);
			resourceMatch.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-equal");
			
			// AttributeValue
			attributeValue = new AttributeValueType();
			resourceMatch.setAttributeValue(attributeValue);
			attributeValue.setDataType("http://www.w3.org/2001/XMLSchema#string");
			contentList = new ArrayList<AttributeValueTypeContentItem>();
			attributeValue.setContentItems(contentList);
			contentItem = new AttributeValueTypeContentItem();
			contentList.add(contentItem);
			contentItem.setText(fileName);
			
			// ResourceAttributeDesignator element
			ResourceAttributeDesignatorType resourceAttributeDesignator = new ResourceAttributeDesignatorType();
			resourceMatch.setResourceAttributeDesignator(resourceAttributeDesignator);
			resourceAttributeDesignator.setAttributeId("http://www.primelife.eu/ppl/fileName");
			resourceAttributeDesignator.setDataType("http://www.w3.org/2001/XMLSchema#string");
		}
		
		// ObligationSet element
		List<Obligation> obligationList = new ArrayList<Obligation>();
		obligationSet.setObligation(obligationList);
		
		// Obligation element
		Obligation obligation = new Obligation();
		obligationList.add(obligation);
		ActionNotifyDataSubject actionNotify = new ActionNotifyDataSubject();
		actionNotify.setAddress(notify);
		actionNotify.setMedia("Email"); 
		obligation.setAction(ofObligation.createActionNotifyDataSubject(actionNotify));
		
		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList = new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);
		
		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H1M0S");
		triggerItem.setItem(ofObligation.createTriggerAtTime(triggerAtTime));
		
		// TriggerPeriodic element
		triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerPeriodic triggerPeriodic = new TriggerPeriodic();
		triggerPeriodic.setStart(dateAndTime);
		triggerPeriodic.setEnd(dateAndTime);
		duration = new Duration();
		duration.setDuration("P0Y0M0DT0H0M1S");
		triggerPeriodic.setMaxDelay(duration);
		duration = new Duration();
		duration.setDuration("P0Y0M0DT0H0M30S");
		triggerPeriodic.setPeriod(duration);
		triggerItem.setItem(ofObligation.createTriggerPeriodic(triggerPeriodic));
		
		// Rule element
		RuleType rule = new RuleType();
		rule.setRuleId("rule-" + UUID.randomUUID());
		rule.setEffect(EffectType.PERMIT);
		
		List<PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem> itemList = new ArrayList<PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem>();
		PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item = new PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem();
		item.setItem(ofXacml.createRule(rule));
		itemList.add(item);
		nestedPolicy.setCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems(itemList);
		
		return stickyPolicy;
	}
	
	
	
	@SuppressWarnings("deprecation")
	private static Date formatDate(String delete) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date now = new Date();
			Date d = (Date)formatter.parse(delete);
			d.setHours(now.getHours());
			d.setMinutes(now.getMinutes());
			d.setSeconds(now.getSeconds());
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> void marshal(T instanse, StringWriter stw){
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(instanse.getClass());
		
	    Marshaller m = context.createMarshaller();
	    
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    m.marshal(instanse, stw);
	    
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	
}
