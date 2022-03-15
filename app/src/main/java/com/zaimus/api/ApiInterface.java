package com.zaimus.api;

import com.zaimus.api.model.AttendanceResponseModel;
import com.zaimus.api.model.DeviceIDResponseModel;
import com.zaimus.api.model.ExportSurveyResponse;
import com.zaimus.api.model.LoginResponse;
import com.zaimus.api.model.SearchUserResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
        // API's endpoints
    Call<LoginResponse> getLoginResponse(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("save_survey")
    Call<ExportSurveyResponse> getExportSurveyResponse(@Field("survey_result") String jsonString);


    @FormUrlEncoded
    @POST("attendance")
    Call<AttendanceResponseModel> getAttendance(@Field("user_id") String user_id, @Field("type") String type, @Field("attendance_id") String attendance_id, @Field("location") String location, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("user_location") String user_location);

    @FormUrlEncoded
    @POST("deviceid")
    Call<DeviceIDResponseModel> getDeviceID(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("fetchcustomers")
    Call<SearchUserResponseModel> getUserData(@Field("phone") String phone, @Field("name") String name);

}
