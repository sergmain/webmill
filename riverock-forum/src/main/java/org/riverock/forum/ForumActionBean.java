/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 */
package org.riverock.forum;

import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.module.action.ModuleActionRequestImpl;
import org.riverock.module.action.ActionNameProvider;
import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.web.url.UrlProvider;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 14:21:58
 *         $Id: ForumActionBean.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class ForumActionBean extends ModuleActionRequestImpl {
    private Long forumId = null;
    private Database adapter = null;

    public ForumActionBean(ModuleRequest request, ModuleResponse response,
        ModuleConfig moduleConfig, UrlProvider urlProvider,
        Long forumId, Database adapter, ActionNameProvider actionNameProvider) {
        super(request, response, moduleConfig, urlProvider, actionNameProvider);
        this.forumId = forumId;
        this.adapter = adapter;
    }

    public void destroy() {
        super.destroy();
        this.forumId = null;
        DatabaseManager.close( adapter );
        this.adapter = null;
    }

    public Long getForumId() {
        return forumId;
    }

    public Database getAdapter() {
        return adapter;
    }
}
