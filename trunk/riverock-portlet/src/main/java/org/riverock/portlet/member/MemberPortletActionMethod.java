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

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.member.RelateClassType;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;
import org.riverock.portlet.schema.member.types.ModuleTypeTypeType;
import org.riverock.portlet.schema.member.types.PrimaryKeyTypeType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Sergei Maslyukov
 *         Date: 17.04.2006
 *         Time: 15:14:45
 */
public class MemberPortletActionMethod {
    private final static Logger log = Logger.getLogger(MemberPortletActionMethod.class);

    // sync object for output debug to file
    private final static Object syncFile = new Object();
    public static void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
        throws PortletException {
        log.debug("Start MemberPortletActionMethod.processAction()");

        MemberProcessingActionRequest mp = null;
        try {

            ModuleManager moduleManager = ModuleManager.getInstance(PropertiesProvider.getConfigPath());
            mp = new MemberProcessingActionRequest(actionRequest, moduleManager);

            String moduleName = PortletService.getString(actionRequest, MemberConstants.MEMBER_MODULE_PARAM, null);
            String actionName = PortletService.getString(actionRequest, MemberConstants.MEMBER_ACTION_PARAM, null);
            String subActionName = PortletService.getString(actionRequest, MemberConstants.MEMBER_SUBACTION_PARAM, null).trim();


            if (log.isDebugEnabled()) {
                Map parameterMap = actionRequest.getParameterMap();
                if (!parameterMap.entrySet().isEmpty()) {
                    log.debug("Action request parameter");
                    for (Object o : parameterMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        log.debug("    key: " + entry.getKey() + ", value: " + entry.getValue());
                    }
                } else {
                    log.debug("Action request map is empty");
                }
                log.debug("   Point #4.1 module '" + moduleName + "'");
                log.debug("   Point #4.2 action '" + actionName + "'");
                log.debug("   Point #4.3 subAction '" + subActionName + "'");
            }

            if (mp.mod == null) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Point #4.2. Module '" + moduleName + "' not found");
                return;
            }

            // Check was module is lookup and can not calling directly from menu.
            if (mp.mod.getType() != null &&
                mp.mod.getType().getType() == ModuleTypeTypeType.LOOKUP_TYPE &&
                (mp.getFromParam() == null || mp.getFromParam().length() == 0)
                ) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Point #4.4. Module " + moduleName + " is lookup module");
                return;
            }

            int actionType = ContentTypeActionType.valueOf(actionName).getType();

            if (log.isDebugEnabled()) {
                log.debug("action name " + actionName);
                log.debug("ContentTypeActionType " + ContentTypeActionType.valueOf(actionName).toString());
                log.debug("action type " + actionType);
            }

            mp.content = MemberServiceClass.getContent(mp.mod, actionType);
            if (mp.content == null) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Module: '" + moduleName + "', action '" + actionName + "', not found");
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Debug. Unmarshal sqlCache object");
                synchronized (syncFile) {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                        SiteUtils.getTempDir() + File.separatorChar + "member-content-site-start-0.xml",
                        "windows-1251");
                }
            }


            if (!MemberServiceClass.checkRole(actionRequest, mp.content)) {
                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Access denied");
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Unmarshal sqlCache object");
                synchronized (syncFile) {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                        SiteUtils.getTempDir() + File.separatorChar + "member-content-site-start-2.xml",
                        "windows-1251");
                }
            }

            initRenderParameters( actionRequest.getParameterMap(), actionResponse);

            if ("commit".equalsIgnoreCase(subActionName)) {
                DatabaseAdapter dbDyn = null;
                PreparedStatement ps = null;
                try {
                    dbDyn = mp.getDatabaseAdapter();


                    int i1;
                    switch (actionType) {
                        case ContentTypeActionType.INSERT_TYPE:

                            if (log.isDebugEnabled())
                                log.debug("Start prepare data for inserting.");

                            String validateStatus = mp.validateFields(dbDyn);

                            if (log.isDebugEnabled())
                                log.debug("Validating status - " + validateStatus);

                            if (validateStatus != null) {
                                WebmillErrorPage.setErrorInfo(
                                    actionResponse,
                                    validateStatus,
                                    MemberConstants.ERROR_TEXT,
                                    null,
                                    "Continue",
                                    MemberConstants.ERROR_URL_NAME
                                );
                                return;
                            }

                            if (log.isDebugEnabled()) {
                                log.debug("Unmarshal sqlCache object");
                                synchronized (syncFile) {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                                        SiteUtils.getTempDir() + File.separatorChar + "member-content-before-yesno.xml",
                                        "windows-1251");
                                }
                            }

                            if (log.isDebugEnabled())
                                log.debug("Start looking for field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString());

                            if (MemberServiceClass.hasYesNoField(actionRequest.getParameterMap(), mp.mod, mp.content)) {
                                if (log.isDebugEnabled())
                                    log.debug("Found field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString());

                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            } else {
                                if (log.isDebugEnabled())
                                    log.debug("Field with type " + FieldsTypeJspTypeType.YES_1_NO_N.toString() + " not found");
                            }

                            String sql_ = MemberServiceClass.buildInsertSQL(mp.content, mp.getFromParam(), mp.mod, dbDyn, actionRequest.getServerName(), mp.getModuleManager(), mp.authSession);

                            if (log.isDebugEnabled()) {
                                log.debug("insert SQL:\n" + sql_ + "\n");
                                log.debug("Unmarshal sqlCache object");
                                synchronized (syncFile) {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                                        SiteUtils.getTempDir() + File.separatorChar + "member-content.xml",
                                        "windows-1251");
                                }
                            }

                            boolean checkStatus = false;
                            // Todo check restrict commented for MYSQL DB -
                            // Todo this temporary solution - need full rewrite restriction method
                            switch (dbDyn.getFamily()) {
                                case DatabaseManager.MYSQL_FAMALY:
                                    break;
                                default:
                                    checkStatus = mp.checkRestrict();
                                    if (!checkStatus)
                                        throw new ServletException("check status of restrict failed");

                                    break;
                            }
                            if (log.isDebugEnabled())
                                log.debug("check status - " + checkStatus);

                            ps = dbDyn.prepareStatement(sql_);
                            Object idNewRec = mp.bindInsert(dbDyn, ps);
                            i1 = ps.executeUpdate();

                            if (log.isDebugEnabled())
                                log.debug("Number of inserter record - " + i1);

                            DatabaseManager.close(ps);
                            ps = null;

                            // block for testing PK with different types
                            if (log.isDebugEnabled()) {
                                outputDebugOfInsertStatus(mp, dbDyn, idNewRec);
                            }

                            mp.prepareBigtextData(dbDyn, idNewRec, false);

                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                RelateClassType rc = mp.mod.getRelateClass(i);

                                if (log.isDebugEnabled())
                                    log.debug("#7.003.003 terminate class " + rc.getClassName());

                                CacheFactory.terminate(rc.getClassName(), null, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                            }
                            break;

                        case ContentTypeActionType.CHANGE_TYPE:

                            if (log.isDebugEnabled())
                                log.debug("Commit change page");

                            validateStatus = mp.validateFields(dbDyn);
                            if (validateStatus != null) {

                                WebmillErrorPage.setErrorInfo(
                                    actionResponse,
                                    validateStatus,
                                    MemberConstants.ERROR_TEXT,
                                    null,
                                    "Continue",
                                    MemberConstants.ERROR_URL_NAME
                                );
                                return;
                            }
                            if (MemberServiceClass.hasYesNoField(actionRequest.getParameterMap(), mp.mod, mp.content)) {
                                if (log.isDebugEnabled())
                                    log.debug("Found field with type " + FieldsTypeJspTypeType.YES_1_NO_N);

                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            }

                            Object idCurrRec;

                            if (log.isDebugEnabled())
                                log.debug("PrimaryKeyType " + mp.content.getQueryArea().getPrimaryKeyType());

                            switch (mp.content.getQueryArea().getPrimaryKeyType().getType()) {
                                case PrimaryKeyTypeType.NUMBER_TYPE:
                                    log.debug("PrimaryKeyType - 'number'");

                                    idCurrRec = PortletService.getLong(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                                    break;
                                case PrimaryKeyTypeType.STRING_TYPE:
                                    log.debug("PrimaryKeyType - 'string'");

                                    idCurrRec = PortletService.getString(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey(), null);
                                    break;
                                default:
                                    throw new Exception("Change. Wrong type of primary key - " +
                                        mp.content.getQueryArea().getPrimaryKeyType());
                            }

                            if (log.isDebugEnabled())
                                log.debug("mp.isSimpleField(): " + mp.isSimpleField());

                            if (mp.isSimpleField()) {
                                log.debug("start build SQL");

                                sql_ = MemberServiceClass.buildUpdateSQL(dbDyn, mp.content, mp.getFromParam(), mp.mod, true, actionRequest.getParameterMap(), actionRequest.getRemoteUser(), actionRequest.getServerName(), mp.getModuleManager(), mp.authSession);

                                if (log.isDebugEnabled())
                                    log.debug("update SQL:" + sql_);

                                ps = dbDyn.prepareStatement(sql_);
                                mp.bindUpdate(dbDyn, ps, idCurrRec, true);

                                i1 = ps.executeUpdate();

                                if (log.isDebugEnabled())
                                    log.debug("Number of updated record - " + i1);

                            }

                            log.debug("prepare big text");

                            mp.prepareBigtextData(dbDyn, idCurrRec, true);

                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() !=
                                PrimaryKeyTypeType.NUMBER_TYPE)
                                throw new Exception("PK of 'Bigtext' table must be a 'number' type");

                            log.debug("start sync cache data");

                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                RelateClassType rc = mp.mod.getRelateClass(i);

                                if (log.isDebugEnabled())
                                    log.debug("#7.003.002 terminate class " + rc.getClassName() + ", id_rec " + idCurrRec);

                                if (mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                    PrimaryKeyTypeType.NUMBER_TYPE) {
                                    CacheFactory.terminate(rc.getClassName(), (Long) idCurrRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                                } else {
                                    actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Change. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                                    return;
                                }
                            }
                            break;

                        case ContentTypeActionType.DELETE_TYPE:

                            log.debug("Commit delete page<br>");

                            Object idRec;

                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                PrimaryKeyTypeType.NUMBER_TYPE) {
                                idRec = PortletService.getLong(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                            } else if (mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                PrimaryKeyTypeType.STRING_TYPE) {
                                idRec = PortletService.getString(actionRequest, mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey(), null);
                            }
                            else {
                                actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Delete. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                                return;
                            }

                            // delete data from slave table for MySQL,
                            // 'cos mysql not support DELETE CASCADE reference integrity
                            // Todo switch from getFamaly() to metadata to
                            // Todo decide support or not DELETE CASCADE
                            if (dbDyn.getFamily() == DatabaseManager.MYSQL_FAMALY)
                                mp.deleteBigtextData(dbDyn, idRec);

                            sql_ = MemberServiceClass.buildDeleteSQL(dbDyn, mp.mod, mp.content, mp.getFromParam(), actionRequest.getParameterMap(), actionRequest.getRemoteUser(), actionRequest.getServerName(), moduleManager, mp.authSession);

                            if (log.isDebugEnabled())
                                log.debug("delete SQL: " + sql_ + "<br>\n");

                            ps = dbDyn.prepareStatement(sql_);

                            mp.bindDelete(ps);
                            i1 = ps.executeUpdate();

                            if (log.isDebugEnabled()) log.debug("Number of deleted record - " + i1);

                            if (idRec != null && (idRec instanceof Long)) {
                                for (int i = 0; i < mp.mod.getRelateClassCount(); i++) {
                                    RelateClassType rc = mp.mod.getRelateClass(i);
                                    if (log.isDebugEnabled())
                                        log.debug("#7.003.001 terminate class " + rc.getClassName() + ", id_rec " + idRec.toString());
                                    CacheFactory.terminate(rc.getClassName(), (Long) idRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()));
                                }
                            }
                            break;

                        default:
                            actionResponse.setRenderParameter(MemberConstants.ERROR_TEXT,  "Unknown type of action - " + actionName);
                            return;
                    }
                    log.debug("do commit");

                    dbDyn.commit();
                }
                catch (Exception e1) {
                    try {
                        dbDyn.rollback();
                    }
                    catch (Exception e001) {
                        log.info("error in rolback()");
                    }

                    log.error("Error while processing this page", e1);


                    if (dbDyn.testExceptionIndexUniqueKey(e1)) {
                        WebmillErrorPage.setErrorInfo(
                            actionResponse,
                            "You input value already exists in DB. Try again with other value",
                            MemberConstants.ERROR_TEXT,
                            null,
                            "Continue",
                            MemberConstants.ERROR_URL_NAME
                        );
                    } else {
                        WebmillErrorPage.setErrorInfo(
                            actionResponse,
                            "Error while processing request",
                            MemberConstants.ERROR_TEXT,
                            e1,
                            "Continue",
                            MemberConstants.ERROR_URL_NAME
                        );
                    }
                }
                finally {
                    DatabaseManager.close(dbDyn, ps);
                }

            }
        }
        catch (Exception e) {
            final String es = "General processing error ";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            if (mp != null) {
                mp.destroy();
            }
        }
    }

    private static void initRenderParameters(Map parameterMap, ActionResponse actionResponse) {
        log.debug("Start initRenderParameters()");
        if (!parameterMap.entrySet().isEmpty()) {
            log.debug("Request parameter");
            Map<String, String[]> map = parameterMap;
            for (Map.Entry<String, String[]> entry : map.entrySet()) {

                if ((entry.getKey()).equals(MemberConstants.MEMBER_ACTION_PARAM)) {
                    actionResponse.setRenderParameter(MemberConstants.MEMBER_ACTION_PARAM, ContentTypeActionType.INDEX.toString());
                }
                else if ((entry.getKey()).equals(MemberConstants.MEMBER_SUBACTION_PARAM)) {
                    // do not include sub-action in render request
                }
                else {
                    for (String v : entry.getValue()) {
                        actionResponse.setRenderParameter(entry.getKey(), v);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("    key: " + entry.getKey() + ", value: " + StringTools.arrayToString(entry.getValue()));
                }
            }
        } else {
            log.debug("Parameters for render request not found");
        }
    }

    private static void outputDebugOfInsertStatus(MemberProcessingActionRequest mp, DatabaseAdapter dbDyn, Object idNewRec) {
        PreparedStatement psTest = null;
        ResultSet rsTest = null;
        try {
            String sqlTest = "select * from " +
                (mp.content.getQueryArea().getTable(0)).getTable() +
                " where " + mp.content.getQueryArea().getPrimaryKey() + " = ?";

            log.debug("Test sql\n" + sqlTest);
            psTest = dbDyn.prepareStatement(sqlTest);
            log.debug("Test fetch. id - " + idNewRec);
            log.debug("Test fetch. type of PK - " + mp.content.getQueryArea().getPrimaryKeyType());

            switch (mp.content.getQueryArea().getPrimaryKeyType().getType()) {
                case PrimaryKeyTypeType.NUMBER_TYPE:
                    psTest.setLong(1, (Long) idNewRec);
                    break;
                case PrimaryKeyTypeType.STRING_TYPE:
                    psTest.setString(1, (String) idNewRec);
                    break;
                default :
                    log.error("Test fetch. Unknown type of PK - " + mp.content.getQueryArea().getPrimaryKeyType());
            }
            rsTest = psTest.executeQuery();

            int i = 0;
            while (rsTest.next())
                i++;
            log.debug("Fetching " + i + " test records");
        }
        catch (Exception e) {
            log.error("Error get test record", e);
        }
        finally {
            DatabaseManager.close(rsTest, psTest);
        }
    }
}
