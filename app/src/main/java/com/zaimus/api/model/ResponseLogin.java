package com.zaimus.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("survey_id")
    @Expose
    private String surveyId;
    @SerializedName("question_id")
    @Expose
    private String questionId;
    @SerializedName("question_type")
    @Expose
    private String questionType;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("suboption_text")
    @Expose
    private String suboptionText;
    @SerializedName("time_stamp")
    @Expose
    private String timeStamp;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("survey_set_id")
    @Expose
    private String surveySetId;
    @SerializedName("tabular_option_id")
    @Expose
    private Object tabularOptionId;
    @SerializedName("tabular_option_value")
    @Expose
    private Object tabularOptionValue;
    @SerializedName("tabular_suboption_id")
    @Expose
    private Object tabularSuboptionId;
    @SerializedName("tabular_suboption_value")
    @Expose
    private Object tabularSuboptionValue;
    @SerializedName("storeimage")
    @Expose
    private Object storeimage;
    @SerializedName("storeimagename")
    @Expose
    private Object storeimagename;
    @SerializedName("survey_no")
    @Expose
    private String surveyNo;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("planDetailId")
    @Expose
    private String planDetailId;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSuboptionText() {
        return suboptionText;
    }

    public void setSuboptionText(String suboptionText) {
        this.suboptionText = suboptionText;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public String getSurveySetId() {
        return surveySetId;
    }

    public void setSurveySetId(String surveySetId) {
        this.surveySetId = surveySetId;
    }

    public Object getTabularOptionId() {
        return tabularOptionId;
    }

    public void setTabularOptionId(Object tabularOptionId) {
        this.tabularOptionId = tabularOptionId;
    }

    public Object getTabularOptionValue() {
        return tabularOptionValue;
    }

    public void setTabularOptionValue(Object tabularOptionValue) {
        this.tabularOptionValue = tabularOptionValue;
    }

    public Object getTabularSuboptionId() {
        return tabularSuboptionId;
    }

    public void setTabularSuboptionId(Object tabularSuboptionId) {
        this.tabularSuboptionId = tabularSuboptionId;
    }

    public Object getTabularSuboptionValue() {
        return tabularSuboptionValue;
    }

    public void setTabularSuboptionValue(Object tabularSuboptionValue) {
        this.tabularSuboptionValue = tabularSuboptionValue;
    }

    public Object getStoreimage() {
        return storeimage;
    }

    public void setStoreimage(Object storeimage) {
        this.storeimage = storeimage;
    }

    public Object getStoreimagename() {
        return storeimagename;
    }

    public void setStoreimagename(Object storeimagename) {
        this.storeimagename = storeimagename;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanDetailId() {
        return planDetailId;
    }

    public void setPlanDetailId(String planDetailId) {
        this.planDetailId = planDetailId;
    }
}
