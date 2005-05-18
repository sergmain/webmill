package org.riverock.forum.action;

import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumPortletv2;
import org.riverock.forum.bean.PostBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.PostDAO;
import org.riverock.forum.util.Constants;
import org.riverock.forum.util.StringUtils;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.session.ModuleSession;

public class PostAction implements Action {
    private final static Logger log = Logger.getLogger(PostAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Integer f_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_CONCRETE_ID );
        Integer t_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_TOPIC_ID );
        boolean reply = StringUtils.parseBoolean(forumActionBean.getRequest().getString(Constants.NAME_FORUM_REPLY));
        if (reply && t_id==null ) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        if (!reply && f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        DAOFactory daof = DAOFactory.getDAOFactory();
        PostDAO postDAO = daof.getPostDAO();

        ModuleSession sess = forumActionBean.getRequest().getSession(false);

        Long lastPortTime = (Long)sess.getAttribute(Constants.LAST_POST_TIME);
        if (log.isDebugEnabled()) {
            log.debug("strLPT: " + lastPortTime);
        }
        if ( lastPortTime!=null ) {
            int floodTime = ForumPortletv2.getFloodTime( forumActionBean.getConfig() );
            if ((System.currentTimeMillis()-lastPortTime.longValue()) < floodTime * 1000) {
                return ForumError.floodError(forumActionBean);
            }
        }
        sess.setAttribute(Constants.LAST_POST_TIME, new Long(System.currentTimeMillis()) );

        PostBean postBean = null;
        if (log.isDebugEnabled()) {
            log.debug("reply: " + reply);
            log.debug("t_id: " + t_id);
        }
        if (reply) {
            postBean = postDAO.reply(forumActionBean.getForumId(), t_id.intValue());
            if (postBean != null && postBean.getT_locked() > 0) {
                return ForumError.topicLockedError(forumActionBean);
            }
        } else {
            postBean = postDAO.post(forumActionBean.getForumId(), f_id.intValue());
        }
        postBean.setForumId( forumActionBean.getForumId() );

        if (log.isDebugEnabled()) {
            log.debug("postBean: " + postBean);
        }

        if (postBean != null) {
            postBean.setReply(reply);
            forumActionBean.getRequest().setAttribute("postBean", postBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.postError(forumActionBean);
        }
    }

}