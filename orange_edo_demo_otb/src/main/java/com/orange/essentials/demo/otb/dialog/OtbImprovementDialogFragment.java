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
package com.orange.essentials.demo.otb.dialog;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.orange.essentials.demo.otb.MainActivity;
import com.orange.essentials.demo.otb.R;
import com.orange.essentials.otb.manager.TrustBadgeManager;
import com.orange.essentials.otb.model.type.GroupType;

/**
 * Created by fab on 2/3/16.
 */
public class OtbImprovementDialogFragment extends DialogFragment {

    public static final String TAG = "ImprovementDialog";

    public static DialogFragment newInstance() {
        return new OtbImprovementDialogFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.otb_dialog_toggle, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) v.findViewById(R.id.otb_dialog_toggle_bt_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick Cancel");
                TrustBadgeManager.INSTANCE.badgeChanged(GroupType.IMPROVEMENT_PROGRAM, true, (AppCompatActivity) getActivity());
                dismiss();
            }
        });

        Button okButton = (Button) v.findViewById(R.id.otb_dialog_toggle_bt_deactivate);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //This is an example of what could be done
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(MainActivity.PREF_IMPROVEMENT, false);
                editor.apply();

                dismiss();
            }
        });

        return v;
    }
}

