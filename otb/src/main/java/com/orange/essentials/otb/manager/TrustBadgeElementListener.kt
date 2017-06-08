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
package com.orange.essentials.otb.manager

import android.support.v7.app.AppCompatActivity

import com.orange.essentials.otb.model.type.GroupType

/**
 * This interface should be implemented by the calling app to listen to badge changes (when a trust badge is toggable)

 * Created by veeb7280 on 2/3/16.
 */
@Deprecated("use rather the interface BadgeListener, as this interface can't handle multiple groupType listening")
interface TrustBadgeElementListener {

    /**
     * This method will be called whenever a toggable badge will be switched
     * @param groupType
     * *
     * @param value
     */
    fun onBadgeChange(groupType: GroupType, value: Boolean, callingActivity: AppCompatActivity)
}
