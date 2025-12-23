package com.yaobing.module_middleware.Utils;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


/**
 * Created by tfhr on 2018/1/8.
 */

public class AnimationUtil {
    private static final String TAG = AnimationUtil.class.getSimpleName();

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 渐变显示
     * @param view
     */
    public void fadeInToVisible(View view) {
        if (view == null) {
            Log.w(TAG, "fadeInToVisible: view is null");
            return;
        }

        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "fadeInToVisible: onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    public void fadeOutToInvisible(View... views) {
        if (views == null || views.length == 0) {
            Log.w(TAG, "fadeOutToInVisible: views is null or empty");
            return;
        }
        for (View view : views) {
            fadeOutToInvisible(view);
        }
    }

    public void fadeInToVisible(View... views) {
        if (views == null || views.length == 0) {
            Log.w(TAG, "fadeInToVisible: views is null or empty");
        }
        for (View view : views) {
            fadeInToVisible(view);
        }
    }

    /**
     * 渐变隐藏
     *
     * @param view
     */
    public void fadeOutToInvisible(View view) {
        if (view == null) {
            Log.w(TAG, "fadeInToVisible: view is null");
            return;
        }
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "fadeOutToInVisible: onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);
    }

    /**
     * 先渐变隐藏再渐变显示
     * @param view
     */
    public void fadeOutAndFadeIn(View view, OnAnimationRepeatCallback onAnimationRepeatCallback) {
        if (view == null) {
            Log.w(TAG, "fadeInAndFadeOut: view is null" );
            return;
        }
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(120);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "fadeInAndFadeOut: onAnimationStart ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "fadeInAndFadeOut: onAnimationEnd ");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "fadeInAndFadeOut: onAnimationRepeat ");
                if (onAnimationRepeatCallback != null) {
                    onAnimationRepeatCallback.onAnimationRepeat();
                }
            }
        });
        view.startAnimation(animation);
    }

    public void scaleViewIn(View view) {
        if (view == null) {
            Log.w(TAG, "scaleViewIn: view is null" );
            return;
        }
        ScaleAnimation animation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f
                , ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(animation);
    }

    public void scaleViewOut(View view) {
        if (view == null) {
            Log.w(TAG, "scaleViewOut: view is null" );
            return;
        }
        ScaleAnimation animation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f
                , ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(animation);
    }

    public interface OnAnimationRepeatCallback {
        void onAnimationRepeat();
    }
}
