/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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

package org.riverock.webmill.container.deployer;

import org.w3c.dom.Document;

/**
 * Utilities for manipulating the context.xml deployment descriptor
 *
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class WebmillContextRewriter {
    private Document document;
    private String portletApplication;
    private boolean changed = false;

    public WebmillContextRewriter(Document doc, String portletApplication) {
        this.document = doc;
        this.portletApplication = portletApplication;
    }

    public void processContextXML() throws Exception {
        if (document != null) {
/*
            try {
                // get root Context
                Element root = null;
                if (!document.hasRootElement()) {
                    root = new Element("Context");
                    document.setRootElement(root);
                }
                else {
                    root = document.getRootElement();
                }   
                
                // set Context path
                String pathAttribute = root.getAttributeValue("path");
                if ((pathAttribute == null) || !pathAttribute.equals("/" + portletApplication)) {
                    root.setAttribute("path", "/" + portletApplication);
                    changed = true;
                }
                
                // set Context docBase
                String docBaseAttribute = root.getAttributeValue("docBase");
                if ((docBaseAttribute == null) || !docBaseAttribute.equals(portletApplication)) {
                    root.setAttribute("docBase", portletApplication);
                    changed = true;
                }
            }
            catch (Exception e) {
                throw new Exception("Unable to process context.xml for infusion " + e.toString(), e);
            }
*/
        }
    }
}
