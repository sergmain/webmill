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

/**
 * User: Admin
 * Date: Nov 25, 2002
 * Time: 8:55:13 PM
 *
 * $Id$
 */
package org.riverock.portlet.member.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.member.MemberProcessing;
import org.riverock.portlet.member.MemberServiceClass;
import org.riverock.portlet.member.ModuleManager;
import org.riverock.portlet.schema.member.RelateClassType;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;
import org.riverock.portlet.schema.member.types.ModuleTypeTypeType;
import org.riverock.portlet.schema.member.types.PrimaryKeyTypeType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.PortletTools;

public class MemberCommitServlet extends HttpServlet
{

    private static Logger log = Logger.getLogger("org.riverock.member.servlet.MemberCommitServlet");

    // sync object for output debug to file
    private static Object syncFile = new Object();

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug( "method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
        throws IOException, ServletException
    {
        PrintWriter out = null;
        try
        {

            out = response.getWriter();

            ContextNavigator.setContentType(response, "utf-8");

            CtxInstance ctxInstance =
                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );

            MemberProcessing mp = new MemberProcessing( ctxInstance);

///////////////

            String moduleName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_MODULE_PARAM);
            String actionName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_ACTION_PARAM);
            String subActionName = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.MEMBER_SUBACTION_PARAM).trim();

            if (log.isDebugEnabled())
            {
                log.debug("Total of MemberFile  - " + ModuleManager.getCountFile());
                log.debug("Total of Module - " + ModuleManager.getCountModule());

                log.debug("Request URL - " + request_.getRequestURL());
                for (Enumeration e = ctxInstance.getPortletRequest().getParameterNames(); e.hasMoreElements();)
                {
                    String s = (String) e.nextElement();
                    log.debug("Request attr - " + s + ", value - " + PortletTools.getString(ctxInstance.getPortletRequest(), s));
                }

                log.debug("Point #2.1 module '" + moduleName + "'");
                log.debug("Point #2.2 action '" + actionName + "'");
                log.debug("Point #2.3 subAction '" + subActionName + "'");
            }


            if (mp.mod == null)
            {
                out.println("Point #4.2. Module '" + moduleName + "' not found");
                return;
            }

// Check was module is lookup and can not calling directly from menu.
            if (mp.mod.getType() != null &&
                mp.mod.getType().getType() == ModuleTypeTypeType.LOOKUP_TYPE &&
                (mp.fromParam == null || mp.fromParam.length() == 0)
            )
            {
                out.println("Point #4.4. Module " + moduleName + " is lookup module<br>");
                return;
            }

            int actionType = ContentTypeActionType.valueOf(actionName).getType();

            if (log.isDebugEnabled())
            {
                log.debug("action name " + actionName);
                log.debug("ContentTypeActionType " + ContentTypeActionType.valueOf(actionName).toString());
                log.debug("action type " + actionType);
            }

            mp.content = MemberServiceClass.getContent(mp.mod, actionType);
            if (mp.content == null)
            {
                out.println("Module: '" + moduleName + "', action '" + actionName + "', not found");
                return;
            }

            if (log.isDebugEnabled())
            {
                log.debug("Unmarshal sqlCache object");
                synchronized(syncFile)
                {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                        WebmillConfig.getWebmillDebugDir()+"member-content-site-start-0.xml",
                        "windows-1251");
                }
            }


            if (!MemberServiceClass.checkRole( ctxInstance.getPortletRequest(), mp.content ) )
            {
                out.println("Access denied");
                return;
            }

            String sql_ = null;

            if (log.isDebugEnabled())
            {
                log.debug("Unmarshal sqlCache object");
                synchronized(syncFile)
                {
                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                        WebmillConfig.getWebmillDebugDir()+"member-content-site-start-2.xml",
                        "windows-1251");
                }
            }

            if ("commit".equalsIgnoreCase(subActionName))
            {
                DatabaseAdapter dbDyn = null;
                PreparedStatement ps = null;
                String redirURL = "";

                try
                {
                    dbDyn = DatabaseAdapter.getInstance(true);


                    int i1;
                    switch (actionType)
                    {
                        case ContentTypeActionType.INSERT_TYPE:

                            if (log.isDebugEnabled())
                                log.debug("Commit add page");

                            String validateStatus = mp.validateFields(dbDyn);

                            if (log.isDebugEnabled())
                                log.debug("Validating status - "+validateStatus);

                            if ( validateStatus != null)
                            {
                                out.write("<html><head></head>\n<body>\n" +
                                    validateStatus +
                                    "\n<p><a href=\"" + mp.getIndexURL() + "\">Continue</a></p>\n" +
                                    "</body>\n</html>"
                                );

                                return;
                            }

                            if (log.isDebugEnabled())
                            {
                                log.debug("SQL:\n" + sql_ + "\n");

                                log.debug("Unmarshal sqlCache object");
                                synchronized(syncFile)
                                {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                                        WebmillConfig.getWebmillDebugDir()+"member-content-before-yesno.xml",
                                        "windows-1251");
                                }
                            }

                            if (log.isDebugEnabled())
                                log.debug("Start looking for field with type "+FieldsTypeJspTypeType.YES_1_NO_N.toString());

                            if (MemberServiceClass.hasYesNoField(ctxInstance.getPortletRequest(), mp.mod, mp.content))
                            {
                                if (log.isDebugEnabled())
                                    log.debug("Found field with type "+FieldsTypeJspTypeType.YES_1_NO_N.toString());

                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            }
                            else
                            {
                                if (log.isDebugEnabled())
                                    log.debug("Field with type "+FieldsTypeJspTypeType.YES_1_NO_N.toString()+" not found");
                            }

                            sql_ = MemberServiceClass.buildInsertSQL( mp.content, mp.fromParam, mp.mod, dbDyn, ctxInstance.getPortletRequest() );

                            if (log.isDebugEnabled())
                            {
                                log.debug("SQL:\n" + sql_ + "\n");
                                log.debug("Unmarshal sqlCache object");
                                synchronized(syncFile)
                                {
                                    XmlTools.writeToFile(mp.content.getQueryArea().getSqlCache(),
                                        WebmillConfig.getWebmillDebugDir()+"member-content.xml",
                                        "windows-1251");
                                }
                            }

                            boolean checkStatus = false;
                            // Todo check restrict commented for MYSQL DB -
                            // Todo this temporary solution - need full rewrite restriction method
                            switch (dbDyn.getFamaly())
                            {
                                case DatabaseManager.MYSQL_FAMALY:
                                    break;
                                default:
                                    checkStatus = mp.checkRestrict();
                                    if (!checkStatus)
                                        throw new ServletException("check status of restrict failed");

                                    break;
                            }
                            if (log.isDebugEnabled())
                                log.debug("check status - "+checkStatus);

                            ps = dbDyn.prepareStatement(sql_);
                            Object idNewRec = mp.bindInsert( dbDyn, ps );
                            i1 = ps.executeUpdate();

                            if (log.isDebugEnabled())
                                log.debug("Number of inserter record - " + i1);

                            DatabaseManager.close(ps);
                            ps = null;

                            // block for testing PK with different types
                            if (log.isDebugEnabled())
                            {
                                PreparedStatement psTest = null;
                                ResultSet rsTest = null;
                                try
                                {
                                    String sqlTest = "select * from " +
                                        (mp.content.getQueryArea().getTable(0)).getTable() +
                                        " where " + mp.content.getQueryArea().getPrimaryKey() + " = ?";

                                    log.debug("Test sql\n" + sqlTest);
                                    psTest = dbDyn.prepareStatement(sqlTest);
                                    log.debug("Test fetch. id - " + idNewRec);
                                    log.debug("Test fetch. type of PK - " + mp.content.getQueryArea().getPrimaryKeyType());

                                    switch (mp.content.getQueryArea().getPrimaryKeyType().getType())
                                    {
                                        case PrimaryKeyTypeType.NUMBER_TYPE:
                                            psTest.setLong(1, ((Long) idNewRec).longValue());
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
                                catch (Exception e)
                                {
                                    log.error("Error get test record", e);
                                }
                                finally
                                {
                                    DatabaseManager.close(rsTest, psTest);
                                    rsTest = null;
                                    psTest = null;
                                }
                            } // if(log.isDebugEnabled())

                            mp.prepareBigtextData(dbDyn, idNewRec, false);

                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++)
                            {
                                RelateClassType rc = mp.mod.getRelateClass(i);

                                if (log.isDebugEnabled())
                                    log.debug("#7.003.003 terminate class " + rc.getClassName());

                                CacheFactory.terminate(rc.getClassName(), null, Boolean.TRUE.equals(rc.getIsFullReinitCache()) );
                            }
                            dbDyn.commit();
                            redirURL = mp.getIndexURL();

                            if (log.isDebugEnabled())
                                log.debug( redirURL );

                            response.sendRedirect(redirURL);
                            return;

                        case ContentTypeActionType.CHANGE_TYPE:

                            if (log.isDebugEnabled())
                                log.debug("Commit change page");

                            validateStatus = mp.validateFields(dbDyn);
                            if ( validateStatus != null)
                            {
                                out.write("<html><head></head>\n<body>\n" +
                                    validateStatus +
                                    "\n<p><a href=\"" + mp.getIndexURL() + "\">Continue</a></p>\n" +
                                    "</body>\n</html>"
                                );

                                return;
                            }
                            if (MemberServiceClass.hasYesNoField(ctxInstance.getPortletRequest(), mp.mod, mp.content))
                            {
                                if (log.isDebugEnabled())
                                    log.debug("Found field with type "+FieldsTypeJspTypeType.YES_1_NO_N);

                                mp.process_Yes_1_No_N_Fields(dbDyn);
                            }

                            Object idCurrRec = null;

                            if (log.isDebugEnabled()) log.debug("PrimaryKeyType " + mp.content.getQueryArea().getPrimaryKeyType());

                            switch (mp.content.getQueryArea().getPrimaryKeyType().getType())
                            {
                                case PrimaryKeyTypeType.NUMBER_TYPE:
                                    log.debug("PrimaryKeyType - 'number'");

                                    idCurrRec = PortletTools.getLong(ctxInstance.getPortletRequest(), mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                                    break;
                                case PrimaryKeyTypeType.STRING_TYPE:
                                    log.debug("PrimaryKeyType - 'string'");

                                    idCurrRec = PortletTools.getString(ctxInstance.getPortletRequest(), mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                                    break;
/*
                        case PrimaryKeyTypeType.DATE_TYPE :

                            if (content.getQueryArea().primaryKeyMask==null ||
                                    content.getQueryArea().primaryKeyMask.trim().length()==0)
                                throw new Exception("date mask for primary key not specified");

                            primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().primaryKey,
                                    content.getQueryArea().primaryKeyMask, "error", Locale.ENGLISH);

                            break;
*/
                                default:
                                    throw new Exception("Change. Wrong type of primary key - " +
                                        mp.content.getQueryArea().getPrimaryKeyType());
                            }

                            if (log.isDebugEnabled()) log.debug("mp.isSimpleField(): "+mp.isSimpleField());

                            if (mp.isSimpleField())
                            {
                                log.debug("start build SQL");

                                sql_ = MemberServiceClass.buildUpdateSQL(mp.content, mp.fromParam, mp.mod, dbDyn, true, ctxInstance.getPortletRequest());

                                if (log.isDebugEnabled()) log.debug("SQL:"+sql_);

                                ps = dbDyn.prepareStatement(sql_);
                                mp.bindUpdate( dbDyn, ps, idCurrRec, true);

                                i1 = ps.executeUpdate();

                                if (log.isDebugEnabled()) log.debug("Number of updated record - " + i1);

                            }

                            log.debug("prepare big text");

                            mp.prepareBigtextData(dbDyn, idCurrRec, true);

                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() !=
                                PrimaryKeyTypeType.NUMBER_TYPE)
                                throw new Exception("PK of 'Bigtext' table must be a 'number' type");

                            log.debug("start sync cache data");

                            for (int i = 0; i < mp.mod.getRelateClassCount(); i++)
                            {
                                RelateClassType rc = mp.mod.getRelateClass(i);

                                if (log.isDebugEnabled()) log.debug("#7.003.002 terminate class " + rc.getClassName() + ", id_rec "+idCurrRec);

                                if (mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                    PrimaryKeyTypeType.NUMBER_TYPE)
                                {
                                    CacheFactory.terminate(rc.getClassName(), (Long)idCurrRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()) );
                                }
                                else
                                    throw new Exception("Change. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());
                            }
                            log.debug("do commit");

                            dbDyn.commit();
                            redirURL = mp.getIndexURL();

                            if (log.isDebugEnabled()) log.debug("redirect url: "+redirURL );

                            response.sendRedirect(redirURL);
                            return;

                        case ContentTypeActionType.DELETE_TYPE:

                            log.debug("Commit delete page<br>");

                            Object idRec = null;

                            if (mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                PrimaryKeyTypeType.NUMBER_TYPE) {
                                idRec = PortletTools.getLong(ctxInstance.getPortletRequest(), mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                            }
                            else if ( mp.content.getQueryArea().getPrimaryKeyType().getType() ==
                                PrimaryKeyTypeType.STRING_TYPE) {
                                idRec = PortletTools.getString(ctxInstance.getPortletRequest(), mp.mod.getName() + '.' + mp.content.getQueryArea().getPrimaryKey());
                            }
/*
else if ( content.getQueryArea().getPrimaryKeyType().equals("date"))
{
if (content.getQueryArea().primaryKeyMask==null ||
content.getQueryArea().primaryKeyMask.trim().length()==0)
throw new Exception("date mask for primary key not specified");

primaryKeyValue = RsetTools.getStringDate(rs, content.getQueryArea().primaryKey,
content.getQueryArea().primaryKeyMask, "error", Locale.ENGLISH);
}
*/
                            else
                                throw new Exception("Delete. Wrong type of primary key - " + mp.content.getQueryArea().getPrimaryKeyType());

                            // delete data from slave table for MySQL,
                            // 'cos mysql not support DELETE CASCADE reference integrity
                            // Todo switch from getFamaly() to metadata to
                            // Todo decide support or not DELETE CASCADE
                            if (dbDyn.getFamaly()==DatabaseManager.MYSQL_FAMALY)
                                mp.deleteBigtextData(dbDyn, idRec);

                            sql_ = MemberServiceClass.buildDeleteSQL(mp.content, mp.mod, mp.fromParam, dbDyn, ctxInstance.getPortletRequest());

                            if (log.isDebugEnabled()) log.debug("SQL: "+sql_+"<br>\n");

                            ps = dbDyn.prepareStatement(sql_);

                            mp.bindDelete(ps);
                            i1 = ps.executeUpdate();

                            if (log.isDebugEnabled()) log.debug("Number of deleted record - " + i1);

                            if (idRec!=null && (idRec instanceof Long)) {
                                for (int i = 0; i < mp.mod.getRelateClassCount(); i++){
                                    RelateClassType rc = mp.mod.getRelateClass(i);
                                    if (log.isDebugEnabled()) log.debug("#7.003.001 terminate class " + rc.getClassName() + ", id_rec " + idRec.toString());
                                    CacheFactory.terminate(rc.getClassName(), (Long)idRec, Boolean.TRUE.equals(rc.getIsFullReinitCache()) );
                                }
                            }

                            dbDyn.commit();
                            redirURL = mp.getIndexURL();

                            log.debug( redirURL );

                            response.sendRedirect(redirURL);
                            return;

                        default:
                            throw new Exception("Unknown type of action - " + actionName);
                    }

                }
                catch (Exception e1)
                {
                    try
                    {
                        dbDyn.rollback();
                    }
                    catch (Exception e001)
                    {
                    }

                    log.error("Error while processing this page", e1);
                    if (dbDyn.testExceptionIndexUniqueKey(e1))
                        out.println("You input value already exists in DB. Try again with other value");
                    else
                        out.println("Error while processing this page:<br>" + sql_ + "<br>" +
                                    ExceptionTools.getStackTrace(e1, mp.linesInException, "<br>")
                        );
                    return;
                }
                finally
                {
                    DatabaseManager.close(dbDyn, ps);
                    ps = null;
                    dbDyn = null;
                }

            } // if ("commit" ...


        }
        catch (Exception e)
        {
            log.error("General processing error ", e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
//        throw e;
        }
    }
}
