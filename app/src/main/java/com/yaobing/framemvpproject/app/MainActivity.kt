package com.yaobing.framemvpproject.app

import android.os.Bundle
import android.util.Log
import com.yaobing.framemvpproject.app.controller.TestController
import com.yaobing.framemvpproject.contract.GithubRepoContract
import com.yaobing.module_apt.Controller
import com.yaobing.module_apt.Presenter
import com.yaobing.module_apt.Router
import com.yaobing.module_common_view.base.presenter.BasePresenter
import com.yaobing.module_middleware.TestRouter
import com.yaobing.module_middleware.Utils.InstanceUtil
import com.yaobing.module_middleware.Utils.ToastUtils
import com.yaobing.module_middleware.activity.BaseControllerActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Router("MainActivity")
@Presenter(value = [GithubRepoPresenter::class])
@Controller(TestController::class)
class MainActivity : BaseControllerActivity() , GithubRepoContract.View{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bt_all.setOnClickListener {
            presenterRouter.create(GithubRepoAPI:: class.java).getAllRepoByName("MCKRGF")
            val a = TestRouter()
            val c = InstanceUtil.getInstance((AAA::class as Any).javaClass)
            a.register(c)
            a.create(TestAPI:: class.java).testFun("MCKRGF")
        }
        bt_over_module.setOnClickListener {



            IntentRouter.go(this, "asdf")
        }
        initWebView()
    }
    override fun getLayoutID(): Int {
        return R.layout.activity_main
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