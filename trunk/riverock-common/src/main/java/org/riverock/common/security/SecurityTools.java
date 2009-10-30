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
package org.riverock.common.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

// OID 1.2.840.113549.1.1.4 - md5WithRSAEncryption
/**
 *
 *  $Id$
 *
 */

public class SecurityTools
{
    public final static String providerName = "Forge";

    public static byte[] getBytesFromFile(String fileName)
            throws SecurityException
    {
        File file_ = null;
        InputStreamReader in = null;
        byte retBytes[] = null;
        try
        {
            file_ = new File(fileName);
            in = new InputStreamReader(new FileInputStream(file_));

            int ch;
            int sizeBuff = 10000;
            byte buffBytes[] = new byte[sizeBuff];
            int i = 0;
            while (((ch = in.read()) != -1) && (i < sizeBuff))
                buffBytes[i++] = (byte) ch;

            in.close();
            in = null;

            retBytes = new byte[i];
            System.arraycopy(buffBytes, 0, retBytes, 0, i);
        }
        catch (Exception e)
        {
            throw new SecurityException(e.toString());
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                    in = null;
                }
                catch (Exception e)
                {
                }
            }
        }

        return retBytes;
    }

    public static PrivateKey getPrivateKeyDER(String fileName)
            throws SecurityException
    {
        try
        {
            byte bytes[] = getBytesFromFile(fileName);
/*
            Base64 base64 = new Base64();

            byte decodedBytes[] = base64.decode(bytes);
*/

/*
            au.com.forge.security.pkcs1.RSAPrivateKey rsaPrivateKey =
                    new au.com.forge.security.pkcs1.RSAPrivateKey(null);

            rsaPrivateKey.decode(decodedBytes);

            RSAPrivateCrtKeySpec privKeySpec = rsaPrivateKey.getKeySpec();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA", providerName);
            PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);

            return privKey;
*/
            return null;
        }
        catch (Exception e)
        {
            throw new SecurityException(e.toString());
        }

    }

    public static X509Certificate getX509CertPEM(String fileName)
            throws SecurityException
    {
        try
        {
            CertificateFactory cf =
                    CertificateFactory.getInstance("X.509");

            if (cf == null)
            {
                return null;
            }

            byte bytes[] = getBytesFromFile(fileName);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            return (X509Certificate) cf.generateCertificate(bais);

        }
        catch (Exception e)
        {
            throw new SecurityException(e.toString());
        }

    }

    public static PublicKey getPublicKeyFromCertPEM(String fileName)
            throws Exception
    {
        X509Certificate cert = getX509CertPEM(fileName);
        return cert.getPublicKey();
    }

    public static byte[] sign(String fileNamePrivateKey, byte dataToSign[], String alg)
            throws SecurityException
    {
        byte signature[] = null;
        try
        {
            PrivateKey privKey = getPrivateKeyDER(fileNamePrivateKey);

            Signature signingSign = Signature.getInstance(alg);

            signingSign.initSign(privKey);

            signingSign.update(dataToSign);

            signature = signingSign.sign();

        }
        catch (Exception e)
        {
            throw new SecurityException(e.toString());
        }
        return signature;
    }


    public static boolean verify(String fileNamePublicKey, byte dataToVerify[],
                                 byte signature[], String alg)
            throws SecurityException
    {
        try
        {
            PublicKey pubKey = getPublicKeyFromCertPEM(fileNamePublicKey);
            Signature verifySig = Signature.getInstance(alg);
            verifySig.initVerify(pubKey);

            verifySig.update(dataToVerify);

            boolean sigOK = verifySig.verify(signature);

            return sigOK;
        }
        catch (Exception e)
        {
            throw new SecurityException(e.toString());
        }
    }

}
