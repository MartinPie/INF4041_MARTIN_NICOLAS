package org.esiea.martin_nicolas.projetmobiles3;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamOperations {
    /**
     * @param in      : le buffer
     * @param bufSize : taille du buffer
     * @return : la chaine correspondant au buffer
     */
    public static String InputStreamToString(InputStream in, int bufSize) {
        final StringBuilder out = new StringBuilder();
        final byte[] buffer = new byte[bufSize];
        try {
            for (int ctr; (ctr = in.read(buffer)) != -1; ) {
                out.append(new String(buffer, 0, ctr));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot convert stream to string", e);
        }
        // On retourne la chaine contenant les donnees de l'InputStream
        return out.toString();
    }

    /**
     * @param in : le buffer
     * @return : la chaine correspondant au buffer
     */
    public static String InputStreamToString(InputStream in) {
        // On appelle la methode precedente avec une taille de buffer par defaut
        return InputStreamToString(in, 1024);
    }
}
