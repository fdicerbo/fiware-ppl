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
package com.sap.research.primelife.dc.dao;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dc.entity.EventLog;
import com.sap.research.primelife.exceptions.SyntaxException;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;


public class ObligationTest {

	private static PiiUniqueIdDao dao;
	private static PiiDao piiDao;
	private static EventLogDao eventDao;
	
	@BeforeClass 
	public static void setUp(){
		DaoInitializer.getInstance();
		dao = new PiiUniqueIdDao();
		eventDao = new EventLogDao();
		piiDao = new PiiDao();
	}
	
	@Test
	public void testSetPiiObligations() throws JAXBException, SyntaxException
	{
		//create PII
		PIIType pii = new PIIType();
		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#emailtest");
		pii.setAttributeValue("test4test@example.com");
		piiDao.persistObject(pii);
		
		//create PIIUniqueId
		PiiUniqueId piiUniqueId = new PiiUniqueId();
		System.out.println("Persisted PII: " + pii.getHjid());
		piiUniqueId.setId(pii.getHjid());
		piiUniqueId.setPii(pii);
		dao.persistObject(piiUniqueId);

		//load obligations
		//UnmarshallImpl unmarshal = new UnmarshallImpl(AttributeType.class.getPackage());
		//sAttributeType sp = (AttributeType) unmarshal.unmarshal(getClass().getResourceAsStream("/obligationTest/1_sp_matching.xml"));		
		//ObligationsSet obligations = sp.getObligationsSet();
		
//		 create piiId for OEE service, set obligations
//		eu.primelife.ppl.obligation.oee.PiiUniqueId piiId =
//		new eu.primelife.ppl.obligation.oee.PiiUniqueId();
//		piiId.setId(String.valueOf(piiUniqueId.getUniqueId()));
//		System.out.println("Set obligations piiUniqueId: " +  piiId.getId());
//		service.setPiiObligations(piiId, obligations);
//		
//		List<EventLog> events = eventDao.findByPii(pii);
//		int eventNumber = events.size();
//		System.out.println("Before:" + eventNumber);
//		// use pii for purpose
//		EventUsePiiForPurpose piiEvent = new EventUsePiiForPurpose();
//		PiiUniqueId piiUniqueId2 = dao.findObject(PiiUniqueId.class, pii.getHjid());
//		eu.primelife.ppl.obligation.oee.PiiUniqueId value = new eu.primelife.ppl.obligation.oee.PiiUniqueId();
//		value.setId(piiUniqueId2.getUniqueId().toString());
//		piiEvent.setPiiId(value);
//		piiEvent.setPurposeUri("http://www.w3.org/2002/01/P3Pv1/contact");
//		System.out.println("Consume Event piiId: " + piiEvent.getPiiId().getId());
//		service.consumeEvent(piiEvent);
		
//		// log is filled?
//		List<EventLog> events2 = eventDao.findByPii(pii);
//		int eventNumber2 = events2.size();
//		System.out.println("After:" + eventNumber2);
//		//Assert.assertTrue(eventNumber2 -1 == eventNumber);
//		
//		//log method content of ActionHandler
//		PiiUniqueId ourPiiUniqueId = dao.findByUniqueId(piiUniqueId2.getId());
//
//		if (piiUniqueId != null) {
//			EventLog log = new EventLog();
//			log.setPii(piiUniqueId.getPii());
//			log.setMessage("OEE dummy message");
//			eventDao.persistObject(log);
//		}
//		
		//log is filled
		List<EventLog> events3 = eventDao.findByPii(pii);
		int eventNumber3 = events3.size();
		System.out.println("After manual call:" + eventNumber3);
	}
	
	
	
	
}
