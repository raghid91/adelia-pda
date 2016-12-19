package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class aDBControllerDAO
{
    /* Initialize variables */
    private SQLiteDatabase database;
    private aDBController controller;

    /* Initialize Constructor */
    public aDBControllerDAO(Context context)
    {
        controller = new aDBController(context);
    }

    /* Open connection with database */
    public void open() throws SQLException
    {
        database = controller.getWritableDatabase();
    }

    /* Remove all records in database */
    public void clearDatabase()
    {
        database.delete("todo_list", null, new String[]{});
        database.delete("time_log", null, new String[]{});
        database.delete("expense_list", null, new String[]{});
        database.delete("exercise_log", null, new String[]{});
    }

    /* Remove all records from a particular table */
    public void clearTable1()
    {
        database.delete("todo_list", null, new String[]{});
    }

    public void clearTable2()
    {
        database.delete("time_log", null, new String[]{});
    }

    public void clearTable3()
    {
        database.delete("expense_list", null, new String[]{});
    }

    public void clearTable4()
    {
        database.delete("exercise_log", null, new String[]{});
    }

    /* Close connection with database */
    public void close()
    {
        database.close();
    }

    /***********************************************************************************************************************/
    /*                                         1. Updating To Do List table                                                */
    /***********************************************************************************************************************/

    /* Update To Do table */
    public void updateToDoItems(ArrayList<aModel_ToDoList> list)
    {
        if (list.size() == 0)
        {
            database.delete("todo_list", null, new String[]{});
        }

        else
        {
            ContentValues values = new ContentValues();
            database.delete("todo_list", null, new String[]{});
            for (aModel_ToDoList item : list)
            {
                values.put("item_name", item.getItemName());
                values.put("item_date", item.getItemDate());
                values.put("uri_path", item.getUriPath());
                if (item.getCheck())
                {
                    values.put("check_box", 1);
                }

                else
                {
                    values.put("check_box", 0);
                }

                database.insert("todo_list", null, values);
            }
        }
    }

    /* Get all records from To Do table */
    public ArrayList<aModel_ToDoList> getToDoItems()
    {
        ArrayList<aModel_ToDoList> list = new ArrayList<>();
        String query = "SELECT * FROM todo_list";
        Cursor cursor = database.rawQuery(query, null);
        /* If table is not empty */
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                aModel_ToDoList item = new aModel_ToDoList();
                item.setItemName(cursor.getString(1));
                item.setItemDate(cursor.getString(2));
                item.setUriPath(cursor.getString(3));
                if (cursor.getInt(4) == 0)
                {
                    item.setCheck(false);
                }

                else
                {
                    item.setCheck(true);
                }
                list.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    /***********************************************************************************************************************/
    /*                                          2. Updating Time Log table                                                 */
    /***********************************************************************************************************************/

    /* Update Time Log table */
    public void updateTimeLogItems(ArrayList<aModel_Timelog> list)
    {
        if (list.size() == 0)
        {
            database.delete("time_log", null, new String[]{});
        }

        else
        {
            ContentValues values = new ContentValues();
            database.delete("time_log", null, new String[]{});
            for (aModel_Timelog item : list)
            {
                values.put("task_name", item.getTaskName());
                values.put("task_description", item.getTaskDescription());
                values.put("from_time", item.getFromTime());
                values.put("to_time", item.getToTime());
                values.put("latitude", item.getLatitude());
                values.put("longitude", item.getLongitude());
                values.put("date", item.getDate());
                if (item.getCheck())
                {
                    values.put("check_box", 1);
                }

                else
                {
                    values.put("check_box", 0);
                }

                database.insert("time_log", null, values);
            }
        }
    }

    /* Get all records from Time Log table */
    public ArrayList<aModel_Timelog> getTimeLogItems()
    {
        ArrayList<aModel_Timelog> list = new ArrayList<>();
        String query = "SELECT * FROM time_log";
        Cursor cursor = database.rawQuery(query, null);
        /* If table is not empty */
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                aModel_Timelog item = new aModel_Timelog();
                item.setTaskName(cursor.getString(1));
                item.setTaskDescription(cursor.getString(2));
                item.setFromTime(cursor.getString(3));
                item.setToTime(cursor.getString(4));
                item.setLatitude(cursor.getDouble(5));
                item.setLongitude(cursor.getDouble(6));
                item.setDate(cursor.getString(7));
                if (cursor.getInt(8) == 0)
                {
                    item.setCheck(false);
                }

                else
                {
                    item.setCheck(true);
                }
                list.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    /***********************************************************************************************************************/
    /*                                           3. Updating Expense table                                                 */
    /***********************************************************************************************************************/

    /* Update Expense table */
    public void updateExpenseItems(ArrayList<aModel_Expense> list)
    {
        if (list.size() == 0)
        {
            database.delete("expense_list", null, new String[]{});
        }

        else
        {
            ContentValues values = new ContentValues();
            database.delete("expense_list", null, new String[]{});
            for (aModel_Expense item : list)
            {
                values.put("reference", item.getReference());
                values.put("description", item.getDescription());
                values.put("category", item.getCategory());
                values.put("amount", item.getAmount());
                values.put("item_date", item.getDate());
                if (item.getCheck())
                {
                    values.put("check_box", 1);
                }

                else
                {
                    values.put("check_box", 0);
                }

                database.insert("expense_list", null, values);
            }
        }
    }

    /* Get all records from Expense table */
    public ArrayList<aModel_Expense> getExpenseItems()
    {
        ArrayList<aModel_Expense> list = new ArrayList<>();
        String query = "SELECT * FROM expense_list";
        Cursor cursor = database.rawQuery(query, null);
        /* If table is not empty */
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                aModel_Expense item = new aModel_Expense();
                item.setReference(cursor.getString(1));
                item.setDescription(cursor.getString(2));
                item.setCategory(cursor.getString(3));
                item.setAmount(cursor.getDouble(4));
                item.setDate(cursor.getString(5));
                if (cursor.getInt(6) == 0)
                {
                    item.setCheck(false);
                }

                else
                {
                    item.setCheck(true);
                }
                list.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    /***********************************************************************************************************************/
    /*                                        4. Updating Exercise Log table                                               */
    /***********************************************************************************************************************/

    /* Update Expense table */
    public void updateExerciseItems(ArrayList<aModel_Exercise> list)
    {
        if (list.size() == 0)
        {
            database.delete("exercise_log", null, new String[]{});
        }

        else
        {
            ContentValues values = new ContentValues();
            database.delete("exercise_log", null, new String[]{});
            for (aModel_Exercise item : list)
            {
                values.put("exercise_name", item.getExerciseName());
                values.put("statistic", item.getStatistic());
                values.put("date", item.getDate());
                values.put("latitude", item.getLatitude());
                values.put("longitude", item.getLongitude());
                if (item.getCheck())
                {
                    values.put("check_box", 1);
                }

                else
                {
                    values.put("check_box", 0);
                }

                database.insert("exercise_log", null, values);
            }
        }
    }

    /* Get all records from Exercise table */
    public ArrayList<aModel_Exercise> getExerciseItems()
    {
        ArrayList<aModel_Exercise> list = new ArrayList<>();
        String query = "SELECT * FROM exercise_log";
        Cursor cursor = database.rawQuery(query, null);
        /* If table is not empty */
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                aModel_Exercise item = new aModel_Exercise();
                item.setExerciseName(cursor.getString(1));
                item.setStatistic(cursor.getInt(2));
                item.setDate(cursor.getString(3));
                item.setLatitude(cursor.getDouble(4));
                item.setLongitude(cursor.getDouble(5));
                if (cursor.getInt(6) == 0)
                {
                    item.setCheck(false);
                }

                else
                {
                    item.setCheck(true);
                }
                list.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }
}
