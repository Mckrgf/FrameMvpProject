package com.yaobing.framemvpproject

import com.yaobing.contract.GithubRepoContract
import com.yaobing.module_middleware.network.ApiService
import java.util.ArrayList

/**
 * @author : yaobing
 * @date   : 2020/10/30 15:50
 * @desc   :
 */
class GithubRepoPresenter: GithubRepoContract.Presenter() {
    var aa: ArrayList<Repo1> = ArrayList<Repo1>()
    override fun getAllRepoByName(name: String?) {
        mCompositeSubscription.add(APIServiceHttpClient.listReposRx(name).onErrorReturn { return@onErrorReturn aa }.subscribe {
            if (it.size == 0) {
                view.getAllRepoByNameFailed("失败")
            }else {
                view.getAllRepoByNameSuccess(it)
            }
        })
    }
}