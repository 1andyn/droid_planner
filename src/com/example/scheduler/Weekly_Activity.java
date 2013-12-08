package com.example.scheduler;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Weekly_Activity extends SherlockFragmentActivity implements Intent_Interface{
	
    /* ENUMERATION FOR TD/EVENT */
    final static int EVENT_CASE = 0;
    final static int TODO_CASE = 1;
    final static int MONTH_CASE = 2;
    final static int EDT_TODO_CASE = 3;
    final static int EDT_EVENT_CASE = 4;
    final static int SCHEDULE_CASE = 5;
    
    /* TO DO SIZE CASES */
    final static int EMPTY = 0;
    final static int NONE = -1;
    final static long NONE_L = -1;
	
	/*SQL Data Source */
	protected SQL_DataSource datasource;
	
	/*Layout data*/
	protected TextView sunDate;
	protected TextView monDate;
	protected TextView tueDate;
	protected TextView wedDate;
	protected TextView thuDate;
	protected TextView friDate;
	protected TextView satDate;
	
	protected LinearLayout sunEvents;
	protected LinearLayout monEvents;
	protected LinearLayout tueEvents;
	protected LinearLayout wedEvents;
	protected LinearLayout thuEvents;
	protected LinearLayout friEvents;
	protected LinearLayout satEvents;
	
	protected ViewStub sunEmpty;
	protected ViewStub monEmpty;
	protected ViewStub tueEmpty;
	protected ViewStub wedEmpty;
	protected ViewStub thuEmpty;
	protected ViewStub friEmpty;
	protected ViewStub satEmpty;

	/* Date data */
	protected Cal_Date givenDate;
	protected Cal_Module CM;
	protected int givenDay;			
	protected int givenMonth; 
	protected int givenYear;
	protected int dayOfWeek;
	protected int prevMonthDays;
	protected int currMonthDays;
	
	protected Cal_Date[] weekDates;
	protected View dividerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		
		/* SQL Source */
		datasource = new SQL_DataSource(this);
		datasource.open();
		
		// data
		Bundle Schedule_Date = getIntent().getExtras();
		givenDate = (Cal_Date) Schedule_Date.get(SELECT_KEY);
		CM = new Cal_Module();
		
		weekDates = new Cal_Date[7];
		for(int x = 0; x < 7; x++) {
			weekDates[x] = new Cal_Date();
		}
		initialize_week();
	}
	
	private void init_today()
	{
		Cal_Module Cal = new Cal_Module();
		Cal_Date temp = Cal.getCurrentDate();
		givenDate.set_day(temp.get_day());
		givenDate.set_month(temp.get_month());
		givenDate.set_year(temp.get_year());
		invalidateOptionsMenu();
		initialize_week();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != REQUEST_CANCELLED && resultCode == RESULT_OKAY){
			if(requestCode == month_REQUESTCODE){
				givenDate.set_day(data.getIntExtra(SCHEDULE_DAY, EMPTY));
				givenDate.set_month(data.getIntExtra(SCHEDULE_MONTH, EMPTY));
				givenDate.set_year(data.getIntExtra(SCHEDULE_YEAR, EMPTY));
				invalidateOptionsMenu();
			}
		}
	}
	
	private void initialize_week()
	{
		check_date();
		load_days();
		load_views();
		load_events_and_todos();
		initListeners();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.weekly_topbar, menu);
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu)
	{
		MenuItem date_text = menu.findItem(R.id.wtb_date);
		date_text.setTitle(DateString(weekDates[0]) + " - " + DateString(weekDates[6]));
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    int id = item.getItemId();
	    switch(id)
	    {		    
		    case R.id.wtb_month: {
		    	switch_activity(MONTH_CASE, NONE);
		    	return false;
		    } case R.id.wtb_date: {
		    	init_today();
		    	return false;
		    } case R.id.wtb_sub_da: {
		    	switch_activity(SCHEDULE_CASE, NONE);
		    	return false;
		    } case R.id.wtb_sub_ev: {
	    		switch_activity(EVENT_CASE, NONE);
	    		return false;
	    	} case R.id.wtb_sub_td: {
	    		switch_activity(TODO_CASE, NONE);
	    		return false;
	    	} case R.id.full_clear: {
	    		clear_everything();
	    		return false;
	    	} default: {
		        return super.onOptionsItemSelected(item);
		    }
	    }
	}
	
	
	protected void switch_activity(int USR_CASE, long id)
	{
		switch(USR_CASE)
		{
			case EVENT_CASE: {
				Intent event_INTENT = new Intent(this, Add_Activity.class);
				event_INTENT.putExtra(SELECT_KEY, givenDate);
				event_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				event_INTENT.putExtra(ADD_KEY, NONE);
				startActivity(event_INTENT);
				// start activity of event's date
				break;
			} case TODO_CASE: {
				Intent todo_INTENT = new Intent(this, TD_Add_Activity.class);
				todo_INTENT.putExtra(SELECT_KEY, givenDate);
				todo_INTENT.putExtra(SELECT_ID_KEY, (long)NONE_L);
				todo_INTENT.putExtra(ADD_KEY, NONE);
				startActivity(todo_INTENT);
				// start activity of todo's date
				break;
			} case MONTH_CASE: {
				Intent month_INTENT = new Intent(this, Month_Activity.class);
				month_INTENT.putExtra(SELECT_KEY, givenDate);
				startActivityForResult(month_INTENT, month_REQUESTCODE);
				initialize_week();
				break;
			} case SCHEDULE_CASE: {
				finish();
				break;
			}
		}
	}
	
	@Override
	protected void onResume()
	{
		datasource.open();
		load_from_database(givenDate);
		super.onResume();
	}
	
	@Override	
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}
	
	
	protected void initLayout()
	{
		setContentView(R.layout.weekly_view);
		
		sunDate = (TextView)findViewById(R.id.SundayDate);
		monDate = (TextView)findViewById(R.id.MondayDate);
		tueDate = (TextView)findViewById(R.id.TuesdayDate);
		wedDate = (TextView)findViewById(R.id.WednesdayDate);
		thuDate = (TextView)findViewById(R.id.ThursdayDate);
		friDate = (TextView)findViewById(R.id.FridayDate);
		satDate = (TextView)findViewById(R.id.SaturdayDate);
		
		sunEvents = (LinearLayout)findViewById(R.id.SundayEvents);
		monEvents = (LinearLayout)findViewById(R.id.MondayEvents);
		tueEvents = (LinearLayout)findViewById(R.id.TuesdayEvents);
		wedEvents = (LinearLayout)findViewById(R.id.WednesdayEvents);
		thuEvents = (LinearLayout)findViewById(R.id.ThursdayEvents);
		friEvents = (LinearLayout)findViewById(R.id.FridayEvents);
		satEvents = (LinearLayout)findViewById(R.id.SaturdayEvents);
		
		sunEmpty = (ViewStub)findViewById(R.id.SundayEmpty);
		monEmpty = (ViewStub)findViewById(R.id.MondayEmpty);
		tueEmpty = (ViewStub)findViewById(R.id.TuesdayEmpty);
		wedEmpty = (ViewStub)findViewById(R.id.WednesdayEmpty);
		thuEmpty = (ViewStub)findViewById(R.id.ThursdayEmpty);
		friEmpty = (ViewStub)findViewById(R.id.FridayEmpty);
		satEmpty = (ViewStub)findViewById(R.id.SaturdayEmpty);
		
		// trying to make divider, does not work
		dividerView = new TextView(this);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
		dividerView.setLayoutParams(params);
		dividerView.setBackgroundColor(Color.BLACK);
		
		config_actionbar();
	}
	
	protected void initListeners() {
		// listener for sunday's date
		View.OnClickListener sunListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[0].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[0].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[0].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		sunEvents.setOnClickListener(sunListener);
		sunEmpty.setOnClickListener(sunListener);
		
		// listener for monday's date
		View.OnClickListener monListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[1].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[1].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[1].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		monEvents.setOnClickListener(monListener);
		monEmpty.setOnClickListener(monListener);

		// listener for tuesday's date
		View.OnClickListener tueListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[2].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[2].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[2].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		tueEvents.setOnClickListener(tueListener);
		tueEmpty.setOnClickListener(tueListener);

		// listener for wednesday's date
		View.OnClickListener wedListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[3].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[3].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[3].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
				
			}
		};
		wedEvents.setOnClickListener(wedListener);
		wedEmpty.setOnClickListener(wedListener);

		// listener for thursday's date
		View.OnClickListener thuListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[4].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[4].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[4].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		thuEvents.setOnClickListener(thuListener);
		thuEmpty.setOnClickListener(thuListener);

		// listener for friday's date
		View.OnClickListener friListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[5].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[5].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[5].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		friEvents.setOnClickListener(friListener);
		friEmpty.setOnClickListener(friListener);

		// listener for saturday's date
		View.OnClickListener satListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent schedule_INTENT = new Intent(Weekly_Activity.this, Schedule.class);
				schedule_INTENT.putExtra(SCHEDULE_MONTH, weekDates[6].get_month());
				schedule_INTENT.putExtra(SCHEDULE_DAY, weekDates[6].get_day());
				schedule_INTENT.putExtra(SCHEDULE_YEAR, weekDates[6].get_year());
				setResult(RESULT_OKAY, schedule_INTENT);
				finish();
			}
		};
		satEvents.setOnClickListener(satListener);
		satEmpty.setOnClickListener(satListener);
		
	}
	
	/* ActionBar Configuration */
	protected void config_actionbar()
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		
		/* Will re-implement last
		LayoutInflater inflater=(LayoutInflater) main_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v =inflater.inflate(R.layout.top_bar_view, null, true);
	    ab.setCustomView(v);
		ab.setDisplayShowCustomEnabled(true);
		
		*/
	}
	
	protected void check_date() {
		Calendar temp = Calendar.getInstance();
		
		temp.set(givenDate.get_year(), givenDate.get_month(),givenDate.get_day());

		dayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
		givenMonth = givenDate.get_month(); // month starts from 0
		givenDay = givenDate.get_day();
		givenYear = givenDate.get_year();
		
		switch(givenMonth) {
		case 0: currMonthDays = 31; prevMonthDays = 31; break;
		case 1: prevMonthDays = 31;
								if(givenYear % 4 == 0) currMonthDays = 29; // leap year
								else currMonthDays = 28;
								break;
		case 2: currMonthDays = 31;								
							 if(givenYear % 4 == 0) prevMonthDays = 29; // leap year
							 else prevMonthDays = 28;
							 break;
		case 3: currMonthDays = 30; prevMonthDays = 31; break;
		case 4: currMonthDays = 31; prevMonthDays = 30; break;
		case 5: currMonthDays = 30; prevMonthDays = 31; break;
		case 6: currMonthDays = 31; prevMonthDays = 30; break;
		case 7: currMonthDays = 31; prevMonthDays = 31; break;
		case 8: currMonthDays = 30; prevMonthDays = 31; break;
		case 9: currMonthDays = 31; prevMonthDays = 30; break;
		case 10: currMonthDays = 30; prevMonthDays = 31; break;
		case 11: currMonthDays = 31; prevMonthDays = 30; break;
		default: break;
		}
		
	}
	
	protected void load_days() {
		
		int DoW = 0;
		
		switch(dayOfWeek){
		case Calendar.SUNDAY: DoW = 0; break;
		case Calendar.MONDAY: DoW = 1; break;
		case Calendar.TUESDAY: DoW = 2; break;
		case Calendar.WEDNESDAY: DoW = 3; break;
		case Calendar.THURSDAY: DoW = 4; break;
		case Calendar.FRIDAY: DoW = 5; break;
		case Calendar.SATURDAY: DoW = 6; break;
		default: DoW = -1; break;
		}
	
		int day = givenDay - DoW;
		int month = givenMonth; 
		int year = givenYear;
		int monthDays = currMonthDays;
		
		for(int x = 0; x < 7; x++) {
			
			if(day <= 0) { 
				if(month == 1) {
					month = 12;
					year--;
				}
				
				else month--;
				
				monthDays = prevMonthDays;
				day = monthDays + day;
			}

			else if(day > monthDays) {
				day = 1;
					
				if(month == 12) {
					month = 1;
					year++;
				}
					
				else month++;
			}

			
			weekDates[x].set_day(day);
			weekDates[x].set_month(month);
			weekDates[x].set_year(year);
			day++;
		}
		
	}
	
	protected void load_views() {
		
		sunDate.setText("Sunday, " + DateString(weekDates[0]));
		monDate.setText("Monday, " + DateString(weekDates[1]));
		tueDate.setText("Tuesday, " + DateString(weekDates[2]));
		wedDate.setText("Wednesday, " + DateString(weekDates[3]));
		thuDate.setText("Thursday, " + DateString(weekDates[4]));
		friDate.setText("Friday, " + DateString(weekDates[5]));
		satDate.setText("Saturday, " + DateString(weekDates[6]));
	}
	
	protected void load_events_and_todos() {
		
		for(int x = 0; x < 7; x++) {
			ArrayList<Event> temp = datasource.getEventsForDate(weekDates[x]);

			if(temp.size() == 0) {
				addEmptyView(x);
			}

			for(int a = 0; a < temp.size(); a++) {
				
				Event e = temp.get(a);
				
				if(e.GetStart() == NONE) {
					WeeklyToDoView wtdv = new WeeklyToDoView(this, e);
					addViewToEvent(x, wtdv);
				}
				
				else {
					WeeklyEventView wev = new WeeklyEventView(this, e);
					addViewToEvent(x, wev);
				}
			}
		}

	}

	
	protected void addEmptyView(int day) {
		
		switch(day) {
		case 0: sunEmpty.setVisibility(View.VISIBLE); break;
		case 1: monEmpty.setVisibility(View.VISIBLE); break;
		case 2: tueEmpty.setVisibility(View.VISIBLE); break;
		case 3: wedEmpty.setVisibility(View.VISIBLE); break;
		case 4: thuEmpty.setVisibility(View.VISIBLE); break;
		case 5: friEmpty.setVisibility(View.VISIBLE); break;
		case 6: satEmpty.setVisibility(View.VISIBLE); break;
		default: break;
		}
	}
	
	protected void addViewToEvent(int day, WeeklyEventView wev) {
		
		switch(day) {
		case 0: sunEvents.addView(wev); break;
		case 1: monEvents.addView(wev); break;
		case 2: tueEvents.addView(wev); break;
		case 3: wedEvents.addView(wev); break;
		case 4: thuEvents.addView(wev); break;
		case 5: friEvents.addView(wev); break;
		case 6: satEvents.addView(wev); break;
		default: break;
		}
	}
	
	protected void addViewToEvent(int day, WeeklyToDoView wtdv) {
		switch(day) {
		case 0: sunEvents.addView(wtdv); break;
		case 1: monEvents.addView(wtdv); break;
		case 2: tueEvents.addView(wtdv); break;
		case 3: wedEvents.addView(wtdv); break;
		case 4: thuEvents.addView(wtdv); break;
		case 5: friEvents.addView(wtdv); break;
		case 6: satEvents.addView(wtdv); break;
		default: break;
		}
	}
	
	protected void clear_everything() {
		sunEvents.removeAllViews();
		monEvents.removeAllViews();
		tueEvents.removeAllViews();
		wedEvents.removeAllViews();
		thuEvents.removeAllViews();
		friEvents.removeAllViews();
		satEvents.removeAllViews();
	}
	
	protected void load_from_database(Cal_Date d)
	{
		clear_everything();
		check_date();
		load_days();
		load_views();
		load_events_and_todos();
	}
	
	protected String DateString(Cal_Date cd) {
		return month_string(cd.get_month()) 
				+ " " + cd.get_day() + ", " + cd.get_year();
	}
	
	protected String month_string(int month) {
		
		switch(month) {
			case 0: { 
				if(isNormalOrSmallResolution()){
					return "Jan";
				} else {
					return "January";
				} 
			} case 1: {
				if(isNormalOrSmallResolution()){
					return "Feb";
				} else {
					return "February";
				} 
			}case 2: {
				if(isNormalOrSmallResolution()){
					return "Mar";
				} else {
					return "March";
				}
			} case 3: {
				if(isNormalOrSmallResolution()){
					return "Apr";
				} else {
					return "April";
				}
			}
			case 4: return "May";
			case 5: return "June";
			case 6: return "July";
			case 7:  {
				if(isNormalOrSmallResolution()){
					return "Aug";
				} else {
					return "August";
				}
			} case 8: {
				if(isNormalOrSmallResolution()){
					return "Sept";
				} else {
					return "September";
				}
			} case 9: {
				if(isNormalOrSmallResolution()){
					return "Oct";
				} else {
					return "October";
				}
			} case 10:  {
				if(isNormalOrSmallResolution()){
					return "Nov";
				} else {
					return "November";
				}
			} case 11: {
				if(isNormalOrSmallResolution()){
					return "Dec";
				} else {
					return "December";
				}
			} default: return null;
		}
	}
	
	private boolean isNormalOrSmallResolution() {
	    boolean isNormalOrSmallResolution = false;
	    if (this != null) {
	        if (((this.getResources().getConfiguration().screenLayout & 
	        		Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
	                || ((this.getResources().getConfiguration().screenLayout & 
	                		Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)) {
	            isNormalOrSmallResolution = true;
	        }
	    }
	    return isNormalOrSmallResolution;
	}
	
}
