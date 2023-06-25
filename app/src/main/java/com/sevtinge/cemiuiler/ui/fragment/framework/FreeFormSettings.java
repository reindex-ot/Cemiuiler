package com.sevtinge.cemiuiler.ui.fragment.framework;

import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isAndroidR;
import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isMoreMiuiVersion;

import android.view.View;

import com.sevtinge.cemiuiler.R;
import com.sevtinge.cemiuiler.ui.base.BaseSettingsActivity;
import com.sevtinge.cemiuiler.ui.fragment.base.SettingsPreferenceFragment;

import moralnorm.preference.PreferenceCategory;
import moralnorm.preference.SwitchPreference;

public class FreeFormSettings extends SettingsPreferenceFragment {

    SwitchPreference mMoreFreeForm; // 多小窗
    PreferenceCategory mSmallFreeForm; // 小窗气泡

    @Override
    public int getContentResId() {
        return R.xml.framework_freeform;
    }

    @Override
    public View.OnClickListener addRestartListener() {
        return view -> ((BaseSettingsActivity)getActivity()).showRestartSystemDialog();
    }

    @Override
    public void initPrefs() {
        mMoreFreeForm = findPreference("prefs_key_system_framework_freeform_count");
        mSmallFreeForm = findPreference("prefs_key_system_framework_freeform_bubble_title");

        mMoreFreeForm.setVisible(isMoreMiuiVersion(13f));
        mSmallFreeForm.setVisible(!isAndroidR());
    }
}
