/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * @author smaslyukov
 *         Date: 06.08.2005
 *         Time: 17:27:56
 *         $Id$
 */
public class DigesterTest {
    public static final String xml = 
        "<?xml  version=\"1.0\"  encoding=\"UTF-8\"?>\n" +
        "<SiteTemplate type=\"test.role\">\n"+
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderStart\"/>\n"+
        "<SiteTemplateItem  type=\"custom\"  value=\"HeaderStart\"/>\n"+
        "</SiteTemplate>\n";

    public static class SiteTemplate {
        private String type = null;
        private List<SiteTemplateItem> items = new ArrayList<SiteTemplateItem>();

        public void setType( String type ) {
            this.type = type;
        }

        public void addItem( SiteTemplateItem item ) {
            items.add( item );
        }

        public String toString() {
            String s =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<SiteTemplate type=\""+type+"\">\n";
            Iterator<SiteTemplateItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                SiteTemplateItem siteTemplateItem = iterator.next();
                s += "    <SiteTemplateItem type=\""+siteTemplateItem.getType()+"\"/>\n";
            }
            s += "</SiteTemplate>";
            return s;
        }
    }

    public static class SiteTemplateItem {
        private String type = null;

        public void setType( String type ) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public static void main(String[] args) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("SiteTemplate", SiteTemplate.class);
        digester.addSetProperties("SiteTemplate", "type", "type");

        digester.addObjectCreate("SiteTemplate/SiteTemplateItem", SiteTemplateItem.class);
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "type", "type");
        digester.addSetNext("SiteTemplate/SiteTemplateItem", "addItem");

        SiteTemplate st = (SiteTemplate) digester.parse( new ByteArrayInputStream( xml.getBytes() ));

        System.out.println("st = " + st);
    }
}
