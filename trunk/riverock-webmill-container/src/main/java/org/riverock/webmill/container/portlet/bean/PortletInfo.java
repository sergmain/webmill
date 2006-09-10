/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.container.portlet.bean;

import java.util.*;
import java.io.Serializable;

/**
 * Class PortletInfoType.
 *
 * @version $Revision$ $Date$
 */
public class PortletInfo implements Serializable {
    private static final long serialVersionUID = 30434672384237157L;


    /**
     * Field id
     */
    private java.lang.String id;

    /**
     * Field title
     */
    private String title;

    /**
     * Field shortTitle
     */
    private String shortTitle;

    /**
     * Field _keywords
     */
//    private String _keywords;
    private List<String> keywords;


    public PortletInfo() {
        super();
    }


    /**
     * Returns the value of field 'id'.
     *
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * Returns the value of field 'keywords'.
     *
     * @return the value of field 'keywords'.
     */
    public List<String> getKeywords() {
        if (keywords == null) {
            setKeywords(new ArrayList<String>());
        }

        return keywords;
    }

    /**
     * Returns the value of field 'shortTitle'.
     *
     * @return the value of field 'shortTitle'.
     */
    public String getShortTitle() {
        return this.shortTitle;
    }

    /**
     * Returns the value of field 'title'.
     *
     * @return the value of field 'title'.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the value of field 'id'.
     *
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Sets the value of field 'keywords'.
     *
     * @param keywords the value of field 'keywords'.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * <p/>
     * setKeywords
     * </p>
     * <p/>
     * A comma delimited list of keywords
     *
     * @param keywordStr String
     */
    public void setKeywords(String keywordStr) {
        if (keywords == null) {
            keywords = new ArrayList<String>();
        }
        StringTokenizer tok = new StringTokenizer(keywordStr, ",");
        while (tok.hasMoreTokens()) {
            keywords.add(tok.nextToken());
        }
    }

    /**
     * Sets the value of field 'shortTitle'.
     *
     * @param shortTitle the value of field 'shortTitle'.
     */
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    /**
     * Sets the value of field 'title'.
     *
     * @param title the value of field 'title'.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
