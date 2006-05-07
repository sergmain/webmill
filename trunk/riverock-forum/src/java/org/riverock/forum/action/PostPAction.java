package org.riverock.forum.action;

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

import javax.portlet.ActionRequest;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.PostPDAO;
import org.riverock.forum.util.Constants;
import org.riverock.forum.util.ForumStringUtils;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;

public class PostPAction implements Action {
    private final static Logger log = Logger.getLogger(PostPAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        ModuleUser auth_ = forumActionBean.getRequest().getUser();
        if (auth_==null) {
            return ForumError.notLoggedError(forumActionBean);
        }

        Long userId = auth_.getId();
        boolean reply = ForumStringUtils.parseBoolean(forumActionBean.getRequest().getString(Constants.NAME_FORUM_REPLY));
        int tm_iconid = forumActionBean.getRequest().getInt( "tm_iconid", new Integer(0)).intValue();

        Integer f_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_CONCRETE_ID );
        Integer t_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_TOPIC_ID );
        if (reply && t_id==null ) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        if (!reply && f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        String subject = forumActionBean.getRequest().getString("subject");
        String content = forumActionBean.getRequest().getString("content");

        if ( log.isDebugEnabled() ) {

            Iterator it  = ((ActionRequest)forumActionBean.getRequest().getOriginRequest()).getParameterMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                log.debug( "key: " + entry.getKey()+", value: "+entry.getValue());
            }
            log.debug( "content #1: " + ((ActionRequest)forumActionBean.getRequest().getOriginRequest()).getParameter("content"));
            log.debug( "content #2: " + forumActionBean.getRequest().getParameter("content"));
            log.debug( "content #3: " + forumActionBean.getRequest().getString("content"));
            log.debug( "content #4: " + content );
        }

        String u_lastip = forumActionBean.getRequest().getRemoteAddr();

        if (StringUtils.isBlank(content) || (!reply && StringUtils.isBlank(subject))) {
            return ForumError.blankError(forumActionBean);
        }
        DAOFactory daof = DAOFactory.getDAOFactory();
        PostPDAO postPDAO = daof.getPostPDAO();
        String urlString =
            moduleActionRequest.getUrlProvider().getUrl( Constants.WM_FORUM_PORTLET_NAME, Constants.TOPIC_ACTION ) +
            Constants.NAME_FORUM_ID+'=' +
            forumActionBean.getForumId() + '&' +
            Constants.NAME_FORUM_TOPIC_ID + '=';

        if (reply) {
            urlString = urlString + t_id + '&'+Constants.NAME_FORUM_MESSAGE_ID+'=' + postPDAO.reply(t_id.intValue(), userId, content, u_lastip, tm_iconid, forumActionBean.getForumId());
        } else {
            urlString = urlString + postPDAO.post(f_id.intValue(), userId, subject, content, u_lastip, tm_iconid, forumActionBean.getForumId());
        }

        if (log.isDebugEnabled()) {
            log.debug("redirect to: " + urlString );
        }

        try {
            forumActionBean.getResponse().sendRedirect(urlString);
        }
        catch (IOException e) {
            String es = "Error sendRedirect to "+ urlString;
            log.error( es, e);
            throw new ActionException( es, e);
        }
        return null;
    }

}