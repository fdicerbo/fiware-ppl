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


package eu.primelife.ppl.policy.obligation.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.hyperjaxb3.item.ItemUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;


/**
 * <p>Java class for TriggerPersonalDataAccessedForPurpose complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TriggerPersonalDataAccessedForPurpose">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.primelife.eu/ppl/obligation}Trigger">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.primelife.eu/ppl}Purpose" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MaxDelay" type="{http://www.primelife.eu/ppl/obligation}Duration"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TriggerPersonalDataAccessedForPurpose", propOrder = {
    "purpose",
    "maxDelay"
})
@Entity(name = "eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose")
@Table(name = "TRIGGERPERSONALDATAACCESSEDF_1")
public class TriggerPersonalDataAccessedForPurpose
    extends Trigger
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "Purpose", namespace = "http://www.primelife.eu/ppl")
    protected List<String> purpose;
    @XmlElement(name = "MaxDelay", required = true)
    protected Duration maxDelay;
    protected transient List<TriggerPersonalDataAccessedForPurposePurposeItem> purposeItems;

    /**
     * Gets the value of the purpose property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the purpose property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPurpose().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    @Transient
    public List<String> getPurpose() {
        if (purpose == null) {
            purpose = new ArrayList<String>();
        }
        return this.purpose;
    }

    /**
     * 
     * 
     */
    public void setPurpose(List<String> purpose) {
        this.purpose = purpose;
    }

    /**
     * Gets the value of the maxDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    @ManyToOne(targetEntity = Duration.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "MAXDELAY_TRIGGERPERSONALDATA_0")
    public Duration getMaxDelay() {
        return maxDelay;
    }

    /**
     * Sets the value of the maxDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setMaxDelay(Duration value) {
        this.maxDelay = value;
    }

    @OneToMany(targetEntity = TriggerPersonalDataAccessedForPurposePurposeItem.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "PURPOSEITEMS_TRIGGERPERSONAL_0")
    public List<TriggerPersonalDataAccessedForPurposePurposeItem> getPurposeItems() {
        if (this.purposeItems == null) {
            this.purposeItems = new ArrayList<TriggerPersonalDataAccessedForPurposePurposeItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.purpose)) {
            this.purpose = ItemUtils.wrap(this.purpose, this.purposeItems, TriggerPersonalDataAccessedForPurposePurposeItem.class);
        }
        return this.purposeItems;
    }

    public void setPurposeItems(List<TriggerPersonalDataAccessedForPurposePurposeItem> value) {
        this.purpose = null;
        this.purposeItems = null;
        this.purposeItems = value;
        if (this.purposeItems == null) {
            this.purposeItems = new ArrayList<TriggerPersonalDataAccessedForPurposePurposeItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.purpose)) {
            this.purpose = ItemUtils.wrap(this.purpose, this.purposeItems, TriggerPersonalDataAccessedForPurposePurposeItem.class);
        }
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TriggerPersonalDataAccessedForPurpose)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TriggerPersonalDataAccessedForPurpose that = ((TriggerPersonalDataAccessedForPurpose) object);
        equalsBuilder.append(this.getPurpose(), that.getPurpose());
        equalsBuilder.append(this.getMaxDelay(), that.getMaxDelay());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TriggerPersonalDataAccessedForPurpose)) {
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
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getPurpose());
        hashCodeBuilder.append(this.getMaxDelay());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            List<String> thePurpose;
            thePurpose = this.getPurpose();
            toStringBuilder.append("purpose", thePurpose);
        }
        {
            Duration theMaxDelay;
            theMaxDelay = this.getMaxDelay();
            toStringBuilder.append("maxDelay", theMaxDelay);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
