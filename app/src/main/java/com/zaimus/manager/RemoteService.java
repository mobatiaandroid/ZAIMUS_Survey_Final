package com.zaimus.manager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemoteService extends Service {
    Context mContext;
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

        time = simpleDateFormat.format(calander.getTime());

        if (time.equals("10:23:00 AM")) {
            Toast.makeText(mContext, "Time :" + time, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

