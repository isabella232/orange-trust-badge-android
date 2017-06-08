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
package com.orange.essentials.otb

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.orange.essentials.otb.event.EventType
import com.orange.essentials.otb.manager.BadgeListener
import com.orange.essentials.otb.manager.TrustBadgeManager
import com.orange.essentials.otb.model.Term
import com.orange.essentials.otb.model.TrustBadgeElement
import com.orange.essentials.otb.model.type.GroupType
import com.orange.essentials.otb.ui.OtbContainerFragment
import com.orange.essentials.otb.ui.OtbDataFragment
import com.orange.essentials.otb.ui.OtbTermsFragment
import com.orange.essentials.otb.ui.OtbUsageFragment
import java.io.Serializable


/**
 * OtbActivity
 * Main activity for the lib
 */

class OtbActivity : AppCompatActivity(), OtbContainerFragment.OtbFragmentListener, BadgeListener {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.otb_activity)

        /** Manage toolbar  */
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.otb_app_name)
        }

        /** Calling the fragment  */
        if (savedInstanceState == null) {
            isMasterDetail = useMasterDetail()
            initFragments()
        } else {
            Log.v(TAG, "savedInstanceState != null")
            Log.v(TAG, "useMasterDetail: " + useMasterDetail())
            if (useMasterDetail() != isMasterDetail) {
                Log.v(TAG, "popBackstack")
                //restore from scratch
                val fm = supportFragmentManager
                for (i in 0..fm.backStackEntryCount - 1) {
                    fm.popBackStack()
                }
                initFragments()
                isMasterDetail = useMasterDetail()
            }
        }

        TrustBadgeManager.INSTANCE.addBadgeListener(this)
    }

    private fun initFragments() {
        val otbContainerFrag = supportFragmentManager.findFragmentByTag(OtbContainerFragment.FRAG_TAG)
        if (otbContainerFrag == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.lightfragment_container, OtbContainerFragment.newInstance(), OtbContainerFragment.FRAG_TAG)
                    .commit()
        } else {
            supportFragmentManager.beginTransaction()
                    .attach(otbContainerFrag)
                    .commit()
        }

        // Landscape mode
        if (useMasterDetail()) {
            Log.d(TAG, "Landscape mode - add child fragment")
            var frag: Fragment? = null
            var tag: String? = null
            if (TrustBadgeManager.INSTANCE.hasData()) {
                Log.d(TAG, "Landscape mode - add data fragment")
                frag = OtbDataFragment()
                tag = OtbDataFragment.FRAG_TAG
            } else if (TrustBadgeManager.INSTANCE.hasUsage()) {
                Log.d(TAG, "Landscape mode - add usage fragment")
                frag = OtbUsageFragment()
                tag = OtbUsageFragment.FRAG_TAG
            } else if (TrustBadgeManager.INSTANCE.hasTerms()) {
                Log.d(TAG, "Landscape mode - add terms fragment")
                frag = OtbTermsFragment()
                tag = OtbTermsFragment.FRAG_TAG
            } else {
                Log.d(TAG, "Landscape mode - No item found, add data fragment by default")
                frag = OtbDataFragment()
                tag = OtbDataFragment.FRAG_TAG
            }
            supportFragmentManager.beginTransaction()
                    .add(R.id.lightfragment_detail, frag, tag)
                    .commit()

        }
    }

    // region OtbContainerFragment.ContainerFragmentListener

    override fun onDataClick() {
        Log.d(TAG, "onDataClick")
        displayDetail(OtbDataFragment(), OtbDataFragment.FRAG_TAG)
    }

    override fun onUsageClick() {
        Log.d(TAG, "onUsageClick")
        displayDetail(OtbUsageFragment(), OtbUsageFragment.FRAG_TAG)
    }

    override fun onTermsClick() {
        Log.d(TAG, "onTermsClick")
        displayDetail(OtbTermsFragment(), OtbTermsFragment.FRAG_TAG)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            val fm = supportFragmentManager
            if (fm.backStackEntryCount > 0) {
                fm.popBackStack()
            } else {
                finish()
            }
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_LEAVE)
        super.onDestroy()
    }

    // endregion


    // region Private methods
    private fun useMasterDetail(): Boolean {
        return findViewById<View>(R.id.lightfragment_detail) != null
    }

    private fun displayDetail(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        if (useMasterDetail()) {
            transaction.replace(R.id.lightfragment_detail, fragment, tag)
        } else {
            transaction.replace(R.id.lightfragment_container, fragment, tag)
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onBadgeChange(trustBadgeElement: TrustBadgeElement, value: Boolean, callingActivity: AppCompatActivity) {
        Log.d(TAG, "onChange trustBadgeElement=$trustBadgeElement value=$value")
        if (GroupType.IMPROVEMENT_PROGRAM == trustBadgeElement.groupType) {
            TrustBadgeManager.INSTANCE.isUsingImprovementProgram = value
            val frag: Fragment
            if (useMasterDetail()) {
                frag = callingActivity.supportFragmentManager.findFragmentById(R.id.lightfragment_detail)
            } else {
                frag = callingActivity.supportFragmentManager.findFragmentById(R.id.lightfragment_container)
            }
            if (frag is OtbDataFragment) {
                frag.refreshPermission()
            }
        }
        //No action defined for other badges
    }
    // endregion


    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "Saving Factory")
        super.onSaveInstanceState(outState)
        outState.putSerializable(BADGES_KEY, TrustBadgeManager.INSTANCE.trustBadgeElements as Serializable)
        outState.putSerializable(TERMS_KEY, TrustBadgeManager.INSTANCE.terms as Serializable)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "Restoring factory")
        super.onRestoreInstanceState(savedInstanceState)
        restoreFactory(savedInstanceState)
    }

    private fun restoreFactory(savedInstanceState: Bundle?) {
        if (null != savedInstanceState && null != savedInstanceState.getSerializable(BADGES_KEY)) {
            Log.d(TAG, "Restoring factory from instanceState")
            val badges = savedInstanceState.getSerializable(BADGES_KEY) as MutableList<TrustBadgeElement>
            val terms = savedInstanceState.getSerializable(TERMS_KEY) as List<Term>
            TrustBadgeManager.INSTANCE.initialize(applicationContext, badges, terms)
        }
    }

    companion object {

        private val TAG = "OtbActivity"
        private val BADGES_KEY = "BadgesKey"
        private val TERMS_KEY = "TermsKey"

        private var isMasterDetail: Boolean = false
    }
}
