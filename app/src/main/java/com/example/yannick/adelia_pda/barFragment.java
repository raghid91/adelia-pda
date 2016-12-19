package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class barFragment extends Fragment
{
    public String buttonClicked;
    public OnItemSelectedListener listener;

    /* Inflate fragment with buttons and set button action */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bargraph_button, container, false);
        Button previous = (Button) view.findViewById(R.id.previous_btn);
        Button next = (Button) view.findViewById(R.id.next_btn);
        previous.setOnClickListener(ButtonChangeListener);
        next.setOnClickListener(ButtonChangeListener);

        return view;
    }

    /* Interface to be implemented in Main activity */
    public interface OnItemSelectedListener
    {
        void onButtonChangeListener(String button);
    }

    /* Create OnClickListener object to detect button clicked */
    private OnClickListener ButtonChangeListener = new OnClickListener()
    {
        public void onClick(View view)
        {
            buttonClicked = view.getContentDescription().toString();
            sendButtonClicked();
        }
    };

    /* Method to trigger action in Main activity from button clicked */
    public void sendButtonClicked()
    {
        if (listener != null)
        {
            listener.onButtonChangeListener(buttonClicked);
        }
    }

    /* Check that Fragment is attached when Main activity is started */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            if (context instanceof OnItemSelectedListener)
            {
                listener = (OnItemSelectedListener) context;
            }

            else
            {
                throw new ClassCastException(context.toString() + " must implement barFragment.OnItemSelectedListener");
            }
        }

        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement barFragment.OnItemSelectedListener");
        }
    }

    /* Cater for API before version 23 */
    @Deprecated
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            if (activity instanceof OnItemSelectedListener)
            {
                listener = (OnItemSelectedListener) activity;
            }

            else
            {
                throw new ClassCastException(activity.toString() + " must implement barFragment.OnItemSelectedListener");
            }
        }

        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement barFragment.OnItemSelectedListener");
        }
    }
}
