package org.riverock.forum.dao;

public class DAOFactory {

    public static DAOFactory getDAOFactory() {
        return new DAOFactory();
    }

    public HomeDAO getHomeDAO() {
        return new HomeDAO();
    }

    public ForumDAO getForumDAO() {
        return new ForumDAO();
    }

    public TopicDAO getTopicDAO() {
        return new TopicDAO();
    }

    public PostDAO getPostDAO() {
        return new PostDAO();
    }

    public PostPDAO getPostPDAO() {
        return new PostPDAO();
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    public UserEditPDAO getUserEditPDAO() {
        return new UserEditPDAO();
    }

    public UserListDAO getUserListDAO() {
        return new UserListDAO();
    }

    public AdminForumDAO getAdminForumDAO() {
        return new AdminForumDAO();
    }

    public ForumListManagerDAO getForumListManagerDAO() {
        return new ForumListManagerDAO();
    }

    public EditMessageDAO getEditMessageDAO() {
        return new EditMessageDAO();
    }

    public CommitEditMessageDAO getCommitEditMessageDAO() {
        return new CommitEditMessageDAO();
    }

    public TopLevelForumDAO getTopLevelForumDAO() {
        return new TopLevelForumDAO();
    }
}