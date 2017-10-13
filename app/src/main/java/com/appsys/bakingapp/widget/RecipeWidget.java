package com.appsys.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.appsys.bakingapp.MainActivity;
import com.appsys.bakingapp.R;
import com.appsys.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class RecipeWidget extends AppWidgetProvider {
    public final static int APP_WIDGET_ID = 10;
    public static final String EXTRA_WORD = "com.appsys.bakingapp.widget";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, ArrayList<Ingredient> list) {
        Intent svcIntent=new Intent(context, WidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews widget=new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget);

        widget.setRemoteAdapter(R.id.recipe_ingredient_list,
                svcIntent);

        Intent clickIntent=new Intent(context, MainActivity.class);
        PendingIntent clickPI=PendingIntent
                .getActivity(context, 0,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setPendingIntentTemplate(R.id.recipe_ingredient_list, clickPI);

        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i], new ArrayList<Ingredient>());
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

