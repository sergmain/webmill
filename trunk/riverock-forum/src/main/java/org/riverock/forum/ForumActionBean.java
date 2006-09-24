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
package org.riverock.forum;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
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
 *         $Id$
 */
public class ForumActionBean extends ModuleActionRequestImpl {
    private Long forumId = null;
    private DatabaseAdapter adapter = null;

    public ForumActionBean(ModuleRequest request, ModuleResponse response,
        ModuleConfig moduleConfig, UrlProvider urlProvider,
        Long forumId, DatabaseAdapter adapter, ActionNameProvider actionNameProvider) {
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

    public DatabaseAdapter getAdapter() {
        return adapter;
    }
}
