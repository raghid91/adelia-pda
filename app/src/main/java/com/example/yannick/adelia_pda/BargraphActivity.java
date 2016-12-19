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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BargraphActivity extends AppCompatActivity implements barFragment.OnItemSelectedListener
{
    /* Initialize variables */
    private TextView title;
    LinearLayout barView;
    String getTitle;
    BarChart chart;
    Bundle getBundle;
    String[] barChartDate;
    String[] barChartStat;
    int getCount;
    int getPreviousCount;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bargraph);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        /* Initialize Text Views */
        title = (TextView) findViewById(R.id.bargraph_Title);
        barView = (LinearLayout) findViewById(R.id.bargraphView);
        try
        {
            /* Get data from Exercise List*/
            getBundle = getIntent().getExtras();
            getTitle = getBundle.getString("title");
            getCount = getBundle.getInt("count");
            barChartDate = getBundle.getStringArray("arrayDate");
            barChartStat = getBundle.getStringArray("arrayStat");
            count = 0;
            getPreviousCount = 0;

        /* Create new Bar Graph */
            chart = new BarChart(this);
        /* Send Data to Bar Graph */
            nextData();
        }

        catch (NullPointerException e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate graph!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("title", getTitle);
        outState.putInt("count", getCount);
        outState.putStringArray("arrayDate", barChartDate);
        outState.putStringArray("arrayStat", barChartStat);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        {
            getTitle = savedInstanceState.getString("title");
            getCount = savedInstanceState.getInt("count");
            barChartDate = savedInstanceState.getStringArray("arrayDate");
            barChartStat = savedInstanceState.getStringArray("arrayStat");
            chart = new BarChart(this);
            nextData();
        }
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
        finish();
    }

    protected void onStart()
    {
        super.onStart();
        title.setText(getTitle);
        /* Display Bar Graph */
        barView.addView(chart, 0);
    }

    /* Listen to buttons click from Button & Report Fragments */
    public void onButtonChangeListener(String buttonClicked)
    {
        try
        {
            /* If NEXT button is clicked, show next 4 items if any */
            if (buttonClicked.equals("NEXT"))
            {
                nextData();
                chart.invalidate();
            }

        /* If PREVIOUS button is clicked, show previous 4 items if any */
            else
            {
                previousData();
                chart.invalidate();
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate graph!", Toast.LENGTH_LONG).show();
        }
    }

    /* Display more data */
    private void nextData()
    {
        try
        {
            if (count >= getCount)
            {
                Toast.makeText(this, "No more data to display!", Toast.LENGTH_LONG).show();
            }

            else
            {
                if (count < getCount)
                {
                    chart.setStat1(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                }

                if (count >= getCount)
                {
                    chart.setStat2("", 0);
                }

                else
                {
                    chart.setStat2(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                }

                if (count >= getCount)
                {
                    chart.setStat3("", 0);
                }

                else
                {
                    chart.setStat3(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                }

                if (count >= getCount)
                {
                    chart.setStat4("", 0);
                }

                else
                {
                    chart.setStat4(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                }
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate graph!", Toast.LENGTH_LONG).show();
        }
    }

    private void previousData()
    {
        try
        {
            if (count - 5 < 0)
            {
                Toast.makeText(this, "You are at beginning of statistic!", Toast.LENGTH_LONG).show();
            }

            else
            {
                if (getPreviousCount == 0)
                {
                    count = (count - (count % 4)) - 4;
                    getPreviousCount = count;
                    chart.setStat1(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat2(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat3(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat4(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                }

                else
                {
                    count = getPreviousCount - 4;
                    chart.setStat1(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat2(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat3(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    chart.setStat4(barChartDate[count], Integer.parseInt(barChartStat[count]));
                    count++;
                    getPreviousCount -= 4;
                }
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate graph!", Toast.LENGTH_LONG).show();
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
                Intent switchHelp = new Intent(BargraphActivity.this, HelpActivity.class);
                switchHelp.putExtra("mode", 6);
                startActivity(switchHelp);
                return true;
            case R.id.menu_exit:
                /* End activity and quit application */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BargraphActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(BargraphActivity.this, HomeActivity.class);
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
                Intent settings = new Intent(BargraphActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
