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

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.orange.essentials.otb.event.EventTagger
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.logger.Logger
import com.orange.essentials.otb.model.Term
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus
import java.util.*

/**
 *
 * File name:   TrustBadgeManager
 * Main singleton handling the trust badge elements
 */
enum class TrustBadgeManager {
    INSTANCE;

    /**
     * mApplicationName
     * Host application name
     */
    var applicationName: String? = null
    /**
     * mApplicationPackageName
     * Host application packageName
     */
    var applicationPackageName: String? = null
        private set
    /**
     * Provides a way to tag events
     */
    //Provides a default event tagger if none has been provided
    var eventTagger: EventTagger? = null
        get() {
            if (null == field) {
                eventTagger = object : EventTagger {
                    override fun tag(eventType: EventType) {
                        Logger.d(TAG, "tagging event $eventType")
                    }

                    override fun tagElement(eventType: EventType, element: TrustBadgeElement) {
                        Logger.d(TAG, "tagging event $eventType for element $element")
                    }
                }
            }
            return field
        }
    /**
     * Store all informations about Main Data, Complementary Data, and Usage Data
     */
    var trustBadgeElements: MutableList<TrustBadgeElement> = ArrayList()
    /**
     * Stores all the listeners who should act when a toggleable badge element value is switched
     */
    val badgeListeners = ArrayList<BadgeListener>()
    /**
     * List of terms and conditions that should be displayed in "terms and conditions" section
     */
    var terms: List<Term>? = ArrayList()
    /**
     * List of Custom data fragments that should be added to the default cards and fragments already available
     */
    var customDataFragments: MutableList<CustomDataFragment> = ArrayList()
    /**
     * Boolean indicating if app checked improvement program
     */
    @Deprecated("This variable is no longer used and is kept for compatibility only. It will be removed in the near future.")
    var isUsingImprovementProgram = true

    /**
     * boolean letting us know if badges are initialized, false by default
     */
    var isInitialized = false
        private set
    /** METHODS *********************************************************************************  */
    /** KEYS DATAS  */
    /**
     * Return the host application name
     * @return Host application name from AndroidManifest.xml
     */
    private fun extractApplicationName(context: Context): String {
        var appName = context.packageName
        Logger.d(TAG, "extractApplicationName")
        val csName = context.applicationInfo.loadLabel(context.packageManager)
        if (csName.isNotEmpty()) {
            appName = csName.toString()
        }
        Logger.d(TAG, "extractApplicationName")
        return appName
    }

    /**
     * provide init with default BadgeFactory
     * @param context : Context providing needed data to retrieve permissions infos
     */
    fun initialize(context: Context) {
        PermissionManager.INSTANCE.initPermissionList(context)
        val elements = TrustBadgeElementFactory.getDefaultPermissionElements(context)
        elements.addAll(TrustBadgeElementFactory.getDefaultAppDataElements(context))
        initialize(context, elements, ArrayList())
    }

    /**
     * initialize
     * @param context            : Activity providing needed data to retrieve permissions infos
     * @param trustBadgeElements : the list of trust badge that will be used into the app
     * @param terms : the list of terms that will be used into the app
     */
    fun initialize(context: Context, trustBadgeElements: MutableList<TrustBadgeElement>, terms: List<Term>) {
        initialize(context, trustBadgeElements, terms, false)
    }

    /**
     * initialize
     * @param context            : Activity providing needed data to retrieve permissions infos
     * @param trustBadgeElements : the list of trust badge that will be used into the app
     * @param terms : the list of terms that will be used into the app
     * @param logEnabled : should log be enabled on trust badge lib : default no
     */
    fun initialize(context: Context, trustBadgeElements: MutableList<TrustBadgeElement>, terms: List<Term>, logEnabled: Boolean = false) {
        Logger.loggingAllowed = logEnabled
        Logger.d(TAG, "TrustBadgeManager initialize $trustBadgeElements")
        clean()
        PermissionManager.INSTANCE.initPermissionList(context)
        applicationName = extractApplicationName(context)
        if (!applicationName!!.isEmpty()) {
            Logger.d(TAG, "mApplicationName is NOT empty")
            applicationPackageName = context.packageName
            if (applicationPackageName != null && applicationPackageName!!.length != 0) {
                Logger.d(TAG, "mApplicationPackageName is NOT empty")
                /** Initialisation of TrustBadgeElements to 15 permissions  */
                this.trustBadgeElements = trustBadgeElements
                this.terms = terms
            } else {
                Logger.d(TAG, "Context does not provide PackageName.")
            }
        } else {
            Logger.d(TAG, "Context does not provide ApplicationName.")
        }
        isInitialized = true
    }

    /**
     * retrieve list of permissions about kids
     */
    val appDataElements: ArrayList<TrustBadgeElement>?
        get() {
            val resultPermissionList = ArrayList<TrustBadgeElement>()
            for (trustBadgeElement in this.trustBadgeElements) {
                if (trustBadgeElement.elementType == ElementType.APP_DATA) {
                    resultPermissionList.add(trustBadgeElement)
                }
            }
            return resultPermissionList
        }
    /**
     * retrieve list of  permissions
     */
    val permissionElements: ArrayList<TrustBadgeElement>?
        get() {
            val resultPermissionList = ArrayList<TrustBadgeElement>()
            for (trustBadgeElement in this.trustBadgeElements) {
                if (trustBadgeElement.elementType == ElementType.PERMISSIONS) {
                    resultPermissionList.add(trustBadgeElement)
                }
            }
            return resultPermissionList
        }

    /**
     * retrieve a specific badge according to type
     */
    fun getSpecificPermission(groupType: GroupType): TrustBadgeElement? {
        Logger.d(TAG, "getSpecificPermission")
        var result: TrustBadgeElement? = null
        for (trustBadgeElement in this.trustBadgeElements) {
            if (trustBadgeElement.groupType == groupType) {
                Logger.d(TAG, "trustBadgeElement.getElementType() equals " + groupType.toString())
                result = trustBadgeElement
            }
        }
        return result
    }
    /**
     * GETTERS & SETTERS************************************************************************
     */
    /**
     * Gets the array of trustbadge elements
     * @return the list of TrustBadgeElements that will be displayed to the user
     */
    fun addTrustBadgeElement(trustBadgeElement: TrustBadgeElement?) {
        if (null != trustBadgeElement) {
            this.trustBadgeElements.add(trustBadgeElement)
        }
    }

    /**
     * Remove all element from the SparseArray and the array
     */
    fun clearTrustBadgeElements() {
        this.trustBadgeElements.clear()
    }

    /**
     * Refresh the badges according to the permissions granted by the app
     */
    fun refreshTrustBadgePermission(context: Context) {
        Logger.d(TAG, "refreshTrustBadgePermission...")
        PermissionManager.INSTANCE.initPermissionList(context)
        for (i in this.trustBadgeElements.indices) {
            val trustBadgeElement = this.trustBadgeElements[i]
            if (trustBadgeElement.isShouldBeAutoConfigured) {
                val type = trustBadgeElement.groupType
                if (type.isSystemPermission || GroupType.NOTIFICATIONS.equals(type)) {
                    trustBadgeElement.userPermissionStatus = PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, trustBadgeElement.groupType)
                }
            }
            Logger.d(TAG, "refresh = $trustBadgeElement")
        }

    }

    /**
     * Method returns true if at least one badge is of type DATA or OTHER

     * @return a boolean indicating if data badges are available
     */
    fun hasPermissions(): Boolean {
        val result = permissionElements != null && permissionElements!!.isNotEmpty()
        Logger.d(TAG, "hasPermissions : $result")
        return result
    }

    /**
     * Method returns true if at least one badge is of type USAGE

     * @return a boolean indicating if usage badges are available
     */
    fun hasAppData(): Boolean {
        val result = appDataElements != null && appDataElements!!.isNotEmpty()
        Logger.d(TAG, "hasAppData : $result")
        return result
    }

    /**
     * Method returns true if at least one term is available

     * @return a boolean indicating if a term is available
     */
    fun hasTerms(): Boolean {
        val terms = terms != null && terms!!.isNotEmpty()
        Logger.d(TAG, "hasTerms : $terms")
        return terms
    }

    /**
     * Method returns true if at least one term is available

     * @return a boolean indicating if a term is available
     */
    fun hasCustomCards(): Boolean {
        return customDataFragments.size > 0
    }

    fun badgeChanged(trustBadgeElement: TrustBadgeElement, toggled: Boolean, callingActivity: AppCompatActivity) {
        for (i in this.badgeListeners.indices) {
            this.badgeListeners[i].onBadgeChange(trustBadgeElement, toggled, callingActivity)
        }
        //Change the UserPermissionStatus as it has been switched by user
        trustBadgeElement.userPermissionStatus = if (toggled) UserPermissionStatus.GRANTED else UserPermissionStatus.NOT_GRANTED
    }

    fun addBadgeListener(badgeListener: BadgeListener) {
        if (!this.badgeListeners.contains(badgeListener)) {
            this.badgeListeners.add(badgeListener)
        }
    }

    fun clearBadgeListeners() = badgeListeners.clear()

    companion object {
        /**
         * Log TAG
         */
        private const val TAG = "TrustBadgeManager"
    }

    /**
     * Add a custom fragment
     */
    fun addCustomCard(title: String, content: String?, fragment: Fragment) {
        customDataFragments.add(CustomDataFragment(title, content, fragment))
    }

    /**
     * Add a custom fragment
     */
    private fun clean() {
        customDataFragments.clear()
    }
}

data class CustomDataFragment(
        val title: String,
        val content: String? = null,
        val fragment: Fragment)
