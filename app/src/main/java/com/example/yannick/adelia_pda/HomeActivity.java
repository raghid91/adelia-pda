package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements OnClickListener
{
    /* Initialize variables */
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 10;
    private TextView setDate;
    private Date date;
    private DateFormat fDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* Check if QUIT APPLICATION button have been pressed from other activities */
        try
        {
            /* If QUIT button is pressed, quit application */
            boolean exitApp = getIntent().getBooleanExtra("exit", false);
            if (exitApp)
            {
                finish();
                System.exit(0);
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: App not quit!", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_home);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        /* Variable to set current date */
        setDate = (TextView) findViewById(R.id.date);
        date = new Date();
        fDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CANADA);
        /* Start listening to all buttons */
        findViewById(R.id.todo).setOnClickListener(this);
        findViewById(R.id.timelog).setOnClickListener(this);
        findViewById(R.id.expenses).setOnClickListener(this);
        findViewById(R.id.exercise).setOnClickListener(this);
        /* Request permission from user to use phone's storage */
        requestPermission();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        /* Format date to display on Home screen*/
        setDate.setText(fDate.format(date));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Process.killProcess(android.os.Process.myPid());
    }

    /* Listen to buttons clicked */
    public void onClick (View view)
    {
        /* Listen to button clicked */
        String buttonPressed = view.getContentDescription().toString();

        try
        {
            switch (buttonPressed)
            {
                /* TODOList button clicked */
                case "todo":
                    Intent switchToOpt1 = new Intent(HomeActivity.this, TodoActivity.class);
                    startActivity(switchToOpt1);
                    break;
                /* TODOList button clicked */
                case "timelog":
                    Intent switchToOpt2 = new Intent(HomeActivity.this, TimeActivity.class);
                    startActivity(switchToOpt2);
                    break;
                /* TODOList button clicked */
                case "expenses":
                    Intent switchToOpt3 = new Intent(HomeActivity.this, ExpenseActivity.class);
                    startActivity(switchToOpt3);
                    break;
                /* TODOList button clicked */
                case "exercise":
                    Intent switchToOpt4 = new Intent(HomeActivity.this, ExerciseActivity.class);
                    startActivity(switchToOpt4);
                    break;
                /* Default Option */
                default:
                    Intent switchToOpt5 = new Intent(HomeActivity.this, TodoActivity.class);
                    startActivity(switchToOpt5);
                    this.overridePendingTransition(R.anim.transition_in, R.anim.transition_out);
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Wrong action!", Toast.LENGTH_LONG).show();
        }
    }

    /* Request permission from user to use Internal Storage */
    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {

            Toast.makeText(this, "Allow this app to use storage. Change it in the Settings/App section!", Toast.LENGTH_LONG).show();
        }

        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
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
            Intent switchHelp = new Intent(HomeActivity.this, HelpActivity.class);
            switchHelp.putExtra("mode", 5);
            startActivity(switchHelp);
            return true;
        case R.id.menu_exit:
            /* End activity and quit application */
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

            /* Setup a dialog window */
            alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    finish();
                    System.exit(0);
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
            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
