package com.example.tomatoclock;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tomatoclock.statistics.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "braind_TimingFragment";
    private TextView mTx_TaskName;
    private TextView mTx_RemainingTime;
    private ProgressBar mPb_TimeProgress;
    private  ConstraintLayout mConstraintLayout;
    private MainActivity mMa_Activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TimingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimingFragment newInstance(String param1, String param2) {
        TimingFragment fragment = new TimingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.timing_fragment, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        mMa_Activity = (MainActivity)getActivity();
        mTx_TaskName = mMa_Activity.findViewById(R.id.task_name_working);
        mTx_RemainingTime = mMa_Activity.findViewById(R.id.textView_remaining_time);
        mPb_TimeProgress = mMa_Activity.findViewById(R.id.progressbar_timing);
        mConstraintLayout = mMa_Activity.findViewById(R.id.layout_timing);

        mTouchListener = new MainActivity.TouchEventListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mRingtone != null && mRingtone.isPlaying()){
                        mRingtone.stop();
                    }
                }
                return false;
            }
        };
        MainActivity.mTouchEventListener = mTouchListener;

        // 下面的代码是测试代码，跨fragment的控件是找不到的，为null
        /*EditText et = ma.findViewById(R.id.edit_taskname);
        if (et == null){
            Log.d(TAG, "et==null");
        }
        else{
            Log.d(TAG, ""+et.getText().toString());
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mb_Ishidden){
            startCuntDownTimer(mSt_TaskName, mInt_TaskTime, TASKING);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private int mInt_TaskTime;
    private int mInt_RelaxTime;
    private String  mSt_TaskName;
    private boolean mb_Ishidden;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged::hidden = "+hidden);
        mb_Ishidden = hidden;
        if (!hidden){
            String st_TaskTime = "";
            String st_RelaxTime = "";
            String st_TaskName = "working";
            if (mMa_Activity != null){
                st_TaskTime = mMa_Activity.mSt_TaskTime;
                st_RelaxTime = mMa_Activity.mSt_RelaxTime;
                if (!mMa_Activity.mSt_TaskName.isEmpty()){
                    st_TaskName = mMa_Activity.mSt_TaskName;
                }
                else {
                    mMa_Activity.mSt_TaskName = st_TaskName;
                }
                mSt_TaskName = st_TaskName;

                Log.d(TAG, "st_TaskTime = "+st_TaskTime+",st_RelaxTime = "
                        +st_RelaxTime + " and mSt_TaskName = "+mSt_TaskName +
                        ",mMa_Activity.mSt_TaskName = "+mMa_Activity.mSt_TaskName);
            }
            mInt_TaskTime = getMinute(st_TaskTime, 0);
            mInt_RelaxTime = getMinute(st_RelaxTime,0);
            mCurrentState = 0;
            startCuntDownTimer(mSt_TaskName, mInt_TaskTime, TASKING);
        }
        else {
            // 如果被隐藏了，那么把计时器关掉
            if (mCountDownTimer != null){
                mCountDownTimer.cancel();
            }

            // 只有在从计时过程中，或者休息过程中，或者完成以后才插入数据库
            if (mCurrentState != 0){
                MainActivity.mCv.clear();

                // 如果现在还是task倒计时阶段，则取当前退出界面的时间为结束时间，并入库
                if ( mCurrentState == TASKING){
                    recordEndTime();
                }
            }
        }
    }

    private final int TASKING = 1;
    private final int RELAXING = 2;
    private final int COMPLETELE = 3;
    private int mCurrentState = 0;
    private static TomatoClockCuntDownTimer mCountDownTimer;
    private void startCuntDownTimer(String st_Title, int int_Minutes, final int int_CuntDownTimerType){
        if (int_Minutes > 0){
            mCurrentState = int_CuntDownTimerType;

            // 倒计时的文字字体设置
            mTx_RemainingTime.setTextSize(200);
            mTx_RemainingTime.setTypeface(MainActivity.mTypeface);
            mTx_TaskName.setText(st_Title);

            // 设置倒计时的时间和间隔；间隔为1秒
            int int_MillisInFuture = int_Minutes * 60 *1000;
            int int_CountdownInterval = 1000;

            if (int_CuntDownTimerType == RELAXING){
                // 进入relax时间后，切换一下背景，换一个心情
                mConstraintLayout.setBackground(mMa_Activity.getDrawable(R.drawable.green_bg_0));
            }
            else if (int_CuntDownTimerType == TASKING){
                mConstraintLayout.setBackground(mMa_Activity.getDrawable(R.drawable.red_bg_1));

                // 启动task倒计时时，记录本次的task的起始时间
                SimpleDateFormat sDF = new SimpleDateFormat(Data.DATE_FORMAT);
                SimpleDateFormat sTimeF = new SimpleDateFormat(Data.TIME_FORMAT);
                long l_currenTime = System.currentTimeMillis();
                Date Current_date = new Date(l_currenTime);
                mMa_Activity.mSt_Date = sDF.format(Current_date);
                mMa_Activity.mSt_Start_Time = sTimeF.format(Current_date);
                Log.d(TAG, "mSt_Date = "+mMa_Activity.mSt_Date + " and mSt_Start_Time = "+mMa_Activity.mSt_Start_Time);
            }

            // 创建倒计时并启动
            mCountDownTimer = (TomatoClockCuntDownTimer) new TomatoClockCuntDownTimer(int_MillisInFuture, int_CountdownInterval, int_Minutes){
                @Override
                public void onTick(long millisUntilFinished) {
                    super.onTick(millisUntilFinished);
                    String string_time = getRemainingTime(millisUntilFinished);
                    int i_Progress = getProgress(millisUntilFinished);
                    updateTimeUi(string_time, i_Progress);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    missionComplete(int_CuntDownTimerType);
                }
            }.start();
        }
        else {
            if(int_CuntDownTimerType == TASKING){
                String st_NoTaskTime = mMa_Activity.getResources().getString(R.string.noTaskTime);
                mTx_RemainingTime.setText(st_NoTaskTime);
                mTx_TaskName.setText("");

                // 如果设置的是无效task时长，那么显示提示文字并修改字体大小，因为太大了显示不下
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                    // 为我女儿的机器定制，机器比较老了
                    mTx_RemainingTime.setTextSize(20);
                }
                else {
                    mTx_RemainingTime.setTextSize(80);
                }
            }
            else if (int_CuntDownTimerType == RELAXING){
                // 没有设置relax时间，直接结束任务
                missionComplete(RELAXING);
            }

        }
    }

    // 任务完成后的处理
    public void missionComplete(int int_missionType){
        mCurrentState = COMPLETELE;

        // 隐藏进度条
        mPb_TimeProgress.setProgress(0);

        if (int_missionType == RELAXING){
            // 设置任务完成后的背景
            mConstraintLayout.setBackground(mMa_Activity.getDrawable(R.drawable.blue_bg));

            // 设置任务完成后显示的文字和字体
            mTx_RemainingTime.setText("complete!");
            mTx_RemainingTime.setTextSize(100);
            // 隐藏任务名称
            mTx_TaskName.setText("");
        }
        else if (int_missionType == TASKING){
            // 启动relax倒计时
            startCuntDownTimer("relaxing", mInt_RelaxTime, RELAXING);

            // task计时结束后，就记录为结束时间并入库，relax的时间不算入task计时
            recordEndTime();
        }

        playSoundAndVibrator(mMa_Activity, RingtoneManager.TYPE_RINGTONE, true);
    }

    private void recordEndTime(){
        SimpleDateFormat sTimeF = new SimpleDateFormat(Data.TIME_FORMAT);
        long l_currenTime = System.currentTimeMillis();
        Date Current_date = new Date(l_currenTime);
        MainActivity.mSt_End_Time = sTimeF.format(Current_date);

        // 入库到数据库
        MainActivity.mCv.put(Data.COLUMN_DATE, MainActivity.mSt_Date);
        MainActivity.mCv.put(Data.COLUMN_DURATION_TIME, MainActivity.mSt_Start_Time+" ~ "+MainActivity.mSt_End_Time);
        MainActivity.mCv.put(Data.COLUMN_TASK_NAME, MainActivity.mSt_TaskName);
        String st_Status = (mCurrentState == TASKING)?getString(R.string.task_interrupt):getString(R.string.task_complete);
        MainActivity.mCv.put(Data.COLUMN_TASK_STATUS, st_Status);
        MainActivity.mSdb.insert(Data.TABLE_NAME,null, MainActivity.mCv);
    }

    // 刷新计时界面的UI：数字和进度条
    public void updateTimeUi(String s_Remaing_time, int i_Progress){
        if(mTx_RemainingTime != null){
            mTx_RemainingTime.setText(s_Remaing_time);
            //Log.d(TAG, "remaining_time = "+s_Remaing_time);
        }
        if(mPb_TimeProgress != null){
            mPb_TimeProgress.setProgress(i_Progress);
        }
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

    private Uri mUri;
    private Ringtone mRingtone;
    private Vibrator mVibrator;
    public void playSoundAndVibrator(Context context, int i_SoundType, boolean b_Vibrator){
        if (mRingtone == null) {
            mUri = RingtoneManager.getDefaultUri(i_SoundType);
            mRingtone = RingtoneManager.getRingtone(context, mUri);
        }

        if (mRingtone != null && !mRingtone.isPlaying()){
            if (mUri != null && mRingtone != null){
                mRingtone.play();
                if (b_Vibrator){
                    if (mVibrator != null){
                        mVibrator.vibrate(1000);
                        //mVibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.EFFECT_HEAVY_CLICK));
                    }
                }
            }
        }



    }

    private MainActivity.TouchEventListener mTouchListener;
}
