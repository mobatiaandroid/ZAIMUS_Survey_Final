package com.zaimus.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zaimus.Profiles.Profile;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.manager.Utils;
import com.zaimus.R;

public class CustomSurveyListAdapter extends BaseAdapter {
	SimpleDateFormat sdf, formatter;
	private Context context;
	private View mView;
	private LayoutInflater inflater;
	ArrayList<Survey> mArrayList;
	TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
			statusTextView, dateTextView;
	Date tempDate;
	String newformat;
	SQLiteAdapter mySQLiteAdapter;
	public ArrayList<Profile> clist = new ArrayList<Profile>();

	// String[] items=new String[]{"Whole Sale","Retail"};

	public CustomSurveyListAdapter(Context context, ArrayList<Survey> arraylist) {
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
		// if (convertView == null) {
		mView = inflater.inflate(R.layout.show_customers, null);
		// } else {
		// mView = convertView;

		mySQLiteAdapter = new SQLiteAdapter(context);
		mySQLiteAdapter.openToRead();

		clist = mySQLiteAdapter.getAllCustomer();
		mySQLiteAdapter.close();

		// }
		Typeface typeface = Utils.setFontTypeToArial(context);
		// sdf = new SimpleDateFormat("yyyy-MM-dd");
		titleTextView = (TextView) mView.findViewById(R.id.fromDate);
		/*descTextView = (TextView) mView.findViewById(R.id.toDate);
		statusTextView = (TextView) mView.findViewById(R.id.districtName);
		reqAmountTextView = (TextView) mView.findViewById(R.id.budget);
		descTextView.setVisibility(View.GONE);
		statusTextView.setVisibility(View.GONE);
		reqAmountTextView.setVisibility(View.GONE);*/
		titleTextView.setText(mArrayList.get(position).customer_name);

		String id = mArrayList.get(position).cus_id;

		for (int j = 0; j < clist.size(); j++) {

			String c_id = clist.get(j).customer_id;

			if (id.equals(c_id)) {
				mView.setBackgroundColor(Color.rgb(144, 238, 144));
			} else {

			}
		}
		return mView;

	}

}
