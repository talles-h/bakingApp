package com.example.forrest.bakingapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity(tableName = "recipes")
public class Recipe {

    @Ignore
    public static final int INVALID_RECIPE_ID = -1;

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "ingredients")
    private ArrayList<Ingredient> ingredients;

    @ColumnInfo(name = "steps")
    private Map<Integer, RecipeStep> steps;

    @ColumnInfo(name = "servings")
    private double servings;

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @Ignore
    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.steps = new LinkedHashMap<>();
    }

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients,
                  Map<Integer, RecipeStep> steps, double servings, String imageUrl){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.imageUrl = imageUrl;
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

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<Integer, RecipeStep> getSteps() {
        return steps;
    }

    public RecipeStep getStep(int stepId) {
        return steps.get(stepId);
    }

    public void addStep(RecipeStep step) {
        if (this.steps == null) {
            this.steps = new LinkedHashMap<>();
        }
        this.steps.put(Integer.valueOf(step.getId()), step);
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Json format:
     *
     * "id": 1,
     * "name": "Nutella Pie",
     * "ingredients": [],
     * "steps": [],
     * "servings": 8,
     * "image": ""
     *
     * @param jsonStr Json string representing this object.
     * @return Recipe object.
     */
    public static Recipe toRecipe(String jsonStr) {
        Recipe recipe = new Recipe();

        try {
            JSONObject recipeJsonObj = new JSONObject(jsonStr);
            recipe.id = recipeJsonObj.getInt("id");
            recipe.name = recipeJsonObj.getString("name");
            recipe.servings = recipeJsonObj.getDouble("servings");
            recipe.imageUrl = recipeJsonObj.getString("image");

            JSONArray ingredientsJsonArray = recipeJsonObj.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                Ingredient ingredientObj = Ingredient.toIngredient(ingredientsJsonArray.getJSONObject(i));

                recipe.ingredients.add(ingredientObj);
            }

            JSONArray stepsJsonArray = recipeJsonObj.getJSONArray("steps");
            for (int i = 0; i < stepsJsonArray.length(); i++) {
                RecipeStep stepObj = RecipeStep.toRecipeStep(stepsJsonArray.getJSONObject(i));

                recipe.addStep(stepObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipe;
    }


    @TypeConverter
    public static String stepsToString(Map<Integer, RecipeStep> stepsMap) {
        ArrayList<RecipeStep> steps = new ArrayList(stepsMap.values());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < steps.size(); i++) {
            jsonArray.put(RecipeStep.toJsonObject(steps.get(i)));
        }

        return jsonArray.toString();
    }

    @TypeConverter
    public static String ingredientsToString(ArrayList<Ingredient> ingredients) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ingredients.size(); i++) {
            jsonArray.put(Ingredient.toJsonObject(ingredients.get(i)));
        }

        return jsonArray.toString();
    }

    @TypeConverter
    public static Map<Integer, RecipeStep> toRecipeSteps(String jsonStr) {
        Map<Integer, RecipeStep> map = new LinkedHashMap<>();

        JSONArray stepsJsonArray;
        try {
            stepsJsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < stepsJsonArray.length(); i++) {
                RecipeStep step = RecipeStep.toRecipeStep(stepsJsonArray.getJSONObject(i));
                if (step != null) {
                    map.put(step.getId(), step);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    @TypeConverter
    public static ArrayList<Ingredient> toIngredients(String jsonStr) {
        ArrayList<Ingredient> arrayList = new ArrayList<>();

        JSONArray ingredientsJsonArray;
        try {
            ingredientsJsonArray = new JSONArray(jsonStr);

            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                Ingredient ingredient = Ingredient.toIngredient(ingredientsJsonArray.getJSONObject(i));
                if (ingredient != null) {
                    arrayList.add(ingredient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
