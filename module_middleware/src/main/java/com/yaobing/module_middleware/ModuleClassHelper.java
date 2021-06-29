package com.yaobing.module_middleware;


import com.yaobing.module_middleware.Utils.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 资源导入器，用于IntentRouter,WidgetWapper 导入
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class ModuleClassHelper {

    /**
     * 模块IntentRouter,WidgetWapper
     * 在这里设置模块名，未设置的模块这些资源不会导入
     */
    private static final String[] MODULES = {
            "module_middleware","mylibrary","app"


    };

    private static class MainRouterHolder {
        private static ModuleClassHelper instance = new ModuleClassHelper();
    }


    public static ModuleClassHelper getInstance() {

        return MainRouterHolder.instance;
    }

    List<String> classPaths = new ArrayList<>();

    private ModuleClassHelper() {
        for (String module : MODULES) {

            classPaths.add("com.yaobing.framemvpproject." + module + ".IntentRouter");
//            classPaths.add("com.yaobing.framemvpproject." + module + ".WidgetWapper");
        }
    }


    public void setup() {

        for (String module : classPaths) {

            try {
                Class clazz = Class.forName(module);

                Method method = clazz.getMethod("setup", new Class[]{});
                method.invoke(null);

            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
    }

}
