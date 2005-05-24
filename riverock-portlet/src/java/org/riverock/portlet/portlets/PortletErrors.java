package org.riverock.portlet.portlets;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.ActionMessage;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.portlet.main.Constants;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 15:15:40
 *         $Id$
 */
public class PortletErrors {
    private static final Log log = LogFactory.getLog(PortletErrors.class);

    public static final String SYSTEM_ERROR = "system_error";

    public static String systemError(ModuleRequest request, ResourceBundle resourceBundle) {
        return error(request, resourceBundle, SYSTEM_ERROR);
    }

    public static String error(ModuleActionRequest actionBean, String errorCode) {
        return error(actionBean.getRequest(), actionBean.getResourceBundle(), errorCode);
    }

    public static String error(ModuleRequest request, ResourceBundle resourceBundle, String errorCode) {
        // hack to output where exception is created
        try {
            getException();
        }
        catch (Exception e) {
            log.error("error", e);
        }
        request.setAttribute(
            Constants.ACTION_MESSAGE,
            new ActionMessage(resourceBundle, errorCode)
        );
        return Constants.ERROR_EXECUTE_STATUS;
    }

    private static void getException() throws Exception {
        throw new Exception();
    }
}
