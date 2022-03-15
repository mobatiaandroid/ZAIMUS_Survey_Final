package com.zaimus.Survey;

import java.util.ArrayList;

public class Survey {
	public static int q_id = -1;
	public static int surveyset_id = 0;
	public static ArrayList<SurveyQuestions> surveyQuestions;
	public static ArrayList<SurveyAnswers> surveyAnswers;
	public static ArrayList<SurveySetModel> surveySets;
	public static String customer_id = "";
	public String cus_id = "";
	public String survey_id;
	public String survey_name;
	public String state_id;
	public String state_name;
	public String survey_questionId;
	public String survey_answer;
	public String surveyTime;
	public String survey_qn_type;
	public String deviceid;
	public String survey_question;
	public String survey_answers;
	public String survey_time;
	public String tabular_suboption_value;
	public String tabular_suboption_id;
	public String tabular_option_id;
	public String tabular_option_value;
	public String status;
	public String userName;
	public String roleId;
	public String userId;
	public String email;
	public String customer_name;
	public String customer_category;
	public String customer_add1;
	public String customer_add2;
	public String customer_add3;
	public String customer_add4;
	public String customer_add5;
	public String customer_phone;
	public String customer_place;
	public String customer_businesstype;
	public String customer_shopname;
	public String customer_phone2;
	public String customer_email;
	public String customer_dist;
	public String created_on;
	public String customer_status;
	public String customer_state;
	public String customer_desgn;
	public String customer_department;
	public String customer_country;
	public String updated_from_phone;
	public String latitude;
	public String longitude;
	public String plan_name;
	public String budget;
	public String plan_id;
	public String plan_date;
	public String plan_from_date;
	public String plan_to_date;
	public String districtName;
	public String detailId;
	public String expenseCliamed;
	public String ovrerallStatus;
	public String is_verified;
	public String isMarkedforDeletion;
	public String survey_no;
public String questionId,imagePath,image_download;
	ArrayList<Survey> planList;
	ArrayList<Survey> dateList;

	public String getSurveyId() {
		//System.out.println("Survey Set ID 123" + surveyset_id);

		return survey_id;
	}

	/**
	 * @param courseId
	 *            the courseId to set
	 */
	public void setSurveyId(String survey_id) {
		this.survey_id = survey_id;
	}

	public void setPlanList(ArrayList<Survey> planList) {
		this.planList = planList;
	}
	
	public void setDistrict(String dist) {
		this.districtName = dist;
	}
	
	public String getDist() {
		////System.out.println("Survey Set ID 123" + surveyset_id);
		//System.out.println("districtName" + districtName);

		return districtName;
	}

	/**
	 * @return the mClassList
	 */
	public ArrayList<Survey> getPlanList() {
		return planList;
	}
	public void setPlanDate(ArrayList<Survey> dateList) {
		this.dateList = dateList;
	}

	/**
	 * @return the mClassList
	 */
	public ArrayList<Survey> getPlanDate() {
		return dateList;
	}
	/*public void setSurveyName(String survey_name) {
		this.survey_name = survey_name;
	}
	public String getSurveyName() {
		//System.out.println("Survey Name" + survey_name);

		return survey_name;
	}
	public void setPlanName(String plan_name) {
		this.plan_name = plan_name;
	}
	public String getPlanName() {
		////System.out.println("Survey Set ID 123" + surveyset_id);
		//System.out.println("Survey Name" + plan_name);

		return plan_name;
	}
	public void setBudget(String budget) {
		this.budget = this.budget;
	}
	public String getBudget() {
		////System.out.println("Survey Set ID 123" + surveyset_id);
		//System.out.println("Survey Name" + budget);

		return budget;
	}*/
}
