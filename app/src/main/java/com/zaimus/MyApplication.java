package com.zaimus;


import android.app.Application;

import com.zaimus.api.model.CustomerData;


/**
 * Created by Ravi Tamada on 15/06/16.
 * www.androidhive.info
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    public static String randomNo;
    //private RequestQueue mRequestQueue;

    public static CustomerData customerData;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        customerData = new CustomerData();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    /*public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }*/

/*

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
*/

}
