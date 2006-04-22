package org.riverock.portlet.member;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 2:45:55
 *         $Id$
 */
public class MemberTools {
    private final static Logger log = Logger.getLogger( MemberTools.class );

    public static PortalDaoProvider getPortalDaoProvider(PortletRequest portletRequest) {
        return (PortalDaoProvider)portletRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
    }

    public static String getGrantedSiteId( DatabaseAdapter adapter, String username ) {
        List<Long> list = getGrantedSiteIdList( adapter, username );
        if( list.size() == 0 )
            return "NULL";

        Iterator<Long> it = list.iterator();
        String r = "";
        while( it.hasNext() ) {
            if( r.length() != 0 ) {
                r += ", ";
            }
            r += it.next();
        }
        return r;
    }

    public static List<Long> getGrantedSiteIdList( DatabaseAdapter adapter, String serverName ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select ID_SITE " +
                "from   WM_PORTAL_VIRTUAL_HOST " +
                "where  lower(NAME_VIRTUAL_HOST)=lower(?)";

            ps = adapter.prepareStatement( sql_ );
            ps.setString( 1, serverName );

            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while( rs.next() ) {
                Long id = RsetTools.getLong( rs, "ID_SITE" );
                if( id == null )
                    continue;
                list.add( id );
            }
            return list;
        }
        catch( Exception e ) {
            final String es = "Exception get siteID";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    /**
     * Печатает в контексте JSP значение поля, хранящего логическое значение в формате
     * 1- true, 0-false
     * Если isEdit==true, то печатается в формате:<br>
     * <PRE>
     * <PRE>
     * <OPTION SELECTED value=1>ДА
     * <OPTION value=0>НЕТ
     * </PRE>
     * </PRE>
     * Если isEdit==false, то печатается для 1 - "ДА", для 0-"НЕТ"
     *
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * boolean isEdit - используется в редактируемой форме или только для просмотра
     * </blockquote>
     */
    public static String printYesNo( ResultSet rs, String f, boolean isEdit, ResourceBundle bundle )
        throws SQLException {
        return printYesNo( RsetTools.getInt( rs, f, 0 ), isEdit, bundle );
    }

    /**
     * Печатает в контексте JSP значение поля, хранящего логическое значение в формате
     * 1- true, 0-false
     * Если isEdit==true, то печатается в формате:<br>
     * <PRE>
     * <PRE>
     * <OPTION SELECTED value=1>ДА
     * <OPTION value=0>НЕТ
     * </PRE>
     * </PRE>
     * Если isEdit==false, то печатается для 1 - "ДА", для 0-"НЕТ"
     *
     * Параметры:
     * <blockquote>
     * int val - значение для вывода
     * boolean isEdit - используется в редактируемой форме или только для просмотра
     * </blockquote>
     */
    public static String printYesNo( int val, boolean isEdit, ResourceBundle bundle ) {

        String v_is_select_yes;
        String v_is_select_no;
        String r = "";

        if (isEdit) {
            if (val == 1) {
                v_is_select_yes = " selected";
                v_is_select_no = "";
            }
            else {
                v_is_select_yes = "";
                v_is_select_no = " selected";
            }

            r += ("<OPTION value=\"1\"" + v_is_select_yes + ">" + bundle.getString("yesno.yes") + "</option>\n" +
                    "<OPTION value=\"0\"" + v_is_select_no + ">" + bundle.getString("yesno.no") + "</option>\n");
        }
        else {
            if (val == 1)
                r += bundle.getString("yesno.yes");
            else
                r += bundle.getString("yesno.no");
        }

        return r;
    }
}
