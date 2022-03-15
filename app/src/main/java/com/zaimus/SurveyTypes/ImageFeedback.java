package com.zaimus.SurveyTypes;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class ImageFeedback extends Activity {

    private ImageView buttonBack;
    private ImageView buttonNext;
    private Bitmap bmp;
    private TextView questionText;
    private SurveyQuestions s_question;
    private SurveyAnswers s_answer;
    private SurveyAnswers ts_answer;
    private int q_id;
    private int surveyAnswer_attend_pos;
    EditText txt_txtip_input;
    String imagebitMap;
    Context context = this;
    Bitmap bitmap;
    ImageView img_txtip_question;
    /**
     * Called when the activity is first created.
     */
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    ProgressDialog pDialog;
    TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;
    Typeface typeface;
    SQLiteAdapter mySqLiteAdapter;
    LoadImage image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);
        setContentView(R.layout.q_image_feedback);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageFeedback.this.finish();

            }
        });
        GlobalConstants.DEVICE_ID = Utils.getDeviceId(ImageFeedback.this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        questionText = (TextView) findViewById(R.id.txt_txtip_question);
        questionText.setText(s_question.getQuestionText());
        mySqLiteAdapter = new SQLiteAdapter(getApplicationContext());
        mySqLiteAdapter.openToRead();
        String master_id = mySqLiteAdapter.getMasterId(String.valueOf(s_question.getQuestionId()));
        //System.out.print(master_id+"<-----------MASTER ID--------->");
        imagebitMap = mySqLiteAdapter.getImageByID(master_id);
        //System.out.print(imagebitMap+"<-----------MASTER ID--------->");

        mySqLiteAdapter.close();
        image = new LoadImage();
        image.execute();
        buttonBack = (ImageView) findViewById(R.id.bt_txtip_back);
        buttonNext = (ImageView) findViewById(R.id.bt_txtip_next);
        txt_txtip_input = (EditText) findViewById(R.id.txt_txtip_input);
        img_txtip_question = (ImageView) findViewById(R.id.img_txtip_question);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);

        txt_txtip_input.setTypeface(typeface);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        questionText.setTypeface(typeface);
        /*
         * buttonImageCapture = (Button) findViewById(R.id.bt_take_picture);
         * buttonShowPicture = (Button) findViewById(R.id.bt_show_picture);
         * buttonDeletePicture = (Button) findViewById(R.id.bt_delete_picture);
         */

        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        /*
         * if (surveyAnswer_attend_pos != -1) { ts_answer =
         * Survey.surveyAnswers.get(surveyAnswer_attend_pos); if
         * (ts_answer.getBitmapPicture() != null) { bmp = (Bitmap)
         * ts_answer.getBitmapPicture();
         * buttonImageCapture.setVisibility(View.INVISIBLE);
         * buttonShowPicture.setVisibility(View.VISIBLE);
         * buttonDeletePicture.setVisibility(View.VISIBLE); } else {
         * buttonImageCapture.setVisibility(View.VISIBLE);
         * buttonShowPicture.setVisibility(View.INVISIBLE);
         * buttonDeletePicture.setVisibility(View.INVISIBLE); } }
         */

        // imageiewImageCaptured = (ImageView)findViewById(R.id.imagecaptured);
        /*
         * Button.OnClickListener buttonImageCaptureOnClickListener = new
         * Button.OnClickListener() {
         *
         * @Override public void onClick(View arg0) { // TODO Auto-generated
         * method stub Intent intent = new Intent(
         * android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
         * startActivityForResult(intent, 0); } };
         */

        /*
         * buttonShowPicture.setOnClickListener(new OnClickListener() {
         *
         * @Override public void onClick(View v) { // TODO Auto-generated method
         * stub Intent i = new Intent(getApplicationContext(),
         * ShowPicture.class); i.putExtra("BitmapImage", bmp); startActivity(i);
         * } });
         */

        /*
         * buttonDeletePicture.setOnClickListener(new OnClickListener() {
         *
         * @Override public void onClick(View v) { // TODO Auto-generated method
         * stub bmp = null; // Reset Bitmap Image if (surveyAnswer_attend_pos !=
         * -1) { // if (Survey.surveyAnswers.size() > Survey.q_id) {
         * Survey.surveyAnswers
         * .get(surveyAnswer_attend_pos).setBitmapPicture(bmp); }
         * buttonImageCapture.setVisibility(View.VISIBLE);
         * buttonShowPicture.setVisibility(View.INVISIBLE);
         * buttonDeletePicture.setVisibility(View.INVISIBLE); } });
         */

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
                if (!txt_txtip_input.getText().toString().equals("")) {
                    if (surveyAnswer_attend_pos != -1) {
                        // Survey.surveyAnswers.get(surveyAnswer_attend_pos).setBitmapPicture(bmp);
                        //System.out.print("next btn" + "-> ");
                    } else {

                        Timestamp currentTimeString = new Timestamp(new Date()
                                .getTime());


                        s_answer = new SurveyAnswers(
                                s_question.getQuestionId(), s_question
                                .getQuestionText(), txt_txtip_input
                                .getText().toString(),
                                currentTimeString.toString(), 0, s_question
                                .getQuestType(), null, null, null,
                                bitmap, Survey.q_id);
                        Survey.surveyAnswers.add(s_answer);
                    }
                    // Survey.q_id++;

                    // ///////////////NEXT QUESTION //////////////
                    LoopQuestion loopQuestion = new LoopQuestion(
                            ImageFeedback.this);
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
                } else {
                    alertbox("Error", "Please fill the field");
                }
            }
        });

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

            // buttonImageCapture.setVisibility(View.INVISIBLE);
            // /buttonShowPicture.setVisibility(View.VISIBLE);
            // buttonDeletePicture.setVisibility(View.VISIBLE);
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

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ImageFeedback.this);
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
                img_txtip_question.setImageBitmap(bitmap);

                pDialog.dismiss();

            } else {

                pDialog.dismiss();
                Toast.makeText(ImageFeedback.this,
                        "Image Does Not exist or Network Error",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}