package com.appsys.bakingapp.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsys.bakingapp.DetailActivity;
import com.appsys.bakingapp.R;
import com.appsys.bakingapp.model.Recipe;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private ArrayList<Recipe> mList;

    public RecipeAdapter() {
    }

    public void swap(ArrayList<Recipe> recipes) {
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
        holder.bind(r.getTitle(), r.getServings(), r.getImage());
    }

    @Override
    public int getItemCount() {
        return (mList == null) ? 0 : mList.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_item_title)
        TextView mTitle;

        @BindView(R.id.recipe_item_image)
        ImageView mImage;

        @BindView(R.id.recipe_item_serving)
        TextView mServing;


        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(String title, int server, String image) {
            mTitle.setText(title);
            mServing.setText(String.valueOf(server));
            if (!TextUtils.isEmpty(image)) {
                Glide.with(itemView.getContext())
                        .asBitmap()
                        .load(image)
                        .into(mImage);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe r = mList.get(position);
            Intent i = new Intent(itemView.getContext(), DetailActivity.class);
            i.putExtra("recipe", r);
            itemView.getContext().startActivity(i);
        }
    }
}
