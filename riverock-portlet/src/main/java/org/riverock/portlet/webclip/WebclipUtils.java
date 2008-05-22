package org.riverock.portlet.webclip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.dao.PortletDaoFactory;

/**
 * User: SMaslyukov
 * Date: 11.05.2007
 * Time: 20:41:34
 */
@SuppressWarnings({"UnusedAssignment"})
public class WebclipUtils {
    private final static Logger log = Logger.getLogger(WebclipPortlet.class);

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

    public static void processStoredContent(
        WebclipBean webclip, String hrefPrefix, String hrefStartPart, PortalSpiProvider portalSpiProvider,
        Long siteLanguageId, WebclipUrlChecker urlChecker
    ) throws IOException {

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
            producer, bytes, WebclipConstants.DIV_NODE_TYPE, "content", siteLanguageId, urlChecker
        );

        os = new ByteArrayOutputStream();

        processor.modify(os);
        String webclipData = os.toString(CharEncoding.UTF_8);
        PortletDaoFactory.getWebclipDao().updateWebclip(webclip, webclipData);
    }

    public static WebclipBeanExtended getWebclip(Long siteId, Map<String, List<String>> m, String msg, boolean isInitData) {
        WebclipBeanExtended w = new WebclipBeanExtended();
        if (m.isEmpty()) {
            w.setStatus(msg + "preferences is empty.");
            return w;
        }

        w.setUrl(getPreferenceValue(m, msg, w, "url not defined.", "too many urls - ", WebclipConstants.URL_SOURCE_PREF));
        if (w.getStatus() !=null){
            return w;
        }
        w.setHref(getPreferenceValue(m, msg, w, "href not defined.", "too many hrefs - ", WebclipConstants.NEW_HREF_PREFIX_PREF));
        if (w.getStatus() !=null){
            return w;
        }
        w.setPrefix(getPreferenceValue(m, msg, w, "prefix not defined.", "too many prefixes - ", WebclipConstants.HREF_START_PAGE_PREF));
        if (w.getStatus() !=null){
            return w;
        }

        String id = getPreferenceValue(m, msg, w, "webclipId not defined.", "too many webclipId - ", WebclipConstants.WEBCLIP_ID_PREF);
        if (w.getStatus() !=null){
            return w;
        }
        Long webclipId = new Long(id);

        WebclipBean webclip = PortletDaoFactory.getWebclipDao().getWebclip(siteId, webclipId, isInitData);
        if (webclip==null) {
            w.setStatus(msg + "webclip for id "+ webclipId+" not found");
            return w;
        }
        w.setWebclip(webclip);
        return w;
    }

    public static String getPreferenceValue(Map<String, List<String>> m, String msg, WebclipBeanExtended w, String notDefined, String tooMany, String preferenceName) {
        List<String> list = m.get(preferenceName);
        if (list==null || list.isEmpty()) {
            w.setStatus(msg + notDefined);
            return null;
        }
        if (list.size()>1) {
            w.setStatus(msg + tooMany +list.size());
            return null;
        }
        String s = list.get(0);
        if (StringUtils.isBlank(s)) {
            w.setStatus(msg + preferenceName + " preference is empty.");
            return null;
        }
        return s;
    }
}
