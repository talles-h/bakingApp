package com.example.forrest.bakingapp.data.source.local;

import android.support.annotation.NonNull;

import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.source.RecipesDataSource;
import com.example.forrest.bakingapp.utils.AppExecutors;

import java.util.List;

public class RecipesLocalDataSource implements RecipesDataSource {

    private RecipesDao mRecipesDao;

    private AppExecutors mAppExecutors;

    private static volatile RecipesLocalDataSource sInstance;

    /* Avoid direct instantiation. This is a singleton class. */
    private RecipesLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull RecipesDao recipesDao) {
        mAppExecutors = appExecutors;
        mRecipesDao = recipesDao;
    }

    /* Returns the singleton. */
    public static RecipesLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull RecipesDao recipesDao) {
        if (sInstance == null) {
            synchronized (RecipesLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RecipesLocalDataSource(appExecutors, recipesDao);
                }
            }
        }

        return sInstance;
    }

    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Recipe> recipes = mRecipesDao.getRecipes();

                /* Call the callback using UI thread. */
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipes == null || recipes.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onRecipesLoaded(recipes);
                        }
                    }
                });
            }
        };

        /* Run disk IO on background thread. */
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getRecipe(final int recipeId, @NonNull final GetRecipeCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Recipe recipe = mRecipesDao.getRecipeById(recipeId);

                /* Call callback using UI thread. */
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipe == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onRecipeLoaded(recipe);
                        }
                    }
                });
            }
        };

        /* Run disk IO on background thread. */
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Insert recipe into the local database.
     * @param recipe Recipe to be saved.
     */
    @Override
    public void saveRecipe(@NonNull final Recipe recipe) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mRecipesDao.insertRecipe(recipe);
            }
        };

        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshRecipes() {
        /* NOT NEEDED. */
    }

    @Override
    public void deleteAllRecipes() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mRecipesDao.deleteRecipes();
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteRecipe(int id) {
        /* NOT NEEDED. */
    }
}
