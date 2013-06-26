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
package com.sap.research.primelife.message.response;

import java.util.Date;

import eu.primelife.ppl.piih.impl.PiiLogged;

public class HistoryResponseItem {

	private Long id;
	private Date date;
	private Long puid;
	private String piiAttributeName;
	private String piiAttributeValue;
	private String owner;
	private String event;
	
	public HistoryResponseItem(){}

	public HistoryResponseItem(PiiLogged piiLogged){
		date = piiLogged.getDate();
		id = piiLogged.getId();
		puid = piiLogged.getPiiUid();
		piiAttributeName = piiLogged.getPiiAttributeName();
		piiAttributeValue = piiLogged.getPiiAttributeValue();
		owner = piiLogged.getOwner();
		event = piiLogged.getEvent().toString();
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPuid() {
		return puid;
	}
	
	public void setPuid(Long puid) {
		this.puid = puid;
	}
	
	public String getPiiAttributeName() {
		return piiAttributeName;
	}
	
	public void setPiiAttributeName(String piiAttributeName) {
		this.piiAttributeName = piiAttributeName;
	}
	
	public String getPiiAttributeValue() {
		return piiAttributeValue;
	}
	
	public void setPiiAttributeValue(String piiAttributeValue) {
		this.piiAttributeValue = piiAttributeValue;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
}
