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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.orange.essentials.otb.event.EventTagger
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.model.Term
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.RatingType
import com.orange.essentials.otb.model.type.UserPermissionStatus
import java.util.*

/**
 *
 *
 * File name:   TrustBadgeManager
 * Main singleton handling the trustbadge elements
 */
enum class TrustBadgeManager {
    INSTANCE;

    /**
     * mApplicationName
     * Host application name
     */
    var applicationName: String? = null
        private set
    /**
     * mApplicationPackageName
     * Host application packageName
     */
    var applicationPackageName: String? = null
        private set

    /**
     * Provides a way to tag events
     */
    //Provides a default eventtagger if none has been provided
    var eventTagger: EventTagger? = null
        get() {
            if (null == field) {
                eventTagger = object : EventTagger {
                    override fun tag(eventType: EventType) {
                        Log.d(TAG, "tagging event " + eventType)
                    }

                    override fun tagElement(eventType: EventType, element: TrustBadgeElement) {
                        Log.d(TAG, "tagging event $eventType for element $element")
                    }
                }
            }
            return field
        }

    /**
     * Store all informations about Main Data, Complementary Data, and Usage Data
     */
    private var mTrustBadgeElements: MutableList<TrustBadgeElement> = ArrayList()

    /**
     * Stores all the listeners who should act when a toggable badge element value is switched
     */
    private val mTrustBadgeElementListeners = ArrayList<TrustBadgeElementListener>()
    /**
     * Stores all the listeners who should act when a toggable badge element value is switched
     */
    private val mBadgeListeners = ArrayList<BadgeListener>()

    /**
     * List of terms and conditions that should be displayed in "terms and conditions" section
     */
    var terms: List<Term>? = ArrayList()
    /**
     * Boolean indicating if app checked improvement program
     */
    var isUsingImprovementProgram = true
        set(usingImprovementProgram) {
            field = usingImprovementProgram
            val improvementProgram = TrustBadgeManager.INSTANCE.getSpecificPermission(GroupType.IMPROVEMENT_PROGRAM)
            if (null != improvementProgram) {
                improvementProgram.userPermissionStatus = if (usingImprovementProgram) UserPermissionStatus.GRANTED else UserPermissionStatus.NOT_GRANTED
            }
        }
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
        Log.d(TAG, "extractApplicationName")
        val csName = context.applicationInfo.loadLabel(context.packageManager)
        if (null != csName) {
            appName = csName.toString()
        }
        Log.d(TAG, "extractApplicationName")
        return appName
    }

    /**
     * provide init with default BadgeFactory

     * @param context : Context providing needed data to retrieve permissions infos
     */
    fun initialize(context: Context) {
        PermissionManager.INSTANCE.initPermissionList(context)
        initialize(context, TrustBadgeElementFactory.getDefaultElements(context), ArrayList<Term>())
    }


    /**
     * initialize

     * @param context            : Activity providing needed data to retrieve permissions infos
     * *
     * @param trustBadgeElements : the list of trsut badge that will be used into the app
     */
    fun initialize(context: Context, trustBadgeElements: MutableList<TrustBadgeElement>, terms: List<Term>) {

        Log.d(TAG, "TrustBadgeManager initialize")
        PermissionManager.INSTANCE.initPermissionList(context)
        applicationName = extractApplicationName(context)
        if (!applicationName!!.isEmpty()) {
            Log.d(TAG, "mApplicationName is NOT empty")
            applicationPackageName = context.packageName
            if (applicationPackageName != null && applicationPackageName!!.length != 0) {
                Log.d(TAG, "mApplicationPackageName is NOT empty")
                /** Initialisation of TrustBadgeElements to 15 permissions  */
                this.trustBadgeElements = trustBadgeElements
                this.terms = terms
            } else {
                Log.d(TAG, "Context does not provide PackageName.")
            }
        } else {
            Log.d(TAG, "Context does not provide ApplicationName.")
        }
        isInitialized = true
    }

    /**
     * retrieve list of permissions about kids
     */
    val elementsForUsage: ArrayList<TrustBadgeElement>?
        get() {
            val resultPermissionList = ArrayList<TrustBadgeElement>()
            for (i in mTrustBadgeElements.indices) {
                val trustBadgeElement = mTrustBadgeElements[i]
                if (trustBadgeElement.elementType == ElementType.USAGE) {
                    resultPermissionList.add(trustBadgeElement)
                }
            }
            return resultPermissionList
        }

    /**
     * retrieve list of  permissions
     */
    val elementsForDataCollected: ArrayList<TrustBadgeElement>?
        get() {
            Log.d(TAG, "getElementsForDataCollected")
            val resultPermissionList = ArrayList<TrustBadgeElement>()
            for (trustBadgeElement in mTrustBadgeElements) {
                Log.d(TAG, "getElementsForDataCollected  : " + trustBadgeElement.groupType + ", " + trustBadgeElement.elementType + ", " + trustBadgeElement.appUsesPermission + ", " + trustBadgeElement.userPermissionStatus)
                if (trustBadgeElement.elementType != ElementType.USAGE) {
                    resultPermissionList.add(trustBadgeElement)
                    Log.d(TAG, trustBadgeElement.nameKey + " ==> ADDED")
                }
            }
            return resultPermissionList
        }

    /**
     * retrieve a specific badge according to type
     */
    fun getSpecificPermission(groupType: GroupType): TrustBadgeElement? {
        Log.d(TAG, "getSpecificPermission")
        var result: TrustBadgeElement? = null
        for (trustBadgeElement in mTrustBadgeElements) {
            if (trustBadgeElement.groupType == groupType) {
                Log.d(TAG, "trustBadgeElement.getElementType() equals " + ElementType.USAGE.toString())
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
    var trustBadgeElements: MutableList<TrustBadgeElement>
        get() = mTrustBadgeElements
        set(trustBadgeElements) {
            mTrustBadgeElements = trustBadgeElements
        }

    fun addTrustBadgeElement(trustBadgeElement: TrustBadgeElement?) {
        if (null != trustBadgeElement) {
            if (null == mTrustBadgeElements) {
                mTrustBadgeElements = ArrayList<TrustBadgeElement>()
            }
            mTrustBadgeElements!!.add(trustBadgeElement)
        }
    }

    /**
     * Remove all element from the SparseArray and the array
     */
    fun clearTrustBadgeElements() {
        mTrustBadgeElements!!.clear()
    }


    /**
     * Refresh the badges according to the permissions granted by the app
     */
    fun refreshTrustBadgePermission(context: Context) {
        Log.d(TAG, "refreshTrustBadgePermission...")
        PermissionManager.INSTANCE.initPermissionList(context)
        for (i in mTrustBadgeElements!!.indices) {
            val trustBadgeElement = mTrustBadgeElements!![i]
            if (trustBadgeElement.isShouldBeAutoConfigured) {
                val type = trustBadgeElement.groupType
                if (type.isSystemPermission) {
                    trustBadgeElement.userPermissionStatus = PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, trustBadgeElement.groupType)
                }
                if (type == GroupType.IMPROVEMENT_PROGRAM) {
                    trustBadgeElement.userPermissionStatus = if (isUsingImprovementProgram) UserPermissionStatus.GRANTED else UserPermissionStatus.NOT_GRANTED
                }
            }

            Log.d(TAG, "refresh = " + trustBadgeElement)
        }

    }

    /**
     * Method returns true if at least one badge is of type DATA or OTHER

     * @return a boolean indicating if data badges are available
     */
    fun hasData(): Boolean {
        val data = elementsForDataCollected != null && elementsForDataCollected!!.isNotEmpty()
        Log.d(TAG, "hasData : " + data)
        return data
    }

    /**
     * Method returns true if at least one badge is of type USAGE

     * @return a boolean indicating if usage badges are available
     */
    fun hasUsage(): Boolean {
        val usage = elementsForUsage != null && elementsForUsage!!.isNotEmpty()
        Log.d(TAG, "hasUsage : " + usage)
        return usage
    }

    /**
     * Method returns true if at least one term is available

     * @return a boolean indicating if a term is available
     */
    fun hasTerms(): Boolean {
        val terms = terms != null && terms!!.isNotEmpty()
        Log.d(TAG, "hasTerms : " + terms)
        return terms
    }

    /**
     * getPegiAge : Retrieve permission pegi age rating

     * @param trustBadgeElement : th badge to retrieve pegi age value text
     * *
     * @return String age permission
     */
    fun getPegiAge(trustBadgeElement: TrustBadgeElement): String {
        var ageInNumber = ""
        val ageRating = trustBadgeElement.descriptionKey!!
        if (ageRating.contains(RatingType.THREE.name)) {
            ageInNumber = Integer.toString(RatingType.THREE.age)
        }
        if (ageRating.contains(RatingType.SEVEN.name)) {
            ageInNumber = Integer.toString(RatingType.SEVEN.age)
        }
        if (ageRating.contains(RatingType.TWELVE.name)) {
            ageInNumber = Integer.toString(RatingType.TWELVE.age)
        }
        if (ageRating.contains(RatingType.SIXTEEN.name)) {
            ageInNumber = Integer.toString(RatingType.SIXTEEN.age)
        }
        if (ageRating.contains(RatingType.EIGHTEEN.name)) {
            ageInNumber = Integer.toString(RatingType.EIGHTEEN.age)
        }
        return ageInNumber
    }

    @Deprecated("")
    fun addTrustBadgeElementListener(trustBadgeElementListener: TrustBadgeElementListener) {
        if (null != mTrustBadgeElementListeners && !mTrustBadgeElementListeners.contains(trustBadgeElementListener)) {
            mTrustBadgeElementListeners.add(trustBadgeElementListener)
        }
    }

    @Deprecated("")
    fun clearTrustBadgeElementListener() {
        mTrustBadgeElementListeners?.clear()
    }


    val trustBadgeElementListeners: List<TrustBadgeElementListener>
        @Deprecated("")
        get() = mTrustBadgeElementListeners

    fun badgeChanged(trustBadgeElement: TrustBadgeElement, toggled: Boolean, callingActivity: AppCompatActivity) {
        if (null != mBadgeListeners) {
            for (i in mBadgeListeners.indices) {
                mBadgeListeners[i].onBadgeChange(trustBadgeElement, toggled, callingActivity)
            }
        }
        //Change the UserPermissionStatus as it has been switched by user
        trustBadgeElement.userPermissionStatus = if (toggled) UserPermissionStatus.GRANTED else UserPermissionStatus.NOT_GRANTED
    }

    val badgeListeners: List<BadgeListener>
        get() = mBadgeListeners

    fun addBadgeListener(badgeListener: BadgeListener) {
        if (null != mBadgeListeners && !mBadgeListeners.contains(badgeListener)) {
            mBadgeListeners.add(badgeListener)
        }
    }


    @Deprecated("use rather the interface BadgeListener, called by\n      badgeChanged(TrustBadgeElement trustBadgeElement, boolean toggled, AppCompatActivity callingActivity)")
    fun badgeChanged(groupType: GroupType, toggled: Boolean, callingActivity: AppCompatActivity) {
        if (null != mTrustBadgeElementListeners) {
            for (i in mTrustBadgeElementListeners.indices) {
                mTrustBadgeElementListeners[i].onBadgeChange(groupType, toggled, callingActivity)
            }
        }
    }

    companion object {

        /**
         * Log TAG
         */
        private val TAG = "TrustBadgeManager"
    }


}

