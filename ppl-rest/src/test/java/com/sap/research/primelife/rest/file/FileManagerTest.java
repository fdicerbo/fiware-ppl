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

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FileManagerTest {

	private final static String FILE_NAME = "test.jpg";
	private final static String FILE_DIR = FileManagerTest.class.getResource("/fileManager").getPath() + File.separator;
	private FileManager fileManager;
	
	@Before
	public void setUp() throws Exception {
		fileManager = new FileManager(FILE_DIR);
	}

	@After
	public void tearDown() throws Exception {
		File folder = new File(FILE_DIR);
		for(File f : folder.listFiles()){
			if(!f.getName().equals(FILE_NAME)){
				f.delete();
			}
		}
	}

	@Test
	public void testGet() {
		File f = fileManager.get(FILE_NAME);
		assertTrue(f.exists());
		f = fileManager.get("toto.jpg");
		assertNull(f);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testStore() {
		String txt = " balalalalaalblibluiblibbloblbo";
		InputStream file = new ByteArrayInputStream(txt.getBytes());
		
		String fileName = fileManager.store("toStore", file);
		
		File f = new File(FILE_DIR + fileName);
		assertTrue(f.exists());
		
		FileInputStream fis = null;
		BufferedInputStream bis;
		DataInputStream dis;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			fail("unable to open file");
		}
		
		bis = new BufferedInputStream(fis);
		dis = new DataInputStream(bis);
		String content = "";
		try {
			while (dis.available() != 0) {
				String tempo = dis.readLine();
				content += tempo;
			}
			fis.close();
			bis.close();
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(txt, content);
	}
	
	@Test
	public void testDelete() {
		File f = new File(FILE_DIR + "toDelete");
		try {
			FileWriter fw = new FileWriter(f);
			fw.append("t");
			fw.append("o");
			fw.append("t");
			fw.append("o");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		boolean ret = fileManager.delete("doNotExist");
		assertFalse(ret);
		f = new File(FILE_DIR + "doNotExist");
		assertFalse(f.exists());
		
		ret = fileManager.delete("toDelete");
		assertTrue(ret);
		f = new File(FILE_DIR + "toDelete");
		assertFalse(f.exists());
	}

}
