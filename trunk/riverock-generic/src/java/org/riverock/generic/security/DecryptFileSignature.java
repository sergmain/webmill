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

import java.security.KeyStore;

import java.security.PrivateKey;

import java.security.SecureRandom;

import java.security.Security;



import org.apache.log4j.Logger;



import org.riverock.generic.config.GenericConfig;



/**

*

* Author: Serg Malyukov

* Date: Aug 21, 2002

* Time: 10:54:52 PM

*

 * $Id$

 *

 * Decrypt a file and use signatures MD5 with RSA for message validation and authentication.

 * This class can be used as a command line tool..

 * DecryptFileSIG [Source Encrypted file] [Destination Decrypted File] [key store]

 */

public class DecryptFileSignature

{

    private static Logger cat = Logger.getLogger("org.riverock.security.DecryptFileSignature" );



    private byte[] inArray = null;

    private File file_out = null;

    private PrivateKey priv_key = null;

    private SecureRandom sec_rand = null;



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

        inArray = null;

        file_out = null;

        priv_key = null;

        sec_rand = null;



        super.finalize();

    }





    /**

     * zero out the passed in byte array

     */

/*

    private static void blank(

            byte[] bytes)

    {

        for (int t = 0; t < bytes.length; t++)

        {

            bytes[t] = 0;

        }

    }

*/



    public void initParam(byte[] inBytes, File fileOut,

                          String keystoreFile, String aliasKS

                          )

    {

        sec_rand = null; // A secure random number generator.



        //

        // Add provider and seed SecureRandom with salt from '/dev/urandom' if device exists

        //



        try

        {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            sec_rand = SecureRandom.getInstance("SHA1PRNG", "SUN"); // SHA bases prng.



            if (new File("/dev/urandom").exists())

            {

                System.out.println("Salting SecureRandom (SHA1PRNG) from /dev/urandom");

                byte[] salt = new byte[8192];

                new FileInputStream("/dev/urandom").read(salt);

                sec_rand.setSeed(salt);

            }



        }

        catch (Exception ex)

        {

            cat.error("Error get instance of SecureRandom", ex);

        }



        //

        // Test for minimum argument count.

        //



        inArray = inBytes; // File in. (cipher text)

        file_out = fileOut; // File out. (plaintex)



        File keystore_file = new File(keystoreFile); // Keystore file.



        //

        // Test for existence of KeyStore file.

        //

        if (!keystore_file.exists())

        {

            cat.error("Keystore file not found. File: " + keystoreFile);

        }



//        boolean stop = false; // Reused boolean for controlling loops.

        KeyStore key_store = null; // The key store.



        try

        {

            key_store = KeyStore.getInstance("JKS", "SUN");  // Instantiate key store.



            //

            // Test password on keystore.

            // KeyStore will throw an exception if password incorrect.

            //

            char[] ks_pass = GenericConfig.getConfig().getSecurityKeyStorage().getPassword().toCharArray();

            try

            {

                key_store.load(new FileInputStream(keystore_file), ks_pass); // load in keystore file.

            }

            catch (Exception ex)

            {

                cat.error("Error create instance of KeyStore", ex);

            }



            //

            // Try fetch the secret key for a given alias.

            // KeyStore will throw an exception in alias not present.

            //



            try

            {

                priv_key = (PrivateKey) key_store.getKey(aliasKS, ks_pass); // Fetch private key.

            }

            catch (Exception ex)

            {

                cat.error("Error get private key", ex);

            }

        }

        catch (Exception ex)

        {

            cat.error("Error initParam for decrypt", ex);

        }

    }



/*

    private String getLine()

    {

        try

        {

            BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));

            return (bin.readLine());

        }

        catch (Exception ex)

        {

            ex.printStackTrace();

        }



        return (null);

    }

*/



    /**

     * Decrypt file.

     */

    public void decryptFile() throws Exception

    {

        //

        // Call parse header to decrypt and extract information

        // from the header.

        // This method will extract and define, 'lock_data', 'their_cert', 'aes_key', 'aes_iv', 'sig_code'

        //

        DecryptSignature dec_sig = new DecryptSignature();



        dec_sig.parseHeader(new ByteArrayInputStream(inArray), sec_rand, priv_key);



        //

        // After parsing the file you should take the senders certificate

        // and validate that against your trust store or what ever mechanism

        // you are using to authenticate the sender of the message.

        //

        // If your system cannot validate the certificate then drop the

        // message. People cause themselves a lot of problems by allowing

        // users to ignore a message that the system can not validate.

        //

        // You must not allow people to bypass or ignore your

        // authentication system. If you do you may as well not have a

        // system of authentication in the first place!

        //

        // This can be a lot more complicated than it looks and, at any

        // rate, it is beyond the scope of the example because I would

        // have to build a whole authentication frame work to get my point

        // across.

        //

        // X509Certificate their_Cert = dec_sig.getSendersCert();



        if (dec_sig.validateSIG(new ByteArrayInputStream(inArray))) // Check the file's integrity.

        {

            dec_sig.decryptFile(new ByteArrayInputStream(inArray),

                    sec_rand, file_out

            ); // Decrypt the file.

        }

        else // Didn't validate.

        {

            System.out.println("SIG failed to validate");

        }

    }

    // Have fun..

}

