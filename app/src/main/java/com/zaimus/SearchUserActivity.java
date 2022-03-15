package com.zaimus;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.Profiles.Profile;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.adapters.ProfileAdapter;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.RecyclerViewTouchListener;
import com.zaimus.manager.Utils;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserActivity extends Activity {

    // Initialize variables for Profile

    ArrayList<Profile> m_profiles;
    public ArrayList<Profile> clist;

    static ProfileAdapter pAdapter;
    private ImageView addNewButton;
    private EditText searchTextEntry;

    ArrayList<Profile> filtered;
    RecyclerView searchListView;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon;
    Activity activity;
    private Context context = this;
    Typeface typeface;
    String conditionString;
    Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        clist = new ArrayList<Profile>();
        clist = mySQLiteAdapter.getAllCustomer();
        mySQLiteAdapter.close();
        setContentView(R.layout.search_user);
        typeface = Utils.setFontTypeToArial(context);
        activity = this;
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "Select Customer");
        headermanager.getHeader(header, 0, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUserActivity.this,
                        VKCAppActivity.class);
				/*
				Intent intent = new Intent(SearchUserActivity.this,
						SettingsActivity.class);*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        initializeComponents();

        addNewButton.requestFocus();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    protected void initializeComponents() {
        // initialize the components
        context = getApplicationContext();
        filtered = new ArrayList<Profile>();
        m_profiles = new ArrayList<Profile>();
        // searchListView = (ListView) findViewById(R.id.searchListview);
        searchListView = (RecyclerView) findViewById(R.id.searchListview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchUserActivity.this);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(SearchUserActivity.this, DividerItemDecoration.HORIZONTAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(SearchUserActivity.this, R.drawable.divider));
        searchListView.setLayoutManager(mLayoutManager);
        searchListView.setItemAnimator(new DefaultItemAnimator());
        searchListView.addItemDecoration(itemDecorator);
        // load profiles


       /* m_profiles = new ArrayList<Profile>();
        pAdapter = new ProfileAdapter(this, m_profiles);
        searchListView.setAdapter(this.pAdapter);*/


        searchTextEntry = (EditText) findViewById(R.id.searchTextEntry);
        // arrad = new ArrayAdapter<String>(this,
        // R.layout.search,R.id.search_item, company_arr);

        // searchListView.setTextFilterEnabled(true);

        addNewButton = (ImageView) findViewById(R.id.RegUserButton);
        addNewButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AppPreferenceManager.saveFromPlan("0", context);

                Intent i = new Intent(getApplicationContext(),
                        AddUserActivity.class);
                startActivityForResult(i, UserValues.ADD_USER_REQ);
            }
        });

        searchTextEntry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                filtered.clear();
                for (int i = 0; i < m_profiles.size(); i++) {
                    if (m_profiles.get(i).get_customer_shopname().toLowerCase()
                            .contains(s.toString()) || m_profiles.get(i).get_customer_phone().toString()
                            .contains(s.toString())
                            || m_profiles.get(i).get_customer_dist()
                            .toLowerCase().contains(s.toString())
                            || m_profiles.get(i).get_customer_place()
                            .toLowerCase().contains(s.toString())
                            || m_profiles.get(i).getZaimus_id()
                            .contains(s.toString())) {
                        filtered.add(m_profiles.get(i));

                    }

                }

                if (searchTextEntry.getText().toString().equalsIgnoreCase("")) {
                    ProfileAdapter pAdapter = new ProfileAdapter(SearchUserActivity.this,
                            m_profiles, clist);
                    searchListView.setAdapter(pAdapter);
                   /* searchListView.setSelection(Integer
                            .parseInt(AppPreferenceManager
                                    .getListPosition(context)));*/
                } else {
                    ProfileAdapter pAdapter = new ProfileAdapter(SearchUserActivity.this,
                            filtered, clist);
                    searchListView.setAdapter(pAdapter);
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

      /*  searchListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                AppPreferenceManager.saveFromPlan("0", context);
                AppPreferenceManager.savePlanDrtailId("0", context);

                TextView item_name = (TextView) view
                        .findViewById(R.id.search_item);
                Intent i = new Intent(getApplicationContext(),
                        ViewUserActivity.class);
                // //System.out.println("ID---------->" + item_name.getTag());
                i.putExtra("Id", "" + item_name.getTag());
                i.putExtra("status", "" + item_name.getTag(R.id.addr1));
                // AppPreferenceManager.saveCustomerId(item_name.getTag().toString(),
                // context);
                AppPreferenceManager.saveListPosition(String.valueOf(position),
                        context);
                ////Log.e("", "status" + item_name.getTag(R.id.addr1));
                startActivityForResult(i, UserValues.VIEW_USER_REQ);
            }
        });*/


        searchListView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), searchListView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                AppPreferenceManager.saveFromPlan("0", context);
                AppPreferenceManager.savePlanDrtailId("0", context);

                TextView item_name = (TextView) view
                        .findViewById(R.id.search_item);
                Intent i = new Intent(getApplicationContext(),
                        ViewUserActivity.class);
                // //System.out.println("ID---------->" + item_name.getTag());

/*                if (filtered.size() > 0) {
                    i.putExtra("Id", filtered.get(position).get_customer_id());
                    i.putExtra("status", filtered.get(position).get_customer_status());
                } else {*/
                i.putExtra("Id", m_profiles.get(position).get_customer_id());
                i.putExtra("status", m_profiles.get(position).get_customer_status());
                //}
                AppPreferenceManager.saveListPosition(String.valueOf(position),
                        context);
                startActivityForResult(i, UserValues.VIEW_USER_REQ);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        showSearchDialog();

       /* BackgroundAsyncTask getCustomerData = new BackgroundAsyncTask();
        getCustomerData.execute();*/
    }



   /* private class ProfileAdapter extends ArrayAdapter<Profile> {

        @SuppressWarnings("unused")
        private Context context;
        private ArrayList<Profile> profiles;
        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = vi.inflate(R.layout.search_list, null);
            } else {
                // holder = (ViewHolder) convertView.getTag();

            }
            Profile pro = profiles.get(position);
            holder = new ViewHolder();
            if (pro != null) {
                holder.shopName = (TextView) convertView
                        .findViewById(R.id.search_item);
                holder.searchLAyout = (LinearLayout) convertView
                        .findViewById(R.id.searchLAyout);
                holder.shopName.setTypeface(typeface);
                if (holder.shopName != null) {
                    holder.shopName.setText(pro.get_customer_shopname().toUpperCase() + ","
                            + pro.get_customer_add1().toUpperCase() + ","
                            + pro.get_customer_place().toUpperCase() + ","
                            + pro.get_customer_dist().toUpperCase() + ","
                            + pro.get_customer_id().toUpperCase());
                    holder.shopName.setTag(pro.get_customer_id());
                    holder.shopName.setTag(R.id.addr1,
                            pro.get_customer_status());
                }

                String id = profiles.get(position).customer_id;
                holder.searchLAyout.setBackgroundColor(Color.TRANSPARENT);
                for (int j = 0; j < clist.size(); j++) {

                    String c_id = clist.get(j).customer_id;

                    if (id.equals(c_id)) {
                        holder.searchLAyout.setBackgroundColor(Color.rgb(144,
                                238, 144));
                    } else {

                    }
                }

            }
            return convertView;
        }

        private class ViewHolder {
            TextView shopName;
            LinearLayout searchLAyout;
        }

        public ProfileAdapter(Context context, int textViewResourceId,
                              ArrayList<Profile> profiles) {
            super(context, textViewResourceId, profiles);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.profiles = profiles;
        }


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserValues.ADD_USER_REQ) {
            if (resultCode == UserValues.ADD_USER_RES) {
                finish();
            }
        } else if (requestCode == UserValues.VIEW_USER_REQ) {
            if (resultCode == UserValues.EDIT_USER_RES) {
                finish();
            }
        }
    }

    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // conditionString = "";
            // mProgressDialog.dismiss();

            if (m_profiles.size() > 0) {
                pAdapter = new ProfileAdapter(SearchUserActivity.this,
                        m_profiles, clist);
                searchListView.setAdapter(pAdapter);
                customDialog.dismiss();
            } else {
                Toast.makeText(SearchUserActivity.this, "No records found...", Toast.LENGTH_LONG).show();
                if (customDialog.isShowing()) {

                } else {
                    customDialog.show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
/*            mProgressDialog = ProgressDialog.show(SearchUserActivity.this,
                    "Please wait...", "Retrieving data ...", true);*/
            m_profiles.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(context);
                mySQLiteAdapter.openToRead();
                String[] columns = new String[]{"customer_shopname",
                        "customer_place", "customer_id", "customer_dist",
                        "customer_add1", "customer_state", "customer_phone", "customer_category", "cust_id", "customer_phone2"};

                Cursor cursor = mySQLiteAdapter.queueAll(
                        "survey_customerdetails", columns,
                        "customer_shopname asc", conditionString);
                cursor.moveToPosition(0);
                String[] col = new String[]{"state_name"};
                Cursor Scursor = mySQLiteAdapter.queueAll("survey_states", col,
                        "state_id asc", "state_id= " + cursor.getString(5));// cursor.getString(5)
                Scursor.moveToPosition(0);
                //System.out.println("inside BackgroundAsyncTask ");
                // //System.out.println("State Name New=  "+mySQLiteAdapter.getStateName());

                ////System.out.println("State Name= " + Scursor.getString(0));
			/*	AppPreferenceManager.saveStateName(Scursor.getString(0), Bibin Save State
						context);*/
                Scursor.close();
                while (cursor.isAfterLast() == false) {
                    String dist = "";
                    if (isParsableToInt(cursor.getString(3))) {
                        Cursor distCursor = null;
                        try {
                            distCursor = mySQLiteAdapter.queueAll(
                                    "survey_distcts",
                                    new String[]{"dist_name"}, null,
                                    "dist_id =" + cursor.getString(3));
                            distCursor.moveToPosition(0);

                            if (distCursor.isAfterLast() == false) {
                                dist = distCursor.getString(0);
                            }
                            distCursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            distCursor.close();
                        }
                    }
                    Profile pro = new Profile();
                    String shop = cursor.getString(0);
					/*if (cursor.getString(0).contains("'")) {
						shop = shop.replaceAll("'", "''");
					}*/

                    pro.setProfile(cursor.getString(2), "", cursor.getString(7),
                            cursor.getString(4), "", "", "", "", cursor.getString(6),
                            cursor.getString(1), "", "", "", dist,
                            shop, "", "", "", "", "", "",
                            "", "", "", "", "", "", cursor.getString(8));
//Add only retailer data
                   /* if (cursor.getString(7).equals("10")) {
                        m_profiles.add(pro);
                    }*/

                    m_profiles.add(pro);

                    cursor.moveToNext();

                }
                cursor.close();
                mySQLiteAdapter.close();
                //	//Log.i("ARRAY", "" + m_profiles.size());
            } catch (Exception e) {

                System.out.println("Exception " + e);
                System.out.println("Exception " + e);


                //Log.e("BACKGROUND_PROC_SEARCH", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    public boolean isParsableToInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Intent intent = new Intent(SearchUserActivity.this,
                VKCAppActivity.class);
				/*
				Intent intent = new Intent(SearchUserActivity.this,
						SettingsActivity.class);*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    /*
     * @Override protected void onRestart() { // TODO Auto-generated method stub
     * super.onRestart(); startActivity(getIntent()); finish(); }
     */

    @Override
    protected void onResume() {
        super.onResume();
        //runOnUiThread(returnRes);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
       /* BackgroundAsyncTask bb = new BackgroundAsyncTask();
        bb.execute();*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private void showSearchDialog() {
        customDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.getWindow().setBackgroundDrawableResource(R.color.white);

        customDialog.setContentView(R.layout.dialog_search);
        final EditText edtCustId, edtMobile, edtWhatsapp, edtShopName, edtState, edtDistrict, edtPlace;
        Button ok = (Button) customDialog.findViewById(R.id.btnSearch);
        edtCustId = (EditText) customDialog.findViewById(R.id.edtCustID);
        edtMobile = (EditText) customDialog.findViewById(R.id.edtCustMobile);
        edtWhatsapp = (EditText) customDialog.findViewById(R.id.edtCustWhatsApp);
        edtShopName = (EditText) customDialog.findViewById(R.id.edtCustShopName);
        edtState = (EditText) customDialog.findViewById(R.id.edtCustState);
        edtDistrict = (EditText) customDialog.findViewById(R.id.edtCustDistrict);
        edtPlace = (EditText) customDialog.findViewById(R.id.edtCustPlace);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //done what do you want to do
                if (edtCustId.getText().toString().trim().equals("")
                        && edtDistrict.getText().toString().trim().equals("")
                        && edtMobile.getText().toString().trim().equals("")
                        && edtWhatsapp.getText().toString().trim().equals("")
                        && edtShopName.getText().toString().trim().equals("")
                        && edtState.getText().toString().trim().equals("")
                        && edtPlace.getText().toString().trim().equals("")) {

                    Toast.makeText(SearchUserActivity.this, "Please enter atleast one value for searching", Toast.LENGTH_LONG).show();

                } else {
                /*    filtered.clear();
                    for (int i = 0; i < m_profiles.size(); i++) {
                        if (m_profiles.get(i).getZaimus_id().contains(edtCustId.getText().toString().trim())
                                || m_profiles.get(i).get_customer_dist().contains(edtDistrict.getText().toString().trim())
                                || m_profiles.get(i).get_customer_phone().contains(edtMobile.getText().toString().trim())
                                || m_profiles.get(i).get_customer_phone2().contains(edtWhatsapp.getText().toString().trim())
                                || m_profiles.get(i).get_customer_shopname().contains(edtShopName.getText().toString().trim())
                                || m_profiles.get(i).get_customer_state().contains(edtState.getText().toString().trim())
                                || m_profiles.get(i).get_customer_place().contains(edtPlace.getText().toString().trim())
                        ) {
                            filtered.add(m_profiles.get(i));
                        }
                    }*/

                    conditionString = "";
                    if (edtCustId.getText().toString().trim().length() > 0) {

                        if (conditionString.isEmpty()) {
                            conditionString = " cust_id like '%" + edtCustId.getText().toString().trim()
                                    + "%'";
                        } else {
                            conditionString = conditionString + " and cust_id like '%" + edtCustId.getText().toString().trim()
                                    + "%'";
                        }

                    }
                    if (edtMobile.getText().toString().trim().length() > 0) {
                        if (conditionString.isEmpty()) {
                            conditionString = " customer_phone like '%" + edtMobile.getText().toString().trim()
                                    + "%'";
                        } else {
                            conditionString = conditionString + " and customer_phone like '%" + edtMobile.getText().toString().trim()
                                    + "%'";
                        }

                    }

                    if (edtWhatsapp.getText().toString().trim().length() > 0) {
                        if (conditionString.isEmpty()) {
                            conditionString = " customer_phone2 like '%" + edtWhatsapp.getText().toString().trim()
                                    + "%'";
                        } else {
                            conditionString = conditionString + " and customer_phone2 like '%" + edtWhatsapp.getText().toString().trim()
                                    + "%'";
                        }

                    }

                    if (edtShopName.getText().toString().trim().length() > 0) {
                        if (conditionString.isEmpty()) {
                            conditionString = " customer_shopname like '%" + edtShopName.getText().toString().trim()
                                    + "%'";
                        } else {
                            conditionString = conditionString + " and customer_shopname like '%" + edtShopName.getText().toString().trim()
                                    + "%'";
                        }

                    }

                    if (edtPlace.getText().toString().trim().length() > 0) {
                        if (conditionString.isEmpty()) {
                            conditionString = " customer_place like '%" + edtPlace.getText().toString().trim()
                                    + "%'";
                        } else {
                            conditionString = conditionString + " and customer_place like '%" + edtPlace.getText().toString().trim()
                                    + "%'";
                        }

                    }

                    /*conditionString = "cust_id like '%" + edtCustId.getText().toString().trim()
                            + "%' or customer_shopname like '%" + edtShopName.getText().toString().trim()
                            + "%' or customer_place like '%" + edtPlace.getText().toString().trim()
                            // + "' or customer_dist= '" + edtDistrict.getText().toString().trim()
                            // + "' or customer_state= '" + edtState.getText().toString().trim()
                            + "%' or customer_phone like '%" + edtMobile.getText().toString().trim()
                            + "%' or customer_phone2 like '%" + edtWhatsapp.getText().toString().trim()
                            + "%'";
*/
                    //  System.out.println("Condition " + conditionString);
                    BackgroundAsyncTask getCustomerData = new BackgroundAsyncTask();
                    getCustomerData.execute();
                    //   if (m_profiles.size() > 0) {

                    //Toast.makeText(SearchUserActivity.this, "Records found..." + m_profiles.size(), Toast.LENGTH_LONG).show();

                    /*pAdapter = new ProfileAdapter(SearchUserActivity.this,
                            m_profiles, clist);
                    searchListView.setAdapter(pAdapter);*/
                    customDialog.dismiss();
                    //  } else {
                    // Toast.makeText(SearchUserActivity.this, "No records found...", Toast.LENGTH_LONG).show();

                    //   }

                }
            }
        });


        Button cancel = (Button) customDialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //done what do you want to do
                customDialog.dismiss();
                finish();
            }
        });

        customDialog.show();

    }
}

