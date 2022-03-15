package com.zaimus;
import java.util.ArrayList;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.SurveyTypes.Decision;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.adapters.SurveySetAdapter;
import com.zaimus.Survey.SurveySetModel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class SurveySetActivity extends Activity {

	private ListView surveySet;
	private SurveySetAdapter adapter;
	private SQLiteAdapter sqLiteAdapter;
	private String customer_id;
	private SurveySetModel surveySetModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.surveyset);
		retrieveFromSurveySetDb();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			customer_id = extras.getString("Id");
			//Log.v("customer_id", customer_id);
		}
		
		initializeUI();
	}

	private void initializeUI() {
		//surveySet = (ListView) findViewById(R.id.surveyset_list);
		adapter = new SurveySetAdapter(SurveySetActivity.this);		
		surveySet.setAdapter(adapter);
		if(Survey.surveySets.size()>0)
		{
			
		}
		else
		{
			Toast.makeText(SurveySetActivity.this, "There is no survey set found", Toast.LENGTH_SHORT).show() ;
		}
		
		surveySet.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Survey.surveyset_id=Survey.surveySets.get(position).getSurveySetId();
				startActivityForResult(new Intent(getApplicationContext(),
						Decision.class).putExtra("Id", customer_id),
						UserValues.VIEW_USER_REQ);
			}
		});
	}

	private void retrieveFromSurveySetDb() {		
		Survey.surveySets = new ArrayList<SurveySetModel>();
		sqLiteAdapter = new SQLiteAdapter(SurveySetActivity.this);
		sqLiteAdapter.openToRead();
		String[] columns = new String[] { "surveyset_id", "surveyset_name","zone", "type" };
		Cursor cursor = sqLiteAdapter.queueAll("survey_sets", columns, "", "");		
		//Log.v("", "count" + cursor.getCount());
		cursor.moveToPosition(0);
		while (cursor.isAfterLast() == false) {
			surveySetModel = new SurveySetModel();
			surveySetModel.setSurveySetId(cursor.getInt(0));
			surveySetModel.setSurveySetName(cursor.getString(1));
			surveySetModel.setZone(cursor.getString(2));
			surveySetModel.setSurveyType(cursor.getString(3));	
			Survey.surveySets.add(surveySetModel);
			cursor.moveToNext();
		}	
		cursor.close();
		sqLiteAdapter.close();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case UserValues.VIEW_USER_REQ:
			if (resultCode == UserValues.SURVEY_RES) {
				setResult(UserValues.SURVEY_RES);
				finish();
			}
			break;
		}
	}


}
