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
package com.sap.research.primelife.dc.timebasedtrigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sap.research.primelife.dc.dao.OEEStatusDao;
import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;

public class TimeBasedTriggerHandler implements ITimeBasedTriggerHandler {

	private static ITimeBasedTriggerHandler instance = null;
	private OEEStatusDao oeeStatusDao;
	private ITimeBasedTriggerFactory factory = new TimeBaseTriggerFactory();
	private HashMap<OEEStatus, ITimeBasedTrigger> timeBasedTriggers;

	public static ITimeBasedTriggerHandler getInstance(){
		if(instance == null){
			instance = new TimeBasedTriggerHandler();
		}
		return instance;
	}
	
	protected TimeBasedTriggerHandler(){
		oeeStatusDao = new OEEStatusDao();
		factory = new TimeBaseTriggerFactory();
		timeBasedTriggers = new HashMap<OEEStatus, ITimeBasedTrigger>();
	}
	
	protected TimeBasedTriggerHandler(OEEStatusDao oeeStatusDao, ITimeBasedTriggerFactory factory){
		this.oeeStatusDao = oeeStatusDao;
		this.factory = factory;
		timeBasedTriggers = new HashMap<OEEStatus, ITimeBasedTrigger>();
	}
	
	@Override
	public void handle(OEEStatus oeeStatus) {
		if((oeeStatus.getTrigger()) instanceof TriggerAtTime){
			timeBasedTriggers.put(oeeStatus, factory.makeTimeBasedTriggerAtTime(oeeStatus));
			timeBasedTriggers.get(oeeStatus).start();
		}else if((oeeStatus.getTrigger()) instanceof TriggerPeriodic){
			timeBasedTriggers.put(oeeStatus, factory.makeTimeBasedTriggerPeriodic(oeeStatus));
			timeBasedTriggers.get(oeeStatus).start();
		}
	}

	@Override
	public void unHandle(OEEStatus oeeStatus) {
		for(Entry<OEEStatus, ITimeBasedTrigger> entry : timeBasedTriggers.entrySet()){
			if(entry.getKey().getId() == oeeStatus.getId()){
				entry.getValue().cancel();
				timeBasedTriggers.remove(entry.getKey());
				break;
			}
		}
	}

	@Override
	public void start() {
		// Reprise sur arret
		List<OEEStatus> oeeList = oeeStatusDao.findObjects(OEEStatus.class);
		for(OEEStatus oeeStatus : oeeList){
			handle(oeeStatus);
		}
	}

	@Override
	public void stop() {
		for(Entry<OEEStatus, ITimeBasedTrigger> entry : timeBasedTriggers.entrySet()){
			entry.getValue().cancel();
		}
		timeBasedTriggers.clear();
	}
}
