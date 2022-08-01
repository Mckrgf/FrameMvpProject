package com.yaobing.module_middleware

import android.content.Intent

/**
 * @author : yaobing
 * @date   : 2022/08/01 10:42
 * @desc   : 用于绑定控制controller的生命周期和其他需要绑定的
 */
class LifeCycleManage<T : Lifecycle> :Lifecycle {

    var lifecycleMap:MutableMap<String,T> = HashMap()


    constructor()

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

    fun unregister(key:String) {
        lifecycleMap?.remove(key)
    }

    override fun onInit() {
        for ((a, value) in lifecycleMap.entries) {
            value.onInit()
        }
    }

    override fun initView() {
        for ((a, value) in lifecycleMap.entries) {
            value.initView()
        }
    }

    override fun initListener() {
        for ((a, value) in lifecycleMap.entries) {
            value.initListener()
        }
    }

    override fun initData() {
        for ((a, value) in lifecycleMap.entries) {
            value.initData()
        }
    }

    override fun onStart() {
        for ((a, value) in lifecycleMap.entries) {
            value.onStart()
        }
    }

    override fun onStop() {
        for ((a, value) in lifecycleMap.entries) {
            value.onStop()
        }
    }

    override fun onResume() {
        for ((a, value) in lifecycleMap.entries) {
            value.onResume()
        }
    }

    override fun onPause() {
        for ((a, value) in lifecycleMap.entries) {
            value.onPause()
        }
    }

    override fun onDestroy() {
        for ((a, value) in lifecycleMap.entries) {
            value.onDestroy()
        }
    }

    override fun onRetry() {
        for ((a, value) in lifecycleMap.entries) {
            value.onRetry()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for ((a, value) in lifecycleMap.entries) {
            value.onActivityResult(requestCode, resultCode, data)
        }
    }

    operator fun get(key: String?): T? {
        return lifecycleMap[key]
    }
}