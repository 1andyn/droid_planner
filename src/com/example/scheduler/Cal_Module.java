package com.example.scheduler;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Cal_Module{
	
	/* Time Extraction Variables*/
	private final int MIN_TIME_DIGITS = 3;	
	private final static int MIN_DIGITS = 2;
	private final static int TEN_MINUTES = 10;
	private final static int ZERO = 0;
	private final static int SECOND = 1;
	private final static int THIRD = 2;
	
	/* Weekday Enumerations */
	private final static int FAIL = 0;
	private final static int sun = 0;
	private final static int mon = 1;
	private final static int tue = 2;
	private final static int wed = 3;
	private final static int thu = 4;
	private final static int fri = 5;
	private final static int sat = 6;
	private final static int NONE = -1;
	
	private final static int INCREMENT_DATE = 1;
	private final static int DECREMENT_DATE = -1;
	
	private final String E1 = "Error C1";
	private final String E2 = "Error C2";
	
	GregorianCalendar G_Calendar;
	Calendar C_Calendar;
	Cal_Date currentDate;
		
	public Cal_Date getCurrentDate()
	{
		C_Calendar = Calendar.getInstance();
		Cal_Date temp = new Cal_Date();
		temp.set_day(C_Calendar.get(Calendar.DAY_OF_MONTH));
		temp.set_month(C_Calendar.get(Calendar.MONTH));
		temp.set_year(C_Calendar.get(Calendar.YEAR));
		return temp;
	}
	
	public Cal_Module()
	{
		C_Calendar = Calendar.getInstance();
	}
	
	public Cal_Module(Date d)
	{
		C_Calendar = Calendar.getInstance();
		C_Calendar.set(d.getYear(), d.getMonth(), d.getDay());
		int temp = d.getStartTime();
		C_Calendar.set(Calendar.HOUR, extract_HOUR(temp));
		C_Calendar.set(Calendar.MINUTE, extract_MINUTES(temp));
	}
	
	public long getMilliseconds()
	{
		if(C_Calendar != null)
		{
			return C_Calendar.getTimeInMillis();
		}
		else 
		{
			return FAIL;
		}
	}
	
	/* returns weekday as a String */
	public String dayToString(Cal_Date d) 
	{
		G_Calendar = new GregorianCalendar(d.get_year(), d.get_month(), d.get_day());
		switch(G_Calendar.get(Calendar.DAY_OF_WEEK)){
			case 1:	return "SUNDAY";
			case 2: return "MONDAY";
			case 3: return "TUESDAY";
			case 4: return "WEDNESDAY";
			case 5: return "THURSDAY";
			case 6: return "FRIDAY";
			case 7: return "SATURDAY";
			default:return E1;
		}
	}
	public int getWeekday(Cal_Date d) 
	{
		G_Calendar = new GregorianCalendar(d.get_year(), d.get_month(), d.get_day());
		switch(G_Calendar.get(Calendar.DAY_OF_WEEK)){
			case 1:	return sun;
			case 2: return mon;
			case 3: return tue;
			case 4: return wed;
			case 5: return thu;
			case 6: return fri;
			case 7: return sat;
			default: return NONE;
		}
	}
	
	/* Converts interger to month */
	public String monthToString(Cal_Date d)
	{
		switch(d.get_month()){
		case 0:	return "JANUARY";
		case 1: return "FEBRUARY";
		case 2: return "MARCH";
		case 3: return "APRIL";
		case 4: return "MAY";
		case 5: return "JUNE";
		case 6: return "JULY";
		case 7: return "AUGUST";
		case 8: return "SEPTEMBER";
		case 9: return "OCTOBER";
		case 10: return "NOVEMBER";
		case 11: return "DECEMBER";
		default:return E2;
		}
	}
	
	public String DateString(Cal_Date d)
	{
		String date = dayToString(d) + " - " + monthToString(d) + " " +
				d.dateToString(); 
		return date;
	}
	
	public Long dateToLong(Cal_Date d)
	{
		C_Calendar = Calendar.getInstance();
		C_Calendar.set(d.get_year(), d.get_month(), d.get_day());
		return C_Calendar.getTimeInMillis();
	}
	
	public void transitionNextDate(Cal_Date d)
	{
		C_Calendar.set(d.get_year(), d.get_month(), d.get_day());
		C_Calendar.add(Calendar.DATE, INCREMENT_DATE);
		d.set_day(C_Calendar.get(Calendar.DAY_OF_MONTH));
		d.set_month(C_Calendar.get(Calendar.MONTH));
		d.set_year(C_Calendar.get(Calendar.YEAR));
	}
	
	public void transitionPrevDate(Cal_Date d)
	{
		C_Calendar.set(d.get_year(), d.get_month(), d.get_day());
		C_Calendar.add(Calendar.DATE, DECREMENT_DATE);
		d.set_day(C_Calendar.get(Calendar.DAY_OF_MONTH));
		d.set_month(C_Calendar.get(Calendar.MONTH));
		d.set_year(C_Calendar.get(Calendar.YEAR));
	}
	
	protected int extract_MINUTES(int time)
	{
		String my_time = "" + time;
		String nu_time = my_time.length() > MIN_DIGITS ? 
				my_time.substring(my_time.length() - MIN_DIGITS) : my_time;
		return Integer.parseInt(nu_time);
	}
	
	protected int extract_HOUR(int time)
	{		
		String my_time = "" + time;
		if(my_time.length() <= MIN_DIGITS){
			return ZERO;
		} else if(my_time.length() == MIN_TIME_DIGITS){
			String nu_time = my_time.substring(ZERO, SECOND);
			return Integer.parseInt(nu_time);
		} else {
			String nu_time = my_time.substring(ZERO, THIRD);
			return Integer.parseInt(nu_time);
		}	
	}
	
}