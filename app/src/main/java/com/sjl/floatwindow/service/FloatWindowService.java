package com.sjl.floatwindow.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.sjl.floatwindow.manager.FloatWindowManager;
import com.sjl.floatwindow.util.AppUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 悬浮窗服务
 *
 * @author SJL
 * @date 2016/11/30 21:35
 */
public class FloatWindowService extends Service {
    private Context context;

    private Timer timer;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            //每隔500毫秒执行刷新操作
            timer.schedule(new RefreshTask(),0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 后台检测刷新任务
     */
    class RefreshTask extends TimerTask {

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void run() {
            if(AppUtil.getLatestPackage(context)==null){
                //最近运行的app包名为null执行刷新小悬浮窗操作
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //已显示悬浮窗，更新小悬浮窗
                        FloatWindowManager.getInstance(context).updateFloatWindowSmall();
                    }
                });
                return;
            }
            if(AppUtil.isHome(context)){
                //当前在桌面上则显示或更新悬浮窗
                if (FloatWindowManager.getInstance(context).isFloatWindowShowing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已显示悬浮窗，更新小悬浮窗
                            FloatWindowManager.getInstance(context).updateFloatWindowSmall();
                        }
                    });
                } else if (!FloatWindowManager.getInstance(context).isFloatWindowShowing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //未显示悬浮窗，则显示小悬浮窗
                            FloatWindowManager.getInstance(context).showFloatWindowSmall(context);
                        }
                    });
                }
            }else{
                //当前不在桌面则关闭所有悬浮窗
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.getInstance(context).closeAllFloatWindow();
                    }
                });
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer = null;
        FloatWindowManager.getInstance(context).closeAllFloatWindow();
        super.onDestroy();
    }
}
