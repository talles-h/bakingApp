package com.example.forrest.bakingapp.recipedetails;

import com.example.forrest.bakingapp.BasePresenter;
import com.example.forrest.bakingapp.BaseView;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.RecipeStep;

public interface RecipeDetailsContract {

    interface Presenter extends BasePresenter {

        void setRecipeId(int id);

        void openRecipeStepDetails(RecipeStep step);

        void openRecipeIngredients();
    }


    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showRecipeDetails(Recipe recipe);

        void showRecipeStepDetailsUI(RecipeStep step);

        void showRecipeIngredientsUi(Recipe recipe);
    }

}
