package com.example.tomatoclock;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Method;

/**
 * create by Administrator
 * create on 2020/2/13 0013
 * 1. 好久没写代码，命名有点乱
 * 2. 就一个Activity，简单实现一个倒计时
 * 3. 横屏的倒计时有逼格一点，所以就全搞横屏的了
 * 4. google的CuntDownTimer，不支持暂停，所以我有懒得做了，集成一个子类，简单实现UI更新就好了
 * 5. 声音提醒还没有做，每一个状态切换时，可以把声音提醒加上
 * 6. 切出界面，计时重新开始，你看着办
 * 7. 不加那些乱七八糟的统计功能，即用即走
 */
public class MainActivity extends FragmentActivity implements TimingFragment.OnFragmentInteractionListener,MainFragment.OnFragmentInteractionListener{

    private static final String TAG = "braind";
    public static AssetManager mAssetManager;
    public static Typeface mTypeface;
    public static int mInt_Main = 0;
    public static int mInt_CuntDownTimer = 1;
    public static int mInt_page_index = -1;
    public static Context mContext;
    public static Vibrator mVibrator;
    public static TimingFragment mTimingFragment;
    public static MainFragment mMainFragment;
    public static String mSt_TaskName;
    public static String mSt_TaskTime;
    public static String mSt_RelaxTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initialFragment();

        mAssetManager = getAssets();
        mTypeface = Typeface.createFromAsset(mAssetManager, "fonts/Roboto-Medium.ttf");
        mContext = this;
        mVibrator = (Vibrator) getSystemService(Activity.VIBRATOR_SERVICE);
    }

    public void initialFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMainFragment = new MainFragment();
        mTimingFragment = new TimingFragment();

        fragmentTransaction.add(R.id.fragmentContainer, mMainFragment, "mMainFragment");
        fragmentTransaction.add(R.id.fragmentContainer,mTimingFragment, "mTimingFragment");

        fragmentTransaction.hide(mTimingFragment);
        fragmentTransaction.commit();

        // 初始化时，首先进入main界面
        mInt_page_index= mInt_Main;
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

    public void startCuntDownTimer() {
        // 步骤1：获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 步骤2：获取FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 步骤4：动态添加fragment
        // 即将创建的fragment添加到Activity布局文件中定义的占位符中（FrameLayout）
        fragmentTransaction.hide(mMainFragment);
        fragmentTransaction.show(mTimingFragment);
        fragmentTransaction.commit();
        mInt_page_index = mInt_CuntDownTimer;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged " + hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        Log.d(TAG, "onResume");

        // 如果是从计时界面切出的，再回来时，重新计时
        if (mInt_page_index == mInt_CuntDownTimer){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
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

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (mInt_page_index == mInt_CuntDownTimer){
            /*//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // 在工作计时界面按返回键时，先把设置界面的布局先设置生效，然后获取其页面实例
            // setContentView(R.layout.activity_main);
            mInt_page_index = mInt_Main;

            getViewInstance(mInt_page_index);
            navigationToPage(mInt_Main);

            // 只有在进入设置界面时，才能设置InputValue
            setInputValue();
            Log.d(TAG, "onBackPressed and back to main");

            if (mCountDownTimer_Task != null){
                mCountDownTimer_Task.cancel();
            }
            if (mCountDownTimer_Relax != null){
                mCountDownTimer_Relax.cancel();
            }*/
            mInt_page_index = mInt_Main;
            FragmentManager fg = getSupportFragmentManager();
            FragmentTransaction ft = fg.beginTransaction();
            ft.hide(mTimingFragment);
            ft.show(mMainFragment);
            ft.commit();
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchEventListener != null){
            mTouchEventListener.onTouch(event);
        }
        else {
            Log.d(TAG, "mTouchEventListener == null");
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface TouchEventListener{
        public boolean onTouch(MotionEvent event);
    }

    public static TouchEventListener mTouchEventListener;
}
