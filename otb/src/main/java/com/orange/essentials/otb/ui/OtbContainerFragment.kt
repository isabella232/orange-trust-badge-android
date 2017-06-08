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

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.R
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus

/**
 * Main fragment to display otb
 */
class OtbContainerFragment : android.support.v4.app.Fragment() {
    private val TAG = "OtbContainerFragment"
    private val KEY_MAIN_SELECTED = "key_main_selected"
    private val DATA_SELECTED = 0
    private val USAGE_SELECTED = 1
    private val TERM_SELECTED = 2
    private var mListener: OtbFragmentListener? = null
    private var mFragmentSelected = DATA_SELECTED
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MAIN_SELECTED, mFragmentSelected)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.otb_home, container, false)
        val headerTv = view.findViewById<TextView>(R.id.otb_header_tv_text)
        headerTv.setText(R.string.otb_home_header_title)
        return view
    }

    override fun onResume() {
        Log.d(TAG, "Resuming Fragment")
        super.onResume()
        /** Manage ActionBar title */
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setTitle(R.string.otb_home_title)
        /** refreshing data  */
        TrustBadgeManager.INSTANCE.refreshTrustBadgePermission(context!!)
        buildCards()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Setting complementary infos about Accessibility  */
        /** data  */
        setTitleAndLabel(R.id.otb_home_data_card,
                R.id.otb_home_data_card_tv_title,
                R.id.otb_home_data_card_tv_content,
                R.string.otb_home_data_title,
                R.string.otb_home_data_content,
                R.string.otb_accessibility_title_description
        )
        /** Usage  */
        setTitleAndLabel(R.id.otb_home_usage_card,
                R.id.otb_home_data_card_tv_title,
                R.id.otb_home_data_card_tv_content,
                R.string.otb_home_usage_title,
                R.string.otb_home_usage_content,
                R.string.otb_accessibility_title_description
        )
        /** terms  */
        setTitleAndLabel(R.id.otb_home_terms_card,
                R.id.otb_home_terms_card_tv_commitment_title,
                0,
                R.string.otb_home_terms_title,
                0,
                R.string.otb_accessibility_title_description
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            initListeners(savedInstanceState.getInt(KEY_MAIN_SELECTED))
        } else {
            initListeners(DATA_SELECTED)
        }

        if (activity is OtbFragmentListener) {
            mListener = activity as OtbFragmentListener
        } else {
            throw RuntimeException("Activity must implements ContainerFragmentListener")
        }


    }
    /**
     * *******************************************************************************************
     * ***********************    PRIVATE METHOD DATA INITIALISATION *****************************
     * *******************************************************************************************
     */
    /**
     * Convenient method used to fill information title, label and accessbility string
     */
    private fun setTitleAndLabel(viewId: Int, titleId: Int, contentId: Int, titleRes: Int, contentRes: Int, accessibilityRes: Int) {
        var dataTitle: TextView?
        var dataDetail: TextView?
        val container = this.getActivity()!!.findViewById<View>(viewId)
        dataTitle = container.findViewById<TextView>(titleId)
        dataDetail = container.findViewById<TextView>(contentId)
        if (dataTitle == null) {
            dataTitle = getActivity()!!.findViewById<TextView>(viewId)
        }
        dataTitle?.setText(titleRes)
        dataTitle?.setContentDescription(getString(accessibilityRes) + "  " + getString(titleRes))
        dataDetail?.setText(contentRes)
    }

    /**
     * Add the policy buttons ont appropriate layout
     */
    private fun buildCards() {
        Log.d(TAG, "buildCards")
        var dataLayout: LinearLayout? = null
        var usageLayout: LinearLayout? = null
        val containerData = this.getActivity()!!.findViewById<View?>(R.id.otb_home_data_card)
        containerData?.setVisibility(if (TrustBadgeManager.INSTANCE.hasData()) View.VISIBLE else View.GONE)
        dataLayout = containerData?.findViewById<LinearLayout?>(R.id.otb_home_data_card_ll_container)
        val containerUsage = this.getActivity()!!.findViewById<View?>(R.id.otb_home_usage_card)
        containerUsage?.setVisibility(if (TrustBadgeManager.INSTANCE.hasUsage()) View.VISIBLE else View.GONE)
        usageLayout = containerUsage?.findViewById<LinearLayout?>(R.id.otb_home_data_card_ll_container)
        val containerTerms = this.getActivity()!!.findViewById<View?>(R.id.otb_home_terms_card)

        containerTerms?.setVisibility(if (TrustBadgeManager.INSTANCE.hasTerms()) View.VISIBLE else View.GONE)

        dataLayout?.removeAllViews()
        usageLayout?.removeAllViews()
        val trustBadgeElements = TrustBadgeManager.INSTANCE.trustBadgeElements
        if (trustBadgeElements != null) {
            for (trustBadgeElement in trustBadgeElements) {
                Log.d(TAG, trustBadgeElement.toString())

                if (trustBadgeElement.elementType == ElementType.MAIN || trustBadgeElement.elementType == ElementType.USAGE) {
                    val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.otb_custom_toggle_button, null)
                    val icon = view.findViewById<ImageView>(R.id.otb_custom_toggle_button_iv_id)
                    icon.setContentDescription(trustBadgeElement.nameKey)
                    val buttonText = view.findViewById<TextView>(R.id.otb_custom_toggle_button_tv_id)
                    if (trustBadgeElement.groupType != GroupType.PEGI) {
                        if ((trustBadgeElement.appUsesPermission == AppUsesPermission.TRUE) || (trustBadgeElement.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT)) {
                            if (trustBadgeElement.userPermissionStatus == UserPermissionStatus.GRANTED || trustBadgeElement.userPermissionStatus == UserPermissionStatus.MANDATORY) {
                                buttonText.setText(getResources().getString(R.string.otb_toggle_button_granted))
                                buttonText.setTextColor(getResources().getColor(R.color.colorAccent))
                                icon.setImageDrawable(getResources().getDrawable(trustBadgeElement.enabledIconId))
                            } else {
                                buttonText.setText(getResources().getString(R.string.otb_toggle_button_not_granted))
                                buttonText.setTextColor(getResources().getColor(R.color.otb_black))
                                icon.setImageDrawable(getResources().getDrawable(trustBadgeElement.disabledIconId))
                            }
                        } else {
                            buttonText.setText(getResources().getString(R.string.otb_toggle_button_not_granted))
                            icon.setImageDrawable(getResources().getDrawable(trustBadgeElement.disabledIconId))
                        }
                    } else {
                        icon.setImageDrawable(getResources().getDrawable(trustBadgeElement.enabledIconId))
                        buttonText.setText(TrustBadgeManager.INSTANCE.getPegiAge(trustBadgeElement) + getString(R.string.otb_home_usage_pegi_age))
                        buttonText.setTextColor(getResources().getColor(R.color.otb_black))
                    }

                    if (trustBadgeElement.elementType == ElementType.MAIN) {
                        dataLayout!!.addView(view)
                        view.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(v: View) {
                                mListener!!.onDataClick()
                            }
                        })
                    } else {
                        usageLayout!!.addView(view)
                        view.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(v: View) {
                                mListener!!.onUsageClick()
                            }
                        })
                    }
                }
            }
        }
    }

    /***********************************************************************************************
     * ***********************    PRIVATE METHOD FOR PERMISSIONS Listener     ************************
     * ********************************************************************************************
     * @param fragmentSelected the selected fragment identifier
     */
    private fun initListeners(fragmentSelected: Int) {
        val dataCardLayout = getActivity()!!.findViewById<View>(R.id.otb_home_data_card)
        val usageCardLayout = getActivity()!!.findViewById<View>(R.id.otb_home_usage_card)
        val termsCardLayout = getActivity()!!.findViewById<View>(R.id.otb_home_terms_card)
        if (dataCardLayout is TextView) {
            when (fragmentSelected) {
                USAGE_SELECTED -> {
                    usageCardLayout.setSelected(true)
                    mFragmentSelected = USAGE_SELECTED
                }
                TERM_SELECTED -> {
                    termsCardLayout.setSelected(true)
                    mFragmentSelected = TERM_SELECTED
                }
                DATA_SELECTED -> {
                    dataCardLayout.setSelected(true)
                    mFragmentSelected = DATA_SELECTED
                }
                else -> {
                    dataCardLayout.setSelected(true)
                    mFragmentSelected = DATA_SELECTED
                }
            }
        }
        dataCardLayout.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (dataCardLayout is TextView) {
                    mFragmentSelected = DATA_SELECTED
                    dataCardLayout.setSelected(true)
                    usageCardLayout.setSelected(false)
                    termsCardLayout.setSelected(false)
                }
                mListener!!.onDataClick()
            }
        })
        usageCardLayout.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (dataCardLayout is TextView) {
                    mFragmentSelected = USAGE_SELECTED
                    dataCardLayout.setSelected(false)
                    usageCardLayout.setSelected(true)
                    termsCardLayout.setSelected(false)
                }
                mListener!!.onUsageClick()
            }
        })
        termsCardLayout.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (dataCardLayout is TextView) {
                    mFragmentSelected = TERM_SELECTED
                    dataCardLayout.setSelected(false)
                    usageCardLayout.setSelected(false)
                    termsCardLayout.setSelected(true)
                }
                mListener!!.onTermsClick()
            }
        })
    }

    /**
     * Interface of "ContainerFragment" listener
     * this listener notify the ContainerFragments events to host application
     */
    interface OtbFragmentListener {
        fun onDataClick()
        fun onUsageClick()
        fun onTermsClick()
    }

    companion object {
        val FRAG_TAG = "OtbContainerFragment"
        fun newInstance(): OtbContainerFragment {
            return OtbContainerFragment()
        }
    }

}
