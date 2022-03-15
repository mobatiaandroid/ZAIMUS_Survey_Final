package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.api.IVkcApis;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import java.sql.Timestamp;
import java.util.Date;

import androidx.core.app.ActivityCompat;

public class AddUserToPlan extends Activity implements LocationListener {

    private SQLiteAdapter mySQlAdapter;
    private Button addUserButton;
    private EditText customer_name;
    private EditText customer_desgn;
    private Spinner customer_category;
    private EditText customer_add1;
    private EditText customer_add2;
    private EditText customer_add3;
    private EditText customer_add4;
    private EditText customer_add5;
    private EditText customer_phone;
    private EditText customer_place;
    private TextView heading;
    /*
     * private Spinner customer_region; private Spinner customer_zone;
     */
    private Spinner customer_businesstype;
    private Spinner customer_dist;
    private Spinner customer_state;
    private EditText customer_country;
    private EditText customer_department;
    private String[][] states;
    private String[][] districts;
    private String[][] btypes;
    private String[][] categs;
    Context context = this;
    private String[][] zones;
    private String[][] regions;

    private String[] dist;
    private EditText customer_shopname;
    private EditText customer_phone2;
    private EditText customer_email;
    private SQLiteAdapter mySQLiteAdapter;
    // private ArrayAdapter<String> distAdapter;
    private LocationManager lm;
    public static double LATITUDE = 0.0000;
    public static double LONGITUDE = 0.0000;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Typeface typeface;
    MySpinnerAdapter statAdapter, distAdapter, cateAdapter, btyAdapter;
    String planId, detailId, district;
    LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.register_user);

        typeface = Utils.setFontTypeToArial(context);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "Add Customer");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {

        } else {
            planId = extras.getString("planId");
            detailId = extras.getString("detailId");
            //district=extras.getString("dist");
        }
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddUserToPlan.this.finish();

            }
        });
        locationTrack = new LocationTrack(AddUserToPlan.this);

        InitializeComponents();
        getDBData();
        startListening();
        // customer_region.setEnabled(false);
        // customer_zone.setEnabled(false);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

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

    private Runnable returnRes = new Runnable() {
        // All questions loaded
        @Override
        public void run() {
            /*
             * ZONES NEW FEATURE
             *
             * String[] zone = new String[zones.length]; for (int i = 0; i <
             * zones.length; i++) { zone[i] = zones[i][0]; }
             * ArrayAdapter<String> zoneAdapter = new
             * ArrayAdapter<String>(getApplicationContext
             * (),android.R.layout.simple_spinner_item, zone);
             * customer_zone.setAdapter(zoneAdapter);
             */

            /* REGIONS NEW FEATURE */
            /*
             * String[] region = new String[regions.length]; for (int i = 0; i <
             * regions.length; i++) { region[i] = regions[i][0]; }
             * ArrayAdapter<String> regionAdapter = new
             * ArrayAdapter<String>(getApplicationContext
             * (),android.R.layout.simple_spinner_item, region);
             * customer_region.setAdapter(regionAdapter);
             */

            String[] stat = new String[states.length];
            for (int i = 0; i < states.length; i++) {
                stat[i] = states[i][0];
            }

            statAdapter = new MySpinnerAdapter(context,
                    R.layout.simple_spinner_item, stat);
            customer_state.setAdapter(statAdapter);
            // customer_state.setty
            String s = AppPreferenceManager.getStateName(context);
            int k = statAdapter.getPosition(s);
            //System.out.println("POSITION----------->" + k);
            customer_state.setSelection(k);
            customer_state.setEnabled(false);

            dist = new String[districts.length];
            for (int i = 0; i < districts.length; i++) {
                dist[i] = districts[i][0];
            }

            distAdapter = new MySpinnerAdapter(getApplicationContext(),
                    R.layout.simple_spinner_item, dist);
            distAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
            customer_dist.setAdapter(distAdapter);
            district = AppPreferenceManager.getDistFromPlan(context);
            int spinpos = distAdapter.getPosition(district);
            //System.out.println("POSITION 123----------->" + spinpos);
            //System.out.println("District Name----------->" + district);

            ////System.out.println(customer_dist.getItemAtPosition(spinpos));
            customer_dist.setSelection(spinpos);
            //distAdapter.notifyDataSetChanged();
            customer_dist.setEnabled(false);
            String[] categ = new String[categs.length];
            for (int i = 0; i < categs.length; i++) {
                categ[i] = categs[i][0];
            }

            /*
             * ArrayAdapter<String> cateAdapter = new ArrayAdapter<String>(
             * getApplicationContext(), R.layout.simple_spinner_item, categ);
             */
            cateAdapter = new MySpinnerAdapter(getApplicationContext(),
                    R.layout.simple_spinner_item, categ);
            cateAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
            customer_category.setAdapter(cateAdapter);

            String[] btype = new String[btypes.length];
            for (int i = 0; i < btypes.length; i++) {
                btype[i] = btypes[i][0];
            }

            btyAdapter = new MySpinnerAdapter(getApplicationContext(),
                    R.layout.simple_spinner_item, btype);
            btyAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
            customer_businesstype.setAdapter(btyAdapter);
        }
    };

    protected void getDBData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor statecursor = null;
                        Cursor discursor = null;
                        Cursor btycursor = null;
                        Cursor catecursor = null;

                        Cursor zoneCursor = null;
                        Cursor regionCursor = null;
                        // TODO Auto-generated method stub
                        try {
                            mySQlAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQlAdapter.openToRead();
                            try {
                                /* LOAD ZONE */
                                zoneCursor = mySQlAdapter.queueAll(
                                        "survey_zones", new String[]{
                                                "zone_name", "zone_id"},
                                        "zone_name asc", "zone_id="
                                                + IVkcApis.surveySeyZoneValue);
                                zoneCursor.moveToPosition(0);

                                zones = new String[zoneCursor.getCount()][2];
                                for (int i = 0; i < zoneCursor.getCount(); i++) {
                                    zones[i][0] = zoneCursor.getString(0);
                                    zones[i][1] = zoneCursor.getString(1);
                                    zoneCursor.moveToNext();
                                }
                                zoneCursor.close();

                                /* LOAD REGIONS NEW FEATURE */
                                regionCursor = mySQlAdapter.queueAll(
                                        "survey_regions", new String[]{
                                                "region_name", "region_id"},
                                        "region_name asc", null);
                                regionCursor.moveToPosition(0);

                                regions = new String[regionCursor.getCount()][2];
                                for (int i = 0; i < regionCursor.getCount(); i++) {
                                    regions[i][0] = regionCursor.getString(0);
                                    regions[i][1] = regionCursor.getString(1);
                                    regionCursor.moveToNext();
                                }
                                regionCursor.close();

                                /* load states */
                                statecursor = mySQlAdapter.queueAll(
                                        "survey_states", new String[]{
                                                "state_name", "state_id"},
                                        "state_name asc", null);
                                statecursor.moveToPosition(0);

                                states = new String[statecursor.getCount()][2];
                                for (int i = 0; i < statecursor.getCount(); i++) {
                                    states[i][0] = statecursor.getString(0);
                                    states[i][1] = statecursor.getString(1);
                                    statecursor.moveToNext();
                                }
                                statecursor.close();
                            } catch (Exception e) {
                                statecursor.close();
                            }
                            // Load districts based on initial state
                            try {
                                discursor = mySQlAdapter.queueAll(
                                        "survey_distcts", new String[]{
                                                "dist_name", "dist_id"},
                                        "dist_name asc", "state_id="
                                                + states[0][1]);
                                discursor.moveToPosition(0);
                                districts = new String[discursor.getCount()][2];
                                for (int i = 0; i < discursor.getCount(); i++) {
                                    districts[i][0] = discursor.getString(0);
                                    districts[i][1] = discursor.getString(1);
                                    discursor.moveToNext();
                                }
                                discursor.close();
                            } catch (Exception e) {
                                discursor.close();
                            }
                            btycursor = mySQlAdapter.queueAll(
                                    "survey_customerbtypes", new String[]{
                                            "btype_text", "btype_id"},
                                    "btype_text asc", null);
                            btycursor.moveToPosition(0);
                            btypes = new String[btycursor.getCount()][2];
                            for (int i = 0; i < btycursor.getCount(); i++) {
                                btypes[i][0] = btycursor.getString(0);
                                btypes[i][1] = btycursor.getString(1);
                                btycursor.moveToNext();
                            }
                            btycursor.close();

                            catecursor = mySQlAdapter.queueAll(
                                    "survey_customercategs", new String[]{
                                            "category_name", "category_id"},
                                    "category_name asc", null);
                            catecursor.moveToPosition(0);
                            categs = new String[catecursor.getCount()][2];
                            for (int i = 0; i < catecursor.getCount(); i++) {
                                categs[i][0] = catecursor.getString(0);
                                categs[i][1] = catecursor.getString(1);
                                catecursor.moveToNext();
                            }
                            catecursor.close();
                            mySQlAdapter.close();
                            /*
                             * for (int i = 0; i < regions.length; i++) { if
                             * (regions
                             * [i][1].equalsIgnoreCase(IVkcApis.surveySeyZoneValue
                             * )) { customer_region.setSelection(i); } }
                             */

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (!(btycursor == null)) {
                                if (!btycursor.isClosed()) {
                                    btycursor.close();
                                }
                            }
                            if (!(catecursor == null)) {
                                if (!catecursor.isClosed()) {
                                    catecursor.close();
                                }
                            }
                            if (!(statecursor == null)) {
                                if (!statecursor.isClosed())
                                    statecursor.close();
                            }
                            if (!(discursor == null)) {
                                if (!discursor.isClosed())
                                    discursor.close();
                            }
                            mySQlAdapter.close();
                        }
                        runOnUiThread(returnRes);
                    }
                });
            }
        }).start();
    }

    protected void InitializeComponents() {
        addUserButton = (Button) findViewById(R.id.addUser);
        customer_name = (EditText) findViewById(R.id.custName);
        customer_desgn = (EditText) findViewById(R.id.custDesg);
        customer_shopname = (EditText) findViewById(R.id.shopName);
        customer_email = (EditText) findViewById(R.id.emailid);
        customer_phone2 = (EditText) findViewById(R.id.phone2);
        customer_businesstype = (Spinner) findViewById(R.id.btype);
        customer_category = (Spinner) findViewById(R.id.category);
        customer_dist = (Spinner) findViewById(R.id.district);
        customer_state = (Spinner) findViewById(R.id.state);
        customer_add1 = (EditText) findViewById(R.id.addr1);
        customer_add2 = (EditText) findViewById(R.id.addr2);
        customer_add3 = (EditText) findViewById(R.id.addr3);
        customer_add4 = (EditText) findViewById(R.id.addr4);
        customer_add5 = (EditText) findViewById(R.id.addr5);
        customer_country = (EditText) findViewById(R.id.country);
        customer_department = (EditText) findViewById(R.id.departmnt);
        customer_place = (EditText) findViewById(R.id.place);
        customer_phone = (EditText) findViewById(R.id.phone);
        heading = (TextView) findViewById(R.id.textView2);
        addUserButton.setTypeface(typeface);
        addUserButton.setText("Save Customer");
        heading.setTypeface(typeface);
        // customer_department.setText(IVkcApis.surveyType);
        customer_department.setVisibility(View.GONE);
        customer_country.setText("INDIA");
        customer_country.setEnabled(false);
        customer_name.setTypeface(typeface);
        customer_desgn.setTypeface(typeface);
        customer_shopname.setTypeface(typeface);
        customer_email.setTypeface(typeface);
        customer_phone2.setTypeface(typeface);
        customer_add1.setTypeface(typeface);
        customer_add2.setTypeface(typeface);
        customer_add3.setTypeface(typeface);
        customer_add4.setTypeface(typeface);
        customer_add5.setTypeface(typeface);
        customer_country.setTypeface(typeface);
        customer_department.setTypeface(typeface);
        customer_place.setTypeface(typeface);
        customer_phone.setTypeface(typeface);
        /*
         * customer_zone = (Spinner) findViewById(R.id.zone); customer_region =
         * (Spinner) findViewById(R.id.region);
         */

        customer_state.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                Cursor discursor = null;
                try {
                    mySQlAdapter = new SQLiteAdapter(getApplicationContext());
                    mySQlAdapter.openToRead();
                    discursor = mySQlAdapter.queueAll("survey_distcts",
                            new String[]{"dist_name", "dist_id"},
                            "dist_name asc",
                            "state_id="
                                    + states[arg0.getPositionForView(arg1)][1]);
                    discursor.moveToPosition(0);
                    districts = new String[discursor.getCount()][2];
                    for (int i = 0; i < discursor.getCount(); i++) {
                        districts[i][0] = discursor.getString(0);
                        districts[i][1] = discursor.getString(1);
                        //Log.v("", "" + discursor.getString(0));
                        discursor.moveToNext();
                    }
                    discursor.close();
                    mySQlAdapter.close();
                    dist = new String[districts.length];
                    for (int i = 0; i < districts.length; i++) {
                        dist[i] = districts[i][0];
                    }
                    distAdapter = new MySpinnerAdapter(getApplicationContext(),
                            R.layout.simple_spinner_item, dist);
                    distAdapter
                            .setDropDownViewResource(R.layout.simple_spinner_item);
                    customer_dist.setAdapter(distAdapter);
                    district = AppPreferenceManager.getDistFromPlan(context);
                    int spinpos = distAdapter.getPosition(district);

                    customer_dist.setSelection(spinpos);
                    customer_dist.setEnabled(false);

                } catch (Exception e) {
                    discursor.close();
                    mySQlAdapter.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        addUserButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Log.v("", "User Submit Clicked");
                if (validate()) {
                    saveUser();
                }
            }
        });
    }

    protected boolean validate() {
        if (customer_shopname.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Shop Name Required!");
            customer_shopname.requestFocus();
            return false;
        }
        /*
         * else if (customer_name.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Customer Name Required!");
         * customer_name.requestFocus(); return false; } else if
         * (customer_add1.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Address1 Required!");
         * customer_add1.requestFocus(); return false; } else if
         * (customer_add2.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Address2 Required!");
         * customer_add2.requestFocus(); return false; } else if
         * (customer_add3.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Address3 Required!");
         * customer_add3.requestFocus(); return false; } else if
         * (customer_add4.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Address4 Required!");
         * customer_add4.requestFocus(); return false; } else if
         * (customer_add5.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Address5 Required!");
         * customer_add5.requestFocus(); return false; } else if
         * (customer_zone.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Zone Required!"); customer_zone.requestFocus();
         * return false; } else if
         * (customer_region.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Region Required!");
         * customer_region.requestFocus(); return false; }
         */
        else if (districts.length == 0) {
            alertbox("Error", "District Required!");
            customer_dist.requestFocus();
            return false;
        } else if (customer_phone.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Phone Number(Mobile) Required!");
            customer_phone.requestFocus();
            return false;
        } else if (customer_place.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Place Required!");
            customer_place.requestFocus();
            return false;
        }

        /*
         * else if (customer_email.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Email Id Required!");
         * customer_email.requestFocus(); return false; } else if
         * (customer_phone2.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Phone Number (Shop) Required!");
         * customer_phone2.requestFocus(); return false; }
         */
        return true;
    }

    protected void saveUser() {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {

                            // Status 1 for new user and 2 for updated user
                            Timestamp currentTimeString = new Timestamp(
                                    new Date().getTime());

                            long time = currentTimeString.getTime();

                            String custom_cate = "";
                            String custom_btype = "";
                            String custom_dist = "";
                            String custom_state = "";
                            // String custom_region = "";
                            // String custom_zone = "";
                            if (categs.length > 0) {
                                custom_cate = categs[customer_category
                                        .getSelectedItemPosition()][1];
                            }

                            if (btypes.length > 0) {
                                custom_btype = btypes[customer_businesstype
                                        .getSelectedItemPosition()][1];
                            }

                            if (districts.length > 0) {
                                custom_dist = districts[customer_dist
                                        .getSelectedItemPosition()][1];
                            }

                            if (states.length > 0) {
                                custom_state = states[customer_state
                                        .getSelectedItemPosition()][1];
                            }

                            /*
                             * if(zones.length>0) {
                             * custom_zone=zones[customer_zone
                             * .getSelectedItemPosition()][1]; }
                             * if(regions.length>0) {
                             * custom_region=regions[customer_region
                             * .getSelectedItemPosition()][1]; }
                             */

                            String[][] data = {
                                    {"status", "1"},
                                    {"customer_id", String.valueOf(time)},
                                    {"customer_name",
                                            customer_name.getText().toString()},
                                    {"customer_category", custom_cate},
                                    {"customer_add1",
                                            customer_add1.getText().toString()},
                                    {"customer_add2",
                                            customer_add2.getText().toString()},
                                    {"customer_add3",
                                            customer_add3.getText().toString()},
                                    {"customer_add4",
                                            customer_add4.getText().toString()},
                                    {"customer_add5",
                                            customer_add5.getText().toString()},
                                    {"customer_phone",
                                            customer_phone.getText().toString()},
                                    {"customer_place",
                                            customer_place.getText().toString()},
                                    /*
                                     * { "customer_region", custom_region }, {
                                     * "customer_zone", custom_zone },
                                     */
                                    {"customer_businesstype", custom_btype},
                                    {"customer_dist", custom_dist},
                                    {
                                            "customer_shopname",
                                            customer_shopname.getText()
                                                    .toString()},
                                    {
                                            "customer_phone2",
                                            customer_phone2.getText()
                                                    .toString()},
                                    {"customer_email",
                                            customer_email.getText().toString()},
                                    {"customer_status", "1"},
                                    {"customer_desgn",
                                            customer_desgn.getText().toString()},
                                    {"customer_state", custom_state},
                                    {
                                            "customer_department",
                                            customer_department.getText()
                                                    .toString() == null ? ""
                                                    : customer_department
                                                    .getText()
                                                    .toString()},
                                    {
                                            "customer_country",
                                            customer_country.getText()
                                                    .toString() == null ? ""
                                                    : customer_country
                                                    .getText()
                                                    .toString()},
                                    {"latitude",
                                            String.valueOf(locationTrack.getLatitude())},
                                    {"longitude",
                                            String.valueOf(locationTrack.getLongitude())},
                                    {"planId", planId},
                                    {"detailId", detailId}};

                            mySQLiteAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQLiteAdapter.openToWrite();
                            mySQLiteAdapter.insert(data,
                                    "survey_customerdetails");
                            // mySQLiteAdapter.openToRead();
                            mySQLiteAdapter.close();
                            showSuceessMessage("Success",
                                    "Customer Information Added");
                        } catch (Exception e) {
                            //Log.e("BACKGROUND_PROC", e.toString());
                            mySQLiteAdapter.close();
                        }
                    }
                });
            }
        }.start();
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

    protected void showSuceessMessage(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                setResult(UserValues.ADD_USER_RES);
                                finish();
								/*Intent i = new Intent(getApplicationContext(),
										SearchUserActivity.class);
								startActivity(i);*/
                                if (AppPreferenceManager.getFromPlan(context).equals("1")) {
                                    Intent intent = new Intent(AddUserToPlan.this,
                                            ShowCustomers.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(AddUserToPlan.this,
                                            SearchUserActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();
        // initializeLocationName();

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

    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/arial.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(),
        // BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }

}
