package org.riverock.forum.action;

import org.riverock.forum.bean.ForumConcreteBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.ForumDAO;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumPortletv2;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.util.Constants;
import org.riverock.module.exception.ActionException;

public class ForumAction implements Action {

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Integer f_id = forumActionBean.getRequest().getInt(Constants.NAME_FORUM_CONCRETE_ID);
        if (f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }
        int start = forumActionBean.getRequest().getInt( "start", new Integer(0)).intValue();
        String keyword = forumActionBean.getRequest().getString("keyword", "");
        keyword = keyword.trim();

        DAOFactory daof = DAOFactory.getDAOFactory();
        ForumDAO forumDAO = daof.getForumDAO();
        ForumConcreteBean forumConcreteBean = forumDAO.execute(
            f_id.intValue(),
            ForumPortletv2.getMessagesPerPage(forumActionBean.getConfig()),
            ForumPortletv2.getTopicsPerPage(forumActionBean.getConfig()),
            start, keyword,
            forumActionBean.getForumId(),
            moduleActionRequest.getUrlProvider()
        );
        if (forumConcreteBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", forumConcreteBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.noSuchForumError(forumActionBean);
        }
    }
}