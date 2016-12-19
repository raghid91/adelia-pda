package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
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

public class ExpenseActivity extends AppCompatActivity implements buttonsFragment.OnItemSelectedListener, reportFragment.OnItemSelectedListener
{
    /* Initialize Variables */
    ArrayList<aModel_Expense> list;
    ExpenseArrayAdapter adapter;
    ListView listView;
    aDBControllerDAO database;
    SoundPool sound;
    int soundID;
    int soundStreamID;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        list = new ArrayList<>();
        database = new aDBControllerDAO(this);
        sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        soundStreamID = 0;
        soundID = sound.load(this, R.raw.cashsample, soundStreamID);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /* Fetch list from database */
        try
        {
            database.open();
            list = database.getExpenseItems();
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
        /* Bind adapterView to ListView with data from database */
        listView = (ListView) findViewById(R.id.expense_list);
        adapter = new ExpenseArrayAdapter(this, R.layout.activity_expense_items, list);
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
            database.updateExpenseItems(list);
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
    }

    /* Listen to buttons click from Button & Report Fragments */
    public void onButtonChangeListener(String buttonClicked)
    {
        try
        {
            if (buttonClicked.equals("add"))
            {
                /* Add new item in the Expense List*/
                /* Launch Dialog box to get user input */
                LayoutInflater layoutInflater = LayoutInflater.from(ExpenseActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_add_expense, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText reference = (EditText) promptView.findViewById(R.id.expense_reference);
                final EditText description = (EditText) promptView.findViewById(R.id.expense_description);
                final EditText category = (EditText) promptView.findViewById(R.id.expense_category);
                final EditText amount = (EditText) promptView.findViewById(R.id.expense_amount);
                /* Variable used to change StreamID so that sound can be used multiple times */
                stopSound();

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setPositiveButton("Add Expense", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        reference.setText(reference.getText());
                        description.setText(description.getText());
                        category.setText(category.getText());
                        amount.setText(amount.getText());
                        if (reference.getText().toString().isEmpty() || description.getText().toString().isEmpty() || category.getText().toString().isEmpty() || amount.getText().toString().isEmpty())
                        {
                            displayError(1);
                        }

                        else if (reference.getText().toString().length() > 60)
                        {
                            displayError(2);
                        }

                        else if (description.getText().toString().length() > 120)
                        {
                            displayError(2);
                        }

                        else if (category.getText().toString().length() > 30)
                        {
                            displayError(2);
                        }

                        else if (!amount.getText().toString().matches("\\-{0,1}\\d+\\.{0,1}\\d{0,2}"))
                        {
                            displayError(5);
                        }

                        else
                        {
                            aModel_Expense itemList = new aModel_Expense(reference.getText().toString(), description.getText().toString(), category.getText().toString(), Double.parseDouble(amount.getText().toString()));
                            list.add(itemList);
                            /* Start playing sound */
                            getSound();
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.expense_deleteTitle).setPositiveButton("Delete Expense", new DialogInterface.OnClickListener()
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
                /* Calculate Total Amount spent for a category */
                if (list.isEmpty())
                {
                    displayError(3);
                }

                else
                {
                    LayoutInflater layoutInflater = LayoutInflater.from(ExpenseActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.dialog_report_expense, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);
                    alertDialogBuilder.setView(promptView);

                    final EditText report = (EditText) promptView.findViewById(R.id.expense_report);

                    /* Setup a dialog window */
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Report Expenses", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            report.setText(report.getText());
                            String reportItem = report.getText().toString();
                            getReport(reportItem, list);
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

    private void getSound()
    {
        try
        {
            sound.play(soundID, 1, audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), 0, 1f);
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot load sound file!", Toast.LENGTH_LONG).show();
        }
    }

    private void stopSound()
    {
        sound.stop(soundID);
        soundID = sound.load(this, R.raw.cashsample, ++soundStreamID);
    }

    /* Compute Expense Report */
    public void getReport(String category, ArrayList<aModel_Expense> list)
    {
        String title;
        try
        {
            double count = 0;
            double totalCount = 0;
            String text;
            for (aModel_Expense reportItem : list)
            {
                totalCount += reportItem.getAmount();

                if (reportItem.getCategory().equals(category))
                {
                    count += reportItem.getAmount();
                }
            }

            /* Give report */
            if (!category.equals("") && count == 0)
            {
                 /* Display error message for nonexistence of category entered */
                displayError(4);
            }

            else
            {
                if (count != 0)
                {
                    text = "The total amount spent under " + category + " is $" + String.format("%.2f", count) + ".";
                    title = category;
                }

                else
                {
                    text = "The total amount spent under all categories is $" + String.format("%.2f", totalCount) + ".";
                    title = "All categories";
                }

                Intent switchReport = new Intent(ExpenseActivity.this, ReportActivity.class);
                switchReport.putExtra("title", title);
                switchReport.putExtra("text", text);
                startActivity(switchReport);
            }
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot generate report!", Toast.LENGTH_LONG).show();
        }
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
            Toast.makeText(this, "Error: Expense category does not exist!", Toast.LENGTH_LONG).show();
        }

        else if (i == 5)
        {
            Toast.makeText(this, "Error: Amount entered in wrong format", Toast.LENGTH_LONG).show();
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
                Intent switchHelp = new Intent(ExpenseActivity.this, HelpActivity.class);
                switchHelp.putExtra("mode", 2);
                startActivity(switchHelp);
                return true;
            case R.id.menu_exit:
                /* End activity and quit application */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(ExpenseActivity.this, HomeActivity.class);
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
                Intent settings = new Intent(ExpenseActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}