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
