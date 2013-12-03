package com.example.scheduler;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class Month_Activity extends SherlockFragmentActivity implements Intent_Interface {
	
	private CalendarView cal_VIEW;
	private Cal_Module selected_CM;
	private Cal_Date selected_CD;
	private Cal_Date trigger_CD;
	private Intent return_INTENT;
	private int RED = 51;
	private int BLU = 229;
	private int GRE = 182;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		selected_CM = new Cal_Module();
		return_INTENT = new Intent();
		
		Bundle Schedule_Date = getIntent().getExtras();
		selected_CD = Schedule_Date.getParcelable(SELECT_KEY);
		
		initalizeLayout();
		setCalendarView();
	}
	
	protected void initalizeLayout()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calendar_view);
		cal_VIEW = (CalendarView) findViewById(R.id.calendarview);
		cal_VIEW.setSelectedWeekBackgroundColor(Color.rgb(RED, GRE, BLU));
		cal_VIEW.setWeekSeparatorLineColor(Color.LTGRAY);
		cal_VIEW.setWeekNumberColor(Color.WHITE);
		cal_VIEW.setDate(selected_CM.dateToLong(selected_CD));
	}
	
	protected void setCalendarView() 
	{
		cal_VIEW.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int day) {
				if(compareTrigger(month, day, year)){
					return_INTENT.putExtra(SCHEDULE_DAY, day);
					return_INTENT.putExtra(SCHEDULE_MONTH, month);
					return_INTENT.putExtra(SCHEDULE_YEAR, year);
					setResult(RESULT_OK, return_INTENT);
					finish();
				}
			}
			
		});
	}
	
	private boolean compareTrigger(int month, int day, int year)
	{
		trigger_CD = new Cal_Date(month ,day ,year);
		
		if(selected_CD.isEqual(trigger_CD)){
			return false;
		}
		return true;
	}
	
}
