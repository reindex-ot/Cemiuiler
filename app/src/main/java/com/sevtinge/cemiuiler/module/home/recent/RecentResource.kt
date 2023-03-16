package com.sevtinge.cemiuiler.module.home.recent

import com.sevtinge.cemiuiler.module.base.BaseHook
import android.content.res.Resources
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.Log
import com.sevtinge.cemiuiler.utils.*
import de.robv.android.xposed.XC_MethodHook

object RecentResource : BaseHook() {
    private val hookMap = ResourcesHookMap<String, ResourcesHookData>()
    private fun hook(param: XC_MethodHook.MethodHookParam) {
        try {
            val resName = appContext.resources.getResourceEntryName(param.args[0] as Int)
            val resType = appContext.resources.getResourceTypeName(param.args[0] as Int)
            if (hookMap.isKeyExist(resName)) if (hookMap[resName]?.type == resType) {
                param.result = hookMap[resName]?.afterValue
            }
        } catch (ignore: Exception) {
        }
    }

    override fun init() {

        Resources::class.java.hookBeforeMethod("getBoolean", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimension", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelOffset", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelSize", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getInteger", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getText", Int::class.javaPrimitiveType) { hook(it) }

        val value = mPrefsMap.getInt("task_view_corners", -1).toFloat()
        val value1 = mPrefsMap.getInt("task_view_header_height", -1).toFloat()
        if (value != -1f && value != 20f) {
            hookMap["recents_task_view_rounded_corners_radius_min"] = ResourcesHookData("dimen", dp2px(value))
            hookMap["recents_task_view_rounded_corners_radius_max"] = ResourcesHookData("dimen", dp2px(value))
        }
        if (value1 != -1f && value != 40f) hookMap["recents_task_view_header_height"] = ResourcesHookData("dimen", dp2px(value1))
    }

}