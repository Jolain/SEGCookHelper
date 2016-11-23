package com.compilers.segcookhelper;

import android.provider.BaseColumns;

/**
 * Created by Jolain Poirier on 11/23/2016.
 */

public class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecipeDatabase.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    private DatabaseContract() {} // This class must not be instantiated.

    // TABLE STRUCTURE

    public static abstract class recipe implements BaseColumns {

    }

    public static abstract class ingredient implements BaseColumns {

    }

    public static abstract class category implements BaseColumns {

    }



}
