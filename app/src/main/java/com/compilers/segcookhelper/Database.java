package com.compilers.segcookhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jolain Poirier on 11/23/2016.
 */

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.I_table.CREATE_TABLE);
        db.execSQL(DatabaseContract.C_table.CREATE_TABLE);
        db.execSQL(DatabaseContract.R_table.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If database changes, drop old tables and create new ones
        db.execSQL(DatabaseContract.I_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.C_table.DELETE_TABLE);
        db.execSQL(DatabaseContract.R_table.DELETE_TABLE);
        onCreate(db);
    }

}
