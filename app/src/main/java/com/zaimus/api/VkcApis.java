package com.zaimus.api;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.zaimus.Profiles.GpsModel;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Claims;
import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveySetModel;
import com.zaimus.UsrValues.PreferenceManager;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.manager.AppPreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VkcApis implements IVkcApis {

    private static HttpClient httpclient;
    private static HttpPost httppost;
    public static HttpResponse httpResponse;
    private static SQLiteAdapter mySQLiteAdapter;
    public static Context context;
    public static Survey surVey;
    SurveySetModel surveySetModel;
    public static int timeoutConnection = 500000;
    public static int timeoutSocket = 500000;

    // public Survey[] surveyArray;
    public VkcApis(Context context) {
        this.context = context;
        // loadJSONFromAsset();
    }

    public void gpsApi(GpsModel gpsModel) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        //Log.v("", "" + DOMAIN_VKC);
        temp_url.append(API_ACTION_GPSSAVE);
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            nameValuePairs.add(new BasicNameValuePair("user_id", gpsModel
                    .getUser_id()));

            // nameValuePairs.add(new BasicNameValuePair("user_id","88"));
            nameValuePairs.add(new BasicNameValuePair("device_id", gpsModel
                    .getDevice_id()));


            nameValuePairs.add(new BasicNameValuePair("latitude", gpsModel
                    .getLatitude()));

            nameValuePairs.add(new BasicNameValuePair("longitude", gpsModel
                    .getLongitude()));


            nameValuePairs.add(new BasicNameValuePair("location", gpsModel
                    .getLocation()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());


            JSONObject json_Response = new JSONObject(json_Response_Str);

            JSONObject json_response_prase = json_Response
                    .getJSONObject("response");

/*
            DownloadFile download = new DownloadFile();
            download.DownloadFromUrl(json_response_prase.getString("filepath"),
                    "dumb.sql");
            SQLiteAdapter.MAX_INSERT_RECORDS = Integer
                    .parseInt(json_response_prase.get("count").toString());*/

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            // TODO: handle exception

        }

    }

    public static void dump_api(String userId, String roleId) {
        // stateId,surveysetId,userId,roleId
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        //	//Log.v("", "" + DOMAIN_VKC);
        temp_url.append(API_ACTION_DBDUMP);
        try {

            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));

            // //Log.e("", "table Length" + tablesStr.length);

            // nameValuePairs.add(new BasicNameValuePair("stateId", stateId));
            /*
             * nameValuePairs.add(new BasicNameValuePair("surveysetId",
             * surveysetId));
             */
            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("roleId", roleId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStringContacts values " + json_Response_Str);
            //System.out.println("jStringContacts values1 " + userId);
            //System.out.println("jStringContacts values 1" + roleId);

            JSONObject json_Response = new JSONObject(json_Response_Str);

            JSONObject json_response_prase = json_Response
                    .getJSONObject("response");

            DownloadFile download = new DownloadFile();
            download.DownloadFromUrl(json_response_prase.getString("filepath"),
                    "dumb.sql");
            SQLiteAdapter.MAX_INSERT_RECORDS = Integer
                    .parseInt(json_response_prase.get("count").toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            // TODO: handle exception

        }

    }

    // /save servey api
    /*
     * public static boolean export_servey(String jsonString, int mode, Context
     * context) {
     */
    public static boolean export_servey(String jsonString, int mode,
                                        Context context) {


        String temp_url = new String(DOMAIN_VKC);
        String[] name;
        if (mode == CUSTOMER_LIST) {
            //   //System.out.println("JSOn STRING--------------->" + jsonString);

            temp_url = temp_url + API_ACTION_SET_CUSTOMER;
        } else {
            // Log.d("TAG", "JSOn STRING--------------->" + jsonString);
            //   //System.out.println("JSOn STRING--------------->" + jsonString);
            temp_url = temp_url + API_ACTION_SAVE_SERVEY;
        }


        try {
            PreferenceManager.saveUpdate("no", context);
            PreferenceManager.saveCustUpdate("no", context);
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));

            if (mode == CUSTOMER_LIST) {
                nameValuePairs.add(new BasicNameValuePair("customer_details",
                        jsonString));
                //  System.out.println("Customer Data Bib" + jsonString);

            } else if (mode == SERVEY_LIST) {
                nameValuePairs.add(new BasicNameValuePair("survey_result",
                        jsonString));
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStringContacts values " + json_Response_Str);

            JSONObject json_Response = new JSONObject(json_Response_Str);
            if (mode == CUSTOMER_LIST) {
                UpdateCustomerNewid(json_Response, context);
            } else if (mode == SERVEY_LIST) {
                try {
                    JSONArray result_json_array = json_Response
                            .getJSONArray("response");
                    for (int i = 0; i < result_json_array.length(); i++) {
                        JSONObject each_result = result_json_array
                                .getJSONObject(i);
                        mySQLiteAdapter = new SQLiteAdapter(context);
                        mySQLiteAdapter.openToRead();
                        String[] columns = new String[]{"customer_id",
                                "survey_id", "survey_question_id",
                                "survey_question", "survey_answer",
                                "survey_time", "survey_question_type",
                                "survey_question_option"};
                        Cursor cursor = mySQLiteAdapter.queueAll(
                                "survey_result", columns, null, "survey_id="
                                        + each_result.get("temp_id"));

                        cursor.moveToPosition(0);
                        while (cursor.isAfterLast() == false) {
                            if (cursor.getString(6).equalsIgnoreCase(
                                    UserValues.Q_PICTURE)) {
                                /*
                                 * if (VkcApis.imageUpload(cursor.getString(4),
                                 * cursor.getString(0),
                                 * each_result.get("new_id").toString(),
                                 * cursor.getString(2))) { //Log.v("",
                                 * "Image file uploaded"); } else { //Log.v("",
                                 * "Image file not uploaded"); }
                                 */
                            }
                            cursor.moveToNext();
                        }
                        cursor.close();
                        mySQLiteAdapter.close();
                    }
                    PreferenceManager.saveUpdate("yes", context);

                    //Bibin added return true 20/12/2018
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    PreferenceManager.saveUpdate("no", context);
                    PreferenceManager.saveCustUpdate("no", context);
                    mySQLiteAdapter.close();
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    PreferenceManager.saveUpdate("no", context);
                    PreferenceManager.saveCustUpdate("no", context);
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    PreferenceManager.saveUpdate("no", context);
                    PreferenceManager.saveCustUpdate("no", context);
                    return false;
                }
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //Log.v("", "MalformedURLException");
            PreferenceManager.saveUpdate("no", context);
            PreferenceManager.saveCustUpdate("no", context);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            //Log.v("", "IOException");
            PreferenceManager.saveUpdate("no", context);
            PreferenceManager.saveCustUpdate("no", context);
            return false;

        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
            //Log.v("", "JSONException");
            PreferenceManager.saveUpdate("no", context);
            PreferenceManager.saveCustUpdate("no", context);
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //Log.v("", "JSONException");
            PreferenceManager.saveUpdate("no", context);
            PreferenceManager.saveCustUpdate("no", context);
            return false;
        }
    }

    public static void UpdateCustomerNewid(JSONObject json_Response,
                                           Context context) {
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        try {
            JSONArray customer_json_arry = json_Response
                    .getJSONArray("response");
            String status = json_Response.optString("status");
            ////Log.v("status", status);
            if (status.equals("success")) {
                for (int i = 0; i < customer_json_arry.length(); i++) {

                    JSONObject each_customer = customer_json_arry
                            .getJSONObject(i);

                    System.out.println("Cust ID: " + each_customer.getString("cust_id"));

                    String[][] data_serveycustomer = {
                            {"status", "0"},
                            {"customer_id", each_customer.getString("new_id")},
                            {"cust_id",
                                    each_customer.getString("cust_id")}

                    };

                    String[][] data_editcust = {{"status", "0"}};
                    String[][] con_editcust = {{"status", "2"}};
                    String[][] data_serveyresults = {{"customer_id",
                            each_customer.getString("new_id"),}

                    };
                    String[][] cons = {{"customer_id",
                            each_customer.getString("temp_id")}};

                    mySQLiteAdapter.update(data_serveycustomer,
                            "survey_customerdetails", cons);
                    mySQLiteAdapter.update(data_editcust,
                            "survey_customerdetails", con_editcust);

                    mySQLiteAdapter.update(data_editcust,
                            "survey_customerdetails", con_editcust);

                    mySQLiteAdapter.update(data_serveyresults, "survey_result",
                            cons);


                }
                PreferenceManager.saveCustUpdate("yes", context);

                mySQLiteAdapter.close();
            } else {
                PreferenceManager.saveCustUpdate("no", context);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            PreferenceManager.saveCustUpdate("no", context);
        }

        mySQLiteAdapter.close();

    }

    public static boolean imageUpload(String fileName, String user_id,
                                      String survey_id, String question_id) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;

        String exsistingFileName = fileName;
        // Is this the place are you doing something wrong.

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;

        String responseFromServer = "";

        String urlString = DOMAIN_IMAGE_UPLOAD + "?user_id=" + user_id
                + "&survey_id=" + survey_id + "&question_id" + question_id;
        // upload

        try {
            // ------------------ CLIENT REQUEST

            //Log.e("fileupload", "Inside second Method");

            FileInputStream fileInputStream = new FileInputStream(new File(
                    exsistingFileName));

            // open a URL connection to the Servlet

            URL url = new URL(urlString);

            // Open a HTTP connection to the URL

            conn = (HttpURLConnection) url.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("user_id", "1");

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"survey_file\";filename=\""
                    + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            //Log.e("MediaPlayer", "Headers are written");

            // create a buffer of maximum size

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            //Log.e("fileupload", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            //Log.e("fileupload", "error: " + ex.getMessage(), ex);
            return false;
        } catch (IOException ioe) {
            //Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
            return false;
        }

        // ------------------ read the SERVER RESPONSE

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // showAlert("Dialoge Box", "Message: " + line,"OK", false);
                //System.out.println("Dialog Box" + line);
            }
            rd.close();

        } catch (IOException ioex) {
            //Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
            return false;
        }
        return true;
    }

    // login api
    public static Survey user_login(String username, String password,
                                    Context mcontext) throws ClientProtocolException, IOException,
            JSONException {
        Survey[] surveyArray = null;
        ArrayList<Survey> sArrayList = new ArrayList<Survey>();
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        Survey surVey = null;
        //String json_Response_Str=null;


        // Survey.surveySets=new ArrayList<SurveySetModel>();
        context = mcontext;
        //Log.v("", "" + DOMAIN_VKC);
        temp_url.append(API_ACTION_LOGIN);
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            httpResponse = httpclient.execute(httppost);
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStr " + json_Response_Str);
            JSONObject json_Response = new JSONObject(json_Response_Str);
            // JSONArray array = json_Response.getJSONArray("accountdetail");
            // for(int i=0;i<array.length();i++){
            //System.out.println("JOBTECT 1");
            surVey = new Survey();
            JSONObject jsonObject = json_Response.getJSONObject("userdetails");
            //System.out.println("Message--------->" + jsonObject);
            // surveySetModel
            // Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show();

            sArrayList.add(surVey);
            String userName = jsonObject.getString("userName").toString();
            String userId = jsonObject.getString("userId").toString();
            String roleId = jsonObject.getString("roleId").toString();
            String email = jsonObject.getString("email").toString();
            String state = jsonObject.getString("state").toString();
            String device_id = jsonObject.getString("imei_no").toString();
            surVey.status = jsonObject.getString("status").toString();
            surVey.userName = userName;
            surVey.userId = userId;
            surVey.roleId = roleId;
            surVey.email = email;
            AppPreferenceManager.saveStateName(state,
                    context);
            AppPreferenceManager.saveDeviceID(device_id, mcontext);
            AppPreferenceManager.saveUserId(userId, mcontext);
            AppPreferenceManager.saveRoleId(roleId, mcontext);
           /* //System.out.println("JOBTECT 2" + userName);
            //System.out.println("JOBTECT 2" + userId);
            //System.out.println("JOBTECT 2" + roleId);
            //System.out.println("JOBTECT 2" + email);*/

        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return surVey;

    }


    public static JSONObject user_attendance(String user_id, String type, String attendance_id, String lat, String lng, String place, String user_location,
                                             Context mContext) throws ClientProtocolException, IOException,
            JSONException {

        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        //String response = "";
        String attendance_id_ = "";
        //String json_Response_Str=null;
        JSONObject json_Response = null;
        if (lat.equals("0.0")) {

            lat = String.valueOf("10.9067713");
            lng = String.valueOf("77.0119241");

        }
        // Survey.surveySets=new ArrayList<SurveySetModel>();
        context = mContext;
        //Log.v("", "" + DOMAIN_VKC);
        temp_url.append(API_ACTION_ATTENDANCE);
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();


            postParameters.add(new BasicNameValuePair("user_id", user_id));
            postParameters.add(new BasicNameValuePair("type", type));
            postParameters.add(new BasicNameValuePair("attendance_id", attendance_id));
            postParameters.add(new BasicNameValuePair("location", place));
            postParameters.add(new BasicNameValuePair("latitude", lat));
            postParameters.add(new BasicNameValuePair("longitude", lng));
            postParameters.add(new BasicNameValuePair("user_location", user_location));

            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            httpResponse = httpclient.execute(httppost);
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStr " + json_Response_Str);
            json_Response = new JSONObject(json_Response_Str);

            // System.out.println("Response",json_Response.toString());
            // JSONArray array = json_Response.getJSONArray("accountdetail");
            // for(int i=0;i<array.length();i++){
            //System.out.println("JOBTECT 1");
            //System.out.println("Message--------->" + jsonObject);
            // surveySetModel
            // Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show();

            //response = json_Response.getString("response").toString();

            // attendance_id_ = json_Response.optString("attendance_id").toString();
            // String message = json_Response.optString("message").toString();
            AppPreferenceManager.saveDeviceID("", mContext);
            /*if (response.equalsIgnoreCase("error")) {
                response = message;
            }
*/
            // AppPreferenceManager.saveAttendance(true, mContext);
            //  AppPreferenceManager.saveAttendanceId(attendance_id_, mContext);
           /* //System.out.println("JOBTECT 2" + userName);
            //System.out.println("JOBTECT 2" + userId);
            //System.out.println("JOBTECT 2" + roleId);
            //System.out.println("JOBTECT 2" + email);*/

        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json_Response;

    }

    public static String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("value.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        //System.out.println("JSON from ASSETS------------->" + json);
        return json;
    }

    public static Survey[] getNewCustomers(String userId, String roleId,
                                           String cusId) throws ClientProtocolException, IOException,
            JSONException {
        Survey[] surVey = null;
        String result = null;
        Survey sdetails = null;
        ArrayList<Survey> cusArrayList = new ArrayList<Survey>();
        JSONObject jObject = null;
        // String loginUrl = "getAllVehicles";
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);

        temp_url.append(API_ACTION_SYNCCUSTOMERS);
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

            postParameters.add(new BasicNameValuePair("userId", userId));
            postParameters.add(new BasicNameValuePair("roleId", roleId));
            postParameters.add(new BasicNameValuePair("lastCustomerId", cusId));
            //System.out.println("To Server " + userId);
            //System.out.println("To Server " + roleId);
            //System.out.println("To Server " + cusId);


            httppost.setEntity(new UrlEncodedFormEntity(postParameters));

            httpResponse = httpclient.execute(httppost);
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());
            //System.out.println("Respnse from Server " + json_Response_Str);
            JSONObject joJsonObject = new JSONObject(json_Response_Str);
            //System.out.println("Respnse from Server " + joJsonObject);

            JSONArray slideContent = (JSONArray) joJsonObject.get("response");
            for (int i = 0; i < slideContent.length(); i++) {
                JSONObject jsonObject = slideContent.getJSONObject(i);

                sdetails = new Survey();

                sdetails.cus_id = jsonObject.getString("customer_id");
                sdetails.customer_name = jsonObject.getString("customer_name");
                sdetails.customer_category = jsonObject
                        .getString("customer_category");
                sdetails.customer_add1 = jsonObject.getString("customer_add1");
                sdetails.customer_add2 = jsonObject.getString("customer_add2");
                sdetails.customer_add3 = jsonObject.getString("customer_add3");
                sdetails.customer_add4 = jsonObject.getString("customer_add4");
                sdetails.customer_add5 = jsonObject.getString("customer_add5");
                sdetails.customer_phone = jsonObject.getString("customer_phone");
                sdetails.customer_place = jsonObject.getString("customer_place");
                sdetails.customer_businesstype = jsonObject
                        .getString("customer_businesstype");
                sdetails.customer_shopname = jsonObject
                        .getString("customer_shopname");
                sdetails.customer_phone2 = jsonObject.getString("customer_phone2");
                sdetails.customer_email = jsonObject.getString("customer_email");
                sdetails.customer_dist = jsonObject.getString("customer_dist");
                sdetails.created_on = jsonObject.getString("created_on");
                sdetails.customer_status = jsonObject.getString("customer_status");
                sdetails.customer_state = jsonObject.getString("customer_state");
                sdetails.customer_desgn = jsonObject.getString("customer_desgn");
                sdetails.customer_department = jsonObject
                        .getString("customer_department");
                sdetails.customer_country = jsonObject
                        .getString("customer_country");
                sdetails.updated_from_phone = jsonObject
                        .getString("updated_from_phone");
                sdetails.latitude = jsonObject.getString("latitude");
                sdetails.longitude = jsonObject.getString("longitude");

                //
                sdetails.is_verified = jsonObject.getString("is_verified");
                sdetails.isMarkedforDeletion = jsonObject.getString("is_marked_deletion");
                cusArrayList.add(sdetails);
            }
            surVey = new Survey[cusArrayList.size()];

            for (int x = 0; x < cusArrayList.size(); ++x) {

                surVey[x] = (Survey) cusArrayList.get(x);
            }
            /*for (int j = 0; j < surVey.length; j++) {
                //System.out.println("ITEM " + surVey[j].cus_id);
                //System.out.println("ITEM " + surVey[j].customer_email);

            }*/
        } catch (SocketTimeoutException e) {
            //System.out.println("Exception-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("Exception-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("Exception-------->12" + ex);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return surVey;
    }

    public static Survey export_gps(String jsonString, Context context) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        String result = null;
        temp_url.append(API_ACTION_SAVE_GPS_CUSTOMER);
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        Survey survey = null;
        try {

            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            //System.out.println("ASync TASK 3------->");

            nameValuePairs.add(new BasicNameValuePair("custData", jsonString));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStringContacts values " + json_Response_Str);
            survey = new Survey();
            JSONObject json_Response = new JSONObject(json_Response_Str);
            String status = json_Response.getString("response");
            //System.out.println("jStatus " + status);

            if (status.contentEquals("success")) {
                survey.status = "success";
            } else {
                survey.status = "failed";

            }
        } catch (SocketTimeoutException e) {
            //System.out.println("Exception-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("Exception-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("Exception-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return survey;
    }

    public static Survey export_claims(String userId, String surveySetId,
                                       String title, String discription, String requestedAmount,
                                       String image, String planId, String planDetailID, Context context) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        String result = null;
        temp_url.append(API_ACTION_SUBMIT_CLAIMS);
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        Survey survey = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            //System.out.println("ASync TASK 3------->");

            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("surveySetId",
                    surveySetId));
            nameValuePairs.add(new BasicNameValuePair("title", title));
            nameValuePairs.add(new BasicNameValuePair("discription",
                    discription));
            nameValuePairs.add(new BasicNameValuePair("requestedAmount",
                    requestedAmount));
            nameValuePairs.add(new BasicNameValuePair("image", image));
            nameValuePairs.add(new BasicNameValuePair("planId", planId));
            nameValuePairs.add(new BasicNameValuePair("plandetailId", planDetailID));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStringContacts values " + json_Response_Str);
            survey = new Survey();
            JSONObject json_Response = new JSONObject(json_Response_Str);
            String status = json_Response.getString("response");
            //System.out.println("jStatus " + status);

            if (status.contentEquals("success")) {
                survey.status = "success";
            } else {
                survey.status = "failed";

            }
        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return survey;
    }

    public static ArrayList<Claims> get_claims(String userId, Context context) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        String result = null;
        temp_url.append(API_ACTION_GET_CLAIMSLIST);
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        Claims claims = null;
        ArrayList<Claims> claimsArrayList = new ArrayList<Claims>();
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            //System.out.println("Get Claims------->");

            nameValuePairs.add(new BasicNameValuePair("userId", userId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("Get Claims values " + json_Response_Str);
            JSONObject json_Response = new JSONObject(json_Response_Str);
            String status = json_Response.getString("response");
            //System.out.println("jStatus " + status);
            JSONArray jaArray = new JSONArray(status);
            //System.out.println("Array is " + jaArray.length());

            for (int i = 0; i < jaArray.length(); i++) {
                JSONObject joObject = jaArray.getJSONObject(i);
                claims = new Claims();

                claims.title = joObject.getString("title").toString();
                claims.description = joObject.getString("discription")
                        .toString();
                claims.reqAmount = joObject.getString("requestedAmount")
                        .toString();
                claims.aprAmount = joObject.getString("approvedAmount")
                        .toString();
                claims.staus = joObject.getString("status").toString();
                claims.date = joObject.getString("billDate").toString();

                //System.out.println("title is " + claims.title);
                claimsArrayList.add(claims);
            }

            /*
             * if (status.contentEquals("success")) { survey.status = "success";
             * } else { survey.status = "failed";
             *
             * }
             */
        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return claimsArrayList;
    }

    public static ArrayList<Survey> get_tourplan(String userId, Context context) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        String result = null;
        temp_url.append(API_ACTION_GET_PLANLIST);
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        Survey plan = null;
        ArrayList<Survey> plansArrayList = new ArrayList<Survey>();
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            //System.out.println("Get Plans------->" + userId);

            nameValuePairs.add(new BasicNameValuePair("userId", userId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("Get Plan values " + json_Response_Str);
            JSONObject json_Response = new JSONObject(json_Response_Str);
            // String status = json_Response.getString("response");
            // //System.out.println("jStatus " + status);
            // JSONArray jaArray = new JSONArray("response");
            JSONArray jaArray = json_Response.getJSONArray("response");
            //System.out.println("Array is " + jaArray.length());
            //System.out.println("Array is " + jaArray);

            for (int i = 0; i < jaArray.length(); i++) {
                //System.out.println("plan is 1");

                JSONObject joObject = jaArray.getJSONObject(i);

                plan = new Survey();

                plan.plan_name = joObject.getString("planName").toString();
                //System.out.println("plan is 2 " + plan.plan_name);

                plan.survey_name = joObject.getString("surveyrole_name")
                        .toString();
                plan.state_name = joObject.getString("state_name").toString();
                // plan.budget = joObject.getString("budget").toString();
                plan.plan_id = joObject.getString("id").toString();

                //System.out.println("plan is " + plan.plan_name);
                plansArrayList.add(plan);
            }

            /*
             * if (status.contentEquals("success")) { survey.status = "success";
             * } else { survey.status = "failed";
             *
             * }
             */
        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return plansArrayList;
    }

    public static Survey get_tourplanindetail(String userId, String planId,
                                              Context context) {
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        String result = null;
        temp_url.append(API_ACTION_GET_PLANLISTDETAIL);
        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(context);
        mySQLiteAdapter.openToWrite();
        Survey plan = null;
        Survey cus = null;
        ArrayList<Survey> plansArrayList = new ArrayList<Survey>();
        ArrayList<Survey> dateArrayList = new ArrayList<Survey>();

        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));
            //System.out.println("Get Plans Details------->" + userId);

            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("planId", planId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("Get Plan values Details " + json_Response_Str);
            JSONObject json_Response = new JSONObject(json_Response_Str);
            // String status = json_Response.getString("response");
            // //System.out.println("Array is " + jaArray.length());
            JSONArray jaArray = json_Response.getJSONArray("response");
            //System.out.println("Array is " + jaArray);

            for (int i = 0; i < jaArray.length(); i++) {
                cus = new Survey();
                String surveyrole_name = jaArray.getJSONObject(i)
                        .getString("surveyrole_name").toString();
                //System.out.println("Plan id is " + surveyrole_name);
                String planName = jaArray.getJSONObject(i)
                        .getString("planName").toString();
                String state_name = jaArray.getJSONObject(i)
                        .getString("state_name").toString();

                AppPreferenceManager.setSurveyNamePreference(surveyrole_name,
                        context);
                AppPreferenceManager.setPlanName(planName, context);
                AppPreferenceManager.setBudget(state_name, context);

                // plan.survey_name=surveyset_name;
                // plan.budget=budget;
                // plan.plan_name=planName;
                // plansArrayList.add(plan);

                String planDates = jaArray.getJSONObject(i)
                        .getString("planDates").toString();
                //System.out.println("planDates" + planDates);

                JSONArray planDateArray = new JSONArray(planDates);

                for (int j = 0; j < planDateArray.length(); j++) {
                    plan = new Survey();
                    String from_date = planDateArray.getJSONObject(j)
                            .getString("from_date").toString();
                    String to_date = planDateArray.getJSONObject(j)
                            .getString("to_date").toString();
                    String districtName = planDateArray.getJSONObject(j)
                            .getString("districtName").toString();
                    String budget = planDateArray.getJSONObject(j)
                            .getString("budget").toString();
                    String detailId = planDateArray.getJSONObject(j)
                            .getString("detailID").toString();
                    String expenseCliamed = planDateArray.getJSONObject(j)
                            .getString("expenceClaimed").toString();
                    String overallStatus = planDateArray.getJSONObject(j)
                            .getString("status").toString();
                    // plan.plan_date=planDateArray.getJSONObject(j).getString("surveyDate").toString();
                    plan.plan_from_date = from_date;
                    plan.plan_to_date = to_date;
                    plan.districtName = districtName;
                    plan.budget = budget;
                    plan.detailId = detailId;
                    plan.expenseCliamed = expenseCliamed;
                    plan.ovrerallStatus = overallStatus;
                    // plan.setDistrict(districtName);
                    AppPreferenceManager.saveDistrict(districtName, context);
                    String customers = planDateArray.getJSONObject(j)
                            .getString("customers").toString();
                    dateArrayList.add(plan);
                    SQLiteAdapter adapter = new SQLiteAdapter(context);
                    adapter.openToWrite();
                    // adapter.makeEmpty("survey_tourplan");
                    JSONArray jCArray = new JSONArray(customers);
                    for (int k = 0; k < jCArray.length(); k++) {
                        plan = new Survey();
                        plan.plan_date = from_date;
                        plan.cus_id = jCArray.getJSONObject(k)
                                .getString("customerId").toString();
                        plan.customer_name = jCArray.getJSONObject(k)
                                .getString("customer_shopname").toString();
                        plan.districtName = districtName;

                        adapter.addtourPlan(plan);
                        plansArrayList.add(plan);
                        //System.out.println("End of Cus loop ");

                    }
                    plan.setPlanList(plansArrayList);
                    //System.out.println("End of Cus loop 1");
                    adapter.close();
                }
                plan.setPlanDate(dateArrayList);
                //System.out.println("End of Cus loop 2");

            }

        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return plan;
    }


    public static String getbalance(String plandetailId) {
        // stateId,surveysetId,userId,roleId
        String response = null;
        StringBuffer temp_url = new StringBuffer(DOMAIN_VKC);
        //Log.v("", "" + DOMAIN_VKC);
        temp_url.append(API_ACTION_GETBALANCE);
        try {
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(temp_url.toString());

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("input",
            // "Hospital:8.521671:76.92638:::"));

            // //Log.e("", "table Length" + tablesStr.length);

            // nameValuePairs.add(new BasicNameValuePair("stateId", stateId));
            /*
             * nameValuePairs.add(new BasicNameValuePair("surveysetId",
             * surveysetId));
             */
            nameValuePairs.add(new BasicNameValuePair("plandetailId", plandetailId));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpclient.execute(httppost);
            // json file returned as a string
            String json_Response_Str = httpResponse.getEntity().toString();
            json_Response_Str = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println("jStringContacts values " + json_Response_Str);
            //System.out.println("jStringContacts values1 " + plandetailId);

            JSONObject json_Response = new JSONObject(json_Response_Str);

            response = json_Response
                    .getString("response");

            //response=json_response_prase.getString("response");

            ////System.out.println("123" + json_response_prase);
            //System.out.println("123" + response);


        } catch (SocketTimeoutException e) {
            //System.out.println("ABCD-------->" + e);
        } catch (ConnectTimeoutException exe) {
            // TODO: handle exception
            //System.out.println("ABCD-------->1" + exe);

        } catch (HttpHostConnectException ex) {
            //System.out.println("ABCD-------->12" + ex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }
}
