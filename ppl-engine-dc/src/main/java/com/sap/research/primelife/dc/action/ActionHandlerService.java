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
package com.sap.research.primelife.dc.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.EventLog;
import com.sap.research.primelife.dc.logger.LoggerHandler;
import com.sap.research.primelife.dc.obligation.ObligationHandler;

import eu.primelife.ppl.pii.impl.PiiUniqueId;


public class ActionHandlerService implements IActionHandlerService{

	private static Logger LOGGER = LoggerFactory.getLogger(ActionHandlerService.class);
	private PiiUniqueIdDao piiUniqueIdDAO;
	private DaoImpl<EventLog> eventLogDAO;
	private ObligationHandler obligationHandler;
	private LoggerHandler loggerHandler;

	public ActionHandlerService(){
		piiUniqueIdDAO = new PiiUniqueIdDao();
		eventLogDAO = new DaoImpl<EventLog>();
		obligationHandler = ObligationHandler.getInstance();
		loggerHandler = new LoggerHandler();
	}
	
	public ActionHandlerService(PiiUniqueIdDao piiUniqueIdDAO, DaoImpl<EventLog> eventLogDAO, ObligationHandler obligationHandler, LoggerHandler loggerHandler){
		this.piiUniqueIdDAO = piiUniqueIdDAO;
		this.eventLogDAO = eventLogDAO;
		this.obligationHandler = obligationHandler;
		this.loggerHandler = loggerHandler;
	}
	
	/**
	 * Delete Data
	 */

	public void delete(Long piiId) {
		
		LOGGER.info("Deleting PII with PiiUniqueId: " + piiId);
		PiiUniqueId piiUniqueId = piiUniqueIdDAO.findByUniqueId(piiId);
		
		if (piiUniqueId != null) {
			loggerHandler.logDelete(piiUniqueId.getPii());
			obligationHandler.deleteObligations(piiUniqueId.getPii());
			piiUniqueIdDAO.deleteObject(piiUniqueId);
			LOGGER.info("PII deleted successfully (PiiId: " + piiId + ").");
		}
		else
			LOGGER.warn("PII identified by PiiId " + piiId + " was not found in Pii Store.");
	}

	
	/**
	 * Logs message
	 */
	
	public void log(Long piiId,String message) {
		
		LOGGER.info("Log message about PII with PiiUniqueId: " + piiId+ ", message: " + message);

		PiiUniqueId piiUniqueId = piiUniqueIdDAO.findByUniqueId(piiId);
		
		if (piiUniqueId != null) {
			EventLog log = new EventLog();
			log.setPii(piiUniqueId.getPii());
			log.setMessage(message);
			eventLogDAO.persistObject(log);
		}
		else
			LOGGER.warn("PII identified by PiiId " + piiId + " was not found in Pii Store.");
	}
	
	
	/**
	 * Logs message with DownstreamUsage
	 */
	
	public void log(Long piiId,String message,String sharedWith) {
		
		LOGGER.info("Log message about PII with PiiUniqueId: " + piiId+ ", message: " + message + ", shared with: " + sharedWith);

		PiiUniqueId piiUniqueId = piiUniqueIdDAO.findByUniqueId(piiId);
		
		if (piiUniqueId != null) {
			EventLog log = new EventLog();
			log.setPii(piiUniqueId.getPii());
			log.setMessage(message);
			log.setSharedWith(sharedWith);
			eventLogDAO.persistObject(log);
		}
		else
			LOGGER.warn("PII identified by PiiId " + piiId + " was not found in Pii Store.");
	}
	
		
	/**
	 * send notification 
	 */
   
	public void notify(Long piiId,String media,String address,String message) {
        
		LOGGER.info("Notifying about PII with PiiUniqueId: " + piiId
                + ", media: " + media + ", address: " + address
                + ", message: " + message);

        java.util.Properties props = new java.util.Properties();
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        
        //Load configuration file
        
        //java.io.InputStream filename = ActionHandlerService.class.getClassLoader().getResourceAsStream("systemconfig.txt");
        java.io.InputStream filename;
        Properties config = new Properties();
		try {
			filename = new FileInputStream(System.getProperty("systemConfigFilePath"));
			config.load(filename);
		 } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
		
		props.put("mail.smtp.host",config.getProperty("mail.smtp.host"));
	    props.put("mail.smtp.port",config.getProperty("mail.smtp.port"));
	
        if (piiId != null && address != null) {       
        	try {
	        msg.setFrom(new InternetAddress(config.getProperty("from")));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
	        msg.setSubject("Notification");
	        msg.setText(message);
	        Transport.send(msg);   
	        } catch (MessagingException e) {
	        	e.printStackTrace();
	        LOGGER.error("Email was not sent");
	        }
        }
        else
			LOGGER.warn("PII identified by PiiId " + piiId + " was not found in Pii Store.");
    	}


	public void anonymize(Long piiId) {
		throw new NotImplementedException();
	}


	public void secureLog(Long piiId) {
		throw new NotImplementedException();
	}
}
