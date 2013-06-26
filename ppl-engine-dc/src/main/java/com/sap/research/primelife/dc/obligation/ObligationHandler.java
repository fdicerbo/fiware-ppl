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
package com.sap.research.primelife.dc.obligation;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.dc.entity.OEEStatus;
import com.sap.research.primelife.dc.timebasedtrigger.ITimeBasedTriggerHandler;
import com.sap.research.primelife.dc.timebasedtrigger.TimeBasedTriggerHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;

public class ObligationHandler implements IObligationHandler{
	
	private final static Logger LOGGER = Logger.getLogger(ObligationHandler.class);
	
	private OEEStatusDao oeeStatusDao;
	private PiiUniqueIdDao piiUniqueIdDao;
	private ITimeBasedTriggerHandler timeBasedTriggerHandler;
	private static ObligationHandler instance = null;
	
	public static ObligationHandler getInstance(){
		if(instance == null){
			instance = new ObligationHandler();
		}
		return instance;
	}

	protected ObligationHandler(){
		oeeStatusDao = new OEEStatusDao();
		piiUniqueIdDao = new PiiUniqueIdDao();
		timeBasedTriggerHandler = TimeBasedTriggerHandler.getInstance();
	}
	
	protected ObligationHandler(OEEStatusDao oeeStatusDao, PiiUniqueIdDao piiUniqueIdDao, ITimeBasedTriggerHandler timeBasedTriggerHandler){
		this.oeeStatusDao = oeeStatusDao;
		this.piiUniqueIdDao = piiUniqueIdDao;
		this.timeBasedTriggerHandler = timeBasedTriggerHandler;
	}
	
	public void addObligations(ObligationsSet obligationSet, PIIType pii){
		PiiUniqueId piiUniqueId = piiUniqueIdDao.findByPiiId(pii.getHjid());
		
		if(piiUniqueId != null){
			for (Obligation obligation: obligationSet.getObligation()){
				addObligation(obligation, pii, piiUniqueId);
			}			
		}
	}
	
	public void deleteObligations(PIIType pii) {
		PiiUniqueId piiUid = piiUniqueIdDao.findByPiiId(pii.getHjid());

		try {
			// Delete OEESTATUS related to piiUid
			List<OEEStatus> oeeStatusList = oeeStatusDao.findByPiiUid(piiUid);
			for(OEEStatus oeeStatus : oeeStatusList){
				if((oeeStatus.getTrigger()) instanceof TriggerAtTime || (oeeStatus.getTrigger()) instanceof TriggerPeriodic){
					timeBasedTriggerHandler.unHandle(oeeStatus);					
				}
				oeeStatusDao.deleteObject(oeeStatus);
			}
		} catch (Exception e) {
			LOGGER.error("Error while deleting obligations", e);
		}
	}
	
	private void addObligation(Obligation obligation, PIIType pii, PiiUniqueId puid){
		for (JAXBElement<? extends Trigger> trigger : obligation.getTriggersSet().getTrigger()){
			OEEStatus oeeStatus = new OEEStatus();
			oeeStatus.setPiiUniqueId(puid);
			oeeStatus.setTriggerName(trigger.getName().toString());
			oeeStatus.setAction(obligation.getActionValue());	
			oeeStatus.setTrigger(trigger.getValue());	

			oeeStatusDao.persistObject(oeeStatus);
			
			if((trigger.getValue()) instanceof TriggerAtTime || (trigger.getValue()) instanceof TriggerPeriodic){
				timeBasedTriggerHandler.handle(oeeStatus);
			}
			
		}
	}
}
