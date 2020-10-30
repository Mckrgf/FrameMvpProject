package com.yaobing.framemvpproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yaobing.contract.GithubRepoContract
import com.yaobing.module_apt.Presenter
import com.yaobing.module_middleware.activity.BasePresenterActivity
import com.yaobing.module_middleware.network.Api
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
@Presenter(value = [GithubRepoPresenter::class])
class MainActivity : BasePresenterActivity() , GithubRepoContract.View{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_all.setOnClickListener {
            presenterRouter.create(GithubRepoAPI :: class.java).getAllRepoByName("MCKRGF")
        }
    }

    override fun getAllRepoByNameFailed(errorMsg: String?) {
        Log.d("zxcv","a")
    }

    override fun getAllRepoByNameSuccess(entity: ArrayList<*>?) {
        Log.d("zxcv","b")
    }
}