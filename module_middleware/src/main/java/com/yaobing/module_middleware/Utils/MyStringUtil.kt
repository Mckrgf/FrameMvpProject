package com.yaobing.module_middleware.Utils

import android.widget.EditText
import android.widget.TextView
import com.yaobing.module_middleware.BaseApp

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

var EditText.isBold: Boolean
    get() {
        return this.paint.isFakeBoldText
    }
    set(value) {
        this.paint.isFakeBoldText = value
    }

// TODO: 只要这么写  也递归
var EditText.isString: String
    get() {
        return ""
    }
    set(value) {
        this.isString = value
    }

class Person(var age: Int,var work :String) {

}

//对象的扩展方法
fun Person.say(memo: String) {
    ToastUtils.show(BaseApp.getInstance(), "我说了：$memo")
}


var Person.setSex: String
    get() {
        return this.work
    }
    set(value) {
//        this.setSex = value// TODO: 对象的扩展属性也会有递归错误，要研究一下
        this.work = setSex
    }

// TODO: 基本数据类型的扩展属性会有递归错误，要研究一下
var String.aaa: Int
    get() {
        return this.aaa
    }
    set(person) {
        aaa = person
    }
