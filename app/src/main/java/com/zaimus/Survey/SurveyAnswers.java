package com.zaimus.Survey;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class SurveyAnswers {
	private String survey_question_type;
	private String survey_question;
	private int survey_question_id;
	private String survey_answer;//
	private String survey_timestamp;//
	private String sub_questions[];
	private int sub_questions_id[];
	private String sub_answers[];//
	private int sub_pos[];//
	private int option_pos;//
	private Bitmap survey_bmp;
	private int question_id_position = -1;
	private boolean isattended = false;
	private ArrayList<Rank> attended_ranks;
	private int[] ranked_options;

	public int[] getRanked_options() {
		return ranked_options;
	}

	public void setRanked_options(int[] ranked_options) {
		this.ranked_options = ranked_options;
	}

	public ArrayList<Rank> getAttended_ranks() {
		return attended_ranks;
	}

	public void setAttended_ranks(ArrayList<Rank> attended_ranks) {
		this.attended_ranks = attended_ranks;
	}

	public boolean isIsattended() {
		return isattended;
	}

	public void setIsattended(boolean isattended) {
		this.isattended = isattended;
	}

	public int getQuestion_id_position() {
		return question_id_position;
	}

	public void setQuestion_id_position(int question_id_position) {
		this.question_id_position = question_id_position;
	}

	public void setSurveyAnswer(String survey_answer) {
		this.survey_answer = survey_answer;
	}

	public void setBitmapPicture(Bitmap survey_bmp) {
		this.survey_bmp = survey_bmp;
	}

	public void setQuestionId(int survey_question_id) {
		this.survey_question_id = survey_question_id;
	}

	public void setSuveyTimeStamp(String survey_timestamp) {
		this.survey_timestamp = survey_timestamp;
	}

	public void setSubAnswers(String[] sub_answers) {
		this.sub_answers = sub_answers;
	}

	public void setSubPosition(int[] sub_pos) {
		this.sub_pos = sub_pos;
	}

	public void setOptionPos(int option_pos) {
		this.option_pos = option_pos;
	}

	public String getSurveyQuestion() {
		return survey_question;
	}

	public String getSurveyAnswer() {
		return survey_answer;
	}

	public String getSurveyTimeStamp() {
		return survey_timestamp;
	}

	public int getoptionPos() {
		return option_pos;
	}

	public int getQuestionId() {
		return survey_question_id;
	}

	public String getQuestionType() {
		return survey_question_type;
	}

	public String[] getSubQuestions() {
		return sub_questions;
	}

	public String[] getSubAnswers() {
		return sub_answers;
	}

	public int[] getSubpos() {
		return sub_pos;
	}

	public Bitmap getBitmapPicture() {
		return survey_bmp;
	}

	public SurveyAnswers(int survey_question_id, String survey_question,
			String survey_answer, String survey_timestamp, int option_pos,
			String survey_question_type, String[] sub_questions,
			String[] sub_answers, int[] sub_pos, Bitmap survey_bmp,
			int question_id_position) {
		this.survey_question = survey_question;
		this.survey_answer = survey_answer;
		this.survey_timestamp = survey_timestamp;
		this.option_pos = option_pos;
		this.survey_question_type = survey_question_type;
		this.sub_answers = sub_answers;
		this.sub_questions = sub_questions;
		this.sub_pos = sub_pos;
		this.survey_question_id = survey_question_id;
		this.survey_bmp = survey_bmp;
		this.question_id_position = question_id_position;
		this.isattended = true;
	}

}
