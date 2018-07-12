package com.example.forrest.bakingapp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({Recipe.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase sInstance;

    public abstract RecipesDao recipesDao();

    private static final Object sLock = new Object();

    public static RecipeDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, String.valueOf(R.string.database_name))
                        .build();
            }
            return sInstance;
        }
    }
}
