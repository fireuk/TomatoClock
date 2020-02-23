package com.example.tomatoclock.statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tomatoclock.R;
import com.example.tomatoclock.statistics.RecordContent.RecordItem;
import com.example.tomatoclock.statistics.RecordFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyRecordRecyclerViewAdapter.ViewHolder> {

    private final List<RecordItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRecordRecyclerViewAdapter(List<RecordItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRecordItem = mValues.get(position);
        holder.mDateView.setText(holder.mRecordItem.date);
        holder.mDurationTimeView.setText(holder.mRecordItem.duration_time);
        holder.mTaskNameView.setText(holder.mRecordItem.task_name);
        holder.mTaskStatusView.setText(holder.mRecordItem.task_status);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mRecordItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        /*public final TextView mIdView;*/
        public final TextView mDateView;
        public final TextView mDurationTimeView;
        public final TextView mTaskNameView;
        public final TextView mTaskStatusView;
        public RecordItem mRecordItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.date);
            mDurationTimeView = (TextView) view.findViewById(R.id.duration_time);
            mTaskNameView = (TextView) view.findViewById(R.id.task_name);
            mTaskStatusView = (TextView)view.findViewById(R.id.task_status);
        }
    }
}
