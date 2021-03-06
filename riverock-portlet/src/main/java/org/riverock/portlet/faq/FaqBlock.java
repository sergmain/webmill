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
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.XmlTools;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.faq.schema.FaqBlockType;
import org.riverock.portlet.faq.schema.FaqGroupType;
import org.riverock.portlet.faq.schema.FaqItemType;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;

/**
 * $Id: FaqBlock.java 1435 2007-09-23 17:08:57Z serg_main $
 */
public final class FaqBlock implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( FaqBlock.class );

    private List<FaqGroup> v = null;
    private RenderRequest renderRequest = null;
//    private RenderResponse renderResponse = null;
//    private ResourceBundle bundle = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
//        this.renderResponse = renderResponse;
//        this.bundle = bundle;
    }

    public PortletResultContent getInstance( final Long id ) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( final String portletCode_ ) throws PortletException {
        return getInstance();
    }

    public List getFaqGroup() {
        return v;
    }

    public FaqBlock() {
    }

    public PortletResultContent getInstance() throws PortletException {

        String sql_ =
            "select ID_SITE_PORTLET_FAQ from WM_PORTLET_FAQ where ID_SITE_SUPPORT_LANGUAGE=?";

        v = new ArrayList<FaqGroup>();
        if (true) return this;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
//            ps = db_.prepareStatement( sql_ );

            PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
            Long idSupportLanguageCurrent = portalInfo.getSiteLanguageId( renderRequest.getLocale() );
            RsetTools.setLong( ps, 1, idSupportLanguageCurrent );

            rs = ps.executeQuery();
            while( rs.next() ) {
                log.debug( "#10.01.04 " + RsetTools.getLong( rs, "ID_SITE_PORTLET_FAQ" ) );
                v.add( new FaqGroup( RsetTools.getLong( rs, "ID_SITE_PORTLET_FAQ" ) ) );
            }

            log.debug( "#10.01.05 " );
        }
        catch( Exception e ) {
            log.error( "Error get faq block ", e );
            throw new PortletException( e.toString() );
        }
        return this;
    }

    public byte[] getPlainHTML() {
        String s = "";
        for (FaqGroup faqGroup : v) {
            s += faqGroup.faqGroupName + "<br>";

            s += "<table border=\"0\">";

            for (FaqItem faqItem : faqGroup.v) {
                s += "<tr><td>" + faqItem.question + "</td><td>" + faqItem.answer + "</td></tr>";
            }
            s += "</table>";
        }
        return s.getBytes();
    }

    public byte[] getXml( final String rootElement ) throws Exception {
        FaqBlockType faqBlock = new FaqBlockType();

        for (FaqGroup faqGroup : v) {
            FaqGroupType group_ = new FaqGroupType();
            group_.setFaqGroupName(faqGroup.faqGroupName);

            for (FaqItem fi : faqGroup.v) {
                FaqItemType item_ = new FaqItemType();
                item_.setFaqItemAnswer(fi.answer);
                item_.setFaqItemQuestion(fi.question);
                item_.setFaqItemDate(DateTools.getStringDate(fi.datePost, "dd.MMM.yyyy", renderRequest.getLocale()));
                item_.setFaqItemTime(DateTools.getStringDate(fi.datePost, "HH:mm", renderRequest.getLocale()));

                group_.getFaqItem().add(item_);
            }
            faqBlock.getFaqGroup().add(group_);
        }

        return XmlTools.getXml( faqBlock, rootElement );
    }

    public byte[] getXml() throws Exception {
        return getXml( "FaqBlock" );
    }

    public List<ClassQueryItem> getList( final Long idSiteCtxLangCatalog, final Long idContext ) {
        return null;
    }
    
    private PortalSpiProvider provider;
    public void setPortalDaoProvider(PortalSpiProvider provider) {
        this.provider=provider;
    }
}