package com.example.forrest.bakingapp.data.source;

import android.support.annotation.NonNull;

import com.example.forrest.bakingapp.data.Recipe;

import java.util.List;

public interface RecipesDataSource {

    interface LoadRecipesCallback {
        void onRecipesLoaded(List<Recipe> recipes);

        void onDataNotAvailable();
    }

    interface GetRecipeCallback {
        void onRecipeLoaded(Recipe recipe);

        void onDataNotAvailable();
    }

    void getRecipes(@NonNull LoadRecipesCallback callback);

    void getRecipe(final int recipeId, @NonNull GetRecipeCallback callback);

    void saveRecipe(@NonNull Recipe recipe);

    void refreshRecipes();

    void deleteAllRecipes();

    void deleteRecipe(int id);
}
