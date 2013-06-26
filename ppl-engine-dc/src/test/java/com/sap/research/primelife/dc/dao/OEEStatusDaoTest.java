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
//package com.sap.research.primelife.dc.dao;
//
//import org.junit.Test;
//
//import com.sap.research.primelife.dao.PiiDao;
//import com.sap.research.primelife.dc.entity.OEEStatus;
//import com.sap.research.primelife.dc.entity.PiiUniqueId;
//
//import eu.primelife.ppl.pii.impl.PIIType;
//import eu.primelife.ppl.policy.obligation.impl.Action;
//import eu.primelife.ppl.policy.obligation.impl.Trigger;
//
//
//public class OEEStatusDaoTest {
//
//	@Test
//	public void testConsumeEvent() {
//			
//		    //Create Pii1
//			PIIType pii = new PIIType();
//			pii.setAttributeName("http://www.example.org/names#user_name");
//			pii.setAttributeValue("Alice");
//			PiiDao piiDao = new PiiDao();
//			piiDao.persistObject(pii);
//			
//			//Create Pii2
//			PIIType pii2 = new PIIType();
//			pii2.setAttributeName("http://www.example.org/names#user_name");
//			pii2.setAttributeValue("Bob");
//			PiiDao piiDao2 = new PiiDao();
//			piiDao2.persistObject(pii2);
//			
//			
//			//Create PiiUniqueId1 
//			PiiUniqueId piiUniqueId = new PiiUniqueId();
//			piiUniqueId.setId(Long.valueOf(1));
//			piiUniqueId.setPii(pii);
//			piiUniqueId.setUniqueId(Long.valueOf(1));
//				
//			PiiUniqueIdDAO piiUniqueIdDAO = new PiiUniqueIdDAO();
//			piiUniqueIdDAO.persistObject(piiUniqueId);
//				
//			//Create PiiUniqueId2 
//			PiiUniqueId piiUniqueId2 = new PiiUniqueId();
//			piiUniqueId2.setId(Long.valueOf(2));
//			piiUniqueId2.setPii(pii);
//			piiUniqueId2.setUniqueId(Long.valueOf(2));
//				
//			PiiUniqueIdDAO piiUniqueIdDAO2 = new PiiUniqueIdDAO();
//			piiUniqueIdDAO.persistObject(piiUniqueId2);
//			
//			//Create Trigger
//			Trigger trigger = new Trigger();
//			trigger.setHjid(Long.valueOf(1));
//			trigger.setName("{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose)");
//			trigger.setMatching(true);
//			
//			//Create Action
//			Action action = new Action();
//			action.setHjid(Long.valueOf(1));
//			action.setName("ActionLog");
//			action.setMatching(true);
//			
////			OEEStatus oeeStatus = new OEEStatus();
////			oeeStatus.setId(1);
////			oeeStatus.setPiiUniqueId(piiUniqueId);
////			oeeStatus.setTrigger(trigger);
////			oeeStatus.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose)");
////			oeeStatus.setAction(action);
////			
////			OEEStatusDAO dao = new OEEStatusDAO();
////			dao.persistObject(oeeStatus);
////			
////			dao.consumeEvent(piiUniqueId,"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose)");
////		
////			System.out.println(dao.consumeEvent(piiUniqueId,"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose)"));
//
//			
//			
//		}
//
//}
