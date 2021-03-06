package com.example.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EventView extends LinearLayout{
	
	/* Primary Event Object */
	private Event event_o;
	
	final int PMCHECK = 1200;	
	final int MIDNIGHT = 12;
	final int START = 0;
	final int END = 1;
	final int TENS_DIGIT = 10;
	
	/* Text view for Start Time*/
	private TextView hr_str;
	private TextView min_str;
	private TextView ampm_tag;
	
	/* Text view for End Time*/
	private TextView ehr_str;
	private TextView emin_str;
	private TextView eampm_tag;

	/* Text view for Event Name/Description */
	private TextView ev_name;
	private TextView des_name;
	
	public EventView (Context con, Event ev)
	{
		super(con);
		LayoutInflater inflater = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.event_view, this, true);
		
		hr_str = (TextView)findViewById(R.id.hr_tview);
		min_str = (TextView)findViewById(R.id.min_tview);
		ampm_tag = (TextView)findViewById(R.id.am_pm);
		
		ehr_str = (TextView)findViewById(R.id.hr_eview);
		emin_str = (TextView)findViewById(R.id.min_eview);
		eampm_tag = (TextView)findViewById(R.id.e_am_pm);

		ev_name = (TextView)findViewById(R.id.t_tview);
		des_name = (TextView)findViewById(R.id.d_tview);
		
		setEvent(ev);
		requestLayout();	
	}
	
	public void setEvent(Event ev)
	{
		event_o = ev;
		ev_name.setText(event_o.getName());
		des_name.setText(event_o.getDescription());
		
		Date temp = event_o.GetDate();
		int s_temp = temp.getStartTime();
		int e_temp = temp.getEndTime();
		
		hr_str.setText(converted_hr(s_temp));
		min_str.setText(converted_min(s_temp));
		
		ehr_str.setText(converted_hr(e_temp));
		emin_str.setText(converted_min(e_temp));
		
		/* Set AM/PM Values*/
		pm_switch(s_temp, START);
		pm_switch(e_temp, END);
	}
	
	public String converted_hr(int input)
	{
		/* extract hour from time input */
		int time = ((input/100)% 12);
		
		/* reset back to 12 if time is 0 */
		if(time == 0) time = MIDNIGHT;
		return String.valueOf(time);
	}
	
	public String converted_min(int input)
	{
		return digit_test((input % 100));
	}
	
	
	public void pm_switch(int time, int flag)
	{
		if(time >= PMCHECK)
		{
			switch(flag)
			{
			case START:
				ampm_tag.setText("PM");
				break;
			case END:
				eampm_tag.setText("PM");
			}
		}
		else
		{
			switch(flag){
			case START:
				ampm_tag.setText("AM");
				break;
			case END:
				eampm_tag.setText("AM");
			}
		}
	}
	
	public String digit_test(int value)
	{
		String s_val;
		if(value < TENS_DIGIT)
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
