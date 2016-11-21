package org.esiea.martin_nicolas.projetmobiles3;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by nicobas on 08/11/16.
 */
public class Ingredient {
    private String name;
    private String measure;

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}