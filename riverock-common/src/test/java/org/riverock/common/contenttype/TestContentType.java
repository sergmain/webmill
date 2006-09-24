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
package org.riverock.common.contenttype;

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
