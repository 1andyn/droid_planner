package com.example.scheduler;

import java.util.GregorianCalendar;

public class Mod_Calendar extends GregorianCalendar {
	
	private final String E1 = "Error C1";
	private final String E2 = "Error C2";
	
	/* returns weekday as an integer */
	public String get_day(int m, int d, int y) 
	{
		GregorianCalendar c = new GregorianCalendar(y, m, d);
		switch(c.get(c.DAY_OF_WEEK)){
			case 1:	return "Sunday";
			case 2: return "Monday";
			case 3: return "Tuesday";
			case 4: return "Wednesday";
			case 5: return "Thursday";
			case 6: return "Friday";
			case 7: return "Sunday";
			default:return E1;
		}
	}
	
	/* Converts interger to month */
	public String get_mon(int m)
	{
		switch(m){
		case 1:	return "January";
		case 2: return "February";
		case 3: return "March";
		case 4: return "April";
		case 5: return "May";
		case 6: return "June";
		case 7: return "July";
		case 8: return "August";
		case 9: return "September";
		case 10: return "October";
		case 11: return "Novemeber";
		case 12: return "December";
		default:return E2;
	}
	}
	
}