package com.nrlm.melasalesoffline.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class LocationMasterUtils implements LocationListener {
    Context mContext;
    AppUtility appUtility;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGPSTrackingEnabled = false;

    Location location;
    public double latitude;
    public double longitude;

    int geocoderMaxResults = 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;

    protected LocationManager locationManager;
    private String provider_info;


    public static LocationMasterUtils locationMasterUtils = null;
    public static LocationMasterUtils getInstance(Context context) {
        if (locationMasterUtils == null)
            locationMasterUtils = new LocationMasterUtils(context);
        return locationMasterUtils;
    }

    public LocationMasterUtils(Context context) {
        this.mContext = context;
        appUtility =AppUtility.getInstance();
    }

    public String getLocation() {
        String latLong ="00.00";
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isNetworkEnabled) {
                this.isGPSTrackingEnabled = true;
                appUtility.showLog("Application use GPS Service",LocationMasterUtils.class);

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */

                provider_info = LocationManager.NETWORK_PROVIDER;

            } else if (isGPSEnabled) { // Try to get location if you Network Service is enabled
                this.isGPSTrackingEnabled = true;
                appUtility.showLog("Application use Network State to get GPS coordinates",LocationMasterUtils.class);

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                provider_info = LocationManager.GPS_PROVIDER;

            }

            // Application can use GPS or Network Provider
            if (ContextCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
            if (!provider_info.isEmpty()) {
                locationManager.requestLocationUpdates(
                        provider_info,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                );

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    if (location != null) {
                        this.latitude = location.getLatitude();
                        this.longitude = location.getLongitude();
                        latLong = ""+latitude+","+longitude;
                    }
                }
            }
        } catch (Exception e) {
            appUtility.showLog("Impossible to connect to LocationManager:- "+e,LocationMasterUtils.class);
        }
        return latLong;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
