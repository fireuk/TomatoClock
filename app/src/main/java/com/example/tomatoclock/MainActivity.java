package com.example.tomatoclock;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
public class MainActivity extends Activity {

    private static final String TAG = "braind";
    private static String m_string_task_name;
    private static String m_string_task_time;
    private static String mString_ralex_time;
    private static EditText m_editText_task_name;
    private static EditText m_editText_task_time;
    private static EditText m_editText_relax_time;
    //private static EditText m_editText_repetition_time;
    private static TextView m_textView_task_name_onworking;
    private static TextView mTextView_remaining_time;
    private static TomatoClockCuntDownTimer mCountDownTimer_Task;
    private static TomatoClockCuntDownTimer mCountDownTimer_Relax;
    public static ConstraintLayout mConstraintLayout_Time;
    public static ProgressBar mProgressBar;
    public static AssetManager mAssetManager;
    public static Typeface mTypeface;
    public static int mInt_Main = 0;
    public static int mInt_CuntDownTimer = 1;
    public static int mInt_page_index = -1;
    public static int mint_Minute = 0;   // 设置的专注时间，单位分钟
    public static Context mContext;
    public static Vibrator mVibrator;

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
        setContentView(R.layout.activity_main);
        getViewInstance(mInt_Main);
        mInt_page_index= mInt_Main;
        mAssetManager = getAssets();
        mTypeface = Typeface.createFromAsset(mAssetManager, "fonts/Roboto-Medium.ttf");
        mContext = this;
        mVibrator = (Vibrator) getSystemService(Activity.VIBRATOR_SERVICE);
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

    public void sendMessage(View view) {
        // 进行界面切换
        if (mInt_page_index == mInt_Main) {

            // 在变换布局之前，先把EditView里面的数据取出来
            getInputValue();

            // 进入计时布局界面
            navigationToPage(mInt_CuntDownTimer);

            // 启动计时
            startTask();
        }
    }

    // 根据传入的index，导航到对应的界面
    public void navigationToPage(int int_page_index){
        int int_LayoutId = -1;
        if (int_page_index == mInt_Main){
            int_LayoutId = R.layout.activity_main;
        }
        else if (int_page_index == mInt_CuntDownTimer){
            int_LayoutId = R.layout.activity_main2;
        }

        if (int_LayoutId != -1){
            setContentView(int_LayoutId);
            mInt_page_index = int_page_index;
            getViewInstance(int_page_index);
        }
    }


    // 刷新计时界面的UI：数字和进度条
    public void updateTimeUi(String s_Remaing_time, int i_Progress){
        if(mTextView_remaining_time != null){
            mTextView_remaining_time.setText(s_Remaing_time);
            Log.d(TAG, "remaining_time = "+s_Remaing_time);
        }
        if(mProgressBar != null){
            mProgressBar.setProgress(i_Progress);
        }
    }

    // 启动task倒计时
    public void startTask(){
        if (m_textView_task_name_onworking != null){
            Log.d(TAG, "m_textView_task_name_onworking != null and m_string_task_name = "+m_string_task_name);
            m_textView_task_name_onworking.setText(m_string_task_name);
        }

        // 获取设置的task时长
        mint_Minute = getMinute(m_string_task_time, 0);
        Log.d(TAG, "m_string_task_time = "+m_string_task_time+" and mint_Minute = "+mint_Minute);

        // 如果设定的时间是>0的，才是有效的时间，才启动计时
        if (mint_Minute > 0){

            // 倒计时的文字字体设置
            mTextView_remaining_time.setTextSize(200);
            mTextView_remaining_time.setTypeface(mTypeface);

            // 设置倒计时的时间和间隔；间隔为1秒
            int int_MillisInFuture = mint_Minute * 60 *1000;
            int int_CountdownInterval = 1000;

            // 创建倒计时并启动
            mCountDownTimer_Task = (TomatoClockCuntDownTimer) new TomatoClockCuntDownTimer(int_MillisInFuture, int_CountdownInterval, mint_Minute){
                @Override
                public void onTick(long millisUntilFinished) {
                    super.onTick(millisUntilFinished);
                    Log.d(TAG, "millisUntilFinished = "+millisUntilFinished);
                    String string_time = getRemainingTime();
                    int i_Progress = getProgress(millisUntilFinished);
                    updateTimeUi(string_time, i_Progress);
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    // 播放提示音
                    playSoundAndVibrator(mContext, RingtoneManager.TYPE_RINGTONE, true);

                    // task时间到了以后，进度条归零
                    mProgressBar.setProgress(0);

                    // 启动relax倒计时
                    startRelax();
                }
            }.start();
        }
        else {
            // 如果设置的是无效task时长，那么显示提示文字并修改字体大小，因为太大了显示不下
            mTextView_remaining_time.setTextSize(100);
        }
    }

    // 启动relax倒计时
    public void startRelax(){
        mint_Minute = getMinute(mString_ralex_time, 0);

        // 设置的relax时间>0才有效，才启动relax倒计时
        if (mint_Minute > 0 ){
            // 进入relax时间后，切换一下背景，换一个心情
            mConstraintLayout_Time.setBackground(getDrawable(R.drawable.green_bg_0));
            m_textView_task_name_onworking.setText(getText(R.string.relaxing));
            int int_MillisInFuture_Relax = mint_Minute * 60 *1000;
            int int_CountdownInterval_Relax = 1000;
            mCountDownTimer_Relax = (TomatoClockCuntDownTimer) new TomatoClockCuntDownTimer(int_MillisInFuture_Relax, int_CountdownInterval_Relax, mint_Minute){
                @Override
                public void onTick(long millisUntilFinished) {
                    super.onTick(millisUntilFinished);

                    String string_relax_time = getRemainingTime();
                    int i_Progress = getProgress(millisUntilFinished);
                    updateTimeUi(string_relax_time, i_Progress);
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    // relax时间到，整个任务结束
                    // 如果循环再调用startTask，会不会造成很深的调用栈？
                    missionComplete();
                }
            }.start();
        }
        else {
            // 没有设置relax时间，直接结束任务
            missionComplete();
        }
    }

    // 任务完成后的处理
    public void missionComplete(){
        playSoundAndVibrator(mContext, RingtoneManager.TYPE_RINGTONE, true);

        // 设置任务完成后的背景
        mConstraintLayout_Time.setBackground(getDrawable(R.drawable.blue_bg));

        // 隐藏进度条
        mProgressBar.setProgress(0);

        // 设置任务完成后显示的文字和字体
        mTextView_remaining_time.setText("complete!");
        mTextView_remaining_time.setTextSize(100);

        // 隐藏任务名称
        m_textView_task_name_onworking.setText("");
    }

    public int getMinute(String minute, int defaultValue){
        int int_value = defaultValue;
        if (! TextUtils.isEmpty(minute)) {
            try {
                int_value =  Integer.parseInt(minute);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return int_value;
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
            startTask();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 切出界面后，计时取消
        if (mCountDownTimer_Task != null){
            mCountDownTimer_Task.cancel();
        }
        if (mCountDownTimer_Relax != null){
            mCountDownTimer_Relax.cancel();
        }
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
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // 在工作计时界面按返回键时，先把设置界面的布局先设置生效，然后获取其页面实例
            /*setContentView(R.layout.activity_main);
            mInt_page_index = mInt_Main;

            getViewInstance(mInt_page_index);*/
            navigationToPage(mInt_Main);

            // 只有在进入设置界面时，才能设置InputValue
            setInputValue();
            Log.d(TAG, "onBackPressed and back to main");

            if (mCountDownTimer_Task != null){
                mCountDownTimer_Task.cancel();
            }
            if (mCountDownTimer_Relax != null){
                mCountDownTimer_Relax.cancel();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    // 获取布局中的控件实例，只有设置生效的布局，才能获取到实例
    public void getViewInstance (int int_page_index){
        if (int_page_index == mInt_Main){
            m_editText_task_name = findViewById(R.id.edit_taskname);
            m_editText_task_time = findViewById(R.id.edit_worktime);
            m_editText_relax_time = findViewById(R.id.edit_relaxtime);
            //m_editText_repetition_time = findViewById(R.id.edit_repetition);
        }
        else if (int_page_index == mInt_CuntDownTimer){
            m_textView_task_name_onworking = findViewById(R.id.task_name_working);
            mTextView_remaining_time = findViewById(R.id.textView_remaining_time);
            mConstraintLayout_Time = findViewById(R.id.layout_timing);
            mProgressBar = findViewById(R.id.progressbar_timing);
        }
    }

    public void getInputValue() {
        m_string_task_name = m_editText_task_name.getText().toString();
        if (m_string_task_name.isEmpty()){
            m_string_task_name = getText(R.string.working).toString();
        }
        m_string_task_time = m_editText_task_time.getText().toString();
        mString_ralex_time = m_editText_relax_time.getText().toString();
        if (mString_ralex_time.isEmpty()){
            mString_ralex_time = m_editText_relax_time.getText().toString();
        }
    }

    public void setInputValue(){
        m_editText_task_name.setText(m_string_task_name);
        m_editText_task_time.setText(m_string_task_time);
        m_editText_relax_time.setText(mString_ralex_time);
    }

    // 按钮自动填充默认时间
    public void choseTime(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_1001:
                m_editText_task_time.setText("10");
                m_editText_relax_time.setText("1");
                break;
            case R.id.btn_2505:
                m_editText_task_time.setText("25");
                m_editText_relax_time.setText("5");
                break;
            case R.id.btn_3005:
                m_editText_task_time.setText("30");
                m_editText_relax_time.setText("5");
                break;
            case R.id.btn_6005:
                m_editText_task_time.setText("60");
                m_editText_relax_time.setText("5");
                break;
            default:
                break;
        }
    }

    Uri mUri;
    Ringtone mRingtone;
    public void playSoundAndVibrator(Context context, int i_SoundType, boolean b_Vibrator){
        mUri = RingtoneManager.getDefaultUri(i_SoundType);
        mRingtone = RingtoneManager.getRingtone(context, mUri);
        if (mUri != null && mRingtone != null){
            mRingtone.play();
        }

        if (b_Vibrator){
            if (mVibrator != null){
                //mVibrator.vibrate(1000);
                mVibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.EFFECT_HEAVY_CLICK));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mRingtone != null && mRingtone.isPlaying()){
                mRingtone.stop();
            }
        }
        return super.onTouchEvent(event);
    }
}
