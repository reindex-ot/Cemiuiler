package com.sevtinge.cemiuiler.module.home

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.findClass
import com.sevtinge.cemiuiler.utils.hookBeforeMethod
import com.sevtinge.cemiuiler.utils.replaceMethod

object SetDeviceLevel : BaseHook() {
    override fun init() {
        val mDeviceLevelUtilsClass = loadClass("com.miui.home.launcher.common.DeviceLevelUtils")
        val mSystemPropertiesClass = loadClass("android.os.SystemProperties")
        val mDeviceConfigClass = loadClass("com.miui.home.launcher.DeviceConfig")

        try {
            loadClass("com.miui.home.launcher.common.CpuLevelUtils").methodFinder().first {
                name == "getQualcommCpuLevel" && parameterCount == 1
            }
        } catch (e: Exception) {
            loadClass("miuix.animation.utils.DeviceUtils").methodFinder().first {
                name == "getQualcommCpuLevel" && parameterCount == 1
            }
        }.createHook {
            returnConstant(2)
        }
        try {
            mDeviceConfigClass.methodFinder().first {
                name == "isUseSimpleAnim"
            }.createHook {
                before {
                    it.result = false
                }
            }
        } catch (_: Throwable) {

        }
        try {
            mDeviceLevelUtilsClass.methodFinder().first {
                name == "getDeviceLevel"
            }.createHook {
                before {
                    it.result = 2
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            mDeviceConfigClass.methodFinder().first {
                name == "isSupportCompleteAnimation"
            }.createHook {
                before {
                    it.result = true
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            mDeviceLevelUtilsClass.methodFinder().first {
                name == "isLowLevelOrLiteDevice"
            }.createHook {
                before {
                    it.result = false
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            mDeviceConfigClass.methodFinder().first {
                name == "isMiuiLiteVersion"
            }.createHook {
                before {
                    it.result = false
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            "com.miui.home.launcher.util.noword.NoWordSettingHelperKt".hookBeforeMethod(
                "isNoWordAvailable"
            ) {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }

        try {
            mSystemPropertiesClass.methodFinder().filter {
                name == "getBoolean" && parameterTypes[0] == String::class.java && parameterTypes[1] == Boolean::class.java
            }.toList().createHooks {
                before {
                    if (it.args[0] == "ro.config.low_ram.threshold_gb") it.result = false
                    if (it.args[0] == "ro.miui.backdrop_sampling_enabled") it.result = true
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            "com.miui.home.launcher.common.Utilities".hookBeforeMethod(
                "canLockTaskView"
            ) {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod(
                "isMIUIWidgetSupport"
            ) {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            "com.miui.home.launcher.MiuiHomeLog".findClass().replaceMethod(
                "log", String::class.java, String::class.java
            ) {
                return@replaceMethod null
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
        try {
            "com.xiaomi.onetrack.OneTrack".hookBeforeMethod("isDisable") {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }
}
