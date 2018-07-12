package com.example.forrest.bakingapp.recipes;

import android.support.annotation.NonNull;

import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.data.source.RecipesRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipesPresenter implements RecipesContract.Presenter {

    private final RecipesRepository mRecipesRepositoty;

    private final RecipesContract.View mRecipesView;

    private boolean mFirstLoad = true;

    public RecipesPresenter(@NonNull RecipesRepository recipesRepository,
                            RecipesContract.View recipesView) {
        mRecipesRepositoty = recipesRepository;
        mRecipesView = recipesView;

        mRecipesView.setPresenter(this);
    }


    @Override
    public void start() {
        loadRecipes(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
        loadRecipes(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    private void loadRecipes(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            // Call mRecipesView to show a loading indicator.
        }

        if (forceUpdate) {
            mRecipesRepositoty.refreshRecipes();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        /* TODO EspressoIdlingResource.increment(); */

        LoadCallback callback = new LoadCallback();

        mRecipesRepositoty.getRecipes(callback);
    }


    @Override
    public void openRecipeDetails(@NonNull Recipe requestedRecipe) {
        mRecipesView.showRecipeDetailsUi(requestedRecipe);
    }


    public class LoadCallback implements RecipesDataSource.LoadRecipesCallback {

        @Override
        public void onRecipesLoaded(List<Recipe> recipes) {
            mRecipesView.showRecipes(recipes);
        }

        @Override
        public void onDataNotAvailable() {
            mRecipesView.showLoadingRecipesError();
        }
    }


}
