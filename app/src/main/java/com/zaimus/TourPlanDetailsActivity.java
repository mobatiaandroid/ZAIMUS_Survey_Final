package com.zaimus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.Survey.Survey;
import com.zaimus.adapters.ListAdapter;
import com.zaimus.api.VkcApis;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;

public class TourPlanDetailsActivity extends Activity {

	ListAdapter listAdapter;
	//ArrayAdapter<Survey> listAdapter;
	ListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg;
	Activity activity = this;
	TextView salesrepName;
	TextView stourPlanName;
	TextView sueveysetName;
	TextView budgetName;
	ArrayList<Survey> planList = new ArrayList<Survey>();
	ArrayList<Survey> dateList = new ArrayList<Survey>();

	Context context = this;
	String planId;
	Survey plan, data;
	String date,district;
	//SpannableString content = new SpannableString("SalesRepresentative Name :");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.tour_plan_expandable);
		Bundle extras = getIntent().getExtras();

		planId = extras.getString("planId");
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "Tour Plan");
		headermanager.getHeader(header, 0, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TourPlanDetailsActivity.this.finish();

			}
		});

		// get the listview
		expListView = (ListView) findViewById(R.id.expandableListView);
		budgetName = (TextView) findViewById(R.id.budgetName);
		sueveysetName = (TextView) findViewById(R.id.sueveysetName);
		stourPlanName = (TextView) findViewById(R.id.stourPlanName);
		salesrepName = (TextView) findViewById(R.id.salesrepName);
		salesrepName.setVisibility(View.GONE);
		plan = new Survey();
		data = new Survey();
		// preparing list data
		
		prepareListData();

		expListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				date=dateList.get(position).plan_from_date;
				district=dateList.get(position).districtName;
				AppPreferenceManager.savePlanDrtailId(dateList.get(position).detailId, context);
				AppPreferenceManager.saveDate(date, context);
				AppPreferenceManager.savePlanDist(district, context);
				//System.out.println("date 1--------->"+date);
			Intent intent=new Intent(TourPlanDetailsActivity.this,ShowCustomers.class);
			intent.putExtra("planId", planId);
			intent.putExtra("detailId", dateList.get(position).detailId);
			intent.putExtra("date", date);
			intent.putExtra("dist", district);
			AppPreferenceManager.saveDistFromPlan(district, context);
			startActivity(intent);
				
			}
		});
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		doPlanDetailAsynchTask planTask = new doPlanDetailAsynchTask();
		planTask.execute();

		// / listDataChild.put(listDataHeader.get(0), top250); // Header, Child
		// data
		// listDataChild.put(listDataHeader.get(1), nowShowing);
		// listDataChild.put(listDataHeader.get(2), comingSoon);
	}

	public class doPlanDetailAsynchTask extends AsyncTask<Void, Integer, Void> {

		private String success;
		ProgressDialog Dialog;

		@Override
		protected void onPostExecute(Void result) {

			Dialog.dismiss();

			if (plan != null) {
				sueveysetName
						.setText("Surveyset Name : "
								+ AppPreferenceManager
										.getSurveyNamepreference(context));
				budgetName.setText("State : "
						+ AppPreferenceManager.getBudget(context));
				stourPlanName.setText("Plan Name : "
						+ AppPreferenceManager.getPlanName(context));
				planList = plan.getPlanList();
				dateList = plan.getPlanDate();
				//System.out.println("plan list size------->"+planList.size());
				if ((planList.size() > 0) && (dateList.size() > 0)) {
					listAdapter = new ListAdapter(context, dateList);
					// setting list adapter
					expListView.setAdapter(listAdapter);
				} else if(planList.size()==0){
					listAdapter = new ListAdapter(context, dateList);
					// setting list adapter
					expListView.setAdapter(listAdapter);
					
				}
					else {
					Toast.makeText(context, "You have no survey plan",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				sueveysetName
				.setText("Surveyset Name : "
						+ AppPreferenceManager
								.getSurveyNamepreference(context));
		budgetName.setText("Budget : "
				+ AppPreferenceManager.getBudget(context));
		stourPlanName.setText("Plan Name : "
				+ AppPreferenceManager.getPlanName(context));

				Toast.makeText(context, "You have no survey plan",
						Toast.LENGTH_SHORT).show();

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			Dialog = new ProgressDialog(TourPlanDetailsActivity.this);
			Dialog.setMessage(TourPlanDetailsActivity.this.getResources()
					.getString(R.string.loading));
			Dialog.setCanceledOnTouchOutside(false);
			Dialog.show();
			Dialog.setContentView(R.layout.progress);
			super.onPreExecute();
			// success = "failed";
		}

		@Override
		protected Void doInBackground(Void... params) {

			plan = VkcApis.get_tourplanindetail(
					AppPreferenceManager.getUserId(context), planId, context);

			return null;
		}

	}

}
