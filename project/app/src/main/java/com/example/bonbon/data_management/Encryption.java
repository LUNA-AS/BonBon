package com.example.bonbon.data_management;

import androidx.annotation.NonNull;


// TODO resolve tink library issues
//import com.google.crypto.tink.KeyTemplates;
//import com.google.crypto.tink.KeysetHandle;
//import com.google.crypto.tink.aead.AeadConfig;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {
    public static String encryptUsername(String UID) {
        String result = "";
        /*
         * get UID and hash it
         * append reversed UID to hash
         * hash the resulting string
         * */
        System.out.println(UID);
        String hash1 = null;
        try {
            hash1 = bytesToHex(MessageDigest.getInstance("SHA-256").digest(UID.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hash1 = "";
        }
        System.out.println(hash1);
        String hashWithReverse = hash1 + reverse(UID);
        System.out.println(hashWithReverse);
        try {
            result = bytesToHex(MessageDigest.getInstance("SHA-256").digest(hashWithReverse.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            result = "";
        }
        System.out.println(result);
        return result;
    }

    @NonNull
    private static String reverse(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result = s.charAt(i) + result;
        }
        return result.toString();
    }

    public static String generateEncryptionKey() {
        String key = "";
        try {
            //AeadConfig.register();
            //KeysetHandle handle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
            //key = handle.toString();

            // generate a random string
            long l = System.currentTimeMillis();
            encryptUsername(String.valueOf(l));

        } catch (Exception e) {
            // TODO add logging
            System.out.println(e.getMessage());
            e.printStackTrace();
            return key;
        }
        return key;
    }

    public static String encryptStringData(String data) {
        // TODO add encryption logic
        return data;
    }

    public static String decryptStringData(String data){
        // TODO add decryption logic
        return data;
    }

    public static String encryptPassword(String s) {
        // Hash and salt input then return it
        return s;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
