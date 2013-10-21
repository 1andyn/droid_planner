package com.example.scheduler;

import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public class Month_Activity extends SherlockFragmentActivity {
	
	private CalendarView cv;
	
	private Intent return_INTENT;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		return_INTENT = new Intent();
		
		initalizeLayout();
		setCalendarView();
	}
	
	protected void initalizeLayout()
	{
		setContentView(R.layout.calendar_view);
	}
	
	protected void setCalendarView() {
		// get current date
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
				
		cv = (CalendarView) findViewById(R.id.calendarview);
		cv.setSelectedWeekBackgroundColor(Color.LTGRAY);
		cv.setWeekSeparatorLineColor(Color.BLACK);
	
		cv.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				
				return_INTENT.putExtra(Schedule.SCHEDULE_DAY, dayOfMonth);
				return_INTENT.putExtra(Schedule.SCHEDULE_MONTH, month);
				return_INTENT.putExtra(Schedule.SCHEDULE_YEAR, year);
				setResult(Schedule.RESULT_OK, return_INTENT);
				finish();
			}
			
		});
	}
}
