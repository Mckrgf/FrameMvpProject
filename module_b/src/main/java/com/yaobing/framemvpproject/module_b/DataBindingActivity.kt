package com.yaobing.framemvpproject.module_b

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.god.yb.testgitdemo.item.ObservableUser
import com.god.yb.testgitdemo.item.User
import com.god.yb.testgitdemo.viewModel.MyNewViewModel
import com.yaobing.framemvpproject.module_b.databinding.ActivityDataBindingBinding
import com.yaobing.framemvpproject.module_b.viewModel.DiceRollViewModel
import com.yaobing.framemvpproject.module_b.viewModel.MyViewModel
import com.yaobing.module_apt.Router
import com.yaobing.module_middleware.activity.BaseActivity
import kotlinx.coroutines.launch

@Router("DataBindingActivity")
class DataBindingActivity : BaseActivity() {
    private lateinit var binding: ActivityDataBindingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("zxcv", "onCreate")
        binding = DataBindingUtil.setContentView<ActivityDataBindingBinding>(
            this,
            R.layout.activity_data_binding
        )


        binding.myViewModel = myViewModel
        binding.lifecycleOwner = this
        livedata_viewmodel()
        databinding_with_observable_data()

    }

    override fun onResume() {
        super.onResume()
        Log.d("zxcv", "onResume")
    }

    private val myViewModel: MyViewModel
        get() {
            return ViewModelProvider(this)[MyViewModel::class.java]
        }
    private val myNewViewModel: MyNewViewModel
        get() {
            return ViewModelProvider(this)[MyNewViewModel::class.java]
        }

    private fun livedata_viewmodel() {
        myViewModel.users.observe(this, Observer {
            it.toString()
        })
        myViewModel.string.observe(this, Observer {
            Log.d("zxcv4", it)
        })
        myViewModel.stringPlus.observe(this, Observer {
            Log.d("zxcv5", it)
        })

        binding.btCalculate1.setOnClickListener {
            myViewModel.changeString("姚冰")
        }
//        binding.btCalculate2.setOnClickListener {
//            model.changeStringPlus()
//        }

        binding.apply {
            myNewViewModel.number.observe(this@DataBindingActivity) {
                btCalculate4.text = it.toString()
            }
            btCalculate2.setOnClickListener {
                myNewViewModel.add()
                myViewModel?.changeStringPlus()
            }
        }

    }


    private fun databinding_with_observable_data() {

        val bluedevice = User()
        bluedevice.name = ("姚冰")
        bluedevice.age = "4"
        bluedevice.sex = 4
        binding.user = bluedevice

        val observableUser = ObservableUser()
        observableUser.name.set("姚冰")
        observableUser.age.set(1)
        observableUser.sex.set(1)
        binding.observableUser = observableUser

        binding.tvShowGroup.setOnClickListener {

            //测试observable数据在dataBinding中的使用
            val age = observableUser.sex.get() + 1
            observableUser.sex.set(age)
            observableUser.age.set(age)

            if (binding.group456.visibility == View.VISIBLE) {
                binding.group456.visibility = View.GONE
            } else {
                binding.group456.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 用kotlin做livedata和viewmodel。目前有点小问题
     */
    private fun kotlin_livedata_viewmodel() {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same DiceRollViewModel instance created by the first activity.

        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact
        val viewModel: DiceRollViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    // Update UI elements
                    Log.d("zxcv", "it:$it")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("zxcv", "activity onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("zxcv", "activity onStop")
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_data_binding
    }

}