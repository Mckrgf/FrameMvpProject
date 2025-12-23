package com.yaobing.module_common_view.views

import android.animation.ValueAnimator
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.AnticipateOvershootInterpolator

import kotlin.math.abs

/**
 * @功能:处理悬浮窗拖动更新位置
 */
class ItemViewTouchListener(
    private val wl: WindowManager.LayoutParams,
    var windowManager: WindowManager,
    val pageDragView: PageDragView,
    val mDeleteAreaView: View
) :
    View.OnTouchListener {
    private var x = 0
    private var y = 0
    private var downRawX = 0
    private var downRawY = 0

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x = motionEvent.rawX.toInt()
                y = motionEvent.rawY.toInt()
                downRawX = motionEvent.rawX.toInt()
                downRawY = motionEvent.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                mDeleteAreaView.visibility = View.VISIBLE
                (view as? PageDragView)?.let {
//                    it.showNormal(false)
                }
                val nowX = motionEvent.rawX.toInt()
                val nowY = motionEvent.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY
                wl.apply {
                    x += movedX
                    y += movedY
//                    pageDragView.y = y
                }
                //更新悬浮球控件位置
//                mDeleteAreaView.setTip(
//                    mDeleteAreaView.context.resources.getString(
//                        if (wl.y > (ScreenUtils.getScreenHeight(
//                                view.context
//                            ) / 2) - ScreenUtils.dip2px(view.context, 85f)
//                        ) R.string.tip2 else R.string.tip1
//                    )
//                )
                windowManager.updateViewLayout(view, wl)
            }

            MotionEvent.ACTION_UP -> {
                val upX: Int = motionEvent.rawX.toInt()
                val upY: Int = motionEvent.rawY.toInt()
                if (abs(upX - downRawX) < ViewConfiguration.get(view.context).scaledTouchSlop &&
                    abs(upY - downRawY) < ViewConfiguration.get(view.context).scaledTouchSlop
                ) {
//                    ServiceManager.getService(MainAppService::class.java)
//                        ?.jumpToActivity("NativePage:PushToRadioFromSuspendButton")
//                    Log.v("ItemViewTouchListener", "点击事件")
//                    YTTrackingManager.get().trackingShortcut(
//                        "打开电台",
//                        if (YTConstant.playType == 0) YTTrackingConstant.TYPE_RADIO else YTTrackingConstant.TYPE_COLUMN,
//                        getInstance().getPlayingTitle(),
//                        YTConstant.currentTabName
//                    )
                } else {
                    animSlide(view, wl.x, -getScreenWidth(view.context) / 2, 250)
                    Log.v("ItemViewTouchListener", "拖动事件")
                }
                if (wl.y > (getScreenHeight(view.context) / 2) - dip2px(
                        view.context,
                        85f
                    )
                ) {
//                    windowManager.removeView(pageDragView)
//                    windowManager.removeView(mDeleteAreaView)
//                    pageDragView.y = 0
//                    LocalBroadcastManager.getInstance(view.context)
//                        .sendBroadcast(Intent(BroadcastAction.LP_RADIO_RELEASE))
//                    getInstance().pauseRadio()
//                    getInstance().releaseRadio()
//                    YTTrackingManager.get().trackingRelease("浮窗快捷方式拖动结束")
//                    YTTrackingManager.get().trackingShortcut(
//                        "退出电台",
//                        if (YTConstant.playType == 0) YTTrackingConstant.TYPE_RADIO else YTTrackingConstant.TYPE_COLUMN,
//                        getInstance().getPlayingTitle(),
//                        YTConstant.currentTabName
//                    )
                }
                mDeleteAreaView.visibility = View.GONE
            }

            else -> {

            }
        }
        return true
    }

    private fun animSlide(view: View, from: Int, to: Int, duration: Int) {
        val valueAnimator = ValueAnimator.ofInt(from, to)
        valueAnimator.interpolator = AnticipateOvershootInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val viewLeft = animation.animatedValue as Int
            wl.apply {
                x = viewLeft
            }
            if (null != windowManager && view.isAttachedToWindow) {
                windowManager.updateViewLayout(view, wl)
            } else {
                Log.i("TAG", "animSlide: ")
            }
        }
        //为防止溢出边界时，duration时间为负值，做下0判断
        valueAnimator.duration = if (duration < 0) 0 else duration.toLong()
        valueAnimator.start()
    }
}