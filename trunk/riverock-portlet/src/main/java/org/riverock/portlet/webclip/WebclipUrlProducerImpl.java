/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.webclip;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 20:40:49
 * <p/>
 * $Id$
 */
public class WebclipUrlProducerImpl implements WebclipUrlProducer {

    private String newHrefPrefix=null;
    private String hrefStartPart=null;
    private String href;

    public WebclipUrlProducerImpl(String newHrefPrefix, String hrefStartPart) {
        this.newHrefPrefix = newHrefPrefix;
        this.hrefStartPart = hrefStartPart;
    }

    /**
     * set current href
     *
     * @param value current href
     */
    public void setCurrentHrefValue(String value) {
        this.href=value;
    }

    /**
     * set new parameter
     *
     * @param name  String paramete name r
     * @param value String parameter value
     */
    public void setParameter(String name, String value) {
    }

    /**
     * get new url
     *
     * @return String
     */
    public String getUrl() {
        if (href==null) {
            return "";
        }

        if (hrefStartPart!=null && newHrefPrefix!=null && href.startsWith(hrefStartPart)) {
            return newHrefPrefix + href.substring(hrefStartPart.length());
        }
        return href;
    }

    /**
     * init mwethod
     */
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
