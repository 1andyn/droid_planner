package com.example.scheduler;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

class Gesture_Module implements GestureDetector.OnGestureListener{
	
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	protected MotionEvent mLastOnDownEvent = null;
	private Context app_context;
	
	public Gesture_Module (Context context)
	{
		app_context = context;
	}
	
	@Override   
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
	{
        if (e1==null)
            e1 = mLastOnDownEvent;
        if (e1==null || e2==null)
            return false;

        float dX = e2.getX()-e1.getX();
        float dY = e2.getY()-e1.getY();

        if (Math.abs(dY)<SWIPE_MAX_OFF_PATH && Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY && Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {
            if (dX>0) {
                Toast.makeText(app_context, "Right Swipe", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(app_context, "Left Swipe", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    	Toast.makeText(app_context, "Long Press Detected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { 
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

   @Override
   public boolean onSingleTapUp(MotionEvent e) {
      // TODO Auto-generated method stub
       return false;
   }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }
}