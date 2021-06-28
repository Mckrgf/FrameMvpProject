package com.yaobing.module_middleware.interfaces;

import java.util.Map;

public interface IRouterManager {
    void register(String name, Class<?> clazz);
    void unregister(String name);
    void register(Map<String, Class<?>> routes);
}
