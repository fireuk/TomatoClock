package com.example.tomatoclock.statistics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tomatoclock.R;
import com.example.tomatoclock.statistics.RecordFragment.OnListFragmentInteractionListener;
import com.example.tomatoclock.statistics.dummy.DummyContent.DummyItem;
import com.example.tomatoclock.statistics.RecordContent.RecordItem;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
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
        holder.mIdView.setText(holder.mRecordItem.id);
        holder.mDateView.setText(holder.mRecordItem.date);
        holder.mStartTimeView.setText(holder.mRecordItem.start_time);
        holder.mEndTimeView.setText(holder.mRecordItem.end_time);

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
        public final TextView mIdView;
        public final TextView mDateView;
        public final TextView mStartTimeView;
        public final TextView mEndTimeView;
        public DummyItem mItem;
        public RecordItem mRecordItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.data_id);
            mDateView = (TextView) view.findViewById(R.id.date);
            mStartTimeView = (TextView) view.findViewById(R.id.start_time);
            mEndTimeView = (TextView) view.findViewById(R.id.end_time);
        }
    }
}
