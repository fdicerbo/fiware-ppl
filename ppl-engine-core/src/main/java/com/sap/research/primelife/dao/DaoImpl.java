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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.exceptions.ValidationException;


/**
 * Implementation of the {@link IDao} interface that makes the CRUD(Create, Read, Update, Delete) operations of any object that is defined in the persistence context.
 * This class depends of the persistence context, in other word, the persistence file which contains 
 * the persistence classes and the Data base configurations.
 * 
 * 
 * @Version 0.1
 * @Date May 3, 2010
 * 
 */
public class DaoImpl <T> implements IDao <T> {

	//@PersistenceContext(unitName = "primelifePU")
	protected EntityManager em;
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoImpl.class);

	public DaoImpl(){
		em = DaoInitializer.getInstance().getEntityManager();
	}

	/* (non-Javadoc)
	 * @see com.sap.research.primelife.common.dao.IDao#deleteObject(java.lang.Object)
	 */
	@Override
	public synchronized void deleteObject(T object) {
		// we verify the validity of the parameter
		if (object == null) {
			LOGGER.error("Trying to delete null object.");
			throw new ValidationException("Cannot delete null object");
		}

		// deleting the object from the db
		try {
			em.getTransaction().begin();
			em.remove(em.merge(object));
			em.getTransaction().commit();
			
			LOGGER.debug("Entity of type " + object.getClass().getName() + " deleted successfully.");
		}
		finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	/* (non-Javadoc)
	 * @see com.sap.research.primelife.common.dao.IDao#findObject(java.lang.Long)
	 */
	@Override
	public T findObject(Class<T> className, Long objectId) {
		// we verify the validity of the parameter
		if (objectId == null) {
			LOGGER.error("Trying to find null object.");
			throw new ValidationException("Invalid id");
		}

		// find object from his Id
		T object = em.find(className, objectId);
		LOGGER.debug("Entity of type " + className.getName() + " and id " + objectId + " found successfully.");

		return object;
	}

	/* (non-Javadoc)
	 * @see com.sap.research.primelife.common.dao.IDao#findObjects()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findObjects(Class<T> className) {
		Query query;
		List<T> objects;

		//em = getEntityManager();

		// load all the objects from the Data base
		query = em.createQuery("SELECT o FROM " + className.getName() + " o ");
		objects = query.getResultList();

		return objects;
	}

	/* (non-Javadoc)
	 * @see com.sap.research.primelife.common.dao.IDao#persistObject(java.lang.Object)
	 */
	@Override
	public synchronized T persistObject(T object) {
		
		// we verify that the parameter is not null
				
		if (object == null) {
			LOGGER.error("Can't persist, the object is null");
			throw new ValidationException("Cannot persist null object");
		}

		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
			LOGGER.debug("Entity of type " + object.getClass().getName() + " persisted successfully.");
		}
		finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}

		return object;
	}

	/* (non-Javadoc)
	 * @see com.sap.research.primelife.common.dao.IDao#updateCategory(java.lang.Object)
	 */
	@Override
	public synchronized T updateObject(final T object) {
		// we verify the validity of the parameter
		if (object == null) {
			LOGGER.error("Trying to update null object.");
			throw new ValidationException("Cannot update null object");
		}

		try {
			// update the object in the db
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
			LOGGER.debug("Entity of type " + object.getClass().getName() + " updated successfully.");
		}
		finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}

		return object;
	}

	/**
	 * @return the em
	 */
	public EntityManager getEm() {
		return em;
	}

}
