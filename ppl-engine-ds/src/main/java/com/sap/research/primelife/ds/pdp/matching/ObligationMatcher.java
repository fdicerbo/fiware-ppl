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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.utils.IdGenerator;

import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionLog;
import eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject;
import eu.primelife.ppl.policy.obligation.impl.ObjectFactory;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose;
import eu.primelife.ppl.policy.obligation.impl.TriggersSet;

public class ObligationMatcher {


		private static final Logger LOGGER = LoggerFactory.getLogger(ObligationMatcher.class);
		private ObjectFactory ofPrimelife;
		private eu.primelife.ppl.stickypolicy.obligation.impl.ObjectFactory ofStickyPolicy;
		
		private eu.primelife.ppl.stickypolicy.obligation.impl.ObligationsSet obligSetSP; 
		private eu.primelife.ppl.stickypolicy.obligation.impl.Mismatches mismatches;
		@SuppressWarnings("unused")
		private eu.primelife.ppl.stickypolicy.obligation.impl.Mismatch mismatch;
		private eu.primelife.ppl.stickypolicy.obligation.impl.MismatchPolicy mismatchPolicy;
		@SuppressWarnings("unused")
		private TriggersSet triggersSetSP;
		private Map<String,TriggersSet> mapPolicy;
		@SuppressWarnings("unused")
		private String triggerName;
		@SuppressWarnings("unused")
		private boolean match;
		private boolean comparedur;
		private String mismatchId = "mismatch" + IdGenerator.generatePositiveInt();
		
		public ObligationMatcher() {
			ofPrimelife = new ObjectFactory();
			ofStickyPolicy = new eu.primelife.ppl.stickypolicy.obligation.impl.ObjectFactory();
			//obligSetSP = ofPrimelife.createObligationsSet();
			obligSetSP = ofStickyPolicy.createObligationsSet();
			mismatches = ofStickyPolicy.createMismatches();
			mismatch = ofStickyPolicy.createMismatch();
			mismatchPolicy = ofStickyPolicy.createMismatchPolicy();
			triggersSetSP = ofPrimelife.createTriggersSet();
			mapPolicy = new HashMap<String, TriggersSet> ();
		}
		
		/*
		 * Compare tow Duration
		 * @param durPref : Preference Duration
		 * @param durPolicy : Policy Duration 
		 */
		
		public boolean compareDuration(String durPolicy,String durPref){
		// catch any parsing exception
        try {    
        	// extract years and check 
        	if (durPolicy.charAt(1) > durPref.charAt(1)){
				return comparedur = true;
			} 
        	
        	// extract month and check
        	if ((durPolicy.charAt(1) == durPref.charAt(1)) && (durPolicy.charAt(3) > durPref.charAt(3))){
				return comparedur = true;
			}
        	
        	// extract day and check
        	if ((durPolicy.charAt(1) == durPref.charAt(1)) && (durPolicy.charAt(3) == durPref.charAt(3)) && (durPolicy.charAt(5) > durPref.charAt(5))){
        		return comparedur = true;
        	}
        	
        	// extract hour and check
        	if ((durPolicy.charAt(1) == durPref.charAt(1)) && (durPolicy.charAt(3) == durPref.charAt(3)) && (durPolicy.charAt(5) == durPref.charAt(5)) && (durPolicy.charAt(8) > durPref.charAt(8))){
        		return comparedur = true;
        	}
        	
        	// extract minute and check
        	if ((durPolicy.charAt(1) == durPref.charAt(1)) && (durPolicy.charAt(3) == durPref.charAt(3)) && (durPolicy.charAt(5) == durPref.charAt(5)) && (durPolicy.charAt(8) == durPref.charAt(8)) && (durPolicy.charAt(10) > durPref.charAt(10))){
        		return comparedur = true;
        	}
        	
        	// extract second and check
        	if ((durPolicy.charAt(1) == durPref.charAt(1)) && (durPolicy.charAt(3) == durPref.charAt(3)) && (durPolicy.charAt(5) == durPref.charAt(5)) && (durPolicy.charAt(8) == durPref.charAt(8)) && (durPolicy.charAt(10) == durPref.charAt(10) && (durPolicy.charAt(12) == durPref.charAt(12)))){
        		return comparedur = true;
        	}
        } catch (Exception e) {
        	return comparedur = false;
        }
		return comparedur;
	}

		
		public eu.primelife.ppl.stickypolicy.obligation.impl.ObligationsSet getStickyPolicy(ObligationsSet oblSetPolicy, ObligationsSet oblSetPreference){
			
			LOGGER.info("Obligations Matching...");
			System.out.println("Obligations Matching...");
			

			//get a IdGenerator
			Long obligId = (long) IdGenerator.generatePositiveInt();
			
			// test : obligationsSet policy and obligationsSet preference are null 
			if (oblSetPolicy == null && oblSetPreference == null) {
				LOGGER.info("ObligationsSet Matching result: MisMatch");
				obligSetSP.setHjid(obligId);
				obligSetSP.setMatching(false);
				System.out.println("Mismatch : Preference & Policy null");
				return obligSetSP;
			}

			// test : obligationsSet policy is not null and obligationsSet preference is null
			if (oblSetPreference == null && oblSetPolicy != null) {
				LOGGER.info("AuthorizationsSet Matching result: Match");
				obligSetSP.setHjid(obligId);
				obligSetSP.setMatching(false);	
				System.out.println("Mismatch : Preference null");
				return obligSetSP;
			}	
			
			// test : obligationsSet policy is null and obligationsSet preference is not null
			if (oblSetPreference != null && oblSetPolicy == null) {
				LOGGER.info("AuthorizationsSet Matching result: Match");
				obligSetSP.setHjid(obligId);
				obligSetSP.setMatching(false);
				System.out.println("Mismatch : Policy null");
				return obligSetSP;
			}
		
			// test: obligationsSet policy and obligationsSet preference are not null
			if (oblSetPreference != null && oblSetPolicy != null) {
				
		    // browse the mapPolicy
			for (Obligation oblPolicy : oblSetPolicy.getObligation()){
			mapPolicy.put(oblPolicy.getActionValue().getName(), oblPolicy.getTriggersSet());
			System.out.println(oblPolicy.getActionName());
		    }
		
			
			for (Obligation oblPreference : oblSetPreference.getObligation()) {
			
			   TriggersSet trigSetPolicy = mapPolicy.get(oblPreference.getActionValue().getName());
		
			   // test : TriggersSet Policy is not null
			   if (trigSetPolicy != null) {
				   
				   // loop the list of the trigger policy and the list of the trigger preference
				   for(JAXBElement<? extends Trigger> trigPolicy : trigSetPolicy.getTrigger()){
								Trigger triggerPolicy = trigPolicy.getValue();
				   for(JAXBElement<? extends Trigger> trigPref : oblPreference.getTriggersSet().getTrigger()){
								Trigger triggerPref = trigPref.getValue();
					
							//test if we have the same policy name	
							if (triggerPolicy.getName() == triggerPref.getName())
							{	
								//test if the type of the trigger is TriggerPersonalDataAccessedForPurpose
								if(triggerPref instanceof TriggerPersonalDataAccessedForPurpose){									
									
									TriggerPersonalDataAccessedForPurpose trigPrefPurp = (TriggerPersonalDataAccessedForPurpose) triggerPref;
									TriggerPersonalDataAccessedForPurpose trigPolicyPurp = (TriggerPersonalDataAccessedForPurpose) triggerPolicy;
									
									boolean matchDur = compareDuration(trigPolicyPurp.getMaxDelay().getDuration(),trigPrefPurp.getMaxDelay().getDuration());
		
									//System.out.println("polic"+trigPolicyPurp.getMaxDelay().getDuration());
									//System.out.println("pref"+trigPrefPurp.getMaxDelay().getDuration());
									//System.out.println("match Duration "+matchDur);
									
									//browse the list of purpose
									for(String policyPurp : trigPolicyPurp.getPurpose())
									for(String prefPurp : trigPrefPurp.getPurpose())
									{{	
										    //test if we have the same purpose and the policyDuration >= then the preferenceDuration
											if ((policyPurp.equalsIgnoreCase(prefPurp)) && (matchDur == true)){
												
												LOGGER.info("Authorizations Matching true");
												System.out.println("Authorizations Matching true");
												obligSetSP.setHjid(obligId);
												obligSetSP.setMatching(true);	
												obligSetSP.setObligationsSet(oblSetPolicy);
												
//												mismatches.setMismatch(mismatch);
//												mismatchPolicy.setActionAnonymizePersonalData(actionAnonymizePersonalData);
//												mismatchPolicy.setActionDeletePersonalData(actionDeletePersonalData);
//												mismatchPolicy.setActionLog(actionLog);
//												mismatchPolicy.setActionNotifyDataSubject(actionNotifyDataSubject);
//												mismatchPolicy.setActionSecureLog(actionSecureLog);
//												mismatchPolicy.setDateAndTime(dateAndTime);
//												mismatchPolicy.setDuration(duration);
//												mismatchPolicy.setElementId(policyPurp);
//												mismatchPolicy.setTriggerPersonalDataAccessedForPurpose(triggerPolicy.getName());
//												obligSetSP.setMismatches(mismatches);
												
												return obligSetSP;
												
											}else if (!(policyPurp.equalsIgnoreCase(prefPurp)) && (matchDur == true)){
												
												LOGGER.info("Authorizations Matching false = purpose");
												System.out.println("Authorizations Matching false : purpose");
												obligSetSP.setHjid(obligId);
												obligSetSP.setMatching(false);			
												obligSetSP.setObligationsSet(oblSetPolicy);
												
												//mismatchPolicy.set
												mismatches.setHjid(obligId);
												obligSetSP.setMismatchId(mismatchId);
												obligSetSP.setMismatches(mismatches);
												
												mismatchPolicy.setObligation(oblSetPolicy.getObligation());
												mismatchPolicy.setTriggerAtTime(null);
												mismatchPolicy.setDateAndTime(null);
												mismatchPolicy.setDuration(null);
												mismatchPolicy.setTriggerPeriodic(null);
												mismatchPolicy.setTriggerPersonalDataAccessedForPurpose(null);
												mismatchPolicy.setTriggerPersonalDataDeleted(null);
												mismatchPolicy.setTriggerPersonalDataSent(null);
												mismatchPolicy.setTriggerDataSubjectAccess(null);
												mismatchPolicy.setTriggerDataLost(null);
												mismatchPolicy.setTriggerOnViolation(null);
												
												if (oblPreference.getActionValue() instanceof ActionDeletePersonalData){
													
													mismatchPolicy.setActionAnonymizePersonalData(null);
													mismatchPolicy.setActionLog(null);
													mismatchPolicy.setActionNotifyDataSubject(null);
													mismatchPolicy.setActionSecureLog(null);
												
												}else if (oblPreference.getActionValue() instanceof ActionNotifyDataSubject){
													
													mismatchPolicy.setActionAnonymizePersonalData(null);
													mismatchPolicy.setActionLog(null);
													mismatchPolicy.setActionDeletePersonalData(null);
													mismatchPolicy.setActionSecureLog(null);
												
												}else if (oblPreference.getActionValue() instanceof ActionLog){
													
													mismatchPolicy.setActionAnonymizePersonalData(null);
													mismatchPolicy.setActionNotifyDataSubject(null);
													mismatchPolicy.setActionDeletePersonalData(null);
													mismatchPolicy.setActionSecureLog(null);
												}
													
												System.out.println(mismatchPolicy);
												
												return obligSetSP;
											
											}else if ((policyPurp.equalsIgnoreCase(prefPurp)) && (matchDur == false)){
												LOGGER.info("Authorizations Matching false = time");
												System.out.println("Authorizations Matching false : time");
												
												//return match=false;
												obligSetSP.setHjid(obligId);
												obligSetSP.setMatching(false);			
												obligSetSP.setObligationsSet(oblSetPolicy);
												
												//mismatchPolicy.set
												mismatches.setHjid(obligId);
												obligSetSP.setMismatchId(mismatchId);
												obligSetSP.setMismatches(mismatches);
												
												return obligSetSP;
											}else {
												
												LOGGER.info("Authorizations Matching false = time & purpose");
												System.out.println("Authorizations Matching false : time & purpose");
												
												//return match=false;
												obligSetSP.setHjid(obligId);
												obligSetSP.setMatching(false);			
												obligSetSP.setObligationsSet(oblSetPolicy);
												
												//mismatchPolicy.set
												mismatches.setHjid(obligId);
												obligSetSP.setMismatchId(mismatchId);
												obligSetSP.setMismatches(mismatches);
												obligSetSP.setMismatchId(prefPurp);
												
												return obligSetSP;
											}
									}}
								
								//test if the type of the trigger is TriggerAtTime
										
								}else if (triggerPref instanceof TriggerAtTime) {
										
										TriggerAtTime trigPrefAtTime = (TriggerAtTime) triggerPref;
										TriggerAtTime trigPolicyAtTime = (TriggerAtTime) triggerPolicy;
										
										// test duration
										if (compareDuration(trigPolicyAtTime.getMaxDelay().getDuration(),trigPrefAtTime.getMaxDelay().getDuration()) == true){
									
											//return match=true;
											obligSetSP.setHjid(obligId);
											obligSetSP.setMatching(true);
											obligSetSP.setObligationsSet(oblSetPolicy);
											obligSetSP.setMismatchId(mismatchId);
											return obligSetSP;
										}
										else{
										//TODO mismatch : duration not the same
										System.out.println("mismatch : duration not the same");
										//return match=false;
										obligSetSP.setHjid(obligId);
										obligSetSP.setMatching(false);
										obligSetSP.setObligationsSet(oblSetPolicy);
										obligSetSP.setMismatchId(mismatchId);
										obligSetSP.setMismatches(mismatches);
										return obligSetSP;
										}
									}
								
								}else{
								//TODO mismatch : trigger not the same
									LOGGER.info("mismatch : trigger not the same");	
									System.out.println("mismatch : trigger not the same");	
									//return match=false;
									obligSetSP.setHjid(obligId);
									obligSetSP.setMatching(false);		
									obligSetSP.setMismatches(mismatches);
									obligSetSP.setMismatchId(mismatchId);
									obligSetSP.setObligationsSet(oblSetPolicy);
									
									return obligSetSP;
								}
							  }
							}
					}else{
						//TODO mismatch : action not the same
						LOGGER.info("mismatch : action not the same");
						System.out.println("mismatch : action not the same");
						//return match=false;
						obligSetSP.setHjid(obligId);
						obligSetSP.setMatching(false);
						obligSetSP.setMismatches(mismatches);
						obligSetSP.setMismatchId(mismatchId);
						return obligSetSP;
					}
			}}	
		return obligSetSP;
		}
}
