package com.yaobing.framemvpproject.app

import com.yaobing.module_middleware.NetworkService

class NetworkEnvImpl : NetworkService {
    override fun isDev(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun isOffice(): Boolean {
        return !BuildConfig.DEBUG
    }
}