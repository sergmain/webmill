/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
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
