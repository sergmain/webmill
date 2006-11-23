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
import java.util.Calendar;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;

/**
 * $Id$
 */
public final class FaqItem implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( FaqItem.class );

    public String question = null;
    public String answer = null;
    public Calendar datePost = null;

    public void reinit() {
    }

    public void terminate( Long id_ ) {
    }

    public FaqItem( String q, String a ) {
        question = q;
        answer = a;
    }

    public FaqItem() {
    }

    public String getFaqItemQuestion() {
        return StringTools.prepareToParsing( question );
    }

    public String getFaqItemAnswer() {
        return StringTools.prepareToParsing( answer );
    }

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
    }

    public byte[] getPlainHTML() throws Exception {
        return null;
    }

    public byte[] getXml( String rootElement ) throws Exception {
        return null;
    }

    public byte[] getXml() throws Exception {
        return null;
    }

    public PortletResultContent getInstance( Long id ) {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            return new FaqItem( db_, id );
        }
        catch (Exception e) {
            String es = "Error getInstance()";
            log.error(es, e);
//            throw new RuntimeException(es, e);
        }
        finally {
            DatabaseManager.close(db_);
            db_ = null;
        }
        return null;
    }

    public PortletResultContent getInstanceByCode( String portletCode_ ) {
        return null;
    }

    public FaqItem( DatabaseAdapter db_, Long id) throws PortletException {
        if ( id==null)
            return;

        String sql_ =
            "select DATE_POST " +
            "from   WM_PORTLET_FAQ_LIST " +
            "where  ID_SITE_PORTLET_FAQ_LIST=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                datePost = RsetTools.getCalendar(rs, "DATE_POST");
            }
            answer = DatabaseManager.getBigTextField(db_, id, "ANSWER", "WM_PORTLET_FAQ_ANSWER", "ID_SITE_PORTLET_FAQ_LIST", "ID_SITE_PORTLET_FAQ_ANSWER");
            question = DatabaseManager.getBigTextField(db_, id, "QUESTION", "WM_PORTLET_FAQ_QUESTION", "ID_SITE_PORTLET_FAQ_LIST", "ID_SITE_PORTLET_FAQ_QUESTION");
        }
        catch(Exception exc) {
            final String es = "Error create Faq item";
            log.error(es, exc);
            throw new PortletException(es, exc);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public List<ClassQueryItem> getList(Long idSiteCtxLangCatalog, Long idContext) {
        return null;
    }

    public void setPortalDaoProvider(PortalDaoProvider provider) {
    }
}