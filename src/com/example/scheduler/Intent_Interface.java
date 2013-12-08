package com.example.scheduler;

public interface Intent_Interface {
	
	/* Intent Codes */
	final static int month_REQUESTCODE = 1;
	final static int week_REQUESTCODE = 2;
	final static int REQUEST_CANCELLED = -1;
	final static int RESULT_OKAY = 1;
	final static String SCHEDULE_DAY = "S_DAY";
	final static String SCHEDULE_MONTH = "S_MON";
	final static String SCHEDULE_YEAR = "S_YR";
	final static String SELECT_KEY = "CURRENT_DATE";
	final static String SELECT_ID_KEY = "SELECT_ID";
	final static String MONTH_KEY = "MONTH_FROM_WEEK";
	final static String ADD_KEY = "ADD_FROM_WEEK";
}
