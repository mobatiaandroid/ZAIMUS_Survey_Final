package com.zaimus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.adapters.CustomerDataAdapter;
import com.zaimus.api.IVkcApis;
import com.zaimus.api.RetrofitAPI;
import com.zaimus.api.VkcApis;
import com.zaimus.api.model.CustomerData;
import com.zaimus.api.model.SearchUserResponseModel;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.CustomProgressBar;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.RecyclerViewTouchListener;
import com.zaimus.manager.Utils;

import org.json.JSONStringer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends Activity implements LocationListener {

    public static double LATITUDE = 0.0000;
    public static double LONGITUDE = 0.0000;
    CustomListViewDialog customDialog;
    Context context = this;
    int relevancePosition;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Typeface typeface;
    static long time;
    static Timestamp currentTimeString;
    MySpinnerAdapter statAdapter, distAdapter, cateAdapter, btyAdapter, relevanceAdapter;
    String planId, detailId;
    LocationTrack locationTrack;
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
    private TextView heading, txtSearch;
    boolean isShop = false;
    /*
     * private Spinner customer_region; private Spinner customer_zone;
     */
    private Spinner customer_businesstype;
    private Spinner customer_dist;
    private Spinner customer_state, spinner_shopRelevance;
    private EditText customer_country;
    private EditText customer_department;
    private String[][] states;
    private String[][] districts;
    private String[][] btypes;
    private String[][] shopRelevance;
    private String[][] categs;
    private String[][] zones;
    private String[][] regions;
    private String[] dist;
    private EditText customer_shopname;
    private EditText customer_phone2;
    private EditText customer_email;
    private SQLiteAdapter mySQLiteAdapter;
    // private ArrayAdapter<String> distAdapter;
    private LocationManager lm;
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
            //  //System.out.println("POSITION----------->" + k);


            //Bibin Comment 21.02.2018
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
            distAdapter.notifyDataSetChanged();

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


            String[] relevance = new String[shopRelevance.length];
            for (int i = 0; i < shopRelevance.length; i++) {
                relevance[i] = shopRelevance[i][0];
            }

            relevanceAdapter = new MySpinnerAdapter(getApplicationContext(),
                    R.layout.simple_spinner_item, relevance);
            relevanceAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
            spinner_shopRelevance.setAdapter(relevanceAdapter);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity = this;
        setContentView(R.layout.activity_new_user);
        isShop = false;
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
        }
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddUserActivity.this.finish();

            }
        });
        locationTrack = new LocationTrack(AddUserActivity.this);

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
                        Cursor relevanceCursor = null;
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
                            relevanceCursor = mySQlAdapter.queueAll(
                                    "shop_relevance", new String[]{
                                            "name", "id"},
                                    "name desc", null);
                            relevanceCursor.moveToPosition(0);

                            // Shop Relevance
                            shopRelevance = new String[relevanceCursor.getCount()][2];
                            for (int i = 0; i < relevanceCursor.getCount(); i++) {
                                shopRelevance[i][0] = relevanceCursor.getString(0);
                                shopRelevance[i][1] = relevanceCursor.getString(1);
                                relevanceCursor.moveToNext();
                            }
                            relevanceCursor.close();
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
        spinner_shopRelevance = (Spinner) findViewById(R.id.spinner_shopRelevance);
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
        txtSearch = (TextView) findViewById(R.id.txtSearch);
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

        txtSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isShop = true;
                if (customer_shopname.getText().toString().trim().length() > 0) {
                    getUserData("", customer_shopname.getText().toString().trim());
                } else {
                    Toast.makeText(activity, "Please enter shop name", Toast.LENGTH_LONG).show();
                }
            }
        });
        spinner_shopRelevance.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relevancePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        customer_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (!isShop) {
                    if (s.length() == 10) {
                        getUserData(s.toString(), "");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // pAdapter.getFilter().filter(s, searchListView);
                // pAdapter.notifyDataSetChanged();
            }
        });
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
                // //Log.v("", "User Submit Clicked");

                if (validate()) {
                    doExportCustomerAsynchTask doTask = new doExportCustomerAsynchTask();
                    doTask.execute();
                }

            }

        });
    }


    public class doExportCustomerAsynchTask extends
            AsyncTask<Void, Integer, Void> {
        JSONStringer js = new JSONStringer();
        final CustomProgressBar progress = new CustomProgressBar(context, R.drawable.loading);

        boolean isAdded = false;

        @Override
        protected void onPostExecute(Void result) {
            if (isAdded) {
                progress.dismiss();
                showSuceessMessage("Success",
                        "Customer Information Added");
            } else {
                progress.dismiss();

            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


            super.onPreExecute();


            saveUser();
            progress.show();

            currentTimeString = new Timestamp(
                    new Date().getTime());
            time = currentTimeString.getTime();
            String custom_cate = "";
            String custom_btype = "";
            String custom_dist = "";
            String custom_state = "";
            String custom_relevance = "";
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
            if (shopRelevance.length > 0) {
                custom_relevance = shopRelevance[spinner_shopRelevance.getSelectedItemPosition()][1];
            }

            try {
                js.object().key("response");
                js.array();

                // //System.out.println("js.toString()"+js.toString());

                // ////Servey results upload json /////


                js.object()
                        .key("customer_id")
                        .value(String.valueOf(time))
                        .key("customer_name")
                        .value(customer_name.getText().toString())
                        .key("customer_category")
                        .value(custom_cate)
                        .key("customer_add1")
                        .value(customer_add1.getText().toString())
                        .key("customer_add2")
                        .value(customer_add2.getText().toString())
                        .key("customer_add3")
                        .value(customer_add3.getText().toString())
                        .key("customer_add4")
                        .value(customer_add4.getText().toString())
                        .key("customer_add5")
                        .value(customer_add5.getText().toString())
                        .key("customer_phone")
                        .value(customer_phone.getText().toString())
                        .key("customer_place")
                        .value(customer_place.getText().toString())

                        .key("customer_businesstype")
                        .value(custom_btype)
                        .key("customer_dist")
                        .value(custom_dist)
                        .key("customer_shopname")
                        .value(customer_shopname.getText()
                                .toString())
                        .key("customer_phone2")
                        .value(customer_phone2.getText()
                                .toString())
                        .key("customer_email")
                        .value(customer_email.getText()
                                .toString())
                        .key("customer_status")
                        .value("1")
                        .key("customer_state")
                        .value(custom_state)
                        .key("customer_desgn")
                        .value(customer_department.getText()
                                .toString() == null ? ""
                                : customer_department
                                .getText()
                                .toString())

                        .key("latitude")
                        .value(String.valueOf(locationTrack.getLatitude()))

                        .key("longitude")
                        .value(String.valueOf(locationTrack.getLongitude()))
                        .key("customer_department")
                        .value(customer_department.getText()
                                .toString() == null ? ""
                                : customer_department
                                .getText()
                                .toString())

                        .key("customer_country")
                        .value(customer_country.getText()
                                .toString() == null ? ""
                                : customer_country
                                .getText()
                                .toString()).key("plan_detailId")
                        .value("")
                        .key("representativeID")
                        .value("")
                        .key("is_verified")
                        .value("1")
                        .key("is_marked_deletion")
                        .value("")
                        .key("shop_relevance_id")
                        .value(custom_relevance)
                        .key("cust_id")
                        .value("")
                        .endObject();


                js.endArray();
                js.endObject();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            isAdded = VkcApis.export_servey(js.toString(), IVkcApis.CUSTOMER_LIST, context);

            return null;
        }

    }

    protected boolean validate() {

        if (customer_phone.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "Mobile Number Required!");
            customer_phone.requestFocus();
            return false;
        } else if (customer_phone.getText().toString().trim().length() < 10) {
            alertbox("Error", "Invalid Mobile !");
            customer_phone.requestFocus();
            return false;

        } else if
        (customer_phone2.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "WhatsApp Number Required!");
            customer_phone2.requestFocus();
            return false;
        } else if (customer_phone2.getText().toString().trim().length() < 10) {
            alertbox("Error", "Invalid WhatsApp Number!");
            customer_phone2.requestFocus();
            return false;

        } else if (customer_shopname.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "Shop Name Required!");
            customer_shopname.requestFocus();
            return false;


        } else if (customer_name.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "Customer Name Required!");
            customer_name.requestFocus();
            return false;
        } else if (customer_add1.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "Door No. Required!");
            customer_add1.requestFocus();
            return false;
        } else if
        (customer_add2.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "Building Required!");
            customer_add2.requestFocus();
            return false;
        } else if
        (customer_add5.getText().toString().trim().equalsIgnoreCase("")) {
            alertbox("Error", "PIN Required!");
            customer_add5.requestFocus();
            return false;
        }/*else if (customer_add1.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Address1 Required!");
		  customer_add1.requestFocus(); return false; } else if
		  (customer_add2.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Address2 Required!");
		  customer_add2.requestFocus(); return false; } else if
		  (customer_add3.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Address3 Required!");
		 customer_add3.requestFocus(); return false; } else if
		  (customer_add4.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Address4 Required!");
		  customer_add4.requestFocus(); return false; } else if
		  (customer_add5.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Address5 Required!");
		  customer_add5.requestFocus(); return false; } *//*else if
		  (customer_zone.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Zone Required!"); customer_zone.requestFocus();
		  return false; } else if
		  (customer_region.getText().toString().equalsIgnoreCase("")) {
		  alertbox("Error", "Region Required!");
		  customer_region.requestFocus(); return false; }*/ else if (districts.length == 0) {
            alertbox("Error", "District Required!");
            customer_dist.requestFocus();
            return false;
        } else if (relevancePosition == 0) {
            alertbox("Error", "Please select shop relevance");
            spinner_shopRelevance.requestFocus();
            return false;
        } else if (customer_place.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Place Required!");
            customer_place.requestFocus();
            return false;
        } else {
            return true;
        }
        /*
         * else if (customer_email.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Email Id Required!");
         * customer_email.requestFocus(); return false; } else if
         * (customer_phone2.getText().toString().equalsIgnoreCase("")) {
         * alertbox("Error", "Phone Number (Shop) Required!");
         * customer_phone2.requestFocus(); return false; }
         */

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


                            System.out.println("Time Data: " + String.valueOf(time));
                            String custom_cate = "";
                            String custom_btype = "";
                            String custom_dist = "";
                            String custom_state = "";
                            String custom_relevance = "";
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
                            if (shopRelevance.length > 0) {
                                custom_relevance = shopRelevance[spinner_shopRelevance.getSelectedItemPosition()][1];
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
                                    {"verify_status",
                                            "1"},
                                    {"shop_relevance_id",
                                            custom_relevance}, {"cust_id", ""}


                            };
                            mySQLiteAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQLiteAdapter.openToWrite();
                            mySQLiteAdapter.insert(data,
                                    "survey_customerdetails");
                            // mySQLiteAdapter.openToRead();
                            mySQLiteAdapter.close();

                            PreferenceManager.saveCustUpdate("no", context);
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

                                Intent intent = new Intent(AddUserActivity.this,
                                        SearchUserActivity.class);
                                startActivity(intent);
                                finish();
                                /*if (AppPreferenceManager.getFromPlan(context).equals("1")) {
                                    Intent intent = new Intent(AddUserActivity.this,
                                            ShowCustomers.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(AddUserActivity.this,
                                            SearchUserActivity.class);
                                    startActivity(intent);
                                }*/
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

    private void getUserData(String mobile, String name) {

        if (Utils.checkInternetConnection(context)) {

            final CustomProgressBar progress = new CustomProgressBar(context, R.drawable.loading);
            progress.show();
            RetrofitAPI.getClient().getUserData(mobile, name).enqueue(new Callback<SearchUserResponseModel>() {
                @Override
                public void onResponse(Call<SearchUserResponseModel> call, Response<SearchUserResponseModel> response) {


                    String response_data = response.body().getResponse();

                    if (response_data.equalsIgnoreCase("Success")) {

                        ArrayList<CustomerData> listData = response.body().getCustomers();
                        if (listData.size() > 0) {

                            progress.dismiss();
                            CustomerDataAdapter adapter = new CustomerDataAdapter(listData, activity);

                            customDialog = new CustomListViewDialog(AddUserActivity.this, adapter, listData);

                            customDialog.show();
                            customDialog.setCanceledOnTouchOutside(false);
                        } else {
                            progress.dismiss();

                        }
                    } else {
                        //dsfsdf
                    }

                }

                @Override
                public void onFailure(Call<SearchUserResponseModel> call, Throwable t) {

                    Toast.makeText(context, "Network error.",
                            Toast.LENGTH_SHORT).show();

                    progress.dismiss();


                }
            });


        }

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


    public class CustomListViewDialog extends Dialog implements View.OnClickListener {


        public CustomListViewDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }


        public Activity activity;
        public Dialog dialog;
        public Button yes, no;
        TextView title;
        RecyclerView recyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        RecyclerView.Adapter adapter;
        ArrayList<CustomerData> custData;

        public CustomListViewDialog(Activity a, RecyclerView.Adapter adapter, ArrayList<CustomerData> custData) {
            super(a);
            this.activity = a;
            this.adapter = adapter;
            this.custData = custData;
            setupLayout();
        }

        private void setupLayout() {

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_customer_list);
            //  yes = (Button) findViewById(R.id.yes);
            //  no = (Button) findViewById(R.id.no);
            //title = findViewById(R.id.title);
            recyclerView = findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);


            recyclerView.setAdapter(adapter);


            recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(activity, recyclerView, new RecyclerViewTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //  MyApplication.customerData = custData.get(position);
                    CustomerData customerData = custData.get(position);
                    dismiss();
                    if (customer_phone.getText().toString().trim().length() == 0 && isShop) {
                        customer_phone.setText(customerData.getPhone());

                    } else if (customer_phone.getText().toString().trim().length() == 0) {
                        customer_phone.setText(customerData.getPhone());

                    }

                    customer_name.setText(customerData.getName());
                    customer_shopname.setText(customerData.getShopName());
                    customer_email.setText(customerData.getEmail());
                    // customer_phone.setText(customerData.getPhone());
                    customer_phone2.setText(customerData.getShopPhone());

                    customer_add1.setText(customerData.getDoorNumber());
                    customer_add2.setText(customerData.getBuilding());
                    customer_add3.setText(customerData.getRoadName());
                    customer_add4.setText(customerData.getPostOffice());
                    customer_add5.setText(customerData.getPinCode());
                    customer_place.setText(customerData.getPlace());

                    customer_country.setText("INDIA");
                    /*for (int i = 0; i < states.length; i++) {
                        if (states[i][1].equalsIgnoreCase(customerData.g)) {
                            customer_state.setSelection(i);
                          //  state_assgn = true;
                            customer_state.setEnabled(false);
                            break;
                        }
                    }
*/
                    for (int i = 0; i < districts.length; i++) {
                        if (districts[i][0].equalsIgnoreCase(customerData.getDistrict())) {
                            customer_dist.setSelection(i);
                        }
                    }

                    for (int i = 0; i < categs.length; i++) {
                        if (categs[i][0].equalsIgnoreCase(customerData.getType())) {
                            customer_category.setSelection(i);
                        }
                    }

                  /*  for (int i = 0; i < btypes.length; i++) {
                        if (btypes[i][1].equalsIgnoreCase(customerData.get_customer_businesstype())) {
                            customer_businesstype.setSelection(i);
                        }
                    }*/
                    /*for (int i = 0; i < shopRelevance.length; i++) {
                        if (shopRelevance[i][0].equalsIgnoreCase(pro.getRelevance())) {
                            spinner_shopRelevance.setSelection(i);
                        }
                    }
                    for (int i = 0; i < categs.length; i++) {
                        if (categs[i][1].equalsIgnoreCase(pro.get_customer_category())) {
                            customer_category.setSelection(i);
                        }
                    }

                    for (int i = 0; i < zones.length; i++) {
                        if (zones[i][1].equalsIgnoreCase(pro.get_customer_zone())) {
                            //customer_zone.setSelection(i);
                        }
                    }*/

                   /* for (int i = 0; i < regions.length; i++) {
                        if (regions[i][1].equalsIgnoreCase(pro.get_customer_region())) {
                            //customer_region.setSelection(i);
                        }
                    }


                    for (int i = 0; i < shopRelevance.length; i++) {

                        String relevance = shopRelevance[i][1];
                        if (shopRelevance[i][1].equalsIgnoreCase(pro.getRelevance())) {
                            spinner_shopRelevance.setSelection(i);
                        }
                    }
*/


                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            //yes.setOnClickListener(this);
            // no.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    //Do Something
                    break;
                case R.id.no:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

}
