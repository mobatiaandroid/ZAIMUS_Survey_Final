package com.zaimus.Survey;

public class Rank {
	private String question;
	private int rank;
	private static int over_rank = 0;
	private int  que_option_id ;
	
	public int getQue_option_id() {
		return que_option_id;
	}
	public void setQue_option_id(int que_option_id) {
		this.que_option_id = que_option_id;
	}
	public String getQuestion(){
		return question;
	}
	public Rank(String question, int rank,int optionid){
		this.question = question;
		this.rank = rank;
		this.que_option_id = optionid ;
	}
	public int getRank() {
		return rank;
	}
	
	public static int getOverRank(){
		return over_rank;
	}
	
	public static void incrOverRank(){
		over_rank++;
	}
	
	public static void decrOverRank(){
		over_rank--;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}
	
	public static void setOverrank(int rank){
		over_rank = rank;
	}
}
