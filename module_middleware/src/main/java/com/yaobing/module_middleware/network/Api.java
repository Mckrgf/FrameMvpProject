package com.yaobing.module_middleware.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yaobing.module_middleware.BaseApp;
import com.yaobing.module_middleware.MiddleWareConstant;
import com.yaobing.module_middleware.Utils.LogUtil;
import com.yaobing.module_middleware.Utils.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : yaobing
 * @date : 2020/10/29 9:33
 * @desc :
 */
public class Api {
    public Retrofit retrofit;
    public ApiService service;
    private static Interceptor networkInterceptor;//自定义的网络拦截器，处理重定向
    private static Interceptor headInterceptor;//自定义的head拦截器，模块单独调试的时候用
    private static Interceptor responseInterceptor;//自定义的head拦截器，模块单独调试的时候用
    private boolean isDebug = true;
    private static boolean followRedirects = true;
    private final int DEFAULT_TIME_OUT_SECONDS = 30;
    private int mTimeOut = DEFAULT_TIME_OUT_SECONDS;

    private static ISSLSocketFactory mSSLSocketFactoryHelper;

    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //构造方法私有
    private Api() {
        build();
    }

    private void build(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.i(message);
            }
        });
        logInterceptor.setLevel(isDebug?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);

        File cacheFile = new File(BaseApp.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(followRedirects)
                .readTimeout(mTimeOut, TimeUnit.SECONDS)
                .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                .writeTimeout(mTimeOut, TimeUnit.SECONDS)
//                .addInterceptor(headInterceptor == null?new HeadInterceptor():headInterceptor)
//                .addInterceptor(new RedirectInterceptor())
//                .addInterceptor(responseInterceptor==null?new LoginInterceptor():responseInterceptor)
//                .addInterceptor(logInterceptor)
                .cache(cache)
//                .cookieJar(new LocalCookieJar())
//                .hostnameVerifier(SSLSocketFactoryHelper.getInstance().getHostnameVerifier())
//                .sslSocketFactory(SSLSocketFactoryHelper.getInstance().getmSslSocketFactory(),
//                        SSLSocketFactoryHelper.getInstance().getmTrustManager())
                ;
        if(mSSLSocketFactoryHelper!=null){

            if(mSSLSocketFactoryHelper.getTrustManager() == null){
                builder.sslSocketFactory(mSSLSocketFactoryHelper.getSslSocketFactory())
                        .hostnameVerifier(mSSLSocketFactoryHelper.getHostnameVerifier());
            }
            else{
                builder.sslSocketFactory(mSSLSocketFactoryHelper.getSslSocketFactory(), mSSLSocketFactoryHelper.getTrustManager())
                        .hostnameVerifier(mSSLSocketFactoryHelper.getHostnameVerifier());
            }

        }


        if(networkInterceptor!=null){
            builder.addNetworkInterceptor(networkInterceptor);
        }


        OkHttpClient okHttpClient = builder.build();

        Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();

        boolean isUrlEnabled = SharedPreferencesUtils.getParam(BaseApp.getInstance(), MiddleWareConstant.SPKey.URL_ENABLE, false);

        if(isUrlEnabled && TextUtils.isEmpty(BaseApp.getUrl()) || !isUrlEnabled && TextUtils.isEmpty(BaseApp.getIp())){
            LogUtil.e("缓存的url或ip为空！");
            BaseApp.setIp("api.github.com/");
            BaseApp.setPort("");
            SharedPreferencesUtils.setParam(BaseApp.getInstance(), MiddleWareConstant.SPKey.URL_ENABLE, false);
        }

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getHost()/*isUrlEnabled?"http://"+MBapApp.getUrl()+"/":"http://"+ MBapApp.getIp()+":"+ MBapApp.getPort()+"/"*/)
                .build();
        service = retrofit.create(ApiService.class);
    }

    public static String getHost() {

        StringBuilder sb = new StringBuilder("http://");

        if (SharedPreferencesUtils.getParam(BaseApp.getInstance(), MiddleWareConstant.SPKey.URL_ENABLE, false)) {
            String url = SharedPreferencesUtils.getParam(BaseApp.getInstance(), MiddleWareConstant.SPKey.URL, "");
            if(TextUtils.isEmpty(url)){
                return "";
            }

            if(url.endsWith("/")){
                url = url.substring(0, url.length()-1);
            }

            if(url.contains("http")){
                sb = new StringBuilder(url);
            }
            else{
                sb.append(url);
            }
        } else {
            sb.append(BaseApp.getIp());
            if (!BaseApp.getPort().isEmpty()) {
                sb.append(":");
                sb.append(BaseApp.getPort());
            }
        }


        return sb.toString();
    }


//    public String getBaseUrl(){
//        boolean isUrlEnabled = SharedPreferencesUtils.getParam(MBapApp.getAppContext(), MBapConstant.SPKey.URL_ENABLE, false);
//
//        return isUrlEnabled?"http://"+MBapApp.getUrl()+"/":"http://"+ MBapApp.getIp()+":"+ MBapApp.getPort()+"/";
//    }

    public void rebuild(){
        build();
    }

    public void setTimeOut(int timeOut){
        this.mTimeOut = timeOut;
        rebuild();
    }

    public void setDebug(boolean isDebug){
        this.isDebug = isDebug;
        rebuild();
    }

    /**
     * 模块单独调试的时候用来导入自定义的HeadInterceptor
     * @param interceptor 拦截器
     */
    public static void setHeadInterceptor(Interceptor interceptor) {
        headInterceptor = interceptor;
    }

    /**
     * networkInterceptor用来处理重定向等操作
     * @param interceptor 网络拦截器
     */
    public static void setNetworkInterceptor(Interceptor interceptor) {
        networkInterceptor = interceptor;
    }

    /**
     * 模块单独调试的时候用来导入自定义的HeadInterceptor
     * @param interceptor 拦截器
     */
    public static void setResponseInterceptor(Interceptor interceptor) {
        responseInterceptor = interceptor;
    }

    /**
     * 是否允许重定向
     * @param flag f
     */
    public static void setFollowRedirects(boolean flag) {
        followRedirects = flag;
    }

    class LocalCookieJar implements CookieJar {
        List<Cookie> cookies;
        @Override
        public List<Cookie> loadForRequest(HttpUrl arg0) {
            if (cookies != null)
                return cookies;
            return new ArrayList<Cookie>();
        }

        @Override
        public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
            this.cookies = cookies;
        }

    }

    public static void setSSLSocketFactoryHelper(ISSLSocketFactory SSLSocketFactoryHelper) {
        mSSLSocketFactoryHelper = SSLSocketFactoryHelper;
    }
}
