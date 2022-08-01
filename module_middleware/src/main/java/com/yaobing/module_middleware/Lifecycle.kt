package com.yaobing.module_middleware

import android.content.Intent

/**
 * @author : yaobing
 * @date   : 2022/08/01 10:41
 * @desc   :
 */
interface Lifecycle {

    /**
     * 初始化
     */
    fun onInit()

    /**
     * 初始化VIEW
     */
    fun initView()

    /**
     * 初始化事件监听
     */
    fun initListener()

    /**
     * 初始化数据
     */
    fun initData()
    fun onStart()
    fun onStop()
    fun onResume()
    fun onPause()

    /**
     * 撤消
     */
    fun onDestroy()

    fun onRetry()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}