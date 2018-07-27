package com.orange.demo.otb.dialog;


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

import com.orange.demo.otb.MainActivity;
import com.orange.demo.otb.R;
import com.orange.essentials.otb.manager.TrustBadgeManager;
import com.orange.essentials.otb.model.type.GroupType;
import com.orange.essentials.otb.model.type.UserPermissionStatus;

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
                TrustBadgeManager.INSTANCE.badgeChanged(TrustBadgeManager.INSTANCE.getSpecificPermission(GroupType.IMPROVEMENT_PROGRAM), true, (AppCompatActivity) getActivity());
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
                TrustBadgeManager.INSTANCE.getSpecificPermission(GroupType.IMPROVEMENT_PROGRAM).setUserPermissionStatus(UserPermissionStatus.NOT_GRANTED);
                TrustBadgeManager.INSTANCE.badgeChanged(TrustBadgeManager.INSTANCE.getSpecificPermission(GroupType.IMPROVEMENT_PROGRAM), false, (AppCompatActivity) getActivity());
                editor.apply();
                /** If you are using AZME use
                 *      EngagementAgent.getInstance(getApplicationContext()).setEnabled(value);
                 *  to propagate it to the library */
                dismiss();
            }
        });

        return v;
    }
}

