package com.zaimus.adapters;

import com.zaimus.R;
import com.zaimus.Survey.Survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SurveySetAdapter extends BaseAdapter {

	private Context context;
	private TextView surveyType;
	private View mView;
	private LayoutInflater inflater;
	//String[] items=new String[]{"Whole Sale","Retail"};

	public SurveySetAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		//return items.length;
		return Survey.surveySets.size();
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
			mView = inflater.inflate(R.layout.search_list, null);
		} else {
			mView = convertView;
		}
		
		surveyType = (TextView) mView.findViewById(R.id.search_item);
		//surveyType.setText(items[position]);
		surveyType.setText(Survey.surveySets.get(position).getSurveySetName());
		return mView;
	}

}
