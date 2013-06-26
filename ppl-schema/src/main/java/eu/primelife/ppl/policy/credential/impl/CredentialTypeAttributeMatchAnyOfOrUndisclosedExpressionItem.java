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


package eu.primelife.ppl.policy.credential.impl;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import org.jvnet.hyperjaxb3.item.Item;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity(name = "eu.primelife.ppl.policy.credential.impl.CredentialTypeAttributeMatchAnyOfOrUndisclosedExpressionItem")
@Table(name = "CREDENTIALTYPEATTRIBUTEMATCH_0")
@Inheritance(strategy = InheritanceType.JOINED)
public class CredentialTypeAttributeMatchAnyOfOrUndisclosedExpressionItem
    implements Serializable, Item<Object>
{

    @XmlElements({
        @XmlElement(name = "AttributeMatchAnyOf", type = AttributeMatchAnyOfType.class),
        @XmlElement(name = "UndisclosedExpression", type = UndisclosedExpressionType.class)
    })
    protected Object item;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * 
     * 
     * @return
     *     possible object is
     *     {@link AttributeMatchAnyOfType }
     *     {@link UndisclosedExpressionType }
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
     *     {@link AttributeMatchAnyOfType }
     *     {@link UndisclosedExpressionType }
     *     
     */
    public void setItem(Object value) {
        this.item = value;
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

    @ManyToOne(targetEntity = AttributeMatchAnyOfType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ITEMATTRIBUTEMATCHANYOF_CRED_0")
    public AttributeMatchAnyOfType getItemAttributeMatchAnyOf() {
        if (this.getItem() instanceof AttributeMatchAnyOfType) {
            return ((AttributeMatchAnyOfType) this.getItem());
        } else {
            return null;
        }
    }

    public void setItemAttributeMatchAnyOf(AttributeMatchAnyOfType target) {
        if (target!= null) {
            setItem(target);
        }
    }

    @ManyToOne(targetEntity = UndisclosedExpressionType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ITEMUNDISCLOSEDEXPRESSION_CR_0")
    public UndisclosedExpressionType getItemUndisclosedExpression() {
        if (this.getItem() instanceof UndisclosedExpressionType) {
            return ((UndisclosedExpressionType) this.getItem());
        } else {
            return null;
        }
    }

    public void setItemUndisclosedExpression(UndisclosedExpressionType target) {
        if (target!= null) {
            setItem(target);
        }
    }

}
