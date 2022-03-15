package com.zaimus.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zaimus.Survey.Survey;
import com.zaimus.manager.Utils;
import com.zaimus.R;

public class TourPlanListAdapter extends BaseAdapter {
	SimpleDateFormat sdf,formatter;
	private Context context;
	private View mView;
	private LayoutInflater inflater;
	ArrayList<Survey> mArrayList;
	TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
			statusTextView,dateTextView,viewTextView;

	// String[] items=new String[]{"Whole Sale","Retail"};

	public TourPlanListAdapter(Context context, ArrayList<Survey> arraylist) {
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
			mView = inflater.inflate(R.layout.tourplan_listitem, null);
		} else {
			mView = convertView;
		}
		Typeface typeface = Utils.setFontTypeToArial(context);
		 //sdf = new SimpleDateFormat("yyyy-MM-dd");
		titleTextView = (TextView) mView.findViewById(R.id.titleTextView);
		descTextView = (TextView) mView.findViewById(R.id.descTextView);
		// surveyType.setText(items[position]);
		reqAmountTextView = (TextView) mView.findViewById(R.id.reqTextView);
		aprAmountTextView = (TextView) mView.findViewById(R.id.aprTextView);
		statusTextView = (TextView) mView.findViewById(R.id.statusTextView);
		dateTextView=(TextView) mView.findViewById(R.id.DateTextView);
		viewTextView=(TextView) mView.findViewById(R.id.viewTextView);
		dateTextView.setVisibility(View.GONE);
		//reqAmountTextView.setVisibility(View.GONE);
		aprAmountTextView.setVisibility(View.GONE);
		
		SpannableString content = new SpannableString("View");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		viewTextView.setText(content);
		titleTextView.setText(mArrayList.get(position).plan_name);
		descTextView.setText(mArrayList.get(position).survey_name);
		reqAmountTextView.setText(mArrayList.get(position).state_name);
		aprAmountTextView.setVisibility(View.GONE);
		statusTextView.setVisibility(View.GONE);
		

		return mView;
	}

}
