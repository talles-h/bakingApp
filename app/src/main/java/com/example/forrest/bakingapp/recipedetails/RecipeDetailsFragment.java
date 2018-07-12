package com.example.forrest.bakingapp.recipedetails;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.RecipeStep;
import com.example.forrest.bakingapp.ingredients.IngredientsActivity;
import com.example.forrest.bakingapp.recipes.RecipesAdapter;
import com.example.forrest.bakingapp.stepdetails.StepDetailsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment implements RecipeDetailsContract.View,
        RecipeStepsAdapter.StepsListOnClickHandler, View.OnClickListener {

    private static final String TAG = "RecipeDetailsFragment";

    private static final String ARGUMENT_RECIPE_ID = "RECIPE_ID";

    private RecipeDetailsContract.Presenter mPresenter;

    private RecipeStepsAdapter mStepsAdapter;

    private RecyclerView mStepsRecyclerView;

    private TextView mIngredientsTextView;

    private RecipeDetailsClickListener clickListener;

    /* ========= ACTIVITY MUST IMPLEMENT THIS INTERFACE ========= */
    public interface RecipeDetailsClickListener {
        void onRecipeDetailsClick(Recipe recipe);
        void onRecipeDetailsClick(RecipeStep step);
    }

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(int recipeId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_RECIPE_ID, recipeId);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mStepsAdapter = new RecipeStepsAdapter(getContext(), this);

        new RecipeDetailsPresenter(getArguments().getInt(ARGUMENT_RECIPE_ID), Injection.provideTasksRepository(getContext()),
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_recipe_details, container, false);

        mStepsRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_steps);

        mIngredientsTextView = rootView.findViewById(R.id.text_view_ingredients);

        mIngredientsTextView.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mStepsRecyclerView.setLayoutManager(layoutManager);

        mStepsRecyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickListener = (RecipeDetailsClickListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mPresenter.start();
    }


    /*** ========== StepsListOnClickHandler method ========== ***/

    @Override
    public void onClick(RecipeStep step) {
        mPresenter.openRecipeStepDetails(step);
    }



    /*** ========== Ingredients click listener method ========== ***/

    @Override
    public void onClick(View v) {
        mPresenter.openRecipeIngredients();
    }


    /*** ========== RecipesContract.View methods ========== ***/

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showRecipeDetails(Recipe recipe) {
        mStepsAdapter.setStepsList(new ArrayList<RecipeStep>(recipe.getSteps().values()));
    }

    @Override
    public void showRecipeStepDetailsUI(RecipeStep step) {
        clickListener.onRecipeDetailsClick(step);
    }

    @Override
    public void showRecipeIngredientsUi(Recipe recipe) {
        clickListener.onRecipeDetailsClick(recipe);
    }

    @Override
    public void setPresenter(RecipeDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }


    public void setRecipeId(int id) {
        Log.d(TAG, "setRecipeId: " + id);
        this.getArguments().putInt(ARGUMENT_RECIPE_ID, id);
        if (mPresenter != null) {
            mPresenter.setRecipeId(id);
        }
    }
}