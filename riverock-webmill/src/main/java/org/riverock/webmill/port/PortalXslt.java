/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.port;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.generic.main.CacheFactory;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public class PortalXslt implements XsltTransformer {
    private static Logger log = Logger.getLogger(PortalXslt.class);

    private static CacheFactory cache = new CacheFactory(PortalXslt.class);

    private Xslt xslt = null;
    private Transformer transformer = null;
    private final Object transformerSync = new Object();

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

    public PortalXslt(Long xsltId) {
        this( InternalDaoFactory.getInternalXsltDao().getXslt(xsltId) );
    }

    public PortalXslt(Xslt xslt) {
        if (StringUtils.isBlank(xslt.getXsltData())) {
            return;
        }

        this.xslt = xslt;
        try {
            createTransformer();
        }
        catch (Exception e) {
            String es = "Error create transformer";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    private void createTransformer() throws Exception {
        Source xslSource = new StreamSource(new StringReader(xslt.getXsltData()));

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Templates translet;
        try {
            translet = tFactory.newTemplates(xslSource);
        }
        catch (TransformerConfigurationException e) {
            log.error("xslt with error\n"+xslt.getXsltData());
            try {
                log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
            }
            catch (Throwable e1) {
                log.error("Error get version of xalan", e1);
            }
            try {
                log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion());
            }
            catch (Throwable e2) {
                log.error("Error get version of xerces", e2);
            }
            log.error("Error create TransformerFactory of XSLT", e);
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("XsltList. translet - " + translet);
            log.debug("Start create Transformer");
        }

        synchronized (transformerSync) {
            transformer=null;
            try {
                Transformer transformerTemp = translet.newTransformer();
                transformer = transformerTemp;
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
                catch (Throwable e2) {
                    log.error("Error get version of xerces", e2);
                }
                log.error("Error create transformer", e);
                throw e;
            }
        }
    }

    public Transformer getTransformer() {
        if (transformer!=null) {
            return transformer;
        }
        synchronized (transformerSync) {
            return transformer;
        }
    }
}