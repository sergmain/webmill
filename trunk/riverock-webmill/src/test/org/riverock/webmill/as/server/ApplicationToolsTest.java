package org.riverock.webmill.as.server;

import java.io.StringReader;

import org.xml.sax.InputSource;
import org.exolab.castor.xml.Unmarshaller;

import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.tools.MainTools;

/**
 * @author smaslyukov
 *         Date: 05.08.2005
 *         Time: 16:37:29
 *         $Id$
 */
public class ApplicationToolsTest {

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        String str =
//            "<ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC6379yDAwAAAAEAAAAOAAAAVmFsdWVQYXJhbWV0ZXIzBABQSwECAAAUAAIACABlYlQut+/cgwMAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSwUGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC67/6XO5wAAAI4BAAAOAAAAVmFsdWVQYXJhbWV0ZXJtUO9LwzAQ/VdkXwUjCDohFi5NHRlTl7lmP77V9ugCra1JSpl/vYnD0W3eh8e9d7l34dEF2qYzOS7wq0PrIg==</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
//            "<ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC6379yDAwAAAAEAAAAOAAAAVmFsdWVQYXJhbWV0ZXIzBABQSwECAAAUAAIACABlYlQut+/cgwMAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSwUGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>UEsDBBQAAgAIAGViVC67/6XO5wAAAI4BAAAOAAAAVmFsdWVQYXJhbWV0ZXJtUO9LwzAQ/VdkXwUjCDohFi5NHRlTl7lmP77V9ugCra1JSpl/vYnD0W3eh8e9d7l34dEF2qYzOS7wq0PrIg==</ValueParameter><Compression typeCompression=\"ZIP\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ResourceRequest><CodeResource>prr.getIDirectionDBItemTypeList</CodeResource><Parameters><NameParameter>prr.id</NameParameter><ValueParameter>MQ==</ValueParameter><Compression typeCompression=\"NONE\"/></Parameters><Parameters><NameParameter>prr.test</NameParameter><ValueParameter>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48UmVzb3VyY2VSZXF1ZXN0PjxDb2RlUmVzb3VyY2U+7+4t8PPx8eroPC9Db2RlUmVzb3VyY2U+PFBhcmFtZXRlcnM+PE5hbWVQYXJhbWV0ZXI+cHJyLmlkPC9OYW1lUGFyYW1ldGVyPjxWYWx1ZVBhcmFtZXRlcj5NUT09PC9WYWx1ZVBhcmFtZXRlcj48Q29tcHJlc3Npb24gdHlwZUNvbXByZXNzaW9uPSJOT05FIi8+PC9QYXJhbWV0ZXJzPjwvUmVzb3VyY2VSZXF1ZXN0Pg==</ValueParameter><Compression typeCompression=\"NONE\"/></Parameters><ResponseCompressionType typeCompression=\"ZIP\"/></ResourceRequest>";
/*
            "<ResourceRequest>" +
            "<CodeResource>prr.getIDirectionDBItemTypeList</CodeResource>" +
            "<Parameters>" +
            "<NameParameter>prr.id</NameParameter>" +
            "<ValueParameter>UEsDBBQAAgAIALZcUy6379yDAwAAAAEAAAAOAAAAVmFsd" +
            "WVQYXJhbWV0ZXIzBABQSwECAAAUAAIACAC2XFMut+/cgw" +
            "MAAAABAAAADgAAAAAAAAAAAAAAAAAAAAAAVmFsdWVQYXJhbWV0ZXJQSw" +
            "UGAAAAAAEAAQA8AAAALwAAAAAA</ValueParameter>" +
            "<Compression typeCompression=\"ZIP\"/>" +
            "</Parameters>" +
            "<ResponseCompressionType typeCompression=\"ZIP\"/>" +
            "</ResourceRequest>";
*/
        InputSource inSrc = new InputSource( new StringReader(str) );
        ResourceRequestType res = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc);


        String result = ApplicationTools.decodeParameter(res.getParameters(0));

        String result1 = ApplicationTools.decodeParameter(res.getParameters(1));

        MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"as-req-param.xml", result1.getBytes());

        InputSource inSrc1 = new InputSource( new StringReader(result1) );
        ResourceRequestType res1 = (ResourceRequestType) Unmarshaller.unmarshal(ResourceRequestType.class, inSrc1);



        System.out.println(result);
//        System.out.println(  org.apache.commons.httpclient.("+");
    }

}
