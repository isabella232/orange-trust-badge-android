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
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.orange.essentials.otb.OtbActivity
import com.orange.essentials.otb.R
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.UserPermissionStatus

/**
 * Main fragment to display otb
 */
class OtbContainerFragment : android.support.v4.app.Fragment() {
    private val keyMainSelected = "key_main_selected"
    private var mListener: OtbFragmentListener? = null
    private var mFragmentSelected = DATA_SELECTED
    private var layoutContainer: LinearLayout? = null
    private var hasPermissionMoreIcon = false
    private var hasAppDataMoreIcon = false
    private var hasAtLeastOnePermissionGranted = false
    private var hasAtLeastOneAppDataGranted = false
    private var dynamicPermissionGrantedText = ""
    private var dynamicAppDataGrantedText = ""
    private var nbPermissions = 0
    private var nbAppData = 0
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(keyMainSelected, mFragmentSelected)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.otb_home, container, false)
        val headerAppNameTv = view.findViewById<TextView>(R.id.otb_header_tv_appName)
        headerAppNameTv.text = TrustBadgeManager.INSTANCE.applicationName

        return view
    }

    override fun onResume() {
        Log.d(tag, "Resuming Fragment")
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
                R.string.otb_home_permissions_title,
                R.string.otb_accessibility_title_description
        )
        /** Usage  */
        setTitleAndLabel(R.id.otb_home_usage_card,
                R.id.otb_home_data_card_tv_title,
                R.string.otb_home_app_data_title,
                R.string.otb_accessibility_title_description
        )
        /** terms  */
        setTitleAndLabel(R.id.otb_home_terms_card,
                R.id.otb_home_terms_card_tv_commitment_title,
                R.string.otb_home_terms_title,
                R.string.otb_accessibility_title_description
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            initListeners(savedInstanceState.getInt(keyMainSelected))
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
    private fun setTitleAndLabel(viewId: Int, titleId: Int, titleRes: Int, accessibilityRes: Int) {
        var dataTitle: TextView?
        val container = activity!!.findViewById<View>(viewId)
        dataTitle = container.findViewById(titleId)
        if (dataTitle == null) {
            dataTitle = activity!!.findViewById(viewId)
        }
        dataTitle?.setText(titleRes)
        dataTitle?.contentDescription = getString(accessibilityRes) + "  " + getString(titleRes)
    }

    /**
     * Add the policy buttons ont appropriate layout
     */
    private fun buildCards() {
        Log.d(tag, "buildCards")
        layoutContainer = activity!!.findViewById(R.id.otb_home_ll_container)
        val permissionLayout: LinearLayout?
        val appDataLayout: LinearLayout?
        val permissionContainer = activity!!.findViewById<View?>(R.id.otb_home_data_card)
        permissionLayout = permissionContainer?.findViewById<LinearLayout?>(R.id.otb_home_data_card_ll_container)
        val dynamicPermissionTv = permissionContainer?.findViewById<TextView>(R.id.otb_home_data_card_tv_content)
        val appDataContainer = activity!!.findViewById<View?>(R.id.otb_home_usage_card)
        appDataLayout = appDataContainer?.findViewById<LinearLayout?>(R.id.otb_home_data_card_ll_container)
        val dynamicAppDataTv = appDataContainer?.findViewById<TextView>(R.id.otb_home_data_card_tv_content)
        val termsContainer = activity!!.findViewById<View?>(R.id.otb_home_terms_card)

        termsContainer?.visibility = if (TrustBadgeManager.INSTANCE.hasTerms()) View.VISIBLE else View.GONE

        permissionLayout?.removeAllViews()
        appDataLayout?.removeAllViews()
        val trustBadgeElements = TrustBadgeManager.INSTANCE.trustBadgeElements
        hasMoreIcons(trustBadgeElements)
        if (nbPermissions == 0) {
            dynamicPermissionTv?.text = getString(R.string.otb_home_permission_no_data)
            permissionLayout?.visibility = View.GONE
        } else if (!hasAtLeastOnePermissionGranted) {
            dynamicPermissionTv?.text = getString(R.string.otb_home_permission_no_granted)
            permissionLayout?.visibility = View.GONE
        } else {
            dynamicPermissionTv?.text = dynamicPermissionGrantedText
        }
        if (nbAppData == 0) {
            dynamicAppDataTv?.text = getString(R.string.otb_home_app_data_no_data)
            appDataLayout?.visibility = View.GONE
        } else if (!hasAtLeastOneAppDataGranted) {
            dynamicAppDataTv?.text = getString(R.string.otb_home_app_data_no_granted)
            appDataLayout?.visibility = View.GONE
        } else {
            dynamicAppDataTv?.text = dynamicAppDataGrantedText
        }
        var indexPermission = 0
        var indexUsage = 0
        if (TrustBadgeManager.INSTANCE.hasPermissions()) {
            val trustBadgeElementsPermissions = TrustBadgeManager.INSTANCE.permissionElements!!.sortedByDescending { it.userPermissionStatus == UserPermissionStatus.GRANTED }
            for (trustBadgeElement in trustBadgeElementsPermissions) {
                if (indexPermission < 5 && hasAtLeastOnePermissionGranted) {
                    val view = View.inflate(context, R.layout.otb_custom_toggle_button, null)
                    val icon = view.findViewById<ImageView>(R.id.otb_custom_toggle_button_iv_id)
                    icon.contentDescription = trustBadgeElement.nameKey
                    var textActivated = false
                    val buttonText = view.findViewById<TextView>(R.id.otb_custom_toggle_button_tv_id)
                    if ((trustBadgeElement.appUsesPermission == AppUsesPermission.TRUE) || (trustBadgeElement.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT)) {
                        if (trustBadgeElement.userPermissionStatus == UserPermissionStatus.GRANTED || trustBadgeElement.userPermissionStatus == UserPermissionStatus.MANDATORY) {
                            textActivated = true
                        }
                    }
                    customizeElementView(indexPermission, hasPermissionMoreIcon, icon, textActivated, buttonText, trustBadgeElement)
                    indexPermission++
                    permissionLayout?.addView(view)
                    view.setOnClickListener({ _ -> mListener!!.onCardClick(DATA_SELECTED) })
                }
            }
        }
        if (TrustBadgeManager.INSTANCE.hasAppData()) {
            val trustBadgeElementsAppDatas = TrustBadgeManager.INSTANCE.appDataElements!!.sortedByDescending { it.appUsesPermission != AppUsesPermission.FALSE && it.userPermissionStatus != UserPermissionStatus.NOT_GRANTED }
            for (trustBadgeElement in trustBadgeElementsAppDatas) {
                Log.d(tag, trustBadgeElement.toString())
                if (indexUsage < 5 && hasAtLeastOneAppDataGranted) {
                    val view = View.inflate(context, R.layout.otb_custom_toggle_button, null)
                    val icon = view.findViewById<ImageView>(R.id.otb_custom_toggle_button_iv_id)
                    icon.contentDescription = trustBadgeElement.nameKey
                    var textActivated = false
                    val buttonText = view.findViewById<TextView>(R.id.otb_custom_toggle_button_tv_id)
                    if ((trustBadgeElement.appUsesPermission == AppUsesPermission.TRUE) || (trustBadgeElement.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT)) {
                        if (trustBadgeElement.userPermissionStatus == UserPermissionStatus.GRANTED || trustBadgeElement.userPermissionStatus == UserPermissionStatus.MANDATORY) {
                            textActivated = true
                        }
                    }

                    if (trustBadgeElement.elementType == ElementType.PERMISSIONS) {
                        customizeElementView(indexPermission, hasPermissionMoreIcon, icon, textActivated, buttonText, trustBadgeElement)
                        indexPermission++
                        permissionLayout?.addView(view)
                        view.setOnClickListener({ _ -> mListener!!.onCardClick(DATA_SELECTED) })
                    } else {
                        customizeElementView(indexUsage, hasAppDataMoreIcon, icon, textActivated, buttonText, trustBadgeElement)
                        indexUsage++
                        appDataLayout?.addView(view)
                        view.setOnClickListener({ _ -> mListener!!.onCardClick(USAGE_SELECTED) })
                    }
                }
            }
        }
        //Add the custom cards / tv if necessary
        if (TrustBadgeManager.INSTANCE.hasCustomCards()) {
            var index = 3
            for (customFragment in TrustBadgeManager.INSTANCE.customDataFragments) {
                var v: View?
                val finalIndex = index
                Log.d(tag, "Adding custom fragment " + customFragment.title + ", index " + index)
                if (OtbActivity.isMasterDetail) {
                    v = layoutInflater.inflate(R.layout.otb_home_tv_large, layoutContainer, false)
                    v.findViewById<TextView>(R.id.otb_home_custom_data_card).text = customFragment.title
                    v.setOnClickListener({ view ->
                        mFragmentSelected = finalIndex
                        for (counter in 0..layoutContainer!!.childCount.minus(1)) {
                            layoutContainer!!.getChildAt(counter).isSelected = false
                        }
                        view.isSelected = true
                        mListener!!.onCardClick(finalIndex)
                    })
                } else {
                    v = layoutInflater.inflate(R.layout.otb_home_data_card, layoutContainer, false)
                    v.findViewById<TextView>(R.id.otb_home_data_card_tv_title).text = customFragment.title
                    val tvContent = v.findViewById<TextView>(R.id.otb_home_data_card_tv_content)
                    if (customFragment.content != null) {
                        tvContent.text = customFragment.content
                    } else {
                        tvContent.visibility = View.GONE
                    }
                    v.setOnClickListener({ _ -> mListener!!.onCardClick(finalIndex) })
                }
                index += 1
                layoutContainer?.addView(v)
            }
        }
    }

    private fun customizeElementView(index: Int, hasMoreIcon: Boolean, icon: ImageView, textActivated: Boolean, buttonText: TextView, trustBadgeElement: TrustBadgeElement) {
        if (index == 4 && hasMoreIcon) {
            icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.otb_ic_more))
        } else {
            if (textActivated) {
                buttonText.text = resources.getString(R.string.otb_toggle_button_granted)
                buttonText.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            } else {
                buttonText.text = resources.getString(R.string.otb_toggle_button_not_granted)
                buttonText.setTextColor(ContextCompat.getColor(requireContext(), R.color.otb_black))
            }
            icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), trustBadgeElement.iconId))
        }
    }

    private fun hasMoreIcons(trustBadgeElements: MutableList<TrustBadgeElement>) {
        var indexPermission = 0
        var indexAppData = 0
        val activatedPermissions = ArrayList<String>()
        val activatedAppData = ArrayList<String>()
        dynamicPermissionGrantedText = ""
        dynamicAppDataGrantedText = ""
        for (trustBadgeElement in trustBadgeElements) {
            if (trustBadgeElement.elementType == ElementType.PERMISSIONS) {
                indexPermission++
                if (indexPermission == 5) {
                    hasPermissionMoreIcon = true
                }
                if ((trustBadgeElement.appUsesPermission == AppUsesPermission.TRUE) || (trustBadgeElement.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT)) {
                    if (trustBadgeElement.userPermissionStatus == UserPermissionStatus.GRANTED || trustBadgeElement.userPermissionStatus == UserPermissionStatus.MANDATORY) {
                        activatedPermissions.add(trustBadgeElement.nameKey)
                        hasAtLeastOnePermissionGranted = true
                    }
                }
            } else if (trustBadgeElement.elementType == ElementType.APP_DATA) {
                indexAppData++
                if (indexAppData == 5) {
                    hasAppDataMoreIcon = true
                }
                if ((trustBadgeElement.appUsesPermission == AppUsesPermission.TRUE) || (trustBadgeElement.appUsesPermission == AppUsesPermission.NOT_SIGNIFICANT)) {
                    if (trustBadgeElement.userPermissionStatus == UserPermissionStatus.GRANTED || trustBadgeElement.userPermissionStatus == UserPermissionStatus.MANDATORY) {
                        activatedAppData.add(trustBadgeElement.nameKey)
                        hasAtLeastOneAppDataGranted = true
                    }
                }
            }
        }
        nbPermissions = indexPermission
        nbAppData = indexAppData
        initializeDynamicText(activatedPermissions, true)
        initializeDynamicText(activatedAppData, false)
    }

    private fun initializeDynamicText(grantedDataList: ArrayList<String>, isPermission: Boolean) {
        var index = 0
        var textToInitialize = ""
        for (perm in grantedDataList) {
            if (index == 0) {
                textToInitialize += perm
            } else if (index == grantedDataList.size - 1 || index == 4) {
                textToInitialize += getString(R.string.otb_home_dynamic_and_link)
                if (index == 4) {
                    textToInitialize += (grantedDataList.size - index)
                    textToInitialize += if (grantedDataList.size == 5) {
                        getString(R.string.otb_home_dynamic_one_more)
                    } else {
                        getString(R.string.otb_home_dynamic_more)
                    }
                    break
                } else {
                    textToInitialize += perm
                }
            } else {
                textToInitialize += getString(R.string.otb_home_dynamic_comma_link) + perm
            }
            index++
        }
        if (isPermission) {
            dynamicPermissionGrantedText = textToInitialize
        } else {
            dynamicAppDataGrantedText = textToInitialize
        }
    }

    /***********************************************************************************************
     * ***********************    PRIVATE METHOD FOR PERMISSIONS Listener     ************************
     * ********************************************************************************************
     * @param fragmentSelected the selected fragment identifier
     */
    private fun initListeners(fragmentSelected: Int) {
        val dataCardLayout = activity!!.findViewById<View>(R.id.otb_home_data_card)
        val usageCardLayout = activity!!.findViewById<View>(R.id.otb_home_usage_card)
        val termsCardLayout = activity!!.findViewById<View>(R.id.otb_home_terms_card)
        if (dataCardLayout is TextView) {
            when (fragmentSelected) {
                USAGE_SELECTED -> {
                    usageCardLayout.isSelected = true
                    mFragmentSelected = USAGE_SELECTED
                }
                TERM_SELECTED -> {
                    termsCardLayout.isSelected = true
                    mFragmentSelected = TERM_SELECTED
                }
                DATA_SELECTED -> {
                    dataCardLayout.isSelected = true
                    mFragmentSelected = DATA_SELECTED
                }
                else -> {
                    dataCardLayout.isSelected = true
                    mFragmentSelected = DATA_SELECTED
                }
            }
        }
        dataCardLayout.setOnClickListener({ _ ->
            if (dataCardLayout is TextView) {
                mFragmentSelected = DATA_SELECTED
                unSelectAllViews()
                dataCardLayout.isSelected = true
            }
            mListener!!.onCardClick(DATA_SELECTED)
        })
        usageCardLayout.setOnClickListener({ _ ->
            if (dataCardLayout is TextView) {
                mFragmentSelected = USAGE_SELECTED
                unSelectAllViews()
                usageCardLayout.isSelected = true
            }
            mListener!!.onCardClick(USAGE_SELECTED)
        })
        termsCardLayout.setOnClickListener({ _ ->
            if (dataCardLayout is TextView) {
                mFragmentSelected = TERM_SELECTED
                unSelectAllViews()
                termsCardLayout.isSelected = true
            }
            mListener!!.onCardClick(TERM_SELECTED)
        })
    }

    private fun unSelectAllViews() {
        for (counter in 0..layoutContainer!!.childCount.minus(1)) {
            layoutContainer!!.getChildAt(counter).isSelected = false
        }
    }

    /**
     * Interface of "ContainerFragment" listener
     * this listener notify the ContainerFragments events to host application
     */
    interface OtbFragmentListener {
        fun onCardClick(index: Int)
    }

    companion object {
        const val FRAG_TAG = "OtbContainerFragment"
        fun newInstance(): OtbContainerFragment {
            return OtbContainerFragment()
        }

        const val DATA_SELECTED = 0
        const val USAGE_SELECTED = 1
        const val TERM_SELECTED = 2

    }

}
