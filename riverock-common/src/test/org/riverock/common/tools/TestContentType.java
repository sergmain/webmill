package org.riverock.common.tools;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 31.12.2004
 * Time: 01:09:38
 * $Id$
 */
public class TestContentType extends TestCase {

    public void testParseContentType()
        throws Exception
    {
        ServletTools.ContentType contentType = null;

        contentType = new ServletTools.ContentType( "text/html; charset=utf-8" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", !"UTF-8".equals( contentType.getCharset().toString() ) );

        contentType = new ServletTools.ContentType( "text/html" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", contentType.getCharset()!=null );

        contentType = new ServletTools.ContentType( "charset=utf-8" );
        assertFalse("contentType wrong", !"charset=utf-8".equals( contentType.getContentType()) );
        assertFalse("charset wrong", contentType.getCharset()!=null );

        contentType = new ServletTools.ContentType( "text/html; charset=ISO-8859-1" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", !"ISO-8859-1".equals( contentType.getCharset().toString() ) );
    }
}
