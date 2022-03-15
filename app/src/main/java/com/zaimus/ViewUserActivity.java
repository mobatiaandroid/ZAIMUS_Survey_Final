package com.zaimus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.Profiles.Profile;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

public class ViewUserActivity extends Activity implements LocationListener {

    private SQLiteAdapter mySQlAdapter;
    private Profile pro = new Profile();
    private TextView customer_name;
    private TextView customer_desgn;
    private TextView customer_shopname;
    private TextView customer_dist;
    private TextView customer_state;
    private TextView customer_category;
    private TextView customer_businesstype;
    private TextView customer_add1;
    private TextView customer_add2;
    private TextView customer_add3;
    private TextView customer_add4;
    private TextView customer_add5;
    private TextView customer_place;
    private TextView customer_region;
    private TextView customer_zone;
    private TextView customer_phone;
    private TextView customer_phone2;
    private TextView customer_email;
    private TextView customer_country;
    private TextView customer_department;
    private String customer_id;
    private Button startSurveyButton;
    private Button editUserButton;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg, btnGPSCapture;
    Activity activity = this;
    Context context = this;
    Typeface typeface, typefacebold;
    TextView titleShopName, titleCustName, titleCustDesg, titleState,
            titleDistrict, titleCountry, titleDepartment, titleCategory,
            titleBtype, titleAdd1, titleAdd2, titleAdd3, titleAdd4, titleAdd5,
            titlePhone, titlePhone2, titleEmailId, titlePlace, titleRegion,
            titleZone, titleGPSCapture, viewRelevance;
    String latitude, longitude;
    double lat, lng;
    Survey sdetails;
    private CheckBox checkDelete;
    private Button buttonVerify;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private Cursor cursor;
    private Cursor statcursor;
    private Cursor btycursor;
    private Cursor relevanceCursor;
    private Cursor ccatecursor;
    private Cursor distcursor;
    private Cursor zoneCursor;
    private Cursor regionCursor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bundle extras = getIntent().getExtras();
        typeface = Utils.setFontTypeToArial(context);

        sdetails = new Survey();
        typefacebold = Utils.setFontTypeToArialbold(context);

        if (extras != null) {
            customer_id = extras.getString("Id");
            // //Log.v("customer_id", customer_id);
            // //Log.v("customer status", extras.getString("status"));
            GlobalConstants.customer_status = extras.getString("status");
            GlobalConstants.customer_id = customer_id;
            setContentView(R.layout.view_user);
            header = (LinearLayout) findViewById(R.id.header);
            headermanager = new Headermanager(activity, "View Customer");
            headermanager.getHeader(header, 0, false);
            headermanager.setButtonLeftSelector(R.drawable.back,
                    R.drawable.back);
            splitIcon = headermanager.getLeftButton();
            //getAddress();
            splitIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ViewUserActivity.this.finish();

                }
            });
            InitializeComponents();
            getProfile();

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

    protected void getProfile() {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Cursor cursor = null;

                        // TODO Auto-generated method stub
                        try {
                            String[] columns = new String[]{"customer_id",
                                    "customer_name", "customer_category",
                                    "customer_add1", "customer_add2",
                                    "customer_add3", "customer_add4",
                                    "customer_add5", "customer_phone",
                                    "customer_place", "customer_region",
                                    "customer_zone", "customer_businesstype",
                                    "customer_dist", "customer_shopname",
                                    "customer_phone2", "customer_email",
                                    "status", "customer_state",
                                    "customer_desgn", "customer_country",
                                    "customer_department", "latitude",
                                    "longitude", "verify_status",
                                    "mark_for_deletion", "shop_relevance_id", "cust_id"};
                            mySQlAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQlAdapter.openToRead();
                            cursor = mySQlAdapter.queueAll(
                                    "survey_customerdetails", columns, null,
                                    "customer_id = " + customer_id);

                            //if(cursor.getCount()>1) {
                                cursor.moveToPosition(0);
                          //  }

                            String btype = "";

                            if(cursor.getString(12).trim().length()>0) {

                                try {
                                    btycursor = mySQlAdapter.queueAll(
                                            "survey_customerbtypes",
                                            new String[]{"btype_text"}, null,
                                            "btype_id =" + cursor.getString(12));
                                    btycursor.moveToPosition(0);
                                    btype = btycursor.getString(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                btycursor.close();
                            }
                            String cacate = "";
                            try {
                                ccatecursor = mySQlAdapter.queueAll(
                                        "survey_customercategs",
                                        new String[]{"category_name"}, null,
                                        "category_id =" + cursor.getString(2));
                                ccatecursor.moveToPosition(0);
                                cacate = ccatecursor.getString(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ccatecursor.close();

                            String dist = "";
                            if(cursor.getString(13).trim().length()>0) {

                                try {
                                    distcursor = mySQlAdapter.queueAll(
                                            "survey_distcts",
                                            new String[]{"dist_name"}, null,
                                            "dist_id =" + cursor.getString(13));
                                    distcursor.moveToPosition(0);
                                    dist = distcursor.getString(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                distcursor.close();
                            }
                            String stat = "";
                            try {
                                statcursor = mySQlAdapter.queueAll(
                                        "survey_states",
                                        new String[]{"state_name"}, null,
                                        "state_id =" + cursor.getString(18));
                                statcursor.moveToPosition(0);
                                stat = statcursor.getString(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            statcursor.close();

                            /*String region = "";
                            try {
                                regionCursor = mySQlAdapter.queueAll(
                                        "survey_regions",
                                        new String[]{"region_name"}, null,
                                        "region_id =" + cursor.getString(10));
                                regionCursor.moveToPosition(0);
                                region = regionCursor.getString(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            regionCursor.close();*/
                           /* String zoneText = "";
                            try {
                                zoneCursor = mySQlAdapter.queueAll(
                                        "survey_zones",
                                        new String[]{"zone_name"}, null,
                                        "zone_id =" + cursor.getString(11));
                                zoneCursor.moveToPosition(0);
                                zoneText = zoneCursor.getString(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            zoneCursor.close();*/


                            String relevanceText = "";
                            if(cursor.getString(26).trim().length()>0) {
                                try {
                                    relevanceCursor = mySQlAdapter.queueAll(
                                            "shop_relevance",
                                            new String[]{"name"}, null,
                                            "id =" + cursor.getString(26));
                                    relevanceCursor.moveToPosition(0);
                                    relevanceText = relevanceCursor.getString(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                relevanceCursor.close();
                            }
                            // loccursor=my
/*                            pro.setProfile(cursor.getString(0),
                                    cursor.getString(1), cursor.getString(2),
                                    cursor.getString(3), cursor.getString(4),
                                    cursor.getString(5), cursor.getString(6),
                                    cursor.getString(7), cursor.getString(8),
                                    cursor.getString(9), cursor.getString(10), cursor.getString(11),
                                    cursor.getString(12), cursor.getString(13), cursor.getString(14),
                                    cursor.getString(15), cursor.getString(16),
                                    cursor.getString(17), cursor.getString(18),
                                    cursor.getString(19), cursor.getString(20),
                                    cursor.getString(21), cursor.getString(22),
                                    cursor.getString(23), cursor.getString(24),
                                    cursor.getString(25), cursor.getString(26), cursor.getString(27))*/
                            ;

                            pro.setProfile(cursor.getString(0),
                                    cursor.getString(1), cacate,
                                    cursor.getString(3), cursor.getString(4),
                                    cursor.getString(5), cursor.getString(6),
                                    cursor.getString(7), cursor.getString(8),
                                    cursor.getString(9), "", "",
                                    btype, dist, cursor.getString(14),
                                    cursor.getString(15), cursor.getString(16),
                                    cursor.getString(17), stat,
                                    cursor.getString(19), cursor.getString(20),
                                    cursor.getString(21), cursor.getString(22),
                                    cursor.getString(23), cursor.getString(24),
                                    cursor.getString(25), relevanceText, cursor.getString(27));
                            cursor.close();
                            mySQlAdapter.close();
                            updateView();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            if (cursor != null) {
                                cursor.close();
                            }

                            mySQlAdapter.close();
                            e.printStackTrace();
                        } catch (Exception e) {

                            if (cursor != null) {
                                cursor.close();
                            }
                            mySQlAdapter.close();
                            //Log.e("BACKGROUND_PROC", e.toString());
                        }
                    }
                });
            }
        }.start();
    }

    protected void updateView() {
        customer_shopname.setText(pro.get_customer_shopname());
        customer_phone.setText(pro.get_customer_phone());
        customer_phone2.setText(pro.get_customer_phone2());
        customer_email.setText(pro.get_customer_email());
        customer_name.setText(pro.get_customer_name());
        customer_desgn.setText(pro.get_customer_desg());
        customer_dist.setText(pro.get_customer_dist());
        customer_state.setText(pro.get_customer_state());
        customer_category.setText(pro.get_customer_category());
        customer_businesstype.setText(pro.get_customer_businesstype());
        customer_add1.setText(pro.get_customer_add1());
        customer_add2.setText(pro.get_customer_add2());
        customer_add3.setText(pro.get_customer_add3());
        customer_add4.setText(pro.get_customer_add4());
        customer_add5.setText(pro.get_customer_add5());
        customer_place.setText(pro.get_customer_place());
        viewRelevance.setText(pro.getRelevance());
        String markStatus = pro.get_isMarkedforDeletion();
        if (markStatus == null) {
            markStatus = "";
        }
        if (!markStatus.equals("1")) {
            checkDelete.setChecked(false);
        } else {
            checkDelete.setChecked(true);
        }
        String isVerified = pro.get_isVerified();
        if (isVerified == null) {
            isVerified = "";
        }
        if (isVerified.equals("1")) {
            buttonVerify.setVisibility(View.GONE);
        } else {
            buttonVerify.setVisibility(View.VISIBLE);
        }
        customer_region.setText(pro.get_customer_lat());
        customer_zone.setText(pro.get_customer_lng());
        customer_country.setText(pro.getCustomer_country());
        // customer_department.setText(pro.getCustomer_department());
        //System.out.println("Customer Id " + customer_id);
        customer_department.setText(pro.getZaimus_id());
        if (pro.get_customer_lat().equalsIgnoreCase("0")
                && pro.get_customer_lng().equalsIgnoreCase("0")) {
            btnGPSCapture.setVisibility(View.VISIBLE);
            titleGPSCapture.setVisibility(View.VISIBLE);
        } else {
            btnGPSCapture.setVisibility(View.INVISIBLE);
            titleGPSCapture.setVisibility(View.INVISIBLE);

        }


        //startSurveyButton.setVisibility(View.VISIBLE);
        if (pro.get_customer_lat().equals("0")
                && pro.get_customer_lng().equals("0")) {
           /* alertbox("Error",
                    "Please update your GPS location by clicking GPS Capture button.");*/
        } else {
            startSurveyButton.setVisibility(View.VISIBLE);

        }

    }

    protected void InitializeComponents() {
        customer_name = (TextView) findViewById(R.id.viewcustname);
        customer_desgn = (TextView) findViewById(R.id.viewcustdesg);
        customer_shopname = (TextView) findViewById(R.id.viewshopname);
        customer_dist = (TextView) findViewById(R.id.viewdistrict);
        customer_state = (TextView) findViewById(R.id.viewstate);
        customer_category = (TextView) findViewById(R.id.viewcategory);
        customer_businesstype = (TextView) findViewById(R.id.viewbtype);
        customer_add1 = (TextView) findViewById(R.id.viewaddr1);
        customer_add2 = (TextView) findViewById(R.id.viewaddr2);
        customer_add3 = (TextView) findViewById(R.id.viewaddr3);
        customer_add4 = (TextView) findViewById(R.id.viewaddr4);
        customer_add5 = (TextView) findViewById(R.id.viewaddr5);
        customer_place = (TextView) findViewById(R.id.viewplace);
        customer_region = (TextView) findViewById(R.id.viewregion);
        customer_zone = (TextView) findViewById(R.id.viewzone);
        customer_phone = (TextView) findViewById(R.id.viewphone);
        customer_phone2 = (TextView) findViewById(R.id.viewphone2);
        customer_email = (TextView) findViewById(R.id.viewemail);

        viewRelevance = (TextView) findViewById(R.id.viewRelevance);
        checkDelete = (CheckBox) findViewById(R.id.checkDeletion);

        buttonVerify = (Button) findViewById(R.id.buttonVerify);
        customer_country = (TextView) findViewById(R.id.viewcountry);
        customer_department = (TextView) findViewById(R.id.viewdepartment);
        startSurveyButton = (Button) findViewById(R.id.startSurveyButton);
        startSurveyButton.setVisibility(View.GONE);
        editUserButton = (Button) findViewById(R.id.editUserButton);
        btnGPSCapture = (ImageView) findViewById(R.id.btngps_capture);
        titleShopName = (TextView) findViewById(R.id.titleShopName);
        titleCustName = (TextView) findViewById(R.id.titleCustName);
        titleCustDesg = (TextView) findViewById(R.id.titleCustDesg);
        titleState = (TextView) findViewById(R.id.titleState);
        titleDistrict = (TextView) findViewById(R.id.titleDistrict);
        titleCountry = (TextView) findViewById(R.id.titleCountry);
        titleDepartment = (TextView) findViewById(R.id.titleDepartment);
        titleCategory = (TextView) findViewById(R.id.titleCategory);
        titleBtype = (TextView) findViewById(R.id.titleBtype);
        titleAdd1 = (TextView) findViewById(R.id.titleAdd1);
        titleAdd2 = (TextView) findViewById(R.id.titleAdd2);
        titleAdd3 = (TextView) findViewById(R.id.titleAdd3);
        titleAdd4 = (TextView) findViewById(R.id.titleAdd4);
        titleAdd5 = (TextView) findViewById(R.id.titleAdd5);
        titlePhone = (TextView) findViewById(R.id.titlePhone);
        titlePhone2 = (TextView) findViewById(R.id.titlePhone2);
        titleEmailId = (TextView) findViewById(R.id.titleEmailId);
        titlePlace = (TextView) findViewById(R.id.titlePlace);
        titleRegion = (TextView) findViewById(R.id.titleRegion);
        titleZone = (TextView) findViewById(R.id.titleZone);
        titleGPSCapture = (TextView) findViewById(R.id.viewgpscapture);
        titleShopName.setTypeface(typefacebold);
        titleCustName.setTypeface(typefacebold);
        titleCustDesg.setTypeface(typefacebold);
        titleState.setTypeface(typefacebold);
        titleDistrict.setTypeface(typefacebold);
        titleCountry.setTypeface(typefacebold);
        titleDepartment.setTypeface(typefacebold);
        titleCategory.setTypeface(typefacebold);
        titleBtype.setTypeface(typefacebold);
        titleAdd1.setTypeface(typefacebold);
        titleAdd2.setTypeface(typefacebold);
        titleAdd3.setTypeface(typefacebold);
        titleAdd4.setTypeface(typefacebold);
        titleAdd5.setTypeface(typefacebold);
        titlePhone.setTypeface(typefacebold);
        titlePhone2.setTypeface(typefacebold);
        titleEmailId.setTypeface(typefacebold);
        titlePlace.setTypeface(typefacebold);
        titleRegion.setTypeface(typefacebold);
        titleZone.setTypeface(typefacebold);

        customer_name.setTypeface(typeface);
        customer_desgn.setTypeface(typeface);
        customer_shopname.setTypeface(typeface);
        customer_dist.setTypeface(typeface);
        customer_state.setTypeface(typeface);
        customer_category.setTypeface(typeface);
        customer_businesstype.setTypeface(typeface);
        customer_add1.setTypeface(typeface);
        customer_add2.setTypeface(typeface);
        customer_add3.setTypeface(typeface);
        customer_add4.setTypeface(typeface);
        customer_add5.setTypeface(typeface);
        customer_place.setTypeface(typeface);
        customer_region.setTypeface(typeface);
        customer_zone.setTypeface(typeface);
        customer_phone.setTypeface(typeface);
        customer_phone2.setTypeface(typeface);
        customer_email.setTypeface(typeface);
        customer_country.setTypeface(typeface);
        customer_department.setTypeface(typeface);

        startSurveyButton.setTypeface(typeface);
        editUserButton.setTypeface(typeface);
        checkDelete.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                String markedDeletion = pro.get_isMarkedforDeletion();
                if (markedDeletion == null) {
                    markedDeletion = "";
                }
                if (isChecked && markedDeletion.equals("")) {
                    showDeletionAlert();
                } else {
                    mySQlAdapter = new SQLiteAdapter(
                            getApplicationContext());
                    mySQlAdapter.openToWrite();
                    mySQlAdapter.update_UserDeletion(customer_id,
                            "");
                    mySQlAdapter.close();
                }

            }
        });

        buttonVerify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showVerifyAlert();
            }
        });
        btnGPSCapture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // gps = new GPSTracker(context);
                double longitude;
                double latitude;
                LocationTrack locationTrack = new LocationTrack(ViewUserActivity.this);


                if (locationTrack.canGetLocation()) {


                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();

                    startSurveyButton.setVisibility(View.VISIBLE);
                    // gps.getLocation();
                   /* lat = latitude;
                    lng = gps.getLongitude();
*/


                    String latitude_str = Double.toString(latitude);
                    String longitude_str = Double.toString(longitude);

                    customer_region.setText(latitude_str);
                    customer_zone.setText(longitude_str);

                    mySQlAdapter = new SQLiteAdapter(getApplicationContext());
                    mySQlAdapter.openToWrite();
                    sdetails.cus_id = customer_id;
                    sdetails.latitude = latitude_str;
                    sdetails.longitude = longitude_str;
                    mySQlAdapter.addGpsCoordinatesToDb(sdetails);
                    mySQlAdapter.close();

                    Toast.makeText(context, "Your GPS location is updated..",
                            Toast.LENGTH_SHORT).show();
                    if (sdetails.latitude.equalsIgnoreCase("0")
                            && sdetails.longitude.equalsIgnoreCase("0")) {
                        btnGPSCapture.setVisibility(View.VISIBLE);
                        titleGPSCapture.setVisibility(View.VISIBLE);
                    } else {
                        btnGPSCapture.setVisibility(View.INVISIBLE);
                        titleGPSCapture.setVisibility(View.INVISIBLE);

                    }
                } else {

                    locationTrack.showSettingsAlert();
                }

                /*   if (gps.isGPSEnabled) {

                 *//*    Location location = null;

                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationManager locationManager = (LocationManager) context
                                .getSystemService(LOCATION_SERVICE);

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, ViewUserActivity.this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            } else {
                                onLocationChanged(location);
                            }
                        }
                    }
*//*

                } else {
                    alertbox("GPS Error",
                            "Go Settings and enable your location!!");
                }*/
            }
        });
        startSurveyButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                /*
                 * startActivityForResult(new Intent(getApplicationContext(),
                 * SurveySetActivity.class).putExtra("Id", customer_id),
                 * UserValues.VIEW_USER_REQ);
                 */


                startActivityForResult(new Intent(getApplicationContext(),
                                SurveyListActivity.class).putExtra("Id", customer_id),
                        UserValues.VIEW_USER_REQ);
                /*
                 * Intent intent=new
                 * Intent(ViewUserActivity.this,SurveyListActivity.class);
                 * startActivity(intent);
                 */
            }
        });

        editUserButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        EditUserActivity.class);
                i.putExtra("Id", customer_id);
                startActivityForResult(i, UserValues.VIEW_USER_REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UserValues.VIEW_USER_REQ:
                if (resultCode == UserValues.EDIT_USER_RES) {
                    setResult(UserValues.EDIT_USER_RES);
                    finish();
                }
                if (resultCode == UserValues.SURVEY_RES) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    protected void showDeletionAlert() {
        new AlertDialog.Builder(this)
                .setMessage(
                        "Do you really want to mark this customer for deletion?")
                .setTitle("Note")
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        checkDelete.setChecked(false);
                        checkDelete.setSelected(false);
                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // isCustomerListsforExport();
                                mySQlAdapter = new SQLiteAdapter(
                                        getApplicationContext());
                                mySQlAdapter.openToWrite();
                                mySQlAdapter.update_UserDeletion(customer_id,
                                        "1");
                                mySQlAdapter.close();
                            }
                        }).show();
    }

    protected void showVerifyAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to verify this customer?")
                .setTitle("Note")
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
                                // isCustomerListsforExport();
                                mySQlAdapter = new SQLiteAdapter(
                                        getApplicationContext());
                                mySQlAdapter.openToWrite();
                                mySQlAdapter
                                        .update_UserVerify(customer_id, "1");
                                mySQlAdapter.close();
                                buttonVerify.setVisibility(View.GONE);
                            }
                        }).show();
    }

    @Override
    public void onLocationChanged(Location location) {
       /*double lat = location.getLatitude();
        double lng = location.getLongitude();
        String latitude_str = Double.toString(lat);
        String longitude_str = Double.toString(lng);

        customer_region.setText(latitude_str);
        customer_zone.setText(longitude_str);*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Here i am given a single just pass the latitude and longitude in this function then you got all the information related to this latitude and longitude.


}