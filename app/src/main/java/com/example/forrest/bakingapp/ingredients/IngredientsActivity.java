
package com.example.forrest.bakingapp.ingredients;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.recipedetails.RecipeDetailsActivity;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class IngredientsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);

        /* Create the Fragment. */
        IngredientsFragment fragment =
                (IngredientsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_ingredients_container);

        if (fragment == null) {

            fragment = IngredientsFragment.newInstance(mRecipeId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            ActivityUtils.addFragmentToActivity(fragmentManager, fragment, R.id.fragment_ingredients_container);
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
