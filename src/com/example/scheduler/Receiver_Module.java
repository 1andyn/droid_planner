package com.example.scheduler;

/* Library Imports */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

public class Receiver_Module extends BroadcastReceiver{
	private final static String EV_NAME = "event_name";
	private final static String EV_DESC = "event_desc";
	private final static String EV_COLR = "event_colr";
	
	@Override
    public void onReceive(Context context, Intent intent)
    {   
       Intent AlarmService = new Intent(context, Alarm_Module.class);
       Bundle extras = intent.getExtras();
	   if(extras != null){
		    AlarmService.putExtra(EV_NAME, extras.getString(EV_NAME));
		    AlarmService.putExtra(EV_DESC, extras.getString(EV_DESC));
		    AlarmService.putExtra(EV_COLR, extras.getInt(EV_COLR));
	   } else {
		   System.out.println("Bundle has been detected as empty");
	   }
       context.startService(AlarmService);
       
    }   
}
