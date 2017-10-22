package com.appsys.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.model.Ingredient;
import com.appsys.bakingapp.model.Recipe;
import com.appsys.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by shakir on 10/12/2017.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "shakir 123";
    private ArrayList<Ingredient> mList = new ArrayList<>();
    private Context mContext;


    public WidgetRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
    }

    @Override
    public void onCreate() {
        updateWidgetListView();

    }

    @Override
    public void onDataSetChanged() {
        updateWidgetListView();
    }

    void updateWidgetListView() {
        try {
            String data = Utils.readStringFile(mContext, Utils.SINGLE_RECIPE);
            Recipe recipe = new Recipe();
            recipe.setByJSON(new JSONObject(data));

            mList = recipe.getIngredients();
        } catch (IOException e) {
            Log.e(TAG, "updateWidgetListView -> IOException: "+e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, "updateWidgetListView -> JSONException: "+e.getMessage(), e);
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(mContext.getPackageName(),
                R.layout.widget_recipe_item_ingredient);
        Ingredient ing = mList.get(position);
        row.setTextViewText(R.id.recipe_ingredient_title, ing.getIngredient());
        row.setTextViewText(R.id.recipe_ingredient_quantity, String.valueOf(ing.getQuantity()));
        row.setTextViewText(R.id.recipe_ingredient_measure, ing.getMeasure());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
