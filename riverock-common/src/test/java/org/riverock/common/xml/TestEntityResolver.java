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
package org.riverock.common.xml;

import junit.framework.TestCase;
import org.xml.sax.EntityResolver;

/**
 * @author Sergei Maslyukov
 *         Date: 05.09.2006
 *         Time: 17:07:03
 *         <p/>
 *         $Id$
 */
public class TestEntityResolver  extends TestCase {

    public void testEntityResolver() throws Exception {

        assertNotNull(EntityResolverImpl.getEntityMap());
        assertNotNull(EntityResolverImpl.getEntityMap().get(EntityResolverImpl.SUN_WEBXML_23_DTD_PUBLIC_ID));
        assertNotNull(EntityResolverImpl.getEntityMap().get(EntityResolverImpl.IBM_PORTLET_DTD_PUBLIC_ID));

        EntityResolver resolver = new EntityResolverImpl();
        assertNotNull(resolver.resolveEntity(EntityResolverImpl.IBM_PORTLET_DTD_PUBLIC_ID, null) );
        assertNotNull(resolver.resolveEntity(EntityResolverImpl.SUN_WEBXML_23_DTD_PUBLIC_ID, null) );
        assertNull(resolver.resolveEntity(null, null) );
        assertNull(resolver.resolveEntity("khsdfkhskf jhklsfhl ksdjhlfkjsd", null) );
    }
}
