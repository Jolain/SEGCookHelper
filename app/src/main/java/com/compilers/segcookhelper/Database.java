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
        //db.execSQL();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database structure should not change, so no need to implement this method for now.

    }

}
