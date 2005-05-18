package org.riverock.forum.action;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.util.Constants;
import org.riverock.module.exception.ActionException;

public class HelpAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        return Constants.OK_EXECUTE_STATUS;
    }
}