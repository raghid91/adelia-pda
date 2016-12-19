package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class aDBController extends SQLiteOpenHelper
{
    /* Define the database and tables */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "adeliaPDA";
    private static final String DATABASE_TABLE1 = "todo_list";
    private static final String DATABASE_TABLE2 = "time_log";
    private static final String DATABASE_TABLE3 = "expense_list";
    private static final String DATABASE_TABLE4 = "exercise_log";

    /* Define To Do List table */
    private static final String DATABASE_TABLE1_ID = "_id";
    private static final String DATABASE_TABLE1_NAME = "item_name";
    private static final String DATABASE_TABLE1_DATE = "item_date";
    private static final String DATABASE_TABLE1_PATH = "uri_path";
    private static final String DATABASE_TABLE1_CHECK = "check_box";

    /* Define Time Log table */
    private static final String DATABASE_TABLE2_ID = "_id";
    private static final String DATABASE_TABLE2_NAME = "task_name";
    private static final String DATABASE_TABLE2_DESCRIPTION = "task_description";
    private static final String DATABASE_TABLE2_FROMTIME = "from_time";
    private static final String DATABASE_TABLE2_TOTIME = "to_time";
    private static final String DATABASE_TABLE2_LATITUDE = "latitude";
    private static final String DATABASE_TABLE2_LONGITUDE = "longitude";
    private static final String DATABASE_TABLE2_DATE = "date";
    private static final String DATABASE_TABLE2_CHECK = "check_box";

    /* Define Expense List table */
    private static final String DATABASE_TABLE3_ID = "_id";
    private static final String DATABASE_TABLE3_REFERENCE = "reference";
    private static final String DATABASE_TABLE3_DESCRIPTION = "description";
    private static final String DATABASE_TABLE3_CATEGORY = "category";
    private static final String DATABASE_TABLE3_AMOUNT = "amount";
    private static final String DATABASE_TABLE3_DATE = "item_date";
    private static final String DATABASE_TABLE3_CHECK = "check_box";

    /* Define Exercise Log table */
    private static final String DATABASE_TABLE4_ID = "_id";
    private static final String DATABASE_TABLE4_EXERCISENAME = "exercise_name";
    private static final String DATABASE_TABLE4_STATISTIC = "statistic";
    private static final String DATABASE_TABLE4_DATE = "date";
    private static final String DATABASE_TABLE4_LATITUDE = "latitude";
    private static final String DATABASE_TABLE4_LONGITUDE = "longitude";
    private static final String DATABASE_TABLE4_CHECK = "check_box";

    /* Create SQL queries to create tables */
    private static final String CREATE_TABLE1 = "CREATE TABLE "
            + DATABASE_TABLE1 + " (" + DATABASE_TABLE1_ID
            + " integer primary key autoincrement, " + DATABASE_TABLE1_NAME
            + " text not null, " + DATABASE_TABLE1_DATE
            + " text not null, " + DATABASE_TABLE1_PATH
            + " text, " + DATABASE_TABLE1_CHECK
            + " integer not null);";

    private static final String CREATE_TABLE2 = "create table "
            + DATABASE_TABLE2 + " (" + DATABASE_TABLE2_ID
            + " integer primary key autoincrement, " + DATABASE_TABLE2_NAME
            + " text not null, " + DATABASE_TABLE2_DESCRIPTION
            + " text not null, " + DATABASE_TABLE2_FROMTIME
            + " text not null, " + DATABASE_TABLE2_TOTIME
            + " text not null, " + DATABASE_TABLE2_LATITUDE
            + " real not null, " + DATABASE_TABLE2_LONGITUDE
            + " real not null, " + DATABASE_TABLE2_DATE
            + " text not null, " + DATABASE_TABLE2_CHECK
            + " integer not null);";

    private static final String CREATE_TABLE3 = "create table "
            + DATABASE_TABLE3 + " (" + DATABASE_TABLE3_ID
            + " integer primary key autoincrement, " + DATABASE_TABLE3_REFERENCE
            + " text not null, " + DATABASE_TABLE3_DESCRIPTION
            + " text not null, " + DATABASE_TABLE3_CATEGORY
            + " text not null, " + DATABASE_TABLE3_AMOUNT
            + " real not null, " + DATABASE_TABLE3_DATE
            + " text not null, " + DATABASE_TABLE3_CHECK
            + " integer not null);";

    private static final String CREATE_TABLE4 = "create table "
            + DATABASE_TABLE4 + " (" + DATABASE_TABLE4_ID
            + " integer primary key autoincrement, " + DATABASE_TABLE4_EXERCISENAME
            + " text not null, " + DATABASE_TABLE4_STATISTIC
            + " integer not null, " + DATABASE_TABLE4_DATE
            + " text not null, " + DATABASE_TABLE4_LATITUDE
            + " real not null, " + DATABASE_TABLE4_LONGITUDE
            + " real not null, " + DATABASE_TABLE4_CHECK
            + " integer not null);";

    /* Initialize constructor */
    public aDBController(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Create new database */
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_TABLE1);
        database.execSQL(CREATE_TABLE2);
        database.execSQL(CREATE_TABLE3);
        database.execSQL(CREATE_TABLE4);
    }

    /* Update existing database */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE1 + "\n" + "DROP TABLE IF EXISTS " + DATABASE_TABLE2
                + "\n" + "DROP TABLE IF EXISTS " + DATABASE_TABLE3 + "\n" + "DROP TABLE IF EXISTS " + DATABASE_TABLE4);
        onCreate(database);
        /* Log upgrade information */
        Log.w(aDBController.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
    }
}
