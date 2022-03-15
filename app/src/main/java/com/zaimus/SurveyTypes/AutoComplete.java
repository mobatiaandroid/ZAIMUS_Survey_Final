package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.SQLiteServices.SQLiteAdapter;
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

public class AutoComplete extends Activity {

    private String[] CUSTOMERS = {""};
    private String[] IDS = {""};
    Context context = this;

    private TableLayout qTablelayout;
    private int mCount = 0;
    private ImageView nextButton;
    private ImageView backButton;
    private ImageView addButton;
    private TextView questionText;
    private SurveyAnswers s_answer;
    private SurveyAnswers ts_answer;
    private SurveyQuestions s_question;
    private int q_id;
    private int surveyAnswer_attend_pos;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg;
    Activity activity = this;
    Typeface typeface;
    TextView txtqDecisionBack, txtqDecisionNext;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.q_dynamictext);
        typeface = Utils.setFontTypeToArial(context);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AutoComplete.this.finish();

            }
        });
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }

        InitializeComponents();
    }

    protected void InitializeComponents() {
        nextButton = (ImageView) findViewById(R.id.qDynamicNext);

        addButton = (ImageView) findViewById(R.id.bt_addDyText);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);

        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                setupRow("");
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String ans = "";
                if (!(mCount > 0)) {
                    alertbox("Error", "Atleast one entry required!");
                    return;
                }
                for (int i = 0; i < qTablelayout.getChildCount(); i++) {
                    TableRow trow = (TableRow) qTablelayout.getChildAt(i);
                    if (trow.getVisibility() == View.VISIBLE) {
                        AutoCompleteTextView txt = (AutoCompleteTextView) trow
                                .getChildAt(0);
                        if (!txt.getText().toString().equals("")) {
                            if (!checkValidText(txt.getText().toString())) {
                                txt.requestFocus();
                                txt.setText("");
                                alertbox("Error", "Enter valid data!");
                                return;
                            } else {
                                ans += reformat(txt.getText().toString()) + ",";
                                //Log.e("", "answer for auto complete text" + ans);
                            }
                        } else {
                            txt.requestFocus();

                            alertbox("Error", "Don't leave empty");
                            return;
                        }
                    }
                }
                ans = ans.substring(0, ans.length() - 1);
                if (surveyAnswer_attend_pos != -1) {
                    // if (Survey.surveyAnswers.size() > Survey.q_id) {
                    Survey.surveyAnswers.get(surveyAnswer_attend_pos)
                            .setSurveyAnswer("" + ans);
                } else {
                    Timestamp currentTimeString = new Timestamp(new Date()
                            .getTime());
                    s_answer = new SurveyAnswers(s_question.getQuestionId(),
                            s_question.getQuestionText(), "" + ans,
                            currentTimeString.toString(), 0, s_question
                            .getQuestType(), null, null, null, null,
                            Survey.q_id);
                    Survey.surveyAnswers.add(s_answer);
                }

                // Survey.q_id++;
                // ///////////////NEXT QUESTION //////////////
                LoopQuestion loopQuestion = new LoopQuestion(AutoComplete.this);
                Survey.q_id = loopQuestion
                        .findNextQuestionidPositionFromSelectedQuestionid(s_question
                                .getQuestionId());
                loopQuestion.closeDB();

                // ///////////////NEXT QUESTION //////////////

                if (Survey.q_id != -1) {
                    // if (Survey.surveyQuestions.size() > Survey.q_id) {
                    //Log.d("TAG", "ABC----------->");
                    SurveyTypeUtils swit = new SurveyTypeUtils();
                    startActivityForResult(
                            swit.getIntent(getApplicationContext()),
                            UserValues.VIEW_USER_REQ);
                } else {
                    //Log.d("TAG", "ABC----------->1");

                    Intent i = new Intent(getApplicationContext(),
                            SurveyUpdateActivity.class);
                    startActivityForResult(i, UserValues.VIEW_USER_REQ);
                }
            }
        });
        backButton = (ImageView) findViewById(R.id.qDynamicBack);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        questionText = (TextView) findViewById(R.id.qDynamicTextView);
        questionText.setTypeface(typeface);
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        questionText.setText(s_question.getQuestionText());
        qTablelayout = (TableLayout) findViewById(R.id.qDynamicTableLayout);
        getCustomers();
        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        if (surveyAnswer_attend_pos != -1) {
            // if (Survey.surveyAnswers.size() > Survey.q_id) {
            ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
            String ans = ts_answer.getSurveyAnswer();
            String[] answers = ans.split(",");
            for (int i = 0; i < answers.length; i++) {
                setupRow(answers[i]);
            }
        } else {
            setupRow("");
        }
    }

    protected String reformat(String inputString) {

        if (inputString.contains(",")) {
            inputString = inputString.replace(",", " ");
        }
        return inputString;

    }

    protected void getCustomers() {
        String cats[] = s_question.getQuestionOptions();

        if (cats.length > 0) {
            // Log.d("TAD", "CATEGORY HJ--------->");
            /*
             * SQLiteAdapter Adapter = new SQLiteAdapter(
             * getApplicationContext()); Adapter.openToRead(); //String[]
             * search_ids = new String[] { "search_ids"}; Cursor Scursor =
             * Adapter.queueAll("survey_multitextoptions", new String[] {
             * "search_ids"}, null, "question_id="+s_question.getQuestionId());
             */
            SQLiteAdapter Adapter = new SQLiteAdapter(getApplicationContext());
            Adapter.openToRead();
            String search_id = Adapter.getSearchID(String.valueOf(s_question
                    .getQuestionId()));
            Adapter.close();//rijo added
            String whereQuery = "";
            if (search_id.equals("3,4") || search_id.equals("4,3")) {
                //String whereQuery = "";
                //Log.d("TAD", "CATEGORY Dealer Or Sub dealer--------->" + search_id);

                for (int i = 0; i < cats.length; i++) {
                    whereQuery = " customer_category in(3, 4)";
                }
                //whereQuery = whereQuery.substring(0, whereQuery.lastIndexOf("OR "));
            } else if (search_id.equals("3")) {
                //Log.d("TAD", "CATEGORY Dealer--------->" + search_id);

                //String whereQuery = "";
                for (int i = 0; i < cats.length; i++) {
                    whereQuery = " customer_category in(3)";
                }
                //whereQuery = whereQuery.substring(0, whereQuery.lastIndexOf("OR "));
            } else if (search_id.equals("4")) {
                //Log.d("TAD", "CATEGORY Sub dealer--------->" + search_id);

                //String whereQuery = "";
                for (int i = 0; i < cats.length; i++) {
                    whereQuery = " customer_category in(4)";
                }
                //whereQuery = whereQuery.substring(0, whereQuery.lastIndexOf("OR "));
            }

            try {
                SQLiteAdapter mySQlAdapter = new SQLiteAdapter(
                        getApplicationContext());
                mySQlAdapter.openToRead();
                String[] columns = new String[]{"customer_shopname",
                        "cust_id"};
                Cursor cursor = mySQlAdapter.queueAll("survey_customerdetails", columns,
                        "customer_shopname asc", whereQuery);
                CUSTOMERS = new String[cursor.getCount()];
                IDS = new String[cursor.getCount()];
                cursor.moveToPosition(0);
                int j = 0;
                while (cursor.isAfterLast() == false) {

                    CUSTOMERS[j] = cursor.getString(0) + ";"
                            + cursor.getString(1);
                    // IDS[j]=cursor.getString(1);
					/*Log.d("", "Dealers------->"+cursor.getString(0));
					//Log.v("shopname", cursor.getString(0));
					//Log.v("id", cursor.getString(1));*/
                    cursor.moveToNext();
                    j++;
                }
                cursor.close();
                mySQlAdapter.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    protected void setupRow(String text) {
		/*//System.out.println("S count------->" + s_question.getCount());
		//System.out.println("mCount------->" + mCount);*/

        if (mCount < s_question.getCount()) {
            mCount++;
            for (int i = 0; i < qTablelayout.getChildCount(); i++) {
                TableRow trow = (TableRow) qTablelayout.getChildAt(i);
                ImageButton imgBt = (ImageButton) trow.getChildAt(trow
                        .getChildCount() - 1);
                imgBt.setImageResource(R.drawable.delete);
                imgBt.setBackgroundColor(Color.WHITE);
                imgBt.setTag(i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.suggest_item, CUSTOMERS);
            TableRow tr = new TableRow(this);
            AutoCompleteTextView auto = new AutoCompleteTextView(this);
            auto.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 6));
            auto.setSingleLine(true);
            auto.setThreshold(1);
            auto.setPadding(15, 10, 0, 0);
            auto.setTextColor(Color.BLACK);
            auto.setAdapter(adapter);
            auto.setText(text);
            // auto.setBackground(getResources().getDrawable(R.drawable.edittext_border));
            auto.setOnEditorActionListener(new OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    // TODO Auto-generated method stub
                    //	//Log.v("", "change Clicked" + v.getText().toString());
                    if (!checkValidText(v.getText().toString())) {
                        v.setText("");
                    }
                    return false;
                }
            });
            auto.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // TODO Auto-generated method stub
                    AutoCompleteTextView txt = (AutoCompleteTextView) v;
					/*//Log.v("", "Focus changed String:"
							+ txt.getText().toString());*/
                    if (!checkValidText(txt.getText().toString())) {
                        txt.setText("");
                    }
                }
            });

            auto.setImeOptions(EditorInfo.IME_ACTION_DONE);

            ImageButton imageButt = new ImageButton(this);
            imageButt.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
            imageButt.setImageResource(R.drawable.delete);
            imageButt.setBackgroundColor(Color.WHITE);
            imageButt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TableRow delrow = (TableRow) v.getParent();
                    delrow.setVisibility(View.GONE);
                    mCount--;
                }
            });

            tr.addView(auto);
            tr.addView(imageButt);
            qTablelayout.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        Survey.q_id = q_id;
        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        super.onResume();
        AudioRecorder.stopTimer();
    }

    protected boolean checkValidText(String text) {
        boolean match = false;
        for (int k = 0; k < CUSTOMERS.length; k++) {
            if (CUSTOMERS[k].equalsIgnoreCase(text)) {
                match = true;
            }
        }
        return match;
    }

    protected int getNameArryIndex(String text) {
        int match = -1;
        for (int k = 0; k < CUSTOMERS.length; k++) {
            if (CUSTOMERS[k].equalsIgnoreCase(text)) {
                match = k;
            }
        }
        return match;
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT,
                AutoComplete.this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Survey.q_id = q_id;
        super.onStart();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
