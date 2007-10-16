package org.riverock.portlet.webclip;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import junit.framework.TestCase;
import org.apache.commons.lang.CharEncoding;
import org.xml.sax.InputSource;

/**
 * User: SergeMaslyukov
 * Date: 16.10.2007
 * Time: 22:46:21
 * $Id$
 */
public class TestParseWebclipHtml extends TestCase {

    public void testParseFragment() throws Exception {


        InputSource inputSource = new InputSource( TestParseWebclipHtml.class.getResourceAsStream("/xml/webclip/test.html"));
        WebclipUrlChecker urlChecker = new WebclipUrlChecker() {
            public boolean isExist(Long siteLanguageId, String url) {
                return true;
            }
        };


        WebclipUrlProducer producer = new WebclipUrlProducerImpl("/hrefPrefix", "/hrefStartPart");
        WebclipDataProcessor processor = new WebclipDataProcessorImpl(
            producer, inputSource, WebclipConstants.DIV_NODE_TYPE, "content", null, 10L, urlChecker
        );

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        processor.modify(os);
        String webclipData = os.toString(CharEncoding.UTF_8);

    }
}
