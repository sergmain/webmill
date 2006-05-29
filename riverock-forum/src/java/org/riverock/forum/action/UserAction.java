package org.riverock.forum.action;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.bean.UserBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.UserDAO;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;

public class UserAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Integer u_id = forumActionBean.getRequest().getInt( "u_id");

        DAOFactory daof = DAOFactory.getDAOFactory();
        UserDAO userDAO = daof.getUserDAO();
        UserBean userBean = userDAO.execute(u_id);
        if (userBean != null) {
            forumActionBean.getRequest().setAttribute("userBean", userBean);
            return Constants.OK_EXECUTE_STATUS;
        }
        else {
            return ForumError.noSuchUserError(forumActionBean);
        }
    }
}