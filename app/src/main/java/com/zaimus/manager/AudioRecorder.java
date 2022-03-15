package com.zaimus.manager;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import com.zaimus.constants.GlobalConstants;

public class AudioRecorder {

	public static MediaRecorder mRecorder = null;
	public static Timer longTimer;
	
	 public  void onRecord(boolean start)
	 {
	    if (start)
	     {
	           startRecording();
	       } 
	    else 
	    {
	          stopRecording();
	        
	    }
	 }
	 
	 private void startRecording() 
	 {
		    mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	        //System.out.println("GlobalConstants.AUDIO_FILEPATH " +GlobalConstants.AUDIO_FILEPATH);
	        mRecorder.setOutputFile(GlobalConstants.AUDIO_FILEPATH);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        try {
	            mRecorder.prepare();
	        } catch (Exception e) {
	            //Log.e("", "prepare() failed");
	        }

	        mRecorder.start();
	    }

	    private void stopRecording()
	    {
	    	try
	    	{
	    		 mRecorder.stop();
	 	        mRecorder.release();
	 	        mRecorder = null;
	    	}
	    	catch (Exception e) {
				// TODO: handle exception
			}
	       
	    }
	    public static synchronized void setupLongTimeout(long timeout,
				final Activity ctx) {
			
		
			if (longTimer != null)
			{
				longTimer.cancel();
			}

			longTimer = new Timer();
			longTimer.schedule(new TimerTask() {
				private Handler updateUI = new Handler() {
					@Override
					public void dispatchMessage(Message msg) {
						super.dispatchMessage(msg);
					}
				};

				public void run()
				{
					longTimer.cancel();
					updateUI.sendEmptyMessage(0);
					
					if(mRecorder!=null)
					{
						 mRecorder.stop();
					        mRecorder.release();
					        mRecorder = null;
					        //Log.e("", "Recorder stopped");
					}
				}
			}, timeout);

		}
		
		/**
		 * Stopping the timer.
		 * 
		 *  Calling in all activites onResume Method
		 */
		public static synchronized void stopTimer()
		{
			if (longTimer != null) {
				longTimer.cancel();
			}
		}

}
