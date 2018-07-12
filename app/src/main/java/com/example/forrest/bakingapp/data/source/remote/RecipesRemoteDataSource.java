package com.example.forrest.bakingapp.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.utils.AppExecutors;
import com.example.forrest.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RecipesRemoteDataSource implements RecipesDataSource {

    private static final String TAG = "RecipesRemoteDataSource";

    private static final String RECIPES_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static RecipesRemoteDataSource sInstance;

    private AppExecutors mAppExecutors;

    /* Avoid direct instantiation. This is a singleton class. */
    private RecipesRemoteDataSource(AppExecutors appExecutors){
        mAppExecutors = appExecutors;
    }


    /* Get singleton. */
    public static RecipesRemoteDataSource getInstance(AppExecutors appExecutors) {

        if (sInstance == null) {
            synchronized (RecipesRemoteDataSource.class) {
                if (sInstance == null)
                    sInstance = new RecipesRemoteDataSource(appExecutors);
            }
        }

        return sInstance;
    }

    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        /* Runnable to execute network IO on background. */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final ArrayList<Recipe> recipes = getRecipesFromNetwork();

                /* Call callback using UI thread, so UI can be updated. */
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipes != null && recipes.size() > 0)
                            callback.onRecipesLoaded(recipes);
                        else
                            callback.onDataNotAvailable();
                    }
                });

            }
        };

        /* Execute network IO. */
        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void getRecipe(final int recipeId, @NonNull final GetRecipeCallback callback) {
        /* SERVER DOES NOT SUPPORT QUERYING ONLY FOR ONE RECIPE.
        * So we get all tasks and return only the required one. */

        /* Runnable to execute network IO on background. */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final ArrayList<Recipe> recipes = getRecipesFromNetwork();
                final Recipe targetRecipe;
                Recipe targetRecipeTemp = null;

                if (recipes != null) {
                    for (int i = 0; i < recipes.size(); i++) {
                        if (recipes.get(i).getId() == recipeId) {
                            targetRecipeTemp = recipes.get(i);
                            break;
                        }
                    }
                }
                targetRecipe = targetRecipeTemp;

                /* Call callback using UI thread, so UI can be updated. */
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (targetRecipe != null)
                            callback.onRecipeLoaded(targetRecipe);
                        else
                            callback.onDataNotAvailable();
                    }
                });

            }
        };

        /* Execute network IO. */
        mAppExecutors.networkIO().execute(runnable);
    }

    @Override
    public void saveRecipe(@NonNull Recipe recipe) {
        /* SERVER DOES NOT SUPPORT UPLOAD OF RECIPES. */
    }

    @Override
    public void refreshRecipes() {
        /* NOT SUPPORTED BY REMOTE DATA SOURCE */
    }

    @Override
    public void deleteAllRecipes() {
        /* SERVER DOES NOT SUPPORT DELETE OPERATION. */
    }

    @Override
    public void deleteRecipe(int id) {
        /* SERVER DOES NOT SUPPORT DELETE OPERATION. */
    }


    private ArrayList<Recipe> getRecipesFromNetwork() {
        ArrayList<Recipe> recipesList = null;

        try {
            URL url = new URL(RecipesRemoteDataSource.RECIPES_URL);

            String recipesJson = NetworkUtils.getResponseFromHttpUrl(url);

            if (recipesJson == null) {
                Log.e(TAG, "Error getting data from server.");
                return null;
            }

            JSONArray recipesJsonArray = new JSONArray(recipesJson);

            for (int i = 0; i < recipesJsonArray.length(); i++) {
                String recipeJsonStr = recipesJsonArray.getJSONObject(i).toString();

                Recipe recipeObj = Recipe.toRecipe(recipeJsonStr);

                if (recipeObj != null) {
                    if (recipesList == null) {
                        recipesList = new ArrayList<>();
                    }
                    recipesList.add(recipeObj);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return recipesList;
    }
}
