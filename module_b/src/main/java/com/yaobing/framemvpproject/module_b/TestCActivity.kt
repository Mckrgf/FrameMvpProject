package com.yaobing.framemvpproject.module_b

import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yaobing.module_apt.Router
import com.yaobing.module_middleware.activity.BaseActivity
import com.yaobing.framemvpproject.module_b.R
import com.yaobing.framemvpproject.module_b.viewModel.DiceRollViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Router("TestCActivity")
class TestCActivity : BaseActivity() {
    override fun initView() {
        super.initView()
        val viewModel: DiceRollViewModel by viewModels()

        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.uiState.collect {
//                    // Update UI elements
//                    Log.d("lifeCycleLog","STARTED")
//                }
//            }
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","CREATED")
                }
            }
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","DESTROYED")
                }
            }
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","RESUMED")
                }
            }
            //不能添加这个，退出的时候会报错
//            repeatOnLifecycle(Lifecycle.State.INITIALIZED) {
//                viewModel.uiState.collect {
//                    // Update UI elements
//                    Log.d("lifeCycleLog","INITIALIZED")
//                }
//            }
        }
        findViewById<Button>(R.id.bt_kotlin_coroutine).also {
            it.setOnClickListener {

            }
        }
    }

//    fun getRepository() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val jsonBody = "{ username: \"$username\", token: \"$token\"}"
//            loginRepository.makeLoginRequest(jsonBody)
//        }
//    }

    override fun getLayoutID(): Int {
        return R.layout.activity_test_c
    }
}