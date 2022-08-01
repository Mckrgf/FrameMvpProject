package com.yaobing.module_middleware

import android.content.Intent

/**
 * @author : yaobing
 * @date   : 2022/08/01 10:42
 * @desc   : 用于绑定控制controller的生命周期和其他需要绑定的
 */
class LifeCycleManage<T : Lifecycle> :Lifecycle {

    lateinit var lifecycleMap:MutableMap<String,T>


    constructor() {
        lifecycleMap = HashMap()
    }

    fun  getLifeCycleMap() :Map<String,T>? {
        return if (null == lifecycleMap) {
            null;
        }else {
            lifecycleMap
        }
    }

    fun register(key : String, lifecycle: T) {
        lifecycleMap!![key] = lifecycle
    }

    fun unRegister(key:String) {
        lifecycleMap?.remove(key)
    }

    override fun onInit() {
        TODO("Not yet implemented")
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initListener() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onRetry() {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("Not yet implemented")
    }
}