package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.esiea.martin_nicolas.projetmobiles3.Drink;
import org.esiea.martin_nicolas.projetmobiles3.R;

import java.util.ArrayList;

public class DrinkDataAdapter extends RecyclerView.Adapter<DrinkDataAdapter.ViewHolder> {
    private ArrayList<Drink> drinks;
    private Context context;

    public DrinkDataAdapter(Context context,ArrayList<Drink> drinks) {
        this.drinks = drinks;
        this.context = context;
    }

    @Override
    public DrinkDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinkDataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_drink.setText(this.drinks.get(i).getName());
        Picasso.with(context).load(this.drinks.get(i).getImgageUrl()).into(viewHolder.img_drink);
    }

    @Override
    public int getItemCount() {
        return this.drinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_drink;
        private ImageView img_drink;
        public ViewHolder(View view) {
            super(view);

            tv_drink = (TextView)view.findViewById(R.id.tv_drink);
            img_drink = (ImageView) view.findViewById(R.id.img_drink);
        }
    }

}