package com.example.scheduler;

public class SQL_Data {
	
	/* Class used for Getting Email Data*/
	private final Email_Fetcher e_f = new Email_Fetcher();
	private String identifier;
	
	private String user;
	private long counter; /* Used as Index of Database */
	private long id;
	private String name;
	private String desc;
	private int alarm;
	private int month;
	private int day;
	private int year;
	private int start_time;
	private int end_time;
	
}
