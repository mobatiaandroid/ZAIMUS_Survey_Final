package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.SurveyTypes.SurveyTypeUtils;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.api.RetrofitAPI;
import com.zaimus.api.model.ExportSurveyResponse;
import com.zaimus.constants.GpsSettings;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Utils;

import org.json.JSONStringer;

import java.io.File;
import java.io.IOException;

import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyUpdateActivity extends Activity implements LocationListener {

    private Button submit_button;
    private Context mContext;
    private LocationManager lm;

    Boolean isSuccess = false;

    static boolean isTrue;

    private static SQLiteAdapter mySQLiteAdapter;
    private ProgressBar progress;
    private Boolean PROCESS_START_FLAG = false;
    static ProgressDialog dialog;
    static Boolean isException = false;
    static int noOfSurvey = 0;
    private static boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.submit_screen);
        progress = (ProgressBar) findViewById(R.id.progressBar1);
        mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());

        startListening();
        submit_button = (Button) findViewById(R.id.bt_submit_survey);
        submit_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (AudioRecorder.mRecorder != null) {
                    AudioRecorder.mRecorder.stop();
                    AudioRecorder.mRecorder.release();
                    AudioRecorder.mRecorder = null;
                    //Log.e("", "Stopped audio recorder");
                }
                SurveyTypeUtils updt = new SurveyTypeUtils();
                if (updt.saveSurveyResults(SurveyUpdateActivity.this, Utils.getDeviceId(mContext))) {

                    if (AppPreferenceManager.getFromPlan(mContext).equals("1")) {
                        Toast.makeText(getApplicationContext(),
                                "Survey has been completed successfully",
                                Toast.LENGTH_SHORT).show();
                        setResult(UserValues.SURVEY_RES);
                        Intent intent = new Intent(SurveyUpdateActivity.this,
                                ShowCustomers.class);
                        startActivity(intent);

                        finish();
                    } else {

                        if (Utils.checkInternetConnection(mContext)) {

//                            doExportAsynchTask doTask = new doExportAsynchTask();
//                            doTask.execute();
                            PROCESS_START_FLAG = true;
                            dialog = new ProgressDialog(SurveyUpdateActivity.this);
                            dialog.setMessage("Please wait...");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            success = false;
                            try {
                                mySQLiteAdapter = new SQLiteAdapter(SurveyUpdateActivity.this);
                                mySQLiteAdapter.openToRead();
                                String[] columns = new String[]{"surveyset_id"};
                                String groupBy = "surveyset_id";

                                Cursor cursor = mySQLiteAdapter.queueAll("survey_result",
                                        columns, null, null, groupBy);
                                cursor.moveToPosition(0);

                                while (cursor.isAfterLast() == false) {
                                    success = getServeyResultsLists(String.valueOf(Survey.surveyset_id), mySQLiteAdapter);

                                    cursor.moveToNext();
                                }
                                cursor.close();
                                mySQLiteAdapter.close();
                            } catch (Exception e) {
                                isException = true;
                                success = false;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Survey has been completed successfully",
                                    Toast.LENGTH_SHORT).show();
                            setResult(UserValues.SURVEY_RES);
                            Intent intent = new Intent(SurveyUpdateActivity.this,
                                    SearchUserActivity.class);
                            startActivity(intent);

                            finish();
                        }


                    }

                }
            }
        });
    }

    private void startListening() {
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
                LocationManager.GPS_PROVIDER,
                5000,
                0,
                this
        );

    }

    @Override
    protected void onPause() {
        super.onPause();
        //AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, SurveyUpdateActivity.this);
    }

    @Override
    protected void onDestroy() {
        stopListening();
        super.onDestroy();
    }

    private void stopListening() {
        if (lm != null)
            lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        GpsSettings.LATITUDE = location.getLatitude();
        GpsSettings.LONGITUDE = location.getLongitude();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AudioRecorder.stopTimer();
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
        //turnGPSOn();
		/*Message msg = handler.obtainMessage();
	     msg.arg1 = 1;
	     handler.sendMessage(msg);*/
    }

    public class doExportAsynchTask extends AsyncTask<Void, Integer, Void> {

        private boolean success = false;

        @Override
        protected void onPostExecute(Void result) {

            dialog.dismiss();

            PROCESS_START_FLAG = false;
            if (success) {
//				Toast.makeText(getApplicationContext(),
//						"Uploaded successfully ", Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(),
                        "Survey has been completed and " + noOfSurvey + " survey exported successfully",
                        Toast.LENGTH_SHORT).show();
                setResult(UserValues.SURVEY_RES);
                AppPreferenceManager.saveUserStatus("0", mContext);
                Intent intent = new Intent(SurveyUpdateActivity.this,
                        SearchUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            } else {


                int result_Count;
                mySQLiteAdapter.openToRead();
                // result_Count = mySQLiteAdapter.surveyResultCount();
                result_Count = mySQLiteAdapter.surveyResultCounting();

                mySQLiteAdapter.close();
                //System.out.println("result_Count::" + result_Count);
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
				/*Toast.makeText(
						getApplicationContext(),
						"Survey has been completed. But export not completed sucessfully. Please try export survey again.",
						Toast.LENGTH_LONG).show();*/
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            PROCESS_START_FLAG = true;

            dialog = new ProgressDialog(SurveyUpdateActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            super.onPreExecute();
            success = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mySQLiteAdapter = new SQLiteAdapter(SurveyUpdateActivity.this);
                mySQLiteAdapter.openToRead();
                String[] columns = new String[]{"surveyset_id"};
                String groupBy = "surveyset_id";

                Cursor cursor = mySQLiteAdapter.queueAll("survey_result",
                        columns, null, null, groupBy);
                cursor.moveToPosition(0);

                while (cursor.isAfterLast() == false) {
                   /* //Log.e("",
                            "survey_id123"
                                    + cursor.getString(cursor
                                    .getColumnIndex("surveyset_id")));*/

                    success = getServeyResultsLists(String.valueOf(Survey.surveyset_id), mySQLiteAdapter);
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

    private boolean getServeyResultsLists(String surveyId, SQLiteAdapter adapter) {
        //	boolean isSuccess = false;
        JSONStringer js = new JSONStringer();
        try {

            String[] columns = new String[]{"customer_id", "survey_id",
                    "survey_question_id", "survey_question", "survey_answer",
                    "survey_time", "survey_question_type",
                    "survey_question_option", "device_id", "surveyset_id",
                    "tabular_option_id", "tabular_suboption_id",
                    "tabular_suboption_value", "tabular_option_value", "image",
                    "filename", "location", "survey_no"};

            Cursor cursor = adapter.queueAll("survey_result", columns, null,
                    "surveyset_id=" + surveyId);
            cursor.moveToPosition(0);
            // Servey results upload json ////


            //	//Log.e("", "Count for particular survey" + cursor.getCount());

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
                        // .key("location").value(cursor.getString(16))
                        .key("storeimagename").value(cursor.getString(15))
                        .key("survey_no").value(cursor.getString(17))
                        .key("userId").value(AppPreferenceManager.getUserId(mContext)).key("planDetailId").value("")
                        .endObject();

                cursor.moveToNext();
                //i++;
            }
            cursor.close();
            // mySQLiteAdapter.close();
            js.endArray();
            js.endObject();
            // //Log.i("Survey Data to Send",""+js.toString());
            ////System.out.println("Survey Data to Send: " + js.toString());
            // writeToSDFile(js.toString());
            // appendLog(js.toString());
            isSuccess = ExportSurvey(js.toString(), mContext);
            if (noOfSurvey == 0) {
                mySQLiteAdapter = new SQLiteAdapter(mContext);
                mySQLiteAdapter.openToRead();
                noOfSurvey = mySQLiteAdapter.getAllCustomerSurveyDistinct();
                mySQLiteAdapter.close();
            }
            if (isSuccess) {
                ////Log.e("For succes", "survey update");
                String condition = "surveyset_id=";
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
//	protected void showExportAlert() {
//		new AlertDialog.Builder(this)
//				.setMessage(
//						"This requires very good internet connection such as WiFi or 3G.\n\nDo you wish to continue ?")
//				.setTitle("Warning")
//				.setCancelable(true)
//				.setIcon(android.R.drawable.stat_notify_error)
//				.setNegativeButton("Later",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//							}
//						})
//				.setPositiveButton("Yes",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//
//							
//									doExportAsynchTask doTask = new doExportAsynchTask();
//									doTask.execute();
//		
//							}
//						}).show();
//	}

    private class BackupDbExport extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SurveyUpdateActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            //  File mydir = getDir("VKCSurvey", Context.MODE_PRIVATE); //Creating an internal dir;

            File mydir = new File(Environment.getExternalStorageDirectory(), "ZaimusSurvey");

            if (mydir.exists()) {

                try {
                    Utils.copyFile(
                            new File(
                                    mydir.getAbsolutePath()
                                            + "/zaimussurvey.sql"),
                            new File(mydir.getAbsolutePath()
                                    + "/zaimussurvey"
                                    + Utils.getCalender() + ".sql"));
                    isSuccess = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else {
                isSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
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
                Toast.makeText(mContext,
                        "Cannot continue at this time, Please try again later",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        mContext,
                        "Export and backup failed. SD Card unmounted or not present",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean ExportSurvey(String jsonData, final Context mContext) {


        RetrofitAPI.getClient().getExportSurveyResponse(jsonData).enqueue(new Callback<ExportSurveyResponse>() {
            @Override
            public void onResponse(Call<ExportSurveyResponse> call, Response<ExportSurveyResponse> response) {

                try {

                    //  String responseData = response.body().getResponse().toString();


                    PreferenceManager.saveUpdate("yes", mContext);

                    //Bibin added return true 20/12/2018
                    isTrue = true;

                    dialog.dismiss();

                    PROCESS_START_FLAG = false;
                    if (isTrue) {
//				Toast.makeText(getApplicationContext(),
//						"Uploaded successfully ", Toast.LENGTH_LONG).show();
                        mySQLiteAdapter = new SQLiteAdapter(mContext);
                        mySQLiteAdapter.openToRead();
                        noOfSurvey = mySQLiteAdapter.getAllCustomerSurveyDistinct();
                        mySQLiteAdapter.close();
                        Toast.makeText(getApplicationContext(),
                                "Survey has been completed and exported successfully",
                                Toast.LENGTH_SHORT).show();

                        //                                "Survey has been completed and " + noOfSurvey + " survey exported successfully",
                        setResult(UserValues.SURVEY_RES);
                        AppPreferenceManager.saveUserStatus("0", mContext);
                        Intent intent = new Intent(SurveyUpdateActivity.this,
                                SearchUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                    } else {


                        int result_Count;
                        mySQLiteAdapter.openToRead();
                        // result_Count = mySQLiteAdapter.surveyResultCount();
                        result_Count = mySQLiteAdapter.surveyResultCounting();

                        mySQLiteAdapter.close();
                        //System.out.println("result_Count::" + result_Count);
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
				/*Toast.makeText(
						getApplicationContext(),
						"Survey has been completed. But export not completed sucessfully. Please try export survey again.",
						Toast.LENGTH_LONG).show();*/
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    PreferenceManager.saveUpdate("no", mContext);
                    PreferenceManager.saveCustUpdate("no", mContext);
                    mySQLiteAdapter.close();
                    isTrue = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    PreferenceManager.saveUpdate("no", mContext);
                    PreferenceManager.saveCustUpdate("no", mContext);
                    isTrue = false;
                }
            }

            @Override
            public void onFailure(Call<ExportSurveyResponse> call, Throwable t) {

            }
        });

        return isTrue;
    }
}