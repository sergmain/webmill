/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

package org.riverock.generic.security;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Signature output stream is a stream wrapper that
 * passes data though a Signature object before passing it on.
 *
 *  $Id$
 *
 *
 * Author: Serg Malyukov
 * Date: Aug 21, 2002
 * Time: 11:02:04 PM
 *
 */

public class SignatureOutputStream
        extends FilterOutputStream
{
    private Signature sig = null;

    protected void finalize() throws Throwable
    {
        sig = null;

        super.finalize();
    }

    public SignatureOutputStream(
            OutputStream out,
            Signature sig)
    {
        super(out);
        this.sig = sig;
    }

    public void write(
            int b)
            throws IOException
    {
        try
        {
            sig.update((byte) b);
        }
        catch (SignatureException s_ex)
        {
            throw new IOException(s_ex.getMessage());
        }
        out.write(b);
    }

    public void write(
            byte[] b)
            throws IOException
    {
        try
        {
            sig.update(b);
        }
        catch (SignatureException s_ex)
        {
            throw new IOException(s_ex.getMessage());
        }

        out.write(b);
    }

    public void write(
            byte[] b,
            int o,
            int l)
            throws IOException
    {
        try
        {
            sig.update(b, o, l);
        }
        catch (SignatureException s_ex)
        {
            throw(new IOException(s_ex.getMessage()));
        }

        out.write(b, o, l);
    }
}
