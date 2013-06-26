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
package com.sap.research.primelife.dao;

import java.util.List;

import javax.persistence.Query;

import com.sap.research.primelife.exceptions.ValidationException;

import eu.primelife.ppl.pii.impl.PIIType;

/**
 * Helper class to query PII, manipulate and delete PII.
 * 
 * @Version 0.1
 * @Date May 5, 2010
 * 
 */
public class PiiDao extends DaoImpl<PIIType> {

	public void addPolicy(PIIType pii, Object policy){
		pii = em.find(PIIType.class, pii.getHjid());
		pii.getPolicySetOrPolicy().add(policy);
		pii = (PIIType) updateObject(pii);
	}

	/**
	 * Get the first PII with that attribute name.
	 * @param attributeName
	 * @return a PII or null if it was not found
	 */
	public PIIType findObject(String attributeName) {
		Query query;

		// we verify the validity of the parameter
		if (attributeName.equals(null))
			throw new ValidationException("Attribute name is null");

		// load all the object from the Data base depending of his attribute name
		// FIXME, add the eager fetch for this query in order to load all the Policy and PolicySet and sub-element
		query = em.createQuery("SELECT pii FROM " + PIIType.class.getName()
				+ " pii where pii.attributeName = :name");
		query.setParameter("name", attributeName);
		if (!query.getResultList().isEmpty()) 
			return (PIIType) query.getResultList().get(0);

		return null;
	}

	/**
	 * Find all PII with that attribute name.
	 * @param attributeName
	 * @return a list of PII
	 */
	@SuppressWarnings("unchecked")
	public List<PIIType> findAllByAttributeName(String attributeName) {
		Query query;

		// we verify the validity of the parameter
		if (attributeName.equals(null))
			throw new ValidationException("Attribute name is null");

		// load all the object from the data base depending of his attribute name
		query = em.createQuery("SELECT pii FROM " + PIIType.class.getName()
				+ " pii where pii.attributeName = :name");
		query.setParameter("name", attributeName);

		return query.getResultList();
	}
	
	
	/**
     * Find PII with that attribute value.
     * @param attributeValue
     * @return PII
     */
    public PIIType findAllByAttributeValue(String attributeValue) {
        Query query;

        // we verify the validity of the parameter
        if (attributeValue.equals(null))
            throw new ValidationException("Attribute value is null");

        // load all the object from the data base depending of his attribute name
        query = em.createQuery("SELECT pii FROM " + PIIType.class.getName()
                + " pii where pii.attributeValue = :name");
        query.setParameter("name", attributeValue);

        return (PIIType) query.getResultList().get(0);
    }
    

    /**
     * Find PII with that owner
     * @param owner
     * @return List<PIIType> 
     */
    @SuppressWarnings("unchecked")
	public List<PIIType> findAllByOwner(Long owner) {
        Query query;

        query = em.createQuery("SELECT pii FROM " + PIIType.class.getName()
                + " pii where pii.owner = :owner");
        query.setParameter("owner", owner);

        return (List<PIIType>) query.getResultList();
    }
    
	/**
	 * Return a list of all PII in the database.
	 * @return
	 */
	public List<PIIType> getAllPII() {
		List<PIIType> result = this.findObjects(PIIType.class);
		
		return result;
	}

	/**
	 * Delete all the PII in the database.
	 * If there are some Policies associated with the pii,
	 * they will also be deleted. (Cascade.ALL)
	 * This assumes a 1:n model for PII and policy.
	 */
	public void deleteAllPii(){
		List<PIIType> pii = this.getAllPII();
		for (PIIType p : pii) {
			this.deleteObject(p);
		}
	}

}
