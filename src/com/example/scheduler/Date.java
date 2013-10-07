package com.example.scheduler;

public class Date{

	private int startTime; /* military time */
	private int endTime;
	private Cal_Date date;
	
	private static final int NONE = -1;
	
	/* Default Constructor */
	public Date() {
		startTime = NONE;
		endTime = NONE;
		date = new Cal_Date();
	}
	
	/* only day month year  given */
	public Date(int m, int d, int y) {
		startTime = NONE;
		endTime = NONE;
		date = new Cal_Date(m,d,y);
	}
	
	// all data given
	public Date(int givenStart, int givenEnd, int m, int d, int y) {
		startTime = givenStart;
		endTime = givenEnd;
		date = new Cal_Date(m,d,y);
	}

	// accessors
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public int getDay() {
		return date.get_day();
	}
	
	public int getMonth()
	{
		return date.get_month();
	}
	
	public int getYear()
	{
		return date.get_year();
	}
		
	// mutators
	public void setStartTime(int givenStart) {
		startTime = givenStart;
	}
	
	public void setEndTime(int givenEnd) {
		endTime = givenEnd;
	}
	
	public void setDay(int givenDay) {
		date.set_day(givenDay);
	}
	
	public void setMth(int month)
	{
		date.set_month(month);
	}
	
	public void setYr (int year)
	{
		date.set_year(year);
	}
}
