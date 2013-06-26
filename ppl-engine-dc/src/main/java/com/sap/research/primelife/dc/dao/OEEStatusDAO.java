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

import java.util.List;

import javax.persistence.Query;
import com.sap.research.primelife.dc.dao.DaoImpl;
import com.sap.research.primelife.dc.entity.OEEStatus;

import eu.primelife.ppl.pii.impl.PiiUniqueId;

/**
 * Extends generic DAO with {@link OEEStatus} entity related methods.
 *
 *
 */

public class OEEStatusDao extends DaoImpl<OEEStatus> {
	
	
	/**
	 * Returns OEEStatus associated with given {@link PiiUniqueId} and triggerName.
	 *
	 * @param piiUniqueId	the {@link PiiUniqueId} for which {@link OEEStatus} should be found
	 * @param triggerName	
	 * 
	 * @return	{@link OEEStatus} associated with the give {@link PiiUniqueId} and trigggerName.
	 */
	
	public OEEStatus consumeEvent(PiiUniqueId piiUniqueId,String triggerName) {
		
		Query query = em.createQuery("SELECT oeeStatus FROM "
					+ OEEStatus.class.getName()
					+ " oeeStatus WHERE oeeStatus.piiUniqueId = :piiUniqueId and oeeStatus.triggerName = :triggerName");
		
			query.setParameter("piiUniqueId", piiUniqueId);
			query.setParameter("triggerName", triggerName);
		
		if(query.getResultList().isEmpty()) return null;
		return (OEEStatus) query.getResultList().get(0);	
	}
	
	@SuppressWarnings("unchecked")
	public List<OEEStatus> findByPiiUid(PiiUniqueId piiUniqueId) {
		
		Query query = em.createQuery("SELECT oeeStatus FROM "
					+ OEEStatus.class.getName()
					+ " oeeStatus WHERE oeeStatus.piiUniqueId = :piiUniqueId");
		
			query.setParameter("piiUniqueId", piiUniqueId);
		
		return (List<OEEStatus>)query.getResultList();	
	}

				
}
