package com.yaobing.module_middleware.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by Benjamin on 16/5/24.
 */
public class ViewUtils {

    public static int getViewLocationScreenX(View view) {
        return getViewLocationScreenXY(view, 0);
    }

    public static int getViewLocationScreenY(View view) {
        return getViewLocationScreenXY(view, 1);
    }

    public static int getViewLocationWindowX(View view) {
        return getViewLocationWindowXY(view, 0);
    }

    public static int getViewLocationWindowY(View view) {
        return getViewLocationWindowXY(view, 1);
    }

    private static int getViewLocationScreenXY(View view, int index) {
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        return xy[index];
    }

    private static int getViewLocationWindowXY(View view, int index) {
        int[] xy = new int[2];
        view.getLocationInWindow(xy);
        return xy[index];
    }

    public static int getId(Context paramContext, String className, String resName)
    {
        try
        {
            Class localClass = Class.forName(paramContext.getPackageName() + ".R$" + className);
            Field localField = localClass.getField(resName);
            int i = Integer.parseInt(localField.get(localField.getName()).toString());
            return i;
        } catch (Exception localException)
        {
            Log.e("getIdByReflection error", localException.getMessage());
        }

        return 0;
    }


    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     * @return host activity; or null if not available
     */
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     * @return host activity; or null if not available
     */
    public static Context getContextFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
