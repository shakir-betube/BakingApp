package com.appsys.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.appsys.bakingapp.MainActivity;
import com.appsys.bakingapp.R;
import com.appsys.bakingapp.model.Ingredient;
import com.appsys.bakingapp.model.Recipe;
import com.appsys.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeWidget extends AppWidgetProvider {
    public final static int APP_WIDGET_ID = 10;
    public static final String EXTRA_WORD = "com.appsys.bakingapp.widget";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String title) {
        Intent svcIntent=new Intent(context, WidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews widget=new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget);

        widget.setRemoteAdapter(R.id.recipe_ingredient_list,
                svcIntent);

        widget.setTextViewText(R.id.widget_recipe_item_title, title);

        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAllWidgets(context,appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        try {
            String data = Utils.readStringFile(context, Utils.SINGLE_RECIPE);

            String title = new JSONObject(data).getString("name");

            for (int i=0; i<appWidgetIds.length; i++) {
                updateAppWidget(context, appWidgetManager, appWidgetIds[i], title);
            }
        } catch (IOException e) {
            Log.e("shakie hello", "updateWidgetListView -> IOException: "+e.getMessage(), e);
        } catch (JSONException e) {
            Log.e("shakie hello", "updateWidgetListView -> JSONException: "+e.getMessage(), e);
        }
    }
}

