/*
 * Copyright 2016 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Orange Trust Badge library
 *
 * Module name: com.orange.essentials:otb
 * Version:     1.0
 * Created:     2016-03-15 by Aurore Penault, Vincent Boesch, and Giovanni Battista Accetta
 */
package com.orange.essentials.otb.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.essentials.otb.R;
import com.orange.essentials.otb.event.EventType;
import com.orange.essentials.otb.manager.TrustBadgeManager;
import com.orange.essentials.otb.model.Term;
import com.orange.essentials.otb.model.type.TermType;
import com.orange.essentials.otb.ui.utils.AutoResizingFrameLayout;
import com.orange.essentials.otb.ui.utils.VideoControllerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * File name:   OtbTermsFragment
 * <p/>
 * Copyright (C) 2015 Orange
 * <p/>
 * Fragment used to display the term conditions of this lib
 */
public class OtbTermsFragment extends Fragment {

    public static final String FRAG_TAG = "OtbTermsFragment";
    private View mView;
    private List<View> mVideoViews = new ArrayList<>();
    private List<MediaPlayer> mPlayers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.otb_terms, container, false);
        LinearLayout llayout = (LinearLayout) mView.findViewById(R.id.otb_terms_layout);
        TextView headerTv = (TextView) llayout.findViewById(R.id.otb_header_tv_text);
        headerTv.setText(R.string.otb_home_terms_content);
        List<Term> terms = TrustBadgeManager.INSTANCE.getTerms();

        for (Term term : terms) {
            View layoutToAdd = null;
            if (term.getTermType() == TermType.VIDEO && Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                layoutToAdd = View.inflate(getActivity(), R.layout.otb_terms_video, null);
                TextView titleTv = (TextView) layoutToAdd.findViewById(R.id.otb_term_video_title);
                final AutoResizingFrameLayout anchorView = (AutoResizingFrameLayout) layoutToAdd.findViewById(R.id.videoSurfaceContainer);
                if (mVideoViews == null) {
                    mVideoViews = new ArrayList<>();
                }
                mVideoViews.add(anchorView);
                SurfaceView videoSurface = (SurfaceView) layoutToAdd.findViewById(R.id.videoSurface);

                titleTv.setText(term.getTitleId());

                SurfaceHolder videoHolder = videoSurface.getHolder();
                MediaPlayer player = new MediaPlayer();
                if (mPlayers == null) {
                    mPlayers = new ArrayList<>();
                }
                mPlayers.add(player);
                final VideoControllerView controller = new VideoControllerView(getContext());
                VideoCallback videoCallback = new VideoCallback(player, controller, anchorView);
                videoHolder.addCallback(videoCallback);
                anchorView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        controller.show();
                        return false;
                    }
                });
                try {
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(getContext(), Uri.parse(getString(term.getContentId())));
                    player.setOnPreparedListener(videoCallback);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (term.getTermType() == TermType.TEXT) {//TermType.TEXT
                layoutToAdd = View.inflate(getActivity(), R.layout.otb_terms_text, null);
                TextView titleTv = (TextView) layoutToAdd.findViewById(R.id.otb_term_text_title);
                TextView contentTv = (TextView) layoutToAdd.findViewById(R.id.otb_term_text_content);
                titleTv.setText(term.getTitleId());
                contentTv.setText(Html.fromHtml(getString(term.getContentId())));
                contentTv.setMovementMethod(LinkMovementMethod.getInstance());
                contentTv.setLinkTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (null != layoutToAdd) {
                llayout.addView(layoutToAdd);
            }
            if (terms.indexOf(term) != terms.size() - 1) {
                View.inflate(getActivity(), R.layout.otb_separator, llayout);
            }
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.otb_home_terms_title);
        }
        TrustBadgeManager.INSTANCE.getEventTagger().tag(EventType.TRUSTBADGE_TERMS_ENTER);
    }

    @Override
    public void onPause() {
        super.onPause();
        for (final MediaPlayer player : mPlayers) {
            if (player.isPlaying()) player.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (final MediaPlayer player : mPlayers) {
            player.stop();
        }
    }

    class VideoCallback implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

        private MediaPlayer player;
        private VideoControllerView controller;
        private FrameLayout anchorView;
        private boolean prepared = false;

        public VideoCallback(MediaPlayer player, VideoControllerView controller, FrameLayout anchorView) {
            this.player = player;
            this.controller = controller;
            this.anchorView = anchorView;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
            try {
                player.prepareAsync();
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (prepared & !isPlaying()) anchorView.findViewById(R.id.otb_play_icon_holder).setVisibility(View.VISIBLE);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void start() {
            anchorView.findViewById(R.id.otb_play_icon_holder).setVisibility(View.GONE);
            player.start();
        }

        @Override
        public void pause() {
            anchorView.findViewById(R.id.otb_play_icon_holder).setVisibility(View.VISIBLE);
            player.pause();
        }

        @Override
        public int getDuration() {
            return player.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }

        @Override
        public void seekTo(int pos) {
            player.seekTo(pos);
        }

        @Override
        public boolean isPlaying() {
            return player.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public boolean isFullScreen() {
            return false;
        }

        @Override
        public void toggleFullScreen() {

        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            controller.setMediaPlayer(this);
            controller.setAnchorView(anchorView);
            anchorView.findViewById(R.id.otb_video_loader).setVisibility(View.GONE);
            anchorView.findViewById(R.id.otb_play_icon_holder).setVisibility(View.VISIBLE);
            prepared = true;
        }
    }

}
