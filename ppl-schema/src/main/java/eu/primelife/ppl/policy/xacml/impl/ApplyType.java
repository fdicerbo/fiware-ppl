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


package eu.primelife.ppl.policy.xacml.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import eu.primelife.ppl.policy.credential.impl.CredentialAttributeDesignatorType;
import eu.primelife.ppl.policy.credential.impl.PrimelifeApplyType;
import eu.primelife.ppl.policy.credential.impl.UndisclosedExpressionType;
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
 * <p>Java class for ApplyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplyType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ExpressionType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}Expression" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FunctionId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplyType", propOrder = {
    "expression"
})
@XmlSeeAlso({
    PrimelifeApplyType.class
})
@Entity(name = "eu.primelife.ppl.policy.xacml.impl.ApplyType")
@Table(name = "APPLYTYPE")
public class ApplyType
    extends ExpressionType
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElementRef(name = "Expression", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", type = JAXBElement.class)
    protected List<JAXBElement<?>> expression;
    @XmlAttribute(name = "FunctionId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String functionId;
    protected transient List<ApplyTypeExpressionItem> expressionItems;

    /**
     * Gets the value of the expression property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the expression property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpression().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SubjectAttributeDesignatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeSelectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResourceAttributeDesignatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ApplyType }{@code >}
     * {@link JAXBElement }{@code <}{@link PrimelifeApplyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeValueType }{@code >}
     * {@link JAXBElement }{@code <}{@link CredentialAttributeDesignatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ActionAttributeDesignatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link VariableReferenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link UndisclosedExpressionType }{@code >}
     * {@link JAXBElement }{@code <}{@link EnvironmentAttributeDesignatorType }{@code >}
     * 
     * 
     */
    @Transient
    public List<JAXBElement<?>> getExpression() {
        if (expression == null) {
            expression = new ArrayList<JAXBElement<?>>();
        }
        return this.expression;
    }

    /**
     * 
     * 
     */
    public void setExpression(List<JAXBElement<?>> expression) {
        this.expression = expression;
    }

    /**
     * Gets the value of the functionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "FUNCTIONID")
    public String getFunctionId() {
        return functionId;
    }

    /**
     * Sets the value of the functionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionId(String value) {
        this.functionId = value;
    }

    @OneToMany(targetEntity = ApplyTypeExpressionItem.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "EXPRESSIONITEMS_APPLYTYPE_HJ_0")
    public List<ApplyTypeExpressionItem> getExpressionItems() {
        if (this.expressionItems == null) {
            this.expressionItems = new ArrayList<ApplyTypeExpressionItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.expression)) {
            this.expression = ItemUtils.wrap(this.expression, this.expressionItems, ApplyTypeExpressionItem.class);
        }
        return this.expressionItems;
    }

    public void setExpressionItems(List<ApplyTypeExpressionItem> value) {
        this.expression = null;
        this.expressionItems = null;
        this.expressionItems = value;
        if (this.expressionItems == null) {
            this.expressionItems = new ArrayList<ApplyTypeExpressionItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.expression)) {
            this.expression = ItemUtils.wrap(this.expression, this.expressionItems, ApplyTypeExpressionItem.class);
        }
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof ApplyType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final ApplyType that = ((ApplyType) object);
        equalsBuilder.append(this.getExpression(), that.getExpression());
        equalsBuilder.append(this.getFunctionId(), that.getFunctionId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ApplyType)) {
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
        hashCodeBuilder.append(this.getExpression());
        hashCodeBuilder.append(this.getFunctionId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            List<JAXBElement<?>> theExpression;
            theExpression = this.getExpression();
            toStringBuilder.append("expression", theExpression);
        }
        {
            String theFunctionId;
            theFunctionId = this.getFunctionId();
            toStringBuilder.append("functionId", theFunctionId);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}