package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class MultiSelectionCheck extends Activity {

    private TextView questionText;
    private TextView scrollMsg;
    private ImageView backButton;
    private ImageView nextButton;
    private int q_id;
    private SurveyQuestions s_question;
    private ListView qListview;
    private SurveyAnswers s_answer;
    private SurveyAnswers ts_answer;
    private int surveyAnswer_attend_pos;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Context context = this;
    Typeface typeface;
    TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);
        setContentView(R.layout.q_checkbox_list);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MultiSelectionCheck.this.finish();

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }

        // get question from
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        scrollMsg = (TextView) findViewById(R.id.txt_check_scrollmsg);
        questionText = (TextView) findViewById(R.id.qCheckTextView);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);

        questionText.setTypeface(typeface);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        questionText.setText(s_question.getQuestionText());
        qListview = (ListView) findViewById(R.id.qMultipleChoiceListView);
        ArrayAdapter<String> cadapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.simple_list_item_multiple_choice,
                s_question.getQuestionOptions());
        cadapter.notifyDataSetChanged();
        qListview.setAdapter(cadapter);
        qListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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

        nextButton = (ImageView) findViewById(R.id.qCheckNext);
        surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId());
        if (surveyAnswer_attend_pos != -1) {
            //if (Survey.surveyAnswers.size() > Survey.q_id) {
            ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
            int tmp_pos[] = ts_answer.getSubpos();
            for (int i = 0; i < tmp_pos.length; i++) {
                //Log.v("" + i, tmp_pos[i] + "");
                if (tmp_pos[i] == 1) {
                    qListview.setItemChecked(i, true);
                    //Log.v("" + i, "Checked");
                }
            }
        }


        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    boolean checked = false;
                    int checkd_pos[] = new int[qListview.getCount()];
                    for (int i = 0; i < qListview.getCount(); i++) {
                        if (qListview.isItemChecked(i)) {
                            checked = true;
                            checkd_pos[i] = 1;
                        } else {
                            checkd_pos[i] = 0;
                        }
                    }
                    if (checked) {
                        if (surveyAnswer_attend_pos != -1) {
                            //if (Survey.surveyAnswers.size() > Survey.q_id) {
                            Survey.surveyAnswers.get(surveyAnswer_attend_pos)
                                    .setSubPosition(checkd_pos);
                        } else {
                            Timestamp currentTimeString = new Timestamp(new Date().getTime());
                            s_answer = new SurveyAnswers(s_question
                                    .getQuestionId(), s_question
                                    .getQuestionText(), null,
                                    currentTimeString.toString(), 0, s_question
                                    .getQuestType(), s_question
                                    .getQuestionOptions(), null,
                                    checkd_pos, null, Survey.q_id);
                            Survey.surveyAnswers.add(s_answer);
                        }

                        //Survey.q_id++;


                        /////////////////NEXT QUESTION //////////////
                        LoopQuestion loopQuestion = new LoopQuestion(MultiSelectionCheck.this);
                        Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
                        //System.out.println("Multi Selection Id " +Survey.q_id );
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

                    } else {
                        alertbox("Error", "Select a choice");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    //Log.v("Error", e.toString());
                }
            }
        });

        backButton = (ImageView) findViewById(R.id.qCheckBack);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
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
        AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, MultiSelectionCheck.this);
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
