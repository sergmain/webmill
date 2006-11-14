/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.faq;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;

/**
 * Author: Serg Malyukov
 * Date: Aug 24, 2002
 * Time: 2:41:03 AM
 * <p/>
 * $Id$
 */
public class FaqGroup implements PortletGetList {
    private static Logger log = Logger.getLogger(FaqGroup.class);

    private static CacheFactory cache = new CacheFactory(FaqGroup.class);

    static String sql_ = null;
    static {
        sql_ =
            "select FAQ_CODE, NAME_FAQ from WM_PORTLET_FAQ where ID_SITE_PORTLET_FAQ=?";

        try {
            SqlStatement.registerSql(sql_, FaqGroup.class);
        }
        catch (Throwable e) {
            String es = "Exception in registerSql, sql\n" + sql_;
            log.error(es, e);
            throw new SqlStatementRegisterException(es, e);
        }
    }
    static String sql1_ = null;
    static {
        sql1_ =
            "select * from WM_PORTLET_FAQ_LIST where ID_SITE_PORTLET_FAQ=? " +
            "ORDER BY ORDER_FIELD asc, DATE_POST DESC";

        try {
            SqlStatement.registerSql(sql1_, FaqGroup.class);
        }
        catch (Throwable e) {
            String es = "Exception in registerSql, sql\n" + sql1_;
            log.error(es, e);
            throw new SqlStatementRegisterException(es, e);
        }
    }


    public String faqGroupName = "";
    public Long id = null;
    public String faqCode = "";
    public List<FaqItem> v = new ArrayList<FaqItem>(); 

    public void reinit() {
        cache.reinit();
    }

    public synchronized void terminate(Long id) {
        cache.terminate(id);
    }

    public String getFaqGroupName() {
        return faqGroupName;
    }

    public List getFaqItem() {
        return v;
    }

    public FaqGroup() {
    }

    public static FaqGroup getInstance(Long id__) throws Exception {
        return (FaqGroup) cache.getInstanceNew(id__);
    }

    public FaqGroup( Long id_) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        id = id_;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                faqGroupName = RsetTools.getString(rs, "NAME_FAQ");
                faqCode = RsetTools.getString(rs, "FAQ_CODE");

                initList();
            }
        }
        catch (Exception e) {
            log.error("Exception in FaqGroup()", e);
            throw e;
        }
        catch (Error e) {
            log.error("Error in FaqGroup()", e);
            throw e;
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            rs = null;
            ps = null;
            adapter = null;
        }
    }

    public void initList() throws Exception {
        if (log.isDebugEnabled())
            log.debug("#10.07.01 " + id);

        if (id == null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        if (log.isDebugEnabled())
            log.debug("#10.07.02 ");

        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(sql1_);
            RsetTools.setLong(ps, 1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                Long idFaqGroupItem = RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ_LIST");

                if (log.isDebugEnabled())
                    log.debug("#10.07.03 " + idFaqGroupItem);

                FaqItem fi = new FaqItem(db_, idFaqGroupItem);
                fi.datePost = RsetTools.getCalendar(rs, "DATE_POST");
                v.add(fi);
            }
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    public List<ClassQueryItem> getList(Long idSiteCtxLangCatalog, Long idContext) {
        return null;
    }
}