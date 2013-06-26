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


package oasis.names.tc.saml._2_0.assertion;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import org.jvnet.hyperjaxb3.item.MixedItem;
import org.jvnet.hyperjaxb3.xml.bind.JAXBContextUtils;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.ElementAsString;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "oasis.names.tc.saml._2_0.assertion.SubjectConfirmationDataTypeContentItem")
@Table(name = "SUBJECTCONFIRMATIONDATATYPEC_0")
@Inheritance(strategy = InheritanceType.JOINED)
public class SubjectConfirmationDataTypeContentItem
    implements Serializable, MixedItem<Object>
{

    @XmlAnyElement(lax = true)
    protected Object item;
    @XmlAttribute(name = "Text")
    protected String text;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * 
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     {@link Object }
     *     
     */
    @Transient
    public Object getItem() {
        return item;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public void setItem(Object value) {
        this.item = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "TEXT")
    @Lob
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the hjid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    /**
     * Sets the value of the hjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

    @Basic
    @Column(name = "ITEMELEMENT")
    @Lob
    public String getItemElement() {
        if (this.getItem() instanceof Element) {
            return XmlAdapterUtils.unmarshall(ElementAsString.class, ((Element) this.getItem()));
        } else {
            return null;
        }
    }

    public void setItemElement(String target) {
        if (target!= null) {
            setItem(XmlAdapterUtils.marshall(ElementAsString.class, target));
        }
    }

    @Basic
    @Column(name = "ITEMOBJECT")
    @Lob
    public String getItemObject() {
        if (JAXBContextUtils.isElement("eu.primelife.ppl.claims.impl:eu.primelife.ppl.stickypolicy.obligation.impl:eu.primelife.ppl.policy.obligation.impl:oasis.names.tc.xacml._2_0.context.schema.os:eu.primelife.ppl.stickypolicy.impl:eu.primelife.ppl.pii.impl:eu.primelife.ppl.policy.credential.impl:eu.primelife.ppl.policy.impl:org.w3._2000._09.xmldsig_:eu.primelife.ppl.policy.xacml.impl:oasis.names.tc.saml._2_0.assertion:eu.primelife.ppl.stickypolicy.authorization.impl:org.w3._2001._04.xmlenc_", this.getItem())) {
            return JAXBContextUtils.unmarshall("eu.primelife.ppl.claims.impl:eu.primelife.ppl.stickypolicy.obligation.impl:eu.primelife.ppl.policy.obligation.impl:oasis.names.tc.xacml._2_0.context.schema.os:eu.primelife.ppl.stickypolicy.impl:eu.primelife.ppl.pii.impl:eu.primelife.ppl.policy.credential.impl:eu.primelife.ppl.policy.impl:org.w3._2000._09.xmldsig_:eu.primelife.ppl.policy.xacml.impl:oasis.names.tc.saml._2_0.assertion:eu.primelife.ppl.stickypolicy.authorization.impl:org.w3._2001._04.xmlenc_", this.getItem());
        } else {
            return null;
        }
    }

    public void setItemObject(String target) {
        if (target!= null) {
            setItem(JAXBContextUtils.marshall("eu.primelife.ppl.claims.impl:eu.primelife.ppl.stickypolicy.obligation.impl:eu.primelife.ppl.policy.obligation.impl:oasis.names.tc.xacml._2_0.context.schema.os:eu.primelife.ppl.stickypolicy.impl:eu.primelife.ppl.pii.impl:eu.primelife.ppl.policy.credential.impl:eu.primelife.ppl.policy.impl:org.w3._2000._09.xmldsig_:eu.primelife.ppl.policy.xacml.impl:oasis.names.tc.saml._2_0.assertion:eu.primelife.ppl.stickypolicy.authorization.impl:org.w3._2001._04.xmlenc_", target));
        }
    }

}
