package org.riverock.module.factory;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionConfigurationBean;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:53:55
 *         $Id$
 */
public interface ActionFactory {
    public ActionConfigurationBean getAction(String actionName);
    public ActionConfigurationBean getDefaultAction();
    public void destroy();
    public String doAction(ModuleActionRequest moduleActionRequest) throws ActionException;
}
