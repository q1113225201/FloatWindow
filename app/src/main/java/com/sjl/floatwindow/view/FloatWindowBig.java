package com.sjl.floatwindow.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sjl.floatwindow.R;
import com.sjl.floatwindow.manager.FloatWindowManager;
import com.sjl.floatwindow.service.FloatWindowService;
import com.sjl.floatwindow.util.AppUtil;

/**
 * 大悬浮窗
 *
 * @author SJL
 * @date 2016/11/30 21:36
 */
public class FloatWindowBig extends LinearLayout {
    private Context context;

    public WindowManager.LayoutParams layoutParams;

    public FloatWindowBig(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);

        View view = findViewById(R.id.floatWindowBigParent);
        layoutParams = new WindowManager.LayoutParams();
        //设置宽高
        layoutParams.width = view.getLayoutParams().width;
        layoutParams.height = view.getLayoutParams().height;
        //设置位置
        layoutParams.x = AppUtil.getScreen(context).x - layoutParams.width;
        layoutParams.y = AppUtil.getScreen(context).y / 2 - layoutParams.height / 2;
        //设置对齐
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //设置图片格式
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置交互
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置显示
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        initView();
    }

    private void initView() {
        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭大悬浮窗
                FloatWindowManager.getInstance(context).closeFloatWindowBig();
                //打开小悬浮窗
                FloatWindowManager.getInstance(context).showFloatWindowSmall(context);
            }
        });
        findViewById(R.id.btnProgress).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭大悬浮窗
                FloatWindowManager.getInstance(context).closeFloatWindowBig();
                //显示进度条悬浮窗
                FloatWindowManager.getInstance(context).showFloatWindowProgress(context);
                //3秒后进度条悬浮窗消失
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //关闭进度条悬浮窗
                        FloatWindowManager.getInstance(context).closeFloatWindowProgress();
                        //显示大悬浮窗
                        FloatWindowManager.getInstance(context).showFloatWindowBig(context);
                    }
                },3000);
            }
        });
        findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭所有悬浮窗
                FloatWindowManager.getInstance(context).closeAllFloatWindow();
                //停止服务
                context.stopService(new Intent(context, FloatWindowService.class));
            }
        });
    }
}
