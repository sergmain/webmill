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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import org.apache.log4j.Logger;

/**
 *
 * Author: Serg Malyukov
 * Date: Aug 21, 2002
 * Time: 10:58:19 PM
 *
 * $Id$
 *
 * Class housing methods to encrypt files with signatures.
 */
public class EncryptSignature
{
    private static Logger cat = Logger.getLogger("org.riverock.security.EncryptSignature" );

    //
    // Define our block header values.
    //

    static final int FILE_HEADER = 0x7e01;  // File header.
    static final int DATA_BLOCK = 1; // Data block
    static final int FINAL_DATA_BLOCK = 2; // Final data block.
    static final int SIG_BLOCK = 3; // Signature block
    static final int CERT_BLOCK = 4; // Certificate block.

    static final int KEY_BLOCK = 16; // Key block
    static final int IV_BLOCK = 17; // IV block
    static final int LOCK_BLOCK = 18; // Locking block


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
     * Encrypt file.
     */
    public static void processFile(
            InputStream inStream,
            X509Certificate x_cert,
            X509Certificate my_cert,
            PrivateKey my_private,
            SecureRandom sec_rand,
            File file_out)
            throws Exception
    {
        processFile(inStream,
                x_cert.getPublicKey(), my_cert, my_private, sec_rand, file_out);
    }

    /**
     * Encrypt file..
     */
    public static void processFile(
            InputStream inStream,
            PublicKey pub_key,
            X509Certificate my_cert,
            PrivateKey my_private,
            SecureRandom sec_rand,
            File file_out)
            throws Exception
    {
        sec_rand = SecureRandom.getInstance("SHA1PRNG", "SUN");

        //
        // Generate key for AES cipher at 128 bits.
        // Using the appropriate key generator.
        // It is important that you use the correct key generator.
        // As it will filter out any keys that may be weak on a cipher by cipher
        // basis.
        //

        System.out.println("Generate key for AES at 128 bits");
        KeyGenerator key_gen = KeyGenerator.getInstance("AES", "BC"); // Get instance of an AES generator.

        key_gen.init(128, sec_rand); // Configure for 128 bits using out previously salted PRNG.

        Key aes_key = key_gen.generateKey(); //Generate key.

        //
        // Instantiate Symmetric cipher for encryption.
        //
        System.out.println("Set up cipher AES/CBC/PKCS7Padding");
        Cipher output_cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"); // Cipher object.

        output_cipher.init(Cipher.ENCRYPT_MODE, aes_key, sec_rand); // Initialize cipher.

        //
        // Get key and IV for cipher so that they can be later
        // encrypted to build a header.
        //
        byte[] aes_key_enc = aes_key.getEncoded();

        //
        // The cipher automatically generates the IV if none is supplied
        // and one is required.
        // You need to collect the IV.
        //

        byte[] aes_iv = output_cipher.getIV();

        //
        // Set up locking data so that one piece of the Signature info or
        // HMAC is based on data that is not directly transmitted. If this
        // is not done the message could be resigned by an attacker if
        // the sender has been compromised and their private key is known.
        // (Which you should expect.)
        //
        // I generally prefer to use locking data like this so that the
        // message can be validated as original without having to be fully
        // decrypted first.
        //
        // As another approach you could take the Signature or HMAC of the
        // plain text of the message the result is still the same as if you
        // had chosen suitably large locking data.
        //
        // NOTE: In the case of an HMAC the locking data is the key to the HMAC.
        //
        byte[] lock = new byte[24];

        sec_rand.nextBytes(lock);

        //
        // Setup Signature
        //
        System.out.println("Generate lock data to tie signature to file");

        Signature sig = Signature.getInstance("MD5withRSA", "BC"); // Set up signature object.

        sig.initSign(my_private, sec_rand); // Initialize with my private signing key.
        sig.update(lock); // put plain text of lock data into signature.


        //
        // Setup RSA to encrypt secrets.
        //
        System.out.println("Set up RSA with OAEPPadding (see PKCS1 V2)");
        Cipher rsa_eng = Cipher.getInstance("RSA/None/OAEPPadding", "BC"); // Set up cipher for RSA.
        rsa_eng.init(Cipher.ENCRYPT_MODE, pub_key, sec_rand); // Initialize.

        //
        // Setup to process File.
        //
//        FileInputStream in_str = new FileInputStream(inStream); // Source of plain text.
        FileOutputStream file_str = new FileOutputStream(file_out); // Final output stream.
        SignatureOutputStream sig_str = new SignatureOutputStream(file_str, sig); // As it says.
        DataOutputStream data_str = new DataOutputStream(sig_str); // See java doc.

        //
        // We are now going to form the header for the encrypted file.
        //

        System.out.println(file_out.getName() + " >> file header");
        data_str.writeShort(FILE_HEADER); // Write a file header.

        //
        // Write out a block for the key of the AES cipher.
        //
        System.out.println(file_out.getName() + " >> Aes key enc with RSA");

        data_str.writeShort(KEY_BLOCK); // Block header.
        byte[] tmp = rsa_eng.doFinal(aes_key_enc); // Encrypt it with RSA.
        data_str.writeInt(tmp.length); // Write length.
        data_str.write(tmp); // Write data.
        blank(tmp); // Erase tmp array.

        //
        // This will upset some people because you don't have to keep the
        // IV a secret.
        //
        // I keep the IV secret because I prefer to keep things secret
        // when I can.
        //
        System.out.println(file_out.getName() + " >> Aes IF enc with RSA (See note in source code)");
        data_str.writeShort(IV_BLOCK);  // Block header
        tmp = rsa_eng.doFinal(aes_iv); // Encrypt with RSA.
        data_str.writeInt(tmp.length); // Write length.
        data_str.write(tmp); // Write data.
        blank(tmp); // Clear.

        //
        // Write out lock data for SIGNATURE.
        //
        // This system takes the signature on the output of the processing and to prevent it
        // from being resigned and messed about with by attackers we incorporate into the signature
        // some salt that is randomly generated.
        // This salt is encrypted with the Symmetric cipher.
        // This forces the attacker to have to be able to decrypt the AES key and IV before they
        // can alter the message in any way.
        //

        System.out.println(file_out.getName() + " >> Signature lock data enc with AES");

        data_str.writeShort(LOCK_BLOCK); // Write header.
        tmp = output_cipher.doFinal(lock); // Encrypt with AES.
        data_str.writeInt(tmp.length); // Write length.
        data_str.write(tmp); // Write data.
        blank(tmp); // Clear.

        //
        // Reset AES cipher back to original, as you can not rely on the doFinal() method to do it.
        //

        output_cipher.init(Cipher.ENCRYPT_MODE, aes_key, new IvParameterSpec(aes_iv)); // initialize with aes_key.

        //
        // Now we must set up to process the file.
        //

        int l = 0; // Universal length variable.
        byte[] buf = new byte[8192]; // A buffer to work in.
        byte[] out = null; // Output buffer.

        //
        // Read while length is > -1
        //

        while ((l = inStream.read(buf)) > -1)
        {
            cat.debug("length - " + l);
            out = output_cipher.update(buf, 0, l); // Encrypt data.
            if (out != null)
            {
                cat.debug("out.length - " + out.length);

                data_str.writeShort(DATA_BLOCK); // Write data block header.
                data_str.writeInt(out.length); // Write length.
                data_str.write(out); // Write encrypted data.
                System.out.println(file_out.getName() + " >> Encrypted " + out.length + " bytes output");
            }
        }

        //
        // This is the last block
        // It will contain the final output of the cipher
        // which will have any remaining plain text less that the block
        // size of AES (16 bytes) padded PKCS7 style and encrypted.
        // Note use of cipher.doFinal();
        //

        out = output_cipher.doFinal(); // Do final encryption.
        data_str.writeShort(FINAL_DATA_BLOCK); // Write header.
        data_str.writeInt(out.length); // Write length.
        data_str.write(out); // Write Data.
        System.out.println(file_out.getName() + " >> Final Encrypted " + out.length + " bytes output");

        blank(buf); // Clear buffer.
        buf = null; // Set Null

        //
        // Now we need to write out our certificate that we
        // use for signing so that the recipient can verify our
        // identity.
        //

        data_str.writeShort(CERT_BLOCK); // Cert block header.
        tmp = my_cert.getEncoded(); // Get encoded in a byte array.
        data_str.writeInt(tmp.length); // Write length.
        data_str.write(tmp); // Write data.

        //
        // The last block we write out is our
        // certificate block.
        //

        System.out.println(file_out.getName() + " >> Write out Signature code block.");
        data_str.writeShort(SIG_BLOCK); // Write Header.
        data_str.flush(); // Flush it..

        //
        // The flush was called because we now need to
        // finalize our signature but calling Signature.sign()
        // so all the data needs to flushed first.
        //

        tmp = sig.sign(); // Get signature code.
        data_str.writeInt(tmp.length); // Write length.
        data_str.write(tmp); // Write data.
        blank(tmp); // Clear.

        //
        // Flush and close output.
        //

        data_str.flush();
        data_str.close();

        //
        // Get rid of key as best we can.
        //

        System.out.println("Dispose of key material as best we can in java..");
        blank(aes_key_enc);     // Clear encoded key array.
        aes_key_enc = null;     // Set null.
        aes_key = null;         // Best we can do on a Key object;
        tmp = null;             // Get rid of tmp also.

        System.out.println("The End..");
    }
}
