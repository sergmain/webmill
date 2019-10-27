/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.module.web.response;

import javax.portlet.PortletResponse;
import java.io.IOException;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:30
 *         $Id: ModuleResponse.java 1044 2006-11-14 14:36:43Z serg_main $
 */
public interface ModuleResponse {
    public Object getOriginResponse();
    public void sendRedirect(String newUrl) throws IOException;
}
