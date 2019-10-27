package org.riverock.portlet.resource;

import java.util.ListResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 16:07:03
 *         $Id: CustomXmlResourceBundle.java 305 2005-12-02 18:57:37Z serg_main $
 */
public abstract class CustomXmlResourceBundle extends ListResourceBundle {

    public abstract void logError( String msg, Throwable th );

    private Object[][] resource = null;
    protected Object[][] getContents() {
        if (resource!=null) {
            return resource;
        }

        PairList list = null;
        try {
            list = digestXmlFile();
        }
        catch (Exception e) {
            String es = "Error digest file " +getFileName();
            logError(es, e);
            throw new IllegalStateException(es, e);
        }
        resource = new Object[list.getPairs().size()][2];
        Iterator<Pair> iterator = list.getPairs().iterator();
        int i=0;
        while (iterator.hasNext()) {
            Pair pair = iterator.next();
            resource[i][0] = pair.getKey();
            resource[i][1] = pair.getValue();
            i++;
        }

        return resource;
    }

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
        InputStream stream;
//        stream = CustomXmlResourceBundle.class.getResourceAsStream( getFileName() );
//        dumpInputStream(stream);
        stream = CustomXmlResourceBundle.class.getResourceAsStream( getFileName() );
        return digestXmlFile( stream );
    }

    private void dumpInputStream(InputStream stream) {
        try {
            String s = System.getProperty("java.io.tmpdi");
            if (s==null) {
                System.out.println("Can not get temp path from system properties");
                return;
            }
            if (s.endsWith("\\") || s.endsWith("//")) {
                s += "xml-res";
            }
            else {
                s += (File.separator+"xml-res");
            }
            File directory = new File(s);
            directory.mkdirs();
            File temp = File.createTempFile("xml-res-"+System.currentTimeMillis(), ".xml", directory);

            FileOutputStream fos = new FileOutputStream(temp);
            IOUtils.copy(stream, fos);
            fos.flush();
            fos.close();
            fos=null;
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
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
