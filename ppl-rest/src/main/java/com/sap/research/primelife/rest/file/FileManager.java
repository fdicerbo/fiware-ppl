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
package com.sap.research.primelife.rest.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.rest.MyServletContainer;

/**
 * 
 * 
 *
 */
public class FileManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);
	private static String FILE_DIR;
	
	public FileManager(){
		this(MyServletContainer.getServletRealPath() + File.separator + "files" + File.separator);
	}
	
	protected FileManager(String dirPath){
		FILE_DIR = dirPath;
		File f = new File(FILE_DIR);
		if(!f.isDirectory()){
			f.mkdir();
			f.setReadable(true);
			f.setReadable(true);
		}
	}
	
	/**
	 * 
	 * @param pii
	 * @return
	 */
	public File get(String fileName){
		File f = new File(FILE_DIR + fileName);
		if(f.exists()) return f;
		return null;
	}
	
	/**
	 * 
	 * @param pii
	 * @param file
	 * @return
	 */
	public String store(String fileName, InputStream file){
		if(fileExist(fileName)){
			LOGGER.warn("File " + fileName +" already exists");
			return null;
		}
		String generatedFileName = generateFileName(fileName);
		if(writeToFile(file, FILE_DIR + generatedFileName)){
			return generatedFileName;
		}
		return null;
	}
	
	/**
	 * 
	 * @param pii
	 * @return
	 */
	public boolean delete(String fileName){
		File f = new File(FILE_DIR + fileName);
		if(f.exists() && f.canWrite()){
			f.delete();
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean fileExist(String fileName){
		File f = new File(FILE_DIR + fileName);
		 return f.exists();
	}
	
	/**
	 * 
	 * @param pii
	 * @return
	 */
	private String generateFileName(String fileName){
		return UUID.randomUUID().toString() + "_" + fileName;
	}
	
	/**
	 * 
	 * @param uploadedInputStream
	 * @param uploadedFileLocation
	 * @return
	 */
	private boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
			 
			try {
				OutputStream out;
				int read = 0;
				byte[] bytes = new byte[1024];
	 
				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
	 
				e.printStackTrace();
				return false;
			}
		
	}
}
