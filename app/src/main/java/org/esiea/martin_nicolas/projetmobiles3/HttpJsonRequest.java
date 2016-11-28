package org.esiea.martin_nicolas.projetmobiles3;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpJsonRequest extends AsyncTask<URL, Integer, Void> {

    private OnGetJsonListener listener;
    private Activity activity;

    public HttpJsonRequest(OnGetJsonListener listener, Activity activity) {
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(URL... arg0) {

        final ArrayList<Drink> drinks = new ArrayList<Drink>();

        try {
            URL url = arg0[0];
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            String result = InputStreamOperations.InputStreamToString(inputStream);

            final JSONObject jsonObject = new JSONObject(result);

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    listener.OnGetJson(jsonObject);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public interface OnGetJsonListener {
        void OnGetJson(JSONObject jsonObject);
    }
}
