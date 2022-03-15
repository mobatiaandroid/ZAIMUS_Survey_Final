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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Matrix extends Activity {
	private SurveyQuestions s_question;
	private SurveyAnswers s_answer;
	private SurveyAnswers ts_answer;
	private int[] t_options;
	private int q_id;
	private TextView questionText;
	private ImageView backButton;
	private ImageView nextButton;
	private String[] sub_question;
	private int[] sub_question_id;
	private String[] q_options;
	private int surveyAnswer_attend_pos;
	private boolean globalRadioCheck=false;
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg;
	Activity activity=this;
	Typeface typeface;
	Context context=this;
	TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
typeface=Utils.setFontTypeToArial(context);
		setContentView(R.layout.q_matrix);
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "");
		headermanager.getHeader(header, 1, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Matrix.this.finish();

			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			q_id = extras.getInt("qid");
			Survey.q_id = q_id;
		}

		//s_question = Survey.surveyQuestions.get(Survey.q_id);
		s_question = Survey.surveyQuestions.get(Survey.q_id);
		questionText = (TextView) findViewById(R.id.qMatrixText);
		txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
		txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
		questionText.setTypeface(typeface);
		txtqDecisionBack.setTypeface(typeface);
		txtqDecisionNext.setTypeface(typeface);
		questionText.setText(s_question.getQuestionText());
		sub_question = s_question.getSubQuestion();
		sub_question_id = s_question.getSubQuestionsId();
		q_options = s_question.getQuestionOptions();

	/*	if (Survey.surveyAnswers.size() > Survey.q_id) {
			ts_answer = Survey.surveyAnswers.get(Survey.q_id);
			t_options = ts_answer.getSubpos();
		}*/
		
		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
		if(surveyAnswer_attend_pos!=-1){	
			ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
			////Log.v((surveyAnswer_attend_pos) + "", ts_answer.getSurveyAnswer());
			t_options = ts_answer.getSubpos();
		}

		TableLayout tl = (TableLayout) findViewById(R.id.matrixLayout);
		for (int i = 0; i < sub_question.length + 1; i++) 
		{ // No of rows
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < q_options.length + 1; j++) 
			{ // No of columns
				if (j == 0 || i == 0)
				{
					TextView txt = new TextView(this);
					txt.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams txtlp = (LinearLayout.LayoutParams) txt
							.getLayoutParams();
					if (i == 0) {
						txtlp.gravity = Gravity.CENTER;
						txtlp.weight = 1;
						if (j != 0)
							txt.setText(q_options[j - 1]);
					} else {
						txtlp.gravity = Gravity.CENTER_VERTICAL;
						txt.setText(sub_question[i - 1]);
					}
					txt.setLayoutParams(txtlp);
					txt.setTextColor(Color.BLACK);
					tr.addView(txt);
				} 
				else 
				{
					RadioButton rb = new RadioButton(this);
					rb.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams rblp = (LinearLayout.LayoutParams) rb
							.getLayoutParams();
					rblp.gravity = Gravity.CENTER;
					rblp.setMargins(4, 4, 4, 4);
					rb.setButtonDrawable(getResources().getDrawable(R.drawable.radio_button_background));
					rb.setLayoutParams(rblp);
					
				//if (Survey.surveyAnswers.size() > Survey.q_id)
					if(surveyAnswer_attend_pos!=-1)	
					{
						if (t_options[i - 1] == (j - 1)) 
						{
							rb.setChecked(true);
						}
					}
					else
					{
						if(j==1)
						{
							//rb.setChecked(true);
						}
					}
					tr.addView(rb);
					rb.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							TableRow trow = (TableRow) v.getParent();
							for (int k = 1; k < trow.getChildCount(); k++) { // Neglects
																				// Textview
																				// at
																				// the
																				// intial
																				// cell
								if (trow.getChildAt(k) != v) { // Uncheck the
																// radio buttons
																// other than
																// the clicked
																// one
									RadioButton rd = (RadioButton) trow.getChildAt(k);
									rd.setChecked(false);
								}
							}
						}
					});
					globalRadioCheck=false;
				}
			}
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		nextButton = (ImageView) findViewById(R.id.qMatrixNext);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableLayout tab = (TableLayout) findViewById(R.id.matrixLayout);
				boolean error = false;
				boolean checked = false;
				
				int[] answerOp = new int[sub_question.length];
				int[] checkedAnswer = new int[sub_question.length];

				String[] subAnswer = new String[sub_question.length];
				
				//Log.v("----question-------", " -----------------"+sub_question.length);
		
				for (int i = 1; i < tab.getChildCount(); i++) 
				{
					checked = false;
					TableRow trow = (TableRow) tab.getChildAt(i);
					for (int j = 1; j < trow.getChildCount(); j++)
					{
						RadioButton rdButton = (RadioButton) trow.getChildAt(j);
						if (rdButton.isChecked())
						{
							checked = true;
							globalRadioCheck=true;
							checkedAnswer[i-1]=1;
							answerOp[i - 1] = j - 1;
							subAnswer[i - 1] = q_options[j - 1];
							break;
						}
						else
						{
							checkedAnswer[i-1]=0;

						}
					}
					/*if (!checked)
					{
						trow.setBackgroundColor(Color.rgb(247, 129, 129));
						trow.setFocusable(true);
						error = true;
						
						
					}
					else 
					{
						trow.setBackgroundColor(Color.TRANSPARENT);
					}*/
				}
				for (int k = 0; k < sub_question.length; k++) 
				{
					if(checkedAnswer[k]==0)
					{
						globalRadioCheck=false;
						break;
					}
					else
					{
						globalRadioCheck=true;

					}
				}
				
				if (!globalRadioCheck) 
				{
//					alertbox("Warning", "Please rank atleast one");
					alertbox("Warning", "Please rank all the poducts");

					return;
				}
			
		//		if (!error) 
		//		{
				if(surveyAnswer_attend_pos!=-1)	
					{

						Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSubAnswers(subAnswer);
						Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSubPosition(answerOp);

					} else {
						Timestamp currentTimeString = new Timestamp(new Date().getTime());

						s_answer = new SurveyAnswers(
								s_question.getQuestionId(), s_question
										.getQuestionText(), null,
								currentTimeString.toString(), 0,
								s_question.getQuestType(), s_question
										.getSubQuestion(), subAnswer, answerOp, null,Survey.q_id);

						Survey.surveyAnswers.add(s_answer);
					}

					//Log.v("Q_ID", "" + Survey.q_id);
					/*for (int j = 0; j < Survey.surveyAnswers.size(); j++)
					{
						//Log.v(""
								+ Survey.surveyAnswers.get(j)
										.getSurveyQuestion() + j, ""
								+ Survey.surveyAnswers.get(j).getSurveyAnswer());
					}*/

					//Survey.q_id++;
					/////////////////NEXT QUESTION //////////////
					LoopQuestion loopQuestion  = new LoopQuestion(Matrix.this);
						Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
					loopQuestion.closeDB();
					
					/////////////////NEXT QUESTION //////////////
					
					if (Survey.q_id !=-1) 
					//if (Survey.surveyQuestions.size() > Survey.q_id) 
					{
						SurveyTypeUtils swit = new SurveyTypeUtils();
						startActivityForResult(
								swit.getIntent(getApplicationContext()),
								UserValues.VIEW_USER_REQ);
					}
					else
					{
						Intent i = new Intent(getApplicationContext(),
								SurveyUpdateActivity.class);
						startActivityForResult(i, UserValues.VIEW_USER_REQ);
						;
					}
	//			}
			}
		});

		backButton = (ImageView) findViewById(R.id.qMatrixBack);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Survey.q_id = q_id;
		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;

		/*//System.out.println("Print all answer");
		SurveyAnswers surveyAnswers ;
		for (int i = 0; i < Survey.surveyAnswers.size(); i++) {
			
			surveyAnswers = Survey.surveyAnswers.get(i);
			//System.out.println("////Attended answers//// + "+ i);
			//System.out.println(surveyAnswers.getQuestionId());
			//System.out.println(surveyAnswers.getQuestionType());
			//System.out.println(surveyAnswers.getSurveyQuestion());
			//System.out.println("////Attended answers////");
			
		}
		
		
		//System.out.println("////////Current answer position//" +surveyAnswer_attend_pos);
		if(surveyAnswer_attend_pos!=-1)	
		{
			surveyAnswers = Survey.surveyAnswers.get(surveyAnswer_attend_pos);

		//System.out.println(surveyAnswers.getQuestionId());
		//System.out.println(surveyAnswers.getQuestionType());
		//System.out.println(surveyAnswers.getSurveyQuestion());
		//System.out.println("////////Current answer position");
		}
		*/
		super.onResume();
		AudioRecorder.stopTimer();
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, Matrix.this);
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
				setResult(UserValues.SURVEY_RES);
				finish();
			}
			break;
		}
	}
	protected void alertbox(String title, String mymessage) {
		new AlertDialog.Builder(this)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setIcon(android.R.drawable.stat_notify_error)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
