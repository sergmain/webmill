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
package org.riverock.webmill.google.sitemap;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.riverock.webmill.google.sitemap.schema.sitemap.Urlset;
import org.riverock.webmill.google.sitemap.schema.sitemap.Url;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 19:38:56
 *         <p/>
 *         $Id$
 */
public class SitemapMarshalTest {
    public static void main(String[] args) throws JAXBException {
        long sm = System.currentTimeMillis();
        for (int i=0; i<1; i++)
            marshal();

        System.out.println("sm = " + (System.currentTimeMillis()-sm));
    }

    private static void marshal() throws JAXBException {
        List<Url> urls = new ArrayList<Url>();
        Url url;

        url = new Url();
        url.setLoc("http://askmore.info/page/about/Paris_hilton");
        urls.add(url);

        url = new Url();
        url.setLoc("http://askmore.info/page/about/Christina_Aguilera");
        urls.add(url);

        GoogleSitemapService.marshall(urls, System.out);
    }
}
