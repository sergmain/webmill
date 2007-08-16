//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.16 at 11:04:13 PM MSD 
//


package org.riverock.update.webmill.v581.convert_template.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}p"/>
 *         &lt;element ref="{}h1"/>
 *         &lt;element ref="{}h2"/>
 *         &lt;element ref="{}h3"/>
 *         &lt;element ref="{}h4"/>
 *         &lt;element ref="{}h5"/>
 *         &lt;element ref="{}h6"/>
 *         &lt;element ref="{}div"/>
 *         &lt;element ref="{}ul"/>
 *         &lt;element ref="{}ol"/>
 *         &lt;element ref="{}dl"/>
 *         &lt;element ref="{}pre"/>
 *         &lt;element ref="{}hr"/>
 *         &lt;element ref="{}blockquote"/>
 *         &lt;element ref="{}address"/>
 *         &lt;element ref="{}fieldset"/>
 *         &lt;element ref="{}table"/>
 *         &lt;element ref="{}form"/>
 *         &lt;element ref="{}noscript"/>
 *         &lt;element ref="{}ins"/>
 *         &lt;element ref="{}del"/>
 *         &lt;element ref="{}script"/>
 *       &lt;/choice>
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dir">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="ltr"/>
 *             &lt;enumeration value="rtl"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="onclick" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ondblclick" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeydown" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeypress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeyup" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmousedown" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmousemove" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseover" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseup" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pOrH1OrH2"
})
@XmlRootElement(name = "noscript")
public class Noscript {

    @XmlElements({
        @XmlElement(name = "h2", type = H2 .class),
        @XmlElement(name = "noscript", type = Noscript.class),
        @XmlElement(name = "h3", type = H3 .class),
        @XmlElement(name = "ul", type = Ul.class),
        @XmlElement(name = "ins", type = Ins.class),
        @XmlElement(name = "h1", type = H1 .class),
        @XmlElement(name = "address", type = Address.class),
        @XmlElement(name = "ol", type = Ol.class),
        @XmlElement(name = "hr", type = Hr.class),
        @XmlElement(name = "script", type = Script.class),
        @XmlElement(name = "del", type = Del.class),
        @XmlElement(name = "h4", type = H4 .class),
        @XmlElement(name = "pre", type = Pre.class),
        @XmlElement(name = "fieldset", type = Fieldset.class),
        @XmlElement(name = "p", type = P.class),
        @XmlElement(name = "form", type = Form.class),
        @XmlElement(name = "div", type = Div.class),
        @XmlElement(name = "h6", type = H6 .class),
        @XmlElement(name = "h5", type = H5 .class),
        @XmlElement(name = "blockquote", type = Blockquote.class),
        @XmlElement(name = "table", type = Table.class),
        @XmlElement(name = "dl", type = Dl.class)
    })
    protected List<java.lang.Object> pOrH1OrH2;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dir;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "lang")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String oldLang;
    @XmlAttribute
    protected String onclick;
    @XmlAttribute
    protected String ondblclick;
    @XmlAttribute
    protected String onkeydown;
    @XmlAttribute
    protected String onkeypress;
    @XmlAttribute
    protected String onkeyup;
    @XmlAttribute
    protected String onmousedown;
    @XmlAttribute
    protected String onmousemove;
    @XmlAttribute
    protected String onmouseout;
    @XmlAttribute
    protected String onmouseover;
    @XmlAttribute
    protected String onmouseup;
    @XmlAttribute
    protected String style;
    @XmlAttribute
    protected String title;
    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String lang;

    /**
     * Gets the value of the pOrH1OrH2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pOrH1OrH2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPOrH1OrH2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link H2 }
     * {@link Noscript }
     * {@link H3 }
     * {@link Ul }
     * {@link Ins }
     * {@link H1 }
     * {@link Address }
     * {@link Ol }
     * {@link Hr }
     * {@link Script }
     * {@link Del }
     * {@link H4 }
     * {@link Pre }
     * {@link Fieldset }
     * {@link P }
     * {@link Form }
     * {@link Div }
     * {@link H6 }
     * {@link H5 }
     * {@link Blockquote }
     * {@link Table }
     * {@link Dl }
     * 
     * 
     */
    public List<java.lang.Object> getPOrH1OrH2() {
        if (pOrH1OrH2 == null) {
            pOrH1OrH2 = new ArrayList<java.lang.Object>();
        }
        return this.pOrH1OrH2;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the dir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir() {
        return dir;
    }

    /**
     * Sets the value of the dir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir(String value) {
        this.dir = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
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
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the oldLang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldLang() {
        return oldLang;
    }

    /**
     * Sets the value of the oldLang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldLang(String value) {
        this.oldLang = value;
    }

    /**
     * Gets the value of the onclick property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnclick() {
        return onclick;
    }

    /**
     * Sets the value of the onclick property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnclick(String value) {
        this.onclick = value;
    }

    /**
     * Gets the value of the ondblclick property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOndblclick() {
        return ondblclick;
    }

    /**
     * Sets the value of the ondblclick property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOndblclick(String value) {
        this.ondblclick = value;
    }

    /**
     * Gets the value of the onkeydown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnkeydown() {
        return onkeydown;
    }

    /**
     * Sets the value of the onkeydown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnkeydown(String value) {
        this.onkeydown = value;
    }

    /**
     * Gets the value of the onkeypress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnkeypress() {
        return onkeypress;
    }

    /**
     * Sets the value of the onkeypress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnkeypress(String value) {
        this.onkeypress = value;
    }

    /**
     * Gets the value of the onkeyup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnkeyup() {
        return onkeyup;
    }

    /**
     * Sets the value of the onkeyup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnkeyup(String value) {
        this.onkeyup = value;
    }

    /**
     * Gets the value of the onmousedown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnmousedown() {
        return onmousedown;
    }

    /**
     * Sets the value of the onmousedown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnmousedown(String value) {
        this.onmousedown = value;
    }

    /**
     * Gets the value of the onmousemove property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnmousemove() {
        return onmousemove;
    }

    /**
     * Sets the value of the onmousemove property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnmousemove(String value) {
        this.onmousemove = value;
    }

    /**
     * Gets the value of the onmouseout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnmouseout() {
        return onmouseout;
    }

    /**
     * Sets the value of the onmouseout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnmouseout(String value) {
        this.onmouseout = value;
    }

    /**
     * Gets the value of the onmouseover property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnmouseover() {
        return onmouseover;
    }

    /**
     * Sets the value of the onmouseover property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnmouseover(String value) {
        this.onmouseover = value;
    }

    /**
     * Gets the value of the onmouseup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnmouseup() {
        return onmouseup;
    }

    /**
     * Sets the value of the onmouseup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnmouseup(String value) {
        this.onmouseup = value;
    }

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyle(String value) {
        this.style = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

}
