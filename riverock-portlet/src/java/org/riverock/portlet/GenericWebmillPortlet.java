package org.riverock.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.portlet.tools.DebugUtils;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Sergei Maslyukov
 *         Date: 15.05.2006
 *         Time: 13:08:48
 */
public abstract class GenericWebmillPortlet implements Portlet {
    private final static Logger log = Logger.getLogger( GenericWebmillPortlet.class );

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
        return Boolean.parseBoolean(s);
    }

    public void doRender(RenderRequest renderRequest, RenderResponse renderResponse, PortletResultObject beanObject) throws PortletException, IOException
    {
        OutputStream out = null;
        try
        {
            out = renderResponse.getPortletOutputStream();
            if (log.isDebugEnabled()) {
                log.debug("Render request: "+ renderRequest);
                DebugUtils.dumpAnyMap(renderRequest.getParameterMap(), "GenericWebmillPortlet. Dump render parameters.");
                boolean isFound = false;
                log.debug("Render request attributes: ");
                for (Enumeration e = renderRequest.getAttributeNames(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();
                    log.debug("    key: " + key + ", value: " + renderRequest.getAttribute(key));
                    isFound = true;
                }
                if (!isFound) {
                    log.debug("Attribute in render request is present: " + isFound);
                }
            }

            String code = (String)renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE);
            String xmlRoot = (String)renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE );
            beanObject.setParameters( renderRequest, renderResponse, portletConfig );
            PortletResultContent result;
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

            byte[] bytes;
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
        }
    }
}
