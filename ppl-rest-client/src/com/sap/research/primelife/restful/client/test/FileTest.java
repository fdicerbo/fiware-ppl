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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileTest {

	String fileSource = "c://uploaded-restclient//test.txt";
	
	public void run(){
		String target = "c://uploaded-restclient//target.txt";
		create(target);
		read(target);
		delete(target);
	}
	
	public void create(String fileTaget){
		try {
			InputStream is = new FileInputStream(fileSource);
			
			try {
				OutputStream out; //= new FileOutputStream(new File(fileTaget));
				int read = 0;
				byte[] bytes = new byte[1024];
	 
				out = new FileOutputStream(new File(fileTaget));
				while ((read = is.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void read(String fileTaget){
		try {
			InputStream is = new FileInputStream(fileTaget);
			BufferedReader myInput = new BufferedReader(new InputStreamReader(is));
		    String thisLine;
			while ((thisLine = myInput.readLine()) != null) {  
			     System.out.println(thisLine);
			}
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String fileTaget){
		File f = new File(fileTaget);
		System.out.println("File exist: " + f.exists());
		System.out.println("Can Read: " + f.canRead());
		System.out.println("Can Write: " + f.canWrite());
		System.out.println("Delete: " + f.delete());

	}
}
