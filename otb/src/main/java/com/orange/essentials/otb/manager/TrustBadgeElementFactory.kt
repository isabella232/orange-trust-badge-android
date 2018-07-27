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
import com.orange.essentials.otb.R
import com.orange.essentials.otb.model.Term
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.TermType
import java.util.*

/**
 * Created by veeb7280 on 29/01/2016.
 * Static methods that provide convenient ways to build a TrustBadgeElement with minimal inputs
 */
object TrustBadgeElementFactory {
    @JvmStatic
    fun getDefaultTermElements(context: Context): MutableList<Term> {
        val mTerms = ArrayList<Term>()
        mTerms.add(Term(TermType.TEXT, R.string.otb_terms_engagement_title, R.string.otb_terms_engagament_content))
        mTerms.add(Term(TermType.TEXT, R.string.otb_terms_video_text_title, R.string.otb_terms_video_text_content))
        mTerms.add(Term(TermType.TEXT, R.string.otb_terms_usage_title, R.string.otb_terms_usage_content))
        mTerms.add(Term(TermType.TEXT, R.string.otb_terms_help_title, R.string.otb_terms_help_content))
        mTerms.add(Term(TermType.TEXT, R.string.otb_terms_more_info_title, R.string.otb_terms_more_info_content))
        return mTerms
    }

    @JvmStatic
    fun getDefaultPermissionElements(context: Context): MutableList<TrustBadgeElement> {
        val trustBadgeElements = ArrayList<TrustBadgeElement>()
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        /** System permissions  */
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.LOCATION) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.LOCATION, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.CONTACTS) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.CONTACTS, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.STORAGE) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.STORAGE, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.CALENDAR) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.CALENDAR, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.PHONE) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.PHONE, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SMS) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.SMS, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.CAMERA) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.CAMERA, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.MICROPHONE) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.MICROPHONE, ElementType.PERMISSIONS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SENSORS) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.SENSORS, ElementType.PERMISSIONS))
        }

        return trustBadgeElements
    }

    @JvmStatic
    fun getDefaultAppDataElements(context: Context): MutableList<TrustBadgeElement> {
        val trustBadgeElements = ArrayList<TrustBadgeElement>()

        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.NOTIFICATIONS, ElementType.APP_DATA, AppUsesPermission.TRUE, false))
        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.ACCOUNT_CREDENTIALS, ElementType.APP_DATA, AppUsesPermission.TRUE, false))
        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.ACCOUNT_INFO, ElementType.APP_DATA, AppUsesPermission.TRUE, true))
        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.IMPROVEMENT_PROGRAM, ElementType.APP_DATA, AppUsesPermission.TRUE, true))
        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.ADVERTISE, ElementType.APP_DATA, AppUsesPermission.FALSE, false))
        //Optional elements
        trustBadgeElements.add(TrustBadgeElementFactory.build(context, GroupType.HISTORY, ElementType.APP_DATA, AppUsesPermission.FALSE, false))

        return trustBadgeElements
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected

     * @param context       the context used to retrieve resources
     * *
     * @param groupType     the group type for this badge
     * *
     * @param elementType   the element type for this badge
     * *
     * @param nameId        the resource id for the name of this badge
     * *
     * @param descriptionId the resource id for the description of this badge
     * *
     * @return a TrustBadgeElement
     */
    @JvmStatic
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType,
              nameId: Int,
              descriptionId: Int): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        var toggleable = false
        if (groupType == GroupType.IMPROVEMENT_PROGRAM) {
            toggleable = true
        }
        val icon = getDefaultIconId(groupType)

        return TrustBadgeElement(groupType,
                elementType,
                context.resources.getString(nameId),
                context.resources.getString(descriptionId),
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                toggleable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected

     * @param groupType   the group type for this badge
     * *
     * @param elementType the element type for this badge
     * *
     * @param name        the name of this badge
     * *
     * @param description the description of this badge
     * *
     * @param toggleable    should this badge be toggleable
     * *
     * @return a TrustBadgeElement
     */
    @JvmStatic
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType,
              name: String,
              description: String,
              toggleable: Boolean): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        val icon = getDefaultIconId(groupType)
        return TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                toggleable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected
     * @param groupType         the group type for this badge
     * *
     * @param elementType       the element typefor this badge
     * *
     * @param appUsesPermission indication wether the app uses this permission
     * *
     * @return a TrustBadgeElement
     */
    @JvmStatic
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType, appUsesPermission: AppUsesPermission, toggable: Boolean): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        val pair = getDefaultNameAndDescriptionResourceIds(groupType)
        val name = context.resources.getString(pair.first, TrustBadgeManager.INSTANCE.applicationName)
        val description = context.resources.getString(pair.second, TrustBadgeManager.INSTANCE.applicationName)
        val icon = getDefaultIconId(groupType)
        return TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                appUsesPermission,
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                toggable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected

     * @param groupType   the group type for this badge
     * *
     * @param elementType the element type for this badge
     * *
     * @return a TrustBadgeElement
     */
    @JvmStatic
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType, toggleable: Boolean = false): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        val pair = getDefaultNameAndDescriptionResourceIds(groupType)
        val name = context.resources.getString(pair.first, TrustBadgeManager.INSTANCE.applicationName)
        val description = context.resources.getString(pair.second, TrustBadgeManager.INSTANCE.applicationName)
        return build(context, groupType, elementType, name, description, toggleable)
    }

    private fun getDefaultIconId(groupType: GroupType): Int {
        var iconId = 0

        when (groupType) {
            GroupType.LOCATION -> iconId = R.drawable.otb_ic_location
            GroupType.CONTACTS -> iconId = R.drawable.otb_ic_contacts
            GroupType.STORAGE -> iconId = R.drawable.otb_ic_storage
            GroupType.CALENDAR -> iconId = R.drawable.otb_ic_calendar
            GroupType.PHONE -> iconId = R.drawable.otb_ic_phone
            GroupType.SMS -> iconId = R.drawable.otb_ic_sms
            GroupType.CAMERA -> iconId = R.drawable.otb_ic_camera
            GroupType.MICROPHONE -> iconId = R.drawable.otb_ic_mic
            GroupType.SENSORS -> iconId = R.drawable.otb_ic_sensors
            GroupType.NOTIFICATIONS -> iconId = R.drawable.otb_ic_notification
            GroupType.ACCOUNT_CREDENTIALS -> iconId = R.drawable.otb_ic_id
            GroupType.ACCOUNT_INFO -> iconId = R.drawable.otb_ic_personal_on
            GroupType.IMPROVEMENT_PROGRAM -> iconId = R.drawable.otb_ic_improvement_prog
            GroupType.ADVERTISE -> iconId = R.drawable.otb_ic_publicity
            GroupType.HISTORY -> iconId = R.drawable.otb_ic_history
            GroupType.CUSTOM_DATA -> iconId = R.drawable.otb_ic_custom
        }
        return iconId
    }

    private fun getDefaultNameAndDescriptionResourceIds(groupType: GroupType): Pair<Int, Int> {
        var descriptionId = 0
        var nameId = 0

        when (groupType) {
            GroupType.ACCOUNT_CREDENTIALS -> {
                descriptionId = R.string.otb_app_data_account_credentials_desc
                nameId = R.string.otb_app_data_account_credentials_title
            }
            GroupType.LOCATION -> {
                descriptionId = R.string.otb_permission_location_desc
                nameId = R.string.otb_permission_location_title
            }
            GroupType.STORAGE -> {
                descriptionId = R.string.otb_permission_storage_desc
                nameId = R.string.otb_permission_storage_title
            }
            GroupType.IMPROVEMENT_PROGRAM -> {
                descriptionId = R.string.otb_app_data_improvement_program_desc
                nameId = R.string.otb_app_data_improvement_program_title
            }
            GroupType.CONTACTS -> {
                descriptionId = R.string.otb_permission_contacts_desc
                nameId = R.string.otb_permission_contact_title
            }
            GroupType.CAMERA -> {
                descriptionId = R.string.otb_permission_camera_desc
                nameId = R.string.otb_permission_camera_title
            }
            GroupType.CALENDAR -> {
                descriptionId = R.string.otb_permission_calendar_desc
                nameId = R.string.otb_permission_calendar_title
            }
            GroupType.SMS -> {
                descriptionId = R.string.otb_permission_sms_desc
                nameId = R.string.otb_permission_sms_title
            }
            GroupType.MICROPHONE -> {
                descriptionId = R.string.otb_permission_microphone_desc
                nameId = R.string.otb_permission_microphone_title
            }
            GroupType.PHONE -> {
                descriptionId = R.string.otb_permission_phone_desc
                nameId = R.string.otb_permission_phone_title
            }
            GroupType.SENSORS -> {
                descriptionId = R.string.otb_permission_sensors_desc
                nameId = R.string.otb_permission_sensors_title
            }
            GroupType.NOTIFICATIONS -> {
                descriptionId = R.string.otb_app_data_notifications_desc
                nameId = R.string.otb_app_data_notifications_title
            }
            GroupType.ADVERTISE -> {
                descriptionId = R.string.otb_app_data_advertise_desc
                nameId = R.string.otb_app_data_advertise_title
            }
            GroupType.HISTORY -> {
                descriptionId = R.string.otb_app_data_history_desc
                nameId = R.string.otb_app_data_history_title
            }
            GroupType.ACCOUNT_INFO -> {
                descriptionId = R.string.otb_app_data_account_info_desc
                nameId = R.string.otb_app_data_account_info_title
            }
            else -> {
            }
        }

        return Pair(nameId, descriptionId)
    }

    private fun getDefaultAppUsesPermissionForGroupType(groupType: GroupType) = when (groupType) {
        GroupType.ACCOUNT_CREDENTIALS, GroupType.ADVERTISE, GroupType.ACCOUNT_INFO, GroupType.IMPROVEMENT_PROGRAM -> AppUsesPermission.TRUE
        else -> PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(groupType)
    }

}
