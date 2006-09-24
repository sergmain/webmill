/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.security;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Signature;
import java.security.SignatureException;

/*
 *
 * Author: Serg Malyukov
 * first verion from bouncycastle.org  example
 * Date: Aug 21, 2002
 * Time: 10:36:21 PM
 *
 */

/**
 * SignatureInputStream. As data is read it updates a MAC
 *
 *  $Id$
 */
public class SignatureInputStream extends FilterInputStream
{
    private Signature sig = null;

    public SignatureInputStream(
            InputStream st,
            Signature sig)
    {
        super(st);
        this.sig = sig;
    }

    public int read()
            throws IOException
    {
        int n = in.read();
        if (n > -1)
        {
            try
            {
                sig.update((byte) n);
            }
            catch (SignatureException s_ex)
            {
                throw(new IOException(s_ex.getMessage()));
            }
        }

        return (n);
    }

    public int read(
            byte[] b)
            throws IOException
    {
        int n = in.read(b);
        if (n > -1)
        {
            try
            {
                sig.update(b, 0, n);
            }
            catch (SignatureException s_ex)
            {
                throw new IOException(s_ex.getMessage());
            }
        }

        return (n);
    }

    public int read(
            byte[] b,
            int o,
            int l)
            throws IOException
    {
        int n = in.read(b, o, l);
        if (n > -1)
        {
            try
            {
                sig.update(b, o, n);
            }
            catch (SignatureException s_ex)
            {
                throw(new IOException(s_ex.getMessage()));
            }
        }

        return (n);
    }
}
