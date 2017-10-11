package com.appsys.bakingapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsys.bakingapp.adapter.RecipeAdapter;
import com.appsys.bakingapp.modal.Ingredient;
import com.appsys.bakingapp.modal.Recipe;
import com.appsys.bakingapp.modal.Step;
import com.appsys.utils.ApiException;
import com.appsys.utils.InternetException;
import com.appsys.utils.JSONParsingException;
import com.appsys.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        mRecipeList.setLayoutManager(new LinearLayoutManager(this));

        mRecipeAdapter = new RecipeAdapter();

        mRecipeList.setAdapter(mRecipeAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey("recipes")) {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList("recipes");
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
//                        JSONArray response = u.getRecipes();
                        JSONArray response = u.getRecipes(MainActivity.this);
                        int recipesCount = response.length();
                        ArrayList<Recipe> recipes = new ArrayList<>();
                        for (int i = 0; i < recipesCount; i++) {
                            JSONObject jsonRecipe = response.getJSONObject(i);
                            Recipe recipe = new Recipe();
                            recipe.setId(jsonRecipe.getInt("id"));
                            recipe.setTitle(jsonRecipe.getString("name"));
                            recipe.setServings(jsonRecipe.getInt("servings"));
                            recipe.setImage(jsonRecipe.getString("image"));

                            JSONArray jsonIngredients = jsonRecipe.getJSONArray("ingredients");
                            int ingredientsCount = jsonIngredients.length();
                            ArrayList<Ingredient> ingredients = new ArrayList<>();

                            for (int j = 0; j < ingredientsCount; j++) {
                                JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);
                                Ingredient ingredient = new Ingredient();
                                ingredient.setQuantity(jsonIngredient.getDouble("quantity"));
                                ingredient.setIngredient(jsonIngredient.getString("ingredient"));
                                ingredient.setMeasure(jsonIngredient.getString("measure"));
                                ingredients.add(ingredient);
                            }
                            recipe.setIngredients(ingredients);

                            JSONArray jsonSteps = jsonRecipe.getJSONArray("steps");
                            int stepsCount = jsonSteps.length();
                            ArrayList<Step> steps = new ArrayList<>();

                            for (int j = 0; j < stepsCount; j++) {
                                JSONObject jsonStep = jsonSteps.getJSONObject(j);
                                Step step = new Step();
                                step.setId(jsonStep.getInt("id"));
                                step.setDescription(jsonStep.getString("description"));
                                step.setShortDescription(jsonStep.getString("shortDescription"));
                                step.setThumbnailURL(jsonStep.getString("thumbnailURL"));
                                step.setVideoURL(jsonStep.getString("videoURL"));
                                steps.add(step);
                            }

                            String imageThumb = "";
                            for (int j = --stepsCount; j > 0; j--) {
                                Step s = steps.get(j);
                                if (!s.getVideoURL().isEmpty()) {
                                    imageThumb = s.getVideoURL();
                                }
                                s.setThumbnailURL(imageThumb);
                            }
                            recipe.setSteps(steps);
                            recipe.setImage(imageThumb);
                            recipes.add(recipe);
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
        outState.putParcelableArrayList("recipes", mRecipeAdapter.getList());
        super.onSaveInstanceState(outState);
    }
}
