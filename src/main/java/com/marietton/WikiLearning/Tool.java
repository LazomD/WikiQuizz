package com.marietton.WikiLearning;

/**
 * Created by lyon on 15/07/2017.
 */
public class Tool {

    public static long calculerTempsAajouter(String delai) {
        long retour = 0;
        if ("1j".equals(delai)) {
            retour = 86400;
        } else if ("3j".equals(delai)) {
            retour = 259200;
        } else if ("1s".equals(delai)) {
            retour = 604800;
        } else if ("2s".equals(delai)) {
            retour = 1209600;
        } else if ("1m".equals(delai)) {
            retour = 2592000;
        } else if ("3m".equals(delai)) {
            retour = 7776000;
        } else if ("n".equals(delai)) {
            retour = 9999999999L;
        }
        return retour;
    }

    public static long calculerIdentifiantDelai(String delai) {
        int retour = 0;
        if ("1j".equals(delai)) {
            retour = 1;
        } else if ("3j".equals(delai)) {
            retour = 2;
        } else if ("1s".equals(delai)) {
            retour = 3;
        } else if ("2s".equals(delai)) {
            retour = 4;
        } else if ("1m".equals(delai)) {
            retour = 5;
        } else if ("3m".equals(delai)) {
            retour = 6;
        } else if ("n".equals(delai)) {
            retour = 7;
        }
        return retour;
    }
}
