package com.yaobing.module_middleware.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;

public class StatusBarUtil {

//    /** Use default color {@link #defaultColor_21} between 5.0 and 6.0.*/
//    private static final int USE_DEFAULT_COLOR = -1;

    /** Use color {@link #setUseStatusBarColor} between 5.0 and 6.0.*/
    private static final int USE_CUR_COLOR = -2;

//    /**
//     * Default status bar color between 21(5.0) and 23(6.0).
//     * If status color is white, you can set the color outermost.
//     * */
//    private static int defaultColor_21 = Color.parseColor("#66000000");

    /**
     * Setting the status bar color.
     * It must be more than 21(5.0) to be valid.
     *
     * @param color Status color.
     */
    private static void setUseStatusBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? color : USE_CUR_COLOR);

        }
    }

    //设置状态栏黑底白字
    public static void setStatusBarBlack(Activity activity){

        StatusBarUtil.setUseStatusBarColor(activity, Color.BLACK);
    }

    public static void setStatusBarWhite(Activity activity) {
        StatusBarUtil.setUseStatusBarColor(activity, Color.WHITE);
    }


    public static void setActivityFullScreen(Activity activity){

        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //设置activity状态栏UI：安卓6.0及以上修改状态栏字体为黑色、背景为透明，6.0以下字体白色、背景黑色
    public static void setAppStatusBar(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            StatusBarUtil.setUseStatusBarColor(activity, Color.WHITE);
            StatusBarUtil.setSystemStatus(activity);
        }else {

            //全屏显示
            StatusBarUtil.setUseStatusBarColor(activity,Color.BLACK);
        }
    }


    /**
     * Setting the status bar transparently.
     * See {@link #setUseStatusBarColor}.
     */
    @Deprecated
    public static void setTransparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // after 21(5.0)
            setUseStatusBarColor(activity, Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // between 19(4.4) and 21(5.0)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * Setting up whether or not to invade the status bar & status bar font color
     *
     第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
     */
    private static void setSystemStatus(Activity activity) {
        int flag = 0;
//        if (isTransparent) {
        // after 16(4.1)
//            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            // after 23(6.0)
            flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // between 19(4.4) and 21(5.0)
//            if (isTransparent) {
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }
            ViewGroup contentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View childAt = contentView.getChildAt(0);
            childAt.setFitsSystemWindows(true);
        }
    }
}
