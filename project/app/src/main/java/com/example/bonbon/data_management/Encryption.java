package com.example.bonbon.data_management;

public class Encryption {
    public String encryptUsername(String UID) {
        String result = "";
        /*
         * get UID and hash it
         * append reversed UID to hash
         * hash the resulting string
         * */
        return result;
    }

    private String reverse(String s) {
        StringBuilder result = new StringBuilder();

        for (int i = s.length(); i > 0; i--) {
            result.append(s.charAt(i));
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
