package com.sevtinge.cemiuiler.module.securitycenter;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import java.util.List;
import java.util.Map;

import io.luckypray.dexkit.DexKitBridge;
import io.luckypray.dexkit.builder.BatchFindArgs;
import io.luckypray.dexkit.descriptor.member.DexClassDescriptor;
import io.luckypray.dexkit.descriptor.member.DexMethodDescriptor;
import io.luckypray.dexkit.enums.MatchType;

public class SecurityCenterDexKit extends BaseHook {

    public static Map<String, List<DexMethodDescriptor>> mSecurityCenterResultMap;
    public static Map<String, List<DexClassDescriptor>> mSecurityCenterResultClassMap;

    @Override
    public void init() {
        System.loadLibrary("dexkit");
        String apkPath = lpparam.appInfo.sourceDir;
        DexKitBridge bridge = DexKitBridge.create(apkPath);
        try {
            if (bridge == null) {
                return;
            }
            mSecurityCenterResultMap =
                bridge.batchFindMethodsUsingStrings(
                    BatchFindArgs.builder()
                        .addQuery("BeautyFace", List.of("taoyao", "IN", "persist.vendor.vcb.ability"))
                        .addQuery("BeautyPc", List.of("persist.vendor.camera.facetracker.support"))
                        .addQuery("BeautyLightAuto", List.of("taoyao"))
                        .addQuery("ScoreManager", List.of("getMinusPredictScore------------------------------------------------ "))
                        .addQuery("rootCheck", List.of("key_check_item_root"))
                        .addQuery("SuperWirelessCharge", List.of("persist.vendor.tx.speed.control"))
                        .addQuery("SuperWirelessChargeTip", List.of("key_is_connected_super_wls_tx"))
                        .addQuery("Macro2", List.of("pref_gb_unsupport_macro_apps"))
                        .addQuery("IsShowReport", List.of("android.intent.action.VIEW", "com.xiaomi.market"))
                        .addQuery("FuckRiskPkg", List.of("riskPkgList", "key_virus_pkg_list", "show_virus_notification"))
                        .addQuery("RemoveScreenHoldOn", List.of("remove_screen_off_hold_on"))
                        .addQuery("AisSupport", List.of("debug.config.media.video.ais.support"))
                        .addQuery("PowerRankHelperHolderSdkHelper", List.of("ishtar", "nuwa", "fuxi"))
                        .matchType(MatchType.CONTAINS)
                        .build()
                );
            mSecurityCenterResultClassMap =
                bridge.batchFindClassesUsingStrings(
                    BatchFindArgs.builder()
                        .addQuery("Macro", List.of("pref_gb_unsupport_macro_apps", "gb_game_gunsight", "com.tencent.tmgp.sgame"))
                        .addQuery("Macro1", List.of("key_macro_toast", "content://com.xiaomi.macro.MacroStatusProvider/game_macro_change"))
                        .addQuery("LabUtils", List.of("mi_lab_ai_clipboard_enable", "mi_lab_blur_location_enable"))
                        .addQuery("BeautyLight", List.of("pref_support_front_light", "pref_privacy_support_devices"))
                        .addQuery("PowerRankHelperHolder", List.of("PowerRankHelperHolder", "not support screenPowerSplit"))
                        .addQuery("GbGameCollimator",List.of("gb_game_collimator_status"))
                        .addQuery("FrcSupport", List.of("ro.vendor.media.video.frc.support"))
                        .matchType(MatchType.CONTAINS)
                        .build()
                );
        } catch (Throwable e) {
            e.printStackTrace();
        }
        bridge.close();
    }
}
