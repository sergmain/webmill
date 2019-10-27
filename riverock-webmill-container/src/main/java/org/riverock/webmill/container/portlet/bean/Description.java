/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.container.portlet.bean;

import java.io.Serializable;

/**
 * The description element is used to provide text describing the
 * parent element. The description element should include any
 * information that the portlet application war file producer
 * wants
 * to provide to the consumer of the portlet application war file
 * (i.e., to the Deployer). Typically, the tools used by the
 * portlet application war file consumer will display the
 * description when processing the parent element that contains
 * the
 * description. It has an optional attribute xml:lang to indicate
 * which language is used in the description according to
 * RFC 1766 (http://www.ietf.org/rfc/rfc1766.txt). The default
 * value of this attribute is English(“en”).
 * Used in: init-param, portlet, portlet-app, security-role
 *
 * @version $Revision: 1055 $ $Date: 2006-11-14 17:56:15 +0000 (Tue, 14 Nov 2006) $
 */
public class Description implements Serializable {
    private static final long serialVersionUID = 30434672384237139L;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * In due course, we should install the relevant ISO 2- and
     * 3-letter
     * codes as the enumerated possible values . . .
     */
    private java.lang.String _lang;


    public Description() {
        super();
        setContent("");
    }


    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     *
     * @return the value of field 'content'.
     */
    public java.lang.String getContent() {
        return this._content;
    }

    /**
     * Returns the value of field 'lang'. The field 'lang' has the
     * following description: In due course, we should install the
     * relevant ISO 2- and 3-letter
     * codes as the enumerated possible values . . .
     *
     * @return the value of field 'lang'.
     */
    public java.lang.String getLang() {
        return this._lang;
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     *
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'lang'. The field 'lang' has the
     * following description: In due course, we should install the
     * relevant ISO 2- and 3-letter
     * codes as the enumerated possible values . . .
     *
     * @param lang the value of field 'lang'.
     */
    public void setLang(java.lang.String lang) {
        this._lang = lang;
    }
}
