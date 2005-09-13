/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2005, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.resource;

import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.resource.XmlResourceBundle;

/**
 * @author Serge Maslyukov
 */
public class PriceOrder_fr_FR extends XmlResourceBundle {
    private final static Logger log = Logger.getLogger( PriceOrder_fr_FR.class );

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
            log.error(es, e);
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
}