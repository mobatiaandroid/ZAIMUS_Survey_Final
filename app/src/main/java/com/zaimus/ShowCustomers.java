package com.zaimus;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.adapters.CustomSurveyListAdapter;
import com.zaimus.adapters.ListAdapter;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;

public class ShowCustomers extends Activity {

	ListAdapter listAdapter;
	// ArrayAdapter<Survey> listAdapter;
	ListView expListView;

	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg;
	Activity activity = this;
	TextView salesrepName;
	TextView stourPlanName;
	TextView sueveysetName;
	TextView budgetName;
	ArrayList<Survey> planList = new ArrayList<Survey>();
	// ArrayList<Survey> dateList = new ArrayList<Survey>();
	SQLiteAdapter adapter;
	Context context = this;
	// String planId;
	Survey plan, data;
	String date, customer_id, district,planId,detailId;
	ImageView AddUserButton;
	SQLiteAdapter mySQLiteAdapter;
	public ArrayList<Survey> surveylist = new ArrayList<Survey>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.tour_plan_expandable);
		Bundle extras = getIntent().getExtras();
		mySQLiteAdapter = new SQLiteAdapter(this);

		if (extras == null) {
			date = AppPreferenceManager.getDate(context);
			district = AppPreferenceManager.getPlanDist(context);
		} else {
			date = extras.getString("date");
			district = extras.getString("dist");
			planId=extras.getString("planId");
			detailId=extras.getString("detailId");
		}

		adapter = new SQLiteAdapter(context);
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "Tour Plan");
		headermanager.getHeader(header, 0, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowCustomers.this.finish();

			}
		});

		// get the listview
		expListView = (ListView) findViewById(R.id.expandableListView);
		budgetName = (TextView) findViewById(R.id.budgetName);
		sueveysetName = (TextView) findViewById(R.id.sueveysetName);
		stourPlanName = (TextView) findViewById(R.id.stourPlanName);
		salesrepName = (TextView) findViewById(R.id.salesrepName);
		AddUserButton = (ImageView) findViewById(R.id.AddUserButton);
		AddUserButton.setVisibility(View.VISIBLE);
		budgetName.setVisibility(View.GONE);
		salesrepName.setVisibility(View.GONE);
		sueveysetName.setVisibility(View.GONE);
		stourPlanName.setVisibility(View.GONE);
		plan = new Survey();
		data = new Survey();

		// district=AppPreferenceManager.getDistrict(context);
		// preparing list data
		prepareListData();

		expListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				customer_id = planList.get(position).cus_id;
				AppPreferenceManager.saveFromPlan("1", context);

				//System.out.println("ID CUSTOMER---------->" + customer_id);
				Intent intent = new Intent(ShowCustomers.this,
						SurveyListActivity.class);
				intent.putExtra("Id", customer_id);
				startActivity(intent);
			}
		});

		AddUserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppPreferenceManager.saveFromPlan("1", context);

				mySQLiteAdapter.openToRead();
				surveylist = mySQLiteAdapter.getAllSurvey();
				mySQLiteAdapter.close();
				if (surveylist.size() > 0) {
					Intent i = new Intent(getApplicationContext(),
							AddUserToPlan.class);
					i.putExtra("planId", planId);
					i.putExtra("detailId", detailId);
					i.putExtra("dist", district);
					startActivityForResult(i, UserValues.ADD_USER_REQ);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please import data", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {

		adapter.openToRead();
		planList = adapter.getDatePlans(date, district);
		adapter.close();
		//System.out.println("SIZE DB" + planList.size());

		if (planList.size() > 0) {
			CustomSurveyListAdapter adapter = new CustomSurveyListAdapter(
					context, planList);
			expListView.setAdapter(adapter);
		} else {
			Toast.makeText(context, "no customers", Toast.LENGTH_SHORT).show();
		}

		// / listDataChild.put(listDataHeader.get(0), top250); // Header, Child
		// data
		// listDataChild.put(listDataHeader.get(1), nowShowing);
		// listDataChild.put(listDataHeader.get(2), comingSoon);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == UserValues.ADD_USER_REQ) {
			if (resultCode == UserValues.ADD_USER_RES) {
				finish();
			}
		}

	}

}
