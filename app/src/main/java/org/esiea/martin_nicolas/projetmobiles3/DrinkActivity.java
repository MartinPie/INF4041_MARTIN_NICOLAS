package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class DrinkActivity extends AppCompatActivity implements HttpJsonRequest.OnGetJsonListener {

    public Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_drink);

            Intent intent = getIntent();

            if (intent != null) {
                int drinkId = intent.getIntExtra("drink_id", 0);

                URL url = new URL("http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + drinkId);

                HttpJsonRequest h = new HttpJsonRequest(this, this);
                h.execute(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        Toast.makeText(this, this.drink.getName(), Toast.LENGTH_SHORT).show();

        ImageView img = (ImageView) findViewById(R.id.detail_img_drink);

        Button button= (Button) findViewById(R.id.btn_internet_drink);
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

        Picasso.with(this).load(this.drink.getImgageUrl()).into(img);
    }
}