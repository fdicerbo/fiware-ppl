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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import org.jvnet.hyperjaxb3.item.Item;
import org.jvnet.hyperjaxb3.xml.bind.JAXBElementUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "eu.primelife.ppl.policy.impl.AuthorizationsSetTypeAuthorizationItem")
@Table(name = "AUTHORIZATIONSSETTYPEAUTHORI_0")
@Inheritance(strategy = InheritanceType.JOINED)
public class AuthorizationsSetTypeAuthorizationItem
    implements Serializable, Item<JAXBElement<? extends AuthorizationType>>
{

    @XmlElementRef(name = "Authorization", namespace = "http://www.primelife.eu/ppl", type = JAXBElement.class)
    protected JAXBElement<? extends AuthorizationType> item;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * 
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AuthorizationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AuthzDownstreamUsageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AuthzUseForPurpose }{@code >}
     *     
     */
    @Transient
    public JAXBElement<? extends AuthorizationType> getItem() {
        return item;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AuthorizationType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AuthzDownstreamUsageType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AuthzUseForPurpose }{@code >}
     *     
     */
    public void setItem(JAXBElement<? extends AuthorizationType> value) {
        this.item = ((JAXBElement<? extends AuthorizationType> ) value);
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

    @SuppressWarnings("unchecked")
    @Basic
    @Column(name = "ITEMNAME")
    public String getItemName() {
        if (this.getItem() instanceof JAXBElement) {
            return JAXBElementUtils.getName(((JAXBElement<AuthorizationType> ) this.getItem()));
        } else {
            return null;
        }
    }

    public void setItemName(String target) {
        if (target!= null) {
            setItem(JAXBElementUtils.wrap(this.getItem(), target, AuthorizationType.class));
        }
    }

    @SuppressWarnings("unchecked")
    @ManyToOne(targetEntity = AuthorizationType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ITEMVALUE_AUTHORIZATIONSSETT_0")
    public AuthorizationType getItemValue() {
        if (this.getItem() instanceof JAXBElement) {
            return JAXBElementUtils.getValue(((JAXBElement<AuthorizationType> ) this.getItem()));
        } else {
            return null;
        }
    }

    public void setItemValue(AuthorizationType target) {
        if (target!= null) {
            setItem(JAXBElementUtils.wrap(this.getItem(), target));
        }
    }

}
