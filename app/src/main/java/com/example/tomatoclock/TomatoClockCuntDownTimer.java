package com.example.tomatoclock;

import android.os.CountDownTimer;

/**
 * create by Administrator
 * create on 2020/2/13 0013
 * description
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

    public TomatoClockCuntDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TomatoClockCuntDownTimer(long millisInFuture, long countDownInterval, int int_minutes){
        super(millisInFuture, countDownInterval);
        mint_Minute = int_minutes;
        ml_totalTime = millisInFuture;
    }

    public String getRemainingTime(int int_minute){
        mStringMinute = (int_minute>=10)?String.valueOf(mint_Minute):"0"+String.valueOf(mint_Minute);
        if (mInt_Second == 0 && mint_Minute > 0){
            mString_task_time = mStringMinute+":00";
            mInt_Second = 59;
            mint_Minute--;
        }
        else if (mInt_Second > 0){
            mString_task_time = mInt_Second >=10
                    ?mStringMinute+":"+String.valueOf(mInt_Second)
                    :mStringMinute+":0"+String.valueOf(mInt_Second);
            mInt_Second--;
        }
        return mString_task_time;
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
