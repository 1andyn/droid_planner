package com.example.scheduler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Alarm_Module extends Service{
	
	private NotificationManager mManager;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	    public void onCreate() 
	    {
	       // TODO Auto-generated method stub  
	       super.onCreate();
	    }
	 
	   @SuppressWarnings("static-access")
	   @Override
	   public int onStartCommand(Intent intent,int flags, int startId)
	   {
	       super.onStartCommand(intent, flags, startId);
	      
	       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
	       Intent intent1 = new Intent(this.getApplicationContext(),Schedule.class);
	     
	       Notification notification = new Notification(R.drawable.ic_action_event,"This is a test message!", System.currentTimeMillis());
	       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
	       notification.flags |= Notification.FLAG_AUTO_CANCEL;
	       notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);
	 
	       mManager.notify(0, notification);
	       return 0;
	    }
	 
	    @Override
	    public void onDestroy() 
	    {
	        // TODO Auto-generated method stub
	        super.onDestroy();
	    }

}
