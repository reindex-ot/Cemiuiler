package com.sevtinge.cemiuiler.module.securitycenter

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.sevtinge.cemiuiler.module.base.BaseHook

object IsSbnBelongToActiveBubbleApp : BaseHook() {
    override fun init() {
        try {
            loadClass("com.miui.bubbles.settings.BubblesSettings").methodFinder().first {
                name == "isSbnBelongToActiveBubbleApp"
            }.createHook {
                returnConstant(true)
            }
        } catch (e: Throwable) {
            logE(e)
        }
    }

}