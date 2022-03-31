package com.example.bonbon.data_management;

import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class Encryption {
    public static String encryptUsername(String UID) {
        String result = "";
        /*
         * get UID and hash it
         * append reversed UID to hash
         * hash the resulting string
         * */
        System.out.println(UID);
        String hash1 = Hashing.sha256().hashString(UID, Charset.defaultCharset()).toString();
        System.out.println(hash1);
        String hashWithReverse = hash1 + reverse(UID);
        System.out.println(hashWithReverse);
        result = Hashing.sha256().hashString(hashWithReverse, Charset.defaultCharset()).toString();
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

    public String generateEncryptionKey() {
        String key = "";

        return key;
    }

    public String encryptStringData(String data){
        String result = "";

        return result;
    }

    public static String encryptPassword(String s){
        // Hash and salt input then return it
        return s;
    }
}
