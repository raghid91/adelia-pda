package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity implements buttonsFragment.OnItemSelectedListener
{
    /* Initialize Variables */
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 5;
    ArrayList<aModel_ToDoList> list;
    TodoArrayAdapter adapter;
    ListView listView;
    String uriPath;
    aDBControllerDAO database;
    PackageManager packageManager;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        list = new ArrayList<>();
        database = new aDBControllerDAO(this);

        /* Check to see if device has camera */
        packageManager = this.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Toast.makeText(this, "Error: this device has no camera!", Toast.LENGTH_LONG).show();
        }

        else
        {
            /* Request permission to use camera */
            requestPermission();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /* Fetch list from database */
        try
        {
            database.open();
            list = database.getToDoItems();
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
        /* Bind adapterView to ListView with data from database */
        listView = (ListView) findViewById(R.id.todo_list);
        adapter = new TodoArrayAdapter(this, R.layout.activity_todolist_items, list);
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
            database.updateToDoItems(list);
            database.close();
        }

        catch (SQLException e)
        {
            Toast.makeText(this, "Error: cannot connect to database!", Toast.LENGTH_LONG).show();
        }
    }

    /* Listen to buttons click from Button Fragment */
    @Override
    public void onButtonChangeListener(String buttonClicked)
    {
        try
        {
            if (buttonClicked.equals("add"))
            {
                /* Add new item in the TO DO List*/
                /* Launch Dialog box to get user input */
                LayoutInflater layoutInflater = LayoutInflater.from(TodoActivity.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_add_todolist, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.todo_addTitle);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setPositiveButton("Add item", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        uriPath = "none";
                        editText.setText(editText.getText());
                        if (editText.getText().toString().isEmpty())
                        {
                            displayError(1);
                            currentPhotoPath = null;
                        }

                        else if (editText.getText().toString().length() > 60)
                        {
                            displayError(2);
                            currentPhotoPath = null;
                        }

                        else
                        {
                            /* If photo is taken, get its path to store in database */
                            if (currentPhotoPath != null)
                            {
                                uriPath = currentPhotoPath;
                            }
                            aModel_ToDoList itemList = new aModel_ToDoList(editText.getText().toString(), uriPath);
                            list.add(itemList);
                            adapter.notifyDataSetChanged();
                            /* Reinitialize path reference */
                            currentPhotoPath = null;
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.todo_deleteTitle).setPositiveButton("Delete item(s)", new DialogInterface.OnClickListener()
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
        }

        catch (Exception e)
        {
            /* Display error message for unknown error */
            Toast.makeText(this, "Error: Cannot add information!", Toast.LENGTH_LONG).show();
        }
    }

    /* Take a photo to include in To Do item */
    public void takeAPhoto(View view)
    {
        Intent takePhotoIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        File image;
        try
        {
            /* Create an image file for photo to be captured by camera */
            image = createPath();
            /* Send path to camera so that photo is stored in the indicated directory with the given filename */
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(takePhotoIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }

        catch (Exception e)
        {
            Toast.makeText(this, "Error: Image file couldn't be created!", Toast.LENGTH_LONG).show();
            Log.e("FILE", "Error: File couldn't be created!");
            currentPhotoPath = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                /* Confirm photo taken */
                Toast.makeText(this, "Image captured!", Toast.LENGTH_LONG).show();
            }

            else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(this, "Image capture was canceled!", Toast.LENGTH_LONG).show();
                currentPhotoPath = null;
            }

            else
            {
                Toast.makeText(this, "Error: Image capture failed!", Toast.LENGTH_LONG).show();
                currentPhotoPath = null;
            }
        }
    }

    /* Get path of image */
    private File createPath() throws IOException
    {
        File image = null;
        try
        {
            /* Set up name nomenclature of photo file */
            String randomNumber = Integer.toString((int) (Math.random() * 1000)) + Integer.toString((int) (Math.random() * 500)) + Integer.toString((int) (Math.random() * 50));
            String imageFileName = "IMG_" + randomNumber +  "_";
            /* Get directory where to store images */
            File album = getAlbumDirectory();
            image = File.createTempFile(imageFileName, ".jpg", album);
            /* Get absolute path of image */
            currentPhotoPath = image.getAbsolutePath();
        }

        catch (IOException e)
        {
            Toast.makeText(this, "Error: Fail to create file with filename!", Toast.LENGTH_LONG).show();
            Log.e("FILE", "Error: Fail to create file with filename!");
        }
        return image;
    }

    /* Get directory in which photos will be stored */
    private File getAlbumDirectory()
    {
        File albumDirectory =  getExternalCacheDir();
        if (albumDirectory == null)
        {
            Toast.makeText(this, "Failed to create a directory", Toast.LENGTH_LONG).show();
            Log.e("FILE", "Error: Fail to create directory!");
        }

        return albumDirectory;
    }

    /* Display error messages from adding or deleting items in list */
    private void displayError(int i)
    {
        if (i == 1)
        {
            Toast.makeText(this, "Error: title cannot be empty!", Toast.LENGTH_LONG).show();
        }

        else if (i == 2)
        {
            Toast.makeText(this, "Error: title cannot be more than 60 characters!", Toast.LENGTH_LONG).show();
        }

        else if (i == 3)
        {
            Toast.makeText(this, "Error: List is empty!", Toast.LENGTH_LONG).show();
        }

        else if (i == 4)
        {
            Toast.makeText(this, "Error: List item does not exist!", Toast.LENGTH_LONG).show();
        }
    }

    /* Request permission from user to use camera */
    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
        {

            Toast.makeText(this, "Allow this app to use storage. Change it in the Settings/App section!", Toast.LENGTH_LONG).show();
        }

        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
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
                Intent switchHelp = new Intent(TodoActivity.this, HelpActivity.class);
                switchHelp.putExtra("mode", 0);
                startActivity(switchHelp);
                return true;
            case R.id.menu_exit:
                /* End activity and quit application */
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoActivity.this);

                /* Setup a dialog window */
                alertDialogBuilder.setCancelable(false).setMessage(R.string.menu_exit_confirm).setPositiveButton("Exit App", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent ExitApp = new Intent(TodoActivity.this, HomeActivity.class);
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
                Intent settings = new Intent(TodoActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}