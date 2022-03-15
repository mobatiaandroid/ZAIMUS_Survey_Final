package com.zaimus.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchUserResponseModel {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("customers")
    @Expose
    private ArrayList<CustomerData> customers = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CustomerData> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<CustomerData> customers) {
        this.customers = customers;
    }

}
