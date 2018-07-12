package com.example.forrest.bakingapp.recipedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.RecipeStep;

import java.util.ArrayList;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsHolder> {

    private ArrayList<RecipeStep> mRecipeSteps;
    private StepsListOnClickHandler mOnClickHandler;
    private Context mContext;

    public RecipeStepsAdapter(Context context, StepsListOnClickHandler clickHandler) {
        mContext = context;
        mOnClickHandler = clickHandler;
        mRecipeSteps = new ArrayList<>(0);
    }

    public interface StepsListOnClickHandler {
        void onClick(RecipeStep step);
    }

    @NonNull
    @Override
    public RecipeStepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RecipeStepsHolder viewHolder = new RecipeStepsHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsHolder holder, int position) {
        holder.bind(mRecipeSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }


    public void setStepsList(ArrayList<RecipeStep> steps) {
        mRecipeSteps = steps;
        notifyDataSetChanged();
    }


    /** ============== RecipeStepsHolder ============== **/
    class RecipeStepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mStepDescTextView;

        public RecipeStepsHolder(View itemView) {
            super(itemView);

            mStepDescTextView = itemView.findViewById(R.id.text_view_step_list_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onClick(mRecipeSteps.get(clickedPosition));
        }

        public void bind(RecipeStep step) {
            mStepDescTextView.setText(step.getShortDescription());
        }
    }

}
