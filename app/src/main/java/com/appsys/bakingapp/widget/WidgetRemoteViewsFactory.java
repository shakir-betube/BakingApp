package com.appsys.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by shakir on 10/12/2017.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Ingredient> mList = new ArrayList<>();
    private Context mContext;


    public WidgetRemoteViewsFactory(Context context, Intent intent)
    {
        mContext = context;
        if (intent != null && intent.hasExtra("ingredients")) {
            mList = intent.getParcelableArrayListExtra("ingredients");
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

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
                R.layout.recipe_item_ingredient);
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
