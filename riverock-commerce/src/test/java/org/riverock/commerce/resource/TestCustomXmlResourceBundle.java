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
package org.riverock.commerce.resource;

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author Sergei Maslyukov
 *         Date: 21.12.2006
 *         Time: 16:05:18
 *         <p/>
 *         $Id$
 */
public class TestCustomXmlResourceBundle extends TestCase {
    private static final String XML_RESOURCE = "/xml/resources/SiteHamradio_ru.xml";

    public void testResourceBundleParser() throws Exception {
        InputStream stream = TestCustomXmlResourceBundle.class.getResourceAsStream( XML_RESOURCE );
        CustomXmlResourceBundle.PairList pairList = CustomXmlResourceBundle.digestXmlFile( stream );
    }
}
