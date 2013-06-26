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
package com.sap.research.primelife.dc.logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.research.primelife.dc.dao.PiiLoggedDao;
import com.sap.research.primelife.dc.dao.PiiUniqueIdDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PiiUniqueId;
import eu.primelife.ppl.piih.impl.EventLoggedAccess;
import eu.primelife.ppl.piih.impl.EventLoggedCreate;
import eu.primelife.ppl.piih.impl.EventLoggedDelete;
import eu.primelife.ppl.piih.impl.EventLoggedDownstream;
import eu.primelife.ppl.piih.impl.EventLoggedUpdate;
import eu.primelife.ppl.piih.impl.PiiLogged;

public class LoggerHandlerTest {

	private ILoggerHandler loggerHandler;
	@Mock private PiiUniqueIdDao puidDao;
	@Mock private PiiLoggedDao piiHistDao;
	
	private PIIType pii;
	private PiiUniqueId piiUid;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		loggerHandler = new LoggerHandler(puidDao, piiHistDao);
		
		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
		pii.setOwner("0");
		
		piiUid = new PiiUniqueId();
		piiUid.setId((long) 0);
		piiUid.setPii(pii);
		piiUid.setUniqueId((long) 123456789);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLogCreate(){
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		loggerHandler.logCreate(pii);
		
		class checkPiiLogged extends ArgumentMatcher<PiiLogged> {
			public boolean matches(Object obj) {
				PiiLogged piiLogged = (PiiLogged) obj;
				return ( piiLogged.getEvent() instanceof EventLoggedCreate &&
					piiLogged.getOwner().equals("0") &&
					piiLogged.getPiiAttributeName().equals("attrName") &&
					piiLogged.getPiiAttributeValue().equals("attrValue") &&
					piiLogged.getPiiUid().equals((long) 123456789));
			}
		}
		
		verify(piiHistDao).persistObject(argThat(new checkPiiLogged()));
	}
	
	@Test
	public void testLogUpdate(){
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		pii.setAttributeValue("updated");
		loggerHandler.logUpdate(pii);
		
		class checkPiiLogged extends ArgumentMatcher<PiiLogged> {
			public boolean matches(Object obj) {
				PiiLogged piiLogged = (PiiLogged) obj;
				return ( piiLogged.getEvent() instanceof EventLoggedUpdate &&
					piiLogged.getOwner().equals("0") &&
					piiLogged.getPiiAttributeName().equals("attrName") &&
					piiLogged.getPiiAttributeValue().equals("updated") &&
					piiLogged.getPiiUid().equals((long) 123456789));
			}
		}
		
		verify(piiHistDao).persistObject(argThat(new checkPiiLogged()));
	}
	
	@Test
	public void testLogDelete(){
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		loggerHandler.logDelete(pii);
		
		class checkPiiLogged extends ArgumentMatcher<PiiLogged> {
			public boolean matches(Object obj) {
				PiiLogged piiLogged = (PiiLogged) obj;
				return ( piiLogged.getEvent() instanceof EventLoggedDelete &&
					piiLogged.getOwner().equals("0") &&
					piiLogged.getPiiAttributeName() == null &&
					piiLogged.getPiiAttributeValue() == null &&
					piiLogged.getPiiUid().equals((long) 123456789));
			}
		}
		
		verify(piiHistDao).persistObject(argThat(new checkPiiLogged()));
	}
	
	@Test
	public void testLogDownsteamUsage(){
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		loggerHandler.logDownstreamUsage(pii, "toto@sap.com");
		
		class checkPiiLogged extends ArgumentMatcher<PiiLogged> {
			public boolean matches(Object obj) {
				PiiLogged piiLogged = (PiiLogged) obj;
				
				if(!(piiLogged.getEvent() instanceof EventLoggedDownstream)){
					return false;
				}
				
				return ( ((EventLoggedDownstream)piiLogged.getEvent()).getSubject().equals("toto@sap.com") && 
						piiLogged.getOwner().equals("0") &&
					piiLogged.getPiiAttributeName().equals("attrName") &&
					piiLogged.getPiiAttributeValue().equals("attrValue") &&
					piiLogged.getPiiUid().equals((long) 123456789));
			}
		}
		
		verify(piiHistDao).persistObject(argThat(new checkPiiLogged()));
	}
	
	@Test
	public void testLogAccess(){
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		
		loggerHandler.logAccess(pii, "read", "marketing");
		
		class checkPiiLogged extends ArgumentMatcher<PiiLogged> {
			public boolean matches(Object obj) {
				PiiLogged piiLogged = (PiiLogged) obj;
				
				if(!(piiLogged.getEvent() instanceof EventLoggedAccess)){
					return false;
				}
				
				return ( ((EventLoggedAccess)piiLogged.getEvent()).getActionPerformed().equals("read") && 
						((EventLoggedAccess)piiLogged.getEvent()).getPurpose().equals("marketing") && 
						piiLogged.getOwner().equals("0") &&
					piiLogged.getPiiAttributeName().equals("attrName") &&
					piiLogged.getPiiAttributeValue().equals("attrValue") &&
					piiLogged.getPiiUid().equals((long) 123456789));
			}
		}
		
		verify(piiHistDao).persistObject(argThat(new checkPiiLogged()));
	}
	
	@Test
	public void testGetHistoryByPii1(){
		List<PiiLogged> data = new ArrayList<PiiLogged>();
		PiiLogged piiL = new PiiLogged();
		piiL.setId((long) 56);
		data.add(piiL);
		
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(this.piiHistDao.findEventsByUniqueId((long) 123456789)).thenReturn(data);
		
		List<PiiLogged> results = loggerHandler.getHistoryByPii((long) 123456789);
		
		assertEquals(data.size(), results.size());
		for(int i=0; i<data.size(); i++){
			assertEquals(data.get(i).getId(), results.get(i).getId());
			assertEquals(data.get(i).getDate(), results.get(i).getDate());
			assertEquals(data.get(i).getOwner(), results.get(i).getOwner());
			assertEquals(data.get(i).getPiiAttributeName(), results.get(i).getPiiAttributeName());
			assertEquals(data.get(i).getPiiAttributeValue(), results.get(i).getPiiAttributeValue());
			assertEquals(data.get(i).getPiiUid() , results.get(i).getPiiUid());
		}
	}
	
	@Test
	public void testGetHistoryByPii2(){
		List<PiiLogged> data = new ArrayList<PiiLogged>();
		PiiLogged piiL = new PiiLogged();
		piiL.setId((long) 56);
		data.add(piiL);
		
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(this.piiHistDao.findEventsByUniqueId((long) 123456789)).thenReturn(data);
		
		List<PiiLogged> results = loggerHandler.getHistoryByPii(pii);
		
		assertEquals(data.size(), results.size());
		for(int i=0; i<data.size(); i++){
			assertEquals(data.get(i).getId(), results.get(i).getId());
			assertEquals(data.get(i).getDate(), results.get(i).getDate());
			assertEquals(data.get(i).getOwner(), results.get(i).getOwner());
			assertEquals(data.get(i).getPiiAttributeName(), results.get(i).getPiiAttributeName());
			assertEquals(data.get(i).getPiiAttributeValue(), results.get(i).getPiiAttributeValue());
			assertEquals(data.get(i).getPiiUid() , results.get(i).getPiiUid());
		}
	}
	
	@Test
	public void testGetHistoryByOwner(){
		List<PiiLogged> data = new ArrayList<PiiLogged>();
		PiiLogged piiL = new PiiLogged();
		piiL.setId((long) 56);
		data.add(piiL);
		
		when(puidDao.findByPiiId((long) 0)).thenReturn(piiUid);
		when(this.piiHistDao.findEventsByOwner(""+0)).thenReturn(data);
		
		List<PiiLogged> results = loggerHandler.getHistoryByOwner(""+0);
		
		assertEquals(data.size(), results.size());
		for(int i=0; i<data.size(); i++){
			assertEquals(data.get(i).getId(), results.get(i).getId());
			assertEquals(data.get(i).getDate(), results.get(i).getDate());
			assertEquals(data.get(i).getOwner(), results.get(i).getOwner());
			assertEquals(data.get(i).getPiiAttributeName(), results.get(i).getPiiAttributeName());
			assertEquals(data.get(i).getPiiAttributeValue(), results.get(i).getPiiAttributeValue());
			assertEquals(data.get(i).getPiiUid() , results.get(i).getPiiUid());
		}
	}

}
