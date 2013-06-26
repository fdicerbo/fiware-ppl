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
//
//import java.util.List;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import javax.xml.bind.JAXBException;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.sap.research.primelife.dao.DaoImpl;
//import com.sap.research.primelife.dao.PiiDao;
//import com.sap.research.primelife.dc.entity.EventLog;
//import com.sap.research.primelife.dc.entity.PiiUniqueId;
//import com.sap.research.primelife.exceptions.SyntaxException;
//import com.sap.research.primelife.marshalling.UnmarshallImpl;
//import com.sap.research.primelife.obligation.enforcement.client.IOEEService;
//import com.sap.research.primelife.obligation.enforcement.client.ObligationEnforcementService;
//
//import eu.primelife.ppl.pii.impl.PIIType;
//import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
//import eu.primelife.ppl.stickypolicy.impl.AttributeType;
//
//
//public class NotifyTest {
//	
//	private static IOEEService service;
//	private static PiiUniqueIdDAO dao;
//	private static PiiDao piiDao;
//	private static EventLogDAO eventDao;
//	
//	@BeforeClass 
//	public static void setUp(){
//		DaoInitializer.getInstance();
//		service = new ObligationEnforcementService().getBasicHttpBindingIOEEService();;
//		dao = new PiiUniqueIdDAO();
//		eventDao = new EventLogDAO();
//		piiDao = new PiiDao();
//	}
//	
//	@Test
//	public void testNotifications() throws AddressException, MessagingException 
//	{
//		
//		//create PII
//		PIIType pii = new PIIType();
//		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#emailtest");
//		pii.setAttributeValue("test4test@example.com");
//		piiDao.persistObject(pii);
//		
//		//create PIIUniqueId
//		PiiUniqueId piiUniqueId = new PiiUniqueId();
//		System.out.println("Persisted PII: " + pii.getHjid());
//		piiUniqueId.setId(pii.getHjid());
//		piiUniqueId.setPii(pii);
//		dao.persistObject(piiUniqueId);
//		
//		List<EventLog> events = eventDao.findByPii(pii);
//	     
//	
//		java.util.Properties props = new java.util.Properties();
//	        
//	      	props.put("mail.smtp.host", "mail.sap.corp");
//	        props.put("mail.smtp.port", "" + 25);
//	        Session session = Session.getDefaultInstance(props, null);
//
//	        Message msg = new MimeMessage(session);
//	        
//
//			if (piiUniqueId != null) {
//	        msg.setFrom(new InternetAddress("ala.eddine.jegham@sap.com"));
//	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("jegham.alaeddine@gmail.com"));
//	        msg.setSubject(piiUniqueId.toString());
//	        
//	        msg.setText("message");
//	      
//	        Transport.send(msg);
//	        System.out.println("Email sent successfully");
//			}
//			}
//
//}
