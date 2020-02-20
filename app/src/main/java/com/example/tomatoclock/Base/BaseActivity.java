package com.example.tomatoclock.Base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

public class BaseActivity extends AppCompatActivity {
    private final String TAG = "braind_BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isnotch = isNotch();
        Log.d(TAG, "isNotch = "+ isnotch);
        if(isnotch){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                setDsiplayInNotchOnP();
            }
            else {
                setDisplayInNotch(this);
            }
        }
        //setContentView(com.example.tomatoclock.R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    public void setDisplayInNotch(Activity activity) {
        Log.d(TAG, "setDisplayInNotch");
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try {
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(activity.getWindow(), flag);
        }
        catch (Exception ignore) {
        }
    }

    // 延伸显示区域到刘海
    public void setDsiplayInNotchOnP(){
        Log.d(TAG, "setDsiplayInNotchOnP");
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        window.setAttributes(lp);
    }

    // 判断是否是刘海屏（可能是适用小米）
    private static boolean isNotch() {
        try {
            Method getInt = Class.forName("android.os.SystemProperties").getMethod("getInt", String.class, int.class);
            int notch = (int) getInt.invoke(null, "ro.miui.notch", 0);
            return notch == 1;
        } catch (Throwable ignore) {
        }
        return false;
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
