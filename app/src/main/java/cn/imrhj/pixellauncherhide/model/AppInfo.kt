package cn.imrhj.pixellauncherhide.model

import android.graphics.drawable.Drawable

/**
 * Created by rhj on 23/02/2018.
 */
data class AppInfo(val appName: CharSequence, val packageName: CharSequence, val appIcon: Drawable, var hide: Boolean)