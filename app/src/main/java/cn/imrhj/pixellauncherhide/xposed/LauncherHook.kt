package cn.imrhj.pixellauncherhide.xposed

import android.content.ComponentName
import android.content.pm.LauncherActivityInfo
import cn.imrhj.pixellauncherhide.BuildConfig
import cn.imrhj.pixellauncherhide.utils.Common
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by rhj on 23/02/2018.
 */
class LauncherHook : IXposedHookLoadPackage {
    private val mPreferences by lazy {
        XSharedPreferences(BuildConfig.APPLICATION_ID, Common.SHARE_PREF_NAME)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (!(lpparam?.packageName?.equals(Common.LAUNCHER_PACKAGE_NAME)!!)) {
            return
        }
        mPreferences.reload()
        XposedBridge.log("rhjlog handleLoadPackage: all ${mPreferences.all}")
        XposedHelpers.findAndHookMethod(
                Common.CLASS_NAME_ALL_APP_LIST,
                lpparam.classLoader,
                "add",
                Common.CLASS_NAME_APPINFO,
                LauncherActivityInfo::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        val appInfo = param?.args?.get(0)
                        val componentName = XposedHelpers.getObjectField(appInfo, "componentName")
                        if (componentName != null && componentName is ComponentName) {
                            if (mPreferences.getBoolean(componentName.packageName, false)) {
                                XposedBridge.log("rhjlog: 隐藏应用 --> ${componentName.packageName}")
                                param?.result = null
                            }
                        }
                    }
                })
    }
}