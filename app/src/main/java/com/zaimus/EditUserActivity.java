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
import android.os.AsyncTask;
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

import com.zaimus.Profiles.Profile;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.api.IVkcApis;
import com.zaimus.api.VkcApis;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.CustomProgressBar;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import org.json.JSONStringer;

import java.sql.Timestamp;
import java.util.Date;

import androidx.core.app.ActivityCompat;

public class EditUserActivity extends Activity implements LocationListener {

    private Button saveUserButton;
    private SQLiteAdapter mySQLiteAdapter;
    private String[][] zones;
    private String[][] regions;
    static long time;

    private String[][] states;
    private String[][] districts;
    private String[][] btypes;
    private String[][] categs;
    private String[][] shopRelevance;
    private Profile pro = new Profile();
    private String customer_id;
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
    private EditText customer_country;
    private EditText customer_department;
    //private Spinner customer_region;
    //private Spinner customer_zone;
    int relevancePosition = 0;
    private Spinner customer_businesstype, spinner_shopRelevance;
    private Spinner customer_dist;
    private Spinner customer_state;
    private EditText customer_shopname;
    private EditText customer_phone2;
    private EditText customer_email;
    private String status = "2";
    //private ArrayAdapter<String> distAdapter;
    private String[] dist;
    boolean initial = true;
    private LocationManager lm;
    public static double LATITUDE = 0.0000;
    public static double LONGITUDE = 0.0000;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    private TextView heading;
    Typeface typeface;
    Context context = this;
    MySpinnerAdapter statAdapter, distAdapter, cateAdapter, btyAdapter, relevanceAdapter;
    LocationTrack locationTrack;
    static Timestamp currentTimeString;
    static String cust_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setContentView(R.layout.register_user);
        typeface = Utils.setFontTypeToArial(context);

        locationTrack = new LocationTrack(EditUserActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customer_id = extras.getString("Id");
            setContentView(R.layout.register_user);
            header = (LinearLayout) findViewById(R.id.header);
            headermanager = new Headermanager(activity, "Edit Customer");
            headermanager.getHeader(header, 0, false);
            headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
            splitIcon = headermanager.getLeftButton();
            splitIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EditUserActivity.this.finish();

                }
            });
            // startListening();
            InitializeComponents();
            getDBData();
//			getProfile();
            //	customer_region.setEnabled(false);
            //customer_zone.setEnabled(false);
        }
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
                LocationManager.NETWORK_PROVIDER,
                5000,
                0,
                this
        );
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
			/*ZONES NEW FEATURE
			String[] zone = new String[zones.length];
			for (int i = 0; i < zones.length; i++) {
				zone[i] = zones[i][0];
			}
			ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, zone);
			customer_zone.setAdapter(zoneAdapter);
			
			REGIONS NEW FEATURE
			String[] region = new String[regions.length];
			for (int i = 0; i < regions.length; i++) {
				region[i] = regions[i][0];
			}
			ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, region);
			customer_region.setAdapter(regionAdapter);
			*/


            String[] stat = new String[states.length];
            for (int i = 0; i < states.length; i++) {
                stat[i] = states[i][0];
            }

            statAdapter = new MySpinnerAdapter(
                    context,
                    R.layout.simple_spinner_item,
                    stat);
            customer_state.setAdapter(statAdapter);

            String[] categ = new String[categs.length];
            for (int i = 0; i < categs.length; i++) {
                categ[i] = categs[i][0];
            }

            cateAdapter = new MySpinnerAdapter(getApplicationContext(), R.layout.simple_spinner_item,
                    categ);
            cateAdapter
                    .setDropDownViewResource(R.layout.simple_spinner_item);
            customer_category.setAdapter(cateAdapter);

            String[] btype = new String[btypes.length];
            for (int i = 0; i < btypes.length; i++) {
                btype[i] = btypes[i][0];
            }

            btyAdapter = new MySpinnerAdapter(
                    getApplicationContext(), R.layout.simple_spinner_item,
                    btype);
            btyAdapter
                    .setDropDownViewResource(R.layout.simple_spinner_item);
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

    protected void InitializeComponents() {
        saveUserButton = (Button) findViewById(R.id.addUser);
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
        customer_country = (EditText) findViewById(R.id.country);
        customer_department = (EditText) findViewById(R.id.departmnt);
        //customer_department.setText(IVkcApis.surveyType);
        customer_department.setVisibility(View.GONE);

        customer_add1 = (EditText) findViewById(R.id.addr1);
        customer_add2 = (EditText) findViewById(R.id.addr2);
        customer_add3 = (EditText) findViewById(R.id.addr3);
        customer_add4 = (EditText) findViewById(R.id.addr4);
        customer_add5 = (EditText) findViewById(R.id.addr5);
        //customer_zone 		= (Spinner) findViewById(R.id.zone);
        //customer_region 	= (Spinner) findViewById(R.id.region);
        customer_place = (EditText) findViewById(R.id.place);
        customer_phone = (EditText) findViewById(R.id.phone);
        heading = (TextView) findViewById(R.id.textView2);
        heading.setTypeface(typeface);

        customer_country.setEnabled(false);
        spinner_shopRelevance.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relevancePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //customer_country.setText("INDIA");
        saveUserButton.setText("Save Customer");
        saveUserButton.setTypeface(typeface);
        saveUserButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  //Log.v("", "Save User Clicked");
                if (validate()) {
                    updateUser();
                }
            }
        });
    }

    protected void getProfile() {
//		new Thread() {
//			public void run() {
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
        // TODO Auto-generated method stub
        Cursor cursor = null;
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
                    "customer_desgn", "customer_country", "customer_department", "shop_relevance_id", "cust_id"};
            mySQLiteAdapter = new SQLiteAdapter(
                    getApplicationContext());
            mySQLiteAdapter.openToRead();
            cursor = mySQLiteAdapter.queueAll(
                    "survey_customerdetails", columns, null,
                    "customer_id = " + customer_id);
            cursor.moveToPosition(0);

            try {


                pro.setProfile(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17) == null ? "" : cursor
                                .getString(17), cursor
                                .getString(18), cursor
                                .getString(19),
                        cursor.getString(20),
                        cursor.getString(21), null, null, null, null, cursor.getString(22), cursor.getString(23));
                cust_id = cursor.getString(23);
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Cursor discursor = mySQLiteAdapter.queueAll(
                    "survey_distcts", new String[]{
                            "dist_name", "dist_id"},
                    "dist_name asc",
                    "state_id=" + pro.get_customer_state());
            discursor.moveToPosition(0);
            districts = new String[discursor.getCount()][2];
            for (int i = 0; i < discursor.getCount(); i++) {
                districts[i][0] = discursor.getString(0);
                districts[i][1] = discursor.getString(1);
                discursor.moveToNext();
            }
            discursor.close();
            dist = new String[districts.length];
            for (int i = 0; i < districts.length; i++) {
                dist[i] = districts[i][0];
            }

            distAdapter = new MySpinnerAdapter(getApplicationContext(),
                    R.layout.simple_spinner_item, dist);
            distAdapter
                    .setDropDownViewResource(R.layout.simple_spinner_item);
            customer_dist.setAdapter(distAdapter);

            mySQLiteAdapter.close();
            updateFields();
        } catch (Exception e) {
            //Log.e("BACKGROUND_PROC123", e.toString());
            if (!(cursor == null)) {
                cursor.close();
            }
        }
//					}
//				});
//			}
//		}.start();
    }

    protected void getDBData() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
        // TODO Auto-generated method stub
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
        // TODO Auto-generated method stub
        try {
            mySQLiteAdapter = new SQLiteAdapter(
                    getApplicationContext());
            mySQLiteAdapter.openToRead();

            Cursor statecursor = null;
            Cursor zoneCursor = null;
            Cursor regionCursor = null;
            Cursor relevanceCursor = null;
            try {

                /*
                 */
                /*LOAD ZONE*//*

                zoneCursor = mySQLiteAdapter.queueAll(
                        "survey_zones", new String[]{
                                "zone_name", "zone_id"},
                        "zone_name asc", null);
                zoneCursor.moveToPosition(0);

                zones = new String[zoneCursor.getCount()][2];
                // Log.d("TAG", "Zone Cursor-------->" + zoneCursor.getCount());

                for (int i = 0; i < zoneCursor.getCount(); i++) {
                    zones[i][0] = zoneCursor.getString(0);
                    zones[i][1] = zoneCursor.getString(1);
                    // Log.d("TAG", "Zone Cursor-------->" + zoneCursor.getString(0));
                    // Log.d("TAG", "Zone Cursor-------->" + zoneCursor.getString(1));

                    zoneCursor.moveToNext();
                }
                zoneCursor.close();

                */
                /*LOAD REGIONS NEW FEATURE*//*

                regionCursor = mySQLiteAdapter.queueAll(
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
*/

                statecursor = mySQLiteAdapter.queueAll(
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

            Cursor btycursor = mySQLiteAdapter.queueAll(
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

            // Shop Relevance
            relevanceCursor = mySQLiteAdapter.queueAll(
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
            Cursor catecursor = mySQLiteAdapter.queueAll(
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
            mySQLiteAdapter.close();

        } catch (Exception e) {
            //Log.e("BACKGROUND_PROCESS", e.getMessage());
        }
//						runOnUiThread(returnRes);

        String[] stat = new String[states.length];
        for (int i = 0; i < states.length; i++) {
            stat[i] = states[i][0];
        }

        statAdapter = new MySpinnerAdapter(
                context,
                R.layout.simple_spinner_item,
                stat);
        customer_state.setAdapter(statAdapter);

        String[] categ = new String[categs.length];
        for (int i = 0; i < categs.length; i++) {
            categ[i] = categs[i][0];
        }

        cateAdapter = new MySpinnerAdapter(getApplicationContext(), R.layout.simple_spinner_item,
                categ);
        cateAdapter
                .setDropDownViewResource(R.layout.simple_spinner_item);
        customer_category.setAdapter(cateAdapter);

        String[] btype = new String[btypes.length];
        for (int i = 0; i < btypes.length; i++) {
            btype[i] = btypes[i][0];
        }

        btyAdapter = new MySpinnerAdapter(
                getApplicationContext(), R.layout.simple_spinner_item,
                btype);
        btyAdapter
                .setDropDownViewResource(R.layout.simple_spinner_item);
        customer_businesstype.setAdapter(btyAdapter);
        String[] relevance = new String[shopRelevance.length];
        for (int i = 0; i < shopRelevance.length; i++) {
            relevance[i] = shopRelevance[i][0];
        }

        relevanceAdapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.simple_spinner_item, relevance);
        relevanceAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner_shopRelevance.setAdapter(relevanceAdapter);

        getProfile();

//					}
//				});
//			}
//		}).start();
    }

    protected void updateFields() {
        // //Log.v("Id", customer_id);


        customer_name.setText(pro.get_customer_name());
        customer_desgn.setText(pro.get_customer_desg());
        customer_shopname.setText(pro.get_customer_shopname());
        customer_email.setText(pro.get_customer_email());
        customer_phone2.setText(pro.get_customer_phone2());
        customer_add1.setText(pro.get_customer_add1());
        customer_add2.setText(pro.get_customer_add2());
        customer_add3.setText(pro.get_customer_add3());
        customer_add4.setText(pro.get_customer_add4());
        customer_add5.setText(pro.get_customer_add5());
        customer_place.setText(pro.get_customer_place());
        customer_phone.setText(pro.get_customer_phone());

        customer_country.setText(pro.getCustomer_country());
        //	customer_department.setText(pro.getCustomer_department());

        //	customer_zone.setText(pro.get_customer_zone());
        //	customer_region.setText(pro.get_customer_region());

        boolean state_assgn = false;
        for (int i = 0; i < states.length; i++) {
            if (states[i][1].equalsIgnoreCase(pro.get_customer_state())) {
                customer_state.setSelection(i);
                state_assgn = true;
                customer_state.setEnabled(false);
                break;
            }
        }

        if (!state_assgn) {
            initial = false;
        }
        for (int i = 0; i < districts.length; i++) {
            if (districts[i][1].equalsIgnoreCase(pro.get_customer_dist())) {
                customer_dist.setSelection(i);
            }
        }
        if (!pro.get_customer_businesstype().equals("")) {

            for (int i = 0; i < btypes.length; i++) {
                if (btypes[i][1].equalsIgnoreCase(pro.get_customer_businesstype())) {
                    customer_businesstype.setSelection(i);
                }
            }
        }

        if (!pro.getRelevance().equals("")) {

            for (int i = 0; i < shopRelevance.length; i++) {
                if (shopRelevance[i][0].equalsIgnoreCase(pro.getRelevance())) {
                    spinner_shopRelevance.setSelection(i);
                }
            }
        }
        for (int i = 0; i < categs.length; i++) {
            if (categs[i][1].equalsIgnoreCase(pro.get_customer_category())) {
                customer_category.setSelection(i);
            }
        }

   /*     for (int i = 0; i < zones.length; i++) {
            if (zones[i][1].equalsIgnoreCase(pro.get_customer_zone())) {
                //customer_zone.setSelection(i);
            }
        }

        for (int i = 0; i < regions.length; i++) {
            if (regions[i][1].equalsIgnoreCase(pro.get_customer_region())) {
                //customer_region.setSelection(i);
            }
        }*/

   /*     if (!pro.getRelevance().equals("")) {
            for (int i = 0; i < shopRelevance.length; i++) {

                String relevance = shopRelevance[i][1];
                if (shopRelevance[i][1].equalsIgnoreCase(pro.getRelevance())) {
                    spinner_shopRelevance.setSelection(i);
                }
            }
        }*/
 /*       customer_state.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Cursor discursor = null;
                try {
                    if (initial == false) {
                        // //Log.v("", "Selected");
                        mySQLiteAdapter = new SQLiteAdapter(
                                getApplicationContext());
                        mySQLiteAdapter.openToRead();
                        discursor = mySQLiteAdapter.queueAll(
                                "survey_distcts",
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
                        mySQLiteAdapter.close();
                        dist = new String[districts.length];
                        for (int i = 0; i < districts.length; i++) {
                            dist[i] = districts[i][0];
                        }
                        distAdapter = new MySpinnerAdapter(getApplicationContext(),
                                R.layout.simple_spinner_item, dist);
                        distAdapter
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        customer_dist.setAdapter(distAdapter);
                    } else {
                        initial = false;
                    }

                } catch (Exception e) {
                    discursor.close();
                    mySQLiteAdapter.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });*/

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
        (customer_phone2.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "WhatsApp Number Required!");
            customer_phone2.requestFocus();
            return false;
        } else if (customer_phone2.getText().toString().trim().length() < 10) {
            alertbox("Error", "Invalid WhatsApp Number!");
            customer_phone2.requestFocus();
            return false;

        } else if (customer_shopname.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Shop Name Required!");
            customer_shopname.requestFocus();
            return false;


        } else if (customer_name.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Customer Name Required!");
            customer_name.requestFocus();
            return false;
        } else if (customer_add1.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Door No. Required!");
            customer_add1.requestFocus();
            return false;
        } else if
        (customer_add2.getText().toString().equalsIgnoreCase("")) {
            alertbox("Error", "Building Required!");
            customer_add2.requestFocus();
            return false;
        } else if
        (customer_add5.getText().toString().equalsIgnoreCase("")) {
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

    protected void updateUser() {
        if (pro.get_customer_status().equals("1")) {
            status = "1";
        } else {
            status = "2";
        }
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        String custom_cate = "";
                        String custom_btype = "";
                        String custom_dist = "";
                        String custom_state = "";
                        String custom_region = "";
                        String custom_zone = "";
                        String custom_relevance = "";
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
						/*if(zones.length>0)
						{
							custom_zone=zones[customer_zone.getSelectedItemPosition()][1];
						}
						if(regions.length>0)
						{
							custom_region=regions[customer_region.getSelectedItemPosition()][1];
						}*/

                        try {
                            String[][] data = {
                                    {"status", status},
                                    {
                                            "customer_name",
                                            customer_name.getText().toString() == null ? ""
                                                    : customer_name.getText()
                                                    .toString()},
                                    {"customer_category", custom_cate},
                                    {
                                            "customer_add1",
                                            customer_add1.getText().toString() == null ? ""
                                                    : customer_add1.getText()
                                                    .toString()},
                                    {
                                            "customer_add2",
                                            customer_add2.getText().toString() == null ? ""
                                                    : customer_add2.getText()
                                                    .toString()},
                                    {
                                            "customer_add3",
                                            customer_add3.getText().toString() == null ? ""
                                                    : customer_add3.getText()
                                                    .toString()},
                                    {
                                            "customer_add4",
                                            customer_add4.getText().toString() == null ? ""
                                                    : customer_add4.getText()
                                                    .toString()},
                                    {
                                            "customer_add5",
                                            customer_add5.getText().toString() == null ? ""
                                                    : customer_add5.getText()
                                                    .toString()},
                                    {
                                            "customer_phone",
                                            customer_phone.getText().toString() == null ? ""
                                                    : customer_phone.getText()
                                                    .toString()},
                                    {
                                            "customer_place",
                                            customer_place.getText().toString() == null ? ""
                                                    : customer_place.getText()
                                                    .toString()},
                                    {
                                            "customer_region",
                                            custom_region == null ? "" : custom_region
                                    },
                                    {
                                            "customer_zone",
                                            custom_zone == null ? "" : custom_zone
                                    },
                                    {"customer_businesstype", custom_btype},
                                    {"customer_dist", custom_dist},
                                    {
                                            "customer_shopname",
                                            customer_shopname.getText()
                                                    .toString() == null ? ""
                                                    : customer_shopname
                                                    .getText()
                                                    .toString()},
                                    {
                                            "customer_phone2",
                                            customer_phone2.getText()
                                                    .toString() == null ? ""
                                                    : customer_phone2.getText()
                                                    .toString()},
                                    {
                                            "customer_email",
                                            customer_email.getText().toString() == null ? ""
                                                    : customer_email.getText()
                                                    .toString()},
                                    {
                                            "customer_desgn",
                                            customer_desgn.getText().toString() == null ? ""
                                                    : customer_desgn.getText()
                                                    .toString()},
                                    {"customer_state", custom_state},
                                    {
                                            "customer_department",
                                            customer_department.getText().toString() == null ? ""
                                                    : customer_department.getText()
                                                    .toString()},
                                    {
                                            "customer_country",
                                            customer_country.getText().toString() == null ? ""
                                                    : customer_country.getText()
                                                    .toString()},
                                    {"latitude", String.valueOf(locationTrack.getLatitude())},
                                    {"longitude", String.valueOf(locationTrack.getLongitude())},
                                    {"shop_relevance_id",
                                            custom_relevance},
                                    {"cust_id",
                                            cust_id}
                            };

                            String[][] cons = {{"customer_id", customer_id}};
                            mySQLiteAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQLiteAdapter.openToWrite();
                            mySQLiteAdapter.update(data,
                                    "survey_customerdetails", cons);
                            mySQLiteAdapter.close();

                        } catch (Exception e) {
                            //Log.e("BACKGROUND_PROC", e.toString());
                            mySQLiteAdapter.close();
                        }
                    }
                });
            }
        }.start();

        doExportCustomerAsynchTask doTask = new doExportCustomerAsynchTask();
        doTask.execute();
    }

    protected void alertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
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
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                setResult(UserValues.EDIT_USER_RES);

                                Intent i = new Intent(getApplicationContext(),
                                        SurveyListActivity.class);
                                i.putExtra("Id", customer_id);
                                startActivity(i);
                                finish();
                            }
                        }).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();

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
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, String[] items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
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
                        "Customer Information Updated");
                ;
            } else {
                progress.dismiss();

            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


            super.onPreExecute();
            progress.show();


            if (validate()) {
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
                            .value(custom_relevance).key("cust_id")
                            .value(cust_id)
                            .endObject();


                    js.endArray();
                    js.endObject();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            isAdded = VkcApis.export_servey(js.toString(), IVkcApis.CUSTOMER_LIST, context);

            return null;
        }

    }
}
