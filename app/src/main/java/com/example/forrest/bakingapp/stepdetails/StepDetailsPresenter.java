package com.example.forrest.bakingapp.stepdetails;

import android.util.Log;

import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.RecipeStep;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.data.source.RecipesRepository;

public class StepDetailsPresenter implements StepDetailsContract.Presenter {

    private int mRecipeId;
    private int mStepId;

    private final RecipesRepository mRecipesRepository;

    private final StepDetailsContract.View mStepDetailsView;

    public StepDetailsPresenter(int recipeId, int stepId, RecipesRepository repository,
                                StepDetailsContract.View stepView) {
        mRecipeId = recipeId;
        mStepId = stepId;
        mRecipesRepository = repository;
        mStepDetailsView = stepView;

        mStepDetailsView.setPresenter(this);
    }

    @Override
    public void start() {
        openStepDetails();
    }

    public void setStepId(int id) {
        mStepId = id;
        start();
    }

    private void openStepDetails() {
        if (mStepId == RecipeStep.INVALID_STEP_ID)
            return;

        mRecipesRepository.getRecipe(mRecipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    showStepDetails(recipe.getStep(mStepId));
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }


    private void showStepDetails(RecipeStep step) {
        mStepDetailsView.showStepDetails(step);
    }
}
