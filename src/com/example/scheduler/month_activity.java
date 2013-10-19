package com.example.scheduler;

import java.util.Calendar;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public class Month_Activity extends SherlockFragmentActivity {
	
	private CalendarView cv;
	private int mYear;
	private int mMonth;
	private int mDay;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				mYear = year;
				mMonth = month;
				mDay = dayOfMonth;
				
				String debug_text = "Date: " + Integer.toString(mMonth) + " " + Integer.toString(mDay) + ", " + Integer.toString(mYear);
				Toast.makeText(Month_Activity.this, debug_text, Toast.LENGTH_SHORT).show();
				// launch activity of given date
			}
			
		});
	}
	
	public void setMinAndMaxDate(int year, int month, int day) {
		// set lowest date for CalendarView
		long minDate = year * 100000 + (month - 2) * 100 + day;
		cv.setMinDate(minDate);
		
		// set furthest date for CalendarView
		long maxDate = (year + 2) * 100000 + month * 100 + day;
		cv.setMaxDate(maxDate);
		
	}
}
