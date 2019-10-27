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
package org.riverock.common.contenttype;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 31.12.2004
 * Time: 01:09:38
 * $Id: TestContentType.java 1040 2006-11-14 13:57:38Z serg_main $
 */
public class TestContentType extends TestCase {

    public void testParseContentType()
        throws Exception
    {
        ContentType contentType = null;

        contentType = new ContentType( "text/html; charset=utf-8" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", !"UTF-8".equals( contentType.getCharset().toString() ) );

        contentType = new ContentType( "text/html" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", contentType.getCharset()!=null );

        contentType = new ContentType( "charset=utf-8" );
        assertFalse("contentType wrong", !"charset=utf-8".equals( contentType.getContentType()) );
        assertFalse("charset wrong", contentType.getCharset()!=null );

        contentType = new ContentType( "text/html; charset=ISO-8859-1" );
        assertFalse("contentType wrong", !"text/html".equals( contentType.getContentType() ) );
        assertFalse("charset wrong", !"ISO-8859-1".equals( contentType.getCharset().toString() ) );
    }
}
