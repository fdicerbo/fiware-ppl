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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.sap.research.primelife.restful.client.message.DelegateResponse;
import com.sap.research.primelife.restful.client.message.UiMessage;
import com.sap.research.primelife.restful.client.message.UiMessageItem;

public class JacksonTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		UiMessage<DelegateResponse> uim = new UiMessage<DelegateResponse>();
		List<UiMessageItem> messages = new ArrayList<UiMessageItem>();
		UiMessageItem e = new UiMessageItem("Info", "test");
		messages.add(e);
		uim.setMessages(messages);
		
		HashMap<String, String> files = new HashMap<String, String>();
		files.put("FileName", "FileRealName");
		DelegateResponse content = new DelegateResponse(files);
		uim.setContent(content);
		
		StringWriter sw = new StringWriter();   // serialize
      ObjectMapper mapper = new ObjectMapper(); 
      MappingJsonFactory jsonFactory = new MappingJsonFactory();
      JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
      mapper.writeValue(jsonGenerator, uim);
      sw.close();
      
      
      String json = sw.getBuffer().toString();
      System.out.println(json);
      UiMessage<DelegateResponse> uim2 = deserialize(json, UiMessage.class);
      
      sw = new StringWriter();   // serialize
      mapper = new ObjectMapper(); 
      jsonFactory = new MappingJsonFactory();
      jsonGenerator = jsonFactory.createJsonGenerator(sw);
      mapper.writeValue(jsonGenerator, uim2);
      sw.close();
      
      System.out.println(sw.getBuffer().toString());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, Class<?> T){
		ObjectMapper mapper = new ObjectMapper();  
		try {
			return (T) mapper.readValue(json, T);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}

}
