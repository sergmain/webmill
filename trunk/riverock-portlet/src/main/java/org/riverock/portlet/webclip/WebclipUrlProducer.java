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
 * @author Sergei Maslyukov
 *         Date: 11.09.2006
 *         Time: 22:07:04
 *         <p/>
 *         $Id$
 */
public interface WebclipUrlProducer {
    
    /**
     * set current href
     *
     * @param value current href
     */
    void setCurrentHrefValue(String value);

    /**
     * set new parameter
     *
     * @param name  String paramete name r
     * @param value String parameter value
     */
    void setParameter(String name, String value);

    /**
     * get new url
     *
     * @return String
     */
    String getUrl();

    /**
     * init mwethod
     */
    void init();
}
