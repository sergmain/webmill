package org.riverock.portlet.auth.role.logic;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.auth.role.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class RoleProvider implements Serializable {
    private final static Logger log = Logger.getLogger( RoleProvider.class );
    private static final long serialVersionUID = 2043007702L;

    public List<RoleBean> getRoleList() {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            ps = db.prepareStatement(
                "select  ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from    WM_AUTH_ACCESS_GROUP "
            );

            rs = ps.executeQuery();
            List<RoleBean> list = new ArrayList<RoleBean>();
            while( rs.next() ) {
                RoleBean bean = new RoleBean();
                Long id = RsetTools.getLong( rs, "ID_ACCESS_GROUP" );
                String name = RsetTools.getString( rs, "NAME_ACCESS_GROUP" );

                if( name != null && id != null ) {
                    bean.setName( name );
                    bean.setRoleId( id );
                    list.add( bean );
                }
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }
}