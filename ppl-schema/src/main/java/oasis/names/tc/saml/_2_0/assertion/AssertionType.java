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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.hyperjaxb3.item.ItemUtils;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Java class for AssertionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssertionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Issuer"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Subject" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Conditions" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Advice" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Statement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthnStatement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AuthzDecisionStatement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}AttributeStatement"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="IssueInstant" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssertionType", propOrder = {
    "issuer",
    "signature",
    "subject",
    "conditions",
    "advice",
    "statementOrAuthnStatementOrAuthzDecisionStatement"
})
@Entity(name = "oasis.names.tc.saml._2_0.assertion.AssertionType")
@Table(name = "ASSERTIONTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class AssertionType implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "Issuer", required = true)
    protected NameIDType issuer;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlElement(name = "Subject")
    protected SubjectType subject;
    @XmlElement(name = "Conditions")
    protected ConditionsType conditions;
    @XmlElement(name = "Advice")
    protected AdviceType advice;
    @XmlElements({
        @XmlElement(name = "AttributeStatement", type = AttributeStatementType.class),
        @XmlElement(name = "Statement"),
        @XmlElement(name = "AuthzDecisionStatement", type = AuthzDecisionStatementType.class),
        @XmlElement(name = "AuthnStatement", type = AuthnStatementType.class)
    })
    protected List<StatementAbstractType> statementOrAuthnStatementOrAuthzDecisionStatement;
    @XmlAttribute(name = "Version", required = true)
    protected String version;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "IssueInstant", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issueInstant;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;
    protected transient List<AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem> statementOrAuthnStatementOrAuthzDecisionStatementItems;

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link NameIDType }
     *     
     */
    @ManyToOne(targetEntity = NameIDType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ISSUER_ASSERTIONTYPE_HJID")
    public NameIDType getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIDType }
     *     
     */
    public void setIssuer(NameIDType value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    @ManyToOne(targetEntity = SignatureType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "SIGNATURE_ASSERTIONTYPE_HJID")
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectType }
     *     
     */
    @ManyToOne(targetEntity = SubjectType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "SUBJECT_ASSERTIONTYPE_HJID")
    public SubjectType getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectType }
     *     
     */
    public void setSubject(SubjectType value) {
        this.subject = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionsType }
     *     
     */
    @ManyToOne(targetEntity = ConditionsType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "CONDITIONS_ASSERTIONTYPE_HJID")
    public ConditionsType getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionsType }
     *     
     */
    public void setConditions(ConditionsType value) {
        this.conditions = value;
    }

    /**
     * Gets the value of the advice property.
     * 
     * @return
     *     possible object is
     *     {@link AdviceType }
     *     
     */
    @ManyToOne(targetEntity = AdviceType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ADVICE_ASSERTIONTYPE_HJID")
    public AdviceType getAdvice() {
        return advice;
    }

    /**
     * Sets the value of the advice property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdviceType }
     *     
     */
    public void setAdvice(AdviceType value) {
        this.advice = value;
    }

    /**
     * Gets the value of the statementOrAuthnStatementOrAuthzDecisionStatement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statementOrAuthnStatementOrAuthzDecisionStatement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatementOrAuthnStatementOrAuthzDecisionStatement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeStatementType }
     * {@link StatementAbstractType }
     * {@link AuthzDecisionStatementType }
     * {@link AuthnStatementType }
     * 
     * 
     */
    @Transient
    public List<StatementAbstractType> getStatementOrAuthnStatementOrAuthzDecisionStatement() {
        if (statementOrAuthnStatementOrAuthzDecisionStatement == null) {
            statementOrAuthnStatementOrAuthzDecisionStatement = new ArrayList<StatementAbstractType>();
        }
        return this.statementOrAuthnStatementOrAuthzDecisionStatement;
    }

    /**
     * 
     * 
     */
    public void setStatementOrAuthnStatementOrAuthzDecisionStatement(List<StatementAbstractType> statementOrAuthnStatementOrAuthzDecisionStatement) {
        this.statementOrAuthnStatementOrAuthzDecisionStatement = statementOrAuthnStatementOrAuthzDecisionStatement;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "VERSION_", length = 255)
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ID", length = 255)
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the issueInstant property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getIssueInstant() {
        return issueInstant;
    }

    /**
     * Sets the value of the issueInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssueInstant(XMLGregorianCalendar value) {
        this.issueInstant = value;
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

    @OneToMany(targetEntity = AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "STATEMENTORAUTHNSTATEMENTORA_1")
    public List<AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem> getStatementOrAuthnStatementOrAuthzDecisionStatementItems() {
        if (this.statementOrAuthnStatementOrAuthzDecisionStatementItems == null) {
            this.statementOrAuthnStatementOrAuthzDecisionStatementItems = new ArrayList<AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.statementOrAuthnStatementOrAuthzDecisionStatement)) {
            this.statementOrAuthnStatementOrAuthzDecisionStatement = ItemUtils.wrap(this.statementOrAuthnStatementOrAuthzDecisionStatement, this.statementOrAuthnStatementOrAuthzDecisionStatementItems, AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem.class);
        }
        return this.statementOrAuthnStatementOrAuthzDecisionStatementItems;
    }

    public void setStatementOrAuthnStatementOrAuthzDecisionStatementItems(List<AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem> value) {
        this.statementOrAuthnStatementOrAuthzDecisionStatement = null;
        this.statementOrAuthnStatementOrAuthzDecisionStatementItems = null;
        this.statementOrAuthnStatementOrAuthzDecisionStatementItems = value;
        if (this.statementOrAuthnStatementOrAuthzDecisionStatementItems == null) {
            this.statementOrAuthnStatementOrAuthzDecisionStatementItems = new ArrayList<AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.statementOrAuthnStatementOrAuthzDecisionStatement)) {
            this.statementOrAuthnStatementOrAuthzDecisionStatement = ItemUtils.wrap(this.statementOrAuthnStatementOrAuthzDecisionStatement, this.statementOrAuthnStatementOrAuthzDecisionStatementItems, AssertionTypeStatementOrAuthnStatementOrAuthzDecisionStatementItem.class);
        }
    }

    @Basic
    @Column(name = "ISSUEINSTANTITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getIssueInstantItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getIssueInstant());
    }

    public void setIssueInstantItem(Date target) {
        setIssueInstant(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof AssertionType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final AssertionType that = ((AssertionType) object);
        equalsBuilder.append(this.getIssuer(), that.getIssuer());
        equalsBuilder.append(this.getSignature(), that.getSignature());
        equalsBuilder.append(this.getSubject(), that.getSubject());
        equalsBuilder.append(this.getConditions(), that.getConditions());
        equalsBuilder.append(this.getAdvice(), that.getAdvice());
        equalsBuilder.append(this.getStatementOrAuthnStatementOrAuthzDecisionStatement(), that.getStatementOrAuthnStatementOrAuthzDecisionStatement());
        equalsBuilder.append(this.getVersion(), that.getVersion());
        equalsBuilder.append(this.getID(), that.getID());
        equalsBuilder.append(this.getIssueInstant(), that.getIssueInstant());
    }

    public boolean equals(Object object) {
        if (!(object instanceof AssertionType)) {
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
        hashCodeBuilder.append(this.getIssuer());
        hashCodeBuilder.append(this.getSignature());
        hashCodeBuilder.append(this.getSubject());
        hashCodeBuilder.append(this.getConditions());
        hashCodeBuilder.append(this.getAdvice());
        hashCodeBuilder.append(this.getStatementOrAuthnStatementOrAuthzDecisionStatement());
        hashCodeBuilder.append(this.getVersion());
        hashCodeBuilder.append(this.getID());
        hashCodeBuilder.append(this.getIssueInstant());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            NameIDType theIssuer;
            theIssuer = this.getIssuer();
            toStringBuilder.append("issuer", theIssuer);
        }
        {
            SignatureType theSignature;
            theSignature = this.getSignature();
            toStringBuilder.append("signature", theSignature);
        }
        {
            SubjectType theSubject;
            theSubject = this.getSubject();
            toStringBuilder.append("subject", theSubject);
        }
        {
            ConditionsType theConditions;
            theConditions = this.getConditions();
            toStringBuilder.append("conditions", theConditions);
        }
        {
            AdviceType theAdvice;
            theAdvice = this.getAdvice();
            toStringBuilder.append("advice", theAdvice);
        }
        {
            List<StatementAbstractType> theStatementOrAuthnStatementOrAuthzDecisionStatement;
            theStatementOrAuthnStatementOrAuthzDecisionStatement = this.getStatementOrAuthnStatementOrAuthzDecisionStatement();
            toStringBuilder.append("statementOrAuthnStatementOrAuthzDecisionStatement", theStatementOrAuthnStatementOrAuthzDecisionStatement);
        }
        {
            String theVersion;
            theVersion = this.getVersion();
            toStringBuilder.append("version", theVersion);
        }
        {
            String theID;
            theID = this.getID();
            toStringBuilder.append("id", theID);
        }
        {
            XMLGregorianCalendar theIssueInstant;
            theIssueInstant = this.getIssueInstant();
            toStringBuilder.append("issueInstant", theIssueInstant);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
