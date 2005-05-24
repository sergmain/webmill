package org.riverock.portlet.auth.action;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.portlet.main.Constants;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:18:38
 *         $Id$
 */
public class HelpAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        return Constants.OK_EXECUTE_STATUS;
    }
}