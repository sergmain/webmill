package org.riverock.portlet.auth.action;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.action.Action;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.portlet.auth.dao.AuthDAOFactory;
import org.riverock.portlet.auth.dao.BindIndexDAO;
import org.riverock.portlet.auth.bean.BindUserListBean;
import org.riverock.portlet.main.Constants;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:51:00
 *         $Id$
 */
public class BindDeleteAction implements Action {

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {
        AuthDAOFactory daof = AuthDAOFactory.getDAOFactory();

        ModuleUser auth = moduleActionRequest.getRequest().getUser();

        BindIndexDAO indexDAO = daof.getBindIndexDAO();
        BindUserListBean users = indexDAO.execute(
            moduleActionRequest, auth
        );

        moduleActionRequest.getRequest().setAttribute("bindBean", users);
        return Constants.OK_EXECUTE_STATUS;
    }
}
