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

	private final static String SCHEDULE_DAY = "S_DAY";
	private final static String SCHEDULE_MONTH = "S_MON";
	private final static String SCHEDULE_YEAR = "S_YR";

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
//		setMinAndMaxDate(mYear, mMonth, mDay);			// broken	
	
		cv.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				
//				mYear = year;
//				mMonth = month;
//				mDay = dayOfMonth;
				
				/* 
				String debug_text = "Date: " + Integer.toString(mMonth) + " " + Integer.toString(mDay) + ", " + Integer.toString(mYear);
				Toast.makeText(Month_Activity.this, debug_text, Toast.LENGTH_SHORT).show();
				*/
				
				return_INTENT.putExtra(SCHEDULE_DAY, dayOfMonth);
				return_INTENT.putExtra(SCHEDULE_MONTH, month);
				return_INTENT.putExtra(SCHEDULE_YEAR, year);
				setResult(RESULT_OK, return_INTENT);
				finish();
			}
			
		});
	}
	
//	public void setMinAndMaxDate(int year, int month, int day) {
//		// set lowest date for CalendarView
//		long minDate = year * 100000 + (month - 2) * 100 + day;
//		cv.setMinDate(minDate);
//		
//		// set furthest date for CalendarView
//		long maxDate = (year + 2) * 100000 + month * 100 + day;
//		cv.setMaxDate(maxDate);
//		
//	}
}
