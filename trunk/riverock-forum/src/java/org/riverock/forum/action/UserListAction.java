package org.riverock.forum.action;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.bean.UserListBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.UserListDAO;
import org.riverock.module.exception.ActionException;

public class UserListAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        int start = forumActionBean.getRequest().getInt( "start", new Integer(0)).intValue();

        String keyword = forumActionBean.getRequest().getString("keyword");
        keyword = (keyword == null ? "" : keyword.trim());

//        int range = ForumPortletv2.getRecordsPerPage(forumActionBean.getConfig());
        int range = 30;

        DAOFactory daof = DAOFactory.getDAOFactory();
        UserListDAO userListDAO = daof.getUserListDAO();
        UserListBean userListBean = userListDAO.execute(start, range, keyword);
        if (userListBean != null) {
            forumActionBean.getRequest().setAttribute("userListBean", userListBean);
            return "/forum/jsp/userList.jsp";
        } else {
            return ForumError.userListError(forumActionBean);
        }
    }
}