package com.sevtinge.cemiuiler.ui.fragment;

import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isAndroidR;
import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isMoreAndroidVersion;
import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isMoreMiuiVersion;

import android.view.View;

import com.sevtinge.cemiuiler.R;
import com.sevtinge.cemiuiler.ui.base.BaseSettingsActivity;
import com.sevtinge.cemiuiler.ui.fragment.base.SettingsPreferenceFragment;

import moralnorm.preference.PreferenceCategory;
import moralnorm.preference.SwitchPreference;

public class SystemSettingsFragment extends SettingsPreferenceFragment {
    PreferenceCategory mHighMode; // 极致模式
    PreferenceCategory mNewNfc; // 新版 NFC 界面
    SwitchPreference mAreaScreenshot; // 区域截屏
    SwitchPreference mKnuckleFunction; // 指关节相关
    SwitchPreference mNoveltyHaptic; // 新版触感调节页面

    @Override
    public int getContentResId() {
        return R.xml.system_settings;
    }

    @Override
    public View.OnClickListener addRestartListener() {
        return view -> ((BaseSettingsActivity)getActivity()).showRestartDialog(
            getResources().getString(R.string.system_settings),
            "com.android.settings"
        );
    }

    @Override
    public void initPrefs() {
        mHighMode = findPreference("prefs_key_system_settings_develop_speed");
        mAreaScreenshot = findPreference("prefs_key_system_settings_area_screenshot");
        mKnuckleFunction = findPreference("prefs_key_system_settings_knuckle_function");
        mNewNfc = findPreference("prefs_key_system_settings_connection_sharing");
        mNoveltyHaptic = findPreference("prefs_key_system_settings_novelty_haptic");

        mHighMode.setVisible(!isAndroidR());
        mAreaScreenshot.setVisible(isAndroidR());
        mKnuckleFunction.setVisible(isMoreMiuiVersion(13f));
        mNewNfc.setVisible(isMoreMiuiVersion(14f) && isMoreAndroidVersion(33));
        mNoveltyHaptic.setVisible(isMoreMiuiVersion(14f) && isMoreAndroidVersion(31));
    }
}
