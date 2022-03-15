package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zaimus.api.VkcApis;
import com.zaimus.gps.GpsUtils;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.CustomProgressBar;
import com.zaimus.manager.UtilityMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class AttendanceActivity extends Activity {


    Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    ProgressDialog Dialog;
    Typeface typeface;
    // TextView txtIn;
    // TextView txtOut;
    LocationTrack locationTrack;
    String latitude, longitude, placename;
    String attendance_type, attendance_id;
    SimpleDateFormat simpleDateFormat;
    String user_location;
    Calendar calander;
    EditText edtLocationName;
    CustomProgressBar progress;
    private boolean isContinue = false;
    private boolean isGPS = false;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    ImageView imgAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attendance);
        context = this;
        imgAttendance=findViewById(R.id.imgAttendance);
        //  txtIn = findViewById(R.id.textViewIN);
        // txtOut = findViewById(R.id.textViewOut);
        edtLocationName = findViewById(R.id.editLocationName);
        if ((int) Build.VERSION.SDK_INT >= 23) {
            TedPermission.with(context)
                    .setPermissionListener(permissionVidyoCalllistener)
                    .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA)
                    .check();
        }



       /* locationTrack = new LocationTrack(AttendanceActivity.this);
        if (locationTrack.canGetLocation()) {
            try {
                Intent i = new Intent(AttendanceActivity.this,
                        GpsLocationService.class);
                startService(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gpsalertbox("GPS Alert", "Please enable your location.."
            );

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }*/

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        getLocation();

       /* latitude = String.valueOf(locationTrack.getLatitude());
        longitude = String.valueOf(locationTrack.getLongitude());*/

        /*if (latitude.equals("0.0")) {

            placename = getAddress(10.9067713, 77.0119241);

        } else {
            placename = getAddress(locationTrack.getLatitude(), locationTrack.getLongitude());
        }
*/
        if (AppPreferenceManager.getAttendanceId(context).length() > 0) {
            if (UtilityMethods.isNetworkConnected(context)) {

                placename = getAddress(10.9067713, 77.0119241);
                latitude = String.valueOf(10.9067713);
                longitude = String.valueOf(77.0119241);
                if (placename != null) {
                    gpsalertbox("GPS Alert", "Please enable your location.."
                    );
                } else {
                    attendanceStatus logTask = new attendanceStatus();
                    logTask.execute();
                }
            } else {
                Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();

            }
        }

        if (AppPreferenceManager.getAttendance(context)) {
            /*txtIn.setEnabled(false);
            txtOut.setEnabled(true);
            txtIn.setTextColor(Color.parseColor("#a9a9a9"));
            txtOut.setTextColor(Color.parseColor("#d00000"));

            Drawable top = getResources().getDrawable(R.drawable.login_fade);
            txtIn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            Drawable top1 = getResources().getDrawable(R.drawable.logout);
            txtOut.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);*/
            imgAttendance.setBackgroundResource(R.drawable.out);

        } else {

/*
            txtIn.setEnabled(true);
            txtOut.setEnabled(false);
            txtIn.setTextColor(Color.parseColor("#03925e"));
            txtOut.setTextColor(Color.parseColor("#a9a9a9"));
            Drawable top = getResources().getDrawable(R.drawable.login);
            txtIn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            Drawable top1 = getResources().getDrawable(R.drawable.logout_fade);
            txtOut.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);*/
            imgAttendance.setBackgroundResource(R.drawable.in);

        }
        /*username.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                username.setFocusable(true);
                return false;
            }
        });*/

        imgAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (edtLocationName.getText().toString().trim().length() > 0) {
                    if(AppPreferenceManager.getAttendanceId(context)!=null) {


                        attendance_type = "out";
                        attendance_id = AppPreferenceManager.getAttendanceId(context);
                        if (latitude.equals("0.0")) {
                            latitude = String.valueOf(10.9067713);
                            longitude = String.valueOf(77.0119241);
                            placename = getAddress(10.9067713, 77.0119241);

                        } else {
                            placename = getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        }                // Toast.makeText(AttendanceActivity.this, "Place :" + placename, Toast.LENGTH_LONG).show();

                        if (UtilityMethods.isNetworkConnected(context)) {
                            markAttendance logTask = new markAttendance();
                            logTask.execute();
                        } else {
                            Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        attendance_type = "in";
                        user_location = edtLocationName.getText().toString().trim();
                        attendance_id = "";
                        if (latitude.equals("0.0")) {
                            latitude = String.valueOf(10.9067713);
                            longitude = String.valueOf(77.0119241);
                            placename = getAddress(10.9067713, 77.0119241);

                        } else {
                            placename = getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        }                    // Toast.makeText(AttendanceActivity.this, "Place :" + latitude, Toast.LENGTH_LONG).show();

                        if (placename != null) {

                            if (UtilityMethods.isNetworkConnected(context)) {
                                markAttendance logTask = new markAttendance();
                                logTask.execute();
                            } else {
                                Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }

                } else {
                    UtilityMethods.setErrorForEditText(edtLocationName, "Mandatory Field");
                    edtLocationName.setText("");
                }
            }


        });

  /*      txtOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                attendance_type = "out";
                user_location = edtLocationName.getText().toString().trim();
                attendance_id = AppPreferenceManager.getAttendanceId(context);
                if (latitude.equals("0.0")) {
                    latitude = String.valueOf(10.9067713);
                    longitude = String.valueOf(77.0119241);
                    placename = getAddress(10.9067713, 77.0119241);

                } else {
                    placename = getAddress(Double.parseDouble(latitude), Double.parseDouble(longitude));
                }                // Toast.makeText(AttendanceActivity.this, "Place :" + placename, Toast.LENGTH_LONG).show();

                if (UtilityMethods.isNetworkConnected(context)) {
                    markAttendance logTask = new markAttendance();
                    logTask.execute();
                } else {
                    Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_SHORT).show();

                }


            }


        });*/
    }

    public class markAttendance extends AsyncTask<Void, Integer, Void> {
        JSONObject response;
        String status;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            progress.dismiss();
            status = response.optString("response");
            if (status.equalsIgnoreCase("Success")) {

                String attendance_id_ = response.optString("attendance_id").toString();
                if (attendance_type.equals("out")) {
                    AppPreferenceManager.saveAttendanceId("", context);
                    AppPreferenceManager.saveAttendance(false, context);
                    Toast.makeText(AttendanceActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();

                } else {
                    AppPreferenceManager.saveAttendance(true, context);
                    AppPreferenceManager.saveAttendanceId(attendance_id_, context);
                    Toast.makeText(AttendanceActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                }
                Intent intent = new Intent(AttendanceActivity.this,
                        VKCAppActivity.class);
                startActivity(intent);
                finish();
                // AppPreferenceManager.saveFlag("0", context);
                //  PreferenceManager.saveUpdate("no", context);
               /* SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());

                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.makeEmpty("survey_customerdetails");
                mySQLiteAdapter.close();*/


            } else {

                String message = response.optString("message").toString();
                Toast.makeText(AttendanceActivity.this, "" + message, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            // setProgrees(false);
            /*
             * progress.setVisibility(View.VISIBLE);
             * Toast.makeText(getApplicationContext(), "Connecting server",
             * Toast.LENGTH_SHORT).show();
             */
            /*Dialog = new ProgressDialog(AttendanceActivity.this);
            Dialog.setMessage(AttendanceActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);*/
            super.onPreExecute();

            progress = new CustomProgressBar(context, R.drawable.loading);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // getCustomerListsforExport();
            // getServeyResultsLists();
            try {
                response = VkcApis.user_attendance(AppPreferenceManager.getUserId(context), attendance_type, attendance_id, latitude, longitude, placename, user_location, context);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        if (AppPreferenceManager.getFlag(context).equals("1")) {

            Intent homeIntent = new Intent(AttendanceActivity.this, VKCAppActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        } else {
            Intent homeIntent = new Intent(AttendanceActivity.this, VKCLoginActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    /*	Intent i = new Intent(VKCLoginActivity.this,
                GpsLocationService.class);
		startService(i);*/
        /*startService( new Intent(VKCLoginActivity.this,
                        GpsLocationService.class)
				);*/
    }

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

    private String getVersion() {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return packageinfo.versionName.toString();
    }

    public String getAddress(double lat, double lng) {//

        String place = "";
        Geocoder geocoder = new Geocoder(AttendanceActivity.this, Locale.getDefault());
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

                Log.v("IGA", "Address" + add);
            } else {
                Toast.makeText(this, "Unable to detect the location", Toast.LENGTH_SHORT).show();
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

    PermissionListener permissionVidyoCalllistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            // splash();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };

    public class attendanceStatus extends AsyncTask<Void, Integer, Void> {
        JSONObject response;
        String status;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            progress.dismiss();

            status = response.optString("response");
            if (status.equalsIgnoreCase("punch_out")) {

                AppPreferenceManager.saveAttendanceId("", context);
                AppPreferenceManager.saveAttendance(false, context);
                //  Toast.makeText(AttendanceActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();


                //AppPreferenceManager.saveAttendance(true, context);
                // Toast.makeText(AttendanceActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();


                // AppPreferenceManager.saveFlag("0", context);
                //  PreferenceManager.saveUpdate("no", context);
               /* SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());

                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.makeEmpty("survey_customerdetails");
                mySQLiteAdapter.close();*/


            } else {
                //  Toast.makeText(AttendanceActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                AppPreferenceManager.saveAttendance(true, context);
            }
            if (AppPreferenceManager.getAttendance(context)) {
                /*txtIn.setEnabled(false);
                txtOut.setEnabled(true);
                txtIn.setTextColor(Color.parseColor("#a9a9a9"));
                txtOut.setTextColor(Color.parseColor("#d00000"));

                Drawable top = getResources().getDrawable(R.drawable.login_fade);
                txtIn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                Drawable top1 = getResources().getDrawable(R.drawable.logout);
                txtOut.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);*/

                imgAttendance.setBackgroundResource(R.drawable.out);


            } else {


                /*txtIn.setEnabled(true);
                txtOut.setEnabled(false);
                txtIn.setTextColor(Color.parseColor("#03925e"));
                txtOut.setTextColor(Color.parseColor("#a9a9a9"));
                Drawable top = getResources().getDrawable(R.drawable.login);
                txtIn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                Drawable top1 = getResources().getDrawable(R.drawable.logout_fade);
                txtOut.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);*/
                imgAttendance.setBackgroundResource(R.drawable.in);

            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            // setProgrees(false);
            /*
             * progress.setVisibility(View.VISIBLE);
             * Toast.makeText(getApplicationContext(), "Connecting server",
             * Toast.LENGTH_SHORT).show();
             */
            progress = new CustomProgressBar(context, R.drawable.loading);
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // getCustomerListsforExport();
            // getServeyResultsLists();
            try {
                response = VkcApis.user_attendance(AppPreferenceManager.getUserId(context), "status", AppPreferenceManager.getAttendanceId(context), latitude, longitude, placename, "", context);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(AttendanceActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            // txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            if (ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
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
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(AttendanceActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    latitude = String.valueOf(location.getLatitude());
                                    longitude = String.valueOf(location.getLongitude());
                                } else {
                                    if (ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
