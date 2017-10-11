package com.appsys.bakingapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsys.bakingapp.R;
import com.appsys.bakingapp.modal.Ingredient;
import com.appsys.bakingapp.modal.Step;
import com.appsys.utils.Utils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepFragment extends Fragment {

    private static final String ARG_LIST_KEY = "list_step";
    private static final String ARG_CURRENT_KEY = "current_step";
    ArrayList<Step> mStep;
    int mCurrent = 0;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

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
        Step step = mStep.get(mCurrent);
        TextView textView = itemView.findViewById(R.id.recipe_description);
        textView.setText((mCurrent+1) +") " +step.getDescription());

        mExoPlayerView = itemView.findViewById(R.id.player_view);
        if (step.getVideoURL().isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
        } else {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }

        return itemView;
    }
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Utils.getUserAgent(getActivity(), "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}