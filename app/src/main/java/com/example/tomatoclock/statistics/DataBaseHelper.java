package com.example.tomatoclock.statistics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final String TAG = "braind_DataBaseHelper";

    public static final String CREATE_TASK = "create table " +
            Data.TABLE_NAME + "(" +
            Data._ID + " integer primary key autoincrement," +
            Data.COLUMN_DATE + " text," +
            Data.COLUMN_DURATION_TIME + " text," +
            Data.COLUMN_TASK_NAME + " text," +
            Data.COLUMN_TASK_STATUS + " text"
            + ")";

    private Context mContext;

    public DataBaseHelper(Context context) {
        super(context, Data.SQLITE_NAME, null, Data.DB_VERSON);
        mContext = context;
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
