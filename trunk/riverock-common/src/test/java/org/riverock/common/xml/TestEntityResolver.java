/*
 * org.riverock.common - Supporting classes and utilities
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
