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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

/**
 *
 * Author: Serg Malyukov
 * Date: Aug 21, 2002
 * Time: 10:49:04 PM
 *
 * origin source - www.bouncycastle.org
 *
 * $Id$
 */
public class EncryptFileSignature
{
    private static Logger cat = Logger.getLogger("org.riverock.security.EncryptFileSignature" );

    /**
     * Encrypt a file and use signatures MD5 with RSA for message validation and authentication.
     * This class can be used as a command line tool..
     * EncryptFileSIG [source file] [dest file] [recipients certificate] [your trust store]
     *
     */
    private byte[] inArray = null;
//    private File inArray = null; // Source File.
    private File file_out = null; // Output File.
    private SecureRandom sec_rand = null; // Secure Random number generator.
    private PublicKey pub_key = null; // Public key.
    private X509Certificate my_cert = null;
    private PrivateKey my_private = null;

    protected void finalize() throws Throwable
    {
        inArray = null;
        file_out = null;
        sec_rand = null;
        pub_key = null;
        my_cert = null;
        my_private = null;

        super.finalize();
    }
/*
    public static void main(String[] args)
    {
        System.out.println("Start example");
        new EncryptFileSignature(args);
    }
*/

    /**
     * Read from commans line constructor.
     */
/*
    public EncryptFileSignature(String[] args)
    {
        try
        {
            initTerm(args);
            encryptFileSIG();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
*/

    public void initParam(
            byte[] inBytes,
            File storeFile,
            String certificateFile,
            String keystoreFile,
            String passwordKS,
            String aliasKS
            )
        throws Exception
    {
        sec_rand = null; // Secure random number

        if(cat.isDebugEnabled())
        {
            cat.debug( "Store file "+storeFile );
            cat.debug( "Certificate file "+certificateFile );
            cat.debug( "Keystore file "+keystoreFile );
            cat.debug( "password keystore "+passwordKS );
            cat.debug( "alias keystore "+aliasKS );
        }

        //
        // Add BouncyCastle JCE as a provider and create and seed the random number generator.
        //

        try
        {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Add provider.
            sec_rand = SecureRandom.getInstance("SHA1PRNG", "SUN"); // Create random number generator.

            //
            // Try and seed PRNG from '/dev/urandom' on UNIX machines this device generally exists.
            // if it does not then SecureRandom will employ its own platform independent methods.
            //

            if (new File("/dev/urandom").exists()) // Does device micro random exist.
            {
                System.out.println("Salting SecureRandom (SHA1PRNG) from /dev/urandom");
                byte[] salt = new byte[8192]; // salt.
                new FileInputStream("/dev/urandom").read(salt); // Read salt.
                sec_rand.setSeed(salt); // Set salt.
            }
        }
        catch (Exception ex)
        {
            cat.error("Error create instance of SecureRandom", ex);
            throw ex;
        }

        //
        // Check from minimum number of command line arguments.
        //

        inArray = inBytes; // Source of plain text.
        file_out = storeFile; // Destination of data.

        //
        // Check that recipients certificate file exists.
        //

        File cert_file = new File(certificateFile); // Certificate file.

        if (!cert_file.exists()) // Does it _not_ exist.
        {
            cat.debug("Certificate file not found. File " + certificateFile);
            return;
        }

        //
        // Check that key store file exists.
        //

        File keystore_file = new File(keystoreFile);

        if (!keystore_file.exists()) // Does it _not_ exist.
        {
            cat.debug("Keystore file not found. File " + keystoreFile);
            return;
        }

        char[] ks_pass = passwordKS.toCharArray();
        KeyStore key_store = null; // Key Store object.

        try
        {
            key_store = KeyStore.getInstance("JKS", "SUN"); // Create a new instance of the JKS key store.

            //
            // We need to obtain the password for the key store from the user.
            // Use our getLine() method to read it in.
            // Exit program on zero length input.
            // Keep looping until user inputs a password that works.
            //

            //
            // Test password on keystore.
            // KeyStore will break when the password is incorrect and
            // throw an exception.
            //

            try
            {
                key_store.load(new FileInputStream(keystore_file), ks_pass);
            }
            catch (Exception ex)
            {
                cat.error("Error loading keystore " + keystoreFile, ex);
                throw ex;
            }

            //
            // We now need to get an alias for the private key and our certificate
            // that we will use to sign the encrypted file so that the recipient
            // can authenticate us.
            // This uses the same process as described above.

            try
            {
//                    my_private = (PrivateKey) key_store.getKey(l, "qwe321".toCharArray()); // Get private key.
                my_private = (PrivateKey) key_store.getKey(aliasKS, ks_pass); // Get private key.
            }
            catch (Exception ex)
            {
                cat.error("Error get private key / Alias " + aliasKS, ex);
                throw ex;
            }

            try
            {
                my_cert = (X509Certificate) key_store.getCertificate(aliasKS); // Get Signing Certificate
            }
            catch (Exception ex)
            {
                cat.error("Error create x509 certificate", ex);
                throw ex;
            }

            if (my_private == null)
            {
                cat.debug("Private key is null");
                return;
            }


            //
            // Now we need to read in the certificate of the recipient.
            // The certificate contains the public key that the recipient uses for encryption.
            // See javadoc for CertificateFactory for more info..
            //

            cat.debug("Reading recipients certificate file.");

            byte[] raw = new byte[(int) cert_file.length()]; // Create new array
            DataInputStream din = new DataInputStream(new FileInputStream(cert_file)); // Some stream wrapper.

            din.readFully(raw); // Read it all in.
            din.close(); // Close file input.

            ByteArrayInputStream b_in = new ByteArrayInputStream(raw); // Need to use this..

            //
            // Use a certificate factory to convert the raw certificate into a certificate object.
            //

            CertificateFactory cf = CertificateFactory.getInstance("X.509"); // Create certificate factory.
            X509Certificate x_cert = (X509Certificate) cf.generateCertificate(b_in); // Create certificate.
            pub_key = x_cert.getPublicKey();
        }
        catch (Exception ex)
        {
            cat.error("Error init encryption parameters", ex);
            throw ex;
        }
    }

    /**
     * Initialise from command line arguments.
     */
/*
    public void initTerm(String[] args)
    {
        System.out.println("Encrypt file with and use Signature (MD5withRSA) to provide"); // Print statements.
        System.out.println("sender authentication and message integrity checking"); // More print statements.

        sec_rand = null; // Secure random number


        //
        // Add BouncyCastle JCE as a provider and create and seed the random number generator.
        //

        try
        {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Add provider.
            sec_rand = SecureRandom.getInstance("SHA1PRNG", "SUN"); // Create random number generator.

            //
            // Try and seed PRNG from '/dev/urandom' on UNIX machines this device generally exists.
            // if it does not then SecureRandom will employ its own platform independent methods.
            //

            if (new File("/dev/urandom").exists()) // Does device micro random exist.
            {
                System.out.println("Salting SecureRandom (SHA1PRNG) from /dev/urandom");
                byte[] salt = new byte[8192]; // salt.
                new FileInputStream("/dev/urandom").read(salt); // Read salt.
                sec_rand.setSeed(salt); // Set salt.
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        //
        // Check from minimum number of command line arguments.
        //

        if (args.length < 4)
        {
            System.out.println("Usage: <source file> <dest file> <recipients certificate> <your trust store>");
            System.exit(0);
        }

        inArray = new File(args[0]); // Source of plain text.
        file_out = new File(args[1]); // Destination of data.

        //
        // Check that source file exists.
        //

        if (!inArray.exists())
        {
            System.out.println("Source File does not exist..!");
            System.exit(0);
        }

        //
        // Check that recipients certificate file exists.
        //

        File cert_file = new File(args[2]); // Certificate file.

        if (!cert_file.exists()) // Does it _not_ exist.
        {
            System.out.println("Recipients Certificate File does not exist..!");
            System.exit(0);
        }

        //
        // Check that key store file exists.
        //

        File keystore_file = new File(args[3]);

        if (!keystore_file.exists()) // Does it _not_ exist.
        {
            System.out.println("Keystore file specified could not be found.");
            System.exit(0);
        }

        char[] ks_pass = null; // Keystore password.
        boolean stop = false; // Stop flag.
        KeyStore key_store = null; // Key Store object.

        try
        {
            key_store = KeyStore.getInstance("JKS", "SUN"); // Create a new instance of the JKS key store.

            //
            // We need to obtain the password for the key store from the user.
            // Use our getLine() method to read it in.
            // Exit program on zero length input.
            // Keep looping until user inputs a password that works.
            //

            while (!stop)
            {
                System.out.println("");
                System.out.print("Your Key Store Password > ");
                String l = getLine(); // Read line.

                if (l == null) // Null entry.
                {
                    System.exit(0);
                }

                if (l.length() == 0) // Zero length.
                {
                    System.exit(0);
                }

                ks_pass = l.toCharArray(); // Need to convert to char[] .

                //
                // Test password on keystore.
                // KeyStore will break when the password is incorrect and
                // throw an exception.
                //

                try
                {
                    key_store.load(new FileInputStream(keystore_file), ks_pass);
                    stop = true; // it worked so stop looping..
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

            stop = false; // Reset stop flag.


            //
            // We now need to get an alias for the private key and our certificate
            // that we will use to sign the encrypted file so that the recipient
            // can authenticate us.
            // This uses the same process as described above.

            while (!stop)
            {
                System.out.println("");
                System.out.print("Secret Key Alias for Signing only use > ");
                String l = getLine();
                if (l == null)
                {
                    System.exit(0);
                }

                if (l.length() == 0)
                {
                    System.exit(0);
                }

                System.out.println("#1.000 " + l + " " + new String(ks_pass));
                try
                {
//                    my_private = (PrivateKey) key_store.getKey(l, "qwe321".toCharArray()); // Get private key.
                    my_private = (PrivateKey) key_store.getKey(l, ks_pass); // Get private key.
                    stop = true;
                }
                catch (Exception ex)
                {
                    System.out.println("#1.001 " + ex.toString());
                }

                try
                {
                    my_cert = (X509Certificate) key_store.getCertificate(l); // Get Signing Certificate
                    stop = true;
                }
                catch (Exception ex)
                {
                    System.out.println("#1.002 " + ex.getMessage());
                }
                if (my_private == null)
                {
                    System.out.println("Private key is null");
                    System.exit(0);
                }

            }


            //
            // Now we need to read in the certificate of the recipient.
            // The certificate contains the public key that the recipient uses for encryption.
            // See javadoc for CertificateFactory for more info..
            //

            System.out.println("Reading recipients certificate file.");

            byte[] raw = new byte[(int) cert_file.length()]; // Create new array
            DataInputStream din = new DataInputStream(new FileInputStream(cert_file)); // Some stream wrapper.

            din.readFully(raw); // Read it all in.
            din.close(); // Close file input.

            ByteArrayInputStream b_in = new ByteArrayInputStream(raw); // Need to use this..

            //
            // Use a certificate factory to convert the raw certificate into a certificate object.
            //

            CertificateFactory cf = CertificateFactory.getInstance("X.509"); // Create certificate factory.
            X509Certificate x_cert = (X509Certificate) cf.generateCertificate(b_in); // Create certificate.
            pub_key = x_cert.getPublicKey();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
*/
    /**
     * Encrpyt file.
     */
    public void encryptFileSIG() throws Exception
    {
        EncryptSignature.processFile(new ByteArrayInputStream(inArray), pub_key, my_cert, my_private, sec_rand, file_out);
    }

    /**
     * Read in line from System.in.
     */
/*
    private static String getLine()
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
}
