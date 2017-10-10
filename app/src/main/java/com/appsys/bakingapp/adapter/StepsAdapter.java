package com.appsys.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.modal.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {

    private List<Step> mList;
    private CallbackStepList mCallback;

    public StepsAdapter(List<Step> steps, CallbackStepList callbackStepList) {
        mList = steps;
        mCallback = callbackStepList;
    }

    public List<Step> getList() {
        return mList;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item_step, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {
        Step s = mList.get(position);
        holder.bind(s);
    }

    @Override
    public int getItemCount() {
        return (mList == null) ? 0 : mList.size();
    }

    public interface CallbackStepList {
        void onClick(int position);
    }

    class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_item_title)
        TextView mTitle;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Step step) {
            mTitle.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCallback.onClick(position);
        }
    }
}
