package com.yaobing.module_middleware

/**
 * @author : yaobing
 * @date   : 2022/08/01 14:37
 * @desc   :
 */
interface IData {
    val NEW_DATA: String
        get() = "NEW_DATA"

    fun refresh()
    fun checkBeforeSubmit(map: Map<String?, Any?>?): Boolean
    fun doSave(map: Map<String?, Any?>?): Boolean
    fun isModified(): Boolean
}