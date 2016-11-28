package org.esiea.martin_nicolas.projetmobiles3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/***
 * Adapter pour la liste des cocktails
 */
public class DrinkDataAdapter extends RecyclerView.Adapter<DrinkDataAdapter.ViewHolder> {
    private ArrayList<Drink> drinks;
    private Context context;
    private OnDrinkClickListener listener;

    /***
     * Constructeur
     * @param context
     * @param drinks liste des cocktails chargés
     * @param listener listener pour le click d'un cocktail
     */
    public DrinkDataAdapter(Context context, ArrayList<Drink> drinks, OnDrinkClickListener listener) {
        this.drinks = drinks;
        this.context = context;
        this.listener = listener;
    }


    /***
     *
     * @param viewGroup
     * @param i
     * @return viewHolder du cocktail avec son nom et son image
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_drink_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrinkDataAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(this.drinks.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return this.drinks.size();
    }

    public interface OnDrinkClickListener {
        void onItemClick(Drink item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_drink;
        private ImageView img_drink;

        public ViewHolder(View view) {
            super(view);

            tv_drink = (TextView) view.findViewById(R.id.tv_drink);
            img_drink = (ImageView) view.findViewById(R.id.img_drink);
        }

        /***
         * on ajoute le nom du cocktail, l'image et l'action
         * @param item cocktail
         * @param listener
         */
        public void bind(final Drink item, final OnDrinkClickListener listener) {
            tv_drink.setText(item.getName());
            Picasso.with(context).load(item.getImgageUrl()).into(img_drink);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}