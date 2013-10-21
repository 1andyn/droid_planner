package com.example.scheduler;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Cal_Module{
	
	private final String E1 = "Error C1";
	private final String E2 = "Error C2";
	
	GregorianCalendar G_Calendar;
	Calendar C_Calendar = Calendar.getInstance();
	Cal_Date currentDate;
	
	public Cal_Date getCurrentDate()
	{
		Cal_Date temp = new Cal_Date();
		temp.set_day(C_Calendar.get(Calendar.DAY_OF_MONTH));
		temp.set_month(C_Calendar.get(Calendar.MONTH));
		temp.set_year(C_Calendar.get(Calendar.YEAR));
		return temp;
	}
	
	
	/* returns weekday as an integer */
	public String dayToString(Cal_Date d) 
	{
		G_Calendar = new GregorianCalendar(d.get_year(), d.get_month(), d.get_day());
		switch(G_Calendar.get(G_Calendar.DAY_OF_WEEK)){
			case 1:	return "SUN";
			case 2: return "MON";
			case 3: return "TUES";
			case 4: return "WED";
			case 5: return "THR";
			case 6: return "FRI";
			case 7: return "SAT";
			default:return E1;
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
	
}