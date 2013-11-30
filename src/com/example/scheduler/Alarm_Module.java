package com.example.scheduler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

public class Alarm_Module extends Service{
	
	/* Bundle or Extra Keys */
	private final static String EV_NAME = "event_name";
	private final static String EV_DESC = "event_desc";
	private final static String EV_COLR = "event_colr";
	
	private Bitmap icon;
	private Uri sound;
	private int ledinterval = 1000; // Milliseconds
	private String ev_name = Default_Name; // Event name
	private String desc_name = Default_Desc; // Desc name
	private final static String note_tick = "Planner Plus Notification!";
	private final static String Default_Name = "DEFAULT EVENT NAME";
	private final static String Default_Desc = "DEFAULT DESCRIPTION TEXT";
	private int ledcolor = 0xff0000ff; // LED Color for Notification (will become Dynamic
	private final long[] pattern = {100, 100, 100, 100, 100, 100}; // Vibrate Pattern
	
     private NotificationManager mManager;

     @Override
     public IBinder onBind(Intent arg0)
     {
        return null;
     }

    @Override
    public void onCreate() 
    {
    	initialization();
    }
   
    /* Function for initializing Variables */
   private void initialization()
   {
	   icon = BitmapFactory.decodeResource(this.getApplicationContext().getResources(),
               R.drawable.notelarge);
	   sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
   }
    
   @SuppressWarnings("static-access")
@Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
       super.onStartCommand(intent, flags, startId);
      
       Bundle extras = intent.getExtras();
	   if(extras != null){
	       ev_name = extras.getString(EV_NAME);
		   desc_name = extras.getString(EV_DESC);
		   ledcolor = extras.getInt(EV_COLR);
	   } else {
		   System.out.println("Bundle has been detected as empty");
	   }
	   Intent OpenIntent = new Intent(this, Schedule.class);
	   OpenIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   /* Pending Intent is for intent that is triggered upon notification click */
	   PendingIntent pendingNoteIntent = PendingIntent.getActivity(this, 0, OpenIntent, 0);

	   /* Notification Creation Code*/
	   Notification notification = new Notification.Builder(getApplicationContext())
       .setContentTitle(ev_name) // TITLE OF NOTIFICATION
       .setContentText(desc_name) // BODY TEXT OF NOTIFICATION
       .setTicker(note_tick) // MESSAGE TO USER during NOTIFICATION (SMALL TEXT)
       .setSmallIcon(R.drawable.ic_event_small) // SMALLER NOTIFICATION ICON
       .setLargeIcon(icon) // LARGER NOTIFICATION ICON
       .setSound(sound) // SET SOUND
       .setVibrate(pattern) // SET VIBRATE PATTERN
       .setLights(ledcolor, ledinterval, ledinterval) // SET LED SETTINGS, LEDCOLOR, DURATION LED ON, DURATION OFF
       .setContentIntent(pendingNoteIntent) // SET INTENT FOR RETURNING TO APPLICATION WHEN NOTIFICATION IS PRESSED
       .build();
       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.NOTIFICATION_SERVICE);

	   // Code that Hides Notification after it is selected
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
	   
       mManager.notify(0, notification);
       return super.onStartCommand(intent, flags, startId);
    }
   
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
    }
    
}
