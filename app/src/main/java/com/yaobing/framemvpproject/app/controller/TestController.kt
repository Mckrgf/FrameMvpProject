package com.yaobing.framemvpproject.app.controller

import android.util.Log
import com.yaobing.module_middleware.BaseController

/**
 * @author : yaobing
 * @date   : 2022/08/01 20:00
 * @desc   :
 */
class TestController : BaseController() {
    override fun onInit() {
        super.onInit()
        Log.d("TestController","onInit ")
    }

    override fun initView() {
        super.initView()
        Log.d("TestController","initView ")
    }

    override fun initListener() {
        super.initListener()
        Log.d("TestController","initListener ")
    }

    override fun initData() {
        super.initData()
        Log.d("TestController","initData ")
    }

    override fun onStart() {
        super.onStart()
        Log.d("TestController","onStart ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TestController","onStop ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TestController","onResume ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TestController","onPause ")
    }
}