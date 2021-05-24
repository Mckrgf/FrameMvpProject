package com.yaobing.framemvpproject

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import com.yaobing.contract.GithubRepoContract
import com.yaobing.module_apt.Presenter
import com.yaobing.module_middleware.Utils.ToastUtil
import com.yaobing.module_middleware.activity.BasePresenterActivity
import com.yaobing.module_middleware.interfaces.PermissionListener
import com.yaobing.module_middleware.service.LocationSubmitService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@Presenter(value = [GithubRepoPresenter::class])
class MainActivity : BasePresenterActivity(), GithubRepoContract.View {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestRunTimePermission(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO
        ), object : PermissionListener {
            override fun onGranted() {  //所有权限授权成功
                ToastUtil.showToast(this@MainActivity, "所有权限授予成功")
            }

            override fun onGranted(grantedPermission: List<String>) { //授权成功权限集合
                val nnn = grantedPermission.size
                Log.i("failed", "" + nnn)
                ToastUtil.showToast(
                    this@MainActivity,
                    "部分权限授予成功" + grantedPermission.size
                )
            }

            override fun onDenied(deniedPermission: List<String>) { //授权失败权限集合
                ToastUtil.showToast(
                    this@MainActivity,
                    "部分权限授予失败" + deniedPermission.size
                )
            }
        })


        bt_all.setOnClickListener {
            presenterRouter.create(GithubRepoAPI::class.java).getAllRepoByName("MCKRGF")
        }

        bt_start_service.setOnClickListener {
            val intent = Intent(this, LocationSubmitService::class.java)
            intent.putExtra(LocationSubmitService.IP_KEY, "192.168.95.163")
            intent.putExtra(LocationSubmitService.PORT_KEY, 10087)
            startForegroundService(intent)
        }
        bt_stop_service.setOnClickListener {
            stopService(Intent(this, LocationSubmitService::class.java))
        }
    }

    override fun getAllRepoByNameFailed(errorMsg: String?) {
        Log.d("zxcv", "a")
    }

    override fun getAllRepoByNameSuccess(entity: ArrayList<*>?) {
        Log.d("zxcv", "b")
    }
}