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
package com.orange.essentials.otb.model.type;

/**
 * PermissionGroupName managed in OTB
 * System PermissionGroupName have to match real Android permission group name
 * ex :
 */
public enum GroupType {
    /** Mandatory Group permissions : Check on M Permission group on 06/11/2015 */
    /**
     * Not A permission
     * OTB Displayed group permission : Identity
     */
    IDENTITY(false, null),
    /**
     * android.permission-group.LOCATION
     * OTB Displayed group permission : Location
     */
    LOCATION(true, new String[]{"ACCESS_FINE_LOCATION", "ACCESS_COARSE_LOCATION"}),
    /**
     * android.permission-group.STORAGE
     * OTB Displayed group permission : STORAGE (Medias)
     */
    STORAGE(true, new String[]{"READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE"}),
    /**
     * Not A permission
     * OTB Displayed group permission : IMPROVEMENT_PROGRAM (Collecte de données)
     */
    IMPROVEMENT_PROGRAM(false, null),
    /**
     * android.permission-group.CONTACTS
     * OTB Displayed group permission : Contact
     */
    CONTACTS(true, new String[]{"READ_CONTACTS", "WRITE_CONTACTS", "GET_ACCOUNTS"}),

    /** Other group permissions */

    /**
     * android.permission-group.CAMERA
     * OTB Displayed group permission : Camera (App photo)
     */
    CAMERA(true, new String[]{"CAMERA"}),
    /**
     * android.permission-group.PERSONAL_INFO
     * OTB Displayed group permission :  Agenda
     */
    AGENDA(true, new String[]{"READ_CALENDAR", "WRITE_CALENDAR"}),
    /**
     * android.permission-group.MESSAGES
     * OTB Displayed group permission :  SMS
     */
    SMS(true, new String[]{"SEND_SMS", "RECEIVE_SMS", "READ_SMS", "RECEIVE_WAP_PUSH", "RECEIVE_MMS"}),
    /**
     * android.permission-group.RECORD_AUDIO
     * OTB Displayed group permission :  Microphone
     */
    MICROPHONE(true, new String[]{"RECORD_AUDIO"}),
    /**
     * android.permission-group.PHONE_CALLS
     * OTB Displayed group permission :  Phone (telephone)
     */
    PHONE(true, new String[]{"READ_PHONE_STATE", "CALL_PHONE", "READ_CALL_LOG", "WRITE_CALL_LOG", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS"}),
    /**
     * android.permission-group.BODY_SENSORS
     * OTB Displayed group permission :  sensors (appels téléphoniques)
     */
    SENSORS(true, new String[]{"BODY_SENSORS"}),
    /**
     * Not a permission
     * OTB Displayed group permission :  billing (achats in app)
     */
    BILLING(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  advertise (publicité)
     */
    ADVERTISE(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  pegi
     */
    PEGI(false, null),
    /**
     * Not A permission
     * OTB Displayed group permission :  social networks
     */
    SOCIAL_INFO(false, null);


    private boolean mSystemPermission;
    private String[] mPermissionNames;
    GroupType(boolean systemPermission, String[] systemPermissionNames) {
        mSystemPermission = systemPermission;
        mPermissionNames = systemPermissionNames;
    }

    public boolean isSystemPermission() {
        return mSystemPermission;
    }

    public boolean matchPermission(String permissionName) {
        boolean belongs = false;
        if (permissionName != null) {
            belongs = permissionName.contains(this.toString());
            if (!belongs && mPermissionNames != null) {
                for (String name : mPermissionNames) {
                    if (belongs = (permissionName.contains(name))) {
                        break;
                    }
                }
            }
        }
        return belongs;
    }
}
