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
