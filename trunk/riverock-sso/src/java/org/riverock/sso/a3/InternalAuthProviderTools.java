/*

 * org.riverock.sso -- Single Sign On implementation

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

 * Date: Sep 23, 2003

 * Time: 7:36:36 PM

 *

 * $Id$

 */

package org.riverock.sso.a3;



import java.sql.PreparedStatement;

import java.sql.ResultSet;



import javax.servlet.http.HttpServletRequest;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.sso.utils.AuthHelper;

import org.riverock.sso.schema.core.AuthUserItemType;

import org.riverock.sso.core.GetAuthUserItem;



import org.apache.log4j.Logger;



public class InternalAuthProviderTools

{

    private static Logger log = Logger.getLogger(InternalAuthProviderTools.class);



    public static boolean checkRigthOnUser(DatabaseAdapter db_,

        Long id_auth_user_check, Long id_auth_user_owner)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            switch (db_.getFamaly())

            {

                case DatabaseManager.MYSQL_FAMALY:

                    AuthUserItemType auth = GetAuthUserItem.getInstance(db_, id_auth_user_owner).item;

                    if (auth==null)

                        return false;



                    ps = db_.prepareStatement(

                        "select null " +

                        "from AUTH_USER a, MAIN_USER_INFO b " +

                        "where a.ID_USER=b.ID_USER and a.ID_AUTH_USER=? and " +

                        "b.ID_FIRM  in ("+AuthHelper.getGrantedFirmId(db_, auth.getUserLogin())+") "

                    );



                    RsetTools.setLong(ps, 1, id_auth_user_check);

                    break;

                default:

                    ps = db_.prepareStatement(

                        "select null " +

                        "from AUTH_USER a, MAIN_USER_INFO b, V$_READ_LIST_FIRM z1 " +

                        "where a.ID_USER=b.ID_USER and a.ID_AUTH_USER=? and " +

                        "b.ID_FIRM = z1.ID_FIRM and z1.ID_AUTH_USER=? "

                    );



                    RsetTools.setLong(ps, 1, id_auth_user_check);

                    RsetTools.setLong(ps, 2, id_auth_user_owner);

                    break;

            }

            rs = ps.executeQuery();



            return rs.next();

        }

        catch (Exception e)

        {

            log.error("Error check right on user", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static Long addRole(DatabaseAdapter db_, String role_name)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            String sql_ =

                "insert into AUTH_ACCESS_GROUP " +

                "( ID_ACCESS_GROUP, NAME_ACCESS_GROUP ) " +

                "VALUES "+

                "( ?, ? )";



            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_auth_access_group");

            seq.setTableName( "AUTH_ACCESS_GROUP");

            seq.setColumnName( "ID_ACCESS_GROUP" );

            Long id = new Long(db_.getSequenceNextValue( seq ));



            ps = db_.prepareStatement(sql_);



            ps.setObject(1, id);

            ps.setString(2, role_name);

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return id;

        }

        catch (Exception e)

        {

            log.error("Error add role", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static Long checkWithCreateRole(DatabaseAdapter ora_, String role_name)

        throws Exception

    {



        Long id_group = getIDRole(ora_, role_name);

        if (id_group == null)

            id_group = addRole(ora_, role_name);



        return id_group;



    }



    public static Long getUserRole(DatabaseAdapter ora_, Long id_auth_user,

        Long id_role)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = ora_.prepareStatement(

                "select id_relate_accgroup from auth_relate_accgroup " +

                "where id_auth_user=? and id_access_group=?"

            );

            ps.setObject(1, id_auth_user);

            ps.setObject(2, id_role);

            rs = ps.executeQuery();



            Long result = null;

            if (rs.next())

                result = RsetTools.getLong(rs, "id_relate_accgroup");



            return result;

        }

        catch (Exception e)

        {

            log.error("Error get user role", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static Long getUserRole(DatabaseAdapter ora_, Long id_user,

        String role_name)

        throws Exception

    {

        Long id_role = getIDRole(ora_, role_name);

        if (id_role == null)

            return null;



        return getUserRole(ora_, id_user, id_role);

    }



    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user,

        String role_name)

        throws Exception

    {

        Long id_role = checkWithCreateRole(ora_, role_name);

        return bindUserRole(ora_, id_auth_user, id_role);

    }



    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user,

        Long id_role)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_auth_relate_accgroup");

            seq.setTableName( "AUTH_RELATE_ACCGROUP");

            seq.setColumnName( "ID_RELATE_ACCGROUP" );

            long id = ora_.getSequenceNextValue( seq );



            ps = ora_.prepareStatement(

                "insert into AUTH_RELATE_ACCGROUP " +

                "(ID_RELATE_ACCGROUP, ID_AUTH_USER, ID_ACCESS_GROUP) " +

                "values "+

                "(?, ?, ? )"

            );

            ps.setLong(1, id);

            ps.setObject(2, id_auth_user);

            ps.setObject(3, id_role);

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);

        }

        catch (Exception e)

        {

            log.error("Error bind user role", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

        return true;

    }



    public static Long addUserAuth(DatabaseAdapter db_, Long id_user,

        Long id_firm, Long id_service, Long id_road,

        String username, String password,

        boolean isFirm, boolean isService, boolean isRoad)

        throws Exception

    {



        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_auth_user");

            seq.setTableName( "AUTH_USER");

            seq.setColumnName( "ID_AUTH_USER" );

            long id_auth_user = db_.getSequenceNextValue( seq );



            ps = db_.prepareStatement(

                "insert into AUTH_USER	" +

                "(ID_AUTH_USER, id_user, ID_FIRM, user_login, user_password, " +

                "is_service, is_road, is_use_current_firm, is_root, id_road, id_service)" +

                "values "+

                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"

            );



            ps.setLong(1, id_auth_user);

            RsetTools.setLong(ps, 2, id_user);

            RsetTools.setLong(ps, 3, id_firm);

            ps.setString(4, username);

            ps.setString(5, password);

            ps.setInt(6, isService ? 1 : 0);

            ps.setInt(7, isRoad ? 1 : 0);

            ps.setInt(8, isFirm ? 1 : 0);

            ps.setInt(9, 0);

            RsetTools.setLong(ps, 10, id_road);

            RsetTools.setLong(ps, 11, id_service);



            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return new Long(id_auth_user);

        }

        catch (Exception e)

        {

            log.error("Error add user auth", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static void setRelateServiceFirm(DatabaseAdapter ora_, Long id_service, Long id_firm)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            if (!getRelateServiceFirm(ora_, id_firm, id_service))

            {

                CustomSequenceType seq = new CustomSequenceType();

                seq.setSequenceName("seq_main_relate_service_firm");

                seq.setTableName( "MAIN_RELATE_SERVICE_FIRM");

                seq.setColumnName( "ID_REL_SERVICE" );

                long id = ora_.getSequenceNextValue( seq );



                ps = ora_.prepareStatement(

                    "insert into MAIN_RELATE_SERVICE_FIRM " +

                    "(ID_REL_SERVICE, ID_SERVICE, ID_FIRM) " +

                    "values "+

                    "(?, ?, ?)"

                );

                ps.setLong(1, id);

                ps.setObject(2, id_service);

                ps.setObject(3, id_firm);

                int i = ps.executeUpdate();



                if (log.isDebugEnabled())

                    log.debug("Count of added role - "+i);

            }

        }

        catch (Exception e)

        {

            log.error("Error setRelateServiceFirm", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }



    }



    public static void setRelateServiceFirm(DatabaseAdapter ora_, String service_name, String firm_name)

        throws Exception

    {

        setRelateServiceFirm(ora_, service_name, firm_name, false, false);

    }



    public static void setRelateServiceFirm(DatabaseAdapter ora_, String service_name, String firm_name, boolean add_service, boolean add_firm)

        throws Exception

    {

        Long id_firm = getIDFirm(ora_, firm_name);

        if ((id_firm == null) && (add_firm))

            id_firm = addNewFirm(ora_, firm_name);

        else

            return;



        Long id_service = getIDService(ora_, service_name);

        if ((id_service == null) && (add_service))

            id_service = addNewService(ora_, service_name);

        else

            return;



        setRelateServiceFirm(ora_, id_service, id_firm);

    }



    public static Long addNewUser(DatabaseAdapter ora_, String first_name,

        String last_name, String middle_name, String firm_name,

        String service_name, String email, String address,

        String phone)

        throws Exception

    {



        setRelateServiceFirm(ora_, service_name, firm_name, true, true);



        return addNewUser(ora_, first_name, last_name, middle_name,

            getIDFirm(ora_, firm_name), email, address, phone);

    }



    public static Long addNewUser(DatabaseAdapter db_, String first_name,

        String last_name, String middle_name, Long id_firm,

        String email, String address, String phone)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_main_user_info");

            seq.setTableName( "MAIN_USER_INFO");

            seq.setColumnName( "ID_USER" );

            long id = db_.getSequenceNextValue( seq );



            ps = db_.prepareStatement(

                "insert into MAIN_USER_INFO " +

                "(ID_USER, FIRST_NAME, LAST_NAME, MIDDLE_NAME, " +

                "ID_FIRM, EMAIL, ADDRESS, TELEPHONE, DATE_START_WORK ) " +

                "values "+

                "( ?, ?, ?, ?, ?, ?, ?, ?, "+db_.getNameDateBind()+" )	"

            );

            ps.setLong(1, id);

            ps.setString(2, first_name);

            ps.setString(3, last_name);

            ps.setString(4, middle_name);

            ps.setObject(5, id_firm);

            ps.setString(6, email);

            ps.setString(7, address);

            ps.setString(8, phone);

            db_.bindDate(ps, 9, DateTools.getCurrentTime());

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return new Long(id);

        }

        catch (Exception e)

        {

            log.error("Error addNewUser", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static Long addNewFirm(DatabaseAdapter db_, String nameFirm)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_main_list_firm");

            seq.setTableName( "MAIN_LIST_FIRM");

            seq.setColumnName( "ID_FIRM" );

            long ID = db_.getSequenceNextValue( seq );



            ps = db_.prepareStatement(

                "insert into MAIN_LIST_FIRM " +

                "(ID_FIRM, FULL_NAME, SHORT_NAME) " +

                "values "+

                "(?, ?, ?)"

            );

            ps.setLong(1, ID);

            ps.setString(2, nameFirm);

            ps.setString(3, nameFirm);

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return new Long(ID);

        }

        catch (Exception e)

        {

            log.error("Error addNewFirm", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static Long addNewService(DatabaseAdapter db_, String nameService)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_main_list_service");

            seq.setTableName( "MAIN_LIST_SERVICE");

            seq.setColumnName( "ID_SERVICE" );

            long ID = db_.getSequenceNextValue( seq );



            ps = db_.prepareStatement(

                "insert into MAIN_LIST_SERVICE " +

                "(ID_SERVICE, FULL_NAME_SERVICE, SHORT_NAME_SERVICE) " +

                "values "+

                "(?, ?, ? )"

            );

            ps.setLong(1, ID);

            ps.setString(2, nameService);

            ps.setString(3, nameService);

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return new Long(ID);

        }

        catch (Exception e)

        {

            log.error("Error addNewService", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static long addNewRoad(DatabaseAdapter db_, String nameRoad)

        throws Exception

    {

        PreparedStatement ps = null;

        try

        {

            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName("seq_main_list_road");

            seq.setTableName( "MAIN_LIST_ROAD");

            seq.setColumnName( "ID_ROAD" );

            long ID = db_.getSequenceNextValue( seq );



            ps = db_.prepareStatement(

                "insert into MAIN_LIST_ROAD " +

                "(ID_ROAD, FULL_NAME_ROAD, NAME_ROAD) " +

                "values "+

                "(?, ?, ? )"

            );

            ps.setLong(1, ID);

            ps.setString(2, nameRoad);

            ps.setString(3, nameRoad);

            int i = ps.executeUpdate();



            if (log.isDebugEnabled())

                log.debug("Count of added role - "+i);



            return ID;

        }

        catch (Exception e)

        {

            log.error("Error addNewRoad", e);

            throw e;

        }

        finally

        {

            if (ps != null)

            {

                try

                {

                    ps.close();

                    ps = null;

                }

                catch (Exception e02)

                {

                }

            }

        }

    }



    public static Long getIDFirm(DatabaseAdapter db_, String nameFirm)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(

                "select ID_FIRM from MAIN_LIST_FIRM where SHORT_NAME=?"

            );

            ps.setString(1, nameFirm);

            rs = ps.executeQuery();

            Long id = null;

            if (rs.next())

                id = RsetTools.getLong(rs, "ID_FIRM");



            return id;

        }

        catch (Exception e)

        {

            log.error("Error getIDFirm", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static Long getIDService(DatabaseAdapter db_, String nameService)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(

                "select ID_SERVICE from MAIN_LIST_SERVICE where SHORT_NAME_SERVICE=?"

            );

            ps.setString(1, nameService);

            rs = ps.executeQuery();

            Long id = null;

            if (rs.next())

                id = RsetTools.getLong(rs, "ID_SERVICE");



            return id;

        }

        catch (Exception e)

        {

            log.error("Error getIDService", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static Long getIDRoad(DatabaseAdapter db_, String nameRoad)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(

                "select ID_ROAD from MAIN_LIST_ROAD where NAME_ROAD=?"

            );

            ps.setString(1, nameRoad);

            rs = ps.executeQuery();

            Long id = null;

            if (rs.next())

                id = RsetTools.getLong(rs, "ID_ROAD");



            return id;

        }

        catch (Exception e)

        {

            log.error("Error getIDRoad", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static boolean getRelateServiceFirm(DatabaseAdapter ora_,

        Long id_firm, Long id_service)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = ora_.prepareStatement(

                "select null COUNT_REC from MAIN_RELATE_SERVICE_FIRM " +

                "where ID_FIRM=? and ID_SERVICE=?"

            );

            ps.setObject(1, id_firm);

            ps.setObject(2, id_service);

            rs = ps.executeQuery();



            return rs.next();

        }

        catch (Exception e)

        {

            log.error("Error getRelateServiceFirm", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static Long getIDRole(DatabaseAdapter db_, String role_name)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(

                "select ID_ACCESS_GROUP from AUTH_ACCESS_GROUP where NAME_ACCESS_GROUP=?"

            );

            ps.setString(1, role_name);

            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getLong(rs, "ID_ACCESS_GROUP");



            return null;

        }

        catch (Exception e)

        {

            log.error("Error getIDRole", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    public static final String firmIdParam = "id_firm";

    public static Long initIdFirm(DatabaseAdapter db_, HttpServletRequest request, String userLogin)

        throws Exception

    {

        return initIdFirm(db_, ServletTools.getLong(request, firmIdParam), userLogin);

    }



    public static Long initIdFirm(DatabaseAdapter db_, Long firmId, String userLogin)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            switch (db_.getFamaly())

            {

                case DatabaseManager.MYSQL_FAMALY:

                    ps = db_.prepareStatement(

                        "select ID_FIRM from main_list_firm " +

                        "where ID_FIRM in ("+AuthHelper.getGrantedFirmId(db_, userLogin)+") and ID_FIRM=?"

                    );

                    ps.setObject(1, firmId);

                    break;

                default:

                    ps = db_.prepareStatement(

                        "select ID_FIRM from v$_read_list_firm where USER_LOGIN=? and ID_FIRM=?"

                    );



                    ps.setString(1, userLogin);

                    ps.setObject(2, firmId);

                    break;

            }





            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getLong(rs, "ID_FIRM");



            return null;

        }

        catch (Exception e)

        {

            log.error("Error get idFirm", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static final String serviceIdParam = "id_service";

    public static Long initIdService(DatabaseAdapter db_, HttpServletRequest request, String userLogin)

        throws Exception

    {

        return initIdService(db_, ServletTools.getLong(request, serviceIdParam), userLogin);

    }



    public static Long initIdService(DatabaseAdapter db_, Long serviceId, String userLogin)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            switch (db_.getFamaly())

            {

                case DatabaseManager.MYSQL_FAMALY:

                    ps = db_.prepareStatement(

                        "select ID_SERVICE from main_list_service " +

                        "where ID_SERVICE in ("+AuthHelper.getGrantedServiceId(db_, userLogin)+") and ID_SERVICE=?"

                    );

                    ps.setObject(1, serviceId);

                    break;

                default:

                    ps = db_.prepareStatement(

                        "select ID_SERVICE from v$_read_list_service where USER_LOGIN=? and ID_SERVICE=?"

                    );



                    ps.setString(1, userLogin);

                    ps.setObject(2, serviceId);



                    break;

            }

            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getLong(rs, "ID_SERVICE");



            return null;

        }

        catch (Exception e)

        {

            log.error("Error get idService", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



    public static final String roadIdParam = "id_road";

    public static Long initIdRoad(DatabaseAdapter db_, HttpServletRequest request, String userLogin)

        throws Exception

    {

        return initIdRoad(db_, ServletTools.getLong(request, roadIdParam), userLogin);

    }



    public static Long initIdRoad(DatabaseAdapter db_, Long roadId, String userLogin)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            switch (db_.getFamaly())

            {

                case DatabaseManager.MYSQL_FAMALY:

                    ps = db_.prepareStatement(

                        "select ID_ROAD from main_list_road " +

                        "where ID_ROAD in ("+AuthHelper.getGrantedRoadId(db_, userLogin)+") and ID_ROAD=?"

                    );

                    ps.setObject(1, roadId);

                    break;

                default:

                    ps = db_.prepareStatement(

                        "select ID_ROAD from v$_read_list_road where USER_LOGIN = ? and ID_ROAD=?"

                    );



                    ps.setString(1, userLogin);

                    ps.setObject(2, roadId);



                    break;

            }

            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getLong(rs, "ID_ROAD");



            return null;

        }

        catch (Exception e)

        {

            log.error("Error get idRoad", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }

    }



}

