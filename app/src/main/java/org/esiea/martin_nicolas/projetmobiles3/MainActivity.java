package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DrinkDataAdapter.OnDrinkClickListener, HttpJsonRequest.OnGetJsonListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            this.recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
            this.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
            this.recyclerView.setLayoutManager(layoutManager);

            URL url = new URL("http://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic");

            HttpJsonRequest h = new HttpJsonRequest(this, this);
            h.execute(url);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onItemClick(Drink item) {
        Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
        intent.putExtra("drink_id", item.getId());
        startActivity(intent);
    }

    public void OnGetJson(JSONObject jsonObject) {

        ArrayList<Drink> drinks = new ArrayList<>();

        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = jsonObject.getJSONArray("drinks");

            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));

                drinks.add(new Drink(obj, true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrinkDataAdapter adapter = new DrinkDataAdapter(getApplicationContext(), drinks, this);
        this.recyclerView.setAdapter(adapter);
    }
}
