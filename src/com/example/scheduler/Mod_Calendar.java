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
	public String get_mon(int m)
	{
		switch(m){
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