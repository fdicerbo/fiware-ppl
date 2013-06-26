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
//package com.sap.research.primelife.dc.dao;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.sap.research.primelife.dao.PiiDao;
//import com.sap.research.primelife.dc.entity.PiiUniqueId;
//
//import eu.primelife.ppl.pii.impl.PIIType;
//
//
///**
// * 
// * 
// * @version 0.1
// * @date Oct 06, 2010
// * 
// */
//public class PiiUniqueIdDaoTest {
//
//	private static PiiUniqueIdDAO dao;
//	private static PiiDao piiDao;
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void oneTimeSetUp() throws Exception {
//		DaoInitializer.getInstance();
//		dao = new PiiUniqueIdDAO();
//		piiDao = new PiiDao();
//
//		for (PiiUniqueId piiUniqueId : dao.findObjects(PiiUniqueId.class))
//			dao.deleteObject(piiUniqueId);
//	}
//
//	@Test
//	public void testPersistObject() {
//		PIIType pii = new PIIType();
//		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
//		pii.setAttributeValue("test@example.com");
//		piiDao.persistObject(pii);
//
//		PiiUniqueId piiUniqueId = new PiiUniqueId();
//		piiUniqueId.setId(pii.getHjid());
//		piiUniqueId.setPii(pii);
//
//		PiiUniqueId piiUniqueId2 = dao.persistObject(piiUniqueId);
//		assertNotNull(piiUniqueId.getUniqueId());
//		assertEquals(piiUniqueId.getId(), piiUniqueId2.getId());
//		assertEquals(piiUniqueId.getPii().getHjid(),
//				piiUniqueId2.getPii().getHjid());
//	}
//
//	@Test
//	public void testDeleteObject() {
//		PIIType pii = new PIIType();
//		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
//		pii.setAttributeValue("test1@example.com");
//		piiDao.persistObject(pii);
//
//		PiiUniqueId piiUniqueId = new PiiUniqueId();
//		piiUniqueId.setId(pii.getHjid());
//		piiUniqueId.setPii(pii);
//
//		PiiUniqueId piiUniqueId2 = dao.persistObject(piiUniqueId);
//		dao.deleteObject(piiUniqueId2);
//
//		PiiUniqueId piiUniqueId3 =
//			dao.findByUniqueId(piiUniqueId2.getUniqueId());
//		assertNull(piiUniqueId3);
//
//		// test whether associated PII was also deleted by cascade
//		PIIType pii2 = piiDao.findObject(PIIType.class, pii.getHjid());
//		assertNull(pii2);
//	}
//
//	@Test
//	public void testFindByUniqueId() {
//		PIIType pii = new PIIType();
//		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
//		pii.setAttributeValue("test2@example.com");
//		piiDao.persistObject(pii);
//
//		PiiUniqueId piiUniqueId = new PiiUniqueId();
//		piiUniqueId.setId(pii.getHjid());
//		piiUniqueId.setPii(pii);
//
//		dao.persistObject(piiUniqueId);
//		assertNotNull(piiUniqueId.getUniqueId());
//
//		PiiUniqueId piiUniqueId2 =
//			dao.findByUniqueId(piiUniqueId.getUniqueId());
//		assertNotNull(piiUniqueId2);
//		assertEquals(piiUniqueId.getId(), piiUniqueId2.getId());
//		assertEquals(piiUniqueId.getUniqueId(), piiUniqueId2.getUniqueId());
//		assertEquals(piiUniqueId.getPii().getHjid(),
//				piiUniqueId2.getPii().getHjid());
//	}
//
//	/**
//	 * In this test two different PiiUniqueId entities with the same uniqueId
//	 * field value are being added to database. Persisting second entity should
//	 * throw exception caused by
//	 * org.hibernate.exception.ConstraintViolationException.
//	 */
//	@Test(expected = Exception.class)
//	public void testUniqueConstraint() {
//		PIIType pii = new PIIType();
//		pii.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
//		pii.setAttributeValue("test3@example.com");
//		piiDao.persistObject(pii);
//
//		PiiUniqueId piiUniqueId = new PiiUniqueId();
//		piiUniqueId.setId(pii.getHjid());
//		piiUniqueId.setPii(pii);
//
//		dao.persistObject(piiUniqueId);
//
//		PIIType pii2 = new PIIType();
//		pii2.setAttributeName("http://www.w3.org/2006/vcard/ns#email");
//		pii2.setAttributeValue("test4@example.com");
//		piiDao.persistObject(pii2);
//
//		PiiUniqueId piiUniqueId2 = new PiiUniqueId();
//		piiUniqueId2.setId(pii2.getHjid());
//		piiUniqueId2.setUniqueId(piiUniqueId.getUniqueId());
//		piiUniqueId2.setPii(pii2);
//
//		dao.getEm().getTransaction().begin();
//		dao.getEm().persist(piiUniqueId2);
//		dao.getEm().getTransaction().commit();
//	}
//
//}
