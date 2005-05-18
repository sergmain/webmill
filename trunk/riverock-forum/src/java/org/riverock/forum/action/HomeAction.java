package org.riverock.forum.action;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.util.Constants;
import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.HomeDAO;

public class HomeAction implements Action {

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        DAOFactory daof = DAOFactory.getDAOFactory();

        HomeDAO homeDAO = daof.getHomeDAO();
        ForumBean homeBean = homeDAO.execute(
            moduleActionRequest.getUrlProvider(), forumActionBean.getForumId(), true
        );
        if (homeBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", homeBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(forumActionBean);
        }
    }
}