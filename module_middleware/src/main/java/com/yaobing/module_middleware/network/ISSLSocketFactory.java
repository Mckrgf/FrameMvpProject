package com.yaobing.module_middleware.network;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Create by 姚冰
 * on 2020/10/29
 */
public interface ISSLSocketFactory {
    HostnameVerifier getHostnameVerifier();

    X509TrustManager getTrustManager();

    SSLSocketFactory getSslSocketFactory();

}
