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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;


/**
 * <p>Java class for CommonDHPSPType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommonDHPSPType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.primelife.eu/ppl}AuthorizationsSet" minOccurs="0"/>
 *         &lt;element ref="{http://www.primelife.eu/ppl/obligation}ObligationsSet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommonDHPSPType", propOrder = {
    "authorizationsSet",
    "obligationsSet"
})
@XmlSeeAlso({
    DataHandlingPolicyType.class,
    DataHandlingPreferencesType.class,
    StickyPolicyType.class
})
@Entity(name = "eu.primelife.ppl.policy.impl.CommonDHPSPType")
@Table(name = "COMMONDHPSPTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class CommonDHPSPType
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "AuthorizationsSet")
    protected AuthorizationsSetType authorizationsSet;
    @XmlElement(name = "ObligationsSet", namespace = "http://www.primelife.eu/ppl/obligation")
    protected ObligationsSet obligationsSet;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * Gets the value of the authorizationsSet property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorizationsSetType }
     *     
     */
    @ManyToOne(targetEntity = AuthorizationsSetType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "AUTHORIZATIONSSET_COMMONDHPS_0")
    public AuthorizationsSetType getAuthorizationsSet() {
        return authorizationsSet;
    }

    /**
     * Sets the value of the authorizationsSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorizationsSetType }
     *     
     */
    public void setAuthorizationsSet(AuthorizationsSetType value) {
        this.authorizationsSet = value;
    }

    /**
     * Gets the value of the obligationsSet property.
     * 
     * @return
     *     possible object is
     *     {@link ObligationsSet }
     *     
     */
    @ManyToOne(targetEntity = ObligationsSet.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "OBLIGATIONSSET_COMMONDHPSPTY_0")
    public ObligationsSet getObligationsSet() {
        return obligationsSet;
    }

    /**
     * Sets the value of the obligationsSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObligationsSet }
     *     
     */
    public void setObligationsSet(ObligationsSet value) {
        this.obligationsSet = value;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof CommonDHPSPType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final CommonDHPSPType that = ((CommonDHPSPType) object);
        equalsBuilder.append(this.getAuthorizationsSet(), that.getAuthorizationsSet());
        equalsBuilder.append(this.getObligationsSet(), that.getObligationsSet());
    }

    public boolean equals(Object object) {
        if (!(object instanceof CommonDHPSPType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getAuthorizationsSet());
        hashCodeBuilder.append(this.getObligationsSet());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            AuthorizationsSetType theAuthorizationsSet;
            theAuthorizationsSet = this.getAuthorizationsSet();
            toStringBuilder.append("authorizationsSet", theAuthorizationsSet);
        }
        {
            ObligationsSet theObligationsSet;
            theObligationsSet = this.getObligationsSet();
            toStringBuilder.append("obligationsSet", theObligationsSet);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
