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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.logger.Logger
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.GroupType
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
class OtbAppDataFragment : Fragment() {
    private var appDataLinearLayout: LinearLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scrollView = inflater.inflate(R.layout.otb_app_data, container, false)
        appDataLinearLayout = scrollView.findViewById(R.id.otb_app_data_layout)
        populateView()
        return scrollView
    }

    private fun getToggleContentDescription(name: String, value: Boolean): String {
        return "$name " + if (value)
            context!!.getString(R.string.otb_accessibility_item_is_used_description)
        else
            context!!.getString(R.string.otb_accessibility_item_not_used_description)
    }

    override fun onResume() {
        super.onResume()
        /** Manage ActionBar  */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.otb_home_app_data_title)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_USAGE_ENTER)
        refreshAppData()
    }

    private fun populateView() {
        val view = appDataLinearLayout!!
        view.removeAllViews()
        val appDatas = TrustBadgeManager.INSTANCE.appDataElements
        Logger.v(TAG, "appDatas elements: " + appDatas!!.size)
        for (appData in appDatas) {
            val appDataView = View.inflate(context, R.layout.otb_data_usage_item, null)
            ViewHelper.INSTANCE.buildView(appDataView, appData, context!!, GroupType.NOTIFICATIONS.equals(appData.groupType))
            val tvPerm = appDataView.findViewById<TextView>(R.id.otb_data_usage_tv_goto)
            tvPerm.setOnClickListener { gotoPermissions() }

            Logger.v(TAG, "add appData view")
            view.addView(appDataView)
            if (appData.isToggable) {
                val switchCompat = appDataView.findViewById<SwitchCompat>(R.id.otb_data_usage_item_sc_switch)
                switchCompat.visibility = View.VISIBLE
                switchCompat.contentDescription = getToggleContentDescription(appData.nameKey, switchCompat.isChecked)
                if (appData.appUsesPermission == AppUsesPermission.TRUE) {
                    switchCompat.isEnabled = true
                    switchCompat.isChecked = appData.userPermissionStatus == UserPermissionStatus.GRANTED
                }

                switchCompat.setOnCheckedChangeListener { _, isChecked ->
                    switchCompat.contentDescription = getToggleContentDescription(appData.nameKey, isChecked)
                    TrustBadgeManager.INSTANCE.eventTagger?.tagElement(EventType.TRUSTBADGE_ELEMENT_TOGGLED, appData)
                    TrustBadgeManager.INSTANCE.badgeChanged(appData, isChecked, activity as AppCompatActivity)
                }
            }

        }
    }

    fun refreshAppData() {
        TrustBadgeManager.INSTANCE.refreshTrustBadgePermission(context!!)
        populateView()
    }

    companion object {
        const val FRAG_TAG = "OtbAppDataFragment"
        const val TAG = "OtbAppDataFragment"
    }

    /**
     * provide access to App permission screen (intent out of the fragment)
     */
    private fun gotoPermissions() {
        /** Code to access to the details settings of the package name  */
        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + TrustBadgeManager.INSTANCE.applicationPackageName)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_GO_TO_SETTINGS)
        context!!.startActivity(i)
    }
}
