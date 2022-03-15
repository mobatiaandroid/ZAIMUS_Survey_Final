package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.gps.LocationTrack;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageCapture extends Activity {

    private Button buttonImageCapture;
    private Button buttonShowPicture;
    private Button buttonDeletePicture;
    private ImageView buttonBack;
    private ImageView buttonNext;
    private Bitmap bmp;
    private TextView questionText;
    private SurveyQuestions s_question;
    private SurveyAnswers s_answer;
    private SurveyAnswers ts_answer;
    private int q_id;
    private int surveyAnswer_attend_pos;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Context context = this;
    boolean check_gps;
    Typeface typeface;
    LocationTrack locationTrack;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);

        setContentView(R.layout.q_camera);
        locationTrack = new LocationTrack(ImageCapture.this);

        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageCapture.this.finish();

            }
        });
        GlobalConstants.DEVICE_ID = Utils.getDeviceId(ImageCapture.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }

        AppPreferenceManager.saveLocation(
                Double.toString(locationTrack.getLatitude()) + ","
                        + Double.toString(locationTrack.getLongitude()),
                context);
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        questionText = (TextView) findViewById(R.id.qCameraTextView);
        questionText.setText(s_question.getQuestionText());

        buttonBack = (ImageView) findViewById(R.id.bt_pic_back);
        buttonNext = (ImageView) findViewById(R.id.bt_pic_next);

        buttonImageCapture = (Button) findViewById(R.id.bt_take_picture);
        buttonShowPicture = (Button) findViewById(R.id.bt_show_picture);
        buttonDeletePicture = (Button) findViewById(R.id.bt_delete_picture);

        buttonImageCapture.setTypeface(typeface);
        buttonShowPicture.setTypeface(typeface);
        buttonDeletePicture.setTypeface(typeface);

        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        if (surveyAnswer_attend_pos != -1) {
            ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
            if (ts_answer.getBitmapPicture() != null) {
                bmp = (Bitmap) ts_answer.getBitmapPicture();
                buttonImageCapture.setVisibility(View.INVISIBLE);
                buttonShowPicture.setVisibility(View.VISIBLE);
                buttonDeletePicture.setVisibility(View.VISIBLE);
            } else {
                buttonImageCapture.setVisibility(View.VISIBLE);
                buttonShowPicture.setVisibility(View.INVISIBLE);
                buttonDeletePicture.setVisibility(View.INVISIBLE);
            }
        }

        // imageiewImageCaptured = (ImageView)findViewById(R.id.imagecaptured);
        Button.OnClickListener buttonImageCaptureOnClickListener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //if (gps.canGetLocation()) {
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                //} else {
                //gps.showSettingsAlert();
                //}
            }
        };

        buttonShowPicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),
                        ShowPicture.class);
                i.putExtra("BitmapImage", bmp);
                startActivity(i);
            }
        });

        buttonDeletePicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bmp = null; // Reset Bitmap Image
                if (surveyAnswer_attend_pos != -1) {
                    // if (Survey.surveyAnswers.size() > Survey.q_id) {
                    Survey.surveyAnswers.get(surveyAnswer_attend_pos)
                            .setBitmapPicture(bmp);
                }
                buttonImageCapture.setVisibility(View.VISIBLE);
                buttonShowPicture.setVisibility(View.INVISIBLE);
                buttonDeletePicture.setVisibility(View.INVISIBLE);
            }
        });

        buttonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        buttonNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // if (Survey.surveyAnswers.size() > Survey.q_id) {
                if (surveyAnswer_attend_pos != -1) {
                    Survey.surveyAnswers.get(surveyAnswer_attend_pos)
                            .setBitmapPicture(bmp);
                } else {
                    String currentTimeString = new SimpleDateFormat(
                            "yyyy-mm-dd HH:mm:ss").format(new Date());
                    s_answer = new SurveyAnswers(s_question.getQuestionId(),
                            s_question.getQuestionText(), "",
                            currentTimeString, 0, s_question.getQuestType(),
                            null, null, null, bmp, Survey.q_id);
                    Survey.surveyAnswers.add(s_answer);

                }
                // Survey.q_id++;

                // ///////////////NEXT QUESTION //////////////
                LoopQuestion loopQuestion = new LoopQuestion(ImageCapture.this);
                Survey.q_id = loopQuestion
                        .findNextQuestionidPositionFromSelectedQuestionid(s_question
                                .getQuestionId());
                loopQuestion.closeDB();

                // ///////////////NEXT QUESTION //////////////

                if (Survey.q_id != -1) {
                    // if (Survey.surveyQuestions.size() > Survey.q_id) {
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

        buttonImageCapture
                .setOnClickListener(buttonImageCaptureOnClickListener);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Survey.q_id = q_id;
        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        super.onResume();
        // AudioRecorder.stopTimer();
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
        // AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT,
        // ImageCapture.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");

            if (surveyAnswer_attend_pos != -1) {
                Survey.surveyAnswers.get(surveyAnswer_attend_pos)
                        .setBitmapPicture(bmp);
            } else {
                Timestamp currentTimeString = new Timestamp(
                        new Date().getTime());
                s_answer = new SurveyAnswers(s_question.getQuestionId(),
                        s_question.getQuestionText(), "",
                        currentTimeString.toString(), 0,
                        s_question.getQuestType(), null, null, null, bmp,
                        Survey.q_id);
                Survey.surveyAnswers.add(s_answer);
            }

            buttonImageCapture.setVisibility(View.INVISIBLE);
            buttonShowPicture.setVisibility(View.VISIBLE);
            buttonDeletePicture.setVisibility(View.VISIBLE);
        }

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