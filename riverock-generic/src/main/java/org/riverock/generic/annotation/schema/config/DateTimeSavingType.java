/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.11.23 at 09:47:05 PM MSK 
//


package org.riverock.generic.annotation.schema.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * SimpleTimeZone is a concrete subclass of TimeZone that represents a time zone for use with a Gregorian calendar. The class holds an offset from GMT, called raw offset, and start and end rules for a daylight saving time schedule. Since it only holds single values for each, it cannot handle historical changes in the offset from GMT and the daylight saving schedule, except that the setStartYear method can specify the year when the daylight saving time schedule starts in effect. 
 * 
 * To construct a SimpleTimeZone with a daylight saving time schedule, the schedule can be described with a set of rules, start-rule and end-rule. A day when daylight saving time starts or ends is specified by a combination of month, day-of-month, and day-of-week values. The month value is represented by a Calendar MONTH field value, such as Calendar.MARCH. The day-of-week value is represented by a Calendar DAY_OF_WEEK value, such as SUNDAY. The meanings of value combinations are as follows. 
 * 
 * Exact day of month
 * To specify an exact day of month, set the month and day-of-month to an exact value, and day-of-week to zero. For example, to specify March 1, set the month to MARCH, day-of-month to 1, and day-of-week to 0. 
 * Day of week on or after day of month
 * To specify a day of week on or after an exact day of month, set the month to an exact month value, day-of-month to the day on or after which the rule is applied, and day-of-week to a DAY_OF_WEEK field value. For example, to specify the second Sunday of April, set month to APRIL, day-of-month to 8, and day-of-week to SUNDAY. 
 * Day of week on or before day of month
 * To specify a day of the week on or before an exact day of the month, set day-of-month and day-of-week to a negative value. For example, to specify the last Wednesday on or before the 21st of March, set month to MARCH, day-of-month is -21 and day-of-week is -WEDNESDAY. 
 * Last day-of-week of month
 * To specify, the last day-of-week of the month, set day-of-week to a DAY_OF_WEEK value and day-of-month to -1. For example, to specify the last Sunday of October, set month to OCTOBER, day-of-week to SUNDAY and day-of-month to -1. 
 * The time of the day at which daylight saving time starts or ends is specified by a millisecond value within the day. There are three kinds of modes to specify the time: WALL_TIME, STANDARD_TIME and UTC_TIME. For example, if daylight saving time ends at 2:00 am in the wall clock time, it can be specified by 7200000 milliseconds in the WALL_TIME mode. In this case, the wall clock time for an end-rule means the same thing as the daylight time. 
 * The following are examples of parameters for constructing time zone objects. 
 * 
 * 
 *       // Base GMT offset: -8:00
 *       // DST starts:      at 2:00am in standard time
 *       //                  on the first Sunday in April
 *       // DST ends:        at 2:00am in daylight time
 *       //                  on the last Sunday in October
 *       // Save:            1 hour
 *       SimpleTimeZone(-28800000,
 *                      "America/Los_Angeles",
 *                      Calendar.APRIL, 1, -Calendar.SUNDAY,
 *                      7200000,
 *                      Calendar.OCTOBER, -1, Calendar.SUNDAY,
 *                      7200000,
 *                      3600000)
 * 
 *       // Base GMT offset: +1:00
 *       // DST starts:      at 1:00am in UTC time
 *       //                  on the last Sunday in March
 *       // DST ends:        at 1:00am in UTC time
 *       //                  on the last Sunday in October
 *       // Save:            1 hour
 *       SimpleTimeZone(3600000,
 *                      "Europe/Paris",
 *                      Calendar.MARCH, -1, Calendar.SUNDAY,
 *                      3600000, SimpleTimeZone.UTC_TIME,
 *                      Calendar.OCTOBER, -1, Calendar.SUNDAY,
 *                      3600000, SimpleTimeZone.UTC_TIME,
 *                      3600000)
 *  These parameter rules are also applicable to the set rule methods, such as setStartRule. 
 * 
 * <p>Java class for DateTimeSavingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DateTimeSavingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="RawOffset" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="Start" type="{}DateTimeSavingAttrType"/>
 *           &lt;element name="End" type="{}DateTimeSavingAttrType"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="TimeZoneName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateTimeSavingType", propOrder = {
    "rawOffset",
    "id",
    "start",
    "end",
    "timeZoneName"
})
public class DateTimeSavingType {

    @XmlElement(name = "RawOffset")
    protected Integer rawOffset;
    @XmlElement(name = "Id")
    protected String id;
    @XmlElement(name = "Start")
    protected DateTimeSavingAttrType start;
    @XmlElement(name = "End")
    protected DateTimeSavingAttrType end;
    @XmlElement(name = "TimeZoneName")
    protected String timeZoneName;

    /**
     * Gets the value of the rawOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRawOffset() {
        return rawOffset;
    }

    /**
     * Sets the value of the rawOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRawOffset(Integer value) {
        this.rawOffset = value;
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
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeSavingAttrType }
     *     
     */
    public DateTimeSavingAttrType getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeSavingAttrType }
     *     
     */
    public void setStart(DateTimeSavingAttrType value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link DateTimeSavingAttrType }
     *     
     */
    public DateTimeSavingAttrType getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTimeSavingAttrType }
     *     
     */
    public void setEnd(DateTimeSavingAttrType value) {
        this.end = value;
    }

    /**
     * Gets the value of the timeZoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZoneName() {
        return timeZoneName;
    }

    /**
     * Sets the value of the timeZoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZoneName(String value) {
        this.timeZoneName = value;
    }

}