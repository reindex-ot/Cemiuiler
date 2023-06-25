package com.sevtinge.cemiuiler.module.systemui;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import de.robv.android.xposed.XposedHelpers;

public class NotificationFix extends BaseHook {
    @Override
    public void init() {
        XposedHelpers.setStaticBooleanField(XposedHelpers.findClass("com.android.systemui.statusbar.notification.NotificationSettingsManager", lpparam.classLoader), "USE_WHITE_LISTS", false);
    }
}
