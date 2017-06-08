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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.UserPermissionStatus
import com.orange.essentials.otb.ui.utils.ViewHelper

/**
 *
 *
 * File name:   OtbUsageFragment
 *
 *
 * Copyright (C) 2015 Orange
 *
 *
 * Fragment used to display usage badges
 */
class OtbUsageFragment : Fragment() {
    private val ignoreCheckedChange = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrollView = inflater!!.inflate(R.layout.otb_usage, container, false)
        val view = scrollView.findViewById<LinearLayout>(R.id.otb_data_usage_layout)
        val header = View.inflate(context, R.layout.otb_header, null)
        val headerTv = header.findViewById<TextView>(R.id.otb_header_tv_text)
        headerTv.setText(R.string.otb_home_usage_content)
        view.addView(header)
        val usages = TrustBadgeManager.INSTANCE.elementsForUsage
        Log.v(TAG, "usages elements: " + usages!!.size)
        for (usage in usages) {
            val usageView = View.inflate(context, R.layout.otb_data_usage_item, null)
            ViewHelper.INSTANCE.buildView(usageView, usage, context!!)
            Log.v(TAG, "add usage view")
            view.addView(usageView)

            if (usage.isToggable) {
                val switchCompat = usageView.findViewById<SwitchCompat>(R.id.otb_data_usage_item_sc_switch)
                switchCompat.visibility = View.VISIBLE
                switchCompat.contentDescription = getToggleContentDescription(usage.nameKey, switchCompat.isChecked)
                if (usage.appUsesPermission == AppUsesPermission.TRUE) {
                    switchCompat.isEnabled = true
                    switchCompat.isChecked = usage.userPermissionStatus == UserPermissionStatus.GRANTED
                }

                switchCompat.setOnCheckedChangeListener(
                        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                            switchCompat.contentDescription = getToggleContentDescription(usage.nameKey, isChecked)
                            if (ignoreCheckedChange) {
                                return@OnCheckedChangeListener
                            }
                            TrustBadgeManager.INSTANCE.eventTagger?.tagElement(EventType.TRUSTBADGE_ELEMENT_TOGGLED, usage)
                            TrustBadgeManager.INSTANCE.badgeChanged(usage, isChecked, activity as AppCompatActivity)
                            TrustBadgeManager.INSTANCE.badgeChanged(usage.groupType, isChecked, activity as AppCompatActivity)
                        }
                )
            }

        }

        return scrollView
    }

    private fun getToggleContentDescription(name: String, value: Boolean): String {
        return name + " " + if (value)
            context!!.getString(R.string.otb_accessibility_item_is_used_description)
        else
            context!!.getString(R.string.otb_accessibility_item_not_used_description)
    }

    override fun onResume() {
        super.onResume()
        /** Manage ActionBar  */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.otb_home_usage_title)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_USAGE_ENTER)

    }

    companion object {
        val FRAG_TAG = "OtbUsageFragment"
        private val TAG = "OtbUsageFragment"
    }

}
