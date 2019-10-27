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
package org.riverock.common.portlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 15:54:17
 * $Id: GenericWebmillPortlet.java 1230 2007-07-02 15:27:45Z serg_main $
 */
public abstract class GenericWebmillPortlet implements Portlet {

    public GenericWebmillPortlet() {
    }

    protected PortletConfig portletConfig = null;

    public void init(PortletConfig portletConfig) throws PortletException {
        if (portletConfig == null) {
            throw new NullPointerException();
        }
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public final void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {
    }

    public boolean isXml() {
        String s = portletConfig.getInitParameter(ContainerConstants.is_xml);
        return s != null && Boolean.parseBoolean(s);
    }

    public void doRender(RenderRequest renderRequest, RenderResponse renderResponse, PortletResultObject beanObject) throws PortletException, IOException {
        OutputStream out = null;
        try {
            out = renderResponse.getPortletOutputStream();

            String code = (String) renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE);
            String xmlRoot = (String) renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE);
            beanObject.setParameters(renderRequest, renderResponse, portletConfig);
            PortletResultContent result;
            if (code == null || code.length() == 0) {
                String portletId = portletConfig.getInitParameter(ContainerConstants.name_portlet_id);

                Long id = PortletUtils.getLong(renderRequest, portletId);
                result = beanObject.getInstance(id);
            }
            else {
                result = beanObject.getInstanceByCode(code);
            }

            if (result != null) {
                result.setParameters(renderRequest, renderResponse, portletConfig);
            }
            else {
                out.write(("Error create portlet " + portletConfig.getPortletName()).getBytes());
                return;
            }

            byte[] bytes;
            if (isXml()) {
                if (xmlRoot == null) {
                    bytes = result.getXml();
                }
                else {
                    bytes = result.getXml(xmlRoot);
                }
            }
            else {
                bytes = result.getPlainHTML();
            }
            if (bytes != null) {
                out.write(bytes);
            }
        }
        catch (Exception e) {
            final String es;
            if (portletConfig != null) {
                es = "Error get " + portletConfig.getPortletName();
            }
            else {
                es = "Error with null";
            }
            throw new PortletException(es, e);
        }
        finally {
            out.flush();
            out.close();
            out = null;
        }
    }
}
