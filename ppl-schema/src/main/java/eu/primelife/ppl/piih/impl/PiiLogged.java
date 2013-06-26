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
package eu.primelife.ppl.piih.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents an entry log for the PIIS
 * 
 *
 */
@Entity
@Table(name = "piiH")
public class PiiLogged {

	private Long id;
	private Date date;
	private Long puid;
	private String piiAttributeName;
	private String piiAttributeValue;
	private String owner;
	private EventLogged event;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getPiiUid() {
		return puid;
	}
	
	public void setPiiUid(Long piiUid) {
		this.puid = piiUid;
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
	
	@OneToOne(cascade = {CascadeType.ALL})
	public EventLogged getEvent() {
		return event;
	}
	
	public void setEvent(EventLogged event) {
		this.event = event;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public boolean equals(Object object){
		PiiLogged piiLogged = (PiiLogged) object;
		return this.id.equals(piiLogged.getId()) &&
				this.date.equals(piiLogged.getDate()) &&
				this.event.equals(piiLogged.getEvent()) &&
				this.owner.equals(piiLogged.getOwner()) &&
				this.piiAttributeName.equals(piiLogged.getPiiAttributeName()) &&
				this.piiAttributeValue.equals(piiLogged.getPiiAttributeValue()) &&
				this.puid.equals(piiLogged.getPiiUid());
	}
	
}
