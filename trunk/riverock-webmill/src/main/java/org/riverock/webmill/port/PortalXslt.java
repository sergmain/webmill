/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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

import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public class PortalXslt implements XsltTransformer {
    private static Logger log = Logger.getLogger(PortalXslt.class);

    private Xslt xslt = null;
//    private Transformer transformer = null;
    private Templates translet = null;
    private final Object transformerSync = new Object();

    protected void finalize() throws Throwable {
        xslt = null;
//        transformer = null;
        translet = null;

        super.finalize();
    }

    public PortalXslt() {
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
            Source xslSource = new StreamSource(new StringReader(this.xslt.getXsltData()));

            TransformerFactory tFactory = TransformerFactory.newInstance();
            try {
                translet = tFactory.newTemplates(xslSource);
            }
            catch (TransformerConfigurationException e) {
                log.error("xslt with error\n"+ this.xslt.getXsltData());
                log.error("Error create TransformerFactory of XSLT", e);
                throw e;
            }

/*
            if (log.isDebugEnabled()) {
                log.debug("XsltList. translet - " + translet);
                log.debug("Start create Transformer");
            }

            synchronized (transformerSync) {
                transformer=null;
                try {
                    transformer = translet.newTransformer();
                }
                catch (TransformerConfigurationException e) {
                    log.error("Error create transformer", e);
                    throw e;
                }
            }
*/
        }
        catch (Exception e) {
            String es = "Error create transformer";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    public Transformer getTransformer() {
        try {
            return translet.newTransformer();
        }
        catch (Exception e) {
            String es = "Error create transformer";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
/*
        if (transformer!=null) {
            return transformer;
        }
        synchronized (transformerSync) {
            return transformer;
        }
*/
    }
}