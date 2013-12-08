package com.example.scheduler;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class WeeklyEventView extends LinearLayout {
	
	/* Views */
	protected LinearLayout weeklyEventBG;
	protected TextView weeklyEventTitle;
	protected TextView weeklyEventStartTime;
	protected TextView weeklyEventEndTime;

	protected Context context;
	
	public WeeklyEventView(Context con, Event e)
	{
		super(con);
		context = con;

		LayoutInflater inflater = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.weekly_event_view, this, true);

		weeklyEventBG = (LinearLayout) findViewById(R.id.WeeklyEventBG);
		weeklyEventTitle = (TextView) findViewById(R.id.WeeklyEventTitle);
		weeklyEventStartTime = (TextView) findViewById(R.id.WeeklyEventStartTime);
		weeklyEventEndTime = (TextView) findViewById(R.id.WeeklyEventEndTime);
		
		setViews(e);
		requestLayout();	
	}
	
	public void setViews(Event e)
	{		
		weeklyEventBG.setBackgroundColor(e.getColor());
		weeklyEventTitle.setText(e.getName());
		
		int startTime = e.GetStart();
		int endTime = e.GetEnd();
		
		if(startTime < 1200)
			weeklyEventStartTime.setText(converted_hr(startTime) + ":" + converted_min(startTime) + "am");
		else {
			
			weeklyEventStartTime.setText(converted_hr(startTime) + ":" + converted_min(startTime) + "pm");
		}
		if(endTime < 1200)
			weeklyEventEndTime.setText(converted_hr(endTime) + ":" + converted_min(endTime) + "am");
		else
			weeklyEventEndTime.setText(converted_hr(endTime) + ":" + converted_min(endTime) + "pm");
	}

	public String converted_hr(int input)
	{
		/* extract hour from time input */
		int time = ((input/100)% 12);
		
		/* reset back to 12 if time is 0 */
		if(time == 0) time = 12;
		return String.valueOf(time);
	}
	
	public String converted_min(int input)
	{
		return digit_test((input % 100));
	}
	
	public String digit_test(int value)
	{
		String s_val;
		if(value < 10)
		{
			s_val = "0"  + value;
			return s_val;
		}
		else
		{
			return String.valueOf(value);
		}
	}
	
}
