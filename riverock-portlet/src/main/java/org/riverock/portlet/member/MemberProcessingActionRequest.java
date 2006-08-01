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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.portlet.ActionRequest;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.sso.a3.AuthSession;
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
import org.riverock.portlet.schema.member.types.TypeFieldType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * $Id$
 */
public final class MemberProcessingActionRequest extends MemberProcessingAbstract {
    private final static Logger log = Logger.getLogger(MemberProcessingActionRequest.class);

    public void destroy() {
        super.destroy();
    }

    public boolean checkRestrict() throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            SqlCacheType sqlCache = content.getQueryArea().getSqlCache();

            if (log.isDebugEnabled())
                log.debug("sqlQuery.getCheckSql not cached, start create new.");

            if (sqlCache.getFromCount() == 0 || sqlCache.getWhereCount() == 0) {
                if (log.isDebugEnabled())
                    log.debug("Return true. from count - " + sqlCache.getFromCount() + ", where count - " + sqlCache.getWhereCount());

                return true;
            }

            String sql = "select null from ";
            boolean isFirst = true;
            for (int i = 0; i < sqlCache.getFromCount(); i++) {
                if (isFirst)
                    isFirst = false;
                else
                    sql += ", ";

                SqlFromType from = sqlCache.getFrom(i);
                sql += from.getTable();
                if (from.getAlias() != null)
                    sql += " " + from.getAlias();
            }

            isFirst = true;
            for (int i = 0; i < sqlCache.getWhereCount(); i++) {
                if (isFirst) {
                    sql += " where ";
                    isFirst = false;
                } else
                    sql += " and ";

                SqlWhereType where = sqlCache.getWhere(i);

                SqlWhereColumnType column = where.getLeft();
                switch (column.getTypeField().getType()) {
                    case TypeFieldType.DATA_TYPE:
                    case TypeFieldType.PARAMETER_TYPE:
                        sql += column.getValue();
                        break;
                    case TypeFieldType.FIELD_TYPE:
                        if (column.getAlias() != null)
                            sql += (column.getAlias() + ".");
                        sql += column.getColumn();
                        break;
                    default:
                        throw new Exception("Unknown type of 'where' left column");
                }
                sql += "=";
                column = where.getRight();
                switch (column.getTypeField().getType()) {
                    case TypeFieldType.DATA_TYPE:
                    case TypeFieldType.PARAMETER_TYPE:
                        sql += column.getValue();
                        break;
                    case TypeFieldType.FIELD_TYPE:
                        if (column.getAlias() != null)
                            sql += (column.getAlias() + ".");
                        sql += column.getColumn();
                        break;
                    default:
                        throw new Exception("Unknown type of 'where' right column");
                }
            }

            if (log.isDebugEnabled())
                log.debug("sql for check\n" + sql);

            ps = db_.prepareStatement(sql);
            int numParam = 1;
            for (int i = 0; i < sqlCache.getCheckParamCount(); i++) {
                SqlCheckParameterType parameter = sqlCache.getCheckParam(i);
                switch (parameter.getType().getType()) {
                    case SqlCheckParameterTypeTypeType.HTTP_REQUEST_TYPE:
                        switch (parameter.getParameterType().getType()) {
                            case ParameterTypeType.NUMBER_TYPE:
                                RsetTools.setLong(ps, numParam++, PortletService.getLong(portletRequest, parameter.getParameter()));
                                break;
                            case ParameterTypeType.STRING_TYPE:
                                ps.setString(numParam++, RequestTools.getString(portletRequest, parameter.getParameter()));
                                break;
                            case ParameterTypeType.DATE_TYPE:
                                throw new Exception("DATE_TYPE of PK not implemented");
                            default:
                                log.info("Unknown type of PK - " + parameter.getParameterType());
                        }
                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_SITE_TYPE:
                        ps.setString(numParam++, portletRequest.getServerName());
                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_FIRM_TYPE:

                        String nameFirmField = isFirmFieldExists(content.getQueryArea());
                        switch (db_.getFamily()) {
                            case DatabaseManager.MYSQL_FAMALY:
                                if (log.isDebugEnabled())
                                    log.debug("nameFirmField: " + nameFirmField);

                                if (nameFirmField != null) {
                                    List<Long> list = authSession.getGrantedCompanyIdList();
                                    Long id = PortletService.getLong(portletRequest, mod.getName() + '.' + nameFirmField);

                                    log.debug("id: " + id);
                                    if (id == null)
                                        return false;

                                    boolean isFound = false;
                                    for (Long aList : list) {
                                        if (id.equals(aList))
                                            isFound = true;
                                    }

                                    log.debug("return false ");
                                    if (!isFound)
                                        return false;
                                }
                                break;
                            default:
                                if (nameFirmField != null)
                                    RsetTools.setLong(ps, numParam++,
                                        PortletService.getLong(portletRequest,
                                            mod.getName() + '.' + nameFirmField));

                                ps.setString(numParam++, portletRequest.getRemoteUser());
                                break;
                        }

                        break;

                    case SqlCheckParameterTypeTypeType.RESTRICT_USER_TYPE:
                        ps.setString(numParam++, portletRequest.getRemoteUser());
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
        catch (Exception e) {
            log.error("Error checkRestriction", e);
            throw e;
        }
        finally {
            DatabaseManager.close(rs, ps);
        }
        return false;
    }

    public boolean isSimpleField() {
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsEdit())) {
                if (FieldsTypeJspTypeType.BIGTEXT_TYPE != ff.getJspType().getType())
                    return true;
            }
        }
        return false;
    }

    private String isFirmFieldExists(QueryAreaType qa) {
        for (int k = 0; k < qa.getFieldsCount(); k++) {
            FieldsType ff = qa.getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow())) {
                if ("ID_FIRM".equalsIgnoreCase(ff.getName()))
                    return ff.getName();
            }
        }

        if ("ID_FIRM".equalsIgnoreCase(content.getQueryArea().getPrimaryKey()))
            return content.getQueryArea().getPrimaryKey();

        return null;
    }

    public Object bindInsert(DatabaseAdapter dbDyn, PreparedStatement ps)
        throws Exception {
        Object returnID = null;

        if (log.isDebugEnabled())
            log.debug("#99.01.00 sequence -  " + "seq_" + content.getQueryArea().getTable(0).getTable());

        // create sequence for PK
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_" + content.getQueryArea().getTable(0).getTable());
        seq.setTableName(content.getQueryArea().getTable(0).getTable());
        seq.setColumnName(content.getQueryArea().getPrimaryKey());
        Long pkID = dbDyn.getSequenceNextValue(seq);

        int numParam = 1;

        if (log.isDebugEnabled())
            log.debug("#99.01.01 value of PK " + pkID);

        // bind PK
        switch (content.getQueryArea().getPrimaryKeyType().getType()) {
            case PrimaryKeyTypeType.NUMBER_TYPE:
                RsetTools.setLong(ps, numParam++, pkID);
                returnID = pkID;

                if (log.isDebugEnabled()) {
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
            default:
                throw new Exception("Wrong type of primary key");
        }

        // Bind main variable
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE))
            {
                if (log.isDebugEnabled())
                    log.debug("#4.05.01 bind " + ff.getJspType().toString() + " param #" + numParam + " " + RequestTools.getString(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff)));

                String stringParam =
                    RequestTools.getString(
                        portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff), null
                    );

                switch (ff.getJspType().getType()) {
                    case FieldsTypeJspTypeType.TEXT_TYPE:
                    case FieldsTypeJspTypeType.TEXT_AREA_TYPE:
                        if (stringParam != null && stringParam.length() > 0)
                            ps.setString(numParam++, stringParam);
                        else
                            ps.setNull(numParam++, Types.VARCHAR);
                        break;

                    case FieldsTypeJspTypeType.DATE_TEXT_TYPE:

                        if (stringParam != null && stringParam.length() > 0) {
                            java.util.Date updateDate =
                                DateTools.getDateWithMask(stringParam, ff.getMask());

                            dbDyn.bindDate(ps, numParam++, new java.sql.Timestamp(updateDate.getTime()));
                        } else
                            ps.setNull(numParam++, Types.DATE);
                        break;

                    case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                        if (stringParam != null && stringParam.length() > 0)
                            ps.setLong(numParam++, new Long(stringParam));
                        else
                            ps.setNull(numParam++, Types.INTEGER);
                        break;

                    case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                    case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:

                        if (stringParam != null && stringParam.length() > 0)
                            ps.setDouble(numParam++, new Double(stringParam));
                        else
                            ps.setNull(numParam++, Types.DECIMAL);
                        break;

                    default:
                        if (stringParam != null && stringParam.length() > 0)
                            ps.setString(numParam++, stringParam);
                        else
                            ps.setNull(numParam++, Types.VARCHAR);
                        break;
                }
            }
        }

        // bind FK to PK of top table
        StringTokenizer st = new StringTokenizer(fromParam, ",");
        while (st.hasMoreTokens()) {
            String modName = st.nextToken();

            ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
            if (cnt != null && cnt.getQueryArea() != null &&
                !st.hasMoreTokens()) {
                if (log.isDebugEnabled())
                    log.debug("#4.09.03.1 bind '" + cnt.getQueryArea().getPrimaryKeyType().toString() +
                        "' param #" + numParam + " " + modName + '.' + cnt.getQueryArea().getPrimaryKey() + ' ' +
                        RequestTools.getString(portletRequest, modName + '.' + cnt.getQueryArea().getPrimaryKey())
                    );

                switch (cnt.getQueryArea().getPrimaryKeyType().getType()) {
                    case PrimaryKeyTypeType.NUMBER_TYPE:
                        RsetTools.setLong(ps, numParam++, PortletService.getLong(portletRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()));
                        break;

                    case PrimaryKeyTypeType.STRING_TYPE:
                        ps.setString(numParam++, RequestTools.getString(portletRequest,
                            modName + '.' + cnt.getQueryArea().getPrimaryKey()));
                        break;

                    default:
                        throw new Exception(cnt.getQueryArea().getPrimaryKeyType().toString() +
                            " type of lookup is wrong"
                        );
                }
            }
        }

        // process self lookup module
        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null) {
            if (log.isDebugEnabled())
                log.debug("#4.01.03.1 bind long param #" + numParam + " " +
                    PortletService.getLong(portletRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L)
                );

            RsetTools.setLong(ps, numParam++,
                PortletService.getLong(portletRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L)
            );
        }

        // processing of restrict
        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
                RestrictTypeTypeType.FIRM_TYPE
            && !MemberServiceClass.checkRestrictField(content, RestrictTypeTypeType.FIRM_TYPE)) {
            if (log.isDebugEnabled())
                log.debug("#4.09.07 bind long param #" + numParam + ' ' + authSession.getUserInfo().getCompanyId());

            RsetTools.setLong(ps, numParam++, authSession.getUserInfo().getCompanyId());
        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
                RestrictTypeTypeType.SITE_TYPE
            && !MemberServiceClass.checkRestrictField(content, RestrictTypeTypeType.SITE_TYPE)) {
            Long siteId = new Long(portletRequest.getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
            if (log.isDebugEnabled())
                log.debug("#4.09.08 bind long param #" + numParam + ' ' + siteId);

            RsetTools.setLong(ps, numParam++, siteId);

        }

        if (content.getQueryArea().getRestrict() != null &&
            content.getQueryArea().getRestrict().getType().getType() ==
                RestrictTypeTypeType.USER_TYPE
            && !MemberServiceClass.checkRestrictField(content, RestrictTypeTypeType.USER_TYPE)) {
            if (log.isDebugEnabled())
                log.debug("#4.09.09  bind long param #" + numParam + " " + authSession.getUserInfo().getUserId());

            RsetTools.setLong(ps, numParam++, authSession.getUserInfo().getUserId());
        }

        if (log.isDebugEnabled())
            log.debug("#4.09.10 count of binded parameters " + (numParam - 1));

        return returnID;
    }

    public void prepareBigtextData(DatabaseAdapter dbDyn, Object idRec, boolean isDelete)
        throws Exception {
        if (log.isDebugEnabled())
            log.debug("#88.01.00 " + idRec);

        // looking for bigtext field
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);

            // if isShow and this field is bigtext then process this field
            if (Boolean.TRUE.equals(ff.getIsShow()) && ff.getJspType().getType() == FieldsTypeJspTypeType.BIGTEXT_TYPE)
            {
                if (log.isDebugEnabled())
                    log.debug("BigText field - " + ff.getName());

                String insertString =
                    RequestTools.getString(
                        portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff)
                    );

                String nameTargetField = null;
                for (int j = 0; j < ff.getQueryArea().getFieldsCount(); j++) {
                    FieldsType field = ff.getQueryArea().getFields(j);
                    if (Boolean.TRUE.equals(field.getIsShow())) {
                        nameTargetField = field.getName();
                        break;
                    }
                }

                if (nameTargetField == null) {
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
        throws Exception {
        if (log.isDebugEnabled())
            log.debug("#88.01.00 " + idRec);

        // looking for bigtext field
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);

            // if bigtext then process this field
            if (ff.getJspType().getType() == FieldsTypeJspTypeType.BIGTEXT_TYPE) {
                if (log.isDebugEnabled())
                    log.debug("BigText field - " + ff.getName());

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
        throws Exception {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or QueryAreaType is null.");

        int numParam = 1;

        switch (db_.getFamily()) {
            case DatabaseManager.MYSQL_FAMALY:
                break;
            default:
                numParam = MemberServiceClass.bindSubQueryParam(ps, numParam, this.fromParam, db_, portletRequest.getParameterMap(), portletRequest.getServerName(), portletRequest.getRemoteUser(), moduleManager);
                break;
        }

        if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
            RsetTools.setLong(ps, numParam++, PortletService.getLong(portletRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()));
        } else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
            ps.setString(numParam++, RequestTools.getString(portletRequest,
                mod.getName() + '.' + content.getQueryArea().getPrimaryKey()));
        }
        else
            throw new Exception("Wrong type of primary key");


        if (mod.getSelfLookup() != null &&
            mod.getSelfLookup().getCurrentField() != null &&
            mod.getSelfLookup().getTopField() != null) {
            RsetTools.setLong(ps, numParam++,
                PortletService.getLong(portletRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L)
            );
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, portletRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, portletRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(numParam++, portletRequest.getRemoteUser());
                    break;
            }
        }
    }

    public void bindUpdate(final DatabaseAdapter dbDyn, final PreparedStatement ps, final Object returnIdPK, final boolean isUsePrimaryKey)
        throws Exception {
        if (content == null || content.getQueryArea() == null)
            throw new Exception("content or content.QueryAreaType is null.");

        int numParam = 1;

        // Если есть хоть одно поле YES_1_NO_N и этот запрос выполняет установку данного поля в false
        // то подразумевается что поле установится в 0 в момент построения SQL запроса
        if (isUsePrimaryKey) {
            String stringParam = null;
            Double doubleParam;
            Long longParam;
            for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
                FieldsType ff = content.getQueryArea().getFields(k);
                if (Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE))
                {
                    switch (ff.getJspType().getType()) {
                        case FieldsTypeJspTypeType.TEXT_TYPE:
                        case FieldsTypeJspTypeType.TEXT_AREA_TYPE:

                            stringParam =
                                RequestTools.getString(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));
                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setString(ps, numParam++, stringParam);
                            break;

                        case FieldsTypeJspTypeType.DATE_TEXT_TYPE:

                            stringParam =
                                RequestTools.getString(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            if (stringParam.length() == 0)
                                ps.setNull(numParam++, Types.DATE);
                            else {
                                java.util.Date updateDate =
                                    DateTools.getDateWithMask(stringParam, ff.getMask());

                                dbDyn.bindDate(ps, numParam++, new java.sql.Timestamp(updateDate.getTime()));
                            }
                            break;

                        case FieldsTypeJspTypeType.INT_TEXT_TYPE:
                            longParam = PortletService.getLong(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setLong(ps, numParam++, longParam);
                            break;

                        case FieldsTypeJspTypeType.FLOAT_TEXT_TYPE:
                        case FieldsTypeJspTypeType.DOUBLE_TEXT_TYPE:

                            doubleParam =
                                PortletService.getDouble(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + doubleParam);

                            RsetTools.setDouble(ps, numParam++, doubleParam);
                            break;

                        default:
                            stringParam = RequestTools.getString(portletRequest, mod.getName() + '.' + MemberServiceClass.getRealName(ff));

                            if (log.isDebugEnabled())
                                log.debug("Param  #" + numParam + ", value: " + stringParam);

                            RsetTools.setString(ps, numParam++, stringParam);
                    }
                }
            }
        }

        switch (db_.getFamily()) {
            case DatabaseManager.MYSQL_FAMALY:
                break;
            default:
                numParam = MemberServiceClass.bindSubQueryParam(ps, numParam, this.fromParam, dbDyn, portletRequest.getParameterMap(), portletRequest.getServerName(), portletRequest.getRemoteUser(), moduleManager);
                break;
        }

        if (isUsePrimaryKey) {
            if (log.isDebugEnabled())
                log.debug("Param  #" + numParam + ", value: " + returnIdPK);

            if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
                RsetTools.setLong(ps, numParam++, ((Long) returnIdPK));
            } else if (content.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
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
            mod.getSelfLookup().getTopField() != null) {
            final Long longParam = PortletService.getLong(portletRequest, mod.getName() + '.' + mod.getSelfLookup().getTopField().getName(), 0L);

            if (log.isDebugEnabled())
                log.debug("Param  #" + numParam + ", value: " + longParam);

            RsetTools.setLong(ps, numParam++,
                longParam
            );
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + portletRequest.getRemoteUser());

                    ps.setString(numParam++, portletRequest.getRemoteUser());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + portletRequest.getServerName());

                    ps.setString(numParam++, portletRequest.getServerName());
                    break;
            }
        }

        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)
        {
            switch (db_.getFamily()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    if (log.isDebugEnabled())
                        log.debug("Param  #" + numParam + ", value: " + portletRequest.getRemoteUser());

                    ps.setString(numParam++, portletRequest.getRemoteUser());
                    break;
            }
        }
    }

    public String checkRecursiveCall() {
        if (fromParam == null || fromParam.length() == 0)
            return null;

        StringTokenizer st = new StringTokenizer(fromParam, ",");
        String str;
        String strCheck;
        int idx = 1;
        ContentType cnt;
        while (st.hasMoreTokens()) {
            str = st.nextToken();

            ModuleType module = moduleManager.getModule(str);
            if (module == null) {
                log.warn("checkRecursiveCall. module is null. Str - " + str);
                return MemberConstants.MEMBER_FROM_PARAM + " section is incorrect. ";
            }

            cnt = module.getContent(ContentTypeActionType.INDEX_TYPE);
            if (cnt == null)
                return "ContentType \"index\" not found in \"" + str + "\" module.";

            if (cnt.getQueryArea().getPrimaryKey() == null)
                return "ContentType \"index\" in \"" + str + "\" module not containt PK.";

            if (portletRequest.getParameter(str + '.' + cnt.getQueryArea().getPrimaryKey()) == null)
                return "Parameter " + str + '.' + cnt.getQueryArea().getPrimaryKey() + " not specified.";

            StringTokenizer stCheck = new StringTokenizer(fromParam, ",");
            int idxCheck = 1;
            while (stCheck.hasMoreTokens()) {
                strCheck = stCheck.nextToken();

                if (idx != idxCheck && str.equalsIgnoreCase(strCheck))
                    return "Recursive call not allowed.";

                idxCheck++;
            }
            idx++;
        }

        return null;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public MemberProcessingActionRequest(ActionRequest renderRequest, ModuleManager moduleManager) {

        this.moduleManager = moduleManager;
        this.portletRequest = renderRequest;
        if (this.portletRequest == null)
            throw new IllegalArgumentException("portletRequest must not null");

        authSession = (AuthSession) this.portletRequest.getUserPrincipal();
        if (authSession == null)
            throw new IllegalArgumentException("UserPrincipal not initialized");

        fromParam = RequestTools.getString(this.portletRequest, MemberConstants.MEMBER_FROM_PARAM, "").trim();
        try {
            db_ = DatabaseAdapter.getInstance();
            String moduleName = RequestTools.getString(this.portletRequest, MemberConstants.MEMBER_MODULE_PARAM);
            if (log.isDebugEnabled()) {
                log.debug("moduleName: " + moduleName);
                for (Enumeration e = this.portletRequest.getParameterNames(); e.hasMoreElements();) {
                    String s = (String) e.nextElement();
                    log.debug("Request parameter: " + s + ", value: " + RequestTools.getString(this.portletRequest, s));
                }
            }

            mod = moduleManager.getModule(moduleName);
        }
        catch (Exception e) {
            log.error("Error get module", e);
            mod = null;
        }

        if (mod != null) {
            for (int i = 0; i < mod.getContentCount(); i++) {
                ContentType contentType = mod.getContent(i);
                if (contentType.getQueryArea().getSqlCache() == null)
                    contentType.getQueryArea().setSqlCache(new SqlCacheType());
            }
        }
    }

    public void process_Yes_1_No_N_Fields(DatabaseAdapter dbDyn)
        throws Exception {
        String sql_ = MemberServiceClass.buildUpdateSQL(dbDyn, content, fromParam, mod, false, portletRequest.getParameterMap(), portletRequest.getRemoteUser(), portletRequest.getServerName(), moduleManager, authSession);

        log.info("sql for update yes1-noN\n" + sql_);


        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement(sql_);
            bindUpdate(dbDyn, ps, null, false);
            ps.executeUpdate();
        }
        finally {
            DatabaseManager.close(ps);
        }

    }

    public String validateFields(DatabaseAdapter dbDyn)
        throws ConfigException {

        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);

            if (log.isDebugEnabled())
                log.debug("Field '" + ff.getName() + "', validator is " + ff.getValidator());

            if (ff.getValidator() != null) {
                if (log.isDebugEnabled())
                    log.debug("Start validate field '" + ff.getName() + "'");

                String result;
                switch (ff.getValidator().getType().getType()) {
                    case FieldValidatorTypeTypeType.XML_TYPE:
                        result = XmlValidator.validate(portletRequest, mod.getName(), ff);
                        break;

                    case FieldValidatorTypeTypeType.XSLT_TYPE:
                        result = XsltValidator.validate(portletRequest, mod.getName(), ff);
                        break;

                    case FieldValidatorTypeTypeType.BIND_DYNAMIC_TYPE:
                        result = BindDynamicValidator.validate(portletRequest, mod.getName(), ff);
                        break;

                    default:
                        return "Error type of validate field " + ff.getName();
                }
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public String getFromParam() {
        return fromParam;
    }
}
