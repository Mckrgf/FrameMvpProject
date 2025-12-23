package com.yaobing.module_common_view.views

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun getScreenHeight(context: Context): Int {
    val metric = DisplayMetrics()
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return 0
    wm.defaultDisplay.getMetrics(metric)
    return metric.heightPixels
}

fun getScreenWidth(context: Context): Int {
    val metric = DisplayMetrics()
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return 0
    wm.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}
fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}
/**
 * 获取屏幕的高宽比   height/width
 * @param context
 * @return
 */
fun getScreenAspectRatio(context: Context): Float {
    return getScreenHeight(context).toFloat() / getScreenWidth(context).toFloat()
}
