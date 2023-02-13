package com.yaobing.framemvpproject.module_b;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yaobing.module_apt.Router;

@Router("TestCActivity")
public class TestCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_c);
    }
}