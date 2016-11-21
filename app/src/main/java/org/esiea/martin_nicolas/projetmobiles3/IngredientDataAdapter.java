package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class IngredientDataAdapter extends RecyclerView.Adapter<IngredientDataAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredients;
    private Context context;


    public IngredientDataAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ingredient_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientDataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(this.ingredients.get(i));
    }

    @Override
    public int getItemCount() {
        return this.ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_ingredient;
        private TextView measure_ingredient;
        private ImageView img_ingredient;

        public ViewHolder(View view) {
            super(view);
            img_ingredient = (ImageView) view.findViewById(R.id.img_ingredient);
            name_ingredient = (TextView) view.findViewById(R.id.name_ingredient);
            measure_ingredient = (TextView) view.findViewById(R.id.measure_ingredient);
        }

        public void bind(final Ingredient item) {
            try {
                name_ingredient.setText(item.getName());
                measure_ingredient.setText(item.getMeasure());
                String urlString = "http://www.thecocktaildb.com/images/ingredients/" + URLEncoder.encode(item.getName(), "UTF-8").replaceAll("\\+", "%20") + "-Small.png";
                Picasso.with(context).load(urlString).into(img_ingredient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}