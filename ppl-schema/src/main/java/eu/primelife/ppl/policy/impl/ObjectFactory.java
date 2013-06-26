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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v3.0-03/04/2009 09:20 AM(valikov)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.03 at 09:58:45 AM CET 
//


package eu.primelife.ppl.policy.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.primelife.ppl.policy.impl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Policy_QNAME = new QName("http://www.primelife.eu/ppl", "Policy");
    private final static QName _StickyPolicy_QNAME = new QName("http://www.primelife.eu/ppl", "StickyPolicy");
    private final static QName _Purpose_QNAME = new QName("http://www.primelife.eu/ppl", "Purpose");
    private final static QName _ProvisionalAction_QNAME = new QName("http://www.primelife.eu/ppl", "ProvisionalAction");
    private final static QName _AuthorizationsSet_QNAME = new QName("http://www.primelife.eu/ppl", "AuthorizationsSet");
    private final static QName _Authorization_QNAME = new QName("http://www.primelife.eu/ppl", "Authorization");
    private final static QName _PolicySet_QNAME = new QName("http://www.primelife.eu/ppl", "PolicySet");
    private final static QName _AuthzDownstreamUsage_QNAME = new QName("http://www.primelife.eu/ppl", "AuthzDownstreamUsage");
    private final static QName _AuthzUseForPurpose_QNAME = new QName("http://www.primelife.eu/ppl", "AuthzUseForPurpose");
    private final static QName _ProvisionalActions_QNAME = new QName("http://www.primelife.eu/ppl", "ProvisionalActions");
    private final static QName _Rule_QNAME = new QName("http://www.primelife.eu/ppl", "Rule");
    private final static QName _DataHandlingPreferences_QNAME = new QName("http://www.primelife.eu/ppl", "DataHandlingPreferences");
    private final static QName _DataHandlingPolicy_QNAME = new QName("http://www.primelife.eu/ppl", "DataHandlingPolicy");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.primelife.ppl.policy.impl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RuleType }
     * 
     */
    public RuleType createRuleType() {
        return new RuleType();
    }

    /**
     * Create an instance of {@link ProvisionalActionsType }
     * 
     */
    public ProvisionalActionsType createProvisionalActionsType() {
        return new ProvisionalActionsType();
    }

    /**
     * Create an instance of {@link PolicySetType }
     * 
     */
    public PolicySetType createPolicySetType() {
        return new PolicySetType();
    }

    /**
     * Create an instance of {@link DataHandlingPolicyType }
     * 
     */
    public DataHandlingPolicyType createDataHandlingPolicyType() {
        return new DataHandlingPolicyType();
    }

    /**
     * Create an instance of {@link CommonDHPSPType }
     * 
     */
    public CommonDHPSPType createCommonDHPSPType() {
        return new CommonDHPSPType();
    }

    /**
     * Create an instance of {@link AuthorizationType }
     * 
     */
    public AuthorizationType createAuthorizationType() {
        return new AuthorizationType();
    }

    /**
     * Create an instance of {@link StickyPolicyType }
     * 
     */
    public StickyPolicyType createStickyPolicyType() {
        return new StickyPolicyType();
    }

    /**
     * Create an instance of {@link PolicyType }
     * 
     */
    public PolicyType createPolicyType() {
        return new PolicyType();
    }

    /**
     * Create an instance of {@link AuthzUseForPurpose }
     * 
     */
    public AuthzUseForPurpose createAuthzUseForPurpose() {
        return new AuthzUseForPurpose();
    }

    /**
     * Create an instance of {@link DataHandlingPreferencesType }
     * 
     */
    public DataHandlingPreferencesType createDataHandlingPreferencesType() {
        return new DataHandlingPreferencesType();
    }

    /**
     * Create an instance of {@link AuthzDownstreamUsageType }
     * 
     */
    public AuthzDownstreamUsageType createAuthzDownstreamUsage() {
        return new AuthzDownstreamUsageType();
    }

    /**
     * Create an instance of {@link ProvisionalActionType }
     * 
     */
    public ProvisionalActionType createProvisionalActionType() {
        return new ProvisionalActionType();
    }

    /**
     * Create an instance of {@link AuthorizationsSetType }
     * 
     */
    public AuthorizationsSetType createAuthorizationsSetType() {
        return new AuthorizationsSetType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "Policy", substitutionHeadNamespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", substitutionHeadName = "Policy")
    public JAXBElement<PolicyType> createPolicy(PolicyType value) {
        return new JAXBElement<PolicyType>(_Policy_QNAME, PolicyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StickyPolicyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "StickyPolicy")
    public JAXBElement<StickyPolicyType> createStickyPolicy(StickyPolicyType value) {
        return new JAXBElement<StickyPolicyType>(_StickyPolicy_QNAME, StickyPolicyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "Purpose")
    public JAXBElement<String> createPurpose(String value) {
        return new JAXBElement<String>(_Purpose_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvisionalActionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "ProvisionalAction")
    public JAXBElement<ProvisionalActionType> createProvisionalAction(ProvisionalActionType value) {
        return new JAXBElement<ProvisionalActionType>(_ProvisionalAction_QNAME, ProvisionalActionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizationsSetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "AuthorizationsSet")
    public JAXBElement<AuthorizationsSetType> createAuthorizationsSet(AuthorizationsSetType value) {
        return new JAXBElement<AuthorizationsSetType>(_AuthorizationsSet_QNAME, AuthorizationsSetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "Authorization")
    public JAXBElement<AuthorizationType> createAuthorization(AuthorizationType value) {
        return new JAXBElement<AuthorizationType>(_Authorization_QNAME, AuthorizationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolicySetType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "PolicySet", substitutionHeadNamespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", substitutionHeadName = "PolicySet")
    public JAXBElement<PolicySetType> createPolicySet(PolicySetType value) {
        return new JAXBElement<PolicySetType>(_PolicySet_QNAME, PolicySetType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthzDownstreamUsageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "AuthzDownstreamUsage", substitutionHeadNamespace = "http://www.primelife.eu/ppl", substitutionHeadName = "Authorization")
    public JAXBElement<AuthzDownstreamUsageType> createAuthzDownstreamUsage(AuthzDownstreamUsageType value) {
        return new JAXBElement<AuthzDownstreamUsageType>(_AuthzDownstreamUsage_QNAME, AuthzDownstreamUsageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthzUseForPurpose }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "AuthzUseForPurpose", substitutionHeadNamespace = "http://www.primelife.eu/ppl", substitutionHeadName = "Authorization")
    public JAXBElement<AuthzUseForPurpose> createAuthzUseForPurpose(AuthzUseForPurpose value) {
        return new JAXBElement<AuthzUseForPurpose>(_AuthzUseForPurpose_QNAME, AuthzUseForPurpose.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvisionalActionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "ProvisionalActions")
    public JAXBElement<ProvisionalActionsType> createProvisionalActions(ProvisionalActionsType value) {
        return new JAXBElement<ProvisionalActionsType>(_ProvisionalActions_QNAME, ProvisionalActionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RuleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "Rule", substitutionHeadNamespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", substitutionHeadName = "Rule")
    public JAXBElement<RuleType> createRule(RuleType value) {
        return new JAXBElement<RuleType>(_Rule_QNAME, RuleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHandlingPreferencesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "DataHandlingPreferences")
    public JAXBElement<DataHandlingPreferencesType> createDataHandlingPreferences(DataHandlingPreferencesType value) {
        return new JAXBElement<DataHandlingPreferencesType>(_DataHandlingPreferences_QNAME, DataHandlingPreferencesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHandlingPolicyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl", name = "DataHandlingPolicy")
    public JAXBElement<DataHandlingPolicyType> createDataHandlingPolicy(DataHandlingPolicyType value) {
        return new JAXBElement<DataHandlingPolicyType>(_DataHandlingPolicy_QNAME, DataHandlingPolicyType.class, null, value);
    }

}
