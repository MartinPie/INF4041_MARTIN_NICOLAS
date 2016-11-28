package org.esiea.martin_nicolas.projetmobiles3;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JsonPullService extends IntentService {


    public static final String[] URLS = {"urlGlass", "urlCategory", "urlIngredient", "urlName", "url"};
    public static final String[] SOURCE_URLS = {"urlGlassSource", "urlCategorySource", "urlIngredientSource", "urlNameSource", "urlSource"};

    public JsonPullService() {
        super("JsonPullService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent broadcastIntent = new Intent();

        for (int i = 0; i < URLS.length; i++) {


            String urlPath = intent.getStringExtra(URLS[i]);

            if (urlPath != null) {
                InputStream is = null;
                BufferedReader r = null;
                StringBuilder result = null;
                // on récupère les données depuis l'url
                try {
                    java.net.URL aURL = new URL(urlPath);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    is = conn.getInputStream();
                    r = new BufferedReader(new InputStreamReader(is));
                    result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                } catch (IOException e) {
                    // message d'erreur

                } finally {
                    // on ferme bien tous les flux
                    if (r != null) {
                        try {
                            r.close();
                        } catch (IOException e) {
                            // message d'erreur
                        }
                    }
                }

                // maintenant on transmet le résultat
                broadcastIntent.setAction(MainActivity.MyReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(SOURCE_URLS[i], result.toString());
            }
        }
        //on envoie le broadcast
        sendBroadcast(broadcastIntent);

    }
}
