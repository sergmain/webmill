/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.test.resource_bundle;

import java.util.ResourceBundle;
import java.util.Locale;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.riverock.common.resource.CustomXmlResourceBundle;

import junit.framework.TestCase;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 21:59:10
 *         $Id$
 */
public class ResourceBundleTest extends TestCase {
    private static final String XML_RESOURCE = "/xml/resources/SiteHamradio_ru.xml";

    public void testResourceBundleParser() throws Exception {
        InputStream stream = ResourceBundleTest.class.getResourceAsStream( XML_RESOURCE );
        CustomXmlResourceBundle.PairList pairList = CustomXmlResourceBundle.digestXmlFile( stream );
    }
}