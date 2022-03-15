package com.zaimus.UsrValues;

import android.os.Environment;

public class SdcardManager {
	
	/// STATIC FUNCTION FOR CHECKING SDCARD IS MOUNTED OR NOT 
	public  static boolean isSDCARDMounted()
	{
		return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
	}
	/// STATIC FUNCTION FOR CHECKING SDCARD IS MOUNTED OR NOT
}
