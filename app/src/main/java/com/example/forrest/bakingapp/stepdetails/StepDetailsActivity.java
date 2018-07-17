package com.example.forrest.bakingapp.stepdetails;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.recipedetails.RecipeDetailsActivity;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";
    public static final String EXTRA_STEP_ID = "step_id";
    private int mRecipeId;
    private int mStepId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        mStepId = getIntent().getIntExtra(EXTRA_STEP_ID, -1);

        /* Create the Fragment. */
        StepDetailsFragment fragment =
                (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_for_step_details);

        if (fragment == null) {

            fragment = StepDetailsFragment.newInstance(mRecipeId, mStepId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            ActivityUtils.addFragmentToActivity(fragmentManager, fragment, R.id.container_for_step_details);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, mRecipeId);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
