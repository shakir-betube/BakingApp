package com.appsys.bakingapp.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.adapter.StepsAdapter;
import com.appsys.bakingapp.model.Ingredient;
import com.appsys.bakingapp.model.Recipe;
import com.appsys.bakingapp.model.Step;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private static final String ARG_RECIPE_KEY = "recipe";
    Recipe mRecipe;
    StepsCallback mCallback;

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(Recipe recipe) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE_KEY, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StepsCallback) {
            mCallback = (StepsCallback) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement StepsFragment.StepsCallback");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof StepsCallback) {
            mCallback = (StepsCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepsFragment.StepsCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.fragment_steps, container, false);
        itemView.findViewById(R.id.ingredients_of_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback == null)
                    return;

                mCallback.onIngredientClick(mRecipe.getIngredients());
            }
        });

        RecyclerView recyclerView = itemView.findViewById(R.id.steps_of_recipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new StepsAdapter(mRecipe.getSteps(), new StepsAdapter.CallbackStepList() {
            @Override
            public void onClick(int position) {
                if (mCallback == null)
                    return;

                mCallback.onStepClick(mRecipe.getSteps(), position);
            }
        }));

        return itemView;
    }

    public interface StepsCallback {
        void onIngredientClick(List<Ingredient> list);

        void onStepClick(List<Step> list, int position);
    }
}
