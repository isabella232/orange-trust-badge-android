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
import com.orange.essentials.otb.ui.OtbAppDataFragment
import com.orange.essentials.otb.ui.OtbContainerFragment
import com.orange.essentials.otb.ui.OtbPermissionsFragment
import com.orange.essentials.otb.ui.OtbTermsFragment
import java.io.Serializable

/**
 * OtbActivity
 * Main activity for the lib
 */
open class OtbActivity : AppCompatActivity(), OtbContainerFragment.OtbFragmentListener, BadgeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.otb_activity)
        /** Manage toolbar  */
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.otb_app_name)
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
                for (i in 0 until fm.backStackEntryCount) {
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
            val frag: Fragment?
            val tag: String?
            when {
                TrustBadgeManager.INSTANCE.hasPermissions() -> {
                    Log.d(TAG, "Landscape mode - add data fragment")
                    frag = OtbPermissionsFragment()
                    tag = OtbPermissionsFragment.FRAG_TAG
                }
                TrustBadgeManager.INSTANCE.hasAppData() -> {
                    Log.d(TAG, "Landscape mode - add usage fragment")
                    frag = OtbAppDataFragment()
                    tag = OtbAppDataFragment.FRAG_TAG
                }
                TrustBadgeManager.INSTANCE.hasTerms() -> {
                    Log.d(TAG, "Landscape mode - add terms fragment")
                    frag = OtbTermsFragment()
                    tag = OtbTermsFragment.FRAG_TAG
                }
                else -> {
                    Log.d(TAG, "Landscape mode - No item found, add data fragment by default")
                    frag = OtbPermissionsFragment()
                    tag = OtbPermissionsFragment.FRAG_TAG
                }
            }
            supportFragmentManager.beginTransaction()
                    .add(R.id.lightfragment_detail, frag, tag)
                    .commit()

        }
    }

    // region OtbContainerFragment.ContainerFragmentListener
    override fun onCardClick(index: Int) {
        Log.d(TAG, "onCardClick, index $index")
        when (index) {
            OtbContainerFragment.DATA_SELECTED -> {
                displayDetail(OtbPermissionsFragment(), OtbPermissionsFragment.FRAG_TAG)
                TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_GO_TO_PERMISSION)
            }
            OtbContainerFragment.USAGE_SELECTED -> {
                displayDetail(OtbAppDataFragment(), OtbAppDataFragment.FRAG_TAG)
                TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_GO_TO_USAGE)
            }
            OtbContainerFragment.TERM_SELECTED -> {
                displayDetail(OtbTermsFragment(), OtbTermsFragment.FRAG_TAG)
                TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_GO_TO_TERMS)
            }
            else -> displayDetail(TrustBadgeManager.INSTANCE.customDataFragments[index - 3].fragment, TrustBadgeManager.INSTANCE.customDataFragments[index - 3].fragment.tag.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            (item.itemId == android.R.id.home) -> {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack()
                } else {
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        when (trustBadgeElement.groupType) {
            GroupType.IMPROVEMENT_PROGRAM -> {
                refreshAll(callingActivity)
            }
            GroupType.CUSTOM_DATA -> {
                refreshAll(callingActivity)
            }
            else -> {
            }
        }
    }

    private fun refreshAll(callingActivity: AppCompatActivity) {
        val frag = if (useMasterDetail()) {
            callingActivity.supportFragmentManager.findFragmentById(R.id.lightfragment_detail)
        } else {
            callingActivity.supportFragmentManager.findFragmentById(R.id.lightfragment_container)
        }
        if (frag is OtbPermissionsFragment) {
            frag.refreshPermissions()
        }
        if (frag is OtbAppDataFragment) {
            frag.refreshAppData()
        }
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
        restoreFactory(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    private fun restoreFactory(savedInstanceState: Bundle?) {
        savedInstanceState?.getSerializable(BADGES_KEY)?.let {
            Log.d(TAG, "Restoring factory from instanceState")
            val badges = savedInstanceState.getSerializable(BADGES_KEY) as MutableList<TrustBadgeElement>
            val terms = savedInstanceState.getSerializable(TERMS_KEY) as List<Term>
            TrustBadgeManager.INSTANCE.initialize(applicationContext, badges, terms)
        }
    }

    override fun onResume() {
        super.onResume()
        TrustBadgeManager.INSTANCE.eventTagger?.tag(EventType.TRUSTBADGE_ENTER)
    }

    companion object {
        private const val TAG = "OtbActivity"
        private const val BADGES_KEY = "BadgesKey"
        private const val TERMS_KEY = "TermsKey"
        var isMasterDetail: Boolean = false
    }
}
