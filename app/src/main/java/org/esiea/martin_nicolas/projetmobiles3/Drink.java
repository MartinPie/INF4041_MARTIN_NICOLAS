package org.esiea.martin_nicolas.projetmobiles3;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicobas on 08/11/16.
 */
public class Drink {
    private int id;
    private String name;
    private String category;
    private String alcoholic;
    private String glass;
    private String instruction;
    private String imgageUrl;
    private HashMap<String, String> ingredients;

    public Drink(JSONObject json, boolean isShort) {
        try {
            this.setId(json.getInt("idDrink"));
            this.setName(json.getString("strDrink"));
            this.setImgageUrl(json.getString("strDrinkThumb"));

            if (this.getImgageUrl().equals("null"))
                this.setImgageUrl("http://www.novelupdates.com/img/noimagefound.jpg");

            if (!isShort) {
                this.setCategory(json.getString("strCategory"));
                this.setAlcoholic(json.getString("strAlcoholic"));
                this.setGlass(json.getString("strGlass"));
                this.setInstruction(json.getString("strInstructions"));

                this.ingredients = new HashMap<String, String>();

                for (int i = 1; i <= 15; i++) {
                    String ingredient = json.getString("strIngredient" + i);
                    String measure = json.getString("strMeasure" + i);

                    if (ingredient != null) {
                        this.ingredients.put(ingredient, measure);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Drink() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getImgageUrl() {
        return imgageUrl;
    }

    public void setImgageUrl(String imgageUrl) {
        this.imgageUrl = imgageUrl;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", alcoholic='" + alcoholic + '\'' +
                ", glass='" + glass + '\'' +
                ", instruction='" + instruction + '\'' +
                ", imgageUrl='" + imgageUrl + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
