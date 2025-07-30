package com.yaobing.framemvpproject.app

import android.content.Context
import android.widget.Toast

/**
 * 测试类，用于测试子工程能否调用主工程本类，及本类的方法。
 */
class FunctionForModule {
    fun toast(context : Context, content : String) {
        Toast.makeText(context, content,Toast.LENGTH_LONG).show()
    }
}