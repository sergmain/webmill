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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.apache.commons.codec.binary.Base64;

// OID 1.2.840.113549.1.1.4 - md5WithRSAEncryption
/**
 *
 *  $Id$
 *
 */

public class SecurityTools
{
    private static Logger cat = Logger.getLogger( "org.riverock.security.SecurityTools"   );

    public final static String providerName = "Forge";

    public static byte[] getBytesFromFile(String fileName)
            throws SecurityException
    {
        File file_ = null;
        InputStreamReader in = null;
        byte retBytes[] = null;
        cat.debug("Start bytes from file");
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
            cat.debug("End bytes from file");
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
            cat.debug("Get bytes");
            byte bytes[] = getBytesFromFile(fileName);
            Base64 base64 = new Base64();

            byte decodedBytes[] = base64.decode(bytes);

            cat.debug("Make RSA private key");
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
            cat.debug("#4.0001 create x509 certificate instance, file " + fileName);
            CertificateFactory cf =
                    CertificateFactory.getInstance("X.509");

            if (cf == null)
            {
                cat.error("#4.0002 Error create x509 certificate instance, cf is null, file name "+ fileName);
                return null;
            }

            byte bytes[] = getBytesFromFile(fileName);
            cat.debug("#4.00021 total bytes " + bytes.length);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            cat.debug("#4.0004 return certificate");
            return (X509Certificate) cf.generateCertificate(bais);

        }
        catch (Exception e)
        {
            cat.error("Exception while create x509 certificate instance", e);
            throw new SecurityException(e.toString());
        }

    }

    public static PublicKey getPublicKeyFromCertPEM(String fileName)
            throws Exception
    {
        cat.debug("#3.0001 get x509 certificate");
        X509Certificate cert = getX509CertPEM(fileName);
        cat.debug("#3.0002 certificate is " + cert.toString());
        return cert.getPublicKey();
    }

    public static byte[] sign(String fileNamePrivateKey, byte dataToSign[], String alg)
            throws SecurityException
    {
        byte signature[] = null;
        try
        {
            cat.debug("Get private key");
            PrivateKey privKey = getPrivateKeyDER(fileNamePrivateKey);

            cat.debug("Creating a signature object");
            Signature signingSign = Signature.getInstance(alg);

            cat.debug("#2.001");
            signingSign.initSign(privKey);

            cat.debug("Signing data");
            signingSign.update(dataToSign);

            cat.debug("#2.003");
            signature = signingSign.sign();

        }
        catch (Exception e)
        {
            cat.error("Error signing data", e);
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
            cat.debug("Get public key");
            PublicKey pubKey = getPublicKeyFromCertPEM(fileNamePublicKey);
            cat.debug("Create signature instance");
            Signature verifySig = Signature.getInstance(alg);
            cat.debug("Init verify");
            verifySig.initVerify(pubKey);

            cat.debug("Update data for verifying");
            verifySig.update(dataToVerify);

            cat.debug("Verifying the signature");
            boolean sigOK = verifySig.verify(signature);

            cat.debug("return value "+sigOK);
            return sigOK;
        }
        catch (Exception e)
        {
            cat.error("Error of verify data", e);
            throw new SecurityException(e.toString());
        }
    }

}
