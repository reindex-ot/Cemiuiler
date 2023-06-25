package com.sevtinge.cemiuiler.module.home.title;

import com.sevtinge.cemiuiler.module.base.BaseHook;

public class EnableIconMonoChrome extends BaseHook {
    @Override
    public void init() {
        findAndHookMethod("com.miui.home.launcher.graphics.MonochromeUtils", "isSupportMonochrome", new MethodHook() {
            @Override
            protected void before(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }
}
