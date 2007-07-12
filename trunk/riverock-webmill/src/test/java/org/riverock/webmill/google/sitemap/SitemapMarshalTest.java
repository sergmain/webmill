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
package org.riverock.webmill.google.sitemap;

import java.util.List;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.riverock.webmill.google.sitemap.schema.sitemap.Urlset;
import org.riverock.webmill.google.sitemap.schema.sitemap.Url;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 19:38:56
 *         <p/>
 *         $Id$
 */
public class SitemapMarshalTest extends TestCase {
    
    public void testMarshallSitemap() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshalSitemap(stream);
        String sitemap = new String(stream.toByteArray());
        assertNotNull(sitemap);
    }


    public static void main(String[] args) throws JAXBException {
        long sm = System.currentTimeMillis();
        for (int i=0; i<1; i++) {
            (new SitemapMarshalTest()).marshalSitemap(System.out);
        }

        System.out.println("sm = " + (System.currentTimeMillis()-sm));
    }

    private void marshalSitemap(OutputStream stream) throws JAXBException {
        List<Url> urls = new ArrayList<Url>();
        Url url;

        url = new Url();
        url.setLoc("http://askmore.info/page/about/Paris_hilton");
        urls.add(url);

        url = new Url();
        url.setLoc("http://askmore.info/page/about/Christina_Aguilera");
        urls.add(url);

        GoogleSitemapService.marshall(urls, stream);
    }
}
