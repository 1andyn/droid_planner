package com.example.scheduler;

public interface Time_Interface {
	final static int SUN = 0;
	final static int MON = 1;
	final static int TUE = 2;
	final static int WED = 3;
	final static int THU = 4;
	final static int FRI = 5;
	final static int SAT = 6;
	final static int DAY_OFFSET = 1;
	final static long WEEK_INTERVAL = 7*24*60*60*1000;
	final static int INTERVAL = 7*24*60*60*1000;
	final int MIN_TIME_DIGITS = 3;	
	final static int MIN_DIGITS = 2;
	final static int TEN_MINUTES = 10;
	final static int ZERO = 0;
	final static int SECOND = 1;
	final static int THIRD = 2;
	final static String NO_OVERLAP = "N";
	final static long NONE_L = -1;
	final static String CHECKED = "Y";
	final static int NONE = -1;
}
