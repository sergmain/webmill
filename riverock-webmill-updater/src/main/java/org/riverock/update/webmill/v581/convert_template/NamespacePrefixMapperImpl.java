package org.riverock.update.webmill.v581.convert_template;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2007
 * Time: 23:21:17
 */
public class NamespacePrefixMapperImpl extends NamespacePrefixMapper {

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if( "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd".equals(namespaceUri) ) {
            return "element";
        }
        // otherwise I don't care. Just use the default suggestion, whatever it may be.
        return suggestion;

    }
}
