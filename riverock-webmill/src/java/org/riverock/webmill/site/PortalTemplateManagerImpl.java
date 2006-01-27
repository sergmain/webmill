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
package org.riverock.webmill.site;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.exception.GenericException;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.container.schema.site.SiteTemplateDescListType;
import org.riverock.webmill.container.schema.site.SiteTemplateDescriptionType;
import org.riverock.webmill.container.portal.bean.PortalTemplateImpl;
import org.riverock.webmill.container.portal.bean.PortalTemplateItemImpl;
import org.riverock.webmill.container.portal.bean.PortalTemplateParameterImpl;
import org.riverock.webmill.exception.PortalException;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.template.PortalTemplateManager;

/**
 * $Id$
 */
public final class PortalTemplateManagerImpl implements PortalTemplateManager {
    private final static Logger log = Logger.getLogger( PortalTemplateManagerImpl.class );
    private final static CacheFactory cache = new CacheFactory( PortalTemplateManagerImpl.class.getName() );

    public Map<String, PortalTemplate> hash = new HashMap<String, PortalTemplate>(10);
    public Map<Long, PortalTemplate> hashId = new HashMap<Long, PortalTemplate>(10);

    public SiteTemplateDescListType templateList = new SiteTemplateDescListType();

    public void reinit(){
        cache.reinit();
    }

    public PortalTemplateManagerImpl(){}

    public static PortalTemplateManagerImpl getInstance(DatabaseAdapter db__, long id__) throws PortalException{
        return getInstance(db__, id__ );
    }

    public static PortalTemplateManagerImpl getInstance(DatabaseAdapter db__, Long id__) throws PortalException{
        try {
            return (PortalTemplateManagerImpl) cache.getInstanceNew(db__, id__);
        } catch (GenericException e) {
            String es = "Error in getInstance(DatabaseAdapter db__, Long id__)";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }

    protected void finalize() throws Throwable {
        if (hash != null) {
            hash.clear();
            hash = null;
        }
        if (hashId != null) {
            hashId.clear();
            hashId = null;
        }
        super.finalize();
    }

    public PortalTemplate getTemplate( final long id ) {
        if (log.isDebugEnabled()) log.debug("search  template for id - "+ id);

        return hashId.get( id );
    }

    public PortalTemplate getTemplate( final String nameTemplate, final String lang ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "search template for name: '" + nameTemplate + "', lang: '" + lang + "'" );
        }

        if ( nameTemplate == null || lang == null )
            return null;

        return hash.get( nameTemplate + '_' + lang );
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +
            "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
            "where  b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";

        try {
            SqlStatement.registerSql( sql_, PortalTemplateManagerImpl.class );
        }
        catch( Throwable exception ) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error( es, exception );
            throw new SqlStatementRegisterException( es, exception );
        }
    }

    public PortalTemplateManagerImpl(DatabaseAdapter db_, Long idSite) throws PortalException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);
            ps.setLong(1, idSite);

            rs = ps.executeQuery();

            while (rs.next())
            {

                String templateData = null;
                try {
                    templateData = RsetTools.getString(rs, "TEMPLATE_DATA");
                    PortalTemplate st = null;
                    st = digestSiteTemplate(templateData, RsetTools.getString(rs, "NAME_SITE_TEMPLATE") );
                    Long idSiteTemplate = RsetTools.getLong(rs, "ID_SITE_TEMPLATE");
                    String lang = StringTools.getLocale(
                        RsetTools.getString(rs, "CUSTOM_LANGUAGE")
                    ).toString();
                    hash.put( st.getTemplateName()+'_'+lang, st);
                    hashId.put( idSiteTemplate, st );

                    SiteTemplateDescriptionType desc = new SiteTemplateDescriptionType();
                    desc.setIdTemplate( idSiteTemplate );
                    desc.setIdTemplateLanguage( RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE"));
                    desc.setNameLanguage( lang );
                    desc.setTemplate( st );
                    templateList.addTemplateDescription( desc );
                }
                catch(Exception e)
                {
                    // Todo. Add to template text data. If we get error, then create template with error message
                    log.error("Error unmarshaling site template "+templateData, e);
                }
            }
        }
        catch(Throwable e) {
            String es = "Exception in SiteTemplateList()";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public static PortalTemplate digestSiteTemplate(String templateData, String templateName) throws IOException, SAXException {
        if (StringTools.isEmpty(templateData) ) {
            final PortalTemplateImpl portalTemplate = new PortalTemplateImpl();
            portalTemplate.setTemplateName( templateName );
            return portalTemplate;
        }
        if (log.isDebugEnabled()) {
            log.debug("Digest template:\n" + templateData);
        }
        PortalTemplateImpl st = null;

        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("SiteTemplate", PortalTemplateImpl.class);
        digester.addSetProperties("SiteTemplate", "role", "role");

        digester.addObjectCreate("SiteTemplate/SiteTemplateItem", PortalTemplateItemImpl.class);
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "type", "type");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "value", "value");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "code", "code");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "xmlRoot", "xmlRoot");
        digester.addSetNext("SiteTemplate/SiteTemplateItem", "addSiteTemplateItem" );
/*
        digester.addCallMethod(
            "SiteTemplate/SiteTemplateItem",
            "addSiteTemplateItem",
            1,
            new Class[] { PortalTemplateItem.class }
        );
*/

        digester.addObjectCreate("SiteTemplate/SiteTemplateItem/Parameter", PortalTemplateParameterImpl.class);
        digester.addSetProperties("SiteTemplate/SiteTemplateItem/Parameter", "name", "name");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem/Parameter", "value", "value");
        digester.addSetNext("SiteTemplate/SiteTemplateItem/Parameter", "addParameter");

        st = (PortalTemplateImpl) digester.parse( new ByteArrayInputStream( templateData.getBytes() ));
        st.setTemplateName( templateName );

        return st;
    }
}