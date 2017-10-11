package com.appsys.bakingapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.modal.Ingredient;
import com.appsys.bakingapp.modal.Step;

import java.util.ArrayList;

public class StepFragment extends Fragment {

    private static final String ARG_LIST_KEY = "list_step";
    private static final String ARG_CURRENT_KEY = "current_step";
    ArrayList<Step> mStep;
    int mCurrent = 0;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(ArrayList<Step> steps, int current) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_KEY, steps);
        args.putInt(ARG_CURRENT_KEY, current);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelableArrayList(ARG_LIST_KEY);
            mCurrent = getArguments().getInt(ARG_CURRENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.step_of_recipe, container, false);
        TextView textView = itemView.findViewById(R.id.recipe_description);
        textView.setText((mCurrent+1) +") " +mStep.get(mCurrent).getDescription());
        return itemView;
    }
}
