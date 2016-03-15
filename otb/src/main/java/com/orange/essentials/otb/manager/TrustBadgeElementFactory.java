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
package com.orange.essentials.otb.manager;

import android.content.Context;
import android.support.v4.util.Pair;

import com.orange.essentials.otb.R;
import com.orange.essentials.otb.model.TrustBadgeElement;
import com.orange.essentials.otb.model.type.AppUsesPermission;
import com.orange.essentials.otb.model.type.ElementType;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.RatingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veeb7280 on 29/01/2016.
 * Static methods that provide convinient ways to build a TrustBadgeElement with minimal inputs
 */
public class TrustBadgeElementFactory {
    /**
     * Default private constructor, as this class should not be instantiated
     */
    private TrustBadgeElementFactory() {
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected
     *
     * @param context       the context used to retrieve ressources
     * @param groupType     the group type for this badge
     * @param elementType   the element type for this badge
     * @param nameId        the ressource id for the name of this badge
     * @param descriptionId the ressource id for the description of this badge
     * @return a TrustBadgeElement
     */
    public static TrustBadgeElement build(Context context, GroupType groupType,
                                          ElementType elementType,
                                          int nameId,
                                          int descriptionId) {
        if (!PermissionManager.INSTANCE.isInitialized()) {
            PermissionManager.INSTANCE.initPermissionList(context);
        }
        boolean toggable = false;
        if (groupType.equals(GroupType.IMPROVEMENT_PROGRAM)) {
            toggable = true;
        }

        int icon = getDefaultIconId(groupType);

        return new TrustBadgeElement(groupType,
                elementType,
                context.getResources().getString(nameId),
                context.getResources().getString(descriptionId),
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                toggable
        );
    }


    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons will be selected
     *
     * @param groupType   the group type for this badge
     * @param elementType the element typefor this badge
     * @param name        the name of this badge
     * @param description the description of this badge
     * @param toggable    should this badge be toggable
     * @return a TrustBadgeElement
     */
    public static TrustBadgeElement build(Context context, GroupType groupType,
                                          ElementType elementType,
                                          String name,
                                          String description,
                                          boolean toggable) {

        if (!PermissionManager.INSTANCE.isInitialized()) {
            PermissionManager.INSTANCE.initPermissionList(context);
        }
        int icon = getDefaultIconId(groupType);
        return new TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                toggable
        );
    }


    /**
     * Convenient method for building a TrustBadgeElement PEGI with minimal inputs.
     * Default icons will be selected
     *
     * @param groupType   the group type for this badge
     * @param elementType the element type for this badge
     * @param ratingType  the rating type for this badge
     * @param name        the name of this badge
     * @param description the description of this badge
     * @return a TrustBadgeElement for PEGI indication (not toggable)
     */
    public static TrustBadgeElement build(Context context, GroupType groupType,
                                          ElementType elementType,
                                          RatingType ratingType,
                                          String name,
                                          String description) {

        if (!PermissionManager.INSTANCE.isInitialized()) {
            PermissionManager.INSTANCE.initPermissionList(context);
        }
        int icon = getDefaultIconForRatingType(ratingType);
        return new TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                getDefaultAppUsesPermissionForGroupType(groupType),
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                false
        );
    }


    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected
     *
     * @param ratingType the group type for this badge
     * @return a TrustBadgeElement GroupType PEGI, ElementType USAGE
     */
    public static TrustBadgeElement build(Context context, RatingType ratingType) {
        Pair<Integer, Integer> pair = getDefaultNameAndDescriptionRessourceIds(GroupType.PEGI);
        String name = context.getResources().getString(pair.first, TrustBadgeManager.INSTANCE.getApplicationName());
        String description = context.getResources().getString(pair.second, TrustBadgeManager.INSTANCE.getApplicationName());

        return build(context, GroupType.PEGI, ElementType.USAGE, ratingType, name, description);
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected
     *
     * @param groupType         the group type for this badge
     * @param elementType       the element typefor this badge
     * @param appUsesPermission indication wether the app uses this permission
     * @return a TrustBadgeElement
     */
    public static TrustBadgeElement build(Context context, GroupType groupType,
                                          ElementType elementType, AppUsesPermission appUsesPermission) {
        boolean toggable = false;
        if (groupType.equals(GroupType.IMPROVEMENT_PROGRAM)) {
            toggable = true;
        }

        if (!PermissionManager.INSTANCE.isInitialized()) {
            PermissionManager.INSTANCE.initPermissionList(context);
        }

        Pair<Integer, Integer> pair = getDefaultNameAndDescriptionRessourceIds(groupType);
        String name = context.getResources().getString(pair.first, TrustBadgeManager.INSTANCE.getApplicationName());
        String description = context.getResources().getString(pair.second, TrustBadgeManager.INSTANCE.getApplicationName());
        int icon = getDefaultIconId(groupType);
        return new TrustBadgeElement(groupType,
                elementType,
                name,
                description,
                appUsesPermission,
                PermissionManager.INSTANCE.doesUserAlreadyAcceptPermission(context, groupType),
                icon,
                icon,
                toggable
        );
    }

    /**
     * Convenient method for building a TrustBadgeElement with minimal inputs.
     * Default icons, name, labels and order will be selected
     *
     * @param groupType   the group type for this badge
     * @param elementType the element typefor this badge
     * @return a TrustBadgeElement
     */
    public static TrustBadgeElement build(Context context, GroupType groupType,
                                          ElementType elementType) {
        boolean toggable = false;
        if (groupType.equals(GroupType.IMPROVEMENT_PROGRAM)) {
            toggable = true;
        }
        Pair<Integer, Integer> pair = getDefaultNameAndDescriptionRessourceIds(groupType);
        String name = context.getResources().getString(pair.first, TrustBadgeManager.INSTANCE.getApplicationName());
        String description = context.getResources().getString(pair.second, TrustBadgeManager.INSTANCE.getApplicationName());
        return build(context, groupType, elementType, name, description, toggable);
    }


    //REGION Private Methods

    private static int getDefaultIconForRatingType(RatingType ratingType) {
        int iconId;
        switch (ratingType) {
            case THREE:
                iconId = R.drawable.ic_rating_3_large;
                break;
            case SEVEN:
                iconId = R.drawable.ic_rating_7_large;
                break;
            case SIXTEEN:
                iconId = R.drawable.ic_rating_16_large;
                break;
            case EIGHTEEN:
                iconId = R.drawable.ic_rating_18_large;
                break;
            case TWELVE:
            default:
                iconId = R.drawable.ic_rating_12_large;
                break;
        }
        return iconId;
    }

    private static int getDefaultIconId(GroupType groupType) {
        int iconId = 0;

        switch (groupType) {
            case IDENTITY:
                iconId = R.drawable.ic_orange_id_black_32dp;
                break;
            case LOCATION:
                iconId = R.drawable.ic_geolocation_black_32dp;
                break;
            case STORAGE:
                iconId = R.drawable.ic_media_black_32dp;
                break;
            case IMPROVEMENT_PROGRAM:
                iconId = R.drawable.ic_improvement_black_32dp;
                break;
            case CONTACTS:
                iconId = R.drawable.ic_contacts_black_32dp;
                break;
            case CAMERA:
                iconId = R.drawable.ic_camera_black_24dp;
                break;
            case AGENDA:
                iconId = R.drawable.ic_calendar_black_24dp;
                break;
            case SMS:
                iconId = R.drawable.ic_sms_black_24dp;
                break;
            case MICROPHONE:
                iconId = R.drawable.ic_mic_black_24dp;
                break;
            case PHONE:
                iconId = R.drawable.ic_local_phone_black_24dp;
                break;
            case SENSORS:
                iconId = R.drawable.ic_sensors;
                break;
            case BILLING:
                iconId = R.drawable.ic_shopping_black_32dp;
                break;
            case ADVERTISE:
                iconId = R.drawable.ic_advertising_black_32dp;
                break;
            case PEGI:
                break;
            case SOCIAL_INFO:
                iconId = R.drawable.ic_social_black_32dp;
                break;
            default:
                break;
        }
        return iconId;
    }


    private static Pair<Integer, Integer> getDefaultNameAndDescriptionRessourceIds(GroupType groupType) {
        int descriptionId = 0, nameId = 0;

        switch (groupType) {
            case IDENTITY:
                descriptionId = R.string.otb_main_data_identity_desc;
                nameId = R.string.otb_main_data_identity_title;

                break;
            case LOCATION:
                descriptionId = R.string.otb_main_data_location_desc;
                nameId = R.string.otb_main_data_location_title;
                break;
            case STORAGE:
                descriptionId = R.string.otb_main_data_medias_desc;
                nameId = R.string.otb_main_data_medias_title;
                break;
            case IMPROVEMENT_PROGRAM:
                descriptionId = R.string.otb_main_data_improvement_program_desc;
                nameId = R.string.otb_main_data_improvement_program_title;
                break;
            case CONTACTS:
                descriptionId = R.string.otb_main_data_contacts_desc;
                nameId = R.string.otb_main_data_contact_title;
                break;
            case CAMERA:
                descriptionId = R.string.otb_other_data_camera_desc;
                nameId = R.string.otb_other_data_camera_title;
                break;
            case AGENDA:
                descriptionId = R.string.otb_other_data_calendar_desc;
                nameId = R.string.otb_other_data_calendar_title;
                break;
            case SMS:
                descriptionId = R.string.otb_other_data_sms_desc;
                nameId = R.string.otb_other_data_sms_title;
                break;
            case MICROPHONE:
                descriptionId = R.string.otb_other_data_microphone_desc;
                nameId = R.string.otb_other_data_microphone_title;
                break;
            case PHONE:
                descriptionId = R.string.otb_other_data_phone_desc;
                nameId = R.string.otb_other_data_phone_title;
                break;
            case SENSORS:
                descriptionId = R.string.otb_other_data_connected_devices_desc;
                nameId = R.string.otb_other_data_connected_devices_title;
                break;
            case BILLING:
                descriptionId = R.string.otb_usage_inapp_purchase_desc;
                nameId = R.string.otb_usage_inapp_purchase_title;
                break;
            case ADVERTISE:
                descriptionId = R.string.otb_usage_advertise_desc;
                nameId = R.string.otb_usage_advertise_title;
                break;
            case PEGI:
                descriptionId = R.string.otb_usage_pegi_desc;
                nameId = R.string.otb_usage_pegi_title;
                break;
            case SOCIAL_INFO:
                descriptionId = R.string.otb_usage_social_network_desc;
                nameId = R.string.otb_usage_social_network_title;
                break;
            default:
                break;
        }

        return new Pair<>(nameId, descriptionId);
    }


    private static AppUsesPermission getDefaultAppUsesPermissionForGroupType(GroupType groupType) {
        AppUsesPermission appUsesPermission;
        switch (groupType) {
            case IDENTITY:
            case ADVERTISE:
            case PEGI:
            case SOCIAL_INFO:
            case IMPROVEMENT_PROGRAM:
                appUsesPermission = AppUsesPermission.TRUE;
                break;
            default:
                appUsesPermission = PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(groupType);
                break;

        }

        return appUsesPermission;
    }


    public static List<TrustBadgeElement> getDefaultElements(Context context) {
        List<TrustBadgeElement> trustBadgeElements = new ArrayList<>();
        /** MANDATORY : MAIN BADGE */
        trustBadgeElements.add(build(context, GroupType.IDENTITY, ElementType.MAIN, AppUsesPermission.TRUE));
        trustBadgeElements.add(build(context, GroupType.LOCATION, ElementType.MAIN));
        trustBadgeElements.add(build(context, GroupType.STORAGE, ElementType.MAIN));
        trustBadgeElements.add(build(context, GroupType.CONTACTS, ElementType.MAIN));
        trustBadgeElements.add(build(context, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE));

        /** NOT MANDATORY : OTHERS BADGES */
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.CAMERA).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.CAMERA, ElementType.OTHERS));
        }
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.AGENDA).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.AGENDA, ElementType.OTHERS));
        }
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SMS).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.SMS, ElementType.OTHERS));
        }
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.MICROPHONE).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.MICROPHONE, ElementType.OTHERS));
        }
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.PHONE).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.PHONE, ElementType.OTHERS));
        }
        if (!PermissionManager.INSTANCE.getAppUsesPermissionForGroupType(GroupType.SENSORS).equals(AppUsesPermission.FALSE)) {
            trustBadgeElements.add(build(context, GroupType.SENSORS, ElementType.OTHERS));
        }

        /** MANDATORY : USAGE BADGES */
        trustBadgeElements.add(build(context, RatingType.TWELVE));
        trustBadgeElements.add(build(context, GroupType.BILLING, ElementType.USAGE));
        trustBadgeElements.add(build(context, GroupType.ADVERTISE, ElementType.USAGE));
        trustBadgeElements.add(build(context, GroupType.SOCIAL_INFO, ElementType.USAGE));

        return trustBadgeElements;
    }
}
