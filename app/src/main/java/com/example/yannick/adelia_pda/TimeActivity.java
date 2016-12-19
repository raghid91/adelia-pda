package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TimeActivity extends AppCompatActivity implements buttonsFragment.OnItemSelectedListener, reportFragment.OnItemSelectedListener
{
    /* Initialize Variables */
    private static final int PERMISSION_REQUEST_CODE = 1;
    ArrayList<aModel_Timelog> list;
    TimelogArrayAdapter adapter;
    ListView listView;
    aDBControllerDAO database;
    LocatePosition gps;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelog);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        list = new ArrayList<>();
        database = new aDBControllerDAO(this);
        /* Request runtime permission to use Location parameters */
        requestPermission();
        /* Create new GPS object */
        gps = new LocatePosition(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /* Fetch list from database */
        try
        {
            database.open();
            list = database.getTimeLogItems();
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
        /* Bind adapterView to ListView with data from database */
        listView = (ListView) findViewById(R.id.timelog_list);
        adapter = new TimelogArrayAdapter(this, R.layout.activity_timelog_items, list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        try
        {
            database.open();
            database.updateTimeLogItems(list);
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        /* Check if GPS Location is on or off */
        LocationManager checkGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try
        {
            gps_enabled = checkGPS.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        catch(Exception e)
        {
            Toast.makeText(this, "Error on checking GPS enabled!", Toast.LENGTH_LONG).show();
        }

        try
        {
            network_enabled = checkGPS.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }

        catch(Exception e)
        {
            Toast.makeText(this, "Error on checking Network enabled!", Toast.LENGTH_LONG).show();
        }

        /* If GPS is off, ask user to turn it on */
        if(!gps_enabled && !network_enabled)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeActivity.this);

            /* Setup a dialog window */
            alertDialogBuilder.setCancelable(false).setMessage(R.string.exercise_enableGps).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
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

        else
        {
            if (gps.getGPS())
            {
                if (gps.getLatitude() != 0)
                {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }

                else
                {
                    /* Cannot obtain GPS coordinates */
                    Toast.makeText(this, "Sorry: cannot obtain GPS coordinates", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /* Request permission from user to use Location parameters */
    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(this, "Allow GPS function for this app. Change it in the Settings/App section!", Toast.LENGTH_LONG).show();
        }

        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    /* Listen to buttons click from Button & Report Fragments */
    public void onButtonChangeListener(String buttonClicked)
    {
        try
        {
            if (buttonClicked.equals("add"))
            {
                /* Add new item in the Time Log List*/
                /* Launch Dialog box to get user input */
                LayoutInflater layoutInflater = LayoutInflater.from(TimeActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_add_timelog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText taskName = (EditText) promptView.findViewById(R.id.timelog_taskName);
                final EditText taskDescription = (EditText) promptView.findViewById(R.id.timelog_taskDescription);
                final EditText fromTime = (EditText) promptView.findViewById(R.id.timelog_fromTime);
                final EditText toTime = (EditText) promptView.findViewById(R.id.timelog_toTime);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setPositiveButton("Add Time Log", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        taskName.setText(taskName.getText());
                        taskDescription.setText(taskDescription.getText());
                        fromTime.setText(fromTime.getText());
                        toTime.setText(toTime.getText());
                        if (taskName.getText().toString().isEmpty() || taskDescription.getText().toString().isEmpty() || fromTime.getText().toString().isEmpty() || toTime.getText().toString().isEmpty())
                        {
                            displayError(1);
                        }

                        else if (taskName.getText().toString().length() > 60)
                        {
                            displayError(2);
                        }

                        else if (taskDescription.getText().toString().length() > 120)
                        {
                            displayError(2);
                        }

                        else if (!validateTime(fromTime.getText().toString()) || !validateTime(toTime.getText().toString()))
                        {
                            displayError(5);
                        }

                        else
                        {
                            aModel_Timelog itemList = new aModel_Timelog(taskName.getText().toString(), taskDescription.getText().toString(), fromTime.getText().toString(), toTime.getText().toString(), latitude, longitude);
                            list.add(itemList);
                            adapter.notifyDataSetChanged();
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

            else if (buttonClicked.equals("delete"))
            {
                /* Delete all items in list which are checked */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.timelog_deleteTitle).setPositiveButton("Delete Time Log", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        try
                        {
                            if (!list.isEmpty())
                            {
                                int traverse = list.size();
                                for (int x = traverse - 1; x >= 0; x--)
                                {
                                    if (list.get(x).getCheck())
                                    {
                                        list.remove(x);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            else
                            {
                                displayError(3);
                            }
                        }

                        catch (Exception e)
                        {
                            displayError(4);
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

            else if (buttonClicked.equals("report"))
            {
                /* Calculate time taken of a given task */
                if (list.isEmpty())
                {
                    displayError(3);
                }

                else
                {
                    LayoutInflater layoutInflater = LayoutInflater.from(TimeActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.dialog_report_timelog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeActivity.this);
                    alertDialogBuilder.setView(promptView);

                    final EditText report = (EditText) promptView.findViewById(R.id.timelog_report);

                    /* Setup a dialog window */
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Report Time Log", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            report.setText(report.getText());
                            if (report.getText().toString().isEmpty())
                            {
                                displayError(1);
                            }

                            else
                            {
                                String reportItem = report.getText().toString();
                                getReport(reportItem, list);
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
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot add information!", Toast.LENGTH_LONG).show();
        }
    }

    /* Compute Time Log Report */
    public void getReport(String item, ArrayList<aModel_Timelog> list)
    {
        try
        {
            int count = 0;
            for (aModel_Timelog reportItem : list)
            {
                if (reportItem.getTaskName().equals(item))
                {
                    /* Calculate fromTime in minutes */
                    String[] fromTokens = reportItem.getFromTime().split(":");
                    int fromHours = Integer.parseInt(fromTokens[0]);
                    int fromMinutes = Integer.parseInt(fromTokens[1]);
                    int fromDuration = (60 * fromHours) + fromMinutes;

                    /* Calculate toTime in minutes */
                    String[] toTokens = reportItem.getToTime().split(":");
                    int toHours = Integer.parseInt(toTokens[0]);
                    int toMinutes = Integer.parseInt(toTokens[1]);
                    int toDuration = (60 * toHours) + toMinutes;

                    count += Math.abs(toDuration - fromDuration);
                }
            }

            /* If Task Name exists give report */
            if (count != 0)
            {
                int hours = count / 60;
                int minutes = count % 60;
                String text = "The total duration for " + item + " is " + hours + " h and " + minutes + " minutes.";
                Intent switchReport = new Intent(TimeActivity.this, ReportActivity.class);
                switchReport.putExtra("title", item);
                switchReport.putExtra("text", text);
                startActivity(switchReport);
            }

            else
            {
                displayError(4);
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate report!", Toast.LENGTH_LONG).show();
        }
    }

    /* Validate time entered */
    private boolean validateTime(String time)
    {
        return (time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"));
    }

    /* Display error messages from adding or deleting items in list */
    private void displayError(int i)
    {
        if (i == 1)
        {
            Toast.makeText(this, "Error: Text cannot be empty!", Toast.LENGTH_LONG).show();
        }

        else if (i == 2)
        {
            Toast.makeText(this, "Error: Text is too long!", Toast.LENGTH_LONG).show();
        }

        else if (i == 3)
        {
            Toast.makeText(this, "Error: List is empty!", Toast.LENGTH_LONG).show();
        }

        else if (i == 4)
        {
            Toast.makeText(this, "Error: Time Log does not exist or result is 0!", Toast.LENGTH_LONG).show();
        }

        else if (i == 5)
        {
            Toast.makeText(this, "Error: Time entered in wrong format", Toast.LENGTH_LONG).show();
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
                /* Switch to Help screen */
                Intent switchHelp = new Intent(TimeActivity.this, HelpActivity.class);
                switchHelp.putExtra("mode", 1);
                startActivity(switchHelp);
                return true;
            case R.id.menu_exit:
                /* End activity and quit application */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(TimeActivity.this, HomeActivity.class);
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
                Intent settings = new Intent(TimeActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}