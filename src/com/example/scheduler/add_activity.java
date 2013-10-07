package com.example.scheduler;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class Add_Activity  extends SherlockFragmentActivity {
	/* Necessary Data for Resources */
	protected EditText name_et;
	protected EditText desc_et;
	
	/* Time Based Data Resources*/
	protected TimePicker start_tp;
	protected TimePicker end_tp;
	protected DatePicker r_dp;
	
	/* Button Based Resources*/
	protected ToggleButton alarm_tb;
	protected Button creation_b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initalizeLayout();
		
	}
	
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.creation_activity);
		config_resources();
		//config_actionbar();
	}
	
	protected void config_resources()
	{
		name_et = (EditText) findViewById(R.id.et_name);
		desc_et = (EditText) findViewById(R.id.et_desc);
		start_tp = (TimePicker) findViewById(R.id.time_start);
		end_tp = (TimePicker) findViewById(R.id.time_end);
		r_dp = (DatePicker) findViewById(R.id.date_sel);
		alarm_tb = (ToggleButton) findViewById(R.id.ce_alarm);
		creation_b = (Button) findViewById(R.id.ce_create); 
		
		final Calendar c = Calendar.getInstance();
		/* Set Default End Time to 1 hr ahead of current Time if it doesn't Pass into Next Day */
		if(c.get(Calendar.HOUR_OF_DAY) < 23)
		{
			end_tp.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
		}
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
		/*
		 *  Functionality is not necessary but may make app look nicer later on
		 * 
		 * ActionBar ab = getSupportActionBar();
		 * ab.setDisplayShowTitleEnabled(false); 
		 * ab.setDisplayShowHomeEnabled(false);
		 * LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     * View v =inflater.inflate(R.layout.top_bar_view, null, true);
	     * ab.setCustomView(v);
		 * ab.setDisplayShowCustomEnabled(true);
		*/
	}
	
	protected void initAddJokeListeners() {
		creation_b.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View view) {
				/* Checks for Empty name as it is a Requirement */
			if(name_et.getText().toString().trim().length() > 0)
			{
				add_event();
			}
			else
			{
				
			}
		  } 
		}
	); 
	}
	

	protected void add_event()
	{
		/* Actually we might not even need to Create an Event since the Data is stored directly into SQLDB*/
		Event temp = new Event();
		Date time = new Date();
		temp.setName(name_et.getText().toString());
		temp.setDescription(desc_et.getText().toString());
		
		int s_hour = start_tp.getCurrentHour();
		int s_min = start_tp.getCurrentMinute();
		String start = "" + s_hour + s_min;
		time.setStartTime(Integer.parseInt(start));
		
		int e_hour = end_tp.getCurrentHour();
		int e_min = end_tp.getCurrentMinute();
		String end = "" + e_hour + e_min;
		time.setEndTime(Integer.parseInt(end));
		
		time.setDay(r_dp.getDayOfMonth());
		time.setMth(r_dp.getMonth());
		time.setYr(r_dp.getYear());
		
		/* Some Code here to Write to SQL Database*/
	}
	


}
