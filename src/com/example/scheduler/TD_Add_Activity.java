package com.example.scheduler;

/* Cloud Based Imports */
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

/* Color Selector Imports */
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.Calendar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TD_Add_Activity extends SherlockFragmentActivity implements Time_Interface,
	Parse_Interface, PrefKey_Interface{
	
	private SharedPreferences UserPrefs;
	private String identifier;
	
	/* Bundle or Extra Keys */
	private final static String EV_NAME = "event_name";
	private final static String EV_COLR = "event_colr";
	
	private int TODO_TRUE = 1;
	private long EMPTY = 0;
	private long ASEC = 0; 
	
	private SQL_DataSource datasource;
	
	private Cal_Date sel_CD;
	private long b_id;
	
	/* Color Selector Resources */
	protected SVBar svBar;
	protected OpacityBar opBar;
	protected ColorPicker c_Picker;
	
	/* Necessary Data for Resources */
	protected EditText name_et;
	
	/* Time Based Data Resources*/
	protected TimePicker end_tp;
	protected DatePicker r_dp;
	
	/* Button Based Resources*/
	protected ToggleButton alarm_tb;
	protected Button creation_b;
	
	/* Alarm cancel Fix*/
	private String original_Alarm = "N";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initalizeLayout();
		add_Event_Listener();
	}
	
	/* Configures everything Visual*/
	protected void initalizeLayout()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.td_activity);
		config_resources();
		//config_actionbar();
	}
	
	protected void config_resources()
	{
		UserPrefs = getSharedPreferences(app_id, Context.MODE_PRIVATE);
	
		/* SQL Configuration */
		datasource = new SQL_DataSource(this);
		datasource.open();
		
		Bundle Schedule_Date = getIntent().getExtras();
		b_id = Schedule_Date.getLong(Schedule.SELECT_ID_KEY);
		
		retrieve_user();
		
		if(b_id == NONE_L){
			sel_CD = Schedule_Date.getParcelable(Schedule.SELECT_KEY);
		}
		
		/* Layout Configuration */
		name_et = (EditText) findViewById(R.id.et_tdname);
		
		end_tp = (TimePicker) findViewById(R.id.td_end);
		end_tp.setIs24HourView(false);
		
		r_dp = (DatePicker) findViewById(R.id.date_td);
		alarm_tb = (ToggleButton) findViewById(R.id.td_alarm);
		creation_b = (Button) findViewById(R.id.td_create); 
		
		c_Picker = (ColorPicker) findViewById(R.id.picker);
		svBar = (SVBar) findViewById(R.id.svbar);
		opBar = (OpacityBar) findViewById(R.id.opacitybar);
		
		c_Picker.setOldCenterColor(getResources().getColor(R.color.White));
		c_Picker.addOpacityBar(opBar);
		c_Picker.addSVBar(svBar);
		
		/* Set Default End Time to 1 hr ahead of current Time if it doesn't Pass into Next Day */
		final Calendar c = Calendar.getInstance();
		if(c.get(Calendar.HOUR_OF_DAY) < 23){
			end_tp.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
		}
		
		if(b_id == NONE_L){
			r_dp.updateDate(sel_CD.get_year(), sel_CD.get_month(), sel_CD.get_day());
		}
	
		check_EDIT_MODE();
	}
	
	private void retrieve_user()
	{
		identifier = UserPrefs.getString(usr_email, null_email);
	}
	
	private void check_EDIT_MODE()
	{
		if(b_id != NONE_L){
			creation_b.setText(getResources().getString(R.string.edt_ev));
			Event temp = datasource.getEvent(b_id);
			name_et.setText(temp.getName());
			end_tp.setCurrentHour(extract_HOUR(temp.GetEnd()));
			end_tp.setCurrentMinute(extract_MINUTES(temp.GetEnd()));
			r_dp.updateDate(temp.GetYear(), temp.GetMonth(), temp.GetDay());
			if(temp.getAlarm() == CHECKED)alarm_tb.setChecked(true);
			else alarm_tb.setChecked(false);
			c_Picker.setColor(temp.getColor());
			original_Alarm = temp.getAlarm();
			
			/* Acquire Original Alarm Data */
			ASEC = temp.get_Asec();
			
			if(temp.getAlarm().equals(CHECKED)) {
				alarm_tb.setChecked(true);
			} else {
				alarm_tb.setChecked(false);
			}
			
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
	
	protected void add_Event_Listener() {
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
				name_et.requestFocus();
				Toast.makeText(TD_Add_Activity.this,"ToDo cannot have an Empty Name!",
                        Toast.LENGTH_SHORT).show();
			}
		  } 
		}
	); 
	}
	

	protected void add_event()
	{
		Event temp = new Event();
		Date time = new Date();
		temp.setName(name_et.getText().toString());
		temp.setDescription(null);
		
		time.setStartTime(NONE);
		int e_hour = end_tp.getCurrentHour();
		int e_min = end_tp.getCurrentMinute();
		String end = "" + e_hour + minutes(e_min);
		time.setEndTime(Integer.parseInt(end));
		
		time.setDay(r_dp.getDayOfMonth());
		time.setMth(r_dp.getMonth());
		time.setYr(r_dp.getYear());
		
		temp.setAlarm(check_toggle());
		temp.setDate(time);
		
		temp.setColor(c_Picker.getColor());
		
		if(!timeIssues(time, b_id))
		{
			if(b_id != NONE_L)
			{
				datasource.deleteEvent(b_id);
				ParseObject.createWithoutData(parse_class, 
						datasource.acquireObjectId(b_id)).deleteEventually();
				datasource.deleteEventObj(b_id);
			}
			
			/* Configure Alarm only if Checked*/
			if(alarm_tb.isChecked()){
				Cal_Module C_MOD = new Cal_Module(time, TODO_TRUE);
				temp.set_Asec(C_MOD.getMilliseconds());
				C_MOD = null; // Delete CMOD
			}
			
			/* SQL_Database Code */
			long id; // Acquire new ID number
			id = datasource.createEvent(temp).GetID();
			
			/* Configure Alarm*/
			if(alarm_tb.isChecked()){
				construct_Alarm(temp, id);
			} else {
				/* Disable Alarm if was previous triggered*/
				/* Tries to cancel alarm anyways */
					if(original_Alarm.equals(CHECKED)){
						/* Cancel Previous Alarm */
						int newid = safeLongToInt(b_id);
						cancel_Alarm(newid);
					}
				}
			
			/* Save to Parse*/
			temp.setID(id);
			datasource.saveObjectID(id, construct_parse_event(temp));
			
			/* Return to Primary Activity*/
			finish();
		}
	}
	
	protected int extract_MINUTES(int time)
	{
		String my_time = "" + time;
		String nu_time = my_time.length() > MIN_DIGITS ? 
				my_time.substring(my_time.length() - MIN_DIGITS) : my_time;
		return Integer.parseInt(nu_time);
	}
	
	protected int extract_HOUR(int time)
	{		
		String my_time = "" + time;
		if(my_time.length() <= MIN_DIGITS) return ZERO;
		else if(my_time.length() == MIN_TIME_DIGITS){
			String nu_time = my_time.substring(ZERO, SECOND);
			return Integer.parseInt(nu_time);
		}else{
			String nu_time = my_time.substring(ZERO, THIRD);
			return Integer.parseInt(nu_time);
		}	
	}
	
	
	protected String minutes(int min)
	{
		String time;
		if(min < TEN_MINUTES){
			time = "" + ZERO + min;
			return time; 
		}else{
			time = "" + min;
			return time;
		}
	}
	
	protected String check_toggle()
	{
		if(alarm_tb.isChecked()){
			return "Y";
		}else{
			return "N";
		}
	}
	
	@Override
	protected void onResume()
	{
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}
	
	protected boolean timeIssues(Date d, long id)
	{
		if(datasource.endTimeExists(d, id) != NO_OVERLAP){
			focus_Time();
			Toast.makeText(TD_Add_Activity.this,"A ToDo at this time already exists with name: " + 
			datasource.endTimeExists(d ,id),
                    Toast.LENGTH_LONG).show();
			return true;
		}

		return false;
	}
		
	protected void focus_Time()
	{
		end_tp.requestFocus();
	}

	private void construct_Alarm(Event e, long id)
	{
		int newid = safeLongToInt(id);
		/* If Old Alarm Exists */
		if(ASEC != EMPTY){
			cancel_Alarm(newid);
		}
		create_Alarm(e, newid);
	}
	
	private void create_Alarm(Event e, int id)
	{		
	    Intent AlarmIntent = new Intent().setClass(this, Receiver_Module.class);
	    AlarmIntent.setData(Uri.parse("custom://" + id));
	    AlarmIntent.setAction(String.valueOf(id));

	    AlarmIntent.putExtra(EV_NAME, e.getName());
	    AlarmIntent.putExtra(EV_COLR, e.getColor());
	    
	    PendingIntent DispIntent = PendingIntent.getBroadcast(this, id, 
	    		AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    /* Scheduling the Alarm to be triggered*/
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, e.get_Asec(), DispIntent);
	}
	
	private void cancel_Alarm(int id)
	{
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(this, Receiver_Module.class);    
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		AlarmIntent.setData(Uri.parse("custom://" + id));
		AlarmIntent.setAction(String.valueOf(id));
		PendingIntent DispIntent = PendingIntent.getBroadcast(this, id, 
				AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/* Instead of setting an alarm, use cancel on the pending Intent*/
		alarmManager.cancel(DispIntent);
	}
	
	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	private String construct_parse_event(Event e)
	{
		ParseObject db_event = new ParseObject(parse_class);
		db_event.put(id, String.valueOf(e.GetID()));
		db_event.put(email, identifier);
		db_event.put(name, String.valueOf(e.getName()));
		db_event.put(desc, String.valueOf(e.getDescription()));
		db_event.put(alarm, String.valueOf(e.getAlarm()));
		db_event.put(month, String.valueOf(e.GetMonth()));
		db_event.put(day, String.valueOf(e.GetDay()));
		db_event.put(year, String.valueOf(e.GetYear()));
		db_event.put(start, String.valueOf(e.GetStart()));
		db_event.put(end, String.valueOf(e.GetEnd()));
		db_event.put(color, String.valueOf(e.getColor()));
		db_event.put(rep, String.valueOf(e.getRep()));
		db_event.put(asec, String.valueOf(e.get_Asec()));		
		db_event.saveEventually();
		return db_event.getObjectId();
	}
	
}
