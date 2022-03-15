package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zaimus.api.model.AttendanceResponseModel;
import com.zaimus.api.model.DeviceIDResponseModel;
import com.zaimus.api.RetrofitAPI;
import com.zaimus.gps.GpsLocationService;
import com.zaimus.gps.GpsUtility;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.CustomProgressBar;
import com.zaimus.manager.UtilityMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VKCSplash extends Activity {
    String flag;
    Context context;
    LocationTrack locationTrack;
    static String latitude, longitude, placename = "";
    ArrayList<String> listEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        flag = AppPreferenceManager.getFlag(context);
       // int size = listEmpty.size();
        new GpsUtility(this).turnGPSOn(new GpsUtility.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                //   Log.e("Gps",String.valueOf(isGPSEnable));
            }
        });

        if ((int) Build.VERSION.SDK_INT >= 23) {
            TedPermission.with(context)
                    .setPermissionListener(permissionVidyoCalllistener)
                    .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA)
                    .check();
        }


      /*  } else {
            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 2 seconds
                        sleep(1000);

                        // After 5 seconds redirect to another intent
                        if (flag.equals("1")) {

                       *//* if (AppPreferenceManager.getAttendance(context)) {

                            Intent i = new Intent(VKCSplash.this, VKCAppActivity.class);
                            startActivity(i);

                        } else {*//*
                            Intent i = new Intent(VKCSplash.this, AttendanceActivity.class);
                            startActivity(i);
                            //  }
*//*
                        Intent i=new Intent(VKCSplash.this,VKCAppActivity.class);
                        startActivity(i);*//*
                        } else if (flag.equals("0")) {
                            Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                            startActivity(i);
                        }

                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                }
            };

            // start thread
            background.start();
        }*/
        // scheduleJob();

    }

    PermissionListener permissionVidyoCalllistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            // splash();

            locationTrack = new LocationTrack(VKCSplash.this);
            if (locationTrack.canGetLocation()) {
                try {
                    Intent i = new Intent(VKCSplash.this,
                            GpsLocationService.class);
                    startService(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gpsalertbox("GPS Alert", "Please enable your location.."
                );


            }
            latitude = String.valueOf(locationTrack.getLatitude());
            longitude = String.valueOf(locationTrack.getLongitude());

            if (latitude.equals("0.0")) {

                placename = getAddress(10.9067713, 77.0119241);

            } else {
                placename = getAddress(locationTrack.getLatitude(), locationTrack.getLongitude());
            }
            // if (AppPreferenceManager.getAttendanceId(context).length() > 0) {

            if (UtilityMethods.isNetworkConnected(context)) {

              /*  if (placename.equals("")) {
                    gpsalertbox("GPS Alert", "Please enable your location.."
                    );
                } else {*/

                    if (AppPreferenceManager.getDeviceID(context).equals("") && !AppPreferenceManager.getUserId(context).equals("")) {
                        getDeviceID();
                    } else if (!AppPreferenceManager.getUserId(context).equals("")) {
                        callAttendanceAPI();
                    } else {
                        if (flag.equals("1")) {
                            finish();

                            Intent i = new Intent(VKCSplash.this, VKCAppActivity.class);
                            startActivity(i);
                            overridePendingTransition(0, 0);

                        } else if (flag.equals("0")) {
                            finish();

                            Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(0, 0);

                        } else {
                            finish();

                            Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(0, 0);

                        }


                    }

              //  }
            } else {
                Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };


    protected void gpsalertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                /*
                                 * Intent intent = new Intent(
                                 * Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                 * context.startActivity(intent);
                                 */
                                //finish();
                            }
                        }).show();
    }

    public String getAddress(double lat, double lng) {//

        String place = "";
        Geocoder geocoder = new Geocoder(VKCSplash.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses.size() > 0) {

                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                place = obj.getLocality();

                //  Log.v("IGA", "Address" + add);
            } else {
                Toast.makeText(this, "Please check your gps connection", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return place;
    }

    public void callAttendanceAPI() {

        final CustomProgressBar progress = new CustomProgressBar(context, R.drawable.loading);
        progress.show();
        RetrofitAPI.getClient().getAttendance(AppPreferenceManager.getUserId(context), "status", AppPreferenceManager.getAttendanceId(context), "5.01", "77.06", "Test", "Test").enqueue(new Callback<AttendanceResponseModel>() {

            @Override
            public void onResponse(Call<AttendanceResponseModel> call, Response<AttendanceResponseModel> response) {
                String status = response.body().getResponse();
                if (status.equalsIgnoreCase("punch_in")) {
                    AppPreferenceManager.saveAttendance(true, context);
                    progress.dismiss();
                    if (flag.equals("1")) {
                        finish();
                        Intent i = new Intent(VKCSplash.this, VKCAppActivity.class);
                        startActivity(i);

                    } else if (flag.equals("0")) {
                        finish();
                        Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                        startActivity(i);
                    } else {
                        finish();
                        Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                        startActivity(i);
                    }


                } else {
                    progress.dismiss();
                    AppPreferenceManager.saveAttendance(false, context);
                    if (flag.equals("1")) {
                        finish();
                        Intent i = new Intent(VKCSplash.this, AttendanceActivity.class);
                        startActivity(i);

                    } else if (flag.equals("0")) {
                        finish();
                        Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                        startActivity(i);
                    } else {
                        finish();
                        Intent i = new Intent(VKCSplash.this, VKCLoginActivity.class);
                        startActivity(i);
                    }


                }

            }

            @Override
            public void onFailure(Call<AttendanceResponseModel> call, Throwable t) {
                progress.dismiss();

            }
        });


    }


    public void getDeviceID() {


        RetrofitAPI.getClient().getDeviceID(AppPreferenceManager.getUserId(context)).enqueue(new Callback<DeviceIDResponseModel>() {

            @Override
            public void onResponse(Call<DeviceIDResponseModel> call, Response<DeviceIDResponseModel> response) {
                String status = response.body().getResponse();
                if (status.equalsIgnoreCase("Success")) {
                    AppPreferenceManager.saveDeviceID(response.body().getDeviceid(), context);
                    callAttendanceAPI();

                }

            }

            @Override
            public void onFailure(Call<DeviceIDResponseModel> call, Throwable t) {


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}


