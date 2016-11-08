package org.esiea.martin_nicolas.projetmobiles3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            URL url = new URL("http://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita");

            HttpJsonRequest h = new HttpJsonRequest();
            h.execute(url);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private class HttpJsonRequest extends AsyncTask<URL, Integer, Void> {
        @Override
        protected Void doInBackground(URL... arg0) {

            ArrayList<Drink> drinks = new ArrayList<Drink>();

            try {
                URL url = arg0[0];
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                String result = InputStreamOperations.InputStreamToString(inputStream);

                // On récupère le JSON complet
                JSONObject jsonObject = new JSONObject(result);
                // On récupère le tableau d'objets qui nous concernent
                JSONArray array = new JSONArray(jsonObject.getString("drinks"));

                // Pour tous les objets on récupère les infos
                for (int i = 0; i < array.length(); i++) {
                    // On récupère un objet JSON du tableau
                    JSONObject obj = new JSONObject(array.getString(i));

                    drinks.add(new Drink(obj, true));
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }
    }
}
