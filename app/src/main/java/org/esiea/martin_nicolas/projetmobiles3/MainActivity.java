package org.esiea.martin_nicolas.projetmobiles3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements DrinkDataAdapter.OnDrinkClickListener{

    private MyReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotification();

        drinks = new ArrayList<>();

        this.recyclerView = (RecyclerView) findViewById(R.id.drinks_recycler_view);
        this.recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        this.recyclerView.setLayoutManager(layoutManager);

        // on initialise notre broadcast
        receiver = new MyReceiver();
        // on lance le service
        Intent jsonIntent = new Intent(this, JsonPullService.class);

        Intent intent = getIntent();

        String category = intent .getStringExtra("category");
        String glass = intent .getStringExtra("glass");
        String ingredient = intent .getStringExtra("ingredient");
        String name = intent .getStringExtra("name");


        if (glass != null || category != null || ingredient != null) {

            if (name != null) {
                jsonIntent.putExtra(JsonPullService.URLS[3], "http://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name.replace(" ","_"));
            }
            if (!glass.equals(getResources().getString(R.string.glass))) {
                jsonIntent.putExtra(JsonPullService.URLS[0], "http://www.thecocktaildb.com/api/json/v1/1/filter.php?g=" + glass.replace(" ", "_"));
            }
            if (!category.equals(getResources().getString(R.string.category))) {
                jsonIntent.putExtra(JsonPullService.URLS[1], "http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=" + category.replace(" ", "_"));
            }
            if (!ingredient.equals(getResources().getString(R.string.ingredient))) {
                jsonIntent.putExtra(JsonPullService.URLS[2], "http://www.thecocktaildb.com/api/json/v1/1/filter.php?i=" + ingredient.replace(" ", "_"));
            }

        } else {
            jsonIntent.putExtra(JsonPullService.URLS[4], "http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic");
        }
        startService(jsonIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // on déclare notre Broadcast Receiver
        IntentFilter filter = new IntentFilter(MyReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // on désenregistre notre broadcast
        unregisterReceiver(receiver);
    }

    private RecyclerView recyclerView;
    private ArrayList<Drink> drinks;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            drinks = new ArrayList<>();

            this.recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
            this.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            this.recyclerView.setLayoutManager(layoutManager);

            Intent intent = getIntent();

            String category = intent.getStringExtra("category");
            String glass = intent.getStringExtra("glass");
            String ingredient = intent.getStringExtra("ingredient");
            String name = intent.getStringExtra("name");


            if (glass != null || category != null || ingredient != null) {

                if (!glass.equals(getResources().getString(R.string.glass))) {
                    URL urlGlass = new URL("http://www.thecocktaildb.com/api/json/v1/1/filter.php?g=" + glass.replace(" ", "_"));
                    HttpJsonRequest hGlass = new HttpJsonRequest(this, this);
                    hGlass.execute(urlGlass);
                }

                if (!category.equals(getResources().getString(R.string.category))) {
                    URL urlCategory = new URL("http://www.thecocktaildb.com/api/json/v1/1/filter.php?c=" + category.replace(" ", "_"));
                    HttpJsonRequest hCategory = new HttpJsonRequest(this, this);
                    hCategory.execute(urlCategory);
                }
                if (!ingredient.equals(getResources().getString(R.string.ingredient))) {
                    URL urlIngredient = new URL("http://www.thecocktaildb.com/api/json/v1/1/filter.php?i=" + ingredient.replace(" ", "_"));
                    HttpJsonRequest hIngredient = new HttpJsonRequest(this, this);
                    hIngredient.execute(urlIngredient);
                }

            } else {

                URL url = new URL("http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic");

                HttpJsonRequest h = new HttpJsonRequest(this, this);
                h.execute(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_random:
                intent = new Intent(MainActivity.this, DrinkActivity.class);
                intent.putExtra("drink_id", 0);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onItemClick(Drink item) {
        Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
        intent.putExtra("drink_id", item.getId());
        startActivity(intent);
    }


    public void OnGetJson(JSONObject jsonObject) {

        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = jsonObject.getJSONArray("drinks");

            ArrayList<Drink> tempDrinks = new ArrayList<Drink>();

            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                // Pour tous les objets on récupère les infos
                tempDrinks.add(new Drink(obj, true));
            }

            if (this.drinks.isEmpty()) {
                this.drinks = tempDrinks;
                DrinkDataAdapter adapter = new DrinkDataAdapter(getApplicationContext(), this.drinks, this);
                this.recyclerView.setAdapter(adapter);
            } else {
                for (int i = 0; i < this.drinks.size(); i++) {
                    Drink drink = this.drinks.get(i);

                    if (!tempDrinks.contains(drink)) {
                        this.recyclerView.removeViewAt(i);
                        this.recyclerView.getAdapter().notifyItemRemoved(i);
                        this.recyclerView.getAdapter().notifyItemRangeChanged(i, this.drinks.size());
                        this.recyclerView.getAdapter().notifyDataSetChanged();
                        this.drinks.remove(drink);
                        i--;
                    }
                }

//                this.recyclerView.removeAllViews();
//                ((DrinkDataAdapter)this.recyclerView.getAdapter()).setDrinks(this.drinks);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        public static final String ACTION_RESP = "filter";

        @Override
        public void onReceive(Context context, Intent intent) {


            for (int i = 0; i < JsonPullService.SOURCE_URLS.length; i++) {
                String text = intent.getStringExtra(JsonPullService.SOURCE_URLS[i]);
                if (text != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        OnGetJson(jsonObject);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private final void createNotification(){
        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        final Intent launchNotifiactionIntent = new Intent(this, AboutActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotifiactionIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.about)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_desc))
                .setContentIntent(pendingIntent);

        mNotification.notify(0, builder.build());
    }

}
