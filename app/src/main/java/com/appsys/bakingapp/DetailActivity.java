package com.appsys.bakingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appsys.bakingapp.fragment.IngredientsFragment;
import com.appsys.bakingapp.fragment.StepFragment;
import com.appsys.bakingapp.fragment.StepsFragment;
import com.appsys.bakingapp.model.Ingredient;
import com.appsys.bakingapp.model.Recipe;
import com.appsys.bakingapp.model.Step;
import com.appsys.bakingapp.widget.RecipeWidget;
import com.appsys.utils.Utils;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements StepsFragment.StepsCallback {

    private Recipe mRecipe;
    private Ingredient mIngredient;
    private Step mStep;
    private Toast mToast;
    private boolean mPhone = true;
    private int mCurrentIndex = -2;
    private static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    private static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FragmentManager fm = getFragmentManager();
        mPhone = (findViewById(R.id.detail_list_fragment) == null);

        if (savedInstanceState == null) {
            Intent data = getIntent();
            if (data != null && data.hasExtra("recipe")) {
                mRecipe = data.getParcelableExtra("recipe");
            }
        } else if (savedInstanceState.containsKey("recipe")) {
            mRecipe = savedInstanceState.getParcelable("recipe");
            mCurrentIndex = savedInstanceState.getInt("currentIndex", -2);
        }

        if (mRecipe == null) {
            showMessage("please contact to developer");
            finish();
            return;
        }

        setTitle(mRecipe.getTitle());

        if (savedInstanceState == null) {
            StepsFragment stepsFragment = StepsFragment.newInstance(mRecipe);
            fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.master_list_fragment, stepsFragment).addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();
        }

        ButterKnife.bind(this);

        if (!mPhone) {
            if (fm.getBackStackEntryCount() > 1) {
                fm.popBackStack(STACK_RECIPE_DETAIL, 0);
            }
            if (mCurrentIndex < 0) {
                IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe.getIngredients());
                fm.beginTransaction()
                        .replace(R.id.detail_list_fragment, ingredientsFragment)
                        .commit();
            } else {
                StepFragment stepFragment = StepFragment.newInstance(mRecipe.getSteps(), mCurrentIndex);
                FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.detail_list_fragment, stepFragment);
                ft.commit();
            }
        } else {
            if (mCurrentIndex == -1) {
                IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe.getIngredients());
                fm.beginTransaction()
                        .replace(R.id.master_list_fragment, ingredientsFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            } else if (mCurrentIndex > -1) {
                StepFragment stepFragment = StepFragment.newInstance(mRecipe.getSteps(), mCurrentIndex);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.master_list_fragment, stepFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL);
                ft.commit();
            }
        }


    }

    private void showMessage(String msg) {
        if (mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mPhone) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 1) {
                fm.popBackStack(STACK_RECIPE_DETAIL, 0);
                mCurrentIndex = -2;
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onIngredientClick(List<Ingredient> list) {
        if (mCurrentIndex == -1)
            return;

        mCurrentIndex = -1;
        FragmentManager fm = getFragmentManager();
        IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe.getIngredients());
        FragmentTransaction ft = fm.beginTransaction();
        if (mPhone) {
            ft.replace(R.id.master_list_fragment, ingredientsFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL);
        } else {
            ft.replace(R.id.detail_list_fragment, ingredientsFragment);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_widget:
                try {
                    Utils.writeStringFile(DetailActivity.this, Utils.SINGLE_RECIPE, mRecipe.getRecipeJSON());
                } catch (IOException e) {
                    Log.e("shakir 1", e.getMessage(), e);
                }
                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));

                RecipeWidget.updateAllWidgets(this, appWidgetManager, appWidgetIds);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStepClick(List<Step> list, int position) {
        if (mCurrentIndex == position)
            return;

        mCurrentIndex = position;
        FragmentManager fm = getFragmentManager();
        StepFragment stepsFragment = StepFragment.newInstance(mRecipe.getSteps(), position);
        FragmentTransaction ft = fm.beginTransaction();
        if (mPhone) {
            ft.replace(R.id.master_list_fragment, stepsFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL);
        } else {
            ft.replace(R.id.detail_list_fragment, stepsFragment);
        }
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("recipe", mRecipe);
        outState.putInt("currentIndex", mCurrentIndex);
        super.onSaveInstanceState(outState);
    }
}
