package com.example.scheduler;

import java.util.Arrays;

public class Cal_date {
	
	final int SIZE_OF_ARRAY = 3;
	final int MONTH_INDEX = 0;
	final int DATE_INDEX = 1;
	final int YEAR_INDEX = 2;
	final int EMPTY = -1;
	
	private int[] cal_array;
	
	/* Main Constructor */
	public Cal_date()
	{
		cal_array = new int[SIZE_OF_ARRAY];
		Arrays.fill(cal_array, EMPTY); /* Sets all values to EMPTY */
	}
	
	public Cal_date(int m, int d, int y)
	{
		cal_array = new int[SIZE_OF_ARRAY];
		cal_array[MONTH_INDEX] = m;
		cal_array[DATE_INDEX] = d;
		cal_array[YEAR_INDEX] = y;
	}
	
	/* Accessors */ 
	public int get_day()
	{
		return cal_array[DATE_INDEX];
	}
	
	public int get_month()
	{
		return cal_array[MONTH_INDEX];
	}
	
	public int get_year()
	{
		return cal_array[YEAR_INDEX];
	}
	
	/* Mutators */
	public void set_day(int d)
	{
		cal_array[DATE_INDEX] = d;
	}
	public void set_month(int m)
	{
		cal_array[MONTH_INDEX] = m;
	}
	public void set_year(int y)
	{
		cal_array[YEAR_INDEX] = y;
	}
	
	
}
