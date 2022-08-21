package com.yaobing.framemvpproject.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import com.yaobing.framemvpproject.app.controller.TestController
import com.yaobing.framemvpproject.contract.GithubRepoContract
import com.yaobing.module_apt.Controller
import com.yaobing.module_apt.Presenter
import com.yaobing.module_apt.Router
import com.yaobing.module_middleware.TestRouter
import com.yaobing.module_middleware.Utils.ToastUtils
import com.yaobing.module_middleware.activity.BaseControllerActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

@Router("MainActivity")
@Presenter(value = [GithubRepoPresenter::class])
@Controller(TestController::class)
class MainActivity : BaseControllerActivity() , GithubRepoContract.View{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bt_all.setOnClickListener {
            //为了测试动态代理方便，先注释掉
//            presenterRouter.create(GithubRepoAPI:: class.java).getAllRepoByName("MCKRGF")

            val testRouterA = TestRouter()
            val obj = AAA()
            testRouterA.register(obj)
            val a = testRouterA.create(TestAPI:: class.java)
            a.testFun("AAA")


        }
        bt_over_module.setOnClickListener {

            val testRouterB = TestRouter()
            val obj1 = BBB()
            testRouterB.register(obj1)
            val b = testRouterB.create(TestAPI:: class.java)
            b.testFun("BBB")

//为了测试动态代理方便，先注释掉
//            IntentRouter.go(this, "asdf")
        }
        initWebView()
    }
    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    fun proxyTest() {
        val demo = AAA() // 创建要被代理的实例
        val proxy = Proxy.newProxyInstance( // 获取实例的代理对象
            AAA::class.java.classLoader, // 获取实例的classloader
            arrayOf(TestAPI::class.java), // 实例接口
            object : InvocationHandler {
                override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any {
                    println("before...") // 代理增强方法
                    val result = method.invoke(demo, *args) // 反射调用实例的原始方法
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            "void" == method.genericReturnType.typeName
                        } else {
                            return false
                        }
                    ) // 如果返回值为void, 要转换为Unit, 否则会报空指针异常
                        return Unit
                    return result
                }
            }) as TestAPI // 类型转换
        proxy.testFun("hello") // 调用代理
    }


    private fun initWebView() {
        webview.loadUrl("http://www.baidu.com")
    }

    override fun getAllRepoByNameFailed(errorMsg: String?) {
        Log.d("zxcv","a")
        ToastUtils.show(context,errorMsg)
    }

    override fun getAllRepoByNameSuccess(entity: ArrayList<*>?) {
        ToastUtils.show(context,entity.toString())
        Log.d("zxcv","b")
    }
}