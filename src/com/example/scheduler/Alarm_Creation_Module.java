package com.example.scheduler;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm_Creation_Module extends Activity {
	private final int EMPTY = -1;
	private Context app_context;
	private int id = 1;
	private String rep_string;
	private Event ev;
	
	public Alarm_Creation_Module(Context c, Event e)
	{
		app_context = c;
		ev = e;
	}
	
	public void setId(int giveId)
	{
		id = giveId;
	}
	
	public int create_Alarm()
	{
		/* Instantiate a Calendar */ 
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.SECOND, 5);
		
	    Intent AlarmIntent = new Intent(app_context, Schedule.class);
	    PendingIntent DispIntent = PendingIntent.getBroadcast(app_context, id, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    /* Scheduling the Alarm to be triggered*/
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), DispIntent);
	    
	    return id;
	}
	
	public void cancel_Alarm()
	{
		/* Recreate the alarm creation data */
		Intent AlarmIntent = new Intent(app_context, Schedule.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent DispIntent = PendingIntent.getBroadcast(app_context, id, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/* Instead of setting an alarm, use cancel on the pending Intent*/
		alarmManager.cancel(DispIntent);
		
		id = -EMPTY;
	}
	
}
