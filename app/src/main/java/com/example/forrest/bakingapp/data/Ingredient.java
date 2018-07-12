package com.example.forrest.bakingapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {
    private String name;
    private double quantity;
    private String measure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }


    /**
     * Json format:
     *
     * "quantity": 2,
     * "measure": "CUP",
     * "ingredient": "Graham Cracker crumbs"
     *
     * @param ingredientJsonObj JSONObject for this Ingredient.
     * @return
     */
    public static Ingredient toIngredient(JSONObject ingredientJsonObj) {
        Ingredient ingredient = new Ingredient();

        try {
            ingredient.name = ingredientJsonObj.getString("ingredient");
            ingredient.quantity = ingredientJsonObj.getDouble("quantity");
            ingredient.measure = ingredientJsonObj.getString("measure");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredient;
    }


    public static JSONObject toJsonObject(Ingredient ingredient) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("quantity", ingredient.quantity);
            obj.put("measure", ingredient.measure);
            obj.put("ingredient", ingredient.name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
