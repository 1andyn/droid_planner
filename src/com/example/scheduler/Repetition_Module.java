package com.example.scheduler;

/* Library Imports */
import java.util.ArrayList;
import java.util.List;

import android.widget.ToggleButton;

public class Repetition_Module {
	
	private final static String NO_REP = "NNNNNNN";
	private final static String TRUE_REP = "Y";
	private final static String FALSE_REP = "N";
	private final static int SIZE_OF_ARRAY = 7;
	private final static int Saturday = 6;
	
	/* Data */
	private ArrayList<ToggleButton> RepeatedDays;
	private String RepetitionString;
	
	/* Rep module constructor */
	public Repetition_Module()
	{
		RepetitionString = NO_REP;
		RepeatedDays = new ArrayList<ToggleButton>();
	}
	
	public Repetition_Module(String rep)
	{
		RepetitionString = rep;
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
		if(day == Saturday) nu_time = RepetitionString.substring(Saturday);
		else nu_time = RepetitionString.substring(strStart,strEnd);
		
		if(nu_time.equals(TRUE_REP)){
			return true;
		}
		return false;
	}
	
	public void set_RepString(String RepString)
	{
		RepetitionString = RepString;
	}
	
	public List<Integer> get_RepArray()
	{
		List<Integer> temp = new ArrayList<Integer>();
		
		for(int x = 0; x < 7; x++){
			String nu_time;
			if(x == Saturday){
				nu_time = RepetitionString.substring(Saturday);
			} else {
				nu_time = RepetitionString.substring(x,x+1);
			}
			if(nu_time.equals(TRUE_REP)){
				temp.add(x);
			}
		}
		return temp;
	}
	
	
}
