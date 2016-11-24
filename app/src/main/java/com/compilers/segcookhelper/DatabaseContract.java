package com.compilers.segcookhelper;

import android.provider.BaseColumns;

/**
 * Created by Jolain Poirier on 11/23/2016.
 *
 * Class file which contains the SQL contract Database.class must follow. Strings are defined
 * to be used with (SQLiteDatabase).execSQL()
 */

public class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecipeDatabase.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    private DatabaseContract() {} // This class must not be instantiated.

    // TABLE STRUCTURE

    public static abstract class R_table implements BaseColumns {
        public static final String TABLE_NAME = "Recipes";
        public static final String COL_NAME = "Name"; // String
        public static final String COL_DESC = "Description"; // String
        public static final String COL_IMG = "LinkedImage"; // Integer

        // SQLite does not support storing arrays. By using arrayToString() and stringToArray(),
        // we can store the ingredient & category array in a string like so:
        // ["Tomato", "Potato"] -> "Tomato__,__Potato"
        public static final String COL_INGREDIENT = "Ingredients"; // String
        public static final String COL_CATEGORY = "Category"; // String

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COL_NAME + TEXT_TYPE + COMMA_SEP +
                COL_DESC + TEXT_TYPE + COMMA_SEP +
                COL_IMG + TEXT_TYPE + COMMA_SEP +
                COL_INGREDIENT + TEXT_TYPE + COMMA_SEP +
                COL_CATEGORY + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class I_table implements BaseColumns {
        public static final String TABLE_NAME = "Ingredients";
        public static final String COL_NAME = "Name";
        // SQLite does not support storing arrays. Explained above @line 29
        public static final String COL_RECIPE = "Recipe";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COL_NAME + TEXT_TYPE + COMMA_SEP +
                COL_RECIPE + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static abstract class C_table implements BaseColumns {
        public static final String TABLE_NAME = "Categories";
        public static final String COL_NAME = "Name";
        // SQLite does not support storing arrays. Explained above @line 29
        public static final String COL_RECIPE = "Recipe";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COL_NAME + TEXT_TYPE + COMMA_SEP +
                COL_RECIPE + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    // Array Conversion Methods

    public String arrayToString(String[] array) {
        String outputString = "";
        for(int i = 0; i < array.length; i++) { // Append each element next to each other with __,__
            outputString = outputString + array[i];
            if(i < array.length - 1) { // Do not append comma to the last element
                outputString = outputString + "__,__";
            }
        }

        return outputString;
    }

    public String[] stringToArray(String s) {
        String[] outputArray = s.split("__,__");
        return outputArray;
    }

}
