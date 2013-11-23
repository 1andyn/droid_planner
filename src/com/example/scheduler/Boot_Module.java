package com.example.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Boot_Module extends BroadcastReceiver {

	/* Intent Data Extra Tags */
	private final static String EV_NAME = "event_name";
	private final static String EV_DESC = "event_desc";
	private final static String EV_COLR = "event_colr";
	
	/* Static Data Values */
	private final static int DAY_OFFSET = 1;
	private final static long WEEK_INTERVAL = 7*24*60*60*1000;
	private final static String BUGFIX = "1337";
	private final static String NO_REP = "NNNNNNN";
	private final static String CHECKED = "Y";
	
	private int NONE = 0;
	
	/* Extraction Static values */
	private final int MIN_TIME_DIGITS = 3;	
	private final static int MIN_DIGITS = 2;
	private final static int ZERO = 0;
	private final static int SECOND = 1;
	private final static int THIRD = 2;
	
	/* Data values used for data interaction */
	private SQL_DataSource datasource;
	
    @Override
    public void onReceive(Context context, Intent intent) {
		
    	/* SQL Configuration */
		datasource = new SQL_DataSource(context);
		datasource.open();
		
		ArrayList<Event> AllEvents = datasource.getAllEvents();
		ArrayList<Event> AlarmEvents  = new ArrayList<Event>();
		
		for(int x = 0; x < AllEvents.size(); x++){
			if(AllEvents.get(x).getAlarm().equals(CHECKED)){
				AlarmEvents.add(AllEvents.get(x));
			}
		}
		AllEvents = null; // Delete AllEvents
		
		for(int x = 0; x < AlarmEvents.size(); x++){
			construct_Alarm(AlarmEvents.get(x), context);
		}
		
		AlarmEvents = null; // Delete AlarmEvents
		datasource.close();
    }
    
	private void construct_Alarm(Event e,Context C)
	{
		int newid = safeLongToInt(e.GetID());
		
		/* Create Alarm (Seed) */ 
		create_Alarm(e, newid, C);
		
		/* Create Repeated Notifications */
		if(!e.getRep().equals(NO_REP)){
			create_repAlarm(e, newid, C);	
		}
		
	}
	
	@SuppressWarnings("static-access")
	private void create_Alarm(Event e, int id, Context C)
	{		
	    Intent AlarmIntent = new Intent().setClass(C, Receiver_Module.class);
	    AlarmIntent.setData(Uri.parse("custom://" + id));
	    AlarmIntent.setAction(String.valueOf(id));

	    AlarmIntent.putExtra(EV_NAME, e.getName());
	    AlarmIntent.putExtra(EV_DESC, e.getDescription());
	    AlarmIntent.putExtra(EV_COLR, e.getColor());
	    
	    PendingIntent DispIntent = PendingIntent.getBroadcast(C, id, 
	    		AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    /* Scheduling the Alarm to be triggered*/
	    AlarmManager alarmManager = (AlarmManager)C.getSystemService(C.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, e.get_Asec(), DispIntent);
	}
	
	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}

	 @SuppressWarnings("static-access")
	private void create_repAlarm(Event e, int id, Context C)
	{		
		Repetition_Module repmod = new Repetition_Module();
		repmod.set_RepString(e.getRep());
		List<Integer> temp = repmod.get_RepArray();
		
		/* Calendar Limitation, Days_of_Week starts at 1 rather than 0 */
		for(int x = 0; x < temp.size(); x++){
		
		String S = "" + id + BUGFIX + id + temp.get(x);	
		int newid = Integer.parseInt(S);
		
		/* Instantiate Calendar */	
		Calendar Cal = Calendar.getInstance();
		
		System.out.println("Event: " + newid + " Count: " + x);
		System.out.println("DOW: " + (temp.get(x) + DAY_OFFSET));
		System.out.println("HROD: " + extract_HOUR(e.GetStart()));
		System.out.println("MNOD: " + extract_MINUTES(e.GetStart()));
		
	    Cal.set(Calendar.DAY_OF_WEEK, (temp.get(x) + DAY_OFFSET));
	    Cal.set(Calendar.HOUR_OF_DAY, extract_HOUR(e.GetStart()));
	    Cal.set(Calendar.MINUTE, extract_MINUTES(e.GetStart()));
	    Cal.set(Calendar.SECOND, NONE);
	    Cal.set(Calendar.MILLISECOND, NONE);
			
	    Intent AlarmIntent = new Intent().setClass(C, Receiver_Module.class);
	    AlarmIntent.setData(Uri.parse("rep://" + newid));
	    AlarmIntent.setAction(String.valueOf(newid));

	    AlarmIntent.putExtra(EV_NAME, e.getName());
	    AlarmIntent.putExtra(EV_DESC, e.getDescription());
	    AlarmIntent.putExtra(EV_COLR, e.getColor());
	    //AlarmIntent.putExtra(EV_REPS, CHECKED); Not sure if needed through this style of implementation
	    
	    PendingIntent DispIntent = PendingIntent.getBroadcast(C, newid, 
	    		AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    /* Scheduling the Alarm to be triggered*/
		AlarmManager alarmManager = (AlarmManager)C.getSystemService(C.ALARM_SERVICE);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Cal.getTimeInMillis(), WEEK_INTERVAL, DispIntent);
	    
	    Cal = null; //Delete Calendar
		}
		
		repmod = null; //Delete repmod
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
		if(my_time.length() <= MIN_DIGITS){
			return ZERO;
		} else if(my_time.length() == MIN_TIME_DIGITS){
			String nu_time = my_time.substring(ZERO, SECOND);
			return Integer.parseInt(nu_time);
		} else {
			String nu_time = my_time.substring(ZERO, THIRD);
			return Integer.parseInt(nu_time);
		}	
	}
    
}