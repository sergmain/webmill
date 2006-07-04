/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
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
        if (log.isDebugEnabled()) {
            log.debug("Looking for forums for siteId " + siteId);
        }
        ForumListManagerDAO adminForumDAO = daof.getForumListManagerDAO();
        adminForumDAO.execute( auth_, moduleActionRequest, moduleActionRequest.getUrlProvider() );

        TopLevelForumDAO homeDAO = daof.getTopLevelForumDAO();
        ForumTopLevelBean homeBean = homeDAO.execute( siteId );

        if (log.isDebugEnabled()) {
            log.debug("before leave execute()");
            log.debug("homeBean " + homeBean);
        }

        if (homeBean != null) {
            moduleActionRequest.getRequest().setAttribute("forums", homeBean);
            moduleActionRequest.getRequest().setAttribute("integrity", DAOFactory.getDAOFactory().getForumIntegrityDao().getIntegrityStatus(siteId));


            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(moduleActionRequest);
        }
    }

}