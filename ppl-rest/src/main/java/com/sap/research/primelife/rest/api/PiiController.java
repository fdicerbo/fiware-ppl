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
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;
import com.sap.research.primelife.message.request.PiiDeleteRequest;
import com.sap.research.primelife.message.response.PiiCreateResponse;
import com.sap.research.primelife.message.response.PiiDeleteResponse;
import com.sap.research.primelife.message.response.PiiUpdateResponse;
import com.sap.research.primelife.rest.service.PiiService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * Pii Controller
 * 
 */
@Path("/pii")
public class PiiController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PiiController.class);
	private PiiUniqueIdDao uidDao;
	private PiiService piiService;
	
	/**
	 * Default constructor
	 */
	public PiiController(){
		this(new PiiService(), new PiiUniqueIdDao());
	}
	
	/**
	 * Constructor allowing dependency injections
	 * Only for test purpose
	 * @param piiService
	 * @param uidDao
	 */
	protected PiiController(PiiService piiService, PiiUniqueIdDao uidDao){
		this.piiService = piiService;
		this.uidDao = uidDao;
	}
	
	@GET
	@Produces("multipart/mixed")
	public Response getPii(@QueryParam("uniqueId") Long uniqueId, @QueryParam("owner") String owner) {
		LOGGER.info("GET /pii recieved");
		
		if(uniqueId == null || owner == null){
			LOGGER.info("Wrong parameter");
			return Response.status(400).build();
		}

		File f = piiService.getPiiFile(uniqueId, owner);
		
		if(f == null){
			LOGGER.info("Pii not found");
			return Response.noContent().status(204).build();
		}
		
		LOGGER.info("Pii found");
		
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).header("content-disposition", "attachment; filename = " + f.getName()).build();
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPiiFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, 
			@FormDataParam("stickyPolicy") StickyPolicy sPolicy,
			@FormDataParam("owner") String owner) {
		
		LOGGER.info("PUT /pii recieved");
		
		if(uploadedInputStream == null ||
			fileDetail == null ||
			sPolicy == null ||
			owner == null){
			return Response.noContent().status(400).build();
		}
	
		PIIType pii = piiService.create(fileDetail.getFileName(), uploadedInputStream, sPolicy, owner);
		
		if(pii == null){
			LOGGER.info("Error creating new pii");
			return Response.status(500).build();
		}
		
		Long uid = uidDao.findByPiiId(pii.getHjid()).getUniqueId();
		LOGGER.info("Pii successfully created, uniqueId:"+ uid);
		
		PiiCreateResponse piiCreateResponse = new PiiCreateResponse(); 
		piiCreateResponse.setUniqueId(uid);
		
		return Response.status(201).type(MediaType.APPLICATION_JSON).entity(piiCreateResponse).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPii(
			@FormParam("name") String attrName,
			@FormParam("value") String attrValue, 
			@FormParam("stickyPolicy") StickyPolicy sPolicy,
			@FormParam("owner") String owner) {
		
		LOGGER.info("PUT /pii recieved");
		
		if(attrName == null ||
			attrValue == null ||
			sPolicy == null ||
			owner == null){
			return Response.noContent().status(400).build();
		}
	
		PIIType pii = piiService.create(attrName, attrValue, sPolicy, owner);
		
		if(pii == null){
			LOGGER.info("Error creating new pii");
			return Response.status(500).build();
		}
		
		Long uid = uidDao.findByPiiId(pii.getHjid()).getUniqueId();
		LOGGER.info("Pii successfully created, uniqueId:"+ uid);
		
		PiiCreateResponse piiCreateResponse = new PiiCreateResponse(); 
		piiCreateResponse.setUniqueId(uid);
		
		return Response.status(201).type(MediaType.APPLICATION_JSON).entity(piiCreateResponse).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response updateFile(
			@FormDataParam("uniqueId") String uniqueIdStr, 
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, 
			@FormDataParam("stickyPolicy") StickyPolicy sPolicy,
			@FormDataParam("owner") String owner) {
		
		LOGGER.info("POST /pii recieved");
		
		if (uploadedInputStream == null || fileDetail == null
				|| sPolicy == null || owner == null || uniqueIdStr == null) {
			return Response.noContent().status(400).build();
		}
		
		Long uniqueId = Long.parseLong(uniqueIdStr);
		
		PIIType pii = piiService.update(uniqueId, owner, fileDetail.getFileName(), uploadedInputStream, sPolicy);

		PiiUpdateResponse piiUpdateResponse = new PiiUpdateResponse(); 
		piiUpdateResponse.setUniqueId(uniqueId);
		
		if(pii == null){
			piiUpdateResponse.setUpdated(false);
			return Response.ok(piiUpdateResponse).build();
		}
		
		LOGGER.info("Pii successfully updated, uniqueId:"+ uniqueId);
		piiUpdateResponse.setUpdated(true);
		return Response.ok(piiUpdateResponse).build();
	 }
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response updatePii(
			@FormParam("uniqueId") Long uniqueId, 
			@FormParam("name") String attrName,
			@FormParam("value") String attrValue,  
			@FormParam("stickyPolicy") StickyPolicy sPolicy,
			@FormParam("owner") String owner) {
		
		LOGGER.info("POST /pii recieved");
		
		if (attrName == null || attrValue == null || sPolicy == null || owner == null || uniqueId == null) {
			return Response.noContent().status(400).build();
		}
		
		PIIType pii = piiService.update(uniqueId, owner, attrName, attrValue, sPolicy);

		PiiUpdateResponse piiUpdateResponse = new PiiUpdateResponse(); 
		piiUpdateResponse.setUniqueId(uniqueId);
		
		if(pii == null){
			piiUpdateResponse.setUpdated(false);
			return Response.ok(piiUpdateResponse).build();
		}
		
		LOGGER.info("Pii successfully updated, uniqueId:"+ uniqueId);
		piiUpdateResponse.setUpdated(true);
		return Response.ok(piiUpdateResponse).build();
	 }

	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(PiiDeleteRequest piiRequest) {
		
		LOGGER.info("DELETE /pii recieved");
		
		if(piiRequest == null){
			return Response.noContent().status(400).build();
		}
		
		PiiDeleteResponse response = piiService.delete(piiRequest);
		
		return Response.ok(response, MediaType.APPLICATION_JSON).build();
	}
}
