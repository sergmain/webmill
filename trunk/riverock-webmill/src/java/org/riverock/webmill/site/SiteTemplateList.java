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



/**

 * $Id$

 */

package org.riverock.webmill.site;



import java.io.StringReader;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Hashtable;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.main.CacheFactory;

import org.riverock.webmill.schema.site.SiteTemplate;

import org.riverock.webmill.schema.site.SiteTemplateDescListType;

import org.riverock.webmill.schema.site.SiteTemplateDescriptionType;

import org.riverock.common.tools.RsetTools;



public class SiteTemplateList

{

    private static Logger log = Logger.getLogger( SiteTemplateList.class );



    private static CacheFactory cache = new CacheFactory( SiteTemplateList.class.getName() );



    public Hashtable hash = new Hashtable(4);

    public Hashtable hashId = new Hashtable(4);



    public SiteTemplateDescListType templateList = new SiteTemplateDescListType();



    public void reinit()

    {

        cache.reinit();

    }



    public SiteTemplateList()

    {

    }



    public static SiteTemplateList getInstance(DatabaseAdapter db__, long id__)

            throws Exception

    {

        return getInstance(db__, new Long(id__) );

    }



    public static SiteTemplateList getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        return (SiteTemplateList) cache.getInstanceNew(db__, id__);

    }



    protected void finalize() throws Throwable

    {

        if (hash != null)

        {

            hash.clear();

            hash = null;

        }

        if (hashId != null)

        {

            hashId.clear();

            hashId = null;

        }



        super.finalize();

    }



    public SiteTemplate getTemplate(long id)

    {

        if (log.isDebugEnabled())

            log.debug("search  template for id - "+ id);



        return ( SiteTemplate ) hashId.get( new Long(id) );

    }



    public SiteTemplate getTemplate(String nameTemplate , String lang)

    {

        if (log.isDebugEnabled())

            log.debug("search  template for name - "+ nameTemplate+" lang "+lang);



        if (nameTemplate == null)

            return null;



        return ( SiteTemplate) hash.get( nameTemplate+'_'+lang );

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +

            "a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +

            "from SITE_TEMPLATE a, SITE_SUPPORT_LANGUAGE b " +

            "where b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";



        try

        {

            SiteTemplateList tempObj = new SiteTemplateList();

            Class dependClass = tempObj.getClass();

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, dependClass );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    public SiteTemplateList(DatabaseAdapter db_, Long idSite)

            throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql_);

            ps.setLong(1, idSite.longValue());



            rs = ps.executeQuery();



            while (rs.next())

            {

                String templateData = null;

                try

                {

                    templateData = RsetTools.getString(rs, "TEMPLATE_DATA");

                    SiteTemplate st = (SiteTemplate)Unmarshaller.unmarshal(SiteTemplate.class,

                            new InputSource(new StringReader( templateData ) )

                    );

                    st.setNameTemplate( RsetTools.getString(rs, "NAME_SITE_TEMPLATE") );

                    Long idSiteTemplate = RsetTools.getLong(rs, "ID_SITE_TEMPLATE");

                    String lang = RsetTools.getString(rs, "CUSTOM_LANGUAGE");

                    hash.put(

                            st.getNameTemplate()+'_'+lang

                            ,st

                    );

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

                    log.error("Error unmarshaling site template "+templateData, e);

                }

            }

        }

        catch(Exception e)

        {

            log.error("Exception in SiteTemplateList(...", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in SiteTemplateList(...", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }

}