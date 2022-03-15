package com.zaimus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.api.model.LoginResponse;
import com.zaimus.api.RetrofitAPI;
import com.zaimus.gps.GpsUtility;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VKCLoginActivity extends Activity {
    EditText username, password;
    Button login;
    String userString, passString;
    String storeUsername, storePassword;
    private Boolean PROCESS_START_FLAG = false;
    private ProgressBar progress;
    Context context = this;
    Survey surVey;
    String flag;
    ProgressDialog Dialog;
    Typeface typeface;
    TextView logintext;
    TextView textVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flag = AppPreferenceManager.getFlag(context);
        typeface = Utils.setFontTypeToArial(context);
        new GpsUtility(this).turnGPSOn(new GpsUtility.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                Log.e("Gps",String.valueOf(isGPSEnable));
            }
        });
        if (flag.equals("1")) {

            Intent intent = new Intent(VKCLoginActivity.this,
                    VKCAppActivity.class);
            startActivity(intent);


        }

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.buttonLogin);
        logintext = (TextView) findViewById(R.id.logintext);
        progress = (ProgressBar) findViewById(R.id.progressBar1);
        textVersion = (TextView) findViewById(R.id.textVersion);

        surVey = new Survey();
        username.setTypeface(typeface);
        password.setTypeface(typeface);
        login.setTypeface(typeface);
        logintext.setTypeface(typeface);
        textVersion.setText("Version : " + getVersion());
        storeUsername = AppPreferenceManager.getUsername(context);
        storePassword = AppPreferenceManager.getPassword(context);

        username.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                username.setFocusable(true);
                return false;
            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                userString = username.getText().toString();
                passString = password.getText().toString();

                if (userString.equals("")) {
                    username.setError("enter username");

                } else if (passString.equals("")) {
                    password.setError("enter password");

                } else {
                    if (Utils.checkInternetConnection(context)) {

                        if (storeUsername.equals(userString) && storePassword.equals(passString)) {
                            AppPreferenceManager.savePreviousUser("", context);
                            AppPreferenceManager.saveUserStatus("0", context);

                        } else if (storeUsername.equals("") && storePassword.equals("")) {

                            AppPreferenceManager.saveUserStatus("0", context);
                        } else {

                            AppPreferenceManager.savePreviousUser(storeUsername, context);
                            AppPreferenceManager.saveUserStatus("1", context);
                        }

                        PROCESS_START_FLAG = true;

                        Dialog = new ProgressDialog(VKCLoginActivity.this);
                        Dialog.setMessage(VKCLoginActivity.this.getResources().getString(
                                R.string.loading));
                        Dialog.setCanceledOnTouchOutside(false);
                        Dialog.show();
                        Dialog.setContentView(R.layout.progress);


                        RetrofitAPI.getClient().getLoginResponse(userString, passString).enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                                surVey.status = response.body().getUserdetails().getStatus();
                                surVey.userName = response.body().getUserdetails().getUserName();
                                surVey.userId = response.body().getUserdetails().getUserId();
                                surVey.roleId = response.body().getUserdetails().getRoleId();
                                surVey.email = response.body().getUserdetails().getEmail();
                                AppPreferenceManager.saveStateName(response.body().getUserdetails().getState(),
                                        context);
                                AppPreferenceManager.saveDeviceID(response.body().getUserdetails().getImeiNo(), context);
                                AppPreferenceManager.saveUserId(response.body().getUserdetails().getUserId(), context);
                                AppPreferenceManager.saveRoleId(response.body().getUserdetails().getRoleId(), context);
                                progress.setVisibility(View.GONE);
                                Dialog.dismiss();
                                if (surVey == null) {
                                    Toast.makeText(context, R.string.conn_err,
                                            Toast.LENGTH_SHORT).show();
                                } else if (surVey.status.equals("success")) {
                                    AppPreferenceManager.saveFlag("1", context);
                                    AppPreferenceManager.saveUsername(
                                            username.getText().toString(), context);
                                    AppPreferenceManager.savePassword(
                                            password.getText().toString(), context);

                                    PreferenceManager.saveUpdate("no", context);
                                    PreferenceManager.saveCustUpdate("no", context);
                                    if (AppPreferenceManager.getAttendanceId(context).length() > 0) {
                                        Intent intent = new Intent(VKCLoginActivity.this,
                                                VKCAppActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(VKCLoginActivity.this,
                                                AttendanceActivity.class);
                                        startActivity(intent);
                                    }
                                } else if (surVey.status.equals("failed")) {
                                    AppPreferenceManager.saveFlag("0", context);

                                    Toast.makeText(context, "Please check your credentials",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {

                                Toast.makeText(context, "Network error.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {

                        if (userString.equals(storeUsername)
                                && passString.equals(storePassword)) {
                            Intent intent = new Intent(VKCLoginActivity.this,
                                    VKCAppActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Internet Connection is must for first time login",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

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
}
