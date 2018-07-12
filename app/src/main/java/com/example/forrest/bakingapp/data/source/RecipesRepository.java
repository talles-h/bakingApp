package com.example.forrest.bakingapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.example.forrest.bakingapp.SimpleIdlingResource;
import com.example.forrest.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
public class RecipesRepository implements RecipesDataSource {

    private static final String TAG = "RecipesRepository";

    private static RecipesRepository sInstance = null;

    private final RecipesDataSource mRecipesRemoteDataSource;

    private final RecipesDataSource mRecipesLocalDataSource;

    private Map<Integer, Recipe> mCachedRecipes;

    private boolean mCacheIsDirty = false;


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /* Avoid direct instantiation. Singleton class. */
    private RecipesRepository(RecipesDataSource recipesRemoteDataSource,
                              RecipesDataSource recipesLocalDataSource) {
        mRecipesRemoteDataSource = recipesRemoteDataSource;
        mRecipesLocalDataSource = recipesLocalDataSource;
        getIdlingResource();
    }


    /* Return the class instance. */
    public static RecipesRepository getInstance(RecipesDataSource recipesRemoteDataSource,
                                                 RecipesDataSource recipesLocalDataSource) {
        if (sInstance == null) {
            sInstance = new RecipesRepository(recipesRemoteDataSource, recipesLocalDataSource);
        }

        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        Log.d(TAG, "getRecipes()");

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        // Respond immediately with cache if available and not dirty.
        if (mCachedRecipes != null && !mCacheIsDirty) {
            Log.d(TAG, "Cache is valid. Returning from cache.");
            callback.onRecipesLoaded(new ArrayList<>(mCachedRecipes.values()));
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
            return;
        }

        if (mCacheIsDirty) {
            Log.d(TAG, "Cache is dirty, retrieving from remote.");
            getRecipesFromRemoteDataSource(callback);
        } else {
            Log.d(TAG, "Querying local data source.");

            // Query the local storage if available. If not, query the network.

            mRecipesLocalDataSource.getRecipes(new LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<Recipe> recipes) {
                    Log.d(TAG, "Callback from Local Data Source. Updating cache.");

                    refreshCache(recipes);
                    callback.onRecipesLoaded(new ArrayList<>(mCachedRecipes.values()));
                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Log.d(TAG, "Data not available locally. Querying remote.");
                    getRecipesFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getRecipe(final int recipeId, @NonNull final GetRecipeCallback callback) {

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        Recipe recipe = mCachedRecipes.get(Integer.valueOf(recipeId));

        // Respond immediately with cache if available.
        if (recipe != null) {
            callback.onRecipeLoaded(recipe);
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
            return;
        }


        // Get from Database. If not available, get from server.
        mRecipesLocalDataSource.getRecipe(recipeId, new GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                if (mCachedRecipes == null) {
                    mCachedRecipes = new LinkedHashMap<>();
                }
                mCachedRecipes.put(recipe.getId(), recipe);
                callback.onRecipeLoaded(recipe);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mRecipesRemoteDataSource.getRecipe(recipeId, new GetRecipeCallback() {
                    @Override
                    public void onRecipeLoaded(Recipe recipe) {
                        if (mCachedRecipes == null) {
                            mCachedRecipes = new LinkedHashMap<>();
                        }
                        mCachedRecipes.put(recipe.getId(), recipe);
                        callback.onRecipeLoaded(recipe);
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void saveRecipe(@NonNull Recipe recipe) {
        Log.d(TAG, "Saving recipe with ID: " + recipe.getId());
        mRecipesLocalDataSource.saveRecipe(recipe);
        mRecipesRemoteDataSource.saveRecipe(recipe);

        if (mCachedRecipes == null) {
            Log.d(TAG, "Creating new cache.");
            mCachedRecipes = new LinkedHashMap<>();
        }
        Log.d(TAG, "Adding recipe to cache.");
        mCachedRecipes.put(recipe.getId(), recipe);
    }

    @Override
    public void refreshRecipes() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllRecipes() {
        mRecipesLocalDataSource.deleteAllRecipes();
        mRecipesRemoteDataSource.deleteAllRecipes();

        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }

        mCachedRecipes.clear();
    }

    @Override
    public void deleteRecipe(int id) {
        mRecipesRemoteDataSource.deleteRecipe(id);
        mRecipesLocalDataSource.deleteRecipe(id);

        mCachedRecipes.remove(Integer.valueOf(id));
    }

    private void getRecipesFromRemoteDataSource(@NonNull final LoadRecipesCallback callback) {
        Log.d(TAG, "getRecipesFromRemoteDataSource()");
        mRecipesRemoteDataSource.getRecipes(new LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(List<Recipe> recipes) {
                Log.d(TAG, "Callback from remote with recipes");
                refreshCache(recipes);
                refreshLocalDataSource(recipes);
                callback.onRecipesLoaded(new ArrayList<>(mCachedRecipes.values()));
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "Callback from remote. No data available");
                callback.onDataNotAvailable();
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }

    private void refreshCache(List<Recipe> recipes) {
        Log.d(TAG, "refreshCache()");
        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }
        mCachedRecipes.clear();
        for (Recipe recipe : recipes) {
            mCachedRecipes.put(recipe.getId(), recipe);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Recipe> recipes) {
        Log.d(TAG, "refreshLocalDataSource()");
        mRecipesLocalDataSource.deleteAllRecipes();
        for (Recipe recipe : recipes) {
            mRecipesLocalDataSource.saveRecipe(recipe);
        }
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}