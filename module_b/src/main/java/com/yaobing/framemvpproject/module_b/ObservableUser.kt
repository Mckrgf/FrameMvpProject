package com.god.yb.testgitdemo.item

import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

/**
 * @author : yaobing
 * @date   : 2022/12/05 19:47
 * @desc   :
 */
class ObservableUser {
    val name = ObservableField<String>()
    val firstName = ObservableField<String>()
//    var age = ObservableField<Int>()
    var age = ObservableInt()
    var sex = ObservableInt()
    var alive = ObservableBoolean()
    val workInformation = ObservableArrayMap<String,Any>()
}