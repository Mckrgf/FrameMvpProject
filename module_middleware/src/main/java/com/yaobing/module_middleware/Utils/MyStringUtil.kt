package com.yaobing.module_middleware.Utils

import android.widget.EditText
import android.widget.TextView

/**
 * @author : yaobing
 * @date   : 2022/11/14 11:02
 * @desc   :
 */
class MyStringUtil {
}

fun String.checkComma(): String {
    return if (this.contains(",")) {
        "有逗号"
    } else {
        "没有逗号"
    }
}

var EditText.isBold :Boolean
    get() {
        return this.paint.isFakeBoldText
    }
    set(value) {
        this.paint.isFakeBoldText = value
    }

class Person {
    var age: Int = 1

    constructor(age: Int) {
        this.age = age
    }
}
// TODO: 会有递归错误，要研究一下
var String.aaa: Int
    get() {
       return this.aaa
    }
    set(person) {
        aaa = person
    }
