/*
 * Copyright (C) 2017 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of rebase-android
 *
 * rebase-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rebase-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rebase-android. If not, see <http://www.gnu.org/licenses/>.
 */

package com.drakeet.rebase.tool;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.security.auth.x500.X500Principal;

/**
 * Enhanced by drakeet
 */
public class BlackBox {

    static final String TAG = BlackBox.class.getSimpleName();

    final String alias;
    final Context context;


    public BlackBox(Context context, String alias) {
        this.context = context;
        this.alias = alias;
    }


    public static class SecurityConstants {

        public static final String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";

        public static final String TYPE_RSA = "RSA";
        public static final String TYPE_DSA = "DSA";
        public static final String TYPE_BKS = "BKS";

        public static final String SIGNATURE_SHA256withRSA = "SHA256withRSA";
        public static final String SIGNATURE_SHA512withRSA = "SHA512withRSA";
    }


    /**
     * Creates a public and private key and stores it using the Android Key
     * Store, so that only this application will be able to access the keys.
     */
    @SuppressWarnings("deprecation")
    public void createKeys() throws NoSuchProviderException,
                                    NoSuchAlgorithmException,
                                    InvalidAlgorithmParameterException {

        // Create a start and end time, for the validity range of the key pair
        // that's about to be generated.
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 1);

        // The KeyPairGeneratorSpec object is how parameters for your key pair
        // are passed to the KeyPairGenerator.
        // For a fun home game, count how many classes in this sample
        // start with the phrase "KeyPair".
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
            // You'll use the alias later to retrieve the key. It's a key
            // for the key!
            .setAlias(alias)
            // The subject used for the self-signed certificate of the
            // generated pair
            .setSubject(new X500Principal("CN=" + alias))
            // The serial number used for the self-signed certificate of the
            // generated pair.
            .setSerialNumber(BigInteger.valueOf(383838))
            // Date range of validity for the generated pair.
            .setStartDate(start.getTime()).setEndDate(end.getTime())
            .build();

        // Initialize a KeyPair generator using the the intended algorithm (in
        // this example, RSA and the KeyStore. This example uses the AndroidKeyStore.
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
            SecurityConstants.TYPE_RSA,
            SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        keyPairGenerator.initialize(spec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Log.d(TAG, "Public Key is: " + keyPair.getPublic().toString());
    }


    /**
     * Signs the data using the key pair stored in the Android Key Store. This
     * signature can be used with the data later to verify it was signed by this
     * application.
     *
     * @return A string encoding of the data signature generated
     */
    public String signData(String input) throws KeyStoreException,
                                                UnrecoverableEntryException,
                                                NoSuchAlgorithmException,
                                                InvalidKeyException, SignatureException,
                                                IOException,
                                                CertificateException {
        byte[] data = input.getBytes();

        KeyStore keyStore = KeyStore
            .getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);

        // Weird artifact of Java API. If you don't have an InputStream to load,
        // you still need to call "load", or it'll crash.
        keyStore.load(null);

        // Load the key pair from the Android Key Store
        KeyStore.Entry entry = keyStore.getEntry(alias, null);

		/*
         * If the entry is null, keys were never stored under this alias. Debug
		 * steps in this situation would be: -Check the list of aliases by
		 * iterating over Keystore.aliases(), be sure the alias exists. -If
		 * that's empty, verify they were both stored and pulled from the same
		 * keystore "AndroidKeyStore"
		 */
        if (entry == null) {
            Log.w(TAG, "No key found under alias: " + alias);
            Log.w(TAG, "Exiting signData()...");
            return null;
        }

		/*
         * If entry is not a KeyStore.PrivateKeyEntry, it might have gotten
		 * stored in a previous iteration of your application that was using
		 * some other mechanism, or been overwritten by something else using the
		 * same keystore with the same alias. You can determine the type using
		 * entry.getClass() and debug from there.
		 */
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w(TAG, "Not an instance of a PrivateKeyEntry");
            Log.w(TAG, "Exiting signData()...");
            return null;
        }

        // This class doesn't actually represent the signature,
        // just the engine for creating/verifying signatures, using
        // the specified algorithm.
        Signature sign = Signature
            .getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);

        // Initialize Signature using specified private key
        sign.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());

        // Sign the data, store the result as a Base64 encoded String.
        sign.update(data);
        byte[] signature = sign.sign();
        String result = null;
        result = Base64.encodeToString(signature, Base64.DEFAULT);

        return result;
    }


    /**
     * Given some data and a signature, uses the key pair stored in the Android
     * Key Store to verify that the data was signed by this application, using
     * that key pair.
     *
     * @param input The data to be verified.
     * @param signed The signature provided for the data.
     * @return A boolean value telling you whether the signature is valid or
     * not.
     */
    public boolean verifyData(String input, String signed)
        throws KeyStoreException, CertificateException,
               NoSuchAlgorithmException, IOException, UnrecoverableEntryException,
               InvalidKeyException, SignatureException {
        byte[] data = input.getBytes();
        byte[] signature;

        // Make sure the signature string exists. If not, bail out, nothing to
        // do.

        if (signed == null) {
            Log.w(TAG, "Invalid signature.");
            Log.w(TAG, "Exiting verifyData()...");
            return false;
        }

        try {
            // The signature is going to be examined as a byte array,
            // not as a base64 encoded string.
            signature = Base64.decode(signed, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            // signatureStr wasn't null, but might not have been encoded
            // properly.
            // It's not a valid Base64 string.
            return false;
        }

        KeyStore keyStore = KeyStore.getInstance(
            SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);

        // Weird artifact of Java API. If you don't have an InputStream to load,
        // you still need
        // to call "load", or it'll crash.
        keyStore.load(null);

        // Load the key pair from the Android Key Store
        KeyStore.Entry entry = keyStore.getEntry(alias, null);

        if (entry == null) {
            Log.w(TAG, "No key found under alias: " + alias);
            Log.w(TAG, "Exiting verifyData()...");
            return false;
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w(TAG, "Not an instance of a PrivateKeyEntry");
            return false;
        }

        // This class doesn't actually represent the signature,
        // just the engine for creating/verifying signatures, using
        // the specified algorithm.
        Signature sign = Signature
            .getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);

        // Verify the data.
        sign.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        sign.update(data);
        return sign.verify(signature);
    }
}
