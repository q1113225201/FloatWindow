package com.sjl.floatwindow.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjl.floatwindow.R;
import com.sjl.floatwindow.manager.FloatWindowManager;
import com.sjl.floatwindow.util.AppUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 小悬浮窗
 *
 * @author SJL
 * @date 2016/11/30 21:36
 */
public class FloatWindowSmall extends LinearLayout {
    private Context context;

    private WindowManager windowManager;

    public WindowManager.LayoutParams layoutParams;

    public FloatWindowSmall(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        View view = findViewById(R.id.floatWindowSmallParent);

        layoutParams = new WindowManager.LayoutParams();

        //设置宽高
        layoutParams.width = view.getLayoutParams().width;
        layoutParams.height = view.getLayoutParams().height;
        //设置位置
        layoutParams.x = AppUtil.getScreen(context).x - layoutParams.width;
        layoutParams.y = AppUtil.getScreen(context).y / 2 - layoutParams.height / 2;
        //设置交互
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置图片格式
        layoutParams.format = PixelFormat.RGBA_8888;
        //设置对齐
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        //设置显示类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        update();
    }

    public void update() {
        ((TextView) findViewById(R.id.tvTime)).setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));
    }

    private float startX = -1;
    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x = (int) (event.getRawX() - startX);
                layoutParams.y = (int) (event.getRawY() - startY);
                windowManager.updateViewLayout(this, layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() == startX && event.getY() == startY) {
                    openFloatWindowBig();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void openFloatWindowBig() {
        FloatWindowManager.getInstance(context).closeFloatWindowSmall();
        FloatWindowManager.getInstance(context).showFloatWindowBig(context);
    }
}
