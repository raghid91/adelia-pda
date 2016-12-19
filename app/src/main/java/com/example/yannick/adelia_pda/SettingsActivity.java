package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class SettingsActivity extends AppCompatActivity implements OnClickListener
{
    /* Initialize variables */
    ArrayAdapter spinnerList;
    Spinner spinner;
    aDBControllerDAO database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        /* Listen to buttons */
        findViewById(R.id.settings_clearDb).setOnClickListener(this);
        findViewById(R.id.settings_clearThisTable).setOnClickListener(this);
        /* Set custom layout for spinner */
        spinner = (Spinner) findViewById(R.id.table_list);
        spinnerList = ArrayAdapter.createFromResource(this, R.array.table_list, R.layout.activity_settings_spinner);
        spinner.setAdapter(spinnerList);
        database = new aDBControllerDAO(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /*  When a button is clicked, clear records in database or in a particular table */
    public void onClick (View view)
    {
        /* Detect which button is clicked */
        String buttonPressed = view.getContentDescription().toString();

        /* Clearing Database */
        if (buttonPressed.equals("ClearAll"))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);

            /* Setup a dialog window */
            alertDialogBuilder.setCancelable(false).setMessage(R.string.settings_databaseConfirm).setPositiveButton("Clear Database", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    try
                    {
                        database.open();
                        database.clearDatabase();
                        database.close();
                        /* Delete also all photos taken */
                        removePhotos();
                        Toast.makeText(getApplicationContext(), "Database cleared!", Toast.LENGTH_SHORT).show();
                    }

                    catch (SQLException e)
                    {
                        Toast.makeText(getApplicationContext(), "Sorry: cannot connect to database!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

            /* Create the alert dialog */
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        /* Clear a particular table in database */
        else if (buttonPressed.equals("Clear this table"))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);

            /* Setup a dialog window */
            alertDialogBuilder.setCancelable(false).setMessage(R.string.settings_tableConfirm).setPositiveButton("Clear Table", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    switch(String.valueOf(spinner.getSelectedItem()))
                    {
                        case ("1. To Do List"):
                            try
                            {
                                database.open();
                                database.clearTable1();
                                database.close();
                                /* Delete also all photos taken */
                                removePhotos();
                                Toast.makeText(getApplicationContext(), "To Do List cleared!", Toast.LENGTH_SHORT).show();
                            }

                            catch (SQLException e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry: cannot connect to database!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ("2. Time Log"):
                            try
                            {
                                database.open();
                                database.clearTable2();
                                database.close();
                                Toast.makeText(getApplicationContext(), "Time Log cleared!", Toast.LENGTH_SHORT).show();
                            }

                            catch (SQLException e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry: cannot connect to database!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ("3. Expenses List"):
                            try
                            {
                                database.open();
                                database.clearTable3();
                                database.close();
                                Toast.makeText(getApplicationContext(), "Expense List cleared!", Toast.LENGTH_SHORT).show();
                            }

                            catch (SQLException e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry: cannot connect to database!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ("4. Exercise Log"):
                            try
                            {
                                database.open();
                                database.clearTable4();
                                database.close();
                                Toast.makeText(getApplicationContext(), "Exercise log cleared!", Toast.LENGTH_SHORT).show();
                            }

                            catch (SQLException e)
                            {
                                Toast.makeText(getApplicationContext(), "Sorry: cannot connect to database!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "To Do List cleared!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

            /* Create the alert dialog */
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    /* Remove any photos taken */
    public void removePhotos()
    {
        /* Delete also all photos taken */
        File directory = getExternalCacheDir();
        String[] photos;
        if (directory != null)
        {
            photos = directory.list();
            for (String item : photos)
            {
                File photo = new File(directory, item);
                photo.delete();
            }
        }
    }

    /* Inflate Action Bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    /* Initialize Menu actions */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_help:
                /* Already in Help screen */
                return true;
            case R.id.menu_exit:
                /* End activity and quit application */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);

                 /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(SettingsActivity.this, HomeActivity.class);
                        ExitApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ExitApp.putExtra("exit", true);
                        startActivity(ExitApp);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

                /* Create the alert dialog */
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            case R.id.menu_settings:
                /* Switch to Settings screen */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
