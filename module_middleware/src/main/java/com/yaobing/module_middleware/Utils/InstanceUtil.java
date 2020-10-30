package com.yaobing.module_middleware.Utils;

/**
 * @author : yaobing
 * @date : 2020/10/28 14:44
 * @desc :
 */
public class InstanceUtil {
    /**
     * 通过实例工厂去实例化相应类
     *
     * @param clazz  返回实例的泛型类型
     * @param <T> t
     * @return 实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        try {
//            return (T) InstanceFactory.create(clazz);
//            Constructor<T> constructor = clazz.getConstructor();
            return clazz.newInstance();

//            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
