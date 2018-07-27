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
package com.orange.essentials.otb.model

import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.ElementType
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus

import java.io.Serializable

/**
 *
 * File name:   TrustBadgeElement
 * Created:     12/10/2015
 * Created by:  VEEB7280 (Vincent BOESCH)
 *
 * Class representing an element which can be either a system permission, usage permissions, or app dedicated permission
 */
data class TrustBadgeElement(
        /**
         * data information id (example : IDENTITY, CAMERA, etc...)
         * unique id for information inside groupInformation,
         */
        val groupType: GroupType,
        /**
         * group permission data type (example : OTHERS, MAIN, USAGE)
         */
        val elementType: ElementType,
        /**
         * nameKey : nameKey to be displayed in the Expandable list view
         */
        var nameKey: String = groupType.name,
        /**
         * descriptionKey  ( OTB rules : 12)
         * text to inform user about the use and goals about the collected data
         * this text is provided by app  OTB component's host app.
         * status : Mandatory for key datas only
         * updated according OTB Rules v.2 on 13/10/2015
         */
        var descriptionKey: String? = groupType.name,
        /**
         * Host APP UsageStatus (example : TRUE, FALSE)
         * status : Mandatory for Orange Permissions (
         */
        var appUsesPermission: AppUsesPermission = AppUsesPermission.TRUE,
        /**
         * UserAllowedPermissionStatus (example : NOT_YET_GRANTED, MANDATORY, GRANTED, etc...)
         * User permission status
         * status : Mandatory for Orange Permissions (
         */
        var userPermissionStatus: UserPermissionStatus = UserPermissionStatus.GRANTED,
        /**
         * data information granted icon Id
         */
        var iconId: Int = 0,
        /**
         * isToggable : should your element display a toggle
         */
        var isToggable: Boolean = false,
        /***
         * isShouldBeAutoConfigured : should your element be configured automatically if system permission
         */
        var isShouldBeAutoConfigured: Boolean = true
) : Serializable
