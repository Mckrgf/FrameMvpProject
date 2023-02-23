package com.yaobing.framemvpproject.module_b

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yaobing.framemvpproject.module_b.viewModel.DiceRollViewModel
import com.yaobing.module_apt.Router
import com.yaobing.module_middleware.Utils.ToastUtil
import com.yaobing.module_middleware.Utils.ToastUtils
import com.yaobing.module_middleware.activity.BaseActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@Router("TestCActivity")
class TestCActivity : BaseActivity() {
    override fun initView() {
        super.initView()
        val viewModel: DiceRollViewModel by viewModels()
        Log.d("threadLog-0", this.toString())

        val buttonCoroutine = findViewById<Button>(R.id.bt_kotlin_coroutine)
        buttonCoroutine.also {
            it.setOnClickListener {
                viewModel.rollDice()
            }
        }
        otherCoroutineCreate(viewModel)

        findViewById<Button>(R.id.bt_entrust_class).setOnClickListener {
            val b = baseImpl(this, "类委托", "b")
            Entrust(b).printf()
        }

    }

    //=========================委托相关
    interface Base {
        fun printf() {
        }
    }

    class baseImpl(var context: Context, var x: String, var name: String) : Base {
        override fun printf() {
            super.printf()
            Log.d("zxcv", "baseImpl用$name 说了$x")
            ToastUtil.showToast(context, "baseImpl用$name 说了$x")
        }
    }

    class Entrust(b: Base) : Base by b

    //=========================协程相关
    private fun otherCoroutineCreate(viewModel: DiceRollViewModel) {
        lifecycleScope.launch {
            Log.d("threadLog-1", this.toString())
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("threadLog-2", this.toString())
                viewModel.uiState.collect {

                    // Update UI elements
                    findViewById<TextView>(R.id.tv_viewmodel).text =
                        getString(R.string.tx_viewmodel) + ":" + it.firstDieValue + "; 循环了${it.numberOfRolls}次"
                    Log.d("threadLog-3", this.toString())

                    Log.d("lifeCycleLog", "STARTED")
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            Log.d("threadLog-4", this.toString())

            Log.d("lifeCycleLog", "launch CREATED")
        }
        lifecycleScope.launchWhenStarted {
            Log.d("threadLog-5", this.toString())

            Log.d("lifeCycleLog", "launch STARTED")
        }

        val eHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            run {
                Log.d("zxcv", coroutineContext.toString() + throwable.toString())
            }
        }
        //可以指定调度器、异常处理、启动模式
        lifecycleScope.launch(Dispatchers.Main + eHandler, CoroutineStart.ATOMIC) {
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog", "DESTROYED")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog", "RESUMED")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("lifeCycleLog", "CREATED")
                }
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