package com.appsys.bakingapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.appsys.bakingapp.fragment.IngredientsFragment;
import com.appsys.bakingapp.fragment.StepFragment;
import com.appsys.bakingapp.fragment.StepsFragment;
import com.appsys.bakingapp.modal.Ingredient;
import com.appsys.bakingapp.modal.Recipe;
import com.appsys.bakingapp.modal.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements StepsFragment.StepsCallback {

    private Recipe mRecipe;
    private Ingredient mIngredient;
    private Step mStep;
    private Toast mToast;
    private boolean mPhone = true;
    private static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    private static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Intent data = getIntent();
            if (data != null && data.hasExtra("recipe")) {
                mRecipe = data.getParcelableExtra("recipe");
            }
            if (mRecipe == null) {
                showMessage("please contact to developer");
                finish();
                return;
            }
            ButterKnife.bind(this);

            StepsFragment stepsFragment = StepsFragment.newInstance(mRecipe);

            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.master_list_fragment, stepsFragment).addToBackStack(STACK_RECIPE_DETAIL)
            .commit();
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
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onIngredientClick(List<Ingredient> list) {
        IngredientsFragment stepsFragment = IngredientsFragment.newInstance(mRecipe.getIngredients());
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.master_list_fragment, stepsFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                .commit();
    }

    @Override
    public void onStepClick(List<Step> list, int position) {
        StepFragment stepsFragment = StepFragment.newInstance(mRecipe.getSteps(), position);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.master_list_fragment, stepsFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                .commit();
    }
}
