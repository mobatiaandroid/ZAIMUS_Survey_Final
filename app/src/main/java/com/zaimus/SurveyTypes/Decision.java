package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zaimus.LoopQuestion;
import com.zaimus.MyApplication;
import com.zaimus.R;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.SearchUserActivity;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.UsrValues.SdcardManager;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.api.IVkcApis;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.AudioRecorder;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class Decision extends Activity {

    private SQLiteAdapter mySQlAdapter;
    private String customer_id;
    private ImageView nextButton;
    private ImageView backButton;
    private RadioGroup radioGroup;
    AudioRecorder ar;
    boolean audioStarted = false;
    Context context = this;
    LoopQuestion.surveyquestionCls[] treeqids = null;
    Bitmap bitmap;
    ProgressDialog pDialog;
    String master_id;
    // String URL="http://dev.mobatia.com/vkcsurvey_uat/";
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Typeface typeface;
    TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;
    ArrayList<Survey> imageList = new ArrayList<Survey>();
    Survey survey;
    String temp;
    byte[] b;
    String quesId;
    String sdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);
        Bundle extras = getIntent().getExtras();
        survey = new Survey();
        Random random = new Random();

        // generate a random integer from 0 to 899, then add 100
        int x = random.nextInt(100000) + 100;
        MyApplication.randomNo = String.valueOf(x);
        /*
         * setContentView(R.layout.q_decision); InitializeComponents();
         */
        if (extras != null) {
            customer_id = extras.getString("Id");
            setContentView(R.layout.q_decision);
            header = (LinearLayout) findViewById(R.id.header);
            headermanager = new Headermanager(activity, "");
            headermanager.getHeader(header, 1, false);
            headermanager.setButtonLeftSelector(R.drawable.back,
                    R.drawable.back);
            splitIcon = headermanager.getLeftButton();
            splitIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Decision.this.finish();

                }
            });
            InitializeComponents();
        }
    }

    @Override
    protected void onResume() {
        if (audioStarted) {
            if (ar != null) {
                ar.onRecord(false);
                // File file = new File(GlobalConstants.AUDIO_FILEPATH);
                // boolean deleted = file.delete();
                // //Log.e("", "status" + deleted);

            }
            audioStarted = false;
        }

        super.onResume();
    }

    protected void InitializeComponents() {

        radioGroup = (RadioGroup) findViewById(R.id.qDecisionRG);

        nextButton = (ImageView) findViewById(R.id.qDecisionNext);
        qDecisionTextView = (TextView) findViewById(R.id.qDecisionTextView);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
        RadioButton qDecisionRBYes = (RadioButton) findViewById(R.id.qDecisionRBYes);
        RadioButton qDecisionRBNo = (RadioButton) findViewById(R.id.qDecisionRBNo);
        qDecisionRBNo.setTypeface(typeface);
        qDecisionRBYes.setTypeface(typeface);
        qDecisionTextView.setTypeface(typeface);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.qDecisionRBYes:

                        /* NEW FEATURE LOCATION LISTENING AND AUDIO RECODER */
                        // alertbox("Error",
                        // "Please import customer data to your device..Then Proceed");
                        if (AppPreferenceManager.getSurveyId(context).equals("")) {
                            Intent intent = new Intent(Decision.this,
                                    SearchUserActivity.class);
                            startActivity(intent);

                        } else {
                            //getImages();

                            getSurveyData();
                        }
                        if (SdcardManager.isSDCARDMounted()) {
                            /*
                             * String filePath; Timestamp currentTimeString = new
                             * Timestamp(new Date().getTime());
                             *
                             * GlobalConstants.AUDIO_FILEPATH=
                             * Utils.getFilePath()+"/" + customer_id + "_" +
                             * Utils.getDeviceId(Decision.this) + "_" +
                             * Survey.surveyset_id +"_"
                             * +currentTimeString.getHours() +"_"
                             * +currentTimeString.getSeconds() +".mp3";
                             *
                             *
                             * GlobalConstants.AUDIO_FILENAME=customer_id + "_" +
                             * Utils.getDeviceId(Decision.this) + "_" +
                             * Survey.surveyset_id +"_"
                             * +currentTimeString.getHours() +"_"
                             * +currentTimeString.getSeconds() +".mp3"; ar = new
                             * AudioRecorder(); ar.onRecord(true);
                             * audioStarted=true;
                             */

                        } else {
                            Toast.makeText(Decision.this, "Sdcard is not mounted",
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.qDecisionRBNo:
                        updateDecision();
                        break;
                    default:
                        alertbox("Warning", "Select option!");
                        break;
                }
            }

            private void getImages() {
                // TODO Auto-generated method stub
                mySQlAdapter = new SQLiteAdapter(getApplicationContext());

                mySQlAdapter.openToRead();
                imageList = mySQlAdapter.getAllImages();

                if (imageList.size() > 0) {
                    for (int k = 0; k < imageList.size(); k++) {
                        //AppPreferenceManager.saveImageId(imageList.get(k).questionId, context);
                        quesId = imageList.get(k).questionId;
                        //System.out.println("Question ID----------->"+quesId);
                        try {

                            new LoadImage().execute((IVkcApis.IMAGE_FEEDBACK_URL
                                    + "" + imageList.get(k).imagePath), quesId);

                        } catch (Exception e) {

                        }
                    }
                }
                mySQlAdapter.close();
            }

        });

        backButton = (ImageView) findViewById(R.id.qDecisionBack);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    protected void updateDecision() {
        try {
            mySQlAdapter = new SQLiteAdapter(getApplicationContext());
            mySQlAdapter.openToWrite();
            String[][] cons = {{"customer_id", customer_id}};
            String[][] data = {{"customer_status", "0"}, {"status", "2"}};
            mySQlAdapter.update(data, "survey_customerdetails", cons);
            mySQlAdapter.close();
            alertboxDecision("Updated", "Survey decision updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void alertboxDecision(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setIcon(android.R.drawable.stat_notify_error)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                setResult(UserValues.SURVEY_RES);
                                finish();
                            }
                        }).show();
    }

    private Runnable returnRes = new Runnable() {
        // All questions loaded
        @Override
        public void run() {
            if (Survey.q_id != -1) {
                Survey.customer_id = customer_id;
                SurveyTypeUtils swit = new SurveyTypeUtils();
                startActivityForResult(swit.getIntent(getApplicationContext()),
                        UserValues.VIEW_USER_REQ);
            } else {
                alertbox("Sorry!!!!", "No Current Survey Data");
                if (audioStarted) {
                    if (ar != null) {
                        ar.onRecord(false);
                        // File file = new File(GlobalConstants.AUDIO_FILEPATH);
                        // boolean deleted = file.delete();
                        // //Log.e("", "status" + deleted);

                    }
                    audioStarted = false;
                }

            }
        }
    };

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

    protected void getSurveyData() {

        // Reset Everything in servey DataStructure

        Survey.surveyQuestions = new ArrayList<SurveyQuestions>();
        Survey.surveyAnswers = new ArrayList<SurveyAnswers>();
        Survey.customer_id = "";

        Survey.surveyset_id = Integer.parseInt(AppPreferenceManager
                .getSurveyId(context));
        Survey.q_id = -1;

        // Call a thread for getting data from database

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            // TODO Auto-generated method stub

                            LoopQuestion loopQuestion = null;
                            mySQlAdapter = new SQLiteAdapter(
                                    getApplicationContext());
                            mySQlAdapter.openToRead();
                            String[] columns = new String[]{"question_id",
                                    "question_text", "question_type",
                                    "question_surveyset_id", "parent_id",
                                    "question_tree_id,option_link_id,enum_path"};
                            Cursor cursor = mySQlAdapter.queueAll(
                                    "survey_questions", columns,
                                    "question_order",
                                    "question_surveyset_id = "
                                            + Survey.surveyset_id
                                            + " and option_link_id=0");
                            /*
                             * Cursor cursor = mySQlAdapter.queueAll(
                             * "survey_questions", columns, "question_order",
                             * "question_surveyset_id = 12"
                             * +" and option_link_id=0");
                             */

                            loopQuestion = new LoopQuestion(Decision.this,
                                    mySQlAdapter);
                            cursor.moveToPosition(0);

                            while (cursor.isAfterLast() == false) {


                                if (cursor.getString(0).equals(
                                        cursor.getString(5))) {

                                    String[] options = null;
                                    int[] options_id = null;

                                    // LoopQuestion.surveyquestionCls[] treeqids
                                    // = null;
                                    LoopQuestion.optionValueCls[] optionsArry = null;

                                    treeqids = loopQuestion
                                            .getTreeIdsOrderBy(cursor
                                                    .getString(0));
                                    for (int j = 0; j < treeqids.length; j++) {
                                        if (treeqids[j].question_type
                                                .equals(UserValues.Q_MATRIX)) {


                                            Cursor qopCursor = mySQlAdapter
                                                    .queueAll(
                                                            "survey_questionoptions",
                                                            new String[]{"option_text"},
                                                            "options_id asc",
                                                            "question_id = "
                                                                    + treeqids[j].questionid);
                                            String[] options_loop = new String[qopCursor
                                                    .getCount()];

                                            int i = 0;
                                            qopCursor.moveToPosition(0);
                                            while (qopCursor.isAfterLast() == false) {
                                                options_loop[i] = qopCursor
                                                        .getString(0);
                                                qopCursor.moveToNext();
                                                i++;
                                            }
                                            qopCursor.close();

                                            // get sub questions

                                            Cursor subQuesCursor = mySQlAdapter
                                                    .queueAll(
                                                            "survey_questions",
                                                            new String[]{
                                                                    "question_text",
                                                                    "question_id"},
                                                            "question_id asc",
                                                            "parent_id = "
                                                                    + treeqids[j].questionid);

                                            int jj = 0;
                                            String[] sub_questions = new String[subQuesCursor
                                                    .getCount()];
                                            int[] sub_questions_id = new int[subQuesCursor
                                                    .getCount()];

                                            subQuesCursor.moveToPosition(0);
                                            while (subQuesCursor.isAfterLast() == false) {
                                                sub_questions[jj] = subQuesCursor
                                                        .getString(0);
                                                sub_questions_id[jj] = Integer
                                                        .parseInt(subQuesCursor
                                                                .getString(1));

                                                subQuesCursor.moveToNext();
                                                jj++;
                                            }
                                            subQuesCursor.close();

                                            // Apply to SurveyQuestions
                                            // DataStructure

                                            SurveyQuestions ques = new SurveyQuestions(
                                                    Integer.parseInt(treeqids[j].questionid),
                                                    treeqids[j].question_text,
                                                    treeqids[j].question_type,
                                                    options_loop,
                                                    sub_questions, options_id,
                                                    sub_questions_id);
                                            if (treeqids[j].option_link_id == 0) {
                                                ques.setOption_link_id(-1);
                                            } else {
                                                ques.setOption_link_id(treeqids[j].option_link_id);
                                            }
                                            ques.setEnum_path(treeqids[j].enum_path);
                                            ques.setQuestion_tree_id(treeqids[j].question_tree_id);
                                            Survey.surveyQuestions.add(ques);

                                            // //////////////////////////MATRIX
                                            // IN
                                            // LOOOP////////////////////////////
                                        }
                                        // //////////////////////////////FEEDBACK
                                        // TYPE///////////////////////////////////
                                        /*
                                         * else if (treeqids[j].question_type
                                         * .equals
                                         * (UserValues.Q_PRODUCT_FEEDBACK)) {
                                         * Log.d("TAG",
                                         * "1-------------->Feedback");
                                         *
                                         * Cursor qopCursor = mySQlAdapter
                                         * .queueAll( "question_images", new
                                         * String[] { "id", "questionId",
                                         * "imagePath" }, null, "questionId = "
                                         * + treeqids[j].master_id);
                                         *
                                         * qopCursor.moveToPosition(0); String[]
                                         * option = new String[qopCursor
                                         * .getCount()]; while
                                         * (qopCursor.isAfterLast() == false) {
                                         * qopCursor.moveToNext(); new
                                         * LoadImage(
                                         * ).execute(IVkcApis.IMAGE_FEEDBACK_URL
                                         * + qopCursor.getString(2));
                                         *
                                         * new LoadImage() .execute(
                                         * "http://freehdw.com/wallpapers/creativity-water-spray-drops-flower-rose-desktop-images.jpg"
                                         * );
                                         *
                                         *
                                         * } }
                                         */

                                        // ////////////////////////////////AUTO
                                        // COMPLETE
                                        // ///////////////////////////////////////
                                        else if (treeqids[j].question_type
                                                .equals(UserValues.Q_DYNAMIC_SUG)) {

                                            Cursor qopCursor = mySQlAdapter
                                                    .queueAll(
                                                            "survey_multitextoptions",
                                                            new String[]{
                                                                    "no_of_textfileds",
                                                                    "search_ids"},
                                                            null,
                                                            "question_id = "
                                                                    + treeqids[j].questionid);
                                            qopCursor.moveToPosition(0);
                                            String[] count = new String[1];
                                            String cate = "";
                                            while (qopCursor.isAfterLast() == false) {
                                                count[0] = qopCursor
                                                        .getString(0);
                                                cate = qopCursor.getString(1);
                                                qopCursor.moveToNext();
                                            }
                                            qopCursor.close();
                                            String[] cates = cate.split(",");
                                            // //Log.v("Options :",Arrays.toString(options));

                                            /*
                                             * SurveyQuestions ques = new
                                             * SurveyQuestions(
                                             * Integer.parseInt(
                                             * treeqids[j].questionid),
                                             * treeqids[j].question_text,
                                             * treeqids[j].question_type,
                                             * options, sub_questions,
                                             * options_id, sub_questions_id);
                                             */
                                            SurveyQuestions ques = new SurveyQuestions(
                                                    Integer.parseInt(treeqids[j].questionid),
                                                    treeqids[j].question_text,
                                                    treeqids[j].question_type,
                                                    cates, count, null, null);

                                            if (treeqids[j].option_link_id == 0) {
                                                ques.setOption_link_id(-1);
                                            } else {
                                                ques.setOption_link_id(treeqids[j].option_link_id);
                                            }
                                            ques.setEnum_path(treeqids[j].enum_path);
                                            ques.setQuestion_tree_id(treeqids[j].question_tree_id);

                                            Survey.surveyQuestions.add(ques);

                                        }
                                        // //////////////////////////////////AUTO
                                        // COMPLETE////////////
                                        else {

                                            optionsArry = loopQuestion
                                                    .getAlloptionsOfQuestionid(treeqids[j].questionid);
                                            options = new String[optionsArry.length];
                                            options_id = new int[optionsArry.length];
                                            for (int j2 = 0; j2 < optionsArry.length; j2++) {

                                                options[j2] = optionsArry[j2].option_value;
                                                options_id[j2] = Integer
                                                        .parseInt(optionsArry[j2].option_id);

                                            }

                                            /*
                                             * SurveyQuestions ques = new
                                             * SurveyQuestions(
                                             * Integer.parseInt(
                                             * treeqids[j].questionid), cursor
                                             * .getString(1), cursor
                                             * .getString(2), options, null,
                                             * options_id, null);
                                             */

                                            SurveyQuestions ques = new SurveyQuestions(
                                                    Integer.parseInt(treeqids[j].questionid),
                                                    treeqids[j].question_text,
                                                    treeqids[j].question_type,
                                                    options, null, options_id,
                                                    null);

                                            if (treeqids[j].option_link_id == 0) {
                                                ques.setOption_link_id(-1);
                                            } else {
                                                ques.setOption_link_id(treeqids[j].option_link_id);
                                            }

                                            ques.setEnum_path(treeqids[j].enum_path);
                                            ques.setQuestion_tree_id(treeqids[j].question_tree_id);
                                            Survey.surveyQuestions.add(ques);
                                        }

                                    }

                                } else {

                                    if (cursor.getString(2).equalsIgnoreCase(

                                            UserValues.Q_MATRIX)) {
                                        if (cursor.getString(4)
                                                .equalsIgnoreCase("0")) {

                                            // Get Question options

                                            Cursor qopCursor = mySQlAdapter
                                                    .queueAll(
                                                            "survey_questionoptions",
                                                            new String[]{"option_text"},
                                                            "options_id asc",
                                                            "question_id = "
                                                                    + cursor.getInt(0));
                                            String[] options = new String[qopCursor
                                                    .getCount()];

                                            int i = 0;
                                            qopCursor.moveToPosition(0);
                                            while (qopCursor.isAfterLast() == false) {
                                                options[i] = qopCursor
                                                        .getString(0);
                                                qopCursor.moveToNext();
                                                i++;
                                            }
                                            qopCursor.close();

                                            // get sub questions

                                            Cursor subQuesCursor = mySQlAdapter
                                                    .queueAll(
                                                            "survey_questions",
                                                            new String[]{
                                                                    "question_text",
                                                                    "question_id"},
                                                            "question_id asc",
                                                            "parent_id = "
                                                                    + cursor.getInt(0));

                                            int j = 0;
                                            String[] sub_questions = new String[subQuesCursor
                                                    .getCount()];
                                            int[] sub_questions_id = new int[subQuesCursor
                                                    .getCount()];

                                            subQuesCursor.moveToPosition(0);
                                            while (subQuesCursor.isAfterLast() == false) {
                                                sub_questions[j] = subQuesCursor
                                                        .getString(0);
                                                sub_questions_id[j] = Integer
                                                        .parseInt(subQuesCursor
                                                                .getString(1));

                                                subQuesCursor.moveToNext();
                                                j++;
                                            }
                                            subQuesCursor.close();

                                            // Apply to SurveyQuestions
                                            // DataStructure

                                            SurveyQuestions ques = new SurveyQuestions(
                                                    cursor.getInt(0), cursor
                                                    .getString(1),
                                                    cursor.getString(2),
                                                    options, sub_questions,
                                                    null, sub_questions_id);

                                            Survey.surveyQuestions.add(ques);
                                        }
                                    } else if (cursor.getString(2)
                                            .equalsIgnoreCase(
                                                    UserValues.Q_DYNAMIC_SUG)) {

                                        Cursor qopCursor = mySQlAdapter
                                                .queueAll(
                                                        "survey_multitextoptions",
                                                        new String[]{
                                                                "no_of_textfileds",
                                                                "search_ids"},
                                                        null,
                                                        "question_id = "
                                                                + cursor.getInt(0));
                                        qopCursor.moveToPosition(0);
                                        String[] count = new String[1];
                                        String cate = "";
                                        while (qopCursor.isAfterLast() == false) {
                                            count[0] = qopCursor.getString(0);
                                            cate = qopCursor.getString(1);
                                            qopCursor.moveToNext();
                                        }
                                        qopCursor.close();
                                        String[] cates = cate.split(",");
                                        // //Log.v("Options :",Arrays.toString(options));

                                        SurveyQuestions ques = new SurveyQuestions(
                                                cursor.getInt(0), cursor
                                                .getString(1), cursor
                                                .getString(2), cates,
                                                count, null, null);
                                        Survey.surveyQuestions.add(ques);

                                    } else {

                                        Cursor qopCursor = mySQlAdapter
                                                .queueAll(
                                                        "survey_questionoptions",
                                                        new String[]{
                                                                "option_text",
                                                                "options_id"},
                                                        "options_id asc",
                                                        "question_id = "
                                                                + cursor.getInt(0));
                                        String[] options = new String[qopCursor
                                                .getCount()];
                                        int[] options_id = new int[qopCursor
                                                .getCount()];

                                        int i = 0;
                                        qopCursor.moveToPosition(0);
                                        while (qopCursor.isAfterLast() == false) {
                                            options[i] = qopCursor.getString(0);
                                            options_id[i] = Integer
                                                    .parseInt(qopCursor
                                                            .getString(1));
                                            qopCursor.moveToNext();
                                            i++;
                                        }
                                        qopCursor.close();

                                        // //Log.v("Options :",Arrays.toString(options));
                                        /*
                                         * treeqids = loopQuestion
                                         * .getTreeIdsOrderBy(cursor
                                         * .getString(0)); for(int
                                         * j=0;j<treeqids.length;i++){
                                         * Log.d("TAG",
                                         * "Master Id is------------>"
                                         * +treeqids[j].master_id);
                                         *
                                         * }
                                         */
                                        SurveyQuestions ques = new SurveyQuestions(
                                                cursor.getInt(0), cursor
                                                .getString(1), cursor
                                                .getString(2), options,
                                                null, options_id, null);
                                        Survey.surveyQuestions.add(ques);

                                    }

                                }
                                cursor.moveToNext();
                                Survey.q_id = 0;
                            }
                            cursor.close();

                            mySQlAdapter.close();
                            loopQuestion.closeDB();
                        } catch (Exception e) {
                        }

                        SurveyQuestions surveyQuestions;

                        for (int i = 0; i < Survey.surveyQuestions.size(); i++) {
                            surveyQuestions = (SurveyQuestions) Survey.surveyQuestions
                                    .get(i);

                        }

                        // ////////////////////////testING FUNCTIONALITIES ////
                        LoopQuestion loopQuestion1 = new LoopQuestion(
                                Decision.this);
                        loopQuestion1.createArray();

                        loopQuestion1.closeDB();

                        // ////////////////////////testING FUNCTIONALITIES ////
                        runOnUiThread(returnRes);
                    }
                });
            }
        }).start();
    }

    @Override
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

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                if (!isFinishing()) { // Without this in certain cases
                    // application will show ANR
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            Decision.this);
                    builder.setMessage(
                            "Your GPS is disabled! Would you like to enable it?")
                            .setCancelable(false)
                            .setPositiveButton("Enable GPS",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            Intent gpsOptionsIntent = new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(gpsOptionsIntent);
                                        }
                                    });
                    builder.setNegativeButton("Do nothing",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

        }
    };

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private String localQuesId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Decision.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.hide();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                localQuesId = args[1];
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                        args[0]).getContent());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
                b = baos.toByteArray();
                temp = Base64.encodeToString(b, Base64.DEFAULT);
                // return temp;
                //AppPreferenceManager.saveImage(temp, context);
                //Log.d("TAG", "IMAGE-------------->" + temp);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            pDialog.dismiss();

            if (image != null) {
                pDialog.dismiss();
                mySQlAdapter.openToWrite();
                //survey.image_download = temp;
                mySQlAdapter.UpdateDownloadedimage(temp, localQuesId);

                mySQlAdapter.close();
            } else {

                pDialog.dismiss();
                Toast.makeText(Decision.this, "Image Does Not exists..",
                        Toast.LENGTH_SHORT).show();

            }

        }
    }

    private Bitmap getImageFromUrl(String url, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getHttpConnection(String surl) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(surl);
        URLConnection conn = url.openConnection();
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
