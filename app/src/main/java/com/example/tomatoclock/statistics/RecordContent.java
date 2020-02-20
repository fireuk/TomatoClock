package com.example.tomatoclock.statistics;

import android.content.Context;
import android.database.Cursor;

import com.example.tomatoclock.MainActivity;

import java.util.ArrayList;

public class RecordContent {

    public static class RecordItem {
        public final String date;
        public final String start_time;
        public final String end_time;
        public final String id;

        public RecordItem(String id, String date, String start_time, String end_time) {
            this.date = date;
            this.start_time = start_time;
            this.end_time = end_time;
            this.id = id;
        }
    }

    ArrayList<RecordItem> ITEMS = new ArrayList<RecordItem>();

    public static ArrayList<RecordItem>getRecordItems(Context contex){
        ArrayList<RecordItem> ITEMS = new ArrayList<RecordItem>();

        int Count = 2;
        RecordItem recordItem;
        Cursor csr = MainActivity.mSdb.query(Data.TABLE_NAME, null,null,null,null,null,null);
        String s_id = "";
        String s_date = "";
        String s_start_time = "";
        String s_end_time="";
        if (csr.moveToFirst()) {
            do {
                s_id = csr.getString(csr.getColumnIndex(Data._ID));
                s_date = csr.getString(csr.getColumnIndex(Data.COLUMN_DATE));
                s_start_time = csr.getString(csr.getColumnIndex(Data.COLUMN_START_TIME));
                s_end_time = csr.getString(csr.getColumnIndex(Data.COLUMN_END_TIME));

                recordItem = createRecordItem(s_id, s_date, s_start_time, s_end_time);
                ITEMS.add(recordItem);
            } while (csr.moveToNext());
        }
        csr.close();

        return  ITEMS;
    }

    private static RecordItem createRecordItem(String id, String date, String start_time, String end_time) {
        return new RecordItem(id, date, start_time, end_time);
    }
}
