package com.example.forrest.bakingapp.recipes;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.RecipeWidgetProvider;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.recipedetails.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment implements RecipesContract.View,
        RecipesAdapter.RecipesListOnClickHandler {

    private RecipesContract.Presenter mPresenter;

    private RecipesAdapter mListAdapter;

    private RecyclerView mListRecyclerView;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new RecipesAdapter(getContext(), this);

        // Create the presenter.
        mPresenter = new RecipesPresenter(
                Injection.provideTasksRepository(getContext()),
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate fragment layout.
        View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        mListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipes_list_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), getResources().getInteger(R.integer.grid_columns));
        mListRecyclerView.setLayoutManager(layoutManager);

        mListRecyclerView.setAdapter(mListAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /** ========== ADAPTER CLICK HANDLER METHOD ========== **/
    @Override
    public void onClick(Recipe recipe) {
        mPresenter.openRecipeDetails(recipe);
    }


    /*** ========== RecipesContract.View methods ========== ***/

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mListAdapter.setRecipesList(new ArrayList<Recipe>(recipes));
    }

    @Override
    public void showRecipeDetailsUi(Recipe recipe) {
        RecipeWidgetProvider.updateAppWidget(getContext(),
                AppWidgetManager.getInstance(getContext()), recipe);

        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    @Override
    public void showLoadingRecipesError() {

    }

    @Override
    public void showNoRecipes() {

    }

    @Override
    public void setPresenter(RecipesContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
