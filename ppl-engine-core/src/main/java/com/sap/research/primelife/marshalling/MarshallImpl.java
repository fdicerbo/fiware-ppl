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
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.exceptions.WritingException;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Marshals {@link Object}s to the given sources and sinks.
 * 
 * 
 * @Version 0.1
 * @Date May 20, 2010
 * 
 */
public class MarshallImpl {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MarshallImpl.class);
	private Marshaller marshaller;

	public MarshallImpl(Package packageInstance) throws JAXBException {
		this(packageInstance, true);
	}

	/**
	 * Constructor with the package parameter. This is used to set the context
	 * of the marshaler.
	 *
	 * @param packageInstance	package instance of the object to marshal
	 * @throws JAXBException
	 */
	public MarshallImpl(Package packageInstance, boolean formattedOutput)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(packageInstance.getName());
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				formattedOutput);
		NamespacePrefixMapper m = new PreferredMapper();
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", m);
	}

	public void marshal(Object jaxbElement, File file) throws WritingException {
		try {
			marshaller.marshal(jaxbElement, file);
		} catch (JAXBException e) {
			LOGGER.error("Exception while marshalling object.");
			WritingException we = new WritingException(
					"Unable to write to the file.", e);
			throw we;
		}
	}

	public void marshal(Object jaxbElement, OutputStream out)
			throws WritingException {
		try {
			marshaller.marshal(jaxbElement, out);
		} catch (JAXBException e) {
			LOGGER.error("Exception while marshalling object.");
			WritingException we = new WritingException(
					"Unable to write to the output stream.", e);
			throw we;
		}
	}

	public void marshal(Object jaxbElement, Writer writer)
			throws WritingException {
		try {
			marshaller.marshal(jaxbElement, writer);
		} catch (JAXBException e) {
			LOGGER.error("Exception while marshalling object.");
			WritingException we = new WritingException(
					"Unable to write to the writer.", e);
			throw we;
		}
	}

	public void marshal(Object jaxbElement, XMLStreamWriter xmlStreamWriter)
			throws WritingException {
		try {
			marshaller.marshal(jaxbElement, xmlStreamWriter);
		} catch (JAXBException e) {
			LOGGER.error("Exception while marshalling object.");
			WritingException we = new WritingException(
					"Unable to write to the XML stream writer.", e);
			throw we;
		}
	}

	/*
	 * NamespacePrefix personalization
	 */

	public static class PreferredMapper extends NamespacePrefixMapper {

		@Override
		public String getPreferredPrefix(String namespaceUri,
				String suggestion, boolean requirePrefix) {
			if ("http://www.primelife.eu/ppl".equals(namespaceUri))
				return "ppl";

			if ("http://www.primelife.eu/ppl/obligation".equals(namespaceUri))
				return "ob";

			if ("http://www.primelife.eu/ppl/credential".equals(namespaceUri))
				return "cr";

			if ("urn:oasis:names:tc:xacml:2.0:policy:schema:os".equals(
					namespaceUri)) {
				return "xacml";
			}

			if ("http://www.w3.org/2001/XMLSchema-instance".equals(
					namespaceUri)) {
				return "xsi";
			}

			if ("http://www.primelife.eu/ppl/stickypolicy".equals(namespaceUri))
				return "sp";

			if ("http://www.primelife.eu/ppl/obligation/mismatch".equals(
					namespaceUri)) {
				return "obmm";
			}

			if ("http://www.primelife.eu/ppl/authorization/mismatch".equals(
					namespaceUri)) {
				return "aumm";
			}

			if ("http://www.primelife.eu/ppl/pii".equals(namespaceUri))
				return "pii";

			if ("http://www.primelife.eu/ppl/claims".equals(namespaceUri))
				return "cl";

			if ("urn:oasis:names:tc:SAML:2.0:assertion".equals(namespaceUri))
				return "samla";

			if ("http://www.w3.org/2001/XMLSchema".equals(namespaceUri))
				return "xsd";

			return suggestion;
		}

	}

}
