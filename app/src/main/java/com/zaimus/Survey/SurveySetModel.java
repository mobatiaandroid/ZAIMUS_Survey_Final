package com.zaimus.Survey;

public class SurveySetModel {
	private int surveySetId;
	private String surveySetName;
	private String zone;
	private String surveyType;

	public void setSurveySetId(int surveySetId) {
		this.surveySetId = surveySetId;
	}

	public int getSurveySetId() {
		return surveySetId;
	}

	public void setSurveySetName(String surveySetName) {
		this.surveySetName = surveySetName;
	}

	public String getSurveySetName() {
		return surveySetName;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getZone() {
		return zone;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	public String getSurveyType() {
		return surveyType;
	}
}
