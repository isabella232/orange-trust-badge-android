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
package com.orange.essentials.otb.ui.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus

/**
 *
 *
 * File name:   ViewHelper
 * Created by:  Vincent Boesch
 *
 *
 * Helper for building a view representing a badge element in the ui
 */
enum class ViewHelper {
    INSTANCE;

    fun buildView(view: View, element: TrustBadgeElement, context: Context) {
        val icon = view.findViewById<ImageView>(R.id.otb_data_usage_item_iv_icon) as ImageView
        val titleTv = view.findViewById<TextView>(R.id.otb_data_usage_item_tv_title) as TextView
        val statusTv = view.findViewById<TextView>(R.id.otb_data_usage_item_tv_status) as TextView
        val childIndicator = view.findViewById<ImageView>(R.id.otb_data_usage_item_iv_child_indicator) as ImageView
        val descriptionTv = view.findViewById<TextView>(R.id.otb_data_usage_item_tv_description) as TextView
        val itemlayout = view.findViewById<View>(R.id.otb_data_usage_item_ll)

        icon.setImageDrawable(context.resources.getDrawable(element.disabledIconId))
        titleTv.text = element.nameKey
        //for accessibility
        titleTv.contentDescription = titleTv.text.toString() + "  " + context.getString(R.string.otb_accessibility_item_open_row_description)

        if (element.groupType == GroupType.PEGI || element.isToggable && element.appUsesPermission == AppUsesPermission.TRUE) {
            statusTv.visibility = View.GONE
        } else {
            if ((element.appUsesPermission == AppUsesPermission.TRUE || element.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT) && (element.userPermissionStatus == UserPermissionStatus.GRANTED || element.userPermissionStatus == UserPermissionStatus.MANDATORY)) {
                statusTv.setTextColor(context.resources.getColor(R.color.colorAccent))
                statusTv.text = context.getString(R.string.otb_toggle_button_granted)
            } else {
                statusTv.setTextColor(context.resources.getColor(R.color.otb_black))
                statusTv.text = context.resources.getString(R.string.otb_toggle_button_not_granted)
            }
        }

        childIndicator.setImageDrawable(context.resources.getDrawable(R.drawable.otb_ic_expand_more))
        val tagMoreValue = "more"
        val tagLessValue = "less"

        childIndicator.tag = tagMoreValue
        descriptionTv.text = element.descriptionKey
        val expandClick = View.OnClickListener {
            TrustBadgeManager.INSTANCE.eventTagger?.tagElement(EventType.TRUSTBADGE_ELEMENT_TAPPED, element)
            if (childIndicator.tag == tagMoreValue) {
                childIndicator.setImageDrawable(context.resources.getDrawable(R.drawable.otb_ic_expand_less))
                childIndicator.tag = tagLessValue
                expand(descriptionTv, view)
                titleTv.contentDescription = titleTv.text.toString() + "  " + context.getString(R.string.otb_accessibility_item_close_row_description)
            } else {
                childIndicator.setImageDrawable(context.resources.getDrawable(R.drawable.otb_ic_expand_more))
                childIndicator.tag = tagMoreValue
                collapse(descriptionTv)
                titleTv.contentDescription = titleTv.text.toString() + "  " + context.getString(R.string.otb_accessibility_item_open_row_description)
            }
        }
        itemlayout.setOnClickListener(expandClick)
    }

    private fun expand(detailView: View, layoutView: View) {
        detailView.visibility = View.VISIBLE
        val widthSpec = View.MeasureSpec.makeMeasureSpec(layoutView.width - layoutView.paddingLeft - layoutView.paddingRight, View.MeasureSpec.AT_MOST)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        detailView.measure(widthSpec, heightSpec)
        val mAnimator = slideAnimator(detailView, 0, detailView.measuredHeight)
        mAnimator.start()
    }

    private fun collapse(detailView: View) {
        val finalHeight = detailView.height
        val mAnimator = slideAnimator(detailView, finalHeight, 0)
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                //Nothing to do here
            }

            override fun onAnimationEnd(animator: Animator) {
                detailView.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
                //Nothing to do here
            }

            override fun onAnimationRepeat(animation: Animator) {
                //Nothing to do here
            }
        })
        mAnimator.start()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun slideAnimator(view: View, start: Int, end: Int): ValueAnimator {
        val animator = ValueAnimator.ofInt(start, end)

        animator.addUpdateListener { valueAnimator ->
            //Update Height
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = value
            view.layoutParams = layoutParams
        }
        return animator
    }

}
