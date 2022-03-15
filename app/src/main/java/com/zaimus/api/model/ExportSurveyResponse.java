package com.zaimus.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExportSurveyResponse {

    @SerializedName("response")
    @Expose
    private List<ResponseLogin> response = null;

    public List<ResponseLogin> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseLogin> response) {
        this.response = response;
    }
}
