package org.riverock.module.action;

import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionBean;

/**
 * Action interface
 */
public interface Action {

    public static final String DEFAULT_FORWARD_ACCESS_DENIED = "access-denied";
    public static final String DEFAULT_FORWARD_NOT_LOGGED = "not-logged";
    public static final String DEFAULT_FORWARD_ERROR = "error";

    /**
     * execute a certain function
     *
     * @param moduleActionRequest   ModuleActionRequest
     * @return forward page
     * @throws org.riverock.module.exception.ActionException
     *          ActionException
     */
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException;
}