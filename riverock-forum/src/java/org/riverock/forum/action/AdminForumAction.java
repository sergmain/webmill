package org.riverock.forum.action;

import org.apache.log4j.Logger;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.util.Constants;
import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.dao.AdminForumDAO;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.HomeDAO;

public class AdminForumAction implements Action {
    private final static Logger log = Logger.getLogger(AdminForumAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        log.debug("in execute()");
        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;

        ModuleUser auth_ = forumActionBean.getRequest().getUser();

        DAOFactory daof = DAOFactory.getDAOFactory();
        AdminForumDAO adminForumDAO = daof.getAdminForumDAO();
        adminForumDAO.execute( auth_, forumActionBean, moduleActionRequest.getUrlProvider(), forumActionBean.getForumId() );

        HomeDAO homeDAO = daof.getHomeDAO();
        ForumBean homeBean = homeDAO.execute(
            moduleActionRequest.getUrlProvider(), forumActionBean.getForumId(), false
        );

        log.debug("before leave execute()");

        if (homeBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", homeBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(forumActionBean);
        }
    }

}