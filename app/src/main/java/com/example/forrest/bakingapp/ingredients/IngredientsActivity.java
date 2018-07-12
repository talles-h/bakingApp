
package com.example.forrest.bakingapp.ingredients;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.stepdetails.StepDetailsFragment;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class IngredientsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);

        /* Create the Fragment. */
        IngredientsFragment fragment =
                (IngredientsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_ingredients_container);

        if (fragment == null) {

            fragment = IngredientsFragment.newInstance(recipeId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            ActivityUtils.addFragmentToActivity(fragmentManager, fragment, R.id.fragment_ingredients_container);
        }
    }
}
