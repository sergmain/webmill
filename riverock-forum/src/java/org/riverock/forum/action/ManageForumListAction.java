package org.riverock.forum.action;

import org.apache.log4j.Logger;

import org.riverock.forum.ForumError;
import org.riverock.forum.bean.ForumTopLevelBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.ForumListManagerDAO;
import org.riverock.forum.dao.TopLevelForumDAO;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.exception.ModuleException;
import org.riverock.module.web.user.ModuleUser;

public class ManageForumListAction implements Action {
    private final static Logger log = Logger.getLogger(ManageForumListAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        log.debug("in execute()");

        ModuleUser auth_ = moduleActionRequest.getRequest().getUser();

        DAOFactory daof = DAOFactory.getDAOFactory();
        Long siteId;
        try {
            siteId = moduleActionRequest.getRequest().getSiteId();
        } catch (ModuleException e) {
            String es = "Error get siteId";
            log.error(es, e);
            throw new IllegalStateException(es,e);
        }
        ForumListManagerDAO adminForumDAO = daof.getForumListManagerDAO();
        adminForumDAO.execute( auth_, moduleActionRequest, moduleActionRequest.getUrlProvider() );

        TopLevelForumDAO homeDAO = daof.getTopLevelForumDAO();
        ForumTopLevelBean homeBean = homeDAO.execute( siteId );

        log.debug("before leave execute()");

        if (homeBean != null) {
            moduleActionRequest.getRequest().setAttribute("forums", homeBean);
            moduleActionRequest.getRequest().setAttribute("integrity", DAOFactory.getDAOFactory().getForumIntegrityDao().getIntegrityStatus(siteId));


            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(moduleActionRequest);
        }
    }

}