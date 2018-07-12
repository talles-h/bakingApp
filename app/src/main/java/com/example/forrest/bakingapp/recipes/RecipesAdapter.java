package com.example.forrest.bakingapp.recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> mRecipes;
    private RecipesListOnClickHandler mClickHandler;
    private Context mContext;

    public RecipesAdapter(Context context, RecipesListOnClickHandler clickHandler) {
        mRecipes = null;
        mClickHandler = clickHandler;
        mContext = context;
        mRecipes = new ArrayList<>(0);
    }


    public interface RecipesListOnClickHandler {
        void onClick(Recipe recipe);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }


    public void setRecipesList(ArrayList<Recipe> list) {
        this.mRecipes = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    /** ============== RecipeViewHolder ============== **/
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRecipeNameTv;
        private ImageView mRecipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            mRecipeNameTv = itemView.findViewById(R.id.recipe_name_tv);

            mRecipeImage = itemView.findViewById(R.id.recipe_image_view);

            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            if (recipe != null) {
                mRecipeNameTv.setText(recipe.getName());
                if (recipe.getImageUrl() != null &&
                        !recipe.getImageUrl().equals("")) {
                    Picasso.with(mRecipeImage.getContext()).load(recipe.getImageUrl()).into(mRecipeImage);
                }
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipes.get(clickedPosition));
        }
    }
}
