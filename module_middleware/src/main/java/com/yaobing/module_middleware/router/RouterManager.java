package com.yaobing.module_middleware.router;


import com.yaobing.module_middleware.interfaces.IRouterManager;

import java.util.HashMap;
import java.util.Map;

public class RouterManager implements IRouterManager {


    private static class RouterManagerHolder{
        private static RouterManager instance = new RouterManager();
    }


    public static RouterManager getInstance() {

        return RouterManagerHolder.instance;
    }

    private RouterManager() {
    }


    private Map<String, Class<?>> mRouterMap = new HashMap<>();

    @Override
    public void register(String name, Class<?> clazz) {
        if(!mRouterMap.containsKey(name))
            mRouterMap.put(name, clazz);
    }

    @Override
    public void unregister(String name) {
        if(mRouterMap.containsKey(name))
            mRouterMap.remove(name);
    }

    @Override
    public void register(Map<String, Class<?>> routes) {
        if(routes==null){
            return;
        }
        mRouterMap.putAll(routes);
    }

    public Class<?> getDestination(String key){
        return  mRouterMap.get(key);
    }


}