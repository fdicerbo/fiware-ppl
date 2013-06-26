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

import org.hibernate.ejb.Ejb3Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The initializer of the DAO (persistence manager) for DC side.
 * 
 * 
 */
public class DaoInitializer extends com.sap.research.primelife.dao.DaoInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoInitializer.class);
	protected static final String PERSISTANCE_UNIT_NAME = "primelifePU-dc";

	/**
	 *  Protected constructor prevents instantiation from other classes
	 *  but not for the classes that want to inherit from this.
	 */
	protected DaoInitializer() {
	}

	/**
	 *  Private constructor prevents instantiation from other classes.
	 */
	private DaoInitializer(String persistenceUnitName) {
		super();
		emf = configure(persistenceUnitName).buildEntityManagerFactory();
		em = emf.createEntityManager();
	}

	private static void initialize(String persistenceUnitName) {
		LOGGER.info("Persistence component initilization...");
		LOGGER.info("Persistence unit name: " + persistenceUnitName);
		INSTANCE = new DaoInitializer(persistenceUnitName);
	}

	/**
	 * Call this method to start the initializer
	 * @return instance of the class
	 */
	public static com.sap.research.primelife.dao.DaoInitializer getInstance() {
		return getInstance(PERSISTANCE_UNIT_NAME);
	}

	/**
	 * Call this method to start the initializer by specifying
	 * persistence unit name.
	 * @return instance of the class
	 */
	public static com.sap.research.primelife.dao.DaoInitializer getInstance(String persistenceUnitName) {
		if (INSTANCE == null) {
			initialize(persistenceUnitName);
		}

		return INSTANCE;
	}

	@Override
	protected Ejb3Configuration configure(String persistenceUnitName){
		Ejb3Configuration ejb = super.configure(persistenceUnitName);
		ejb 
				.addAnnotatedClass(com.sap.research.primelife.dc.dao.DCResource.class)
				.addAnnotatedClass(com.sap.research.primelife.dc.entity.EventLog.class)
				.addAnnotatedClass(com.sap.research.primelife.dc.entity.OEEStatus.class)
				.addAnnotatedClass(eu.primelife.ppl.pii.impl.PiiUniqueId.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLogged.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLoggedUpdate.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLoggedCreate.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLoggedDownstream.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLoggedDelete.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.EventLoggedAccess.class)
				.addAnnotatedClass(eu.primelife.ppl.piih.impl.PiiLogged.class);
		
		return ejb;
	}

}
