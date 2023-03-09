package com.sevtinge.cemiuiler.module.home;

import com.sevtinge.cemiuiler.module.base.BaseHook;

public class EnableIconMonoChrome extends BaseHook {
        @Override
        public void init() {
            findAndHookMethod("com.miui.home.launcher.graphics.MonoChromeUtils", "isSupportMonoChrome", new MethodHook() {
                @Override
                protected void before(MethodHookParam param) throws Throwable {
                    param.setResult(true);
                }
            });
        }
    }