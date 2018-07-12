package com.example.forrest.bakingapp.recipedetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.RecipeStep;
import com.example.forrest.bakingapp.ingredients.IngredientsActivity;
import com.example.forrest.bakingapp.ingredients.IngredientsFragment;
import com.example.forrest.bakingapp.stepdetails.StepDetailsActivity;
import com.example.forrest.bakingapp.stepdetails.StepDetailsFragment;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsFragment.RecipeDetailsClickListener {

    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    private StepDetailsFragment mStepDetailsFragment;
    private IngredientsFragment mIngredientsFragment;

    // Used for steps.
    private View mContainerForStepDetails;

    // Used for ingredients.
    private View mContainerForIngredients;

    private boolean mIsTwoPane = false;

    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Get the requested recipe id
        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);

        // Create RecipeDetailsFragment
        RecipeDetailsFragment detailFragment = (RecipeDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_for_recipe_details);

        if (detailFragment == null) {
            detailFragment = RecipeDetailsFragment.newInstance(mRecipeId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    detailFragment, R.id.container_for_recipe_details);
        }

        // Check if its two pane mode.
        // Does we have the view for step details?
        mContainerForStepDetails = findViewById(R.id.container_for_step_details);
        mContainerForIngredients = findViewById(R.id.container_for_ingredients_details);
        if (mContainerForStepDetails != null) {

            mIsTwoPane = true;

            // Step Details Fragment
            mStepDetailsFragment =
                    (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_for_step_details);

            if (mStepDetailsFragment == null) {

                mStepDetailsFragment = StepDetailsFragment.newInstance(mRecipeId, RecipeStep.INVALID_STEP_ID);

                FragmentManager fragmentManager = getSupportFragmentManager();

                ActivityUtils.addFragmentToActivity(fragmentManager, mStepDetailsFragment, R.id.container_for_step_details);
            }

            // Ingredients Fragment.
            mIngredientsFragment =
                    (IngredientsFragment) getSupportFragmentManager().findFragmentById(R.id.container_for_ingredients_details);

            if (mIngredientsFragment == null) {

                mIngredientsFragment = IngredientsFragment.newInstance(mRecipeId);

                FragmentManager fragmentManager = getSupportFragmentManager();

                ActivityUtils.addFragmentToActivity(fragmentManager, mIngredientsFragment, R.id.container_for_ingredients_details);
            }
        }

    }

    // Show ingredients.
    @Override
    public void onRecipeDetailsClick(Recipe recipe) {
        if (mIsTwoPane) {
            mContainerForStepDetails.setVisibility(View.GONE);
            mContainerForIngredients.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(IngredientsActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        }
    }

    // Show recipe steps.
    @Override
    public void onRecipeDetailsClick(RecipeStep step) {
        if (mIsTwoPane) {
            mContainerForIngredients.setVisibility(View.GONE);
            mStepDetailsFragment.setStepId(step.getId());
            mContainerForStepDetails.setVisibility(View.VISIBLE);

        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(StepDetailsActivity.EXTRA_RECIPE_ID, mRecipeId);
            intent.putExtra(StepDetailsActivity.EXTRA_STEP_ID, step.getId());
            startActivity(intent);
        }
    }
}
