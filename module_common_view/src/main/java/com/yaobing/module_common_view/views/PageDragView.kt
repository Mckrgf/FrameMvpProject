package com.yaobing.module_common_view.views

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.yaobing.module_common_view.R

/**
 * 拖扯控件可以左右吸附(吸附功能处理逻辑在ItemViewTouchListener中)
 */
class PageDragView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
//    private val mIcon: ImageView? = null
//    private val mBG: ImageView? = null
//    private val mWindowManager: WindowManager? = null
//    private val rotateAnimation: ObjectAnimator? = null

    //    private DeleteAreaView mDeleteAreaView;
    private val pauseTime: Long = 0
    private var activity: Activity? = null
    var y = 0

    constructor(context: Context?) : this(context, null) {
        Log.d("zxcv", "fun 1")
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        Log.d("zxcv", "fun 2")
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0) {
        Log.d("zxcv", "fun 3")
    }

    init {
        Log.d("zxcv", "fun 4")
        LayoutInflater.from(context).inflate(R.layout.page_drag_view, this)
    }

    fun initDrag(activity: Activity) {
        this.activity = activity
        initViews()
    }

    private fun initViews() {
        val mParams = WindowManager.LayoutParams()
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.x = getScreenWidth(activity!!) / 2
        mParams.y = y
        mParams.format = PixelFormat.RGBA_8888
        setOnTouchListener(ItemViewTouchListener(mParams, activity!!.windowManager, this, View(activity)))
        activity!!.windowManager.addView(this, mParams)
    }
}