package com.example.tomatoclock;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * create by Administrator
 * create on 2020/2/13 0013
 * 1. 继承Android的CountDownTimer，不用自己写Handler来更新UI了
 * 2. 但是功能相对简单，只能start和cancel,没有pause和restart的能力
 * 3. 加了个进度条和定制的时间显示
 */
public class TomatoClockCuntDownTimer extends CountDownTimer {

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    private int mInt_Second = 0;
    private String mString_task_time;
    private String mStringMinute;
    private int mint_Minute = 0;
    private long ml_totalTime = 0;
    private String TAG = "braind_TomatoClockCuntDownTimer";

    public TomatoClockCuntDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TomatoClockCuntDownTimer(long millisInFuture, long countDownInterval, int int_minutes){
        super(millisInFuture, countDownInterval);
        mint_Minute = int_minutes;
        ml_totalTime = millisInFuture;
    }

    public String getRemainingTime(long l_UntilFinished){
        mString_task_time = "" ;
        int int_TotalSeconds = (int)l_UntilFinished/1000;
        //Log.d(TAG, "l_UntilFinished = " + l_UntilFinished + " and int_TotalSeconds = "+int_TotalSeconds);
        int int_minute = int_TotalSeconds/60;
        int int_second = int_TotalSeconds%60;
        Log.d(TAG, "int_minute = " + int_minute + " and int_second = "+int_second);

        if( int_minute < 10 ){
            mString_task_time += "0" ;
        }
        mString_task_time += int_minute+":" ;
        if( int_second < 10 ){
            mString_task_time += "0" ;
        }
        mString_task_time += int_second ;
        return mString_task_time ;
    }

    public int getProgress(long l_UntilFinished){
        int i_progress = 0;
        long l_elapsed = ml_totalTime - l_UntilFinished;
        float f_progress= 1000*l_elapsed/ml_totalTime;
        i_progress = (int)f_progress;
        return i_progress;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }


}
