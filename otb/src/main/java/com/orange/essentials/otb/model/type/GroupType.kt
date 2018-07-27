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
package com.orange.essentials.otb.model.type

/**
 * PermissionGroupName managed in OTB
 * System PermissionGroupName have to match real Android permission group name
 * ex :
 */
enum class GroupType constructor(val isSystemPermission: Boolean, private val mPermissionNames: Array<String>?) {
    /** System Permissions : Permission group on 06/11/2015  */
    /**
     * android.permission-group.PERSONAL_INFO
     * OTB Displayed group permission :  Calendar (Agenda)
     */
    CALENDAR(true, arrayOf("READ_CALENDAR", "WRITE_CALENDAR")),
    /**
     * android.permission-group.CAMERA
     * OTB Displayed group permission : Camera (Appareil photo)
     */
    CAMERA(true, arrayOf("CAMERA")),
    /**
     * android.permission-group.CONTACTS
     * OTB Displayed group permission : Contacts (Contacts)
     */
    CONTACTS(true, arrayOf("READ_CONTACTS", "WRITE_CONTACTS", "GET_ACCOUNTS")),
    /**
     * android.permission-group.LOCATION
     * OTB Displayed group permission : Location (Position)
     */
    LOCATION(true, arrayOf("ACCESS_FINE_LOCATION", "ACCESS_COARSE_LOCATION")),
    /**
     * android.permission-group.RECORD_AUDIO
     * OTB Displayed group permission :  Micro
     */
    MICROPHONE(true, arrayOf("RECORD_AUDIO")),
    /**
     * android.permission-group.PHONE_CALLS
     * OTB Displayed group permission :  Phone (telephone)
     */
    PHONE(true, arrayOf("READ_PHONE_STATE", "CALL_PHONE", "READ_CALL_LOG", "WRITE_CALL_LOG", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS")),
    /**
     * android.permission-group.BODY_SENSORS
     * OTB Displayed group permission :  sensors (appels téléphoniques)
     */
    SENSORS(true, arrayOf("BODY_SENSORS")),
    /**
     * android.permission-group.MESSAGES
     * OTB Displayed group permission :  SMS
     */
    SMS(true, arrayOf("SEND_SMS", "RECEIVE_SMS", "READ_SMS", "RECEIVE_WAP_PUSH", "RECEIVE_MMS")),
    /**
     * android.permission-group.STORAGE
     * OTB Displayed group permission : STORAGE (Medias)
     */
    STORAGE(true, arrayOf("READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE")),

    /** Application data permissions  */

    /**
     * Not a permission
     * OTB Displayed group permission :  Notifications (notifications)
     */
    NOTIFICATIONS(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission : Account credentials (identifiants)
     */
    ACCOUNT_CREDENTIALS(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  Account informations (Infos de comptes)
     */
    ACCOUNT_INFO(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission : IMPROVEMENT_PROGRAM (Programme d'améliorations)
     */
    IMPROVEMENT_PROGRAM(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  Advertise (publicité)
     */
    ADVERTISE(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  History (Historique et préférences)
     */
    HISTORY(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  others (app specific)
     */
    CUSTOM_DATA(false, null);

    fun matchPermission(permissionName: String?): Boolean {
        var belongs = false
        if (permissionName != null) {
            if (mPermissionNames == null) {
                belongs = permissionName.contains(this.toString())
            } else {
                for (name in mPermissionNames) {
                    if (permissionName.contains(name)) {
                        belongs = true
                        break
                    }
                }
            }
        }
        return belongs
    }
}
