package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TodoArrayAdapter extends ArrayAdapter<aModel_ToDoList>
{
    /* Initialize Variables */
    private ArrayList<aModel_ToDoList> listItems;
    Context theContext;

    /* Initialize constructor */
    public TodoArrayAdapter(Context theContext, int theListID, ArrayList<aModel_ToDoList> items)
    {
        super(theContext, theListID, items);
        this.listItems = items;
        this.theContext = theContext;
    }

    /* Create ViewHolder to keep track of checked CheckBox when scrolling */
    public class ViewHolder
    {
        TextView nameView;
        TextView dateView;
        Button viewPhoto;
        CheckBox itemChk;
    }

    /* Get information for each TO DO LIST item */
    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder;

        /* If ListView is empty, inflate empty view */
        if (view == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_todolist_items, null);

            holder.nameView = (TextView) view.findViewById(R.id.todo_list_itemName);
            holder.dateView = (TextView) view.findViewById(R.id.todo_list_itemDate);
            holder.viewPhoto = (Button) view.findViewById(R.id.todo_list_viewPhoto);
            holder.itemChk = (CheckBox) view.findViewById(R.id.todo_list_itemChk);
            view.setTag(holder);
        }

        else
        {
            /* Get existing view */
            holder = (ViewHolder) view.getTag();
        }

        if (listItems != null)
        {
            holder.nameView.setText(listItems.get(position).getItemName());
            holder.dateView.setText(listItems.get(position).getItemDate());
            /* Set checkbox status from boolean array */
            holder.itemChk.setChecked(listItems.get(position).getCheck());
            /* Listen to any change in status of checkbox */
            holder.itemChk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View buttonView)
                {
                    if (((CheckBox) buttonView).isChecked())
                    {
                        setStatus(true, position);
                    }

                    else
                    {
                        setStatus(false, position);
                    }
                }
            });
            /* Listen to View Photo button */
            holder.viewPhoto.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View buttonView)
                {
                    if (listItems.get(position).getCheck())
                    {
                        Toast.makeText(getContext(), "To Do item is checked: cannot view photo!", Toast.LENGTH_LONG).show();
                    }

                    else if (listItems.get(position).getUriPath().equals("none"))
                    {
                        Toast.makeText(getContext(), "To Do item does not hold a photo!", Toast.LENGTH_LONG).show();
                    }

                    else if (!listItems.get(position).getUriPath().equals("none") && !listItems.get(position).getCheck())
                    {
                        /* Show photo in pop up window */
                        LayoutInflater layoutInflater = LayoutInflater.from(theContext);
                        View promptView = layoutInflater.inflate(R.layout.dialog_show_photo_todolist, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(theContext);
                        alertDialogBuilder.setView(promptView);

                        final ImageView image = (ImageView) promptView.findViewById(R.id.todo_showPhoto);
                        /* Get path from To Do Item and display image in pop up window */
                        Bitmap bitmap = BitmapFactory.decodeFile(listItems.get(position).getUriPath(), resizePhoto());
                        image.setImageBitmap(bitmap);
                        /* Setup a dialog window */
                        alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener()
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
            });
        }
        return view;
    }

    /* Set status on checked or unchecked CheckBox of item */
    public void setStatus(boolean status, int position)
    {
        if (status)
        {
            listItems.get(position).setCheck(true);
        }

        else
        {
            listItems.get(position).setCheck(false);
        }
    }

    /* Resize photo to save memory space */
    public BitmapFactory.Options resizePhoto()
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Math.max(5, 5);
        return options;
    }
}