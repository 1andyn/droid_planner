package com.example.scheduler;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.Toast;

class Gesture_Module extends SimpleOnGestureListener{
	
    private int REL_SWIPE_MIN_DISTANCE; 
    private int REL_SWIPE_MAX_OFF_PATH;
    private int REL_SWIPE_THRESHOLD_VELOCITY;
    private ListView listv;
	private Context app_context;
	
	public Gesture_Module(Context c, ListView l, int min_dist, int max_off, int thresh)
	{
		app_context = c;
		listv = l;
		REL_SWIPE_MIN_DISTANCE = min_dist;
		REL_SWIPE_MAX_OFF_PATH = max_off;
		REL_SWIPE_THRESHOLD_VELOCITY = thresh;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
	    //int pos = listv.pointToPosition((int)e.getX(), (int)e.getY());
	    System.out.println("Single Tap");
	    return false;
	}

	@Override 
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
		try {
	        if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH) 
	            return false; 
	        if(e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE && 
	            Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
	        	Toast.makeText(app_context, "Left Swipe", Toast.LENGTH_SHORT).show();
	        	System.out.println("SWIPE LEFT");
	        }  else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE && 
	            Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) { 
	        	Toast.makeText(app_context, "Right Swipe", Toast.LENGTH_SHORT).show();
	        	System.out.println("SWIPE RIGHT");
	        } 
	    } catch (Exception e) {
	    	System.out.println("Exception Detected");
	    }
	    return false; 
	} 

}
