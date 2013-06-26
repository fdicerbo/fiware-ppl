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
package com.sap.research.primelife.rest.service;

import java.util.ArrayList;

import com.sap.research.primelife.dc.logger.ILoggerHandler;
import com.sap.research.primelife.dc.logger.LoggerHandler;
import com.sap.research.primelife.message.response.HistoryResponse;
import com.sap.research.primelife.message.response.HistoryResponseItem;

import eu.primelife.ppl.piih.impl.PiiLogged;

public class LoggerService {

	private ILoggerHandler loggerHandler;
	
	public LoggerService(){
		this(new LoggerHandler());
	}
	
	protected LoggerService(ILoggerHandler loggerHandler){
		this.loggerHandler = loggerHandler;
	}

	public HistoryResponse getLogsByOwner(String owner){
		HistoryResponse historyResponse = new HistoryResponse();
		historyResponse.setEvents(new ArrayList<HistoryResponseItem>());
		for(PiiLogged piiLogged :  loggerHandler.getHistoryByOwner(owner)){
			historyResponse.getEvents().add(new HistoryResponseItem(piiLogged));
		}
		return historyResponse;
	}
	
	public HistoryResponse getLogsByPii(Long uniqueId){
		HistoryResponse historyResponse = new HistoryResponse();
		historyResponse.setEvents(new ArrayList<HistoryResponseItem>());
		
		for(PiiLogged piiLogged :  loggerHandler.getHistoryByPii(uniqueId)){
			historyResponse.getEvents().add(new HistoryResponseItem(piiLogged));
		}
		return historyResponse;
	}
}
