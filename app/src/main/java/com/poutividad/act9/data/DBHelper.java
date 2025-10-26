package com.poutividad.act9.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "company.db";
    private static final int DB_VERSION = 2; // <<<< VERSION INCREMENTED

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.EmployeeEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This will drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.EmployeeEntry.TABLE_NAME);
        onCreate(db);
    }
}
