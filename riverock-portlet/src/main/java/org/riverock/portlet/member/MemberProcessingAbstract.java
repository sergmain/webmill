/*
 * org.riverock.portlet - Portlet Library
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
