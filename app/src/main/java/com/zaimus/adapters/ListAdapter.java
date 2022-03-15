package com.zaimus.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class ListAdapter extends BaseAdapter {
	SimpleDateFormat sdf, formatter;
	private Context context;
	private View mView;
	private LayoutInflater inflater;
	ArrayList<Survey> mArrayList;
	/*TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
			statusTextView, dateTextView,expenseClaimed,overallstat;*/
	Date tempDate;
	String newformat;
	ViewHolder holder;
	// String[] items=new String[]{"Whole Sale","Retail"};

	public ListAdapter(Context context, ArrayList<Survey> arraylist) {
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
			mView = inflater.inflate(R.layout.tour_list_item, null);
		} else {
			mView = convertView;
		}
		holder=new ViewHolder();
		Typeface typeface = Utils.setFontTypeToArial(context);
		// sdf = new SimpleDateFormat("yyyy-MM-dd");
		holder.titleTextView = (TextView) mView.findViewById(R.id.fromDate);
		holder.descTextView = (TextView) mView.findViewById(R.id.toDate);
		holder.statusTextView = (TextView) mView.findViewById(R.id.districtName);
		holder.reqAmountTextView = (TextView) mView.findViewById(R.id.budget);
		holder.expenseClaimed=(TextView) mView.findViewById(R.id.expenseClaimed);
		holder.overallstat=(TextView) mView.findViewById(R.id.overallstat);
		holder.titleTextView.setText("From :"
				+ mArrayList.get(position).plan_from_date +" To :"+mArrayList.get(position).plan_to_date);
		holder.descTextView.setText("District :"+mArrayList.get(position).districtName);
		holder.statusTextView.setText("Budget :"+mArrayList.get(position).budget);
		holder.expenseClaimed.setText("Expense Claimed :"+mArrayList.get(position).expenseCliamed);
		holder.overallstat.setText("Status :"+mArrayList.get(position).ovrerallStatus);
		SpannableString content = new SpannableString("View Customers");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		holder.reqAmountTextView.setText(content);
		//reqAmountTextView.setText("View Customers");
		//System.out.println("Status--------->"+mArrayList.get(position).ovrerallStatus);

		return mView;
	}
	
	private class ViewHolder {
		TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
		statusTextView, dateTextView,expenseClaimed,overallstat;
	    
	}

}
