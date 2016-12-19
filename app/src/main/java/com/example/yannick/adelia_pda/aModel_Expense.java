package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class aModel_Expense
{
    private String reference;
    private String description;
    private String category;
    private double amount;
    private String date;
    private boolean check;

    /* Initialize Constructor */
    public aModel_Expense(){}
    public aModel_Expense(String reference, String description, String category, double amount)
    {
        Date tdate = new Date();
        DateFormat fDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CANADA);
        this.reference = reference;
        this.description = description;
        this.category = category;
        this.amount = amount;
        date = fDate.format(tdate);
        check = false;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    /* Send values to Main Program */
    public String getReference()
    {
        return reference;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCategory()
    {
        return category;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getDate()
    {
        return date;
    }

    public boolean getCheck()
    {
        return check;
    }
}
