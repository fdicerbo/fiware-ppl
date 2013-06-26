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
package com.sap.research.primelife.dc.event;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.sap.research.primelife.dc.action.ActionHandler;
import com.sap.research.primelife.dc.action.IActionHandler;
import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;


/**
 * Provides methods that facilitate the OEE service method for consuming events.
 * Current implementation supports nine triggers:
 * <ul>
 * 	<li>Trigger Personal Data Accessed for Purpose</li>
 * 	<li>Trigger Personal Data Deleted</li>
 * 	<li>Trigger Personal Data Sent</li>
 * 	<li>Trigger Personal Data Deleted</li>
 * 	<li>Trigger At Time</li>
 * 	<li>Trigger Periodic</li>
 * 	<li>Trigger Data Subject Access (not implemented)</li>
 * 	<li>Trigger Data Lost (not implemented)</li>
 * 	<li>Trigger On Violation (not implemented)</li>
 * </ul>
 *
 */
public class EventHandler implements IEventHandler{

	private final static Logger LOGGER = Logger.getLogger(EventHandler.class);
	private PiiUniqueIdDao puidDao;
	private OEEStatusDao oeeDao;
	private IActionHandler actionHandler;
	
	public EventHandler(){
		puidDao = new PiiUniqueIdDao();
		oeeDao = new OEEStatusDao();
		actionHandler = new ActionHandler();
	}
	
	public EventHandler(PiiUniqueIdDao puidDao, OEEStatusDao oeeDao, IActionHandler actionHandler){
		this.puidDao = puidDao;
		this.oeeDao = oeeDao;
		this.actionHandler = actionHandler;
	}

	/**
	 * Triggers <strong>Personal Data Accessed for Purpose</strong> 
	 * @param pii		the PII associated 
	 * @param purpose	URI of the purpose for which the PII was used
	 * @param sharedWith SubjectId 
	 */
	public void firePersonalDataUseForPurpose(PIIType pii, String purpose, String sharedWith) {
		LOGGER.info("Triggering event 'Personal Data Accessed for Purpose' for PII #" + pii.getHjid() + " and purpose '" + purpose + "'");
		writeToFile("["+getDate()+"]: "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+" was used for purpose "+purpose+"\n");
		OEEStatus oeeStatus = oeeDao.consumeEvent(getPiiUniqueId(pii),"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataAccessedForPurpose");
		
		if(oeeStatus != null){
			String message = "Trigger for PII(" + pii.getAttributeName() + ") access for purpose(s) "+ purpose + " will trigger action shared with " + sharedWith;
			actionHandler.perform(oeeStatus, message, sharedWith);
		}
	}
	
	/**
	 * Triggers <strong>Personal Data Sent</strong> 
	 * @param pii		the PII associated 
	 * @param sharedWith SubjectId 
	 */
	public void firePersonalDataSent(PIIType pii, String sharedWith) {
		LOGGER.info("Triggering event 'Personal Data Sent' for PII #" + pii.getHjid());
		writeToFile("["+getDate()+"]: "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+" was shared with " + sharedWith +"\n");
		OEEStatus oeeStatus = oeeDao.consumeEvent(getPiiUniqueId(pii),"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataSent");
		if(oeeStatus != null){
			String message = "Trigger for PII(" + pii.getAttributeName() + ") sent will trigger action shared with " + sharedWith;
			actionHandler.perform(oeeStatus, message, sharedWith);
		}
	}
	
	/**
	 * Triggers <strong>Personal Data Accessed Deleted</strong> 
	 * @param pii		the PII associated 
	 */
	public void firePersonalDataDeleted(PIIType pii) {
		LOGGER.info("Triggering event 'Personal Data Deleted' for PII #" + pii.getHjid());
		writeToFile("["+getDate()+"]: "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+" has been deleted \n");
		OEEStatus oeeStatus = oeeDao.consumeEvent(getPiiUniqueId(pii),"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataDeleted");
		if(oeeStatus != null){
			String message = "["+getDate()+"]: "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+" has been deleted";
			actionHandler.perform(oeeStatus, message);
		}
	}
	
	/**
	 * Triggers <strong>At Time</strong> 
	 * @param pii		the PII associated 
	 */
	public void fireAtTime(PIIType pii) {
		LOGGER.info("Triggering event 'At Time' for PII #" + pii.getHjid());
		writeToFile("["+getDate()+"]: TriggerAtTime triggered for "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+ "\n");
		OEEStatus oeeStatus = oeeDao.consumeEvent(getPiiUniqueId(pii),"{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		if(oeeStatus != null){
			String message = "["+getDate()+"]: TriggerAtTime triggered for "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid();
			actionHandler.perform(oeeStatus, message);
		}
	}
	
	/**
	 * Triggers <strong>Periodic</strong> 
	 * @param pii		the PII associated 
	 */
	public void firePeriodic(PIIType pii) {
		LOGGER.info("Triggering event 'Periodic' for PII #" + pii.getHjid());
		writeToFile("["+getDate()+"]: TriggerPeriodic triggered for "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid()+ "\n");
		OEEStatus oeeStatus = oeeDao.consumeEvent(getPiiUniqueId(pii),"{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		if(oeeStatus != null){
			String message = "["+getDate()+"]: TriggerPeriodic triggered for "+pii.getAttributeValue()+" for pii name "+pii.getAttributeName()+" with id "+ pii.getHjid();
			actionHandler.perform(oeeStatus, message);
		}
	}
	
	/**
	 * Triggers <strong>Data Subject Access</strong> 
	 * @param pii		the PII associated 
	 */
	public void fireDataSubjectAccess(PIIType pii) {
		throw new NotImplementedException();
	}

	/**
	 * Triggers <strong>Data Lost</strong> 
	 * @param pii		the PII associated 
	 */
	public void fireDataLost(PIIType pii) {
		throw new NotImplementedException();
	}

	/**
	 * Triggers <strong>On Violation</strong> 
	 * @param pii		the PII associated 
	 */
	public void fireOnViolation(PIIType pii) {
		throw new NotImplementedException();
	}

	/**
	 * Helper method for setting PII unique id 
	 * @param pii		the PII which unique id value will be set
	 */
	private PiiUniqueId getPiiUniqueId(PIIType pii) {

		PiiUniqueId piiUniqueId = puidDao.findObject(PiiUniqueId.class, pii.getHjid());
		return piiUniqueId;		
	}
	
	
	private void writeToFile(String content){

		try {
			String filePath = getFilePath();
			if(filePath != null){
				FileWriter fstream = new FileWriter(filePath,true);
				BufferedWriter buff = new BufferedWriter(fstream);
				buff.write(content);
				buff.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getFilePath(){
		String entityName = System.getProperty("EntityName");
		
		java.io.InputStream filename;
		Properties config = new Properties();

        try {
        	if(System.getProperty("systemConfigFilePath") == null){
        		return null;
        	}
        	
        	filename = new FileInputStream(System.getProperty("systemConfigFilePath"));
        	config.load(filename);
        	if(entityName.equals("DC")){
        		return config.getProperty("DC_log");
        		
        	}
        	else if(entityName.equals("DS")){
        		return config.getProperty("DS_log");
        	}
        	else if(entityName.equals("3P")){
        		return config.getProperty("3P_log");
        	}
        	else {
        		return null;
        	}

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null ;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
	}
	
	private String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
	}
}
