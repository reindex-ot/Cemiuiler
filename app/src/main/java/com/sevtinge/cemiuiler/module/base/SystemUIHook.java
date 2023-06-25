package com.sevtinge.cemiuiler.module.base;


import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isMoreAndroidVersion;

import android.os.Build;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public abstract class SystemUIHook extends BaseHook {

    public Class<?> mPluginLoaderClass;
    public String mPluginLoaderClassName;

    @Override
    public void setLoadPackageParam(LoadPackageParam param) {
        super.setLoadPackageParam(param);

        if (isMoreAndroidVersion(Build.VERSION_CODES.TIRAMISU)) {
            mPluginLoaderClassName = "com.android.systemui.shared.plugins.PluginInstance$Factory";
        } else {
            mPluginLoaderClassName = "com.android.systemui.shared.plugins.PluginManagerImpl";
        }

        mPluginLoaderClass = findClassIfExists(mPluginLoaderClassName);
    }
}
