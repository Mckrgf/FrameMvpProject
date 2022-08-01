package com.yaobing.module_middleware

import android.content.Intent

/**
 * @author : yaobing
 * @date   : 2022/08/01 10:42
 * @desc   :
 */
class LifecycleManage1<T : Lifecycle> :Lifecycle {
    private var iifecycleMap: MutableMap<String, T>? = null

    fun LifecycleManage() {
        iifecycleMap = HashMap()
    }

    fun getIifecycleMap(): Map<String, T>? {
        return iifecycleMap
    }

    fun register(key: String, lifecycle: T) {
        iifecycleMap!![key] = lifecycle
    }

    fun unregister(key: String) {
        iifecycleMap!!.remove(key)
    }

    override fun onInit() {
        for ((_, value) in iifecycleMap!!) {
            value.onInit()
        }
    }

    override fun initView() {
        for ((_, value) in iifecycleMap!!) {
            value.initView()
        }
    }

    override fun initListener() {
        for ((_, value) in iifecycleMap!!) {
            value.initListener()
        }
    }

    override fun initData() {
        for ((_, value) in iifecycleMap!!) {
            value.initData()
        }
    }

    override fun onStart() {
        for ((_, value) in iifecycleMap!!) {
            value.onStart()
        }
    }

    override fun onStop() {
        for ((_, value) in iifecycleMap!!) {
            value.onStop()
        }
    }

    override fun onResume() {
        for ((_, value) in iifecycleMap!!) {
            value.onResume()
        }
    }

    override fun onPause() {
        for ((_, value) in iifecycleMap!!) {
            value.onPause()
        }
    }

    override fun onDestroy() {
        for ((_, value) in iifecycleMap!!) {
            value.onDestroy()
        }
    }

    override fun onRetry() {
        for ((_, value) in iifecycleMap!!) {
            value.onRetry()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for ((_, value) in iifecycleMap!!) {
            value.onActivityResult(requestCode, resultCode, data)
        }
    }

    operator fun get(key: String): T? {
        return iifecycleMap!![key]
    }
}