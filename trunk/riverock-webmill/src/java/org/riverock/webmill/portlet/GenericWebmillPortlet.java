package org.riverock.webmill.portlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.tools.MainTools;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 15:54:17
 * $Id$
 */
public abstract class GenericWebmillPortlet implements Portlet {
    private final static Logger log = Logger.getLogger( GenericWebmillPortlet.class );

    public GenericWebmillPortlet(){}

    protected PortletConfig portletConfig = null;
    public void init(PortletConfig portletConfig) throws PortletException {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public final void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public boolean isXml() {
        String s = portletConfig.getInitParameter( PortletTools.is_xml );
        if (s==null)
            return false;
        return new Boolean(s).booleanValue();
    }

    public void doRender(RenderRequest renderRequest, RenderResponse renderResponse, PortletResultObject beanObject) throws PortletException, IOException
    {
//        Writer out = null;
        OutputStream out = null;
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance( false );
//            out = renderResponse.getWriter();
            out = renderResponse.getPortletOutputStream();

            String code = (String)renderRequest.getAttribute(
                PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE );

            String xmlRoot = (String)renderRequest.getAttribute(
                PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE );

            if (log.isDebugEnabled()) {
                log.debug( "Portlet code: "+code+", xmlRoot: "+xmlRoot );
                log.debug( "renderRequest: "+renderRequest );
            }

            // portletConfig.getResourceBundle( renderRequest.getLocale() )
            beanObject.setParameters( renderRequest, renderResponse, portletConfig );
            PortletResultContent result = null;
            if ( code==null || code.length()==0 ){
                String portletId = portletConfig.getInitParameter( PortletTools.name_portlet_id );
                Long id = PortletTools.getLong( renderRequest, portletId );
                if ( log.isDebugEnabled() ) {
                    log.debug( "nameId: " + portletId );
                    log.debug( "Id: " + id );
                }
                result = beanObject.getInstance( db_, id );
            }
            else {
                result = beanObject.getInstanceByCode( db_, code );
            }

            result.setParameters( renderRequest, renderResponse, portletConfig );

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

            if (log.isDebugEnabled()) {
                String fileName =
                    WebmillConfig.getWebmillTempDir() +
                    renderResponse.getNamespace() +
                    "-portlet-data."+(isXml()?"xml":"bin");
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, bytes );
                fileName =
                    WebmillConfig.getWebmillTempDir() +
                    renderResponse.getNamespace() +
                    "-0-portlet-data."+(isXml()?"xml":"bin");
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, new String(bytes, "utf8").getBytes("utf8") );
            }
            out.write( bytes );
//            out.write( new String(bytes, "utf8") );
        }
        catch (Exception e){
            final String es = "Error get "+portletConfig.getPortletName();
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally{
            DatabaseManager.close( db_ );
            db_ = null;
            out.flush();
            out.close();
            out = null;
        }
    }
}
