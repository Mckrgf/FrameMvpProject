package com.yaobing.framemvpproject

import android.os.Bundle
import android.util.Log
import com.yaobing.contract.GithubRepoContract
import com.yaobing.module_apt.Presenter
import com.yaobing.module_middleware.activity.BasePresenterActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@Presenter(value = [GithubRepoPresenter::class])
class MainActivity : BasePresenterActivity() , GithubRepoContract.View{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bt_all.setOnClickListener {
            presenterRouter.create(GithubRepoAPI :: class.java).getAllRepoByName("MCKRGF")
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
    }

    override fun getAllRepoByNameSuccess(entity: ArrayList<*>?) {
        Log.d("zxcv","b")
    }
}