/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2005, Riverock Software, All Rights Reserved.
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

/*
 * Copyright 2003,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* 

 */
package org.riverock.webmill.container.tags;

import javax.portlet.PortletURL;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * Supporting class for the <CODE>param</CODE> tag.
 * defines a parameter that can be added to a <CODE>actionURL</CODE> or
 * a <CODE>renderURL</CODE>
 * <BR>The following attributes are mandatory
 * <UL>
 * <LI><CODE>name</CODE>
 * <LI><CODE>value</CODE>
 * </UL>
 */
public class ParamTag extends TagSupport {

    private String name;
    private String value;

    /**
     * Processes the <CODE>param</CODE> tag.
     *
     * @return <CODE>SKIP_BODY</CODE>
     */
    public int doStartTag() throws JspException {
        BasicURLTag urlTag = (BasicURLTag) findAncestorWithClass(this, BasicURLTag.class);
        if (urlTag == null) {
            throw new JspException("the 'param' Tag must have actionURL or renderURL as a parent");
        }
        PortletURL url = urlTag.getUrl();

        if (getName() != null) {
            url.setParameter(getName(), getValue());
        }

        return SKIP_BODY;
    }

    /**
     * Returns the name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value.
     *
     * @return String
     */
    public String getValue() {
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * Sets the name.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value.
     *
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
