package com.zaimus.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.json.JSONObject;
import org.json.JSONStringer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.zaimus.constants.GlobalConstants;

import androidx.core.app.ActivityCompat;

public class Utils {

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei_no;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return telephonyManager.getDeviceId();
        }

        if (Build.VERSION.SDK_INT > 28 ) {
            imei_no=AppPreferenceManager.getDeviceID(context);
        }

       else if (Build.VERSION.SDK_INT >= 26 ) {
            int phoneCount = telephonyManager.getPhoneCount();
            //only api 21 above
            if (phoneCount > 1) {
                imei_no = telephonyManager.getImei(0);
            } else {
                imei_no = telephonyManager.getImei();
            }
        } else {
            //only api 21 down


            imei_no = telephonyManager.getDeviceId();
        }
        return imei_no;
    }


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFilePath() {
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/vkc_uploads/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        //System.out.println("folder.getAbsolutePath()" + folder.getAbsolutePath());
        return folder.getAbsolutePath();
    }

    public static String jsonForQ_DYNAMIC_SUG(Context context, String answerText) {
        //	String sn= "an footwears:100,httfootweears:560";
        String shopnames = "";
        String[] commaSeperated = null, name = null, colonSeperated = null;
        commaSeperated = answerText.split(",");
        JSONStringer js = new JSONStringer();
        try {
            //js.object();
            js.array();

            if (commaSeperated == null) {
                colonSeperated = answerText.split(":");
                js.object().key("shop_name").value(colonSeperated[0]);
                js.key("dealer_id").value(colonSeperated[1]);
                js.key("rank").value(1);
                js.key("cid").value(255);
                js.endObject();
            } else {
                for (int i = 0; i < commaSeperated.length; i++) {
                    name = commaSeperated[i].split(":");
                    js.object().key("shop_name").value(name[0]);
                    js.key("dealer_id").value(name[1]);
                    js.key("rank").value(i + 1);
                    js.key("cid").value(255);
                    js.endObject();
                }
            }

            js.endArray();
            //	js.endObject();
            //System.out.println("json" + js.toString());
            return js.toString();

        } catch (Exception e) {
            //System.out.println("error" + e.getMessage());
        }
        return js.toString();
    }

    public static String jsonObjectForQ_DYNAMIC_SUG(Context context, String answerText) {
        //	String sn= "an footwears:100,httfootweears:560";
        String shopnames = "";
        String[] commaSeperated = null, name = null, colonSeperated = null;
        commaSeperated = answerText.split(",");
        JSONObject js = new JSONObject();
        try {


            if (commaSeperated == null) {
                colonSeperated = answerText.split(":");
                //js.object().key("answerText").value(colonSeperated[0]);
                js.put("shopname", colonSeperated[0]);
                js.put("dealer_id", colonSeperated[1]);
                js.put("rank", 1);
                js.put("cid", 255);
            } else {
                for (int i = 0; i < commaSeperated.length; i++) {
                    name = commaSeperated[i].split(":");
                    //	shopnames+=name[0]+",";
                    //	js.object().key("answerText").value(shopnames);
                    js.put("shop_name", name[0]);
                    js.put("dealer_id", name[1]);
                    js.put("rank", i + 1);
                    js.put("cid", 255);
                }
            }

            //		js.endArray();
            //	js.endObject();
            //System.out.println("json" + js.toString());
            return js.toString();

        } catch (Exception e) {
            //System.out.println("error" + e.getMessage());
        }
        return js.toString();
    }

    public static boolean isSDCardMounted() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }


    }

    public static String getCalender() {
        Calendar c = Calendar.getInstance();
        GlobalConstants.year = String.valueOf(c.get(Calendar.YEAR));
        GlobalConstants.month = String.valueOf(c.get(Calendar.MONTH));
        GlobalConstants.day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        GlobalConstants.hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        GlobalConstants.minute = String.valueOf(c.get(Calendar.MINUTE));
        GlobalConstants.seconds = String.valueOf(c.get(Calendar.SECOND));

        String timestamp = GlobalConstants.hour + "_" + GlobalConstants.minute + "_" + GlobalConstants.seconds
                + "_" + GlobalConstants.day + "_" + getMonth(Integer.valueOf(GlobalConstants.month)) + "_" + GlobalConstants.year;
        ////Log.e("", "Time stamp"+timestamp);
        return timestamp;

    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();

            if (outChannel != null)
                outChannel.close();
        }


    }

    public static void checkDirExists(String dir) {
        File directory = new File(android.os.Environment.getExternalStorageDirectory(), dir);
        if (!directory.exists())
            directory.mkdirs();

    }
	/*public static int[] getWindowDimens(Context context) {
	int dimen[] = new int[2];
	Point size = new Point();
	WindowManager w = ((Activity) context).getWindowManager();
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	w.getDefaultDisplay().getSize(size);
	dimen[0] = size.x;
	dimen[1] = size.y;
	} else {
	Display d = w.getDefaultDisplay();
	dimen[0] = d.getWidth();
	dimen[1] = d.getHeight();
	}
	return dimen;
	}*/

    public static Typeface setFontTypeToArial(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
        return typeface;
    }

    public static Typeface setFontTypeToArialbold(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/arialbd.ttf");
        return typeface;
    }
}
