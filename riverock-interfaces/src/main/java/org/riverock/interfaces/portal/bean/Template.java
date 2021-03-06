/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.interfaces.portal.bean;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 23:41:35
 *         $Id: Template.java 1314 2007-08-18 13:47:36Z serg_main $
 */
public interface Template extends Serializable {
    public String getTemplateLanguage();
    public Long getTemplateId();
    public Long getSiteLanguageId();
    public String getTemplateName();
    public String getTemplateData();
    public boolean isDefaultDynamic();
    public boolean isMaximizedTemplate();
    public boolean isPopupTemplate();
    int getVersion();
    String getRoles();
}
