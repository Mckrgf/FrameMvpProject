package com.yaobing.framemvpproject.app

import android.util.Log

/**
 * @author : yaobing
 * @date : 2022/08/03 15:05
 * @desc :
 */
open class BBB : OriginData(),TestAPI {
    override fun testFun(name: String?) {
        super.testFun(name)
        Log.d("zxcv", "dfdfBBB")
    }
}