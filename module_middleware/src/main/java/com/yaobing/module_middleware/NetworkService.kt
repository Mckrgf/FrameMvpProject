package com.yaobing.module_middleware

import com.yaobing.module_apt.ServiceManagerInfo

@com.yaobing.module_apt.ServiceManagerInfo("com.yaobing.framemvpproject.app.NetworkEnvImpl")
interface NetworkService : IService {
    fun isDev() : Boolean
    fun isOffice() : Boolean
}