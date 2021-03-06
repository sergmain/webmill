//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.4-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.25 at 01:13:55 AM MSK 
//


package org.riverock.portlet.job.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JobItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JobItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TextJob" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ageFrom" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="ageFromString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ageString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ageTill" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="ageTillString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="city" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cityString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="contactPerson" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="contactPersonString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dateEnd" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dateEndString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="datePost" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="datePostString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="education" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="educationString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="gender" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="genderString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="jobName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="jobNameString" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="salary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="salaryString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="testPeriod" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="testPeriodString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="textJobString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JobItemType", propOrder = {
    "textJob"
})
public class JobItemType {

    @XmlElement(name = "TextJob")
    protected String textJob;
    @XmlAttribute
    protected Integer ageFrom;
    @XmlAttribute
    protected String ageFromString;
    @XmlAttribute
    protected String ageString;
    @XmlAttribute
    protected Integer ageTill;
    @XmlAttribute
    protected String ageTillString;
    @XmlAttribute
    protected String city;
    @XmlAttribute
    protected String cityString;
    @XmlAttribute
    protected String contactPerson;
    @XmlAttribute
    protected String contactPersonString;
    @XmlAttribute
    protected String dateEnd;
    @XmlAttribute
    protected String dateEndString;
    @XmlAttribute
    protected String datePost;
    @XmlAttribute
    protected String datePostString;
    @XmlAttribute
    protected String education;
    @XmlAttribute
    protected String educationString;
    @XmlAttribute
    protected String gender;
    @XmlAttribute
    protected String genderString;
    @XmlAttribute
    protected String jobName;
    @XmlAttribute(required = true)
    protected String jobNameString;
    @XmlAttribute
    protected String salary;
    @XmlAttribute
    protected String salaryString;
    @XmlAttribute
    protected String testPeriod;
    @XmlAttribute
    protected String testPeriodString;
    @XmlAttribute
    protected String textJobString;
    @XmlAttribute
    protected String url;

    /**
     * Gets the value of the textJob property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextJob() {
        return textJob;
    }

    /**
     * Sets the value of the textJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextJob(String value) {
        this.textJob = value;
    }

    /**
     * Gets the value of the ageFrom property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgeFrom() {
        return ageFrom;
    }

    /**
     * Sets the value of the ageFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgeFrom(Integer value) {
        this.ageFrom = value;
    }

    /**
     * Gets the value of the ageFromString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeFromString() {
        return ageFromString;
    }

    /**
     * Sets the value of the ageFromString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeFromString(String value) {
        this.ageFromString = value;
    }

    /**
     * Gets the value of the ageString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeString() {
        return ageString;
    }

    /**
     * Sets the value of the ageString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeString(String value) {
        this.ageString = value;
    }

    /**
     * Gets the value of the ageTill property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgeTill() {
        return ageTill;
    }

    /**
     * Sets the value of the ageTill property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgeTill(Integer value) {
        this.ageTill = value;
    }

    /**
     * Gets the value of the ageTillString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeTillString() {
        return ageTillString;
    }

    /**
     * Sets the value of the ageTillString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeTillString(String value) {
        this.ageTillString = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the cityString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityString() {
        return cityString;
    }

    /**
     * Sets the value of the cityString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityString(String value) {
        this.cityString = value;
    }

    /**
     * Gets the value of the contactPerson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the value of the contactPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPerson(String value) {
        this.contactPerson = value;
    }

    /**
     * Gets the value of the contactPersonString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPersonString() {
        return contactPersonString;
    }

    /**
     * Sets the value of the contactPersonString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPersonString(String value) {
        this.contactPersonString = value;
    }

    /**
     * Gets the value of the dateEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateEnd() {
        return dateEnd;
    }

    /**
     * Sets the value of the dateEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateEnd(String value) {
        this.dateEnd = value;
    }

    /**
     * Gets the value of the dateEndString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateEndString() {
        return dateEndString;
    }

    /**
     * Sets the value of the dateEndString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateEndString(String value) {
        this.dateEndString = value;
    }

    /**
     * Gets the value of the datePost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePost() {
        return datePost;
    }

    /**
     * Sets the value of the datePost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePost(String value) {
        this.datePost = value;
    }

    /**
     * Gets the value of the datePostString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePostString() {
        return datePostString;
    }

    /**
     * Sets the value of the datePostString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePostString(String value) {
        this.datePostString = value;
    }

    /**
     * Gets the value of the education property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducation() {
        return education;
    }

    /**
     * Sets the value of the education property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducation(String value) {
        this.education = value;
    }

    /**
     * Gets the value of the educationString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducationString() {
        return educationString;
    }

    /**
     * Sets the value of the educationString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationString(String value) {
        this.educationString = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the genderString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenderString() {
        return genderString;
    }

    /**
     * Sets the value of the genderString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenderString(String value) {
        this.genderString = value;
    }

    /**
     * Gets the value of the jobName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the value of the jobName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobName(String value) {
        this.jobName = value;
    }

    /**
     * Gets the value of the jobNameString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobNameString() {
        return jobNameString;
    }

    /**
     * Sets the value of the jobNameString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobNameString(String value) {
        this.jobNameString = value;
    }

    /**
     * Gets the value of the salary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalary() {
        return salary;
    }

    /**
     * Sets the value of the salary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalary(String value) {
        this.salary = value;
    }

    /**
     * Gets the value of the salaryString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalaryString() {
        return salaryString;
    }

    /**
     * Sets the value of the salaryString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalaryString(String value) {
        this.salaryString = value;
    }

    /**
     * Gets the value of the testPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestPeriod() {
        return testPeriod;
    }

    /**
     * Sets the value of the testPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestPeriod(String value) {
        this.testPeriod = value;
    }

    /**
     * Gets the value of the testPeriodString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTestPeriodString() {
        return testPeriodString;
    }

    /**
     * Sets the value of the testPeriodString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTestPeriodString(String value) {
        this.testPeriodString = value;
    }

    /**
     * Gets the value of the textJobString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextJobString() {
        return textJobString;
    }

    /**
     * Sets the value of the textJobString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextJobString(String value) {
        this.textJobString = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
