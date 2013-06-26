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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.exceptions.SyntaxException;

/**
 * Unmarshals {@link Object}s from the given sources and sinks.
 * 
 * @Version : 0.1
 * @Date : Apr 15, 2010
 * 
 */
public class UnmarshallImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UnmarshallImpl.class);
	private JAXBContext context;
	private Unmarshaller unmarshaller;

	/**
	 * Constructor with package name parameter.
	 * This can set the context of the unmarshalling
	 * @param packageInstance
	 * 			package instance of the object to unmarshall
	 * @throws JAXBException 
	 */
	public UnmarshallImpl(Package packageInstance) throws JAXBException {
		this.context = JAXBContext.newInstance(packageInstance.getName());
		unmarshaller = context.createUnmarshaller();
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(File file) throws SyntaxException {
		try {
			Object object = unmarshaller.unmarshal(file);
			return ((JAXBElement<Object>) object).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the file.");
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the file.");
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(InputStream inputStream) throws SyntaxException {
		try {
			Object object = unmarshaller.unmarshal(inputStream);
			return ((JAXBElement<Object>) object).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the input stream.");
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the input stream.");
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(Reader reader) throws SyntaxException {
		try {
			Object object = unmarshaller.unmarshal(reader);
			return ((JAXBElement<Object>) object).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the reader");
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the reader");
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(XMLStreamReader xmlStreamReader)
			throws SyntaxException {
		try {
			Object object = unmarshaller.unmarshal(xmlStreamReader);
			return ((JAXBElement<Object>) object).getValue();
		} catch (JAXBException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the xmlStreamReader");
			LOGGER.error(se.getMessage(), e);
			throw se;
		} catch (ClassCastException e) {
			SyntaxException se = new SyntaxException("Unable to unmarshal the xmlStreamReader");
			LOGGER.error(se.getMessage(), e);
			throw se;
		}
	}

}
