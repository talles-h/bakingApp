package com.example.forrest.bakingapp.stepdetails;


import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.forrest.bakingapp.Injection;
import com.example.forrest.bakingapp.R;
import com.example.forrest.bakingapp.data.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment implements StepDetailsContract.View,
        ExoPlayer.EventListener  {

    private static final String TAG = "StepDetailsFragment";
    private static final String ARGUMENT_RECIPE_ID = "recipe_id_arg";
    private static final String ARGUMENT_STEP_ID = "step_id_arg";

    private static final String KEY_POSITION = "playback_position";

    private StepDetailsContract.Presenter mPresenter;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView mStepDescTextView;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private long mPreviousPosition = -1;
    private boolean mShouldRestorePosition = false;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(int recipeId, int stepId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_RECIPE_ID, recipeId);
        arguments.putInt(ARGUMENT_STEP_ID, stepId);
        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    public void setStepId(int id) {
        this.getArguments().putInt(ARGUMENT_STEP_ID, id);
        if (mPresenter != null) {
            mPresenter.setStepId(id);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StepDetailsPresenter(getArguments().getInt(ARGUMENT_RECIPE_ID),
                getArguments().getInt(ARGUMENT_STEP_ID), Injection.provideTasksRepository(getContext()),
                this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step_details, container, false);

        mStepDescTextView = root.findViewById(R.id.step_description);
        mPlayerView = root.findViewById(R.id.playerView);

        Log.d(TAG, "onCreateView");
        mShouldRestorePosition = false;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_POSITION)) {
                Log.d(TAG, "pos must be retored");
                mPreviousPosition = savedInstanceState.getLong(KEY_POSITION);
                mShouldRestorePosition = true;
            }
        }

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            mPreviousPosition = mExoPlayer.getCurrentPosition();
            mShouldRestorePosition = true;
            outState.putLong(KEY_POSITION, mExoPlayer.getCurrentPosition());
        }
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this.getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        String userAgent = Util.getUserAgent(this.getContext(), "BakingApp");

        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }

        if (mShouldRestorePosition) {
            Log.d(TAG, "retoring position");
            mExoPlayer.seekTo(mPreviousPosition);
            mShouldRestorePosition = false;
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    /*** ========== StepDetailsContract.View methods ========== ***/

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showStepDetails(RecipeStep step) {
        mStepDescTextView.setText(step.getDescription());

        if (step.getVideoUrl() != null && !step.getVideoUrl().equals("")) {
            initializeMediaSession();
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(step.getVideoUrl()));
        } else if (step.getThumbnailUrl() != null && !step.getThumbnailUrl().equals("")){
            initializeMediaSession();
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(step.getThumbnailUrl()));
        }

    }

    @Override
    public void setPresenter(StepDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }



    /*** ========== Exoplayer Listener methods ========== ***/

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_ENDED) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }



    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }


        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }


        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

    }
}
