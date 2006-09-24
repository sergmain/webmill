/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.Properties;

/**
 * @author SergeMaslyukov
 *         Date: 01.12.2005
 *         Time: 16:25:45
 *         $Id$
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
