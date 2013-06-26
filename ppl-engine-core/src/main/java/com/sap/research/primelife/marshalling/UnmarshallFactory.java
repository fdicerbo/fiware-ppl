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
package com.sap.research.primelife.marshalling;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import oasis.names.tc.saml._2_0.assertion.AssertionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.primelife.ppl.claims.impl.ClaimsType;
import eu.primelife.ppl.policy.impl.PolicySetType;

/**
 * This class aims at improving performance. The most common MarshallImpl are created in advance,
 * so that they are not created on each accessResource request for example.
 * 
 *
 */
public class UnmarshallFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UnmarshallFactory.class);
	private static Map<String, UnmarshallImpl> unmarshallCache = new HashMap<String, UnmarshallImpl>();
	
	/**
	 * Constructor with the package parameter. This is used to set the context
	 * of the marshaller.
	 *
	 * @param packageInstance	package instance of the object to marshal
	 * @throws JAXBException
	 */
	public static UnmarshallImpl createUnmarshallImpl(Package packageInstance)
	throws JAXBException {
		if (unmarshallCache.containsKey((packageInstance.getName()))) {
			LOGGER.debug("Loading " + packageInstance.getName()+ " unmarshaller from cache");
			return unmarshallCache.get(packageInstance.getName());
		} else {
			return new UnmarshallImpl(packageInstance);
		}		
	}
	
	public static void initialize() throws JAXBException{
		unmarshallCache.put(ClaimsType.class.getPackage().getName(), new UnmarshallImpl(ClaimsType.class.getPackage()));
		unmarshallCache.put(AssertionType.class.getPackage().getName(), new UnmarshallImpl(AssertionType.class.getPackage()));
		unmarshallCache.put(PolicySetType.class.getPackage().getName(), new UnmarshallImpl(PolicySetType.class.getPackage()));
		//the same type
		//unmarshallCache.put(PolicyType.class.getPackage().getName(), new UnmarshallImpl(PolicyType.class.getPackage()));
		//unmarshallCache.put(AuthorizationsSetType.class.getPackage().getName(), new UnmarshallImpl(AuthorizationsSetType.class.getPackage()));
	}

}
