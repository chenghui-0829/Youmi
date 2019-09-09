package com.liangzhiyang.youmi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class LocationUtils {

    private Context context;
    private WeakReference<Activity> activity;
    public String mCity = "";

    public LocationUtils (Context context, Activity activity) {
        this.activity = new WeakReference<>(activity);
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public void startLocation() {
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) activity.get().getSystemService(serviceName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {
        String latLongString;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Geocoder geocoder = new Geocoder(context);
            List places = null;

            try {
                places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
//                Toast.makeText(TestActivity.this, places.size()+"", Toast.LENGTH_LONG).show();
                System.out.println(places.size() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String placename = "";
            if (places != null && places.size() > 0) {
                mCity = ((Address) places.get(0)).getLocality();
                //一下的信息将会具体到某条街
                //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
                placename = ((Address) places.get(0)).getAddressLine(0) + ", " + System.getProperty("line.separator")
                        + ((Address) places.get(0)).getAddressLine(1) + ", "
                        + ((Address) places.get(0)).getAddressLine(2);
            }

            latLongString = "纬度:" + lat + "/n经度:" + lng + "   " + mCity;
            Log.e("Tag","==============   " + latLongString);
        } else {
            latLongString = "无法获取地理信息";
        }
    }
}
