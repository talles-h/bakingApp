package com.example.forrest.bakingapp.stepdetails;

import com.example.forrest.bakingapp.BasePresenter;
import com.example.forrest.bakingapp.BaseView;
import com.example.forrest.bakingapp.data.RecipeStep;

public interface StepDetailsContract {

    interface Presenter extends BasePresenter {
        void setStepId(int id);
    }


    interface View extends BaseView<StepDetailsContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showStepDetails(RecipeStep step);
    }
}
