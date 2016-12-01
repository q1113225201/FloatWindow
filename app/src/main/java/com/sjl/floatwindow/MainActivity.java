package com.sjl.floatwindow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.sjl.floatwindow.service.FloatWindowService;

public class MainActivity extends Activity {
    private static final int REQUEST_ACTION_USAGE_ACCESS_SETTINGS = 10;
    private static final int REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 开启悬浮窗服务的点击事件
     * @param view
     */
    public void startService(View view) {
        //检查是否有查看使用情况的权限
        //有，开启悬浮窗服务
        //无，去开启
        if(checkUsagePermission()) {
            startFloatWindowService();
        }else{
            openUsagePermission();
        }
    }
    /**
     * 查看使用情况授权
     * @return
     */
    private boolean checkUsagePermission() {
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = AppOpsManager.MODE_ALLOWED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),getPackageName());
        }
        return mode ==AppOpsManager.MODE_ALLOWED;
    }
    /**
     * 打开查看应用使用情况授权
     */
    private void openUsagePermission() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请开启查看应用使用情况的权限")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //In some cases, a matching Activity may not exist, so ensure you safeguard against this.
                        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),REQUEST_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    /**
     * 开启悬浮窗服务
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void startFloatWindowService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //版本在6.0以上判断是否有创建悬浮窗的权限
            //有，开启悬浮窗服务
            //无，去开启创建悬浮窗的权限
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, FloatWindowService.class));
                Toast.makeText(this,"开启悬浮窗服务成功",Toast.LENGTH_SHORT).show();
            }else{
                openOverlayPermission();
            }
        } else {
            startService(new Intent(this, FloatWindowService.class));
            Toast.makeText(this,"开启悬浮窗服务成功",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开悬浮窗权限
     */
    private void openOverlayPermission() {
        new AlertDialog.Builder(this)
                .setTitle("权限请求")
                .setMessage("显示悬浮窗需要开启悬浮窗显示权限，是否去开启？")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivityForResult(intent, REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    /**
     * 停止悬浮窗服务的点击事件
     * @return
     */
    public void stopService(View view) {
        stopService(new Intent(this, FloatWindowService.class));
    }

    /**
     * 授权查看应用使用情况权限的点击事件
     * @param view
     */
    public void authorizationUsage(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            Toast.makeText(this, "只有4.4以上手机需要查看应用使用情况权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 授权悬浮窗的点击事件
     * @param view
     */
    public void authorizationFloatWindow(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        } else {
            Toast.makeText(this, "只有6.0以上手机需要开启悬浮窗权限", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ACTION_USAGE_ACCESS_SETTINGS == requestCode){
            if(checkUsagePermission()) {
                startFloatWindowService();
            }
        }else if (REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION == requestCode) {
            startFloatWindowService();
        }
    }
}
