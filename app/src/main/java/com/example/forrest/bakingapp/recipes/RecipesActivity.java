package com.example.forrest.bakingapp.recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.SimpleIdlingResource;
import com.example.forrest.bakingapp.utils.ActivityUtils;

public class RecipesActivity extends AppCompatActivity {

    private static final String TAG = "RecipesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);



        /* Create the Fragment. */
        RecipesFragment recipesFragment =
                (RecipesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (recipesFragment == null) {

            recipesFragment = RecipesFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();

            ActivityUtils.addFragmentToActivity(fragmentManager, recipesFragment, R.id.fragment_container);
        }


    }

    public IdlingResource getIdlingResource() {
        return Injection.provideTasksRepository(this).getIdlingResource();
    }



}
