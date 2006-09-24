/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.member;

import javax.portlet.PortletRequest;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.schema.member.ContentType;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;

/**
 * @author Sergei Maslyukov
 *         Date: 17.04.2006
 *         Time: 16:39:22
 */
public class MemberProcessingAbstract {
    protected DatabaseAdapter db_ = null;
    protected AuthSession authSession = null;

    protected boolean isMainAccess = false;
    protected boolean isIndexAccess = false;
    protected boolean isInsertAccess = false;
    protected boolean isChangeAccess = false;
    protected boolean isDeleteAccess = false;

    final static String aliasFirmRestrict = "z1243";
    final static String aliasSiteRestrict = "z1207";
    final static String aliasUserRestrict = "z1279";

    public final int linesInException = 20;

    public ModuleType mod = null;
    public ContentType content = null;
    protected String fromParam = null;
    protected ModuleManager moduleManager = null;

    protected PortletRequest portletRequest = null;

    public DatabaseAdapter getDatabaseAdapter() {
        return db_;
    }

    public void destroy()
    {
        DatabaseAdapter.close(db_);
        db_ = null;

        portletRequest = null;

        mod = null;
        content = null;

        fromParam = null;
    }

    public void initRight() {
        isMainAccess = MemberServiceClass.checkRole(portletRequest, content);

        isIndexAccess =
            MemberServiceClass.checkRole(portletRequest, MemberServiceClass.getContent(mod, ContentTypeActionType.INDEX_TYPE));
        isInsertAccess =
            MemberServiceClass.checkRole(portletRequest, MemberServiceClass.getContent(mod, ContentTypeActionType.INSERT_TYPE));
        isChangeAccess =
            MemberServiceClass.checkRole(portletRequest, MemberServiceClass.getContent(mod, ContentTypeActionType.CHANGE_TYPE));
        isDeleteAccess =
            MemberServiceClass.checkRole(portletRequest, MemberServiceClass.getContent(mod, ContentTypeActionType.DELETE_TYPE));
    }
}
