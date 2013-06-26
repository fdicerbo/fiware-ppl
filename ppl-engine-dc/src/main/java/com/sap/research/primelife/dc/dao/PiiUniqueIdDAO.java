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

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;

/**
 * Extends generic DAO implementation with custom {@link PiiUniqueId} management
 * methods.
 *
 * 
 * @version 0.1
 * @date Oct 06, 2010
 *
 */
public class PiiUniqueIdDao extends DaoImpl<PiiUniqueId> {
	
	public static String test = "NO";
	private static Logger log = LoggerFactory.getLogger(PiiUniqueIdDao.class);

	/**
	 * Creates uniqueId value (based on PII Hjid value and timestamp).
	 *
	 * @param object	{@link PiiUniqueId} object to be persisted
	 * @return persisted {@link PiiUniqueId} object
	 */
	@Override
	public synchronized PiiUniqueId persistObject(PiiUniqueId object) {
		String piiHjidStr = object.getPii().getHjid().toString();
		if(object.getUniqueId() == null){
			object.setUniqueId(createUniqueId(piiHjidStr));
		}else{
			if(findByUniqueId(object.getUniqueId()) != null){
				object.setUniqueId(createUniqueId(piiHjidStr));
			}
		}
		return super.persistObject(object);
	}
	
	public Long createUniqueId(PIIType pii) {
		String piiHjidStr = pii.getHjid().toString();
		return createUniqueId(piiHjidStr);
	}

	private Long createUniqueId(String piiHjidStr) {
		Long uniqueId;
		
		do {
			uniqueId = new Long(piiHjidStr
					+ String.valueOf(Calendar.getInstance().getTimeInMillis())
					+ String.valueOf((int) (Math.random() * 1000)));
		} while (findByUniqueId(uniqueId) != null);
		
		return uniqueId;
	}

	/**
	 * Find the PiiUniqueId object.
	 * @param uniqueId
	 * @return the PIIUniqueId or null if it is not found
	 */
	@SuppressWarnings("unchecked")
	public PiiUniqueId findByUniqueId(Long uniqueId) {
		Query query = em.createQuery("SELECT puid FROM "
				+ PiiUniqueId.class.getName()
				+ " puid where puid.uniqueId = :uniqueid");
		
		query.setParameter("uniqueid", uniqueId);

		List<Object> results = query.getResultList();
		if (!results.isEmpty())
			return (PiiUniqueId) results.get(0);

		return null;
	}
	
	/**
	 * Find the PiiUniqueId object.
	 * @param uniqueId
	 * @param owner
	 * @return the PIIUniqueId or null if it is not found
	 */
	@SuppressWarnings("unchecked")
	public PiiUniqueId findByUniqueIdAndOwner(Long uniqueId, String owner) {
		Query query = em.createQuery("SELECT puid FROM "
				+ PiiUniqueId.class.getName() 
				+ " puid where puid.uniqueId = :uniqueid"
				+ " AND puid.pii.owner = :owner");
		
		query.setParameter("uniqueid", uniqueId);
		query.setParameter("owner", owner);
		
		List<Object> results = query.getResultList();
		if (!results.isEmpty())
			return (PiiUniqueId) results.get(0);

		return null;
	}
	
	/**
	 * Find the PiiUniqueId which is associated with a PII.
	 * @param piiId - the PII's Hjid
	 * @return a PiiUniqueId
	 */
	public PiiUniqueId findByPiiId(Long piiId) {
		List<PiiUniqueId> ids = this.findObjects(PiiUniqueId.class);
		for (PiiUniqueId id : ids) {
			if (id.getPii() != null && id.getPii().getHjid().equals(piiId)) {
				return id;
			}
		}
		log.info("PiiUniqueId not found");
		return null;
	}

}
