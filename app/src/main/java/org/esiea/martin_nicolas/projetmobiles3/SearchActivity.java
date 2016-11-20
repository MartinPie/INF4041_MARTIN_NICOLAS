package org.esiea.martin_nicolas.projetmobiles3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements HttpJsonRequest.OnGetJsonListener, View.OnClickListener {

    Spinner searchCategory;
    Spinner searchIngredient;
    Spinner searchGlass;
    Button btnsubmit;

    String scategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchCategory = (Spinner) findViewById(R.id.searchCategory);
        searchGlass = (Spinner) findViewById(R.id.searchGlass);
        searchIngredient = (Spinner) findViewById(R.id.searchIngredient);
        btnsubmit = (Button) findViewById(R.id.submit);

       /* searchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                scategory = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        */

        try {
            URL urlCategory = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");
            URL urlGlass = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?g=list");
            URL urlIngredient = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?i=list");
            //URL urlAlcoholic = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?a=list");


            HttpJsonRequest jsonCategory = new HttpJsonRequest(this, this);
            jsonCategory.execute(urlCategory);
            HttpJsonRequest jsonGlass = new HttpJsonRequest(this, this);
            jsonGlass.execute(urlGlass);
            HttpJsonRequest jsonIngredient = new HttpJsonRequest(this, this);
            jsonIngredient.execute(urlIngredient);
            //HttpJsonRequest jsonAlcoholic = new HttpJsonRequest(this, this);
            //jsonAlcoholic.execute(urlAlcoholic);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void OnGetJson(JSONObject jsonObject) {

        ArrayList<String> list = new ArrayList<>();
        String typeList= "";

        try {
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = jsonObject.getJSONArray("drinks");

            if (array.length() > 0) {

                if(!new JSONObject(array.getString(0)).isNull("strCategory")){
                    typeList = "strCategory";
                    list.add(getResources().getString(R.string.category));
                }
                if(!new JSONObject(array.getString(0)).isNull("strGlass")){
                    typeList = "strGlass";
                    list.add(getResources().getString(R.string.glass));
                }
                if(!new JSONObject(array.getString(0)).isNull("strIngredient1")){
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
        }catch(Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        switch (typeList){
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

    public void onClick(View v) {

       // Toast.makeText(this, "qdsqsd", Toast.LENGTH_SHORT).show();
    }
}
