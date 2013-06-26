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

import java.util.Timer;
import java.util.TimerTask;

import com.sap.research.primelife.dc.entity.OEEStatus;
import com.sap.research.primelife.dc.event.EventHandler;
import com.sap.research.primelife.dc.event.IEventHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.policy.obligation.impl.Action;

public abstract class TimeBasedTrigger implements ITimeBasedTrigger{

	protected OEEStatus oeeStatus;
	protected Action action;
	protected PiiUniqueId puid;
	protected PIIType pii;
	protected IEventHandler eventHandler;
	protected Timer timer;
	protected TimerTask timerTask;
	
	protected TimeBasedTrigger(OEEStatus oees){
		this(oees, new EventHandler());
	}
	
	protected TimeBasedTrigger(OEEStatus oees, IEventHandler eventHandler){
		oeeStatus = oees;
		action = oees.getAction();
		puid = oees.getPiiUniqueId();
		pii = oees.getPiiUniqueId().getPii();
		this.eventHandler = eventHandler;
		timer = new Timer();
	}
	
	public synchronized void cancel() {
		this.timerTask.cancel();
        this.timer.cancel();
	} 
	
	public abstract void start();
}
