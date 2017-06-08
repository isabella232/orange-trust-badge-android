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
import android.support.v4.util.Pair
import com.orange.essentials.otb.R
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.RatingType
import java.util.*

/**
 * Created by veeb7280 on 29/01/2016.
 * Static methods that provide convinient ways to build a TrustBadgeElement with minimal inputs
 */
object TrustBadgeElementFactory {
    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected

     * @param context       the context used to retrieve ressources
     * *
     * @param groupType     the group type for this badge
     * *
     * @param elementType   the element type for this badge
     * *
     * @param nameId        the ressource id for the name of this badge
     * *
     * @param descriptionId the ressource id for the description of this badge
     * *
     * @return a TrustBadgeElement
     */
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType,
              nameId: Int,
              descriptionId: Int): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        var toggable = false
        if (groupType == GroupType.IMPROVEMENT_PROGRAM) {
            toggable = true
        }
        val icon = getDefaultIconId(groupType)

        return TrustBadgeElement(groupType,
                elementType,
                context.resources.getString(nameId),
                context.resources.getString(descriptionId),
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                toggable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected

     * @param groupType   the group type for this badge
     * *
     * @param elementType the element typefor this badge
     * *
     * @param name        the name of this badge
     * *
     * @param description the description of this badge
     * *
     * @param toggable    should this badge be toggable
     * *
     * @return a TrustBadgeElement
     */
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType,
              name: String,
              description: String,
              toggable: Boolean): TrustBadgeElement {
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
                icon,
                toggable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement PEGI with minimal inputs.
     * Default icons will be selected

     * @param groupType   the group type for this badge
     * *
     * @param elementType the element type for this badge
     * *
     * @param ratingType  the rating type for this badge
     * *
     * @param name        the name of this badge
     * *
     * @param description the description of this badge
     * *
     * @return a TrustBadgeElement for PEGI indication (not toggable)
     */
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType,
              ratingType: RatingType,
              name: String,
              description: String): TrustBadgeElement {
        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        val icon = getDefaultIconForRatingType(ratingType)
        return TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                false
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected

     * @param ratingType the group type for this badge
     * *
     * @return a TrustBadgeElement GroupType PEGI, ElementType USAGE
     */
    fun build(context: Context, ratingType: RatingType): TrustBadgeElement {
        val pair = getDefaultNameAndDescriptionRessourceIds(GroupType.PEGI)
        val name = context.resources.getString(pair.first!!, TrustBadgeManager.INSTANCE.applicationName)
        val description = context.resources.getString(pair.second!!, TrustBadgeManager.INSTANCE.applicationName)

        return build(context, GroupType.PEGI, ElementType.USAGE, ratingType, name, description)
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
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType, appUsesPermission: AppUsesPermission): TrustBadgeElement {
        var toggable = false
        if (groupType == GroupType.IMPROVEMENT_PROGRAM) {
            toggable = true
        }

        if (!PermissionManager.INSTANCE.isInitialized) {
            PermissionManager.INSTANCE.initPermissionList(context)
        }
        val pair = getDefaultNameAndDescriptionRessourceIds(groupType)
        val name = context.resources.getString(pair.first!!, TrustBadgeManager.INSTANCE.applicationName)
        val description = context.resources.getString(pair.second!!, TrustBadgeManager.INSTANCE.applicationName)
        val icon = getDefaultIconId(groupType)
        return TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                appUsesPermission,
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                toggable
        )
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected

     * @param groupType   the group type for this badge
     * *
     * @param elementType the element typefor this badge
     * *
     * @return a TrustBadgeElement
     */
    fun build(context: Context, groupType: GroupType,
              elementType: ElementType): TrustBadgeElement {
        var toggable = false
        if (groupType == GroupType.IMPROVEMENT_PROGRAM) {
            toggable = true
        }
        val pair = getDefaultNameAndDescriptionRessourceIds(groupType)
        val name = context.resources.getString(pair.first!!, TrustBadgeManager.INSTANCE.applicationName)
        val description = context.resources.getString(pair.second!!, TrustBadgeManager.INSTANCE.applicationName)
        return build(context, groupType, elementType, name, description, toggable)
    }
    //REGION Private Methods
    private fun getDefaultIconForRatingType(ratingType: RatingType): Int {
        val iconId: Int
        when (ratingType) {
            RatingType.THREE -> iconId = R.drawable.otb_ic_rating_3_large
            RatingType.SEVEN -> iconId = R.drawable.otb_ic_rating_7_large
            RatingType.SIXTEEN -> iconId = R.drawable.otb_ic_rating_16_large
            RatingType.EIGHTEEN -> iconId = R.drawable.otb_ic_rating_18_large
            RatingType.TWELVE -> iconId = R.drawable.otb_ic_rating_12_large
            else -> iconId = R.drawable.otb_ic_rating_12_large
        }
        return iconId
    }

    private fun getDefaultIconId(groupType: GroupType): Int {
        var iconId = 0

        when (groupType) {
            GroupType.IDENTITY -> iconId = R.drawable.otb_ic_orange_id_black_32dp
            GroupType.LOCATION -> iconId = R.drawable.otb_ic_geolocation_black_32dp
            GroupType.STORAGE -> iconId = R.drawable.otb_ic_media_black_32dp
            GroupType.IMPROVEMENT_PROGRAM -> iconId = R.drawable.otb_ic_improvement_black_32dp
            GroupType.CONTACTS -> iconId = R.drawable.otb_ic_contacts_black_32dp
            GroupType.CAMERA -> iconId = R.drawable.otb_ic_camera_black_24dp
            GroupType.AGENDA -> iconId = R.drawable.otb_ic_calendar_black_24dp
            GroupType.SMS -> iconId = R.drawable.otb_ic_sms_black_24dp
            GroupType.MICROPHONE -> iconId = R.drawable.otb_ic_mic_black_24dp
            GroupType.PHONE -> iconId = R.drawable.otb_ic_local_phone_black_24dp
            GroupType.SENSORS -> iconId = R.drawable.otb_ic_sensors
            GroupType.BILLING -> iconId = R.drawable.otb_ic_shopping_black_32dp
            GroupType.ADVERTISE -> iconId = R.drawable.otb_ic_advertising_black_32dp
            GroupType.PEGI -> {
            }
            GroupType.SOCIAL_INFO -> iconId = R.drawable.otb_ic_social_black_32dp
            else -> {
            }
        }
        return iconId
    }

    private fun getDefaultNameAndDescriptionRessourceIds(groupType: GroupType): Pair<Int, Int> {
        var descriptionId = 0
        var nameId = 0

        when (groupType) {
            GroupType.IDENTITY -> {
                descriptionId = R.string.otb_main_data_identity_desc
                nameId = R.string.otb_main_data_identity_title
            }
            GroupType.LOCATION -> {
                descriptionId = R.string.otb_main_data_location_desc
                nameId = R.string.otb_main_data_location_title
            }
            GroupType.STORAGE -> {
                descriptionId = R.string.otb_main_data_medias_desc
                nameId = R.string.otb_main_data_medias_title
            }
            GroupType.IMPROVEMENT_PROGRAM -> {
                descriptionId = R.string.otb_main_data_improvement_program_desc
                nameId = R.string.otb_main_data_improvement_program_title
            }
            GroupType.CONTACTS -> {
                descriptionId = R.string.otb_main_data_contacts_desc
                nameId = R.string.otb_main_data_contact_title
            }
            GroupType.CAMERA -> {
                descriptionId = R.string.otb_other_data_camera_desc
                nameId = R.string.otb_other_data_camera_title
            }
            GroupType.AGENDA -> {
                descriptionId = R.string.otb_other_data_calendar_desc
                nameId = R.string.otb_other_data_calendar_title
            }
            GroupType.SMS -> {
                descriptionId = R.string.otb_other_data_sms_desc
                nameId = R.string.otb_other_data_sms_title
            }
            GroupType.MICROPHONE -> {
                descriptionId = R.string.otb_other_data_microphone_desc
                nameId = R.string.otb_other_data_microphone_title
            }
            GroupType.PHONE -> {
                descriptionId = R.string.otb_other_data_phone_desc
                nameId = R.string.otb_other_data_phone_title
            }
            GroupType.SENSORS -> {
                descriptionId = R.string.otb_other_data_connected_devices_desc
                nameId = R.string.otb_other_data_connected_devices_title
            }
            GroupType.BILLING -> {
                descriptionId = R.string.otb_usage_inapp_purchase_desc
                nameId = R.string.otb_usage_inapp_purchase_title
            }
            GroupType.ADVERTISE -> {
                descriptionId = R.string.otb_usage_advertise_desc
                nameId = R.string.otb_usage_advertise_title
            }
            GroupType.PEGI -> {
                descriptionId = R.string.otb_usage_pegi_desc
                nameId = R.string.otb_usage_pegi_title
            }
            GroupType.SOCIAL_INFO -> {
                descriptionId = R.string.otb_usage_social_network_desc
                nameId = R.string.otb_usage_social_network_title
            }
            else -> {
            }
        }

        return Pair(nameId, descriptionId)
    }

    private fun getDefaultAppUsesPermissionForGroupType(groupType: GroupType): AppUsesPermission {
        val appUsesPermission: AppUsesPermission
        when (groupType) {
            GroupType.IDENTITY, GroupType.ADVERTISE, GroupType.PEGI, GroupType.SOCIAL_INFO, GroupType.IMPROVEMENT_PROGRAM -> appUsesPermission = AppUsesPermission.TRUE
            else -> appUsesPermission = PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(groupType)
        }

        return appUsesPermission
    }

    fun getDefaultElements(context: Context): MutableList<TrustBadgeElement> {
        val trustBadgeElements = ArrayList<TrustBadgeElement>()
        /** MANDATORY : MAIN BADGE  */
        trustBadgeElements.add(build(context, GroupType.IDENTITY, ElementType.MAIN, AppUsesPermission.TRUE))
        trustBadgeElements.add(build(context, GroupType.LOCATION, ElementType.MAIN))
        trustBadgeElements.add(build(context, GroupType.STORAGE, ElementType.MAIN))
        trustBadgeElements.add(build(context, GroupType.CONTACTS, ElementType.MAIN))
        trustBadgeElements.add(build(context, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE))
        /** NOT MANDATORY : OTHERS BADGES  */
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.CAMERA) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.CAMERA, ElementType.OTHERS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.AGENDA) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.AGENDA, ElementType.OTHERS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SMS) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.SMS, ElementType.OTHERS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.MICROPHONE) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.MICROPHONE, ElementType.OTHERS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.PHONE) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.PHONE, ElementType.OTHERS))
        }
        if (PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SENSORS) != AppUsesPermission.FALSE) {
            trustBadgeElements.add(build(context, GroupType.SENSORS, ElementType.OTHERS))
        }
        /** MANDATORY : USAGE BADGES  */
        trustBadgeElements.add(build(context, RatingType.TWELVE))
        trustBadgeElements.add(build(context, GroupType.BILLING, ElementType.USAGE))
        trustBadgeElements.add(build(context, GroupType.ADVERTISE, ElementType.USAGE))
        trustBadgeElements.add(build(context, GroupType.SOCIAL_INFO, ElementType.USAGE))

        return trustBadgeElements
    }
}
/**
 * Default private constructor, as this class should not be instantiated
 */
