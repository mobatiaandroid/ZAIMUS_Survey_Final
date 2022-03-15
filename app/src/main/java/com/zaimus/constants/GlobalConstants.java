package com.zaimus.constants;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GlobalConstants {

	static Calendar cal = new GregorianCalendar();
	public static String AUDIO_FILEPATH;
	public static String customer_id;
	public static String customer_status;
	public static String AUDIO_FILENAME;
	public static String DEVICE_ID ;
	public static final int LOCATION_REQUEST = 1000;
	public static final int GPS_REQUEST = 1001;
	public static long AUDIO_TIMEOUT=2*60*1000;
	
	public static String day;
	public static String year;
	public static String month;
	public static String hour;
	public static String minute;
	public static String seconds;
/*	public static String AUDIO_FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	+ "/vkc_uploads/";
	//+ "/data/com.vkc/audio_" + new Date().getTime() + ".3gp";
*/	
	
}
