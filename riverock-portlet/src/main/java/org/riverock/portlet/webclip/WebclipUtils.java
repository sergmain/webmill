package org.riverock.portlet.webclip;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.apache.commons.lang.CharEncoding;

import org.riverock.portlet.dao.PortletDaoFactory;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;

/**
 * User: SMaslyukov
 * Date: 11.05.2007
 * Time: 20:41:34
 */
@SuppressWarnings({"UnusedAssignment"})
public class WebclipUtils {
    private static Logger log = Logger.getLogger(WebclipPortlet.class);

    public static void loadContentFromSource(WebclipBean webclip, String url) throws IOException {
        URL urlObject = new URL(url);
        URLConnection urlConnection;
//                Proxy proxy = prepareProxy(request);
//                if (proxy!=null) {
//                    urlConnection = urlObject.openConnection(proxy);
//                }
//                else {
        urlConnection = urlObject.openConnection();
//                }
        if (log.isDebugEnabled()) {
            log.debug("Current read timeout: " + urlConnection.getReadTimeout());
        }
        urlConnection.setReadTimeout(WebclipConstants.DEFAULT_READ_TIMEOUT);
        InputStream is = urlConnection.getInputStream();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(os);

        byte[] bytes = new byte[1024];
        int count;
        while ((count=is.read(bytes)) != -1) {
            gzipOutputStream.write(bytes, 0, count);
        }
        is.close();
        is = null;

        gzipOutputStream.flush();
        gzipOutputStream.close();
        gzipOutputStream=null;

        PortletDaoFactory.getWebclipDao().setOriginContent(webclip, os.toByteArray());
    }

    public static void processStoredContent(WebclipBean webclip, String hrefPrefix, String hrefStartPart, PortalDaoProvider portalDaoProvider, Long siteLanguageId) throws IOException {
        byte[] bytes = webclip.getZippedOriginContentAsBytes();
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bytes = new byte[1024];
        int count;
        while ((count=gzipInputStream.read(bytes)) != -1) {
            os.write(bytes, 0, count);
        }
        gzipInputStream.close();
        gzipInputStream = null;

        bytes = os.toByteArray();

        WebclipUrlProducer producer = new WebclipUrlProducerImpl(hrefPrefix, hrefStartPart);
        WebclipDataProcessor processor = new WebclipDataProcessorImpl(
            producer, bytes, WebclipConstants.DIV_NODE_TYPE, "content", portalDaoProvider, siteLanguageId
        );

        os = new ByteArrayOutputStream();

        processor.modify(os);
        String webclipData = os.toString(CharEncoding.UTF_8);
        PortletDaoFactory.getWebclipDao().updateWebclip(webclip, webclipData);
    }
}
