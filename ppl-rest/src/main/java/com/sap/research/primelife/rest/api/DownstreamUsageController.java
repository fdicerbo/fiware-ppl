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
package com.sap.research.primelife.rest.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.message.request.DelegateRequest;
import com.sap.research.primelife.message.response.DelegateResponse;
import com.sap.research.primelife.message.response.DelegateResponseItem;
import com.sap.research.primelife.rest.file.FileManager;
import com.sap.research.primelife.rest.service.DownstreamUsageService;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.MultiPartMediaTypes;

import eu.primelife.ppl.pii.impl.PIIType;

@Path("/downstreamusage")
public class DownstreamUsageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownstreamUsageController.class);
	private DownstreamUsageService downstreamUsageService;
	private FileManager fileService;
	
	public DownstreamUsageController(){
		this(new DownstreamUsageService(), new FileManager());
	}
	
	protected DownstreamUsageController(DownstreamUsageService downstreamUsageService, FileManager fileService){
		this.downstreamUsageService = downstreamUsageService;
		this.fileService = fileService;
	}

	@Path("/file")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MultiPartMediaTypes.MULTIPART_MIXED)
	public Response requestPiiFile(DelegateRequest delegateRequest){
			LOGGER.info("DownsteamUsage request recieved");
		
			if(delegateRequest == null){
				return Response.noContent().status(400).build();
			}
			
			List<PIIType> piis = downstreamUsageService.requestPiis(delegateRequest);
			
			MultiPart m = new MultiPart();		
			int nbParts = 0;
			for(PIIType pii : piis){
				File f = fileService.get(pii.getAttributeValue());
				if(f != null){
					nbParts++;
					BodyPart filePart = new BodyPart();
					filePart.setMediaType(MediaType.APPLICATION_OCTET_STREAM_TYPE);
					filePart.setEntity(f);
					filePart.getHeaders().putSingle("content-disposition", "attachment;filename=" + pii.getAttributeName());
					m.bodyPart(filePart);
				}
			}

			if(nbParts == 0){
				try {
					m.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return Response.noContent().build();
			}
			
			return Response.ok(m, MultiPartMediaTypes.MULTIPART_MIXED_TYPE).build();
	}
	
	@Path("/pii")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestPii(DelegateRequest delegateRequest){
			LOGGER.info("DownsteamUsage request recieved");
		
			if(delegateRequest == null){
				return Response.noContent().status(400).build();
			}
			
			List<PIIType> piis = downstreamUsageService.requestPiis(delegateRequest);
			DelegateResponse response = new DelegateResponse();
			for(PIIType pii : piis){
				response.getItems().add(new DelegateResponseItem(pii.getAttributeName(), pii.getAttributeValue()));
			}
			
			return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}
}
