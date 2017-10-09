package com.appsys.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.modal.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    private List<Ingredient> mList;
    public IngredientAdapter(List<Ingredient> ingredients){
        mList = ingredients;
    }

    public List<Ingredient> getList() {
        return mList;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item_ingredient, parent, false);
        return new IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        Ingredient r = mList.get(position);
        holder.bind(r.getIngredient(), r.getMeasure(), r.getQuantity());
    }

    @Override
    public int getItemCount() {
        return (mList==null) ? 0 : mList.size();
    }

    class IngredientHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_ingredient_title)
        TextView mIngredient;

        @BindView(R.id.recipe_ingredient_quantity)
        TextView mQuantity;

        @BindView(R.id.recipe_ingredient_measure)
        TextView mMeasure;

        public IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String ingredient, String measure, double quantity) {
            mIngredient.setText(ingredient);
            mMeasure.setText(measure);
            mQuantity.setText(String.valueOf(quantity));
        }
    }
}
