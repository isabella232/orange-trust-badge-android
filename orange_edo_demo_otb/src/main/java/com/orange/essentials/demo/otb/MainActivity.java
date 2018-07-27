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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.orange.essentials.demo.otb.dialog.OtbImprovementDialogFragment;
import com.orange.essentials.otb.OtbActivity;
import com.orange.essentials.otb.manager.BadgeListener;
import com.orange.essentials.otb.manager.TrustBadgeManager;
import com.orange.essentials.otb.model.TrustBadgeElement;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;


public class MainActivity extends AppCompatActivity implements BadgeListener {

    public static final String TAG = "MainActivity";
    public static final String PREF_IMPROVEMENT = "improvement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_brand);
        setTitle(R.string.app_name);
    }

    public void startDemOTB(View v) {
        TrustBadgeManager.INSTANCE.clearBadgeListeners();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        CustomBadgeFactory customBadgeFactory = new CustomBadgeFactory(getApplicationContext());
        TrustBadgeManager.INSTANCE.initialize(getApplicationContext(), customBadgeFactory.getTrustBadgeElements(), customBadgeFactory.getTerms());
        //You may add a custom card if wanted
        TrustBadgeManager.INSTANCE.addCustomCard("Sample card", "Put your own content in a custom card and view.", new CustomFragment());

        //The badge provide a method for standard Improvement Program Toggle switch
        //We save its status on shared preferences
        boolean improvement = sp.getBoolean(PREF_IMPROVEMENT, true);
        Log.d(TAG, "Improvement Program?" + improvement);

        //If you have others or custom toggle switchs (such as customBadge in this example)
        // you need to initialize them manually
        for (TrustBadgeElement element : TrustBadgeManager.INSTANCE.getTrustBadgeElements()) {
            if (element.getGroupType() == GroupType.IMPROVEMENT_PROGRAM) {
                element.setUserPermissionStatus(improvement ? UserPermissionStatus.GRANTED : UserPermissionStatus.NOT_GRANTED);
            } else {
                if (element.isToggable()) {
                    boolean granted = sp.getBoolean(element.getNameKey(), true);
                    Log.d(TAG, "Setting " + element.getNameKey() + " to value " + granted);
                    element.setUserPermissionStatus(granted ? UserPermissionStatus.GRANTED : UserPermissionStatus.NOT_GRANTED);
                }
            }
        }

        //We can add a badge listener. Make sure to do not have multiple copy of the same listener
        //You can also clear existing listener using: TrustBadgeManager.INSTANCE.clearBadgeListeners();
        if (!TrustBadgeManager.INSTANCE.getBadgeListeners().contains(this)) {
            TrustBadgeManager.INSTANCE.addBadgeListener(this);
        }

        Intent intent = new Intent(MainActivity.this, OtbActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBadgeChange(TrustBadgeElement trustBadgeElement,
                              boolean value, AppCompatActivity callingActivity) {
        Log.d(TAG, "onBadgeChange trustBadgeElement =" + trustBadgeElement + " value=" + value);
        if (null != trustBadgeElement) {
            //In this example we treat differently the improvement Program switch
            if (GroupType.IMPROVEMENT_PROGRAM.equals(trustBadgeElement.getGroupType())) {
                if (!value) {
                    //if user is deactivating the program, we ask for confirmation
                    showDialog(callingActivity);
                } else {
                    //Activate again the improvement program
                    saveValueToPreferences(true, PREF_IMPROVEMENT);
                    trustBadgeElement.setUserPermissionStatus(UserPermissionStatus.GRANTED);
                }
            } else {
                //We do not use the dialog for other switches
                //Save this new value to preferences to make it persistent
                saveValueToPreferences(value, trustBadgeElement.getNameKey());
                //Change the UserPermissionStatus as it has been switched by user
                trustBadgeElement.setUserPermissionStatus(value ? UserPermissionStatus.GRANTED : UserPermissionStatus.NOT_GRANTED);
            }
        }
    }

    private void saveValueToPreferences(boolean value, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void showDialog(AppCompatActivity callingActivity) {
        FragmentTransaction ft = callingActivity.getSupportFragmentManager().beginTransaction();
        Fragment prev = callingActivity.getSupportFragmentManager().findFragmentByTag(callingActivity.getLocalClassName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment dialogFragment = OtbImprovementDialogFragment.newInstance();
        dialogFragment.show(ft, callingActivity.getLocalClassName());
    }

}
