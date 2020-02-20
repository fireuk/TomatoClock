package com.example.tomatoclock.statistics;

import android.provider.BaseColumns;

public class Data implements BaseColumns {
    // 数据库信息
    public static final String SQLITE_NAME = "taskStore.db";
    public static final int DB_VERSON = 1;

    /*
    * 信息表和字段
    * */
    public static final String TABLE_NAME = "task";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_START_TIME  = "start_time";
    public static final String COLUMN_END_TIME = "end_time";

    /*
    * 日期和时间格式
    * */
    public static final String DATE_FORMAT = "yyyy年MM年dd日";
    public static final String TIME_FORMAT = "HH:mm";


}
