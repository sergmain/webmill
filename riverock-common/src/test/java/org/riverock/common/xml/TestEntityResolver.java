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
