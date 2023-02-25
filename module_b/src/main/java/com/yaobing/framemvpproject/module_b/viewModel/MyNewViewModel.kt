package com.god.yb.testgitdemo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author : yaobing
 * @date   : 2022/12/19 20:06
 * @desc   :
 */
class MyNewViewModel : ViewModel() {
    var number = MutableLiveData<Int>(0)
    fun add()
    {
        number.value = number.value?.plus(1)
    }
}