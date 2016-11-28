package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements HttpJsonRequest.OnGetJsonListener, View.OnClickListener {

    Spinner searchCategory;
    Spinner searchIngredient;
    Spinner searchGlass;
    EditText searchName;
    Button btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Récupération des éléments
        searchCategory = (Spinner) findViewById(R.id.searchCategory);
        searchGlass = (Spinner) findViewById(R.id.searchGlass);
        searchIngredient = (Spinner) findViewById(R.id.searchIngredient);
        btnsubmit = (Button) findViewById(R.id.submit);
        searchName = (EditText) findViewById(R.id.searchName);


        btnsubmit.setOnClickListener(this);

        //Récupération des listes catégorie, verres et ingrédients à partir de l'api
        try {
            URL urlCategory = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");
            URL urlGlass = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?g=list");
            URL urlIngredient = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?i=list");

            HttpJsonRequest jsonCategory = new HttpJsonRequest(this, this);
            jsonCategory.execute(urlCategory);
            HttpJsonRequest jsonGlass = new HttpJsonRequest(this, this);
            jsonGlass.execute(urlGlass);
            HttpJsonRequest jsonIngredient = new HttpJsonRequest(this, this);
            jsonIngredient.execute(urlIngredient);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     *
     * @param jsonObject json de la liste récupérée
     */
    public void OnGetJson(JSONObject jsonObject) {

        ArrayList<String> list = new ArrayList<>();
        String typeList = "";

        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = jsonObject.getJSONArray("drinks");

            //On teste de quelle liste il s'agit (catégorie, verre ou ingrédient)
            if (array.length() > 0) {

                if (!new JSONObject(array.getString(0)).isNull("strCategory")) {
                    typeList = "strCategory";
                    list.add(getResources().getString(R.string.category));
                }
                if (!new JSONObject(array.getString(0)).isNull("strGlass")) {
                    typeList = "strGlass";
                    list.add(getResources().getString(R.string.glass));
                }
                if (!new JSONObject(array.getString(0)).isNull("strIngredient1")) {
                    typeList = "strIngredient1";
                    list.add(getResources().getString(R.string.ingredient));
                }

                // Pour tous les objets on récupère les infos
                if (!"".equals(typeList)) {
                    for (int i = 0; i < array.length(); i++) {
                        // On récupère un objet JSON du tableau
                        JSONObject obj = new JSONObject(array.getString(i));

                        list.add(obj.getString(typeList));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    //On crée un adapter avec la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //On Attribue la liste avec le spinner correspondant
        switch (typeList) {
            case "strCategory":
                searchCategory.setAdapter(adapter);
                break;

            case "strGlass":
                searchGlass.setAdapter(adapter);
                break;

            case "strIngredient1":
                searchIngredient.setAdapter(adapter);
                break;
        }
    }

    /***
     * Action du click sur le bouton Submit
     *
     */
    public void onClick(View v) {

        HashMap<String, String> results = new HashMap<String, String>();

        Intent intent = new Intent(SearchActivity.this, MainActivity.class);

        //On envoie toutes les infos récupérées
        intent.putExtra("category", searchCategory.getSelectedItem().toString());
        intent.putExtra("glass", searchGlass.getSelectedItem().toString());
        intent.putExtra("ingredient", searchIngredient.getSelectedItem().toString());
        intent.putExtra("name", searchName.getText().toString());

        startActivity(intent);
    }
}
