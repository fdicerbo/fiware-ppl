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
package com.sap.research.primelife.dc.logger;

import java.util.List;

import com.sap.research.primelife.dc.dao.PiiLoggedDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.piih.impl.EventLogged;
import eu.primelife.ppl.piih.impl.EventLoggedAccess;
import eu.primelife.ppl.piih.impl.EventLoggedCreate;
import eu.primelife.ppl.piih.impl.EventLoggedDelete;
import eu.primelife.ppl.piih.impl.EventLoggedDownstream;
import eu.primelife.ppl.piih.impl.EventLoggedUpdate;
import eu.primelife.ppl.piih.impl.PiiLogged;

public class LoggerHandler implements ILoggerHandler{
	
	private PiiUniqueIdDao puidDao;
	private PiiLoggedDao piiHistDao;

	public LoggerHandler(){
		puidDao = new PiiUniqueIdDao();
		piiHistDao = new PiiLoggedDao();
	}
	
	public LoggerHandler(PiiUniqueIdDao piiUniqueIdDao, PiiLoggedDao piiHistorizedDao){
		puidDao = piiUniqueIdDao;
		piiHistDao = piiHistorizedDao;
	}
	
	@Override
	public void logCreate(PIIType pii){
		PiiLogged piih = createFromPii(pii);
		EventLogged event = new EventLoggedCreate();
		piih.setEvent(event);
		piiHistDao.persistObject(piih);
		piiHistDao.refresh(piih);
	}

	@Override
	public void logUpdate(PIIType pii) {
		PiiLogged piih = createFromPii(pii);
		EventLogged event = new EventLoggedUpdate();
		piih.setEvent(event);
		piiHistDao.persistObject(piih);
		piiHistDao.refresh(piih);
	}

	@Override
	public void logDelete(PIIType pii) {
		PiiLogged piih = createFromPii(pii);
		piih.setPiiAttributeName(null);
		piih.setPiiAttributeValue(null);
		EventLogged event = new EventLoggedDelete();
		piih.setEvent(event);
		piiHistDao.persistObject(piih);
		piiHistDao.refresh(piih);
	}

	@Override
	public void logDownstreamUsage(PIIType pii, String subject) {
		PiiLogged piih = createFromPii(pii);
		EventLoggedDownstream event = new EventLoggedDownstream();
		event.setSubject(subject);
		piih.setEvent(event);
		piiHistDao.persistObject(piih);
		piiHistDao.refresh(piih);
	}

	@Override
	public void logAccess(PIIType pii, String action, String purpose) {
		PiiLogged piih = createFromPii(pii);
		EventLoggedAccess event = new EventLoggedAccess();
		event.setActionPerformed(action);
		event.setPurpose(purpose);
		piih.setEvent(event);
		piiHistDao.persistObject(piih);
		piiHistDao.refresh(piih);
	}

	@Override
	public List<PiiLogged> getHistoryByPii(PIIType pii){
		return piiHistDao.findEventsByUniqueId(puidDao.findByPiiId(pii.getHjid()).getUniqueId());
	}
	
	@Override
	public List<PiiLogged> getHistoryByPii(Long piiUniqueId){
		return piiHistDao.findEventsByUniqueId(piiUniqueId);
	}

	@Override
	public List<PiiLogged> getHistoryByOwner(String owner) {
		return piiHistDao.findEventsByOwner(owner);
	}
	
	private PiiLogged createFromPii(PIIType pii){
		PiiLogged piih = new PiiLogged();
		piih.setPiiAttributeName(pii.getAttributeName());
		piih.setPiiAttributeValue(pii.getAttributeValue());
		piih.setPiiUid(puidDao.findByPiiId(pii.getHjid()).getUniqueId());
		piih.setOwner(pii.getOwner());
		return piih;
	}
}
