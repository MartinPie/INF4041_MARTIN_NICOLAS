package org.esiea.martin_nicolas.projetmobiles3;

/**
 * Classe pour les ingrédients avec son nom et sa mesure
 */
public class Ingredient {
    private String name;
    private String measure;

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
