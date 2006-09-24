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
