/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.common.resource;

import java.util.ListResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
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
