package com.yaobing.module_middleware.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.yaobing.module_apt.Controller
import com.yaobing.module_middleware.BaseController
import com.yaobing.module_middleware.LifeCycleManage
import com.yaobing.module_middleware.Lifecycle

/**
 * @author : yaobing
 * @date   : 2022/08/01 15:09
 * @desc   :
 */
abstract class BaseControllerActivity : BasePresenterActivity() {

    private val controllers: LifeCycleManage<BaseController> = LifeCycleManage()

    fun initControllers() {
        for (annotation in javaClass.annotations) {
            if (annotation is Controller) {
                val controllerClasses = annotation.value
                for (controllerClass in controllerClasses) {
                    var baseController: BaseController? = null
                    for (constructor in controllerClass.java.constructors) {
                        val types = constructor.parameterTypes
                        when (types.size) {
                            0 -> {
                                baseController = constructor.newInstance() as BaseController
                            }
                            1 -> {
                                if (types[0] == View::class.java) {
                                    baseController = constructor.newInstance(rootView) as BaseController?
                                } else if (types[0] == Context::class.java) {
                                    baseController = constructor.newInstance(context) as BaseController?
                                }
                            }
                            else -> {
                                baseController = constructor.newInstance() as BaseController
                            }
                        }
                        baseController?.setTarget(context)
                        registerController(controllerClass::class.java.simpleName, baseController!!)
                    }
                }
            }
        }
    }

    private fun registerController(key: String, controller: BaseController) {
        controllers.register(key, controller)
    }

    open fun <A : Lifecycle?> getController(clazz: Class<A>) : A{
        return controllers[clazz.simpleName] as A

    }

    override fun onInit() {
        super.onInit()
        initControllers()
        onRegisterController()
        controllers.onInit()
    }



    /**
     * 开始注册控制器
     */
    protected open fun onRegisterController() {}

    //--------------------------------------------------------绑定controllers的生命周期
    override fun initView() {
        super.initView()
        controllers.initView()
    }

    override fun initListener() {
        super.initListener()
        controllers.initListener()
    }

    override fun initData() {
        super.initData()
        controllers.initData()
    }

    override fun onStart() {
        super.onStart()
        controllers.onStart()
    }

    override fun onPause() {
        super.onPause()
        controllers.onPause()
    }

    override fun onStop() {
        super.onStop()
        controllers.onStop()
    }

    override fun onResume() {
        super.onResume()
        controllers.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        controllers.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        controllers.onActivityResult(requestCode,resultCode,data)
    }
    open fun onRetry() {
        controllers.onRetry()
    }


    //------------------------------------生命周期绑定结束

}