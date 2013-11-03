package com.example.scheduler;

/* Library Imports */
import java.util.ArrayList;

import android.widget.ToggleButton;

public class Repetition_Module {
	
	private final static String NO_REP = "NNNNNNN";
	private final static String TRUE_REP = "Y";
	private final static String FALSE_REP = "N";
	private final static int SIZE_OF_ARRAY = 7;
	private final static int Sunday = 6;
	
	/* Data */
	private ArrayList<ToggleButton> RepeatedDays;
	private String RepetitionString;
	
	/* Rep module constructor */
	public Repetition_Module()
	{
		RepetitionString = NO_REP;
		RepeatedDays = new ArrayList<ToggleButton>();
	}
	
	public void clear_Repeated()
	{
		RepeatedDays.clear();
	}
	
	public void add_ToggleButton(ToggleButton e)
	{
		if(RepeatedDays.size() < SIZE_OF_ARRAY) RepeatedDays.add(e);
	}
	
	public String get_RepString()
	{
		if(RepeatedDays.size() < SIZE_OF_ARRAY) return NO_REP;
		
		StringBuilder strBuilder = new StringBuilder();
		String rep_string;
		for(int INDEX = 0; INDEX < SIZE_OF_ARRAY; INDEX++){
			if(RepeatedDays.get(INDEX).isChecked()) strBuilder.append(TRUE_REP);
			else strBuilder.append(FALSE_REP);
		}
		rep_string = strBuilder.toString();
		return rep_string;
	}
	
	public Boolean toggle_Check(int day)
	{
		int strStart, strEnd;
		strStart = day;
		strEnd = strStart + 1;
		
		String nu_time;
		if(day == Sunday) nu_time = RepetitionString.substring(Sunday);
		else  nu_time = RepetitionString.substring(strStart,strEnd);
		
		if(nu_time == TRUE_REP)
		{
			return true;
		}
		return false;
	}
	
	public void set_RepString(String RepString)
	{
		RepetitionString = RepString;
	}
	
	
}
