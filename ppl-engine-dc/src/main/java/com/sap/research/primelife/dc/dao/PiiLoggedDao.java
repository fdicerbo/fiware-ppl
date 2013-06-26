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
package com.sap.research.primelife.dc.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import eu.primelife.ppl.piih.impl.PiiLogged;

public class PiiLoggedDao extends DaoImpl<PiiLogged> {

	@SuppressWarnings("unchecked")
	public List<PiiLogged> findEventsByUniqueId(Long uniqueId) {
		Query query = em.createQuery("SELECT piiH FROM "
				+ PiiLogged.class.getName()
				+ " piiH where piiH.puid = :uniqueid"
				+ " ORDER BY piiH.date ASC");
		
		query.setParameter("uniqueid", uniqueId);

		List<PiiLogged> results = query.getResultList();

		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<PiiLogged> findEventsByOwner(String owner) {
		Query query = em.createQuery("SELECT piiH FROM "
				+ PiiLogged.class.getName()
				+ " piiH where piiH.owner = :owner"
				+ " ORDER BY piiH.piiUid, piiH.date ASC");
		
		query.setParameter("owner", owner);

		List<PiiLogged> results = query.getResultList();
		
		if(results == null){
			results = new ArrayList<PiiLogged>();
		}
		
		return results;
	}

	public void refresh(PiiLogged piih) {
		em.refresh(piih);
	}
}
