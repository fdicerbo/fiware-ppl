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
package com.sap.research.primelife.initializer;

import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.DaoInitializer;

/**
 * The initializer of the PPL Enginge.
 * It uses the Singleton Design Pattern to have only one instance of all the initializer.
 * 
 * @Version 0.1
 * @Date July 6, 2010
 * 
 */
public class Initializer {

	private static final Logger LOGGER =
		LoggerFactory.getLogger(Initializer.class);
	private static final Initializer INSTANCE = new Initializer();

	/**
	 * Protected constructor prevents instantiation from other classes
	 */
	protected Initializer() {
	}

	/**
	 * Call this method to start the initializer for the default persistence unit ("primelifePU")
	 * @return instance of the class
	 */
	public static Initializer getInstance() {
		LOGGER.info("Starting PPL Engine initilization...");
		herasInit();
		DaoInitializer.getInstance();

		return INSTANCE;
	}

	/**
	 * Call this method to start the initializer
	 * @return instance of the class
	 */
	public static Initializer getInstance(String persistenceUnitName) {
		LOGGER.info("Starting PPL Engine initilization...");
		herasInit();
		DaoInitializer.getInstance(persistenceUnitName);

		return INSTANCE;
	}

	/**
	 * init Heras simplePDP for xacml enforcement 
	 */
	protected static void herasInit(){
		LOGGER.info("Heras XACML component initilization...");
		SimplePDPFactory.useDefaultInitializers();
		SimplePDPFactory.getSimplePDP();
	}

}
