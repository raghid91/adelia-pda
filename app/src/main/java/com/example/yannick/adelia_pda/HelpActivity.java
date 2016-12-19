package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity implements OnClickListener
{
    /* Initialize variables */
    private TextView todoView;
    private TextView timelogView;
    private TextView expenseView;
    private TextView exerciseView;
    private int getMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        /* Listen to Back button */
        findViewById(R.id.back_btn).setOnClickListener(this);
        /* Initialize Text Views */
        todoView = (TextView) findViewById(R.id.todo_text);
        timelogView = (TextView) findViewById(R.id.timelog_text);
        expenseView = (TextView) findViewById(R.id.expense_text);
        exerciseView = (TextView) findViewById(R.id.exercise_text);
        getMode = getIntent().getExtras().getInt("mode");
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
    public void onBackPressed()
    {
        safeExit();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    protected void onStart()
    {
        super.onStart();
        /* Detect which Activity has called the Help Screen and display text*/
        switch(getMode)
        {
            case 0:
                todoView.setVisibility(View.VISIBLE);
                break;
            case 1:
                timelogView.setVisibility(View.VISIBLE);
                break;
            case 2:
                expenseView.setVisibility(View.VISIBLE);
                break;
            case 3:
                exerciseView.setVisibility(View.VISIBLE);
                break;
            default:
                /* No Default View */
            /* No Default Text */
        }
    }

    /*  When Back button is clicked, return to previous screen */
    public void onClick (View view)
    {
        safeExit();
    }

    /* Safe exit */
    private void safeExit()
    {
        if (getMode == 6)
        {
            Intent intent = new Intent(HelpActivity.this, ExerciseActivity.class);
            startActivity(intent);
            finish();
        }

        else
        {
            finish();
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(HelpActivity.this, HomeActivity.class);
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
                Intent settings = new Intent(HelpActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}