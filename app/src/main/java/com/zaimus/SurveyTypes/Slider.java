package com.zaimus.SurveyTypes;

import java.sql.Timestamp;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

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


public class Slider extends Activity {

	private ImageView nextButton;
	private ImageView backButton;
	private SeekBar slider;
	private int q_id;
	private SurveyAnswers s_answer;
	private SurveyAnswers ts_answer;
	private SurveyQuestions s_question;
	private TextView questionText;
	private TextView percentText;	
	private int surveyAnswer_attend_pos;
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg;
	Activity activity=this;
	Typeface typeface;
	TextView txtqDecisionBack, txtqDecisionNext;
	Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		typeface=Utils.setFontTypeToArial(context);

		setContentView(R.layout.q_slider);
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "");
		headermanager.getHeader(header,1, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Slider.this.finish();

			}
		});
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			q_id = extras.getInt("qid");
			Survey.q_id = q_id;
		}
		s_question = Survey.surveyQuestions.get(Survey.q_id);
		nextButton = (ImageView) findViewById(R.id.bt_slider_next);
		backButton = (ImageView) findViewById(R.id.bt_slider_back);
		txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
		txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
		slider = (SeekBar) findViewById(R.id.slider);
		questionText = (TextView) findViewById(R.id.txt_slider_question);
		questionText.setTypeface(typeface);
		percentText = (TextView) findViewById(R.id.txt_slider_percent);
		//txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
		//txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
		txtqDecisionBack.setTypeface(typeface);
		txtqDecisionNext.setTypeface(typeface);
		
		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
		if(surveyAnswer_attend_pos!=-1){
		//if (Survey.surveyAnswers.size() > Survey.q_id) {
			ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
			slider.setProgress(Integer.parseInt(ts_answer.getSurveyAnswer()));
			percentText.setText(ts_answer.getSurveyAnswer() + "%");
		}

		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				percentText.setText(progress + "%");
			}
		});

		
		questionText.setText(s_question.getQuestionText());

		// slider.getProgress();

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(surveyAnswer_attend_pos!=-1){
				//if (Survey.surveyAnswers.size() > Survey.q_id) {
					Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSurveyAnswer(
							"" + slider.getProgress());
				} else {
					Timestamp currentTimeString = new Timestamp(new Date().getTime());
					s_answer = new SurveyAnswers(s_question.getQuestionId(),
							s_question.getQuestionText(), ""
									+ slider.getProgress(), currentTimeString.toString(),
							0, s_question.getQuestType(), null, null, null, null,Survey.q_id);
					Survey.surveyAnswers.add(s_answer);
				}

				//Survey.q_id++;
/////////////////NEXT QUESTION //////////////
				LoopQuestion loopQuestion  = new LoopQuestion(Slider.this);
					Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
				loopQuestion.closeDB();
				
				/////////////////NEXT QUESTION //////////////
				
				if (Survey.q_id !=-1){
				//if (Survey.surveyQuestions.size() > Survey.q_id) {
					SurveyTypeUtils swit = new SurveyTypeUtils();
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
	@Override
	protected void onPause() 
	{
		super.onPause();
		AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, Slider.this);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
