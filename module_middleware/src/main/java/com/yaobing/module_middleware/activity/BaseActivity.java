package com.yaobing.module_middleware.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.yaobing.module_middleware.BaseApp;
import com.yaobing.module_middleware.Utils.ViewBinder;
import com.yaobing.module_middleware.interfaces.PermissionListener;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseActivity extends AppCompatActivity {

    private BaseApp app;
    public Uri uriForFile;
    public String path;
    public File file;
    protected View rootView;
    protected Context context;
    public final int permissionRequestCode = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        rootView = LayoutInflater.from(this).inflate(getLayoutID(), null);
        setContentView(rootView);
        onInit();
        initView();
        initListener();
        initData();
        app = (BaseApp) getApp();
        app.addActivity(this);
        String activities = "";
        for (int i = 0; i < ((BaseApp) getApp()).getActivitylists().size(); i++) {
            activities = activities + "\n" + ((BaseApp) getApp()).getActivitylists().get(i).toString();
        }
    }
    protected abstract int getLayoutID();
    protected void onInit() {
        ViewBinder.bindTag(this);
    }
    protected void initView() {

    }
    protected void initListener() {

    }
    protected void initData() {

    }

    public void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != app) app.removeActivity(this);
    }

    public Context getContext() {
        return this;
    }

    public Application getApp() {
        return getApplication();
    }

    public void openActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

    public void openActivity(Class<?> cls, HashMap hashMap) {
        Intent i = new Intent(this, cls);
        i.putExtra("data", (Serializable) hashMap);
        startActivity(i);
    }

    public void openCamera(int requestCode, String file_name) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/pic/" + file_name);
        file.getParentFile().mkdirs();
        path = file.getPath();
        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        uriForFile = FileProvider.getUriForFile(this, "com.god.yb.testgitdemo.fileprovider", file);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        startActivityForResult(intent, requestCode);
    }

    private ProgressDialog dialog;

    public void showProgress() {
        if (null == dialog) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("加载中...");
        }
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);
        dialog.show();
    }

    public void closeProgress() {
        if (null != dialog && dialog.isShowing()) {
            dialog.cancel();

        }
    }

    protected Retrofit getRetrofit(String base_url, boolean isShowDialog) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (isShowDialog) {
            showProgress();
        }
        return retrofit;
    }

    private PermissionListener mListener;

    /**
     * 权限申请
     * @param permissions 待申请的权限集合
     * @param listener  申请结果监听事件
     */

    public void requestRunTimePermission(String[] permissions, PermissionListener listener){
        this.mListener = listener;

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()){  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),permissionRequestCode);
        }else{  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case permissionRequestCode:
                if (grantResults.length > 0){
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED){ //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){  //用户拒绝权限为空
                        mListener.onGranted();
                    }else {  //不为空
                        //回调授权成功的接口
                        mListener.onDenied(deniedPermissions);
                        //回调授权失败的接口
                        mListener.onGranted(grantedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

}
