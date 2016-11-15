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
import com.orange.essentials.otb.model.type.RatingType;
import com.orange.essentials.otb.model.type.TermType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veeb7280 on 08/02/2016.
 */
public class CustomBadgeFactory {

    private transient Context mContext;
    private List<TrustBadgeElement> mTrustBadgeElements = null;
    private List<Term> mTerms = null;

    public CustomBadgeFactory(Context context) {
        this.mContext = context;
    }

    public List<TrustBadgeElement> getTrustBadgeElements() {
        if (null == mTrustBadgeElements) {
            if (null != mContext) {
                mTrustBadgeElements = new ArrayList<>();
                /** MANDATORY : MAIN BADGE */

                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IDENTITY, ElementType.MAIN, AppUsesPermission.TRUE));

                //Define your own system app permission status
                //No scan for app permission is done in that case
                TrustBadgeElement elt = TrustBadgeElementFactory.build(mContext, GroupType.LOCATION, ElementType.MAIN);
                elt.setShouldBeAutoConfigured(false);
                elt.setAppUsesPermission(AppUsesPermission.TRUE);
                elt.setUserPermissionStatus(UserPermissionStatus.GRANTED);
                mTrustBadgeElements.add(elt);

                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.STORAGE, ElementType.MAIN));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CONTACTS, ElementType.MAIN));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE));

                //custom element in MAIN list
                TrustBadgeElement customBadge1 = TrustBadgeElementFactory.build(mContext, GroupType.OTHER, ElementType.MAIN, R.string.custom_badge_1_title, R.string.custom_badge_1_label);
                customBadge1.setEnabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge1.setDisabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge1.setToggable(true);
                customBadge1.setAppUsesPermission(AppUsesPermission.TRUE);
                mTrustBadgeElements.add(customBadge1);

                /** NOT MANDATORY : OTHERS BADGES */
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CAMERA, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.AGENDA, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SMS, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.MICROPHONE, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.PHONE, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SENSORS, ElementType.OTHERS));

                //custom element in OTHERS list
                TrustBadgeElement customBadge2 = TrustBadgeElementFactory.build(mContext, GroupType.OTHER, ElementType.OTHERS, R.string.custom_badge_2_title, R.string.custom_badge_2_label);
                customBadge2.setEnabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge2.setDisabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge2.setToggable(true);
                customBadge2.setAppUsesPermission(AppUsesPermission.TRUE);
                mTrustBadgeElements.add(customBadge2);

                /** MANDATORY : USAGE BADGES */
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, RatingType.THREE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.BILLING, ElementType.USAGE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ADVERTISE, ElementType.USAGE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SOCIAL_INFO, ElementType.USAGE));

                // Custom Element in usage badge
                TrustBadgeElement customBadge3 = TrustBadgeElementFactory.build(mContext, GroupType.OTHER, ElementType.USAGE, R.string.custom_badge_3_title, R.string.custom_badge_3_label);
                customBadge3.setEnabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge3.setDisabledIconId(R.drawable.otb_ic_contacts_black_32dp);
                customBadge3.setAppUsesPermission(AppUsesPermission.TRUE);
                customBadge3.setToggable(true);
                mTrustBadgeElements.add(customBadge3);
            }
        }
        return mTrustBadgeElements;
    }

    public List<Term> getTerms() {
        if (null == mTerms) {
            mTerms = new ArrayList<>();
            //Add video => This term will only work if device OS > 2.3
            mTerms.add(new Term(TermType.VIDEO, R.string.otb_terms_video_title, R.string.otb_terms_video_content));
            mTerms.add(new Term(TermType.TEXT, R.string.otb_terms_usage_title, R.string.otb_terms_usage_content));
            mTerms.add(new Term(TermType.TEXT, R.string.otb_terms_help_title, R.string.otb_terms_help_content));
            mTerms.add(new Term(TermType.TEXT, R.string.otb_terms_more_info_title, R.string.otb_terms_more_info_content));
        }
        return mTerms;
    }
}
