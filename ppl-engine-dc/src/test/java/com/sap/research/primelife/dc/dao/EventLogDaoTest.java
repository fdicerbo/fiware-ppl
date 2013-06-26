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
package com.sap.research.primelife.dc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dc.entity.EventLog;

import eu.primelife.ppl.pii.impl.PIIType;


/**
 * Test cases for {@link EventLogDao} class.
 *
 * 
 * @date Nov 10, 2010
 * 
 */
public class EventLogDaoTest {

	private static EventLogDao dao;
	private static PiiDao piiDao;
	
	@BeforeClass
	public static void setUp(){
		DaoInitializer.getInstance();
		dao = new EventLogDao();
		piiDao = new PiiDao();
	}

	/**
	 * Tests if {@link EventLog} object is persisted correctly (e.g. time stamp
	 * field is created automatically).
	 */
	@Test
	public void testPersistObject() {
		PIIType pii = new PIIType();
		pii.setAttributeName("http://www.example.org/names#user_name");
		pii.setAttributeValue("Alice");
		piiDao.persistObject(pii);
		assertNotNull(pii.getHjid());

		EventLog log = new EventLog();
		log.setPii(pii);
		log.setMessage("PII used for purpose: 'http://www.w3.org/2006/01/P3Pv11/marketing'");
		dao.persistObject(log);
		assertNotNull(log.getId());
		assertNotNull(log.getReceivedAt());
	}

	/**
	 * Tests <code>findByPii<code> method from {@link EventLogDao} class.
	 */
	@Test
	public void testFindByPii() {
		PIIType pii = new PIIType();
		pii.setAttributeName("http://www.example.org/names#user_name");
		pii.setAttributeValue("Bob");
		piiDao.persistObject(pii);
		assertNotNull(pii.getHjid());

		EventLog log = new EventLog();
		log.setPii(pii);
		log.setMessage("PII used for purpose: 'http://www.w3.org/2002/01/P3Pv1/contact'");
		dao.persistObject(log);
		assertNotNull(log.getId());
		assertNotNull(log.getReceivedAt());

		EventLog log2 = new EventLog();
		log2.setPii(pii);
		log2.setMessage("PII used for purpose: 'http://www.w3.org/2002/01/P3Pv1/admin'");
		dao.persistObject(log2);
		assertNotNull(log2.getId());
		assertNotNull(log2.getReceivedAt());

		List<EventLog> logs = dao.findByPii(pii);
		assertNotNull(logs);
		assertEquals(2, logs.size());
		assertTrue(logs.contains(log));
		assertTrue(logs.contains(log2));
	}

}
