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

import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.ActionAnonymizePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionLog;
import eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject;
import eu.primelife.ppl.policy.obligation.impl.ActionSecureLog;

public class ActionHandler implements IActionHandler {
	
	private IActionHandlerService actionHandlerService;
	
	public ActionHandler(){
		actionHandlerService = new ActionHandlerService();
	}
	
	public ActionHandler(IActionHandlerService actionHandlerService){
		this.actionHandlerService = actionHandlerService;
	}

	public void perform(OEEStatus oeeStatus) {
		perform(oeeStatus, null, null);
	}
	
	public void perform(OEEStatus oeeStatus, String message) {
		perform(oeeStatus, message, null);
	}
	
	public void perform(OEEStatus oeeStatus, String message, String sharedWith) {
		
		Action action = oeeStatus.getAction();
		
		// remove this and add a do method in Action, 
		// and override it in every Action implementation
		// then just call action.do()
		if(action instanceof ActionDeletePersonalData){
			actionHandlerService.delete(oeeStatus.getPiiUniqueId().getUniqueId());
		}else if(action instanceof ActionAnonymizePersonalData){
			actionHandlerService.anonymize(oeeStatus.getPiiUniqueId().getUniqueId());
		}else if(action instanceof ActionNotifyDataSubject){
			actionHandlerService.notify(oeeStatus.getPiiUniqueId().getUniqueId(), ((ActionNotifyDataSubject)action).getMedia(), ((ActionNotifyDataSubject)action).getAddress(), message);
		}else if(action instanceof ActionLog){
			actionHandlerService.log(oeeStatus.getPiiUniqueId().getUniqueId(),message,sharedWith);	
		}else if(action instanceof ActionSecureLog){
			actionHandlerService.secureLog(oeeStatus.getPiiUniqueId().getUniqueId());
		}
	}

}
