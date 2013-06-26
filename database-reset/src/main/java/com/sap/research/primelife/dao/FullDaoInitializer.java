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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class resets the database by recreating the schema for the selected database names.
 * Therefore it initializes the entity manager with the different persistence units.
 * 
 *
 */
public class FullDaoInitializer extends com.sap.research.primelife.dc.dao.DaoInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FullDaoInitializer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.debug("Start import");
		System.out.println("Clean primelife database - Tool");
		System.out.println("Requires databases 'ppl' for DS, 'ppl-dc' for DC, 'ppl-3p' for DSU and 'ppl-ds-test' and 'ppl-dc-test' for" +
				" testing the DS and DC");
		System.out.println("USAGE: \nNo parameters = reset DS, DC and DSU.");
		System.out.println("Any parameters: reset dc, ds, dsu, ds-test or dc-test database. E.g. dc ds resets database for DC and DS");
		System.out.println("resetting database ...");
		if (args.length == 0) {
			LOGGER.info("resetting Data Controller database");
			resetDC();
			LOGGER.info("resetting Data Subject database");
			resetDS();
			LOGGER.info("resetting downstream usage database");
			resetDSU();
		}
		for (String arg : args){
			if (arg.equals("dc")){
				LOGGER.info("resetting Data Controller database");
				resetDC();
			}
			if (arg.equals("ds")){
				LOGGER.info("resetting Data Subject database");
				resetDS();
			}
			if (arg.equals("dsu")){
				LOGGER.info("resetting downstream usage database");
				resetDSU();
			}
			if (arg.equals("ds-test")){
				LOGGER.info("resetting downstream usage database");
				resetDSTest();
			}
			if (arg.equals("dc-test")){
				LOGGER.info("resetting downstream usage database");
				resetDCTest();
			}
		}
		LOGGER.info("Reset finished");
		System.out.println("database reset completed");
	}

	private FullDaoInitializer(String persistenceUnitName) {
		LOGGER.debug("FullDaoInitializer for " + persistenceUnitName);
		emf = configure(persistenceUnitName).buildEntityManagerFactory();
		em = emf.createEntityManager();
	}

	private static void resetDC() {
		new FullDaoInitializer("primelifePU-dc");
	}
	
	private static void resetDS() {
		DaoInitializer.getInstance();
	}
	
	private static void resetDSU() {
		new FullDaoInitializer("primelifePU-3p");
	}
	private static void resetDSTest() {
		new FullDaoInitializer("primelifePU-ds-test");
	}
	private static void resetDCTest() {
		new FullDaoInitializer("primelifePU-dc-test");
	}

}
