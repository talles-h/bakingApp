package com.example.forrest.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.forrest.bakingapp.data.source.RecipesRepository;
import com.example.forrest.bakingapp.data.source.local.RecipeDatabase;
import com.example.forrest.bakingapp.data.source.local.RecipesLocalDataSource;
import com.example.forrest.bakingapp.data.source.remote.RecipesRemoteDataSource;
import com.example.forrest.bakingapp.utils.AppExecutors;

public class Injection {
    public static RecipesRepository provideTasksRepository(@NonNull Context context) {

        RecipeDatabase database = RecipeDatabase.getInstance(context);
        return RecipesRepository.getInstance(RecipesRemoteDataSource.getInstance(new AppExecutors()),
                RecipesLocalDataSource.getInstance(new AppExecutors(),
                        database.recipesDao()));
    }
}
