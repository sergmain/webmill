//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.08.13 at 09:28:43 PM MSD 
//


package org.riverock.webmill.template.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlMixed;
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
 *         &lt;element ref="{}table"/>
 *         &lt;element ref="{}br"/>
 *         &lt;element ref="{}span"/>
 *         &lt;element ref="{}bdo"/>
 *         &lt;element ref="{}map"/>
 *         &lt;element ref="{}object"/>
 *         &lt;element ref="{}img"/>
 *         &lt;element ref="{}tt"/>
 *         &lt;element ref="{}i"/>
 *         &lt;element ref="{}b"/>
 *         &lt;element ref="{}big"/>
 *         &lt;element ref="{}small"/>
 *         &lt;element ref="{}em"/>
 *         &lt;element ref="{}strong"/>
 *         &lt;element ref="{}dfn"/>
 *         &lt;element ref="{}code"/>
 *         &lt;element ref="{}q"/>
 *         &lt;element ref="{}samp"/>
 *         &lt;element ref="{}kbd"/>
 *         &lt;element ref="{}var"/>
 *         &lt;element ref="{}cite"/>
 *         &lt;element ref="{}abbr"/>
 *         &lt;element ref="{}acronym"/>
 *         &lt;element ref="{}sub"/>
 *         &lt;element ref="{}sup"/>
 *         &lt;element ref="{}noscript"/>
 *         &lt;element ref="{}ins"/>
 *         &lt;element ref="{}del"/>
 *         &lt;element ref="{}script"/>
 *       &lt;/choice>
 *       &lt;attribute name="accesskey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dir">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="ltr"/>
 *             &lt;enumeration value="rtl"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="disabled">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="disabled"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onblur" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onclick" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ondblclick" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onfocus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeydown" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeypress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onkeyup" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmousedown" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmousemove" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseover" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="onmouseup" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tabindex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" default="submit">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="submit"/>
 *             &lt;enumeration value="reset"/>
 *             &lt;enumeration value="button"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "content"
})
@XmlRootElement(name = "button")
public class Button {

    @XmlElementRefs({
        @XmlElementRef(name = "del", type = Del.class),
        @XmlElementRef(name = "tt", type = Tt.class),
        @XmlElementRef(name = "h5", type = H5 .class),
        @XmlElementRef(name = "br", type = Br.class),
        @XmlElementRef(name = "sup", type = Sup.class),
        @XmlElementRef(name = "b", type = B.class),
        @XmlElementRef(name = "dl", type = Dl.class),
        @XmlElementRef(name = "ins", type = Ins.class),
        @XmlElementRef(name = "div", type = Div.class),
        @XmlElementRef(name = "big", type = Big.class),
        @XmlElementRef(name = "i", type = I.class),
        @XmlElementRef(name = "acronym", type = Acronym.class),
        @XmlElementRef(name = "map", type = Map.class),
        @XmlElementRef(name = "strong", type = Strong.class),
        @XmlElementRef(name = "cite", type = Cite.class),
        @XmlElementRef(name = "ol", type = Ol.class),
        @XmlElementRef(name = "table", type = Table.class),
        @XmlElementRef(name = "h4", type = H4 .class),
        @XmlElementRef(name = "dfn", type = Dfn.class),
        @XmlElementRef(name = "pre", type = Pre.class),
        @XmlElementRef(name = "var", type = Var.class),
        @XmlElementRef(name = "hr", type = Hr.class),
        @XmlElementRef(name = "p", type = P.class),
        @XmlElementRef(name = "blockquote", type = Blockquote.class),
        @XmlElementRef(name = "h6", type = H6 .class),
        @XmlElementRef(name = "script", type = Script.class),
        @XmlElementRef(name = "em", type = Em.class),
        @XmlElementRef(name = "q", type = Q.class),
        @XmlElementRef(name = "bdo", type = Bdo.class),
        @XmlElementRef(name = "abbr", type = Abbr.class),
        @XmlElementRef(name = "address", type = Address.class),
        @XmlElementRef(name = "h1", type = H1 .class),
        @XmlElementRef(name = "samp", type = Samp.class),
        @XmlElementRef(name = "span", type = Span.class),
        @XmlElementRef(name = "kbd", type = Kbd.class),
        @XmlElementRef(name = "img", type = Img.class),
        @XmlElementRef(name = "noscript", type = Noscript.class),
        @XmlElementRef(name = "h3", type = H3 .class),
        @XmlElementRef(name = "object", type = org.riverock.webmill.template.schema.Object.class),
        @XmlElementRef(name = "small", type = Small.class),
        @XmlElementRef(name = "ul", type = Ul.class),
        @XmlElementRef(name = "code", type = Code.class),
        @XmlElementRef(name = "sub", type = Sub.class),
        @XmlElementRef(name = "h2", type = H2 .class)
    })
    @XmlMixed
    protected List<java.lang.Object> content;
    @XmlAttribute
    protected String accesskey;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String dir;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String disabled;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "lang")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String oldLang;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String onblur;
    @XmlAttribute
    protected String onclick;
    @XmlAttribute
    protected String ondblclick;
    @XmlAttribute
    protected String onfocus;
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
    protected String tabindex;
    @XmlAttribute
    protected String title;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute
    protected String value;
    @XmlAttribute(namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String lang;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link Del }
     * {@link Tt }
     * {@link H5 }
     * {@link Br }
     * {@link Sup }
     * {@link B }
     * {@link Dl }
     * {@link Ins }
     * {@link Div }
     * {@link I }
     * {@link Big }
     * {@link Strong }
     * {@link Map }
     * {@link Acronym }
     * {@link Table }
     * {@link Ol }
     * {@link Cite }
     * {@link H4 }
     * {@link Dfn }
     * {@link Var }
     * {@link Pre }
     * {@link Hr }
     * {@link P }
     * {@link H6 }
     * {@link Blockquote }
     * {@link Script }
     * {@link Em }
     * {@link Q }
     * {@link Abbr }
     * {@link Bdo }
     * {@link Address }
     * {@link H1 }
     * {@link Samp }
     * {@link Noscript }
     * {@link Img }
     * {@link Kbd }
     * {@link Span }
     * {@link H3 }
     * {@link Ul }
     * {@link Small }
     * {@link org.riverock.webmill.template.schema.Object }
     * {@link Sub }
     * {@link Code }
     * {@link H2 }
     * 
     * 
     */
    public List<java.lang.Object> getContent() {
        if (content == null) {
            content = new ArrayList<java.lang.Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the accesskey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccesskey() {
        return accesskey;
    }

    /**
     * Sets the value of the accesskey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccesskey(String value) {
        this.accesskey = value;
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
     * Gets the value of the disabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisabled() {
        return disabled;
    }

    /**
     * Sets the value of the disabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisabled(String value) {
        this.disabled = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the onblur property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnblur() {
        return onblur;
    }

    /**
     * Sets the value of the onblur property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnblur(String value) {
        this.onblur = value;
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
     * Gets the value of the onfocus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnfocus() {
        return onfocus;
    }

    /**
     * Sets the value of the onfocus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnfocus(String value) {
        this.onfocus = value;
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
     * Gets the value of the tabindex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTabindex() {
        return tabindex;
    }

    /**
     * Sets the value of the tabindex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTabindex(String value) {
        this.tabindex = value;
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "submit";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
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
