package com.supcon.common.view.base.controller;

import android.content.Intent;

import com.supcon.common.view.Lifecycle;
import com.supcon.common.view.base.IData;

import java.util.Map;

/**
 * Created by wangshizhan on 17/6/29.
 */

public  class BaseController implements Lifecycle, IData{

    /**
     * 最初初始化
     */
    public void onInit(){

    }

    /**
     * 初始化VIEW
     */
    public void initView() {

    }

    /**
     * 初始化事件监听
     */
    public void initListener() {

    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * activity 或 fragment onStart
     */
    public void onStart() {

    }

    /**
     * activity 或 fragment onStop
     */
    public void onStop() {

    }

    /**
     * activity 或 fragment onResume
     */
    public void onResume() {

    }

    /**
     * activity 或 fragment onPause
     */
    public void onPause() {
    }

    /**
     * activity 或 fragment onDestroy
     */
    public void onDestroy() {

    }

    /**
     * activity 或 fragment onRetry
     */
    public void onRetry() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean checkBeforeSubmit(Map<String, Object> map) {
        return doSave(map);
    }

    @Override
    public boolean doSave(Map<String, Object> map) {
        return true;
    }

    @Override
    public boolean isModified() {
        return false;
    }
}
