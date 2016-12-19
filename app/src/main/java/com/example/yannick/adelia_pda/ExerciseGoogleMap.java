package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

public class ExerciseGoogleMap extends FragmentActivity implements OnMapReadyCallback
{
    /* Initialize variables */
    GoogleMap googleMapWithMarker;
    Bundle getBundle;
    Double getLatitude;
    Double getLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_googlemap);
        getBundle = getIntent().getExtras();
        getLatitude = getBundle.getDouble("latitude");
        getLongitude = getBundle.getDouble("longitude");
        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        googleMapWithMarker = googleMap;
        if (getLatitude != null && getLongitude != null)
        {
            LatLng exercise = new LatLng(getLatitude, getLongitude);
            try
            {
                /* Style Google Map */
                boolean success = googleMapWithMarker.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

                if (!success)
                {
                    Log.e("MAP_STYLE", "Error: Style file parsing failed!");
                }
            }

            catch (Resources.NotFoundException e)
            {
                Log.e("MAP_STYLE", "Error: Cannot find style file!");
            }

            googleMapWithMarker.addMarker(new MarkerOptions().position(exercise).title("Your exercise was here"));
            googleMapWithMarker.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLatitude, getLongitude), calculateZoomArea(50000, getLatitude)));
        }
    }

    /* Calculate zoom level according to screen size */
    private float calculateZoomArea (double desiredMeters, double latitude)
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        float mapWidth = widthPixels / metrics.scaledDensity;
        double latitudinalAdjustment = Math.cos(Math.PI * latitude / 180.0);
        double arg = 40075004 * mapWidth * latitudinalAdjustment / (desiredMeters * 256.0);

        return (float) (Math.log(arg) / Math.log(2.0));
    }
}
