package com.yaobing.module_middleware.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.yaobing.module_middleware.Utils.LogUtil;

/**
 * @author : yaobing
 * @date : 2021/8/5 13:24
 * @desc :
 */
public class StopCheckService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Python py;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(StopCheckService.this));
                }
                py = Python.getInstance();
            }
        }).start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (null != py) {
                PyObject aa = py.getModule("mobilephonetest").callAttr("insertData", values[0], values[1], values[2]);
                if (null != aa) {
                    LogUtil.d("获取数据成功，运动状态为：" + aa);
                }else {
                    LogUtil.e("获取数据失败");
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != sensorManager) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
