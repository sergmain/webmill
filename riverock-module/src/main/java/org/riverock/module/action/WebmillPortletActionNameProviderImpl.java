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
package org.riverock.module.action;

import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.WebmillPortletConstants;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:26:15
 *         $Id$
 */
public class WebmillPortletActionNameProviderImpl implements ActionNameProvider {

    private ModuleRequest request = null;
    public WebmillPortletActionNameProviderImpl(ModuleRequest request) {
        this.request = request;
    }

    public String getActionName() {
        return request.getParameter( WebmillPortletConstants.ACTION_NAME_PARAM );
    }
}
