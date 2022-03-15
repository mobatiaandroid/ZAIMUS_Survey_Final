package com.zaimus.Survey;

public class SurveyQuestions {
	private int question_id;
	private String question_text;
	private String question_type;
	private String[] sub_questions;
	private int[] sub_questions_id;
	private int[] options_id;
	private String[] question_options;

	private int option_link_id = -1;
	private int question_tree_id  = -1 ;
	private String enum_path  ="";
	
	
	
	public int getQuestion_tree_id() {
		return question_tree_id;
	}

	public void setQuestion_tree_id(int question_tree_id) {
		this.question_tree_id = question_tree_id;
	}

	public int getOption_link_id() {
		return option_link_id;
	}

	public void setOption_link_id(int option_link_id) {
		this.option_link_id = option_link_id;
	}

	public String getEnum_path() {
		return enum_path;
	}

	public void setEnum_path(String enum_path) {
		this.enum_path = enum_path;
	}

	public int getQuestionId() {
		return question_id;
	}

	public String getQuestionText() {
		return question_text;
	}

	public int[] getOptionsId() {
		return options_id;
	}
	public int getOptionIdSelected(int pos){
		return options_id[pos];
	}
	

	public int[] getSubQuestionsId() {
		return sub_questions_id;
	}

	public String getQuestType() {
		return question_type;
	}

	public String[] getQuestionOptions() {
////System.out.println("Question Options------->"+question_options[]);
		return question_options;
	}

	public String[] getSubQuestion() {
		return sub_questions;
	}

	public String getOption(int pos) {
		return question_options[pos];
	}
	
	public int getCount(){
		return Integer.parseInt(sub_questions[0]);
	}

	public SurveyQuestions(int question_id, String question_text,
			String question_type, String[] question_options,
			String[] sub_questions, int[] options_id, int[] sub_questions_id) {
		this.question_id = question_id;
		this.question_text = question_text;
		this.question_type = question_type;
		this.question_options = question_options;
		this.sub_questions = sub_questions;
		this.options_id = options_id;
		this.sub_questions_id = sub_questions_id;
	}
}
