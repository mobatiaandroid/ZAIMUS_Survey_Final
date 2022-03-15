package com.zaimus;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.Profiles.Profile;
import com.zaimus.SQLiteServices.DatabaseHelper;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveySetModel;
import com.zaimus.SurveyTypes.Decision;
import com.zaimus.adapters.SurveySetAdapter;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

public class SurveyListActivity extends ListActivity {
    public ArrayList<Profile> filtered;
    private ListView surveySet;
    private SurveySetAdapter adapter;
    private SQLiteAdapter sqLiteAdapter;
    private String customer_id;
    private SurveySetModel surveySetModel;
    Context context = this;
    ArrayList<Survey> myAList = new ArrayList<Survey>();
    ArrayList<String> arrayList = new ArrayList<String>();
    Survey survey;
    String jsonStr, userId, stateId, surveysetId, roleId;
    JSONObject jobject;
    // private ProgressBar progress;
    private Boolean PROCESS_START_FLAG = false;
    private SQLiteAdapter mySQLiteAdapter;
    Bitmap bitmap;
    ProgressDialog pDialog;
    ProgressDialog Dialog;
    // stateId,surveysetId,userId,roleId

    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity;
    private TextView surveyName;
    SurveyListAdapter listAdapter;
    public ArrayList<Survey> surveylist = new ArrayList<Survey>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.surveyset);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customer_id = extras.getString("Id");
        }
        activity = this;
        header = (LinearLayout) findViewById(R.id.header);
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        headermanager = new Headermanager(activity, "Select Survey");
        headermanager.getHeader(header, 0, false);
        //System.out.println("oncreate surveylist activity------->");
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // AppPreferenceManager.saveFlag("0", context);
                SurveyListActivity.this.finish();
                /*
                 * Intent intent=new
                 * Intent(SurveyListActivity.this,VKCAppActivity.class);
                 * startActivity(intent);
                 */
            }
        });
        //checkDatabase();

        try {
            initializeUI();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initializeUI() throws JSONException {
        // TODO Auto-generated method stub
        // surveySet = (ListView) findViewById(R.id.surveyset_list);
        surveySet = getListView();
        // progress = (ProgressBar) findViewById(R.id.progressBar1);

        // myAList=AppPreferenceManager.getArray(context);
        surveylist = mySQLiteAdapter.getAllSurvey();

        survey = new Survey();
        /*
         * survey.survey_name="Trivandrum Business Survey";
         * surveylist.add(survey); survey=new Survey();
         * survey.survey_name="Chennai Marketing Survey";
         * surveylist.add(survey);
         */
        if (surveylist.size() > 0) {
            listAdapter = new SurveyListAdapter(context, surveylist);
            surveySet.setAdapter(listAdapter);
        } else {
            Toast.makeText(context, "Please import customer data!!",
                    Toast.LENGTH_SHORT).show();
        }
        /*
         * if(AppPreferenceManager.getPosition(activity).equals("")){ }else{
         *
         * }
         */
        surveySet.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // stateId,surveysetId,userId,roleId

                userId = AppPreferenceManager.getUserId(context);
                roleId = AppPreferenceManager.getRoleId(context);

                // surveysetId = myAList.get(position).survey_id;
                // stateId = myAList.get(position).state_id;

                AppPreferenceManager.savePosition(String.valueOf(position),
                        context);
                AppPreferenceManager.saveSurveyName(
                        surveylist.get(position).survey_name, context);
                // AppPreferenceManager.saveSurveyId(surveylist.get(position).survey_id,
                // context);
                AppPreferenceManager.saveSurveyId(
                        surveylist.get(position).survey_id, context);
                Log.d("TAG",
                        "surveyName-------------->"
                                + surveylist.get(position).survey_name);

                String s_id = surveylist.get(position).survey_id;
                Log.d("Survey ID",
                        "surveyName-------------->"
                                + s_id);
                Intent intent = new Intent(SurveyListActivity.this,
                        Decision.class);
                intent.putExtra("Id", customer_id);
                startActivity(intent);
                /*
                 * startActivityForResult(new Intent(getApplicationContext(),
                 * Decision.class).putExtra("Id", customer_id),
                 * UserValues.VIEW_USER_REQ);
                 */

            }
        });

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
                        //System.out.println("124");

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

    public class SurveyListAdapter extends BaseAdapter {

        private Context context;
        private View mView;
        private LayoutInflater inflater;
        ArrayList<Survey> mArrayList;

        // String[] items=new String[]{"Whole Sale","Retail"};

        public SurveyListAdapter(Context context, ArrayList<Survey> arraylist) {
            this.context = context;
            this.mArrayList = arraylist;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // return items.length;
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {

            return 0;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mView = inflater.inflate(R.layout.custom_survey_listitem, null);
            } else {
                mView = convertView;
            }
            Typeface typeface = Utils.setFontTypeToArial(context);
            surveyName = (TextView) mView.findViewById(R.id.surveyName);
            tickImg = (ImageView) mView.findViewById(R.id.tickImg);
            // surveyType.setText(items[position]);
            surveyName.setText(mArrayList.get(position).survey_name);
            surveyName.setTypeface(typeface);
            // tickImg.setImageDrawable(context.getResources().getDrawable(
            // R.drawable.tickmarkred));

            if (AppPreferenceManager.getPosition(context).equals(
                    String.valueOf(position))) {
                /*
                 * tickImg.setImageDrawable(context.getResources().getDrawable(
                 * R.drawable.tickmarkgreen));
                 */
                mView.setBackgroundColor(Color.rgb(144, 238, 144));

            } else {
                // tickImg.setImageDrawable(null));

            }

            return mView;
        }

    }

	/*@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("TAG", "On Restart of SurveyListActivity--------->");
		// startActivity(getIntent());
		Intent intent = new Intent(SurveyListActivity.this,
				SearchUserActivity.class);
		startActivity(intent);
		finish();
	}*/

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // AppPreferenceManager.saveFlag("0", context);
        SurveyListActivity.this.finish();
        Log.d("TAG", "On Back of SurveyListActivity--------->");

    }
}
