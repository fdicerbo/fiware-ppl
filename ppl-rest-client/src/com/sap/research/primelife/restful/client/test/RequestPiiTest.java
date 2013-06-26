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
package com.sap.research.primelife.restful.client.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sap.research.primelife.restful.client.service.FileService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.MultiPartConfig;
import com.sun.jersey.multipart.impl.MultiPartReaderClientSide;

public class RequestPiiTest {

	private static final String SERVER_URL = "http://localhost:9998/downstreamusage";

	public void run() throws IOException {
		System.out.println("Running RequestPii test ...");
		
		String delegate = "toto@sap.com";
		String fileName = "Koala.jpg";

		
		sendRequest(fileName, delegate);
	}

	private void sendRequest(String fileName, String delegate) {
		
		String request = "{\"email\":"+ delegate + ", \"fileName\":" + fileName  + "}";
		
		// Send request to Fiware
		Client client = Client.create();
		WebResource webResource = client.resource(SERVER_URL);
		
		MultivaluedMap<String, String> form = new MultivaluedMapImpl();
		form.add("request", request);
		
		
       ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, form);

		if (response.getStatus() != 200) {
			System.out.println("Fi-ware return error status: " + response.getStatus());
		}else{
			System.out.println("Fi-ware return sucess status: " + response.getStatus());
			response.bufferEntity();
			
			MultiPartReaderClientSide mc = new MultiPartReaderClientSide(response.getClient().getProviders(), new MultiPartConfig());
			
			try {
				MultiPart m = mc.readFrom(MultiPart.class, BodyPart.class, new Annotation[0], response.getType(), response.getHeaders(), response.getEntityInputStream());
				List<BodyPart> list = m.getBodyParts();

				for(BodyPart b : list){
					File f = b.getEntityAs(File.class);
					FileService.store(f.getName() + "-" + b.getContentDisposition().getFileName(), new FileInputStream(f));
				}
			
			} catch (WebApplicationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		// send the file to the client
		
	}
}
