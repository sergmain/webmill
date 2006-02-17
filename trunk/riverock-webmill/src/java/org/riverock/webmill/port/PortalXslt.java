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

import java.io.FileOutputStream;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public class PortalXslt implements XsltTransformer{
    private static Logger log = Logger.getLogger(PortalXslt.class);

    private static CacheFactory cache = new CacheFactory(PortalXslt.class.getName());

    private String xslt = null;
    private Transformer transformer = null;
    private Object transformerSync = new Object();

    public void terminate(Long id_) {
        cache.reinit();
    }

    public void reinit() {
        cache.reinit();
    }

    protected void finalize() throws Throwable {
        xslt = null;
        transformer = null;

        super.finalize();
    }

    public PortalXslt() {
    }

    public static PortalXslt getInstance(Long id__)
        throws Exception {
        return (PortalXslt) cache.getInstanceNew(id__);
    }

    public PortalXslt(Long id) {
        this( InternalDaoFactory.getInternalDao().getXslt( id ).toString() );
    }

    public PortalXslt(String xslt) {
        this.xslt = xslt;
        if (!StringTools.isEmpty(xslt)) {
            try {
                createTransformer();
            }
            catch (Exception e) {
                String es = "Error create transformer";
                log.error(es, e);
                throw new IllegalStateException(es,e );
            }
        }
    }

    private static Object syncObj = new Object();

    private void createTransformer() throws Exception {
        Source xslSource = new StreamSource(new StringReader(xslt));

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Templates translet = null;
        try {
            translet = tFactory.newTemplates(xslSource);
        }
        catch (TransformerConfigurationException e) {
            synchronized (syncObj) {
                FileOutputStream out = new FileOutputStream(WebmillConfig.getWebmillTempDir() + "\\xslt-with-error.xsl");
                out.write(xslt.getBytes(WebmillConfig.getHtmlCharset()));
                out.flush();
                out.close();
                out = null;
            }
            try {
                log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
            }
            catch (Throwable e1) {
                log.error("Error get version of xalan", e1);
            }
            try {
                log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion());
            }
            catch (Exception e2) {
                log.error("Error get version of xerces", e2);
            }
            log.error("Error create TransformerFactory of XSLT", e);
            translet = null;
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("XsltList. translet - " + translet);
            log.debug("Start create Transformer");
        }

        synchronized (transformerSync) {
            try {
                transformer = translet.newTransformer();
            }
            catch (javax.xml.transform.TransformerConfigurationException e) {
                try {
                    log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
                }
                catch (Throwable e1) {
                    log.error("Error get version of xalan", e1);
                }
                try {
                    log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion());
                }
                catch (Exception e2) {
                    log.error("Error get version of xerces", e2);
                }
                log.error("Error create transformer", e);
                transformer = null;
                throw e;
            }
        }
    }

    public Transformer getTransformer() {
        synchronized (transformerSync) {
            return transformer;
        }
    }
}