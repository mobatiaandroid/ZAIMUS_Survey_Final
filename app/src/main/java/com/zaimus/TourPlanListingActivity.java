package com.zaimus;
import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zaimus.Survey.Survey;
import com.zaimus.adapters.TourPlanListAdapter;
import com.zaimus.api.VkcApis;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

public class TourPlanListingActivity extends Activity {
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, addClaim;
	Activity activity = this;
	ProgressDialog Dialog;
	ArrayList<Survey> tourList = new ArrayList<Survey>();
	Context context = this;
	ListView cliamslistView;
	String planId;
	EditText edtSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.claimlist);
		header = (LinearLayout) findViewById(R.id.header);
		cliamslistView = (ListView) findViewById(R.id.claimslistView);
		edtSearch=(EditText) findViewById(R.id.searchTextEntry);
		
		edtSearch.setVisibility(View.GONE);
		headermanager = new Headermanager(activity, "Tour Plan");
		headermanager.getHeader(header, 0, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		//addClaim = headermanager.getRightButton();
		//addClaim.setVisibility(View.VISIBLE);

		
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TourPlanListingActivity.this.finish();

			}
		});
		if (Utils.checkInternetConnection(context)) {
			doPlansAsynchTask task = new doPlansAsynchTask();
			task.execute();
		} else {
			Toast.makeText(context, "No internet connection",
					Toast.LENGTH_SHORT).show();

		}
		
		cliamslistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				planId=tourList.get(position).plan_id;
				Intent intent=new Intent(TourPlanListingActivity.this,TourPlanDetailsActivity.class);
				intent.putExtra("planId", planId);
				startActivity(intent);
			}
		});
	}

	public class doPlansAsynchTask extends AsyncTask<Void, Integer, Void> {

		private String success;

		@Override
		protected void onPostExecute(Void result) {

			Dialog.dismiss();

			if (tourList.size() == 0) {
				Toast.makeText(context, "No tourplans available",
						Toast.LENGTH_SHORT).show();
			} else {
				TourPlanListAdapter adapter = new TourPlanListAdapter(context,
						tourList);
				cliamslistView.setAdapter(adapter);
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			Dialog = new ProgressDialog(TourPlanListingActivity.this);
			Dialog.setMessage(TourPlanListingActivity.this.getResources().getString(
					R.string.loading));
			Dialog.setCanceledOnTouchOutside(false);
			Dialog.show();
			Dialog.setContentView(R.layout.progress);
			super.onPreExecute();
			// success = "failed";
		}

		@Override
		protected Void doInBackground(Void... params) {

			tourList = VkcApis.get_tourplan(
					AppPreferenceManager.getUserId(context), context);
			return null;
		}

	}

}
