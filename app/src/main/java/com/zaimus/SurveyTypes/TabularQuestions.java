package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.zaimus.LoopQuestion;
import com.zaimus.MyApplication;
import com.zaimus.R;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.SurveyUpdateActivity;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.manager.AppPreferenceManager;
import com.zaimus.manager.Headermanager;
import com.zaimus.manager.QuestionFlowManager;
import com.zaimus.manager.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TabularQuestions extends Activity {
    TextView ques;
    private int q_id;
    private SurveyAnswers s_answer;
    private SurveyQuestions s_question;
    ImageView buttonBack, buttonNext, removeOptionsButton;
    private int surveyAnswer_attend_pos;
    SQLiteAdapter sqLiteAdapter;
    String q;
    String master_id, op_id1, op_id2, op_id3;
    String[] opList;
    ArrayList<String> opArray = new ArrayList<String>();

    ArrayList<String> selectedList = new ArrayList<String>();
    static String[][] selectedListArray = new String[40][40];
    static ArrayList<String> subOpArray = new ArrayList<String>();
    String[] newArray;
    Survey survey;
    // GridView subLayout;
    // GridView optionsList;
    // GridView suboptionslist1, suboptionlist2, suboptionlist3;
    Context context = this;
    TableLayout gridLayout;
    // DisplayMetrics displaymetrics;
    // int height, width;
    String s_id;
    Headermanager headermanager;
    LinearLayout header;
    ImageView splitIcon, tickImg, addOption;
    Activity activity = this;
    Typeface typeface;
    TextView qDecisionTextView, txtqDecisionBack, txtqDecisionNext;
    String item;
    View subitem;
    GridView optionList, optionList1;
    GridViewAdapter gridAdapter;
    GridViewAdapterWO gridAdapter1;
    int count = 0;
    Dialog dialog;
    ImageView btnClosePopup, btnAddPopup;
    TextView urOption;
    String choice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        typeface = Utils.setFontTypeToArial(context);

        setContentView(R.layout.q_tabularquestion);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TabularQuestions.this.finish();

            }
        });
        ques = (TextView) findViewById(R.id.text);
        ques.setTypeface(typeface);
        // opList=new String[]{};
        buttonBack = (ImageView) findViewById(R.id.bt_txtip_back);
        buttonNext = (ImageView) findViewById(R.id.bt_txtip_next);
        // optionsList = (GridView) findViewById(R.id.optionList);
        // subLayout = (GridView) findViewById(R.id.qListlayout);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
        addOption = (ImageView) findViewById(R.id.AddOptionsButton);
        removeOptionsButton = (ImageView) findViewById(R.id.removeOptionsButton);
        gridLayout = (TableLayout) findViewById(R.id.gridLayout);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        // displaymetrics = new DisplayMetrics();
        selectedListArray = new String[40][40];
        // selectedList.clear();
        count = 0;
        survey = new Survey();
        // getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // height = displaymetrics.heightPixels;
        // width = displaymetrics.widthPixels;
        // suboptionslist1 = (ListView) findViewById(R.id.listview1);
        // suboptionlist2 = (ListView) findViewById(R.id.listview2);
        // suboptionlist3 = (ListView) findViewById(R.id.listview3);
        sqLiteAdapter = new SQLiteAdapter(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }
        s_question = Survey.surveyQuestions.get(Survey.q_id);
        q = String.valueOf(s_question.getQuestionId());
        sqLiteAdapter.openToRead();//rijo added
        master_id = sqLiteAdapter.getMasterId(q);
//		sqLiteAdapter.close();//rijo added

        surveyAnswer_attend_pos = QuestionFlowManager
                .getposQuestioninAnswerArray(s_question.getQuestionId());
        ques.setText(s_question.getQuestionText());

        getDataFromDb("");
        buttonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        removeOptionsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                removeAddView();
            }
        });
        /*
         * subLayout.setOnItemClickListener(new OnItemClickListener() {
         *
         * @Override public void onItemClick(AdapterView<?> parent, View view,
         * int position, long id) { // TODO Auto-generated method stub item =
         * subLayout.getItemAtPosition(position).toString();
         *
         * if(item.equals(AppPreferenceManager.getPreItem(context))){
         * //System.out.println("equlllllll"); //subitem =
         * subLayout.getChildAt(position);
         *
         * //subitem.setBackgroundColor(Color.WHITE); subLayout.clearChoices();
         * } int checked = subLayout.getSelectedItemPosition();
         *
         *
         * if (!item.equals("")) { selectedList.add(item); Log.d("TAG",
         * "Item-------------->" + item + "--------" + position +
         * "Checked -----------" + checked);
         * AppPreferenceManager.savePreItem(item, context); subitem =
         * subLayout.getChildAt(position);
         * subitem.setBackgroundColor(Color.rgb(144, 238, 144)); } else {
         * subitem = subLayout.getChildAt(position);
         *
         * subitem.setBackgroundColor(Color.WHITE);
         *
         * } } });
         */
        addOption.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectedList = new ArrayList<String>();
                //System.out.println("gridLayout count::"+((gridLayout.getChildCount())));
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < opArray.size(); j++) {
                        if (selectedListArray[i][j] != null) {
                            //System.out.println("selectedListArray::"+selectedListArray[i][j]);

                            if (!selectedListArray[i][j].equals("")
                                    && !selectedListArray[i][j]
                                    .equalsIgnoreCase("Select") && !selectedListArray[i][j]
                                    .equalsIgnoreCase("Others")) {
                                selectedList.add(selectedListArray[i][j]);
                            }
                        }
                    }
                }
                if (selectedList.size() == 0
                        || selectedList.size() < (opArray.size() * count)) {
                    alertbox("Error", "Please fill all the data to add new row");
                } else {
                    getDataFromDb2();

                }
            }
        });
        buttonNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                selectedList = new ArrayList<String>();
                //System.out.println("gridLayout count::"+gridLayout.getChildCount());
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < opArray.size(); j++) {

                        if (selectedListArray[i][j] != null) {

                            if (!selectedListArray[i][j].equals("")
                                    && !selectedListArray[i][j]
                                    .equalsIgnoreCase("Select") && !selectedListArray[i][j]
                                    .equalsIgnoreCase("Others")) {

                                selectedList.add(selectedListArray[i][j]);
                            }
                        }
                    }
                }
                if (selectedList.size() == 0
                        || selectedList.size() < (opArray.size() * count)) {
                    alertbox("Error", "Please fill all the data to continue");
                } else {
                    LoopQuestion loopQuestion = new LoopQuestion(
                            TabularQuestions.this);
                    Survey.q_id = loopQuestion
                            .findNextQuestionidPositionFromSelectedQuestionid(s_question
                                    .getQuestionId());
                    loopQuestion.closeDB();

                    if (surveyAnswer_attend_pos != -1) {
                        // Survey.surveyAnswers.get(surveyAnswer_attend_pos).setBitmapPicture(bmp);
                        //System.out.print("next btn" + "-> ");
                    } else {
                        String device_id = Utils.getDeviceId(context);
                        String[][] rdata = {
                                {"customer_id", Survey.customer_id},
                                {"surveyset_id", "" + Survey.surveyset_id},
                                {"device_id", "" + device_id}};

                        //Log.v("Survey.customer_id", Survey.customer_id);
                        sqLiteAdapter.openToWrite();
                        Timestamp currentTimeString = new Timestamp(new Date()
                                .getTime());
                        sqLiteAdapter.insert(rdata, "survey_result_master");
                        sqLiteAdapter.close();

                        sqLiteAdapter.openToRead();
                        String[] columns = new String[]{"survey_id"};
                        Cursor cursor = sqLiteAdapter.queueAll(
                                "survey_result_master", columns,
                                "survey_id desc limit 1", null);

                        cursor.moveToPosition(0);
                        while (cursor.isAfterLast() == false) {
                            s_id = cursor.getString(0);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        sqLiteAdapter.close();//rijo newly added
                        sqLiteAdapter.openToWrite();
                        for (int i = 0; i < count; i++) {
                            for (int j = 0; j < opArray.size(); j++) {


                                survey.survey_questionId = String
                                        .valueOf(Survey.q_id);
                                survey.customer_id = String
                                        .valueOf(Survey.customer_id);
                                survey.survey_questionId = String
                                        .valueOf(s_question.getQuestionId());
                                survey.survey_qn_type = s_question
                                        .getQuestType();
                                survey.survey_id = s_id;
                                survey.deviceid = device_id;
                                survey.survey_question = s_question
                                        .getQuestionText();
                                survey.survey_time = currentTimeString
                                        .toString();
                                if (sqLiteAdapter
                                        .getTabularId(selectedListArray[i][j]) != null) {

                                    survey.tabular_suboption_id = sqLiteAdapter
                                            .getTabularSubOpIdFromQuestion(opArray
                                                    .get(j));
                                    /*
                                     * survey.tabular_option_id = sqLiteAdapter
                                     * .getTabularId(selectedList.get(i));
                                     */
                                    survey.tabular_option_value = opArray
                                            .get(j);

                                } else {
                                    // survey.tabular_suboption_id = "0";
                                    // survey.tabular_option_id = "0";
                                    // survey.tabular_option_value = "Others";
                                    survey.tabular_suboption_id = sqLiteAdapter
                                            .getTabularSubOpIdFromQuestion(opArray
                                                    .get(j));
                                    survey.tabular_option_id = "0";

                                    survey.tabular_option_value = opArray
                                            .get(j);//+ ":Others";

                                }
                                survey.tabular_suboption_value = selectedListArray[i][j];
                                survey.tabular_option_id = sqLiteAdapter
                                        .getTabularIdUsingSubOption(
                                                selectedListArray[i][j],
                                                survey.tabular_suboption_id);
                                survey.survey_no = MyApplication.randomNo;

                                // sqLiteAdapter.openToRead();
                                /*
                                 * survey.tabular_suboption_id = sqLiteAdapter
                                 * .getTabularSubOpId(selectedList.get(i));
                                 * survey.tabular_option_id = sqLiteAdapter
                                 * .getTabularId(selectedList.get(i));
                                 * survey.tabular_option_value = sqLiteAdapter
                                 * .getTabularOp(survey.tabular_suboption_id);
                                 */
                                // }
                                sqLiteAdapter.addTabularSurVeyResults(survey);
                                sqLiteAdapter.close();//rijo commented
                            }
                        }

                        /*
                         * for(int i=0;i<selectedList.size();i++){
                         * //System.out.print("next btn" + "->  Else--------->");
                         * String currentTimeString = new SimpleDateFormat(
                         * "yyyy-mm-dd HH:mm:ss").format(new Date()); s_answer =
                         * new SurveyAnswers(s_question .getQuestionId(),
                         * s_question .getQuestionText(), null,
                         * currentTimeString.toString(), 0, s_question
                         * .getQuestType(), s_question .getQuestionOptions(),
                         * null, null, null,Survey.q_id);
                         * Survey.surveyAnswers.add(s_answer); }
                         */
                    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // bmp = (Bitmap) extras.get("data");

            if (surveyAnswer_attend_pos != -1) {
                // Survey.surveyAnswers.get(surveyAnswer_attend_pos).setBitmapPicture(bmp);
            } else {

                Timestamp currentTimeString = new Timestamp(
                        new Date().getTime());
                s_answer = new SurveyAnswers(s_question.getQuestionId(),
                        s_question.getQuestionText(), "",
                        currentTimeString.toString(), 0,
                        s_question.getQuestType(), null, null, null, null,
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

    public void getDataFromDb(String text) {
        // //System.out.println("Count------->"+count);
        count = count + 1;
        sqLiteAdapter.openToRead();
        opArray = sqLiteAdapter.getAllTabularOptions(master_id);
        sqLiteAdapter.close();//rijo commented
        optionList = new GridView(TabularQuestions.this);
        gridAdapter = new GridViewAdapter(context, opArray,
                String.valueOf(q_id));
        optionList.setNumColumns(opArray.size());
        gridAdapter.notifyDataSetChanged();
        optionList.setAdapter(gridAdapter);
        // tr.addView(optionList);
        gridLayout.addView(optionList);


    }

    public void getDataFromDb2() {
        // //System.out.println("Count------->"+count);
        removeOptionsButton.setVisibility(View.VISIBLE);
        count = count + 1;
        sqLiteAdapter.openToRead();
        opArray = sqLiteAdapter.getAllTabularOptions(master_id);
        sqLiteAdapter.close();//rijo commented
        optionList1 = new GridView(TabularQuestions.this);
        gridAdapter1 = new GridViewAdapterWO(context, opArray,
                String.valueOf(q_id));
        optionList1.setNumColumns(opArray.size());
        gridAdapter1.notifyDataSetChanged();
        optionList1.setAdapter(gridAdapter1);
        // tr.addView(optionList);
        gridLayout.addView(optionList1);

//		gridLayout.invalidate();
//		gridLayout.refreshDrawableState();
    }

    public void removeAddView() {
        //System.out.println("Count--remove----->"+(gridLayout.getChildCount()-1));
        count = count - 1;
        // sqLiteAdapter.openToRead();
        // opArray = sqLiteAdapter.getAllTabularOptions(master_id);
        // sqLiteAdapter.close();
        // optionList1 = new GridView(TabularQuestions.this);
        // gridAdapter1 = new GridViewAdapterWO(context,
        // opArray,String.valueOf(q_id));
        // optionList1.setNumColumns(opArray.size());
        // optionList1.setAdapter(gridAdapter1);

//		gridLayout.removeViewAt(count);

        int childCount = gridLayout.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            gridLayout.removeViewAt(childCount - 1);
        }

        if (count == 1) {
            removeOptionsButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        //System.out.println("onPause called");
        // selectedList.clear();
        // count=0;
        super.onPause();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Survey.q_id = q_id;
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
//		Survey.q_id = q_id;//rijo commented
//		surveyAnswer_attend_pos = QuestionFlowManager
//				.getposQuestioninAnswerArray(s_question.getQuestionId());//rijo commented
        super.onResume();
        // AudioRecorder.stopTimer();
    }

    public class GridViewAdapter extends BaseAdapter {

        SimpleDateFormat sdf, formatter;
        private Context context;
        //		private View mView;
        private LayoutInflater inflater;
        ArrayList<String> mArrayList;
        ArrayList<String> cArrayList;
        ViewHolders holders;

        // EditText optionText;
        Date tempDate;
        String optionId;
        Filter filter;
        String question_id;
        SQLiteAdapter sqLiteAdapters;

        public GridViewAdapter(Context context, ArrayList<String> temparray,
                               String question_id) {
            this.context = context;
            optionId = optionId;
            this.cArrayList = temparray;
            this.question_id = question_id;
            // this.cArrayList.addAll(mArrayList);
            inflater = LayoutInflater.from(context);


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return cArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.q_tabular_grid_item, null);
                holders = new ViewHolders();

                holders.spin = (Spinner) convertView.findViewById(R.id.spin);
                holders.titleTextView = (TextView) convertView.findViewById(R.id.optionTabular);
                convertView.setTag(holders);
                holders.setTag(0);

            } else {
//				mView = convertView;
                holders = (ViewHolders) convertView.getTag();

            }

            holders.spin.getBackground().setColorFilter(
                    getResources().getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP);
            // optionText = (EditText) mView.findViewById(R.id.optionText);
            // optionText.setVisibility(View.GONE);
            holders.titleTextView.setVisibility(View.VISIBLE);
            holders.spin.setVisibility(View.VISIBLE);
            holders.titleTextView.setText(cArrayList.get(position));
            sqLiteAdapters = new SQLiteAdapter(context);

            sqLiteAdapters.openToRead();//rijo added

            optionId = sqLiteAdapters.getQuesId(cArrayList.get(position),
                    master_id);
            /*
             * optionId =
             * sqLiteAdapter.getQuesId(cArrayList.get(position),String
             * .valueOf(Survey.q_id));
             */
            //System.out.println("Option Id");
            AppPreferenceManager.savePreItem(optionId, context);

            subOpArray = sqLiteAdapters.getAllTabularSubOptions(optionId);
            sqLiteAdapters.close();//rijo added

            newArray = new String[subOpArray.size() + 2];
            newArray[0] = "Select";
            // //System.out.println("Sub Option Items " + subOpArray);
            int i = 1;
            for (int j = 0; j < subOpArray.size(); j++, i++) {
                newArray[i] = subOpArray.get(j);
                // //System.out.println("Option Items "+newArray[j]);
            }
            newArray[subOpArray.size() + 1] = "Others";

            /*
             * ArrayAdapter<String> l3Adapter = new
             * ArrayAdapter<String>(context, R.layout.simple_spinner_item,
             * newArray);
             */
            ArrayAdapter<String> l3Adapter = new ArrayAdapter<String>(context,
                    R.layout.simple_spinner_item, newArray);
            // l3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // subLayout.setNumColumns(opArray.size());
            holders.spin.setAdapter(l3Adapter);
            holders.spin.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int mPosition, long id) {
                    // TODO Auto-generated method stub
                    // item = spin.getSelectedItem().toString();
                    // //System.out.println("Item -------->" +
                    // parent.getItemAtPosition(position).toString());

                    if (!parent.getItemAtPosition(mPosition).toString()
                            .equals("")
                            && !parent.getItemAtPosition(mPosition).toString()
                            .equals("Select")
                            && !parent.getItemAtPosition(mPosition).toString()
                            .equalsIgnoreCase("Others")) {

                        selectedListArray[0][position] = parent
                                .getItemAtPosition(mPosition).toString();

//						gridLayout.invalidate();
//						gridLayout.refreshDrawableState();

                        // optionText.setVisibility(View.GONE);
                    } else if (parent.getItemAtPosition(mPosition).toString()
                            .equalsIgnoreCase("Others")) {
                        initiatePopupWindow(0, position);
                        // //System.out.println("hjjkjk"+AppPreferenceManager.getPreItem(context));

                        // optionText.setVisibility(View.VISIBLE);
                        // spin.setVisibility(View.GONE);
                        // selectedList.add(optionText.getText().toString());
                    } else if (parent.getItemAtPosition(mPosition).toString()
                            .equals("Select")) {
                        if (position != -1) {
                            selectedListArray[0][position] = "Select";
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

            return convertView;
        }

    }

    // add button click
    public class GridViewAdapterWO extends BaseAdapter {

        SimpleDateFormat sdf, formatter;
        private Context context;
        //		private View mView;
        private LayoutInflater inflater;
        ArrayList<String> mArrayList;
        ArrayList<String> cArrayList;

        TextView titleTextView;
        // EditText optionText1;
        Date tempDate;
        String optionId;
        Filter filter;
        String question_id;
        SQLiteAdapter sqLiteAdapters2;
        ViewHolder holder;

        public GridViewAdapterWO(Context context, ArrayList<String> temparray,
                                 String question_id) {
            this.context = context;
            // optionId = optionId;
            this.cArrayList = temparray;
            this.question_id = question_id;
            // this.cArrayList.addAll(mArrayList);

            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return cArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.q_tabularnew, null);
                holder = new ViewHolder();

                holder.spinner = (Spinner) convertView.findViewById(R.id.spin);
                convertView.setTag(holder);
                holder.setTag(count - 1);
            } else {
//				mView = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.spinner.getBackground().setColorFilter(
                    getResources().getColor(R.color.red),
                    PorterDuff.Mode.SRC_ATOP);
            sqLiteAdapters2 = new SQLiteAdapter(context);

            sqLiteAdapters2.openToRead();//rijo added

            optionId = sqLiteAdapters2.getQuesId(cArrayList.get(position),
                    master_id);

            // optionId =
            // sqLiteAdapter.getQuesId(cArrayList.get(position),String.valueOf(Survey.q_id));

            subOpArray = sqLiteAdapters2.getAllTabularSubOptions(optionId);
            sqLiteAdapters2.close();//rijo added

            newArray = new String[subOpArray.size() + 2];
            newArray[0] = "Select";

            int i = 1;
            for (int j = 0; j < subOpArray.size(); j++, i++) {
                newArray[i] = subOpArray.get(j);
                //System.out.println(newArray[i]);

            }
            newArray[subOpArray.size() + 1] = "Others";

            ArrayAdapter<String> l3Adapter = new ArrayAdapter<String>(context,
                    R.layout.simple_spinner_item, newArray);
            // l3Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            holder.spinner.setAdapter(l3Adapter);

            holder.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int mPosition, long id) {
                    // TODO Auto-generated method stub
                    // item = spinner.getItemAtPosition(position).toString();
                    //System.out.println("gridLayoutPositionTag --"+holder.getTag());
                    if (!parent.getItemAtPosition(mPosition).toString()
                            .equals("")
                            && !parent.getItemAtPosition(mPosition).toString()
                            .equals("Select")
                            && !parent.getItemAtPosition(mPosition).toString()
                            .equalsIgnoreCase("Others")) {
                        selectedListArray[holder.getTag()][position] = parent
                                .getItemAtPosition(mPosition).toString();

//						gridLayout.invalidate();
//						gridLayout.refreshDrawableState();

                        // optionText.setVisibility(View.GONE);
                    } else if (parent.getItemAtPosition(mPosition).toString()
                            .equalsIgnoreCase("Others")) {

                        initiatePopupWindow(holder.getTag(), position);

                    } else if (parent.getItemAtPosition(mPosition).toString()
                            .equals("Select")) {
                        if (position != -1) {
                            selectedListArray[holder.getTag()][position] = "Select";

                        }
                    }
//				


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

            return convertView;
        }

    }

    private void initiatePopupWindow(final int rowPosition,
                                     final int gridPosition) {
        try {
            // We need to get the instance of the LayoutInflater
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.screen_popup);
            btnClosePopup = (ImageView) dialog
                    .findViewById(R.id.btn_close_popup);
            btnAddPopup = (ImageView) dialog.findViewById(R.id.btn_add_popup);
            urOption = (TextView) dialog.findViewById(R.id.txtView);

            if (selectedListArray[rowPosition][gridPosition] != null) {
                if (selectedListArray[rowPosition][gridPosition].startsWith("Others:") || selectedListArray[rowPosition][gridPosition].equalsIgnoreCase("Others")) {
                    urOption.setText(selectedListArray[rowPosition][gridPosition].replaceAll("Others:", ""));
                } else {
                    urOption.setText("");
                }

            }

            btnClosePopup.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					gridLayout.invalidate();
//					gridLayout.refreshDrawableState();
                    dialog.dismiss();

                }
            });

            btnAddPopup.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (urOption.getText().toString().equals("")) {
                        selectedListArray[rowPosition][gridPosition] = "Others";
                    } else {
                        choice = urOption.getText().toString();
                        selectedListArray[rowPosition][gridPosition] = "Others:" + choice;
//						gridLayout.invalidate();
//						gridLayout.refreshDrawableState();

                    }
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder {
        Spinner spinner;
        int position1 = 0;

        void setTag(int position12) {
            this.position1 = position12;
        }

        int getTag() {
            return position1;
        }

    }

    class ViewHolders {
        Spinner spin;
        TextView titleTextView;
        int positionRow = 0;

        void setTag(int positionRow1) {
            this.positionRow = positionRow1;
        }

        int getTag() {
            return positionRow;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
