package com.example.bonbon.data_management;


import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {
    private static String secret;
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void _setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptStringData(final String strToEncrypt) {
        System.out.println("encrypting");
        try {
            _setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptStringData(final String strToDecrypt) {
        System.out.println("decrypting");
        try {
            _setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static String oneWayEncrypt(String UID) {
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
        String hashWithReverse = hash1 + invert(UID);
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
    private static String invert(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result = s.charAt(i) + result;
        }
        return result;
    }

    public static String generateEncryptionKey() {
        // To generate 128 bit string 16 characters are required.
        String allChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String key = "";

        // Create a random number generator and seed it
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < 16; i++) {
            key += allChars.charAt(rnd.nextInt(allChars.length()));
        }
        return key;
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

    public static void setKey(String _key) {
        secret = _key;
    }

}
