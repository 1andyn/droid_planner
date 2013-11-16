package com.example.scheduler;

/* Library Imports */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver_Module extends BroadcastReceiver{
	@Override
    public void onReceive(Context context, Intent intent)
    {
       Intent AlarmService = new Intent(context, Alarm_Module.class);
       context.startService(AlarmService);
    }   
}
