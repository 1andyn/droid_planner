package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeeklyToDoView extends LinearLayout{
	
	/* Views */
	protected LinearLayout weeklyToDoBG;
	protected TextView weeklyToDoTitle;
	protected TextView weeklyToDoEndTime;

	protected Context context;
	
	public WeeklyToDoView(Context con, Event e)
	{
		super(con);
		context = con;

		LayoutInflater inflater = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.weekly_todo_view, this, true);

		weeklyToDoBG = (LinearLayout) findViewById(R.id.WeeklyToDoBG);
		weeklyToDoTitle = (TextView) findViewById(R.id.WeeklyToDoTitle);
		weeklyToDoEndTime = (TextView) findViewById(R.id.WeeklyToDoEndTime);
		
		setViews(e);
		requestLayout();	
	}
	
	public void setViews(Event e)
	{		
		weeklyToDoBG.setBackgroundColor(e.getColor());
		weeklyToDoTitle.setText(e.getName());
		
		int endTime = e.GetEnd();
		
		if(endTime < 1200)
			weeklyToDoEndTime.setText(converted_hr(endTime) + ":" + converted_min(endTime) + "am");
		else
			weeklyToDoEndTime.setText(converted_hr(endTime) + ":" + converted_min(endTime) + "pm");
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
