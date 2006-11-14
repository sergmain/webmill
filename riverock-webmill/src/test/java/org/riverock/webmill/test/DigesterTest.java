/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
