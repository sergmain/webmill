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
package org.riverock.webmill.portal.template;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 01.01.2006
 *         Time: 9:12:11
 *         $Id: PortalTemplateManager.java 1375 2007-08-28 19:35:44Z serg_main $
 */
public interface PortalTemplateManager extends Serializable {
    public PortalTemplate getTemplate( final String templateName, final String localeName );
    public PortalTemplate getTemplate( final long templateId );

    void destroy();

    void destroy(Long templateId);
}
