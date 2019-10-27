package org.riverock.webmill.utils;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 20:22:55
 */
public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if( "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd".equals(namespaceUri) )
            return "element";

/*
            // I want the namespace foo to be the default namespace.
            if( "http://www.example.com/foo".equals(namespaceUri) )
                return "";

            // and the namespace bar will use "b".
            if( "http://www.example.com/bar".equals(namespaceUri) )
                return "b";

*/
        // otherwise I don't care. Just use the default suggestion, whatever it may be.
        return suggestion;

    }
}
