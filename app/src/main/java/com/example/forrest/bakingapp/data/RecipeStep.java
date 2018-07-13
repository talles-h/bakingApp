package com.example.forrest.bakingapp.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeStep {

    public static final int INVALID_STEP_ID = -1;

    private int id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * Json format:
     *
     * "id": 0,
     * "shortDescription": "Recipe Introduction",
     * "description": "Recipe Introduction",
     * "videoURL": "https://....",
     * "thumbnailURL": ""
     *
     * @param recipeStepJsonObj JSONObject for this RecipeStep.
     * @return RecipeStep object.
     */
    public static RecipeStep toRecipeStep(JSONObject recipeStepJsonObj) {
        RecipeStep recipeStep = new RecipeStep();
        try {
            recipeStep.id = recipeStepJsonObj.getInt("id");
            recipeStep.shortDescription = recipeStepJsonObj.getString("shortDescription");
            recipeStep.description = recipeStepJsonObj.getString("description");
            recipeStep.videoUrl = recipeStepJsonObj.getString("videoURL");
            recipeStep.thumbnailUrl = recipeStepJsonObj.getString("thumbnailURL");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeStep;
    }

    public static JSONObject toJsonObject(RecipeStep recipeStep) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", recipeStep.id);
            obj.put("shortDescription", recipeStep.shortDescription);
            obj.put("description", recipeStep.description);
            obj.put("videoURL", recipeStep.videoUrl);
            obj.put("thumbnailURL", recipeStep.thumbnailUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

}
