package com.zaimus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.ConnectionManager;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.UsrValues.SdcardManager;
import com.zaimus.api.IVkcApis;
import com.zaimus.api.VkcApis;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.ConnectivityReceiver.ConnectivityReceiverListener;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class SettingsActivity extends Activity implements
        ConnectivityReceiverListener, OnClickListener {
    private Activity activity;
    private ImageView synchButton;
    // private ImageView setupButton;
    private ImageView btn_export_customer;
    private ImageView btn_gps;
    private SQLiteAdapter mySQLiteAdapter;
    private SQLiteAdapter mySQLiteAdapters;
    private SQLiteAdapter mySQLiteAdaptersSurvey;

    private Context context;
    private Boolean PROCESS_START_FLAG = false;
    private ImageView btnBackup;
    private ImageView btnRestore;
    private ImageView btnSelectSurvey;
    private ImageView btnSycCustomer, btnAttendance, btnSetUp;
    private Intent i;
    ProgressDialog progressDialog;
    Boolean isSuccess = false;
    Boolean isException = false;
    Boolean isCustomerListsforExportFlag;
    String nString;
    ProgressDialog dialog;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon;
    TextView textViewGPS;

    LinearLayout llExportCustomer, llImportdata, llExportSurvey, llGps, llRestore, llBackup, llSyncCustomer, llSelectSurvey;
    Typeface typeface;
    String userId, roleId;
    Survey[] surData;
    Survey survey;
    ArrayList<Survey> cusIdArray = new ArrayList<Survey>();
    ArrayList<Survey> imageList = new ArrayList<Survey>();
    String quesId;
    ProgressDialog pDialog;
    Bitmap bitmap;
    byte[] b;
    String temp;
    int noOfSurvey = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        ////Log.v("", "On create calld");
        context = this;
        activity = this;
        survey = new Survey();
        noOfSurvey = 0;
        mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
        header = (LinearLayout) findViewById(R.id.header);
        typeface = Utils.setFontTypeToArial(context);
        headermanager = new Headermanager(activity, "Settings");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
                /*
                 * Intent intent = new Intent(SettingsActivity.this,
                 * VKCAppActivity.class); startActivity(intent);
                 */
            }
        });
        // if(AppPreferenceManager.getUsername(context).equals(object))
        // alertbox(title, mymessage);

        llExportSurvey = (LinearLayout) findViewById(R.id.llExportSurvey);
        llExportCustomer = (LinearLayout) findViewById(R.id.llExportCustomer);
        // setupButton = (ImageView) findViewById(R.id.bt_setup);
        btn_gps = (ImageView) findViewById(R.id.button_gps);
        llBackup = (LinearLayout) findViewById(R.id.llBackup);
        llRestore = (LinearLayout) findViewById(R.id.llRestore);
        llSelectSurvey = (LinearLayout) findViewById(R.id.llSelectSurvey);
        llSyncCustomer = (LinearLayout) findViewById(R.id.llSyncCustomer);
        // btnAttendance = (ImageView) findViewById(R.id.button_attendance);
        llImportdata = (LinearLayout) findViewById(R.id.llImportData);
        textViewGPS = (TextView) findViewById(R.id.textViewGPS);
        llGps = (LinearLayout) findViewById(R.id.llGps);
        // setupButton.setVisibility(View.GONE);

        llBackup.setOnClickListener(this);
        llRestore.setOnClickListener(this);
        //btnAttendance.setOnClickListener(this);
        // btnAboutDeveloper.setOnClickListener(this);
        /* FOR RESTORE PURPOSE DISABLING ALL OTHER BUTTONS */

        btn_gps.setEnabled(true);
        // setupButton.setEnabled(false);

        /*
         * if (isCustomerListsforExport()) { synchButton.setEnabled(false);
         * //alertbox("Error", "Export Customers First.Then Export Survey!!"); }
         * else { synchButton.setEnabled(true); }
         */

        if (AppPreferenceManager.getGpsStatus(SettingsActivity.this)) {
            // btn_gps.setText(getResources().getString(R.string.GPS_ON));
            // btn_gps.setBackgroundColor(Color.GREEN);
        } else {
            // btn_gps.setBackgroundColor(Color.RED);
            // btn_gps.setText(getResources().getString(R.string.GPS_ON));
        }
        /*
         * if (AppPreferenceManager.getUserStatus(context).equals("1")) { int
         * count=mySQLiteAdapter.getResultCount();
         * //System.out.println("123 "+count);
         *
         * if(count>1){ showDifferentUserAlert(); } }
         */

        llGps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (AppPreferenceManager.getGpsStatus(SettingsActivity.this))
                // {
                // btn_gps.setBackgroundColor(Color.RED);
                // btn_gps.setText(getResources().getString(R.string.GPS_ON));
                /*
                 * Toast.makeText(context,
                 * getResources().getString(R.string.GPS_ON),
                 * Toast.LENGTH_SHORT).show();
                 */
                AppPreferenceManager
                        .saveGpsStatus(false, SettingsActivity.this);
                // startService(new Intent(this, GpsLocationService.class));
                LocationTrack locationTrack = new LocationTrack(SettingsActivity.this);
                if (locationTrack.canGetLocation()) {
                    textViewGPS.setText("GPS ON");
                    /*
                     * Intent intent = new Intent(SettingsActivity.this,
                     * GpsLocationService.class); startService(intent);
                     */
                } else {
                    // gps.showSettingsAlert();
                    gpsalertbox("Enable GPS",
                            "Your GPS seems to be disabled, Go Settings and enable Location? ");
                }

                /*
                 * }else { // btn_gps.setBackgroundColor(Color.GREEN); //
                 * btn_gps.setText(getResources().getString(R.string.GPS_ON));
                 * Toast.makeText(context,
                 * getResources().getString(R.string.GPS_OFF),
                 * Toast.LENGTH_SHORT).show();
                 * AppPreferenceManager.saveGpsStatus(true,
                 * SettingsActivity.this); }
                 */
            }
        });

        llSyncCustomer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
                 * Intent intent = new Intent(SettingsActivity.this,
                 * SyncCustomerService.class); startService(intent);
                 */
                // callAsynchronousTask();
                // call aync task to import newly added customers
                AppPreferenceManager.saveSyncStatus("1", context);
                if (Utils.checkInternetConnection(context)) {
                    showSyncCustomerAlert();
                } else {
                    Toast.makeText(context, "No internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (AppPreferenceManager.getSyncStatus(context).equals("1")) {
            // call aync task to update customers
        }

        llSelectSurvey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
                 * if (AppPreferenceManager.getSurveyId(context).equals("")) {
                 * alertbox("Error",
                 * "Please import customer data and then proceed.."); } else {
                 */
                mySQLiteAdapter.openToRead();
                String[] columns = new String[]{"customer_shopname",
                        "customer_place", "customer_id", "customer_dist",
                        "customer_add1", "customer_state"};
                Cursor cursor = mySQLiteAdapter.queueAll(
                        "survey_customerdetails", columns,
                        "customer_shopname asc", null);
                cursor.moveToPosition(0);
                mySQLiteAdapter.close();
                //	//System.out.println("Count--------->" + cursor.getCount());
                if (cursor.getCount() == 0) {
                    alertbox("Error",
                            "Please import customer data and then proceed..");
                } else {
                    Intent i = new Intent(getApplicationContext(),
                            SearchUserActivity.class);
                    startActivity(i);
                }
                // }
            }
        });
        llImportdata.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (SdcardManager.isSDCARDMounted()) {

                    if (Utils.checkInternetConnection(context)) {
                        //System.out.println("Not upgraded");

                        showImportCustomerAlert();
                    } else {
                        Toast.makeText(context, "No internet connection ",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Sdcard is  not mounted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        llExportSurvey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (Utils.checkInternetConnection(context)) {
                    doGPSSyncAsynchTask gpsSync = new doGPSSyncAsynchTask();
                    gpsSync.execute();

                    //Export Survey Method
                    showExportAlert();
                } else {
                    Toast.makeText(context, "No internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        llExportCustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (ConnectionManager.checkIntenetConnection(context)) {
                    showExportAlert_cutomer();
                } else {
                    Toast.makeText(context, "No internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void showExportAlert() {
        String alerText = "";
        // if (noOfSurvey == 0) {
        mySQLiteAdapters = new SQLiteAdapter(context);
        mySQLiteAdapters.openToRead();
        noOfSurvey = mySQLiteAdapters.getAllCustomerSurveyDistinct();
        mySQLiteAdapters.close();
        if (noOfSurvey == 0) {
            alerText = "No pending surveys to export.";

        } else if (noOfSurvey == 1) {
            alerText = noOfSurvey + " survey export pending.";

        } else {
            alerText = noOfSurvey + " surveys export pending.";

        }
        // }
        new AlertDialog.Builder(this)
                .setMessage(
                        "This requires very good internet connection such as WiFi or 3G.\n\n"
                                + alerText + "\n\nDo you wish to continue ?")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                if (isCustomerListsforExport()) {
                                    Toast.makeText(
                                            context,
                                            "Sorry.Please Export customer data first!!", // Bibin
                                            // if
                                            // unable
                                            // to
                                            // export
                                            // commented
                                            Toast.LENGTH_SHORT).show();
                                    synchButton.setEnabled(true);
                                } else {
                                    doExportAsynchTask doTask = new doExportAsynchTask();
                                    doTask.execute();
                                }
                            }
                        }).show();
    }

    protected void showExportAlert_cutomer() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "This requires very good internet connection such as WiFi or 3G.\n\nDo you wish to continue ?")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                if (isCustomerListsforExport()) {
                                    doExportCustomerAsynchTask doTask = new doExportCustomerAsynchTask();
                                    doTask.execute();
                                } else {
                                    Toast.makeText(
                                            context,
                                            "Sorry! No new customer data found to export!!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
    }

    protected void showSyncCustomerAlert() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "This requires very good internet connection such as WiFi or 3G.\n\nDo you wish to continue ?")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // isCustomerListsforExport();
                                boolean b = isCustomerListsforExport();
                                System.out
                                        .println("isCustomerListsforExport()------->"
                                                + b);
                                if (isCustomerListsforExport()) {
                                    Toast.makeText(
                                            context,
                                            "Sorry! Export customer data first!!",
                                            Toast.LENGTH_SHORT).show();
                                    // synchButton.setEnabled(false);
                                } else {
                                    // async task to sync customer data
                                    // mySQLiteAdapter = new
                                    // SQLiteAdapter(getApplicationContext());

                                    mySQLiteAdapter.openToRead();
                                    cusIdArray = mySQLiteAdapter
                                            .getAllSurveyMax();
                                    mySQLiteAdapter.close();
                                    if (cusIdArray.size() == 0) {
                                        alertbox("Error",
                                                "Please import customer data");
                                    } else {
                                        SyncNewCustomerAsyncTask synTask = new SyncNewCustomerAsyncTask();
                                        synTask.execute();
                                    }

                                }
                            }
                        }).show();
    }

    protected void showAlertMessage() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "This will removes old survey results. This requires very good internet connection such as WiFi or 3G.\nDo you wish to continue ?")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        }).show();
    }

    public class doSetupAsynchTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Setup completed",
                    Toast.LENGTH_SHORT).show();

            //Bibin 26/04/2021
            AppPreferenceManager.saveUserStatus("0", context);
            // AppPreferenceManager.saveSurveySetId(surveysetId, context);
            AppPreferenceManager.getSurveySetId(context);
            PreferenceManager.saveUpgrade("yes", getApplicationContext());
            String[] columns = new String[]{""};
            // listAdapter = new SurveyListAdapter(context, surveylist);
            // surveySet.setAdapter(listAdapter);
            // progress.setVisibility(View.GONE);
            //getImages();
            PROCESS_START_FLAG = false;
            Intent intent = new Intent(SettingsActivity.this,
                    SearchUserActivity.class);
            startActivity(intent);


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            PROCESS_START_FLAG = true;

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(SettingsActivity.this.getResources().getString(
                    R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            // Dialog.setContentView(R.layout.progress);
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                // stateId,surveysetId,userId,roleId
                userId = AppPreferenceManager.getUserId(context);
                roleId = AppPreferenceManager.getRoleId(context);
                // //System.out.println("USER ID---------------->" + userId);
                // //System.out.println("ROLE ID---------------->" + roleId);


            } catch (Exception e) {
                // TODO: handle exception
            }
            // mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
            try {

                mySQLiteAdapter.openToWrite();
                //   //System.out.println("SQl-------------->2");

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
                mySQLiteAdapter.makeEmpty("shop_relevance");
                VkcApis.dump_api(userId, roleId);
                // //System.out.println("SQl-------------->1");

                File sdCard = Environment.getExternalStorageDirectory();
                File f = new File(sdCard.getAbsolutePath()
                        + "/media_data/dumb.sql");
                FileInputStream fileIS = new FileInputStream(f);
                Scanner scanner = new Scanner(f);
                //System.out.println("scanner " + scanner.toString());
                while (scanner.hasNextLine()) {
                    try {
                        mySQLiteAdapter.excuteRawQuery(scanner.nextLine()
                                .replaceAll("\n", "").replaceAll("\r", "")
                                .replaceAll("\t", ""));
                    } catch (Exception e) {

                        continue;
                    }
                }
                scanner.close();
                // BufferedReader buf = new BufferedReader(new
                // InputStreamReader(
                // fileIS));
                // //System.out.println("File Length-------------->" +
                // f.length());
                // String readString = new String();
                // // just reading each line and pass it on the db insertion
                // //Log.v("", "" + SQLiteAdapter.MAX_INSERT_RECORDS);
                //
                // while ((readString = buf.readLine()) != null) {
                // try {
                // mySQLiteAdapter.excuteRawQuery(readString
                // .replaceAll("\n", "").replaceAll("\r", "")
                // .replaceAll("\r\n", "").replaceAll("\t", "")
                // .replaceAll("<br>", ""));
                // } catch (Exception e) {
                // //System.out.println("Exception------->" + e.toString());
                // continue;
                // }
                // }

            } catch (SQLException e) {
                e.printStackTrace();
            } /*catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/ catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            /*//System.out.println("Inserting " + values[0].toString() + " of "
                    + SQLiteAdapter.MAX_INSERT_RECORDS);*/
            mySQLiteAdapter.close();
        }
    }

    public class doExportAsynchTask extends AsyncTask<Void, Integer, Void> {

        private boolean success = false;

        @Override
        protected void onPostExecute(Void result) {


            dialog.dismiss();

            PROCESS_START_FLAG = false;
            if (success) {
                if (noOfSurvey == 0) {
                    // mySQLiteAdapter = new SQLiteAdapter(context);
                    // mySQLiteAdapter.openToRead();
                    // noOfSurvey =
                    // mySQLiteAdapter.getAllCustomerSurveyDistinct();
                    // mySQLiteAdapter.close();
                    Toast.makeText(getApplicationContext(),
                            "Survey uploaded successfully.", Toast.LENGTH_SHORT)
                            .show();
                } else if (noOfSurvey == 1) {
                    Toast.makeText(getApplicationContext(),
                            noOfSurvey + " Survey uploaded successfully.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            noOfSurvey + " Surveys uploaded successfully. ",
                            Toast.LENGTH_LONG).show();
                }

                AppPreferenceManager.saveUserStatus("0", context);
            } else {
                // Toast.makeText(
                // getApplicationContext(),
                // "Export not completed sucessfully. Please try export survey again.",
                // Toast.LENGTH_LONG).show();

                int result_Count;
                mySQLiteAdapter.openToRead();
                // result_Count = mySQLiteAdapter.surveyResultCount();
                result_Count = mySQLiteAdapter.surveyResultCounting();

                mySQLiteAdapter.close();
                //  //System.out.println("result_Count::" + result_Count);
                // Survey Result Count Check
                if (result_Count > 0) {
                    BackupDbExport backup = new BackupDbExport();
                    backup.execute();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Export not completed . Please try export survey again.Data backup failed since there is no pending survey.",
                            Toast.LENGTH_LONG).show();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            PROCESS_START_FLAG = true;

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(SettingsActivity.this.getResources().getString(
                    R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
            success = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mySQLiteAdapter = new SQLiteAdapter(SettingsActivity.this);
                mySQLiteAdapter.openToRead();
                String[] columns = new String[]{"survey_id"};
                String groupBy = "survey_id";

                Cursor cursor = mySQLiteAdapter.queueAll("survey_result",
                        columns, null, null, groupBy);
                cursor.moveToPosition(0);

                while (cursor.isAfterLast() == false) {
                   /* //Log.e("",
                            "survey_id123"
                                    + cursor.getString(cursor
                                    .getColumnIndex("survey_id")));
*/
                    success = getServeyResultsLists(cursor.getString(cursor
                            .getColumnIndex("survey_id")), mySQLiteAdapter);
                    // }

                    cursor.moveToNext();
                }
                cursor.close();
                mySQLiteAdapter.close();
            } catch (Exception e) {
                //Log.e("", "Error" + e.getMessage());
                isException = true;
                success = false;
            }

            return null;
        }
    }

    public class doExportCustomerAsynchTask extends
            AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            if (PreferenceManager.getCustUpdate(getApplicationContext())
                    .equals("yes")) {
                // isCustomerListsforExport=true;

                SQLiteAdapter mySQlAdapter = new SQLiteAdapter(
                        getApplicationContext());
                mySQlAdapter.openToWrite();
                mySQlAdapter
                        .updateCustometStatus();
                mySQlAdapter.close();
                Toast.makeText(getApplicationContext(),
                        "Customer data exported successfully ",
                        Toast.LENGTH_LONG).show();


                PROCESS_START_FLAG = false;
                context.startActivity(new Intent(context, SearchUserActivity.class));
                finish();

            } else {
                // isCustomerListsforExport=false;
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Export failed!",
                        Toast.LENGTH_LONG).show();
            }


            // setProgrees(true);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            PROCESS_START_FLAG = true;

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(SettingsActivity.this.getResources().getString(
                    R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            // context = getApplicationContext();
            dialog.setContentView(R.layout.progress);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            getCustomerListsforExport();

            return null;
        }

    }

    private boolean getServeyResultsLists(String surveyId, SQLiteAdapter adapter) {
        //boolean isSuccess = false;
        JSONStringer js = new JSONStringer();
        try {

            // mySQLiteAdapter = new SQLiteAdapter(this);
            // mySQLiteAdapter.openToRead();
            /*
             * String[] columns = new String[] { "customer_id", "survey_id",
             * "survey_question_id", "survey_question", "survey_answer",
             * "survey_time", "survey_question_type", "survey_question_option",
             * "device_id", "surveyset_id", "tabular_option_id",
             * "tabular_suboption_id", "tabular_suboption_value",
             * "tabular_option_value", "image", "filename",
             * "location","userId","planDetailId" };
             */
            String[] columns = new String[]{"customer_id", "survey_id",
                    "survey_question_id", "survey_question", "survey_answer",
                    "survey_time", "survey_question_type",
                    "survey_question_option", "device_id", "surveyset_id",
                    "tabular_option_id", "tabular_suboption_id",
                    "tabular_suboption_value", "tabular_option_value", "image",
                    "filename", "location", "survey_no"};

            Cursor cursor = adapter.queueAll("survey_result", columns, null,
                    "survey_id=" + surveyId);
            cursor.moveToPosition(0);
            // Servey results upload json /////

            //Log.e("", "Count for particular survey" + cursor.getCount());

            // try {
            js.object().key("response");
            js.array();

            while (cursor.isAfterLast() == false) {
                /*
                 * if (!cursor.getString(6).equalsIgnoreCase(
                 * UserValues.Q_PICTURE)) {
                 */
                js.object().key("survey_id").value(cursor.getString(1))
                        .key("question_id").value(cursor.getString(2))
                        .key("question_type").value(cursor.getString(6))
                        .key("customer_id").value(cursor.getString(0))
                        .key("suboption_text").value(cursor.getString(4))
                        .key("time_stamp").value(cursor.getString(5))
                        .key("device_id").value(cursor.getString(8))
                        .key("survey_set_id").value(cursor.getString(9))
                        .key("tabular_option_id").value(cursor.getString(10))
                        .key("tabular_option_value")
                        .value(cursor.getString(13))
                        .key("tabular_suboption_id")
                        .value(cursor.getString(11))
                        .key("tabular_suboption_value")
                        .value(cursor.getString(12))
                        .key("storeimage")
                        .value(cursor.getString(14))
                        .key("survey_no")
                        .value(cursor.getString(17))
                        // .key("location").value(cursor.getString(16))
                        .key("storeimagename").value(cursor.getString(15))
                        .key("userId").value(AppPreferenceManager.getUserId(activity)).key("planDetailId").value("")
                        .endObject();

                cursor.moveToNext();
            }
            cursor.close();
            // mySQLiteAdapter.close();
            js.endArray();
            js.endObject();
            // //Log.i("Survey Data to Send",""+js.toString());
            // //System.out.println("Survey Data to Send: " + js.toString());
            // writeToSDFile(js.toString());
            // appendLog(js.toString());

            isSuccess = VkcApis.export_servey(js.toString(),
                    IVkcApis.SERVEY_LIST, context);
            /*
             * JSONObject jsonStringObject=new JSONObject(js.toString());
             * JSONArray
             * jsonStringArray=jsonStringObject.optJSONArray("response");
             * noOfSurvey=jsonStringArray.length();
             */
            // if(noOfSurvey==0)
            // {
            // mySQLiteAdapter = new SQLiteAdapter(this);
            // mySQLiteAdapter.openToRead();
            // noOfSurvey = mySQLiteAdapter.getAllCustomerSurveyDistinct();
            // mySQLiteAdapter.close();
            // }
            if (isSuccess) {
                String condition = "survey_id=";
                adapter.makeEmpty("survey_result", condition + surveyId);
                // adapter.makeEmpty("survey_result_temp", null);

                isSuccess = false;
                return true;
            } else {
                return false;

            }

        } catch (Exception e) {
            //Log.e("BACKGROUND_PROC", e.getMessage());
            return false;
        }

    }

    private void getCustomerListsforExport() {
        JSONStringer js = new JSONStringer();
        try {

            mySQLiteAdapter = new SQLiteAdapter(this);
            mySQLiteAdapter.openToRead();

            /*
             * String[] columns = new String[] { "customer_id", "customer_name",
             * "customer_category", "customer_add1", "customer_add2",
             * "customer_add3", "customer_add4", "customer_add5",
             * "customer_phone", "customer_place", "customer_region",
             * "customer_zone", "customer_businesstype", "customer_dist",
             * "customer_shopname", "customer_phone2", "customer_email",
             * "customer_status", "customer_state", "customer_desgn",
             * "latitude", "longitude", "customer_department",
             * "customer_country","planId","detailId"};
             */
            String[] columns = new String[]{"customer_id", "customer_name",
                    "customer_category", "customer_add1", "customer_add2",
                    "customer_add3", "customer_add4", "customer_add5",
                    "customer_phone", "customer_place", "customer_region",
                    "customer_zone", "customer_businesstype", "customer_dist",
                    "customer_shopname", "customer_phone2", "customer_email",
                    "customer_status", "customer_state", "customer_desgn",
                    "latitude", "longitude", "customer_department",
                    "customer_country", "verify_status", "mark_for_deletion", "shop_relevance_id"};
            Cursor cursor = mySQLiteAdapter.queueAll("survey_customerdetails",
                    columns, null, "status=1 or status=2");  //Bibin added status 0 - 15/10/2018 or status=0

            /*
             * Cursor cursor =
             * mySQLiteAdapter.queueAll("survey_customerdetails", columns, null,
             * "status=1 or status=2 ");
             */
            cursor.moveToPosition(0);
            // Servey results upload json /////

            try {
                js.object().key("response");
                js.array();

                // //System.out.println("js.toString()"+js.toString());

                // ////Servey results upload json /////

                while (cursor.isAfterLast() == false) {

                    js.object()
                            .key("customer_id")
                            .value(cursor.getString(0))
                            .key("customer_name")
                            .value(cursor.getString(1))
                            .key("customer_category")
                            .value(cursor.getString(2) == null ? "" : cursor
                                    .getString(2))
                            .key("customer_add1")
                            .value(cursor.getString(3) == null ? "" : cursor
                                    .getString(3))
                            .key("customer_add2")
                            .value(cursor.getString(4) == null ? "" : cursor
                                    .getString(4))
                            .key("customer_add3")
                            .value(cursor.getString(5) == null ? "" : cursor
                                    .getString(5))
                            .key("customer_add4")
                            .value(cursor.getString(6) == null ? "" : cursor
                                    .getString(6))
                            .key("customer_add5")
                            .value(cursor.getString(7) == null ? "" : cursor
                                    .getString(7))
                            .key("customer_phone")
                            .value(cursor.getString(8) == null ? "" : cursor
                                    .getString(8))
                            .key("customer_place")
                            .value(cursor.getString(9) == null ? "" : cursor
                                    .getString(9))

                            .key("customer_businesstype")
                            .value(cursor.getString(12) == null ? "" : cursor
                                    .getString(12))
                            .key("customer_dist")
                            .value(cursor.getString(13) == null ? "" : cursor
                                    .getString(13))
                            .key("customer_shopname")
                            .value(cursor.getString(14) == null ? "" : cursor
                                    .getString(14))
                            .key("customer_phone2")
                            .value(cursor.getString(15) == null ? "" : cursor
                                    .getString(15))
                            .key("customer_email")
                            .value(cursor.getString(16) == null ? "" : cursor
                                    .getString(16))
                            .key("customer_status")
                            .value(cursor.getString(17) == null ? "" : cursor
                                    .getString(17))
                            .key("customer_state")
                            .value(cursor.getString(18) == null ? "" : cursor
                                    .getString(18))
                            .key("customer_desgn")
                            .value(cursor.getString(19) == null ? "" : cursor
                                    .getString(19))

                            .key("latitude")
                            .value(cursor.getString(20) == null ? "" : cursor
                                    .getString(20))

                            .key("longitude")
                            .value(cursor.getString(21) == null ? "" : cursor
                                    .getString(21))
                            .key("customer_department")
                            .value(cursor.getString(22) == null ? "" : cursor
                                    .getString(22))

                            .key("customer_country")
                            .value(cursor.getString(23) == null ? "" : cursor
                                    .getString(23)).key("plan_detailId")
                            .value("")
                            .key("representativeID")
                            .value("")
                            .key("is_verified")
                            .value(cursor.getString(24) == null ? "" : // cursor.getString(24)

                                    cursor.getString(24))
                            // 1

                            .key("is_marked_deletion")
                            .value(cursor.getString(25) == null ? "" : cursor
                                    .getString(25))
                            .key("shop_relevance_id")
                            .value(cursor.getString(26) == null ? "" : cursor
                                    .getString(26))
                            .endObject();

                    // String is_verified = cursor.getString(24);
                    // //System.out.println("is_verified " + is_verified);
                    // js.endObject();

                    cursor.moveToNext();

                    // //Log.v("",cursor.getString(1));

                }
                js.endArray();
                js.endObject();

                cursor.close();

                mySQLiteAdapter.close();
                ////System.out.println("JSON" + js);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //Log.e("BACKGROUND_PROC", e.getMessage());
        }
        VkcApis.export_servey(js.toString(), IVkcApis.CUSTOMER_LIST, context);
        // //System.out.println("Data" + js.toString());
        // Export Customer List API

        /*
         * try { JSONObject obj=new JSONObject(js.toString()); JSONArray
         * array=obj.getJSONArray("response"); if(array.length()<=0) {
         * Toast.makeText(SettingsActivity.this, "No new customer data added",
         * Toast.LENGTH_SHORT).show(); } else {
         * VkcApis.export_servey(js.toString(), IVkcApis.CUSTOMER_LIST,
         * context); } } catch (JSONException e) { // TODO Auto-generated catch
         * block e.printStackTrace(); }
         */

    }

    public boolean isCustomerListsforExport() {
        mySQLiteAdapter.openToRead();

        String[] columns = new String[]{"customer_id"};

        Cursor cursor = mySQLiteAdapter.queueAll("survey_customerdetails",
                columns, null, "status=1 or status=2");


        if (cursor.getCount() > 0) {
            cursor.close();
            mySQLiteAdapter.close();
            return true;

        } else {
            cursor.close();
            mySQLiteAdapter.close();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (PROCESS_START_FLAG) {
            return;
        }
        Intent intent = new Intent(SettingsActivity.this,
                VKCAppActivity.class);
				/*
				Intent intent = new Intent(SearchUserActivity.this,
						SettingsActivity.class);*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == llBackup) {
            int result_Count;
            mySQLiteAdapter.openToRead();
            // result_Count = mySQLiteAdapter.surveyResultCount();
            result_Count = mySQLiteAdapter.surveyResultCounting();

            mySQLiteAdapter.close();
            //System.out.println("result_Count::" + result_Count);
            // Survey Result Count Check
            if (result_Count > 0) {
                BackupDb backup = new BackupDb();
                backup.execute();
            } else {
                Toast.makeText(context,
                        "Unable to backup file. The survey result is empty",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (v == llRestore) {
            i = new Intent(SettingsActivity.this, RestoreList.class);
            startActivity(i);
        }

    }

    private class BackupDb extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SettingsActivity.this, "",
                    "In Progress...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");

            //   File mydir = getDir("VKCSurvey", Context.MODE_PRIVATE); //Creating an internal dir;


            if (mydir.exists()) {


                try {
                    Utils.copyFile(
                            new File(
                                    mydir.getAbsolutePath()
                                            + "/zaimussurvey.sql"),
                            new File(mydir.getAbsolutePath()
                                    + "/zaimussurvey"
                                    + Utils.getCalender() + ".sql"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isSuccess = true;
            } else {
                isSuccess = false;
            }
           /* if (Utils.isSDCardMounted()) {
                Utils.checkDirExists("/VKCSurvey");


                isSuccess = true;
            } else {
                isSuccess = false;
            }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (isSuccess) {
                Toast.makeText(context,
                        "Successfully backuped the file in sdcard",
                        Toast.LENGTH_SHORT).show();
                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                try {
                    mySQLiteAdapter.openToWrite();

                    mySQLiteAdapter.makeEmpty("survey_result");

                    mySQLiteAdapter.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (isException) {
                Toast.makeText(context,
                        "Cannot continue at this time, Please try again later",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "SD Card unmounted or not present",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class BackupDbExport extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SettingsActivity.this, "",
                    "In Progress...");
        }

        @Override
        protected Void doInBackground(Void... params) {


            // File mydir = getDir("VKCSurvey", Context.MODE_PRIVATE); //Creating an internal dir;

            File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");

            if (mydir.exists()) {
                Utils.checkDirExists("/ZaimusSurvey");

                try {
                    Utils.copyFile(
                            new File(
                                    mydir.getAbsolutePath()
                                            + "/zaimussurvey.sql"),
                            new File(mydir.getAbsolutePath()
                                    + "/zaimussurvey" + Utils.getCalender() + ".sql"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                isSuccess = true;
            } else {
                isSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (isSuccess) {
                // Toast.makeText(context,
                // "Successfully backuped the file in sdcard",
                // Toast.LENGTH_SHORT).show();
                Toast.makeText(
                        getApplicationContext(),
                        "Export not completed . Please try export survey again. Data backup has been created.",
                        Toast.LENGTH_LONG).show();
                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                try {
                    mySQLiteAdapter.openToWrite();

                    mySQLiteAdapter.makeEmpty("survey_result");

                    mySQLiteAdapter.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (isException) {
                Toast.makeText(context,
                        "Cannot continue at this time, Please try again later",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        context,
                        "Export and backup failed. SD Card unmounted or not present",
                        Toast.LENGTH_SHORT).show();
            }
        }

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

                                finish();
                            }
                        }).show();
    }

    private class SyncNewCustomerAsyncTask extends
            AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.progress);

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            mySQLiteAdapter.openToRead();
            cusIdArray = mySQLiteAdapter.getAllSurveyMax();

            String cusId = cusIdArray.get(cusIdArray.size() - 1).customer_id;
            userId = AppPreferenceManager.getUserId(context);
            roleId = AppPreferenceManager.getRoleId(context);

            if (Utils.checkInternetConnection(context)) {

                try {
                    surData = VkcApis.getNewCustomers(userId, roleId, cusId);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "No internet connection!!",
                        Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (surData == null) {
                Toast.makeText(context, R.string.conn_err, Toast.LENGTH_SHORT)
                        .show();
            } else if (surData.length == 0) {
                Toast.makeText(context, "There is no new customers!!",
                        Toast.LENGTH_SHORT).show();

            } else {
                for (int i = 0; i < surData.length; i++) {
                    String[][] data = {
                            {"status", ""},
                            {"customer_id", surData[i].cus_id},
                            {"customer_name", surData[i].customer_name},
                            {"customer_category", surData[i].customer_category},
                            {"customer_add1", surData[i].customer_add1},
                            {"customer_add2", surData[i].customer_add2},
                            {"customer_add3", surData[i].customer_add3},
                            {"customer_add4", surData[i].customer_add4},
                            {"customer_add5", surData[i].customer_add5},
                            {"customer_phone", surData[i].customer_phone},
                            {"customer_place", surData[i].customer_place},

                            {"customer_businesstype",
                                    surData[i].customer_businesstype},
                            {"customer_dist", surData[i].customer_dist},
                            {"customer_shopname", surData[i].customer_shopname},
                            {"customer_phone2", surData[i].customer_phone2},
                            {"customer_email", surData[i].customer_email},
                            {"customer_status", surData[i].customer_status},
                            {"customer_desgn", surData[i].customer_desgn},
                            {"customer_state", surData[i].customer_state},
                            {"customer_department",
                                    surData[i].customer_department},
                            {"customer_country", surData[i].customer_country},
                            {"latitude", surData[i].latitude},
                            {"longitude", surData[i].longitude},
                            {"is_verified", surData[i].is_verified},
                            {"is_marked_deletion",
                                    surData[i].isMarkedforDeletion}};
                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.insert(data, "survey_customerdetails");
                    // mySQLiteAdapter.openToRead();
                    mySQLiteAdapter.close();
                }
            }
        }

    }

    protected void showDifferentUserAlert() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "Export survey result is pending for the previous user"

                                + " .Do you want to continue??")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (ConnectionManager
                                        .checkIntenetConnection(context)) {
                                    //System.out.println("Not upgraded");
                                    doSetupAsynchTask set = new doSetupAsynchTask();
                                    set.execute();
                                } else {
                                    Toast.makeText(context,
                                            "No internet connection ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
    }

    protected void showImportCustomerAlert() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "This requires very good internet connection such as WiFi or 3G.\n\nDo you wish to continue ?")
                .setTitle("Warning")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
//Bibin Comment


                                mySQLiteAdapter.openToWrite();
                                mySQLiteAdapter.makeEmpty("survey_customerdetails");
                                mySQLiteAdapter.close();
                                doSetupAsynchTask set = new doSetupAsynchTask();
                                set.execute();
                               /* if (PreferenceManager.getCustUpdate(context).equals("no")) {
                                    Toast.makeText(
                                            context,
                                            "Sorry! Export customer data first!!",
                                            Toast.LENGTH_SHORT).show();
                                    // synchButton.setEnabled(false);
                                } else {

                                    doSetupAsynchTask set = new doSetupAsynchTask();
                                    set.execute();

                                }*/
                            }
                        }).show();
    }

    public class doGPSSyncAsynchTask extends AsyncTask<Void, Integer, Void> {

        private String success;

        @Override
        protected void onPostExecute(Void result) {

            // setProgrees(true);
            dialog.dismiss();

            PROCESS_START_FLAG = false;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            PROCESS_START_FLAG = true;

            //System.out.println("ASync TASK------->");
            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(SettingsActivity.this.getResources().getString(
                    R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.progress);
            super.onPreExecute();
            // success = "failed";
        }

        @Override
        protected Void doInBackground(Void... params) {
            //System.out.println("ASync TASK 1------->");

            getGPSResultsLists();

            return null;
        }

        public void getGPSResultsLists() {
            String isSuccess = null;
            JSONStringer js = new JSONStringer();
            try {

                // mySQLiteAdapter = new SQLiteAdapter(this);
                mySQLiteAdapter.openToRead();
                String[] columns = new String[]{"customer_id", "latitude",
                        "longitude"};
                //System.out.println("ASync TASK------->");

                Cursor cursor = mySQLiteAdapter.queueAll("survey_location",
                        columns, null, null);
                cursor.moveToPosition(0);
                // Servey results upload json /////

                //Log.e("", "Count for GPS location" + cursor.getCount());


                // try {
                js.object().key("customerGps");
                js.array();

                while (cursor.isAfterLast() == false) {

                    js.object().key("customerId").value(cursor.getString(0))
                            .key("latitude").value(cursor.getString(1))
                            .key("longitude").value(cursor.getString(2))
                            .endObject();

                    cursor.moveToNext();
                }
                cursor.close();
                // mySQLiteAdapter.close();
                js.endArray();
                js.endObject();
                //System.out.println("GPS " + js.toString());

                survey = VkcApis.export_gps(js.toString(), context);
                //System.out.println("GPS 123" + survey.status);

                if (survey.status.equals("success")) {
                    mySQLiteAdapter.openToRead();
                    String[] values = new String[]{"customer_id", "latitude",
                            "longitude"};
                    //System.out.println("ASync TASK------->");

                    Cursor lcursor = mySQLiteAdapter.queueAll(
                            "survey_location", columns, null, null);
                    lcursor.moveToPosition(0);

                    // //System.out.println("GPS 123 Success" + survey.status);
                    // //System.out.println("GPS 123 data" +
                    // lcursor.getString(1));
                    // //System.out.println("GPS 123 data" +
                    // lcursor.getString(2));
                    mySQLiteAdapter.openToWrite();
                    survey.latitude = lcursor.getString(1);
                    survey.longitude = lcursor.getString(2);
                    mySQLiteAdapter.update_GPS(survey, lcursor.getString(0));
                    mySQLiteAdapter.makeEmpty("survey_location");
                    lcursor.close();
                    mySQLiteAdapter.close();
                } else {
                    //System.out.println("GPS 123 failed :" + survey.status);

                }

            } catch (Exception e) {
                // //Log.e("BACKGROUND_PROC", e.getMessage());
                // return success;
                Log.d("", " Exception");

            }

        }
    }

    private void getImages() {
        // TODO Auto-generated method stub
        mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());

        mySQLiteAdapter.openToRead();
        imageList = mySQLiteAdapter.getAllImages();

        if (imageList.size() > 0) {
            for (int k = 0; k < imageList.size(); k++) {
                // AppPreferenceManager.saveImageId(imageList.get(k).questionId,
                // context);
                quesId = imageList.get(k).questionId;
                //System.out.println("Question ID----------->" + quesId);
                try {

                    new LoadImage().execute(
                            (IVkcApis.IMAGE_FEEDBACK_URL + "" + imageList
                                    .get(k).imagePath), quesId);

                } catch (Exception e) {

                }
            }
        }
        mySQLiteAdapter.close();
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private String localQuesId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SettingsActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.hide();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                localQuesId = args[1];
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                        args[0]).getContent());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                b = baos.toByteArray();
                temp = Base64.encodeToString(b, Base64.DEFAULT);
                // return temp;
                // AppPreferenceManager.saveImage(temp, context);
                // Log.d("TAG", "IMAGE-------------->" + temp);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            pDialog.dismiss();

            if (image != null) {
                pDialog.dismiss();
                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                mySQLiteAdapter.openToWrite();
                // survey.image_download = temp;
                mySQLiteAdapter.UpdateDownloadedimage(temp, localQuesId);

                mySQLiteAdapter.close();
            } else {

                pDialog.dismiss();
                /*
                 * Toast.makeText(SettingsActivity.this,
                 * "Image Does Not exists..", Toast.LENGTH_SHORT).show();
                 */

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        // MyApplication.getInstance().setConnectivityListener(this);

        if (Utils.checkInternetConnection(context)) {
            showAlertSurvey(Utils.checkInternetConnection(context));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        // TODO Auto-generated method stub
        // showSnack(isConnected);

        showAlertSurvey(isConnected);

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
}
