package org.riverock.portlet.auth;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.portlet.portlets.PortletErrors;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 15:39:24
 *         $Id$
 */
public class AuthError {
    public static String notLoggedError(ModuleActionRequest actionBean) {
        return PortletErrors.error(actionBean, AuthConstants.NOT_LOGGED);
    }

}
