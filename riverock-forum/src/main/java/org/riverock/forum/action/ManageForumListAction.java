/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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