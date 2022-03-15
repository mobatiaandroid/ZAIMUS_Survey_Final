package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class Star extends Activity {

    private ImageView nextButton;
    private ImageView backButton;
    private RatingBar star;
    private int q_id;
    private SurveyAnswers s_answer;
    private SurveyAnswers ts_answer;
    private SurveyQuestions s_question;
    private TextView questionText;
    private int surveyAnswer_attend_pos;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Typeface typeface;
    TextView txtqDecisionBack, txtqDecisionNext;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);

        setContentView(R.layout.q_star);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Star.this.finish();

            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }

        nextButton = (ImageView) findViewById(R.id.bt_star_next);
        backButton = (ImageView) findViewById(R.id.bt_star_back);
        star = (RatingBar) findViewById(R.id.starbar);
        questionText = (TextView) findViewById(R.id.txt_star_question);
        questionText.setTypeface(typeface);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        questionText.setText(s_question.getQuestionText());
        surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId());
        if (surveyAnswer_attend_pos != -1) {
            //if (Survey.surveyAnswers.size() > Survey.q_id) {
            ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
            star.setRating(Float.parseFloat(ts_answer.getSurveyAnswer()));
        }


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
                //Log.v("", star.getRating() + " Out of " + star.getNumStars());
                if (surveyAnswer_attend_pos != -1) {
                    //if (Survey.surveyAnswers.size() > Survey.q_id) {
                    Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSurveyAnswer(
                            "" + star.getRating());
                } else {
                    Timestamp currentTimeString = new Timestamp(new Date().getTime());
                    s_answer = new SurveyAnswers(s_question.getQuestionId(),
                            s_question.getQuestionText(),
                            "" + star.getRating(), currentTimeString.toString(), 0,
                            s_question.getQuestType(), null, null, null, null, Survey.q_id);
                    Survey.surveyAnswers.add(s_answer);
                }

                //Survey.q_id++;
/////////////////NEXT QUESTION //////////////
                LoopQuestion loopQuestion = new LoopQuestion(Star.this);
                Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
                loopQuestion.closeDB();

                /////////////////NEXT QUESTION //////////////

                if (Survey.q_id != -1) {
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
        surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId());
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
    protected void onPause() {
        super.onPause();
        AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, Star.this);
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
