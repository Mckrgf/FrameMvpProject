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
        Log.d("threadLog-0",this.toString())

        lifecycleScope.launch {
            Log.d("threadLog-1",this.toString())
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("threadLog-2",this.toString())

                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("threadLog-3",this.toString())

                    Log.d("lifeCycleLog","STARTED")
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            Log.d("threadLog-4",this.toString())

            Log.d("lifeCycleLog","launch CREATED")
        }
        lifecycleScope.launchWhenStarted {
            Log.d("threadLog-5",this.toString())

            Log.d("lifeCycleLog","launch STARTED")
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","DESTROYED")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","RESUMED")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog","CREATED")
                }
            }
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