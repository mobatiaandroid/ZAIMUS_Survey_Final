package com.zaimus.SurveyTypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zaimus.LoopQuestion;
import com.zaimus.R;
import com.zaimus.Survey.Rank;
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
import java.util.ArrayList;
import java.util.Date;


public class Order extends Activity {

    private ListView rankList;
    private TextView scrollMsg;
    private SurveyQuestions s_question;
    private SurveyAnswers s_answer;
    private String[] r_options;
    private int[] r_options_ids;
    private int q_id;
    private TextView questionText;
    private RankAdapter radap;
    private ImageView nextButton;
    private ImageView backButton;
    private SurveyAnswers ts_answer;
    ArrayList<Rank> ranks;
    private int surveyAnswer_attend_pos;
    public static int OVER_RANK;
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            q_id = extras.getInt("qid");
            Survey.q_id = q_id;
        }

        s_question = Survey.surveyQuestions.get(Survey.q_id);
        r_options = s_question.getQuestionOptions();
        r_options_ids = s_question.getOptionsId();
        setContentView(R.layout.q_ranking);
        header = (LinearLayout) findViewById(R.id.header);
        headermanager = new Headermanager(activity, "");
        headermanager.getHeader(header, 1, false);
        headermanager.setButtonLeftSelector(R.drawable.back, R.drawable.back);
        splitIcon = headermanager.getLeftButton();
        splitIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Order.this.finish();

            }
        });
        rankList = (ListView) findViewById(R.id.qRankingListView);
        questionText = (TextView) findViewById(R.id.qRankingTextView);
        scrollMsg = (TextView) findViewById(R.id.txt_rank_scrollmsg);
        questionText.setText(s_question.getQuestionText());
        questionText.setTypeface(typeface);
        nextButton = (ImageView) findViewById(R.id.qRankingNext);
        backButton = (ImageView) findViewById(R.id.qRankingBack);
        txtqDecisionBack = (TextView) findViewById(R.id.txtqDecisionBack);
        txtqDecisionNext = (TextView) findViewById(R.id.txtqDecisionNext);
        txtqDecisionBack.setTypeface(typeface);
        txtqDecisionNext.setTypeface(typeface);
        Rank rs = new Rank("", 0, -8);
        rs.setOverrank(0);
        ranks = new ArrayList<Rank>();
        ////System.out.println("RRRRRRRRRRRRRRRRRRRRRR===="+r_options[0]);

        surveyAnswer_attend_pos = QuestionFlowManager.getposQuestioninAnswerArray(s_question.getQuestionId());
        //System.out.println("survey=="+surveyAnswer_attend_pos);
        if (surveyAnswer_attend_pos != -1) {
            //if (Survey.surveyAnswers.size() > Survey.q_id) {
            ts_answer = Survey.surveyAnswers.get(surveyAnswer_attend_pos);
            String[] ts_answer_ops = ts_answer.getSubAnswers();
            ranks = ts_answer.getAttended_ranks();
            Rank.setOverrank(OVER_RANK);
			/*for (int i = 0; i < r_options.length; i++) {
				Rank r = new Rank(r_options[i],
						Integer.parseInt(ts_answer_ops[i]));
				//r.setOverrank(r_options.length);
				r.setOverrank(OVER_RANK);
				ranks.add(r);
			}*/
        } else {
            for (int i = 0; i < r_options.length; i++) {
                Rank r = new Rank(r_options[i], 0, r_options_ids[i]);
                ranks.add(r);
            }
        }
        radap = new RankAdapter(getApplicationContext(), R.layout.q_ranking,
                ranks);
        rankList.setAdapter(radap);

        // setup scroll msg

        rankList.post(new Runnable() {
            public void run() {
                if (rankList.getCount() > (rankList.getLastVisiblePosition()
                        - rankList.getFirstVisiblePosition() + 1)) {
                    scrollMsg.setVisibility(View.VISIBLE);
                } else {
                    scrollMsg.setVisibility(View.GONE);
                }
            }
        });

        rankList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Rank item = radap.getItem(position);
                TextView txtOp = (TextView) view.findViewById(R.id.rank_item);
                if (item.getRank() == 0) {
                    Rank.incrOverRank();
                    item.setRank(Rank.getOverRank());
                    txtOp.setText(item.getRank() + " " + item.getQuestion());
                    view.setBackgroundColor(Color.rgb(144, 238, 144));
                } else {
                    Rank.decrOverRank();
                    for (int i = 0; i < radap.getCount(); i++) {
                        if (item.getRank() < radap.getItem(i).getRank()) {
                            radap.getItem(i).setRank(
                                    radap.getItem(i).getRank() - 1);
                            TextView temptxt = (TextView) parent.getChildAt(i)
                                    .findViewById(R.id.rank_item);
                            temptxt.setText(radap.getItem(i).getRank() + " "
                                    + radap.getItem(i).getQuestion());
                        }
                    }
                    item.setRank(0);
                    txtOp.setText(item.getQuestion());
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
				/*for (int i = 0; i < radap.getCount(); i++) {
					//Log.v("" + radap.getItem(i).getQuestion(), ""
							+ radap.getItem(i).getRank());
				}*/
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Rank tr = ranks.get(0);
                String[] answerOp = new String[findSizeofRankedItems()];
                int answer_index = 0;
                int[] ranked_options = new int[findSizeofRankedItems()];
                for (int i = 0; i < ranks.size(); i++) {
                    if (ranks.get(i).getRank() != 0) {
                        answerOp[answer_index] = "" + ranks.get(i).getRank();
                        ranked_options[answer_index] = ranks.get(i).getQue_option_id();
                        answer_index++;
                    }

                }
                //if (tr.getOverRank() == ranks.size()) {
                if (tr.getOverRank() > 0) {
                    if (surveyAnswer_attend_pos != -1) {
                        //if (Survey.surveyAnswers.size() > Survey.q_id) {
                        Survey.surveyAnswers.get(surveyAnswer_attend_pos).setSubAnswers(
                                answerOp);
                        Survey.surveyAnswers.get(surveyAnswer_attend_pos).setAttended_ranks(ranks);
                        Survey.surveyAnswers.get(surveyAnswer_attend_pos).setRanked_options(ranked_options);
                    } else {
                        Timestamp currentTimeString = new Timestamp(new Date().getTime());

                        s_answer = new SurveyAnswers(
                                s_question.getQuestionId(), s_question
                                .getQuestionText(), null,
                                currentTimeString.toString(), 0,
                                s_question.getQuestType(), s_question
                                .getQuestionOptions(), answerOp, null, null, Survey.q_id);
                        s_answer.setAttended_ranks(ranks);
                        s_answer.setRanked_options(ranked_options);
                        Survey.surveyAnswers.add(s_answer);
                    }

                    for (int i = 0; i < answerOp.length; i++) {
                        //System.out.println("Answer options"+answerOp[i]);
                    }
                    //Log.v("Q_ID", "" + Survey.q_id);
                    for (int j = 0; j < Survey.surveyAnswers.size(); j++) {
                        //Log.e("answer ",""+Survey.surveyAnswers.get(j).getSurveyAnswer());
                    }

                    //Survey.q_id++;
                    /////////////////NEXT QUESTION //////////////
                    LoopQuestion loopQuestion = new LoopQuestion(Order.this);
                    Survey.q_id = loopQuestion.findNextQuestionidPositionFromSelectedQuestionid(s_question.getQuestionId());
                    loopQuestion.closeDB();
                    OVER_RANK = tr.getOverRank();
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
                    alertbox("Error", "Please select atleast one!.");
                }
            }
        });

        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Rank.setOverrank(findlargestOverrank());
                OVER_RANK = findlargestOverrank();
                finish();
            }
        });
    }

    private class RankAdapter extends ArrayAdapter<Rank> {

        private Context context;
        private ArrayList<Rank> ranks;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.ranklist, null);
            }
            Rank rank = ranks.get(position);
            if (rank != null) {
                TextView rankName = (TextView) v.findViewById(R.id.rank_item);
                if (rankName != null) {
                    if (rank.getRank() > 0) {
                        rankName.setText(rank.getRank() + " "
                                + rank.getQuestion());
                        View item = (View) rankName.getParent();
                        item.setBackgroundColor(Color.rgb(250, 150, 42));
                    } else {
                        rankName.setText(rank.getQuestion());
                        View item = (View) rankName.getParent();
                        item.setBackgroundColor(Color.TRANSPARENT);
                    }

                }
            }
            return v;
        }

        public RankAdapter(Context context, int textViewResourceId,
                           ArrayList<Rank> ranks) {
            super(context, textViewResourceId, ranks);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.ranks = ranks;
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
        AudioRecorder.setupLongTimeout(GlobalConstants.AUDIO_TIMEOUT, Order.this);
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        //Rank.setOverrank(findlargestOverrank());
        OVER_RANK = findlargestOverrank();
        finish();
    }

    private int findlargestOverrank() {
        Rank rank;
        int largest = 0;
        for (int i = 0; i < ranks.size(); i++) {
            rank = ranks.get(i);
            if (rank.getRank() > largest)
                largest = rank.getRank();
        }
        return largest;
    }

    private int findSizeofRankedItems() {
        int size = 0;
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i).getRank() != 0) {
                size++;
            }

        }

        return size;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
