package com.example.forrest.bakingapp.stepdetails;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";
    public static final String EXTRA_STEP_ID = "step_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        int stepId = getIntent().getIntExtra(EXTRA_STEP_ID, -1);


        /* Create the Fragment. */
        StepDetailsFragment fragment =
                (StepDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_for_step_details);

        if (fragment == null) {

            fragment = StepDetailsFragment.newInstance(recipeId, stepId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            ActivityUtils.addFragmentToActivity(fragmentManager, fragment, R.id.container_for_step_details);
        }


    }
}
