package com.sevtinge.cemiuiler.module.securitycenter

import android.annotation.SuppressLint
import android.content.Context
import android.util.ArrayMap
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.getObjectField
import org.lsposed.hiddenapibypass.HiddenApiBypass

class RemoveConversationBubbleSettingsRestriction : BaseHook() {
    @SuppressLint("PrivateApi")
    override fun init() {
        loadClass("com.miui.bubbles.settings.BubblesSettings").methodFinder().first {
            name == "getDefaultBubbles"
        }.createHook {
            before { param ->
                val classBubbleApp = loadClass("com.miui.bubbles.settings.BubbleApp")
                val arrayMap = ArrayMap<String, Any>()
                val mContext =
                    param.thisObject.getObjectField("mContext") as Context
                val mCurrentUserId =
                    param.thisObject.getObjectField("mCurrentUserId") as Int
                val freeformSuggestionList = HiddenApiBypass.invoke(
                    Class.forName("android.util.MiuiMultiWindowUtils"),
                    null,
                    "getFreeformSuggestionList",
                    mContext
                ) as List<*>
                if (freeformSuggestionList.isNotEmpty()) {
                    for (str in freeformSuggestionList) {
                        val bubbleApp = classBubbleApp.getConstructor(
                            String::class.java, Int::class.java
                        ).newInstance(str, mCurrentUserId)
                        classBubbleApp.getMethod("setChecked", Boolean::class.java)
                            .invoke(bubbleApp, true)
                        arrayMap[str as String] = bubbleApp
                    }
                }
                param.result = arrayMap
            }
        }
    }
}
