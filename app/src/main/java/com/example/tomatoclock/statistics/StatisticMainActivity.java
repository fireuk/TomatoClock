package com.example.tomatoclock.statistics;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomatoclock.Base.BaseActivity;
import com.example.tomatoclock.R;

public class StatisticMainActivity extends BaseActivity implements RecordFragment.OnListFragmentInteractionListener{

    private FragmentManager mFtM;
    private FragmentTransaction mFtT;
    private Fragment mFt_Record;
    private final String TAG = "braind_StatisticMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_main);

        mFtM = getSupportFragmentManager();
        mFtT = mFtM.beginTransaction();
        mFt_Record = new RecordFragment();
        mFtT.add(R.id.statisticsfragmentContainer, mFt_Record);
        mFtT.commit();
    }

    @Override
    public void onListFragmentInteraction(RecordContent.RecordItem item) {
        Log.d(TAG,"item.date = "+item.date);
    }
}
