package com.zaimus.SurveyTypes;

import java.sql.Timestamp;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;

public class ImageSelection extends Activity {

	private TextView questionText;
	private TextView scrollMsg;
	private ImageView backButton;
	private int q_id;
	private ImageView nextButton;
	private SurveyQuestions s_question;
	private ListView qListview;
	private SurveyAnswers s_answer;
	private SurveyAnswers ts_answer;
	private int surveyAnswer_attend_pos;
	Headermanager headermanager;
	LinearLayout header;
	ImageView splitIcon, tickImg,imageFeedback;
	Activity activity=this;
	Typeface typeface;
	TextView  txtqDecisionBack, txtqDecisionNext;
	Context context=this;
	String imagebitMap;
	Bitmap bitmap;
	SQLiteAdapter mySqLiteAdapter;
	LoadImage image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setup radio layout
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		typeface=Utils.setFontTypeToArial(context);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			q_id = extras.getInt("qid");
			Survey.q_id = q_id;
		}
				
		setContentView(R.layout.q_imageselection);
		header = (LinearLayout) findViewById(R.id.header);
		headermanager = new Headermanager(activity, "");
		headermanager.getHeader(header,1, false);
		headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
		splitIcon = headermanager.getLeftButton();
		splitIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageSelection.this.finish();

			}
		});
		// get question from
		s_question = Survey.surveyQuestions.get(Survey.q_id);
		questionText = (TextView) findViewById(R.id.qRadioTextView);
		scrollMsg = (TextView) findViewById(R.id.txt_radio_scrollmsg);
		txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
		txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
		imageFeedback=(ImageView) findViewById(R.id.qImage);
		
		mySqLiteAdapter = new SQLiteAdapter(getApplicationContext());
		mySqLiteAdapter.openToRead();
		String master_id=mySqLiteAdapter.getMasterId(String.valueOf(s_question.getQuestionId()));
		//System.out.print(master_id+"<-----------MASTER ID--------->");
		imagebitMap = mySqLiteAdapter.getImageByID(master_id);
		//System.out.print(imagebitMap+"<-----------MASTER ID--------->");

		mySqLiteAdapter.close();
		image = new LoadImage();
		image.execute();

		questionText.setText(s_question.getQuestionText());
		qListview = (ListView) findViewById(R.id.qSingleChoiceRadioListView);
		ArrayAdapter<String> cadapter = new ArrayAdapter<String>(
				getApplicationContext(),
				R.layout.simple_list_item_single_choice,
				s_question.getQuestionOptions());
		cadapter.notifyDataSetChanged();
		qListview.setAdapter(cadapter);
		qListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// setup please scroll message

		qListview.post(new Runnable() {
			public void run() {
				if (qListview.getCount() > (qListview.getLastVisiblePosition()
						- qListview.getFirstVisiblePosition() + 1)) {
					scrollMsg.setVisibility(View.VISIBLE);
				} else {
					scrollMsg.setVisibility(View.GONE);
				}
			}
		});

		surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId()) ;
		if(surveyAnswer_attend_pos!=-1){
		///if(QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId())!=-1){
			ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
			//Log.v((surveyAnswer_attend_pos) + "", ts_answer.getSurveyAnswer());
			qListview.setItemChecked(ts_answer.getoptionPos(), true);
		}

		backButton = (ImageView) findViewById(R.id.qRadioBack);
		nextButton = (ImageView) findViewById(R.id.qRadioNext);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int selected_option = -1 ;
				try {
					boolean checked = false;
					for (int i = 0; i < qListview.getCount(); i++) {
						if (qListview.isItemChecked(i)) {
							selected_option = i ;
							if(surveyAnswer_attend_pos!=-1){
								Survey.surveyAnswers.get(surveyAnswer_attend_pos)
										.setOptionPos(i);
								Survey.surveyAnswers.get(surveyAnswer_attend_pos)
										.setSurveyAnswer(
												s_question.getOption(i));
							} else {
								Timestamp currentTimeString = new Timestamp(new Date().getTime());
								s_answer = new SurveyAnswers(s_question.getQuestionId(), s_question
										.getQuestionText(), s_question
										.getOption(i), currentTimeString.toString(), i,
										s_question.getQuestType(), null, null,
										null, null,Survey.q_id);
								Survey.surveyAnswers.add(s_answer);
							}
							//Log.v("Q_ID", "" + Survey.q_id);
							/*for (int j = 0; j < Survey.surveyAnswers.size(); j++) {
								//Log.v("selected_option"
										+ Survey.surveyAnswers.get(j)
												.getSurveyQuestion(), ""
										+ Survey.surveyAnswers.get(j)
												.getSurveyAnswer());
							}*/
							checked = true;
						}
					}
					if (checked)
					{
						//Survey.q_id++;
						
						/////////////////NEXT QUESTION //////////////
						LoopQuestion loopQuestion  = new LoopQuestion(ImageSelection.this);
						if(s_question.getOption_link_id()!=-1 || s_question.getQuestion_tree_id()!=-1)// this is a loop question
						{
							
							//System.out.println("selected_option" +s_question.getOptionIdSelected(selected_option));
							int next_qid  ;
							next_qid = loopQuestion.findNextQuestionidpostion(s_question.getOptionIdSelected(selected_option));
							//System.out.println("Next Qid" +next_qid);
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
						/////////////////NEXT QUESTION //////////////
						
					//	if (Survey.surveyQuestions.size() > Survey.q_id) {
						if (Survey.q_id !=-1) {
							SurveyTypeUtils swit = new SurveyTypeUtils();
							startActivityForResult(
									swit.getIntent(getApplicationContext()),
									UserValues.VIEW_USER_REQ);
						} else {
							Intent i = new Intent(getApplicationContext(),
									SurveyUpdateActivity.class);
							startActivityForResult(i,UserValues.VIEW_USER_REQ);
						}

					}
					else {
						alertbox("Error", "Select a choice");
					}
				} catch (Exception e) {
					// TODO: handle exception
					//Log.v("Error", e.toString());
				}
			}
		});

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
		AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, ImageSelection.this);
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
	
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ImageSelection.this);
			pDialog.setMessage("Loading Image ....");
			pDialog.show();

		}

		protected Bitmap doInBackground(String... args) {
			try {
				StringToBitMap(imagebitMap);


			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		private void ByteToBitMap(byte[] imagebitMap) {
			// TODO Auto-generated method stub
			bitmap = BitmapFactory.decodeByteArray(imagebitMap, 0,
					imagebitMap.length);
		}

		protected void onPostExecute(Bitmap image) {

			if (image != null) {
				// img.setImageBitmap(image);
				imageFeedback.setImageBitmap(bitmap);

				pDialog.dismiss();

			} else {

				pDialog.dismiss();
				Toast.makeText(ImageSelection.this,
						"Image Does Not exist or Network Error",
						Toast.LENGTH_SHORT).show();

			}
		}
	}
	
	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);

			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
