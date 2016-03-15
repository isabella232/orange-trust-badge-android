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
package com.orange.essentials.otb.model;

import com.orange.essentials.otb.model.type.AppUsesPermission;
import com.orange.essentials.otb.model.type.ElementType;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;

import java.io.Serializable;

/**
 * <p/>
 * File name:   TrustBadgeElement
 * Created:     12/10/2015
 * Created by:  VEEB7280 (Vincent BOESCH)
 * <p/>
 * Class representing an element which can be either a system permission, usage permissions, or app dedicated permission
 */
public class TrustBadgeElement implements Serializable {

    private static final long serialVersionUID = 8991654307608731484L;

    /**
     * data information id (example : IDENTITY, CAMERA, etc...)
     * unique id for information inside groupInformation,
     */
    private GroupType mGroupType;

    /**
     * group permission data type (example : OTHERS, MAIN, USAGE)
     */
    private ElementType mElementType;

    /**
     * Host APP UsageStatus (example : TRUE, FALSE)
     * status : Mandatory for Orange Permissions (
     */
    private AppUsesPermission mAppUsesPermission;

    /**
     * data information granted icon Id
     */
    private int mEnabledIconId;

    /**
     * data information Not Granted icon Id
     */
    private int mDisabledIconId;

    /**
     * mDescriptionKey  ( OTB rules : 12)
     * text to inform user about the use and goals about the collected data
     * this text is provided by app  OTB component's host app.
     * status : Mandatory for key datas only
     * updated according OTB Rules v.2 on 13/10/2015
     */
    private String mDescriptionKey;

    /**
     * UserAllowedPermissionStatus (example : NOT_YET_GRANTED, MANDATORY, GRANTED, etc...)
     * User permission status
     * status : Mandatory for Orange Permissions (
     */
    private UserPermissionStatus mUserPermissionStatus;


    /**
     * mNameKey : mNameKey to be displayed in the Expandable list view
     */
    private String mNameKey;


    /**
     * mToggable : shiould your element display a toggle
     */
    private boolean mToggable;

    /************************
     * PUBLIC METHODS ****************************************************
     */

    /**
     * Constructor with minimal values.
     * User should provide necessary complementary informations by using setters :
     * iconid (enabled/disabled), name, description, toggable bage, AppUsesPermission, and UserPermissionStatus
     * @param groupType the groupType for this badge
     * @param elementType the elementType of this badge
     */
    public TrustBadgeElement(GroupType groupType,
                             ElementType elementType){
        this(groupType, elementType, groupType.name(), groupType.name(), AppUsesPermission.TRUE, UserPermissionStatus.GRANTED, 0, 0, false);
    }


    /**
     * Creates a TrustBadgeElement with default non toggable status
     *
     * @param groupType            the groupType of this badge
     * @param elementType          the elementType of this badge
     * @param nameKey              the name of this bade
     * @param descriptionKey       the description of this badge
     * @param appUsesPermission    wether app uses this permission or not
     * @param userPermissionStatus wether user has granted this permission for this app
     * @param enabledIconId        enabled icon
     * @param disabledIconId       disabled icon
     */
    public TrustBadgeElement(GroupType groupType,
                             ElementType elementType,
                             String nameKey,
                             String descriptionKey,
                             AppUsesPermission appUsesPermission,
                             UserPermissionStatus userPermissionStatus,
                             int enabledIconId,
                             int disabledIconId) {
        this(groupType, elementType, nameKey, descriptionKey, appUsesPermission, userPermissionStatus, enabledIconId, disabledIconId, false);
    }

    /**
     * Constructor TrustBadgeElement
     *
     * @param groupType            the groupType of this badge
     * @param elementType          the elementType of this badge
     * @param nameKey              the name of this bade
     * @param descriptionKey       the description of this badge
     * @param appUsesPermission    wether app uses this permission or not
     * @param userPermissionStatus wether user has granted this permission for this app
     * @param enabledIconId        enabled icon
     * @param disabledIconId       disabled icon
     * @param toggable             wether this badge is toggable (visible switch)
     */
    public TrustBadgeElement(GroupType groupType,
                             ElementType elementType,
                             String nameKey,
                             String descriptionKey,
                             AppUsesPermission appUsesPermission,
                             UserPermissionStatus userPermissionStatus,
                             int enabledIconId,
                             int disabledIconId,
                             boolean toggable) {
        this.mGroupType = groupType;
        this.mElementType = elementType;
        this.mNameKey = nameKey;
        this.mDescriptionKey = descriptionKey;
        this.mAppUsesPermission = appUsesPermission;
        this.mUserPermissionStatus = userPermissionStatus;
        this.mEnabledIconId = enabledIconId;
        this.mDisabledIconId = disabledIconId;
        this.mToggable = toggable;
    }

    public boolean isToggable() {
        return mToggable;
    }

    public void setToggable(boolean toggable) {
        mToggable = toggable;
    }

    public GroupType getGroupType() {
        return mGroupType;
    }

    public String getDescriptionKey() {
        return mDescriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        mDescriptionKey = descriptionKey;
    }

    public UserPermissionStatus getUserPermissionStatus() {
        return mUserPermissionStatus;
    }

    public void setUserPermissionStatus(UserPermissionStatus userPermissionStatus) {
        this.mUserPermissionStatus = userPermissionStatus;
    }

    public String getNameKey() {
        return mNameKey;
    }

    public void setNameKey(String nameKey) {
        mNameKey = nameKey;
    }

    public ElementType getElementType() {
        return mElementType;
    }

    public AppUsesPermission getAppUsesPermission() {
        return mAppUsesPermission;
    }

    public void setAppUsesPermission(AppUsesPermission appUsesPermission) {
        mAppUsesPermission = appUsesPermission;
    }

    public int getEnabledIconId() {
        return mEnabledIconId;
    }

    public void setEnabledIconId(int enabledIconId) {
        mEnabledIconId = enabledIconId;
    }

    public int getDisabledIconId() {
        return mDisabledIconId;
    }

    public void setDisabledIconId(int disabledIconId) {
        mDisabledIconId = disabledIconId;
    }

    @Override
    public String toString() {
        return "TrustBadgeElement{" +
                "mAppUsesPermission=" + mAppUsesPermission +
                ", mGroupType=" + mGroupType +
                ", mElementType=" + mElementType +
                ", mEnabledIconId=" + mEnabledIconId +
                ", mDisabledIconId=" + mDisabledIconId +
                ", mDescriptionKey='" + mDescriptionKey + '\'' +
                ", mUserPermissionStatus=" + mUserPermissionStatus +
                ", mNameKey='" + mNameKey + '\'' +
                ", mToggable=" + mToggable +
                '}';
    }
}
