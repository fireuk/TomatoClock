package com.example.tomatoclock;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomatoclock.Base.BaseActivity;
import com.example.tomatoclock.custom.CustomActivity;
import com.example.tomatoclock.statistics.DataBaseHelper;

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
public class MainActivity extends BaseActivity implements TimingFragment.OnFragmentInteractionListener,MainFragment.OnFragmentInteractionListener{

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initialFragment();

        mAssetManager = getAssets();
        mTypeface = Typeface.createFromAsset(mAssetManager, "fonts/Roboto-Medium.ttf");
        mContext = this;
        mVibrator = (Vibrator) getSystemService(Activity.VIBRATOR_SERVICE);

        initialDateBase();
    }

    // 初始化存储数据库
    public static DataBaseHelper mDb_helper;
    public static ContentValues mCv;
    public static SQLiteDatabase mSdb;
    private void initialDateBase(){
        mDb_helper = new DataBaseHelper(mContext);
        mSdb = mDb_helper.getWritableDatabase();
        mCv = new ContentValues();
    }

    // 初始化首页和倒计时页面两个fragment
    public void initialFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMainFragment = new MainFragment();
        mTimingFragment = new TimingFragment();

        fragmentTransaction.add(R.id.fragmentContainer, mMainFragment, "mMainFragment");
        fragmentTransaction.add(R.id.fragmentContainer,mTimingFragment, "mTimingFragment");

        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.show(mMainFragment);
        fragmentTransaction.hide(mTimingFragment);
        fragmentTransaction.commit();

        // 初始化时，首先进入main界面
        mInt_page_index= mInt_Main;
    }

    public static String mSt_Date;
    public static String mSt_Start_Time;
    public static String mSt_End_Time;
    public void startCuntDownTimer() {
        // 步骤1：获取FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 步骤2：获取FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 步骤4：动态添加fragment
        // 即将创建的fragment添加到Activity布局文件中定义的占位符中（FrameLayout）
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.hide(mMainFragment);
        fragmentTransaction.show(mTimingFragment);
        fragmentTransaction.commit();
        mInt_page_index = mInt_CuntDownTimer;
    }

    public void startStatisticsActivity(){
        Intent intent = new Intent(MainActivity.this, CustomActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged " + hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
    }



    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if (mInt_page_index == mInt_CuntDownTimer){
            mInt_page_index = mInt_Main;
            FragmentManager fg = getSupportFragmentManager();
            FragmentTransaction ft = fg.beginTransaction();
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
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
