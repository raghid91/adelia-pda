package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class aModel_ToDoList
{
    private String itemName;
    private String itemDate;
    private String uriPath;
    private boolean check;

    /* Initialize Constructor */
    public aModel_ToDoList() { }
    public aModel_ToDoList(String itemName, String uriPath)
    {
        Date date = new Date();
        DateFormat fDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CANADA);
        this.itemName = itemName;
        itemDate = fDate.format(date);
        this.uriPath = uriPath;
        check = false;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public void setItemDate(String itemDate)
    {
        this.itemDate = itemDate;
    }

    public void setUriPath(String uriPath)
    {
        this.uriPath = uriPath;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    /* Send values to Main Program */
    public String getItemName()
    {
        return itemName;
    }

    public String getItemDate()
    {
        return itemDate;
    }

    public String getUriPath()
    {
        return uriPath;
    }

    public boolean getCheck()
    {
        return check;
    }
}