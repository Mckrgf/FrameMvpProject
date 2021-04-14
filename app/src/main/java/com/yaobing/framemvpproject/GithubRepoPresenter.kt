package com.yaobing.framemvpproject

import android.util.Log
import com.yaobing.contract.GithubRepoContract
import com.yaobing.module_middleware.network.Api
import com.yaobing.module_middleware.network.RxSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * @author : yaobing
 * @date   : 2020/10/30 15:50
 * @desc   :
 */
class GithubRepoPresenter : GithubRepoContract.Presenter() {
    var aa: ArrayList<Repo1> = ArrayList<Repo1>()
    override fun getAllRepoByName(name: String?) {
        val aaa = Api.getInstance().retrofit.create(APIService::class.java)
        val flowable = aaa.listReposRx(name).compose(RxSchedulers.io_main())


        mCompositeSubscription.add(flowable.onErrorReturn {
            Log.d("zxcvb0", Thread.currentThread().name)
            return@onErrorReturn aa
        }.subscribe {
            Log.d("zxcvb1", Thread.currentThread().name)

            if (it.size == 0) {
                view.getAllRepoByNameFailed("失败")
            } else {
                view.getAllRepoByNameSuccess(it)
            }
        })
    }
}