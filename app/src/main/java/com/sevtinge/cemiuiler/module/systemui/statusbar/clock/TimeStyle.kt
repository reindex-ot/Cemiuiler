package com.sevtinge.cemiuiler.module.systemui.statusbar.clock

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.MemberExtensions.paramCount
import com.github.kyuubiran.ezxhelper.finders.ConstructorFinder.`-Static`.constructorFinder
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.devicesdk.isAndroidR
import com.sevtinge.cemiuiler.utils.devicesdk.isMoreAndroidVersion


object TimeStyle : BaseHook() {
    @SuppressLint("RtlHardcoded", "DiscouragedApi")
    override fun init() {
        val mClockClass = when {
            isAndroidR() -> loadClass("com.android.systemui.statusbar.policy.MiuiClock")
            isMoreAndroidVersion(31) -> loadClass("com.android.systemui.statusbar.views.MiuiClock")
            else -> null
        }
        val clockBold = mPrefsMap.getBoolean("system_ui_statusbar_clock_bold")
        val getMode = mPrefsMap.getStringAsInt("system_ui_statusbar_clock_mode", 0)
        val isAlign = mPrefsMap.getStringAsInt("system_ui_statusbar_clock_double_mode", 0)
        val isGeekAlign = mPrefsMap.getStringAsInt("system_ui_statusbar_clock_double_mode_geek", 0)
        val verticalOffset = mPrefsMap.getInt("system_ui_statusbar_clock_vertical_offset", 12)

        mClockClass!!.constructorFinder().first {
            paramCount == 3
        }.createHook {
            after {
                val textV = it.thisObject as TextView
                val res: Resources = textV.resources

                if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                    // 时钟加粗
                    if (clockBold) {
                        textV.typeface = Typeface.DEFAULT_BOLD
                    }
                    // 时钟对齐方式
                    when (getMode) {
                        // 预设模式下
                        1 -> {
                            textV.textAlignment = when (isAlign) {
                                1 -> View.TEXT_ALIGNMENT_CENTER
                                2 -> View.TEXT_ALIGNMENT_TEXT_END
                                else -> View.TEXT_ALIGNMENT_TEXT_START
                            }
                        }
                        // 极客模式下
                        2 -> {
                            textV.textAlignment = when (isGeekAlign) {
                                1 -> View.TEXT_ALIGNMENT_CENTER
                                2 -> View.TEXT_ALIGNMENT_TEXT_END
                                else -> View.TEXT_ALIGNMENT_TEXT_START
                            }
                        }
                    }
                }

                // 时钟边距调整
                if (verticalOffset != 12) {
                    val marginTop =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            (verticalOffset - 12) * 0.5f,
                            res.displayMetrics
                        )
                    textV.translationY = marginTop
                }
            }
        }
    }
}
