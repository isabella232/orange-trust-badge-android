/*
 * Copyright (C) 2016 Orange
 *
 * This software is distributed under the terms and conditions of the 'Apache-2.0'
 * license which can be found in the file 'LICENSE.txt' in this package distribution
 * or at 'http://www.apache.org/licenses/LICENSE-2.0'.
 */
/* Orange Trust Badge library
 *
 * Module name: com.orange.essentials:otb
 * Version:     1.0-SNAPSHOT
 * Created:     2016-02-09 by Aurore Penault and Vincent Boesch
 */
package com.orange.essentials.otb.manager

import android.support.v7.app.AppCompatActivity

import com.orange.essentials.otb.model.TrustBadgeElement

/**
 * This interface should be implemented by the calling app to listen to badge changes (when a trust badge is toggable)
 * Created by veeb7280 on 2/3/16.
 */
interface BadgeListener {

    /**
     * This method will be called whenever a toggable badge will be switched
     * @param trustBadgeElement
     * *
     * @param value
     */
    fun onBadgeChange(trustBadgeElement: TrustBadgeElement, value: Boolean, callingActivity: AppCompatActivity)
}
