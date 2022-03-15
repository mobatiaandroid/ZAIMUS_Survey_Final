package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zaimus.SQLiteServices.DatabaseHelper;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.ConnectionManager;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.UsrValues.SdcardManager;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.api.model.DeviceIDResponseModel;
import com.zaimus.api.RetrofitAPI;
import com.zaimus.api.VkcApis;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.ConnectivityReceiver.ConnectivityReceiverListener;
import com.zaimus.manager.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VKCAppActivity extends Activity implements ConnectivityReceiverListener, LocationListener {

    private int MainLayout = R.layout.main;
    private ConstraintLayout takeSurveyButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout addUserButton;
    private ConstraintLayout claims;
    private ConstraintLayout toursPlan;
    private ConstraintLayout image_attendance;

    private ImageView logout;
    protected Dialog mSplashDialog;
    private ProgressBar downloadprogress;
    private TextView txtviewProgress, userName, surName;
    private SQLiteAdapter mySQLiteAdapter;
    private Context context;
    private Boolean PROCESS_START_FLAG = false;
    Intent intent;
    DatabaseHelper databaseHelper;
    String user, survayname, userId, roleId;
    Typeface typeface;
    TextView textViewAdduser, textViewSettings, textViewTakeSurvey,
            textviewtoursplan, textviewClaims, textVersion, textviewAttendance;
    public ArrayList<Survey> surveylist = new ArrayList<Survey>();
    private LocationManager mLocManager;
    private boolean isGpsEnabled;
    private boolean isNetworkEnabled;
    private boolean isPassiveNetworkEnabled;
    private Location location;
    private double latitude;
    private double longitude;
    Context mContext;
    int noOfSurvey = 0;
    private SQLiteAdapter mySQLiteAdaptersSurvey;
    LocationTrack locationTrack;

    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        survayname = AppPreferenceManager.getSurveyName(context);
        locationTrack = new LocationTrack(VKCAppActivity.this);
        mContext = this;
        noOfSurvey = 0;
        /*calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

        time = simpleDateFormat.format(calander.getTime());
       // time.toUpperCase();
        if (time.equals("12:22:00 PM")) {
            Toast.makeText(mContext, "Time :" + time, Toast.LENGTH_LONG).show();
        }*/
        /*
         * if (AppPreferenceManager.getSurveyName(context).equals("")) { Intent
         * intent = new Intent(this, SurveySetActivity.class);
         * startActivity(intent); }
         */
        setContentView(R.layout.acticity_main);
        user = AppPreferenceManager.getUsername(context);
        databaseHelper = new DatabaseHelper(this);

        // Utils.jsonForQ_DYNAMIC_SUG(context,"an footwears:100,httfootweears:560");
        // Utils.jsonObjectForQ_DYNAMIC_SUG(context,
        // "an footwears:100,httfootweears:560");

        // SQLiteAdapter mySQLiteAdapter = new
        // SQLiteAdapter(getApplicationContext());
        // mySQLiteAdapter.openToWrite();
        // mySQLiteAdapter.makeEmpty("survey_result");

        // scheduleJob();
        if ((int) Build.VERSION.SDK_INT >= 23) {
            TedPermission.with(mContext)
                    .setPermissionListener(permissionVidyoCalllistener)
                    .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA)
                    .check();
        } else {
            try {
                databaseHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // setupSplash();
            // PreferenceManager.saveUpgrade("yes", context);
            initializeComponents();
            mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
        }

        if (Build.VERSION.SDK_INT >= 29) {
            if (ConnectionManager
                    .checkIntenetConnection(context)) {


                getDeviceID();
            } else {
                Toast.makeText(context,
                        "No internet connection ",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // //Loop question testing
        // $tree_ques =
        // ORM::factory('survey_question')->where('question_tree_id', '=',
        // $member->question_id)->order_by('enum_path')->find_all();

        // loopQuestion.getTreeIdsOrderBy("63");
        // loopQuestion.findallLoopQuestions();
        // loopQuestion.getAlloptionsOfQuestionid("64");
        // //System.out.println(loopQuestion.findQuestionId("73"));

        // //Loop question testing

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // register connection status listener
        // MyApplication.getInstance().setConnectivityListener(this);
        /*
         * if (AppPreferenceManager.getGpsStatus(context)) { intent = new
         * Intent(VKCAppActivity.this, GpsLocationService.class);
         * startService(intent); } else { intent = new
         * Intent(VKCAppActivity.this, GpsLocationService.class);
         * stopService(intent); }
         */
        /*
         * if (gps.isGPSEnabled) { startService( new Intent(VKCAppActivity.this,
         * GpsLocationService.class)); }else{
         * gpsalertbox("GPS Alert","Please enable your location.." );
         * ////System.out.println("No GPS"); }
         */

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

    protected void checkDatabase() {

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                runOnUiThread(new Runnable() {

                    public void run() {
                        // Check database
                        DatabaseHelper myDbHelper = new DatabaseHelper(
                                getApplicationContext());

                        try {
                            myDbHelper.createDataBase();
                        } catch (IOException ioe) {
                            throw new Error("Unable to create database");
                        }
                        try {
                            myDbHelper.openDataBase();
                            myDbHelper.close();
                        } catch (SQLException sqle) {
                            //Log.v("", "Exception in thread");
                            throw sqle;
                        }
                    }
                });
            }
        }.start();

    }

    protected void setupSplash() {
        MyStateSaver data = (MyStateSaver) getLastNonConfigurationInstance();
        if (data != null) {
            // Show splash screen if still loading
            if (data.showSplashScreen) {
                showSplashScreen();
            }
            setContentView(MainLayout);

            // Rebuild your UI with your saved state here
        } else {
            if (UserValues.splash) {
                showSplashScreen();
            }
            setContentView(MainLayout);
            // Do your heavy loading here on a background thread
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        MyStateSaver data = new MyStateSaver();
        // Save your important data here

        if (mSplashDialog != null) {
            data.showSplashScreen = true;
            removeSplashScreen();
            // initializeComponents();
        }
        return data;
    }

    /**
     * Removes the Dialog that displays the splash screen
     */
    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }

    /**
     * Shows the splash screen over the full Activity
     */
    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, android.R.style.Theme_NoTitleBar);
        mSplashDialog.setContentView(R.layout.spalsh_screen);
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();

        // Set Runnable to remove splash screen just in case
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeSplashScreen();
                // initializeComponents();
            }
        }, 3000);
    }

    /*
     * @Override protected void onDestroy() {
     *
     * super.onDestroy(); intent = new
     * Intent(VKCAppActivity.this,GpsLocationService.class);
     * stopService(intent);
     *
     * }
     */

    protected void initializeComponents() {
        typeface = Utils.setFontTypeToArial(context);

        takeSurveyButton = (ConstraintLayout) findViewById(R.id.cl_takesurvey);
        settingsButton = (ConstraintLayout) findViewById(R.id.cl_settings);
        addUserButton = (ConstraintLayout) findViewById(R.id.cl_add_customer);
        toursPlan = (ConstraintLayout) findViewById(R.id.cl_tourPlan);
        image_attendance = (ConstraintLayout) findViewById(R.id.cl_attendance);

        claims = (ConstraintLayout) findViewById(R.id.cl_claims);
        downloadprogress = (ProgressBar) findViewById(R.id.progressBar1);
        txtviewProgress = (TextView) findViewById(R.id.txtview_progress);
        userName = (TextView) findViewById(R.id.userTextView);
        surName = (TextView) findViewById(R.id.surveyTextView);
        logout = (ImageView) findViewById(R.id.logoutbtn);
        textViewAdduser = (TextView) findViewById(R.id.textviewAddUser);
        textViewSettings = (TextView) findViewById(R.id.textviewSettings);
        textViewTakeSurvey = (TextView) findViewById(R.id.textviewSurvey);
        textviewClaims = (TextView) findViewById(R.id.textviewClaims);
        textviewtoursplan = (TextView) findViewById(R.id.textviewtoursplan);
        textviewAttendance = (TextView) findViewById(R.id.textviewAttendance);


        textVersion = (TextView) findViewById(R.id.textVersion);
        textVersion.setTypeface(typeface);

        userName.setText( user.toUpperCase());
        if(survayname.length()>0) {
            surName.setVisibility(View.VISIBLE);
            surName.setText(survayname + " Survey selected");
        }
        else
        {
            surName.setVisibility(View.GONE);
        }
        textVersion.setText("V_" + getVersion());

        /*if (locationTrack.canGetLocation()) {
            try {
                startService(new Intent(VKCAppActivity.this,
                        GpsLocationService.class));
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Crash----------->");
            }
        } else {
            gpsalertbox("GPS Alert", "Please enable your location..");
            // //System.out.println("No GPS");
        }*/

        takeSurveyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                resetHomeIcon();
                takeSurveyButton.setBackground(getResources().getDrawable(R.drawable.light_pink));
                if (AppPreferenceManager.getUserStatus(context).equals("1")) {
                    alertbox("Error",
                            "No surveyset found.Please import your customer data!!");

                } else {
                    if (SdcardManager.isSDCARDMounted()) {
                        if (PreferenceManager.getUpgrade(
                                getApplicationContext()).equals("yes")) {
                            //Log.v("", "Upgraded already");
                            Intent i = new Intent(getApplicationContext(),
                                    SearchUserActivity.class);
                            startActivity(i);
                        } else {
                            if (ConnectionManager
                                    .checkIntenetConnection(context)) {


                                //System.out.println("Not upgraded");
                                BackgroundAsyncTask ii = new BackgroundAsyncTask();
                                ii.execute();
                            } else {
                                Toast.makeText(context,
                                        "No internet connection ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }// TODO Auto-generated method stub
                    } else {
                        Toast.makeText(context, "Sdcard is  not mounted",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppPreferenceManager.saveFlag("0", context);
                PreferenceManager.saveUpdate("no", context);
                AppPreferenceManager.saveAttendance(false, context);

                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.makeEmpty("survey_customerdetails");
                mySQLiteAdapter.close();
                PreferenceManager.saveCustUpdate("no", context);
                Intent intent = new Intent(VKCAppActivity.this,
                        VKCLoginActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });

        claims.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetHomeIcon();
                claims.setBackground(getResources().getDrawable(R.drawable.light_pink));

                if (AppPreferenceManager.getUserId(context).equals("")
                        && AppPreferenceManager.getSurveyId(context).equals("")) {
                    // Toast.makeText(getApplicationContext(),
                    // "Please import customer data to your device..Then Proceed",
                    // duration)
                    alertbox("Error",
                            "Please import customer data to your device..Then Proceed");
                } else {
                    Intent intent = new Intent(VKCAppActivity.this,
                            ClaimListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        toursPlan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                resetHomeIcon();
                toursPlan.setBackground(getResources().getDrawable(R.drawable.light_pink));

                Intent intent = new Intent(VKCAppActivity.this,
                        TourPlanListingActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        image_attendance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetHomeIcon();
                image_attendance.setBackground(getResources().getDrawable(R.drawable.light_pink));

                Intent i = new Intent(VKCAppActivity.this, AttendanceActivity.class);
                startActivity(i);
            }
        });
        addUserButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                 * Intent i = new Intent(getApplicationContext(),
                 * SurveyListActivity.class); startActivity(i);
                 */
                resetHomeIcon();
                addUserButton.setBackground(getResources().getDrawable(R.drawable.light_pink));

                if (AppPreferenceManager.getUserStatus(context).equals("1")) {
                    alertbox("Error",
                            "No customers in your zone.Please import customers and come back");

                }/*
                 * else
                 * if(AppPreferenceManager.getSurveyName(context).equals("")){
                 * alertbox("Error", "Import customers and come back"); }
                 */ else {
                    mySQLiteAdapter.openToRead();
                    surveylist = mySQLiteAdapter.getAllSurvey();
                    mySQLiteAdapter.close();
                    if (surveylist.size() > 0) {
                        Intent i = new Intent(getApplicationContext(),
                                AddUserActivity.class); //Searchuser
                        startActivity(i);
                    } else {
                        alertbox("Error", "Import customers and come back");

                    }
                }
            }
        });

        settingsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // getServeyResultsLists();
                // getCustomerListsforExport();
                resetHomeIcon();
                settingsButton.setBackground(getResources().getDrawable(R.drawable.light_pink));

                Intent i = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(i);
            }
        });


    }

    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {

        int myProgress;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            PROCESS_START_FLAG = false;
           // setProgressBar(true, "");

         //   txtviewProgress.setText("Starting...");
            PreferenceManager.saveUpgrade("yes", getApplicationContext());
            Intent i = new Intent(getApplicationContext(),
                    SearchUserActivity.class);
            startActivity(i);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /*
             * PROCESS_START_FLAG = true; setProgressBar(false, "");
             * txtviewProgress.setText("Installing..."); myProgress = 0;
             */

            ProgressDialog Dialog = new ProgressDialog(VKCAppActivity.this);
            Dialog.setMessage(VKCAppActivity.this.getResources().getString(
                    R.string.loading));
            Dialog.setCanceledOnTouchOutside(false);
            Dialog.show();
            Dialog.setContentView(R.layout.progress);
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
            try {
                userId = AppPreferenceManager.getUserId(context);
                roleId = AppPreferenceManager.getRoleId(context);
                VkcApis.dump_api(userId, roleId);
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.makeEmpty("survey_customerdetails");
                mySQLiteAdapter.makeEmpty("survey_questionoptions");
                mySQLiteAdapter.makeEmpty("survey_questions");
                mySQLiteAdapter.makeEmpty("survey_result");
                mySQLiteAdapter.makeEmpty("survey_distcts");
                mySQLiteAdapter.makeEmpty("survey_states");
                mySQLiteAdapter.makeEmpty("survey_customercategs");
                mySQLiteAdapter.makeEmpty("survey_customerbtypes");
                mySQLiteAdapter.makeEmpty("survey_sets");
                mySQLiteAdapter.makeEmpty("survey_images");
                mySQLiteAdapter.makeEmpty("survey_result_master");
                mySQLiteAdapter.makeEmpty("survey_multitextoptions");
                mySQLiteAdapter.makeEmpty("question_images");
                mySQLiteAdapter.makeEmpty("tabularSuboptions");
                mySQLiteAdapter.makeEmpty("tabularoptions");
                mySQLiteAdapter.makeEmpty("dealers");
                /* NEW TABLES */
                mySQLiteAdapter.makeEmpty("survey_tourplan");
                mySQLiteAdapter.makeEmpty("survey_location");
                mySQLiteAdapter.makeEmpty("zone_district");

                mySQLiteAdapter.makeEmpty("survey_zones");
                mySQLiteAdapter.makeEmpty("survey_regions");
                mySQLiteAdapter.makeEmpty("survey_result_temp");

                File sdCard = Environment.getExternalStorageDirectory();
                File f = new File(sdCard.getAbsolutePath()
                        + "/media_data/dumb.sql");
                FileInputStream fileIS = new FileInputStream(f);
                BufferedReader buf = new BufferedReader(new InputStreamReader(
                        fileIS));
                String readString = new String();

                // just reading each line and pass it on the db insertion
                //Log.v("", "" + SQLiteAdapter.MAX_INSERT_RECORDS);
                while ((readString = buf.readLine()) != null) {
                    try {
                        mySQLiteAdapter.excuteRawQuery(readString.substring(0,
                                readString.length() - 1));
                        myProgress++;
                        publishProgress(myProgress);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        continue;
                    }
                }
                mySQLiteAdapter.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
//            downloadprogress.setProgress(values[0]);
        //    downloadprogress.setMax(SQLiteAdapter.MAX_INSERT_RECORDS);

           /* txtviewProgress.setText("Inserting " + values[0].toString()
                    + " of " + SQLiteAdapter.MAX_INSERT_RECORDS);*/
        }

    }

    private void setProgressBar(boolean enabled, String text) {
        // findViewById(R.id.takeSurveyButton).setEnabled(enabled ? true :
        // false);
        findViewById(R.id.settingsButton).setEnabled(enabled ? true : false);

        findViewById(R.id.progressbar_layout).setVisibility(
                enabled ? View.GONE : View.VISIBLE);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        startActivity(getIntent());
        finish();
        /*
         * Intent intent=getIntent(); startActivity(intent); Intent intent=new
         * Intent(VKCAppActivity.this,VKCAppActivity.class);
         * startActivity(intent);
         */
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        /*
         * if (PROCESS_START_FLAG) { return; }
         */

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        // finish();
        super.onBackPressed();
    }

    protected void alertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).show();
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
                                // finish();
                            }
                        }).show();
    }

    public Location getLocation() {
        try {
            mLocManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGpsEnabled = mLocManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = mLocManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            isPassiveNetworkEnabled = mLocManager
                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            if (isGpsEnabled == false && isNetworkEnabled == false
                    && isPassiveNetworkEnabled == false) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    location = null;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        // return ;
                    }
                    mLocManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0L, (float) 0.0,
                            this);
                    if (mLocManager != null) {
                        location = mLocManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                else if (isGpsEnabled) {
                    location = null;
                    mLocManager
                            .requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 0L,
                                    (float) 0.0, this);
                    if (mLocManager != null) {
                        location = mLocManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                } else {
                    location = null;
                    mLocManager.requestLocationUpdates(
                            LocationManager.PASSIVE_PROVIDER, 0L, (float) 0.0,
                            this);
                    if (mLocManager != null) {
                        location = mLocManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
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
        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0L, (float) 0.0, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        // TODO Auto-generated method stub
//	        showSnack(isConnected);
        showAlertSurvey(isConnected);

    }

    public void getDeviceID() {


        RetrofitAPI.getClient().getDeviceID(AppPreferenceManager.getUserId(context)).enqueue(new Callback<DeviceIDResponseModel>() {

            @Override
            public void onResponse(Call<DeviceIDResponseModel> call, Response<DeviceIDResponseModel> response) {
                String status = response.body().getResponse();
                if (status.equalsIgnoreCase("Success")) {
                    AppPreferenceManager.saveDeviceID(response.body().getDeviceid(), context);

                }

            }

            @Override
            public void onFailure(Call<DeviceIDResponseModel> call, Throwable t) {


            }
        });
    }

    protected void showAlertSurvey(boolean isConnected) {
        if (isConnected) {
            String alerTexts = "";
            mySQLiteAdaptersSurvey = new SQLiteAdapter(context);
            mySQLiteAdaptersSurvey.openToRead();
            noOfSurvey = mySQLiteAdaptersSurvey.getAllCustomerSurveyDistinct();
            mySQLiteAdaptersSurvey.close();
            if (noOfSurvey == 1) {
                alerTexts = noOfSurvey + " survey export pending.";

            } else if (noOfSurvey > 1) {
                alerTexts = noOfSurvey + " surveys export pending.";

            }


            AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
            final AlertDialog ad;
            mAlert.setMessage(alerTexts)
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.stat_notify_error)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                }
                            });
            ad = mAlert.create();

            if (noOfSurvey >= 1) {
                if (ad.isShowing()) {
                    ad.dismiss();
                }

                ad.show();
            } else {
                if (ad.isShowing()) {
                    ad.dismiss();
                }
            }

        }


    }


    PermissionListener permissionVidyoCalllistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
            // splash();
            try {
                databaseHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // setupSplash();
            // PreferenceManager.saveUpgrade("yes", context);
            initializeComponents();
            mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };


    /*private void scheduleJob() {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(
                Context.JOB_SCHEDULER_SERVICE);

        // The JobService that we want to run
        final ComponentName name = new ComponentName(this, TimerService.class);

        // Schedule the job
        final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        // If successfully scheduled, log this thing
        if (result == JobScheduler.RESULT_SUCCESS) Log.d(TAG, "Scheduled job successfully!");

    }

    private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
        final long interval = TimeUnit.MINUTES.toMillis(hour); // run every minute
        final boolean isPersistent = true; // persist through boot
        final int networkType = JobInfo.NETWORK_TYPE_ANY; // Requires some sort of connectivity

        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    // .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPeriodic(1000)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                   // .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .setPeriodic(1000)
                    .build();
        }

        return jobInfo;
    }
*/

    private void resetHomeIcon() {
        takeSurveyButton.setBackground(getResources().getDrawable(R.drawable.light_violet));
        settingsButton.setBackground(getResources().getDrawable(R.drawable.light_violet));
        addUserButton.setBackground(getResources().getDrawable(R.drawable.light_violet));
        toursPlan.setBackground(getResources().getDrawable(R.drawable.light_violet));
        image_attendance.setBackground(getResources().getDrawable(R.drawable.light_violet));
        claims.setBackground(getResources().getDrawable(R.drawable.light_violet));
    }
}

/**
 * Simple class for storing important data across config changes
 */

class MyStateSaver {
    public boolean showSplashScreen = false;
    // Your other important fields here
}




