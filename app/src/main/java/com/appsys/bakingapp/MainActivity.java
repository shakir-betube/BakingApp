package com.appsys.bakingapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsys.bakingapp.adapter.RecipeAdapter;
import com.appsys.bakingapp.model.Recipe;
import com.appsys.utils.ApiException;
import com.appsys.utils.InternetException;
import com.appsys.utils.JSONParsingException;
import com.appsys.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private final static int LOADER_ID = 10;
    private final static String RECIPES = "recipes";

    @BindView(R.id.recipes_list)
    RecyclerView mRecipeList;

    @BindView(R.id.recipes_progress)
    ProgressBar mProgressBar;

    private Toast mToast;

    private RecipeAdapter mRecipeAdapter;

    private String mErrorMessage = "";

    private void showMessage(String msg) {
        if (mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecipeList.setLayoutManager(new GridLayoutManager(this, computeNoOfColumns()));

        mRecipeAdapter = new RecipeAdapter();

        mRecipeList.setAdapter(mRecipeAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES)) {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPES);
            mRecipeAdapter.swap(recipes);
        } else {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }


    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @SuppressWarnings({"TryWithIdenticalCatches", "EmptyCatchBlock"})
            @Override
            public ArrayList<Recipe> loadInBackground() {
                int tryNo = 0;
                mErrorMessage = "currently recipes are unavailable";
                while (tryNo++ < 3) {
                    try {
                        Utils u = new Utils();
                        JSONArray response = u.getRecipes();
//                        JSONArray response = u.getRecipes(MainActivity.this);
                        int recipesCount = response.length();
                        ArrayList<Recipe> recipes = new ArrayList<>();
                        for (int i = 0; i < recipesCount; i++) {
                            JSONObject jsonRecipe = response.getJSONObject(i);
                            Recipe recipe = new Recipe();
                            recipe.setByJSON(jsonRecipe);
                            recipes.add(recipe);
                        }
                        if (recipes.size() > 0 && !(new File(Utils.SINGLE_RECIPE).exists())) {
                            try {
                                Utils.writeStringFile(MainActivity.this, Utils.SINGLE_RECIPE, recipes.get(0).getRecipeJSON().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        return recipes;
                    } catch (JSONException e) {
                        mErrorMessage = e.getMessage();
                    } catch (JSONParsingException e) {
                        mErrorMessage = e.getMessage();
                    } catch (ApiException e) {
                        mErrorMessage = e.getMessage();
                    } catch (InternetException e) {
                        continue;
                    }
                    break;
                }
                return null;
            }
        };
    }

    protected int computeNoOfColumns() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float dpWidth = dm.widthPixels / dm.density;
        int result = (int) (dpWidth / 400);
        if (result < 1) {
            result = 1;
        }
        return result;
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        mRecipeAdapter.swap(recipes);
        mProgressBar.setVisibility(View.GONE);
        getLoaderManager().destroyLoader(LOADER_ID);
        if (recipes == null || recipes.size() < 1) {
            showMessage(mErrorMessage);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Recipe>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES, mRecipeAdapter.getList());
        super.onSaveInstanceState(outState);
    }
}
