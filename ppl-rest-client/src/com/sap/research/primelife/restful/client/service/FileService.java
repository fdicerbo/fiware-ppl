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
package com.sap.research.primelife.restful.client.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * 
 * 
 *
 */
public class FileService {

	private static final String FILE_DIR = "c://uploaded-restclient/";
	
	public static ArrayList<File> getFilesInFolder(String path) {
		File folder = new File(FILE_DIR + path);
		ArrayList<File> files = new ArrayList<File>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	            files.add(fileEntry);
	        } 
	    }
	    
	    return files;
	}
	
	public static File get(String fileName){
		return new File(FILE_DIR + fileName);
	}
	
	/**
	 * 
	 * @param pii
	 * @param file
	 * @return
	 */
	public static boolean store(String fileName, InputStream file){
		if(fileExist(fileName)){
			System.out.println("WARNING: File already exists");
			return false;
		}
		
		return writeToFile(file, FILE_DIR + fileName);
	}
	
	/**
	 * 
	 * @param pii
	 * @return
	 */
	public static boolean delete(String fileName){
		File f = new File(FILE_DIR + fileName);
		if(f.exists() && f.canWrite()){
			f.delete();
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param pii
	 * @return
	 */
	public static boolean deleteFolder(String folderName){
		File folder = new File(FILE_DIR + folderName);
		
		if(folder.isDirectory()){
			System.out.println(folder.getAbsolutePath() + " is not a folder");
			return false;
		}
		
		if(folder.exists() && folder.canWrite()){
			File[] files = folder.listFiles();
			for(File f : files){
				if(!f.delete()){
					System.out.println("Unable to delete file: " + f.getAbsolutePath());
				}
			}
			
			if(!folder.delete()){
				System.out.println("Unable to delete folder: " + folder.getAbsolutePath());
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean fileExist(String fileName){
			File f = new File(FILE_DIR + fileName);
			return f.exists();
	}
	
	public static boolean createFolder(String path){
		return (new File(FILE_DIR + path)).mkdirs();
	}
	
	public static boolean folderExists(String path){
		File f = new File(FILE_DIR + path);
		return f.isDirectory();
	}
	
	/**
	 * 
	 * @param uploadedInputStream
	 * @param uploadedFileLocation
	 * @return
	 */
	private static boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
			 
			try {
				OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
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
