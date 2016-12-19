package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocatePosition extends Service implements LocationListener
{
    /* Initializes Variable */
    Context context;
    protected LocationManager locationManager;
    Location location;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean getLocation = false;
    double latitude = 0;
    double longitude = 0;

    /* Initialize constructor */
    public LocatePosition(Context context)
    {
        this.context = context;
        /* Detect GPS coordinates if possible */
        getLocation();
    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            /* Getting GPS Status */
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            /* Getting Network Status */
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled)
            {
                /* No network or GPS Detected - set latitude and longitude to 0 */
                latitude = 0;
                longitude = 0;
            }

            else
            {
                getLocation = true;

                /* Get location from Network Provider First */
                if (isNetworkEnabled)
                {
                    try
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    catch (SecurityException e)
                    {
                        Log.e("NETWORK", "Network provider is not detected!");
                    }
                }

                /* If GPS is active, use GPS instead to get location */
               if (isGPSEnabled)
                {
                    try
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    catch (SecurityException e)
                    {
                       Log.e("GPS", "GPS is not enabled!");
                    }
                }
            }
        }

        catch (Exception e)
        {
            Log.e("GPS", "Cannot get any GPS coordinates!");
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        try
        {
            locationManager.removeUpdates(this);
        }

        catch (SecurityException e)
        {
            Log.e("LOCATION", "Cannot remove location update!");
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    /* Check if object can get GPS coordinates */
    public boolean getGPS()
    {
        return getLocation;
    }

    /* Obtain Latitude */
    public double getLatitude()
    {
        return latitude;
    }

    /* Obtain Longitude */
    public double getLongitude()
    {
        return longitude;
    }
}
