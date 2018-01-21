package ru.home.sbertest.my.util;

public abstract class Delimiter {

    private static final String DELIMITER_DEFAULT = "\t";

    public static String get(String s) {
        if(s == null || s.isEmpty()){
            return DELIMITER_DEFAULT;
        }
        return s.replace("\\s", " ").replace("\\t", "    ");
    }
}
