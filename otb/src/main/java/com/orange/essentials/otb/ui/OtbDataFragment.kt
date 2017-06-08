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
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.UserPermissionStatus
import com.orange.essentials.otb.ui.utils.ViewHelper

/**
 *
 *
 * File name:   OtbDataFragment
 *
 *
 * Fragment used to display the details for badges other than usage badges
 */
class OtbDataFragment : Fragment() {
    private val ignoreCheckedChange = false
    private var mMainll: LinearLayout? = null
    private var mOtherll: LinearLayout? = null
    private var mNoOtherLayout: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.otb_data, container, false)
        val headerTv = view.findViewById<TextView>(R.id.otb_header_tv_text)
        headerTv.setText(R.string.otb_home_data_content)
        val keyDataTitleText = view.findViewById<TextView>(R.id.otb_data_tv_main_data_title)
        keyDataTitleText.contentDescription = context!!.getString(R.string.otb_accessibility_category_description) + "  " + context!!.getString(R.string.otb_main_data_title)


        mMainll = view.findViewById<LinearLayout>(R.id.otb_data_ll_main_data)
        mOtherll = view.findViewById<LinearLayout>(R.id.otb_data_ll_other_data)
        mNoOtherLayout = view.findViewById(R.id.otb_data_tv_no_other_data)
        /** Adding listener on bottom button  */
        val bottomButton = view.findViewById<Button>(R.id.otb_data_bt_parameter)
        bottomButton.setOnClickListener { gotoPermissions() }

        return view
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        /** Manage ActionBar  */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.otb_home_data_title)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_PERMISSION_ENTER)
        //Refreshes the view according to permissions status
        refreshPermission()
    }

    /**
     * provide access to App permission screen (intent out of the fragment)
     */
    private fun gotoPermissions() {
        /** Code to access to the details settings of the package name  */
        val i = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + TrustBadgeManager.INSTANCE.applicationPackageName)
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_GO_TO_SETTINGS)
        context!!.startActivity(i)
    }

    private fun getToggleContentDescription(name: String, value: Boolean): String {
        return name + " " + if (value)
            context!!.getString(R.string.otb_accessibility_item_is_used_description)
        else
            context!!.getString(R.string.otb_accessibility_item_not_used_description)
    }

    fun refreshPermission() {
        TrustBadgeManager.INSTANCE.refreshTrustBadgePermission(context!!)
        val datas = TrustBadgeManager.INSTANCE.elementsForDataCollected
        mMainll!!.removeAllViews()
        mOtherll!!.removeAllViews()
        var isOtherTitleAdded = false
        for (data in datas!!) {
            val usageView = View.inflate(context, R.layout.otb_data_usage_item, null)
            ViewHelper.INSTANCE.buildView(usageView, data, context!!)
            if (data.elementType == ElementType.MAIN) {
                mMainll!!.addView(usageView)
            } else {
                mOtherll!!.addView(usageView)
                isOtherTitleAdded = true
            }

            if (data.isToggable) {
                val switchCompat = usageView.findViewById<SwitchCompat>(R.id.otb_data_usage_item_sc_switch)
                switchCompat.visibility = View.VISIBLE
                switchCompat.contentDescription = getToggleContentDescription(data.nameKey, switchCompat.isChecked)
                if (data.appUsesPermission == AppUsesPermission.TRUE) {
                    switchCompat.isEnabled = true
                    switchCompat.isChecked = data.userPermissionStatus == UserPermissionStatus.GRANTED
                }

                switchCompat.setOnCheckedChangeListener(
                        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                            switchCompat.contentDescription = getToggleContentDescription(data.nameKey, isChecked)
                            if (ignoreCheckedChange) {
                                return@OnCheckedChangeListener
                            }
                            TrustBadgeManager.INSTANCE.eventTagger?.tagElement(EventType.TRUSTBADGE_ELEMENT_TOGGLED, data)
                            TrustBadgeManager.INSTANCE.badgeChanged(data, isChecked, activity as AppCompatActivity)
                            TrustBadgeManager.INSTANCE.badgeChanged(data.groupType, isChecked, activity as AppCompatActivity)
                        }
                )
            }
        }

        if (!isOtherTitleAdded) {
            mOtherll!!.visibility = View.GONE
            mNoOtherLayout!!.visibility = View.VISIBLE
            /** Accessibility : Adding "other data" section  */
            val otherDataTitleText = mNoOtherLayout!!.findViewById<TextView>(R.id.otb_data_tv_other_data_title)
            if (otherDataTitleText != null) {
                otherDataTitleText.contentDescription = context!!.getString(R.string.otb_accessibility_category_description) + "  " + context!!.getString(R.string.otb_other_data_title)
            }
        } else {
            mOtherll!!.visibility = View.VISIBLE
            mNoOtherLayout!!.visibility = View.GONE

        }
    }

    companion object {
        val FRAG_TAG = "OtbDataFragment"
        private val TAG = "OtbDataFragment"
    }

}
