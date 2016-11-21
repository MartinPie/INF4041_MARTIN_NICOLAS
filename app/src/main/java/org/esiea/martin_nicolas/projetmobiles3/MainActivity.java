package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements DrinkDataAdapter.OnDrinkClickListener, HttpJsonRequest.OnGetJsonListener {

    private RecyclerView recyclerView;
    private ArrayList<Drink> drinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            drinks = new ArrayList<>();

            this.recyclerView = (RecyclerView) findViewById(R.id.drinks_recycler_view);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Toast.makeText(this, getResources().getString(R.string.menu_refresh), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_random:
                Toast.makeText(this, getResources().getString(R.string.menu_random), Toast.LENGTH_SHORT).show();
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
                DrinkDataAdapter  adapter = new DrinkDataAdapter(getApplicationContext(), this.drinks, this);
                this.recyclerView.setAdapter(adapter);
            } else {
                for (Drink drink : tempDrinks) {
                    if (!this.drinks.contains(drink)) {
                        this.drinks.remove(drink);
                    }
                }
                this.recyclerView.getAdapter().notifyDataSetChanged();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
