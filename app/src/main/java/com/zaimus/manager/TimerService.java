package com.zaimus.manager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.zaimus.VKCAppActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TimerService extends JobService {


    Context mContext;
    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;

    @Override

    public boolean onStartJob(JobParameters params) {

        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        time = simpleDateFormat.format(calander.getTime());

        if (time.equalsIgnoreCase("02:05 pm")) {
            Toast.makeText(mContext, "Time :" + time, Toast.LENGTH_LONG).show();
        }
        return true;


    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    VKCAppActivity mActivity;
    private final LinkedList<JobParameters> jobParamsMap = new LinkedList<JobParameters>();

    public void setUiCallback(VKCAppActivity activity) {
        mActivity = activity;
    }
}
