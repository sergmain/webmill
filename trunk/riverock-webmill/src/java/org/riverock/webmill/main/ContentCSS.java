/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.common.tools.RsetTools;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

import org.apache.log4j.Logger;

/**
 *
 * $Revision$
 * $Date$
 * $RCSfile$
 *
 */
public final class ContentCSS
{
    private final static Logger log = Logger.getLogger( ContentCSS.class );

    private String css = "";
    private Calendar datePost = null;

    private static CacheFactory cache = new CacheFactory( ContentCSS.class.getName() );

    protected void finalize() throws Throwable
    {
        setCss( null );
        setDatePost( null );

        super.finalize();
    }

    public ContentCSS(){}

    public void terminate(Long id)
    {
        cache.reinit();
    }

    public void reinit()
    {
        cache.reinit();
    }

    public static ContentCSS getInstance(DatabaseAdapter db__, long id__)
            throws Exception
    {
        return (ContentCSS) cache.getInstanceNew(db__, id__ );
    }

    public static ContentCSS getInstance(DatabaseAdapter db__, Long id__)
            throws Exception
    {
        if (id__==null)
            return null;
        
        return (ContentCSS) cache.getInstanceNew(db__, id__);
    }

    static String sql_ =
        "select a.date_post, b.css_data " +
        "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " +
        "where  a.ID_SITE_SUPPORT_LANGUAGE=? and a.is_current=1 and " +
        "       a.id_site_content_css=b.id_site_content_css " +
        "order by ID_SITE_CONTENT_CSS_DATA asc";
    static {
        try {
            SqlStatement.registerSql( sql_, new ContentCSS().getClass() );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public ContentCSS(DatabaseAdapter db_, Long id_)
            throws Exception
    {
        if (id_ == null)
            return;

        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        try
        {
            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong(ps, 1, id_);
            rset = ps.executeQuery();
            setCss( "" );
            while (rset.next()){
                if (isFirstRecord){
                    setDatePost( RsetTools.getCalendar(rset, "DATE_POST") );
                    isFirstRecord = false;
                }

                setCss( getCss() + RsetTools.getString(rset, "CSS_DATA") );
            }
        }
        catch (Exception e) {
            log.error("Exception in ContentCSS()", e);
            throw e;
        }
        finally {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }
    }

    public String getCss() {
        return css;
    }

    public void setCss( String css ) {
        this.css = css;
    }

    public Calendar getDatePost() {
        return datePost;
    }

    public void setDatePost( Calendar datePost ) {
        this.datePost = datePost;
    }

    public boolean getIsEmpty() {
        if (css==null || css.length()==0)
            return true;
        else
            return false;
    }
}