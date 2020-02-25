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
    public static final String COLUMN_DURATION_TIME  = "duration_time";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_STATUS = "task_status";

    /*
    * 日期和时间格式
    * */
    public static final String DATE_FORMAT = "yyyy年MM月dd日";
    public static final String TIME_FORMAT = "HH:mm";


}
