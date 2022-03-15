package com.zaimus.gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.zaimus.Profiles.GpsModel;
import com.zaimus.constants.GpsSettings;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.core.app.ActivityCompat;

public class GpsLocationService extends Service implements LocationListener {
    AlarmManager am;
    PendingIntent sender;
    private LocationManager lm;
    String bestProvider;
    private final Context mContext = this;
    Context context = this;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude
    GpsModel gpsmodel;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    //	LocationManage locationmanage;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        //am.cancel(sender);
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }


    @Override
    public void onStart(Intent intent, int startId) {
        //System.out.println("start service ");
        try {
            initializeLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

/////////////////////Auto-generated method stub//////////////////////////
        //System.out.println("start service 1");

        Intent intent1 = new Intent(GpsLocationService.this, RepeatGpsReciever.class);
        sender = PendingIntent.getBroadcast(GpsLocationService.this, 0, intent1, 0);
        //System.out.println("start service 2");

        //////////////////// We want the alarm to go off 30 seconds from now.////
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 2 * 60 * 1000;
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 300000, sender);
        startListening();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //System.out.println("GpsLocation Service on destroy");
        stopListening();
        super.onDestroy();
       // am.cancel(sender);
        //System.out.println("GpsLocation Service on destroy");
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void startListening() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0,
                this
        );

    }

    private void stopListening() {
        if (lm != null)
            lm.removeUpdates(this);
    }


    public void initializeLocation() {
        // //////////////////////From Location Demo ////////////
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0,
                this
        );
        getLocation();
        // ///////CODE FOR FINDING THE LOCATION NAME ,COUNTRY NAME ETC ////
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append("\n");
                GpsSettings.LOCATION = sb.toString();
                sb.append(address.getLocality()).append("\n");
                //gpsmodel=new GpsModel();
                //gpsmodel.getLocation()=GpsSettings.LOCATION;
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                //System.out.println(" location found");

            } else {
                //System.out.println("No location found");
            }
            //System.out.println(sb.toString());
        } catch (IOException e) {
        }
        // ///////CODE FOR FINDING THE LOCATION NAME ,COUNTRY NAME ETC ////

        GpsApiThread gpsapithread = new GpsApiThread(context);
        gpsapithread.start();
    }

    /*
    public void initializeLocationName() {
		// ////////////////From Location Demo ////////////
		// ///////CODE FOR FINDING THE LOCATION NAME ,COUNTRY NAME ETC ////

		Geocoder gc = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(GpsSettings.LATITUDE,
					GpsSettings.LONGITUDE, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
					sb.append(address.getAddressLine(i));
				GpsSettings.LOCATION =sb.toString();
				
			} else {
				//System.out.println("No location found");
			}
			//System.out.println(sb.toString());
		} catch (IOException e) {
		}
		// ///////CODE FOR FINDING THE LOCATION NAME ,COUNTRY NAME ETC ////
	}*/

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        GpsSettings.LATITUDE = location.getLatitude();
        GpsSettings.LONGITUDE = location.getLongitude();
        //initializeLocationName();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public Location getLocation() {
        try {


            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

}
