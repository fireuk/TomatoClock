package com.example.tomatoclock;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "braind_MainFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    private Button mBtn_1001;
    private Button mBtn_2505;
    private Button mBtn_3005;
    private Button mBtn_6005;
    private Button mBtn_Start;
    private MainActivity mMa_Activity;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");
        mMa_Activity = (MainActivity) getActivity();
        mBtn_Start = (Button)mMa_Activity.findViewById(R.id.start_btn);
        mBtn_Start.setOnClickListener(this);
        mBtn_1001 = (Button)mMa_Activity.findViewById(R.id.btn_1001);
        mBtn_1001.setOnClickListener(this);
        mBtn_2505 = (Button)mMa_Activity.findViewById(R.id.btn_2505);
        mBtn_2505.setOnClickListener(this);
        mBtn_3005 = (Button)mMa_Activity.findViewById(R.id.btn_3005);
        mBtn_3005.setOnClickListener(this);
        mBtn_6005 = (Button)mMa_Activity.findViewById(R.id.btn_6005);
        mBtn_6005.setOnClickListener(this);
        mEt_TaskTime = (EditText)mMa_Activity.findViewById(R.id.edit_worktime);
        mEt_RelaxTime = (EditText)mMa_Activity.findViewById(R.id.edit_relaxtime);
        mEt_TaskName = (EditText)mMa_Activity.findViewById(R.id.edit_taskname);
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
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

    // 按钮自动填充默认时间
    private EditText mEt_TaskTime;
    private EditText mEt_RelaxTime;
    private EditText mEt_TaskName;
    @Override
    public void onClick(View v) {
        Log.d(TAG, "id = "+v.getId());
        int id = v.getId();
        switch (id){
            case R.id.btn_1001:
                mEt_TaskTime.setText("10");
                mEt_RelaxTime.setText("1");
                break;
            case R.id.btn_2505:
                mEt_TaskTime.setText("25");
                mEt_RelaxTime.setText("5");
                break;
            case R.id.btn_3005:
                mEt_TaskTime.setText("30");
                mEt_RelaxTime.setText("5");
                break;
            case R.id.btn_6005:
                mEt_TaskTime.setText("60");
                mEt_RelaxTime.setText("5");
                break;
            case R.id.start_btn:
                MainActivity ma = (MainActivity)getActivity();
                ma.mSt_TaskTime = mEt_TaskTime.getText().toString();
                ma.mSt_RelaxTime = mEt_RelaxTime.getText().toString();
                ma.mSt_TaskName = mEt_TaskName.getText().toString();
                ma.startCuntDownTimer();
            default:
                break;
        }
    }

    String getTaskTime(){
        String st = "xxx";
        if (mEt_TaskTime != null){
            st = mEt_TaskTime.getText().toString();
        }
        return st;
    }
}
