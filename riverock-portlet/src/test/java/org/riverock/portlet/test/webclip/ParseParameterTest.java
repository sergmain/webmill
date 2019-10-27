package org.riverock.portlet.test.webclip;

import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.ParameterParser;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;

import org.riverock.portlet.webclip.WebclipDataProcessorImpl;

/**
 * User: SMaslyukov
 * Date: 21.05.2007
 * Time: 22:35:45
 */
public class ParseParameterTest {
    private final static ParameterParser PARAMETER_PARSER = new ParameterParser();

    public static void main(String[] args) throws URIException {
//        String s = "/w/index.php?title=Template_talk:Niger-Congo-speaking_nations&action=edit";
//        String s = "http://www.dodge.com/sprinter/index.html?context=vehiclePage&type=vehicleLink";
        String s = "/w/index.php?title=Aminadav_Moshav&amp;action=edit";
        s = StringUtils.replace(s, "&amp;", "&");
        s = URIUtil.decode(s);
        List<NameValuePair> params = PARAMETER_PARSER.parse(s, WebclipDataProcessorImpl.SEPARATOR_HTTP_REQUEST_PARAM);
        System.out.println("params = " + params);
        params = PARAMETER_PARSER.parse(s.substring(s.indexOf(WebclipDataProcessorImpl.QUERY_SEPARATOR_HTTP_REQUEST_PARAM)+1), WebclipDataProcessorImpl.SEPARATOR_HTTP_REQUEST_PARAM);
        System.out.println("params = " + params);
    }
}
