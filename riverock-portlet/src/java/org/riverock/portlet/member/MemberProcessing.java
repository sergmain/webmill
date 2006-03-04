/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.portlet.member;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;


import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.portlet.member.validator.BindDynamicValidator;
import org.riverock.portlet.member.validator.XmlValidator;
import org.riverock.portlet.member.validator.XsltValidator;
import org.riverock.portlet.schema.member.*;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.FieldValidatorTypeTypeType;
import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;
import org.riverock.portlet.schema.member.types.ParameterTypeType;
import org.riverock.portlet.schema.member.types.PrimaryKeyTypeType;
import org.riverock.portlet.schema.member.types.RestrictTypeTypeType;
import org.riverock.portlet.schema.member.types.SqlCheckParameterTypeTypeType;
import org.riverock.portlet.schema.member.types.TargetModuleTypeActionType;
import org.riverock.portlet.schema.member.types.TypeFieldType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * $Id$
 */
public final class MemberProcessing {
    private final static Logger log = Logger.getLogger( MemberProcessing.class );

    public ModuleType mod = null;
    public ContentType content = null;

    public DatabaseAdapter getDatabaseAdapter() {
        return db_;
    }

    private DatabaseAdapter db_ = null;

    public boolean isMainAccess = false;
    public boolean isIndexAccess = false;
    public boolean isInsertAccess = false;
    public boolean isChangeAccess = false;
    public boolean isDeleteAccess = false;

    private PortletRequest renderRequest = null;
    private PortletResponse renderResponse = null;
    private ResourceBundle bundle = null;
    private ModuleManager moduleManager = null;

    final static String aliasFirmRestrict = "z1243";
    final static String aliasSiteRestrict = "z1207";
    final static String aliasUserRestrict = "z1279";

    public final int linesInException = 20;

    private String fromParam = null;
    private String thisURI = null;
    private String commitURI = null;
    private Long firmId = null;
    private Long userId = null;
    private AuthSession authSession = null;

    public void destroy()
    {
        DatabaseAdapter.close(db_);
        db_ = null;

        mod = null;
        content = null;
        renderRequest = null;
        renderResponse = null;

        fromParam = null;
        thisURI = null;
        commitURI = null;
        firmId = null;
        userId = null;

    }

    public boolean checkRestrict() throws Exception
    {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            SqlCacheType sqlCache = content.getQueryArea().getSqlCache();
            String sql = null;

                if (log.isDebugEnabled())
                    log.debug("sqlQuery.getCheckSql not cached, start create new.");

                if (sqlCache.getFromCount()==0 || sqlCache.getWhereCount()==0)
                {
                    if (log.isDebugEnabled())
                        log.debug("Return true. from count - "+sqlCache.getFromCount()+", where count - "+sqlCache.getWhereCount());

                    return true;
                }

                sql = "select null from ";
                boolean isFirst = true;
                for (int i=0; i<sqlCache.getFromCount(); i++)
                {
                    if (isFirst)
                        isFirst = false;
                    else
                        sql += ", ";

                    SqlFromType from = sqlCache.getFrom(i);
                    sql += from.getTable();
                    if (from.getAlias() != null)
                        sql += " "+from.getAlias();
                }

                isFirst = true;
                for (int i=0; i<sqlCache.getWhereCount(); i++)
                {
                    if (isFirst)
                    {
                        sql += " where ";
                        isFirst = false;
                    }
                    else
                        sql += " and ";

                    SqlWhereType where = sqlCache.getWhere(i);

                    SqlWhereColumnType column = where.getLeft();
                    switch( column.getTypeField().getType())
                    {
                        case TypeFieldType.DATA_TYPE:
                        case TypeFieldType.PARAMETER_TYPE:
                            sql += column.getValue();
                            break;
                        case TypeFieldType.FIELD_TYPE:
                            if (column.getAlias()!=null)
                                sql += (column.getAlias()+".");
                            sql += column.getColumn();
                            break;
                        default:
                            throw new Exception("Unknown type of 'where' left column");
                    }
                    sql += "=";
                    column = where.getRight();
                    switch( column.getTypeField().getType())
                    {
                        case TypeFieldType.DATA_TYPE:
                        case TypeFieldType.PARAMETER_TYPE:
                            sql += column.getValue();
                            break;
                        case TypeFieldType.FIELD_TYPE:
                            if (column.getAlias()!=null)
                                sql += (column.getAlias()+".");
                            sql += column.getColumn();
                            break;
                        default:
                            throw new Exception("Unknown type of 'where' right column");
                    }
                }

            if (log.isDebugEnabled())
                log.debug("sql for check\n"+sql);

            ps = db_.prepareStatement( sql );
            int numParam = 1;
            for (int i=0; i<sqlCache.getCheckParamCount(); i++)
            {
                SqlCheckParameterType parameter = sqlCache.getCheckParam(i);
                switch( parameter.getType().getType() )
                {
                    case SqlCheckParameterTypeTypeType.HTTP_REQUEST_TYPE:
                        switch ( parameter.getParameterType().getType() )
                        {
                            case ParameterTypeType.NUMBER_TYPE:
                                RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest, parameter.getParameter()) );
                                break;
                            case ParameterTypeType.STRING_TYPE:
                                ps.setString(numParam++, RequestTools.getString(renderRequest, parameter.getParameter()));
                                break;
                            case ParameterTypeType.DATE_TYPE:
//                        RsetTools.setLong(ps, numParam++, PortletService.getLong(req, parameter.getParameter()));
//                        break;
                                throw new Exception("DATE_TYPE of PK not implemented");
                            default:
                                log.info("Unknown type of PK - "+parameter.getParameterType());
//                    throw new Exception("Wrong type of ")
                        }
                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_SITE_TYPE:
                        ps.setString(numParam++, renderRequest.getServerName());
                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_FIRM_TYPE:

                        String nameFirmField = isFirmFieldExists(content.getQueryArea());
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                if (log.isDebugEnabled())
                                    log.debug("nameFirmField: "+nameFirmField);

                                if (nameFirmField!=null) {
                                    List list = authSession.getGrantedCompanyIdList();
                                    Long id = PortletService.getLong(renderRequest, mod.getName() + '.' + nameFirmField);

                                    log.debug("id: "+id);
                                    if (id==null)
                                        return false;

                                    boolean isFound = false;
                                    for (int k=0; k<list.size(); k++) {
                                        if (id.equals((Long)list.get(k)))
                                            isFound=true;
                                    }

                                    log.debug("return false ");
                                    if (!isFound)
                                        return false;
                                }
                                break;
                            default:
                                if (nameFirmField != null)
                                    RsetTools.setLong(ps, numParam++,
                                        PortletService.getLong(renderRequest,
                                            mod.getName() + '.' + nameFirmField) );

                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }

                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_USER_TYPE:
                        ps.setString(numParam++, renderRequest.getRemoteUser());
                        break;
                }
            }
            if (log.isDebugEnabled())
                log.debug("start executeQuery ");

            rs = ps.executeQuery();

            if (log.isDebugEnabled())
                log.debug("done executeQuery");

            if (rs.next())
                return true;
        }
        catch(Exception e)
        {
            log.error("Error checkRestriction", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return false;
    }

    public boolean isSimpleField()
    {
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++)
        {
            FieldsType ff = content.getQueryArea().getFields(k);

//log.debug("#15.01 "+ff.jspType, 99);

            if (Boolean.TRUE.equals(ff.getIsEdit()) )
            {
//                if (!"bigtext".equals(ff.jspType))
                if (FieldsTypeJspTypeType.BIGTEXT_TYPE != ff.getJspType().getType())
                    return true;
            }
        }
        return false;
    }

    public static SqlWhereType getWhere(
        String leftAlias, String leftColumn, String leftValue, TypeFieldType leftType,
        String rightAlias, String rightColumn, String rightValue, TypeFieldType rightType)
    {
        SqlWhereType whereType = new SqlWhereType();

        whereType.setLeft(
            getWhereColumn(leftAlias, leftColumn, leftValue, leftType)
        );
        whereType.setRight(
            getWhereColumn(rightAlias, rightColumn, rightValue, rightType)
        );

        return whereType;
    }

    private static SqlWhereColumnType getWhereColumn(
        String alias,
        String column,
        String value,
        TypeFieldType type
        )
    {
        SqlWhereColumnType columnType = new SqlWhereColumnType();

        columnType.setAlias( alias );
        columnType.setColumn( column );
        columnType.setValue( value );
        columnType.setTypeField( type );

        return columnType;
    }

    private String getDateTextCell(ResultSet rs, FieldsType ff, String defValue)
        throws Exception
    {
        return
            RsetTools.getStringDate(rs, MemberServiceClass.getRealName(ff),
                (ff.getMask() == null || ff.getMask().trim().length() == 0 ? "dd.MM.yyyy" : ff.getMask()),
                defValue, renderRequest.getLocale());
    }

    private String getCellValue(ResultSet rs, FieldsType ff, boolean isEdit)
        throws Exception
    {

        String s_ = "";
        switch (ff.getJspType().getType())
        {
            case FieldsTypeJspTypeType.TEXT_TYPE:
            case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                s_ = RsetTools.getString(rs, MemberServiceClass.getRealName(ff), "");
                break;

            case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
            case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                Double doubleValue = RsetTools.getDouble(rs, MemberServiceClass.getRealName(ff));
                s_ = doubleValue==null?"":doubleValue.toString();
                break;

            case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                Long longValue = RsetTools.getLong(rs, MemberServiceClass.getRealName(ff));
                s_ = longValue==null?"":longValue.toString();
                break;

            case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                s_ = getDateTextCell(rs, ff, "");
                break;

            case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
            case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:
                s_ = MemberTools.printYesNo(rs, MemberServiceClass.getRealName(ff), false, bundle );
                break;
            case FieldsTypeJspTypeType.CHECKBOX_SWITCH_TYPE:
                if (1==RsetTools.getInt(rs, MemberServiceClass.getRealName(ff), 0) )
                    s_ = "X";
                else
                    s_ = "-";
                break;
            case FieldsTypeJspTypeType.LOOKUP_TYPE:
                s_ = getRsetSelectValue(ff.getQueryArea(),
                    RsetTools.getLong(rs, MemberServiceClass.getRealName(ff))
                );

                break;

            case FieldsTypeJspTypeType.LOOKUPCLASS_TYPE:
                s_ = getSelectListFromClass(rs, content.getQueryArea() , ff, isEdit);
                break;

            case FieldsTypeJspTypeType.BIGTEXT_TYPE:

                String primaryKeyValue = null;
                int primaryKeyType = content.getQueryArea().getPrimaryKeyType().getType();
                switch (primaryKeyType)
                {
                    case PrimaryKeyTypeType.NUMBER_TYPE:
                        primaryKeyValue = "" + RsetTools.getLong(rs, content.getQueryArea().getPrimaryKey());
                        break;
                    case PrimaryKeyTypeType.STRING_TYPE:
                        primaryKeyValue = RsetTools.getString(rs, content.getQueryArea().getPrimaryKey());
                        break;
                    case PrimaryKeyTypeType.DATE_TYPE:
                        if (content.getQueryArea().getPrimaryKeyMask() == null ||
                            content.getQueryArea().getPrimaryKeyMask().trim().length() == 0)
                            throw new Exception("date mask for primary key not specified");

                        primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                            content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
                        break;
                    default:
                        log.error("Wrong type of primary key");
                        throw new Exception("Wrong type of primary key");
                }
                s_ = getRsetBigtextValue(ff.getQueryArea(), primaryKeyValue);

//log.debug("#12.015 "+s_);

                break;
            case FieldsTypeJspTypeType.BIGTEXT_LOB_TYPE:
                s_ = db_.getClobField(rs, MemberServiceClass.getRealName(ff));

                break;
            default:
                log.error("Error of field type - " + ff.getJspType().toString());
                throw new Exception("Error of field type - " + ff.getJspType().toString());
        }

        return (s_ == null ? "" : s_);
    }

    private String isFirmFieldExists(QueryAreaType qa)
    {
        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) )
            {
                if ("ID_FIRM".equalsIgnoreCase(ff.getName()))
                    return ff.getName();
            }
        }

        if ("ID_FIRM".equalsIgnoreCase(content.getQueryArea().getPrimaryKey()))
            return content.getQueryArea().getPrimaryKey();

        return null;
    }

    public String buildSelectForUpdateSQL()
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            return null;


        String pkID = prepareTableAlias(content.getQueryArea().getMainRefTable()) + '.' +
            content.getQueryArea().getPrimaryKey();

        String sql_ = "select " + pkID;

        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) || Boolean.TRUE.equals(ff.getIsSelect()) )
                sql_ += ',' + prepareTableAlias(ff.getRefTable()) + '.' + ff.getName();
        }

        String fromSQL = "";
        for (int k = 0; k < content.getQueryArea().getTableCount(); k++)
        {
            TableType table = content.getQueryArea().getTable(k);

            if (fromSQL.length() > 0)
                fromSQL += ',';
            fromSQL += table.getTable() + ' ' + prepareTableAlias(table.getRef());
        }

        SqlClause sc = new SqlClause();
        SqlClause lookupSc = null;

//log.debug("#44.99.000 "+sc.where);

        String prevPK = null;
        String prevAlias = null;
        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = MemberServiceClass.getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    lookupSc = MemberServiceClass.buildSelectClause(content, cnt, module, db_, renderRequest.getServerName(), authSession );

                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)
                    {

                        if (sc.from.length() != 0)
                            sc.from += ',';
                        sc.from += lookupSc.from;

//log.debug("#44.99.001 "+sc.where);
                        if (sc.where.length() > 0)
                            sc.where += " and ";
                        sc.where += prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                            '.' + cnt.getQueryArea().getPrimaryKey() + "=?";

//log.debug("#44.99.004 "+sc.where);
                        if (prevPK != null)
                        {
//log.debug("#44.99.007 "+sc.where);
                            if (sc.where.length() > 0)
                                sc.where += " and ";

                            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                                '.' + prevPK;

                        }
//log.debug("#44.99.009 "+sc.where);

                        if (lookupSc.where.length() > 0)
                        {
//log.debug("#44.99.011 "+sc.where);
                            if (sc.where.length() != 0)
                                sc.where += " and ";

                            sc.where += lookupSc.where;
                        }
//log.debug("#44.99.015 "+sc.where);

                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
            }

        }

//log.debug("#44.99.018 "+sc.where);
        if (prevPK != null)
        {
//log.debug("#44.99.019 "+sc.where);
            if (sc.where.length() != 0)
                sc.where += " and ";

            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                '.' + prevPK;

        }
//log.debug("#44.99.021 "+sc.where);

// --
        if (fromSQL.length() > 0 && sc.from.length() > 0)
            fromSQL += ',';

        fromSQL += sc.from;
        String whereSQL = sc.where;

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
        {
            if (whereSQL.length() > 0)
                whereSQL += " and ";

            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    whereSQL += prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_FIRM in ("+idList+") ";

                    break;
                default:
                    fromSQL += ", v$_read_list_firm " + prepareTableAlias(aliasFirmRestrict) + ' ';

                    whereSQL += prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_FIRM=" + prepareTableAlias(aliasFirmRestrict) +
                        ".ID_FIRM and " + prepareTableAlias(aliasFirmRestrict) + ".user_login = ? ";
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
        {
            if (whereSQL.length() > 0)
                whereSQL += " and ";

            switch (db_.getFamaly()){
                case DatabaseManager.MYSQL_FAMALY:
                    String idSite = MemberTools.getGrantedSiteId(db_, renderRequest.getServerName());
                    whereSQL +=
                        prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_SITE in ("+idSite+") ";

                    break;
                default:
                    fromSQL += ", WM_PORTAL_VIRTUAL_HOST " + prepareTableAlias(aliasSiteRestrict) + ' ';

                    whereSQL += prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_SITE=" + prepareTableAlias(aliasSiteRestrict) +
                        ".ID_SITE and " + prepareTableAlias(aliasSiteRestrict) + ".NAME_VIRTUAL_HOST = lower(?) ";
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            if (whereSQL.length() > 0)
                whereSQL += " and ";

            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    String idUser = authSession.getGrantedUserId();
                    whereSQL +=
                        prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_USER in ("+idUser+") ";
                    break;

                default:
                    whereSQL +=
                        prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                        ".ID_USER in " +
                        " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod.getName()) +
                        '.' + "ID_USER " +
                        "  from WM_AUTH_USER " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod.getName()) +
                        "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod.getName()) +
                        ".USER_LOGIN=?) ";
                    break;
            }
        }

        if (whereSQL.length() > 0)
            whereSQL += " and ";

        whereSQL += pkID + "=? ";

        if (! Boolean.TRUE.equals(mod.getIsDelete()) )
        {
            if (whereSQL.length() > 0)
                whereSQL += " and ";
            whereSQL += prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                ".is_deleted=0";
        }

        String orderSQL = "";

        return sql_ + " from " + fromSQL + " where  " + whereSQL + orderSQL;

    }

    public String buildSelectForDeleteSQL()
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            return null;

        String pkID = prepareTableAlias(content.getQueryArea().getMainRefTable()) +
            '.' + content.getQueryArea().getPrimaryKey();

        String sql_ = "select " + pkID;


        SqlClause sc = new SqlClause();
        SqlClause lookupSc = null;

        String prevPK = null;
        String prevAlias = null;
        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = MemberServiceClass.getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    lookupSc = MemberServiceClass.buildSelectClause(content, cnt, module, db_, renderRequest.getServerName(), authSession );

                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)
                    {
                        if (sc.from.length() != 0)
                            sc.from += ',';

                        sc.from += lookupSc.from;

                        if (sc.where.length() != 0)
                            sc.where += " and ";

                        sc.where += prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                            '.' + cnt.getQueryArea().getPrimaryKey() + "=?";

                        if (prevPK != null)
                        {
                            if (sc.where.length() != 0)
                                sc.where += " and ";

                            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                                '.' + prevPK;

                        }

                        if (lookupSc.where.length() > 0)
                        {
                            if (sc.where.length() != 0)
                                sc.where += " and ";

                            sc.where += lookupSc.where;
                        }

                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
            }

        }

        if (prevPK != null)
        {
            if (sc.where.length() != 0)
                sc.where += " and ";

            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                '.' + prevPK;

        }

        lookupSc = MemberServiceClass.buildSelectClause(content, content, mod, db_, renderRequest.getServerName(), authSession );

        if (sc.from.length() != 0)
        {
            if (lookupSc.from.length() != 0)
                lookupSc.from += ',';

            lookupSc.from += sc.from;
        }

        if (sc.where.length() != 0)
        {
            if (lookupSc.where.length() != 0)
                sc.where += " and ";

            lookupSc.where = sc.where + lookupSc.where;
        }


        lookupSc.where = " where " + pkID + "=? " +
            (lookupSc.where.length() > 0 ? "and " + lookupSc.where : "");


        return sql_ + ',' + lookupSc.select +
            " from " + lookupSc.from +
            lookupSc.where +
            (lookupSc.order.length() > 0 ? " order by " + lookupSc.order : "");
    }

    private String prepareTableAlias(String aliasName)
    {
        return prepareTableAlias(aliasName, mod.getName());
    }

    public static String prepareTableAlias(String aliasName, String nameModule)
    {
        return nameModule.replace('.', '_') + '_' + (aliasName != null ? aliasName : "");
    }

    public String buildSelectSQL()
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            return null;

        SqlClause sc = new SqlClause();
        SqlClause lookupSc = null;

        String prevPK = null;
        String prevAlias = null;
        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = MemberServiceClass.getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    lookupSc = MemberServiceClass.buildSelectClause(content, cnt, module, db_, renderRequest.getServerName(), authSession );

                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)
                    {
                        if (sc.from.length() != 0)
                            sc.from += ',';

                        sc.from += lookupSc.from;

                        if (sc.where.length() != 0)
                            sc.where += " and ";

                        sc.where += prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                            '.' + cnt.getQueryArea().getPrimaryKey() + "=?";

                        if (prevPK != null)
                        {
                            if (sc.where.length() != 0)
                                sc.where += " and ";

                            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +
                                '.' + prevPK;
                        }

                        if (lookupSc.where.length() > 0)
                        {
                            if (sc.where.length() != 0)
                                sc.where += " and ";

                            sc.where += lookupSc.where;
                        }

                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
            }
        }

        if (prevPK != null) {
            if (sc.where.length() != 0)
                sc.where += " and ";

            sc.where += prevAlias + prevPK + "=" + prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                '.' + prevPK;
        }

        lookupSc = MemberServiceClass.buildSelectClause(content, content, mod, db_, renderRequest.getServerName(), authSession );

        if (sc.from.length() != 0) {
            if (lookupSc.from.length() != 0)
                lookupSc.from += ',';

            lookupSc.from += sc.from;
        }

        if (sc.where.length() != 0) {
            if (lookupSc.where.length() != 0)
                sc.where += " and ";

            lookupSc.where = sc.where + lookupSc.where;
        }

        String selfLookup = "";
        if (mod.getSelfLookup() != null && mod.getSelfLookup().getCurrentField() != null & mod.getSelfLookup().getTopField() != null)
        {
            selfLookup = prepareTableAlias(content.getQueryArea().getMainRefTable()) +
                '.' + mod.getSelfLookup().getTopField().getName() + " = ? ";

            if (lookupSc.where.length() != 0)
                lookupSc.where += " and ";

            lookupSc.where += selfLookup;
        }

        return "select " + lookupSc.select +
            " from " + lookupSc.from +
            (lookupSc.where.length() > 0 ? " where " + lookupSc.where : "") +
            (lookupSc.order.length() > 0 ? " order by " + lookupSc.order : "");
    }

    private void bindSelectSQL(PreparedStatement ps)
        throws Exception
    {
        if (content == null || content.getQueryArea() == null) {
            throw new Exception("content or QueryAreaType is null.");
        }

        if (log.isDebugEnabled()) {
            log.debug("Start bindSelectSql" );
        }

        int numParam = 1;
        Long longTemp = null;
        String stringTemp = null;
        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    //prepare lookup PK
                    if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
                    {
                        longTemp = PortletService.getLong(renderRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                        if (log.isDebugEnabled()) {
                            log.debug("param#"+numParam+": "+longTemp);
                        }
                        RsetTools.setLong(ps, numParam++, longTemp );
                    }
                    else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
                    {
                        stringTemp = RequestTools.getString(renderRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                        if (log.isDebugEnabled()) {
                            log.debug("param#"+numParam+": "+stringTemp);
                        }
                        ps.setString(numParam++, stringTemp );
                    }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
                    else
                        throw new Exception("Wrong type of primary key");

/*
s_ = getRsetBigtextValue( ff.getQueryArea(), primaryKeyValue );

RsetTools.setLong(ps, numParam++, PortletService.getLong(req,
modName+'.'+cnt.getQueryArea().getPrimaryKey()) );
*/

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
                    {
                        if (log.isDebugEnabled()) {
                            log.debug("#3.001.04 param#"+numParam+": "+renderRequest.getRemoteUser() );
                        }
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) {
                                    log.debug("param#"+numParam+": "+renderRequest.getRemoteUser());
                                }
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled())
                                    log.debug("#3.001.07 param#"+numParam+": "+renderRequest.getServerName());
                                ps.setString(numParam++, renderRequest.getServerName());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                if (log.isDebugEnabled()) {
                                    log.debug("#3.001.09 param#"+numParam+": "+renderRequest.getRemoteUser());
                                }
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                            default:
                                if (log.isDebugEnabled()) {
                                    log.debug("#3.001.09 param#"+numParam+": "+renderRequest.getRemoteUser());
                                }
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }
                }
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled()) {
                        log.debug("param#"+numParam+": "+renderRequest.getRemoteUser());
                    }
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled()) {
                        log.debug("param#"+numParam+": "+renderRequest.getServerName());
                    }
                    ps.setString(numParam++, renderRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            if (log.isDebugEnabled()) {
                log.debug("param#"+numParam+": "+renderRequest.getRemoteUser());
            }
            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (mod.getSelfLookup() != null && mod.getSelfLookup().getCurrentField() != null & mod.getSelfLookup().getTopField() != null) {
            longTemp = PortletService.getLong(renderRequest,
                mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L );
            if (log.isDebugEnabled()) {
                log.debug("param#"+numParam+": "+longTemp);
            }
            RsetTools.setLong(ps, numParam++, longTemp );
        }
    }

    public void bindSelectForUpdate(PreparedStatement ps)
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or QueryAreaType is null.");

        int numParam = 1;

        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    //prepare lookup PK
                    if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
                    {
                        if (log.isDebugEnabled())
                            log.debug(" 1 Bind param #"+numParam+" " + PortletService.getLong(renderRequest,
                                    modName + '.' + cnt.getQueryArea().getPrimaryKey())
                            );

                        RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()) );
                    }
                    else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
                    {
                        if (log.isDebugEnabled())
                            log.debug(" 2 Bind param #"+numParam+" " + RequestTools.getString(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey())
                            );

                        ps.setString(numParam++, RequestTools.getString(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()));
                    }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
                    else
                        throw new Exception("Wrong type of primary key");


                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled())
                                    log.debug(" 4 Bind param #"+numParam+" " + renderRequest.getRemoteUser());
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) log.debug(" 5 Bind param #"+numParam+" " + renderRequest.getServerName());
                                ps.setString(numParam++, renderRequest.getServerName());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) log.debug(" 6 Bind param #"+numParam+" " + renderRequest.getRemoteUser());
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }
                }
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            if (log.isDebugEnabled()) log.debug(" 7 Bind param #"+numParam+" " + renderRequest.getRemoteUser());

            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            if (log.isDebugEnabled()) log.debug(" 8 Bind param #"+numParam+" " + renderRequest.getServerName());

            switch (db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            if (log.isDebugEnabled()) log.debug(" 9 Bind param #"+numParam+" " + renderRequest.getRemoteUser());

            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
        {
            if (log.isDebugEnabled())
                log.debug("10 Bind param #"+numParam+" " + PortletService.getLong(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey())
                );

            RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()) );
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
        {
            if (log.isDebugEnabled())
                log.debug("11 Bind param #"+numParam+" " + RequestTools.getString(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey())
                );

            ps.setString(numParam++, RequestTools.getString(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()));
        }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
        else
            throw new Exception("Wrong type of primary key");


    }

    public void bindSelectForDelete(PreparedStatement ps)
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or QueryAreaType is null.");

        int numParam = 1;

        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
        {
            RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()) );
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
        {
            ps.setString(numParam++, RequestTools.getString(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()));
        }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
}
*/
        else
            throw new Exception("Wrong type of primary key");


        if (fromParam.length() > 0)
        {
            StringTokenizer st = new StringTokenizer(fromParam, ",");

            while (st.hasMoreTokens())
            {
                String modName = st.nextToken();

                ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null)
                {
                    //prepare lookup PK
                    if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
                    {
                        RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()) );
                    }
                    else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
                    {
                        ps.setString(numParam++, RequestTools.getString(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()));
                    }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
                    else
                        throw new Exception("Wrong type of primary key");

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled())
                                    log.debug("#3.001.04 "+renderRequest.getRemoteUser() );
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled())
                                    log.debug("#3.001.07 " + renderRequest.getServerName());
                                ps.setString(numParam++, renderRequest.getServerName());
                                break;
                        }
                    }

                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
                    {
                        switch (db_.getFamaly())
                        {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled())
                                    log.debug("#3.001.09 "+renderRequest.getRemoteUser() );
                                ps.setString(numParam++, renderRequest.getRemoteUser());
                                break;
                        }
                    }
                }
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
        {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
        {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }
    }

    public Object bindInsert( DatabaseAdapter dbDyn, PreparedStatement ps )
        throws Exception {
        Object returnID = null;

        if (log.isDebugEnabled()) log.debug("#99.01.00 sequence -  "+"seq_"+content.getQueryArea().getTable(0).getTable());

        // create sequence for PK
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_" + content.getQueryArea().getTable(0).getTable());
        seq.setTableName( content.getQueryArea().getTable(0).getTable() );
        seq.setColumnName( content.getQueryArea().getPrimaryKey() );
        Long pkID = dbDyn.getSequenceNextValue( seq );

        int numParam = 1;

        if (log.isDebugEnabled()) log.debug("#99.01.01 value of PK " + pkID);

        // bind PK
        switch (content.getQueryArea().getPrimaryKeyType().getType())
        {
            case PrimaryKeyTypeType.NUMBER_TYPE:
                RsetTools.setLong(ps, numParam++, pkID);
                returnID = pkID;

                if (log.isDebugEnabled())
                {
                    log.debug("#99.01.02.1 PK object " + returnID);
                    log.debug("#99.01.02.2 type of PK " + returnID.getClass().getName());
                }
                break;

            case PrimaryKeyTypeType.STRING_TYPE:
                ps.setString(numParam++, "" + returnID);
                returnID = "" + pkID;

                if (log.isDebugEnabled())
                    log.debug("#99.01.03 " + returnID);

                break;

            case PrimaryKeyTypeType.DATE_TYPE:
/*
                if (content.getQueryArea().getPrimaryKeyMask()==null ||
                    content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
                    throw new Exception("date mask for primary key not specified");

                primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                    content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

                break;
*/
                throw new Exception(
                    content.getQueryArea().getPrimaryKeyType().toString() +
                    " type of lookup not implemented"
                );
            default:
                throw new Exception("Wrong type of primary key");
        }

        // Bind main variable
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++)
        {
            FieldsType ff = content.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)) {
                if (log.isDebugEnabled()) log.debug("#4.05.01 bind "+ff.getJspType().toString()+" param #" + numParam + " " + RequestTools.getString(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff)));

                String stringParam =
                    RequestTools.getString(
                        renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff), null
                    );

                switch (ff.getJspType().getType())
                {
                    case FieldsTypeJspTypeType.TEXT_TYPE:
                    case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                        if (stringParam!=null && stringParam.length()>0)
                            ps.setString(numParam++, stringParam );
                        else
                            ps.setNull(numParam++, Types.VARCHAR);
                        break;

                    case FieldsTypeJspTypeType.DATE_TEXT_TYPE:

                        if (stringParam!=null && stringParam.length()>0)
                        {
                            java.util.Date updateDate =
                                DateTools.getDateWithMask( stringParam, ff.getMask() );

                            dbDyn.bindDate( ps, numParam++, new java.sql.Timestamp( updateDate.getTime()) );
                        }
                        else
                            ps.setNull( numParam++, Types.DATE );
                        break;

                    case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                        if (stringParam!=null && stringParam.length()>0)
                            ps.setLong(numParam++, new Long( stringParam ));
                        else
                            ps.setNull(numParam++, Types.INTEGER);
                        break;

                    case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                    case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:

                        if (stringParam!=null && stringParam.length()>0)
                            ps.setDouble(numParam++, new Double( stringParam ));
                        else
                            ps.setNull(numParam++, Types.DECIMAL);
                        break;

                    default:
                        if (stringParam!=null && stringParam.length()>0)
                            ps.setString(numParam++, stringParam );
                        else
                            ps.setNull(numParam++, Types.VARCHAR);
                        break;
                }
            }
        }

        // bind FK to PK of top table
        StringTokenizer st = new StringTokenizer(fromParam, ",");
        while (st.hasMoreTokens())
        {
            String modName = st.nextToken();

            ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
            if (cnt != null && cnt.getQueryArea() != null &&
                !st.hasMoreTokens())
            {
                if (log.isDebugEnabled())
                    log.debug("#4.09.03.1 bind '" + cnt.getQueryArea().getPrimaryKeyType().toString() +
                        "' param #" + numParam + " " + modName + '.' + cnt.getQueryArea().getPrimaryKey()+' '+
                            RequestTools.getString(renderRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey())
                    );

                switch( cnt.getQueryArea().getPrimaryKeyType().getType() )
                {
                    case PrimaryKeyTypeType.NUMBER_TYPE:
                        RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()) );
                        break;

                    case PrimaryKeyTypeType.STRING_TYPE:
                            ps.setString(numParam++, RequestTools.getString(renderRequest,
                        modName + '.' + cnt.getQueryArea().getPrimaryKey()));
                        break;

                    case PrimaryKeyTypeType.DATE_TYPE:
/*
                        if (content.getQueryArea().getPrimaryKeyMask()==null ||
                            content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
                            throw new Exception("date mask for primary key not specified");

                        primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                            content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
*/
                        throw new Exception(
                            cnt.getQueryArea().getPrimaryKeyType().toString() +
                            " type of lookup not implemented"
                        );

                    default:
                        throw new Exception( cnt.getQueryArea().getPrimaryKeyType().toString() +
                            " type of lookup is wrong"
                        );
                }
            }
        }

        // process self lookup module
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            if (log.isDebugEnabled())
                log.debug("#4.01.03.1 bind long param #" + numParam + " " +
                    PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L)
                );

            RsetTools.setLong(ps, numParam++,
                PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L )
            );
        }

        // processing of restrict
        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
            RestrictTypeTypeType.FIRM_TYPE
            && !MemberServiceClass.checkRestrictField( content, RestrictTypeTypeType.FIRM_TYPE) )
        {
            if (log.isDebugEnabled())
                log.debug("#4.09.07 bind long param #" + numParam + ' ' + firmId);

            RsetTools.setLong(ps, numParam++, firmId );
        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
            RestrictTypeTypeType.SITE_TYPE
            && !MemberServiceClass.checkRestrictField( content, RestrictTypeTypeType.SITE_TYPE) )
        {
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            if (log.isDebugEnabled())
                log.debug("#4.09.08 bind long param #" + numParam + ' ' +siteId );

            RsetTools.setLong(ps, numParam++, siteId);

        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
            RestrictTypeTypeType.USER_TYPE
            && !MemberServiceClass.checkRestrictField( content, RestrictTypeTypeType.USER_TYPE) )
        {
            if (log.isDebugEnabled())
                log.debug("#4.09.09  bind long param #" + numParam + " " + userId);

            RsetTools.setLong(ps, numParam++, userId );
        }

        if (log.isDebugEnabled())
            log.debug( "#4.09.10 count of binded parameters " + (numParam-1) );

        return returnID;
    }

    public void prepareBigtextData(DatabaseAdapter dbDyn, Object idRec, boolean isDelete)
        throws Exception
    {
        if (log.isDebugEnabled()) log.debug("#88.01.00 " + idRec);

        // looking for bigtext field
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++)
        {
            FieldsType ff = content.getQueryArea().getFields(k);

            // if isShow and this field is bigtext then process this field
            if (Boolean.TRUE.equals(ff.getIsShow()) && ff.getJspType().getType() == FieldsTypeJspTypeType.BIGTEXT_TYPE)
            {
                if (log.isDebugEnabled()) log.debug("BigText field - " + ff.getName());

                String insertString =
                    RequestTools.getString(
                        renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff)
                    );

                String nameTargetField = null;
                for (int j = 0; j < ff.getQueryArea().getFieldsCount(); j++){
                    FieldsType field = ff.getQueryArea().getFields(j);
                    if (Boolean.TRUE.equals(field.getIsShow()) ){
                        nameTargetField = field.getName();
                        break;
                    }
                }

                if (nameTargetField == null){
                    log.error("Name field for store data not found.");
                    throw new Exception("Name field for store data not found.");
                }

                DatabaseManager.insertBigText(
                    dbDyn,
                    idRec, content.getQueryArea().getPrimaryKey(),
                    PrimaryKeyTypeTypeType.valueOf(content.getQueryArea().getPrimaryKeyType().toString()),
                    ff.getQueryArea().getTable(0).getTable(), ff.getQueryArea().getPrimaryKey(),
                    nameTargetField,
                    insertString,
                    isDelete
                );
            }
        }
    }

    public void deleteBigtextData(DatabaseAdapter dbDyn, Object idRec)
        throws Exception
    {
        if (log.isDebugEnabled()) log.debug("#88.01.00 " + idRec);

        // looking for bigtext field
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++){
            FieldsType ff = content.getQueryArea().getFields(k);

            // if bigtext then process this field
            if (ff.getJspType().getType() == FieldsTypeJspTypeType.BIGTEXT_TYPE){
                if (log.isDebugEnabled()) log.debug("BigText field - " + ff.getName());

                DatabaseManager.deleteFromBigTable(
                    dbDyn,
                    ff.getQueryArea().getTable(0).getTable(),
                    content.getQueryArea().getPrimaryKey(),
                    PrimaryKeyTypeTypeType.valueOf(content.getQueryArea().getPrimaryKeyType().toString()),
                    idRec
                );
            }
        }
    }

    public void bindDelete(PreparedStatement ps)
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or QueryAreaType is null.");

        int numParam = 1;

        switch (db_.getFamaly())
        {
            case DatabaseManager.MYSQL_FAMALY:
                break;
            default:
                numParam = MemberServiceClass.bindSubQueryParam(ps, numParam, this.fromParam, db_, renderRequest.getParameterMap(), renderRequest.getServerName(), renderRequest.getRemoteUser(), moduleManager );
                break;
        }

        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
        {
            RsetTools.setLong(ps, numParam++, PortletService.getLong(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()) );
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
        {
            ps.setString(numParam++, RequestTools.getString(renderRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()));
        }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
        else
            throw new Exception("Wrong type of primary key");


        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            RsetTools.setLong(ps, numParam++,
                PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L)
            );
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE){
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE){
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }
    }

    public void bindUpdate( final DatabaseAdapter dbDyn, final PreparedStatement ps, final Object returnIdPK, final boolean isUsePrimaryKey )
        throws Exception
    {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or content.QueryAreaType is null.");

        int numParam = 1;

        // Если есть хоть одно поле YES_1_NO_N и этот запрос выполняет установку данного поля в false
        // то подразумевается что поле установится в 0 в момент построения SQL запроса
        if (isUsePrimaryKey) {
            String stringParam = null;
            Double doubleParam = null;
            Long longParam = null;
            for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
                FieldsType ff = content.getQueryArea().getFields(k);
                if (Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)) {
                    switch (ff.getJspType().getType()) {
                        case FieldsTypeJspTypeType.TEXT_TYPE:
                        case FieldsTypeJspTypeType.TEXT_AREA_TYPE:

                            stringParam =
                                RequestTools.getString(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));
                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setString(ps, numParam++, stringParam);
                            break;

                        case FieldsTypeJspTypeType.DATE_TEXT_TYPE:

                            stringParam =
                                RequestTools.getString(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            if (stringParam.length()==0)
                                ps.setNull( numParam++, Types.DATE );
                            else
                            {
                                java.util.Date updateDate =
                                        DateTools.getDateWithMask( stringParam, ff.getMask() );

                                dbDyn.bindDate(ps, numParam++, new java.sql.Timestamp( updateDate.getTime()) );
                            }
                            break;

                        case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                            longParam = PortletService.getLong(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setLong(ps, numParam++, longParam);
                            break;

                        case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                        case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:

                            doubleParam =
                                PortletService.getDouble(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + doubleParam);

                            RsetTools.setDouble(ps, numParam++, doubleParam);
                            break;

                        default:
                            stringParam = RequestTools.getString(renderRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setString(ps, numParam++, stringParam);
                    }
                }
            }
        }

        switch (db_.getFamaly()) {
            case DatabaseManager.MYSQL_FAMALY:
                break;
            default:
                numParam = MemberServiceClass.bindSubQueryParam(ps, numParam, this.fromParam, dbDyn, renderRequest.getParameterMap(), renderRequest.getServerName(), renderRequest.getRemoteUser(), moduleManager );
                break;
        }

        if ( isUsePrimaryKey ) {
            if (log.isDebugEnabled())
                log.debug("Param  #" + numParam + ", value: " + returnIdPK);

            if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
                RsetTools.setLong(ps, numParam++, ((Long) returnIdPK) );
            }
            else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
                ps.setString(numParam++, (String) returnIdPK);
            }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
            else
                throw new Exception("Wrong type of primary key");
        }


        if (mod.getSelfLookup() != null && mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            final Long longParam = PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L );

            if (log.isDebugEnabled())
                log.debug("Param  #" + numParam + ", value: " + longParam);

            RsetTools.setLong(ps, numParam++,
                longParam
            );
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + renderRequest.getRemoteUser());

                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + renderRequest.getServerName());

                    ps.setString(numParam++, renderRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + renderRequest.getRemoteUser());

                    ps.setString(numParam++, renderRequest.getRemoteUser());
                    break;
            }
        }
    }

    public String addLookupURL(boolean asURL)
        throws Exception
    {
        return addLookupURL(asURL, false);
    }

    public String addLookupURL(boolean asURL, boolean isCurrFrom)
        throws Exception
    {
        String from = fromParam;

        StringTokenizer st = new StringTokenizer(fromParam, ",");

        String s = "";
        if (st.hasMoreTokens())
        {
            if (asURL)
                s += MemberConstants.MEMBER_FROM_PARAM + '=' + from;
            else
                s += buildHiddenForm(MemberConstants.MEMBER_FROM_PARAM, from);
        }

        if (isCurrFrom && asURL)
        {
            if (s.length() == 0)
                s += MemberConstants.MEMBER_FROM_PARAM + '=' + mod.getName();
            else
                s += ',' + mod.getName();
        }

        if (asURL && s.length() > 0)
            s += "&";

        while (st.hasMoreTokens())
        {
            String modName = st.nextToken();
            ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);

            if (cnt != null && cnt.getQueryArea() != null)
            {
                String pkValue = null;
                if (cnt.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
                {
                    pkValue = "" + PortletService.getLong(renderRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                }
                else if (cnt.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
                {
                    pkValue = RequestTools.getString(renderRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
}
*/
                else
                    throw new Exception("Wrong type of primary key");

                if (asURL)
                    s += modName + '.' + cnt.getQueryArea().getPrimaryKey() + '=' + pkValue + '&';
                else
                    s += buildHiddenForm(modName + '.' + cnt.getQueryArea().getPrimaryKey(), pkValue);
            }
        }

        return s;
    }

    public String checkRecursiveCall()
        throws Exception
    {
        if (fromParam == null || fromParam.length() == 0)
            return null;

        StringTokenizer st = new StringTokenizer(fromParam, ",");
        String str = null;
        String strCheck = null;
        int idx = 1;
        ContentType cnt = null;
        while (st.hasMoreTokens())
        {
            str = st.nextToken();

            ModuleType module = moduleManager.getModule(str);
            if (module == null)
            {
                log.warn("checkRecursiveCall. module is null. Str - " + str);
                return MemberConstants.MEMBER_FROM_PARAM + " section is incorrect. ";
            }

            cnt = module.getContent(ContentTypeActionType.INDEX_TYPE);
            if (cnt == null)
                return "ContentType \"index\" not found in \"" + str + "\" module.";

            if (cnt.getQueryArea().getPrimaryKey() == null)
                return "ContentType \"index\" in \"" + str + "\" module not containt PK.";

            if (renderRequest.getParameter(str + '.' + cnt.getQueryArea().getPrimaryKey()) == null)
                return "Parameter " + str + '.' + cnt.getQueryArea().getPrimaryKey() + " not specified.";

            StringTokenizer stCheck = new StringTokenizer(fromParam, ",");
            int idxCheck = 1;
            while (stCheck.hasMoreTokens())
            {
                strCheck = stCheck.nextToken();

// log.debug("#1.003.001 idx: "+idx+" idxCheck: "+idxCheck);
// log.debug("#1.003.005 str: "+str);
// log.debug("#1.003.007 strCheck: "+strCheck);

                if (idx != idxCheck && str.equalsIgnoreCase(strCheck))
                    return "Recursive call not allowed.";

                idxCheck++;
            }
            idx++;
        }

        return null;
    }

    public String buildAddURL()
        throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("Start build add URL");

        for (int i = 0; i < content.getTargetModuleCount(); i++)
        {
            TargetModuleType ta = content.getTargetModule(i);

            if (ta.getAction().getType() == TargetModuleTypeActionType.INSERT_TYPE)
            {

                if (log.isDebugEnabled())
                    log.debug("Build insert URL " + ta.getModule() + ' ' + ta.getAction());

                String s = "<a href=\"" +
                    thisURI +
                    MemberConstants.MEMBER_MODULE_PARAM + '=' + mod.getName() + '&' +
                    MemberConstants.MEMBER_ACTION_PARAM + "=insert&" +
                    addLookupURL(true);


                if (mod.getSelfLookup() != null &&
                    mod.getSelfLookup().getCurrentField() != null &&
                    mod.getSelfLookup().getTopField() != null)
                {
                    if (!s.endsWith("&"))
                        s += "&";

                    s += (
                        mod.getName() + '.' + mod.getSelfLookup().getTopField().getName() +
                        '=' +
                        PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L) +
                        '&'
                        );
                }

                return s + "\">" + MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "</a>";
            }
        }
        return "";
    }

    private String buildChangeButton(ResultSet rs)
        throws Exception
    {
        String s = "";
        if (rs == null)
            return "";

        String pkID = null;

        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
            pkID = "" + RsetTools.getLong(rs, content.getQueryArea().getPrimaryKey());
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
            pkID = RsetTools.getString(rs, content.getQueryArea().getPrimaryKey());
        }

/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);

}
*/
        else
            throw new Exception("Wrong type of primary key");


        for (int i = 0; i < content.getTargetModuleCount(); i++)
        {
            String sl = null;
            if (mod.getSelfLookup() != null &&
                mod.getSelfLookup().getCurrentField() != null &&
                mod.getSelfLookup().getTopField() != null)
            {
                sl = (
                    mod.getName() + '.' + mod.getSelfLookup().getTopField().getName() +
                    '=' +
                    PortletService.getLong(renderRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L) +
                    '&'
                    );
            }
            else
                sl = "";

            TargetModuleType ta = content.getTargetModule(i);
            switch (ta.getAction().getType())
            {
                case TargetModuleTypeActionType.CHANGE_TYPE:
                    if (isChangeAccess)
                    {
                        s += "<input type=\"button\" value=\"" +
                            MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "\" onclick=\"location.href='" +
                            thisURI +
                            sl +
                            MemberConstants.MEMBER_MODULE_PARAM + '=' + mod.getName() + '&' +
                            MemberConstants.MEMBER_ACTION_PARAM + '=' + TargetModuleTypeActionType.CHANGE.toString() + '&' +
                            addLookupURL(true) +
                            mod.getName() + '.' + content.getQueryArea().getPrimaryKey() + "=" + pkID + "';\">\n";
                    }
                    break;
                case TargetModuleTypeActionType.DELETE_TYPE:
                    if (isDeleteAccess)
                    {
                        s += "<input type=\"button\" value=\"" +
                            MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "\" onclick=\"location.href='" +
                            thisURI +
                            sl +
                            MemberConstants.MEMBER_MODULE_PARAM + '=' + mod.getName() + '&' +
                            MemberConstants.MEMBER_ACTION_PARAM + '=' + TargetModuleTypeActionType.DELETE.toString() + '&' +
                            addLookupURL(true) +
                            mod.getName() + '.' + content.getQueryArea().getPrimaryKey() + "=" + pkID + "';\">\n";
                    }
                    break;
                case TargetModuleTypeActionType.SELF_TYPE:
                    if (mod.getSelfLookup() == null || mod.getSelfLookup().getCurrentField() == null || mod.getSelfLookup().getTopField() == null)
                        throw new Exception("<SelfLookup> element not correctly defined");

                    Enumeration e = null;
                    String param = "";
                    if (renderRequest.getParameter(mod.getName() + '.' + mod.getSelfLookup().getTopField().getName()) == null)
                    {
                        param = (String)renderRequest.getAttribute( ContainerConstants.PORTAL_QUERY_STRING_ATTRIBUTE );
                        if (!param.endsWith("&"))
                            param += "&";

                        param += (
                            mod.getName() + '.' + mod.getSelfLookup().getTopField().getName() +
                            '=' +
                            RsetTools.getLong(rs, mod.getSelfLookup().getCurrentField().getName(), 0L) +
                            '&'
                            );
                    }
                    else
                    {
                        for (e = renderRequest.getParameterNames(); e.hasMoreElements();)
                        {
                            String p = (String) e.nextElement();
                            param += (p + '=');
                            if (p.equals(mod.getName() + '.' + mod.getSelfLookup().getTopField().getName()))
                            {
                                param += RsetTools.getLong(rs, mod.getSelfLookup().getCurrentField().getName(), 0L) + "&";
                            }
                            else
                            {
                                param += renderRequest.getParameter(p) + '&';
                            }
                        }
                    }

//log.debug("#5.05.01 "+param);

                    s += "<input type=\"button\" value=\"" +
                        MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "\" onclick=\"location.href='" +
                        thisURI +
                        param + "';\">\n";
                    break;
                case TargetModuleTypeActionType.LOOKUP_TYPE:
                    s += "<input type=\"button\" value=\"" +
                        MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "\" onclick=\"location.href='" +
                        thisURI +
                        sl +
                        MemberConstants.MEMBER_MODULE_PARAM + '=' + ta.getModule() + '&' +
                        MemberConstants.MEMBER_ACTION_PARAM + '=' + TargetModuleTypeActionType.INDEX.toString() + '&' +
                        addLookupURL(true, true) +
                        mod.getName() + '.' + content.getQueryArea().getPrimaryKey() + '=' + pkID + "';\">\n";
                    break;

                // index и insert action не обрабатываются - для них не делаются кнопки
                case TargetModuleTypeActionType.INDEX_TYPE:
                case TargetModuleTypeActionType.INSERT_TYPE:
                    break;
                default:
                    log.error("Wrong type of action " + ta.getAction().toString());
                    throw new Exception("Wrong type of action " + ta.getAction().toString());
            }
        }
        return s;

    }

    private String buildHiddenForm(String param_, String value_)
    {
        return "<input type=\"hidden\" name=\"" + param_ + "\" value=\"" + value_ + "\">\n";
    }

    public String buildAddCommitForm()
        throws Exception
    {
        if (content.getAction().getType() != ContentTypeActionType.INSERT_TYPE)
            return "";

        String s = null;
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            s =
                buildHiddenForm(
                    mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(),
                    "" + PortletService.getLong(
                        renderRequest,
                        mod.getName() + '.' +
                        mod.getSelfLookup().getTopField().getName(), 0L
                    )
                );
        }
        else
            s = "";


        return "<form method=\"POST\" action=\"" + commitURI + "\">\n" +
            buildHiddenForm(MemberConstants.MEMBER_MODULE_PARAM, mod.getName()) +
            buildHiddenForm(MemberConstants.MEMBER_ACTION_PARAM, ContentTypeActionType.INSERT.toString()) +
            buildHiddenForm(MemberConstants.MEMBER_SUBACTION_PARAM, "commit") +
            addLookupURL(false) +
            s;
    }

    public String buildUpdateCommitForm()
        throws Exception
    {
        if (content.getAction().getType() != ContentTypeActionType.CHANGE_TYPE)
            return "";

        String s = null;
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            s =
                buildHiddenForm(mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(),
                    "" + PortletService.getLong(renderRequest,
                        mod.getName() + '.' +
                mod.getSelfLookup().getTopField().getName(), 0L
                    )
                );
        }
        else
            s = "";


        String pkValue = null;
        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
        {
            pkValue = "" + PortletService.getLong(renderRequest, mod.getName() + '.' + content.getQueryArea().getPrimaryKey());
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
        {
            pkValue = RequestTools.getString(renderRequest, mod.getName() + '.' + content.getQueryArea().getPrimaryKey());
        }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
}
*/
        else
            throw new Exception("Wrong type of primary key");

        return "<form method=\"POST\" action=\"" + commitURI + "\">\n" +
            buildHiddenForm(MemberConstants.MEMBER_MODULE_PARAM, mod.getName()) +
            buildHiddenForm(MemberConstants.MEMBER_ACTION_PARAM, ContentTypeActionType.CHANGE.toString()) +
            buildHiddenForm(MemberConstants.MEMBER_SUBACTION_PARAM, "commit") +
            addLookupURL(false) +
            buildHiddenForm(mod.getName() + '.' + content.getQueryArea().getPrimaryKey(), pkValue) +
            s;
    }

    public String buildDeleteCommitForm()
        throws Exception
    {
        if (content.getAction().getType() != ContentTypeActionType.DELETE_TYPE)
            return "";

        String s = null;
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            s =
                buildHiddenForm(mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(),
                    "" + PortletService.getLong(renderRequest,
                        mod.getName() + '.' +
                mod.getSelfLookup().getTopField().getName(), 0L
                    )
                );
        }
        else
            s = "";

        String pkValue = null;
        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE)
        {
            pkValue = "" + PortletService.getLong(renderRequest, mod.getName() + '.' + content.getQueryArea().getPrimaryKey());
        }
        else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE)
        {
            pkValue = RequestTools.getString(renderRequest, mod.getName() + '.' + content.getQueryArea().getPrimaryKey());
        }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().getPrimaryKeyMask()==null ||
content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
}
*/
        else
            throw new Exception("Wrong type of primary key");

        return "<form method=\"POST\" action=\"" + commitURI + "\">\n" +
            buildHiddenForm(MemberConstants.MEMBER_MODULE_PARAM, mod.getName()) +
            buildHiddenForm(MemberConstants.MEMBER_ACTION_PARAM, ContentTypeActionType.DELETE.toString()) +
            buildHiddenForm(MemberConstants.MEMBER_SUBACTION_PARAM, "commit") +
            addLookupURL(false) +
            buildHiddenForm(mod.getName() + '.' + content.getQueryArea().getPrimaryKey(), pkValue) +
            s;
    }

    public String getIndexURL()
        throws Exception
    {
        String s = null;
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null)
        {
            s = mod.getName() + '.' + mod.getSelfLookup().getTopField().getName() + '=' +
                PortletService.getLong(renderRequest,
                    mod.getName() + '.' +
                mod.getSelfLookup().getTopField().getName(), 0L
                ) + '&';
        }
        else
            s = "";

        return thisURI +
            s +
            MemberConstants.MEMBER_MODULE_PARAM + '=' + mod.getName() + '&' +
            MemberConstants.MEMBER_ACTION_PARAM + '=' + ContentTypeActionType.INDEX + '&' +
            addLookupURL(true);
    }

    public String buildIndexURL()
        throws Exception
    {
        for (int i = 0; i < content.getTargetModuleCount(); i++)
        {
            TargetModuleType ta = content.getTargetModule(i);
            if (ta.getAction().getType() == TargetModuleTypeActionType.INDEX_TYPE)
            {
                return "<a href=\"" + getIndexURL() + "\">" +
                    MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "</a>";
            }
        }
        return "";
    }

    public String buildMainIndexURL()
        throws Exception
    {

        if (fromParam == null || fromParam.length() == 0)
            return "";

        StringTokenizer st = new StringTokenizer(fromParam, ",");
        String nameModule = st.nextToken();
        ContentType tempCnt = moduleManager.getContent(nameModule, ContentTypeActionType.INDEX_TYPE);

        for (int i=0; tempCnt!=null && i<tempCnt.getTargetModuleCount(); i++){
            TargetModuleType ta = tempCnt.getTargetModule(i);
            if (ta.getAction().getType() == TargetModuleTypeActionType.INDEX_TYPE){
                String tempStr = thisURI +
                    MemberConstants.MEMBER_MODULE_PARAM + '=' + nameModule + '&' +
                    MemberConstants.MEMBER_ACTION_PARAM + '=' + ContentTypeActionType.INDEX + '&';

                return "<a href=\"" + tempStr + "\">" +
                    MemberServiceClass.getString(ta.getTargetModuleName(), renderRequest.getLocale()) + "</a>";
            }
        }

        return "";
    }

    private String buildLookupSQL(QueryAreaType qa)
        throws Exception
    {
        String sql_ = "select ";
        boolean isAppend = false;
        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (!isAppend)
                isAppend = true;
            else
                sql_ += ',';
            sql_ += (ff.getRefTable() != null ? ff.getRefTable() + '.' : "") + ff.getName();
        }
        isAppend = false;
        String fromSQL = "";
        for (int k = 0; k < qa.getTableCount(); k++)
        {
            TableType table = qa.getTable(k);
            if (!isAppend)
            {
                isAppend = true;
                fromSQL += " from ";
            }
            else
                fromSQL += ',';

            fromSQL += table.getTable() + (table.getRef() != null ? ' ' + table.getRef() : "");
        }

        String whereSQL = (qa.getWhere() != null ? " where " + qa.getWhere() : "");

        if (log.isDebugEnabled())
        {
            log.debug("#2.001.004 " + whereSQL + ' ' + qa.getWhere());
            log.debug("#2.003.003 " + qa);
        }

        if (qa.getRestrict() != null &&
            qa.getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
        {
            if (whereSQL == null || whereSQL.length() == 0)
                whereSQL += " where ";
            else
                whereSQL += " and ";

            switch (db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    whereSQL += qa.getMainRefTable() +
                        ".ID_FIRM in ("+idList+") ";

                    break;
                default:
                    fromSQL += ", v$_read_list_firm " + aliasFirmRestrict + ' ';

                    whereSQL += qa.getMainRefTable() +
                        ".ID_FIRM=" + aliasFirmRestrict +
                        ".ID_FIRM and " + aliasFirmRestrict + ".user_login = ? ";
                    break;
            }
        }

        if (qa.getRestrict()!=null && qa.getRestrict().getType().getType()==RestrictTypeTypeType.SITE_TYPE) {
            fromSQL += ", WM_PORTAL_VIRTUAL_HOST " + aliasSiteRestrict + ' ';
            if ("".equals(whereSQL))
                whereSQL += " where ";
            else
                whereSQL += " and ";

            whereSQL += qa.getMainRefTable() +
                ".ID_SITE=" + aliasSiteRestrict +
                ".ID_SITE and " + aliasSiteRestrict + ".NAME_VIRTUAL_HOST = lower(?) ";
        }

        if (qa.getRestrict() != null &&
            qa.getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            throw new Exception("Not implemented block #44.12");
        }

        isAppend = false;
        String orderSQL = "";

        for (int k = 0; k < qa.getOrderCount(); k++)
        {
            SortOrderType order = qa.getOrder(k);
            if (!isAppend)
                isAppend = true;
            else
                orderSQL += ',';

            orderSQL += order.getField() + ' ' +
                (order.getDirection() != null ? order.getDirection().toString() : "");
        }

        if (orderSQL.length() != 0)
            orderSQL = " order by " + orderSQL;

        if (log.isDebugEnabled())
        {
            log.debug("#22.05.07 " + sql_ + fromSQL + whereSQL + orderSQL);
            log.debug("#22.05.09 " + whereSQL);
        }

        return sql_ + fromSQL + whereSQL + orderSQL;
    }

    private String getRsetSelectSQL(QueryAreaType qa)
        throws Exception
    {
        boolean isAppend = false;
        String sql_ = "select ";

        // process list of fiels in QueryArea
        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (!isAppend)
                isAppend = true;
            else
                sql_ += ',';
            sql_ += (ff.getRefTable() != null ? ff.getRefTable() + '.' : "") + ff.getName();
        }

        isAppend = false;
        String fromSQL = "";

        // process list of tables
        for (int k = 0; k < qa.getTableCount(); k++)
        {
            TableType table = qa.getTable(k);
            if (!isAppend)
            {
                isAppend = true;
                fromSQL += " from ";
            }
            else
                fromSQL += ',';

            fromSQL += table.getTable() + (table.getRef() != null ? ' ' + table.getRef() : "");
        }

        // put 'where' clause with primary key in query
        String whereSQL = " where " +
            (qa.getMainRefTable() != null ? qa.getMainRefTable() + '.' : "") +
            qa.getPrimaryKey() + "=? ";

        String orderSQL = "";

        return sql_ + fromSQL + whereSQL + orderSQL;
    }

    private String getRsetBigtextSQL(QueryAreaType qa)
        throws Exception
    {
        String sql_ = "select ";
        boolean isAppend = false;
        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (!isAppend)
                isAppend = true;
            else
                sql_ += ',';
            sql_ += (ff.getRefTable() != null ? ff.getRefTable() + '.' : "") + ff.getName();
        }
        isAppend = false;
        String fromSQL = "";
        for (int k = 0; k < qa.getTableCount(); k++)
        {
            TableType table = qa.getTable(k);
            if (!isAppend)
            {
                isAppend = true;
                fromSQL += " from ";
            }
            else
                fromSQL += ',';

            fromSQL += table.getTable() + (table.getRef() != null ? ' ' + table.getRef() : "");
        }

        String whereSQL = " where " +
            (qa.getMainRefTable() != null ? qa.getMainRefTable() + '.' : "") +
            content.getQueryArea().getPrimaryKey() + "=? ";

        return sql_ +
            fromSQL +
            whereSQL +
            " order by " + qa.getPrimaryKey() + " asc ";
    }

    private String buildSelectList(QueryAreaType qa, String nameSelect, boolean isEdit)
        throws Exception
    {
        return buildSelectList(qa, (String)null, nameSelect, isEdit);
    }

    private String buildSelectList(QueryAreaType qa, Long id, String nameSelect, boolean isEdit)
        throws Exception
    {
        return buildSelectList(qa, id!=null?id.toString():null, nameSelect, isEdit);
    }

    private String buildSelectList(QueryAreaType qa, String id_, String nameSelect, boolean isEdit)
        throws Exception
    {
        String r = "";
        String sql_ = buildLookupSQL(qa);

        if (log.isDebugEnabled())
            log.debug("#22.05.01 " + sql_);

        PreparedStatement ps = null;
        ResultSet rs = null;
        int numParam = 1;
        try
        {
            ps = db_.prepareStatement(sql_);

            if (qa.getRestrict() != null &&
                qa.getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
            {
                switch (db_.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:
                        break;
                    default:
                        ps.setString(numParam++, renderRequest.getRemoteUser());
                        break;
                }
            }

            if (qa.getRestrict() != null &&
                qa.getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
                ps.setString(numParam++, renderRequest.getServerName());

            if (qa.getRestrict() != null &&
                qa.getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
                throw new Exception("Not tested block #44.10");

            rs = ps.executeQuery();

            String v_val;
            String v_str;
            String v_select;

            while (rs.next())
            {
                switch (qa.getPrimaryKeyType().getType())
                {
                    case PrimaryKeyTypeType.NUMBER_TYPE:
                        v_val = ""+RsetTools.getLong(rs, qa.getPrimaryKey());
                        break;
                    case PrimaryKeyTypeType.STRING_TYPE:
                        v_val = RsetTools.getString(rs, qa.getPrimaryKey());
                        break;
                    case PrimaryKeyTypeType.DATE_TYPE:
                        Calendar cal = RsetTools.getCalendar(rs, qa.getPrimaryKey(), null);
                        if (cal==null)
                            throw new Exception("Error get Calendar for field "+qa.getPrimaryKey());
                        v_val = DateTools.getStringDate(cal, qa.getPrimaryKeyMask(), renderRequest.getLocale() );
                        break;
                    default:
                        throw new Exception("Unknown type of primary key");
                }

//                if (log.isDebugEnabled())
//                {
//                    log.debug( "qa.getPrimaryKey() - "+qa.getPrimaryKey() );
//                    log.debug( "value of "+qa.getPrimaryKey() + " "+ v_val );
//                }

                v_str = "";
                boolean isComma = false;
                for (int k = 0; k < qa.getFieldsCount(); k++)
                {
                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        if (!isComma)
                            isComma = true;
                        else
                            v_str += ',';


                        switch (ff.getJspType().getType())
                        {
                            case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                                v_str += getDateTextCell(rs, ff, "");
                                break;
                            case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                            case FieldsTypeJspTypeType.TEXT_TYPE:
                                v_str += RsetTools.getString(rs, MemberServiceClass.getRealName(ff));
                                break;

                            case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                            case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                                v_str += RsetTools.getDouble(rs, MemberServiceClass.getRealName(ff));
                                break;

                            case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                                v_str += RsetTools.getLong(rs, MemberServiceClass.getRealName(ff));
                                break;

                            default:
                                throw new Exception("Type of field '"+ff.getJspType().toString()+"' not implemented");
                        }
                    }
                }
                if (isEdit)
                {
                    if (id_ != null && v_val.equals(id_))
                        v_select = " SELECTED";
                    else
                        v_select = "";

                    r += ("<OPTION" + v_select + " value=\"" +
                        v_val + "\">" + v_str.replace('\"', '\'') +
                        "</OPTION>\n");

                }
                else
                {
                    if (id_ != null && v_val.equals(id_))
                        r = v_str.replace('\"', '\'');
                }

            }
        }
        catch (Exception e) {
            log.error("Error buildSelectList.\n" + sql_, e);
            throw e;
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        if (isEdit)
            return "<select name=\"" + nameSelect + "\">\n" + r + "</select>\n";
        else
            return r;
    }

    /**
     * return string with value of fields from list
     * @param qa
     * @param id_
     * @return
     * @throws Exception
     */
    private String getRsetSelectValue(QueryAreaType qa, Object id_)
        throws Exception
    {
        if (id_==null)
            return null;

        String sql_ = getRsetSelectSQL(qa);

        if (log.isDebugEnabled()) log.debug("sql: "+ sql_+"\nid: "+id_ );

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);
            ps.setObject(1, id_);

            rs = ps.executeQuery();

            String v_str = "";

            if (rs.next())
            {
                boolean isComma = false;
                for (int k = 0; k < qa.getFieldsCount(); k++)
                {
                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        if (!isComma)
                            isComma = true;
                        else
                            v_str += ',';

                        Object sTemp = null;
                        switch (ff.getJspType().getType())
                        {
                            case FieldsTypeJspTypeType.TEXT_TYPE:
                            case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                                sTemp = RsetTools.getString(rs, MemberServiceClass.getRealName(ff), "");
                                if (sTemp!=null)
                                    v_str += (""+sTemp);
                                break;

                            case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                            case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                                sTemp = RsetTools.getDouble(rs, MemberServiceClass.getRealName(ff));
                                if (sTemp!=null)
                                    v_str += (""+sTemp);
                                break;

                            case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                                sTemp = RsetTools.getLong(rs, MemberServiceClass.getRealName(ff));
                                if (sTemp!=null)
                                    v_str += (""+sTemp);
                                break;

                            case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                                v_str += getDateTextCell(rs, ff, "");
                                break;

                            case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
                            case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:
                                v_str += MemberTools.printYesNo(rs, MemberServiceClass.getRealName(ff), false, bundle );
                                break;
                            case FieldsTypeJspTypeType.CHECKBOX_SWITCH_TYPE:
                                if ( 1==RsetTools.getInt(rs, MemberServiceClass.getRealName(ff), 0) )
                                    v_str += "X";
                                else
                                    v_str += "-";
                                break;
                            case FieldsTypeJspTypeType.LOOKUP_TYPE:
                                sTemp = getRsetSelectValue(
                                    ff.getQueryArea(),
                                    RsetTools.getLong(rs, MemberServiceClass.getRealName(ff))
                                );
                                if (sTemp!=null)
                                    v_str += (""+sTemp);

                                break;

                            case FieldsTypeJspTypeType.LOOKUPCLASS_TYPE:
                                v_str += getSelectListFromClass(rs, content.getQueryArea() , ff, false);
//                log.error("LookupClass field can't request directly from getCellValue()");
//                throw new Exception("LookupClass field can't request directly from getCellValue()");
                                break;

                            case FieldsTypeJspTypeType.BIGTEXT_TYPE:
//log.debug("#12.001 processing bigtext field "+RsetTools.getLong(rs, content.getQueryArea().getPrimaryKey()) );

                                String primaryKeyValue = null;
                                int primaryKeyType = content.getQueryArea().getPrimaryKeyType().getType();
                                switch (primaryKeyType)
                                {
                                    case PrimaryKeyTypeType.NUMBER_TYPE:
                                        primaryKeyValue = "" + RsetTools.getLong(rs, content.getQueryArea().getPrimaryKey());
                                        break;
                                    case PrimaryKeyTypeType.STRING_TYPE:
                                        primaryKeyValue = RsetTools.getString(rs, content.getQueryArea().getPrimaryKey());
                                        break;
                                    case PrimaryKeyTypeType.DATE_TYPE:
                                        if (content.getQueryArea().getPrimaryKeyMask() == null ||
                                            content.getQueryArea().getPrimaryKeyMask().trim().length() == 0)
                                            throw new Exception("date mask for primary key not specified");

                                        primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                                            content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
                                        break;
                                    default:
                                        log.error("Wrong type of primary key");
                                        throw new Exception("Wrong type of primary key");
                                }
                                v_str += getRsetBigtextValue(ff.getQueryArea(), primaryKeyValue);

                                break;
                            case FieldsTypeJspTypeType.BIGTEXT_LOB_TYPE:
                                v_str += db_.getClobField(rs, MemberServiceClass.getRealName(ff));

                                break;
                            default:
                                log.error("Error of field type - " + ff.getJspType().toString());
                                throw new Exception("Error of field type - " + ff.getJspType().toString());
                        }

                    }
                }

            }
            return v_str;
        }
        catch(Exception e)
        {
            log.error("Error execute getRsetSelectValue()\n"+sql_+"\nid - "+id_, e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    private String getRsetBigtextValue(QueryAreaType qa, String id_)
        throws Exception
    {
        return getRsetBigtextValue(qa, id_, Integer.MAX_VALUE);
    }

    private String getRsetBigtextValue(QueryAreaType qa, String id_, int maxLength)
        throws Exception
    {
        String sql_ = getRsetBigtextSQL(qa);

//log.debug( "#12.003 Big: "+sql_ );

        PreparedStatement ps = null;
        ResultSet rs = null;
        String v_str = "";
        try
        {
            ps = db_.prepareStatement(sql_);
//log.debug( "#12.004 "+id_ );

            ps.setString(1, id_);

            rs = ps.executeQuery();

            while (rs.next())
            {
                boolean isComma = false;
                for (int k = 0; k < qa.getFieldsCount(); k++)
                {
                    if (v_str.length() >= maxLength)
                    {
                        v_str = v_str.substring(0, maxLength);
                        return v_str;
                    }

                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        if (!isComma)
                            isComma = true;
                        else
                            v_str += ',';

                        v_str += RsetTools.getString(rs, MemberServiceClass.getRealName(ff));
                    }
                }
            }
            ;
            if (v_str.length() >= maxLength)
                v_str = v_str.substring(0, maxLength);

            return v_str;
        }
        catch(Exception e) {
            log.error("Error execute getRsetBigtextValue()\n"+sql_, e);
            throw e;
        }
        finally {
            if (log.isDebugEnabled())log.debug("#12.007 " + v_str.length() + " " + maxLength);
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public String buildAddHTMLTable()
        throws Exception
    {
        QueryAreaType qa = content.getQueryArea();

        String s_ = null;

        s_ = "<table border=\"1\">\n";

        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()))
            {
                s_ += "<tr>\n<td>" +
                    (Boolean.TRUE.equals(ff.getIsNotNull()) ? "<span style=\"color:red\">*</span>" : "");

                if (ff.getNameColumn() != null)
                {
                    s_ += MemberServiceClass.getString(ff.getNameColumn(), renderRequest.getLocale());
                }
                s_ += "</td>\n<td width=\"5%\">&nbsp;</td>\n<td>\n";

                String defValue = (ff.getDefValue() == null ? "" : ff.getDefValue());
                switch (ff.getJspType().getType())
                {
                    case FieldsTypeJspTypeType.LOOKUPCLASS_TYPE:
                        s_ += getSelectListFromClass(null, qa, ff, true);
                        break;

                    case FieldsTypeJspTypeType.LOOKUP_TYPE:
                        if (log.isDebugEnabled())
                        {
                            log.debug("Field type - FieldsTypeJspTypeType.LOOKUP_TYPE");
                            log.debug("mod.getName() - "+mod.getName());
                            log.debug("MemberServiceClass.getRealName(ff) - "+
                                MemberServiceClass.getRealName(ff));
                        }
                        s_ += buildSelectList(ff.getQueryArea(),
                            mod.getName() + '.' + MemberServiceClass.getRealName(ff), true);
                        break;

                    case FieldsTypeJspTypeType.TEXT_TYPE:
                        s_ += "<input type=\"text\" name=\"" +
                            mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                            "\" size=\"" + (( ff.getLength()==null || ff.getLength() > 50) ? 50 : ff.getLength()) + "\" maxlength=\"" +
                            ff.getLength() + "\" value=\"" +
                            StringEscapeUtils.escapeXml(defValue) +
                            "\"" +
                            ">\n";
                        break;

                    case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                        s_ += "<textarea name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                            "\" rows=\"" + ((ff.getLength()!=null?ff.getLength():0) / 50 + 5) + "\" cols=\"50\" maxlength=\"" +
                            ff.getLength() + "\">" +
                            StringEscapeUtils.escapeXml(defValue) +
                            "</textarea>";
                        break;

                    case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
                    case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:
                        s_ +=
                            "<select name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) + "\">\n" +
                            MemberTools.printYesNo(
                                ("1".equals(ff.getDefValue()) ? 1 : 0),
                                true, bundle ) +
                            "</select>\n";
                        break;

                    case FieldsTypeJspTypeType.BIGTEXT_TYPE:
                    case FieldsTypeJspTypeType.BIGTEXT_LOB_TYPE:
                        s_ += "<textarea name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                            "\" rows=\"50\" cols=\"65\">" +
                            StringEscapeUtils.escapeXml(defValue) +
                            "</textarea>";
                        break;

                    case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                    case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                    case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                    case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                        s_ += "<input type=\"text\" name=\"" +
                            mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                            "\" size=\"20\" maxlength=\"20\" value=\"" +
                            defValue + "\"" +
                            ">\n";
                        break;

                    default:
                        log.error("Wrond type of jspType - " + ff.getJspType().toString());
                        throw new Exception("Wrond type of jspType - " + ff.getJspType().toString());
                }
                s_ += "</td>\n</tr>\n";
            }
        }
        s_ += "</table>";
        return s_;
    }

    public String buildUpdateHTMLTable(String sql_)
        throws Exception
    {
        QueryAreaType qa = content.getQueryArea();

        String s_ = "<table border=\"1\">\n";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

            bindSelectForUpdate(ps);

            rs = ps.executeQuery();
            if (rs.next()) {
                if (log.isDebugEnabled()) log.debug("#33.01.01 " + qa.getFieldsCount());

                for (int k = 0; k < qa.getFieldsCount(); k++) {
                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        s_ += "<tr>\n<td>" +
                            (Boolean.TRUE.equals(ff.getIsNotNull()) ? "<span style=\"color:red\">*</span>" : "");

                        if (ff.getNameColumn() != null)
                            s_ += MemberServiceClass.getString(ff.getNameColumn(), renderRequest.getLocale());

                        s_ += "</td>\n<td width=\"5%\">&nbsp;</td>\n";
                        s_ += "<td>\n";

                        switch (ff.getJspType().getType()) {
                            case FieldsTypeJspTypeType.LOOKUP_TYPE:
                                s_ += buildSelectList(ff.getQueryArea(),
                                    RsetTools.getLong(rs, MemberServiceClass.getRealName(ff)),
                                    mod.getName() + '.' + MemberServiceClass.getRealName(ff),
                                    Boolean.TRUE.equals(ff.getIsEdit())
                                );
                                break;

                            case FieldsTypeJspTypeType.LOOKUPCLASS_TYPE:
                                s_ += getSelectListFromClass(rs, qa, ff, true);
                                break;

                            default:

                                String editVal = (ff.getDefValue() == null ? "" : ff.getDefValue());

                                switch (ff.getJspType().getType())
                                {
                                    case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                                    case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                                        Double doubleValue = RsetTools.getDouble(rs, MemberServiceClass.getRealName(ff));
                                        editVal = doubleValue==null?"":doubleValue.toString();
                                        break;

                                    case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                                        Long longValue = RsetTools.getLong(rs,MemberServiceClass.getRealName(ff));
                                        editVal = longValue==null?"":longValue.toString();
                                        break;

                                    case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                                        editVal = getDateTextCell(rs, ff, "");
                                        break;

                                    case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
                                    case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:
                                        s_ +=
                                            "<select name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) + "\">\n" +
                                            MemberTools.printYesNo(rs, MemberServiceClass.getRealName(ff),
                                                true, bundle ) +
                                            "</select>\n";
                                        break;
                                    case FieldsTypeJspTypeType.BIGTEXT_TYPE:
                                        editVal = getCellValue(rs, ff, true);

                                        if (log.isDebugEnabled())
                                            log.debug("#55.001 " + editVal);

                                        break;

                                    default:
                                        editVal = RsetTools.getString(rs,
                                            MemberServiceClass.getRealName(ff), "");
                                }

                                if (Boolean.TRUE.equals(ff.getIsEdit()))
                                {

                                    int inputMaxSize;
                                    int inputSize;
                                    switch (ff.getJspType().getType())
                                    {
                                        case FieldsTypeJspTypeType.TEXT_TYPE:
                                            inputSize = ((ff.getLength()==null || ff.getLength() > 50) ? 50 : ff.getLength());
                                            if (inputSize == 0) inputSize = 20;
                                            inputMaxSize = ((ff.getLength()!=null && ff.getLength() != 0) ? ff.getLength() : 200);

                                            s_ += "<input type=\"text\" name=\"" +
                                                mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                                                "\" size=\"" + inputSize + "\" maxlength=\"" + inputMaxSize +
                                                "\" value=\"" +
                                                StringEscapeUtils.escapeXml(editVal) +
                                                "\">\n";
                                            break;
                                        case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                                            inputMaxSize = ((ff.getLength()!=null && ff.getLength() != 0) ? ff.getLength() : 500);

                                            s_ += "<textarea name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                                                "\" rows=\"" + ((ff.getLength()!=null?ff.getLength():0) / 50 + 5) + "\" cols=\"50\" maxlength=\"" +
                                                inputMaxSize + "\">" +
                                                StringEscapeUtils.escapeXml(editVal) +
                                                "</textarea>";
                                            break;
                                        case FieldsTypeJspTypeType.BIGTEXT_TYPE:
                                            if (log.isDebugEnabled())
                                                log.debug("bigtext - \n" + editVal);

                                            s_ += "<textarea name=\"" + mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                                                "\" rows=\"50\" cols=\"65\">" +
                                                StringEscapeUtils.escapeXml(editVal) +
                                                "</textarea>";
                                            break;
                                        case FieldsTypeJspTypeType.DATE_TEXT_TYPE:
                                            s_ += "<input type=\"text\" name=\"" +
                                                mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                                                "\" size=\"20\" maxlength=\"20\" value=\"" +
                                                editVal + "\"" +
                                                ">\n";
                                            break;
                                        case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                                        case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                                        case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                                            inputSize = ((ff.getLength()==null||ff.getLength() + 1 > 50) ? 50 : ff.getLength());
                                            if (inputSize == 0)
                                                inputSize = 30;
                                            inputMaxSize = ((ff.getLength()!=null && ff.getLength() != 0) ? ff.getLength() : 50);

                                            s_ += "<input type=\"text\" name=\"" +
                                                mod.getName() + '.' + MemberServiceClass.getRealName(ff) +
                                                "\" size=\"" + inputSize +
                                                "\" maxlength=\"" + inputMaxSize + "\" value=\"" +
                                                editVal + "\"" +
                                                ">\n";
                                            break;
                                        case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
                                        case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:
                                            // already processes in above block
                                            break;
                                        default:
                                            log.error("Field '"+mod.getName() + '.' + MemberServiceClass.getRealName(ff)+
                                                "' not processed. Need update code");
                                            throw new Exception("Field '"+mod.getName() + '.' + MemberServiceClass.getRealName(ff)+
                                                "' not processed. Need update code");
                                    }
                                }
                                else // if (Boolean.TRUE.equals(ff.getIsEdit()))
                                {
//log.debug("#7.001.003 "+editVal+' '+editVal.length());
                                    if (editVal.length() == 0)
                                        editVal = "&nbsp;";

                                    s_ += editVal;
                                }
                        }
                        s_ += "</td>\n</tr>\n";
                    }
                } // for( int

            } // if (rs.next())
        }
        catch (Exception e)
        {
            log.error("sql: "+sql_);
            log.error("Error processing buildUpdateHTMLTable.", e);

            s_ = "Exeception: " + e.toString() + "\n<br>" +
                ExceptionTools.getStackTrace(e, linesInException, "<br>");
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        s_ += "</table>";
        return s_;
    }

    private String getSelectListFromClass(ResultSet rs, QueryAreaType qa, FieldsType ff, boolean isEdit)
        throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("Start getSelectListFromClass()");

        ClassQueryType cq = ff.getClassQuery();
        if (cq==null)
            return "";

        if (log.isDebugEnabled())
            log.debug("Create class '"+cq.getNameClass()+"'");

        Object obj = MainTools.createCustomObject( cq.getNameClass() );

        if (log.isDebugEnabled())
            log.debug("Class obj is "+obj);

        for (int k = 0; k < cq.getParametersCount(); k++)
        {
            ClassQueryParameterType param = cq.getParameters(k);

            String setter = param.getMethod();
            Class[] cl = { Class.forName( param.getType().toString() ) };

            if (log.isDebugEnabled())
                log.debug("Create method '"+setter+"' param '"+param.getType().toString()+"'");

            Method method = obj.getClass().getMethod( setter, cl );

            if (method == null)
            {
                log.error( "Error create method '"+setter+"()' for class  " + cq.getNameClass() );
                throw new Exception("Error create method '"+setter+"()' for class  " + cq.getNameClass());
            }

            if (log.isDebugEnabled())
                log.debug("Method is " + method);

            Object[] objArgs = null;

            FieldsType mainField = MemberServiceClass.getField( qa, param.getField() );

            if (log.isDebugEnabled())
            {
                log.debug("mainField is "+mainField);
                if (mainField!=null)
                    log.debug("mainField.getName() "+mainField.getName());
            }

            if ( "java.lang.Long".equals( param.getType().toString() ) )
            {
                Long longValue = RsetTools.getLong(rs, MemberServiceClass.getRealName(mainField));
                Object[] args = { longValue };
                objArgs = args;
            }
            else if ( "java.lang.String".equals( param.getType().toString() ) )
            {
                String stringValue = RsetTools.getString(rs, MemberServiceClass.getRealName(mainField));
                Object[] args = { stringValue };
                objArgs = args;
            }
            else
            {
                log.error("Type of field '"+param.getType()+"' not processed. Need update code.");
                throw new Exception("Type of field '"+param.getType()+"' not processed. Need update code.");
            }

            if (log.isDebugEnabled())
                log.debug("invoke "+method.toString()+" with parameter "+objArgs);

            method.invoke(obj, objArgs);

        }

        BaseClassQuery baseClass = (BaseClassQuery)obj;
        MemberQueryParameter queryParameter = new MemberQueryParameter();
        baseClass.setQueryParameter( queryParameter );

        if (isEdit && Boolean.TRUE.equals(ff.getIsEdit())) {
            String r = "";
            String isSelected = null;
            Iterator iterator = baseClass.getSelectList( renderRequest, bundle ).iterator();
            while(iterator.hasNext()) {
                ClassQueryItem item = (ClassQueryItem)iterator.next();
                if ( item.isSelected() )
                    isSelected = " selected";
                else
                    isSelected = "";

                r += ("<option" + isSelected + " value=\"" +
                    item.getIndex() + "\">" + (item.getValue()==null?"":item.getValue()).replace('\"', '\'') +
                    "</option>\n");
            }
            return
                "<select name=\"" + mod.getName() + '.' +
                MemberServiceClass.getRealName(ff) + "\">\n" +
                r +
                "</select>\n";
        }
        else {
            return baseClass.getCurrentValue( renderRequest, bundle );
        }
    }

    public String buildDeleteHTMLTable(String sql_)
        throws Exception
    {
        QueryAreaType qa = content.getQueryArea();

        String s_ = "<table border=\"1\">\n";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

            bindSelectForDelete(ps);

            rs = ps.executeQuery();
            if (rs.next())
            {

                for (int k = 0; k < qa.getFieldsCount(); k++)
                {
                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        s_ += "<tr>\n<td valign=\"top\">" +
                            (Boolean.TRUE.equals(ff.getIsNotNull()) ? "<span style=\"color:red\">*</span>" : "");

                        if (ff.getNameColumn() != null)
                        {
                            s_ += MemberServiceClass.getString(ff.getNameColumn(), renderRequest.getLocale());
                        }
                        s_ += "</td>\n<td width=\"5%\">&nbsp;</td>\n";
                        s_ += "<td>\n";

                        String editVal = null;

                        if (ff.getJspType().getType() == FieldsTypeJspTypeType.LOOKUP_TYPE)
                        {
                            editVal = getRsetSelectValue(ff.getQueryArea(),
                                RsetTools.getLong(rs, MemberServiceClass.getRealName(ff))
                            );
                            if (editVal == null || editVal.length() == 0)
                                s_ += "&nbsp;";
                            else
                                s_ += editVal;
                        }
                        else
                        {
                            if (log.isDebugEnabled())
                            {
                                log.debug("BuildDeleteHTMLTable. jspType "+ff.getJspType().toString());
                            }

                            Object obj = null;
                            switch (ff.getJspType().getType())
                            {
                                case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                                case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:
                                    obj = RsetTools.getDouble(rs, MemberServiceClass.getRealName(ff));
                                    if (obj!=null)
                                        editVal = obj.toString();
                                    break;

                                case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                                    obj = RsetTools.getLong(rs, MemberServiceClass.getRealName(ff));
                                    if (obj!=null)
                                        editVal = obj.toString();
                                    break;

                                case FieldsTypeJspTypeType.BIGTEXT_TYPE:

                                    editVal = StringTools.truncateString(
                                        getCellValue(rs, ff, false),
                                        1000);

                                    if (editVal == null)
                                        editVal = "";

//                                    if (log.isDebugEnabled())
//                                        log.debug("#12.0004 "+editVal);

                                    break;

                                case FieldsTypeJspTypeType.DATE_TEXT_TYPE:

                                    editVal = getDateTextCell(rs, ff, "");
                                    if (obj!=null)
                                        editVal = obj.toString();
                                    break;

                                case FieldsTypeJspTypeType.YESNO_SWITCH_TYPE:
                                case FieldsTypeJspTypeType.YES_1_NO_N_TYPE:

                                    editVal = MemberTools.printYesNo(rs,
                                        MemberServiceClass.getRealName(ff), false, bundle );
                                    break;

                                default:
                                    editVal = RsetTools.getString(rs,
                                        MemberServiceClass.getRealName(ff));

                            }

                            if (editVal==null || editVal.trim().length()==0)
                                editVal = "&nbsp;";
                            else
                                editVal = StringTools.toPlainHTML( editVal );

                            s_ += editVal;
                        }
                        s_ += "</td>\n</tr>\n";
                    }
                } // for( int

            } // if (rs.next())
        }
        catch (Exception e)
        {
            final String es = "Error processing buildDeleteHTMLTable.";
            log.error(es, e);
            throw new MemberException( es, e );
//            s_ = "Exeception: " + e.toString() + "\n<br>" +
//                ExceptionTools.getStackTrace(e, linesInException, "<br>");
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        s_ += "</table>";
        return s_;
    }

    public String buildSelectHTMLTable(String sql_) throws Exception {
        QueryAreaType qa = content.getQueryArea();
        String s_ = null;

        s_ = "<table>\n<tr>\n";

        // output header
        for (int k = 0; k < qa.getFieldsCount(); k++)
        {
            FieldsType ff = qa.getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()))
            {
                s_ += "<th class=\"memberArea\">\n";
                if (ff.getNameColumn() != null)
                {
                    s_ += MemberServiceClass.getString(ff.getNameColumn(), renderRequest.getLocale());
                }
                s_ += "</th>\n";
            }
        }

        boolean isAction = MemberServiceClass.isUpdateAction(content);
        if (isAction)
            s_ += "<th class=\"memberArea\">\n" +
                MemberServiceClass.getString(content.getTargetAreaName(), renderRequest.getLocale()) + "</th>\n";

        s_ += "</tr>\n";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

            bindSelectSQL(ps);

            rs = ps.executeQuery();
            int countRecord = 0;

            String idPK = null;
            while (rs.next())
            {
                ++countRecord;

                if (countRecord == 1)
                {
                    switch (content.getQueryArea().getPrimaryKeyType().getType())
                    {
                        case PrimaryKeyTypeType.NUMBER_TYPE:
                            idPK = "" + RsetTools.getLong(rs, content.getQueryArea().getPrimaryKey());
                            break;

                        case PrimaryKeyTypeType.STRING_TYPE:
                            idPK = RsetTools.getString(rs, content.getQueryArea().getPrimaryKey());
                            break;

                        case PrimaryKeyTypeType.DATE_TYPE:
                            if (content.getQueryArea().getPrimaryKeyMask()==null ||
                                content.getQueryArea().getPrimaryKeyMask().trim().length()==0)
                                throw new Exception("date mask for primary key not specified");

                            idPK = RsetTools.getStringDate(rs, content.getQueryArea().getPrimaryKey(),
                                content.getQueryArea().getPrimaryKeyMask(), "error", Locale.ENGLISH);
                            break;

                        default:
                            throw new Exception("Wrong type of primary key");
                    }

                    if (log.isDebugEnabled())
                        log.debug("value of primary key - "+idPK);
                }


                s_ += "<tr>\n";
                for (int k = 0; k < qa.getFieldsCount(); k++)
                {
                    FieldsType ff = qa.getFields(k);
                    if (Boolean.TRUE.equals(ff.getIsShow()))
                    {
                        s_ += "<td valign=\"top\" class=\"memberArea\">";

                        String editVal = StringTools.toPlainHTML(
                                StringTools.truncateString(
                                        getCellValue(rs, ff, false),1000
                                )
                        );

                        if (editVal == null || editVal.length() == 0)
                            editVal = "&nbsp;";

                        s_ += (editVal + "</td>\n");

//                        if (log.isDebugEnabled())
//                            log.debug("#12.0004 "+s_);
                    }
                }

                if (isAction)
                    s_ += "<td valign=\"top\" class=\"memberAreaAction\">\n" + buildChangeButton(rs) + "</td>\n";

                s_ += "</tr>\n";
            }

            s_ += "</table>";
        }
        catch (Exception e) {
            log.error("SQL:\n"+sql_);
            final String es = "Error processing buildSelectHTMLTable.";
            log.error(es, e);
            throw new MemberException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return s_;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public MemberProcessing( PortletRequest renderRequest, PortletResponse renderResponse, ResourceBundle bundle, ModuleManager moduleManager )
        throws Exception {

        this.moduleManager = moduleManager;
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.bundle = bundle;
        if (this.renderRequest== null)
            throw new Exception("portletRequest must not null");

        if (this.renderResponse== null)
            throw new Exception("renderResponse must not null");

        authSession = (AuthSession)this.renderRequest.getUserPrincipal();
        if (authSession==null)
            throw new Exception("UserPrincipal not initialized");

        this.firmId = authSession.getUserInfo().getCompanyId();
        this.userId = authSession.getUserInfo().getUserId();

        fromParam = RequestTools.getString(this.renderRequest, MemberConstants.MEMBER_FROM_PARAM, "").trim();
        db_ = DatabaseAdapter.getInstance();
        try
        {
            String moduleName = RequestTools.getString( this.renderRequest, MemberConstants.MEMBER_MODULE_PARAM );
            if (log.isDebugEnabled())
            {
                log.debug("moduleName: "+moduleName);
                for (Enumeration e = this.renderRequest.getParameterNames(); e.hasMoreElements();)
                {
                    String s = (String) e.nextElement();
                    log.debug("Request parameter: " + s + ", value: " + RequestTools.getString(this.renderRequest, s).toString() );
                }
            }

            mod = moduleManager.getModule( moduleName );
        }
        catch (Exception e)
        {
            log.error("Error get module", e);
            mod = null;
        }

        if (mod != null)
        {
            for (int i=0; i<mod.getContentCount(); i++)
            {
                ContentType contentType = mod.getContent(i);
                if (contentType.getQueryArea().getSqlCache() == null)
                    contentType.getQueryArea().setSqlCache( new SqlCacheType() );
            }
        }

        thisURI = PortletService.url(MemberConstants.CTX_TYPE_MEMBER_VIEW , renderRequest, this.renderResponse ) + '&';
        commitURI = PortletService.url(MemberConstants.CTX_TYPE_MEMBER_COMMIT , renderRequest, this.renderResponse ) + '&';

    }

    public void initRight() throws Exception {
        isMainAccess = MemberServiceClass.checkRole( renderRequest, content );

        isIndexAccess =
            MemberServiceClass.checkRole( renderRequest, MemberServiceClass.getContent( mod, ContentTypeActionType.INDEX_TYPE) );
        isInsertAccess =
            MemberServiceClass.checkRole( renderRequest, MemberServiceClass.getContent( mod, ContentTypeActionType.INSERT_TYPE) );
        isChangeAccess =
            MemberServiceClass.checkRole( renderRequest, MemberServiceClass.getContent( mod, ContentTypeActionType.CHANGE_TYPE) );
        isDeleteAccess =
            MemberServiceClass.checkRole( renderRequest, MemberServiceClass.getContent( mod, ContentTypeActionType.DELETE_TYPE) );
    }


    public void process_Yes_1_No_N_Fields(DatabaseAdapter dbDyn)
        throws Exception
    {
        String sql_ = MemberServiceClass.buildUpdateSQL( dbDyn, content, fromParam, mod, false, renderRequest.getParameterMap(), renderRequest.getRemoteUser(), renderRequest.getServerName(), moduleManager, authSession );

        log.info("sql for update yes1-noN\n" + sql_);


        PreparedStatement ps = null;
        try
        {
            ps = dbDyn.prepareStatement(sql_);
            bindUpdate( dbDyn, ps, null, false );
            ps.executeUpdate();
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }

    }

    public String validateFields(DatabaseAdapter dbDyn)
        throws ConfigException
    {

        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++)
        {
            FieldsType ff = content.getQueryArea().getFields(k);

            if (log.isDebugEnabled())
                log.debug("Field '"+ff.getName()+"', validator is "+ff.getValidator());

            if (ff.getValidator()!=null)
            {
                if (log.isDebugEnabled())
                    log.debug("Start validate field '"+ff.getName()+"'");

                String result = null;
                switch (ff.getValidator().getType().getType())
                {
                    case FieldValidatorTypeTypeType.XML_TYPE:
                        result = XmlValidator.validate(renderRequest, mod.getName(), ff);
                        break;

                    case  FieldValidatorTypeTypeType.XSLT_TYPE:
                        result = XsltValidator.validate(renderRequest, mod.getName(), ff);
                        break;

                    case  FieldValidatorTypeTypeType.BIND_DYNAMIC_TYPE:
                        result = BindDynamicValidator.validate(renderRequest, mod.getName(), ff);
                        break;

                    default:
                        return "Error type of validate field "+ff.getName();
                }
                if (result!=null) {
                    return result;
                }
            }
        }
        return null;
    }

    public String getFromParam() {
        return fromParam;
    }

    public AuthSession getAuthSession() {
        return authSession;
    }
}