package org.riverock.webmill.container.resource;

import java.util.ListResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 16:07:03
 *         $Id$
 */
public abstract class XmlResourceBundle extends ListResourceBundle {

    public final static class PairList {
        private List<Pair> pairs = new ArrayList<Pair>();

        public void addPair( Pair pair ) {
            pairs.add( pair );
        }

        public List<Pair> getPairs() {
            return pairs;
        }
    }

    public final static class Pair {
        private String key = null;
        private String value = null;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public String getFileName() {
        String name = this.getClass().getName();
        name = "/" + name.replace( '.', '/');

        return name + ".xml";
    }

    public PairList digestXmlFile() throws IOException, SAXException {

        InputStream stream = this.getClass().getResourceAsStream( getFileName() );
        PairList pairList = null;

        pairList = digestXmlFile( stream );


        return pairList;
    }

    public static PairList digestXmlFile( InputStream stream ) throws IOException, SAXException {
        PairList pairList;
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("resource", PairList.class);

        digester.addObjectCreate("resource/pair", Pair.class);
        digester.addSetProperties("resource/pair", "key", "key");
        digester.addSetProperties("resource/pair", "value", "value");
        digester.addSetNext("resource/pair", "addPair");

        pairList = (PairList)digester.parse(stream);
        return pairList;
    }

}
