package org.riverock.webmill.portlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.schema.site.SitePortletDataListType;
import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.webmill.schema.site.TemplateItemType;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;

/**
 * User: SergeMaslyukov
 * Date: 24.11.2004
 * Time: 23:53:05
 */
public final class PortalRequestProcessor {

    private final static Logger log = Logger.getLogger(PortalRequestProcessor.class);

    private static final int NUM_LINES = 100;

    private static Object syncSitePortletDataList = new Object();
    /**
     *  Main method for processing pages with portlets
     */
    static void processRequest( PortalRequestInstance portalRequestInstance ) {

        if ( log.isDebugEnabled() ) {
            log.debug( "Dynamic content type: "+portalRequestInstance.getDefaultPortletType() );
            log.debug( "Template name: "+portalRequestInstance.getNameTemplate() );
            log.debug( "Locale request: "+portalRequestInstance.getLocale().toString() );
        }

        try {

            // output copyright
            portalRequestInstance.byteArrayOutputStream.write( ContextNavigator.copyright.getBytes() );

            processSiteTemplateItems( portalRequestInstance );

            if ( log.isDebugEnabled() ) {
                synchronized(syncSitePortletDataList) {
                    if (portalRequestInstance.portletResultOutput!=null) {
                        SitePortletDataListType tmp = new SitePortletDataListType();

                        tmp.setPortlet( (ArrayList)portalRequestInstance.portletResultOutput );

                        log.debug( "Unmarshal SitePortletDataListType object" );
                        FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"site-portlets-data.xml" );
                        Marshaller marsh = new Marshaller( w );
                        marsh.setMarshalAsDocument( true );
                        marsh.setEncoding( "windows-1251" );
                        marsh.marshal( tmp );
                        marsh = null;
                        w.flush();
                        w.close();
                        w = null;
                    }
                    else
                        log.warn("List portletResultOutput is null. Unknown situation.");
                }
            }

            // we are collect output and now build result page 
            processPortletData( portalRequestInstance );
        }
        catch (Exception e) {
            final String es = "Error build page.<br>Name template - "+portalRequestInstance.getNameTemplate()+
                            "<br>"+"Dynamic type - "+portalRequestInstance.getDefaultPortletType()+"<br>";
            log.error( es, e );
            try {
                portalRequestInstance.byteArrayOutputStream.write(
                    (es + ExceptionTools.getStackTrace( e, NUM_LINES, "<BR>" )).getBytes()
                );
            }
            catch (Exception e01){
            }
        }
    }

/*
    private static void runModelPortlets(PortalRequestInstance portalRequestInstance, CtxInstance ctxInstance)
        throws FileManagerException, IOException, PortalException, MultipartRequestException, ServletException {

        if ( log.isDebugEnabled() ) log.debug( " start process 'model' portlet" );

        // run all 'models' portlet
        for ( int i = 0; i<portalRequestInstance.template.getSiteTemplateItemCount(); i++ ) {
            TemplateItemType item = portalRequestInstance.template.getSiteTemplateItem( i );

            if ( log.isDebugEnabled() ) {
                log.debug( "item - "+item );
                if ( item!=null )
                    log.debug( "item.getDefaultPortletType() - "+item.getType() );
            }

            if ( item.getType().getType()==TemplateItemTypeTypeType.PORTLET_TYPE ){
                PortletType d = PortletManager.getPortletDescription( item.getValue() );

                if ( log.isDebugEnabled() ) {
                    log.debug( " portlet - "+item.getValue() );
                    log.debug( " portlet  desc - "+d );
                    if ( d!=null ) {
                        String typePortletTemp =
                            PortletTools.getStringParam(d, PortletTools.type_portlet);

                        log.debug( " portlet  desc.type - "+typePortletTemp );
                    }
                }

                if ( d!=null){
                    String typePortletTemp =
                        PortletTools.getStringParam(d, PortletTools.type_portlet);

                    if ( typePortletTemp!=null &&
                        typePortletTemp.equals(
                            PortletDescriptionTypeTypePortletType.MODEL.toString()
                        )
                    ) {
                        Map map = PortletTools.getParameters(ctxInstance.getHttpRequest());
                        Map tempMap = null;
                        if (ctxInstance.getDefaultPortletDescription()!=null) {
                            tempMap = CtxInstance.getGlobalParameter( ctxInstance.getDefaultPortletDescription().getPortletConfig(), ctxInstance.getPortletId());
                            map.putAll( tempMap );

                            if (log.isDebugEnabled()) {
                                log.debug( "Print param from map" );
                                for ( Iterator it = tempMap.keySet().iterator(); it.hasNext(); ) {
                                    Object s = it.next();
                                    log.debug( "Param in map - "+s.toString()+", value - "+ tempMap.get(s) );
                                }
                            }
                        }

                        ctxInstance.setParameters(
                            map,
                            PortletTools.getStringParam(d, PortletTools.locale_name_package)
                        );
                        ctxInstance.getPortletRequest().getPortletSession().setAttribute( Constants.PORTLET_REQUEST_SESSION, ctxInstance );

                        Boolean isUrlTemp =
                            PortletTools.getBooleanParam(
                                d, PortletTools.is_url
                            );

                        if ( log.isDebugEnabled() )
                            log.debug( "process 'model' portlet - "+item.getValue()+" file "+d.getPortletClass()+", isUrl - "+isUrlTemp );

                        boolean flag = true;
                        if ( Boolean.FALSE.equals(isUrlTemp) ) {
                            String nameFile = PropertiesProvider.getApplicationPath()+d.getPortletClass();
                            if ( nameFile.indexOf( '?' )!=-1 )
                                nameFile = nameFile.substring( 0, nameFile.indexOf( '?' ) );

                            File testFile = new File( nameFile );
                            if ( !testFile.exists() ) {
                                String errorString = "Portlet "+ctxInstance.getDefaultPortletType()+" refered to file "+testFile+" is broken";
                                portalRequestInstance.byteArrayOutputStream.write( errorString.getBytes() );
                                log.error( errorString );
                                flag = false;
                            }
                        }


                        if ( flag )
                            ServletUtils.include(
                                ctxInstance.getHttpRequest(), ctxInstance.getHttpResponse(),
                                tempMap,
                                d.getPortletClass(), new StringWriter() );

                        ctxInstance.getPortletRequest().getPortletSession().removeAttribute( Constants.PORTLET_REQUEST_SESSION );
                    }
                }
            }
        }

        if ( log.isDebugEnabled() ) log.debug( "end process 'model' portlets" );
    }
*/

    private static Object syncCtxDebug = new Object();
    private static void processPortletData(PortalRequestInstance portalRequestInstance)
        throws Exception {

        ByteArrayOutputStream outputStream = null;
        for ( int i = 0; i<portalRequestInstance.portletResultOutput.size(); i++ ) {
            SitePortletDataType item = (SitePortletDataType)portalRequestInstance.portletResultOutput.get( i );

            if ( log.isDebugEnabled() )
                log.debug( "getIsError() - "+item.getIsError()+", getIsXml() - "+item.getIsXml() );

            if ( Boolean.TRUE.equals(item.getIsError()) || !Boolean.TRUE.equals(item.getIsXml()) ) {
                if (outputStream!=null) {
                    processTransforming( outputStream, portalRequestInstance );
                    outputStream = null;
                }

                if ( log.isDebugEnabled() ) {
                    synchronized(syncCtxDebug) {
                        MainTools.writeToFile( WebmillConfig.getWebmillDebugDir()+"ctx-from-url.xml", item.getData() );
                    }
                }

                portalRequestInstance.byteArrayOutputStream.write( item.getData() );
            }
            else {
                // 1st xml element
                if ( outputStream==null ) {
                    outputStream = new ByteArrayOutputStream();

                    // format for controll of expiration of content
                    //<META HTTP-EQUIV="Expires" CONTENT="Wed, 01 Jan 1986 00:00:01 GMT">
                    //<META HTTP-EQUIV="Last-Modified" CONTENT="Wed, 17 Mar 2010 10:13:25 GMT">

                    outputStream.write(
                        ( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
                        "<SiteTemplate language=\""+
                          portalRequestInstance.getLocale().toString()+"\" type=\""+portalRequestInstance.getDefaultPortletType()+"\">" ).getBytes()
                    );
                }

                outputStream.write( item.getData() );

                boolean flag =
                    ( i+1>=portalRequestInstance.portletResultOutput.size() ||
                    !Boolean.TRUE.equals(( (SitePortletDataType)portalRequestInstance.portletResultOutput.get( i+1 ) ).getIsXml())
                    );

                if ( log.isDebugEnabled() ) log.debug( "#1.4 "+ flag );

                // if next portlet not xml's portlet, then transform current part and continue
                if ( flag ) {
                    processTransforming( outputStream, portalRequestInstance );
                    outputStream = null;
                }
            }
        }
    }

    private static void processSiteTemplateItems( PortalRequestInstance portalRequestInstance )
        throws Exception{

        portalRequestInstance.portletResultOutput = new ArrayList( portalRequestInstance.template.getSiteTemplateItemCount() );

        for ( int i = 0; i<portalRequestInstance.template.getSiteTemplateItemCount(); i++ ){
            TemplateItemType item = portalRequestInstance.template.getSiteTemplateItem( i );
            if (item.getNamespace()==null) {
                item.setNamespace( "p"+i );
            }

            if ( log.isDebugEnabled() )
                log.debug( "idx: "+i+", TemplateItem type: "+item.getType()+", namespace: "+item.getNamespace() );

            SitePortletDataType data = null;
            try {
                data = TemplateItemBaseClass.processTemplateItem( item, portalRequestInstance );
            }
            catch (Exception e) {
                final String es = "Error process template item";
                log.error( es, e );
                data = setData( es+"<br>"+ExceptionTools.getStackTrace( e, NUM_LINES, "<br>" ), true, false);
            }
            portalRequestInstance.portletResultOutput.add( data );
        }
    }

    private static void processTransforming( ByteArrayOutputStream xml, PortalRequestInstance portalRequestInstance )
        throws Exception {
        xml.write( "</SiteTemplate>".getBytes() );

        if ( log.isDebugEnabled() )
            log.debug( "string to transforming\n"+xml );

        xml.flush();
        xml.close();

        byte[] bytes = xml.toByteArray();
        if (log.isDebugEnabled()) {
            String fileName =
                WebmillConfig.getWebmillTempDir() + "portlet-data.xml";
            log.debug( "write portlet result to file "+fileName );
            MainTools.writeToFile( fileName, bytes );
        }

        ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
        Source xmlSource = new StreamSource( stream );

        try {
            portalRequestInstance.xslt.getTransformer().transform( xmlSource, new StreamResult( portalRequestInstance.byteArrayOutputStream ) );
        }
        catch(javax.xml.transform.TransformerException e) {
            try {
                log.error("Xalan version - " + org.apache.xalan.Version.getVersion());
            }
            catch(Throwable e1) {
                log.error("Error get version of xalan", e1);
            }

            try {
                log.error("Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
            }
            catch(Exception e2) {
                log.error("Error get version of xerces", e2);
            }
            log.error("TransformerException, try to recreate Transformer", e);

            try {
                portalRequestInstance.xslt.reinitTransformer();
            }
            catch(Exception e01) {
                log.error("General exception reintTransformer()", e01);
                throw e01;
            }
            catch(Error e02) {
                log.error("General error reintTransformer()", e02);
                throw e02;
            }
            portalRequestInstance.xslt.getTransformer().transform( xmlSource, new StreamResult( portalRequestInstance.byteArrayOutputStream ) );
        }
    }

    static SitePortletDataType setData(String data, boolean isError, boolean isXml) {
        return setData(data.getBytes(), isError, isXml);
    }

    static SitePortletDataType setData(byte[] bytes, boolean isError, boolean isXml) {
        SitePortletDataType data = new SitePortletDataType();

        data.setData( bytes );
        data.setIsError( new Boolean(isError) );
        data.setIsXml( new Boolean(isXml) );

        return data;
    }
}
