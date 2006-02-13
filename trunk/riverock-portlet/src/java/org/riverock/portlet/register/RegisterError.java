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
package org.riverock.portlet.register;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.portlet.portlets.PortletErrors;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 20:28:28
 *         $Id$
 */
public class RegisterError {
    public static String notLoggedError(ModuleActionRequest actionBean) {
        return PortletErrors.error(actionBean, RegisterConstants.NOT_LOGGED);
    }

    public static String emailIsEmpty(ModuleActionRequest actionBean) {
        return PortletErrors.error(actionBean, RegisterConstants.EMAIL_IS_EMPTY);
    }

    public static String noSuchEmail( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.NO_SUCH_EMAIL);
    }

    public static String systemError( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.SYSTEM_ERROR);
    }

    public static String roleIsNull( ModuleActionRequest actionBean ) {
        return PortletErrors.error(actionBean, RegisterConstants.ROLE_IS_NULL);
    }
}
