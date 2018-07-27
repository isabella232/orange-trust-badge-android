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
package com.orange.essentials.otb.ui

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.type.TermType
import com.orange.essentials.otb.ui.utils.AutoResizingFrameLayout
import com.orange.essentials.otb.ui.utils.VideoControllerView
import java.io.IOException
import java.util.*

/**
 *
 *
 * File name:   OtbTermsFragment
 *
 *
 * Copyright (C) 2015 Orange
 *
 *
 * Fragment used to display the term conditions of this lib
 */
class OtbTermsFragment : Fragment() {

    private var mView: View? = null
    private var mVideoViews: MutableList<View>? = ArrayList()
    private var mPlayers: MutableList<MediaPlayer>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.otb_terms, container, false)
        val llayout = mView!!.findViewById<LinearLayout>(R.id.otb_terms_layout)
        val terms = TrustBadgeManager.INSTANCE.terms

        for (term in terms!!) {
            var layoutToAdd: View? = null
            if (term.termType == TermType.VIDEO) {
                layoutToAdd = View.inflate(activity, R.layout.otb_terms_video, null)
                val titleTv = layoutToAdd!!.findViewById<TextView>(R.id.otb_term_video_title)
                val anchorView = layoutToAdd.findViewById<AutoResizingFrameLayout>(R.id.videoSurfaceContainer)
                if (mVideoViews == null) {
                    mVideoViews = ArrayList()
                }
                mVideoViews!!.add(anchorView)
                val videoSurface = layoutToAdd.findViewById<SurfaceView>(R.id.videoSurface)

                titleTv.setText(term.titleId)
                val videoHolder = videoSurface.holder
                val player = MediaPlayer()
                if (mPlayers == null) {
                    mPlayers = ArrayList()
                }
                mPlayers!!.add(player)
                val controller = VideoControllerView(context!!)
                val videoCallback = VideoCallback(player, controller, anchorView)
                videoHolder.addCallback(videoCallback)
                anchorView.setOnTouchListener { _, _ ->
                    controller.show()
                    false
                }
                try {
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    player.setDataSource(context, Uri.parse(getString(term.contentId)))
                    player.setOnPreparedListener(videoCallback)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: SecurityException) {
                    e.printStackTrace()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (term.termType == TermType.TEXT) {//TermType.TEXT
                layoutToAdd = View.inflate(activity, R.layout.otb_terms_text, null)
                val titleTv = layoutToAdd!!.findViewById<TextView>(R.id.otb_term_text_title)
                val contentTv = layoutToAdd.findViewById<TextView>(R.id.otb_term_text_content)
                titleTv.setText(term.titleId)
                contentTv.text = Html.fromHtml(getString(term.contentId))
                contentTv.movementMethod = LinkMovementMethod.getInstance()
                contentTv.setLinkTextColor(resources.getColor(R.color.colorAccent))
            }
            if (null != layoutToAdd) {
                llayout.addView(layoutToAdd)
            }
            if (terms.indexOf(term) != terms.size - 1) {
                View.inflate(activity, R.layout.otb_separator, llayout)
            }
        }

        return mView
    }

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.otb_home_terms_title)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_TERMS_ENTER)
    }

    override fun onPause() {
        super.onPause()
        for (player in mPlayers!!) {
            if (player.isPlaying) player.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for (player in mPlayers!!) {
            player.stop()
        }
    }

    internal inner class VideoCallback(private val player: MediaPlayer, private val controller: VideoControllerView, private val anchorView: FrameLayout) : SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
        private var prepared = false
        override fun surfaceCreated(holder: SurfaceHolder) {
            player.setDisplay(holder)
            try {
                player.prepareAsync()
            } catch (ise: IllegalStateException) {
                ise.printStackTrace()
            }

        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            if (prepared and !isPlaying) anchorView.findViewById<View>(R.id.otb_play_icon_holder).visibility = View.VISIBLE
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {

        }

        override fun start() {
            anchorView.findViewById<View>(R.id.otb_play_icon_holder).visibility = View.GONE
            player.start()
        }

        override fun pause() {
            anchorView.findViewById<View>(R.id.otb_play_icon_holder).visibility = View.VISIBLE
            player.pause()
        }

        override val duration: Int
            get() = player.duration
        override val currentPosition: Int
            get() = player.currentPosition

        override fun seekTo(pos: Int) {
            player.seekTo(pos)
        }

        override val isPlaying: Boolean
            get() = player.isPlaying
        override val bufferPercentage: Int
            get() = 0

        override fun canPause(): Boolean {
            return true
        }

        override fun canSeekBackward(): Boolean {
            return true
        }

        override fun canSeekForward(): Boolean {
            return true
        }

        override val isFullScreen: Boolean
            get() = false

        override fun toggleFullScreen() {

        }

        override fun onPrepared(mp: MediaPlayer) {
            controller.setMediaPlayer(this)
            controller.setAnchorView(anchorView)
            anchorView.findViewById<View>(R.id.otb_video_loader).visibility = View.GONE
            anchorView.findViewById<View>(R.id.otb_play_icon_holder).visibility = View.VISIBLE
            prepared = true
        }
    }

    companion object {
        const val FRAG_TAG = "OtbTermsFragment"
    }

}
