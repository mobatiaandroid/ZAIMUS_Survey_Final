package com.zaimus;
import android.content.Context;
import android.database.Cursor;

import com.zaimus.SQLiteServices.SQLiteAdapter;
import com.zaimus.Survey.Survey;
import com.zaimus.api.IVkcApis;
import com.zaimus.Survey.SurveyQuestions;

public class LoopQuestion implements IVkcApis {
	private Context context ;
	 private SQLiteAdapter sqLiteAdapter;
	public LoopQuestion(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context ;
		sqLiteAdapter = new SQLiteAdapter(this.context);
		sqLiteAdapter.openToRead();
	}
	public LoopQuestion(Context context,SQLiteAdapter sqiLiteAdapter) {
		// TODO Auto-generated constructor stub
		this.context = context ;
		this.sqLiteAdapter = sqiLiteAdapter ;
		
	}
	
	public void createArray()
	{
		surveyquestionCls[] allLoopquestions = findallLoopQuestions();
		surveyquestionCls [] treeqids = null ;
		optionValueCls [] options = null;
		String question_id_optionlink ="";
		for (int i = 0; i < allLoopquestions.length; i++) {
		//	//System.out.println(allLoopquestions);
			if(allLoopquestions[i].questionid.equals(allLoopquestions[i].question_tree_id))
			{
				//System.out.print(allLoopquestions[i].questionid+"  ");
				treeqids = getTreeIdsOrderBy(allLoopquestions[i].questionid);
				for (int j = 0; j < treeqids.length; j++) {
					//System.out.print(treeqids[j]+"-> ");
					options = getAlloptionsOfQuestionid(String.valueOf(treeqids[j].question_tree_id));
					for (int j2 = 0; j2 < options.length; j2++) {
						/// here fill all options .
						
						//question_id_optionlink = findQuestionId(options[j2]);
						
						////System.out.print(options[j2]+"-"+question_id_optionlink +" ");
						////System.out.print(  "-"+question_id_optionlink +" ");
					}
				}
			}
			else
			{
				//System.out.print(allLoopquestions[i].questionid+",");
			}
			
		}
	}
	
	public surveyquestionCls[]  findallLoopQuestions()
	{
		surveyquestionCls question_ids [] = null ; 
		int index = 0 ;

		String coloumns[] ={"question_id","question_tree_id"};
		//Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "question_order asc", "option_link_id=0");
		Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "", "option_link_id=0");
	//	Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "", "");
		cursor.moveToPosition(0);
		question_ids = new surveyquestionCls[cursor.getCount()];
		
		while(!cursor.isAfterLast())
		{		
				question_ids[index] = new surveyquestionCls();
				question_ids[index].questionid = cursor.getString(0);
				question_ids[index].question_tree_id = cursor.getInt(1);
				//System.out.println("question_id " +cursor.getString(0));
				index++;
				cursor.moveToNext();
		}
		cursor.close();
		sqLiteAdapter.close();
		return question_ids;
	}
	
	public String  findQuestionId(String option_link_id)
	{
		String question_id = null ; 
		
		SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(this.context);
		sqLiteAdapter.openToRead();
		String coloumns[] ={"question_id"};
		Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "question_order asc", "option_link_id="+option_link_id);
	//	Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "", "");
		cursor.moveToPosition(0);
		if(!cursor.isAfterLast())
		{
			question_id = cursor.getString(0);	
		}
		cursor.close();
		sqLiteAdapter.close();
		return question_id;
	}
	
	
	
	public optionValueCls[] getAlloptionsOfQuestionid(String questionid)
	{
		optionValueCls option_ids [] = null ; 
		int index = 0 ;
		SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(this.context);
		sqLiteAdapter.openToRead();
		String coloumns[] ={"options_id","option_text"};
		Cursor cursor  = sqLiteAdapter.queueAll("survey_questionoptions", coloumns, "", "question_id="+questionid);
	//	Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "", "");
		cursor.moveToPosition(0);
		option_ids = new optionValueCls[cursor.getCount()];
		while(!cursor.isAfterLast())
		{
				
			option_ids[index] = new optionValueCls();
				option_ids[index].option_id = cursor.getString(0);
				option_ids[index].option_value = cursor.getString(1);
				////System.out.println("question_id " +option_ids[index]);
				index++;
				cursor.moveToNext();
		}
		cursor.close();
		sqLiteAdapter.close();
		return option_ids;
	}
	
	public int  getQuestionid(int selectedoptionid)
	{
		int  option_id  = -1 ; 
		
		SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(this.context);
		sqLiteAdapter.openToRead();
		String coloumns[] ={"question_id"};
		Cursor cursor  = sqLiteAdapter.queueAll("survey_questionoptions", coloumns, "", "options_id="+selectedoptionid);
		
		if(cursor.moveToFirst())
		{
				option_id = Integer.valueOf(cursor.getString(0));	
		}
		cursor.close();
		sqLiteAdapter.close();
		return option_id;
	}
	
	
	
	public  surveyquestionCls[] getTreeIdsOrderBy (String questionid)
	{

		surveyquestionCls tree_ids [] = null ; 
		int index = 0 ;
		SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(this.context);
		sqLiteAdapter.openToRead();
		String coloumns[] ={"question_id","question_tree_id","option_link_id","enum_path","question_type","question_text","master_id"};
		Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "enum_path", "question_tree_id="+questionid);
	//	Cursor cursor  = sqLiteAdapter.queueAll("survey_questions", coloumns, "", "");
		cursor.moveToPosition(0);
		tree_ids = new surveyquestionCls[cursor.getCount()];
		while(!cursor.isAfterLast())
		{	
			tree_ids[index] = new surveyquestionCls();
			tree_ids[index].questionid  = cursor.getString(0);
			tree_ids[index].question_tree_id  = cursor.getInt(1);
			tree_ids[index].option_link_id  = Integer.parseInt(cursor.getString(2));
			tree_ids[index].enum_path  = cursor.getString(3);
			tree_ids[index].question_type  = cursor.getString(4);
			tree_ids[index].question_text  = cursor.getString(5);
			tree_ids[index].master_id=cursor.getString(6);
			//	//System.out.print("question_id_tree  " +tree_ids[index]);
				index++;
				cursor.moveToNext();
		}
		cursor.close();
		sqLiteAdapter.close();
		return tree_ids;
	}
	
	
public int findNextQuestionidpostion(int selectedoptionid)
{
	SurveyQuestions surveyQuestions;
	for (int i = 0; i < Survey.surveyQuestions.size(); i++) {
		surveyQuestions = Survey.surveyQuestions.get(i);
		if(surveyQuestions.getOption_link_id()==selectedoptionid)
			return i;
			
	}
	return -1 ;
}



public int findQuestionposition(int selectedoptionId)
{
	SurveyQuestions surveyQuestions;
	int selected_option_quesitonid  = getQuestionid(selectedoptionId);
	//System.out.println("selected_option_quesitonid" +selected_option_quesitonid);
	for (int i = 0; i < Survey.surveyQuestions.size(); i++) {
		surveyQuestions = Survey.surveyQuestions.get(i);
		
		if(surveyQuestions.getQuestionId()==selected_option_quesitonid)
			return i;
			
	}
	return -1 ;
}

public int findNextQuestionidPositionFromSelectedQuestionid(int currentquestionid)
{
	
	int current_question_position = 0  ;
	int next_question_id = -1 ;
	SurveyQuestions surveyQuestions;
	for (int i = 0; i < Survey.surveyQuestions.size(); i++) {
		surveyQuestions = Survey.surveyQuestions.get(i);
		if(surveyQuestions.getQuestionId()==currentquestionid)
			{
			current_question_position =  i;
			break ;
			}
		
			
	}
	//System.out.println("current_question_position" +current_question_position);
	for (int j = current_question_position; j < Survey.surveyQuestions.size(); j++) {
		surveyQuestions = Survey.surveyQuestions.get(j);
		//System.out.println("option linkid " + surveyQuestions.getOption_link_id() +  "question id "+ surveyQuestions.getQuestionId());
		if(surveyQuestions.getOption_link_id()==-1 && surveyQuestions.getQuestionId()!=currentquestionid)
			{
			next_question_id =  j;
			break ;
			}
		
			
	}
	 
	return next_question_id ;
}

	
public 	class surveyquestionCls 
	{
		public String questionid ;
		public int question_tree_id ;
		public int option_link_id ;
		public String enum_path ;
		public String question_text ;
		public String question_type ;
		public String master_id;
		
	}
public	class optionValueCls
	{
		public String option_id ;
		public String option_value ;
		public int question_id  ;
	}
	
	public void closeDB()
	{
		 sqLiteAdapter.close();
	}

}
