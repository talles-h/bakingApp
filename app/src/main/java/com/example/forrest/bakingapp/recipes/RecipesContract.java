package com.example.forrest.bakingapp.recipes;

import android.support.annotation.NonNull;

import com.example.forrest.bakingapp.BasePresenter;
import com.example.forrest.bakingapp.BaseView;
import com.example.forrest.bakingapp.data.Recipe;

import java.util.List;

public class RecipesContract {

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadRecipes(boolean forceUpdate);

        void openRecipeDetails(@NonNull Recipe requestedRecipe);
    }


    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showRecipes(List<Recipe> recipes);

        void showRecipeDetailsUi(Recipe recipe);

        void showLoadingRecipesError();

        void showNoRecipes();
    }

}
