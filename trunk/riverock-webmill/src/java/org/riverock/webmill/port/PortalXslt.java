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

package org.riverock.webmill.port;

import java.io.StringReader;
import java.io.FileOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.main.CacheFactory;
import org.riverock.webmill.core.GetWmPortalXsltDataWithIdSiteXsltList;
import org.riverock.webmill.schema.core.WmPortalXsltDataItemType;
import org.riverock.webmill.schema.core.WmPortalXsltDataListType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.tools.StringTools;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public class PortalXslt
{
    private static Logger log = Logger.getLogger( PortalXslt.class );

    private static CacheFactory cache = new CacheFactory( PortalXslt.class.getName() );

    public String xsltLang = null;
    private String xslt = null;
    private Transformer transformer = null;
    private Object transformerSync = new Object();

    public void reinit()
    {
        cache.reinit();
    }

    protected void finalize() throws Throwable
    {
        xsltLang = null;
        xslt = null;
        transformer = null;

        super.finalize();
    }

    public PortalXslt(){}

    public static PortalXslt getInstance(DatabaseAdapter db__, long id__)
        throws Exception
    {
        return getInstance(db__, new Long(id__) );
    }

    public static PortalXslt getInstance(DatabaseAdapter db__, Long id__)
        throws Exception
    {
        return (PortalXslt) cache.getInstanceNew(db__, id__);
    }

    static
    {
        try
        {
            org.riverock.sql.cache.SqlStatement.registerRelateClass( PortalXslt.class, GetWmPortalXsltDataWithIdSiteXsltList.class );
        }
        catch (Exception exception)
        {
            log.error("Exception in ", exception);
        }
    }

    public PortalXslt(DatabaseAdapter db_, Long id)
        throws Exception
    {
        try
        {
            xslt = "";
            WmPortalXsltDataListType xsltList = GetWmPortalXsltDataWithIdSiteXsltList.getInstance(db_, id).item;
            for (int i=0; i<xsltList.getWmPortalXsltDataCount(); i++)
            {
                WmPortalXsltDataItemType item = xsltList.getWmPortalXsltData(i);
                xslt += item.getXslt();
            }
            if (!StringTools.isEmpty(xslt)) {
                createTransformer();
            }
        }
        catch(Exception e)
        {
            log.error("Xslt object id "+id);
            log.error("Exception create new PortalXslt()", e);
            throw e;
        }
    }

    private static Object syncObj = new Object();
    private void createTransformer() throws Exception
    {
        Source xslSource = new StreamSource(
            new StringReader(xslt)
        );

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Templates translet = null;
        try
        {
            translet = tFactory.newTemplates(xslSource);
        }
        catch (javax.xml.transform.TransformerConfigurationException e)
        {
            synchronized(syncObj)
            {
                FileOutputStream out = new FileOutputStream(WebmillConfig.getWebmillTempDir()+"\\xslt-with-error.xsl");
                out.write(xslt.getBytes( WebmillConfig.getHtmlCharset()));
                out.flush();
                out.close();
                out = null;
            }
            try
            {
                log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
            }
            catch(Throwable e1)
            {
                log.error("Error get version of xalan", e1);
            }
            try
            {
                log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
            }
            catch(Exception e2)
            {
                log.error("Error get version of xerces", e2);
            }
            log.error("Error create TransformerFactory of XSLT", e);
            translet = null;
            throw e;
        }

        if (log.isDebugEnabled())
        {
            log.debug("XsltList. translet - " + translet );
            log.debug("Start create Transformer");
        }

        synchronized(transformerSync)
        {
            try
            {
                transformer = translet.newTransformer();
            }
            catch(javax.xml.transform.TransformerConfigurationException e)
            {
                try
                {
                    log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
                }
                catch(Throwable e1)
                {
                    log.error("Error get version of xalan", e1);
                }
                try
                {
                    log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
                }
                catch(Exception e2)
                {
                    log.error("Error get version of xerces", e2);
                }
                log.error("Error create transformer", e);
                transformer =  null;
                throw e;
            }
        }
    }

    public void reinitTransformer()
        throws Exception
    {
        synchronized(transformerSync)
        {
            transformer = null;
            createTransformer();
        }
    }

    public Transformer getTransformer()
    {
        synchronized(transformerSync)
        {
            return transformer;
        }
    }
}