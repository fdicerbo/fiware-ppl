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
package com.sap.research.primelife.ds.pdp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.PiiDaoHelper;
import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.dao.PolicyDao;
import com.sap.research.primelife.ds.pdp.provisionalAction.RevealUnderDhp;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.ConsiderMissingPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.filter.ConsiderOnlyPresentPiiStrategy;
import com.sap.research.primelife.ds.pdp.provisionalAction.merging.MatchStrategy;
import com.sap.research.primelife.ds.pdp.query.pii.PiiQueryByAttributeNameStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPiiStrategy;
import com.sap.research.primelife.ds.pdp.query.policy.PolicyByPreferenceGroupStrategy;
import com.sap.research.primelife.ds.pdp.request.PDPRequest;
import com.sap.research.primelife.ds.pdp.response.ActionResponse;
import com.sap.research.primelife.exceptions.MissingPreferenceGroupException;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import eu.primelife.ppl.policy.impl.PolicyType;


public class RevealUnderDhpTest {
	
	private static PolicyDao dao;
	private static PiiDao piiDao;
	private static PiiDaoHelper piiDaoHelper;
	
	@BeforeClass
	public static void setUp(){
		dao = new PolicyDao();
		dao.deleteAllPreferenceGroups();
		piiDao = new PiiDao();
		piiDaoHelper = new PiiDaoHelper(piiDao);
		piiDao.deleteAllPii();
	}
	
	private DataHandlingPolicyType readDHP(String path) throws JAXBException, SyntaxException {
		Reader r = new InputStreamReader(getClass().getResourceAsStream(path));
		UnmarshallImpl unmarshal = new UnmarshallImpl(DataHandlingPolicyType.class.getPackage());
		return (DataHandlingPolicyType) unmarshal.unmarshal(r);
	}
	
	private PolicyType readPolicyPrefs(String path) throws JAXBException, SyntaxException {
		UnmarshallImpl unmarshal = new UnmarshallImpl(PolicyType.class.getPackage());
		return (PolicyType) unmarshal.unmarshal(getClass().getResourceAsStream(path));
	}
	
	/**
	 * If a PII is missing in the DS store, but defined ACCESS in a policy,
	 * the result should be permit, the value has to be filled by the user.
	 * @throws SyntaxException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws MissingPreferenceGroupException 
	 */
	@Test
	public void testRevealUnderDhpOfMissingPIIInDS() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException,
			IOException, MissingPreferenceGroupException{
		
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/MissingPiiTestPreferences.xml");
		dao.addAsPreferenceGroup(policy);
		
		
		RevealUnderDhp pa = new RevealUnderDhp("missingAttribute");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "missingAttribute"));
		pa.setDataHandlingPolicy(pol);
		pa.setFilterPiiStrategy(new ConsiderMissingPiiStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPreferenceGroupStrategy("MissingPiiTest"));
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		pa.setMergingStrategy(new MatchStrategy());
		List<ActionResponse> result = pa.handle();
		Assert.assertEquals(1, result.size());
		ActionResponse a = result.get(0);
		Assert.assertEquals("missingAttribute", a.getAttributeName());
		Assert.assertNotNull(a.getEnforcementResponse());
		Assert.assertEquals("", a.getEnforcementResponse().getAttributeValue());
		Assert.assertEquals(DecisionType.PERMIT, a.getEnforcementResponse().getDecision());
		Assert.assertEquals(false, a.getAttributeSP().isMatching());
	}
	
	/**
	 * If a PII is missing on DS and also not defined in a policy, but requested under dhp,
	 * this is treated as if it was an empty policy. So the result must be mismatching.
	 * In the end, the user has to decide whether to accept the mismatch.
	 * @throws SyntaxException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws MissingPreferenceGroupException 
	 */
	@Test
	public void testRevealUnderDhpOfMissingPIIInDS2() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException,
			IOException, MissingPreferenceGroupException{
		
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/MissingPiiTestPreferences.xml");
		dao.addAsPreferenceGroup(policy);
		
		
		RevealUnderDhp pa = new RevealUnderDhp("missingAttribute2");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "missingAttribute2"));
		pa.setDataHandlingPolicy(pol);
		pa.setFilterPiiStrategy(new ConsiderMissingPiiStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPreferenceGroupStrategy("MissingPiiTest"));
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		List<ActionResponse> result = pa.handle();
		
		Assert.assertEquals(1, result.size());
		ActionResponse a = result.get(0);
		Assert.assertEquals("missingAttribute2", a.getAttributeName());
		Assert.assertNotNull(a.getEnforcementResponse());
		Assert.assertNull(a.getEnforcementResponse().getAttributeValue());
		Assert.assertEquals(DecisionType.NOT_APPLICABLE, a.getEnforcementResponse().getDecision());
		Assert.assertNull(a.getAttributeSP());
	}
	
	/**
	 * If we request a PII under dhp but the preference group cannot be found,
	 * the result must be an error.
	 * @throws SyntaxException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws MissingPreferenceGroupException 
	 */
	@Test (expected = MissingPreferenceGroupException.class)
	public void testPrefGroupNotFoundDS() throws SyntaxException,
			org.herasaf.xacml.core.SyntaxException, ProcessingException,
			MissingAttributeException, WritingException, JAXBException,
			IOException, MissingPreferenceGroupException{
		
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/MissingPiiTestPreferences.xml");
		dao.addAsPreferenceGroup(policy);
		piiDaoHelper.createOrUpdatePII("testMissingPrefGroup", "bla");
		
		
		RevealUnderDhp pa = new RevealUnderDhp("testMissingPrefGroup");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "testMissingPrefGroup"));
		pa.setDataHandlingPolicy(pol);
		//name in db should be #MissingPiiTest
		pa.setPolicyQueryStrategy(new PolicyByPreferenceGroupStrategy("#MissingPiiTest"));
	}
	
	/**
	 * Test downstream usage when no policy is assigned to the pii. In this case, the servers dhp shall be applied.
	 * @throws JAXBException
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws MissingPreferenceGroupException 
	 */
	@SuppressWarnings("unused")
	@Test
	public void testMissingStickyPolicy() throws JAXBException, SyntaxException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException, MissingPreferenceGroupException{
		PIIType pii = piiDaoHelper.createOrUpdatePII("testMissingSP", "noSP");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/DefaultServerPrefs.xml");
		dao.addAsPreferenceGroup(policy);
		//requesting policy
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		
		RevealUnderDhp pa = new RevealUnderDhp("testMissingSP");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "testMissingSP"));
		pa.setDataHandlingPolicy(pol);
		pa.setFilterPiiStrategy(new ConsiderOnlyPresentPiiStrategy());
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPiiStrategy("default"));
		pa.setMergingStrategy(new MatchStrategy());
		List<ActionResponse> result = pa.handle();
		Assert.assertEquals(1, result.size());
		ActionResponse a = result.get(0);
		Assert.assertEquals("testMissingSP", a.getAttributeName());
		Assert.assertNotNull(a.getAttributeSP());
		Assert.assertNotNull(a.getEnforcementResponse());
		Assert.assertEquals( org.herasaf.xacml.core.context.impl.DecisionType.PERMIT, a.getEnforcementResponse().getDecision());
	}
	
	/**
	 * Test the downstream usage when there is no downstream usage policy, but downstream usage allowed.
	 * @throws JAXBException
	 * @throws SyntaxException
	 * @throws FileNotFoundException
	 * @throws org.herasaf.xacml.core.SyntaxException
	 * @throws ProcessingException
	 * @throws MissingAttributeException
	 * @throws WritingException
	 * @throws MissingPreferenceGroupException 
	 */
	@Test
	public void testPresentStickyPolicy() throws JAXBException, SyntaxException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException, MissingPreferenceGroupException{
		PIIType pii = new PIIType();
		pii.setAttributeName("testPresentSP");
		pii.setAttributeValue("withSP");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/StickyPolicyStressTest.xml");
		pii.getPolicySetOrPolicy().add(policy);
		piiDao.persistObject(pii);
		PolicyType defaultPolicy = readPolicyPrefs("/revealUnderDhpTest/DefaultServerPrefs.xml");
		dao.addAsPreferenceGroup(defaultPolicy);
		
		Assert.assertNotNull(((PolicyType) pii.getPolicySetOrPolicy().get(0)).getStickyPolicy().getAuthorizationsSet());
		
		RevealUnderDhp pa = new RevealUnderDhp("testPresentSP");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "testPresentSP"));
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		pa.setDataHandlingPolicy(pol);
		
		pa.setFilterPiiStrategy(new ConsiderOnlyPresentPiiStrategy());
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPiiStrategy("default"));
		pa.setMergingStrategy(new MatchStrategy());
		List<ActionResponse> result = pa.handle();
		Assert.assertEquals(1, result.size());
		ActionResponse a = result.get(0);
		Assert.assertEquals("testPresentSP", a.getAttributeName());
		Assert.assertNotNull(a.getAttributeSP());
		Assert.assertTrue(a.getAttributeSP().isMatching());
	}
	/**
	 * Test the downstream usage when the policy is in the AuthForDownstreamUsage element.
	 * @throws SyntaxException 
	 * @throws JAXBException 
	 * @throws MissingPreferenceGroupException 
	 * @throws org.herasaf.xacml.core.SyntaxException 
	 * @throws WritingException 
	 * @throws MissingAttributeException 
	 * @throws ProcessingException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testDownstreamUsagePolicy() throws JAXBException, SyntaxException, MissingPreferenceGroupException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException {
		PIIType pii = new PIIType();
		pii.setAttributeName("testPresentSPWithDSU");
		pii.setAttributeValue("withSP");
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/StickyPolicyStressTestWithDSUPolicy.xml");
		pii.getPolicySetOrPolicy().add(policy);
		piiDao.persistObject(pii);
		PolicyType defaultPolicy = readPolicyPrefs("/revealUnderDhpTest/DefaultServerPrefs.xml");
		dao.addAsPreferenceGroup(defaultPolicy);
		
		Assert.assertNotNull(((PolicyType) pii.getPolicySetOrPolicy().get(0)).getStickyPolicy().getAuthorizationsSet());
		
		RevealUnderDhp pa = new RevealUnderDhp("testPresentSPWithDSU");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "testPresentSPWithDSU"));
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		pa.setDataHandlingPolicy(pol);
		
		pa.setFilterPiiStrategy(new ConsiderOnlyPresentPiiStrategy());
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPiiStrategy("default"));
		pa.setMergingStrategy(new MatchStrategy());
		List<ActionResponse> result = pa.handle();
		Assert.assertEquals(1, result.size());
		ActionResponse a = result.get(0);
		Assert.assertEquals("testPresentSPWithDSU", a.getAttributeName());
		Assert.assertNotNull(a.getAttributeSP());
		Assert.assertFalse(a.getAttributeSP().isMatching());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testNullPreferenceGroup() throws JAXBException, SyntaxException, MissingPreferenceGroupException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException {
		PIIType pii = new PIIType();
		pii.setAttributeName("nullPrefGroup");
		pii.setAttributeValue("bla");
		piiDao.persistObject(pii);
		
		PolicyType policy = readPolicyPrefs("/revealUnderDhpTest/MissingPiiTestPreferences.xml");
		dao.addAsPreferenceGroup(policy);
		
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		
		RevealUnderDhp pa = new RevealUnderDhp("missingAttribute");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "missingAttribute"));
		pa.setDataHandlingPolicy(pol);
		pa.setFilterPiiStrategy(new ConsiderMissingPiiStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPreferenceGroupStrategy(null));
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		List<ActionResponse> result = pa.handle();
	}
	
	@SuppressWarnings("unused")
	@Test (expected = MissingPreferenceGroupException.class)
	public void testNullPreferenceGroup2() throws JAXBException, SyntaxException, MissingPreferenceGroupException, FileNotFoundException, org.herasaf.xacml.core.SyntaxException, ProcessingException, MissingAttributeException, WritingException {
		PIIType pii = new PIIType();
		pii.setAttributeName("nullPrefGroup");
		pii.setAttributeValue("bla");
		piiDao.persistObject(pii);
		dao.deleteAllPreferenceGroups();
		
		DataHandlingPolicyType pol = readDHP("/revealUnderDhpTest/DemoDataHandlingPolicy.xml");
		
		RevealUnderDhp pa = new RevealUnderDhp("missingAttribute");
		pa.setXacmlRequest(PDPRequest.createDummyXacmlRequest("http://store.example.com", "missingAttribute"));
		pa.setDataHandlingPolicy(pol);
		pa.setFilterPiiStrategy(new ConsiderMissingPiiStrategy());
		pa.setPolicyQueryStrategy(new PolicyByPreferenceGroupStrategy(null));
		pa.setPiiLoadingStrategy(new PiiQueryByAttributeNameStrategy());
		List<ActionResponse> result = pa.handle();
	}

}
