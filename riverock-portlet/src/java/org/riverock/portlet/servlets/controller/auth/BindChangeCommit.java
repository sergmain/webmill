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

 * Author: mill

 * Date: Dec 3, 2002

 * Time: 11:55:59 AM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.controller.auth;



import java.io.IOException;

import java.io.Writer;

import java.sql.PreparedStatement;

import java.sql.Types;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.sso.a3.InternalAuthProviderTools;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



public class BindChangeCommit extends HttpServlet

{

    private static Logger cat = Logger.getLogger(BindChangeCommit.class);



    public BindChangeCommit()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Long idFirm = null;

        Long idService = null;

        Long idRoad = null;



        Writer out = null;

        try

        {



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if ( auth_==null )

                return;



            DatabaseAdapter dbDyn = null;

            String index_page = null;



            if( auth_.isUserInRole("webmill.auth_bind") )

            {

                PreparedStatement ps = null;

                try

                {

                    dbDyn = DatabaseAdapter.getInstance( true );

                    InitPage  jspPage =  new InitPage(dbDyn, request,

                                                      "mill.locale.AUTH_USER"

                    );



                    index_page = CtxURL.url( request, response, jspPage, "mill.auth.bind");



                    if (ServletTools.isNotInit(request, response, "id_auth_user", index_page))

                        return;

                    Long id_auth_user = ServletTools.getLong(request, "id_auth_user");



                    AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

                    AuthInfo authInfoUser = AuthInfo.getInstance( dbDyn, id_auth_user );



                    boolean isRigthRelate = false;



                    if (authInfoUser!=null &&  authInfo!= null)

                    {

                        isRigthRelate =

                                InternalAuthProviderTools.checkRigthOnUser(dbDyn,

                                        authInfoUser.authUserID, authInfo.authUserID);

                    }



                    if (isRigthRelate)

                    {

                        ps = dbDyn.prepareStatement(

                            "UPDATE AUTH_USER "+

                            "SET "+

                            "	user_login = ?, "+

                            "	user_password = ?, "+

                            "	is_service = ?, "+

                            "	is_road = ?, "+

                            "	is_use_current_firm = ?, "+

                            "	ID_FIRM = ?, "+

                            "	id_service = ?, "+

                            "	id_road = ? "+

                            "WHERE id_auth_user=? and ID_FIRM in "+

                            "(select z.ID_FIRM from v$_read_list_firm z where z.user_login = ? )"

                        );



                        idFirm = InternalAuthProviderTools.initIdFirm(dbDyn, request, authInfo.userLogin);

                        idService = InternalAuthProviderTools.initIdService(dbDyn, request, authInfo.userLogin);

                        idRoad = InternalAuthProviderTools.initIdRoad(dbDyn, request, authInfo.userLogin);



                        if (cat.isDebugEnabled())

                        {

                            cat.debug("idFirm "+idFirm);

                            cat.debug("idService "+idService);

                            cat.debug("idRoad " + idRoad);



                            cat.debug("user_login "+ ServletUtils.getString(request, "user_login"));

                            cat.debug("user_password "+ ServletUtils.getString(request, "user_password"));



                            cat.debug("is_service "+ (authInfo.isService==1?

                                    ServletTools.getInt(request, "is_service", new Integer(0)).intValue():

                                    0

                                    )

                            );

                            cat.debug("is_road "+ (authInfo.isRoad==1?

                                    ServletTools.getInt(request, "is_road", new Integer(0)).intValue():

                                    0

                                    )

                            );

                            cat.debug("is_use_current_firm "+ (authInfo.isUseCurrentFirm==1?

                                    ServletTools.getInt(request, "is_use_current_firm", new Integer(0)).intValue():

                                    0

                                    )

                            );

                            cat.debug("id_auth_user "+ id_auth_user);

                            cat.debug("auth_.getUserLogin() "+ auth_.getUserLogin() );

                        }





                        ps.setString(1, ServletUtils.getString(request, "user_login"));

                        ps.setString(2, ServletUtils.getString(request, "user_password"));



                        RsetTools.setInt(ps,3, (authInfo.isService==1?

                                ServletTools.getInt(request, "is_service"):

                                null

                                )

                        );

                        RsetTools.setInt(ps, 4, (authInfo.isRoad==1?

                                ServletTools.getInt(request, "is_road"):

                                null

                                )

                        );

                        RsetTools.setInt(ps, 5, (authInfo.isUseCurrentFirm==1?

                                ServletTools.getInt(request, "is_use_current_firm"):

                                null

                                )

                        );



                        if (idFirm != null)

                            RsetTools.setLong(ps, 6, idFirm );

                        else

                            ps.setNull(6, Types.INTEGER);



                        if (idService != null)

                            RsetTools.setLong(ps, 7, idService );

                        else

                            ps.setNull(7, Types.INTEGER);



                        if (idRoad != null)

                            RsetTools.setLong(ps, 8, idRoad );

                        else

                            ps.setNull(8, Types.INTEGER);



                        RsetTools.setLong(ps, 9, id_auth_user);

                        ps.setString(10, auth_.getUserLogin() );



                        int i1 = ps.executeUpdate();



                        if (cat.isDebugEnabled())

                            cat.debug("Count of updated records - "+i1);



                        dbDyn.commit();

                    }



                    response.sendRedirect( index_page );

                    return;



                }

                catch(Exception e1)

                {

                    cat.error("Error commit add bind", e1);

                    try

                    {

                        dbDyn.rollback();

                    }catch(Exception e01){};

                    out.write("<html><head></head<body>" +

                            "Error while processing this page:<br>"+

                            ExceptionTools.getStackTrace(e1, 20, "<br>")+"<br>" +

                            "<p><a href=\"" + index_page+ "\">continue</a></p>" +

                            "</body></html>"

                    );

                }

                finally

                {

                    if (ps != null)

                    {

                        try {

                            ps.close();

                            ps = null;

                        } catch(Exception e) {}

                    }

                    if (dbDyn !=null)

                    {

                        DatabaseAdapter.close( dbDyn );

                        dbDyn = null;

                    }

                }

            }

        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }



}

