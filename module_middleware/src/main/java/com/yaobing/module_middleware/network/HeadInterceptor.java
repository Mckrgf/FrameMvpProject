package com.yaobing.module_middleware.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author : yaobing
 * @date : 2020/10/29 9:44
 * @desc :
 */
public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl url = chain.request().url();
        if(url.encodedPath().contains("/cas/mobile/logon") /*|| url.encodedPath().contains("/cas/logout")*/){
            return chain.proceed(chain.request());
        }

        if(url.encodedPath().contains("/services/public/") ){
            return chain.proceed(chain.request().newBuilder()
                    .addHeader("USER_AGENT", "Linux; U; Android")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build());
        }

        return chain.proceed(chain.request().newBuilder()
//                .addHeader("Cookie", BaseApp.getCooki())
//                .addHeader("Authorization", BaseApp.getAuthorization())
                .addHeader("USER_AGENT", "Linux; U; Android")
                .addHeader("Content-Type", "application/json;text/plain")
                .build());
    }
}
