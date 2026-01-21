package com.yaobing.module_middleware.network

import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlIntercepter : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val a = chain.request().header()
    }
}