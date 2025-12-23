package com.yaobing.module_middleware;

import android.util.Log;

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
public class ProxyHandler implements InvocationHandler {
    //负责存储代理类的接口interface
    private Map<String, Object> mPresenterMap = new HashMap<>();

//    private static class PresenterRouterHolder{
//        private static PresenterRouter instance = new PresenterRouter();
//    }
//
//
//    public static PresenterRouter getInstance() {
//
//        return PresenterRouterHolder.instance;
//    }

    /**
     * 注册一个服务，提供给业务层使用
     * 服务必须继承BasePresenter
     * @param presenter
     */
    public void register(Object presenter){
        Class a = presenter.getClass();
        Class b = a.getSuperclass();
        Class<?>[] interfaces = a.getInterfaces();

        if(interfaces == null ||  interfaces.length ==0){
            return;
        }

        if(mPresenterMap.containsKey(interfaces[0].getName())) {
            mPresenterMap.remove(interfaces[0].getName());
        }
        mPresenterMap.put(interfaces[0].getName(), presenter);
    }


    /**
     * 注销一个服务，提供给业务层使用
     * 服务必须继承BasePresenter
     * @param presenter
     */
    public void unRegister(Object presenter){

        Class<?>[] interfaces = presenter.getClass().getInterfaces();

        if(interfaces == null && interfaces.length ==0){
            return;
        }

        if(mPresenterMap.containsKey(interfaces[0].getName())) {
            mPresenterMap.remove(interfaces[0].getName());
        }

    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Log.d("zxcv","4调用invoke方法");
        String presenterName = o.getClass().getInterfaces()[0].getName();
        Object presenter = mPresenterMap.get(presenterName);

        if(presenter!=null){
            Log.d("zxcv","5获取到接口，然后调用实例中的接口方法");
            return method.invoke(presenter, objects[0] + "（已经被代理类增强了功能！）");
        }


        return null;
    }

    public <T> T create(final Class<T> presenter) {
        Log.d("zxcv","1调用了create");
        T a = (T) Proxy.newProxyInstance(presenter.getClassLoader(), new Class<?>[] { presenter },this);
        Log.d("zxcv","2根据类加载器+接口+handler获取到实例的代理类" + a.getClass().getName());
        return a;
    }


}
