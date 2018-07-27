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
import android.os.Binder
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.orange.essentials.otb.model.type.AppUsesPermission
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.model.type.UserPermissionStatus
import java.util.*

/**
 * PermissionManager
 * This singleton is used to check app permission and affect the state of a group permission to the trustbadges
 */
enum class PermissionManager {
    INSTANCE;

    /**
     * mGroupPermissionDataList
     * Store Permission informations from host app Manifest.xml
     */
    private val mGroupNameList = ArrayList<String>()
    var isInitialized = false
        private set

    fun initPermissionList(context: Context) {
        Log.d(TAG, "initPermissionList for context $context")
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
                Log.v(TAG, "requestedPermissions is not null")
                for (i in requestedPermissions.indices) {
                    Log.v(TAG, "Adding permission : " + requestedPermissions[i])
                    mGroupNameList.add(requestedPermissions[i])
                    try {
                        Log.v(TAG, "Looking group for permission " + requestedPermissions[i])
                        val pInfo = context.packageManager.getPermissionInfo(requestedPermissions[i], PackageManager.GET_META_DATA)
                        if (pInfo.group != null) {
                            Log.v(TAG, "Adding permission group " + pInfo.group)
                            mGroupNameList.add(pInfo.group)
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.v(TAG, "PackageManagerName NOT found. Adding permission name " + requestedPermissions[i] + ", " + e.message)
                    }

                }
            }
            isInitialized = true
        } else {
            Log.v(TAG, "pkgInfo not found")
        }
    }

    /**
     * Return the user status about a specific permission

     * @param groupType : permission to be check
     * *
     * @return Values.USER_STATUS
     */
    fun doesUserAlreadyAcceptPermission(context: Context, groupType: GroupType): UserPermissionStatus {
        Log.d(TAG, "doesUserAlreadyAcceptPermissionGroup $groupType")
        val result: UserPermissionStatus

        if (!groupType.isSystemPermission) {
            var ok = true
            if (GroupType.NOTIFICATIONS.equals(groupType)) {
                ok = NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
            if (ok) {
                result = UserPermissionStatus.MANDATORY
            } else {
                result = UserPermissionStatus.NOT_GRANTED
            }
        } else {
            val currentApiVersion = Build.VERSION.SDK_INT
            if (currentApiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                Log.d(TAG, "currentApiVersion >= Build.VERSION_CODES.M =true")
                val permissions = findInAppPermission(context, groupType)
                var found = false
                for (perm in permissions) {
                    val permission = context.checkPermission(perm, android.os.Process.myPid(), Binder.getCallingUid())
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        found = true
                        break
                    }
                }
                result = if (found) {
                    UserPermissionStatus.GRANTED
                } else {
                    UserPermissionStatus.NOT_GRANTED
                }
            } else {
                result = if (!findInAppPermission(context, groupType).isEmpty()) {
                    UserPermissionStatus.GRANTED
                } else {
                    UserPermissionStatus.NOT_GRANTED
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
    private fun findInAppPermission(context: Context, groupType: GroupType): ArrayList<String> {
        var result = ArrayList<String>()
        try {
            val pkgInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
            )
            val requestedPermissions = pkgInfo.requestedPermissions
            for (permissionData in requestedPermissions) {
                if (groupType.matchPermission(permissionData)) {
                    result.add(permissionData)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "findInAppPermission, package manager not found", e)
        }

        Log.d(TAG, "finInAppPermission for groupType $groupType returns $result")
        return result
    }

    private fun getGroupNameForGroupType(groupType: GroupType): String? {
        var result: String? = null
        Log.d(TAG, "getGroupName $groupType in ${mGroupNameList.size}")
        for (data in mGroupNameList) {
            if (groupType.matchPermission(data)) {
                result = data
                break
            }
        }
        return result
    }

    fun getAppUsesPermissionForGroupType(groupType: GroupType): AppUsesPermission {
        val appUsesPermission = if (getGroupNameForGroupType(groupType) != null) {
            AppUsesPermission.TRUE
        } else {
            if (groupType.isSystemPermission) {
                AppUsesPermission.FALSE
            } else {
                AppUsesPermission.NOT_SIGNIFICANT
            }
        }
        Log.d(TAG, "GroupType $groupType, appUses $appUsesPermission")
        return appUsesPermission
    }

    companion object {
        const val TAG = "PermissionManager"
    }
}
