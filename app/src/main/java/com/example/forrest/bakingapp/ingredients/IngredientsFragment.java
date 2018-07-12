package com.example.forrest.bakingapp.ingredients;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.Ingredient;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.data.source.RecipesRepository;
import com.example.forrest.bakingapp.stepdetails.StepDetailsFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    private static final String ARGUMENT_RECIPE_ID = "recipe_id_arg";

    private ArrayList<Ingredient> mIngredients;

    private TextView mIngredientsTextView;

    public IngredientsFragment() {
        // Required empty public constructor
    }


    public static IngredientsFragment newInstance(int recipeId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_RECIPE_ID, recipeId);
        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);

        mIngredientsTextView = root.findViewById(R.id.text_view_ingredients);

        requestIngredients();

        return root;
    }


    private void requestIngredients() {
        RecipesRepository repository = Injection.provideTasksRepository(this.getContext());

        repository.getRecipe(this.getArguments().getInt(ARGUMENT_RECIPE_ID), new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (recipe != null) {
                    mIngredients = recipe.getIngredients();
                    updateUi();
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void updateUi() {
        if(mIngredients != null) {
            mIngredientsTextView.setText("");

            for (Ingredient ingredient : mIngredients) {
                mIngredientsTextView.append(ingredient.getName());
                mIngredientsTextView.append("\n");
                mIngredientsTextView.append("Quantity (" + ingredient.getMeasure() + "): " + ingredient.getQuantity());
                mIngredientsTextView.append("\n\n");
            }
        }
    }
}
