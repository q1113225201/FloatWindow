package com.sjl.floatwindow.manager;

import android.content.Context;
import android.view.WindowManager;

import com.sjl.floatwindow.view.FloatWindowBig;
import com.sjl.floatwindow.view.FloatWindowProgress;
import com.sjl.floatwindow.view.FloatWindowSmall;

/**
 * 悬浮窗管理
 *
 * @author SJL
 * @date 2016/11/30 21:34
 */
public class FloatWindowManager {
    private static FloatWindowManager floatWindowManager;

    private static WindowManager windowManager;

    private Context context;

    public static FloatWindowManager getInstance(Context context) {
        if (floatWindowManager == null) {
            floatWindowManager = new FloatWindowManager(context);
        }
        return floatWindowManager;
    }

    public FloatWindowManager(Context context) {
        this.context = context;
    }

    private static FloatWindowSmall floatWindowSmall;

    /**
     * 显示小悬浮窗
     *
     * @param context
     */
    public void showFloatWindowSmall(Context context) {
        if (floatWindowSmall == null) {
            floatWindowSmall = new FloatWindowSmall(context);
            this.context = context;
        }
        //floatWindowManager = new FloatWindowManager(context);
        getWindowManager().addView(floatWindowSmall, floatWindowSmall.layoutParams);
    }

    /**
     * 更新小悬浮窗
     */
    public void updateFloatWindowSmall() {
        if (floatWindowSmall != null) {
            floatWindowSmall.update();
        }
    }

    /**
     * 关闭小悬浮窗
     */
    public void closeFloatWindowSmall() {
        if (floatWindowSmall != null) {
            getWindowManager().removeView(floatWindowSmall);
            floatWindowSmall = null;
        }
    }

    private FloatWindowBig floatWindowBig;

    /**
     * 显示大悬浮窗
     *
     * @param context
     */
    public void showFloatWindowBig(Context context) {
        if (floatWindowBig == null) {
            floatWindowBig = new FloatWindowBig(context);
            this.context = context;
        }
        getWindowManager().addView(floatWindowBig, floatWindowBig.layoutParams);
    }

    /**
     * 关闭大悬浮窗
     */
    public void closeFloatWindowBig() {
        if (floatWindowBig != null) {
            getWindowManager().removeView(floatWindowBig);
            floatWindowBig = null;
        }
    }

    private FloatWindowProgress floatWindowProgress;

    /**
     * 显示进度条悬浮窗
     *
     * @param context
     */
    public void showFloatWindowProgress(Context context) {
        if (floatWindowProgress == null) {
            floatWindowProgress = new FloatWindowProgress(context);
            this.context = context;
        }

        getWindowManager().addView(floatWindowProgress, floatWindowProgress.layoutParams);
    }

    /**
     * 关闭进度条悬浮窗
     */
    public void closeFloatWindowProgress(){
        if(floatWindowProgress!=null){
            getWindowManager().removeView(floatWindowProgress);
            floatWindowProgress = null;
        }
    }

    /**
     * 悬浮窗是否显示
     * @return
     */
    public boolean isFloatWindowShowing() {
        return floatWindowBig != null || floatWindowSmall != null || floatWindowProgress != null;
    }

    /**
     * 关闭所有悬浮窗
     */
    public void closeAllFloatWindow() {
        closeFloatWindowSmall();
        closeFloatWindowBig();
        closeFloatWindowProgress();
    }

    private WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }
}
