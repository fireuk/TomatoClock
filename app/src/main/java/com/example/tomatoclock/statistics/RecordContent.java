package com.example.tomatoclock.statistics;

import android.content.Context;
import android.database.Cursor;

import com.example.tomatoclock.MainActivity;

import java.util.ArrayList;

public class RecordContent {

    public static class RecordItem {
        public final String date;
        public final String duration_time;
        public final String task_name;
        public final String task_status;

        public RecordItem(String date, String duration_time, String task_name, String task_status) {
            this.date = date;
            this.duration_time = duration_time;
            this.task_name = task_name;
            this.task_status = task_status;
        }
    }

    public static ArrayList<RecordItem>getRecordItems(Context contex){
        ArrayList<RecordItem> ITEMS = new ArrayList<RecordItem>();

        RecordItem recordItem;
        Cursor csr = MainActivity.mSdb.query(Data.TABLE_NAME, null,null,null,null,null,null);
        String s_date = "";
        String s_duration_time = "";
        String s_task_name = "";
        String s_task_status = "";
        if (csr.moveToFirst()) {
            do {
                s_date = csr.getString(csr.getColumnIndex(Data.COLUMN_DATE));
                s_duration_time = csr.getString(csr.getColumnIndex(Data.COLUMN_DURATION_TIME));
                s_task_name = csr.getString(csr.getColumnIndex(Data.COLUMN_TASK_NAME));
                s_task_status = csr.getString(csr.getColumnIndex(Data.COLUMN_TASK_STATUS));

                recordItem = createRecordItem(s_date, s_duration_time, s_task_name, s_task_status);
                ITEMS.add(recordItem);
            } while (csr.moveToNext());
        }
        csr.close();

        return  ITEMS;
    }

    private static RecordItem createRecordItem(String date, String duration_time, String task_name, String task_status) {
        return new RecordItem(date, duration_time, task_name, task_status);
    }
}
