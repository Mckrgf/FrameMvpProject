package com.yaobing.module_middleware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yaobing
 * @date : 2020/10/27 17:02
 * @desc :
 */
public class PresenterRouter implements InvocationHandler {
    private Map<String, Object> mPresenterMap = new HashMap<>();

    public PresenterRouter() {
    }

    public void register(Object presenter){

        Class<?>[] interfaces = presenter.getClass().getSuperclass().getInterfaces();

        if(interfaces == null ||  interfaces.length ==0){
            return;
        }

        if(mPresenterMap.containsKey(interfaces[0].getName())) {
            mPresenterMap.remove(interfaces[0].getName());
        }
        mPresenterMap.put(interfaces[0].getName(), presenter);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String presenterName = o.getClass().getInterfaces()[0].getName();
        Object presenter = mPresenterMap.get(presenterName);

        if(presenter!=null){
            return method.invoke(presenter, objects);
        }

        return null;
    }

    public <T> T create(final Class<T> presenter) {
        return (T) Proxy.newProxyInstance(presenter.getClassLoader(), new Class<?>[] { presenter },
                this);
    }


}
