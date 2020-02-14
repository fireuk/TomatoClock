package com.example.tomatoclock;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    private static final String TAG = "braind";
    private static String m_string_task_name;
    private static String m_string_task_time;
    private static String mString_ralex_time;
    private static EditText m_editText_task_name;
    private static EditText m_editText_task_time;
    private static EditText m_editText_relax_time;
    private static EditText m_editText_repetition_time;
    private static TextView m_textView_task_name_onworking;
    private static TextView mTextView_remaining_time;
    private static TomatoClockCuntDownTimer mCountDownTimer_Task;
    private static TomatoClockCuntDownTimer mCountDownTimer_Relax;
    public static ConstraintLayout mConstraintLayout_Time;
    public static ProgressBar mProgressBar;
    public static AssetManager mAssetManager;
    public static Typeface mTypeface;

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
        getViewInstance(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAssetManager = getAssets();
        mTypeface = Typeface.createFromAsset(mAssetManager, "fonts/Roboto-Medium.ttf");
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

    public void setDsiplayInNotchOnP(){
        // 延伸显示区域到刘海
        Log.d(TAG, "setDsiplayInNotchOnP");
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        window.setAttributes(lp);
    }

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
        /*Intent intent = new Intent(this, Main2Activity.class);
        EditText editText = findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);*/

        // 选择Activity为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // 在变换布局之前，先把EditView里面的数据举出来
            getInputValue();

            setContentView(R.layout.activity_main2);

            // 必须在应用横屏布局后，才能找到这个布局的控件实例
            // 布局更换后，前一个布局的实例也被销毁了
            getViewInstance(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            // 启动计时
            startTask();
        }
    }


    public void updateTimeUi(String s_Remaing_time, int i_Progress){
        if(mTextView_remaining_time != null){
            mTextView_remaining_time.setText(s_Remaing_time);
            Log.d(TAG, "remaining_time = "+s_Remaing_time);
        }
        if(mProgressBar != null){
            mProgressBar.setProgress(i_Progress);
        }
    }

    public static int mint_Minute = 0;
    public void startTask(){
        if (m_textView_task_name_onworking != null){
            Log.d(TAG, "m_textView_task_name_onworking != null and m_string_task_name = "+m_string_task_name);
            m_textView_task_name_onworking.setText(m_string_task_name);
        }

        mint_Minute = getMinute(m_string_task_time, 0);
        Log.d(TAG, "m_string_task_time = "+m_string_task_time+" and mint_Minute = "+mint_Minute);
        int int_MillisInFuture = mint_Minute * 60 *1000;
        int int_CountdownInterval = 1000;
        if (mint_Minute > 0){
            mTextView_remaining_time.setTextSize(200);
            mTextView_remaining_time.setTypeface(mTypeface);
            mCountDownTimer_Task = (TomatoClockCuntDownTimer) new TomatoClockCuntDownTimer(int_MillisInFuture, int_CountdownInterval, mint_Minute){
                @Override
                public void onTick(long millisUntilFinished) {
                    super.onTick(millisUntilFinished);
                    Log.d(TAG, "millisUntilFinished = "+millisUntilFinished);
                    String string_time = getRemainingTime(mint_Minute);
                    int i_Progress = getProgress(millisUntilFinished);
                    updateTimeUi(string_time, i_Progress);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressBar.setProgress(0);
                    startRelax();
                }
            }.start();
        }
        else {
            mTextView_remaining_time.setTextSize(100);
        }
    }

    public void startRelax(){
/*        if(mProgressBar != null){
            mProgressBar.setProgress(0);
        }*/
        mint_Minute = getMinute(mString_ralex_time, 0);
        if (mint_Minute > 0 ){
            mConstraintLayout_Time.setBackground(getDrawable(R.drawable.green_bg_0));
            m_textView_task_name_onworking.setText(getText(R.string.relaxing));
            int int_MillisInFuture_Relax = mint_Minute * 60 *1000;
            int int_CountdownInterval_Relax = 1000;
            mCountDownTimer_Relax = (TomatoClockCuntDownTimer) new TomatoClockCuntDownTimer(int_MillisInFuture_Relax, int_CountdownInterval_Relax, mint_Minute){
                @Override
                public void onTick(long millisUntilFinished) {
                    super.onTick(millisUntilFinished);

                    String string_relax_time = getRemainingTime(mint_Minute);
                    int i_Progress = getProgress(millisUntilFinished);
                    updateTimeUi(string_relax_time, i_Progress);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    misionComplete();
                }
            }.start();
        }
        else {
            misionComplete();
        }
    }

    public void misionComplete(){
        mConstraintLayout_Time.setBackground(getDrawable(R.drawable.blue_bg));
        mProgressBar.setProgress(0);
        mTextView_remaining_time.setText("complete!");
        mTextView_remaining_time.setTextSize(100);
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
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
        if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation){

        }
        else if (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation){

        }
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
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // 在工作计时界面按返回键时，先把设置界面的布局先设置生效，然后获取其页面实例
            setContentView(R.layout.activity_main);
            getViewInstance(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // 只有在进入设置界面时，才能设置InputValue
            setInputValue();
            Log.d(TAG, "onBackPressed and SCREEN_ORIENTATION_LANDSCAPE");

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

    public void getViewInstance (int orientation){
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            m_editText_task_name = findViewById(R.id.edit_taskname);
            m_editText_task_time = findViewById(R.id.edit_worktime);
            m_editText_relax_time = findViewById(R.id.edit_relaxtime);
            m_editText_repetition_time = findViewById(R.id.edit_repetition);
        }
        else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
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
}
