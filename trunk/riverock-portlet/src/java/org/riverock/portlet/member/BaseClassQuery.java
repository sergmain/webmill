/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.member;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:06:57 PM
 *
 * $Id$
 */
public abstract class BaseClassQuery {
/**
 * Return current value for output on web page
 * @return String
 */
    public abstract String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle ) throws Exception;

/**
 * Return list of possible values for create 'select' html-element
 * @return List of org.riverock.member.ClassQueryItem
 */
    public abstract List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle ) throws Exception;

    public abstract void setQueryParameter(MemberQueryParameter parameter) throws Exception;
}
