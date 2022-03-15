package com.zaimus.manager;

import com.zaimus.Survey.Survey;
import com.zaimus.Survey.SurveyAnswers;

public class QuestionFlowManager {
	public static int getposQuestioninAnswerArray(int questionid)
	{
		int postion = -1 ;
		SurveyAnswers surveyAnswers ;
		for (int i = 0; i < Survey.surveyAnswers.size(); i++) {
			surveyAnswers = Survey.surveyAnswers.get(i);
			if(surveyAnswers.getQuestionId()==questionid)
				return i ;
		}
		return postion ;
	}

}
