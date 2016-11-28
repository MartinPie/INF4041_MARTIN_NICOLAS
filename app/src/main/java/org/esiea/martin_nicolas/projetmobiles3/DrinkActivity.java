package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

/***
 * Activité affichant les détails d'un cocktail
 */
public class DrinkActivity extends AppCompatActivity implements HttpJsonRequest.OnGetJsonListener {

    public Drink drink;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drink);

            Intent intent = getIntent();

            //Récupération des éléments
            this.recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
            this.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            this.recyclerView.setLayoutManager(layoutManager);

            if (intent != null) {
                int drinkId = intent.getIntExtra("drink_id", 0);

                URL url;

                //Si l'id est égal à 0, l'utilisateur a cliqué sur "Random", on utilise donc la fonction de l'api
                if (drinkId > 0)
                    url = new URL("http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + drinkId);
                else
                    url = new URL("http://www.thecocktaildb.com/api/json/v1/1/random.php");

                HttpJsonRequest h = new HttpJsonRequest(this, this);
                h.execute(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * ici on récupère le json de l'objet et on le transforme avec le constructeur de Drink
     * (on met le isShort a false car on veut toutes les informations)
     *
     * @param jsonObject objet json du cocktail
     */
    public void OnGetJson(JSONObject jsonObject) {
        try {
            JSONObject drinkObj = new JSONObject(jsonObject.getJSONArray("drinks").getString(0));

            this.drink = new Drink(drinkObj, false);

            this.initView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initView() {
        //Petit toast avec le nom du cocktail
        Toast.makeText(this, this.drink.getName(), Toast.LENGTH_SHORT).show();

        ImageView img = (ImageView) findViewById(R.id.detail_img_drink);

        //Bouton servant à aller sur google pour chercher où trouver le cocktail
        Button button = (Button) findViewById(R.id.btn_internet_drink);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_prefif = getString(R.string.url_prefix_recherche);

                String url = url_prefif + " " + drink.getName();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        TextView nameView = (TextView) findViewById(R.id.detail_name);
        TextView categoryView = (TextView) findViewById(R.id.detail_category);
        TextView alcoholicView = (TextView) findViewById(R.id.detail_alcoholic);
        TextView glassView = (TextView) findViewById(R.id.detail_glass);
        TextView instructionView = (TextView) findViewById(R.id.detail_instruction);

        //Picasso servant à afficher l'image
        Picasso.with(this).load(this.drink.getImgageUrl()).into(img);

        nameView.setText(this.drink.getName());
        categoryView.setText(this.drink.getCategory());
        alcoholicView.setText(this.drink.getAlcoholic());
        glassView.setText(this.drink.getGlass());
        instructionView.setText(this.drink.getInstruction());

        //Adapter servant pour les ingrédients
        IngredientDataAdapter adapter = new IngredientDataAdapter(getApplicationContext(), this.drink.getIngredients());
        this.recyclerView.setAdapter(adapter);
    }
}
