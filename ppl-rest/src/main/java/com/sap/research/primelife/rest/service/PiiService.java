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
package com.sap.research.primelife.rest.service;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dc.pep.PEP;
import com.sap.research.primelife.exceptions.ValidationException;
import com.sap.research.primelife.message.request.PiiDeleteRequest;
import com.sap.research.primelife.message.response.PiiDeleteResponse;
import com.sap.research.primelife.rest.file.FileManager;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * 
 * 
 *
 */
public class PiiService {   
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PiiService.class);
	private FileManager fileService;
	private PEP pep;
	
	
	public PiiService(){
		this(new FileManager(), new PEP());
	}
	
	protected PiiService(FileManager fileService, PEP pep){
		this.fileService = fileService;
		this.pep =  pep;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param file
	 * @param sPolicy
	 * @return
	 */
	public PIIType create(String fileName, InputStream file, StickyPolicy sPolicy, String owner){
		String realFileName = fileService.store(fileName, file);
		if(realFileName == null){
			LOGGER.info("Unable to store the file, PII not stored");
			return null;
		}
		PIIType pii = pep.storePii(fileName, realFileName, sPolicy.getAttribute().get(0), owner);
		LOGGER.info("Pii created");

		return pii;
	}
	
	public PIIType create(String attrName, String attrValue, StickyPolicy sPolicy, String owner){
		PIIType pii = pep.storePii(attrName, attrValue, sPolicy.getAttribute().get(0), owner);
		LOGGER.info("Pii created");
		return pii;
	}
	
	/**
	 * @param uniqueId
	 * @param owner
	 * @param fileName
	 * @param file
	 * @param sPolicy
	 * @return
	 */
	public PIIType update(long uniqueId, String owner, String fileName, InputStream file, StickyPolicy sPolicy){
		PIIType pii = getPii(uniqueId, owner);
		if(pii != null){
			update(pii, fileName, file, sPolicy);
		}
		return pii;
	}
	
	public PIIType update(long uniqueId, String owner, String attrName, String attrValue, StickyPolicy sPolicy){
		PIIType pii = getPii(uniqueId, owner);
		if(pii != null){
			update(pii, attrName, attrValue, sPolicy);
		}
		return pii;
	}
	
	/**
	 * 
	 * @param piiId
	 * @param fileName
	 * @param file
	 * @param sPolicy
	 * @return
	 */
	public PIIType update(PIIType pii, String fileName, InputStream file, StickyPolicy sPolicy){
		String realFileName = fileService.store(fileName, file);
		if(realFileName == null){
			LOGGER.info("Unable to store the file, PII not stored");
			return null;
		}
		fileService.delete(pii.getAttributeValue());
		PIIType piiUpdated = pep.updatePii(pii, fileName, realFileName, sPolicy.getAttribute().get(0));
		LOGGER.info("Pii updated");
		return piiUpdated;
	}
	
	public PIIType update(PIIType pii, String attrName, String attrValue, StickyPolicy sPolicy){
		PIIType piiUpdated = pep.updatePii(pii, attrName, attrValue, sPolicy.getAttribute().get(0));
		LOGGER.info("Pii updated");
		return piiUpdated;
	}
	
	/**
	 * 
	 * @param owner 
	 * @param piid
	 * @return
	 */
	public PIIType getPii(long uniqueId, String owner){
		return pep.getPii(uniqueId, owner);
	}
	
	/**
	 * 
	 * @param owner 
	 * @param piid
	 * @return
	 */
	public File getPiiFile(long uniqueId, String owner){
		PIIType pii = getPii(uniqueId, owner);
		if(pii == null){
			return null;
		}
		
		return fileService.get(pii.getAttributeValue());
	}
	
	/**
	 * 
	 * @param piid
	 * @return
	 */
	public PiiDeleteResponse delete(PiiDeleteRequest piiRequest){
		PiiDeleteResponse response = new PiiDeleteResponse();
		try{
			String attrvalue = pep.deletePii(piiRequest.getPiiUniqueId(), piiRequest.getOwner());
			if(attrvalue == null){
				LOGGER.info("Pii not found");
				response.setDeleted(false);
			}else{
				response.setDeleted(true);
				if(fileService.fileExist(attrvalue)){
					if(!fileService.delete(attrvalue)){
						LOGGER.warn("Fail to delete file");
						response.setDeleted(false);
					}
				}
			}
		}catch(ValidationException e){
			e.printStackTrace();
			response.setDeleted(false);
		}
		
		return response;
	}
}  
