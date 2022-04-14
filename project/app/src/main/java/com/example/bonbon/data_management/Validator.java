package com.example.bonbon.data_management;


import android.os.Build;


public class Validator {
    private static final String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "_", "|", ","};
    private static final String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final String[] upper = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final String[] lower = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z"};

    public static boolean checkPasswordIsStrong(String password) {
        if (password.length() > 5) {
            for (String num : numbers) {
                if (password.contains(num)) {
                    for (String special : specialChars) {
                        if (password.contains(special)) {
                            for (String upperLetter : upper) {
                                if (password.contains(upperLetter)) {
                                    for (String lowerLetter : lower) {
                                        if (password.contains(lowerLetter)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
