package com.sjl.floatwindow.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sjl.floatwindow.R;
import com.sjl.floatwindow.util.AppUtil;

/**
 * 进度条悬浮窗
 * 
 * @author SJL
 * @date 2016/11/30 21:37
 */
public class FloatWindowProgress extends LinearLayout {
    private Context context;

    public WindowManager.LayoutParams layoutParams;

    public FloatWindowProgress(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.float_window_progress,this);

        layoutParams = new WindowManager.LayoutParams();

        View view = findViewById(R.id.floatWindowProgressParent);

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
    }
}
