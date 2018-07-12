package com.example.forrest.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.forrest.bakingapp.data.Ingredient;
import com.example.forrest.bakingapp.data.Recipe;
import com.example.forrest.bakingapp.data.source.RecipesRepository;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe) {

        if (recipe != null) {

            CharSequence widgetText = context.getString(R.string.appwidget_text);
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.text_view_recipe_name, recipe.getName());

            String ingStr = "";
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingStr += ingredient.getName();
                ingStr += ": " + ingredient.getQuantity() + " (" + ingredient.getMeasure() + ").\n";
            }

            views.setTextViewText(R.id.text_view_recipe_ingredients, ingStr);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(new ComponentName(context, RecipeWidgetProvider.class), views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

