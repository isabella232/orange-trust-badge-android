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

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * <p/>
 * File name:   AutoResizingFrameLayout
 * Created by:  Giovanni Battista Accetta
 * <p/>
 * A view which adapt its actual height on the varying width when rotating screen,
 * by mantaining a 16/9 ration
 */
public class AutoResizingFrameLayout extends FrameLayout {

    public AutoResizingFrameLayout(Context context) {
        super(context);
    }

    public AutoResizingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizingFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            int height = (int) ((float) w * 9 / 16);
            setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
