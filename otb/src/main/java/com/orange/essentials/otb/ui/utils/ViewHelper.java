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
package com.orange.essentials.otb.ui.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orange.essentials.otb.R;
import com.orange.essentials.otb.event.EventType;
import com.orange.essentials.otb.manager.TrustBadgeManager;
import com.orange.essentials.otb.model.TrustBadgeElement;
import com.orange.essentials.otb.model.type.AppUsesPermission;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;

/**
 * <p/>
 * File name:   ViewHelper
 * Created by:  Vincent Boesch
 * <p/>
 * Helper for building a view representing a badge element in the ui
 */
public enum ViewHelper {

    INSTANCE;


    public void buildView(final View view, final TrustBadgeElement element, final Context context) {
        ImageView icon = (ImageView) view.findViewById(R.id.otb_data_usage_item_iv_icon);
        final TextView titleTv = (TextView) view.findViewById(R.id.otb_data_usage_item_tv_title);
        TextView statusTv = (TextView) view.findViewById(R.id.otb_data_usage_item_tv_status);
        final ImageView childIndicator = (ImageView) view.findViewById(R.id.otb_data_usage_item_iv_child_indicator);
        final TextView descriptionTv = (TextView) view.findViewById(R.id.otb_data_usage_item_tv_description);
        View itemlayout = view.findViewById(R.id.otb_data_usage_item_ll);

        icon.setImageDrawable(context.getResources().getDrawable(element.getDisabledIconId()));
        titleTv.setText(element.getNameKey());
        //for accessibility
        titleTv.setContentDescription(titleTv.getText() + "  " + context.getString(R.string.otb_accessibility_item_open_row_description));

        if (element.getGroupType() == GroupType.PEGI || element.isToggable() && element.getAppUsesPermission() == AppUsesPermission.TRUE) {
            statusTv.setVisibility(View.GONE);
        } else {
            if ((element.getAppUsesPermission() == AppUsesPermission.TRUE ||
                    element.getAppUsesPermission() == AppUsesPermission.NOT_SIGNIFICANT)
                    && (element.getUserPermissionStatus() == UserPermissionStatus.GRANTED ||
                    element.getUserPermissionStatus() == UserPermissionStatus.MANDATORY)) {
                statusTv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                statusTv.setText(context.getString(R.string.otb_toggle_button_granted));
            } else {
                statusTv.setTextColor(context.getResources().getColor(R.color.otb_black));
                statusTv.setText(context.getResources().getString(R.string.otb_toggle_button_not_granted));
            }
        }

        childIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.otb_ic_expand_more));
        final String tagMoreValue = "more";
        final String tagLessValue = "less";

        childIndicator.setTag(tagMoreValue);
        descriptionTv.setText(element.getDescriptionKey());

        View.OnClickListener expandClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrustBadgeManager.INSTANCE.getEventTagger().tagElement(EventType.TRUSTBADGE_ELEMENT_TAPPED, element);
                if (childIndicator.getTag().equals(tagMoreValue)) {
                    childIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.otb_ic_expand_less));
                    childIndicator.setTag(tagLessValue);
                    expand(descriptionTv, view);
                    titleTv.setContentDescription(titleTv.getText() + "  " + context.getString(R.string.otb_accessibility_item_close_row_description));
                } else {
                    childIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.otb_ic_expand_more));
                    childIndicator.setTag(tagMoreValue);
                    collapse(descriptionTv);
                    titleTv.setContentDescription(titleTv.getText() + "  " + context.getString(R.string.otb_accessibility_item_open_row_description));
                }
            }
        };
        itemlayout.setOnClickListener(expandClick);
    }

    @SuppressLint("NewApi")
    private void expand(final View detailView, final View layoutView) {
        detailView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final int widthSpec = View.MeasureSpec.makeMeasureSpec(layoutView.getWidth() - layoutView.getPaddingLeft() - layoutView.getPaddingRight(), View.MeasureSpec.AT_MOST);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            detailView.measure(widthSpec, heightSpec);
            ValueAnimator mAnimator = slideAnimator(detailView, 0, detailView.getMeasuredHeight());
            mAnimator.start();
        }
    }


    @SuppressLint("NewApi")
    private void collapse(final View detailView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            int finalHeight = detailView.getHeight();

            ValueAnimator mAnimator = slideAnimator(detailView, finalHeight, 0);
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    //Nothing to do here
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    detailView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    //Nothing to do here
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    //Nothing to do here
                }
            });
            mAnimator.start();
        } else {
            detailView.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ValueAnimator slideAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

}
