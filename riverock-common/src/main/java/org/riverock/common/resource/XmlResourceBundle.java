/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.common.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.Properties;

/**
 * @author SergeMaslyukov
 *         Date: 01.12.2005
 *         Time: 16:25:45
 *         $Id: XmlResourceBundle.java 1229 2007-06-28 11:25:40Z serg_main $
 */
public abstract class XmlResourceBundle extends ListResourceBundle {

    public abstract void logError( String msg, Throwable th );

    private Object[][] resource = null;
    protected Object[][] getContents() {
        if (resource!=null) {
            return resource;
        }
        prepareProperties(this);
        return resource;
    }

    private static void prepareProperties(XmlResourceBundle xmlResourceBundle) {
        Properties properties;
        try {
            properties = loadFromXml(xmlResourceBundle);
        }
        catch (Exception e) {
            String es = "Error load properties from xml file " +getFileName(xmlResourceBundle);
            xmlResourceBundle.logError(es, e);
            throw new IllegalStateException(es, e);
        }
        xmlResourceBundle.resource = new Object[properties.size()][2];
        int i=0;
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            xmlResourceBundle.resource[i][0] = entry.getKey();
            xmlResourceBundle.resource[i][1] = entry.getValue();
            i++;
        }
    }

    private static String getFileName(XmlResourceBundle xmlResourceBundle) {
        String name = xmlResourceBundle.getClass().getName();
        name = "/" + name.replace( '.', '/');

        return name + ".xml";
    }

    private static Properties loadFromXml(XmlResourceBundle xmlResourceBundle) throws IOException {

        InputStream stream = xmlResourceBundle.getClass().getResourceAsStream( getFileName(xmlResourceBundle) );
        Properties properties = new Properties();
        properties.loadFromXML( stream );
        stream.close();

        return properties;
    }
}
