package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

public class aModel_Exercise
{
    private String exerciseName;
    private int statistic;
    private String date;
    private double latitude;
    private double longitude;
    private boolean check;

    /* Initialize Constructor */
    public aModel_Exercise(){}
    public aModel_Exercise(String exerciseName, String date, int statistic, double latitude, double longitude)
    {
        this.exerciseName = exerciseName;
        this.date = date;
        this.statistic = statistic;
        this.latitude = latitude;
        this.longitude = longitude;
        check = false;
    }

    public void setExerciseName(String exerciseName)
    {
        this.exerciseName = exerciseName;
    }

    public void setStatistic(int statistic)
    {
        this.statistic = statistic;
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
    public String getExerciseName()
    {
        return exerciseName;
    }

    public int getStatistic()
    {
        return statistic;
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
