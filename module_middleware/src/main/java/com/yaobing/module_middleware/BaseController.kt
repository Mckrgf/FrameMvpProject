package com.yaobing.module_middleware

import android.content.Intent

/**
 * @author : yaobing
 * @date   : 2022/08/01 14:46
 * @desc   : 要求所有子类controller都要继承baseController的生命周期，便于管控，防止内存泄漏之类的
 */
class BaseController : Lifecycle,IData {


    //为controller绑定的context所在。比如activity/fragment
    private var target: Any? = null

    fun setTarget(target: Any?) {
        this.target = target
    }

    fun getTarget(): Any? {
        return target
    }

    fun <T> getActivity(target: T): T {
        return target!!::class.java.cast(target)!!
    }
    //------------------------------------------------下面的都是重写的生命周期的方法

    override fun refresh() {
        
    }

    override fun checkBeforeSubmit(map: Map<String?, Any?>?): Boolean {
        return doSave(map)
    }

    override fun doSave(map: Map<String?, Any?>?): Boolean {
        return true
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun onInit() {
        
    }

    override fun initView() {
        
    }

    override fun initListener() {
        
    }

    override fun initData() {
        
    }

    override fun onStart() {
        
    }

    override fun onStop() {
        
    }

    override fun onResume() {
        
    }

    override fun onPause() {
        
    }

    override fun onDestroy() {
        
    }

    override fun onRetry() {
        
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        
    }
}