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
