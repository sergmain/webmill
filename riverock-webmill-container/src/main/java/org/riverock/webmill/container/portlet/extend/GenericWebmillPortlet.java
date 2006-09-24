/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet.extend;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 15:54:17
 * $Id$
 */
public abstract class GenericWebmillPortlet implements Portlet {

    public GenericWebmillPortlet(){}

    protected PortletConfig portletConfig = null;
    public void init(PortletConfig portletConfig) throws PortletException {
        if (portletConfig==null) {
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
        String s = portletConfig.getInitParameter( ContainerConstants.is_xml );
        if (s==null)
            return false;
        return new Boolean(s);
    }

    public void doRender(RenderRequest renderRequest, RenderResponse renderResponse, PortletResultObject beanObject) throws PortletException, IOException
    {
        OutputStream out = null;
        try
        {
            out = renderResponse.getPortletOutputStream();

            String code = (String)renderRequest.getAttribute(
                ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE );

            String xmlRoot = (String)renderRequest.getAttribute(
                ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE );

            beanObject.setParameters( renderRequest, renderResponse, portletConfig );
            PortletResultContent result = null;
            if ( code==null || code.length()==0 ){
                String portletId = portletConfig.getInitParameter( ContainerConstants.name_portlet_id );

                Long id = PortletService.getLong( renderRequest, portletId );
                result = beanObject.getInstance( id );
            }
            else {
                result = beanObject.getInstanceByCode( code );
            }

            if (result!=null) {
                result.setParameters( renderRequest, renderResponse, portletConfig );
            } else {
                out.write( ("Error create portlet "+portletConfig.getPortletName()).getBytes() );
                return;
            }

            byte[] bytes = null;
            if ( isXml() ) {
                if (xmlRoot==null) {
                    bytes = result.getXml();
                }
                else {
                    bytes = result.getXml( xmlRoot );
                }
            }
            else {
                bytes = result.getPlainHTML();
            }
            if (bytes!=null)
                out.write( bytes );
        }
        catch (Exception e){
            final String es;
            if (portletConfig!=null)
                es = "Error get " + portletConfig.getPortletName();
            else
                es = "Error with null";
            throw new PortletException(es, e);
        }
        finally{
            out.flush();
            out.close();
            out = null;
        }
    }
}
