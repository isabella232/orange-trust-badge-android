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
/* Orange Trust Badge library demo app
 *
 * Version:     1.0
 * Created:     2016-03-15 by Aurore Penault, Vincent Boesch, and Giovanni Battista Accetta
 */
package com.orange.essentials.demo.otb;

import android.content.Context;

import com.orange.essentials.otb.manager.TrustBadgeElementFactory;
import com.orange.essentials.otb.model.Term;
import com.orange.essentials.otb.model.TrustBadgeElement;
import com.orange.essentials.otb.model.type.AppUsesPermission;
import com.orange.essentials.otb.model.type.ElementType;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veeb7280 on 08/02/2016.
 */
public class CustomBadgeFactory {

    private transient Context mContext;

    public CustomBadgeFactory(Context context) {
        this.mContext = context;
    }

    public List<TrustBadgeElement> getTrustBadgeElements() {
        List<TrustBadgeElement> mTrustBadgeElements = null;
        if (null != mContext) {
            mTrustBadgeElements = new ArrayList<>();

            mTrustBadgeElements.addAll(TrustBadgeElementFactory.getDefaultPermissionElements(mContext));

            /* System permissions */
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.LOCATION, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CONTACTS, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.STORAGE, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CALENDAR, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.PHONE, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SMS, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CAMERA, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.MICROPHONE, ElementType.PERMISSIONS));
//                trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SENSORS, ElementType.PERMISSIONS));
//
            /* App data elements */
            mTrustBadgeElements.addAll(TrustBadgeElementFactory.getDefaultAppDataElements(mContext));

//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.NOTIFICATIONS, ElementType.APP_DATA, AppUsesPermission.TRUE, false));
//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ACCOUNT_CREDENTIALS, ElementType.APP_DATA, AppUsesPermission.TRUE, false));
//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ACCOUNT_INFO, ElementType.APP_DATA, AppUsesPermission.FALSE, false));
//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IMPROVEMENT_PROGRAM, ElementType.APP_DATA, AppUsesPermission.TRUE, true));
//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ADVERTISE, ElementType.APP_DATA, AppUsesPermission.FALSE, false));
//                //Optional elements
//                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.HISTORY, ElementType.APP_DATA, AppUsesPermission.FALSE, false));

            //a first custom badge in the Main list (set to be a toggle switch)
            TrustBadgeElement customBadge1 = TrustBadgeElementFactory.build(mContext, GroupType.CUSTOM_DATA, ElementType.APP_DATA, R.string.custom_badge_1_title, R.string.custom_badge_1_label);
            customBadge1.setIconId(R.drawable.otb_ic_contacts);
            customBadge1.setToggable(true);
            customBadge1.setAppUsesPermission(AppUsesPermission.TRUE);
            customBadge1.setUserPermissionStatus(UserPermissionStatus.GRANTED);
            mTrustBadgeElements.add(customBadge1);

        }
        return mTrustBadgeElements;
    }

    public List<Term> getTerms() {
        return TrustBadgeElementFactory.getDefaultTermElements(mContext);
    }
}
