/*package com.vkcrestore.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.vkcrestore.Survey.Claims;
import com.vkcrestore.manager.Utils;
import com.vkcrestore.R;

public class ClaimListAdapter extends BaseAdapter {
	SimpleDateFormat sdf, formatter;
	private Context context;
	private View mView;
	private LayoutInflater inflater;
	ArrayList<Claims> mArrayList;
	ArrayList<Claims> cArrayList;

	TextView titleTextView, descTextView, reqAmountTextView, aprAmountTextView,
			statusTextView, dateTextView;
	Date tempDate;
	String newformat;
	private Filter filter;

	// String[] items=new String[]{"Whole Sale","Retail"};

	public ClaimListAdapter(Context context, ArrayList<Claims> arraylist) {
		this.context = context;
		this.mArrayList = arraylist;
		this.cArrayList = new ArrayList<Claims>();
		this.cArrayList.addAll(mArrayList);
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

	public Filter getFilter() {
		if (filter == null){
		    filter  = new ClaimListFilter();
		   }
		   return filter;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mView = inflater.inflate(R.layout.claimlist_item, null);
		} else {
			mView = convertView;
		}
		Typeface typeface = Utils.setFontTypeToArial(context);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		titleTextView = (TextView) mView.findViewById(R.id.titleTextView);
		descTextView = (TextView) mView.findViewById(R.id.descTextView);
		// surveyType.setText(items[position]);
		reqAmountTextView = (TextView) mView.findViewById(R.id.reqTextView);
		aprAmountTextView = (TextView) mView.findViewById(R.id.aprTextView);
		statusTextView = (TextView) mView.findViewById(R.id.statusTextView);
		dateTextView = (TextView) mView.findViewById(R.id.DateTextView);

		try {
			tempDate = sdf.parse(mArrayList.get(position).date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("dd-MMM-yy");
		newformat = formatter.format(tempDate);

		titleTextView.setText(mArrayList.get(position).title);
		descTextView.setText(mArrayList.get(position).description);
		reqAmountTextView.setText(mArrayList.get(position).reqAmount);
		aprAmountTextView.setText(mArrayList.get(position).aprAmount);
		statusTextView.setText(mArrayList.get(position).staus);
		if (mArrayList.get(position).staus.equals("Approved")) {
			statusTextView.setTextColor(Color.rgb(0, 128, 0));
		} else if (mArrayList.get(position).staus.equals("Pending")) {
			statusTextView.setTextColor(Color.rgb(255, 69, 0));

		} else if (mArrayList.get(position).staus.equals("Rejected")) {
			statusTextView.setTextColor(Color.rgb(220, 20, 60));

		}
		dateTextView.setText(newformat);

		return mView;
	}

	private class ClaimListFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<Claims> filteredItems = new ArrayList<Claims>();

				for (int i = 0, l = mArrayList.size(); i < l; i++) {
					Claims claims = mArrayList.get(i);
					if (claims.staus.toLowerCase().contains(constraint))
						filteredItems.add(claims);
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				
					result.values = mArrayList;
					result.count = mArrayList.size();
				
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			cArrayList = (ArrayList<Claims>) results.values;
			notifyDataSetChanged();
			mArrayList.clear();
			for (int i = 0, l = cArrayList.size(); i < l; i++)
				mArrayList.add(cArrayList.get(i));
			notifyDataSetInvalidated();
		}

	}
}
*/