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
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.sap.research.primelife.restful.client.service.PolicyService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * 
 * 
 *
 */
public class DeletePiiTest {
	
private static final String FOLDER = "C://test-file//";
	
	private static final String SERVER_URL = "http://localhost:9998/pii";

	public void run() throws IOException {
		System.out.println("Running Delete Pii test ...");
		String fileName = "Penguins.jpg";
		List<String> delegates = new ArrayList<String>();
		delegates.add("toto@sap.com");
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(fileName);
		String notify = "paul.cervera.y.alvarez@sap.com";
	
		Long uid = createPii(fileName, delegates, fileNames, notify);
		fileName = "Koala.jpg";
		deletePii(uid);
	}
	
	private void deletePii(Long uid){
		try {
			
			Client client = Client.create();
			WebResource webResource = client.resource(SERVER_URL + "?uniqueId=" + uid);
			ClientResponse response;
			
			FormDataMultiPart form = new FormDataMultiPart();
			form.field("uniqueId", uid.toString());
			response = webResource.delete(ClientResponse.class);

			
	
			if (response.getStatus() != 200) {
				System.out.println("Fi-ware return error status: " + response.getStatus());
			}
			
			System.out.println("Fi-ware successfully deleted the pii: " + response.getStatus());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Long createPii(String fileName, List<String> delegates, List<String> fileNames, String notify){
		
		String filePath = FOLDER + fileName;
		File file = new File(filePath);

		StickyPolicy stickyPolicy = PolicyService.buildStickyPolicy(delegates, notify, null, fileNames, true);
		StringWriter stw = new StringWriter();
		PolicyService.marshal(stickyPolicy, stw);
		
		String ret = sendPii(file, stw.toString(), null);
		
		JSONObject jsob;
		try {
			jsob = new JSONObject(ret);
			String uniqueIdStr = (String) jsob.get("uniqueId");
			return Long.parseLong(uniqueIdStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendPii(File file, String stickyPolicy, Long uid){
		try {
			
			Client client = Client.create();
			WebResource webResource = client.resource(SERVER_URL);
			ClientResponse response;
			
			FormDataMultiPart form = new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("file", file));
			form.field("stickyPolicy", stickyPolicy);
			if(uid != null){
				form.field("uniqueId", uid.toString());
				 response = webResource
							.type(MediaType.MULTIPART_FORM_DATA)
							.accept(MediaType.APPLICATION_JSON)
							.post(ClientResponse.class, form);
			}else{
				 response = webResource
							.type(MediaType.MULTIPART_FORM_DATA)
							.accept(MediaType.APPLICATION_JSON)
							.put(ClientResponse.class, form);
			}
	
			if (response.getStatus() != 201) {
				System.out.println("Fi-ware return error status: " + response.getStatus());
			}
			
			String repStr = response.getEntity(String.class);
			
			System.out.println("Fi-ware stored the pii and its policy with success. PiiId= " + repStr);
			return repStr;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
