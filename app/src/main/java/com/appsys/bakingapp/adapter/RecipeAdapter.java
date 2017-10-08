package com.appsys.bakingapp.adapter;

import android.os.Binder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.modal.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private ArrayList<Recipe> mList;
    public RecipeAdapter(){
    }

    public void swap(ArrayList<Recipe> recipes){
        mList = recipes;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getList() {
        return mList;
    }

    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {
        Recipe r = mList.get(position);
        holder.bind(r.getTitle(), r.getServings());
    }

    @Override
    public int getItemCount() {
        return (mList==null) ? 0 : mList.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_item_title)
        TextView mTitle;

        @BindView(R.id.recipe_item_serving)
        TextView mServing;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String title, int server) {
            mTitle.setText(title);
            mServing.setText(String.valueOf(server));
        }
    }
}
