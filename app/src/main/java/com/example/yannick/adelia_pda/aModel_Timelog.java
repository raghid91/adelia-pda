package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class aModel_Timelog
{
    private String taskName;
    private String taskDescription;
    private String fromTime;
    private String toTime;
    private double latitude;
    private double longitude;
    private String date;
    private boolean check;

    /* Initialize Constructor */
    public aModel_Timelog() {}
    public aModel_Timelog(String taskName, String taskDescription, String fromTime, String toTime, double latitude, double longitude)
    {
        Date tdate = new Date();
        DateFormat fDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CANADA);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.latitude = latitude;
        this.longitude = longitude;
        date = fDate.format(tdate);
        check = false;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription)
    {
        this.taskDescription = taskDescription;
    }

    public void setFromTime(String fromTime)
    {
        this.fromTime = fromTime;
    }

    public void setToTime(String toTime)
    {
        this.toTime = toTime;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    /* Send values to Main Program */
    public String getTaskName()
    {
        return taskName;
    }

    public String getTaskDescription()
    {
        return taskDescription;
    }

    public String getFromTime()
    {
        return fromTime;
    }

    public String getToTime()
    {
        return toTime;
    }

    public String getDate()
    {
        return date;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public boolean getCheck()
    {
        return check;
    }
}
