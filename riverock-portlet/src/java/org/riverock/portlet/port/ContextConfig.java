/*
 * org.riverock.portlet -- Portlet Library
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

package org.riverock.portlet.port;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;

/**
 * $Id$
 */

public class ContextConfig
{


    private static Logger cat = Logger.getLogger( ContextConfig.class );

    public Long id = null;
    public String type = "";
    public boolean isXml = false;
    public boolean isXsltData = false;

    public String xsltData = "";
    public String xsltComment = "";
    public Long xsltID = null;

    public Templates translet = null;

    protected void finalize() throws Throwable
    {
        type = null;
        xsltData = null;
        xsltComment = null;
        translet = null;

        super.finalize();
    }


    public ContextConfig(DatabaseAdapter db_, ResultSet rs)
            throws Exception
    {
        type = RsetTools.getString(rs, "TYPE");
        isXml = ( new Integer(1).equals(RsetTools.getInt(rs, "IS_XML"))?true:false);
        id = RsetTools.getLong(rs, "ID_SITE_CTX_CFG");

        cat.debug("#15.05.01 " + type + " id " + id);

        if (id == null)
        {
            isXml = false;
            isXsltData = false;
            return;
        }

        final String sql_ =
                "select a.TEXT_COMMENT, b.XSLT, a.ID_SITE_CTX_XSLT " +
                "from SITE_CTX_XSLT a, SITE_CTX_XSLT_DATA b " +
                "where a.is_current=1 and " +
                "a.ID_SITE_CTX_CFG=? and " +
                "a.ID_SITE_CTX_XSLT=b.ID_SITE_CTX_XSLT " +
                "order by ID_SITE_CTX_XSLT_DATA asc";


        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        try
        {
            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id);
            rset = ps.executeQuery();
            while (rset.next())
            {
                if (isFirstRecord)
                {
                    xsltComment = RsetTools.getString(rset, "TEXT_COMMENT");
                    xsltID = RsetTools.getLong(rset, "ID_SITE_CTX_XSLT");

                    isFirstRecord = false;
                }

                xsltData += RsetTools.getString(rset, "XSLT");
            }
        }
        catch (Exception e)
        {
            cat.error("Error create ContextConfig ", e);
            throw new Exception(e.toString());
        }
        finally
        {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }

        if (xsltData == null || xsltData.length() == 0)
            isXsltData = false;
        else
        {
            isXsltData = true;

            Source xslSource = new StreamSource(
                    new StringReader(xsltData)
            );
            TransformerFactory tFactory = TransformerFactory.newInstance();
            try
            {
                translet = tFactory.newTemplates(xslSource);
            }
            catch(javax.xml.transform.TransformerConfigurationException e)
            {
                cat.error("Error create template of XSLT. Context type  "+type, e);
                translet = null;
                isXsltData = false;
                xsltData = null;
            }
        }
    }


/*
    public void reinit()
    {
	lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
	lastReadData = 0;
    }
*/


}