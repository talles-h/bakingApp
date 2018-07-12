package com.example.forrest.bakingapp.recipedetails;

import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.RecipeStep;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.data.source.RecipesRepository;

public class RecipeDetailsPresenter implements RecipeDetailsContract.Presenter {

    private final RecipesRepository mRecipesRepository;

    private final RecipeDetailsContract.View mRecipeDetailView;

    private int mRecipeId;

    public RecipeDetailsPresenter(int recipeId, RecipesRepository recipesRepository,
                                  RecipeDetailsContract.View recipeDetailView) {
        mRecipeId = recipeId;
        mRecipesRepository = recipesRepository;
        mRecipeDetailView = recipeDetailView;

        mRecipeDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openRecipe();
    }

    @Override
    public void setRecipeId(int id) {
        mRecipeId = id;
        start();
    }

    @Override
    public void openRecipeStepDetails(RecipeStep step) {
        mRecipeDetailView.showRecipeStepDetailsUI(step);
    }

    @Override
    public void openRecipeIngredients() {
        mRecipesRepository.getRecipe(mRecipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    showIngredients(recipe);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void openRecipe() {
        if (mRecipeId == Recipe.INVALID_RECIPE_ID)
            return;

        mRecipesRepository.getRecipe(mRecipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    showRecipe(recipe);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }


    private void showRecipe(Recipe recipe) {
        mRecipeDetailView.showRecipeDetails(recipe);
    }

    private void showIngredients(Recipe recipe) {
        mRecipeDetailView.showRecipeIngredientsUi(recipe);
    }

}
