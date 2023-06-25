package com.sevtinge.cemiuiler.module;

import android.text.TextUtils;

import com.sevtinge.cemiuiler.module.base.BaseModule;
import com.sevtinge.cemiuiler.module.updater.DeviceModify;
import com.sevtinge.cemiuiler.module.updater.UpdaterDexKit;
import com.sevtinge.cemiuiler.module.updater.VabUpdate;
import com.sevtinge.cemiuiler.module.updater.VersionCodeModify;

public class Updater extends BaseModule {

    @Override
    public void handleLoadPackage() {
        initHook(new UpdaterDexKit());
        initHook(new VersionCodeModify(), !TextUtils.isEmpty(mPrefsMap.getString("various_updater_miui_version", "")));
        initHook(new VabUpdate(), mPrefsMap.getBoolean("updater_fuck_vab"));
        initHook(DeviceModify.INSTANCE, !TextUtils.isEmpty(mPrefsMap.getString("updater_device", "")));
    }
}
