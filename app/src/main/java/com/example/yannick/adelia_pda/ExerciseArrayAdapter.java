package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExerciseArrayAdapter extends ArrayAdapter<aModel_Exercise>
{

    /* Initialize Variables */
    private ArrayList<aModel_Exercise> listItems;
    Context theContext;

    /* Initialize constructor */
    public ExerciseArrayAdapter(Context theContext, int theListID, ArrayList<aModel_Exercise> items)
    {
        super(theContext, theListID, items);
        this.listItems = items;
        this.theContext = theContext;
    }

    /* Create ViewHolder to keep track of checked CheckBox when scrolling */
    public class ViewHolder
    {
        TextView nameView;
        TextView statView;
        TextView dateView;
        TextView latitudeView;
        TextView longitudeView;
        Button viewMap;
        CheckBox itemChk;
    }

    /* Get information for each Time Log item */
    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder;

        /* If ListView is empty, inflate empty view */
        if (view == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_exercise_items, null);

            holder.nameView = (TextView)view.findViewById(R.id.exercise_list_name);
            holder.statView = (TextView)view.findViewById(R.id.exercise_list_statistic);
            holder.dateView = (TextView)view.findViewById(R.id.exercise_list_recordDate);
            holder.latitudeView = (TextView)view.findViewById(R.id.exercise_list_latitude);
            holder.longitudeView = (TextView)view.findViewById(R.id.exercise_list_longitude);
            holder.viewMap = (Button) view.findViewById(R.id.exercise_list_viewMap);
            holder.itemChk = (CheckBox)view.findViewById(R.id.exercise_list_recordChk);
            view.setTag(holder);
        }

        else
        {
            /* Get existing view */
            holder = (ViewHolder) view.getTag();
        }

        if (listItems != null)
        {
            String latitudeText = "Latitude: " + String.format("%f", listItems.get(position).getLatitude());
            String longitudeText = "Longitude: " + String.format("%f", listItems.get(position).getLongitude());
            holder.nameView.setText(listItems.get(position).getExerciseName());
            holder.statView.setText(String.format("%d", listItems.get(position).getStatistic()));
            holder.dateView.setText(listItems.get(position).getDate());
            holder.latitudeView.setText(latitudeText);
            holder.longitudeView.setText(longitudeText);
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
            /* Listen to View Map button */
            holder.viewMap.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View buttonView)
                {
                    if (isNetworkAvailable())
                    {
                        Intent intent = new Intent (theContext, ExerciseGoogleMap.class);
                        intent.putExtra("latitude", listItems.get(position).getLatitude());
                        intent.putExtra("longitude", listItems.get(position).getLongitude());
                        theContext.startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(getContext(), "Sorry: there's a problem with the internet connection!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }

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

    /* Check for network connection */
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
