package com.example.scheduler;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Toast;

class Gesture_Module extends SimpleOnGestureListener {
	
	private final Context App_Context;
//	private final com.actionbarsherlock.view.ActionMode Action; 
	
	public Gesture_Module (Context c)
	{
		App_Context = c;
//		Action = act;
	}
	
	
//	AdapterView.OnItemClickListener Listen = new AdapterView.OnItemLongClickListener() {
//        public boolean onItemLongClick(AdapterView<?> adv, View v, int pos, long id) 
//        {
//        	selected_view = v;
//        	
//            if(m_Action != null)
//            {
//            	return false;
//            }
//            
//            m_Action = Schedule.this.startActionMode(m_ActionCall);
//            selected_event = (Event) e_adapter.getItem(pos);
//            adv.setSelection(pos);
//            e_adapter.notifyDataSetChanged();
//            return true;                
//        }
//		});
	
	  @Override
	  public boolean onSingleTapConfirmed(MotionEvent event) {
	    // Trigger the touch event on the calendar
		  Toast.makeText(App_Context, "Single Tap", Toast.LENGTH_SHORT).show();
	    return super.onSingleTapUp(event);
	  }

	  @Override
	  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	    ViewConfiguration viewConfiguration = ViewConfiguration.get(App_Context);
	    int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
	    int minSwipeVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
	    int maxSwipeOffPath = viewConfiguration.getScaledTouchSlop();

	    if (Math.abs(e1.getY() - e2.getY()) > maxSwipeOffPath) {
	      return false;
	    }

	    if (Math.abs(velocityX) > minSwipeVelocity) {
	      // Right to left swipe
	      if (e1.getX() - e2.getX() > minSwipeDistance) {
          	Toast.makeText(App_Context, "Swipe Right", Toast.LENGTH_SHORT).show();
	      }
	      // Left to right
	      else if (e2.getX() - e1.getX() > minSwipeDistance) {
          	Toast.makeText(App_Context, "Swipe Left", Toast.LENGTH_SHORT).show();
	      }
	    }

	    return false;
	  }
	} 