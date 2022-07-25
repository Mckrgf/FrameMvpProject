package com.yaobing.module_middleware.Utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.yaobing.module_apt.Bind;
import com.yaobing.module_apt.BindByTag;
import com.yaobing.module_apt.custom.OnChild;
import com.yaobing.module_apt.custom.OnItemChild;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaobing on 2018/4/27.
 * Email:yaobing@supcon.com
 */

public class ViewBinder {

    public static void bindTag(Activity activity) {
        bindTag(activity, activity.getWindow().getDecorView());
    }

    public static void bindTag(final Object target, View source) {
        List<Field> fields = getAllContextFields(target);
        if (fields != null && fields.size() > 0) {
            for (final Field field : fields) {
                try {
                    field.setAccessible(true);
                    if (field.get(target) != null) {
                        continue;
                    }

                    Bind bind = field.getAnnotation(Bind.class);
                    if (bind != null) {
                        int viewId = bind.value();
                        field.set(target, source.findViewById(viewId));
                        continue;
                    }

                    BindByTag bindByTag = field.getAnnotation(BindByTag.class);
                    if (bindByTag != null) {
                        String tag = bindByTag.value();
                        field.set(target, source.findViewWithTag(tag));
                        continue;
                    }

                    String fieldName = field.getName();
                    field.set(target, source.findViewWithTag(fieldName));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> void setParam(Object target, T result, String param){
        try {

            Field paramField = null;
            if(param.contains(".")){
//                Field entityField = target.getClass().getDeclaredField(param.split("\\.")[0]);
                Field entityField = getOneField(target, param.split("\\.")[0]);
                if(entityField == null){
                    return;
                }
                entityField.setAccessible(true);
                Object entity = entityField.get(target);
                if(entity!=null){
//                    paramField = entity.getClass().getDeclaredField(param.split("\\.")[1]);
                    paramField = getOneField(entity, param.split("\\.")[1]);
                        if(paramField!= null){
                            paramField.setAccessible(true);
                            paramField.set(entity, result);
                        }
                    }
            }
            else{
                paramField = getOneField(target, param);
                if(paramField!= null){
                    paramField.setAccessible(true);
                    paramField.set(target, result);
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static Object getParam(Object target, String param){

        if(TextUtils.isEmpty(param)){
            return null;
        }

        try {

            Field paramField = null;
            if(param.contains(".")){
//                Field entityField = target.getClass().getDeclaredField(param.split("\\.")[0]);
                Field entityField = getOneField(target, param.split("\\.")[0]);
                if(entityField == null){
                    return null;
                }
                entityField.setAccessible(true);
                Object entity = entityField.get(target);
                if(entity!=null){
//                    paramField = entity.getClass().getDeclaredField(param.split("\\.")[1]);
                    paramField = getOneField(entity, param.split("\\.")[1]);
                    if(paramField!= null){
                        paramField.setAccessible(true);
                        Object current =  paramField.get(entity);
                        if(current!=null){
                            return current;
                        }
                    }
                }
            }
            else{
                paramField = getOneField(target, param);
                if(paramField!= null){
                    paramField.setAccessible(true);
                    Object current =  paramField.get(target);
                    if(current!=null){
                        return current;
                    }
                }
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static void bindListener(Object target, View source) {
//        List<Method> methods = getAllContextMethods(target);
//        if (methods != null && methods.size() > 0) {
//            for (Method method : methods) {
//                try {
//                    method.setAccessible(true);
//
//                    OnChild onChild = method.getAnnotation(OnChild.class);
//                    if (onChild != null) {
//                        String[] views = onChild.views();
//                        for (String viewTag : views) {
//                            View custom = source.findViewWithTag(viewTag);
//
//                            Method m = custom.getClass()
//                                    .getMethod("setOnChildViewClickListener", OnChildViewClickListener.class);
//                            if (m != null) {
//                                m.invoke(custom, new DeclaredOnChildViewClickListener(method, target));
//                            }
//                        }
//                    }
//
//
//                    OnItemChild onItemChild = method.getAnnotation(OnItemChild.class);
//                    if (onItemChild != null) {
//                        Field refreshListControllerField = getOneField(target, "refreshListController");
//
//                        refreshListControllerField.setAccessible(true);
//                        RefreshListController refreshListController = (RefreshListController) refreshListControllerField.get(target);
//                        Object adapter = refreshListController.getListAdapter();
//                        Method m = adapter.getClass()
//                                .getMethod("setOnItemChildViewClickListener", OnItemChildViewClickListener.class);
//                        if (m != null) {
//                            m.invoke(adapter, new DeclaredOnItemChildViewClickListener(method, target));
//                        }
//                    }
//
//                    continue;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private static class DeclaredOnChildViewClickListener implements OnChildViewClickListener {
//
//        private Method mMethod;
//        private Object mObject;
//
//        public DeclaredOnChildViewClickListener(Method method, Object object) {
//            this.mMethod = method;
//            this.mObject = object;
//        }
//
//        @Override
//        public void onChildViewClick(View childView, int action, Object obj) {
//            mMethod.setAccessible(true);
//
//            try {
//                mMethod.invoke(mObject, childView, action, obj);
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    mMethod.invoke(mObject);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//    }

//    private static class DeclaredOnItemChildViewClickListener implements OnItemChildViewClickListener {
//
//        private Method mMethod;
//        private Object mObject;
//
//        public DeclaredOnItemChildViewClickListener(Method method, Object object) {
//            this.mMethod = method;
//            this.mObject = object;
//        }
//
//
//        @Override
//        public void onItemChildViewClick(View childView, int position, int action, Object obj) {
//            mMethod.setAccessible(true);
//
//            try {
//                mMethod.invoke(mObject, childView, position, action, obj);
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    mMethod.invoke(mObject);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//    }


    private static List<Field> getAllContextFields(Object target){
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = target.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).

            if(tempClass.getName().contains("com.supcon.common.view")){
                tempClass = null;
            }
            else {
                fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
                tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
            }
        }
        return fieldList;
    }

    private static List<Method> getAllContextMethods(Object target){
        List<Method> methodList = new ArrayList<>() ;
        Class tempClass = target.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).


            if(tempClass.getName().contains("com.supcon.common.view")){
                tempClass = null;
            }
            else {
                methodList.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
                tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
            }
        }
        return methodList;
    }

    private static List<Field> getAllFields(Object target){
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = target.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    private static Field getOneField(Object target, String name){
        Field field = null;
        Class tempClass = target.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            try {
                field = tempClass.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
            }
            if(field != null){
               return field;
           }
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己

        }
        return field;
    }

    private static Object getFieldValue(Object target, String name){
        Field field = getOneField(target, name);

        if(field != null){
            try {
                field.setAccessible(true);
                return field.get(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}