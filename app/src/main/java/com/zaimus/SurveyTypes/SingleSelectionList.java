package com.zaimus.SurveyTypes;

import java.sql.Timestamp;
import java.util.Date;

import com.zaimus.LoopQuestion;
import com.zaimus.R;

import com.zaimus.Survey.Survey;

import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SingleSelectionList extends Activity {
	private TextView questionText;
	private ImageView backButton;
	private ImageView nextButton;
	private int q_id;
	private SurveyQuestions s_question;
	private Spinner qSpinner;
	private SurveyAnswers s_answer;
	private SurveyAnswers ts_answer;
	private int surveyAnswer_attend_pos;
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg;
	Activity activity=this;
	Typeface typeface;
	TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;
	Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			q_id = extras.getInt("qid");
			Survey.q_id = q_id;
		}
		typeface=Utils.setFontTypeToArial(context);
		setContentView(R.layout.q_spinner);
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "");
		headermanager.getHeader(header, 1, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SingleSelectionList.this.finish();

			}
		});
		s_question = Survey.surveyQuestions.get(Survey.q_id);

		questionText = (TextView) findViewById(R.id.qSpinnerTextView);
		questionText.setTypeface(typeface);
		questionText.setText(s_question.getQuestionText());
		questionText.setTypeface(typeface);
		nextButton = (ImageView) findViewById(R.id.qListNext);
		backButton = (ImageView) findViewById(R.id.qListBack);
		txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
		txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
		txtqDecisionBack.setTypeface(typeface);
		txtqDecisionNext.setTypeface(typeface);
		qSpinner = (Spinner) findViewById(R.id.qSpinner);
		/*ArrayAdapter<String> spinAdapter = 
			
		new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				s_question.getQuestionOptions());
		qSpinner.setAdapter(spinAdapter);*/
		ArrayAdapter<String> spinAdapter = 
				
				new ArrayAdapter<String>(
						getApplicationContext(), R.layout.simple_spinner_item,
						s_question.getQuestionOptions());
				qSpinner.setAdapter(spinAdapter);

		//if (Survey.surveyAnswers.size() > Survey.q_id) {
		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
		if(surveyAnswer_attend_pos!=-1){
			ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
			qSpinner.setSelection(ts_answer.getoptionPos());
		}

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	if (Survey.surveyAnswers.size() > Survey.q_id) {
				//surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
				if(surveyAnswer_attend_pos!=-1){
					Survey.surveyAnswers.get(surveyAnswer_attend_pos).setOptionPos(
							qSpinner.getSelectedItemPosition());
					Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSurveyAnswer(
							s_question.getOption(qSpinner
									.getSelectedItemPosition()));
					//Survey.surveyAnswers.get(surveyAnswer_attend_pos).setIsattended(true);
				} else {
					Timestamp currentTimeString = new Timestamp(new Date().getTime());
					s_answer = new SurveyAnswers(s_question.getQuestionId(),
							s_question.getQuestionText(), s_question
									.getOption(qSpinner
											.getSelectedItemPosition()),
							currentTimeString.toString(), qSpinner
									.getSelectedItemPosition(), s_question
									.getQuestType(), null, null, null, null,Survey.q_id);
					Survey.surveyAnswers.add( s_answer);
				}
				
				LoopQuestion loopQuestion  = new LoopQuestion(SingleSelectionList.this);
				if(s_question.getOption_link_id()!=-1 || s_question.getQuestion_tree_id()!=-1)// this is a loop question
				{
					
					int next_qid  ;
					next_qid = loopQuestion.findNextQuestionidpostion(s_question.getOptionIdSelected(qSpinner.getSelectedItemPosition()));
					//System.out.println("Next Qid " +next_qid);
					if(next_qid==-1)
					{
						Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestion_tree_id());
					}
					else
					{
						Survey.q_id = next_qid;
					}
				}
				else// not a loop question 
				{
					Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
					//System.out.println("Next Qid not aloop " +Survey.q_id);
				}
				loopQuestion.closeDB();
				//Survey.q_id++;
				//if (Survey.surveyQuestions.size() > Survey.q_id) {
					if (Survey.q_id !=-1) {
						//System.out.println("Survey NEXT" +UserValues.VIEW_USER_REQ);

					SurveyTypeUtils swit = new SurveyTypeUtils();
					//System.out.println("Survey NEXT 1 " +UserValues.VIEW_USER_REQ);

					startActivityForResult(
							swit.getIntent(getApplicationContext()),
							UserValues.VIEW_USER_REQ);
				} else {
					Intent i = new Intent(getApplicationContext(),
							SurveyUpdateActivity.class);
					startActivityForResult(i, UserValues.VIEW_USER_REQ);
				}
			}
		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {			
			/*	if(surveyAnswer_attend_pos!=-1)
				{
					Survey.surveyAnswers.get(surveyAnswer_attend_pos).setIsattended(false);
				}*/
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Survey.q_id = q_id;
		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
		super.onResume();
		AudioRecorder.stopTimer();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Survey.q_id = q_id;
		super.onStart();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		
		
		case UserValues.VIEW_USER_REQ:
			if (resultCode == UserValues.SURVEY_RES) {
				//System.out.println("Survey NEXT 2" +UserValues.VIEW_USER_REQ);
				setResult(UserValues.SURVEY_RES);
				finish();
			}
			break;
		}
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, SingleSelectionList.this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		/*if(surveyAnswer_attend_pos!=-1)
		{
			Survey.surveyAnswers.get(surveyAnswer_attend_pos).setIsattended(false);
		}*/
		finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
