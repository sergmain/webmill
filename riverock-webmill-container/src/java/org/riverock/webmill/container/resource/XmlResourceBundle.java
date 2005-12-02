/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.container.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
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

        Properties properties = null;
        try {
            properties = loadFromXml();
        }
        catch (Exception e) {
            String es = "Error load properties from xml file " +getFileName();
            logError(es, e);
            throw new IllegalStateException(es, e);
        }
        resource = new Object[properties.size()][2];
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        int i=0;
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            resource[i][0] = entry.getKey();
            resource[i][1] = entry.getValue();
            i++;
        }

        return resource;
    }

    public String getFileName() {
        String name = this.getClass().getName();
        name = "/" + name.replace( '.', '/');

        return name + ".xml";
    }

    private Properties loadFromXml() throws IOException {

        InputStream stream = this.getClass().getResourceAsStream( getFileName() );
        Properties properties = new Properties();
        properties.loadFromXML( stream );
        stream.close();
        stream = null;

        return properties;
    }
}
