package id.my.asmith.rizalapps.utils;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by User on 6/27/2018.
 */

public class LoctListnImpl implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationListenerImpl" , location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationListenerImpl" , "Status Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LocationListenerImpl" , "Provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LocationListenerImpl" , "Provider disabled");
    }
    
}
