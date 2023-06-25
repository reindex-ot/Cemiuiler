package com.sevtinge.cemiuiler.module.systemframework

import android.content.Context
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.api.field

object RemoveSmallWindowRestrictions : BaseHook() {
    private val mSettingsClass = loadClass("com.android.server.wm.WindowManagerService\$SettingsObserver")
    private val mWindowsUtilsClass = loadClass("android.util.MiuiMultiWindowUtils")
    private val mWindowsClass = loadClass("android.util.MiuiMultiWindowAdapter")

    override fun init() {
        try {
            loadClass("com.android.server.wm.ActivityTaskManagerService").methodFinder().first {
                name == "retrieveSettings"
            }.createHook {
                after { param ->
                    param.thisObject.javaClass.field("mDevEnableNonResizableMultiWindow")
                        .setBoolean(param.thisObject, true)
                }
            }
        } catch (e: Throwable) {
            log("Hook retrieveSettings failed by: $e")
        }

        try {
            mSettingsClass.methodFinder().filter {
                name == "updateDevEnableNonResizableMultiWindow"
            }.toList().createHooks {
                after { param ->
                    val this0 = param.thisObject.javaClass.field("this\$0").get(param.thisObject)
                    val mAtmService = this0.javaClass.field("mAtmService").get(this0)
                    mAtmService.javaClass.field("mDevEnableNonResizableMultiWindow").setBoolean(mAtmService, true)
                }
            }
        } catch (e: Throwable) {
            log("Hook updateDevEnableNonResizableMultiWindow failed by: $e")
        }

        try {
            mSettingsClass.methodFinder().filter {
                name == "onChange"
            }.toList().createHooks {
                after { param ->
                    val this0 = param.thisObject.javaClass.field("this\$0").get(param.thisObject)
                    val mAtmService = this0.javaClass.field("mAtmService").get(this0)
                    mAtmService.javaClass.field("mDevEnableNonResizableMultiWindow").setBoolean(mAtmService, true)
                }
            }
        } catch (e: Throwable) {
            log("Hook onChange failed by: $e")
        }

        try {
            mWindowsUtilsClass.methodFinder().first {
                name == "isForceResizeable"
            }.createHook {
                returnConstant(true)
            }
        } catch (e: Throwable) {
            log("Hook isForceResizeable failed by: $e")
        }

        // Author: LittleTurtle2333
        try {
            loadClass("com.android.server.wm.Task").methodFinder().first {
                name == "isResizeable"
            }.createHook {
                returnConstant(true)
            }
        } catch (e: Throwable) {
            log("Hook isResizeable failed by: $e")
        }

        try {
            mWindowsClass.methodFinder().first {
                name == "getFreeformBlackList"
            }.createHook {
                returnConstant(mutableListOf<String>())
            }
        } catch (e: Throwable) {
            log("Hook getFreeformBlackList failed by: $e")
        }

        try {
            mWindowsClass.methodFinder().first {
                name == "getFreeformBlackListFromCloud" && parameterTypes[0] == Context::class.java
            }.createHook {
                returnConstant(mutableListOf<String>())
            }
        } catch (e: Throwable) {
            log("Hook getFreeformBlackListFromCloud failed by: $e")
        }

        try {
            mWindowsClass.methodFinder().first {
                name == "getStartFromFreeformBlackListFromCloud"
            }.createHook {
                returnConstant(mutableListOf<String>())
            }
        } catch (e: Throwable) {
            log("Hook getStartFromFreeformBlackListFromCloud failed by: $e")
        }

        try {
            mWindowsUtilsClass.methodFinder().first {
                name == "supportFreeform"
            }.createHook {
                returnConstant(true)
            }
        } catch (e: Throwable) {
            log("Hook supportFreeform failed by: $e")
        }

    }

}
