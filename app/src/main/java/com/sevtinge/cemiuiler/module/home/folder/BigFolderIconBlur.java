package com.sevtinge.cemiuiler.module.home.folder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sevtinge.cemiuiler.module.base.BaseHook;
import com.sevtinge.cemiuiler.utils.BlurUtils;
import com.sevtinge.cemiuiler.utils.DisplayUtils;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedHelpers;

public class BigFolderIconBlur extends BaseHook {

    boolean isShowEditPanel;
    Class<?> mLauncher;
    Class<?> mFolderInfo;
    Class<?> mFolderIcon;
    Class<?> mLauncherState;
    Class<?> mDragView;

    Class<?> mFolderIcon2x2;
    Class<?> mFolderIcon2x2_4;
    Class<?> mFolderIcon2x2_9;

    @Override
    public void init() {

        mLauncher = findClassIfExists("com.miui.home.launcher.Launcher");
        mFolderInfo = findClassIfExists("com.miui.home.launcher.FolderInfo");
        mFolderIcon = findClassIfExists("com.miui.home.launcher.FolderIcon");
        mLauncherState = findClassIfExists("com.miui.home.launcher.LauncherState");
        mDragView = findClassIfExists("com.miui.home.launcher.DragView");
        mFolderIcon2x2 = findClassIfExists("com.miui.home.launcher.folder.FolderIcon2x2");
        mFolderIcon2x2_4 = findClassIfExists("com.miui.home.launcher.folder.FolderIcon2x2_4");
        mFolderIcon2x2_9 = findClassIfExists("com.miui.home.launcher.folder.FolderIcon2x2_9");

        hookAllConstructors(mFolderIcon, new MethodHook() {
            @Override
            protected void after(MethodHookParam param) throws Throwable {
                Context mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                Object mDockBlur = XposedHelpers.getAdditionalInstanceField(param.thisObject, "mDockBlur");
                if (mDockBlur != null) return;
                mDockBlur = new FrameLayout(mContext);
                XposedHelpers.setAdditionalInstanceField(param.thisObject, "mDockBlur", mDockBlur);
            }
        });

        MethodHook mBigFolderIconBlur = new MethodHook() {
            @Override
            protected void after(MethodHookParam param) throws Throwable {
                Context mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                int mFolderWidth = DisplayUtils.dip2px(mContext, mPrefsMap.getInt("home_big_folder_icon_bg_width", 145));
                int mFolderHeight = DisplayUtils.dip2px(mContext, mPrefsMap.getInt("home_big_folder_icon_bg_height", 145));
                ImageView mIconImageView = (ImageView) XposedHelpers.getObjectField(param.thisObject, "mIconImageView");
                FrameLayout mIconContainer = (FrameLayout) mIconImageView.getParent();
                FrameLayout mDockBlur = (FrameLayout) XposedHelpers.getAdditionalInstanceField(param.thisObject, "mDockBlur");
                FrameLayout view = new FrameLayout(mIconImageView.getContext());

                mIconImageView.setVisibility(View.GONE);
                mDockBlur.addView(view);

                new BlurUtils(mDockBlur, "home_big_folder_icon_bg_custom");

                mIconContainer.addView(mDockBlur, 0);
                FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) mDockBlur.getLayoutParams();
                lp1.gravity = Gravity.CENTER;
                lp1.width = mFolderWidth;
                lp1.height = mFolderHeight;
                findAndHookMethod(mLauncher, "showEditPanel", boolean.class, new MethodHook() {
                    @Override
                    protected void after(MethodHookParam param) throws Throwable {
                        isShowEditPanel = (boolean) param.args[0];
                        if (isShowEditPanel) {
                            mDockBlur.setVisibility(View.GONE);
                            mIconImageView.setVisibility(View.VISIBLE);
                        } else {
                            mDockBlur.setVisibility(View.VISIBLE);
                            mIconImageView.setVisibility(View.GONE);
                        }
                    }
                });

                findAndHookMethod(mLauncher, "openFolder", mFolderInfo, View.class, new MethodHook() {
                    @Override
                    protected void after(MethodHookParam param) throws Throwable {
                        mDockBlur.setVisibility(View.GONE);
                    }
                });

                findAndHookMethod(mLauncher, "closeFolder", boolean.class, new MethodHook() {
                    @Override
                    protected void after(MethodHookParam param) throws Throwable {
                        if (!isShowEditPanel) mDockBlur.setVisibility(View.VISIBLE);
                    }
                });

                findAndHookMethod(mLauncher, "onStateSetStart", mLauncherState, new MethodHook() {
                    @Override
                    protected void after(MethodHookParam param) throws Throwable {
                        if (param.args[0].getClass().getSimpleName().equals("LauncherState")) {
                            mDockBlur.setVisibility(View.VISIBLE);
                        } else {
                            mDockBlur.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };

        Method FolderIcon2x2_4_OnFinishInflate = XposedHelpers.findMethodExactIfExists(mFolderIcon2x2_4, "onFinishInflate", Void.TYPE);
        Method FolderIcon2x2_9_OnFinishInflate = XposedHelpers.findMethodExactIfExists(mFolderIcon2x2_9, "onFinishInflate", Void.TYPE);

        if (FolderIcon2x2_4_OnFinishInflate != null && FolderIcon2x2_9_OnFinishInflate != null) {
            findAndHookMethod(mFolderIcon2x2_4, "onFinishInflate", mBigFolderIconBlur);
            findAndHookMethod(mFolderIcon2x2_9, "onFinishInflate", mBigFolderIconBlur);
        } else if (mFolderIcon2x2 != null) {
            findAndHookMethod(mFolderIcon2x2, "onFinishInflate", mBigFolderIconBlur);
        }

        hookAllConstructors(mDragView, new MethodHook() {
            @Override
            protected void after(MethodHookParam param) throws Throwable {
                View mDragView = (View) param.thisObject;
                mDragView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

                Object mDragInfo = XposedHelpers.getObjectField(param.thisObject, "mDragInfo");
                int itemType = (int) XposedHelpers.getObjectField(mDragInfo, "itemType");
                Object mLauncher = XposedHelpers.getObjectField(param.thisObject, "mLauncher");
                boolean isFolderShowing = (boolean) XposedHelpers.callMethod(mLauncher, "isFolderShowing");

                if (!isFolderShowing && itemType == 21) {

                    new BlurUtils(mDragView, "home_big_folder_icon_bg_custom");
                }
            }
        });
    }
}
