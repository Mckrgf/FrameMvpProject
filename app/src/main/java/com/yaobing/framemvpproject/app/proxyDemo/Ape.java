package com.yaobing.framemvpproject.app.proxyDemo;

import android.util.Log;

public class Ape implements LifeBehaviorInterface{
    @Override
    public void eat(String food) {
        Log.d("zxcv","吃：" + food);
    }
}
