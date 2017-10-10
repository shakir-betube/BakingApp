package com.appsys.bakingapp.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.adapter.IngredientAdapter;
import com.appsys.bakingapp.modal.Ingredient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    private static final String ARG_LIST_KEY = "list_ingredient";
    ArrayList<Ingredient> mIngredients;


    public IngredientsFragment() {
        // Required empty public constructor
    }

    public static IngredientsFragment newInstance(ArrayList<Ingredient> ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_KEY, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredients = getArguments().getParcelableArrayList(ARG_LIST_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.ingredient_list, container, false);

        RecyclerView recyclerView = itemView.findViewById(R.id.recipe_ingredient_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new IngredientAdapter(mIngredients));

        return itemView;
    }

}
