package com.yaobing.module_middleware.Utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 10228 on 2016/11/14.
 * 实现检查软键盘是否打开，如果打开则进行关闭软键盘
 */
public class UiTools {

    public static void hideSoftKeyboard(Activity context) {
        if(context.getCurrentFocus() == null) {
            return;
        }
        IBinder ib = context.getCurrentFocus()
                .getWindowToken();
        if(ib != null) {
            ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(ib,0);
        }
    }



    //设置状态栏透明
    public static void setStatusBarTransparent(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //5.0 全透明实现
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //4.4 全透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 关键字加粗
     * @param keyword 要高亮的关键字
     * @param total	  文本内容
     * @return 关键字已经加粗的文本
     */
    public static SpannableStringBuilder setKeywordBold(String keyword, String total){

        String docInfo = total;
        int keywordIndex = total.indexOf(keyword);
        SpannableStringBuilder style = new SpannableStringBuilder(docInfo);
        while (keywordIndex != -1) {

            style.setSpan(new StyleSpan(Typeface.BOLD),keywordIndex,keywordIndex + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int tempkeywordTempIndex = keywordIndex + keyword.length();
            total = docInfo.substring(tempkeywordTempIndex);
            keywordIndex = total.indexOf(keyword);
            if (keywordIndex != -1) {
                keywordIndex = keywordIndex + tempkeywordTempIndex;
            }
        }

        return style;
    }


    /**
     *  @return 虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     *  @param activity 当前activity
     *  @describe 当前界面软键盘是否显示
     *  @return 软键盘是否显示
     */
    public static boolean isSoftShowing(Activity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        if(getSoftButtonsBarHeight(activity) > 0) {
            screenHeight = screenHeight + getSoftButtonsBarHeight(activity);
        }
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight*2/3 > rect.bottom;
    }


}
