package com.zaimus.SurveyTypes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

import com.zaimus.MyApplication;
import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.UsrValues.UserValues;
import com.zaimus.constants.GlobalConstants;
import com.zaimus.Survey.SurveyAnswers;
import com.zaimus.Survey.SurveyQuestions;
import com.zaimus.manager.AppPreferenceManager;


public class SurveyTypeUtils {
	private SurveyQuestions s_question;
	private SQLiteAdapter mySQlAdapter;
	private SurveyAnswers save_answer;
	String temp;
	//new url
	//String PhotoURL="http://survey.walkaroo.in/media/uploads/storeimages/";
	//String PhotoURL="http://115.248.105.147/media/uploads/storeimages/";
	//String PhotoURL="http://dev.mobatia.com/vkcsurveyUAT1/media/uploads/storeimages/";
	String PhotoURL="http://survey.walkaroo.in/media/uploads/storeimages/";
	public Intent getIntent(Context context) {

		s_question = Survey.surveyQuestions.get(Survey.q_id);
		//Log.v("", "s_question.getQuestID()" + s_question.getQuestionId());
		//Log.v("", "s_question.getQuestType()" + s_question.getQuestType());

		// Display question ID
		if (s_question.getQuestType().equalsIgnoreCase(UserValues.Q_SRADIO)) {
			// If the question type is single selection radio
			return new Intent(context, SingleSelectionRadio.class).putExtra(
					"qid", Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_SLIST)) {
			// If the question type is single selection list

			return new Intent(context, SingleSelectionList.class).putExtra(
					"qid", Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_MATRIX)) {
			// If the question type is matrix

			return new Intent(context, Matrix.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_ORDER)) {

			return new Intent(context, Order.class)
					.putExtra("qid", Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_MCHECK)) {

			return new Intent(context, MultiSelectionCheck.class).putExtra(
					"qid", Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_SLIDER)) {

			return new Intent(context, Slider.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType()
				.equalsIgnoreCase(UserValues.Q_STAR)) {

			return new Intent(context, Star.class).putExtra("qid", Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_TXT_IP)) {

			return new Intent(context, TextInput.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_INT_IP)) {

			return new Intent(context, IntInput.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_PICTURE)) {

			return new Intent(context, ImageCapture.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_DYNAMIC_SUG)) {
			return new Intent(context, AutoComplete.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_PRODUCT_FEEDBACK)) {
			
			return new Intent(context, ImageFeedback.class).putExtra("qid",
					Survey.q_id);
		} else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_TABULAR)) {
			return new Intent(context, TabularQuestions.class).putExtra("qid",
					Survey.q_id);
		}else if (s_question.getQuestType().equalsIgnoreCase(
				UserValues.Q_IMAGE_SELECTION)) {
			return new Intent(context, ImageSelection.class).putExtra("qid",
					Survey.q_id);
		}
		return null;
	}

	public boolean saveSurveyResults(Context context, String device_id) {

		mySQlAdapter = new SQLiteAdapter(context);
		mySQlAdapter.openToWrite();

		String status = "0";

		if (Build.VERSION.SDK_INT > 28 ) {
			device_id=AppPreferenceManager.getDeviceID(context);
		}
		/* for updating customer table */
		/*Log.d("TAg", "GlobalConstants.customer_status---------->1"+GlobalConstants.customer_status);
		if (GlobalConstants.customer_status.equals("1")) {
			Log.d("TAg", "GlobalConstants.customer_status---------->2"+GlobalConstants.customer_status);

			status = "1";
		} else {
			Log.d("TAg", "GlobalConstants.customer_status---------->3"+GlobalConstants.customer_status);

			status = "2";
		}
		String[][] cons = { { "customer_id", GlobalConstants.customer_id } };
		String[][] data_editcust = { { "status", status },
				{ "latitude", String.valueOf(GpsSettings.LATITUDE) },
				{ "longitude", String.valueOf(GpsSettings.LONGITUDE) } };

		//Log.e("customer_id", GlobalConstants.customer_id);
		//Log.e("latitude", String.valueOf(GpsSettings.LATITUDE));
		//Log.e("longitude", String.valueOf(GpsSettings.LONGITUDE));
		mySQlAdapter.update(data_editcust, "survey_customerdetails", cons);*/

		/*
		 * String[][] rdata = { { "customer_id", Survey.customer_id }, {
		 * "surveyset_id", "" + Survey.surveyset_id }, { "device_id", "" +
		 * device_id } };
		 * 
		 * //Log.v("Survey.customer_id",Survey.customer_id);
		 * 
		 * mySQlAdapter.insert(rdata, "survey_result_master");
		 */

		String survey_id = "";
		
		// get the last inserted ID

		String[] columns = new String[] { "survey_id" };
		Cursor cursor = mySQlAdapter.queueAll("survey_result_master", columns,
				"survey_id desc limit 1", null);

		cursor.moveToPosition(0);
		while (cursor.isAfterLast() == false) {
			survey_id = cursor.getString(0);
			cursor.moveToNext();
		}
		cursor.close();

		if (survey_id.equals("")) {
			//Log.v("","wwewewe");
			//Log.v("","wwewewe 1"+Survey.customer_id );
			//Log.v("","wwewewe 2"+Survey.surveyset_id );

			 String[][] rdata = { { "customer_id", Survey.customer_id }, {
			"surveyset_id", "" + Survey.surveyset_id }, { "device_id", "" +
				  device_id } };
				  

				  mySQlAdapter.insert(rdata, "survey_result_master");
				  
				  String[] columnsurvey = new String[] { "survey_id" };
					Cursor scursor = mySQlAdapter.queueAll("survey_result_master", columnsurvey,
							"survey_id desc limit 1", null);

					scursor.moveToPosition(0);
					while (scursor.isAfterLast() == false) {
						survey_id = scursor.getString(0);
						scursor.moveToNext();
					}
					scursor.close();
		}
		
		String s_id=null;
		String survey_keyid=null;
		String[] resultdata=new String[]{"surveyset_id","survey_id"};
		//String[] resultdata=new String[]{"surveyset_id","survey_id"};
		//Log.v("","Survey ID 1-------->"+Survey.customer_id);

		Cursor resultCursor=mySQlAdapter.queueAll("survey_result", resultdata,
				null, "customer_id="+Survey.customer_id);
		resultCursor.moveToPosition(0);
		while (resultCursor.isAfterLast() == false) {
			s_id = resultCursor.getString(0);
			survey_keyid=resultCursor.getString(1);
			//Log.v("","Surveyset ID-------->"+s_id);
			//Log.v("","Survey ID-------->"+survey_keyid);


			resultCursor.moveToNext();
		}
		resultCursor.close();
		
		
	
		for (int i = 0; i < Survey.surveyAnswers.size(); i++) {

			save_answer = Survey.surveyAnswers.get(i);
			s_question = Survey.surveyQuestions.get(save_answer
					.getQuestion_id_position());
			if (s_question.getQuestType().equalsIgnoreCase(UserValues.Q_SRADIO)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_SLIST)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_SLIDER)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_STAR)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_TXT_IP)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_INT_IP)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_DYNAMIC_SUG)
					|| s_question.getQuestType().equalsIgnoreCase(
							UserValues.Q_PRODUCT_FEEDBACK)||s_question.getQuestType().equalsIgnoreCase(
									UserValues.Q_TABULAR)||
							s_question.getQuestType().equalsIgnoreCase(UserValues.Q_IMAGE_SELECTION)) {
				String[][] data = {
						{ "customer_id", Survey.customer_id },
						{ "survey_question_id",
								"" + save_answer.getQuestionId() },
						{ "survey_id", "" + survey_id },
						{ "surveyset_id", "" + Survey.surveyset_id },
						{ "survey_question", s_question.getQuestionText() },
						{ "survey_answer", save_answer.getSurveyAnswer() },
						{ "survey_time", save_answer.getSurveyTimeStamp() },
						{ "survey_question_type", s_question.getQuestType() },
						{ "device_id", "" + device_id },
						{"userId",AppPreferenceManager.getUserId(context)},
						{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
						{"survey_no",MyApplication.randomNo}};
				
				mySQlAdapter.insert(data, "survey_result");
				String[][] cus={{"customer_id",Survey.customer_id}};
				mySQlAdapter.insert(cus, "survey_result_temp");
			
			}
			/*
			 * else if(s_question.getQuestType().equalsIgnoreCase(
			 * UserValues.Q_DYNAMIC_SUG)) { String answer=
			 * Utils.jsonForQ_DYNAMIC_SUG
			 * (context,save_answer.getSurveyAnswer());
			 * 
			 * 
			 * String[][] data = { { "customer_id", Survey.customer_id }, {
			 * "survey_question_id", "" + save_answer.getQuestionId() }, {
			 * "survey_id", "" + survey_id }, { "surveyset_id", "" +
			 * Survey.surveyset_id }, { "survey_question",
			 * s_question.getQuestionText() }, { "survey_answer",
			 * answer.toString() }, { "survey_time",
			 * save_answer.getSurveyTimeStamp() }, { "survey_question_type",
			 * s_question.getQuestType() }, { "device_id", "" + device_id } };
			 * mySQlAdapter.insert(data, "survey_result"); }
			 */

			else if (s_question.getQuestType().equalsIgnoreCase(
					UserValues.Q_ORDER)) {
				String[] save_question_options = Survey.surveyQuestions.get(
						save_answer.getQuestion_id_position())
						.getQuestionOptions();
				String[] save_answers_options = Survey.surveyAnswers.get(i)
						.getSubAnswers();
				// int[] option_ids =
				// Survey.surveyQuestions.get(save_answer.getQuestion_id_position()).getOptionsId();
				int[] option_ids = Survey.surveyAnswers.get(i)
						.getRanked_options();

				int[] ranks = new int[save_answers_options.length];

				for (int m = 0; m < save_answers_options.length; m++) {
					ranks[m] = Integer.parseInt(save_answers_options[m]);
				}

				for (int n = 0; n < ranks.length; n++) {
					for (int p = 0; p < (ranks.length - 1); p++) {
						if (ranks[n] < ranks[p]) {
							int temp = ranks[n];
							int temp_id = option_ids[n];

							ranks[n] = ranks[p];
							option_ids[n] = option_ids[p];

							ranks[p] = temp;
							option_ids[p] = temp_id;
						}
					}
				}
				String survey_answer = "";
				for (int q = 0; q < option_ids.length; q++) {
					survey_answer += option_ids[q] + ",";
				}
				survey_answer = survey_answer.substring(0,
						survey_answer.length() - 1);
				String[][] data = {
						{ "customer_id", Survey.customer_id },
						{ "survey_question_id", "" + s_question.getQuestionId() },
						{ "survey_id", "" + survey_id },
						{ "surveyset_id", "" + Survey.surveyset_id },
						{ "survey_question", s_question.getQuestionText() },
						{ "survey_answer", survey_answer },
						{ "survey_time", save_answer.getSurveyTimeStamp() },
						{ "survey_question_type", s_question.getQuestType() },
						{ "device_id", "" + device_id }, 
						{"userId",AppPreferenceManager.getUserId(context)},
						{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
						{"survey_no",MyApplication.randomNo}};
				
				mySQlAdapter.insert(data, "survey_result");
				String[][] cus={{"customer_id",Survey.customer_id}};
				mySQlAdapter.insert(cus, "survey_result_temp");
				

			} else if (s_question.getQuestType().equalsIgnoreCase(
					UserValues.Q_MATRIX)) {
				String[] save_question_options = Survey.surveyQuestions.get(
						save_answer.getQuestion_id_position()).getSubQuestion();
				int[] save_question_ids = Survey.surveyQuestions.get(
						save_answer.getQuestion_id_position())
						.getSubQuestionsId();
				String[] save_answers_options = Survey.surveyAnswers.get(i)
						.getSubAnswers();

				for (int j = 0; j < save_answers_options.length; j++) {
					// //Log.v("Matrix ID", "" + save_question_ids[j]);
					String[][] data = {
							{ "customer_id", Survey.customer_id },
							{ "survey_question_id", "" + save_question_ids[j] },
							{ "survey_id", "" + survey_id },
							{ "surveyset_id", "" + Survey.surveyset_id },
							{ "survey_question", s_question.getQuestionText() },
							{
									"survey_answer",
									save_answers_options[j] == null ? "Not ranked"
											: save_answers_options[j] },
							{ "survey_time", save_answer.getSurveyTimeStamp() },
							{ "survey_question_type", s_question.getQuestType() },
							{ "survey_question_option",
									save_question_options[j] },
							{ "device_id", "" + device_id },
							{"userId",AppPreferenceManager.getUserId(context)},
							{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
							{"survey_no",MyApplication.randomNo}};
					
					mySQlAdapter.insert(data, "survey_result");
					String[][] cus={{"customer_id",Survey.customer_id}};
					mySQlAdapter.insert(cus, "survey_result_temp");
					
				}
			} else if (s_question.getQuestType().equalsIgnoreCase(
					UserValues.Q_MCHECK)) {
				String[] save_question_options = Survey.surveyQuestions.get(
						save_answer.getQuestion_id_position())
						.getQuestionOptions();
				int[] save_answers_options = Survey.surveyAnswers.get(i)
						.getSubpos();
				String survey_answer = "";
				for (int j = 0; j < save_question_options.length; j++) {
					if (save_answers_options[j] == 1) {
						survey_answer += save_question_options[j] + "|";
					}
				}
				survey_answer = survey_answer.substring(0,
						survey_answer.length() - 1);
				String[][] data = {
						{ "customer_id", Survey.customer_id },
						{ "survey_id", "" + survey_id },
						{ "surveyset_id", "" + Survey.surveyset_id },
						{ "survey_question_id", "" + s_question.getQuestionId() },
						{ "survey_question", s_question.getQuestionText() },
						{ "survey_answer", survey_answer },
						{ "survey_time", save_answer.getSurveyTimeStamp() },
						{ "survey_question_type", s_question.getQuestType() },
						{ "device_id", "" + device_id },
						{"userId",AppPreferenceManager.getUserId(context)},
						{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
						{"survey_no",MyApplication.randomNo}};
				
				mySQlAdapter.insert(data, "survey_result");
				String[][] cus={{"customer_id",Survey.customer_id}};
				mySQlAdapter.insert(cus, "survey_result_temp");
				
			} else if (s_question.getQuestType().equalsIgnoreCase(
					UserValues.Q_PICTURE)) {
				Bitmap bmp = (Bitmap) save_answer.getBitmapPicture();
				try {
					File sdCard = Environment.getExternalStorageDirectory();
					File dir = new File(sdCard.getAbsolutePath()
							+ "/vkc_uploads");
					dir.mkdirs();
					
					File path = new File(dir, Survey.customer_id + "_"
							+ GlobalConstants.DEVICE_ID + "_"
							+ Survey.surveyset_id + ".png");
					FileOutputStream out = new FileOutputStream(path);

					String fileName = Survey.customer_id + "_"
							+ GlobalConstants.DEVICE_ID + "_"
							+ Survey.surveyset_id + ".png";
					bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
					String location=AppPreferenceManager.getLocation(context);

					BitMapToString(bmp);
					String[][] data = {
							{ "customer_id", Survey.customer_id },
							{ "survey_id", "" + survey_id },
							{ "surveyset_id", "" + Survey.surveyset_id },
							{ "survey_question_id",
									"" + s_question.getQuestionId() },
							{ "survey_question", s_question.getQuestionText() },
							{ "filename", fileName.toString() },
							{"image",temp},
							{"survey_answer",PhotoURL+fileName.toString()},
							// { "survey_answer",
							// fileName+","+GlobalConstants.AUDIO_FILENAME },
							// { "survey_answer", "imagejpgANDaudiomp3" },
							{ "survey_time", save_answer.getSurveyTimeStamp() },
							{ "survey_question_type", s_question.getQuestType() },
							{ "device_id", "" + device_id },
							{"location",location},
							{"userId",AppPreferenceManager.getUserId(context)},
							{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
							{"survey_no",MyApplication.randomNo}};
					
					
					mySQlAdapter.insert(data, "survey_result");
					String[][] cus={{"customer_id",Survey.customer_id}};
					mySQlAdapter.insert(cus, "survey_result_temp");
					//Log.v("Filename:", fileName.toString());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (s_question.getQuestType().equalsIgnoreCase(
					UserValues.Q_TABULAR)) {

				String[][] data = {
						{ "customer_id", Survey.customer_id },
						{ "survey_id", "" + survey_id },
						{ "surveyset_id", "" + Survey.surveyset_id },
						{ "survey_question_id", "" + s_question.getQuestionId() },
						{ "survey_question", s_question.getQuestionText() },
						{ "survey_answer", "Tabular" },
						{ "survey_time", save_answer.getSurveyTimeStamp() },
						{ "survey_question_type", s_question.getQuestType() },
						{ "device_id", "" + device_id },
						{"userId",AppPreferenceManager.getUserId(context)},
						{"planDetailId",AppPreferenceManager.getPlanDrtailId(context)},
						{"survey_no",MyApplication.randomNo}};
				
				mySQlAdapter.insert(data, "survey_result");
				String[][] cus={{"customer_id",Survey.customer_id}};
				mySQlAdapter.insert(cus, "survey_result_temp");
				
			}

		}

		// //////////////Audio Question type survey results ///////
		/*
		 * Timestamp currentTimeString = new Timestamp(new Date().getTime());
		 * String[][] data = { { "customer_id", Survey.customer_id }, {
		 * "survey_id", "" + survey_id }, { "surveyset_id", "" +
		 * Survey.surveyset_id }, { "survey_question_id", "-101" }, {
		 * "survey_question", "Audio recorded"}, { "survey_answer",
		 * GlobalConstants.AUDIO_FILEPATH }, { "survey_time",
		 * currentTimeString.toString() }, { "survey_question_type", "20" }, {
		 * "device_id", "" + device_id } }; mySQlAdapter.insert(data,
		 * "survey_result");
		 */
		// //////////////Audio Question type survey results //////
		mySQlAdapter.close();
		return true;
	}
	
	 public String BitMapToString(Bitmap bitmap){
         ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
         byte [] b=ByteStream.toByteArray();
          temp=Base64.encodeToString(b, Base64.DEFAULT);
         return temp;
   }
}
