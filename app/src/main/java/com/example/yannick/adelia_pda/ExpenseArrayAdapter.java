package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseArrayAdapter extends ArrayAdapter<aModel_Expense>
{
    /* Initialize Variables */
    private ArrayList<aModel_Expense> listItems;

    /* Initialize constructor */
    public ExpenseArrayAdapter(Context theContext, int theListID, ArrayList<aModel_Expense> items)
    {
        super(theContext, theListID, items);
        this.listItems = items;
    }

    /* Create ViewHolder to keep track of checked CheckBox when scrolling */
    public class ViewHolder
    {
        TextView referenceView;
        TextView descriptionView;
        TextView categoryView;
        TextView dateView;
        TextView amountView;
        CheckBox itemChk;
    }

    /* Get information for each Expense item */
    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        ViewHolder holder;

        /* If ListView is empty, inflate empty view */
        if (view == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_expense_items, null);

            holder.referenceView = (TextView)view.findViewById(R.id.expense_list_reference);
            holder.descriptionView = (TextView)view.findViewById(R.id.expense_list_Description);
            holder.categoryView = (TextView)view.findViewById(R.id.expense_list_category);
            holder.dateView = (TextView)view.findViewById(R.id.expense_list_recordDate);
            holder.amountView = (TextView)view.findViewById(R.id.expense_list_amount);
            holder.itemChk = (CheckBox)view.findViewById(R.id.expense_list_recordChk);
            view.setTag(holder);
        }

        else
        {
            /* Get existing view */
            holder = (ViewHolder) view.getTag();
        }

        if (listItems != null)
        {
            String amount = "$" + String.format("%.2f", listItems.get(position).getAmount());
            String category = "Category: " + listItems.get(position).getCategory();
            holder.referenceView.setText(listItems.get(position).getReference());
            holder.descriptionView.setText(listItems.get(position).getDescription());
            holder.categoryView.setText(category);
            holder.dateView.setText(listItems.get(position).getDate());
            holder.amountView.setText(amount);
            /* Set checkbox status from boolean array */
            holder.itemChk.setChecked(listItems.get(position).getCheck());
            /* Listen to any change in status of checkbox */
            holder.itemChk.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View buttonView)
                {
                    if (((CheckBox)buttonView).isChecked())
                    {
                        setStatus(true, position);
                    }

                    else
                    {
                        setStatus(false, position);
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
}
