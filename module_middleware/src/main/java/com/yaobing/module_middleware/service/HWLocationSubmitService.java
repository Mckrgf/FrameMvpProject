package com.yaobing.module_middleware.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.yaobing.module_middleware.BaseApp;
import com.yaobing.module_middleware.R;
import com.yaobing.module_middleware.activity.BaseActivity;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@RequiresApi(Build.VERSION_CODES.O)
public class HWLocationSubmitService extends Service {

    private LocationManager lm;
    private RemoteViews notifyViews;
    public static String stringBuffer = "";
    public Location currentLocation;
    private DatagramSocket socket;
    String meid = "";
    JSONObject jsonObject = new JSONObject();
    JSONObject jsonObjectContent = new JSONObject();
    public static String ip;
    public static int port;
    public static int period = 3;
    public static String IP_KEY = "IP_KEY";
    public static String PORT_KEY = "PORT_KEY";
    private Disposable disposable;

    // 定位交互接入对象
    private FusedLocationProviderClient fusedLocationProviderClient;
    // 定位请求信息对象
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    public void setIpAndStart(String ip, int port) {
        meid = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        this.ip = ip;
        this.port = port;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            Toast.makeText(this, "请打开GPS", Toast.LENGTH_SHORT).show();
            stopSelf();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    stringBuffer = (location.toString());
                    currentLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
        notifyViews = new RemoteViews(getPackageName(), R.layout.layout_nitification);
        notifyViews.setTextViewText(R.id.tv_tem, "获取定位信息：" + meid + "\n地址：" + ip + ":" + port);
        startNotification();

        addTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult
            ) {

                if (locationResult != null) {
                    List<Location> locations = locationResult.getLocations();
                    if (!locations.isEmpty()) {
                        for (Location location : locations) {
                            // 处理位置回调结果
                            Log.i("zxcv", "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.getLongitude() + "," + location.getLatitude() + "," + location.getAccuracy());
                        }
                    }

                }
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        // 设置位置更新的间隔（单位为毫秒）
        mLocationRequest.setInterval(1000);
        // 设置定位类型
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // 检查设备定位设置
        settingsClient.checkLocationSettings(locationSettingsRequest)
                // 检查设备定位设置接口成功监听回调
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse
                    ) {
                        // 设置满足定位条件，再发起位置请求
                        fusedLocationProviderClient
                                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                                // 请求位置更新接口成功监听回调
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid
                                    ) {
                                        Log.d("zxcv","add成功");
                                    }
                                });
                    }
                })
                // 检查设备定位设置接口失败监听回调
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // 设置不满足定位条件
                        int statusCode = ((ApiException) e).getStatusCode();
                        Log.d("zxcv","检查设备定位设置接口失败监听回调");
                        // 当收到的异常为ResolvableApiException实例时，说明这时需要引导安装或升级HMS Core（APK）。
                        if (e instanceof ResolvableApiException) {
                            ResolvableApiException apiException = (ResolvableApiException)e;
                            // 调用apiException中的pendingIntent，传入requestCode参数。
                            try {
                                apiException.startResolutionForResult(BaseApp.getInstance().getActivitylists().get(BaseApp.getInstance().getActivitylists().size()-1), 111);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

            startNotification();


//        ip = intent.getStringExtra(IP_KEY);
//        port = intent.getIntExtra(PORT_KEY,0);
//        setIpAndStart(ip,port);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void addTimerTask() {
        Observable<Long> observable = Observable.interval(period, TimeUnit.SECONDS, Schedulers.io()).startWith((long) 0);
        Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onNext(@NonNull Long o) {
                if (null != currentLocation) {

                    try {
                        jsonObject.put("MsgType", "Location");
                        jsonObjectContent.put("sn", meid);
                        jsonObjectContent.put("timestamp", System.currentTimeMillis());
                        jsonObjectContent.put("lon", currentLocation.getLongitude());
                        jsonObjectContent.put("lat", currentLocation.getLatitude());

                        //以下不计
                        jsonObjectContent.put("floor", 1);
                        jsonObjectContent.put("buildId", 1);
                        jsonObjectContent.put("height", 1);
                        jsonObjectContent.put("battery", 1);

                        jsonObject.put("Content", jsonObjectContent);

                        InetAddress serverAddress = InetAddress.getByName(ip);
                        DatagramPacket mPackage = new DatagramPacket(jsonObject.toString().getBytes(), jsonObject.toString().getBytes().length, serverAddress, port);
                        socket.send(mPackage);
                        Log.i("zxcv", "发送一次数据 \n" + jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) { }

            @Override
            public void onComplete() { }
        };
        observable.subscribe(observer);
    }

    private void startNotification() {
        String channel_one_id = getPackageName();
        String channel_one_name = "channel gps";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_one_id, channel_one_name, NotificationManager.IMPORTANCE_NONE);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = new Notification.Builder(this, "123456")
                .setContent(notifyViews)
                .setSmallIcon(R.drawable.item_icon)
                .setChannelId(channel_one_id)
                .build();
        startForeground(123456, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        lm = null;
//        disposable.dispose();
        fusedLocationProviderClient
                .removeLocationUpdates(mLocationCallback
                )
                // 停止位置更新成功监听回调
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid
                    ) {
                        // ...
                    }
                })
                // 停止位置更新失败监听回调
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // ...
                    }
                });
    }
}
