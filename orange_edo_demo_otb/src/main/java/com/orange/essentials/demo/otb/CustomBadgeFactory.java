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
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.LOCATION, ElementType.MAIN));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.STORAGE, ElementType.MAIN));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CONTACTS, ElementType.MAIN));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE));

                /** NOT MANDATORY : OTHERS BADGES */
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CAMERA, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.AGENDA, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SMS, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.MICROPHONE, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.PHONE, ElementType.OTHERS));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SENSORS, ElementType.OTHERS));

                /** MANDATORY : USAGE BADGES */
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, RatingType.TWELVE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.BILLING, ElementType.USAGE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ADVERTISE, ElementType.USAGE));
                mTrustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SOCIAL_INFO, ElementType.USAGE));
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
