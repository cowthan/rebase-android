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

import android.util.Log;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

/**
 * @author drakeet
 */
public class Keys {

    private static final String TAG = Keys.class.getSimpleName();
    public static final String ALIAS = "rebase";


    public static boolean createKeys(BlackBox box) {
        try {
            box.createKeys();
            Log.d(TAG, "Keys created");
            return true;
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (InvalidAlgorithmParameterException e) {
            Log.w(TAG, "No such provider: AndroidKeyStore");
        } catch (NoSuchProviderException e) {
            Log.w(TAG, "Invalid Algorithm Parameter Exception", e);
        }
        return false;
    }


    public static String signData(BlackBox box, String input) {
        String result = null;
        try {
            result = box.signData(input);
        } catch (KeyStoreException e) {
            Log.w(TAG, "KeyStore not Initialized", e);
        } catch (UnrecoverableEntryException e) {
            Log.w(TAG, "KeyPair not recovered", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (InvalidKeyException e) {
            Log.w(TAG, "Invalid Key", e);
        } catch (SignatureException e) {
            Log.w(TAG, "Invalid Signature", e);
        } catch (IOException e) {
            Log.w(TAG, "IO Exception", e);
        } catch (CertificateException e) {
            Log.w(TAG, "Error occurred while loading certificates", e);
        }
        Log.d(TAG, "Signature: " + result);
        return result;
    }


    public static boolean verifyData(BlackBox box, String input, String signed) {
        boolean verified = false;
        try {
            if (signed != null) {
                verified = box.verifyData(input, signed);
            }
        } catch (KeyStoreException e) {
            Log.w(TAG, "KeyStore not Initialized", e);
        } catch (CertificateException e) {
            Log.w(TAG, "Error occurred while loading certificates", e);
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "RSA not supported", e);
        } catch (IOException e) {
            Log.w(TAG, "IO Exception", e);
        } catch (UnrecoverableEntryException e) {
            Log.w(TAG, "KeyPair not recovered", e);
        } catch (InvalidKeyException e) {
            Log.w(TAG, "Invalid Key", e);
        } catch (SignatureException e) {
            Log.w(TAG, "Invalid Signature", e);
        }
        if (verified) {
            Log.d(TAG, "Data Signature Verified");
        } else {
            Log.d(TAG, "Data not verified.");
        }
        return verified;
    }
}
