package com.yaobing.module_middleware.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

/**
 * Create by 姚冰
 * on 2020/10/29
 */
public class SharedPreferencesUtils {
    private static final String DEFAULT_FILE_NAME = "common";
    private static final String CACHE_FILE_NAME = "common-cache";
    private static final int MODE7 = 0;
    private static final int MODE = 7;

    public SharedPreferencesUtils() {
    }

    public static void setParam(Context context, String key, Object object) {
        setValue(context, "common", key, object, true);
    }

    public static void setParamByCommit(Context context, String key, Object object) {
        setValue(context, "common", key, object, false);
    }

    private static void setValue(Context context, String fileName, String key, Object object, boolean async) {
        if (context != null && !TextUtils.isEmpty(key) && object != null) {
            String type = object.getClass().getSimpleName();
            SharedPreferences sp = getSharedPreferences(context, fileName);
            SharedPreferences.Editor editor = sp.edit();
            if ("String".equals(type)) {
                editor.putString(key, (String)object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer)object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean)object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float)object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long)object);
            } else if ("Double".equals(type)) {
                editor.putFloat(key, ((Double)object).floatValue());
            }

            if (async) {
                editor.apply();
            } else {
                editor.commit();
            }

        }
    }

    public static void setCacheParam(Context context, String key, Object object) {
        setValue(context, "common-cache", key, object, true);
    }

    private static Object getValue(Context context, String fileName, String key, Object defaultObject) {
        if (context != null && !TextUtils.isEmpty(key)) {
            String type = defaultObject.getClass().getSimpleName();
            SharedPreferences sp = getSharedPreferences(context, fileName);
            if ("String".equals(type)) {
                return sp.getString(key, (String)defaultObject);
            } else if ("Integer".equals(type)) {
                return sp.getInt(key, (Integer)defaultObject);
            } else if ("Boolean".equals(type)) {
                return sp.getBoolean(key, (Boolean)defaultObject);
            } else if ("Float".equals(type)) {
                return sp.getFloat(key, (Float)defaultObject);
            } else if ("Long".equals(type)) {
                return sp.getLong(key, (Long)defaultObject);
            } else {
                return "Double".equals(type) ? (double)sp.getFloat(key, ((Double)defaultObject).floatValue()) : null;
            }
        } else {
            return null;
        }
    }

    private static SharedPreferences getSharedPreferences(Context context, String fileName) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= 24) {
            sp = context.getSharedPreferences(fileName, 0);
        } else {
            sp = context.getSharedPreferences(fileName, 7);
        }

        return sp;
    }

    public static <T> T getParam(Context context, String key, T defaultObject) {
        return (T) getValue(context, "common", key, defaultObject);
    }

    public static <T> T getCacheParam(Context context, String key, Object defaultObject) {
        return (T) getValue(context, "common-cache", key, defaultObject);
    }

    public static boolean toggleBooleanValue(Context context, String key, Boolean defaultValue) {
        boolean original = (Boolean)getParam(context, key, defaultValue);
        boolean newconfig = !original;
        setParam(context, key, newconfig);
        return newconfig;
    }
}
