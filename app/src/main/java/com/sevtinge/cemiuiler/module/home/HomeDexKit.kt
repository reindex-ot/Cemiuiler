package com.sevtinge.cemiuiler.module.home

import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.DexKit.closeDexKit
import com.sevtinge.cemiuiler.utils.DexKit.dexKitBridge
import com.sevtinge.cemiuiler.utils.DexKit.initDexKit
import com.sevtinge.cemiuiler.utils.DexKit.loadDexKit
import io.luckypray.dexkit.descriptor.member.DexClassDescriptor
import io.luckypray.dexkit.enums.MatchType

class HomeDexKit : BaseHook() {
    override fun init() {
        System.loadLibrary("dexkit")
        initDexKit(lpparam)
        loadDexKit()
        try {
            mHomeResultClassMap = dexKitBridge.batchFindClassesUsingStrings {
                addQuery("HideAllApp", listOf("appInfo.packageName", "activityInfo"))
                matchType = MatchType.FULL
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        closeDexKit()
    }

    companion object {
        var mHomeResultClassMap: Map<String, List<DexClassDescriptor>>? = null
    }
}
