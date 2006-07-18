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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 *
 * Author: Serg Malyukov
 * first verion from bouncycastle.org  example
 * Date: Aug 21, 2002
 * Time: 10:28:41 PM
 *
 * $Id$
 *
 * Class housing methods to decrypt files with signatures.
 */
public class DecryptSignature
{
    private static Logger cat = Logger.getLogger("org.riverock.security.DecryptSignature" );

    private byte[] lock_data = null; // Lock data
    private X509Certificate their_cert = null; // Senders Authentication Certificate.
    private SecretKey aes_key = null; // AES key.
    private byte[] aes_iv = null;   // AES IV
    private byte[] sig_code = null; // Signature.

    final int FILE_HEADER = 0x7e01; // File header.
    final int DATA_BLOCK = 1; // Header for Data block.
    final int FINAL_DATA_BLOCK = 2; // Header for last data block.
    final int SIG_BLOCK = 3; // Header for Signature Block.
    final int CERT_BLOCK = 4; // Header for senders certificate block.

    final int KEY_BLOCK = 16; // Header for key block. (AES)
    final int IV_BLOCK = 17; // Header for IV block. (AES)
    final int LOCK_BLOCK = 18; // Header for locking block.

    protected void finalize() throws Throwable
    {
        lock_data = null;
        their_cert = null;
        aes_key = null;
        aes_iv = null;
        sig_code = null;

        super.finalize();
    }

    public DecryptSignature()
    {
    };

    /**
     * zero out the passed in byte array
     */
    private static void blank(
            byte[] bytes)
    {
        for (int t = 0; t < bytes.length; t++)
        {
            bytes[t] = 0;
        }
    }

    /**
     * Get senders Certificate.
     */
    public X509Certificate getSendersCert()
    {
        return (their_cert);
    }

    /**
     * Decrypt the file.
     */
    public void decryptFile(
            InputStream  inStream,
            SecureRandom sec_rand,
            File file_out)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"); // Get Cipher object.

        cat.debug("Cipher - "+cipher);
        //
        // Initialize Cipher object with SecretKey and IV for the Symmetric cipher (AES)
        //
        cipher.init(Cipher.DECRYPT_MODE, aes_key, new IvParameterSpec(aes_iv));


        FileOutputStream file_str = new FileOutputStream(file_out); // Where to save plain text.

        cat.debug("target file - "+file_out);
        cat.debug("target file - "+file_str);
        //
        // Input source file, via DataInputStream wrapper.
        //

        DataInputStream data_str = new DataInputStream(inStream);


        boolean stop = false; // Loop breaker.
        int cmd = 0; // Variable to hold block header.
        int l = 0; // Universal length variable.
        byte[] buf = new byte[8192]; // A buffer to work in.
        byte[] _out = null;

        for (; ;)
        {
            cmd = data_str.readShort(); // Read in block header.

            if (cmd == DATA_BLOCK)
            {
                l = data_str.readInt(); // Get length of data.
                data_str.readFully(buf, 0, l); // Read data.

                cat.debug("length of data  - "+l);
                cat.debug("target file - "+file_str);
                cat.debug("Cipher - "+cipher);
                cat.debug("buf  - "+buf);

                _out = cipher.update(buf,0,l);
                if (_out != null)
                {
                    file_str.write( _out ); // Decrypt and save block.
                }
                else
                {
                    cat.error("Cipher.update -is null.Need report to bouncycastle.org");
                }

                System.out.print(".");
            }

            if (cmd == FINAL_DATA_BLOCK)
            {
                l = data_str.readInt(); // Length of data.
                data_str.readFully(buf, 0, l); // Read in data.
                _out = cipher.doFinal(buf, 0, l);
                if (_out != null)
                {
                    file_str.write(_out); // Decrypt and save final block. (deal with padding etc)
                }
                else
                {
                    cat.error("Cipher.doFinal -is null.Need report to bouncycastle.org");
                }
                System.out.println("!");
                break;
            }

            //
            // The following blocks and their content are skipped.
            //
            if (cmd == KEY_BLOCK) // Skip the KEY Block
            {
                l = data_str.readInt();
                data_str.skip(l);
            }

            if (cmd == IV_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.skip(l);
            }

            if (cmd == LOCK_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.skip(l);
            }

            if (cmd == CERT_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.skip(l);
            }

            if (cmd == SIG_BLOCK)
            {
                l = data_str.readInt();
                data_str.skip(l);
            }
        }

        blank(buf); // Erase buffer.

        buf = null; // Set null.

        //
        // Flush and streams where necessary.
        //

        file_str.flush();
        file_str.close();
        data_str.close();
    }

    /**
     * This method, tests the validity of the file by using the Signature.
     */
    public boolean validateSIG(
            InputStream inStream)
            throws Exception
    {
        Signature sig = Signature.getInstance("MD5withRSA", "BC"); // Set up signature object.

        sig.initVerify(their_cert); // Initialize with senders certificate.
        sig.update(lock_data); // Update with lock data.

        //
        // Set up input stream wrappers.
        //

        SignatureInputStream sig_str = new SignatureInputStream( inStream, sig);
        DataInputStream data_str = new DataInputStream(sig_str);

        int cmd = 0; // variable to store block header.
        byte[] buf = new byte[8192]; // Buffer to work in.
        int l = 0; // Universal length variable.

        //
        // It is worth while noting that I have used dummy reads instead of skipping
        // through the data. SignatureInputStream only processes data when a read method
        // is called. Now while the skip command my simply read through (n) bytes of data
        // on some streams it may actually do a skip which means that the data will not get
        // processed through the signature calculation.
        //

        for (; ;)
        {
            cmd = data_str.readShort(); // Read off block header/

            if (cmd == DATA_BLOCK) // Skip HMAC block.
            {
                l = data_str.readInt(); // Read length.
                data_str.read(buf, 0, l); // dummy read.
            }

            if (cmd == FINAL_DATA_BLOCK) // Skip the KEY Block
            {
                l = data_str.readInt();
                data_str.read(buf, 0, l); // dummy read
            }


            if (cmd == KEY_BLOCK) // Skip the KEY Block
            {
                l = data_str.readInt();
                data_str.read(buf, 0, l); // dummy read
            }

            if (cmd == IV_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.read(buf, 0, l); // dummy read
            }

            if (cmd == LOCK_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.read(buf, 0, l); // dummy read
            }

            if (cmd == CERT_BLOCK) // Skip the IV block.
            {
                l = data_str.readInt();
                data_str.read(buf, 0, l);// dummy read
            }

            if (cmd == SIG_BLOCK)
            {
                break;
            }
        }

        data_str.close();

        //
        // Note that the sig code has already been extracted from the file by the
        // parseHeader() method.
        //

        return sig.verify(sig_code);
    }

    /**
     * The parseHeader() method extracts and decrypts the information contained in
     * encrypted files header.
     */
    public void parseHeader(
            InputStream inStream,
            SecureRandom sec_rand,
            PrivateKey priv_key)
            throws Exception
    {

        //
        // Set up input stream wrappers to read the file.
        //

        DataInputStream data_in = new DataInputStream( inStream);
        int l = 0; // Universal length variable.
        boolean ena = false; // Enable flag.. (Set when file header found)
        boolean stop = false; // Stop flag.

        Cipher rsa_eng = Cipher.getInstance("RSA/None/OAEPPadding", "BC"); // RSA Cipher instance.
        rsa_eng.init(Cipher.DECRYPT_MODE, priv_key, sec_rand); // Initialize cipher for decryption.

        while (!stop)
        {
            try
            {
                int cmd = data_in.readShort(); // Read in block header.

                if (cmd == FILE_HEADER) // File header.
                {
                    ena = true; // Flag file header found.
                    System.out.println("Header Parse: File Header");
                    continue;
                }

                if (cmd == DATA_BLOCK) // Read in data block
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }
                    System.out.println("Header Parse: Data Block, size = " + l);

                    l = data_in.readInt(); // Read length.
                    data_in.skip(l); // Skip this data.
                    continue;
                }

                if (cmd == FINAL_DATA_BLOCK) // Final data block.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }
                    l = data_in.readInt(); // Read length
                    System.out.println("Header Parse: Final Data Block, size = " + l);
                    data_in.skip(l); // Skip this data.
                    continue;
                }

                if (cmd == SIG_BLOCK) // Signature block.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }
                    l = data_in.readInt(); // Read length.
                    System.out.println("Parse: Signature block size = " + l);
                    sig_code = new byte[l]; // Create new array (l) in size.
                    data_in.readFully(sig_code); // Read in signature.
                    continue;
                }


                if (cmd == KEY_BLOCK) // Read in key block.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }

                    l = data_in.readInt(); // Read length.
                    System.out.println("Parse: Key encoded block size = " + l);
                    byte[] d = new byte[l];  // Create new array (l) in size.
                    data_in.readFully(d); // read in data.

                    //
                    // We must use a SecretKeySpec set up for AES
                    // to convert the raw encoded AES key back into a SecretKey Object.
                    // The key is also decrypted before processing.
                    //

                    aes_key = (SecretKey) new SecretKeySpec(rsa_eng.doFinal(d), "AES");
                    continue;
                }

                if (cmd == IV_BLOCK) // Read in IV.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }
                    l = data_in.readInt(); // Read length
                    System.out.println("Parse: IV block, size = " + l);
                    aes_iv = new byte[l]; // Create new array for IV (l) in size.
                    data_in.readFully(aes_iv); // Read in IV.
                    aes_iv = rsa_eng.doFinal(aes_iv); // Decrypt IV
                    continue;
                }


                if (cmd == LOCK_BLOCK) // Read lock.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }


                    //
                    // Set up cipher to decrypt lock data.
                    //

                    Cipher dec_lock = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"); // Get Cipher object.
                    dec_lock.init(Cipher.DECRYPT_MODE, aes_key, new IvParameterSpec(aes_iv));
                    l = data_in.readInt(); // Read Length
                    System.out.println("Parse: Locking data block, size = " + l);
                    byte[] d = new byte[l]; // Create new byte array (l) in size.
                    data_in.readFully(d); // Read lock.
                    lock_data = dec_lock.doFinal(d); // Decrypt lock.

                    dec_lock = null; // Make null;

                    continue;
                }


                if (cmd == CERT_BLOCK) // Senders certificate block.
                {
                    if (!ena)
                    {
                        throw new Exception("Broken header");
                    }

                    l = data_in.readInt(); // Read Length
                    System.out.println("Parse: Fetch Cert block, size = " + l);

                    byte[] d = new byte[l]; // Create new array (l) in length.

                    data_in.readFully(d); // Read in certificate.

                    //
                    // The encoded cert must be converted back into an Certificate Object.
                    // More specifically an X509Certificate object.
                    // To do this we use the CertificateFactory set up for our type.
                    // In this case the type is X509.
                    //
                    // See CertificateFactory in the java doc for more info.
                    //

                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    their_cert = (X509Certificate) cf.generateCertificate(
                            new ByteArrayInputStream(d)
                    );
                    continue;
                }
            }
            catch (EOFException eof)
            {
                stop = true;
            }
        }
        return;
    }

}
