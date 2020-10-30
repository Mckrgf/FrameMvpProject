package com.yaobing.module_middleware;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.yaobing.module_middleware.Utils.AppUtils;
import com.yaobing.module_middleware.Utils.SharedPreferencesUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tfhr on 2018/2/1.
 */

public class BaseApp extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;

    private static final String TAG = "App";
    private static boolean isLogin = false;
    private static String userName = "";
    private static String password = "";
    private static String ip = "";
    private static String port = "";
    private static String url = "";

    private static Typeface newFontType;
    private static boolean isAlone = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Utils.init(this);
        CrashHandler.getInstance().init(this);
        Log.i(TAG, "当前的进程名字是：" + AppUtils.getCurProcessName(this));
    }

    //测试文件的输入输出（以流的方式）所使用的路径
    public static String commonPath = Environment.getExternalStorageDirectory() + "/testgitdemo";
    public static String cachePath = commonPath + "/cachefile.txt";
    /**
     * 实现android 程序退出
     */
    private List<Activity> mList = new LinkedList<Activity>();

    //获取mList列表
    public List<Activity> getActivitylists() {
        return mList;
    }

    /**
     * 在BaseActivity的onCreate方法中调用 ，维护一个activity队列
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 在BaseActivity的onDestroy方法中调用 如果一个activity已经销毁了
     * 从队列中删除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    /**
     * 程序退出
     *
     * @param code
     */
    public final void exit(int code) {
        System.exit(code);
    }

    /**
     * 释放资源，退出程序时候调用
     */
    public final void release() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean screenIsLock = false;
    private static BaseApp app;

    public static BaseApp getInstance() {
        return app;
    }

    public static String getIp() {
        if(TextUtils.isEmpty(ip)){
            ip = SharedPreferencesUtils.getParam(getInstance(), MiddleWareConstant.SPKey.IP, "");
        }

        return ip;
    }

    public static void setIp(String ip) {
        BaseApp.ip = ip;
        SharedPreferencesUtils.setParam(getInstance(), MiddleWareConstant.SPKey.IP, ip);
    }

    public static String getPort() {
        if(TextUtils.isEmpty(port)){
            port = SharedPreferencesUtils.getParam(getInstance(), MiddleWareConstant.SPKey.PORT, "");
        }
        return port;
    }

    public static void setPort(String port) {
        BaseApp.port = port;
        SharedPreferencesUtils.setParam(getInstance(), MiddleWareConstant.SPKey.PORT, port);
    }

    public static String getUrl() {
        if(TextUtils.isEmpty(url)){
            url = SharedPreferencesUtils.getParam(getInstance(), MiddleWareConstant.SPKey.URL, "");
        }
        return url;
    }

    public static void setUrl(String url) {
        BaseApp.url = url;
        SharedPreferencesUtils.setParam(getInstance(), MiddleWareConstant.SPKey.URL, url);
    }

}
