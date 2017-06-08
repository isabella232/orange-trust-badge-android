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
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Binder
import android.os.Build
import android.util.Log

import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus

import java.util.ArrayList

/**
 * PermissionManager
 * This singleton is used to check app permission and affect the state of a group permission to the trustbadges
 */
enum class PermissionManager {
    INSTANCE;

    /**
     * mGroupPermissionDataList
     * Store Permission Informations from host app Manifest.xml
     */
    private val mGroupNameList = ArrayList<String>()

    var isInitialized = false
        private set

    fun initPermissionList(context: Context) {
        Log.d(TAG, "initPermissionList for context " + context)
        //Cleaning old stored groups
        mGroupNameList.clear()
        var pkgInfo: PackageInfo? = null
        try {
            pkgInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
            )

        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "PackageManagerName NOT found.", e)
        }

        if (null != pkgInfo) {
            //getting Host app permissions declared in Manifest.xml
            val requestedPermissions = pkgInfo.requestedPermissions
            if (requestedPermissions != null) {
                Log.d(TAG, "requestedPermissions is not null")
                for (i in requestedPermissions.indices) {
                    Log.d(TAG, "Adding permission : " + requestedPermissions[i])
                    mGroupNameList.add(requestedPermissions[i])
                    try {
                        Log.d(TAG, "Looking group for permission " + requestedPermissions[i])
                        val pinfo = context.packageManager.getPermissionInfo(requestedPermissions[i], PackageManager.GET_META_DATA)
                        if (pinfo.group != null) {
                            Log.d(TAG, "Adding permission group " + pinfo.group)
                            mGroupNameList.add(pinfo.group)
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.d(TAG, "PackageManagerName NOT found. Adding permission name " + requestedPermissions[i] + ", " + e.message)
                    }

                }
            }
            isInitialized = true
        } else {
            Log.d(TAG, "pkgInfo not found")
        }
    }


    /**
     * Return the user status about a specific permission

     * @param groupType : permission to be check
     * *
     * @return Values.USER_STATUS
     */
    fun doesUserAlreadyAcceptPermission(context: Context, groupType: GroupType): UserPermissionStatus {
        Log.d(TAG, "doesUserAlreadyAcceptPermissionGroup " + groupType)
        val result: UserPermissionStatus

        if (groupType == GroupType.IMPROVEMENT_PROGRAM || groupType == GroupType.IDENTITY) {
            result = UserPermissionStatus.MANDATORY
        } else {
            val currentapiVersion = Build.VERSION.SDK_INT
            if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                Log.d(TAG, "currentapiVersion >= Build.VERSION_CODES.M =true")
                val permission = context.checkPermission(findInAppPermission(context, groupType), android.os.Process.myPid(), Binder.getCallingUid())
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    result = UserPermissionStatus.NOT_GRANTED
                } else {
                    result = UserPermissionStatus.GRANTED
                }
            } else {
                if (!findInAppPermission(context, groupType).isEmpty()) {
                    result = UserPermissionStatus.GRANTED
                } else {
                    result = UserPermissionStatus.NOT_GRANTED

                }

            }
        }
        Log.d(TAG, "doesUserAlreadyAcceptPermissionGroup $groupType is $result")
        return result
    }


    /**
     * Retrieve the permissionGroupName of a specific permission

     * @param groupType : permission to find in GroupPermissionList
     * *
     * @return String containing the permissionGroupName
     */
    fun findInAppPermission(context: Context, groupType: GroupType): String {
        var result = ""
        try {
            val pkgInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
            )
            val requestedPermissions = pkgInfo.requestedPermissions
            for (permissionData in requestedPermissions) {
                if (groupType.matchPermission(permissionData)) {
                    result = permissionData
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "findInAppPermission, package manager not found", e)
        }

        Log.d(TAG, "finInAppPermission for groupType $groupType returns $result")
        return result
    }

    fun getGroupNameForGroupType(groupType: GroupType): String? {
        var result: String? = null
        Log.d(TAG, "getGroupName " + groupType + " in " + mGroupNameList.size)
        for (data in mGroupNameList) {
            if (groupType.matchPermission(data)) {
                result = data
                break
            }
        }
        return result
    }


    fun getAppUsesPermissionForGroupType(groupType: GroupType): AppUsesPermission {
        val appUsesPermission: AppUsesPermission
        if (getGroupNameForGroupType(groupType) != null) {
            appUsesPermission = AppUsesPermission.TRUE
        } else {
            if (groupType.isSystemPermission) {
                appUsesPermission = AppUsesPermission.FALSE
            } else {
                appUsesPermission = AppUsesPermission.NOT_SIGNIFICANT
            }
        }
        return appUsesPermission
    }

    companion object {

        val TAG = "PermissionManager"
    }
}
